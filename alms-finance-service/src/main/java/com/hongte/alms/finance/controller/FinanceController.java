/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.text.html.parser.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.entity.Columns;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;

/**
 * @author 王继光
 * 2018年4月27日 下午6:00:37
 */
@RestController
@RefreshScope
@RequestMapping(value="/finance")
public class FinanceController {

	private static Logger logger = LoggerFactory.getLogger(FinanceController.class);
	
	@Autowired
	@Qualifier("BasicBusinessService")
	private BasicBusinessService basicBusinessService ;
	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	private BasicRepaymentTypeService basicRepaymentTypeService ;
	@Autowired
	@Qualifier("BizOutputRecordService")
	private BizOutputRecordService bizOutputRecordService ;
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService ;
	@Autowired
	@Qualifier("BasicBusinessTypeService")
	private BasicBusinessTypeService basicBusinessTypeService ;
	@GetMapping(value="repayBaseInfo")
	@ApiOperation(value="获取还款基本信息")
	public Result repayBaseInfo(String businessId,String afterId) {
		Result result ;
		logger.info("@repayBaseInfo@获取还款基本信息--开始[{},{}]",businessId,afterId);
		BasicBusiness basicBusiness =  basicBusinessService.selectById(businessId);
		if (basicBusiness==null) {
			result = Result.error("500", "找不到对应的业务"); 
			logger.info("@repayBaseInfo@获取还款基本信息--结束[{}]",result);
			return result;
		}
		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id",afterId)) ;
		BasicBusinessType basicBusinessType =  basicBusinessTypeService.selectById(basicBusiness.getBusinessType());
		BasicRepaymentType basicRepaymentType =  basicRepaymentTypeService.selectById(basicBusiness.getRepaymentTypeId()) ;
		List<BizOutputRecord> outputRecords = bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId));
		BigDecimal outPutMoney = new BigDecimal(0) ;
		for (BizOutputRecord bizOutputRecord : outputRecords) {
			outPutMoney = outPutMoney.add(bizOutputRecord.getFactOutputMoney()) ;
		}
		JSONObject r = new JSONObject() ;
		r.put("businessId", businessId);
		r.put("afterId", afterId);
		r.put("businessType", basicBusinessType.getBusinessTypeName());
		r.put("companyName", basicBusiness.getCompanyName());
		r.put("operatorName", basicBusiness.getOperatorName()==null?basicBusiness.getOperatorId():basicBusiness.getOperatorName());
		r.put("customerName", basicBusiness.getCustomerName());
		r.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		r.put("repayDate", repaymentBizPlanList.getDueDate());
		r.put("repayAmount", repaymentBizPlanList.getTotalBorrowAmount());
		r.put("borrowAmount", outPutMoney);
		r.put("borrowLimit", basicBusiness.getBorrowLimit());
		r.put("borrowLimitUnit", basicBusiness.getBorrowLimitUnit());
		r.put("borrowRate", basicBusiness.getBorrowRate());
		r.put("borrowRateUnit", basicBusiness.getBorrowRateUnit());
		result = Result.success(r) ;
		logger.info("@repayBaseInfo@获取还款基本信息--结束[{}]",result);
		return result;
	}
}
