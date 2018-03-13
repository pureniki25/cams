package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarAuction;
import com.hongte.alms.base.mapper.CarAuctionMapper;
import com.hongte.alms.base.service.CarAuctionService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆拍卖信息 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-10
 */
@Service("CarAuctionService")
public class CarAuctionServiceImpl extends BaseServiceImpl<CarAuctionMapper, CarAuction> implements CarAuctionService {

}
