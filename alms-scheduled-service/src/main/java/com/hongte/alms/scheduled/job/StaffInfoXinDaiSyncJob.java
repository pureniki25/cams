package com.hongte.alms.scheduled.job;

import com.hongte.alms.scheduled.service.IXDSyncDataService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时同步信贷电催/催收信息的Job
 * 曾坤  2018-5-21
 */
@Component
public class StaffInfoXinDaiSyncJob implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);

    @Autowired
    private IXDSyncDataService ixdSyncDataService;

    public StaffInfoXinDaiSyncJob() {

    }


    @Scheduled(cron = "0 0 3 * * ?")
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        ixdSyncDataService.syncBaseBusinessData();
        _log.info("同步完毕: " + new Date());
    }
}
