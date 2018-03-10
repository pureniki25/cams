package com.hongte.alms.base.service;


import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.common.service.BaseService;


/**
 * <p>
 * 定时器控制表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
public interface SysJobConfigService extends BaseService<SysJobConfig> {

    SysJobConfig getCanExecuteConfig(String jobId);
}
