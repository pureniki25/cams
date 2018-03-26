package com.hongte.alms.base.assets.car.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.AuctionAplyVo;
import com.hongte.alms.base.assets.car.vo.AuctionBidderVo;
import com.hongte.alms.base.assets.car.vo.AuctionRespVo;
import com.hongte.alms.base.assets.car.vo.AuctionsReqVo;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

public interface CarService {

	public Page<CarVo> selectCarPage(CarReq carReq);
	public List<CarVo> selectCarList(CarReq carReq);
	Page<AuctionRespVo> selectAuctionsPageForApp( AuctionsReqVo vo);
	Page<AuctionBidderVo> selectBiddersPageForApp( AuctionsReqVo vo);
	Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(String auctionId);
	
	Page<AuctionRespVo> selectAuctionsRegPageForApp( AuctionsReqVo vo);
	
	String auctionAply(AuctionAplyVo auctionAplyVo);
	void auctionAudit(AuctionAplyVo auctionAplyVo)throws Exception;
	void againAssess(AuctionAplyVo auctionAplyVo);
}
