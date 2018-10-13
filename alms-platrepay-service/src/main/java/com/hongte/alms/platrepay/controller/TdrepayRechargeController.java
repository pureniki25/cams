package com.hongte.alms.platrepay.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.dto.compliance.TdAdvanceShareProfitDTO;
import com.hongte.alms.base.dto.compliance.TdProjectPaymentInfoResult;
import com.hongte.alms.base.dto.compliance.TdRefundMonthInfoDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.BankEnum;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.vo.compliance.AgencyRechargeLogVO;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.base.vo.compliance.RechargeRecordReq;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.platrepay.dto.TdGuaranteePaymentDTO;
import com.hongte.alms.platrepay.dto.TdProjectPaymentDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitResult;
import com.hongte.alms.platrepay.enums.PlatformStatusTypeEnum;
import com.hongte.alms.platrepay.enums.ProcessStatusTypeEnum;
import com.hongte.alms.platrepay.enums.RepaySourceEnum;
import com.hongte.alms.platrepay.vo.TdGuaranteePaymentVO;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.util.BeanUtils;

import io.swagger.annotations.ApiOperation;

//@CrossOrigin
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

	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	private TuandaiProjectInfoService tuandaiProjectInfoService;

	@Autowired
	@Qualifier("RepaymentProjPlanService")
	private RepaymentProjPlanService repaymentProjPlanService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Autowired
	@Qualifier("SysUserRoleService")
	SysUserRoleService sysUserRoleService;

	@Autowired
	private EipRemote eipRemote;

	@SuppressWarnings({ "rawtypes" })
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
		if (vo.getConfirmTime() == null) {
			return Result.error(INVALID_PARAM_CODE, "confirmTime" + INVALID_PARAM_DESC);
		}
		if (CollectionUtils.isEmpty(vo.getDetailList())) {
			return Result.error(INVALID_PARAM_CODE, "detailList" + INVALID_PARAM_DESC);
		}

		try {

			int count = tdrepayRechargeLogService.selectCount(new EntityWrapper<TdrepayRechargeLog>()
					.eq("project_id", vo.getProjectId()).eq("confirm_log_id", vo.getConfirmLogId()).eq("is_valid", 1)
					.eq("after_id", vo.getAfterId()));
			if (count > 0) {
				return Result.error("-99", "异常：数据重复推送");
			}

			// 根据projectId查询平台还垫付信息
			/*
			 * Map<String, com.ht.ussp.core.Result> resultMap = tdrepayRechargeService
			 * .getAdvanceShareProfitAndProjectPayment(vo.getProjectId());
			 * com.ht.ussp.core.Result queryProjectPaymentResult =
			 * resultMap.get("queryProjectPaymentResult"); com.ht.ussp.core.Result
			 * advanceShareProfitResult = resultMap.get("advanceShareProfitResult");
			 * 
			 * if (isPastPeriodAdvance(vo, queryProjectPaymentResult,
			 * advanceShareProfitResult)) { return Result.error("-99", "该标的号存在往期还垫付未结清记录");
			 * }
			 */

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
			BigDecimal principalAndInterest = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount = BigDecimal.ZERO; // 实还平台服务费
			BigDecimal orgAmount = BigDecimal.ZERO; // 实还资产端服务费
			BigDecimal guaranteeAmount = BigDecimal.ZERO; // 实还担保公司服务费
			BigDecimal arbitrationAmount = BigDecimal.ZERO; // 实还仲裁服务费

			// 还垫付信息
			BigDecimal principalAndInterest2 = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount2 = BigDecimal.ZERO; // 实还平台服务费
			BigDecimal orgAmount2 = BigDecimal.ZERO; // 实还资产端服务费
			BigDecimal guaranteeAmount2 = BigDecimal.ZERO; // 实还担保公司服务费
			BigDecimal arbitrationAmount2 = BigDecimal.ZERO; // 实还仲裁服务费

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

					principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? principalAndInterest
							: guaranteePayment.getPrincipalAndInterest();
					tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? tuandaiAmount
							: guaranteePayment.getTuandaiAmount();
					orgAmount = guaranteePayment.getOrgAmount() == null ? orgAmount : guaranteePayment.getOrgAmount();
					guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? guaranteeAmount
							: guaranteePayment.getGuaranteeAmount();
					arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? arbitrationAmount
							: guaranteePayment.getArbitrationAmount();

					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : tdReturnAdvanceShareProfitDTOs) {
						if (tdReturnAdvanceShareProfitDTO.getPeriod() == tdPeriod) {
							principalAndInterest2 = tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest() == null
									? principalAndInterest2
									: tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest();
							tuandaiAmount2 = tdReturnAdvanceShareProfitDTO.getTuandaiAmount() == null ? tuandaiAmount2
									: tdReturnAdvanceShareProfitDTO.getTuandaiAmount();
							orgAmount2 = tdReturnAdvanceShareProfitDTO.getOrgAmount() == null ? orgAmount2
									: tdReturnAdvanceShareProfitDTO.getOrgAmount();
							guaranteeAmount2 = tdReturnAdvanceShareProfitDTO.getGuaranteeAmount() == null
									? guaranteeAmount2
									: tdReturnAdvanceShareProfitDTO.getGuaranteeAmount();
							arbitrationAmount2 = tdReturnAdvanceShareProfitDTO.getArbitrationAmount() == null
									? arbitrationAmount2
									: tdReturnAdvanceShareProfitDTO.getArbitrationAmount();
						}
					}

					// 往期剩余多少没有还
					BigDecimal totalAmount = BigDecimal.ZERO;
					totalAmount = totalAmount.add(principalAndInterest.subtract(principalAndInterest2));

					totalAmount = totalAmount.add(tuandaiAmount.subtract(tuandaiAmount2));

					totalAmount = totalAmount.add(orgAmount.subtract(orgAmount2));

					totalAmount = totalAmount.add(guaranteeAmount.subtract(guaranteeAmount2));

					totalAmount = totalAmount.add(arbitrationAmount.subtract(arbitrationAmount2));

					LOG.info("标的ID：{}；往期剩余：{} 垫付未还！", vo.getProjectId(), totalAmount);

					if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
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
	public PageResult<List<TdrepayRechargeInfoVO>> queryComplianceRepaymentData(
			@ModelAttribute ComplianceRepaymentVO vo) {
		try {
			Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<>();
			wrapperSysUserRole.eq("user_id", loginUserInfoHelper.getUserId());
			wrapperSysUserRole.and(
					" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 0 ) ");
			List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
			if (null != userRoles && !userRoles.isEmpty()) {
				vo.setNeedPermission(0);// 全局用户 不需要验证权限
			}

			if (vo != null && vo.getConfirmTimeEnd() != null) {
				vo.setConfirmTimeEnd(DateUtil.addDay2Date(1, vo.getConfirmTimeEnd()));
			}
			vo.setUserId(loginUserInfoHelper.getUserId());
			int count = tdrepayRechargeService.countComplianceRepaymentData(vo);
			if (count == 0) {
				return PageResult.success(count);
			}

			List<TdrepayRechargeLog> list = tdrepayRechargeService.queryComplianceRepaymentData(vo);

			List<TdrepayRechargeInfoVO> resultList = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(list)) {

				Map<String, IssueSendOutsideLog> batchIdMap = new HashMap<>();

				for (TdrepayRechargeLog tdrepayRechargeLog : list) {
					String batchId = tdrepayRechargeLog.getBatchId();
					if (StringUtil.isEmpty(batchId)) {
						continue;
					}
					IssueSendOutsideLog issueSendOutsideLog = batchIdMap.get(batchId);
					if (issueSendOutsideLog == null) {
						issueSendOutsideLog = issueSendOutsideLogService
								.selectOne(new EntityWrapper<IssueSendOutsideLog>()
										.eq("Interfacecode", Constant.INTERFACE_CODE_SEND_DISTRIBUTE_FUND)
										.eq("send_key", batchId));
						if (issueSendOutsideLog != null) {
							batchIdMap.put(batchId, issueSendOutsideLog);
						} else {
							batchIdMap.put(batchId, new IssueSendOutsideLog());
						}
					}
				}

				for (TdrepayRechargeLog tdrepayRechargeLog : list) {
					TdrepayRechargeInfoVO infoVO = BeanUtils.deepCopy(tdrepayRechargeLog, TdrepayRechargeInfoVO.class);
					if (infoVO != null) {
						IssueSendOutsideLog issueSendOutsideLog = batchIdMap.get(infoVO.getBatchId());
						if (issueSendOutsideLog != null) {
							if (JsonUtil.isJSONValid(issueSendOutsideLog.getReturnJson())) {
								JSONObject parseObject = JSONObject.parseObject(issueSendOutsideLog.getReturnJson());
								if (parseObject != null) {
									infoVO.setRemark(parseObject.getString("codeDesc"));
								}
							} else {
								infoVO.setRemark(issueSendOutsideLog.getReturnJson());
							}
						}

						infoVO.setBusinessTypeStr(BusinessTypeEnum.getName(infoVO.getBusinessType()));
						infoVO.setRepaymentTypeStr(RepaySourceEnum.getName(infoVO.getRepaySource()));
						switch (infoVO.getSettleType()) {
						case 0:
							infoVO.setPeriodTypeStr("正常还款");
							break;
						case 10:
							infoVO.setPeriodTypeStr("正常结清");
							break;
						case 11:
							infoVO.setPeriodTypeStr("逾期结清");
							break;
						case 20:
							infoVO.setPeriodTypeStr("展期确认");
							break;
						case 30:
							infoVO.setPeriodTypeStr("坏账结清");
							break;

						default:
							break;
						}
						if (StringUtil.notEmpty(infoVO.getPlatStatus())) {
							infoVO.setPlatformTypeStr(
									PlatformStatusTypeEnum.getName(Integer.valueOf(infoVO.getPlatStatus())));
						}
						infoVO.setProcessStatusStr(ProcessStatusTypeEnum.getName(infoVO.getProcessStatus()));
						infoVO.setFactRepayDateStr(DateUtil.formatDate(infoVO.getFactRepayDate()));
					}
					resultList.add(infoVO);
				}
				return PageResult.success(resultList, count);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "标的还款信息查询接口")
	@GetMapping("/getProjectPayment")
	@ResponseBody
	public Result<Map<String, Object>> getProjectPayment(@RequestParam("projectId") String projectId) {
		if (StringUtil.isEmpty(projectId)) {
			return Result.error("-99", "标的ID不能为空");
		}

		try {

			com.ht.ussp.core.Result result = tdrepayRechargeService.remoteGetProjectPayment(projectId);

			Map<String, Object> paramMap = new HashMap<>();
			Map<String, Object> paramMap2 = new HashMap<>();
			Map<String, Object> resultMap = new HashMap<>();
			paramMap.put("projectId", projectId);

			TuandaiProjectInfo info = tuandaiProjectInfoService.selectById(projectId);

			paramMap2.put("userId", info.getTdUserId());
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", paramMap);
			com.ht.ussp.core.Result resultActual = eipRemote.queryProjectPayment(paramMap);
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", resultActual);

			LOG.info("提前结清平台费用查询/eip/td/repayment/queryRepaymentEarlier参数信息，{}", paramMap);
			com.ht.ussp.core.Result resultEarlier = eipRemote.queryRepaymentEarlier(paramMap);
			LOG.info("提前结清平台费用查询/eip/td/repayment/queryRepaymentEarlier返回信息，{}", resultEarlier);

			LOG.info("查询用户账户余额/eip/xiaodai/QueryUserAviMoney参数信息，{}", paramMap2);
			com.ht.ussp.core.Result resultUserAviMoney = eipRemote.queryUserAviMoney(paramMap2); // 查询用户余额
			LOG.info("查询用户账户余额/eip/xiaodai/QueryUserAviMoney返回信息，{}", resultUserAviMoney);

			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;

			TdProjectPaymentInfoResult tdProjectPaymentInfoResult = null;
			if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
				tdProjectPaymentInfoResult = JSONObject.parseObject(JSONObject.toJSONString(result.getData()),
						TdProjectPaymentInfoResult.class);
				List<TdRefundMonthInfoDTO> periodsList = tdProjectPaymentInfoResult.getPeriodsList();
				if (tdProjectPaymentInfoResult != null && CollectionUtils.isNotEmpty(periodsList)) {
					for (TdRefundMonthInfoDTO tdRefundMonthInfoDTO : periodsList) {
						Double amount = tdRefundMonthInfoDTO.getAmount() == null ? 0 : tdRefundMonthInfoDTO.getAmount();
						Double interestAmout = tdRefundMonthInfoDTO.getInterestAmout() == null ? 0
								: tdRefundMonthInfoDTO.getInterestAmout();
						Double overdueAmount = tdRefundMonthInfoDTO.getOverdueAmount() == null ? 0
								: tdRefundMonthInfoDTO.getOverdueAmount();
						Double advanceAmount = tdRefundMonthInfoDTO.getAdvanceAmount() == null ? 0
								: tdRefundMonthInfoDTO.getAdvanceAmount();
						tdRefundMonthInfoDTO
								.setTotal(BigDecimal.valueOf(amount + interestAmout + overdueAmount + advanceAmount)
										.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

						switch (tdRefundMonthInfoDTO.getStatus()) {
						case 1:
							tdRefundMonthInfoDTO.setStatusStr("已还款");
							break;
						case 2:
							tdRefundMonthInfoDTO.setStatusStr("逾期");
							break;
						case 3:
							tdRefundMonthInfoDTO.setStatusStr("待还款");
							break;

						default:
							break;
						}

					}
				}

				resultMap.put("periodsList", periodsList);

				if (resultActual != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(resultActual.getReturnCode())
						&& resultActual.getData() != null) {

					JSONObject parseObject = (JSONObject) JSONObject.toJSON(resultActual.getData());
					if (parseObject.get("projectPayments") != null) {
						tdProjectPaymentDTOs = JSONObject.parseArray(
								JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
					}
				}

				if (resultEarlier != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(resultEarlier.getReturnCode())
						&& resultEarlier.getData() != null) {
					resultMap.putAll(
							JSONObject.parseObject(JSONObject.toJSONString(resultEarlier.getData()), Map.class));
				}

				if (resultUserAviMoney != null
						&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(resultUserAviMoney.getReturnCode())
						&& resultUserAviMoney.getData() != null) {
					resultMap.put("aviMoney",
							JSONObject.parseObject(JSONObject.toJSONString(resultUserAviMoney.getData()), Map.class));
				}
			}

			if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
				for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
					switch (tdProjectPaymentDTO.getStatus()) {
					case 1:
						tdProjectPaymentDTO.setStatusStrActual("已结清");
						break;
					case 0:
						tdProjectPaymentDTO.setStatusStrActual("逾期");
						break;

					default:
						break;
					}
				}
			}

			resultMap.put("tdProjectPaymentDTOs", tdProjectPaymentDTOs);
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "根据业务ID获取标信息")
	@GetMapping("/getProjectInfoByBusinessId")
	@ResponseBody
	public Result<List<TuandaiProjectInfo>> getProjectInfoByBusinessId(@RequestParam("businessId") String businessId) {
		if (StringUtil.isEmpty(businessId)) {
			return Result.error("-99", "业务编号不能为空");
		}

		try {
			Wrapper<RepaymentProjPlan> wrapper = new EntityWrapper<RepaymentProjPlan>();
			wrapper.eq("original_business_id", businessId);
			wrapper.orderBy("query_full_success_date", false);
			List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanService.selectList(wrapper);

			String businessId2 = businessId;

			if (CollectionUtils.isNotEmpty(repaymentProjPlans)) {
				businessId2 = repaymentProjPlans.get(0).getBusinessId();
			}

			return Result.success(tuandaiProjectInfoService
					.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", businessId2)));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据标ID获取垫付记录")
	@GetMapping("/returnAdvanceShareProfit")
	@ResponseBody
	public Result<Map<String, Object>> returnAdvanceShareProfit(@RequestParam("projectId") String projectId) {
		if (StringUtil.isEmpty(projectId)) {
			return Result.error("-99", "标ID不能为空");
		}

		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", projectId);

			Map<String, Object> resultMap = new HashMap<>();
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", JSONObject.toJSONString(paramMap));
			com.ht.ussp.core.Result result = eipRemote.returnAdvanceShareProfit(paramMap);
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", JSONObject.toJSONString(result));

			LOG.info("标的还款信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", JSONObject.toJSONString(paramMap));
			com.ht.ussp.core.Result queryProjectPaymentResult = eipRemote.queryProjectPayment(paramMap);
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}",
					JSONObject.toJSONString(queryProjectPaymentResult));

			if (result != null && result.getData() != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
					&& queryProjectPaymentResult != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())) {
				TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = JSONObject
						.parseObject(JSONObject.toJSONString(result.getData()), TdReturnAdvanceShareProfitResult.class);
				List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
						.getReturnAdvanceShareProfits();

				if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
						switch (tdReturnAdvanceShareProfitDTO.getStatus()) {
						case 1:
							tdReturnAdvanceShareProfitDTO.setStatusStrActual("已结清");
							break;
						case 0:
							tdReturnAdvanceShareProfitDTO.setStatusStrActual("未结清");
							break;

						default:
							break;
						}
					}
				}

				// 标的还款信息
				List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;
				if (queryProjectPaymentResult.getData() != null) {

					JSONObject parseObject = (JSONObject) JSONObject.toJSON(queryProjectPaymentResult.getData());
					if (parseObject.get("projectPayments") != null) {
						tdProjectPaymentDTOs = JSONObject.parseArray(
								JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
					}
				}

				List<TdGuaranteePaymentVO> tdGuaranteePaymentVOs = new LinkedList<>();
				if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
					for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
						TdGuaranteePaymentDTO guaranteePayment = tdProjectPaymentDTO.getGuaranteePayment();
						if (guaranteePayment != null) {
							TdGuaranteePaymentVO vo = BeanUtils.deepCopy(guaranteePayment, TdGuaranteePaymentVO.class);
							if (vo != null) {
								vo.setPeriod(tdProjectPaymentDTO.getPeriod());
								switch (tdProjectPaymentDTO.getStatus()) {
								case 1:
									vo.setStatus("已结清");
									break;
								case 0:
									vo.setStatus("逾期");
									break;

								default:
									break;
								}

								vo.setAddDate(tdProjectPaymentDTO.getAddDate());
								BigDecimal total = BigDecimal.ZERO;
								total = total.add(vo.getPrincipalAndInterest() == null ? BigDecimal.ZERO
										: vo.getPrincipalAndInterest());
								total = total
										.add(vo.getPenaltyAmount() == null ? BigDecimal.ZERO : vo.getPenaltyAmount());
								total = total
										.add(vo.getTuandaiAmount() == null ? BigDecimal.ZERO : vo.getTuandaiAmount());
								total = total.add(vo.getOrgAmount() == null ? BigDecimal.ZERO : vo.getOrgAmount());
								total = total.add(
										vo.getGuaranteeAmount() == null ? BigDecimal.ZERO : vo.getGuaranteeAmount());
								total = total.add(vo.getArbitrationAmount() == null ? BigDecimal.ZERO
										: vo.getArbitrationAmount());
								total = total
										.add(vo.getAgencyAmount() == null ? BigDecimal.ZERO : vo.getAgencyAmount());

								vo.setTotal(total.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
								tdGuaranteePaymentVOs.add(vo);
							}
						}
					}
				}

				resultMap.put("returnAdvanceShareProfits", returnAdvanceShareProfits);
				resultMap.put("tdGuaranteePaymentVOs", tdGuaranteePaymentVOs);

				return Result.success(resultMap);
			} else {
				return Result.error("-99", "查询平台还垫付信息接口失败");
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "查询资金分发记录")
	@GetMapping("/queryDistributeFundRecord")
	@ResponseBody
	public Result<List<DistributeFundRecordVO>> queryDistributeFundRecord(@RequestParam("projectId") String projectId) {
		try {
			if (StringUtil.isEmpty(projectId)) {
				return Result.error("-99", "标ID不能为空");
			}

			return Result.success(tdrepayRechargeService.queryDistributeFundRecord(projectId));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询资金分发处理状态")
	@GetMapping("/queryDistributeFund")
	@ResponseBody
	public Result queryDistributeFund() {
		try {
			agencyRechargeLogService.queryDistributeFund();
			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "获取标还款计划")
	@GetMapping("/queryRepaymentProjPlan")
	@ResponseBody
	public Result<RepaymentProjPlan> queryRepaymentProjPlan(@RequestParam("projectId") String projectId) {
		try {
			if (StringUtil.isEmpty(projectId)) {
				return Result.error("-99", "标ID不能为空");
			}

			List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanService
					.selectList(new EntityWrapper<RepaymentProjPlan>().eq("project_id", projectId));
			if (CollectionUtils.isEmpty(repaymentProjPlans)) {
				return Result.success();
			}
			return Result.success(repaymentProjPlans.get(0));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "资产端对团贷网通用合规化还款流程")
	@PostMapping("/repayComplianceWithRequirements")
	@ResponseBody
	public Result<RepaymentProjPlan> repayComplianceWithRequirements() {
		try {
			tdrepayRechargeService.repayComplianceWithRequirements();
			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "查看充值记录")
	@PostMapping("/queryRechargeRecord")
	@ResponseBody
	public PageResult<List<AgencyRechargeLogVO>> queryRechargeRecord(@RequestBody RechargeRecordReq req) {
		try {

			if (req != null && req.getCreateTimeEnd() != null) {
				req.setCreateTimeEnd(DateUtil.addDay2Date(1, req.getCreateTimeEnd()));
			}
			int count = tdrepayRechargeService.countRechargeRecord(req);
			if (count == 0) {
				return PageResult.success(count);
			}

			List<AgencyRechargeLogVO> agencyRechargeLogVOs = new ArrayList<>();

			List<AgencyRechargeLog> agencyRechargeLogs = tdrepayRechargeService.queryRechargeRecord(req);
			if (CollectionUtils.isNotEmpty(agencyRechargeLogs)) {
				for (AgencyRechargeLog agencyRechargeLog : agencyRechargeLogs) {
					AgencyRechargeLogVO vo = BeanUtils.deepCopy(agencyRechargeLog, AgencyRechargeLogVO.class);
					if (vo != null) {
						vo.setBankName(BankEnum.getName(agencyRechargeLog.getBankCode()));
						String bankAccount = agencyRechargeLog.getBankAccount();
						vo.setSubBankAccount(StringUtil.isEmpty(bankAccount) ? bankAccount
								: bankAccount.substring(bankAccount.length() - 4, bankAccount.length()));
						switch (agencyRechargeLog.getHandleStatus()) {
						case "1":
							vo.setHandleStatusStr("处理中");
							break;
						case "2":
							vo.setHandleStatusStr("充值成功");
							break;
						case "3":
							vo.setHandleStatusStr("充值失败");
							break;

						default:
							break;
						}
						vo.setCreateUsername(loginUserInfoHelper.getLoginInfo().getUserName());
						agencyRechargeLogVOs.add(vo);
					}
				}
				return PageResult.success(agencyRechargeLogVOs, count);
			}
			return PageResult.success(0);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return PageResult.error(-99, e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询代充值账户余额")
	@GetMapping("/queryRechargeAccountBalance")
	@ResponseBody
	public Result<List<Map<String, Object>>> queryRechargeAccountBalance() {
		try {

			Map<String, Object> paramMap = new HashMap<>();

			List<Map<String, Object>> resultList = new ArrayList<>();

			Set<String> nameSet = new HashSet<>();
			BusinessTypeEnum[] businessTypeEnums = BusinessTypeEnum.values();
			for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {
				nameSet.add(businessTypeEnum.rechargeAccountName());
			}

			int num = 1;

			for (String rechargeAccountType : nameSet) {

				Map<String, Object> resultMap = new HashMap<>();

				SysParameter sysParameter = tdrepayRechargeService.queryRechargeAccountSysParams(rechargeAccountType);
				if (sysParameter == null) {
					continue;
				}

				String userId = sysParameter.getParamValue();

				paramMap.put("userId", userId);

				LOG.info("查询代充值账户余额/eip/td/queryUserAviMoneyForNet参数信息，{}", paramMap);
				com.ht.ussp.core.Result result = eipRemote.queryUserAviMoneyForNet(paramMap);
				LOG.info("查询代充值账户余额/eip/td/queryUserAviMoneyForNet返回信息，{}", result);

				resultMap.put("rechargeAccountType", rechargeAccountType);
				resultMap.put("num", num++);

				if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
						&& result.getData() != null) {
					JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
					resultMap.put("balance", jsonObject.get("AviMoney"));
				} else {
					resultMap.put("balance", "查询" + rechargeAccountType + "账户余额失败");
				}

				resultList.add(resultMap);
			}

			return Result.success(resultList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "查询资金分发记录状态")
	@PostMapping("/queryTdrepayRechargeRecord")
	@ResponseBody
	public Result<List<Map<String, Object>>> queryTdrepayRechargeRecord(@RequestBody Map<String, Object> paramMap) {

		if (paramMap == null || StringUtil.isEmpty((String) paramMap.get("projectId"))
				|| StringUtil.isEmpty((String) paramMap.get("confirmLogId"))) {
			return Result.error("-99", "标的ID或者实还流水ID不能为空");
		}

		try {
			return Result.success(tdrepayRechargeLogService.queryTdrepayRechargeRecord(
					(String) paramMap.get("projectId"), (String) paramMap.get("confirmLogId")));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", "系统异常");
		}

	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "撤销资金分发")
	@PostMapping("/revokeTdrepayRecharge")
	@ResponseBody
	public Result revokeTdrepayRecharge(@RequestBody Map<String, Object> paramMap) {

		if (paramMap == null || StringUtil.isEmpty((String) paramMap.get("projectId"))
				|| StringUtil.isEmpty((String) paramMap.get("confirmLogId"))) {
			return Result.error("-99", "标的ID或者实还流水ID不能为空");
		}

		try {
			Integer[] processStatus = { 0, 3 };

			List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService.selectList(
					new EntityWrapper<TdrepayRechargeLog>().eq("project_id", (String) paramMap.get("projectId"))
							.eq("confirm_log_id", (String) paramMap.get("confirmLogId"))
							.in("process_status", processStatus).eq("is_valid", 1));

			if (!CollectionUtils.isEmpty(tdrepayRechargeLogs)) {

				tdrepayRechargeService.revokeTdrepayRecharge(tdrepayRechargeLogs);

				return Result.success();
			} else {
				return Result.error("-99", "没有找到对应的数据，撤销资金分发失败");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", "系统异常");
		}

	}

	@ApiOperation(value = "查询所有业务类型")
	@GetMapping("/queryBusinessTypes")
	@ResponseBody
	public Result<List<Map<String, Object>>> queryBusinessTypes() {
		try {
			List<Map<String, Object>> resultList = new LinkedList<>();
			BusinessTypeEnum[] businessTypeEnums = BusinessTypeEnum.values();

			for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {
				if (businessTypeEnum.value() == 25) {
					continue;
				}
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("value", businessTypeEnum.value());
				resultMap.put("name", businessTypeEnum.getName());
				resultList.add(resultMap);
			}

			return Result.success(resultList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}

	@ApiOperation(value = "处理合规化还款处理中的数据")
	@GetMapping("/handleRunningData")
	@ResponseBody
	public Result handleRunningData() {
		try {

			tdrepayRechargeService.handleRunningData();

			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "处理处理资金分发失败的数据")
	@GetMapping("/handleRechargeFailedData")
	@ResponseBody
	public Result<List<Map<String, Object>>> handleRechargeFailedData() {
		try {

			List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
					.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("process_status", 3).eq("is_valid", 1));
			List<Map<String, Object>> lst = new LinkedList<>();
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				Map<String, Object> paramMap = new HashMap<>();
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {

					paramMap.put("batchId", tdrepayRechargeLog.getBatchId());
					paramMap.put("requestNo", tdrepayRechargeLog.getRequestNo());
					paramMap.put("oidPartner", tdrepayRechargeLog.getOidPartner());
					paramMap.put("userId", tdrepayRechargeLog.getTdUserId());

					com.ht.ussp.core.Result queryDistributeFund = eipRemote.queryDistributeFund(paramMap);
					com.ht.ussp.core.Result queryUserAviMoney = eipRemote.queryUserAviMoney(paramMap);
					Map<String, Object> resultMap = new HashMap<>();
					resultMap.put("logId", tdrepayRechargeLog.getLogId());
					resultMap.put("projectId", tdrepayRechargeLog.getProjectId());
					resultMap.put("customerName", tdrepayRechargeLog.getCustomerName());
					resultMap.put("businessId", tdrepayRechargeLog.getOrigBusinessId());
					resultMap.put("paramMap", JSONObject.toJSONString(paramMap));
					if (queryDistributeFund != null) {
						resultMap.put("queryDistributeFund", queryDistributeFund.getData());
					}
					if (queryUserAviMoney != null) {
						resultMap.put("queryUserAviMoney", queryUserAviMoney.getData());
					}
					lst.add(resultMap);
				}
			}

			return Result.success(lst);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}


	@ApiOperation(value = "指定标的指定期数还垫付")
	@GetMapping("/alsoToPayOneProject")
	@ResponseBody
	public Result<RepaymentProjPlan> alsoToPayOneProject(@RequestParam("alsoToPayProjectId") String alsoToPayProjectId,
														 @RequestParam("alsoToPayAfterId") String alsoToPayAfterId) {
		try {
			if (StringUtil.isEmpty(alsoToPayProjectId) || StringUtil.isEmpty(alsoToPayAfterId)) {
				return Result.error("-99", "标ID和afterId不能为空");
			}

			Integer[] arrStatus = { 0, 3 };
			List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
					.selectList(new EntityWrapper<TdrepayRechargeLog>()
							.in("status", arrStatus)
							.eq("is_valid", 1)
							.eq("project_id",alsoToPayProjectId)
							.eq("after_id",alsoToPayAfterId));

			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {

				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					oneLogAdvanceShareProfit(tdrepayRechargeLog);
				}
			}else{

				return Result.error("-99", "此标没有可还垫付的资金分发记录");
			}

			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("-99", e.getMessage());
		}
	}



	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "偿还垫付功能")
	@GetMapping("/advanceShareProfit")
	@ResponseBody
	public Result advanceShareProfit() {
		// 1、查出所有偿还垫付失败和未偿还垫付的数据
		Integer[] arrStatus = { 0, 3 };
		List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
				.selectList(new EntityWrapper<TdrepayRechargeLog>().in("status", arrStatus).eq("is_valid", 1));

		
		if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
			
			
			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				try {
					oneLogAdvanceShareProfit(tdrepayRechargeLog);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		return Result.success();
	}


	/**
	 * 针对指定资金分发记录，执行还垫付
	 * @param tdrepayRechargeLog
	 * @return
	 */
	public Result  oneLogAdvanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog){
		try {
			// 3、根据 project_id 查询担保公司垫付记录、还垫付记录

			String projectId = tdrepayRechargeLog.getProjectId();
			Map<String, com.ht.ussp.core.Result> resultMap = tdrepayRechargeService
					.getAdvanceShareProfitAndProjectPayment(projectId);
			com.ht.ussp.core.Result queryProjectPaymentResult = resultMap.get("queryProjectPaymentResult");
			com.ht.ussp.core.Result advanceShareProfitResult = resultMap.get("advanceShareProfitResult");

			Map<String, Object> paramMap1 = new HashMap<>();
			paramMap1.put("projectId", projectId);
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", paramMap1);
			com.ht.ussp.core.Result resultActual = eipRemote.queryProjectPayment(paramMap1);
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", resultActual);

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
			BigDecimal principalAndInterest = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount = BigDecimal.ZERO; // 平台服务费
			BigDecimal orgAmount = BigDecimal.ZERO; // 资产端服务费
			BigDecimal guaranteeAmount = BigDecimal.ZERO; // 担保公司服务费
			BigDecimal arbitrationAmount = BigDecimal.ZERO; // 仲裁服务费
			BigDecimal penaltyAmount = BigDecimal.ZERO; // 滞纳金

			// 还垫付信息
			BigDecimal principalAndInterest2 = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount2 = BigDecimal.ZERO; // 平台服务费
			BigDecimal orgAmount2 = BigDecimal.ZERO; // 资产端服务费
			BigDecimal guaranteeAmount2 = BigDecimal.ZERO; // 担保公司服务费
			BigDecimal arbitrationAmount2 = BigDecimal.ZERO; // 仲裁服务费

			// 剩余未还担保公司垫付金额
			BigDecimal principalAndInterest3 = null; // 本金利息
			BigDecimal tuandaiAmount3 = null; // 平台服务费
			BigDecimal orgAmount3 = null; // 资产端服务费
			BigDecimal guaranteeAmount3 = null; // 担保公司服务费
			BigDecimal arbitrationAmount3 = null; // 仲裁服务费
			BigDecimal totalAmount = BigDecimal.ZERO; // 费用合计

			Integer period = tdrepayRechargeLog.getPeriod();
			if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
				for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
					if (tdProjectPaymentDTO.getPeriod() == period.intValue()) {
						TdGuaranteePaymentDTO guaranteePayment = tdProjectPaymentDTO.getGuaranteePayment();
						if (guaranteePayment != null) {
							principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? principalAndInterest
									: guaranteePayment.getPrincipalAndInterest();
							penaltyAmount = guaranteePayment.getPenaltyAmount() == null ? penaltyAmount
									: guaranteePayment.getPenaltyAmount();
							// 若担保公司垫付了滞纳金，则需要将滞纳金计算到本息上
							if (BigDecimal.ZERO.compareTo(penaltyAmount) <= 0) {
								principalAndInterest = principalAndInterest.add(penaltyAmount);
							}
							tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? tuandaiAmount
									: guaranteePayment.getTuandaiAmount();
							orgAmount = guaranteePayment.getOrgAmount() == null ? orgAmount : guaranteePayment.getOrgAmount();
							guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? guaranteeAmount
									: guaranteePayment.getGuaranteeAmount();
							arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? arbitrationAmount
									: guaranteePayment.getArbitrationAmount();
						}
						break;
					}
				}
			}

			if (returnAdvanceShareProfitResult != null
					&& CollectionUtils.isNotEmpty(returnAdvanceShareProfitResult.getReturnAdvanceShareProfits())) {
				List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
						.getReturnAdvanceShareProfits();

				if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
						if (tdReturnAdvanceShareProfitDTO.getPeriod() == period.intValue()) {
							principalAndInterest2 = tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest() == null
									? principalAndInterest2
									: tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest();
							tuandaiAmount2 = tdReturnAdvanceShareProfitDTO.getTuandaiAmount() == null ? tuandaiAmount2
									: tdReturnAdvanceShareProfitDTO.getTuandaiAmount();
							orgAmount2 = tdReturnAdvanceShareProfitDTO.getOrgAmount() == null ? orgAmount2
									: tdReturnAdvanceShareProfitDTO.getOrgAmount();
							guaranteeAmount2 = tdReturnAdvanceShareProfitDTO.getGuaranteeAmount() == null
									? guaranteeAmount2
									: tdReturnAdvanceShareProfitDTO.getGuaranteeAmount();
							arbitrationAmount2 = tdReturnAdvanceShareProfitDTO.getArbitrationAmount() == null
									? arbitrationAmount2
									: tdReturnAdvanceShareProfitDTO.getArbitrationAmount();
							break;
						}
					}
				}
			}

			// 5、计算剩余应偿还担保公司垫付记录
			principalAndInterest3 = principalAndInterest.subtract(principalAndInterest2);
			totalAmount = totalAmount.add(principalAndInterest3);
			tuandaiAmount3 = tuandaiAmount.subtract(tuandaiAmount2);
			totalAmount = totalAmount.add(tuandaiAmount3);
			orgAmount3 = orgAmount.subtract(orgAmount2);
			totalAmount = totalAmount.add(orgAmount3);
			guaranteeAmount3 = guaranteeAmount.subtract(guaranteeAmount2);
			totalAmount = totalAmount.add(guaranteeAmount3);
			arbitrationAmount3 = arbitrationAmount.subtract(arbitrationAmount2);
			totalAmount = totalAmount.add(arbitrationAmount3);

			if (totalAmount.compareTo(BigDecimal.ZERO) < 1) {
				
				throw new ServiceRuntimeException(
						"标的号：" + projectId + "，在平台期数：" + period + "没有未还垫付记录。totalAmount = " + totalAmount);
				
			}

			/*
			 *  6、根据 tdUserId 查询存管账户余额, 比较应还垫付总额与客户存管账户余额记录
			 *   a.若是主借标，则只查询主借款人账户余额；
			 *   b.若是共借标：
			 *   	若共借人存管账户余额若大于或等于应还垫付总额，则传结清状态给平台，调用偿还垫付接口
			 *   	若共借人存管账户余额若小于应还垫付总额，则还需查询主借人存管账户余额：
			 *   		若主、共借人存管账户余额之和大于或等于应还垫付总额，则传结清状态给平台，调用偿还垫付接口
			 *   		若主、共借人存管账户余额之和小于应还垫付总额，则传结非清状态给平台，调用偿还垫付接口
			 */

			// 查询客户存管账户余额
			BigDecimal aviMoney = queryUserAviMoney(tdrepayRechargeLog.getTdUserId());

			Set<Integer> businessTypes = new HashSet<>();
			businessTypes.add(28);	// 商贸贷共借
			businessTypes.add(29);	// 业主贷共借
			businessTypes.add(31);	// 车贷共借
			businessTypes.add(32);	// 房贷共借
			businessTypes.add(33);	// 一点车贷共借

			// 若是共借标，则需要查找主借标的 tdUserId
			if (businessTypes.contains(tdrepayRechargeLog.getBusinessType())) {
				if (aviMoney.compareTo(totalAmount) < 0) {
					TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService
							.selectOne(new EntityWrapper<TuandaiProjectInfo>()
									.eq("business_id", tdrepayRechargeLog.getOrigBusinessId())
									.where("project_id = master_issue_id"));

					if (tuandaiProjectInfo != null) {
						aviMoney = aviMoney.add(queryUserAviMoney(tuandaiProjectInfo.getTdUserId()));
					}

					if (aviMoney.compareTo(totalAmount) >= 0) {
						advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3,
								orgAmount3, guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
					}else {
						totalAmount = aviMoney;
						if (aviMoney.compareTo(principalAndInterest3) > 0) {
							aviMoney = aviMoney.subtract(principalAndInterest3);
							if (aviMoney.compareTo(tuandaiAmount3) > 0) {
								aviMoney = aviMoney.subtract(tuandaiAmount3);
								if (aviMoney.compareTo(guaranteeAmount3) > 0) {
									orgAmount3 = aviMoney.subtract(guaranteeAmount3);
								}else {
									guaranteeAmount3 = aviMoney;
									orgAmount3 = BigDecimal.ZERO;
								}
							}else {
								tuandaiAmount3 = aviMoney;
								guaranteeAmount3 = BigDecimal.ZERO;
								orgAmount3 = BigDecimal.ZERO;
							}
						}else {
							principalAndInterest3 = aviMoney;
							tuandaiAmount3 = BigDecimal.ZERO;
							guaranteeAmount3 = BigDecimal.ZERO;
							orgAmount3 = BigDecimal.ZERO;
						}
						advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3,
								orgAmount3, guaranteeAmount3, arbitrationAmount3, totalAmount, period, 0);
					}
				}else {
					advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3,
							orgAmount3, guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
				}
			}else {
				if (aviMoney.compareTo(totalAmount) >= 0) {
					advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3,
							orgAmount3, guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
				}else {
					totalAmount = aviMoney;
					if (aviMoney.compareTo(principalAndInterest3) > 0) {
						aviMoney = aviMoney.subtract(principalAndInterest3);
						if (aviMoney.compareTo(tuandaiAmount3) > 0) {
							aviMoney = aviMoney.subtract(tuandaiAmount3);
							if (aviMoney.compareTo(guaranteeAmount3) > 0) {
								orgAmount3 = aviMoney.subtract(guaranteeAmount3);
							}else {
								guaranteeAmount3 = aviMoney;
								orgAmount3 = BigDecimal.ZERO;
							}
						}else {
							tuandaiAmount3 = aviMoney;
							guaranteeAmount3 = BigDecimal.ZERO;
							orgAmount3 = BigDecimal.ZERO;
						}
					}else {
						principalAndInterest3 = aviMoney;
						tuandaiAmount3 = BigDecimal.ZERO;
						guaranteeAmount3 = BigDecimal.ZERO;
						orgAmount3 = BigDecimal.ZERO;
					}
					advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3,
							orgAmount3, guaranteeAmount3, arbitrationAmount3, totalAmount, period, 0);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
		return  Result.success();
	}

	/**
	 * 偿还垫付
	 * @param tdrepayRechargeLog
	 * @param projectId
	 * @param principalAndInterest3
	 * @param tuandaiAmount3
	 * @param orgAmount3
	 * @param guaranteeAmount3
	 * @param arbitrationAmount3
	 * @param totalAmount
	 * @param period
	 */
	private void advanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog, String projectId,
			BigDecimal principalAndInterest3, BigDecimal tuandaiAmount3, BigDecimal orgAmount3,
			BigDecimal guaranteeAmount3, BigDecimal arbitrationAmount3, BigDecimal totalAmount, Integer period,
			Integer status) {
		// 参数DTO
		TdAdvanceShareProfitDTO paramDTO = new TdAdvanceShareProfitDTO();
		paramDTO.setProjectId(projectId);
		paramDTO.setPeriod(period);
		paramDTO.setTotalAmount(totalAmount);
		paramDTO.setPrincipalAndInterest(principalAndInterest3);
		paramDTO.setTuandaiAmount(tuandaiAmount3);
		paramDTO.setOrgAmount(orgAmount3);
		paramDTO.setGuaranteeAmount(guaranteeAmount3);
		paramDTO.setArbitrationAmount(arbitrationAmount3);
		paramDTO.setOrgType(BusinessTypeEnum.getOrgTypeByValue(tdrepayRechargeLog.getBusinessType()));
		paramDTO.setStatus(status);

		// 第三方接口调用日志
		IssueSendOutsideLog issueSendOutsideLog = new IssueSendOutsideLog();
		issueSendOutsideLog.setCreateTime(new Date());
		issueSendOutsideLog.setCreateUserId(loginUserInfoHelper.getUserId());
		issueSendOutsideLog.setSendJson(JSONObject.toJSONString(paramDTO));
		issueSendOutsideLog.setInterfacecode(Constant.INTERFACE_CODE_ADVANCE_SHARE_PROFIT);
		issueSendOutsideLog.setInterfacename(Constant.INTERFACE_NAME_ADVANCE_SHARE_PROFIT);
		issueSendOutsideLog.setSystem(Constant.SYSTEM_CODE_EIP);
		issueSendOutsideLog.setSendKey(projectId);

		// 调用偿还垫付接口
		LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit参数信息，{}", paramDTO);
		com.ht.ussp.core.Result result = eipRemote.advanceShareProfit(paramDTO);
		LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit返回信息，{}", result);

		issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
		
		if (result != null) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", projectId);
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
			com.ht.ussp.core.Result advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);

			int logStatus = 3;
			
			if (advanceShareProfitResult != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
					&& advanceShareProfitResult.getData() != null) {
					JSONObject parseObject = (JSONObject) JSONObject.toJSON(advanceShareProfitResult.getData());
					if (parseObject.get("returnAdvanceShareProfits") != null) {
						List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = JSONObject.parseArray(
								JSONObject.toJSONString(parseObject.get("returnAdvanceShareProfits")),
								TdReturnAdvanceShareProfitDTO.class);
						if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
							for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
								if (tdReturnAdvanceShareProfitDTO.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()
										&& tdReturnAdvanceShareProfitDTO.getStatus() == 1) {
									logStatus = 2;
								}
							}
						}
					}
			}else {
				logStatus = 4;
			}
			tdrepayRechargeLog.setStatus(logStatus);
			tdrepayRechargeLog.setRemark(result.getCodeDesc());
		} else {
			tdrepayRechargeLog.setStatus(3);
			tdrepayRechargeLog.setRemark("eip偿还垫付接口调用异常");
		}
		tdrepayRechargeLog.setUpdateTime(new Date());
		tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
		tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
		issueSendOutsideLogService.insert(issueSendOutsideLog);
	}
	
	/**
	 * 查询客户存管账户余额
	 * @param tdUserId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private BigDecimal queryUserAviMoney(String tdUserId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", tdUserId);
		LOG.info("查询代充值账户余额/eip/td/queryUserAviMoneyForNet参数信息，{}", paramMap);
		com.ht.ussp.core.Result result = eipRemote.queryUserAviMoneyForNet(paramMap);
		LOG.info("查询代充值账户余额/eip/td/queryUserAviMoneyForNet返回信息，{}", result);
		
		if (result != null
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
				&& result.getData() != null) {
			Map map = JSONObject.parseObject(JSONObject.toJSONString(result.getData()),
					Map.class);
			if (map != null) {
				return map.get("AviMoney") == null ? BigDecimal.ZERO : (BigDecimal) map.get("AviMoney");
			}
		}
		return BigDecimal.ZERO;
	}

	public static void main(String[] args) throws Exception {
		FileInputStream inStream = new FileInputStream(
				new File("C:\\Users\\Administrator\\Desktop\\ALMS_source_data.json"));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int length = -1;

		while ((length = inStream.read(buffer)) != -1)

		{

			bos.write(buffer, 0, length);

		}

		bos.close();

		inStream.close();
		Result result = JSONObject.parseObject(bos.toString(), Result.class);
		List<Map> list = JSONObject.parseArray(JSONObject.toJSONString(result.getData()), Map.class);
		List<Map> lst1 = new ArrayList<>();
		List<Map> lst2 = new ArrayList<>();
		List<String> lst3 = new ArrayList<>();
		for (Map map : list) {
			Map m = JSONObject.parseObject(JSONObject.toJSONString(map.get("queryUserAviMoney")), Map.class);
			if (Double.valueOf((String) m.get("aviMoney")) == 0) {
				lst1.add(map);
				lst3.add((String) map.get("businessId"));
			} else {
				lst2.add(map);
			}
		}
		for (String string : lst3) {

			System.out.println(string);
		}
	}

}
