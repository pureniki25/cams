/**
 * 
 */
package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.service.SettleService;
import com.hongte.alms.common.util.StringUtil;

/**
 * @author 王继光 2018年6月14日 下午3:52:47
 */
@Service("SettleService")
public class SettleServiceImpl implements SettleService {
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;

	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper ;
	@Override
	public JSONObject settleInfo(String businessId, String afterId, String planId) {
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", businessId).eq("after_id", afterId);
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListMapper.selectList(planListEW);
		BigDecimal item10 = new BigDecimal("0");
		BigDecimal item20 = new BigDecimal("0");
		BigDecimal item30 = new BigDecimal("0");
		BigDecimal item50 = new BigDecimal("0");
		BigDecimal offlineOverDue = new BigDecimal("0");
		BigDecimal onlineOverDue = new BigDecimal("0");
		BigDecimal planAmount = new BigDecimal("0");
		BigDecimal lack = new BigDecimal("0");
		BigDecimal factAmount = new BigDecimal("0");
		BigDecimal penalty = new BigDecimal("0");
		List<JSONObject> derateList = new ArrayList<>();
		List<JSONObject> lackList = new ArrayList<>();
		for (RepaymentBizPlanList repaymentBizPlanList : planLists) {
		}
		return null;
	}

}
