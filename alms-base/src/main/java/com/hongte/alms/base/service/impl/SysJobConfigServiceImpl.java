package com.hongte.alms.base.service.impl;


import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.enums.JobConfigEnums;
import com.hongte.alms.base.mapper.SysJobConfigMapper;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 定时器控制表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
@Service("SysJobConfigService")
public class SysJobConfigServiceImpl extends BaseServiceImpl<SysJobConfigMapper, SysJobConfig> implements SysJobConfigService {

    @Override
    public SysJobConfig getCanExecuteConfig(String jobConfigId) {
        SysJobConfig config = selectById(jobConfigId);
        if(config == null){
            return null;
        }
        //如果把定时任务的配置设置为无效，则直接返回，不继续判断
        if(config.getActivate().equals(JobConfigEnums.ActiveEnum.INACTIVE.getValue())){
            return config;
        }

        //判断定时任务是否开启，未开启则返回空
        if(!config.getFlagLock().equals(JobConfigEnums.JobLockType.UNLOCK.getValue())){
            return null;
        }
        if(config.getLastRunTime()==null){
            return config;
        }

        if(config.getTimeType().equals(JobConfigEnums.TimeIntervalType.BY_HOUR.getValue())){
            //按小时算
            if(new Date().compareTo(DateUtil.addHour2Date(config.getTimeInterval(),config.getLastRunTime()))>0){
                return config;
            }else{
                return null;
            }

        }else  if (config.getTimeType().equals(JobConfigEnums.TimeIntervalType.BY_MINUTE.getValue())){
            //按分钟算
            Date date = DateUtil.addMinute2Date(config.getTimeInterval(),config.getLastRunTime());
            Date nowDate = new Date();
            if(nowDate.compareTo(date)>0){
                return config;
            }else{
                return null;
            }
        }else  if (config.getTimeType().equals(JobConfigEnums.TimeIntervalType.BY_SECOND.getValue())){
            //按秒算
            if(new Date().compareTo(DateUtil.addSecond2Date(config.getTimeInterval(),config.getLastRunTime()))>0){
                return config;
            }else{
                return null;
            }
        }else  if (config.getTimeType().equals(JobConfigEnums.TimeIntervalType.BY_DAY.getValue())){
            //按天算
            if(new Date().compareTo(DateUtil.addDay2Date(config.getTimeInterval(),config.getLastRunTime()))>0){
                return config;
            }else{
                return null;
            }
        }else  if (config.getTimeType().equals(JobConfigEnums.TimeIntervalType.NONE.getValue())){
            //无限制
            return config;
        }

        return null;
    }
}
