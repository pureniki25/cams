package com.hongte.alms.scheduled.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.enums.JobConfigEnums;
import com.hongte.alms.base.feignClient.service.EipOperateService;
import com.hongte.alms.base.service.SysJobConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author:陈泽圣
 * @date: 2018/3/13
 */
@Component
public class CalOverDaysAndLateFeeJob {

    private static Logger logger = LoggerFactory.getLogger(CalOverDaysAndLateFeeJob.class);

    @Autowired
    @Qualifier("SysJobConfigService")
    private SysJobConfigService sysJobConfigService;

   
    public void calOverDaysAndLateFeeJob(){

        SysJobConfig config = null;
        try {
            //判断定时任务是否可执行
            config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.Cal_OVERDAYS_LATEFEE.getValue());
            if(config ==null)
                return;
            try {
   
                logger.info("完成一次自动推送贷后跟踪记录");
                config.setLastRunTime(new Date());
                sysJobConfigService.updateById(config);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("定时任务 自动推送贷后跟踪记录 异常："+e.getMessage());
            }


        }catch (Exception e){
            e.printStackTrace();
            logger.error("定时任务 自动推送贷后跟踪记录  更新执行时间 异常："+e.getMessage());
        }

    }

    //运行的标志位
    private  static  boolean runningFlage = false;

//    @Scheduled(cron = "0 0/10 * * * ? ")


    @Scheduled(cron = "0 0 1 * * ?")
    public  void job(){

        if(runningFlage){
            return;
        }
        runningFlage = true;

        calOverDaysAndLateFeeJob();


        runningFlage = false;

//        List<ProcessTypeStep> stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);

    }

}
