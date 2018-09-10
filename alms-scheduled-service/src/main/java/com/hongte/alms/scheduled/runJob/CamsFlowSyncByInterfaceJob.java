package com.hongte.alms.scheduled.runJob;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.scheduled.client.CamsFlowSyncByInterfaceJobClient;
import com.hongte.alms.scheduled.client.WithholdingClient;
import com.hongte.alms.scheduled.job.AutoSetCollectionJob;
import com.ht.ussp.core.Result;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 
 * @author liuzq
 * 自动设置用户可访问的业务关系
 */
@JobHandler(value = "CamsFlowSyncByInterfaceJob")
@Component
public class CamsFlowSyncByInterfaceJob extends IJobHandler  {


    private static Logger logger = LoggerFactory.getLogger(CamsFlowSyncByInterfaceJob.class);
    
	@Autowired
	CamsFlowSyncByInterfaceJobClient camsFlowSyncByInterfaceJobClient;
    
    @Override
    public ReturnT<String> execute(String params) {
        try {
        	XxlJobLogger.log("同步流水到核心开始"+new Date().getTime());
        	
        	// 批量推送还款结清
        	int retryTimes1 = 0;
	    	while(retryTimes1 < 3) {
	    		try {
	    			camsFlowSyncByInterfaceJobClient.addBatchFlow();
	    			camsFlowSyncByInterfaceJobClient.cancelRepayFlow();
	    			//camsFlowSyncByInterfaceJobClient.addBatchFenFaFlow();
	    			//camsFlowSyncByInterfaceJobClient.cancelFenFaFlow();
	    		} catch (Exception e) {
	    			Thread.sleep(100);
	    			retryTimes1++;
				}
	    	}
        	
        	XxlJobLogger.log("同步流水到核心结束"+new Date().getTime());
            logger.info("同步流水到核心结束");
            return SUCCESS;
        }catch (Exception e){
            logger.error("同步流水到核心异常:"+e.getMessage());
            return FAIL;
        }
    }



}
