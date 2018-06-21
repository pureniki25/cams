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
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
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
			List<RepaymentProjPlanList> projLists = repaymentProjPlanListService
					.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("projPlanId", projPlan.getProjPlanId()));
			List<RepaymentBizPlanList> pLists = repaymentBizPlanListService
					.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", projPlan.getPlanId()));
			projPlan.setCreatSysType(3);//标志为你我金融的还款计划
			if (dto.getProjectStatus() == 2) {// 已结清
				for (RepaymentProjPlanList projPlanList : projLists) {
					List<RepaymentProjPlanListDetail> projListDetails = repaymentProjPlanListDetailService
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
									projPlanList.getProjPlanListId()));
					for (RepaymentProjPlanListDetail detail : projListDetails) {
						detail.setProjFactAmount(detail.getProjPlanAmount());
						detail.setUpdateDate(new Date());
						repaymentProjPlanListDetailService.updateById(detail); 
					}
					projPlanList.setCurrentStatus("已还款");
					projPlanList.setRepayFlag(1);// 1：已还款 你我金融的单，还款后标志为1
					projPlanList.setUpdateTime(new Date());
					repaymentProjPlanListService.updateById(projPlanList);
				}

				for (RepaymentBizPlanList pList : pLists) {
					List<RepaymentBizPlanListDetail> pListDetails = repaymentBizPlanListDetailService.selectList(
							new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId()));
					for (RepaymentBizPlanListDetail pListDetail : pListDetails) {
						pListDetail.setFactAmount(pListDetail.getPlanAmount());
						pListDetail.setUpdateDate(new Date());
						repaymentBizPlanListDetailService.updateById(pListDetail);
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

			}
			
			
			try {
				if (dto.getProjectStatus() == 1) {// 还款中
					List<RepaymentProjPlanListDto> repaymentProjPlanListDtos=new ArrayList();//用来记录你我金融每个标对应贷后每个标的还款计划的日志
					for (RepaymentProjPlanList projPlanList : projLists) {
						for (RepaymentBizPlanList pList : pLists) {
							for (NiWoProjPlanListDetailDto detailDto : dto.getRepaymentPlans()) {
								if (projPlanList.getPeriod() == pList.getPeriod()
										&& detailDto.getPeriod() == projPlanList.getPeriod()) {
									List<RepaymentProjPlanListDetail> projDetails = repaymentProjPlanListDetailService
											.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
													.eq("proj_plan_list_id", projPlanList.getProjPlanListId()));
									List<RepaymentBizPlanListDetail> planDetails = repaymentBizPlanListDetailService
											.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",
													pList.getPlanListId()));
									// 看标有没有滞纳金明细
									Integer projDetailcount = repaymentBizPlanListDetailService
											.selectCount(new EntityWrapper<RepaymentBizPlanListDetail>()
													.eq("plan_list_id", pList.getPlanListId())
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
											if (projDetail.getFeeId() == planDetail.getFeeId()) {
												boolean isDifferent=false;
												if (RepayPlanFeeTypeEnum.PRINCIPAL.getUuid()
														.equals(projDetail.getFeeId())) { // 本金
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getPrincipal())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getPrincipal());
													planDetail.setPlanAmount(detailDto.getPrincipal());
													projDetail.setProjFactAmount(detailDto.getRepaidPrincipal());
													planDetail.setFactAmount(detailDto.getRepaidPrincipal());
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.INTEREST.getUuid()
														.equals(projDetail.getFeeId())) { // 利息
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getInterest())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													
													}
													projDetail.setProjPlanAmount(detailDto.getInterest());
													planDetail.setPlanAmount(detailDto.getInterest());
													projDetail.setProjFactAmount(detailDto.getRepaidInterest());
													planDetail.setFactAmount(detailDto.getRepaidInterest());
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getUuid()
														.equals(projDetail.getFeeId())) {// 担保服务费
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getCommissionGuaranteFee())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getCommissionGuaranteFee());
													planDetail.setPlanAmount(detailDto.getCommissionGuaranteFee());
													projDetail
															.setProjFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													planDetail.setFactAmount(detailDto.getRepaidCommissionGuaranteFee());
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.PLAT_CHARGE.getUuid()
														.equals(projDetail.getFeeId())) {// //平台管理费
													if(projDetail.getProjPlanAmount().compareTo(detailDto.getPlatformManageFee())!=0) {//贷后应还的和你我金融应还的金额不一致，计入异常日志表
														isDifferent=true;
													}
													projDetail.setProjPlanAmount(detailDto.getPlatformManageFee());
													planDetail.setPlanAmount(detailDto.getPlatformManageFee());
													projDetail.setProjFactAmount(detailDto.getRepaidPlatformManageFee());
													planDetail.setFactAmount(detailDto.getRepaidPlatformManageFee());
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentProjPlanListDetailService.updateById(projDetail);
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()
														.equals(projDetail.getFeeId())
														&& detailDto.getTotalPenalty() != null && detailDto
																.getTotalPenalty().compareTo(BigDecimal.valueOf(0)) > 0) {// 逾期滞纳金
													
												
													projDetail.setProjPlanAmount(detailDto.getTotalPenalty());
													planDetail.setPlanAmount(detailDto.getTotalPenalty());
													projDetail.setProjFactAmount(detailDto.getRepaidPenalty());
													planDetail.setFactAmount(detailDto.getRepaidPenalty());
													repaymentProjPlanListDetailService.updateById(projDetail);
													projDetail.setUpdateDate(new Date());
													planDetail.setUpdateDate(new Date());
													repaymentBizPlanListDetailService.updateById(planDetail);
												} else if (projDetailcount > 0 && detailDto.getTotalPenalty() != null && detailDto
														.getTotalPenalty().compareTo(BigDecimal.valueOf(0)) > 0) {
													RepaymentBizPlanListDetail planTemp=planDetails.get(0);
													RepaymentBizPlanListDetail planListDetailCopy = ClassCopyUtil.copyObject(planTemp,
															RepaymentBizPlanListDetail.class);
													planListDetailCopy.setPlanDetailId(UUID.randomUUID().toString());
													planListDetailCopy.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
													planListDetailCopy.setPlanAmount(detailDto.getTotalPenalty());
													planListDetailCopy.setPlanItemType(60);
													planListDetailCopy.setPlanItemName("滞纳金");
													planListDetailCopy.setCreateDate(new Date());
													repaymentBizPlanListDetailService.insertOrUpdate(planListDetailCopy);
													
													
													
													RepaymentProjPlanListDetail temp = projDetails.get(0);
													RepaymentProjPlanListDetail projListDetail = ClassCopyUtil.copyObject(temp,
															RepaymentProjPlanListDetail.class);
													projListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
													projListDetail.setFeeId(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
													projListDetail.setProjPlanAmount(detailDto.getTotalPenalty());
													projListDetail.setPlanItemType(60);
													projDetail.setPlanDetailId(planListDetailCopy.getPlanDetailId());
													projDetail.setPlanItemName("滞纳金");
													repaymentProjPlanListDetailService.insertOrUpdate(projDetail);
													
												
												}
												if(isDifferent) {
													RecordExceptionLog(dto, projDetail,null);	
												}
									
												projPlanList.setOverdueDays(BigDecimal.valueOf(getOverDays(detailDto.getRefundDate())));
												projPlanList.setOverdueAmount(detailDto.getTotalPenalty());
												pList.setOverdueDays(BigDecimal.valueOf(getOverDays(detailDto.getRefundDate())));
												pList.setOverdueAmount(detailDto.getTotalPenalty());
												repaymentProjPlanListService.updateById(projPlanList);
												repaymentBizPlanListService.updateById(pList);
											}

										}

									}
									projPlanList.setUpdateTime(new Date());
									repaymentProjPlanListService.updateById(projPlanList);
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
			if(array!=null) {
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
		}
			return dto;
	}

	@Override
	public void SearchNiWoRepayPlan() {
		//定时查询你我金融的还款计划，未结清、未完全付款的还款计划需要每天查询
		List<RepaymentProjPlan> projPlans=repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("creat_sys_type", 3).eq("plan_status", 0));
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
}
