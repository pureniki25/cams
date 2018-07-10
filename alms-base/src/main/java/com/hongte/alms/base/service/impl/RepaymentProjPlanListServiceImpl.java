package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.repayPlan.RepayPlanBorrowRateUnitEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.ProfitItemSetService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.core.Result;
import com.ht.ussp.util.DateUtil;
import com.mysql.cj.core.io.BigDecimalValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 标的还款计划列表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2018-05-15
 */
@Service("RepaymentProjPlanListService")
public class RepaymentProjPlanListServiceImpl extends
		BaseServiceImpl<RepaymentProjPlanListMapper, RepaymentProjPlanList> implements RepaymentProjPlanListService {
	private static Logger logger = LoggerFactory.getLogger(RepaymentProjPlanListServiceImpl.class);


	//进位方式枚举
    private  RoundingMode roundingMode=RoundingMode.HALF_UP;
    
   //保留小数位数
    private  Integer smallNum=2;
	@Autowired
	@Qualifier("ProfitItemSetService")
	ProfitItemSetService profitItemSetService;
	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper;
	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService repaymentProjPlanService;

	@Autowired
	@Qualifier("RepaymentProjPlanListDetailService")
	RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;
	
	 
    @Autowired
    EipRemote eipRemote;
	
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void calLateFee() {
		// 所有业务贷后生成的业务
		List<BasicBusiness> basicBusiness = basicBusinessService
				.selectList((new EntityWrapper<BasicBusiness>().eq("src_type", 2)));

		for (BasicBusiness business : basicBusiness) {
                  
			// 每个业务对应所有贷后生成的还款计划
			List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList((new EntityWrapper<RepaymentBizPlan>().eq("src_type", 2))
							.eq("original_business_id", business.getBusinessId()));
			
			
			for(RepaymentBizPlan plan:plans) {
				List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.getPlanListForCalLateFee(plan.getPlanId());
				    for(RepaymentBizPlanList pList:pLists) {
				    	// 每个业务的还款计划列表对应所有标的还款计划列表
						List<RepaymentProjPlanList> projList = getProListForCalLateFee(pList.getPlanListId());
						BigDecimal underLateFeeSum=BigDecimal.valueOf(0);//每个业务每期还款计划的线下收费
						BigDecimal onlineLateFeeSum=BigDecimal.valueOf(0);//每个业务每期还款计划的线上收费
						
						if(pList.getPlanListId().equals("095118d7-633d-4f1c-82ef-4699563fffb6")) {
							System.out.println("stop");
						}
						
						
							for (RepaymentProjPlanList projPList : projList) {
							
								// 每个表的还款计划列表对应所的标的还款计划
								RepaymentProjPlan projPlan = repaymentProjPlanService
										.selectOne((new EntityWrapper<RepaymentProjPlan>().eq("creat_sys_type", 2))
												.eq("proj_plan_id", projPList.getProjPlanId()));
							
								getRondModeAndSmallNum(projPlan);//确定进位方式和保留小数位
								RepaymentProjPlanListDetail underLineProjDetail=null;   //线下费用的Detail
								RepaymentProjPlanListDetail onLineProjDetail=null;   //线上费用的Detail
								
//								if(projPList.getProjPlanListId().equals("f0bc7a22-c45e-4f9f-8ac8-ec2a993cc2c1")) {
//									  System.out.println("STOP");	
//									} 
								//如果已经全部收取本期应交的本息服务费费及线上逾期费用后则停止计算滞纳金
								if(getOnLineFactAmountSum(projPList.getProjPlanListId())>=getOnLinePlanAmountSum(projPList.getProjPlanListId())) {
									continue;
								}
								// 没有逾期,且不是你我金融生成
								if (isOverDue(new Date(), projPList.getDueDate()) >=0&&projPList.getCreatSysType()!=3) {
									continue;
									// 逾期的当前期
								} else {
									
									
									
									logger.info("===============：planListid:"+pList.getPlanListId()+"逾期费用计算开始===============");
									//获取平台对应标对应期的还款日期,取晚的日期
									Date platformDueDate=getPlatformDuedate(projPlan.getProjectId(), projPList.getPeriod().toString());
									BigDecimal onlineDueDays=BigDecimal.valueOf(0);//线上逾期天数
									BigDecimal underlineDueDays=BigDecimal.valueOf(0);//线下逾期天数
									if(platformDueDate!=null&&platformDueDate.compareTo(projPList.getDueDate())>0) {
										if(isOverDue(new Date(), platformDueDate)>=0) {
											onlineDueDays=BigDecimal.valueOf(0);
										}else {
											onlineDueDays=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), platformDueDate)));
										}
									
									}else {
										onlineDueDays=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), projPList.getDueDate())));
									}
									underlineDueDays=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), projPList.getDueDate())));
									
									projPList.setOverdueDays(underlineDueDays);
									BigDecimal underLateFee=getUnderLateFee(projPList,projList, projPlan,underlineDueDays).setScale(2, roundingMode);//线下逾期费
									underLateFeeSum=underLateFeeSum.add(underLateFee).setScale(2, roundingMode);
									underLineProjDetail=	updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), underLateFee);
									
									BigDecimal onlineLateFee=BigDecimal.valueOf(0);//线上逾期费
								
									if(onlineDueDays.compareTo(BigDecimal.valueOf(0))>0) {//如果线上的逾期天数为0,则不用计算线上滞纳金
										 onlineLateFee=getOnLineLateFee(projPList, projPlan,onlineDueDays).setScale(2, roundingMode);//线上逾期费
										onlineLateFeeSum=onlineLateFeeSum.add(onlineLateFee).setScale(2, roundingMode);
										onLineProjDetail=updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFee);
									}else {
										 onlineLateFee=BigDecimal.valueOf(0);
										onlineLateFeeSum=onlineLateFeeSum.add(onlineLateFee).setScale(2, roundingMode);
										onLineProjDetail=updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFee);
									}
							        
									projPList.setOverdueAmount(underLateFee.add(onlineLateFee));
									projPList.setCurrentStatus(RepayCurrentStatusEnums.逾期.name());
									updateById(projPList);
									logger.info("逾期费用===============：projListId:"+projPList.getProjPlanListId()+"线下逾期费:"+underLateFee+",线上逾期费:"+onlineLateFee+"==============");
								}
								
								//更新还款计划业务表
								updateOrInsertPlanDetail(pList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), underLateFeeSum,underLineProjDetail);//每个业务每期还款计划的线下收费
								if(onLineProjDetail!=null) {//线上逾期费不为0
									updateOrInsertPlanDetail(pList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFeeSum,onLineProjDetail);//每个业务每期还款计划的线上收费
								}
				
								BigDecimal days=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), pList.getDueDate())));//每个业务每期款还计划的逾期天数
								pList.setOverdueDays(days);
								pList.setOverdueAmount(getPlanListOverAmountSum(pList));
								pList.setCurrentStatus(RepayCurrentStatusEnums.逾期.name());
								repaymentBizPlanListService.updateById(pList);
								logger.info("===============：planListid:"+pList.getPlanListId()+"逾期费用计算结束===============");
							}
							
							
							
				    }
			
			}
			
		
			/**
			// 每个业务对应所有贷后生成的标的还款计划
			List<RepaymentProjPlan> projPlans = repaymentProjPlanService
					.selectList((new EntityWrapper<RepaymentProjPlan>().eq("creat_sys_type", 2))
							.eq("original_business_id", business.getBusinessId()));
            
			for (RepaymentProjPlan projPlan : projPlans) {
				// 每个标的还款计划对应所有标的列表
				List<RepaymentProjPlanList> projList = selectList((new EntityWrapper<RepaymentProjPlanList>()
						.eq("creat_sys_type", 2).eq("active", 1).ne("current_status", RepayCurrentStatusEnums.已还款)
						.ne("current_sub_status", RepayRegisterFinanceStatus.还款待确认)).eq("proj_plan_id",
								projPlan.getProjPlanId()));

				for (RepaymentProjPlanList projPList : projList) {
					// 没有逾期
					if (isOverDue(new Date(), projPList.getDueDate()) >=0) {
						continue;
						// 逾期的当前期
					} else {
						BigDecimal days=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), projPList.getDueDate())));//逾期天数
						projPList.setOverdueDays(days);
						updateById(projPList);
						
						BigDecimal underLateFee=getUnderLateFee(projList, projPlan);//线下逾期费
						updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), underLateFee);
						
						BigDecimal onlineLateFee=getOnLineLateFee(projList, projPlan);//线上逾期费
						updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFee);
					}
				}
			}
  */
		}
     
	}
    
    /**
     * 
     * 获取每期planList的滞纳金总和
     */
	private BigDecimal getPlanListOverAmountSum(RepaymentBizPlanList pList) {
		BigDecimal overAmountSum=BigDecimal.valueOf(0);
		List<RepaymentProjPlanListDetail>  lists=repaymentProjPlanListDetailService.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("plan_list_id", pList.getPlanListId()).eq("plan_item_type", 60)); 
		for(RepaymentProjPlanListDetail detail:lists) {
			overAmountSum=overAmountSum.add(detail.getProjPlanAmount()==null?BigDecimal.valueOf(0):detail.getProjPlanAmount());
		}
		return overAmountSum;
	}

	/**
		 *判断是否逾期,获取逾期天数
		 * @param nowDate
		 * @param repayDate
		 * @return
		 */
		private int isOverDue(Date nowDate, Date repayDate) {
		 	nowDate=DateUtil.getDate(DateUtil.formatDate(nowDate));
		 	repayDate=DateUtil.getDate(DateUtil.formatDate(repayDate));
			int i = DateUtil.getDiffDays(nowDate, repayDate);
			return i;
		}
		
		
		
	    /**
	     * 线上逾期费用
	     * @param projPlanList
	     * @param projPlan
	     * @return
	     */
		private BigDecimal getOnLineLateFee(RepaymentProjPlanList projPlanList,RepaymentProjPlan projPlan,BigDecimal days) {
			System.out.println("================="+projPlan.getOriginalBusinessId()+"====================");
			BigDecimal restPricipal=getRestPrincipal(projPlanList, projPlan);
			BigDecimal principalAndInterest=BigDecimal.valueOf(getPrincipalAndinterestPeriod(projPlanList.getProjPlanListId()));
			BigDecimal onLineLatefee=new BigDecimal("0");
		
			onLineLatefee=getLateFee(projPlan.getOnLineOverDueRate(), projPlan.getOnLineOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest, projPlan.getProjectId(),projPlanList.getPeriod());
			
			onLineLatefee=onLineLatefee.multiply(days);
			return onLineLatefee;
		}
		
		
	    /**
	     * 线下逾期费用
	     * @param projPlanList
	     * @param projPlan
	     * @return
	     */
		private BigDecimal getUnderLateFee(RepaymentProjPlanList projPlanList,List<RepaymentProjPlanList> projPlanLists,RepaymentProjPlan projPlan,BigDecimal days) {
			System.out.println("================="+projPlan.getOriginalBusinessId()+"====================");
			BigDecimal restPricipal=getRestPrincipal(projPlanList, projPlan);
			BigDecimal principalAndInterest=BigDecimal.valueOf(getPrincipalAndinterestPeriod(projPlanList.getProjPlanListId()));
			BigDecimal underLatefee=new BigDecimal("0");
		   List<RepaymentProjPlanList> list=selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlan.getProjPlanId()));
			Date lastDate=getLastDueDate(list);
			    //期外
			    if(new Date().compareTo(lastDate)>0) {
			    	underLatefee=getLateFee(projPlan.getOffLineOutOverDueRate(), projPlan.getOffLineOutOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest,projPlan.getProjectId(),projPlanList.getPeriod());
			    	
			    	
			    }
			    //期内
			    else {
			    	underLatefee=getLateFee(projPlan.getOffLineInOverDueRate(), projPlan.getOffLineInOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest,projPlan.getProjectId(),projPlanList.getPeriod());
					    	
			    }
			underLatefee=underLatefee.multiply(days);
			return underLatefee;
		}
	
	     /**
	      * 
	      * 获取每个标的还款计划中的最后一期的应还日期	
	      * @param projPlanList
	      * @return
	      */
		private Date getLastDueDate(List<RepaymentProjPlanList> projPlanList) {
			RepaymentProjPlanList projPlan=projPlanList.stream().max(new Comparator<RepaymentProjPlanList>() {

				@Override
				public int compare(RepaymentProjPlanList o1, RepaymentProjPlanList o2) {
					return o1.getDueDate().compareTo(o2.getDueDate());
				}
				
				
			}).get();
			
			
			return projPlan.getDueDate();
		}
		
		
		/**
		 * 计算这个标的进位方式和保留小数位
		 */
		
		private void getRondModeAndSmallNum(RepaymentProjPlan projPlan) {
			//'进位方式标志位 0：进一位，1：不进位，4：四舍五入, 6:银行家舍入法';
			if(projPlan.getRondmode()==null||projPlan.getSmallNum()==null) {
				projPlan.setRondmode(4);
				projPlan.setSmallNum(2);
			}
			switch(projPlan.getRondmode()) {
		    case 0:
		    	roundingMode=RoundingMode.UP;
		    	smallNum=projPlan.getSmallNum();
                break;
            case 1:
            	roundingMode=RoundingMode.DOWN;
            	smallNum=projPlan.getSmallNum();
                break;
            case 4:
            	roundingMode=RoundingMode.HALF_UP ;
            	smallNum=projPlan.getSmallNum();
                break;
            case 6:
            	roundingMode=RoundingMode.HALF_EVEN;
            	smallNum=projPlan.getSmallNum();
                break;
            default:
                break;
			}
			
		}
	    /**
	     * 计算出日利率
	     * @param rate
	     * @param rateUnit
	     * @return
	     */
	    private BigDecimal getDayRate(BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit){
	        BigDecimal dayRate;
	        switch (rateUnit){
	            case YEAR_RATE:
	            	dayRate = rate.divide(new BigDecimal("365"),10,roundingMode);
	                break;
	            case MONTH_RATE:
	            	dayRate = rate.divide(new BigDecimal("30"),10,roundingMode);
	                break;
	            case DAY_RATE:
	            	dayRate = rate;
	                break;
	            default:
	            	dayRate = rate;
	                break;
	        }
	        return dayRate.divide(new BigDecimal("100"),10,roundingMode);
	    }
	    
	    
	    /**
	     * 计算滞纳金
	     * @param rate
	     * @param
	     * @return
	     *
	     */
	    private BigDecimal getLateFee(BigDecimal rate,Integer rateType,BigDecimal borrowMoney,BigDecimal restPrincipal,BigDecimal principalAndInterest,String projId,Integer period) {
	    	BigDecimal lateFee=BigDecimal.valueOf(0);
	        switch (rateType){
	            case 1:
	            	lateFee =borrowMoney.multiply(rate.divide(new BigDecimal("100")));
	                break;
	            case 2:
	            	lateFee = restPrincipal.multiply(rate.divide(new BigDecimal("100")));
	                break;
	            case 3:
	            	lateFee = rate;
	                break;
	            case 4:
	            	lateFee = principalAndInterest.multiply(rate.divide(new BigDecimal("100")));
	                break;
	            case 5:
	            	lateFee=principalAndInterest(projId, period).multiply(rate.divide(new BigDecimal("100")));
	            	break;
	            	
	        }
	        return lateFee;
	    }
	    /**
	     * 获取平台垫付的本息
	     * @return
	     */
	private BigDecimal principalAndInterest(String projId, Integer period) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projId);
		Result result = null;
		try {
			result = eipRemote.queryProjectPayment(paramMap);
			if (result == null) {
				logger.info("调查询平台垫付记录接口出错");
			}

			if (result != null && result.getReturnCode().equals("0000")) {
				HashMap<String, HashMap<String, String>> map = (HashMap) result.getData();
				List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) map.get("projectPayments");
				Double principalAndInterest = 0.0;
				if (list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						String periods = String.valueOf(list.get(i).get("period"));
						if (period.toString().equals(periods)) {
							principalAndInterest = (Double) list.get(i).get("principalAndInterest");
						}
					}
				}
				if (principalAndInterest == 0) {
					return BigDecimal.valueOf(0);
				} else {
					return BigDecimal.valueOf(Double.valueOf(principalAndInterest));
				}
			} else {
				return BigDecimal.valueOf(0);
			}
		} catch (Exception e) {
			logger.error("获取平台垫付的本息" + e);
		}
		return BigDecimal.valueOf(0);
	}
	    
	    /**
	     * 剩余本金
	     * @param projList
	     * @param projPlan
	     * @return
	     */
	    private BigDecimal getRestPrincipal(RepaymentProjPlanList projList,RepaymentProjPlan projPlan) {
	    	BigDecimal repayAmount=BigDecimal.valueOf(getFactAmountSum(projPlan.getProjPlanId()));
	       BigDecimal restPrincipal=projPlan.getBorrowMoney().subtract(repayAmount);
			return restPrincipal;
	    }
	    
	    
	    
	    /**
	     * 更新repaymentProjPlanListDetail表记录或者插入新记录
	     * @param projPList
	     * @param feeId
	     * @param lateFee
	     */
		private RepaymentProjPlanListDetail updateOrInsertProjDetail(RepaymentProjPlanList projPList, String feeId, BigDecimal lateFee) {
 			RepaymentProjPlanListDetail projDetail = repaymentProjPlanListDetailService.selectOne(
					new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPList.getProjPlanListId())
							.eq("fee_id", feeId));
			try {
				if (projDetail != null) {
					projDetail.setProjPlanAmount(lateFee);
					repaymentProjPlanListDetailService.updateById(projDetail);
					
				} else {
					List<RepaymentProjPlanListDetail> projDetailList = repaymentProjPlanListDetailService
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
									projPList.getProjPlanListId()));
					
					List<RepaymentProjPlanListDetail> feeIdLists = repaymentProjPlanListDetailService
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("plan_list_id",
									projPList.getPlanListId()).eq("fee_id", feeId));
					
					if (projDetailList != null && projDetailList.size() > 0) {
					
						RepaymentProjPlanListDetail temp = projDetailList.get(0);
						projDetail = ClassCopyUtil.copyObject(temp,
								RepaymentProjPlanListDetail.class);
						projDetail.setProjPlanDetailId(UUID.randomUUID().toString());
						projDetail.setFeeId(feeId);
						projDetail.setProjPlanAmount(lateFee);
						projDetail.setPlanItemType(60);
						String planDetailId="";
						for(RepaymentProjPlanListDetail detail:feeIdLists) {
							if(detail.getFeeId().equals(feeId)) {
								planDetailId=detail.getPlanDetailId();
							}
						}
						if(StringUtil.isEmpty(planDetailId)) {
							projDetail.setPlanDetailId(UUID.randomUUID().toString());
						}else {
							projDetail.setPlanDetailId(planDetailId);
						}
						 
						projDetail.setPlanItemName("滞纳金");
						repaymentProjPlanListDetailService.insertOrUpdate(projDetail);
					}
				}
	
			} catch (Exception e) {
	           
			}
			return projDetail;
	
		}
		
		
		
	    /**
	     * 更新repaymentBizPlanListDetail表记录或者插入新记录
	     * @param pList
	     * @param feeId
	     * @param lateFee
	     */
		private void updateOrInsertPlanDetail(RepaymentBizPlanList pList, String feeId, BigDecimal lateFee,RepaymentProjPlanListDetail projDetail) {
			RepaymentBizPlanListDetail pDetail = repaymentBizPlanListDetailService.selectOne(
					new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId())
							.eq("fee_id", feeId));
			
			BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getOrigBusinessId()));
			try {
				if (pDetail != null) {
					pDetail.setPlanAmount(lateFee);
					repaymentBizPlanListDetailService.updateById(pDetail);
				} else {
					List<RepaymentBizPlanListDetail> pDetails = repaymentBizPlanListDetailService
							.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",
									pList.getPlanListId()));
					if (pDetails != null && pDetails.size() > 0) {
						RepaymentBizPlanListDetail temp = pDetails.get(0);
						RepaymentBizPlanListDetail copy = ClassCopyUtil.copyObject(temp,
								RepaymentBizPlanListDetail.class);
						copy.setPlanDetailId(projDetail.getPlanDetailId());
						copy.setFeeId(feeId);
						copy.setPlanAmount(lateFee);
						copy.setShareProfitIndex(profitItemSetService.getLevel(business.getBusinessType().toString(), RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getValue().intValue(), RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid()).get("feeLevel"));
						copy.setPlanItemType(60);
						copy.setPlanItemName("滞纳金");
						repaymentBizPlanListDetailService.insertOrUpdate(copy);
					}
				}
	
			} catch (Exception e) {
	           
			}
	
		}

		@Override
		public Double getOnLinePlanAmountSum(String projListId) {
			return repaymentProjPlanListMapper.getOnLinePlanAmountSum(projListId);
		}
		@Override
		public Double getOnLineFactAmountSum(String projListId) {
			return repaymentProjPlanListMapper.getOnLineFactAmountSum(projListId);
		}
		@Override
		public Double getFactAmountSum(String projPlanId) {
			return repaymentProjPlanListMapper.getFactAmountSum(projPlanId);
		}
		
		@Override
		public Double getPrincipalAndinterestPeriod(String projListId) {
			return repaymentProjPlanListMapper.getPrincipalAndInterestPeriod(projListId);
		}
		
		/**
		 * 获取平台对应期数的还款日期
		 * @param projId
		 * @param periods
		 * @return
		 */
		private Date getPlatformDuedate(String projId,String periods) {
			logger.info("====================标ID:"+projId+"获取平台标的期数为："+periods+"的还款日日期=====开始=======================================");
			String dueDate="";
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId",projId);
			paramMap.put("type", "1");
			Result result=null;
			try {
				  result = eipRemote.queryProjectRepayment(paramMap);
			}catch(Exception e) {
				logger.info("还款日期获取失败"+e);
			}
			if(result!=null) {
				if(result.getReturnCode().equals("0000")) {
					HashMap<String,HashMap<String,String>> map=(HashMap) result.getData();
					List<HashMap<String,String>> list=(List<HashMap<String, String>>) map.get("RepaymentList");
					for(int i=0;i<list.size();i++) {
						String period=String.valueOf(list.get(i).get("Periods"));
						if(period.equals(periods))
						{
							dueDate= list.get(i).get("CycDate").toString();
							logger.info("平台还款日期为:"+dueDate);
						}
					}
					if(!StringUtil.isEmpty(dueDate)) {
						return DateUtil.getDate(dueDate);
					}else {
						return null;
					}
				}else {
					return null;
				}
			}else {
				return null;
			}
		
		
			
		}
		
	
		@Override
		public List<RepaymentProjPlanList> getProListForCalLateFee(String projListId) {
			
			return repaymentProjPlanListMapper.getProListForCalLateFee(projListId);
		} 
		 

} 

