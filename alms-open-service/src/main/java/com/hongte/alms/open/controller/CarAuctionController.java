package com.hongte.alms.open.controller;

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
import com.hongte.alms.base.assets.car.vo.AuctionBidderVo;
import com.hongte.alms.base.assets.car.vo.AuctionRespVo;
import com.hongte.alms.base.assets.car.vo.AuctionsReqVo;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public PageResult<List<AuctionRespVo>> selectAuctionsPage (@RequestBody AuctionsReqVo req) {
			 
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
	@ApiOperation(value = "分页查询竞价信息对外接口")
    @PostMapping("/selectBiddersPage")
    public PageResult<List<AuctionBidderVo>> selectBiddersPage (@RequestBody AuctionsReqVo req) {
			 
		try {
	
			if(req==null||StringUtils.isEmpty(req.getPriceID())||StringUtils.isEmpty(req.getTelephone())) {
				logger.error("参数为空,priceId="+req.getPriceID()+",telephone="+req.getTelephone());
				return PageResult.error(9999, "参数为空,priceId="+req.getPriceID()+",telephone="+req.getTelephone()); 
			}
			Page<AuctionBidderVo> pages=carService.selectBiddersPageForApp(req);
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
	public Result<Object>updateAuctions(@RequestBody Map<String,Object> params){
		try {
			String priceID=(String) params.get("priceID");
			String userName=(String) params.get("userName");
			String userId=(String) params.get("userID");
			String telephone=(String) params.get("telephone");
			double amount =(double) params.get("amount");
			List<CarAuction> auctions=carAuctionService.selectList( new EntityWrapper<CarAuction>().eq("auction_id", priceID).eq("status", "04"));
			if(auctions==null||auctions.size()!=1) {
				logger.error("参数为空,auction_id="+priceID);
				return Result.error("9999", "无效的拍卖"); 
			}
			CarAuction carAuction=auctions.get(0);
			if(carAuction.getAuctionStartTime()==null||carAuction.getAuctionEndTime()==null) {
				logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
				return Result.error("9999", "无效的拍卖"); 
			}
			//判断当前是否还在拍卖时间
			long startTime=carAuction.getAuctionStartTime().getTime();
			long endtTime=carAuction.getAuctionEndTime().getTime();
			long currentTime=new Date().getTime();
			if(!(currentTime>=startTime&&currentTime<=endtTime)) {
				logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
				return Result.error("9999", "不在竞拍时间内不能竞拍"); 
			}
			//查询拍卖登记信息并更新保存竞拍记录
			List<CarAuctionReg> auctRegs=carAuctionRegService.selectList(new EntityWrapper<CarAuctionReg>().eq("auction_id", priceID).eq("reg_tel", telephone));
			if(auctRegs==null||auctRegs.size()!=1) {
				logger.error("参数为空,auction_id="+priceID+",reg_tel"+telephone);
				return Result.error("9999", "无效的拍卖登记信息"); 
			}
			CarAuctionReg auctReg=auctRegs.get(0);
			//未交保证金不参与竞拍
			if(!auctReg.getPayDeposit()) {
				logger.error("未缴纳保证金,auction_id="+priceID+",reg_tel"+telephone);
				return Result.error("9999", "未缴纳保证金不允许竞拍"); 
			}
			CarAuctionPriceLog pLog=new CarAuctionPriceLog();
			pLog.setId(UUID.randomUUID().toString());
			pLog.setAuctionId(auctReg.getAuctionId());
			pLog.setBidderTel(auctReg.getRegTel());
			pLog.setPrice(amount+"");
			pLog.setCreateTime(new Date());
			pLog.setBidderCertId(userId);
			carAuctionPriceLogService.insert(pLog);
			auctReg.setOfferAmount(new BigDecimal(amount));
			auctReg.setUpdateTime(new Date());
			carAuctionRegService.updateById(auctReg);
			return Result.error("0000", "竞拍成功"); 
		}catch (Exception e) {
			logger.error(e.getMessage());
			return Result.error("9999", "系统异常"); 
		}
	}
	@ApiOperation(value="拍卖报名")
	@PostMapping("/auctionSign")
	public Result<Object>auctionSign(@RequestBody Map<String,Object> params){
	try {
		String priceID=(String) params.get("priceID");
		String userName=(String) params.get("userName");
		String userID=(String) params.get("userId");  
		String telephone=(String) params.get("telePhone");
		String bank =(String) params.get("bank");
		String cardNo=(String) params.get("carNO");
		//Boolean isPayBood=(Boolean) params.get("isPayBood");
		if(StringUtils.isEmpty(priceID)||StringUtils.isEmpty(userName)
			||StringUtils.isEmpty(userID)||StringUtils.isEmpty(telephone)
			||StringUtils.isEmpty(bank)||StringUtils.isEmpty(cardNo)
			) {
			logger.error("参数为空,auction_id="+priceID+",userName="+userName+
					",userID="+userID+",telephone="+telephone+",bank="+bank
					+",cardNo="+cardNo);
			return Result.error("9999", "参数为空"); 
		}
		List<CarAuction> auctions=carAuctionService.selectList( new EntityWrapper<CarAuction>().eq("auction_id", priceID).eq("status", "04"));
		if(auctions==null||auctions.size()!=1) {
			logger.error("无效的拍卖,auction_id="+priceID);
			return Result.error("9999", "无效的拍卖"); 
		}
		CarAuction carAuction=auctions.get(0);
		if(carAuction.getAuctionStartTime()==null||carAuction.getAuctionEndTime()==null) {
			logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			return Result.error("9999", "无效的拍卖"); 
		}
		//判断当前是否还在拍卖时间
		long startTime=carAuction.getAuctionStartTime().getTime();
		//long endtTime=carAuction.getAuctionEndTime().getTime();
		long currentTime=new Date().getTime();
		if(currentTime>startTime) {
			logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			return Result.error("9999", "该拍卖正在进行中或已完成不允许报名"); 
		}
		List<CarAuctionReg> rCarAuctionRegs=carAuctionRegService.selectList(new EntityWrapper<CarAuctionReg>().eq("auction_id", priceID).eq("reg_tel", telephone));
		if(rCarAuctionRegs!=null&&rCarAuctionRegs.size()>0) {
			logger.error("已报名登记，不允许重复登记,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			return Result.error("9999", "该拍卖正在进行中或已完成不允许报名"); 
		}
		CarAuctionReg auctReg=new CarAuctionReg();
		CarAuctionBidder rBidder=carAuctionBidderService.selectById(userID);
		
		if(rBidder==null) {
			rBidder=new CarAuctionBidder();
			rBidder.setCreateTime(new Date());
			rBidder.setCreateUser("");
		}
		rBidder.setBidderCertId(userID);
		rBidder.setBidderName(userName);
		rBidder.setBidderTel(telephone);
		
		rBidder.setTransAccountName(userName);
		rBidder.setTransAccountNum(cardNo);
		rBidder.setTransBank(bank);
		rBidder.setUpdateTime(new Date());
		
		auctReg.setAuctionId(priceID);
		auctReg.setBusinessId(carAuction.getBusinessId());
		auctReg.setCreateTime(new Date());
		auctReg.setCreateUser(userName);
		auctReg.setRegCertId(userID);
		//auctReg.setPayDeposit(isPayBood);
		auctReg.setRegId(UUID.randomUUID().toString());
		auctReg.setRegTel(telephone);
		auctReg.setUpdateTime(new Date());
		carAuctionRegService.insert(auctReg);
		carAuctionBidderService.insertOrUpdate(rBidder);
		return Result.error("0000", "报名成功"); 
	}catch (Exception e) {
		logger.error(e.getMessage());
		return Result.error("9999", "报名失败"); 
	}
	}
	@ApiOperation(value = "分页查询竞拍记录")
    @PostMapping("/selectAuctionReg")
    public PageResult<List<AuctionRespVo>> selectAuctionReg (@RequestBody AuctionsReqVo req) {
			 
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
