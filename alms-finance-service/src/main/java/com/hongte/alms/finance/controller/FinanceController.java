/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.dto.RepaymentPlanInfoDTO;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.req.MoneyPoolReq;
import com.hongte.alms.finance.service.FinanceService;

import io.swagger.annotations.ApiOperation;

/**
 * @author 王继光 2018年4月27日 下午6:00:37
 */
@RestController
@RefreshScope
@RequestMapping(value = "/finance")
public class FinanceController {

	private static Logger logger = LoggerFactory.getLogger(FinanceController.class);

	@Autowired
	@Qualifier("BasicBusinessService")
	private BasicBusinessService basicBusinessService;
	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	private BasicRepaymentTypeService basicRepaymentTypeService;
	@Autowired
	@Qualifier("BizOutputRecordService")
	private BizOutputRecordService bizOutputRecordService;
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService;
	@Autowired
	@Qualifier("BasicBusinessTypeService")
	private BasicBusinessTypeService basicBusinessTypeService;
	@Autowired
	@Qualifier("MoneyPoolService")
	private MoneyPoolService moneyPoolService;
	@Autowired
	@Qualifier("MoneyPoolRepaymentService")
	private MoneyPoolRepaymentService moneyPoolRepaymentService;
	@Autowired
	@Qualifier("BasicCompanyService")
	private BasicCompanyService basicCompanyService;
	@Autowired
	@Qualifier("FinanceService")
	private FinanceService financeService;

	@GetMapping(value = "/repayBaseInfo")
	@ApiOperation(value = "获取还款基本信息")
	public Result repayBaseInfo(String businessId, String afterId) {
		Result result;
		logger.info("@repayBaseInfo@获取还款基本信息--开始[{},{}]", businessId, afterId);
		BasicBusiness basicBusiness = basicBusinessService.selectById(businessId);
		if (basicBusiness == null) {
			result = Result.error("500", "找不到对应的业务");
			logger.info("@repayBaseInfo@获取还款基本信息--结束[{}]", result);
			return result;
		}
		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(
				new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));
		BasicBusinessType basicBusinessType = basicBusinessTypeService.selectById(basicBusiness.getBusinessType());
		BasicRepaymentType basicRepaymentType = basicRepaymentTypeService
				.selectById(basicBusiness.getRepaymentTypeId());
		List<BizOutputRecord> outputRecords = bizOutputRecordService
				.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId));
		BigDecimal outPutMoney = new BigDecimal(0);
		for (BizOutputRecord bizOutputRecord : outputRecords) {
			outPutMoney = outPutMoney.add(bizOutputRecord.getFactOutputMoney());
		}
		JSONObject r = new JSONObject();
		r.put("businessId", businessId);
		r.put("afterId", afterId);
		r.put("businessType", basicBusinessType.getBusinessTypeName());
		r.put("companyName", basicBusiness.getCompanyName());
		r.put("operatorName", basicBusiness.getOperatorName() == null ? basicBusiness.getOperatorId()
				: basicBusiness.getOperatorName());
		r.put("customerName", basicBusiness.getCustomerName());
		r.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		r.put("repayDate", repaymentBizPlanList.getDueDate());
		r.put("repayAmount", repaymentBizPlanList.getTotalBorrowAmount());
		r.put("borrowAmount", outPutMoney);
		r.put("borrowLimit", basicBusiness.getBorrowLimit());
		r.put("borrowLimitUnit", basicBusiness.getBorrowLimitUnit());
		r.put("borrowRate", basicBusiness.getBorrowRate());
		r.put("borrowRateUnit", basicBusiness.getBorrowRateUnit());
		result = Result.success(r);
		logger.info("@repayBaseInfo@获取还款基本信息--结束[{}]", result);
		return result;
	}

	@GetMapping(value = "/moneyPool")
	@ApiOperation(value = "根据条件获取款项池")
	public PageResult moneyPool(MoneyPoolReq req) {
		PageResult result;
		logger.info("@moneyPool@根据条件获取款项池--开始[{}]", req);
		EntityWrapper ew = new EntityWrapper<MoneyPool>();
		if (req.getAcceptBank() != null && req.getAcceptBank().length() > 0 && !req.getAcceptBank().equals("所有账户")) {
			ew.eq("accept_bank", req.getAcceptBank());
		}
		if (req.getRepayDate() != null) {
			ew.eq("DATE_FORMAT(trade_date,'%Y-%m-%d')", req.getRepayDate());
		}
		if (req.getAccountMoney() != null) {
			ew.eq("account_money", req.getAccountMoney());
		}
		ew.eq("status", RepayRegisterState.待领取.toString());
		Page<MoneyPool> page = moneyPoolService.selectPage(new Page<MoneyPool>(req.getCurPage(), req.getPageSize()),
				ew);
		result = PageResult.success(page.getRecords(), page.getTotal());
		logger.info("@moneyPool@根据条件获取款项池--结束[{}]", result);
		return result;

	}

	@PostMapping(value = "/matchBankStatement")
	@ApiOperation(value = "银行流水匹配还款计划")
	public Result matchBankStatement(@RequestBody JSONObject req) {
		Result result ;
		logger.info("@matchBankStatement@还款计划匹配银行流水--开始[{}]", req.toJSONString());
		String businessId = req.getString("businessId");
		String afterId = req.getString("afterId");
		JSONArray array = req.getJSONArray("array");
		String mprid = req.getString("mprid");
		
		List<String> moneyPoolIds = new ArrayList<>();
		for (Object obj : array) {
			JSONObject j = (JSONObject) obj;
			String moneyPoolId = j.getString("moneyPoolId");
			moneyPoolIds.add(moneyPoolId);
		}
		List<MoneyPool> list = moneyPoolService.selectBatchIds(moneyPoolIds);
		result = financeService.matchBankStatement(moneyPoolIds, businessId, afterId,mprid);
		logger.info("@matchBankStatement@还款计划匹配银行流水--结束[{}]", req.toJSONString());
		return result;
	}
	
	@PostMapping(value="/rejectRepayReg")
	@ApiOperation(value="拒绝客户还款登记")
	public Result rejectRepayReg(@RequestBody JSONObject req) {
		Result result ;
		logger.info("@rejectRepayReg@拒绝客户还款登记--开始[{}]", req.toJSONString());
		String mprid = req.getString("mprid");
		MoneyPoolRepayment mpr = moneyPoolRepaymentService.selectById(mprid);
		if (mpr.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
			result = Result.error("500", RepayRegisterFinanceStatus.财务确认已还款.toString()+"的登记不可拒绝");
			logger.info("@rejectRepayReg@拒绝客户还款登记--结束[{}]", result);
			return result ;
		}
		mpr.setIsFinanceMatch(0);
		mpr.setState(RepayRegisterFinanceStatus.还款登记被财务拒绝.toString());
		boolean res = mpr.updateById();
		if (res) {
			result = Result.success();
			logger.info("@rejectRepayReg@拒绝客户还款登记--结束[{}]", result);
			return result ;
		}else {
			result = Result.error("500", "更新数据失败");
			logger.info("@rejectRepayReg@拒绝客户还款登记--结束[{}]", result);
			return result;
		}
	}

	@GetMapping(value = "/getCompanys")
	@ApiOperation(value = "获取所有分公司数据")
	public Result getCompanys() {
		logger.info("@getCompanys@获取所有分公司数据--开始[]");
		Result result;
		// 公司
		List<BasicCompany> company_list = basicCompanyService
				.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
		CompanySortByPINYINUtil.sortByPINYIN(company_list);
		result = Result.success(company_list);
		logger.info("@getCompanys@获取所有分公司数据--结束[{}]", result);
		return result;
	}

	@GetMapping(value = "/getBusinessType")
	@ApiOperation(value = "获取所有业务类型")
	public Result getBusinessType() {
		logger.info("@getBusinessType@获取所有业务类型--开始[]");
		Result result;
		List<BasicBusinessType> list = basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>());
		result = Result.success(list);
		logger.info("@getBusinessType@获取所有业务类型--结束[{}]", result);
		return result;
	}

	@GetMapping(value = "/getFinanceMangerList")
	@ApiOperation(value = "获取财务管理列表数据")
	public PageResult getFinanceMangerList(FinanceManagerListReq req) {
		logger.info("@getFinanceMangerList@获取财务管理列表数据--开始[{}]", req);
		PageResult pageResult = repaymentBizPlanListService.selectByFinanceManagerListReq(req);
		logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]", pageResult);
		return pageResult;
	}

	@GetMapping(value = "/regRepayInfoList")
	@ApiOperation(value = "获取登记还款信息列表数据")
	public Result regRepayInfoList(String planListId, String businessId, String afterId) {
		Result result;
		logger.info("@getFinanceMangerList@获取财务管理列表数据--开始[{},{},{}]", planListId, businessId, afterId);
		RepaymentBizPlanList repaymentBizPlanList;
		if (planListId == null) {
			repaymentBizPlanList = repaymentBizPlanListService.selectOne(
					new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));
		} else {
			repaymentBizPlanList = repaymentBizPlanListService.selectById(planListId);
		}
		if (repaymentBizPlanList == null) {
			result = Result.error("500", "找不到对应的还款计划");
			logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]", result);
			return result;
		}
		List<MoneyPoolRepayment> list = moneyPoolRepaymentService.selectList(new EntityWrapper<MoneyPoolRepayment>()
				.eq("plan_list_id", repaymentBizPlanList.getPlanListId()).ne("is_deleted", 1).orderBy("trade_date"));
		result = Result.success(list);
		logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]", result);
		return result;

	}

	@GetMapping(value = "/matchedBankStatementList")
	@ApiOperation(value = "获取匹配的款项池银行流水数据")
	public Result matchedBankStatementList(String planListId, String businessId, String afterId) {
		Result result;
		logger.info("@getFinanceMangerList@获取财务管理列表数据--开始[{},{},{}]", planListId, businessId, afterId);
		RepaymentBizPlanList repaymentBizPlanList;
		if (planListId == null) {
			repaymentBizPlanList = repaymentBizPlanListService.selectOne(
					new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));
		} else {
			repaymentBizPlanList = repaymentBizPlanListService.selectById(planListId);
		}
		if (repaymentBizPlanList == null) {
			result = Result.error("500", "找不到对应的还款计划");
			logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]", result);
			return result;
		}
		List<MoneyPoolRepayment> list = moneyPoolRepaymentService.selectList(
				new EntityWrapper<MoneyPoolRepayment>().eq("plan_list_id", repaymentBizPlanList.getPlanListId())
						.eq("is_finance_match", 1).ne("is_deleted", 1).orderBy("trade_date"));
		result = Result.success(list);
		logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]", result);
		return result;

	}
	
	@GetMapping(value="/selectConfirmedBankStatement")
	@ApiOperation(value="查询已完成的还款信息")
	public Result selectConfirmedBankStatement(String businessId,String afterId) {
		Result result  ;
		logger.info("@selectConfirmedBankStatement@查询已完成的还款信息--开始[{}{}]",businessId,afterId);
		List<MatchedMoneyPoolVO> list = financeService.selectConfirmedBankStatement(businessId, afterId);
		result = Result.success(list);
		logger.info("@selectConfirmedBankStatement@查询已完成的还款信息--结束[{}]",result);
		return result;
	}

	@ResponseBody
	@PostMapping("/appointBankStatement")
	@ApiOperation(value = "财务指定(新增)银行流水")
	public Result appointBankStatement(@RequestBody RepaymentRegisterInfoDTO repayInfo) {
		Result result;
		try {
			logger.info("@appointBankStatement@财务指定(新增)银行流水--开始[{}]", repayInfo);
			result = financeService.appointBankStatement(repayInfo);
			logger.info("@appointBankStatement@财务指定(新增)银行流水--结束[{}]", result);
			return result;
		} catch (Exception e) {
			result = Result.error("500", e.getMessage());
			logger.info("@appointBankStatement@财务指定(新增)银行流水--结束[{}]", result);
			return result;
		}

	}
	
	@PostMapping(value="/disMatchBankStatement")
	@ApiOperation(value="取消关联银行流水")
	public Result disMatchedBankStatement(@RequestBody JSONObject req) {
		Result result ;
		logger.info("@disMatchBankStatement@取消关联银行流水--开始[{}]",req);
		String mpid = req.getString("mpid");
		List<MoneyPoolRepayment> mprs= moneyPoolRepaymentService.selectList(new EntityWrapper<MoneyPoolRepayment>().eq("money_pool_id", mpid).eq("is_finance_match", 1));
		if (mprs==null||mprs.size()==0) {
			result = Result.error("500", "找不到对应的还款登记") ;
			logger.info("@disMatchBankStatement@取消关联银行流水--结束[{}]",result);
			return result ;
		}
		MoneyPoolRepayment moneyPoolRepayment = mprs.get(0);
		MoneyPool moneyPool = moneyPoolService.selectById(mpid);
		if(moneyPool==null) {
			result = Result.error("500", "找不到对应的银行流水") ;
			logger.info("@disMatchBankStatement@取消关联银行流水--结束[{}]",result);
			return result;
		}
		if (moneyPool.getStatus().equals(RepayRegisterState.完成.toString())) {
			result = Result.error("500", "已完成的银行流水不能取消关联");
			logger.info("@disMatchBankStatement@取消关联银行流水--结束[{}]",result);
			return result ;
		}
		result = financeService.disMatchedBankStatement(moneyPool, moneyPoolRepayment);
		logger.info("@disMatchBankStatement@取消关联银行流水--结束[{}]",result);
		return result ;
	}
	
	@GetMapping(value="/thisPeroidRepayment")
	@ApiOperation(value="本期还款信息(包括标的信息)")
	public Result thisPeroidRepayment(String businessId,String afterId) {
		Result result ;
		logger.info("@thisPeroidRepayment@本期还款信息(包括标的信息)--开始[{}{}]",businessId,afterId);
		result = financeService.thisPeroidRepayment(businessId, afterId);
		logger.info("@thisPeroidRepayment@本期还款信息(包括标的信息)--结束[{}]",result);
		return result ;
	}
	
	@GetMapping(value="/thisTimeRepayment")
	@ApiOperation(value="本次还款信息")
	public Result thisTimeRepayment(String businessId,String afterId) {
		Result result ;
		logger.info("@thisTimeRepayment@本次还款信息--开始[{}{}]",businessId,afterId);
		CurrPeriodRepaymentInfoVO infoVO  = financeService.getCurrPeriodRepaymentInfoVO(businessId, afterId);
		if (infoVO==null) {
			result = Result.error("500", "找不到本次还款信息");
		}
		result = Result.success(infoVO);
		logger.info("@thisTimeRepayment@本次还款信息--结束[{}]",result);
		return result ;
	}
	
	@PostMapping(value="/previewConfirmRepayment")
	@ApiOperation(value="预览确认还款拆标情况")
	public Result previewConfirmRepayment(@RequestBody ConfirmRepaymentReq req) {
		Result result ;
		logger.info("@previewConfirmRepayment@预览确认还款拆标情况--开始[{}]",req);
		result = financeService.previewConfirmRepayment(req);
		logger.info("@previewConfirmRepayment@预览确认还款拆标情况--结束[{}]",result);
		return result ;
	}
	
	@GetMapping(value="/getSurplusFund")
	@ApiOperation(value="获取结余情况")
	public Result getSurplusFund(String businessId,String afterId) {
		Result result ;
		logger.info("@getSurplusFund@获取结余情况--开始[{}{}]",businessId,afterId);
		BigDecimal surplusFund = financeService.getSurplusFund(businessId, afterId);
		result = Result.success(surplusFund);
		logger.info("@getSurplusFund@获取结余情况--结束[{}]",result);
		return result ;
	}
	
	@GetMapping(value = "/queryRepaymentPlanInfoByBusinessId")
	@ApiOperation(value = "根据源业务编号获取还款计划信息")
	public Result<Map<String, Object>> queryRepaymentPlanInfoByBusinessId(String businessId) {
		try {
			Result<Map<String, Object>> result;

			logger.info("@queryRepaymentPlanInfoByBusinessId@根据源业务编号获取还款计划信息--开始[{}]", businessId);

			result = Result.success(financeService.queryRepaymentPlanInfoByBusinessId(businessId));

			logger.info("@queryRepaymentPlanInfoByBusinessId@根据源业务编号获取还款计划信息--结束[{}]", result);

			return result;
		} catch (Exception e) {
			logger.error("根据源业务编号获取还款计划信息失败--[{}]", e);
			return Result.error("-500", "系统异常，获取还款计划信息失败");
		}
	}
	
}
