package com.hongte.alms.scheduled.runJob;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.feignClient.AlmsOpenServiceFeignClient;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 向信贷系统推送还款计划的变更失败记录重试
 * Created by 张贵宏 on 2018/7/11 16:46
 */
@JobHandler("FinanceUpdateRepayPlanToLMSJob")
@Component
public class FinanceUpdateRepayPlanToLMSJob extends IJobHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanceUpdateRepayPlanToLMSJob.class);

    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;

    @Autowired
    private AlmsOpenServiceFeignClient almsOpenServiceFeignClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            LOGGER.info("@FinanceUpdateRepayPlanToLMSJob@向信贷系统推送还款计划的变更失败记录重试--开始");
            long start = System.currentTimeMillis();

            // 根据ref_id分组，查找调用失败，且次数小于5次的，且次数最大的一条数据
            //财务确认还款
            List<SysApiCallFailureRecord> records = sysApiCallFailureRecordService.queryCallFailedDataByApiCode(
                    Constant.INTERFACE_CODE_FINANCE_FINANCE_PREVIEWCONFIRMREPAYMENT,
                    AlmsServiceNameEnums.FINANCE.getName());
            //你我金融
            records.addAll( sysApiCallFailureRecordService.queryCallFailedDataByApiCode(Constant.INTERFACE_CODE_FINANCE_NIWOCONTROLLER_SYCREPAYPLAN, AlmsServiceNameEnums.FINANCE.getName()));

            if (CollectionUtils.isNotEmpty(records)) {
                for (SysApiCallFailureRecord record : records) {
                    if (record.getRetrySuccess() != null && record.getRetrySuccess().intValue() == 1) {
                        continue;
                    }
                    Integer retryCount = record.getRetryCount();
                    String apiParamPlaintext = record.getApiParamPlaintext();
                    if (StringUtil.notEmpty(apiParamPlaintext)) {
                        Map paramMap = JSONObject.parseObject(apiParamPlaintext, Map.class);
                        Result result = null;
                        try {
                            result = almsOpenServiceFeignClient.updateRepayPlanToLMS(paramMap);
                        } catch (Exception e) {
                            record.setApiReturnInfo(e.getMessage());
                            LOGGER.error(
                                    "@FinanceUpdateRepayPlanToLMSJob@向信贷系统推送还款计划的变更失败记录重试{}",
                                    record.getRefId());
                        }
                        if (result == null || !"1".equals(result.getCode())) {
                            record.setRetrySuccess(0);
                            if (result != null) {
                                record.setApiReturnInfo(JSONObject.toJSONString(result));
                            }
                        } else {
                            record.setRetrySuccess(1);
                        }
                        record.setRetryCount(++retryCount);
                        record.setCreateUser(this.getClass().getSimpleName());
                        record.setCraeteTime(new Date());
                        record.setRetryTime(new Date());
                        sysApiCallFailureRecordService.insert(record);
                    }
                }
            }

            long end = System.currentTimeMillis();
            LOGGER.info("@FinanceUpdateRepayPlanToLMSJob@向信贷系统推送还款计划的变更失败记录重试--结束，耗时：{}", (end - start));
            return SUCCESS;
        } catch (Exception e) {
            LOGGER.info("@FinanceUpdateRepayPlanToLMSJob@向信贷系统推送还款计划的变更失败记录重试--异常", e);
            return FAIL;
        }
    }
}
