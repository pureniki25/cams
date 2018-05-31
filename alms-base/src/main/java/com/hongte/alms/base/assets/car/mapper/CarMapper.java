package com.hongte.alms.base.assets.car.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.assets.car.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆管理
 * @since 2018-01-25
 */
public interface CarMapper  {

	List<CarVo> selectCarPage(CarReq carReq);
	int selectCarPageCount(CarReq carReq);
	//分页查询拍卖信息返回app端
	List<AuctionRespVo> selectAuctionsPageForApp(AuctionsReq vo);
	int selectAuctionsCountForApp(AuctionsReq vo);
	//查询此次拍卖的最高竞价
	Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(@Param("auctionId") String auctionId);
	//查询拍卖人信息
	List<AuctionBidderVo> selectBiddersPageForApp( AuctionsReq vo);
	int selectBiddersCountForApp( AuctionsReq vo);
	
	List<AuctionRespVo> selectAuctionsRegPageForApp(AuctionsReq vo);
	int selectAuctionsRegCountForApp(AuctionsReq vo);

	List<MyBadeCarVo> selectMyBidCarsForApp(Pagination pages, MyBadeCarReq req);

}
