package com.hongte.alms.base.assets.car.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.hongte.alms.base.assets.car.vo.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.enums.CarStatusEnums;
import com.hongte.alms.base.assets.car.mapper.CarMapper;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.baseException.AlmsBaseExcepiton;
import com.hongte.alms.base.entity.CarAuction;
import com.hongte.alms.base.entity.CarAuctionBidder;
import com.hongte.alms.base.entity.CarAuctionReg;
import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.base.entity.CarDetection;
import com.hongte.alms.base.entity.CarReturnReg;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.entity.MsgTemplate;
import com.hongte.alms.base.enums.AuctionStatusEnums;
import com.hongte.alms.base.enums.MsgTemplateEnableEnum;
import com.hongte.alms.base.enums.MsgTemplateTypeEnum;
import com.hongte.alms.base.enums.SmsTypeEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.CarAuctionBidderMapper;
import com.hongte.alms.base.mapper.CarAuctionMapper;
import com.hongte.alms.base.mapper.CarAuctionRegMapper;
import com.hongte.alms.base.mapper.CarBasicMapper;
import com.hongte.alms.base.mapper.CarDetectionMapper;
import com.hongte.alms.base.mapper.CarReturnRegMapper;
import com.hongte.alms.base.mapper.DocMapper;
import com.hongte.alms.base.mapper.DocTmpMapper;
import com.hongte.alms.base.mapper.DocTypeMapper;
import com.hongte.alms.base.mapper.MsgTemplateMapper;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.vo.comm.SmsVo;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.core.Result;

@Service("CarService")
@Transactional(rollbackFor = Exception.class)
public class CarServiceImpl  implements CarService {
    private Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);
	@Autowired
	private CarMapper carMapper;
	
	@Autowired
	private DocMapper docMapper;
	
	@Autowired
	private DocTypeMapper docTypeMapper;

	
	@Autowired
	private CarAuctionMapper carAuctionMapper;
	
    @Autowired
    @Qualifier("ProcessService")
    ProcessService processService;
    
    @Autowired
    private DocTmpMapper docTmpMapper;

    @Autowired
    private CarDetectionMapper carDetectionMapper;
	@Autowired
	private CarAuctionRegMapper carAuctionRegMapper;
	@Autowired
	private CarBasicMapper carBasicMapper;
	
   @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;
   
   @Autowired
   private CarAuctionBidderMapper carAuctionBidderMapper;
   
   @Autowired
   private EipRemote eipRemote;
   
   @Autowired
   private MsgTemplateMapper msgTemplateMapper;
   
   @Autowired
   private CarReturnRegMapper carReturnRegMapper;

	@Override
	public Page<CarVo> selectCarPage(CarReq carReq) {
			Page<CarVo> pages = new Page<CarVo>();
			String userId = loginUserInfoHelper.getUserId();
			carReq.setUserId(userId);
		   int count=carMapper.selectCarPageCount(carReq);
		   if(count<=0) {
			   return pages;
		   }
		   List<CarVo> list=carMapper.selectCarPage(carReq);
		   pages.setTotal(count);
		   pages.setRecords(list);
		return pages;
	}

	@Override
	public List<CarVo> selectCarList(CarReq carReq){
		String userId = loginUserInfoHelper.getUserId();
		carReq.setUserId(userId);
		  List<CarVo> list=carMapper.selectCarPage(carReq);
		  return list;
	}
	
	@Override
	public Page<AuctionRespVo> selectAuctionsPageForApp(AuctionsReq req){
		Page<AuctionRespVo> pages = new Page<AuctionRespVo>();

		int count=carMapper.selectAuctionsCountForApp(req);
		  if(count<=0) {
			   return pages;
		   }
		List<AuctionRespVo> list=carMapper.selectAuctionsPageForApp(req);
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
			List<AuctionDocVo> auctionDocVoList = new ArrayList<>();
			for (Doc doc : docs) {
				AuctionDocVo auctionDocVo = new AuctionDocVo();
				auctionDocVo.setDocUrl(doc.getDocUrl());
				auctionDocVoList.add(auctionDocVo);
			}
			res.setDocs(auctionDocVoList);
		}
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;
	}

	@Override
	public Page<MyBadeCarVo> selectMyBidCarsForApp(MyBadeCarReq req) {
		Page<MyBadeCarVo> pages = new Page<>();
		pages.setCurrent(req.getPage());
		pages.setSize(req.getLimit());
		List<DocType> docTypes = docTypeMapper.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_CarAuction"));
		if (docTypes == null || docTypes.size() != 1) {
			logger.error("文件类型不存在或存在多条记录,docTypeCode=AfterLoan_Material_CarAuction");
			throw new RuntimeException("文件类型不存在或存在多条记录");
		}
		DocType docType = docTypes.get(0);
		List<MyBadeCarVo> list = carMapper.selectMyBidCarsForApp(pages, req);
		for (MyBadeCarVo myBadeCarVo : list) {
			List<Doc> docs = docMapper.selectPage(new RowBounds(0, 3), new EntityWrapper<Doc>().eq("doc_type_id", docType.getDocTypeId()).eq("doc_attr", "01").eq("business_id", myBadeCarVo.getBusinessId()).orderBy("create_time", true));
			List<AuctionDocVo> auctionDocVoList = new ArrayList<>();
			for (Doc doc : docs) {
				AuctionDocVo auctionDocVo = new AuctionDocVo();
				auctionDocVo.setDocUrl(doc.getDocUrl());
				auctionDocVoList.add(auctionDocVo);
			}
			myBadeCarVo.setDocs(auctionDocVoList);
		}
		pages.setRecords(list);
		return pages;
	}

	@Override
	public Map<String,BigDecimal> selectMaxOfferPriceByAuctionId(String auctionId){
		return carMapper.selectMaxOfferPriceByAuctionId(auctionId);
	}

	@Override
	public Page<AuctionRespVo> selectAuctionsRegPageForApp(AuctionsReq vo){
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
			List<Doc> docs = docMapper.selectPage(new RowBounds(0, 3), new EntityWrapper<Doc>().eq("doc_type_id", docType.getDocTypeId()).eq("doc_attr", "01").eq("business_id", res.getBusinessId()).orderBy("create_time", true));
			List<AuctionDocVo> auctionDocVoList = new ArrayList<>();
			for (Doc doc : docs) {
				AuctionDocVo auctionDocVo = new AuctionDocVo();
				auctionDocVo.setDocUrl(doc.getDocUrl());
				auctionDocVoList.add(auctionDocVo);
			}
			res.setDocs(auctionDocVoList);
		}
		pages.setTotal(count);
		pages.setRecords(list);
		return pages;
	}
	@Override
	public String auctionAply(AuctionAplyVo auctionAplyVo) {
		
		if(auctionAplyVo==null||auctionAplyVo.getCarAuction()==null||auctionAplyVo.getCarBase()==null
			||auctionAplyVo.getCarDetection()==null) {
			logger.error("参数为空");
			throw new AlmsBaseExcepiton("参数为空");
		}
	 	CarBasic retCarBasic=carBasicMapper.selectById(auctionAplyVo.getCarAuction().getBusinessId());
	 	if(retCarBasic==null) {
      		logger.error("该业务记录不存在");
      		throw new AlmsBaseExcepiton("无效业务");
      	}
		if(!CarStatusEnums.PENDING.getStatusCode().equals(retCarBasic.getStatus())) {
      		logger.error("非待处置状态的车辆不能进行拍卖");
      		throw new AlmsBaseExcepiton("非待处置状态的车辆不能进行拍卖");
      	}
		auctionAplyVo.getCarBase().setBusinessId(retCarBasic.getBusinessId());
		CarDetection carDetection=carDetectionMapper.selectById(auctionAplyVo.getCarDetection().getId());
		if(carDetection==null) {
			logger.error("该车评估记录不存在");
      		throw new AlmsBaseExcepiton("该车评估记录不存在");
		}
		//更新评估信息
		carDetection.setTrafficViolationFee(auctionAplyVo.getCarDetection().getTrafficViolationFee());
		carDetection.setAnnualVerificationExpirationDate(auctionAplyVo.getCarDetection().getAnnualVerificationExpirationDate());
		carDetection.setOdometer(auctionAplyVo.getCarDetection().getOdometer());
		carDetection.setFuelLeft(auctionAplyVo.getCarDetection().getFuelLeft());
		carDetection.setEvaluationAmount(auctionAplyVo.getCarDetection().getEvaluationAmount());
		carDetection.setUserNature(auctionAplyVo.getCarDetection().getUserNature());
		carDetection.setToolWithCar(auctionAplyVo.getCarDetection().getToolWithCar());
		carDetection.setRelatedDocs(auctionAplyVo.getCarDetection().getRelatedDocs());
		carDetection.setTransactionMode(auctionAplyVo.getCarDetection().getTransactionMode());
		carDetection.setRemark(auctionAplyVo.getCarDetection().getRemark());
		carDetection.setCarPosition(auctionAplyVo.getCarDetection().getCarPosition());
		carDetection.setTrafficViolationSituation(auctionAplyVo.getCarDetection().getTrafficViolationSituation());
		carDetection.setUpdateTime(new Date());
		carDetection.setUpdateUser(loginUserInfoHelper.getUserId());
		carDetectionMapper.updateById(carDetection);
		//拍卖记录
      	List<CarAuction> carAuctions=carAuctionMapper.selectList(new EntityWrapper<CarAuction>().eq("business_id", auctionAplyVo.getCarAuction().getBusinessId()));
      	if(carAuctions!=null&&carAuctions.size()>0) {
      		for(CarAuction carAuc:carAuctions) {
      			if(AuctionStatusEnums.AUDIT.getKey().equals(carAuc.getStatus())) {
      				logger.error("该申请已提交审核，business_id="+auctionAplyVo.getCarAuction().getBusinessId());
      				throw new AlmsBaseExcepiton("该申请已提交审核,请勿重复提交");
      			}else {
      				auctionAplyVo.getCarAuction().setAuctionId(carAuc.getAuctionId());
      				
      				
      			}
      		}
      	}else {
      		if(StringUtils.isEmpty(auctionAplyVo.getCarAuction().getAuctionId())) {
      			auctionAplyVo.getCarAuction().setAuctionId(UUID.randomUUID().toString());
      			auctionAplyVo.getCarAuction().setCreateTime(new Date());
      			auctionAplyVo.getCarAuction().setCreateUser(loginUserInfoHelper.getUserId());
      			auctionAplyVo.getCarAuction().setBidderCount(new Random().nextInt(451)+50);
      		}
      	}
      	ProcessSaveReq processSaveReq=new ProcessSaveReq();
      	processSaveReq.setProcessId(auctionAplyVo.getProcessId());
      	if("audit".equals(auctionAplyVo.getSubmitType())) {
      		auctionAplyVo.getCarAuction().setStatus(AuctionStatusEnums.AUDIT.getKey());
      	
      		//创建审核流程
      		processSaveReq.setProcessStatus(ProcessStatusEnums.RUNNING.getKey());
      		auctionAplyVo.getCarBase().setStatus(CarStatusEnums.AUCTION_AUDIT.getStatusCode());
      		
      	}else {
      		processSaveReq.setProcessStatus(ProcessStatusEnums.NEW.getKey());
      		auctionAplyVo.getCarAuction().setStatus(AuctionStatusEnums.DRAFT.getKey());
      		
      	}
      	CarAuction rCarAuction=carAuctionMapper.selectById(auctionAplyVo.getCarAuction().getAuctionId());
      	if(rCarAuction==null) {
      		carAuctionMapper.insert(auctionAplyVo.getCarAuction());
      	}else {
      		rCarAuction.setStartingPrice(auctionAplyVo.getCarAuction().getStartingPrice());
      		rCarAuction.setAuctionStartTime(auctionAplyVo.getCarAuction().getAuctionStartTime());
			rCarAuction.setAuctionEndTime(auctionAplyVo.getCarAuction().getAuctionEndTime());
			rCarAuction.setConsultant(auctionAplyVo.getCarAuction().getConsultant());
			rCarAuction.setConsultantTel(auctionAplyVo.getCarAuction().getConsultantTel());
			rCarAuction.setConsultantEmail(auctionAplyVo.getCarAuction().getConsultantEmail());
			rCarAuction.setPaymentMethod(auctionAplyVo.getCarAuction().getPaymentMethod());
			rCarAuction.setAuctionPosition(auctionAplyVo.getCarAuction().getAuctionPosition());
			rCarAuction.setRemark(auctionAplyVo.getCarAuction().getRemark());
			rCarAuction.setStatus("audit".equals(auctionAplyVo.getSubmitType())?AuctionStatusEnums.AUDIT.getKey():rCarAuction.getStatus());
			carAuctionMapper.updateById(auctionAplyVo.getCarAuction());
      	}
      	processSaveReq.setTitle(ProcessTypeEnums.Aply_CarAuction.getName());
      	processSaveReq.setBusinessId(auctionAplyVo.getCarAuction().getBusinessId());
      	com.hongte.alms.base.process.entity.Process process=processService.saveProcess(processSaveReq,ProcessTypeEnums.Aply_CarAuction);
      	
      	carBasicMapper.updateById(auctionAplyVo.getCarBase());
      	if(auctionAplyVo.getFiles()!=null&&auctionAplyVo.getFiles().size()>0) {
      		for(FileVo file:auctionAplyVo.getFiles()) {
      			DocTmp tmp=docTmpMapper.selectById(file.getOldDocId());//将临时表保存的上传信息保存到主表中
      			if(tmp!=null) {
      				Doc doc=new Doc();
      				BeanUtils.copyProperties(tmp, doc);
      				doc.setOriginalName(file.getOriginalName());
      				Doc rDoc=docMapper.selectById(doc.getDocId());
      				if(rDoc==null) {
      				docMapper.insert(doc);
      				}else {
      					docMapper.updateById(doc);
      				}
      			}
      			
      		}
      	}
      	return process.getProcessId();
	}

	@Override
	public void auctionAudit(AuctionAplyVo auctionAplyVo) throws Exception {
		if(auctionAplyVo==null||auctionAplyVo.getCarAuction()==null
			||auctionAplyVo.getAuditVo()==null||auctionAplyVo.getCarDetection()==null) {
			logger.error("参数为空");
			throw new AlmsBaseExcepiton("参数为空");
		}
		CarBasic retCarBasic=carBasicMapper.selectById(auctionAplyVo.getCarAuction().getBusinessId());
      	
      	if(retCarBasic==null) {
      		logger.error("该业务记录不存在");
      		throw new AlmsBaseExcepiton( "无效业务");
      	}
		CarDetection carDetection=carDetectionMapper.selectById(auctionAplyVo.getCarDetection().getId());
		if(carDetection==null) {
			logger.error("该车评估记录不存在");
      		throw new AlmsBaseExcepiton("该车评估记录不存在");
		}
		//更新评估信息
		carDetection.setTrafficViolationFee(auctionAplyVo.getCarDetection().getTrafficViolationFee());
		carDetection.setAnnualVerificationExpirationDate(auctionAplyVo.getCarDetection().getAnnualVerificationExpirationDate());
		carDetection.setOdometer(auctionAplyVo.getCarDetection().getOdometer());
		carDetection.setFuelLeft(auctionAplyVo.getCarDetection().getFuelLeft());
		carDetection.setEvaluationAmount(auctionAplyVo.getCarDetection().getEvaluationAmount());
		carDetection.setUserNature(auctionAplyVo.getCarDetection().getUserNature());
		carDetection.setToolWithCar(auctionAplyVo.getCarDetection().getToolWithCar());
		carDetection.setRelatedDocs(auctionAplyVo.getCarDetection().getRelatedDocs());
		carDetection.setTransactionMode(auctionAplyVo.getCarDetection().getTransactionMode());
		carDetection.setRemark(auctionAplyVo.getCarDetection().getRemark());
		carDetection.setCarPosition(auctionAplyVo.getCarDetection().getCarPosition());
		carDetection.setTrafficViolationSituation(auctionAplyVo.getCarDetection().getTrafficViolationSituation());
		carDetection.setUpdateTime(new Date());
		carDetection.setUpdateUser(loginUserInfoHelper.getUserId());
		carDetectionMapper.updateById(carDetection);
		
      	com.hongte.alms.base.process.entity.Process p=processService.selectById(auctionAplyVo.getAuditVo().getProcessId());
      	if(p==null) {
      		logger.error("未创建流程，ProcessId="+auctionAplyVo.getAuditVo().getProcessId());
      		throw new AlmsBaseExcepiton("未创建流程");
      	}
//      	if(CarStatusEnums.PENDING.getStatusCode().equals(retCarBasic.getStatus())&&p.getCurrentStep()!=-1) {//不为草稿状态
//      		logger.error("非待处置状态的车辆不能进行拍卖");
//      		throw new AlmsBaseExcepiton("非待处置状态的车辆不能进行拍卖");
//      	}
      	auctionAplyVo.getCarBase().setBusinessId(retCarBasic.getBusinessId());
     
      	List<CarAuction> carAuctions=carAuctionMapper.selectList(new EntityWrapper<CarAuction>().eq("business_id", auctionAplyVo.getCarAuction().getBusinessId()).ne("status", AuctionStatusEnums.DRAFT.getKey()));
      	if(carAuctions!=null&&carAuctions.size()>0) {
      		for(CarAuction carAuc:carAuctions) {
      			if(AuctionStatusEnums.AUDIT.getKey().equals(carAuc.getStatus())) {
//      				logger.error("该申请已提交审核，business_id="+carAuction.getBusinessId());
//      	    		return Result.error("9999", "该申请已提交审核,请勿重复提交");
      			}else {
      				auctionAplyVo.getCarAuction().setAuctionId(carAuc.getAuctionId());
      				
      				
      			}
      		}
      	}else {
      		if(StringUtils.isEmpty(auctionAplyVo.getCarAuction().getAuctionId())) {
      			auctionAplyVo.getCarAuction().setAuctionId(UUID.randomUUID().toString());
      			auctionAplyVo.getCarAuction().setCreateTime(new Date());
      			auctionAplyVo.getCarAuction().setCreateUser("admin");
      		}
      	}
      	if(auctionAplyVo.getFiles()!=null&&auctionAplyVo.getFiles().size()>0) {
      		for(FileVo file:auctionAplyVo.getFiles()) {
      			DocTmp tmp=docTmpMapper.selectById(file.getOldDocId());//将临时表保存的上传信息保存到主表中
      			if(tmp!=null) {
      				Doc doc=new Doc();
      				BeanUtils.copyProperties(tmp, doc);
      				doc.setOriginalName(file.getOriginalName());
      				Doc rDoc=docMapper.selectById(doc.getDocId());
      				if(rDoc==null) {
      				docMapper.insert(doc);
      				}else {
      					docMapper.updateById(doc);
      				}
      			}
      			
      		}
      	}
      	processService.saveProcessApprovalResult(auctionAplyVo.getAuditVo(), ProcessTypeEnums.Aply_CarAuction);
      	
      	
      	if(ProcessTypeEnums.Aply_CarAuction.getEndStep().equals(p.getCurrentStep())&&Integer.valueOf(ProcessApproveResult.PASS.getKey()).equals(auctionAplyVo.getAuditVo().getIsPass())) {
      		auctionAplyVo.getCarBase().setStatus(CarStatusEnums.AUCTION.getStatusCode());
      		auctionAplyVo.getCarAuction().setStatus(AuctionStatusEnums.AUDITED.getKey());
      	}else {
      		auctionAplyVo.getCarBase().setStatus(CarStatusEnums.AUCTION_AUDIT.getStatusCode());
      		auctionAplyVo.getCarAuction().setStatus(AuctionStatusEnums.AUDIT.getKey());
      	}
      		auctionAplyVo.getCarBase().setUpdateTime(new Date());
      		carAuctionMapper.updateById(auctionAplyVo.getCarAuction());
      		carBasicMapper.updateById(auctionAplyVo.getCarBase());
		
	}

	@Override
	public void againAssess(AuctionAplyVo auctionAplyVo) {
		if(auctionAplyVo==null||auctionAplyVo.getCarBase()==null
				||auctionAplyVo.getCarDetection()==null) {
				logger.error("参数为空");
				throw new AlmsBaseExcepiton("参数为空");
			}
    	CarBasic rCarBasic=carBasicMapper.selectById(auctionAplyVo.getCarBase().getBusinessId());
    	if(rCarBasic==null) {
    		logger.error("车辆信息不存在,businessId="+auctionAplyVo.getCarBase().getBusinessId());
    		throw new AlmsBaseExcepiton("非待处置状态的车辆不能重新评估");
    	}
      	if(!CarStatusEnums.PENDING.getStatusCode().equals(rCarBasic.getStatus())) {
      		logger.error("非待处置状态的车辆不能重新评估,businessId="+auctionAplyVo.getCarBase().getBusinessId());
      		throw new AlmsBaseExcepiton("非待处置状态的车辆不能重新评估");
      	}
    	CarDetection rCarDetection=carDetectionMapper.selectById(auctionAplyVo.getCarDetection().getId());
    	CarDetection reqCarDetection=auctionAplyVo.getCarDetection();
    	String uuId=UUID.randomUUID().toString();
    	rCarDetection.setId(uuId);
    	rCarDetection.setTrafficViolationSituation(reqCarDetection.getTrafficViolationSituation());
    	rCarDetection.setTrafficViolationFee(reqCarDetection.getTrafficViolationFee());
    	rCarDetection.setVehicleVesselTax(reqCarDetection.getVehicleVesselTax());
    	rCarDetection.setAnnualTicketFee(reqCarDetection.getAnnualTicketFee());
    	rCarDetection.setDrivingLicenseConsistent(reqCarDetection.getDrivingLicenseConsistent());
    	rCarDetection.setDrivingLicenseInconsistentDescription(reqCarDetection.getDrivingLicenseInconsistentDescription());
    	rCarDetection.setFuelType(reqCarDetection.getFuelType());
    	rCarDetection.setVehicleLicenseRegistrationDate(reqCarDetection.getVehicleLicenseRegistrationDate());
    	rCarDetection.setAnnualVerificationExpirationDate(reqCarDetection.getAnnualVerificationExpirationDate());
    	rCarDetection.setMortgage(reqCarDetection.getMortgage());
    	rCarDetection.setOdometer(reqCarDetection.getOdometer());
    	rCarDetection.setFuelLeft(reqCarDetection.getFuelLeft());
    	rCarDetection.setCenterPanelNormal(reqCarDetection.getCenterPanelNormal());
    	rCarDetection.setCenterPanelAbnormalDescription(reqCarDetection.getCenterPanelAbnormalDescription());
    	rCarDetection.setVentilatorNormal(reqCarDetection.getVentilatorNormal());
    	rCarDetection.setVentilatorAbnormalDescription(reqCarDetection.getVentilatorAbnormalDescription());
    	rCarDetection.setInteriorNormal(reqCarDetection.getInteriorNormal());
    	rCarDetection.setInteriorAbnormalDescription(reqCarDetection.getInteriorAbnormalDescription());
    	rCarDetection.setWindowGlassNormal(reqCarDetection.getWindowGlassNormal());
    	rCarDetection.setWindowGlassAbnormalDescription(reqCarDetection.getWindowGlassAbnormalDescription());
    	rCarDetection.setRadiatorNormal(reqCarDetection.getRadiatorNormal());
    	rCarDetection.setRadiatorAbnormalDescription(reqCarDetection.getRadiatorAbnormalDescription());
    	rCarDetection.setEngineNormal(reqCarDetection.getEngineNormal());
    	rCarDetection.setEngineAbnormalDescription(reqCarDetection.getEngineAbnormalDescription());
    	rCarDetection.setFrameNormal(reqCarDetection.getFrameNormal());
    	rCarDetection.setFrameAbnormalDescription(reqCarDetection.getFrameAbnormalDescription());
    	rCarDetection.setTireNormal(reqCarDetection.getTireNormal());
    	rCarDetection.setTireAbnormalDescription(reqCarDetection.getTireAbnormalDescription());
    	rCarDetection.setSpareTireNormal(reqCarDetection.getSpareTireNormal());
    	rCarDetection.setSpareTireAbnormalDescrioption(reqCarDetection.getSpareTireAbnormalDescrioption());
    	rCarDetection.setDoorNormal(reqCarDetection.getDoorNormal());
    	rCarDetection.setDoorAbnormalDescription(reqCarDetection.getDoorAbnormalDescription());
    	rCarDetection.setGearBox(reqCarDetection.getGearBox());
    	rCarDetection.setEdition(reqCarDetection.getEdition());
    	rCarDetection.setEvaluationAmount(reqCarDetection.getEvaluationAmount());
    	rCarDetection.setTransferFee(reqCarDetection.getTransferFee());
    	rCarDetection.setAccelerationPerformanceNormal(reqCarDetection.getAccelerationPerformanceNormal());
    	rCarDetection.setAccelerationPerformanceAbnormalDescription(reqCarDetection.getAccelerationPerformanceAbnormalDescription());
    	rCarDetection.setBrakingPerformanceNormal(reqCarDetection.getBrakingPerformanceNormal());
    	rCarDetection.setBrakingPerformanceAbnormalDescription(reqCarDetection.getBrakingBalancePerformanceAbnormalDescription());
    	rCarDetection.setBrakingBalancePerformanceNormal(reqCarDetection.getBrakingBalancePerformanceNormal());
    	rCarDetection.setBrakingBalancePerformanceAbnormalDescription(reqCarDetection.getBrakingBalancePerformanceAbnormalDescription());
    	rCarDetection.setSteerPerformanceNormal(reqCarDetection.getSteerPerformanceNormal());
    	rCarDetection.setSteerPerformanceAbnormalDescription(reqCarDetection.getSteerPerformanceAbnormalDescription());
    	rCarDetection.setGearPerformanceNormal(reqCarDetection.getGearPerformanceNormal());
    	rCarDetection.setGearPerformanceAbnormalDescription(reqCarDetection.getGearPerformanceAbnormalDescription());
    	rCarDetection.setOtherDriveDescription(reqCarDetection.getOtherDriveDescription());
    	rCarDetection.setVinConsistent(reqCarDetection.getVinConsistent());
    	rCarDetection.setEngineModelConsistent(reqCarDetection.getEngineModelConsistent());
    	rCarDetection.setAccident(reqCarDetection.getAccident());
    	rCarDetection.setOtherTrouble(reqCarDetection.getOtherTrouble());
    	rCarDetection.setOtherTroubleDescription(reqCarDetection.getOtherTroubleDescription());
    	rCarDetection.setOrigin(false);
    	rCarDetection.setCreateTime(new Date());
    	rCarDetection.setCreateUser(loginUserInfoHelper.getUserId());
    	carDetectionMapper.insert(rCarDetection);
    	rCarBasic.setLastDetectionId(uuId);
    	rCarBasic.setUpdateTime(new Date());
    	rCarBasic.setUpdateUser(loginUserInfoHelper.getUserId());
    	carBasicMapper.updateById(rCarBasic);
	}
	@Override
	public void auctionSign(Map<String, Object> params) {
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
			throw new AlmsBaseExcepiton( "参数为空"); 
		}
		List<CarAuction> auctions=carAuctionMapper.selectList( new EntityWrapper<CarAuction>().eq("auction_id", priceID).eq("status", "04"));
		if(auctions==null||auctions.size()!=1) {
			logger.error("无效的拍卖,auction_id="+priceID);
			throw new AlmsBaseExcepiton( "无效的拍卖"); 
		}
		CarAuction carAuction=auctions.get(0);
		if(carAuction.getAuctionStartTime()==null||carAuction.getAuctionEndTime()==null) {
			logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			throw new AlmsBaseExcepiton("无效的拍卖"); 
		}
		//判断当前是否还在拍卖时间
		long startTime=carAuction.getBuyStartTime().getTime();
		//long endtTime=carAuction.getAuctionEndTime().getTime();
		long currentTime=new Date().getTime();
		if(currentTime>startTime) {
			logger.error("不在竞拍时间内,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			throw new AlmsBaseExcepiton( "该拍卖正在进行中或已完成不允许报名"); 
		}
		List<CarAuctionReg> rCarAuctionRegs=carAuctionRegMapper.selectList(new EntityWrapper<CarAuctionReg>().eq("auction_id", priceID).eq("reg_tel", telephone));
		if(rCarAuctionRegs!=null&&rCarAuctionRegs.size()>0) {
			logger.error("已报名登记，不允许重复登记,auctionStartTime="+carAuction.getAuctionStartTime()+",auctionEndTime"+carAuction.getAuctionEndTime());
			throw new AlmsBaseExcepiton( "该拍卖正在进行中或已完成不允许报名"); 
		}
		CarAuctionReg auctReg=new CarAuctionReg();
		CarAuctionBidder rBidder=carAuctionBidderMapper.selectById(telephone);
		
		if(rBidder==null) {
			rBidder=new CarAuctionBidder();
			rBidder.setCreateTime(new Date());
			rBidder.setCreateUser("admin");
			
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
		carAuctionRegMapper.insert(auctReg);
		rBidder.insertOrUpdate();
		//发送短信
		List<MsgTemplate> msgTemplates=msgTemplateMapper.selectList(new EntityWrapper<MsgTemplate>().eq("template_type",MsgTemplateTypeEnum.SMS.getValue()).eq("template_code", "SMS0000001"));
		if(msgTemplates==null||msgTemplates.size()!=1) {
			logger.error("报名登记短信模板不存在,template_type="+MsgTemplateTypeEnum.SMS.getValue()+",template_code=SMS0000001");
			throw new AlmsBaseExcepiton( "报名登记短信模板不存在"); 
		}
		MsgTemplate msgTemplate=msgTemplates.get(0);
		if(
			!StringUtils.isEmpty(msgTemplate.getMsg())) {
			String msg=String.format(msgTemplate.getMsg(),userName,DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", carAuction.getPaymentEndTime()),carAuction.getDeposit(),
					carAuction.getAcountName(),carAuction.getOpenBank(),carAuction.getAcountNum());
			Set<String> phones=new HashSet<String>();
			phones.add(telephone);
			SmsVo vo=new SmsVo();
			vo.setPhones(phones);
			vo.setContent(msg);
			vo.setType(SmsTypeEnum.NOTICE.getValue());
			//记录短信日志
			InfoSms sms=new InfoSms();
//			sms.setLogId(UUID.randomUUID().toString());
			sms.setOriginalBusinessId(carAuction.getBusinessId());
			sms.setPhoneNumber(telephone);
			sms.setRecipient(userName);
			sms.setSmsType("微信公众号竞拍报名登记短信通知");
			sms.setSendDate(new Date());
			sms.setContent(msg);
			sms.setServiceName("调EIP发送短信");
			sms.setCreateTime(new Date());
			sms.setCreateUser("admin");//系统触发自动发送
			try {
				if(MsgTemplateEnableEnum.ENABLE.getValue().equals(msgTemplate.getEnableFlag())) {//发送开关
					Result eipResult=eipRemote.sendSms(vo);
					logger.info("EIP返回信息,code="+eipResult.getReturnCode()+",msg="+eipResult.getMsg());
					if(eipResult==null||!"0000".equals(eipResult.getReturnCode())) {
						logger.error("调用EIP系统发送短信失败,eipResult="+eipResult==null?null:"调用EIP系统发送短信返回码："+eipResult.getReturnCode());
						throw new AlmsBaseExcepiton( "调用EIP系统发送短信失败"); 
					}

					sms.setStatus("已发送");
				}else {
					sms.setStatus("未发送");	
				}
			}catch (Exception e) {
				sms.setStatus("未发送");
				logger.error(e.getMessage());
				throw new AlmsBaseExcepiton( "调用EIP系统发送短信失败"); 
				
			}finally {
				sms.insert();//插入短信日志
			}
			
		}
	}

	@Override
	public void addReturnReg(Map<String, Object> params) {
		CarReturnReg returnReg=JsonUtil.map2obj((Map<String,Object>)params.get("returnReg"), CarReturnReg.class);
    	List<FileVo> files=JsonUtil.map2objList(params.get("returnRegFiles"), FileVo.class);
    	
    	CarBasic carBasic=carBasicMapper.selectById(returnReg.getBusinessId());
    	if(carBasic==null) {
    		logger.error("车辆信息不存在,businessId="+returnReg.getBusinessId());
    		throw new AlmsBaseExcepiton( "车辆信息不存在"); 
    	}
    	carBasic.setStatus(CarStatusEnums.RETURNED.getStatusCode());
    	carBasic.setUpdateTime(new Date());
    	carBasic.setUpdateUser(loginUserInfoHelper.getUserId());
    	carBasicMapper.updateById(carBasic);
    	
    	List<CarReturnReg> carReturnRegs=carReturnRegMapper.selectList(new EntityWrapper<CarReturnReg>().eq("business_id", returnReg.getBusinessId()).eq("drag_id", returnReg.getDragId()));
    	if(carReturnRegs!=null&&carReturnRegs.size()>0) {
    		logger.error("该车辆拖车后存在多条归还记录,businessId="+returnReg.getBusinessId()+"dragId="+returnReg.getDragId());
    		throw new AlmsBaseExcepiton( "该车辆拖车后存在多条归还记录"); 
    	}
    	//CarReturnReg carReturnReg=carReturnRegs.get(0);
    	CarReturnReg carReturnReg=new CarReturnReg();
    		carReturnReg.setReturnRegId(UUID.randomUUID().toString());
    		carReturnReg.setCreateTime(new Date());
    		carReturnReg.setCreateUser(loginUserInfoHelper.getUserId());
    	
  		BeanUtils.copyProperties(returnReg, carReturnReg);
  		carReturnReg.insertOrUpdate();
    	if(files!=null&&files.size()>0) {
    		for(FileVo file:files) {
    			
    			DocTmp tmp=docTmpMapper.selectById(file.getOldDocId());//将临时表保存的上传信息保存到主表中
    			if(tmp!=null) {
    				Doc doc=new Doc();
    				BeanUtils.copyProperties(tmp, doc);
    				doc.setOriginalName(file.getOriginalName());
    				doc.insertOrUpdate();
    			}
    			
    		}
    	}
		//
	}

	@Override
	public Page<BusinessBidsVo> selectBusinessBids(BusinessBidsReq req) {
		Page<BusinessBidsVo> pages = new Page<>();
		pages.setCurrent(req.getPage());
		pages.setSize(req.getLimit());
		List<BusinessBidsVo> list = carMapper.selectBusinessBids(pages, req);
		pages.setRecords(list);
		return pages;
	}
}
