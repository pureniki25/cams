package com.hongte.alms.base.service;

import com.hongte.alms.base.assets.car.vo.CarBidReq;
import com.hongte.alms.base.entity.CarAuctionPriceLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 竞价记录表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-10
 */
public interface CarAuctionPriceLogService extends BaseService<CarAuctionPriceLog> {

    void bid(CarBidReq bidReq);

}
