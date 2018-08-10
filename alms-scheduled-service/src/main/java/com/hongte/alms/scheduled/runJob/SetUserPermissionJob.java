package com.hongte.alms.scheduled.runJob;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.scheduled.job.AutoSetCollectionJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

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
    
    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;

    @Override
    public ReturnT<String> execute(String params) {
    	int steps = 1000;
        try {
            List<SysParameter> repayTypeList = sysParameterService.selectList(new EntityWrapper<SysParameter>()
                    .eq("param_type", SysParameterTypeEnums.RIGHT_STEP_TIME.getKey()));
        	if(null != repayTypeList && !repayTypeList.isEmpty()) {
        		try {
					steps = Integer.parseInt(repayTypeList.get(0).getParamValue());
				} catch (Exception e) {
					steps = 1000;
				}
        	}
        	
            List<SysUser> list =  sysUserService.selectList(new EntityWrapper<SysUser>());
            
            for(SysUser user:list){
            	XxlJobLogger.log("@SyncDaihouJob@同步用户等待{}",steps);
            	logger.info("同步用户等待{}!",steps);
            	Thread.sleep(steps);
            	XxlJobLogger.log("@SyncDaihouJob@同步用户权限开始{},时间{}",user.getUserId(),new Date().getTime());
            	logger.info("同步用户{}权限开始!",user.getUserId());
                sysUserPermissionService.setUserPermissons(user.getUserId());
                logger.info("同步用户{}权限完成!",user.getUserId());
                XxlJobLogger.log("@SyncDaihouJob@同步用户权限结束时间{},时间{}",user.getUserId(),new Date().getTime());
                
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
