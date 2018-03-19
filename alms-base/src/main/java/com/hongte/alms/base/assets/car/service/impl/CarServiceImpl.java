package com.hongte.alms.base.assets.car.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.mapper.CarMapper;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.assets.car.vo.AuctionBidderVo;
import com.hongte.alms.base.assets.car.vo.AuctionRespVo;
import com.hongte.alms.base.assets.car.vo.AuctionsReqVo;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.mapper.DocMapper;
import com.hongte.alms.base.mapper.DocTypeMapper;
import com.hongte.alms.common.util.DateUtil;

@Service("CarService")
public class CarServiceImpl  implements CarService {
    private Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);
	@Autowired
	private CarMapper carMapper;
	
	@Autowired
	private DocMapper docMapper;
	
	@Autowired
	private DocTypeMapper docTypeMapper;

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
	
	public Page<AuctionRespVo> selectAuctionsPageForApp( AuctionsReqVo vo){
		Page<AuctionRespVo> pages = new Page<AuctionRespVo>();
		if(vo.getCurrentDate()==null) {
			vo.setCurrentDate(new Date());
		}
		int count=carMapper.selectAuctionsCountForApp(vo);
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionRespVo> list=carMapper.selectAuctionsPageForApp(vo);
		if(list==null||list.size()<=0) {
			return pages;
		}
		List<DocType> docTypes=docTypeMapper.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_CarAuction"));
		if(docTypes==null||docTypes.size()!=1) {
			logger.error("文件类型不存在或存在多条记录,docTypeCode=AfterLoan_Material_CarAuction");
			new RuntimeException("文件类型不存在或存在多条记录");
		}
		DocType docType=docTypes.get(0);
		for(AuctionRespVo res:list) {
		List<Doc> docs=docMapper.selectPage(new RowBounds(0,3), new EntityWrapper<Doc>().eq("doc_type_id", docType.getDocTypeId()).eq("doc_attr", "01").eq("business_id", res.getBusinessId()).orderBy("create_time", true));
		res.setDocs(docs);
		}
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;
	}
	public Page<AuctionBidderVo> selectBiddersPageForApp( AuctionsReqVo vo){
		Page<AuctionBidderVo>  pages=new Page<AuctionBidderVo> ();
		int count=carMapper.selectBiddersCountForApp( vo);
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionBidderVo> list=carMapper.selectBiddersPageForApp(vo);
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;		
	}
	public Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(String auctionId){
		return carMapper.selectMaxOfferPriceByAuctionId(auctionId);
	}
	public Page<AuctionRespVo> selectAuctionsRegPageForApp( AuctionsReqVo vo){
		Page<AuctionRespVo> pages = new Page<AuctionRespVo>();
		int count=carMapper.selectAuctionsRegCountForApp(vo);
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionRespVo> list=carMapper.selectAuctionsRegPageForApp(vo);
		if(list==null||list.size()<=0) {
			return pages;
		}
		List<DocType> docTypes=docTypeMapper.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_CarAuction"));
		if(docTypes==null||docTypes.size()!=1) {
			logger.error("文件类型不存在或存在多条记录,docTypeCode=AfterLoan_Material_CarAuction");
			new RuntimeException("文件类型不存在或存在多条记录");
		}
		DocType docType=docTypes.get(0);
		for(AuctionRespVo res:list) {
		List<Doc> docs=docMapper.selectPage(new RowBounds(0,3), new EntityWrapper<Doc>().eq("doc_type_id", docType.getDocTypeId()).eq("doc_attr", "01").eq("business_id", res.getBusinessId()).orderBy("create_time", true));
		res.setDocs(docs);
		}
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;
	}
}
