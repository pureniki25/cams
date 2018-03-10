package com.hongte.alms.scheduled.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.JobConfigEnums;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 设置用户可访问的业务id数据库表
 * @author zengkun
 * @since 2018/2/7
 */
@Component
public class SetUserPermission implements   BaseJob {

   private  static Logger logger = LoggerFactory.getLogger(SetUserPermission.class);
//
//    @Autowired
//    @Qualifier("ProcessTypeService")
//    ProcessTypeService processTypeService;
//
//    @Autowired
//    @Qualifier("ProcessTypeStepService")
//    ProcessTypeStepService processTypeStepService;
//
//       @Autowired
//       SysUserClient sysUserClient;

    @Autowired
    @Qualifier("SysJobConfigService")
    SysJobConfigService sysJobConfigService;

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    //运行的标志位
    private  static  boolean runningFlage = false;

//    @Scheduled(cron = "0/1 * * * * ? ")
    public  void job(){

        if(runningFlage){
            return;
        }
        logger.info("进入定时任务");
        runningFlage = true;

        SetUserPermission();


        runningFlage = false;

//        List<ProcessTypeStep> stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);

    }

    public void SetUserPermission(){
        SysJobConfig config = null;
        try {
            //判断定时任务是否可执行
            config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.SET_USER_PSERMISION.getValue());
            if(config ==null)
                return;
            try {

                List<SysUser> list =  sysUserService.selectList(new EntityWrapper<SysUser>());

                for(SysUser user:list){
                    sysUserPermissionService.setUserPermissons(user.getUserId());
                }
                logger.info("完成一次用户对照关系设置");
                config.setLastRunTime(new Date());
                sysJobConfigService.updateById(config);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("定时任务 设置用户可访问业务对照关系 异常："+e.getMessage());
            }


        }catch (Exception e){
            e.printStackTrace();
            logger.error("定时任务 设置用户可访问业务对照关系  更新执行时间 异常："+e.getMessage());
        }
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        jobTest();
    }
}
