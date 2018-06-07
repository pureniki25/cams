/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.common.util.DateUtil;

/**
 * @author 王继光
 * 2018年3月30日 上午10:00:23
 */
public class ExpenseSettleRepaymentPlanVO  {
	
	private Logger logger = LoggerFactory.getLogger(ExpenseSettleRepaymentPlanVO.class) ;
	private List<RepaymentBizPlan> repaymentBizPlans ;
	private List<ExpenseSettleRepaymentPlanListVO> repaymentPlanListVOs ;
	
	private List<ExpenseSettleRepaymentPlanListVO> currentPeriodVOs ;
	private List<RepaymentBizPlanListDetail> currentDetails ;
	private List<RepaymentBizPlanListDetail> allDetails ;
	private List<ExpenseSettleRepaymentPlanListVO> pastPeriodVOs ;
	private List<ExpenseSettleRepaymentPlanListVO> surplusPeriodVOs ;
	private ExpenseSettleRepaymentPlanListVO finalPeriod ;
	private ExpenseSettleRepaymentPlanListVO currentPeriod;
	private Boolean isDefer = false ;
	class PlanListSortor implements Comparator<RepaymentBizPlanList> {

		@Override
		public int compare(RepaymentBizPlanList o1, RepaymentBizPlanList o2) {
			return DateUtil.getDiffDays(o1.getDueDate(), o2.getDueDate());
		}
		
	}
	
	class DetailSortor implements Comparator<RepaymentBizPlanListDetail> {
		@Override
		public int compare(RepaymentBizPlanListDetail o1, RepaymentBizPlanListDetail o2) {
			return o1.getPeriod() - o2.getPeriod();
		}
		
	}
	/**
	 * 
	 */
	public ExpenseSettleRepaymentPlanVO(List<RepaymentBizPlan > plans,List<RepaymentBizPlanList> planLists,List<RepaymentBizPlanListDetail> details) {
		super();
		this.repaymentBizPlans = plans ;
		this.setRepaymentPlanListVOs(new ArrayList<>()) ;
		
		for (RepaymentBizPlanList planList : planLists) {
			ExpenseSettleRepaymentPlanListVO vo = new ExpenseSettleRepaymentPlanListVO() ;
			vo.setRepaymentBizPlanList(planList);
			
			List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = new ArrayList<>() ;
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
				if (repaymentBizPlanListDetail.getPlanListId().equals(planList.getPlanListId())) {
					repaymentBizPlanListDetails.add(repaymentBizPlanListDetail);
				}
			}
			vo.setRepaymentBizPlanListDetails(repaymentBizPlanListDetails);
			this.getRepaymentPlanListVOs().add(vo);
			String businessId = planList.getBusinessId();
			String orgBusinessId = planList.getOrigBusinessId();
			if (!businessId.equals(orgBusinessId)&&!getIsDefer()) {
				setIsDefer(true);
			}
		}
	}

	
	/**
	 * 
	 * 结清试算查找试算当前期
	 * @author 王继光
	 * 2018年3月30日 上午11:56:26
	 * @param settleDate
	 * @return
	 */
	public List<ExpenseSettleRepaymentPlanListVO> findCurrentPeriods(Date settleDate){
		if (currentPeriodVOs!=null&&currentPeriodVOs.size()>0) {
			return currentPeriodVOs ;
		}
		ExpenseSettleRepaymentPlanListVO finalPeriod = findFinalPeriod();
		this.finalPeriod=finalPeriod;
		//diff>0;应还日期大于结清日期
		//diff=0;应还日期等于结清日期
		//diff<0;应还日期小于结清日期;
		int diff = DateUtil.getDiffDays(settleDate, finalPeriod.getRepaymentBizPlanList().getDueDate());
		//提前结清
		if (diff>0) {
			for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : getRepaymentPlanListVOs()) {
				diff = DateUtil.getDiffDays(settleDate,expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
				if (diff>=0) {
					currentPeriod = expenseSettleRepaymentPlanListVO ;
					break ;
				}
			}
		}else {
			//期外结清
			currentPeriod = finalPeriod ;
		}
		
		
		
		List<ExpenseSettleRepaymentPlanListVO> expenseSettleRepaymentPlanListVOs = new ArrayList<>() ;
		
		logger.info("CurrentPeriod:"+currentPeriod.getRepaymentBizPlanList().getAfterId());
		int compareYear = DateUtil.getYear(currentPeriod.getRepaymentBizPlanList().getDueDate());
		int compareMonth = DateUtil.getMonth(currentPeriod.getRepaymentBizPlanList().getDueDate());
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : this.getRepaymentPlanListVOs()) {
			int year = DateUtil.getYear(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			int month = DateUtil.getMonth(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			
			if (compareYear==year&&compareMonth==month) {
				expenseSettleRepaymentPlanListVOs.add(expenseSettleRepaymentPlanListVO) ;
			}
		}
		
		
		
		
		
		
		
		
		
		/*int diff = 0 ;
		ExpenseSettleRepaymentPlanListVO compare = null ;
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : this.getRepaymentPlanListVOs()) {
			int t = DateUtil.getDiffDays(settleDate,expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			int abs = Math.abs(t);
			if (compare==null) {
				diff = t;
				compare = expenseSettleRepaymentPlanListVO ;
			}else {
				if (abs<diff) {
					diff = abs ;
					compare = expenseSettleRepaymentPlanListVO ;
				}
			}
		}
		
		logger.info("CurrentPeriod:"+compare.getRepaymentBizPlanList().getAfterId());
		int compareYear = DateUtil.getYear(compare.getRepaymentBizPlanList().getDueDate());
		int compareMonth = DateUtil.getMonth(compare.getRepaymentBizPlanList().getDueDate());
		if (compareMonth==12) {
			compareYear = compareYear + 1 ;
			compareMonth = 1 ;
		}else {
			compareMonth = compareMonth + 1 ;
		}
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : this.getRepaymentPlanListVOs()) {
			int year = DateUtil.getYear(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			int month = DateUtil.getMonth(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			
			if (compareYear==year&&compareMonth==month) {
				expenseSettleRepaymentPlanListVOs.add(expenseSettleRepaymentPlanListVO) ;
			}
		}
		
		if (expenseSettleRepaymentPlanListVOs.isEmpty()) {
			compareYear = DateUtil.getYear(compare.getRepaymentBizPlanList().getDueDate());
			compareMonth = DateUtil.getMonth(compare.getRepaymentBizPlanList().getDueDate());
			for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : this.getRepaymentPlanListVOs()) {
				int year = DateUtil.getYear(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
				int month = DateUtil.getMonth(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
				
				if (compareYear==year&&compareMonth==month) {
					expenseSettleRepaymentPlanListVOs.add(expenseSettleRepaymentPlanListVO) ;
				}
			}
		}*/
		
		Collections.sort(expenseSettleRepaymentPlanListVOs);
		currentPeriodVOs = expenseSettleRepaymentPlanListVOs ;
		return expenseSettleRepaymentPlanListVOs;
		
	}
	
	
	/**
	 * 结清试算查找当前期的details
	 * @author 王继光
	 * 2018年3月30日 下午2:57:15
	 * @param settleDate
	 * @return
	 */
	public List<RepaymentBizPlanListDetail> findCurrentDetails(Date settleDate){
		if (currentDetails!=null&&currentDetails.size()>0) {
			return currentDetails ;
		}
		List<RepaymentBizPlanListDetail> details = new ArrayList<>() ;
		List<ExpenseSettleRepaymentPlanListVO>  currentPeriods = findCurrentPeriods(settleDate);
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : currentPeriods) {
			details.addAll(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanListDetails());
		}
		currentDetails = details ;
		return details;
	}
	
	/**
	 * 
	 * @author 王继光
	 * 2018年3月30日 下午3:10:15
	 * @param settleDate 试结清时间
	 * @param itemType 应还项目所属分类10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,
	 * @param factAmount true:计算实还,false:计算应还
	 * @return
	 */
	public BigDecimal calCurrentDetails(Date settleDate,Integer itemType,boolean factAmount) {
		BigDecimal res = new BigDecimal("0");
		for (RepaymentBizPlanListDetail detail : findCurrentDetails(settleDate)) {
			if (detail.getPlanItemType().equals(itemType)&&detail.getAccountStatus()!=null&&!detail.getAccountStatus().equals(0)) {
				if (factAmount) {
					res=res.add(detail.getFactAmount()==null?new BigDecimal("0"):detail.getFactAmount());
				}else {
					res=res.add(detail.getPlanAmount()==null?new BigDecimal("0"):detail.getPlanAmount());
				}
			}
		}
		return res ;
	}
	
	
	/**
	 * 结清试算获取所有还款计划最后一期
	 * @author 王继光
	 * 2018年3月30日 下午5:27:21
	 * @return
	 */
	public ExpenseSettleRepaymentPlanListVO findFinalPeriod() {
		if (finalPeriod!=null) {
			return finalPeriod ;
		}
		finalPeriod = this.getRepaymentPlanListVOs().get(this.getRepaymentPlanListVOs().size()-1) ;
		return finalPeriod ;
	}
	
	/**
	 * 获取所有detail
	 * @author 王继光
	 * 2018年3月30日 下午5:50:59
	 * @return
	 */
	public List<RepaymentBizPlanListDetail> allDetails(){
		if (allDetails!=null&&allDetails.size()>0) {
			return allDetails ;
		}
		List<RepaymentBizPlanListDetail> list = new ArrayList<>();new ArrayList<>();
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : getRepaymentPlanListVOs()) {
			list.addAll(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanListDetails());
		}
		allDetails = list ;
		return list ;
	}
	
	/**
	 * 查找所有已还款的还款计划
	 * @author 王继光
	 * 2018年5月22日 下午1:57:10
	 * @return
	 */
	public List<RepaymentBizPlanList> findRepaidPeriods(){
		List<ExpenseSettleRepaymentPlanListVO> list = this.repaymentPlanListVOs ;
		List<RepaymentBizPlanList> res = new ArrayList<>() ;
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : list) {
			RepaymentBizPlanList repaymentBizPlanList =  expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList();
			if (repaymentBizPlanList.getCurrentStatus().equals(RepayPlanStatus.REPAYED.getName())) {
				res.add(repaymentBizPlanList);
			}
		}
		return res;
		
	}
	
	/**
	 * 获取往期还款计划
	 * @author 王继光
	 * 2018年3月30日 下午6:18:10
	 * @param settleDate
	 * @return
	 */
	public List<ExpenseSettleRepaymentPlanListVO> findPastPeriods(Date settleDate){
		if (pastPeriodVOs!=null&&pastPeriodVOs.size()>0) {
			return pastPeriodVOs ;
		}
		List<ExpenseSettleRepaymentPlanListVO> pastPeriods = new ArrayList<>();
		List<ExpenseSettleRepaymentPlanListVO> currentPeriods = findCurrentPeriods(settleDate);
		Date currentPeriodDate = currentPeriods.get(0).getRepaymentBizPlanList().getDueDate() ;
		int cyear = DateUtil.getYear(currentPeriodDate);
		int cmonth = DateUtil.getMonth(currentPeriodDate);
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : getRepaymentPlanListVOs()) {
			int year = DateUtil.getYear(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			int month = DateUtil.getMonth(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			
			if (year<cyear||(year==cyear&&month<cmonth)) {
				pastPeriods.add(expenseSettleRepaymentPlanListVO);
			}
		}
		Collections.sort(pastPeriods);
		pastPeriodVOs = pastPeriods ;
		return pastPeriods ;
	}

	/**
	 * @return the repaymentPlanListVOs
	 */
	public List<ExpenseSettleRepaymentPlanListVO> getRepaymentPlanListVOs() {
		return repaymentPlanListVOs;
	}

	/**
	 * @param repaymentPlanListVOs the repaymentPlanListVOs to set
	 */
	public void setRepaymentPlanListVOs(List<ExpenseSettleRepaymentPlanListVO> repaymentPlanListVOs) {
		this.repaymentPlanListVOs = repaymentPlanListVOs;
	}

	public ExpenseSettleRepaymentPlanListVO getFinalPeriod() {
		return finalPeriod;
	}

	public void setFinalPeriod(ExpenseSettleRepaymentPlanListVO finalPeriod) {
		this.finalPeriod = finalPeriod;
	}

	public ExpenseSettleRepaymentPlanListVO getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(ExpenseSettleRepaymentPlanListVO currentPeriod) {
		this.currentPeriod = currentPeriod;
	}
	
	/**
	 * 求剩余的还款计划(先用findCurrentPeriods找到当前期后再使用此方法)
	 * @author 王继光
	 * 2018年5月15日 上午10:48:53
	 * @return
	 */
	public List<ExpenseSettleRepaymentPlanListVO> getSurplusPeriod() {
		
		if (this.surplusPeriodVOs!=null&&this.surplusPeriodVOs.size()>0) {
			return surplusPeriodVOs ;
		}
		
		List<ExpenseSettleRepaymentPlanListVO> surplus = new ArrayList<>() ;
		if (currentPeriodVOs==null||currentPeriodVOs.size()==0) {
			return null ;
		}
		
		Date lastCurrDate = currentPeriodVOs.get(currentPeriodVOs.size()-1).getRepaymentBizPlanList().getDueDate();
		this.surplusPeriodVOs = new ArrayList<>() ;
		for (ExpenseSettleRepaymentPlanListVO e : getRepaymentPlanListVOs()) {
			Date compareDate = e.getRepaymentBizPlanList().getDueDate() ;
			if (DateUtil.getDiffDays(lastCurrDate, compareDate)>0) {
				surplusPeriodVOs.add(e);
			}
		}
		return surplusPeriodVOs;
		
	}

	/**
	 * 求剩余的期数
	 * @author 王继光
	 * 2018年5月22日 下午5:25:54
	 * @param settleDate
	 * @return
	 */
	public Integer getSurplusPeriodSize(Date settleDate) {
		if (currentPeriodVOs==null||currentPeriodVOs.size()==0) {
			this.findCurrentPeriods(settleDate);
		}
//		Integer surplusDefer = 0 ;
//		for (ExpenseSettleRepaymentPlanListVO planListVO : getSurplusPeriod()) {
//			RepaymentBizPlanList repaymentBizPlanList = planListVO.getRepaymentBizPlanList();
//			String businessId = repaymentBizPlanList.getBusinessId();
//			String orgBusinessId = repaymentBizPlanList.getOrigBusinessId();
//			if (!businessId.equals(orgBusinessId)) {
//				surplusDefer++;
//			}
//		}
		Integer periodCount = 0;
		Integer orgPeriods = 0 ;
		Integer renewPeriods = 0 ;
		for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
			periodCount += repaymentBizPlan.getBorrowLimit();
			if (repaymentBizPlan.getBusinessId().equals(repaymentBizPlan.getOriginalBusinessId())) {
				orgPeriods = repaymentBizPlan.getBorrowLimit();
			}else {
				renewPeriods+=repaymentBizPlan.getBorrowLimit();
			}
		}
		RepaymentBizPlanList finalCurrentPeriod = currentPeriodVOs.get(currentPeriodVOs.size()-1).getRepaymentBizPlanList();
		Integer currentPeriod = finalCurrentPeriod.getPeriod() ; ;
		if (!finalCurrentPeriod.getBusinessId().equals(finalCurrentPeriod.getOrigBusinessId())) {
			if (finalCurrentPeriod.getPeriod()==0) {
				RepaymentBizPlanList firstCurrentPeriod = currentPeriodVOs.get(0).getRepaymentBizPlanList();
				currentPeriod = firstCurrentPeriod.getPeriod() ;
			}
			currentPeriod += orgPeriods;
		}
		return periodCount-currentPeriod;
		
	}

	/**
	 * @return the isDefer
	 */
	public Boolean getIsDefer() {
		return isDefer;
	}

	/**
	 * @param isDefer the isDefer to set
	 */
	public void setIsDefer(Boolean isDefer) {
		this.isDefer = isDefer;
	}


	/**
	 * @return the repaymentBizPlans
	 */
	public List<RepaymentBizPlan> getRepaymentBizPlans() {
		return repaymentBizPlans;
	}

	/**
	 * @param repaymentBizPlans the repaymentBizPlans to set
	 */
	public void setRepaymentBizPlans(List<RepaymentBizPlan> repaymentBizPlans) {
		this.repaymentBizPlans = repaymentBizPlans;
	}

}
