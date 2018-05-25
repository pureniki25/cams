package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标实还明细表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@Service("RepaymentProjFactRepayService")
public class RepaymentProjFactRepayServiceImpl extends BaseServiceImpl<RepaymentProjFactRepayMapper, RepaymentProjFactRepay> implements RepaymentProjFactRepayService {

	@Autowired
	RepaymentProjFactRepayMapper repaymentProjFactRepayMapper ;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper ;
	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper ;
	
	@Override
	public BigDecimal caluUnpaid(String businessId, String afterId) {
		BigDecimal unpaid = new BigDecimal(0);
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(
				new EntityWrapper<RepaymentBizPlanList>()
				.eq("business_id", businessId)
				.eq("after_id", afterId));
		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			String planListId = repaymentBizPlanList.getPlanListId() ;
			BigDecimal planAmount = repaymentBizPlanList.getTotalBorrowAmount();
			BigDecimal overDue = repaymentBizPlanList.getOverdueAmount()==null?new BigDecimal(0):repaymentBizPlanList.getOverdueAmount();
			
			unpaid = unpaid.add(planAmount).add(overDue);
			
			List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(
					new EntityWrapper<RepaymentProjPlanList>()
					.eq("plan_list_id", planListId)
					.orderBy("total_borrow_amount",false));
			
			for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
				String projPlanListId = repaymentProjPlanList.getProjPlanListId() ;
				List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper.selectList(
						new EntityWrapper<RepaymentProjFactRepay>()
						.eq("proj_plan_list_id", projPlanListId));
				for (RepaymentProjFactRepay repaymentProjFactRepay : factRepays) {
					unpaid = unpaid.subtract(repaymentProjFactRepay.getFactAmount());
				}
			}
		}
		return unpaid;
	}
	
}
