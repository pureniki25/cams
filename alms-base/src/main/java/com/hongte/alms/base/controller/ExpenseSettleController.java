package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.base.vo.module.MoneyPoolRepaymentXindaiDTO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/expenseSettle")
@RefreshScope
public class ExpenseSettleController {

	private Logger logger = LoggerFactory.getLogger(ExpenseSettleController.class);

	@Value("${bmApi.apiUrl}")
	String xindaiAplUrlUrl ;
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;

	@Autowired
	@Qualifier("RenewalBusinessService")
	RenewalBusinessService renewalBusinessService ;
	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService;

	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	BasicRepaymentTypeService basicRepaymentTypeService;

	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@Autowired
	@Qualifier("ExpenseSettleService")
	ExpenseSettleService expenseSettleService;
	
	@Autowired
	TransferOfLitigationMapper transferOfLitigationMapper ;

	private List<ExpenseSettleLackFeeVO> lackFeeList = new ArrayList<>();
	private BigDecimal principal = new BigDecimal("0").setScale(2);
	private BigDecimal interest = new BigDecimal("0").setScale(2);
	private BigDecimal servicecharge = new BigDecimal("0").setScale(2);
	private BigDecimal guaranteeFee = new BigDecimal("0").setScale(2);
	private BigDecimal platformFee = new BigDecimal("0").setScale(2);

	/**
	 * 期内滞纳金
	 */
	private BigDecimal lateFee = new BigDecimal("0");
	/**
	 * 期外逾期费
	 */
	private BigDecimal demurrage = new BigDecimal("0");
	/**
	 * 提前还款违约金
	 */
	private BigDecimal penalty = new BigDecimal("0");
	/**
	 * 往期少缴费用
	 */
	private BigDecimal lackFee = null;
	private BigDecimal balance = new BigDecimal("0");
	private BigDecimal deposit = new BigDecimal("0");

	@GetMapping("/business")
	@ApiOperation(value = "获取房贷结清试算基础数据")
	@ResponseBody
	public Result getBusiness(String businessId) {
		BasicBusiness business = basicBusinessService
				.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		if (business == null) {
			return Result.error("500", "business wasn't found");
		}
		BasicRepaymentType basicRepaymentType = basicRepaymentTypeService.selectById(business.getRepaymentTypeId());
		List<BizOutputRecord> bizOutputRecord = bizOutputRecordService.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("current_status", RepayPlanStatus.REPAYED.getName()).orderBy("fact_repay_date",false));
		Date lastRepayDate = null ;
		if (repaymentBizPlanLists!=null&&repaymentBizPlanLists.size()>0) {
			lastRepayDate = repaymentBizPlanLists.get(0).getFactRepayDate();
		}
		/*List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId)
						.orderBy("period").orderBy("plan_item_type"));*/
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("business", business);
		jsonObject.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		jsonObject.put("output", bizOutputRecord);
		jsonObject.put("lastRepayDate", lastRepayDate==null?lastRepayDate:DateUtil.toDateString(lastRepayDate, DateUtil.DEFAULT_FORMAT_DATE));
		return Result.success(jsonObject);
	}

	@GetMapping("/calByPreSettleDate")
	@ApiOperation(value = "根据清算日期获取房贷结清试算数据")
	@ResponseBody
	public Result<ExpenseSettleVO> calByPreSettleDate(String preSettleDate, String businessId, String afterId) {
		if (StringUtil.isEmpty(businessId) || StringUtil.isEmpty(preSettleDate)) {
			return Result.error("500", "参数不能为空！");
		}
		Date settleDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		ExpenseSettleVO expenseSettleVO = null ;
		try {
			expenseSettleVO = expenseSettleService.cal(businessId, settleDate);
			return Result.success(expenseSettleVO);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("500", e.getMessage()) ;
		}
	}

	@GetMapping("/cal")
	@ApiOperation(value = "获取房贷结清试算数据")
	@ResponseBody
	public Result<ExpenseSettleVO> cal(String businessId, String afterId) {
		if (StringUtil.isEmpty(businessId)) {
			return Result.error("500", "参数不能为空！");
		}
		Date settleDate = new Date();
		ExpenseSettleVO expenseSettleVO = null ;
		try {
			expenseSettleVO = expenseSettleService.cal(businessId, settleDate);
			return Result.success(expenseSettleVO);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("500", e.getMessage()) ;
		}
	}

}
