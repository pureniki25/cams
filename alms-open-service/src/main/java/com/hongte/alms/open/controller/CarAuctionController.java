package com.hongte.alms.open.controller;

import com.hongte.alms.base.assets.car.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.baseException.AlmsBaseExcepiton;
import com.hongte.alms.base.entity.CarAuction;
import com.hongte.alms.base.entity.CarAuctionBidder;
import com.hongte.alms.base.entity.CarAuctionPriceLog;
import com.hongte.alms.base.entity.CarAuctionReg;
import com.hongte.alms.base.service.CarAuctionBidderService;
import com.hongte.alms.base.service.CarAuctionPriceLogService;
import com.hongte.alms.base.service.CarAuctionRegService;
import com.hongte.alms.base.service.CarAuctionService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("carAuction")
@Api(tags = "CarAuctionController", description = "拍卖相关接口")

public class CarAuctionController {
    private Logger logger = LoggerFactory.getLogger(CarAuctionController.class);
    
	@Autowired
	@Qualifier("CarService")
	private CarService carService;
	
	@Autowired
	@Qualifier("CarAuctionService")
	private CarAuctionService carAuctionService;
	
	
	@Autowired
	@Qualifier("CarAuctionRegService")
	private CarAuctionRegService carAuctionRegService;
	
	@Autowired
	@Qualifier("CarAuctionPriceLogService")
	private CarAuctionPriceLogService carAuctionPriceLogService;
	
	@Autowired
	@Qualifier("CarAuctionBidderService")
	private CarAuctionBidderService carAuctionBidderService;
	
	
	@ApiOperation(value = "分页查询审核信息对外接口")
    @PostMapping("/selectAuctionsPage")
    public PageResult<List<AuctionRespVo>> selectAuctionsPage (@RequestBody AuctionsReq req) {
			 
		try {

			if(req==null||StringUtils.isEmpty(req.getType())) {
				logger.error("参数为空,type="+req.getType());
				return PageResult.error(9999, "参数为空,type="+req.getType());
			}
			Page<AuctionRespVo> pages=carService.selectAuctionsPageForApp(req);
			return PageResult.success(pages.getRecords(),pages.getTotal());
		}catch (Exception e) {
			logger.error(e.getMessage());
			return PageResult.error(9999, "系统异常"); 
		}
    }

	@ApiOperation(value="查询拍卖最高竞价")
	@PostMapping("/selectMaxOfferPriceByAuctionId")
	public Result<Object> selectMaxOfferPriceByAuctionId(@RequestBody Map<String,Object> params){
		try {
		String priceID=(String) params.get("priceID");
		if(StringUtils.isEmpty(priceID)) {
			logger.error("参数为空,priceId="+priceID);
			return Result.error("9999", "参数为空"); 
		}
		Map<String,BigDecimal> maxPrice=carService.selectMaxOfferPriceByAuctionId(priceID);
		return Result.build("0000", "查询成功", maxPrice);
		}catch (Exception e) {
			logger.error(e.getMessage());
			return Result.error("9999", "系统异常"); 
		}
	}

	@ApiOperation(value="更新拍卖信息")
	@PostMapping("/updateAuctions")
	public Result<Object> updateAuctions(@RequestBody CarBidReq req){
		try {
			List<CarAuction> auctions=carAuctionService.selectList( new EntityWrapper<CarAuction>().eq("auction_id", req.getAuctionId()).eq("status", "04"));
			if(auctions==null||auctions.size()!=1) {
				logger.error("参数为空,auction_id="+req.getAuctionId());
				return Result.error("9999", "无效的拍卖");
			}
			CarAuction carAuction=auctions.get(0);
			//判断当前是否还在拍卖时间
			long startTime=carAuction.getAuctionStartTime().getTime();
			long endtTime=carAuction.getAuctionEndTime().getTime();
			long currentTime=System.currentTimeMillis();
			if(!(currentTime>=startTime&&currentTime<=endtTime)) {
				logger.error("不在活动时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
				return Result.error("9999", "不在活动时间内");
			}
			//查询竞价记录
			List<CarAuctionPriceLog> bidLogs=carAuctionPriceLogService.selectList(new EntityWrapper<CarAuctionPriceLog>().eq("auction_id", req.getAuctionId()).eq("bidder_tel", req.getTelephone()));
			if(bidLogs.size()>=3) {
				logger.error("超过3次竞价,auction_id="+req.getAuctionId()+",reg_tel"+req.getTelephone());
				return Result.error("9999", "已经超过报价次数，不能报价");
			}
			for(CarAuctionPriceLog bidLog :bidLogs)
			{
				if(bidLog.getPrice().equals(req.getAmount()))
				{
					logger.error("报价重复,auction_id="+req.getAuctionId()+",reg_tel"+req.getTelephone()+",price"+req.getAmount());
					return Result.error("9999", "与之前报价重复，请修改价格后再报价");
				}
			}
			carAuctionPriceLogService.bid(req);
			return Result.error("0", "提交成功");
		}catch (Exception e) {
			logger.error(e.getMessage());
			return Result.error("9999", "系统异常"); 
		}
	}

	@ApiOperation(value="我的报价记录")
	@PostMapping("/myBids")
	public Result<Object> myBids(@RequestBody MyBidsReq req){
		try {
         List<CarAuctionPriceLog> bids=carAuctionPriceLogService.selectList(new EntityWrapper<CarAuctionPriceLog>().eq("user_id",req.getUserId()).eq("auction_id",req.getAuctionId()));
         List<MyBidsVo> myBidsList=new ArrayList<MyBidsVo>();
         for(CarAuctionPriceLog bid :bids) {
			 MyBidsVo myBidsVo = new MyBidsVo();
			 myBidsVo.setBidTime(bid.getCreateTime());
			 myBidsVo.setBidPrice(bid.getPrice());
			 myBidsList.add(myBidsVo);
		 }
		 return Result.build("0","请求成功",myBidsList);
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Result.error("9999", "系统异常");
		}
	}


	@ApiOperation(value = "分页查询我报过价的车辆")
	@PostMapping("/myBidCars")
	public PageResult<List<MyBadeCarVo>> myBidCars(@RequestBody MyBadeCarReq req) {

		try {
			if(req==null) {
				logger.error("参数为空,req为null");
				return PageResult.error(9999, "参数为空,req为null");
			}
			Page<MyBadeCarVo> pages=carService.selectMyBidCarsForApp(req);
			return PageResult.success(pages.getRecords(),pages.getTotal());
		}catch (Exception e) {
			logger.error(e.getMessage());
			return PageResult.error(9999, "系统异常");
		}
	}


	@ApiOperation(value="拍卖报名")
	@PostMapping("/auctionSign")
	public Result<Object>auctionSign(@RequestBody Map<String,Object> params){
	try {
		carService.auctionSign(params);
		return Result.error("0000", "报名成功"); 
	}catch (AlmsBaseExcepiton e) {
		logger.error(e.getMsg());
		return Result.error(e.getCode(),e.getMsg()); 
	}catch (Exception e) {
		logger.error(e.getMessage());
		e.printStackTrace();
		return Result.error("9999", "报名失败"); 
	}
	}

	@ApiOperation(value = "分页查询竞拍记录")
    @PostMapping("/selectAuctionReg")
    public PageResult<List<AuctionRespVo>> selectAuctionReg (@RequestBody AuctionsReq req) {
			 
		try {

			if(req==null||StringUtils.isEmpty(req.getType())) {
				logger.error("参数为空,type="+req.getType());
				return PageResult.error(9999, "参数为空,type="+req.getType()); 
			}
			Page<AuctionRespVo> pages=carService.selectAuctionsRegPageForApp(req);
			return PageResult.success(pages.getRecords(),pages.getTotal());
		}catch (Exception e) {
			logger.error(e.getMessage());
			return PageResult.error(9999, "系统异常"); 
		}
    }
}
