package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowOptReq;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.common.util.AliyunHelper;
import com.hongte.alms.common.util.OssResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.service.CustomerRepayFlowService;
import com.hongte.alms.finance.service.ShareProfitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service("customerRepayFlowServiceImpl")
@RefreshScope
public class CustomerRepayFlowServiceImpl implements CustomerRepayFlowService {

    private Logger logger = LoggerFactory.getLogger(CustomerRepayFlowServiceImpl.class);


    private final static String STATE_NOPASS = "未审核";
    private final static String STATE_PASS = "已审核";
    private final static String STATE_PASS_NUMBER = "2";

    private final static String STATE_REJECT = "拒绝";
    private final static String STATE_REJECT_NUMBER = "3";

    @Value("${ht.excel.file.save.path}")
    private String excelSavePath;

    @Autowired
    private AliyunHelper ossClient;

    @Autowired
    private MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;

    @Autowired
    private ShareProfitService shareProfitService;

    @Autowired
    private RepaymentBizPlanListMapper repaymentBizPlanListMapper;

    @Override
    public String customerFlowExcelWorkBook(Workbook workbook) throws Exception {
        String prefix = UUID.randomUUID().toString();
        String suffix = ".xls";
        OutputStream os = null;
        FileInputStream fis = null;
        String ossUrl = "";
        String msg = null;
        String excelName = "";
        File tempFile = null;
        try {
            String path = excelSavePath;
            // 2、保存到临时文件
            tempFile = File.createTempFile(prefix, suffix);
            excelName = tempFile.getName();
            logger.info("临时文件所在的本地路径：" + tempFile.getCanonicalPath());

            os = new FileOutputStream(tempFile);

            workbook.write(os);
            byte[] buffer = null;
            os.close();
            fis = new FileInputStream(tempFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            buffer = bos.toByteArray();

            String docUrl = "upload/" + "/" + excelName;
            //step1 先写入OSS
            OssResult ossResult = ossClient.putObject(docUrl, buffer);
            if (ossResult != null && ossResult.isSuccess()) {
                //step2 返回当前OSS文档信息
                ossUrl = docUrl;
            } else {
                msg = "上传OSS不成功";
            }


        } catch (IOException e) {
            logger.error(e.getMessage());
            msg = e.getMessage();
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg = e.getMessage();

        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (tempFile != null) {
                    tempFile.deleteOnExit();
                }
                if (msg != null) {
                    throw new ServiceRuntimeException("500", msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ossUrl;
    }

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
    public void importCustomerFlowExcel(MultipartFile file) throws Exception {

        String fileName = file.getName();
        InputStream fis = null;
        try {
            fis = file.getInputStream();
            byte[] buffer = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            buffer = bos.toByteArray();
//            OssResult ossResult = ossClient.putObject(fileName, buffer);
//            if (ossResult != null && ossResult.isSuccess()) {
//                //step2 返回当前OSS文档信息
//                logger.info("上传oss成功 fileName=" + fileName);
//            } else {
//                logger.info("上传oss失败 fileName=" + fileName);
//            }

        } catch (Exception e) {
            logger.info("上传oss过程失败 {}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ImportParams importParams = new ImportParams();
        List<CustomerRepayFlowExel> customerList = ExcelImportUtil.importExcel(file.getInputStream(), CustomerRepayFlowExel.class, importParams);

        List<MoneyPoolRepayment> updateList = new ArrayList<>();//excel导入的数据
        List<MoneyPoolRepayment> auditList = new ArrayList<>(); //excel导入审核通过数据
        List<MoneyPoolRepayment> repayList = new ArrayList<>(); //excel导入审核通过需要匹配还款数据

        List<Integer> ids = new ArrayList<>();

        if (!CollectionUtils.isEmpty(customerList)) {
            for (CustomerRepayFlowExel customerRepayFlowExel : customerList) {
                MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment();

                moneyPoolRepayment.setId(Integer.parseInt(customerRepayFlowExel.getId()));
                //未审核_未关联银行流水", "已审核_财务确认已还款", "拒绝_还款登记被财务拒绝

                moneyPoolRepayment.setState(customerRepayFlowExel.getState());
                moneyPoolRepayment.setAccountMoney(customerRepayFlowExel.getAccountMoney() == null ? null : new BigDecimal(customerRepayFlowExel.getAccountMoney()));
                moneyPoolRepayment.setBankAccount(customerRepayFlowExel.getBankAccount());
                moneyPoolRepayment.setFactTransferName(customerRepayFlowExel.getFactTransferName());
                moneyPoolRepayment.setUpdateTime(new Date());

                moneyPoolRepayment.setOriginalBusinessId(customerRepayFlowExel.getOriginalBusinessId());
                moneyPoolRepayment.setAfterId(customerRepayFlowExel.getAfterId());
                moneyPoolRepayment.setMoneyPoolId(customerRepayFlowExel.getMoneyPoolId());

                if (RepayRegisterFinanceStatus.财务确认已还款.toString().equals(customerRepayFlowExel.getState())) {
                    auditList.add(moneyPoolRepayment);
                    ids.add(moneyPoolRepayment.getId());
                }
                updateList.add(moneyPoolRepayment);
            }
        }
        //查询以前的状态 做比对
        if (!CollectionUtils.isEmpty(auditList)) {
            List<MoneyPoolRepayment> moneyPoolRepaymentsList = moneyPoolRepaymentMapper.selectBatchIds(ids);
            if (!CollectionUtils.isEmpty(moneyPoolRepaymentsList)) {
                for (MoneyPoolRepayment audit : auditList) { // 导入的审核通过的数据
                    boolean flag = true;
                    for (MoneyPoolRepayment old : moneyPoolRepaymentsList) { //查询的老的数据
                        if (audit.getId() == old.getId() && audit.getState().equals(old.getState())) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        if (StringUtil.isEmpty(audit.getMoneyPoolId())) {
                            throw new ServiceRuntimeException("500", "流水ID不能为空");
                        }
                        repayList.add(audit);
                    }
                }
            }

            //不为空进行自动还款
            if (!CollectionUtils.isEmpty(repayList)) {
                //自动还款逻辑
                logger.info("自动还款list==={}", repayList.toString());
                for (MoneyPoolRepayment moneyPoolRepayment : repayList) {
                    ConfirmRepaymentReq confirmRepaymentReq = new ConfirmRepaymentReq();
                    confirmRepaymentReq.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
                    confirmRepaymentReq.setAfterId(moneyPoolRepayment.getAfterId());
                    List<String> mr = new ArrayList<>(1);
                    mr.add(moneyPoolRepayment.getMoneyPoolId());
                    confirmRepaymentReq.setMprIds(mr);
                    List<Integer> repaySource = new ArrayList<>(1);
                    repaySource.add(10); //10：线下转账，20：线下代扣，30：银行代扣,11:用往期结余还款
                    confirmRepaymentReq.setCallFlage(10);
                    shareProfitService.execute(confirmRepaymentReq, true);
                }
            }
        }


        //更新信息
        moneyPoolRepaymentMapper.batchUpdateMoneyPool(updateList);


    }

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
    public void auditOrRejectCustomerFlow(CustomerRepayFlowOptReq customerRepayFlowOptReq) throws Exception {
        String idsStr = customerRepayFlowOptReq.getIdsStr();
        List<Integer> ids = new ArrayList<>();
        List<MoneyPoolRepayment> updateList = new ArrayList<>();
        String stateStr = "";
        if (idsStr.endsWith(",")) {
            String[] arrStr = idsStr.substring(0, idsStr.length()).split(",");
            for (String id : arrStr) {
                ids.add(Integer.parseInt(id));

            }
            System.out.println(arrStr);
        } else {
            ids.add(Integer.parseInt(idsStr));
        }

        //查询所有的

        if (STATE_PASS_NUMBER.equals(customerRepayFlowOptReq.getOpt())) { //审核通过
            stateStr = RepayRegisterFinanceStatus.财务确认已还款.toString();

            List<MoneyPoolRepayment> moneyPoolRepaymentsList = moneyPoolRepaymentMapper.selectBatchIds(ids);
            if (!CollectionUtils.isEmpty(moneyPoolRepaymentsList)) {
                for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepaymentsList) {
                    String planListId = moneyPoolRepayment.getPlanListId();
                    RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListMapper.selectById(planListId);
                    if (repaymentBizPlanList != null) {
                        String currentStatus = repaymentBizPlanList.getCurrentStatus();
                        if ("还款中".equals(currentStatus) || "逾期".equals(currentStatus)) {
                            String planId = repaymentBizPlanList.getPlanId();
                            List<RepaymentBizPlanList> list = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planId).orderBy("period", true));
                            if (!CollectionUtils.isEmpty(list)) {
                                for (RepaymentBizPlanList repaymentBizPlan : list) {
                                    if ("还款中".equals(repaymentBizPlan.getCurrentStatus()) || "逾期".equals(repaymentBizPlan.getCurrentStatus())) {
                                        ConfirmRepaymentReq confirmRepaymentReq = new ConfirmRepaymentReq();
                                        confirmRepaymentReq.setBusinessId(repaymentBizPlan.getBusinessId());
                                        confirmRepaymentReq.setAfterId(repaymentBizPlan.getAfterId());
                                        List<String> mr = new ArrayList<>(1);
                                        mr.add(moneyPoolRepayment.getMoneyPoolId());
                                        confirmRepaymentReq.setMprIds(mr);
                                        List<Integer> repaySource = new ArrayList<>(1);
                                        repaySource.add(10); //10：线下转账，20：线下代扣，30：银行代扣,11:用往期结余还款
                                        confirmRepaymentReq.setCallFlage(10);
                                        shareProfitService.execute(confirmRepaymentReq, true);
                                    }

                                }
                            }
                        }

                    }

                }
            }
        } else if (STATE_REJECT_NUMBER.equals(customerRepayFlowOptReq.getOpt())) { //拒绝
            stateStr = RepayRegisterFinanceStatus.还款登记被财务拒绝.toString();
        }

        for (Integer id : ids) {
            MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment();
            moneyPoolRepayment.setId(id);
            moneyPoolRepayment.setState(stateStr);
            updateList.add(moneyPoolRepayment);
        }
        moneyPoolRepaymentMapper.batchUpdateMoneyPool(updateList);
    }


}
