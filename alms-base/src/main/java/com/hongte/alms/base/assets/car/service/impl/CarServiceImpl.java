package com.hongte.alms.base.assets.car.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.mapper.CarMapper;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.assets.car.vo.AuctionBidderVo;
import com.hongte.alms.base.assets.car.vo.AuctionRespVo;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

@Service("CarService")
public class CarServiceImpl  implements CarService {
	
	@Autowired
	private CarMapper carMapper;

	public Page<CarVo> selectCarPage(CarReq carReq) {
			Page<CarVo> pages = new Page<CarVo>();
		   int count=carMapper.selectCarPageCount(carReq);
		   if(count<=0) {
			   return pages;
		   }
		   List<CarVo> list=carMapper.selectCarPage(carReq);
		   pages.setTotal(count);
		   pages.setRecords(list);
		return pages;
	}

	public List<CarVo> selectCarList(CarReq carReq){
		  List<CarVo> list=carMapper.selectCarPage(carReq);
		  return list;
	}
	
	public Page<AuctionRespVo> selectAuctionsPageForApp( int page, int limit){
		Page<AuctionRespVo> pages = new Page<AuctionRespVo>();
		int count=carMapper.selectAuctionsCountForApp();
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionRespVo> list=carMapper.selectAuctionsPageForApp(page, limit);
		  pages.setTotal(count);
		   pages.setRecords(list);
		return pages;
	}
	public Page<AuctionBidderVo> selectBiddersPageForApp(int page, int limit,Integer isPayDeposit,String auctionId,String bidderName,String retTel){
		Page<AuctionBidderVo>  pages=new Page<AuctionBidderVo> ();
		int count=carMapper.selectBiddersCountForApp(isPayDeposit, auctionId, bidderName,retTel);
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionBidderVo> list=carMapper.selectBiddersPageForApp(page, limit, isPayDeposit, auctionId, bidderName,retTel);
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;		
	}
	public Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(String auctionId){
		return carMapper.selectMaxOfferPriceByAuctionId(auctionId);
	}
}
