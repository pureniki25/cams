/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.finance.ConfirmWithholdListVO;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.req.MoneyPoolReq;
import com.hongte.alms.finance.service.FinanceService;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

/**
 * @author 王继光 2018年4月27日 下午6:00:37
 */
@RestController
@RefreshScope
@RequestMapping(value = "/calLateFeeController")
public class CalLateFeeController {

	private static Logger logger = LoggerFactory.getLogger(CalLateFeeController.class);


	@Autowired
	@Qualifier("RepaymentBizPlanService")
	private RepaymentBizPlanService repaymentBizPlanService ;
	
	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;
	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper ;

	@GetMapping(value = "/calLateFee")
	@ApiOperation(value = "计算滞纳金")
	public Result calLateFee() {
		Result result=new Result();
		logger.info("@calLateFee@计算滞纳金--开始[{},{}]");
		repaymentProjPlanListService.calLateFee();
		logger.info("@calLateFee@计算滞纳金--结束[{}]");
		result.success(1);
		return result;
	}
	
	@GetMapping(value = "/calLateFeeByFactRpeayDate")
	@ApiOperation(value = "根据实还日期重新计算滞纳金")
	public Result calLateFeeByFactRpeayDate(@RequestParam("beginDate") String beginDate,@RequestParam("endDate") String endDate) {
		Date beginDate1=DateUtil.getDate(beginDate);
		Date endDate1=DateUtil.getDate(endDate);
		logger.info("@calLateFee@计算滞纳金--开始[{},{}]");
		List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList((new EntityWrapper<RepaymentBizPlan>().eq("src_type", 2)).gt("create_time", beginDate1).lt("create_time", endDate1));
		
		for(RepaymentBizPlan plan:plans) {
			List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.getPlanListForCalLateFee(plan.getPlanId());
			    for(RepaymentBizPlanList pList:pLists) {
			    	List<RepaymentProjPlanList> projList = repaymentProjPlanListService.getProListForCalLateFee(pList.getPlanListId());
						for (RepaymentProjPlanList projPList : projList) {
							// 每个表的还款计划列表对应所的标的还款计划
							repaymentProjPlanListService.calLateFeeForPerPList(pList,null);
				    }
			}
		}
		Result result=new Result();
		result.success(1);
		logger.info("@calLateFee@计算滞纳金--结束[{}]");
		return result;
	}

	
}
