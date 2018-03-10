package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.base.mapper.CarBasicMapper;
import com.hongte.alms.base.service.CarBasicService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆基本信息 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-24
 */
@Service("CarBasicService")
public class CarBasicServiceImpl extends BaseServiceImpl<CarBasicMapper, CarBasic> implements CarBasicService {

}
