/**
 * 
 */
package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.BizOutputRecordMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.common.util.DateUtil;

/**
 * @author 王继光 2018年3月12日 下午5:10:46
 */
@Service("ExpenseSettleService")
public class ExpenseSettleServiceImpl implements ExpenseSettleService {

	@Autowired
	BasicBusinessMapper basicBusinessMapper;
	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;
	
	@Autowired
	BizOutputRecordMapper bizOutputRecordMapper ;

	@Override
	public ExpenseSettleVO cal(String preSettleDate, String businessId) {
		BasicBusiness business = basicBusinessMapper.selectById(businessId);
		if (business == null) {
			throw new RuntimeException("业务不存在");
		}
		
		List<BizOutputRecord> bizOutputRecords = bizOutputRecordMapper.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date",false));
		
		if (bizOutputRecords==null) {
			throw new RuntimeException("业务尚未出款");
		}
		
		Date outPutDate = bizOutputRecords.get(0).getFactOutputDate();
		
		/*2017.3.22前出款或展期不收取；
		2017.3.22-2017.6.5之间出款或展期的前置收取；
		2017.6.5之后出款或展期的后置收取；*/

		String serviceChargeRule = "" ;
		if (outPutDate.before(DateUtil.getDate("2017-03-22", "yyyy-MM-dd"))) {
			serviceChargeRule = "" ;
		}else if (outPutDate.after(DateUtil.getDate("2017-03-22","yyyy-MM-dd"))&&outPutDate.before(DateUtil.getDate("2017-06-05","yyyy-MM-dd"))) {
			serviceChargeRule = "pre" ;
		}else {
			serviceChargeRule = "next" ;
		}
		
		
		
		
		BigDecimal businessRate = business.getBorrowRate();
		int rateUnit = business.getBorrowRateUnit();

		if (rateUnit == 1) {
			businessRate = businessRate.divide(new BigDecimal(12), 10);
		}

		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId)
						.le("due_date", preSettleDate).orderBy("due_date", false));

		List<RepaymentBizPlanList> residualRepaymentBizPlanLists = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId)
						.gt("due_date", preSettleDate).orderBy("due_date", false));
		
		if (repaymentBizPlanLists == null) {
			throw new RuntimeException("业务不存在还款计划");
		}

		Date lastPeriod = repaymentBizPlanLists.get(0).getDueDate();
		Date preSettle = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");

		int differ = 0;
		differ = DateUtil.getDiffDays(lastPeriod, preSettle);

		List<String> list = new ArrayList<>();
		BigDecimal principal = new BigDecimal(0);
		BigDecimal interest = new BigDecimal(0);
		BigDecimal servicecharge = new BigDecimal(0);
		BigDecimal guaranteeFee = new BigDecimal(0);
		BigDecimal platformFee = new BigDecimal(0);
		BigDecimal lateFee = new BigDecimal(0);
		BigDecimal demurrage = new BigDecimal(0);
		BigDecimal penalty = new BigDecimal(0);
		BigDecimal lackFee = new BigDecimal(0);

		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			list.add(repaymentBizPlanList.getPlanListId());
		}
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id", list)
						.eq("business_id", businessId).orderBy("period").orderBy("plan_item_type"));

		/*
		 * 1 到期还本息 2 每月付息到期还本 5 等额本息 9 分期还本付息 500 分期还本付息5年 1000 分期还本付息10年
		 */
		if (business.getRepaymentTypeId() == 2) {
			/*2017.6.5之前出款的不收取；
			2017.6.5—至今，收取标准：剩余本金*0.5%*剩余还款期数。(注：实际中如有业务与上述一般情况非一致的，以实际为准）*/
			boolean penaltyRule = false ;
			if (outPutDate.before(DateUtil.getDate("2017-06-05", "yyyy-MM-dd"))) {
				
			}else {
				penaltyRule = true ;
			}
			
			
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
				if (repaymentBizPlanListDetail.getPlanItemName().equals("利息")) {
					interest = interest.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}

				if (repaymentBizPlanListDetail.getPlanItemName().equals("本金")) {
					principal = principal.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}

				if (repaymentBizPlanListDetail.getPlanItemName().equals("服务费")) {
					servicecharge = servicecharge.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
				
				if (repaymentBizPlanListDetail.getPlanItemName().equals("滞纳金")) {
					lateFee = lateFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
				if (repaymentBizPlanListDetail.getPlanItemName().equals("平台费")) {
					platformFee = platformFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
				if (repaymentBizPlanListDetail.getPlanItemName().equals("担保费")) {
					guaranteeFee = guaranteeFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
			}

			if (serviceChargeRule.equals("")) {
				
			}else if (serviceChargeRule.equals("pre")) {
				
			}else {
				
			}
			BigDecimal calByMonth = businessRate.divide(new BigDecimal(100), 10).multiply(principal);
			BigDecimal calByDay = new BigDecimal(0.001).multiply(new BigDecimal(differ)).multiply(principal);

			if (differ > 10) {
				interest = interest.add(calByMonth);
			} else {
				if (calByDay.compareTo(calByMonth) > 0) {
					interest = interest.add(calByMonth);
				} else {
					interest = interest.add(calByDay);
				}

			}
			
			if (penaltyRule) {
				penalty = principal.multiply(new BigDecimal(0.005)).multiply(new BigDecimal(residualRepaymentBizPlanLists.size()));
			}

		} else if (business.getRepaymentTypeId() == 5) {
			/*2017.3之前不收取
			2017.3—2017.12.4，收取标准：剩余借款本金 * 分公司服务费率 * 服务费的剩余还款期数，但不超过剩余本金的6%；超过6% 按6% 收取*/
			boolean penaltyRule = false ;
			if (outPutDate.before(DateUtil.getDate("2017-06-05", "yyyy-MM-dd"))) {
				
			}else {
				penaltyRule = true ;
			}
		}

		return null;
	}

}
