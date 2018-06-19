package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.repayPlan.RepayPlanBorrowRateUnitEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.finance.controller.CalLateFeeController;
import com.ht.ussp.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
				List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList((new EntityWrapper<RepaymentBizPlanList>()
							.eq("src_type", 2).ne("current_status", RepayCurrentStatusEnums.已还款.name())
							.ne("current_sub_status", RepayRegisterFinanceStatus.还款待确认.name()).or().isNull("current_sub_status")).eq("plan_id",
									plan.getPlanId()));
				
				    for(RepaymentBizPlanList pList:pLists) {
				    	// 每个业务的还款计划列表对应所有标的还款计划列表
						List<RepaymentProjPlanList> projList = selectList((new EntityWrapper<RepaymentProjPlanList>()
								.eq("creat_sys_type", 2).ne("current_status", RepayCurrentStatusEnums.已还款.name())
								.ne("current_sub_status", RepayRegisterFinanceStatus.还款待确认.name()).or().isNull("current_sub_status")).eq("plan_list_id",
										pList.getPlanListId()));
						BigDecimal underLateFeeSum=BigDecimal.valueOf(0);//每个业务每期还款计划的线下收费
						BigDecimal onlineLateFeeSum=BigDecimal.valueOf(0);//每个业务每期还款计划的线上收费
						
						
						
							for (RepaymentProjPlanList projPList : projList) {
							
								// 每个表的还款计划列表对应所的标的还款计划
								RepaymentProjPlan projPlan = repaymentProjPlanService
										.selectOne((new EntityWrapper<RepaymentProjPlan>().eq("creat_sys_type", 2))
												.eq("proj_plan_id", projPList.getProjPlanId()));
								//如果已经全部收取本期应交的本息服务费费及线上逾期费用后则停止计算滞纳金
								if(getOnLineFactAmountSum(projPList.getProjPlanListId())>=getOnLinePlanAmountSum(projPList.getProjPlanListId())) {
									continue;
								}
								// 没有逾期,若逾期一天不计算逾期费用，逾期> 1天按实际逾期天数进行计算
								if (isOverDue(new Date(), projPList.getDueDate()) >=0||isOverDue(new Date(), projPList.getDueDate())==-1) {
									continue;
									// 逾期的当前期
								} else {
									logger.info("逾期费用计算开始===============：planListid:"+pList.getPlanListId()+"===============");
									BigDecimal days=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), projPList.getDueDate())));//逾期天数
									projPList.setOverdueDays(days);
								
									
									BigDecimal underLateFee=getUnderLateFee(projPList,projList, projPlan);//线下逾期费
									underLateFeeSum=underLateFeeSum.add(underLateFee);
									updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), underLateFee);
									
									BigDecimal onlineLateFee=getOnLineLateFee(projPList, projPlan);//线上逾期费
									onlineLateFeeSum=onlineLateFeeSum.add(onlineLateFee);
									updateOrInsertProjDetail(projPList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFee);
									projPList.setOverdueAmount(underLateFee.add(onlineLateFee));
									projPList.setCurrentStatus(RepayCurrentStatusEnums.逾期.name());
									updateById(projPList);
									logger.info("逾期费用===============：projListId:"+projPList.getProjPlanListId()+"线下逾期费:"+underLateFee+",线上逾期费:"+onlineLateFee+"==============");
								}
								
								//更新还款计划业务表
								updateOrInsertPlanDetail(pList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), underLateFeeSum);//每个业务每期还款计划的线下收费
								updateOrInsertPlanDetail(pList, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), onlineLateFeeSum);//每个业务每期还款计划的线上收费
								
								BigDecimal days=BigDecimal.valueOf(Math.abs(isOverDue(new Date(), pList.getDueDate())));//每个业务每期款还计划的逾期天数
								pList.setOverdueDays(days);
								pList.setOverdueAmount(underLateFeeSum.add(onlineLateFeeSum));
								pList.setCurrentStatus(RepayCurrentStatusEnums.逾期.name());
								repaymentBizPlanListService.updateById(pList);
								logger.info("逾期费用计算结束===============：planListid:"+pList.getPlanListId()+"===============");
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
		private BigDecimal getOnLineLateFee(RepaymentProjPlanList projPlanList,RepaymentProjPlan projPlan) {
			BigDecimal restPricipal=getRestPrincipal(projPlanList, projPlan);
			BigDecimal principalAndInterest=BigDecimal.valueOf(getPrincipalAndinterestPeriod(projPlanList.getProjPlanListId()));
			BigDecimal onLineLatefee=new BigDecimal(0);
		
			onLineLatefee=getLateFee(projPlan.getOnLineOverDueRate(), projPlan.getOnLineOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest);
			return onLineLatefee;
		}
		
		
	    /**
	     * 线下逾期费用
	     * @param projPlanList
	     * @param projPlan
	     * @return
	     */
		private BigDecimal getUnderLateFee(RepaymentProjPlanList projPlanList,List<RepaymentProjPlanList> projPlanLists,RepaymentProjPlan projPlan ) {
			BigDecimal restPricipal=getRestPrincipal(projPlanList, projPlan);
			BigDecimal principalAndInterest=BigDecimal.valueOf(getPrincipalAndinterestPeriod(projPlanList.getProjPlanListId()));
			BigDecimal underLatefee=new BigDecimal(0);
		   List<RepaymentProjPlanList> list=selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlan.getProjPlanId()));
			Date lastDate=getLastDueDate(list);
			    //期外
			    if(new Date().compareTo(lastDate)>0) {
			    	underLatefee=getLateFee(projPlan.getOffLineOutOverDueRate(), projPlan.getOffLineOutOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest);
			    	
			    	
			    }
			    //期内
			    else {
			    	underLatefee=getLateFee(projPlan.getOffLineInOverDueRate(), projPlan.getOffLineInOverDueRateType(), projPlan.getBorrowMoney(), restPricipal, principalAndInterest);
					    	
			    }
			
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
	     * 计算出日利率
	     * @param rate
	     * @param rateUnit
	     * @return
	     */
	    private BigDecimal getDayRate(BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit){
	        BigDecimal dayRate;
	        switch (rateUnit){
	            case YEAR_RATE:
	            	dayRate = rate.divide(new BigDecimal(365),10,roundingMode);
	                break;
	            case MONTH_RATE:
	            	dayRate = rate.divide(new BigDecimal(30),10,roundingMode);
	                break;
	            case DAY_RATE:
	            	dayRate = rate;
	                break;
	            default:
	            	dayRate = rate;
	                break;
	        }
	        return dayRate.divide(new BigDecimal(100),10,roundingMode);
	    }
	    
	    
	    /**
	     * 计算滞纳金
	     * @param rate
	     * @param rateUnit
	     * @return
	     *
	     */
	    private BigDecimal getLateFee(BigDecimal rate,Integer rateType,BigDecimal borrowMoney,BigDecimal restPrincipal,BigDecimal principalAndInterest) {
	    	BigDecimal lateFee=BigDecimal.valueOf(0);
	        switch (rateType){
	            case 1:
	            	lateFee =borrowMoney.multiply(rate.divide(new BigDecimal(100)));
	                break;
	            case 2:
	            	lateFee = restPrincipal.multiply(rate.divide(new BigDecimal(100)));
	                break;
	            case 3:
	            	lateFee = rate;
	                break;
	            case 4:
	            	lateFee = principalAndInterest.multiply(rate);
	                break;
	        }
	        return lateFee;
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
		private void updateOrInsertProjDetail(RepaymentProjPlanList projPList, String feeId, BigDecimal lateFee) {
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
					if (projDetailList != null && projDetailList.size() > 0) {
						RepaymentProjPlanListDetail temp = projDetailList.get(0);
						RepaymentProjPlanListDetail copy = ClassCopyUtil.copyObject(temp,
								RepaymentProjPlanListDetail.class);
						copy.setProjPlanDetailId(UUID.randomUUID().toString());
						copy.setFeeId(feeId);
						copy.setProjPlanAmount(lateFee);
						copy.setPlanItemType(60);
						copy.setPlanItemName("滞纳金");
						repaymentProjPlanListDetailService.insertOrUpdate(copy);
					}
				}
	
			} catch (Exception e) {
	           
			}
	
		}
		
		
		
	    /**
	     * 更新repaymentBizPlanListDetail表记录或者插入新记录
	     * @param pList
	     * @param feeId
	     * @param lateFee
	     */
		private void updateOrInsertPlanDetail(RepaymentBizPlanList pList, String feeId, BigDecimal lateFee) {
			RepaymentBizPlanListDetail pDetail = repaymentBizPlanListDetailService.selectOne(
					new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId())
							.eq("fee_id", feeId));
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
						copy.setPlanDetailId(UUID.randomUUID().toString());
						copy.setFeeId(feeId);
						copy.setPlanAmount(lateFee);
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
		
		public static void main(String[] args) {
			Date date1=DateUtil.getDate("2018-05-19", "yyyy-MM-dd");
			Date date2=DateUtil.getDate("2018-05-15", "yyyy-MM-dd");
			System.out.println(DateUtil.getDiffDays(date1, date2));
		} 
		 

} 

