package com.hongte.alms.base.assets.car.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.assets.car.vo.*;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.common.result.Result;

public interface CarService {

	Page<CarVo> selectCarPage(CarReq carReq);
	List<CarVo> selectCarList(CarReq carReq);
	Page<AuctionRespVo> selectAuctionsPageForApp( AuctionsReq req);

	Page<MyBadeCarVo> selectMyBidCarsForApp(MyBadeCarReq req);
	Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(String auctionId);
	
	Page<AuctionRespVo> selectAuctionsRegPageForApp( AuctionsReq req);
	
	String auctionAply(AuctionAplyVo auctionAplyVo);
	void auctionAudit(AuctionAplyVo auctionAplyVo)throws Exception;
	void againAssess(AuctionAplyVo auctionAplyVo);
	void auctionSign(Map<String,Object> params);
	void addReturnReg( @RequestBody Map<String,Object> params);
}
