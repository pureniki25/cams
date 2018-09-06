package com.hongte.alms.scheduled.runJob;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.scheduled.job.AutoSetCollectionJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 
 * @author liuzq
 * 自动设置用户可访问的业务关系
 */
@JobHandler(value = "CamsFlowSyncJob")
@Component
public class CamsFlowSyncJob extends IJobHandler  {


    private static Logger logger = LoggerFactory.getLogger(AutoSetCollectionJob.class);

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Override
    public ReturnT<String> execute(String params) {
        try {
        	XxlJobLogger.log("同步流水到临时表开始"+new Date().getTime());
        	Map<String,Object> paramLastDayMap = new HashMap<>();
        	Date preDate = DateUtil.addDay2Date(-1, new Date());
        	paramLastDayMap.put("syncDay1",DateUtil.getThatDayBegin(preDate));
        	paramLastDayMap.put("syncDay2",DateUtil.getThatDayEnd(preDate));
        	paramLastDayMap.put("syncDay", DateUtil.toDateString(preDate, DateUtil.DEFAULT_FORMAT_DATE));
            basicBusinessService.addLastDayFlow(paramLastDayMap);
        	basicBusinessService.addLastDayFlowItem(paramLastDayMap);
        	XxlJobLogger.log("同步流水到临时表结束"+new Date().getTime());
            logger.info("同步流水到临时表结束");
            return SUCCESS;
        }catch (Exception e){
            logger.error("同步流水到临时表异常:"+e.getMessage());
            return FAIL;
        }
    }



}
