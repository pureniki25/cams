package com.hongte.alms.timer.job;

import com.hongte.alms.base.mapper.BasicBusinessMapper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class NewJob implements BaseJob {  
  
    private static Logger _log = LoggerFactory.getLogger(NewJob.class);

    @Autowired
    private BasicBusinessMapper basicBusinessMapper;

    public NewJob() {  
          
    }  

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        _log.error("New Job执行时间: " + new Date());  
          
    }


}  