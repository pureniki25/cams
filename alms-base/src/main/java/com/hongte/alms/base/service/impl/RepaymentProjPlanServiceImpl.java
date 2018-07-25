package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentSettleLogMapper;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 标的还款计划信息 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@Service("RepaymentProjPlanService")
public class RepaymentProjPlanServiceImpl extends BaseServiceImpl<RepaymentProjPlanMapper, RepaymentProjPlan> implements RepaymentProjPlanService {

	@Autowired
	private  RepaymentProjPlanMapper repaymentProjPlanMapper;

	@Override
	public RepaymentProjPlanList findCurrentPeriod(Date settleDate, List<RepaymentProjPlanList> projPlanLists) {
		RepaymentProjPlanList finalPeriod = projPlanLists.get(projPlanLists.size() - 1);
	        int diff = DateUtil.getDiffDays(settleDate, finalPeriod.getDueDate());
	        RepaymentProjPlanList currentPeriod = null;

	        // 提前还款结清
	        if (diff > 0) {
	        	RepaymentProjPlanList temp = new RepaymentProjPlanList();
	            temp.setDueDate(settleDate);
	            temp.setBusinessId("temp");
	            // 把提前结清的日期放进PlanLists一起比较
	            projPlanLists.add(temp);
	            projPlanLists.sort(
	                    (RepaymentProjPlanList a1, RepaymentProjPlanList a2) -> a1.getDueDate().compareTo(a2.getDueDate()));
	            for (int i = 0; i < projPlanLists.size(); i++) {
	                if (projPlanLists.get(i).getBusinessId().equals("temp")) {
	                    currentPeriod = projPlanLists.get(i + 1);
	                }
	            }

	            // 筛选temp记录
	            for (Iterator<RepaymentProjPlanList> it = projPlanLists.iterator(); it.hasNext(); ) {
	            	RepaymentProjPlanList pList = it.next();
	                if (pList.getBusinessId().equals("temp")) {
	                    it.remove();
	                }
	            }

	        } else {
	            // 期外
	            currentPeriod = finalPeriod;
	        }


	        return currentPeriod;
	}

	@Override
	public List<RepaymentSettleMoneyDto> selectProjPlanMoney(String businessId, String planId) {
		return repaymentProjPlanMapper.selectProjPlanMoney(businessId,planId);
	}


}
