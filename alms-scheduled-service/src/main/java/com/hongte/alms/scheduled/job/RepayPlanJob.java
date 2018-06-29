package com.hongte.alms.scheduled.job;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.feignClient.AlmsOpenServiceFeignClient;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.scheduled.util.XinDaiEncryptUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 还款计划相关定时任务
 * Created by 张贵宏 on 2018/6/27 21:38
 */
@Component
public class RepayPlanJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepayPlanJob.class);

    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;

    @Autowired
    private RestTemplate restTemplateNoLoadBalanced;

    @Autowired
    private AlmsOpenServiceFeignClient almsOpenServiceFeignClient;


    /**
     * 定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    //@Scheduled(cron = "0/1 * * * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0/60 * * * ?")
    public void updateRepayPlanToLMS() {
        LOGGER.info("定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试");
        long start = System.currentTimeMillis();
        // 根据ref_id分组，查找调用失败，且次数小于5次的，且次数最大的一条数据
        List<SysApiCallFailureRecord> records = sysApiCallFailureRecordService
                .queryCallFailedDataByApiCode(Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS, AlmsServiceNameEnums.OPEN.getName());
        if (CollectionUtils.isNotEmpty(records)) {
            for (SysApiCallFailureRecord record : records) {
                if (record.getRetrySuccess() != null && record.getRetrySuccess().intValue() == 1) {
                    continue;
                }
                Integer retryCount = record.getRetryCount();
                String apiParamCiphertext = record.getApiParamCiphertext();
                String targetUrl = record.getTargetUrl();
                ResponseData respData = null;
                if (StringUtil.notEmpty(apiParamCiphertext) && StringUtil.notEmpty(targetUrl)) {
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.set("Content-Type", "application/json");
                        HttpEntity<String> entity = new HttpEntity<>(apiParamCiphertext, headers);
                        //String respStr
                        ResponseEntity<String> response = restTemplateNoLoadBalanced.postForEntity(targetUrl, entity, String.class);
                        // 返回数据解密
                        respData = XinDaiEncryptUtil.getRespData(response.getBody());
                    } catch (Exception e) {
                        record.setApiReturnInfo(e.getMessage());
                        LOGGER.error("将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败，refId：{}", record.getRefId());
                    }
                    if (respData == null || !"1".equals(respData.getReturnCode())) {
                        record.setRetrySuccess(0);
                        if (respData != null) {
                            record.setApiReturnInfo(JSONObject.toJSONString(respData));
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
        LOGGER.info("定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试，耗时：{}", (end - start));
    }


    /**
     * 确认还款拆标情况并存储接口调度任务
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Scheduled(cron = "0 0/60 * * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void financePreviewconfirmrepayment() {
        LOGGER.info("确认还款拆标情况并存储接口调度任务");
        long start = System.currentTimeMillis();

        // 根据ref_id分组，查找调用失败，且次数小于5次的，且次数最大的一条数据
        List<SysApiCallFailureRecord> records = sysApiCallFailureRecordService
                .queryCallFailedDataByApiCode(Constant.INTERFACE_CODE_FINANCE_FINANCE_PREVIEWCONFIRMREPAYMENT, AlmsServiceNameEnums.FINANCE.getName());
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
                        LOGGER.error("确认还款拆标情况并存储接口失败，refId：{}", record.getRefId());
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
        LOGGER.info("确认还款拆标情况并存储接口调度任务，耗时：{}", (end - start));
    }
}
