package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.platrepay.dto.TdGuaranteePaymentDTO;
import com.hongte.alms.platrepay.dto.TdProjectPaymentDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitResult;
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

	@Autowired
	@Qualifier("IssueSendOutsideLogService")
	private IssueSendOutsideLogService issueSendOutsideLogService;

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
		if (StringUtil.isEmpty(vo.getTdUserId())) {
			return Result.error(INVALID_PARAM_CODE, "tdUserId" + INVALID_PARAM_DESC);
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
		if (vo.getConfirmLogId() == null) {
			return Result.error(INVALID_PARAM_CODE, "confirmLogId" + INVALID_PARAM_DESC);
		}
		if (vo.getIsComplete() == null) {
			return Result.error(INVALID_PARAM_CODE, "isComplete" + INVALID_PARAM_DESC);
		}
		if (CollectionUtils.isEmpty(vo.getDetailList())) {
			return Result.error(INVALID_PARAM_CODE, "detailList" + INVALID_PARAM_DESC);
		}

		try {
			// 根据projectId查询平台还垫付信息
			Map<String, com.ht.ussp.core.Result> resultMap = tdrepayRechargeService
					.getAdvanceShareProfitAndProjectPayment(vo.getProjectId());
			com.ht.ussp.core.Result queryProjectPaymentResult = resultMap.get("queryProjectPaymentResult");
			com.ht.ussp.core.Result advanceShareProfitResult = resultMap.get("advanceShareProfitResult");

			if (isPastPeriodAdvance(vo, queryProjectPaymentResult, advanceShareProfitResult)) {
				return Result.error("-99", "该标的号存在往期还垫付未结清记录");
			}
			
			tdrepayRechargeService.saveTdrepayRechargeInfo(vo);
		} catch (ServiceRuntimeException e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-500", e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-500", "系统异常，接口调用失败");
		}

		return Result.success();
	}

	@SuppressWarnings("rawtypes")
	private boolean isPastPeriodAdvance(TdrepayRechargeInfoVO vo, com.ht.ussp.core.Result queryProjectPaymentResult,
			com.ht.ussp.core.Result advanceShareProfitResult) {
		if (queryProjectPaymentResult != null && advanceShareProfitResult != null
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())) {
			// 标的还款信息
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;
			if (queryProjectPaymentResult.getData() != null) {
				JSONObject parseObject = (JSONObject) JSONObject.toJSON(queryProjectPaymentResult.getData());
				if (parseObject.get("projectPayments") != null) {
					tdProjectPaymentDTOs = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
				}
			}

			// 还垫付信息
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = null;
			if (advanceShareProfitResult.getData() != null) {
				String json2 = JSONObject.toJSONString(advanceShareProfitResult.getData());
				returnAdvanceShareProfitResult = JSONObject.parseObject(json2, TdReturnAdvanceShareProfitResult.class);
			}

			// 担保公司垫付信息
			double principalAndInterest = 0; // 本金利息
			double tuandaiAmount = 0; // 实还平台服务费
			double orgAmount = 0; // 实还资产端服务费
			double guaranteeAmount = 0; // 实还担保公司服务费
			double arbitrationAmount = 0; // 实还仲裁服务费

			// 还垫付信息
			double principalAndInterest2 = 0; // 本金利息
			double tuandaiAmount2 = 0; // 实还平台服务费
			double orgAmount2 = 0; // 实还资产端服务费
			double guaranteeAmount2 = 0; // 实还担保公司服务费
			double arbitrationAmount2 = 0; // 实还仲裁服务费

			// 得到往期的标的还款信息
			List<TdProjectPaymentDTO> pastPeriodDTOs = handlePastPeriodTdProjectPaymentDTO(vo.getPeriod(),
					tdProjectPaymentDTOs);

			// 得到往期的标的还垫付信息
			List<TdReturnAdvanceShareProfitDTO> tdReturnAdvanceShareProfitDTOs = handlePastPeriodTdReturnAdvanceShareProfitDTO(
					returnAdvanceShareProfitResult, vo.getPeriod());

			// 判断往期是否有未还垫付记录
			for (TdProjectPaymentDTO tdProjectPaymentDTO : pastPeriodDTOs) {

				TdGuaranteePaymentDTO guaranteePayment = tdProjectPaymentDTO.getGuaranteePayment();

				if (guaranteePayment != null) {

					int tdPeriod = tdProjectPaymentDTO.getPeriod();

					principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? 0
							: guaranteePayment.getPrincipalAndInterest().doubleValue();
					tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? 0
							: guaranteePayment.getTuandaiAmount().doubleValue();
					orgAmount = guaranteePayment.getOrgAmount() == null ? 0
							: guaranteePayment.getOrgAmount().doubleValue();
					guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? 0
							: guaranteePayment.getGuaranteeAmount().doubleValue();
					arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? 0
							: guaranteePayment.getArbitrationAmount().doubleValue();

					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : tdReturnAdvanceShareProfitDTOs) {
						if (tdReturnAdvanceShareProfitDTO.getPeriod() == tdPeriod) {
							principalAndInterest2 = tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest().doubleValue();
							tuandaiAmount2 = tdReturnAdvanceShareProfitDTO.getTuandaiAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getTuandaiAmount().doubleValue();
							orgAmount2 = tdReturnAdvanceShareProfitDTO.getOrgAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getOrgAmount().doubleValue();
							guaranteeAmount2 = tdReturnAdvanceShareProfitDTO.getGuaranteeAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getGuaranteeAmount().doubleValue();
							arbitrationAmount2 = tdReturnAdvanceShareProfitDTO.getArbitrationAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getArbitrationAmount().doubleValue();
						}
					}

					// 往期剩余多少没有还
					double principalAndInterest3 = BigDecimal.valueOf(principalAndInterest - principalAndInterest2)
							.doubleValue();
					double tuandaiAmount3 = BigDecimal.valueOf(tuandaiAmount - tuandaiAmount2).doubleValue();
					double orgAmount3 = BigDecimal.valueOf(orgAmount - orgAmount2).doubleValue();
					double guaranteeAmount3 = BigDecimal.valueOf(guaranteeAmount - guaranteeAmount2).doubleValue();
					double arbitrationAmount3 = BigDecimal.valueOf(arbitrationAmount - arbitrationAmount2)
							.doubleValue();
					double totalAmount = BigDecimal.valueOf(
							principalAndInterest3 + tuandaiAmount3 + orgAmount3 + guaranteeAmount3 + arbitrationAmount3)
							.doubleValue();

					if (totalAmount > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 得到往期的标的还垫付信息
	 * 
	 * @param returnAdvanceShareProfitResult
	 * @param period
	 * @return
	 */
	private List<TdReturnAdvanceShareProfitDTO> handlePastPeriodTdReturnAdvanceShareProfitDTO(
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult, Integer period) {
		List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfitDTOs = new ArrayList<>();

		if (returnAdvanceShareProfitResult != null
				&& CollectionUtils.isNotEmpty(returnAdvanceShareProfitResult.getReturnAdvanceShareProfits())) {

			List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
					.getReturnAdvanceShareProfits();

			// 得到往期的标的还垫付信息
			for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
				if (period != null && tdReturnAdvanceShareProfitDTO.getPeriod() < period.intValue()) {
					returnAdvanceShareProfitDTOs.add(tdReturnAdvanceShareProfitDTO);
				}
			}
		}
		return returnAdvanceShareProfitDTOs;
	}

	/**
	 * 得到往期的标的还款信息
	 * 
	 * @param period
	 * @param projectPaymentResult
	 * @return
	 */
	private List<TdProjectPaymentDTO> handlePastPeriodTdProjectPaymentDTO(Integer period,
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs) {

		List<TdProjectPaymentDTO> pastPeriodDTOs = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
			// 得到当期的标的还款信息
			for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
				if (period != null && tdProjectPaymentDTO.getPeriod() < period.intValue()) {
					pastPeriodDTOs.add(tdProjectPaymentDTO);
				}
			}
		}

		return pastPeriodDTOs;
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
