package com.hongte.alms.scheduled.runJob;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.scheduled.client.CollectionRemoteApi;
import com.hongte.alms.scheduled.client.WithholdingClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/7/12
 * 定时任务，用于更新失败的电催分配数据
 */
@JobHandler(value = "syncUpdateFailCollectionInfoJobHandler")
@Component
public class SyncUpdateFailCollectionInfoJob extends IJobHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SyncRechargeResultJob.class);



    @Autowired
    IssueSendOutsideLogService issueSendOutsideLogService;


    @Autowired
    CollectionRemoteApi collectionRemoteApi;



    @Override
    public ReturnT<String> execute(String arg0) throws Exception {
        try {

            LOG.info("@AutoRecharge@重新同步同步失败的电催人员信息--开始[{},{}]");
            List<IssueSendOutsideLog> outsideLogList= issueSendOutsideLogService.selectList(
                    new EntityWrapper<IssueSendOutsideLog>()
                            .eq("send_url","/collection/setPhoneStaff")
                            .eq("return_json","{\"code\":\"111111\",\"msg\":\"同步失败\"}"));

            for(IssueSendOutsideLog issueSendOutsideLog:outsideLogList){
                String json= issueSendOutsideLog.getSendJson();
                CarBusinessAfter carBusinessAfter = JSON.parseObject(json,CarBusinessAfter.class);
                Result result =collectionRemoteApi.transferOnePhoneSet(carBusinessAfter);
                issueSendOutsideLog.setReturnJson(JSON.toJSONString(result));
                issueSendOutsideLogService.updateById(issueSendOutsideLog);
            }

            LOG.info("@AutoRecharge@重新同步同步失败的电催人员信息--结束[{}]");
            return SUCCESS;
        } catch (Exception e) {
            LOG.error("同步代扣结果失败", e);
            return FAIL;
        }

    }
}
