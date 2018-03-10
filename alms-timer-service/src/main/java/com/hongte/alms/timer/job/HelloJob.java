package com.hongte.alms.timer.job;

import com.hongte.alms.base.mapper.BasicBusinessMapper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class HelloJob implements BaseJob {  
  
    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);
    @Autowired
    private BasicBusinessMapper basicBusinessMapper;
    public HelloJob() {  
          
    }  

    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException {
        _log.error("Hello Job执行时间: " + new Date());  
          
    }



    public static BaseJob getClass(String classname) throws Exception
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }
}  
