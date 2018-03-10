package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarAuctionReg;
import com.hongte.alms.base.mapper.CarAuctionRegMapper;
import com.hongte.alms.base.service.CarAuctionRegService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆拍卖信息登记 服务实现类
 * </p>
 *
 * @author cj
 * @since 2018-03-06
 */
@Service("CarAuctionRegService")
public class CarAuctionRegServiceImpl extends BaseServiceImpl<CarAuctionRegMapper, CarAuctionReg> implements CarAuctionRegService {

}
