package com.hongte.alms.scheduled.job;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.scheduled.util.XinDaiEncryptUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * 还款计划相关定时任务
 * Created by 张贵宏 on 2018/6/27 21:38
 */
public class RepayPlanJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepayPlanJob.class);

    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    //@Scheduled(cron = "0/1 * * * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateRepayPlanToLMS() {
        LOGGER.info("定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试");
        long start = System.currentTimeMillis();

        // 根据ref_id分组，查找调用失败，且次数小于5次的，且次数最大的一条数据
        List<SysApiCallFailureRecord> records = sysApiCallFailureRecordService
                .queryCallFailedDataByApiCode(Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS);
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
                        String respStr = restTemplate.postForObject(targetUrl, apiParamCiphertext, String.class);
                        // 返回数据解密
                        respData = XinDaiEncryptUtil.getRespData(respStr);
                    } catch (Exception e) {
                        record.setApiReturnInfo(e.getMessage());
                        LOGGER.error("将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败，refId：{}", record.getRefId());
                    }
                    if (respData == null || !"1".equals(respData.getReturnCode())) {
                        record.setRetrySuccess(1);
                        record.setApiReturnInfo(JSONObject.toJSONString(respData));
                    } else {
                        record.setRetrySuccess(0);
                    }
                    record.setRetryCount(++retryCount);
                    record.setCreateUser("定时任务");
                    record.setCraeteTime(new Date());
                    sysApiCallFailureRecordService.insert(record);
                }
            }
        }

        long end = System.currentTimeMillis();
        LOGGER.info("定时将指定业务的还款计划的变动通过信贷接口推送给信贷系统失败的记录重试，耗时：{}", (end - start));
    }
}
