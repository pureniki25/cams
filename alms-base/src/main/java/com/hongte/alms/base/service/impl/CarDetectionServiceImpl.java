package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarDetection;
import com.hongte.alms.base.mapper.CarDetectionMapper;
import com.hongte.alms.base.service.CarDetectionService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆检测 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-24
 */
@Service("CarDetectionService")
public class CarDetectionServiceImpl extends BaseServiceImpl<CarDetectionMapper, CarDetection> implements CarDetectionService {

}
