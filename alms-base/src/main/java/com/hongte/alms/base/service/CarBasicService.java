package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 车辆基本信息 服务类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-24
 */
public interface CarBasicService extends BaseService<CarBasic> {

    //更新车辆状态为已结清
    public boolean updateCarStatusToSettled(String businessId);

    //将车辆状态还原为已结清之前的状态
    public boolean revokeCarStatus(String  businessId);

}
