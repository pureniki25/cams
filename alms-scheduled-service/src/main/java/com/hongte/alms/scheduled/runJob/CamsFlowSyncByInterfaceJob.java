package com.hongte.alms.scheduled.runJob;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.scheduled.client.CamsFlowSyncByInterfaceJobClient;
import com.ht.ussp.core.Result;
import com.ht.ussp.util.DateUtil;
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
        	int retryTimes1 = 1;
	    	//while(retryTimes1 < 3) {
	    		try {
	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步还款流水到核心开始");
	    			Result<Object> ret1 = camsFlowSyncByInterfaceJobClient.addBatchFlow();
	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步还款流水到核心结束,"+JSONObject.toJSONString(ret1)+"等待30秒推送撤销流水");
	    			
//	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步资金分发流水到核心开始");
//	    			Result<Object> ret3 = camsFlowSyncByInterfaceJobClient.addBatchFenFaFlow();
//	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步资金分发流水到核心结束"+JSONObject.toJSONString(ret3));
//	    			
//	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步你我金融流水到核心开始");
//	    			Result<Object> ret3 = camsFlowSyncByInterfaceJobClient.pushNiWoRepayFlowToCams();
//	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步你我金融流水到核心结束"+JSONObject.toJSONString(ret3));
//	    			
	    			Thread.sleep(30000);
	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步撤销流水到核心开始");
	    			Result<Object> ret2 = camsFlowSyncByInterfaceJobClient.cancelRepayFlow();
	    			XxlJobLogger.log(DateUtil.formatDate(DateUtil.FULL_TIME_FORMAT, new Date())+"同步撤销流水到核心结束"+JSONObject.toJSONString(ret2));
	    			
	    			//break;//跳出循环
	    			//camsFlowSyncByInterfaceJobClient.addBatchFenFaFlow();
	    			//camsFlowSyncByInterfaceJobClient.cancelFenFaFlow();
	    		} catch (Exception e) {
	    			XxlJobLogger.log(e.getMessage());
	    			e.printStackTrace();
	    			Thread.sleep(100);
	    			retryTimes1++;
				}
	    	//}
        	XxlJobLogger.log("同步流水到核心结束"+new Date().getTime());
            logger.info("同步流水到核心结束");
            return SUCCESS;
        }catch (Exception e){
            logger.error("同步流水到核心异常:"+e.getMessage());
            return FAIL;
        }
    }



}
