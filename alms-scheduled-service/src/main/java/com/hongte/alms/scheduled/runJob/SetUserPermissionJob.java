package com.hongte.alms.scheduled.runJob;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
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
import java.util.List;

/**
 * 
 * @author zk
 * 自动设置用户可访问的业务关系
 */
@JobHandler(value = "setUserPermissionHandler")
@Component
public class SetUserPermissionJob extends IJobHandler  {


    private static Logger logger = LoggerFactory.getLogger(AutoSetCollectionJob.class);

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    @Override
    public ReturnT<String> execute(String params) {

        try {

            List<SysUser> list =  sysUserService.selectList(new EntityWrapper<SysUser>());

            for(SysUser user:list){
                sysUserPermissionService.setUserPermissons(user.getUserId());
            }

            logger.info("完成一次用户对照关系设置");
            return SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("定时任务 设置用户可访问业务对照关系 异常："+e.getMessage());
            return FAIL;
        }
    }



}
