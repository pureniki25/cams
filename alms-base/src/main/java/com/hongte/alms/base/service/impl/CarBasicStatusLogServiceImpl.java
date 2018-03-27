package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarBasicStatusLog;
import com.hongte.alms.base.mapper.CarBasicStatusLogMapper;
import com.hongte.alms.base.service.CarBasicStatusLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆基本信息状态变更记录表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-26
 */
@Service("CarBasicStatusLogService")
public class CarBasicStatusLogServiceImpl extends BaseServiceImpl<CarBasicStatusLogMapper, CarBasicStatusLog> implements CarBasicStatusLogService {

}
