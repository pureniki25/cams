package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.WithholdingFlowRecordMapper;
import com.hongte.alms.base.service.WithholdingFlowRecordService;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.SecurityUtil;
import com.ht.ussp.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
public class WithholdingFlowRecordServiceImpl extends BaseServiceImpl<WithholdingFlowRecordMapper, WithholdingFlowRecord> implements WithholdingFlowRecordService {
    @Autowired
    private EipRemote eipRemote;
    @Autowired
    private WithholdingFlowRecordMapper  withholdingFlowRecordMapper;

    @Transactional
    @Override
    public void importWidthholdingFlowFromBaoFu(String settleDate) {
        //如果已经导入了当天数据则不再重复导入
        WithholdingFlowRecord oldWithholdingFlowRecord = new WithholdingFlowRecord();
        try {
            int count = oldWithholdingFlowRecord.selectCount(
                    new EntityWrapper<WithholdingFlowRecord>()
                            .eq("liquidation_date", new SimpleDateFormat("yyyy-MM-dd").parse(settleDate))
                            .eq("withholding_platform", (Integer) PlatformEnum.BF_FORM.getValue())

            );
            if (count > 0) {
                throw new RuntimeException(String.format("@WithholdingFlowRecordServiceImpl@导入宝付代扣流水失败 宝付指定结清日期[%s]的[%s]条数据已经导入过，请勿重复导入", settleDate, count));
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
            //1.通过eip请求宝付接口拿数据
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("fileType", "fi");
            paramMap.put("settleDate", settleDate);
            Result rslt = eipRemote.baoFuLoadFile(paramMap);
            if (!Constant.REMOTE_EIP_SUCCESS_CODE.equals(rslt.getReturnCode())) {
                log.error("@WithholdingFlowRecordServiceImpl@导入宝付代扣流水失败 调用eip下载宝付流水文件失败.msg:[{}]", rslt.getMsg());
                throw new RuntimeException("调用eip下载宝付流水文件失败");
            }
            //进行base64解码，解密后为byte类型
            byte[] restr = SecurityUtil.Base64Decode(rslt.getData().toString());
            //把获取的zip文件的byte放入输入流
            is = new ByteArrayInputStream(restr);
            String filePath = System.getProperty("java.io.tmpdir") + File.separator + "baofu" + File.separator + settleDate + ".zip";    //存在本地的路径（自行设置）
            targetFile = new File(filePath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            //创建文件
            targetFile.createNewFile();
            os = new FileOutputStream(targetFile);
            byte[] buf = new byte[1024];
            while (is.available() > 0) {
                //读取接收的文件流
                is.read(buf);
                //写入文件
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
                        //开始处理文件内容
                        String[] cols = line.split("\\|");
                        if (cols.length <=8 ) {
                            //跳过不需要的数据
                            continue;
                        }
                        if (cols.length > 0) {
                            WithholdingFlowRecord flow = new WithholdingFlowRecord();
                            flow.setWithholdingPlatform((Integer) PlatformEnum.BF_FORM.getValue());
                            flow.setMerchantNo(cols[0]);
                            flow.setTerminalNo(cols[1]);
                            //flow.setTradeType(Constant.TRADE_TYPE_MAP_BAOFU.get(cols[2]) + Constant.TRADE_SUB_TYPE_MAP_BAOFU.get(cols[3]));
                            flow.setTradeType(Constant.TRADE_TYPE_MAP_BAOFU.get(cols[2]));
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
                if (is != null) is.close();
                if (os != null) os.close();
                if (zipIs != null) zipIs.close();
                if (zipFile != null) zipFile.close();
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
        //如果已经导入了当天数据则不再重复导入
        WithholdingFlowRecord oldWithholdingFlowRecord = new WithholdingFlowRecord();
        try {
            int count = oldWithholdingFlowRecord.selectCount(
                    new EntityWrapper<WithholdingFlowRecord>()
                            .eq("liquidation_date", new SimpleDateFormat("yyyy-MM-dd").parse(settleDate))
                            .eq("withholding_platform", (Integer) PlatformEnum.YB_FORM.getValue())

            );
            if (count > 0) {
                throw new RuntimeException(String.format("@WithholdingFlowRecordServiceImpl@导入易宝代扣流水失败 易宝指定结清日期[%s]的[%s]条数据已经导入过，请勿重复导入", settleDate, count));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //1.通过eip请求易宝接口拿数据
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("startdate", settleDate);
            paramMap.put("enddate", settleDate);
            Result rslt = eipRemote.yiBaoPayClearData(paramMap);
            if (!Constant.REMOTE_EIP_SUCCESS_CODE.equals(rslt.getReturnCode())) {
                log.error("@WithholdingFlowRecordServiceImpl@导入易宝代扣流水失败 调用eip下载易宝流水文件失败.msg:[{}]", rslt.getMsg());
                throw new RuntimeException("调用eip下载易宝流水文件失败");
            }
            //进行base64解码，解密后为byte类型
            String retstr = rslt.getData().toString();
            String[] rows = retstr.split("\\r\\n|\\n");
            /*Pattern pattern = Pattern.compile("\\r\\n|\\n");
            Matcher matcher = pattern.matcher(retstr);
            while (matcher.find()){
                System.out.println(matcher.group(0));
            }*/
            if (rows.length == 0)
                return;
            for (String row : rows) {
                if (StringUtils.isBlank(row)) {
                    //处理到空行截止
                    break;
                }
                if (row.contains("商户账户编号")) {
                    continue;
                }
                //开始处理文件内容
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
                    //接口没有状态，默认为成功
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
		
	}
}