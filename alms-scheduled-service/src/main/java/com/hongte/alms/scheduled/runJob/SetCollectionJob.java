package com.hongte.alms.scheduled.runJob;


import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.scheduled.job.AutoSetCollectionJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 
 * @author zk
 * 每天自动分配催收
 */
@JobHandler(value = "setCollectionHandler")
@Component
public class SetCollectionJob extends IJobHandler  {


    private static Logger logger = LoggerFactory.getLogger(AutoSetCollectionJob.class);

    @Autowired
    @Qualifier("CollectionStatusService")
    private CollectionStatusService collectionStatusService;

    @Override
    public ReturnT<String> execute(String params) {

        try {
            logger.info("自动分配催收信息  开始");
            collectionStatusService.autoSetBusinessStaff();
            logger.info("自动分配催收信息  结束" );
            return SUCCESS;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("自动分配催收信息  异常："+ ex.getStackTrace() );
            return FAIL;
        }
    }


}
