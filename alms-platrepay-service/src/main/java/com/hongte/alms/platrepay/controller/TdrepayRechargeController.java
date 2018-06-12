package com.hongte.alms.platrepay.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.platrepay.service.TdrepayRechargeService;
import com.hongte.alms.platrepay.vo.TdrepayRechargeInfoVO;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/tdrepayRecharge")
public class TdrepayRechargeController {

	private static final Logger LOG = LoggerFactory.getLogger(TdrepayRechargeController.class);

	private static final String INVALID_PARAM_CODE = "-9999";
	private static final String INVALID_PARAM_DESC = " Parameters cannot be null";

	@Autowired
	@Qualifier("TdrepayRechargeService")
	private TdrepayRechargeService tdrepayRechargeService;
	
	@Autowired
	@Qualifier("BasicCompanyService")
	private BasicCompanyService basicCompanyService;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "代充值资金分发参数接入接口")
	@PostMapping("/accessTdrepayReCharge")
	@ResponseBody
	public Result accessTdrepayReCharge(@RequestBody TdrepayRechargeInfoVO vo) {
		if (vo == null) {
			return Result.error(INVALID_PARAM_CODE, INVALID_PARAM_DESC);
		}
		if (StringUtil.isEmpty(vo.getProjectId())) {
			return Result.error(INVALID_PARAM_CODE, "projectId" + INVALID_PARAM_DESC);
		}
		if (vo.getAssetType() == null) {
			return Result.error(INVALID_PARAM_CODE, "assetType" + INVALID_PARAM_DESC);
		}
		if (StringUtil.isEmpty(vo.getOrigBusinessId())) {
			return Result.error(INVALID_PARAM_CODE, "origBusinessId" + INVALID_PARAM_DESC);
		}
		if (vo.getBusinessType() == null) {
			return Result.error(INVALID_PARAM_CODE, "businessType" + INVALID_PARAM_DESC);
		}
		if (vo.getFactRepayDate() == null) {
			return Result.error(INVALID_PARAM_CODE, "factRepayDate" + INVALID_PARAM_DESC);
		}
		if (StringUtil.isEmpty(vo.getCustomerName())) {
			return Result.error(INVALID_PARAM_CODE, "customerName" + INVALID_PARAM_DESC);
		}
		if (StringUtil.isEmpty(vo.getCompanyName())) {
			return Result.error(INVALID_PARAM_CODE, "companyName" + INVALID_PARAM_DESC);
		}
		if (vo.getRepaySource() == null) {
			return Result.error(INVALID_PARAM_CODE, "repaySource" + INVALID_PARAM_DESC);
		}
		if (StringUtil.isEmpty(vo.getAfterId())) {
			return Result.error(INVALID_PARAM_CODE, "afterId" + INVALID_PARAM_DESC);
		}
		if (vo.getPeriod() == null) {
			return Result.error(INVALID_PARAM_CODE, "period" + INVALID_PARAM_DESC);
		}
		if (vo.getSettleType() == null) {
			return Result.error(INVALID_PARAM_CODE, "settleType" + INVALID_PARAM_DESC);
		}
		if (vo.getResourceAmount() == null) {
			return Result.error(INVALID_PARAM_CODE, "resourceAmount" + INVALID_PARAM_DESC);
		}
		if (vo.getFactRepayAmount() == null) {
			return Result.error(INVALID_PARAM_CODE, "factRepayAmount" + INVALID_PARAM_DESC);
		}
		if (vo.getRechargeAmount() == null) {
			return Result.error(INVALID_PARAM_CODE, "rechargeAmount" + INVALID_PARAM_DESC);
		}
		if (vo.getAdvanceType() == null) {
			return Result.error(INVALID_PARAM_CODE, "advanceType" + INVALID_PARAM_DESC);
		}
		if (vo.getIsComplete() == null) {
			return Result.error(INVALID_PARAM_CODE, "isComplete" + INVALID_PARAM_DESC);
		}
		if (CollectionUtils.isEmpty(vo.getDetailList())) {
			return Result.error(INVALID_PARAM_CODE, "detailList" + INVALID_PARAM_DESC);
		}

		if (!StringUtil.isEmpty(vo.getProjPlanListId())) {
			int count = tdrepayRechargeLogService.selectCount(
					new EntityWrapper<TdrepayRechargeLog>().eq("proj_plan_list_id", vo.getProjPlanListId()));
			if (count > 0) {
				return Result.error(INVALID_PARAM_CODE, "实还流水：" + vo.getProjPlanListId() + " 重复推送！");
			}
		} else {
			int count = tdrepayRechargeLogService.selectCount(new EntityWrapper<TdrepayRechargeLog>()
					.eq("project_id", vo.getProjectId()).eq("period", vo.getPeriod()));
			if (count > 0) {
				return Result.error(INVALID_PARAM_CODE, "标ID：" + vo.getProjectId() + "，期次：" + vo.getPeriod() + "，重复推送！");
			}
		}

		try {
			tdrepayRechargeService.saveTdrepayRechargeInfo(vo);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-500", "系统异常，接口调用失败");
		}

		return Result.success();
	}
	
	@ApiOperation(value = "查询合规化还款主页面列表")
	@GetMapping("/queryComplianceRepaymentData")
	@ResponseBody
	public PageResult<List<TdrepayRechargeLog>> queryComplianceRepaymentData(@ModelAttribute ComplianceRepaymentVO vo) {
		try {
			if (vo != null && vo.getConfirmTimeEnd() != null) {
				vo.setConfirmTimeEnd(DateUtil.addDay2Date(1, vo.getConfirmTimeEnd()));
			}
			int count = tdrepayRechargeService.countComplianceRepaymentData(vo);
			if (count == 0) {
				return PageResult.success(count);
			}
			
			List<TdrepayRechargeLog> list = tdrepayRechargeService.queryComplianceRepaymentData(vo);
			if (CollectionUtils.isNotEmpty(list)) {
				return PageResult.success(list, count);
			}
			return PageResult.success(0);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return PageResult.error(-99, "系统异常，接口调用失败");
		}
	}
	
	@ApiOperation(value = "查询所有分公司")
	@GetMapping("/queryAllBusinessCompany")
	@ResponseBody
	public Result<List<BasicCompany>> queryAllBusinessCompany() {
		try {
			return Result.success(basicCompanyService.selectList(new EntityWrapper<>()));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", "系统异常");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "执行资金分发")
	@PostMapping("/userDistributeFund")
	@ResponseBody
	public Result userDistributeFund(@RequestBody List<TdrepayRechargeInfoVO> vos) {
		try {
			if (CollectionUtils.isEmpty(vos)) {
				return Result.error("-99", "请选择要资金分发的数据");
			}
			
			tdrepayRechargeService.tdrepayRecharge(vos);
			
			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}
}
