package com.hongte.alms.base.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.WithholdingFlowRecordMapper;
import com.hongte.alms.base.service.WithholdingFlowRecordService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.SecurityUtil;
import com.ht.ussp.core.Result;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 代扣平台代扣流水 服务实现类
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-16
 */
@Service("WithholdingFlowRecordService")
@Slf4j
public class WithholdingFlowRecordServiceImpl extends
        BaseServiceImpl<WithholdingFlowRecordMapper, WithholdingFlowRecord> implements WithholdingFlowRecordService {
    @Autowired
    private EipRemote eipRemote;
    @Autowired
    private WithholdingFlowRecordMapper withholdingFlowRecordMapper;
    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService;

    @Transactional
    @Override
    public void importWidthholdingFlowFromBaoFu(String settleDate) {
        // 如果已经导入了当天数据则不再重复导入
        WithholdingFlowRecord oldWithholdingFlowRecord = new WithholdingFlowRecord();
        try {
            int count = oldWithholdingFlowRecord.selectCount(new EntityWrapper<WithholdingFlowRecord>()
                    .eq("liquidation_date", new SimpleDateFormat("yyyy-MM-dd").parse(settleDate))
                    .eq("withholding_platform", (Integer) PlatformEnum.BF_FORM.getValue())

            );
            if (count > 0) {
                throw new RuntimeException(
                        String.format("@WithholdingFlowRecordServiceImpl@导入宝付代扣流水失败 宝付指定结清日期[%s]的[%s]条数据已经导入过，请勿重复导入",
                                settleDate, count));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OutputStream os = null;
        InputStream is = null;
        ZipInputStream zipIs = null;
        ZipFile zipFile = null;
        File targetFile = null;
        try {
            // 1.通过eip请求宝付接口拿数据
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("fileType", "fi");
            paramMap.put("settleDate", settleDate);
            Result rslt = eipRemote.baoFuLoadFile(paramMap);
            if (!Constant.REMOTE_EIP_SUCCESS_CODE.equals(rslt.getReturnCode())) {
                log.error("@WithholdingFlowRecordServiceImpl@导入宝付代扣流水失败 调用eip下载宝付流水文件失败.msg:[{}]", rslt.getMsg());
                throw new RuntimeException("调用eip下载宝付流水文件失败");
            }
            // 进行base64解码，解密后为byte类型
            byte[] restr = SecurityUtil.Base64Decode(rslt.getData().toString());
            // 把获取的zip文件的byte放入输入流
            is = new ByteArrayInputStream(restr);
            String filePath = System.getProperty("java.io.tmpdir") + File.separator + "baofu" + File.separator
                    + settleDate + ".zip"; // 存在本地的路径（自行设置）
            targetFile = new File(filePath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 创建文件
            targetFile.createNewFile();
            os = new FileOutputStream(targetFile);
            byte[] buf = new byte[1024];
            while (is.available() > 0) {
                // 读取接收的文件流
                is.read(buf);
                // 写入文件
                os.write(buf);
            }
            is.close();
            os.flush();
            os.close();
            zipIs = new ZipInputStream(new FileInputStream(filePath));
            zipFile = new ZipFile(filePath);
            ZipEntry zipEnt = null;
            while ((zipEnt = zipIs.getNextEntry()) != null) {
                if (!zipEnt.isDirectory()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipEnt)));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("商户号")) {
                            continue;
                        }
                        // 开始处理文件内容
                        String[] cols = line.split("\\|");
                        if (cols.length <= 8) {
                            // 跳过不需要的数据
                            continue;
                        }
                        if (cols.length > 0) {
                            WithholdingFlowRecord flow = new WithholdingFlowRecord();
                            flow.setWithholdingPlatform((Integer) PlatformEnum.BF_FORM.getValue());
                            flow.setMerchantNo(cols[0]);
                            flow.setTerminalNo(cols[1]);
                            // flow.setTradeType(Constant.TRADE_TYPE_MAP_BAOFU.get(cols[2]) +
                            // Constant.TRADE_SUB_TYPE_MAP_BAOFU.get(cols[3]));
                            flow.setTradeType(Constant.TRADE_TYPE_MAP_KUAIQIAN.get(cols[2]));
                            flow.setWithholdingStatus(Constant.TRADE_SUB_TYPE_MAP_BAOFU.get(cols[3]));
                            flow.setTradeOrderNo(cols[4]);
                            flow.setMerchantOrderNo(cols[5]);
                            try {
                                flow.setLiquidationDate(new SimpleDateFormat("yyyy-MM-dd").parse(cols[6]));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            flow.setAmount(new BigDecimal(cols[8]));
                            flow.setServiceCharge(new BigDecimal(cols[9]));
                            flow.setTradeWaterNo(cols[10]);
                            flow.setImportTime(new Date());
                            flow.setImportSystem(this.getClass().getSimpleName());
                            flow.insert();
                        }
                    }
                    br.close();
                }
                zipIs.closeEntry();
            }

        } catch (IOException ex) {
            log.error("@WithholdingFlowRecordServiceImpl@导入宝付代扣流水失败 解析宝付流水zip文件失败.msg:[{}]", ex.getMessage(), ex);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
                if (zipIs != null)
                    zipIs.close();
                if (zipFile != null)
                    zipFile.close();
                if (targetFile != null && targetFile.exists()) {
                    targetFile.delete();
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    @Transactional
    @Override
    public void importWidthholdingFlowFromYiBao(String settleDate) {
        // 如果已经导入了当天数据则不再重复导入
        WithholdingFlowRecord oldWithholdingFlowRecord = new WithholdingFlowRecord();
        try {
            int count = oldWithholdingFlowRecord.selectCount(new EntityWrapper<WithholdingFlowRecord>()
                    .eq("liquidation_date", new SimpleDateFormat("yyyy-MM-dd").parse(settleDate))
                    .eq("withholding_platform", (Integer) PlatformEnum.YB_FORM.getValue())

            );
            if (count > 0) {
                throw new RuntimeException(
                        String.format("@WithholdingFlowRecordServiceImpl@导入易宝代扣流水失败 易宝指定结清日期[%s]的[%s]条数据已经导入过，请勿重复导入",
                                settleDate, count));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            // 1.通过eip请求易宝接口拿数据
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("startdate", settleDate);
            paramMap.put("enddate", settleDate);
            Result rslt = eipRemote.yiBaoPayClearData(paramMap);
            if (!Constant.REMOTE_EIP_SUCCESS_CODE.equals(rslt.getReturnCode())) {
                log.error("@WithholdingFlowRecordServiceImpl@导入易宝代扣流水失败 调用eip下载易宝流水文件失败.msg:[{}]", rslt.getMsg());
                throw new RuntimeException("调用eip下载易宝流水文件失败");
            }
            // 进行base64解码，解密后为byte类型
            String retstr = rslt.getData().toString();
            String[] rows = retstr.split("\\r\\n|\\n");
            /*
             * Pattern pattern = Pattern.compile("\\r\\n|\\n"); Matcher matcher =
             * pattern.matcher(retstr); while (matcher.find()){
             * System.out.println(matcher.group(0)); }
             */
            if (rows.length == 0)
                return;
            for (String row : rows) {
                if (StringUtils.isBlank(row)) {
                    // 处理到空行截止
                    break;
                }
                if (row.contains("商户账户编号")) {
                    continue;
                }
                // 开始处理文件内容
                String[] cols = row.split(",");
                if (cols.length > 0) {
                    WithholdingFlowRecord flow = new WithholdingFlowRecord();
                    flow.setWithholdingPlatform((Integer) PlatformEnum.YB_FORM.getValue());
                    flow.setMerchantNo(cols[0]);
                    try {
                        flow.setLiquidationDate(new SimpleDateFormat("yyyy-MM-dd").parse(cols[1]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        flow.setTradeDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(cols[3]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    flow.setMerchantOrderNo(cols[4]);
                    flow.setTradeWaterNo(cols[5]);
                    flow.setAmount(new BigDecimal(cols[7]));
                    flow.setTradeType(cols[8]);
                    // 接口没有状态，默认为成功
                    flow.setWithholdingStatus("成功");
                    flow.setServiceCharge(new BigDecimal(cols[9]));
                    flow.setProductName(cols[12]);
                    flow.setPaymentCardType(cols[13]);
                    flow.setPaymentCardNo(cols[14]);
                    flow.setImportTime(new Date());
                    flow.setImportSystem(this.getClass().getSimpleName());
                    flow.insert();
                }
            }
        } catch (Exception ex) {
            log.error("@WithholdingFlowRecordServiceImpl@导入易宝代扣流水失败.msg:[{}]", ex.getMessage(), ex);
        }
    }

    @Override
    public WithholdingFlowRecordSummaryVo querySummary(WithholdFlowReq withholdFlowReq) {
        return withholdingFlowRecordMapper.querySummary(withholdFlowReq);
    }

    @Override
    public void importWidthholdingFlowFromKuaiQian(String settleDate) {
        // 如果已经导入了当天数据则不再重复导入
        WithholdingFlowRecord oldWithholdingFlowRecord = new WithholdingFlowRecord();
        try {
            int count = oldWithholdingFlowRecord.selectCount(new EntityWrapper<WithholdingFlowRecord>()
                    .eq("liquidation_date", new SimpleDateFormat("yyyy-MM-dd").parse(settleDate))
                    .eq("withholding_platform", (Integer) PlatformEnum.KQ_FORM.getValue())
            );
            if (count > 0) {
                throw new RuntimeException(
                        String.format("@WithholdingFlowRecordServiceImpl@导入快钱代扣流水失败 快钱指定结清日期[%s]的[%s]条数据已经导入过，请勿重复导入",
                                settleDate, count));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            // 0.获取快钱当天的所有交易流水号
            String startDate = settleDate + " 00:00:00.0";
            String endDate = settleDate + " 23:59:59.999";
            List<WithholdingRepaymentLog> repaymentLogs = withholdingRepaymentLogService
                    .findByLiquidationDate(PlatformEnum.KQ_FORM, startDate, endDate);
            if (repaymentLogs == null || repaymentLogs.size() == 0) {
                log.error("@WithholdingFlowRecordServiceImpl@代扣日志表中无快钱当天的交易记录.startDate:[{}],endDate:[{}]", startDate,
                        endDate);
                return;
            }
            for (WithholdingRepaymentLog repaymentLog : repaymentLogs) {
                if(StringUtils.isBlank(repaymentLog.getMerchOrderId())){
                    //如果代扣表的商户订单号为空则跳过
                    continue;
                }
                // 1.通过eip请求快钱接口拿数据
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("txnType", "PUR");
                paramMap.put("externalRefNumber", repaymentLog.getMerchOrderId());
                Result rslt = eipRemote.kuaiQianQueryPayAndRefund(paramMap);
                if (!Constant.REMOTE_EIP_SUCCESS_CODE.equals(rslt.getReturnCode())) {
                    log.error("@WithholdingFlowRecordServiceImpl@导入快钱代扣流水失败 调用eip下载快钱流水文件失败.msg:[{}]", rslt.getMsg());
                    throw new RuntimeException("调用eip下载快钱流水文件失败");
                }
                Map<String,String> retMap = (Map<String,String>)rslt.getData();
                if (retMap != null && !retMap.isEmpty()) {
                    WithholdingFlowRecord flow = new WithholdingFlowRecord();
                    flow.setWithholdingPlatform((Integer) PlatformEnum.KQ_FORM.getValue());
                    flow.setMerchantNo(retMap.get("merchantId"));
                    //transTime: 快钱MAS系统处理本笔交易请求时的系统时间
                    String transTime = retMap.get("transTime");
                    String formatedTransTime = transTime.substring(0, 4) + "-"+
                                               transTime.substring(4, 6)+"-"+
                                               transTime.substring(6, 8);

                    flow.setLiquidationDate( new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).parse(formatedTransTime));
                    //entryTime: 商户发起交易请求时的系统时间
                    String entryTime = retMap.get("entryTime");
                    String formattedEntryTime = entryTime.substring(0, 4) + "-" +
                                                entryTime.substring(4, 6) + "-"+
                                                entryTime.substring(6, 8)+ " "+
                                                entryTime.substring(8, 10)+ ":"+
                                                entryTime.substring(10, 12)+ ":"+
                                                entryTime.substring(12, 14);
                    flow.setTradeDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.SIMPLIFIED_CHINESE).parse(formattedEntryTime));
                    flow.setMerchantOrderNo(retMap.get("externalRefNumber"));
                    // flow.setTradeWaterNo(cols[5]);
                    flow.setAmount(new BigDecimal(retMap.get("amount")));
                    //flow.setTradeType(retMap.get("txnType"));
                    //PUR:消费交易, RFD:退货交易
                    flow.setTradeType("消费交易");
                    // 接口没有状态，默认为成功
                    String txnStatus = retMap.get("txnStatus");
                    if (Constant.TRADE_TYPE_MAP_KUAIQIAN.containsKey(txnStatus)) {
                        flow.setWithholdingStatus(Constant.TRADE_TYPE_MAP_KUAIQIAN.get(txnStatus));
                    }
                    flow.setTerminalNo(retMap.get("terminalId"));
                    
                     /* flow.setServiceCharge(new BigDecimal(cols[9]));
                      flow.setProductName(cols[12]); flow.setPaymentCardType(cols[13]);
                      flow.setPaymentCardNo(cols[14]);*/
                     
                    flow.setImportTime(new Date());
                    flow.setImportSystem(this.getClass().getSimpleName());
                    flow.insert();
                }
            }
        } catch (Exception ex) {
            log.error("@WithholdingFlowRecordServiceImpl@导入快钱代扣流水失败.msg:[{}]", ex.getMessage(), ex);
        }
    }

	@Override
	public Page<WithholdingFlowRecordVo> selectFlowBfRecordPage(WithholdFlowReq req) {
	        Page<WithholdingFlowRecordVo> pages = new Page<>();
	        pages.setCurrent(req.getPage());
	        pages.setSize(req.getLimit());
	        List<WithholdingFlowRecordVo> list = withholdingFlowRecordMapper.selectFlowBfRecordPage(pages,req);
	        pages.setRecords(list);
		return pages;
	}
}