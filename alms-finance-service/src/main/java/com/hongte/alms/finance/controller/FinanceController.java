/**
 * 
 */
package com.hongte.alms.finance.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.dto.MoneyPoolManagerReq;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.dto.core.LayTableQuery;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.*;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.TdReturnAdvanceShareProfitDTO;
import com.hongte.alms.base.feignClient.dto.TdReturnAdvanceShareProfitResult;
import com.hongte.alms.base.feignClient.dto.TdrepayProjectInfoDTO;
import com.hongte.alms.base.feignClient.dto.TdrepayProjectPeriodInfoDTO;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.finance.*;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.req.MoneyPoolReq;
import com.hongte.alms.finance.req.OrderSetReq;
import com.hongte.alms.finance.service.FinanceService;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 王继光 2018年4月27日 下午6:00:37
 */
@RestController
@RefreshScope
@RequestMapping(value = "/finance")
@Api(tags = "FinanceController", description = "财务管理模块相关控制器")
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
    @Qualifier("SysUserService")
    private SysUserService sysUserService;
	@Autowired
	@Qualifier("FinanceService")
	private FinanceService financeService;
	@Autowired
	@Qualifier("ShareProfitService")
	private ShareProfitService shareService;
	@Autowired
	@Qualifier("RepaymentConfirmLogService")
	private RepaymentConfirmLogService confrimLogService;
	@Autowired
	@Qualifier("AccountantOverRepayLogService")
	private AccountantOverRepayLogService accountantOverRepayLogService;
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	private RepaymentBizPlanService repaymentBizPlanService ;
	
	@Autowired
	@Qualifier("ShareProfitService")
	private ShareProfitService shareProfitService ;
	
	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper ;
	
	@Qualifier("DepartmentBankService")
	@Autowired
	private DepartmentBankService departmentBankService ;

	@Qualifier("SysFinancialOrderService")
	@Autowired
	private SysFinancialOrderService sysFinancialOrderService;
	
	@Autowired
	@Qualifier("WithholdingRepaymentLogService")
	WithholdingRepaymentLogService withholdingRepaymentLogService;
	
	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	TuandaiProjectInfoService tuandaiProjectInfoService ;

	@Autowired
	EipRemote eipRemote ;

	@Value("${oss.readUrl}")
	private String ossReadUrl ;
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
		BigDecimal outPutMoney = new BigDecimal("0");
		for (BizOutputRecord bizOutputRecord : outputRecords) {
			outPutMoney = outPutMoney.add(bizOutputRecord.getFactOutputMoney());
		}
		JSONObject r = new JSONObject();
		r.put("businessId", businessId);
		r.put("afterId", afterId);
		r.put("businessType", basicBusinessType.getBusinessTypeName());
		r.put("companyName", basicBusiness.getCompanyName()==null?basicBusiness.getCompanyId(): basicBusiness.getCompanyName());
		r.put("operatorName", basicBusiness.getOperatorName() == null ? basicBusiness.getOperatorId()
				: basicBusiness.getOperatorName());
		r.put("customerName", basicBusiness.getCustomerName());
		r.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		r.put("repayDate", DateUtil.formatDate(repaymentBizPlanList.getDueDate()));
		r.put("repayAmount", repaymentBizPlanList.getTotalBorrowAmount());
		r.put("borrowAmount", basicBusiness.getBorrowMoney());
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
	
	@GetMapping(value = "/moneyPoolManager")
	@ApiOperation(value = "导入流水管理页面列表数据")
	public PageResult moneyPoolManager(MoneyPoolManagerReq req) {
		PageResult result;
		logger.info("@moneyPoolManager@导入流水管理页面列表数据--开始[{}]", req);
		Page<MoneyPoolManagerVO> page = new Page<>(req.getCurPage(), req.getPageSize());
		if (req==null) {
			logger.info("@moneyPoolManager@导入流水管理页面列表数据--结束[{}]",  "参数不能为空");
			return PageResult.error(500, "参数不能为空");
		}
		page = moneyPoolService.selectMoneyPoolManagerList(req);
		result = PageResult.success(page.getRecords(), page.getTotal());
		logger.info("@moneyPoolManager@导入流水管理页面列表数据--结束[{}]",  page);
		return result;
	}
	
	@GetMapping(value = "/delMoneyPool")
	@ApiOperation(value = "删除流水数据")
	public Result delMoneyPool(@RequestParam("mpids[]")String[] mpids) {
		Result result;
		logger.info("@moneyPoolManager@导入流水管理页面列表数据--开始[{}]", mpids);
		if(mpids==null||mpids.length==0) {
			logger.info("@moneyPoolManager@导入流水管理页面列表数据--结束[{}]", "参数不能空");
			return Result.error("500", "参数不能空");
		}
		List<String> ids = Arrays.asList(mpids);
		List<MoneyPool> moneyPools = moneyPoolService.selectBatchIds(ids);
		for (MoneyPool moneyPool : moneyPools) {
			if (moneyPool!=null
					&&!moneyPool.getStatus().equals(RepayRegisterState.待领取.toString())) {
				logger.info("@moneyPoolManager@导入流水管理页面列表数据--结束[{}]", "存在部分非待领取状态的流水,删除终止,请检查");
				return Result.error("500", "存在部分非待领取状态的流水,删除终止,请检查");
			}
		}
		moneyPoolService.deleteBatchIds(ids);
		logger.info("@moneyPoolManager@导入流水管理页面列表数据--结束[{}]", mpids);
		return Result.success();
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
		String remark = req.getString("remark");
		MoneyPoolRepayment mpr = moneyPoolRepaymentService.selectById(mprid);
		if (mpr.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
			result = Result.error("500", RepayRegisterFinanceStatus.财务确认已还款.toString()+"的登记不可拒绝");
			logger.info("@rejectRepayReg@拒绝客户还款登记--结束[{}]", result);
			return result ;
		}
		mpr.setIsFinanceMatch(0);
		if (remark!=null) {
			mpr.setRemark(remark);
		}
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
	
	@GetMapping(value = "/getAreaCompany")
	@ApiOperation(value = "获取所有区域分公司数据")
	public Result getAreaCompany() {
		logger.info("@getCompanys@获取所有分公司数据--开始[]");
		Result result;
		Map<String, Object> retMap = new HashMap<>();
		//区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.AREA_LEVEL.getKey()));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
        result = Result.success(retMap);
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
		req.setUserId(loginUserInfoHelper.getUserId());
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
	@ApiOperation(value="确认还款拆标情况并存储")
	public Result previewConfirmRepayment(@RequestBody ConfirmRepaymentReq req) {
		Result result ;
		logger.info("@previewConfirmRepayment@预览确认还款拆标情况--开始[{}]",req);
		try {
			List<CurrPeriodProjDetailVO> detailVOs = shareService.execute(req, false);
			result = Result.success(detailVOs);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result = Result.error("500", e.getMessage());
		}
		logger.info("@previewConfirmRepayment@预览确认还款拆标情况--结束[{}]",result);
		return result ;
	}
	
	@PostMapping(value="/confirmRepayment")
	@ApiOperation(value="预览确认还款拆标情况")
	public Result confirmRepayment(@RequestBody ConfirmRepaymentReq req) {
		Result result ;
		logger.info("@confirmRepayment@确认还款拆标情况--开始[{}]",req);
		try {
			List<CurrPeriodProjDetailVO> detailVOs = shareService.execute(req, true);
			moneyPoolService.confirmRepaidUpdateMoneyPool(req);
			result = Result.success(detailVOs);
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					financeService.sendLoanBalanceToDataPlatform(req.getBusinessId());
					
				}
			});
			t.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = Result.error("500", e.getMessage());
			e.printStackTrace();
		}
		logger.info("@confirmRepayment@确认还款拆标情况--结束[{}]",result);
		return result ;
	}
	
	
	@GetMapping(value="/getSurplusFund")
	@ApiOperation(value="获取结余情况")
	public Result getSurplusFund(String businessId,String afterId) {
		Result result ;
		logger.info("@getSurplusFund@获取结余情况--开始[{}{}]",businessId,afterId);
		BigDecimal surplusFund = accountantOverRepayLogService.caluCanUse(businessId, null );
		result = Result.success(surplusFund);
		logger.info("@getSurplusFund@获取结余情况--结束[{}]",result);
		return result ;
	}
	
	@GetMapping(value = "/queryRepaymentPlanInfoByBusinessId")
	@ApiOperation(value = "根据源业务编号获取还款计划信息")
	public Result<Map<String, Object>> queryRepaymentPlanInfoByBusinessId(@RequestParam("businessId") String businessId) {
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
	
	@GetMapping(value = "/queryPlanRepaymentProjInfoByPlanListId")
	@ApiOperation(value = "根据业务还款计划列表ID获取所有对应的标的应还还款计划信息")
	public Result<Map<String, Object>> queryPlanRepaymentProjInfoByPlanListId(@RequestParam("planListId") String planListId) {
		try {
			Result<Map<String, Object>> result;
			
			logger.info("@queryPlanRepaymentProjInfoByPlanListId@根据业务还款计划列表ID获取所有对应的标的应还还款计划信息--开始[{}]", planListId);
			
			result = Result.success(financeService.queryPlanRepaymentProjInfoByPlanListId(planListId));
			
			logger.info("@queryPlanRepaymentProjInfoByPlanListId@根据业务还款计划列表ID获取所有对应的标的应还还款计划信息--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("根据业务还款计划列表ID获取所有对应的标的应还还款计划信息失败--[{}]", e);
			return Result.error("-500", "系统异常，获取所有对应的标的应还还款计划信息失败");
		}
	}
	
	@GetMapping(value = "/queryActualRepaymentProjInfoByPlanListId")
	@ApiOperation(value = "根据业务还款计划列表ID获取所有对应的标的实还还款计划信息")
	public Result<Map<String, Object>> queryActualRepaymentProjInfoByPlanListId(@RequestParam("planListId") String planListId) {
		try {
			Result<Map<String, Object>> result;
			
			logger.info("@queryActualRepaymentProjInfoByPlanListId@根据业务还款计划列表ID获取所有对应的标的实还还款计划信息--开始[{}]", planListId);
			
			result = Result.success(financeService.queryActualRepaymentProjInfoByPlanListId(planListId));
			
			logger.info("@queryActualRepaymentProjInfoByPlanListId@根据业务还款计划列表ID获取所有对应的标的实还还款计划信息--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("根据业务还款计划列表ID获取所有对应的标的实还还款计划信息失败--[{}]", e);
			return Result.error("-500", "系统异常，获取所有对应的标的实还还款计划信息失败");
		}
	}
	
	@GetMapping(value = "/queryDifferenceRepaymentProjInfo")
	@ApiOperation(value = "获取标还款计划差额")
	public Result<Map<String, Object>> queryDifferenceRepaymentProjInfo(@RequestParam("planListId") String planListId) {
		try {
			Result<Map<String, Object>> result;
			
			logger.info("@queryDifferenceRepaymentProjInfo@获取标还款计划差额--开始[{}]", planListId);
			
			result = Result.success(financeService.queryDifferenceRepaymentProjInfo(planListId));
			
			logger.info("@queryDifferenceRepaymentProjInfo@获取标还款计划差额--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("获取标还款计划差额失败--[{}]", e);
			return Result.error("-500", "系统异常，获取标还款计划差额信息失败");
		}
	}
	
	@GetMapping(value = "/queryProjOtherFee")
	@ApiOperation(value = "获取标维度的其他费用")
	public Result<List<String>> queryProjOtherFee(@RequestParam("projPlanListId") String projPlanListId) {
		try {
			Result<List<String>> result;
			
			logger.info("@queryProjOtherFee@获取标维度的其他费用--开始[{}]", projPlanListId);
			
			result = Result.success(financeService.queryProjOtherFee(projPlanListId));
			
			logger.info("@queryProjOtherFee@获取标维度的其他费用--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("获取标维度的其他费用失败--[{}]", e);
			return Result.error("-500", "系统异常，获取标维度的其他费用失败");
		}
	}
	
	@GetMapping(value = "/queryBizOtherFee")
	@ApiOperation(value = "获取业务维度的其他费用")
	public Result<List<String>> queryBizOtherFee(@RequestParam("planListId") String planListId) {
		try {
			Result<List<String>> result;
			
			logger.info("@queryBizOtherFee@获取业务维度的其他费用--开始[{}]", planListId);
			
			result = Result.success(financeService.queryBizOtherFee(planListId));
			
			logger.info("@queryBizOtherFee@获取业务维度的其他费用--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("获取业务维度的其他费用--[{}]", e);
			return Result.error("-500", "系统异常，获取业务维度的其他费用");
		}
	}
	
	@GetMapping(value = "/queryActualPaymentByBusinessId")
	@ApiOperation(value = "根据业务编号查找实还流水")
	public Result<Map<String, Object>> queryActualPaymentByBusinessId(@RequestParam("businessId") String businessId) {
		try {
			Result<Map<String, Object>> result;
			
			logger.info("@queryBizOtherFee@根据业务编号查找实还流水--开始[{}]", businessId);
			
			result = Result.success(financeService.queryActualPaymentByBusinessId(businessId));
			
			logger.info("@queryBizOtherFee@根据业务编号查找实还流水--结束[{}]", result);
			
			return result;
		} catch (Exception e) {
			logger.error("根据业务编号查找实还流水失败--[{}]", e);
			return Result.error("-500", "系统异常，根据业务编号查找实还流水失败");
		}
	}
	
	@GetMapping(value = "/revokeConfirm")
	@ApiOperation(value = "撤销还款确认")
	public Result revokeConfirm(String businessId,String afterId) {
		try {
			logger.info("@revokeConfirm@撤销还款确认--开始[{}]", businessId,afterId);
			Result result = null;
			result = confrimLogService.revokeConfirm(businessId, afterId);
			logger.info("@revokeConfirm@撤销还款确认--结束[{}]", result);
			return result;
		}  catch (ServiceRuntimeException se) {
			return Result.error("500", se.getMessage());
		}catch (Exception e) {
			logger.error("撤销还款确认失败--[{}]", e);
			return Result.error("-500", "系统异常:撤销还款确认失败");
		}
	}
	
	@GetMapping(value = "/listConfirmWithhold")
	@ApiOperation(value = "查找业务代扣确认列表")
	public Result listConfirmWithhold(String businessId) {
		try {
			logger.info("@listConfirmWithhold@查找业务代扣确认列表--开始[{}]", businessId);
			Result result = null;
			List<ConfirmWithholdListVO> list = repaymentBizPlanListService.listConfirmWithhold(businessId);
			result = Result.success(list);
			logger.info("@listConfirmWithhold@查找业务代扣确认列表--结束[{}]", result);
			return result;
		} catch (Exception e) {
			logger.error("查找业务代扣确认列表失败--[{}]", e);
			return Result.error("-500", "系统异常:查找业务代扣确认列表失败");
		}
	}

	@GetMapping(value = "/confirmWithhold")
	@ApiOperation(value = "代扣确认")
	public Result confirmWithhold(String businessId, String afterId) {
		try {
			logger.info("@confirmWithhold@代扣确认--开始[{}]", businessId);
			Result result = null;

			EntityWrapper<RepaymentBizPlanList> ew = new EntityWrapper<RepaymentBizPlanList>();
			ew.eq("business_id", businessId);
			if (afterId != null) {
				ew.eq("after_id", afterId);
			}
			ew.andNew().isNull("confirm_flag").or().eq("confirm_flag", 0);
			List<RepaymentBizPlanList> list = repaymentBizPlanListService.selectList(ew);
			for (RepaymentBizPlanList planList : list) {
				planList.setConfirmFlag(1);
				planList.setAutoWithholdingConfirmedDate(new Date());
				planList.setAutoWithholdingConfirmedUser(loginUserInfoHelper.getUserId());
				planList.setAutoWithholdingConfirmedUserName(loginUserInfoHelper.getLoginInfo().getUserName());
				planList.updateById();
			}

			logger.info("@confirmWithhold@代扣确认--结束[{}]", result);
			return Result.success();
		} catch (Exception e) {
			logger.error("@confirmWithhold@代扣确认失败--[{}]", e);
			e.printStackTrace();
			return Result.error("-500", "系统异常:代扣确认失败");
		}
	}
	
	/*@GetMapping(value="/listRepaymentSettleListVOs")
	@ApiOperation(value="还款计划")
	public Result listRepaymentSettleListVOs(String businessId,String planId) {
		try {
			logger.info("@listRepaymentSettleListVOs@还款计划--开始[{}]", businessId);
			Result result = null;
			List<RepaymentSettleListVO> list = repaymentBizPlanService.listRepaymentSettleListVOs(businessId, planId);
			result = Result.success(list);
			logger.info("@listRepaymentSettleListVOs@还款计划--结束[{}]", result);
			return result;
		} catch (Exception e) {
			logger.error("@listRepaymentSettleListVOs@还款计划--[{}]", e);
			e.printStackTrace();
			return Result.error("500", "系统异常:还款计划失败");
		}
	}*/

	
	@ApiOperation(value = "分润")
	@PostMapping("/shareProfit")
	public Result shareProfit(@RequestParam("businessId") String businessId,@RequestParam("afterId") String afterId,@RequestParam("logId") Integer logId){
		logger.info("@shareProfit@自动代扣核销--开始[{}{}{}]", businessId,afterId,logId);
		Result result=new Result();
		WithholdingRepaymentLog log=withholdingRepaymentLogService.selectById(logId);
	
		try {
			ConfirmRepaymentReq req=new ConfirmRepaymentReq();
			List<Integer> list=new ArrayList<Integer>();
			List<Integer> logIds=new ArrayList<Integer>();
			
			//调用方标志位  10：财务人员还款确认（线下转账），20：自动线下代扣，21：人工线下代扣，30：自动银行代扣，31：人工银行代扣
			if(log.getBindPlatformId()==PlatformEnum.YH_FORM.getValue()&&log.getCreateUser().equals("auto_run")) {//自动银行代扣已还款
				req.setCallFlage(30);
			}else if(log.getBindPlatformId()==PlatformEnum.YH_FORM.getValue()&&(!log.getCreateUser().equals("auto_run"))) {//人工银行代扣已还款
				req.setCallFlage(31);
			}else if(log.getBindPlatformId()!=PlatformEnum.YH_FORM.getValue()&&log.getCreateUser().equals("auto_run")) {//20：自动线下代扣已还款
				req.setCallFlage(20);
			}else if(log.getBindPlatformId()!=PlatformEnum.YH_FORM.getValue()&&(!log.getCreateUser().equals("auto_run"))){//21，人工线下代扣已还款
				req.setCallFlage(21);
			}
			
			
			logIds.add(logId);
//			list.add(30);//还款来源银行代扣
			req.setLogIds(logIds);
			req.setAfterId(afterId);
			req.setBusinessId(businessId);
//			req.setRepaySource(list);
			shareProfitService.execute(req, true);
              result.success(1);
      		logger.info("@shareProfit@自动代扣核销--开始[{}{}{}]", businessId,afterId,logId);
		} catch (Exception ex) {
			logger.error("分润出现异常"+ex);
			return Result.error("-1","分润出现异常"+ex);
		}
		return result;
	}
	
	@ApiOperation(value = "网关充值快捷充值核心接口")
	@PostMapping("/recharge")
	public Result recharge(@RequestBody ConfirmRepaymentReq req){
		logger.info("@recharge@网关充值快捷充值核心接口--开始[{}]", req);
		Result result=new Result();
		try {
			List<CurrPeriodProjDetailVO> list = shareProfitService.execute(req, true);
			logger.info("@recharge@网关充值快捷充值核心接口--结束[{}]", list);
			return result.success(list);
		} catch (Exception ex) {
			logger.error("分润出现异常"+ex);
			return Result.error("-1","分润出现异常"+ex);
		}
	}
	
	@ApiOperation(value = "删除财务新增的银行流水")
	@GetMapping("/deleteMoneyPool")
	public Result deleteMoneyPool(String mprId) {
		try {
			logger.info("@deleteMoneyPool@删除财务新增的银行流水--开始[{}]", mprId);
			Result result = null;
			boolean res = moneyPoolRepaymentService.deleteFinanceAddStatement(mprId);
			result = Result.success();
			logger.info("@deleteMoneyPool@删除财务新增的银行流水--结束[{}]", result);
			return result;
		} catch (Exception e) {
			logger.error("@deleteMoneyPool@删除财务新增的银行流水--[{}]", e);
			return Result.error("500", "删除财务新增的银行流水失败");
		}
		
		
	}
	
	@ApiOperation(value = "查看所有银行账号")
	@GetMapping("/listDepartmentBank")
	public Result listDepartmentBank() {
		logger.info("@listDepartmentBank@查看所有银行账号--开始[]");
		Result result = null;
		List<DepartmentBank> res = departmentBankService.listDepartmentBank();
		result = Result.success(res);
		logger.info("@listDepartmentBank@查看所有银行账号--结束[{}]", result);
		return result;
	}

	@RequestMapping("/importExcel")
	public Result importExcel(@RequestParam("file") MultipartFile file,
            HttpServletRequest request)  {
		Result result = null ;
		logger.info("@importExcel@导入银行流水Excel--开始[]");
		try {
			ImportParams ip = new ImportParams() ;
			ip.setKeyIndex(8);
			List<MoneyPoolExcelEntity> list =  ExcelImportUtil.importExcel(file.getInputStream(), MoneyPoolExcelEntity.class, ip);
			if (list==null||list.isEmpty()) {
				result = Result.error("500", "Excel没有数据");
				logger.info("@importExcel@导入银行流水Excel--结束[{}]",result);
				return result;
			}
			List<MoneyPool> moneyPools = new ArrayList<>();
			for (MoneyPoolExcelEntity entity : list) {
				MoneyPool moneyPool = entity.transform();
				if (moneyPool==null) {
					continue;
				}
				
				if (!StringUtil.isEmpty(entity.getPayCode())) {
					List<MoneyPool> s = moneyPoolService.selectList(new EntityWrapper<MoneyPool>().eq("pay_code", entity.getPayCode()));
					if (s.size()>0) {
						result = Result.error("500", "款项编码与数据库已存在数据重复,请修改:"+entity.getPayCode());
						logger.info("@importExcel@导入银行流水Excel--结束[{}]",result);
						return result ;
					}
					
				}
				
				moneyPool.setCreateUser(loginUserInfoHelper.getUserId());
				moneyPool.setImportUser(loginUserInfoHelper.getUserId());
				if(loginUserInfoHelper.getLoginInfo()!=null&&loginUserInfoHelper.getLoginInfo().getUserName()!=null) {
					moneyPool.setImportUserName(loginUserInfoHelper.getLoginInfo().getUserName());
				}
				moneyPools.add(moneyPool);
			}
			
			
			if (moneyPools.isEmpty()) {
				result = Result.error("500", "Excel内容格式错误");
				logger.info("@importExcel@导入银行流水Excel--结束[{}]",result);
				return result;
			}
			
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					if (i==j) {
						continue;
					}
					if (list.get(i).getPayCode().equals(list.get(j).getPayCode())) {
						result = Result.error("500", "款项编码重复,请修改:"+list.get(i).getPayCode());
						logger.info("@importExcel@导入银行流水Excel--结束[{}]",result);
						return result;
					}
				}
			}
			boolean insertRes = moneyPoolService.insertBatch(moneyPools, moneyPools.size());
			if (insertRes) {
				result = Result.success();return result;
			}else {
				result = Result.error("500", "数据库存储失败");return result;
			}
		} catch (IOException e) {
			logger.info("@importExcel@导入银行流水Excel--IOException[{}]",e.getMessage());
			result = Result.error("500", "文件读错误");
			return result;
		}catch (Exception e) {
			logger.info("@importExcel@导入银行流水Excel--Exception[{}]",e.getMessage());
			e.printStackTrace();
			result = Result.error("500", e.getMessage());
			return result;
		}
		
	}


	@ApiOperation(value = "查找财务人员跟单设置查询相关信息")
	@GetMapping("/getOrderSetSearchInfo")
	public Result getOrderSetSearchInfo(){

        logger.info("@getOrderSetSearchInfo@查找财务人员跟单设置查询相关信息--开始[]");
        Result result = null;
        Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();
        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));
        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company", (JSONArray) JSON.toJSON(company_list, JsonUtil.getMapping()));
        //业务类型
        List<BasicBusinessType> btype_list = basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
        retMap.put("businessType", (JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));
        //查询用户
        //List<SysUser> users = sysUserService.selectList(new EntityWrapper<>());
        List<SysUser> users = sysUserService.selectUsersByRole(SysRoleEnums.DH_CASHIER.getKey());
        retMap.put("users", (JSONArray) JSON.toJSON(users, JsonUtil.getMapping()));

        logger.info("@getOrderSetSearchInfo@查找财务人员跟单设置查询相关信息--结束[{}]", JSON.toJSONString(retMap));
        return Result.success(retMap);

	}



	@GetMapping(value = "/getOrderSetPage")
	@ApiOperation(value = "根据条件获取财务人员跟单设置，分页")
	public PageResult getOrderSetPage(OrderSetReq req) {
		PageResult result;
		logger.info("@getOrderSetPage@根据条件获取财务人员跟单设置--开始[{}]", JSON.toJSONString(req));
		EntityWrapper ew = new EntityWrapper<SysFinancialOrder>();

		if (req.getCompanyId() != null) {
			ew.eq("company_id", req.getCompanyId());
		}
		if (req.getAreaId() != null) {
			ew.eq("area_id", req.getAreaId());
		}
		if (req.getUserName() != null) {
			ew.like("user_names", req.getUserName());
		}
		Page<MoneyPool> page = sysFinancialOrderService.selectPage(new Page<SysFinancialOrder>(req.getCurPage(), req.getPageSize()),
				ew);
		result = PageResult.success(page.getRecords(), page.getTotal());
		logger.info("@moneyPool@根据条件获取款项池--结束[{}]", JSON.toJSONString(result));
		return result;

	}


    @ApiOperation(value = "获取区域信息")
    @GetMapping("/company/getCompanyAreaList")
    @ResponseBody
    public Result<Map<String, JSONArray>> getCompanyAreaList(@RequestParam("id") String id) {
        int type;
        if ("1".equals(id)) {//区域
            type = AreaLevel.AREA_LEVEL.getKey();
        } else {//分公司
            type = AreaLevel.COMPANY_LEVEL.getKey();
        }
        Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();
        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", type));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));

        return Result.success(retMap);
    }


	@RequestMapping("/search")
	@ApiOperation("查询")
	public PageResult search(ServletRequest request, LayTableQuery query) {
		try {
			EntityWrapper<SysFinancialOrder> ew = new EntityWrapper<>();

			String userName = request.getParameter("userName");
			String areaId = request.getParameter("areaId");
			String companyId = request.getParameter("companyId");
			String businessTypeId = request.getParameter("businessTypeId");
           /* if (StringUtils.isNotBlank(userName)) {
                ew.like("user_names", userName);
            }
            if (StringUtils.isNotBlank(companyId)) {
                ew.eq("company_id", companyId);
            }
            if (StringUtils.isNotBlank(areaId)) {
                ew.eq("area_id", areaId);
            }
            if (StringUtils.isNotBlank(businessTypeId)) {
                ew.eq("business_type_id", Integer.valueOf(businessTypeId));
            }*/
			//Page<SysFinancialOrder> page = sysFinancialOrderService.selectPage(new Page<>(query.getPage(), query.getLimit()), ew);
			Page<SysFinancialOrderVO> page = sysFinancialOrderService.search(
					new Page<>(query.getPage(), query.getLimit()),
					StringUtils.isBlank(businessTypeId) ? null : Integer.valueOf(businessTypeId),
					areaId, companyId, userName
			);
			return PageResult.success(page.getRecords(), page.getTotal());
		} catch (Exception ex) {
			logger.error("根据条件获取财务人员跟单设置失败", ex);
			return PageResult.error(500, ex.getMessage());
		}
	}


	@RequestMapping("/edit")
	@ApiOperation("编辑")
    public Result edit(@RequestBody SysFinancialOrderReq vo) {
        if (vo.getCompanyId() == null || vo.getCompanyId().size() == 0 || vo.getBusinessType() == null || vo.getBusinessType().size() == 0 || vo.getCollectionGroup1Users() == null || vo.getCollectionGroup1Users().size() == 0) {
			return Result.error("参数验证失败");
		}
		try{
            //新增
            sysFinancialOrderService.save(vo);
			return  Result.success();
		}catch (Exception e){
			logger.error("设置财务人员跟单设置失败", e);
			return Result.error( e.getMessage());
		}
	}

	/*@RequestMapping("/delete")
	@ApiOperation(value = "删除")
	public  Result delete(Integer id, String userId){
		try{
			sysFinancialOrderService.delete(id, userId);
			return Result.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return Result.error(e.getMessage());
		}
	}*/


	@RequestMapping("/delete")
	@ApiOperation(value = "删除")
    public Result delete(Integer id) {
		try{
            sysFinancialOrderService.delete(id);
			return Result.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	@RequestMapping("/lastRepayConfirm")
	@ApiOperation(value="查询上次还款来源")
	public Result lastRepayConfirm(String businessId,String afterId) {
		try {
			logger.info("@lastRepayConfirm@查询上次还款来源--开始[{}]");
			List<RepaymentConfirmLog> list = confrimLogService.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("org_business_id", businessId).eq("after_id",afterId).orderBy("idx",false));
			if (list==null||list.size()==0) {
				logger.info("@lastRepayConfirm@查询上次还款来源--结束[0]");
				return Result.success(0);
			}
			logger.info("@lastRepayConfirm@查询上次还款来源--结束[{}]",list.get(0).getRepaySource());
			return Result.success(list.get(0).getRepaySource());
		} catch (Exception e) {
			logger.info("@lastRepayConfirm@查询上次还款来源--结束[{}]",e.getMessage());
			return Result.error("查询上次还款来源异常");
		}
	}


	@RequestMapping("/checkLastRepay")
	@ApiOperation(value = "检查前面的还款计划是否有未还垫付")
	public Result checkLastRepay(String businessId, String afterId) {
		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectOne(
				new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId).eq("after_id", afterId));
		List<TuandaiProjectInfo> list = tuandaiProjectInfoService
				.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", businessId));
		for (TuandaiProjectInfo tuandaiProjectInfo : list) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", tuandaiProjectInfo.getProjectId());

			Map<String, Object> resultMap = new HashMap<>();

			com.ht.ussp.core.Result result = eipRemote.returnAdvanceShareProfit(paramMap);

			if (result != null && result.getData() != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

				TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = JSONObject
						.parseObject(JSONObject.toJSONString(result.getData()), TdReturnAdvanceShareProfitResult.class);
				List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
						.getReturnAdvanceShareProfits();
			}
		}
		return null;
	}
}
