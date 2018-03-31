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

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.common.util.DateUtil;

/**
 * @author 王继光
 * 2018年3月30日 上午10:00:23
 */
public class ExpenseSettleRepaymentPlanVO  {
	private Logger logger = LoggerFactory.getLogger(ExpenseSettleRepaymentPlanVO.class) ;
	private RepaymentBizPlan repaymentBizPlan ;

	private List<ExpenseSettleRepaymentPlanListVO> repaymentPlanListVOs ;
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
	public ExpenseSettleRepaymentPlanVO(RepaymentBizPlan plan,List<RepaymentBizPlanList> planLists,List<RepaymentBizPlanListDetail> details) {
		super();
		this.repaymentBizPlan = plan ;
		planLists.sort(new PlanListSortor());
		details.sort(new DetailSortor());
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
		}
	}

	/**
	 * @return the repaymentBizPlan
	 */
	public RepaymentBizPlan getRepaymentBizPlan() {
		return repaymentBizPlan;
	}

	/**
	 * @param repaymentBizPlan the repaymentBizPlan to set
	 */
	public void setRepaymentBizPlan(RepaymentBizPlan repaymentBizPlan) {
		this.repaymentBizPlan = repaymentBizPlan;
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
		List<ExpenseSettleRepaymentPlanListVO> expenseSettleRepaymentPlanListVOs = new ArrayList<>() ;
		int diff = 0 ;
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
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : expenseSettleRepaymentPlanListVOs) {
			int year = DateUtil.getDay(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			int month = DateUtil.getMonth(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate());
			
			if (compareYear==year&&compareMonth==month) {
				expenseSettleRepaymentPlanListVOs.add(expenseSettleRepaymentPlanListVO) ;
			}
		}
		
		
		return getRepaymentPlanListVOs();
		
	}
	
	
	/**
	 * 结清试算查找当前期的details
	 * @author 王继光
	 * 2018年3月30日 下午2:57:15
	 * @param settleDate
	 * @return
	 */
	public List<RepaymentBizPlanListDetail> findCurrentDetails(Date settleDate){
		List<RepaymentBizPlanListDetail> details = new ArrayList<>() ;
		List<ExpenseSettleRepaymentPlanListVO>  currentPeriods = findCurrentPeriods(settleDate);
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : currentPeriods) {
			details.addAll(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanListDetails());
		}
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
		BigDecimal res = new BigDecimal(0);
		for (RepaymentBizPlanListDetail detail : findCurrentDetails(settleDate)) {
			if (detail.getPlanItemType().equals(itemType)) {
				if (factAmount) {
					res=res.add(detail.getFactAmount());
				}else {
					res=res.add(detail.getPlanAmount()==null?new BigDecimal(0):detail.getPlanAmount());
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
		return this.getRepaymentPlanListVOs().get(this.getRepaymentPlanListVOs().size()-1);
	}
	
	/**
	 * 获取所有detail
	 * @author 王继光
	 * 2018年3月30日 下午5:50:59
	 * @return
	 */
	public List<RepaymentBizPlanListDetail> allDetails(){
		List<RepaymentBizPlanListDetail> list = new ArrayList<>();new ArrayList<>();
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : getRepaymentPlanListVOs()) {
			list.addAll(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanListDetails());
		}
		return list ;
	}
	
	/**
	 * 获取往期还款计划
	 * @author 王继光
	 * 2018年3月30日 下午6:18:10
	 * @param settleDate
	 * @return
	 */
	public List<ExpenseSettleRepaymentPlanListVO> findPastPeriods(Date settleDate){
		List<ExpenseSettleRepaymentPlanListVO> pastPeriods = new ArrayList<>();
		List<ExpenseSettleRepaymentPlanListVO> currentPeriods = findCurrentPeriods(settleDate);
		Date currentPeriodDate = currentPeriods.get(0).getRepaymentBizPlanList().getDueDate() ;
		for (ExpenseSettleRepaymentPlanListVO expenseSettleRepaymentPlanListVO : getRepaymentPlanListVOs()) {
			int res = DateUtil.dateCompare(expenseSettleRepaymentPlanListVO.getRepaymentBizPlanList().getDueDate(), currentPeriodDate) ;
			if (res<0) {
				pastPeriods.add(expenseSettleRepaymentPlanListVO) ;
			}
		}
		
		
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
}
