package com.hongte.alms.base.assets.car.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.assets.car.vo.AuctionBidderVo;
import com.hongte.alms.base.assets.car.vo.AuctionRespVo;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

/**
 * 车辆管理
 * @since 2018-01-25
 */
public interface CarMapper  {

	List<CarVo> selectCarPage(CarReq carReq);
	int selectCarPageCount(CarReq carReq);
	//分页查询拍卖信息返回app端
	List<AuctionRespVo> selectAuctionsPageForApp(@Param("page") int page,@Param("limit") int limit);
	int selectAuctionsCountForApp();
	//查询此次拍卖的最高竞价
	Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(@Param("auctionId") String auctionId);
	//查询拍卖信息
	List<AuctionBidderVo> selectBiddersPageForApp(@Param("page") int page,@Param("limit") int limit ,@Param("isPayDeposit")Integer isPayDeposit,@Param("auctionId")String auctionId,@Param("bidderName") String bidderName,@Param("regTel") String regTel);
	int selectBiddersCountForApp(@Param("isPayDeposit")Integer isPayDeposit,@Param("auctionId")String auctionId,@Param("bidderName") String bidderName,@Param("regTel") String regTel);
}
