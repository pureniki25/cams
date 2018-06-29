package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.BizCustomerTypeEnum;
import com.hongte.alms.base.enums.BooleanEnum;
import com.hongte.alms.base.enums.BusinessSourceTypeEnum;
import com.hongte.alms.base.enums.MsgCodeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.MsgRemote;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.MsgRequestDto;
import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;
import com.hongte.alms.base.feignClient.dto.NiWoProjPlanListDetailDto;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.base.RepayPlan.req.*;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import com.hongte.alms.finance.service.NiWoRepayPlanService;
import com.ht.ussp.core.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.common.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author czs
 * @since 2018/6/13
 * 
 */
@Service("NiWoRepayPlanService")
public class NiWoRepayPlanServiceImpl implements NiWoRepayPlanService {

	private static Logger logger = LoggerFactory.getLogger(NiWoRepayPlanServiceImpl.class);

	@Autowired
	EipRemote eipRemote;

	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;

	@Autowired
	@Qualifier("RepaymentProjPlanListDetailService")
	RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService repaymentProjPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;
	
	@Autowired
	@Qualifier("SysExceptionLogService")
	SysExceptionLogService sysExceptionLogService;
	
	@Autowired
	@Qualifier("IssueSendOutsideLogService")
	IssueSendOutsideLogService issueSendOutsideLogService;
	
	@Autowired
	@Qualifier("BasicBizCustomerService")
	BasicBizCustomerService basicBizCustomerService;
	
	@Autowired
	@Qualifier("SysMsgTemplateService")
	SysMsgTemplateService sysMsgTemplateService;
	
	
	
	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	TuandaiProjectInfoService tuandaiProjectInfoService;
	
	@Autowired
    Executor executor;
	
	
	@Autowired
    MsgRemote msgRemote;
	
	
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void sycNiWoRepayPlan(String orderNo,HashMap<String,Object> niwoMap) {
		    NiWoProjPlanDto dto=new NiWoProjPlanDto();
		    //获取你我金融还款计划信息
		if(niwoMap!=null) {
			  dto=getNiWoRepayPlanByMap(niwoMap, dto);
			  orderNo=dto.getOrderNo();
		}
		if(orderNo!=null&&!orderNo.equals("")) {
			   dto=getNiWoRepayPlan(orderNo,dto);
		}
		  
		if(dto==null) {
			logger.info("获取你我金融的标的记录为空,请求编号{request_no}为："+orderNo);
		}
			
			RepaymentProjPlan projPlan = repaymentProjPlanService
					.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("request_no", orderNo).eq("plate_type", 2));
	
			if(projPlan==null) {
				logger.info("你我金融的标的请求编号在贷后找不到对应的还款计划记录,请求编号{request_no}为："+orderNo);
				return;
			}
			//Business
			List<RepaymentProjPlanList> projLists = repaymentProjPlanListService
					.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlan.getProjPlanId()));
			List<RepaymentBizPlanList> pLists = repaymentBizPlanListService
					.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", projPlan.getPlanId()));
			projPlan.setCreatSysType(3);//标志为你我金融的还款计划
			if (dto.getProjectStatus() == 2) {// 已结清
				
				 NiWoProjPlanDto dtoTemp=dto;
				List<RepaymentProjPlanListDto> repaymentProjPlanListDtos=new ArrayList();
				for (RepaymentProjPlanList projPlanList : projLists) {
					RepaymentProjPlanListDto  repaymentProjPlanListDto=new RepaymentProjPlanListDto();
					List<RepaymentProjPlanListDetail> projListDetails = repaymentProjPlanListDetailService
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
									projPlanList.getProjPlanListId()));
					//封装好同步你我金融还款计划前，贷后的还款计划镜像
					repaymentProjPlanListDto.setProjPlanListDetails(projListDetails);
					repaymentProjPlanListDto.setRepaymentProjPlanList(projPlanList);
					repaymentProjPlanListDtos.add(repaymentProjPlanListDto);
					
					for (RepaymentProjPlanListDetail detail : projListDetails) {
						for (NiWoProjPlanListDetailDto detailDto : dto.getRepaymentPlans()) {
							 if(detail.getPeriod()==detailDto.getPeriod()) {
									// 看标有没有滞纳金明细
									Integer projDetailcount = repaymentProjPlanListDetailService
											.selectCount(new EntityWrapper<RepaymentProjPlanListDetail>()
													.eq("proj_plan_list_id", projPlanList.getProjPlanListId())
													.eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()));
									if(projDetailcount==0&&detailDto.getTotalPenalty().compareTo(BigDecimal.valueOf(0))>0) {
										try {
										RepaymentProjPlanListDetail projListDetail = ClassCopyUtil.copyObject(detail,
												RepaymentProjPlanListDetail.class);
										projListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
										projListDetail.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
										projListDetail.setProjPlanAmount(detailDto.getTotalPenalty());
										projListDetail.setPlanItemType(60);
										projListDetail.setPlanDetailId(projListDetail.getPlanDetailId());
										projListDetail.setProjFactAmount(detailDto.getRepaidPenalty());
										projListDetail.setPlanItemName("线上滞纳金");
										projListDetail.setCreatSysType(3);
										repaymentProjPlanListDetailService.insertOrUpdate(projListDetail);
										}catch(Exception e) {
											logger.info("你我金融更新滞纳金错误"+e);
										}
								
										
									}
									detail.setProjFactAmount(detail.getProjPlanAmount());
									detail.setUpdateDate(new Date());
									repaymentProjPlanListDetailService.updateById(detail); 
							 }
					
					 }
					}
					
					 
					projPlanList.setCurrentStatus("已还款");
					projPlanList.setRepayFlag(1);// 1：已还款 你我金融的单，还款后标志为1
					projPlanList.setUpdateTime(new Date());
					projPlanList.setCreatSysType(3);
					repaymentProjPlanListService.updateById(projPlanList);
				}
				
				for (RepaymentBizPlanList pList : pLists) {
					List<RepaymentBizPlanListDetail> pListDetails = repaymentBizPlanListDetailService.selectList(
							new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId()));
					for (RepaymentBizPlanListDetail pListDetail : pListDetails) {
						for (NiWoProjPlanListDetailDto detailDto : dto.getRepaymentPlans()) {
						
							
						 if(pListDetail.getPeriod()==detailDto.getPeriod()) {
								// 看计划有没有滞纳金明细
								Integer planDetailCount = repaymentBizPlanListDetailService
										.selectCount(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",
												pList.getPlanListId()).eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()));
								
								if(planDetailCount==0&&detailDto.getTotalPenalty().compareTo(BigDecimal.valueOf(0))>0) {//证明有滞纳金
									try {
										RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(pListDetail,
												RepaymentBizPlanListDetail.class);
										planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
										planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
										planListDetailCopy.setPlanAmount(detailDto.getTotalPenalty());
										planListDetailCopy.setPlanItemType(60);
										planListDetailCopy.setPlanItemName("线上滞纳金");
										planListDetailCopy.setFactAmount(detailDto.getRepaidPenalty());
										planListDetailCopy.setCreateDate(new Date());
										repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
										
									}catch(Exception e) {
										logger.info("你我金融更新滞纳金错误"+e);
									}
							
									
								}
							pListDetail.setFactAmount(pListDetail.getPlanAmount());
							pListDetail.setUpdateDate(new Date());
							repaymentBizPlanListDetailService.updateById(pListDetail);
					   }
					 }
					}
					pList.setCurrentStatus("已还款");
					pList.setRepayFlag(1);// 1：已还款 你我金融的单，还款后标志为1
					pList.setUpdateTime(new Date());
					repaymentBizPlanListService.updateById(pList);
				}
				projPlan.setPlanStatus(20);// 已结清
				projPlan.setUpdateTime(new Date());
				repaymentProjPlanService.updateById(projPlan);
				RepaymentBizPlan plan = repaymentBizPlanService.selectById(projPlan.getPlanId());
				plan.setPlanStatus(20);// 已结清
				plan.setUpdateTime(new Date());
				repaymentBizPlanService.updateById(plan);
				//记录同步你我金融还款计划日志
				executor.execute(new Runnable() {
					
					@Override
					public void run() {
						RecordLog(dtoTemp, repaymentProjPlanListDtos);
					}
				});
			

			}
			
			
			try {
				if (dto.getProjectStatus() == 1) {// 还款中
					List<RepaymentProjPlanListDto> repaymentProjPlanListDtos=new ArrayList();//用来记录你我金融每个标对应贷后每个标的还款计划的日志
					for (RepaymentProjPlanList projPlanList : projLists) {
						for (RepaymentBizPlanList pList : pLists) {
							for (NiWoProjPlanListDetailDto detailDto : dto.getRepaymentPlans()) {
								if (projPlanList.getPeriod() == pList.getPeriod()&& detailDto.getPeriod() == projPlanList.getPeriod()) {
									
									List<RepaymentProjPlanListDetail> projDetails = repaymentProjPlanListDetailService
											.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
													.eq("proj_plan_list_id", projPlanList.getProjPlanListId()));
									List<RepaymentBizPlanListDetail> planDetails = repaymentBizPlanListDetailService
											.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",
													pList.getPlanListId()));
							
								    //同步之前先计算当前期已经实还的金额
									BigDecimal beforeRepayAmountSum=getRepayAmountSum(planDetails);
									// 看标有没有滞纳金明细
									// 看标有没有滞纳金明细
									Integer projDetailcount = repaymentProjPlanListDetailService
											.selectCount(new EntityWrapper<RepaymentProjPlanListDetail>()
													.eq("proj_plan_list_id", projPlanList.getProjPlanListId())
													.eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()));
									// 看计划有没有滞纳金明细
									Integer planDetailCount = repaymentBizPlanListDetailService
											.selectCount(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",
													pList.getPlanListId()).eq("fee_id", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()));
									RepaymentProjPlanListDto projPlanListDto=new RepaymentProjPlanListDto();
									projPlanListDto.setRepaymentProjPlanList(projPlanList);
									projPlanListDto.setProjPlanListDetails(projDetails);
									repaymentProjPlanListDtos.add(projPlanListDto);
								
									for (RepaymentProjPlanListDetail projDetail : projDetails) {
										for (RepaymentBizPlanListDetail planDetail : planDetails) {
											if (projDetail.getPeriod()==planDetail.getPeriod()&&projDetail.getPeriod()==detailDto.getPeriod()) {
												
												//担保费用项数目
											    int guaranteFeeCount=repaymentProjPlanListDetailService.selectCount(new EntityWrapper<RepaymentProjPlanListDetail>().eq("plan_item_type", RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()).eq("proj_plan_list_id", projPlanList.getProjPlanListId()));
												//平台费用项数目
											    int platformFeeCount=repaymentProjPlanListDetailService.selectCount(new EntityWrapper<RepaymentProjPlanListDetail>().eq("plan_item_type", RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()).eq("proj_plan_list_id", projPlanList.getProjPlanListId()));
												//公司服务费用项数目
											    int companyFeeCount=repaymentProjPlanListDetailService.selectCount(new EntityWrapper<RepaymentProjPlanListDetail>().eq("plan_item_type", RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()).eq("proj_plan_list_id", projPlanList.getProjPlanListId()));

												boolean isDifferent=false;
												if (RepayPlanFeeTypeEnum.PRINCIPAL.getValue()==projDetail.getPlanItemType()) { // 本金
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getPrincipal())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getPrincipal());
													planDetail.setPlanAmount(detailDto.getPrincipal());
													projDetail.setProjFactAmount(detailDto.getRepaidPrincipal());
													planDetail.setFactAmount(detailDto.getRepaidPrincipal());
													projDetail.setUpdateDate(new Date());
													projDetail.setCreatSysType(3);
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.INTEREST.getValue()==projDetail.getPlanItemType()) { // 利息
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getInterest())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													
													}
													projDetail.setProjPlanAmount(detailDto.getInterest());
													planDetail.setPlanAmount(detailDto.getInterest());
													projDetail.setProjFactAmount(detailDto.getRepaidInterest());
													planDetail.setFactAmount(detailDto.getRepaidInterest());
													projDetail.setUpdateDate(new Date());
													projDetail.setCreatSysType(3);
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()==projDetail.getPlanItemType()) {// 担保服务费
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getCommissionGuaranteFee())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getCommissionGuaranteFee());
													planDetail.setPlanAmount(detailDto.getCommissionGuaranteFee());
													projDetail
															.setProjFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													planDetail.setFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													projDetail.setCreatSysType(3);
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												}else if (RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()==projDetail.getPlanItemType()) {// //平台管理费
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getPlatformManageFee())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getPlatformManageFee());
													planDetail.setPlanAmount(detailDto.getPlatformManageFee());
													projDetail.setProjFactAmount(detailDto.getRepaidPlatformManageFee());
													planDetail.setFactAmount(detailDto.getRepaidPlatformManageFee());
													projDetail.setCreatSysType(3);
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()==projDetail.getPlanItemType()) {// //分公司服务费
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getPlatformManageFee())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getShouldConsultingFee());
													planDetail.setPlanAmount(detailDto.getShouldConsultingFee());
													projDetail.setProjFactAmount(detailDto.getRepaidConsultingFee());
													projDetail.setCreatSysType(3);
													planDetail.setFactAmount(detailDto.getRepaidConsultingFee());
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												
												}else if (RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getValue()==projDetail.getPlanItemType()
														&& detailDto.getTotalPenalty() != null && detailDto
																.getTotalPenalty().compareTo(BigDecimal.valueOf(0)) > 0) {// 逾期滞纳金
													projDetail.setProjPlanAmount(detailDto.getTotalPenalty());
													projDetail.setProjFactAmount(detailDto.getRepaidPenalty());
													projDetail.setUpdateDate(new Date());
													projDetail.setCreatSysType(3);
													planDetail.setUpdateDate(new Date());
													planDetail.setFactAmount(detailDto.getRepaidPenalty());
													planDetail.setPlanAmount(detailDto.getTotalPenalty());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (projDetailcount ==0 && detailDto.getTotalPenalty() != null && detailDto
														.getTotalPenalty().compareTo(BigDecimal.valueOf(0)) > 0) {
													RepaymentBizPlanListDetail planTemp=planDetails.get(0);
													RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(planTemp,
															RepaymentBizPlanListDetail.class);
													planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
													planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
													planListDetailCopy.setPlanAmount(detailDto.getTotalPenalty());
													planListDetailCopy.setPlanItemType(60);
													planListDetailCopy.setPlanItemName("线上滞纳金");
													planListDetailCopy.setFactAmount(detailDto.getRepaidPenalty());
													planListDetailCopy.setCreateDate(new Date());
													repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
													
													
													
													RepaymentProjPlanListDetail temp = projDetails.get(0);
													RepaymentProjPlanListDetail projListDetail = ClassCopyUtil.copyObject(temp,
															RepaymentProjPlanListDetail.class);
													projListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
													projListDetail.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
													projListDetail.setProjPlanAmount(detailDto.getTotalPenalty());
													projListDetail.setPlanItemType(60);
													projListDetail.setPlanDetailId(planListDetailCopy.getPlanDetailId());
													projListDetail.setProjFactAmount(detailDto.getRepaidPenalty());
													projListDetail.setPlanItemName("滞纳金");
													projListDetail.setCreatSysType(3);
													projListDetail.setCreateDate(new Date());
													repaymentProjPlanListDetailService.insertOrUpdate(projDetail);
													
												
												}
												
												 if(guaranteFeeCount==0&&detailDto.getCommissionGuaranteFee().compareTo(BigDecimal.valueOf(0))>0){//如果担保费用项为0，记录异常，新增担保费用项
													isDifferent=true;
													RepaymentProjPlanListDetail projPlanListDetailCopy = ClassCopyUtil.copyObject(projDetail,
															RepaymentProjPlanListDetail.class);
													RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(planDetail,
															RepaymentBizPlanListDetail.class);
													projPlanListDetailCopy.setProjPlanDetailId(UUID.randomUUID().toString());
													projPlanListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getUuid());
													projPlanListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getDesc());
													projPlanListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue());
													projPlanListDetailCopy.setPlanDetailId(projPlanListDetailCopy.getPlanDetailId());
													projPlanListDetailCopy.setCreatSysType(3);
													projPlanListDetailCopy.setProjPlanAmount(detailDto.getCommissionGuaranteFee());
													projPlanListDetailCopy.setProjFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													projPlanListDetailCopy.setCreateDate(new Date());
				
													planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
													planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getUuid());
													planListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getDesc());
													planListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue());
													planListDetailCopy.setPlanAmount(detailDto.getCommissionGuaranteFee());
													planListDetailCopy.setFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													planListDetailCopy.setCreateDate(new Date());
													
													
													repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
													repaymentProjPlanListDetailService.insertOrUpdate(projPlanListDetailCopy);
												}
												if(platformFeeCount==0&&detailDto.getPlatformManageFee().compareTo(BigDecimal.valueOf(0))>0) {//如果平台管理费用项为0，记录异常，新增平台管理费用项
													isDifferent=true;
													RepaymentProjPlanListDetail projPlanListDetailCopy = ClassCopyUtil.copyObject(projDetail,
															RepaymentProjPlanListDetail.class);
													RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(planDetail,
															RepaymentBizPlanListDetail.class);
													projPlanListDetailCopy.setProjPlanDetailId(UUID.randomUUID().toString());
													projPlanListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.PLAT_CHARGE.getUuid());
													projPlanListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.PLAT_CHARGE.getDesc());
													projPlanListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue());
													projPlanListDetailCopy.setPlanDetailId(projPlanListDetailCopy.getPlanDetailId());
													projPlanListDetailCopy.setCreatSysType(3);
													projPlanListDetailCopy.setProjPlanAmount(detailDto.getPlatformManageFee());
													projPlanListDetailCopy.setProjFactAmount(detailDto.getRepaidPlatformManageFee());
													projPlanListDetailCopy.setCreateDate(new Date());
													
													planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
													planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.PLAT_CHARGE.getUuid());
													planListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.PLAT_CHARGE.getDesc());
													planListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue());
													planListDetailCopy.setPlanAmount(detailDto.getPlatformManageFee());
													planListDetailCopy.setFactAmount(detailDto.getRepaidPlatformManageFee());
													planListDetailCopy.setCreateDate(new Date());
													repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
													repaymentProjPlanListDetailService.insertOrUpdate(projPlanListDetailCopy);
												}
												if(companyFeeCount==0&&detailDto.getShouldConsultingFee().compareTo(BigDecimal.valueOf(0))>0) { //如果分公司服务费用项为0，记录异常，新增分公司服务费用项
													isDifferent=true;
													RepaymentProjPlanListDetail projPlanListDetailCopy = ClassCopyUtil.copyObject(projDetail,
															RepaymentProjPlanListDetail.class);
													RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(planDetail,
															RepaymentBizPlanListDetail.class);
													projPlanListDetailCopy.setProjPlanDetailId(UUID.randomUUID().toString());
													projPlanListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getUuid());
													projPlanListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getDesc());
													projPlanListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue());
													projPlanListDetailCopy.setPlanDetailId(projPlanListDetailCopy.getPlanDetailId());
													projPlanListDetailCopy.setCreatSysType(3);
													projPlanListDetailCopy.setProjPlanAmount(detailDto.getShouldConsultingFee());
													projPlanListDetailCopy.setProjFactAmount(detailDto.getRepaidConsultingFee());
													projPlanListDetailCopy.setCreateDate(new Date());
													
													planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
													planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getUuid());
													planListDetailCopy.setPlanItemName(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getDesc());
													planListDetailCopy.setPlanItemType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue());
													planListDetailCopy.setPlanAmount(detailDto.getShouldConsultingFee());
													planListDetailCopy.setFactAmount(detailDto.getRepaidConsultingFee());
													planListDetailCopy.setCreateDate(new Date());
													repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
													repaymentProjPlanListDetailService.insertOrUpdate(projPlanListDetailCopy);
												}
												if(isDifferent) {
													RecordExceptionLog(dto, projDetail,null);	
												}
									
											
											}

										}

									}
								
									
							
								 
								   
									projPlanList.setOverdueDays(BigDecimal.valueOf(getOverDays(detailDto.getRefundDate())));
									projPlanList.setDueDate(new Date(detailDto.getRefundDate()));
									projPlanList.setOverdueAmount(detailDto.getTotalPenalty());
									projPlanList.setUpdateTime(new Date());
									projPlanList.setCreatSysType(3);
									repaymentProjPlanListService.updateById(projPlanList);
									pList.setOverdueDays(BigDecimal.valueOf(getOverDays(detailDto.getRefundDate())));
									pList.setDueDate(new Date(detailDto.getRefundDate()));
									pList.setOverdueAmount(detailDto.getTotalPenalty());
									pList.setUpdateTime(new Date());
									repaymentProjPlanListService.updateById(projPlanList);
									repaymentBizPlanListService.updateById(pList);
									
									BigDecimal planAmountSum=getPlanAmountSum(planDetails);//当前期计划要还的总金额
									BigDecimal afterRepayAmountSum=getRepayAmountSum(planDetails);//当前期已还总金额
									pList.setTotalBorrowAmount(planAmountSum);;
									projPlanList.setTotalBorrowAmount(planAmountSum);
									pList.setUpdateTime(new Date());
									projPlanList.setUpdateTime(new Date());
									repaymentProjPlanListService.updateById(projPlanList);
									repaymentBizPlanListService.updateById(pList);
									/*	
									 * 同步完你我金融的当前期还款计划之后，如果当前期已还总金额等于当前期计划要还的总金额，并且同步之后的当前期已还总金额大于同步之前的当前期已还总金额
								     *说明需要发成功代扣的短信
									 */
									if(afterRepayAmountSum.compareTo(planAmountSum)==0&&afterRepayAmountSum.compareTo(beforeRepayAmountSum)>0&&(!pList.getCurrentStatus().equals("已还款"))) {
										BigDecimal repayMoney=afterRepayAmountSum.subtract(beforeRepayAmountSum);
										if(afterRepayAmountSum.compareTo(planAmountSum)==0) {//当期已还款
											pList.setCurrentStatus("已还款");
											pList.setFactRepayDate(new Date());
											projPlanList.setCurrentStatus("已还款");
											projPlanList.setFactRepayDate(new Date());
											for(RepaymentProjPlanListDetail projDetail : projDetails) {
												projDetail.setFactRepayDate(new Date());
												repaymentProjPlanListDetailService.updateById(projDetail);
											}
											for(RepaymentBizPlanListDetail planDetail : planDetails) {
												planDetail.setFactRepayDate(new Date());
												repaymentBizPlanListDetailService.updateById(planDetail);
											}
											
											repaymentProjPlanListService.updateById(projPlanList);
											repaymentBizPlanListService.updateById(pList);
										}
									           try {
												logger.info("你我金融-发送短信开始==================");
												sendSuccessSms(pList.getOrigBusinessId(), planAmountSum, repayMoney);
												logger.info("你我金融-发送短信结束==================");
									           }catch(Exception e) {
									        	   logger.error("你我金融-发送短信出错"+e);  
									           }
										
									}
								
								}
							}

						}
					}
					
					RecordLog(dto, repaymentProjPlanListDtos);//用来记录你我金融每个标对应贷后每个标的还款计划的日志
					projPlan.setUpdateTime(new Date());
					repaymentProjPlanService.updateById(projPlan);
				}
			} catch (Exception e) {
				logger.info("同步你我金融标的数据时候出错,上标编号的ID为："+orderNo+"====="+e);
			}
		}
		

 
	
	
	private void RecordExceptionLog(NiWoProjPlanDto dto,RepaymentProjPlanListDetail projDetail,String projId) {
		SysExceptionLog log=new SysExceptionLog();
		String returnJason="";
		String sendJason="";
		if(projDetail!=null&&dto!=null) {
			 returnJason=JSON.toJSONString(dto);
			 sendJason=JSON.toJSONString(projDetail);
			 log.setLogMessage("贷后生成的还款计划与你我金融生成的还款计划不一致");
			log.setBusinessId(dto.getOrderNo());
		}else if(projDetail==null){
			 log.setLogMessage("贷后生成的还款计划与你我金融生成的还款计划不一致");
				log.setBusinessId(dto.getOrderNo());
		}else {
			log.setLogMessage("超过3天没有同步到你我金融标的还款计划");
			log.setBusinessId(projId);
		}

		log.setLogDate(new Date());
		log.setLogLevel("3");
		log.setSendJason(sendJason);
		log.setReturnJason(returnJason);
	
		log.setLogType("同步你我金融计划,businessID存储的是标的iD");
		log.insert();
		sysExceptionLogService.insertOrUpdate(log);
	}
	
	
	private void RecordLog(NiWoProjPlanDto dto,List<RepaymentProjPlanListDto> repaymentProjPlanListDtos) {
		IssueSendOutsideLog log=new IssueSendOutsideLog();
		String returnJason=JSON.toJSONString(dto);
		String sendJason=JSON.toJSONString(repaymentProjPlanListDtos);
		log.setBusinessId(dto.getOrderNo());
		log.setCreateTime(new Date());
        log.setCreateUserId("admin");
        log.setReturnJson(returnJason);
        log.setSendJson(sendJason);
        log.setSystem("eip-niwo");
        log.setInterfacename("queryApplyOrder");
        issueSendOutsideLogService.insertOrUpdate(log);
	}


	
	
	private NiWoProjPlanDto getNiWoRepayPlanByMap(HashMap<String, Object> map,NiWoProjPlanDto dto) {
			dto.setContractUrl(map.get("orderNo") == null ? "" : map.get("orderNo").toString());
			dto.setOrderStatus(map.get("orderStatus") == null ? 0 : Integer.valueOf(map.get("orderStatus").toString()));
			dto.setOrderMsg(map.get("orderMsg") == null ? "" : map.get("orderMsg").toString());
			dto.setContractUrl(map.get("contractUrl") == null ? "" : map.get("contractUrl").toString());
			dto.setOrderNo(map.get("orderNo") == null ? "" : map.get("orderNo").toString());
			dto.setProjectStatus(
					map.get("projectStatus") == null ? 0 : Integer.valueOf(map.get("projectStatus").toString()));
			dto.setWithdrawMsg(map.get("withdrawMsg") == null ? "" : map.get("withdrawMsg").toString());
			dto.setWithdrawStatus(
					map.get("withdrawStatus") == null ? 0 : Integer.valueOf(map.get("withdrawStatus").toString()));
			dto.setWithdrawSuccessTime(
					map.get("withdrawSuccessTime") == null ? 0 : Long.valueOf(map.get("withdrawSuccessTime").toString()));
			dto.setWithdrawTime(map.get("withdrawTime") == null ? 0 : Long.valueOf(map.get("withdrawTime").toString()));
			Object array=map.get("repaymentPlan")==null?"":map.get("repaymentPlan"); 
			if(array!=null&&!array.equals("")) {
				List<NiWoProjPlanListDetailDto>  repaymentPlans=new ArrayList();
				List<HashMap<String,Object>>  repaymentPlanMaps=(List<HashMap<String,Object>>) array;
				for(HashMap<String,Object> repaymentPlanMap:repaymentPlanMaps) {
					NiWoProjPlanListDetailDto detailDto=new NiWoProjPlanListDetailDto();
					detailDto.setPeriod(repaymentPlanMap.get("period")==null?null:Integer.valueOf(repaymentPlanMap.get("period").toString()));
					detailDto.setPrincipal(repaymentPlanMap.get("principal")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("principal").toString())));
					detailDto.setInterest(repaymentPlanMap.get("interest")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("interest").toString())));
					detailDto.setPlatformManageFee(repaymentPlanMap.get("platformManageFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("platformManageFee").toString())));
					detailDto.setCommissionGuaranteFee(repaymentPlanMap.get("commissionGuaranteFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("commissionGuaranteFee").toString())));
					detailDto.setRefundDate(repaymentPlanMap.get("refundDate")==null?Long.valueOf(0):Long.valueOf(repaymentPlanMap.get("refundDate").toString()));
					detailDto.setRepaidPrincipal(repaymentPlanMap.get("repaidPrincipal")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidPrincipal").toString())));
					detailDto.setRepaidInterest(repaymentPlanMap.get("repaidInterest")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidInterest").toString())));
					detailDto.setRepaidPlatformManageFee(repaymentPlanMap.get("repaidPlatformManageFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidPlatformManageFee").toString())));
					detailDto.setRepaidCommissionGuaranteFee(repaymentPlanMap.get("repaidCommissionGuaranteFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidCommissionGuaranteFee").toString())));
					detailDto.setTotalPenalty(repaymentPlanMap.get("totalPenalty")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("totalPenalty").toString())));
					detailDto.setRepaidPenalty(repaymentPlanMap.get("repaidPenalty")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidPenalty").toString())));
					detailDto.setShouldConsultingFee(repaymentPlanMap.get("shouldConsultingFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("shouldConsultingFee").toString())));
					detailDto.setRepaidConsultingFee(repaymentPlanMap.get("repaidConsultingFee")==null?BigDecimal.valueOf(0):BigDecimal.valueOf(Double.valueOf(repaymentPlanMap.get("repaidConsultingFee").toString())));
					repaymentPlans.add(detailDto);		
				}
		
				dto.setRepaymentPlan(repaymentPlans);
			}
			return dto;
		
	}
	private  NiWoProjPlanDto getNiWoRepayPlan(String orderNo,NiWoProjPlanDto dto) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("orderNo", orderNo);
			Result result = eipRemote.queryApplyOrder(paramMap);
			if(result.getReturnCode().equals("0000")) {
				HashMap<String, Object> map = (HashMap) result.getData();
				dto=getNiWoRepayPlanByMap(map,dto);
		}else {
			logger.info(result.getCodeDesc());
			return null;
		}
			return dto;
	}

	@Override
	public void SearchNiWoRepayPlan() {
		//定时查询你我金融的还款计划，未结清、未完全付款的还款计划需要每天查询
		List<RepaymentProjPlan> projPlans=repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("plate_type", 2).eq("plan_status", 0));
		for(RepaymentProjPlan repaymentProjPlan:projPlans) {
			sycNiWoRepayPlan(repaymentProjPlan.getRequestNo(),null);
			//超过3天没有同步到你我金融标的还款计划
			if(isOver3Days(repaymentProjPlan)) {
				RecordExceptionLog(null, null,repaymentProjPlan.getProjectId());
			}
		}
		
	}
	
	
	private boolean isOver3Days(RepaymentProjPlan repaymentProjPlan) {
		boolean isOver3Days=false;
		if(DateUtil.getDiffDays(repaymentProjPlan.getUpdateTime(), new Date())>3) {
			isOver3Days=true;
		}else {
			isOver3Days=false;
		}
		return isOver3Days;
		
	}
	
	private Integer getOverDays(Long refundDate) {
		if(refundDate==null) {
			return 0;
		}
		Date repayDate=new Date(refundDate);
		Integer days=0;
		if(new Date().compareTo(repayDate)>0) {
			days=DateUtil.getDiffDays(repayDate, new Date());
		}
		return days;
	}
	
	private BigDecimal getRepayAmountSum(List<RepaymentBizPlanListDetail> planDetails) {
		BigDecimal sum=BigDecimal.valueOf(0);
		for(RepaymentBizPlanListDetail detail:planDetails) {
			sum=sum.add(detail.getFactAmount()==null?BigDecimal.valueOf(0):detail.getFactAmount());
		}
		return sum;
		
	}
	
	
	private BigDecimal getPlanAmountSum(List<RepaymentBizPlanListDetail> planDetails) {
		BigDecimal sum=BigDecimal.valueOf(0);
		for(RepaymentBizPlanListDetail detail:planDetails) {
			
			sum=sum.add(detail.getPlanAmount()==null?BigDecimal.valueOf(0):detail.getPlanAmount());
		}
		return sum;
		
	}
	
	/**
	 * 发送扣款成功短信
	 * @return 
	 */
	private void sendSuccessSms(String businessId,BigDecimal planAmount,BigDecimal repayAmount) {
		TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("business_id",businessId));
		
	
		String templateCode=MsgCodeEnum.NIWO_REPAY_SUCCESS.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("你我金融-发送扣款成功短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("贷后你我金融扣款成功提示");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(tuandaiProjectInfo.getTelNo());
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", tuandaiProjectInfo.getRealName());
		data.put("planAmount", planAmount);
		data.put("factAmount", repayAmount);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
		
	}





	@Override
	public void calFailMsg() {
		List<RepaymentProjPlanList> projPlanLists=repaymentProjPlanListService.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("plate_type", 2).ne("current_status", "已还款").eq("due_date", DateUtil.getDate(DateUtil.formatDate(new Date()))));

		String templateCode=MsgCodeEnum.NIWO_REPAY_FAIL.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("你我金融-发送扣款失败短信模板为空");
			return;
		}
		
		for(RepaymentProjPlanList projPlanList:projPlanLists) {
			RepaymentProjPlan repaymentProjPlan=repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("proj_plan_id", projPlanList.getProjPlanId()));
			TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id", repaymentProjPlan.getProjectId()));
			Date borrowDate=null;
			if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
				borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
			}else {
				borrowDate=tuandaiProjectInfo.getCreateTime();
			}
			Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
			MsgRequestDto dto=new MsgRequestDto();
			dto.setApp("alms");
			dto.setMsgTitle("贷后你我金融扣款失败提示");
			dto.setMsgModelId(msgModeId);
			dto.setMsgTo(tuandaiProjectInfo.getTelNo());
			//组装发送短信内容的Json数据
			JSONObject data = new JSONObject() ;
			data.put("name", tuandaiProjectInfo.getRealName());
			data.put("date", DateUtil.getChinaDay(borrowDate));
			data.put("borrowAmount", repaymentProjPlan.getBorrowMoney());
			data.put("tailCardNum", tuandaiProjectInfo.getBankAccountNo().substring(tuandaiProjectInfo.getBankAccountNo().length()-4, tuandaiProjectInfo.getBankAccountNo().length()));
			dto.setMsgBody(data);
			String jason=JSON.toJSONString(dto);
			msgRemote.sendRequest(jason);
			
			
		}
		
		
	}





	@Override
	public void sendRepayRemindMsg(Integer days) {
		   Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
		   
			String templateCode=MsgCodeEnum.NIWO_REPAY_REMIND.getValue();
			SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

		
			if(sysMsgTemplate==null) {
				logger.info("你我金融-还款提醒短信模板为空");
				return;
			}
		   List<RepaymentProjPlanList>  lists=repaymentProjPlanListService.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("current_status","还款中").eq("due_date", dueDate).eq("plate_type", 2));
		   for(RepaymentProjPlanList projPlanList:lists) {
				RepaymentProjPlan repaymentProjPlan=repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("proj_plan_id", projPlanList.getProjPlanId()));
			   TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id", repaymentProjPlan.getProjectId()));
				Date borrowDate=null;
				if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
					borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
				}else {
					borrowDate=tuandaiProjectInfo.getCreateTime();
				}
				Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
				MsgRequestDto dto=new MsgRequestDto();
				dto.setApp("alms");
				dto.setMsgTitle("贷后你我金融还款提醒");
				dto.setMsgModelId(msgModeId);
				dto.setMsgTo(tuandaiProjectInfo.getTelNo());
				//组装发送短信内容的Json数据
				JSONObject data = new JSONObject() ;
				data.put("name", tuandaiProjectInfo.getRealName());
				data.put("date", DateUtil.getChinaDay(borrowDate));
				data.put("borrowAmount", repaymentProjPlan.getBorrowMoney());
				data.put("period", projPlanList.getPeriod());
				data.put("amount", projPlanList.getTotalBorrowAmount());
				data.put("dueDate", DateUtil.getChinaDay(projPlanList.getDueDate()));
				data.put("tailCardNo", tuandaiProjectInfo.getBankAccountNo().substring(tuandaiProjectInfo.getBankAccountNo().length()-4, tuandaiProjectInfo.getBankAccountNo().length()));
				dto.setMsgBody(data);
				String jason=JSON.toJSONString(dto);
				msgRemote.sendRequest(jason);
			   
			}
	}





	@Override
	public void sendSettleRemindMsg(Integer days) { 
		  Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, new Date())));
		  List<RepaymentProjPlanList>  lists=repaymentProjPlanListService.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("current_status","还款中").eq("due_date", dueDate).eq("plate_type", 2).orderBy("due_date",false));
   		
			String templateCode=MsgCodeEnum.NIWO_SETTLE_REMIND.getValue();
			SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

		
			if(sysMsgTemplate==null) {
				logger.info("你我金融-结清提醒短信模板为空");
				return;
			}
		  
		  //筛选是最后一期的还款记录
			for(Iterator<RepaymentProjPlanList> it = lists.iterator();it.hasNext();) {
				RepaymentProjPlanList list=it.next();
				if(!istLastPeriod(list)) {
					it.remove();
				}
			}
			
			 for(RepaymentProjPlanList projPlanList:lists) {
					   RepaymentProjPlan repaymentProjPlan=repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("proj_plan_id", projPlanList.getProjPlanId()));
					   TuandaiProjectInfo tuandaiProjectInfo=tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id", repaymentProjPlan.getProjectId()));
						Date borrowDate=null;
						if(tuandaiProjectInfo.getQueryFullSuccessDate()!=null) {
							borrowDate=tuandaiProjectInfo.getQueryFullSuccessDate();
						}else {
							borrowDate=tuandaiProjectInfo.getCreateTime();
						}
						Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
						MsgRequestDto dto=new MsgRequestDto();
						dto.setApp("alms");
						dto.setMsgTitle("贷后你我金融结清提醒");
						dto.setMsgModelId(msgModeId);
						dto.setMsgTo(tuandaiProjectInfo.getTelNo());
						//组装发送短信内容的Json数据
						JSONObject data = new JSONObject() ;
						data.put("name", tuandaiProjectInfo.getRealName());
						data.put("date", DateUtil.getChinaDay(borrowDate));
						data.put("borrowAmount", repaymentProjPlan.getBorrowMoney());
						data.put("dueDate",DateUtil.getChinaDay(projPlanList.getDueDate()));
						dto.setMsgBody(data);
						String jason=JSON.toJSONString(dto);
						msgRemote.sendRequest(jason);
				
				}
		
	}
	
	public boolean istLastPeriod(RepaymentProjPlanList projList) {
		boolean isLast=false;
		List<RepaymentProjPlanList> projLists=repaymentProjPlanListService.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projList.getProjPlanId()));
		RepaymentProjPlanList lastProjList=projLists.stream().max(new Comparator<RepaymentProjPlanList>() {
			@Override
			public int compare(RepaymentProjPlanList o1, RepaymentProjPlanList o2) {
				return o1.getDueDate().compareTo(o2.getDueDate());
			}
		}).get();
		
		if(projList.getPlanListId().equals(lastProjList.getPlanListId())) {
			isLast=true;
		}
		return isLast;
	}

	
}
