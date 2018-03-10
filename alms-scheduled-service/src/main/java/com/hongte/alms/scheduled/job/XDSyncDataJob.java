package com.hongte.alms.scheduled.job;

import com.hongte.alms.scheduled.service.IXDSyncDataService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 信贷数据同步定时器
 * @Author: 黄咏康
 * @Date: 2018/1/27 0027 下午 1:21
 */
public class XDSyncDataJob implements BaseJob {
    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);

    @Autowired
    private IXDSyncDataService ixdSyncDataService;

    public XDSyncDataJob() {

    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        ixdSyncDataService.syncBaseBusinessData();
        _log.info("同步完毕: " + new Date());
    }
}
