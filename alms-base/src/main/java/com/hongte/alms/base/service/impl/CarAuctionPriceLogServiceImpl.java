package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.vo.CarBidReq;
import com.hongte.alms.base.entity.CarAuction;
import com.hongte.alms.base.entity.CarAuctionPriceLog;
import com.hongte.alms.base.entity.CarAuctionReg;
import com.hongte.alms.base.mapper.CarAuctionPriceLogMapper;
import com.hongte.alms.base.service.CarAuctionPriceLogService;
import com.hongte.alms.base.service.CarAuctionRegService;
import com.hongte.alms.base.service.CarAuctionService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 竞价记录表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-10
 */
@Service("CarAuctionPriceLogService")
public class CarAuctionPriceLogServiceImpl extends BaseServiceImpl<CarAuctionPriceLogMapper, CarAuctionPriceLog> implements CarAuctionPriceLogService {

    @Autowired
    @Qualifier("CarAuctionService")
    private CarAuctionService carAuctionService;

    @Autowired
    @Qualifier("CarAuctionRegService")
    private CarAuctionRegService carAuctionRegService;

    @Autowired
    @Qualifier("CarAuctionPriceLogService")
    private CarAuctionPriceLogService carAuctionPriceLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bid(CarBidReq bidReq) {
        Date currentTime=new Date();
        CarAuctionPriceLog newBidLog=new CarAuctionPriceLog();
        newBidLog.setId(UUID.randomUUID().toString());
        newBidLog.setAuctionId(bidReq.getAuctionId());
        newBidLog.setBidderTel(bidReq.getTelephone());
        newBidLog.setPrice(bidReq.getAmount());
        newBidLog.setCreateTime(currentTime);
        newBidLog.setBidderCertId(bidReq.getIdCard());
        newBidLog.setUserId(bidReq.getUserId());
        newBidLog.setUsername(bidReq.getUserName());
        newBidLog.setRemark(bidReq.getRemark());
        carAuctionPriceLogService.insert(newBidLog);
        CarAuction carAuction=carAuctionService.selectOne(new EntityWrapper().eq("auction_id",bidReq.getAuctionId()));
        if(carAuction==null)
        {
            throw new NullPointerException("CarAuction不存在");
        }
        carAuction.setBidderCount(carAuction.getBidderCount()+1);
        carAuctionService.updateById(carAuction);
        CarAuctionReg auctionReg=carAuctionRegService.selectOne(new EntityWrapper<CarAuctionReg>().eq("auction_id",bidReq.getAuctionId()).eq("user_id",bidReq.getUserId()));
        if(auctionReg==null)
        {
            auctionReg=new CarAuctionReg();
            auctionReg.setRegId(UUID.randomUUID().toString());
            auctionReg.setAuctionId(bidReq.getAuctionId());
            auctionReg.setAuctionSuccess(false);
            auctionReg.setBusinessId(carAuction.getBusinessId());
            auctionReg.setCreateTime(currentTime);
            auctionReg.setCreateUser("");
            auctionReg.setOfferAmount(bidReq.getAmount());
            auctionReg.setOfferTime(currentTime);
            auctionReg.setRegCertId(bidReq.getIdCard());
            auctionReg.setUserId(bidReq.getUserId());
            auctionReg.setUsername(bidReq.getUserName());
            auctionReg.setRegTel(bidReq.getTelephone());
            auctionReg.setRemark(bidReq.getRemark());
            carAuctionRegService.insert(auctionReg);
        }
        else
        {
            auctionReg.setRegCertId(bidReq.getIdCard());
            auctionReg.setUsername(bidReq.getUserName());
            auctionReg.setOfferAmount(bidReq.getAmount());
            auctionReg.setRegTel(bidReq.getTelephone());
            auctionReg.setRemark(bidReq.getRemark());
            auctionReg.setOfferTime(currentTime);
            auctionReg.setUpdateTime(currentTime);
            auctionReg.setUpdateUser("");
            carAuctionRegService.updateById(auctionReg);
        }
    }
}
