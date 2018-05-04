/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.html.parser.Entity;

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
import com.baomidou.mybatisplus.entity.Columns;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.req.MoneyPoolReq;

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
	@Autowired
	@Qualifier("MoneyPoolService")
	private MoneyPoolService moneyPoolService ;
	@Autowired
	@Qualifier("BasicCompanyService")
	private BasicCompanyService basicCompanyService ;
	
	@GetMapping(value="/repayBaseInfo")
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
	
	@GetMapping(value="/moneyPool")
	@ApiOperation(value="根据条件获取款项池")
	public Result moneyPool(MoneyPoolReq req) {
		Result result ;
		logger.info("@moneyPool@根据条件获取款项池--开始[{}]",req);
		EntityWrapper ew = new EntityWrapper<MoneyPool>();
		if (req.getMoneyPoolId()!=null&&req.getMoneyPoolId().length()>0) {
			ew.eq("money_pool_id", req.getMoneyPoolId()) ;
		}else {
			if (req.getAcceptBank()!=null&&req.getAcceptBank().length()>0) {
				ew.eq("accept_bank", req.getAcceptBank());
			}
			if (req.getRepayDate()!=null) {
				ew.eq("DATE_FORMAT(trade_date,'%Y-%m-%d')", req.getRepayDate());
			}
			if (req.getAccountMoney()!=null) {
				ew.eq("account_money", req.getAccountMoney());	
			}
			if (req.getStatus()!=null&&req.getStatus().length()>0) {
				ew.eq("status", req.getStatus());
			}
		}
		List<MoneyPool> list = moneyPoolService.selectList(ew);
		result = Result.success(list);
		logger.info("@moneyPool@根据条件获取款项池--结束[{}]",result);
		return result;
		
	}
	
	@PostMapping(value="/matchBankStatement")
	@ApiOperation(value="还款计划匹配银行流水")
	public Result matchBankStatement(@RequestBody JSONObject req) {
		logger.info("@matchBankStatement@还款计划匹配银行流水--开始[{}]",req.toJSONString());
		String businessId = req.getString("businessId");
		String afterId = req.getString("afterId");
		JSONArray array = req.getJSONArray("array") ;
		List<MoneyPool> moneyPools = new ArrayList<>();
		for (Object obj : array) {
			moneyPools.add((MoneyPool)obj);
		}
		RepaymentBizPlanList planList = repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));
		logger.info("@matchBankStatement@还款计划匹配银行流水--结束[{}]",req.toJSONString());
		return null;
		//TODO
	}
	
	@GetMapping(value="/getCompanys")
	@ApiOperation(value="获取所有分公司数据")
	public Result getCompanys() {
		logger.info("@getCompanys@获取所有分公司数据--开始[]");
		Result result ;
		//公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        result = Result.success(company_list);
        logger.info("@getCompanys@获取所有分公司数据--结束[{}]",result);
        return result;
	}
	
	@GetMapping(value="/getBusinessType")
	@ApiOperation(value="获取所有业务类型")
	public Result getBusinessType() {
		logger.info("@getBusinessType@获取所有业务类型--开始[]");
		Result result ;
		List<BasicBusinessType> list = basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>());
		result = Result.success(list) ;
        logger.info("@getBusinessType@获取所有业务类型--结束[{}]",result);
        return result;
	}
	@GetMapping(value="/getFinanceMangerList")
	@ApiOperation(value="获取财务管理列表数据")
	public PageResult getFinanceMangerList(FinanceManagerListReq req) {
		logger.info("@getFinanceMangerList@获取财务管理列表数据--开始[{}]",req);
		PageResult pageResult = repaymentBizPlanListService.selectByFinanceManagerListReq(req);
		logger.info("@getFinanceMangerList@获取财务管理列表数据--结束[{}]",pageResult);
		return pageResult ;
	}
}
