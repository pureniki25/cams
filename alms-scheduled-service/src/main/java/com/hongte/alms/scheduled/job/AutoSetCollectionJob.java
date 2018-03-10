package com.hongte.alms.scheduled.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.JobConfigEnums;
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
 * @author zengkun
 * @since 2018/3/8
 * 自动移交催收的定时任务
 */
@Component
public class AutoSetCollectionJob {

    private static Logger logger = LoggerFactory.getLogger(AutoSetCollectionJob.class);

    @Autowired
    @Qualifier("CollectionStatusService")
    private CollectionStatusService collectionStatusService;

    @Autowired
    @Qualifier("SysJobConfigService")
    private SysJobConfigService sysJobConfigService;

    public void autoSetCollection(){

        SysJobConfig config = null;
        try {
            //判断定时任务是否可执行
            config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.SET_USER_PSERMISION.getValue());
            if(config ==null)
                return;
            try {

               collectionStatusService.autoSetBusinessStaff();


                logger.info("完成一次催收自动移交");
                config.setLastRunTime(new Date());
                sysJobConfigService.updateById(config);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("定时任务 业务自动移交 异常："+e.getMessage());
            }


        }catch (Exception e){
            e.printStackTrace();
            logger.error("定时任务 设置用户可访问业务对照关系  更新执行时间 异常："+e.getMessage());
        }

    }

    //运行的标志位
    private  static  boolean runningFlage = false;

    @Scheduled(cron = "0/1 * * * * ? ")
    public  void job(){

        if(runningFlage){
            return;
        }
        logger.info("进入定时任务");
        runningFlage = true;

        autoSetCollection();


        runningFlage = false;

//        List<ProcessTypeStep> stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);

    }

}
