package com.hongte.alms.core.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设置用户可访问的业务id数据库表
 * @author zengkun
 * @since 2018/2/7
 */
//@Component
public class SetUserPermission {

   private  static Logger logger = LoggerFactory.getLogger(SetUserPermission.class);
//
//    @Autowired
//    @Qualifier("ProcessTypeService")
//    ProcessTypeService processTypeService;
//
//    @Autowired
//    @Qualifier("ProcessTypeStepService")
//    ProcessTypeStepService processTypeStepService;

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    //运行的标志位
    private  static  boolean runningFlage = false;

//    @Scheduled(cron = "0/1 * * * * ? ")
    public  void jobTest(){

        if(runningFlage){
            return;
        }

        runningFlage = true;

        try {
            List<SysUser> list =  sysUserService.selectList(new EntityWrapper<SysUser>());

            for(SysUser user:list){
                sysUserPermissionService.setUserPermissons(user.getUserId());
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        runningFlage = false;

//        List<ProcessTypeStep> stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);

    }

}
