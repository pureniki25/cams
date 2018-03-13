package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.common.result.Result;

@Controller
@RequestMapping("/expenseSettle")
public class ExpenseSettleController {
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService ;
	
	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService ;
	
	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	BasicRepaymentTypeService basicRepaymentTypeService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService ;
	
	@GetMapping("/business")
	@ResponseBody
	public Result getBusiness(String businessId) {
		BasicBusiness business = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		if (business==null) {
			return Result.error("500", "business wasn't found");
		}
		BasicRepaymentType basicRepaymentType = basicRepaymentTypeService.selectById(business.getRepaymentTypeId());
		List<BizOutputRecord > bizOutputRecord = bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId).orderBy("period").orderBy("plan_item_type"));
		JSONObject jsonObject = new JSONObject() ;
		jsonObject.put("business", business);
		jsonObject.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		jsonObject.put("output", bizOutputRecord);
		jsonObject.put("repaymentDetails", repaymentBizPlanListDetails);
		return Result.success(jsonObject) ;
	}
	
	@GetMapping("/calByPreSettleDate")
	@ResponseBody
	public Result calByPreSettleDate(String preSettleDate,String businessId,String afterId) {
		BasicBusiness business = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		if (business==null) {
			return Result.error("500", "business wasn't found");
		}
		/*
		 * 1 到期还本息 2 每月付息到期还本 5 等额本息 9 分期还本付息 500 分期还本付息5年 1000 分期还本付息10年
		 */
		Integer repaymentType = business.getRepaymentTypeId() ;
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId));
		BigDecimal factRepay = new BigDecimal(0) ;
		
		
		JSONObject jsonObject = new JSONObject() ;
		jsonObject.put("interest", repaymentBizPlanListDetails);
		return Result.success(jsonObject) ;
		
	}
}
