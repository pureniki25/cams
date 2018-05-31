/**
 * 
 */
package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.BizOutputRecordMapper;
import com.hongte.alms.base.mapper.RenewalBusinessMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.PaymentWithPenaltyService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.ExpenseSettleRepaymentPlanVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;

import feign.Feign;

/**
 * 
 * @author 王继光
 * 2018年5月30日 下午8:59:40
 */
public class PaymentWithPenaltyServiceImpl implements PaymentWithPenaltyService {
	@Autowired
	BasicBusinessMapper basicBusinessMapper;
	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;

	@Autowired
	RenewalBusinessMapper renewalBusinessMapper ;
	
	@Autowired
	BizOutputRecordMapper bizOutputRecordMapper;
	
	@Autowired
	@Qualifier("ExpenseSettleService")
	ExpenseSettleService expenseSettleService ;
	
	@Override
	public BigDecimal caluPenalty(String businessId, String afterId, Date settleDate) {
		return new BigDecimal(0);
	}
	

}
