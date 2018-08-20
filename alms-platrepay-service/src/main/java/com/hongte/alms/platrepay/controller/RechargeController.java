package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.RechargeModalDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.vo.RechargeModalVO;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

//@CrossOrigin
@RestController
@RequestMapping("/recharge")
public class RechargeController {
	private static final Logger LOG = LoggerFactory.getLogger(RechargeController.class);

	private static final String INVALID_PARAM_CODE = "-9999";
	private static final String INVALID_PARAM_DESC = " Parameters cannot be null";

	@Autowired
	private EipRemote eipRemote;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Autowired
	@Qualifier("TdrepayRechargeService")
	private TdrepayRechargeService tdrepayRechargeService;

	@Autowired
	@Qualifier("DepartmentBankService")
	private DepartmentBankService departmentBankService;

	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	private TuandaiProjectInfoService tuandaiProjectInfoService;

	@Autowired
	@Qualifier("BasicBusinessService")
	private BasicBusinessService basicBusinessService;

	@ApiOperation(value = "获取所有的线下还款账户")
	@GetMapping("/listAllDepartmentBank")
	@ResponseBody
	public Result<List<DepartmentBank>> listAllDepartmentBank() {
		try {
			return Result.success(departmentBankService.listDepartmentBank());
		} catch (Exception e) {
			LOG.error("获取所有的线下还款账户失败", e);
			return Result.error("500", "获取所有的线下还款账户失败！");
		}
	}

	@ApiOperation(value = "获取所有代充值账户类型")
	@GetMapping("/queryRechargeAccountTypes")
	@ResponseBody
	public Result<List<Map<String, Object>>> queryRechargeAccountTypes() {
		try {
			List<Map<String, Object>> resultList = new LinkedList<>();
			Set<Integer> rechargeAccountIdSet = new HashSet<>();
			BusinessTypeEnum[] businessTypeEnums = BusinessTypeEnum.values();
			for (BusinessTypeEnum businessTypeEnum : businessTypeEnums) {
				if (businessTypeEnum.rechargeAccountId() == -1) {
					continue;
				}
				rechargeAccountIdSet.add(businessTypeEnum.rechargeAccountId());
			}

			for (Integer rechargeAccountId : rechargeAccountIdSet) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("rechargeAccountId", rechargeAccountId);
				resultMap.put("rechargeAccountName",
						BusinessTypeEnum.getRechargeAccountNameByRechargeAccountId(rechargeAccountId));
				resultList.add(resultMap);
			}
			return Result.success(resultList);
		} catch (Exception e) {
			LOG.error("获取所有的线下还款账户失败", e);
			return Result.error("500", "获取所有的线下还款账户失败！");
		}
	}

	@ApiOperation(value = "根据业务类型ID获取所有代充值账户类型")
	@GetMapping("/queryRechargeAccountTypeByBusinessType")
	@ResponseBody
	public Result<String> queryRechargeAccountTypeByBusinessType(@RequestParam("businessType") Integer businessType) {
		try {
			LOG.info("queryRechargeAccountTypeByBusinessType业务类型：{}", businessType);
			return Result.success(BusinessTypeEnum.getRechargeAccountName(businessType));
		} catch (Exception e) {
			LOG.error("获取所有的线下还款账户失败", e);
			return Result.error("500", "获取所有的线下还款账户失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询代充值账户余额")
	@GetMapping("/queryUserAviMoney")
	@ResponseBody
	public Result queryUserAviMoney(@RequestParam("rechargeAccountType") String rechargeAccountType) {

		Map<String, Object> paramMap = new HashMap<>();

		SysParameter sysParameter = tdrepayRechargeService.queryRechargeAccountSysParams(rechargeAccountType);
		if (sysParameter == null) {
			return Result.error("没有找到代充值账户的参数配置");
		}

		String userId = sysParameter.getParamValue();

		paramMap.put("userId", userId);

		com.ht.ussp.core.Result result = null;
		LOG.info("查询代充值账户余额/eip/xiaodai/queryUserAviMoneyForNet参数信息，{}", JSONObject.toJSONString(paramMap));
		try {
			result = eipRemote.queryUserAviMoneyForNet(paramMap);
			LOG.info("查询代充值账户余额/eip/xiaodai/queryUserAviMoneyForNet接口返回信息：{}", JSONObject.toJSONString(result));
		} catch (Exception e) {
			return Result.error("500", "调用外联平台查询代充值账户余额接口失败！");
		}

		return Result.success(result);
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "提交充值数据")
	@PostMapping("/commitRechargeData")
	@ResponseBody
	public Result commitRechargeData(@RequestBody RechargeModalVO vo) {

		if (vo == null) {
			return Result.error("不能空数据！");
		}

		vo.setClientIp(CommonUtil.getClientIp());
		vo.setChargeType("3");

		String rechargeAccountType = vo.getRechargeAccountType();

		SysParameter sysParameter = tdrepayRechargeService.queryRechargeAccountSysParams(rechargeAccountType);
		if (sysParameter == null) {
			return Result.error("没有找到代充值账户的参数配置");
		}

		String rechargeUserId = sysParameter.getParamValue();
		String oIdPartner = sysParameter.getParamValue2();

		vo.setRechargeUserId(rechargeUserId);
		vo.setOperator(loginUserInfoHelper.getUserId());

		Result reuslt = rechargeAmount(vo, oIdPartner);
		String resultJson = JSONObject.toJSONString(reuslt);
		LOG.info("平台代充值接口返回结果：{}", resultJson);
		return reuslt;

	}

	/**
	 * 充值金额
	 * 
	 * @param vo
	 * @param clientIp
	 * @param rechargeUserId
	 * @param oIdPartner
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Result rechargeAmount(RechargeModalVO vo, String oIdPartner) {
		String cmOrderNo = UUID.randomUUID().toString();

		RechargeModalDTO dto = new RechargeModalDTO();
		dto.setAmount(BigDecimal.valueOf(vo.getRechargeAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		// 若转账类型为1 对公，则在银行编码后 + "2B"
		dto.setBankCode("1".equals(vo.getTransferType()) ? vo.getBankCode() + "2B" : vo.getBankCode());
		dto.setClientIp(vo.getClientIp());
		dto.setChargeType(vo.getChargeType()); // 1：网关、2：快捷、3：代充值
		dto.setCmOrderNo(cmOrderNo);
		dto.setoIdPartner(oIdPartner);
		dto.setRechargeUserId(vo.getRechargeUserId());

		AgencyRechargeLog agencyRechargeLog = handleAgencyRechargeLog(vo, cmOrderNo, dto, oIdPartner);

		agencyRechargeLogService.insert(agencyRechargeLog);

		com.ht.ussp.core.Result result = null;
		LOG.info("代充值接口/eip/td/assetside/agencyRecharge参数信息，{}", JSONObject.toJSONString(dto));
		try {

			result = eipRemote.agencyRecharge(dto);
			LOG.info("代充值接口/eip/td/assetside/agencyRecharge返回信息，{}", JSONObject.toJSONString(result));

			if (result == null) {
				return Result.error("500", "接口调用失败！");
			}
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("500", "提交充值数据失败！");
		}

		try {
			if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				agencyRechargeLog.setUpdateTime(new Date());
				agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());

				agencyRechargeLogService.update(agencyRechargeLog,
						new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
				return Result.success(result);
			} else {
				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
				return Result.error("500", result.getCodeDesc());
			}
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("500", "提交充值数据失败！");
		}
	}

	/**
	 * 快捷充值金额
	 * 
	 * @param vo
	 * @param clientIp
	 * @param rechargeUserId
	 * @param oIdPartner
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Result quickRechargeAmount(RechargeModalVO vo, String oIdPartner) {
		String cmOrderNo = UUID.randomUUID().toString();

		RechargeModalDTO dto = new RechargeModalDTO();
		dto.setAmount(BigDecimal.valueOf(vo.getRechargeAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		// 若转账类型为1 对公，则在银行编码后 + "2B"
		dto.setBankCode("1".equals(vo.getTransferType()) ? vo.getBankCode() + "2B" : vo.getBankCode());
		dto.setClientIp(vo.getClientIp());
		dto.setChargeType(vo.getChargeType()); // 1：网关、2：快捷、3：代充值
		dto.setCmOrderNo(cmOrderNo);
		dto.setoIdPartner(oIdPartner);
		dto.setRechargeUserId(vo.getRechargeUserId());

		AgencyRechargeLog agencyRechargeLog = handleAgencyRechargeLog(vo, cmOrderNo, dto, oIdPartner);

		agencyRechargeLogService.insert(agencyRechargeLog);

		com.ht.ussp.core.Result result = null;
		LOG.info("代充值接口/eip/td/assetside/agencyRecharge参数信息，{}", JSONObject.toJSONString(dto));
		try {

			result = eipRemote.agencyRecharge(dto);
			LOG.info("代充值接口/eip/td/assetside/agencyRecharge返回信息，{}", JSONObject.toJSONString(result));

			if (result == null) {
				return Result.error("调用EPI平台接口失败！");
			}
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("系统异常！");
		}

		try {
			if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				agencyRechargeLog.setUpdateTime(new Date());
				agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());

				agencyRechargeLogService.update(agencyRechargeLog,
						new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
			} else {
				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			}
			return Result.build(result.getReturnCode(), result.getMsg(), result.getData());
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("系统异常！");
		}
	}

	private void handleUpdateLogFail(String cmOrderNo, AgencyRechargeLog agencyRechargeLog) {
		agencyRechargeLog.setUpdateTime(new Date());
		agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
		agencyRechargeLog.setHandleStatus("3");
		agencyRechargeLogService.update(agencyRechargeLog,
				new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
	}

	private AgencyRechargeLog handleAgencyRechargeLog(RechargeModalVO vo, String cmOrderNo, RechargeModalDTO dto,
			String oIdPartner) {
		AgencyRechargeLog agencyRechargeLog = new AgencyRechargeLog();
		agencyRechargeLog.setOrigBusinessId(vo.getOrigBusinessId());
		agencyRechargeLog.setAfterId(vo.getAfterId());
		agencyRechargeLog.setParamJson(JSONObject.toJSONString(vo) + "|" + JSONObject.toJSONString(dto));
		agencyRechargeLog.setCmOrderNo(cmOrderNo);
		agencyRechargeLog.setBankCode(vo.getBankCode());
		agencyRechargeLog.setChargeType(dto.getChargeType());
		agencyRechargeLog.setClientIp(vo.getClientIp());
		agencyRechargeLog.setoIdPartner(dto.getoIdPartner());
		agencyRechargeLog.setRechargeAccountBalance(
				BigDecimal.valueOf(vo.getRechargeAccountBalance() == null ? 0 : vo.getRechargeAccountBalance())
						.setScale(2, BigDecimal.ROUND_HALF_UP));
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog
				.setRechargeAmount(BigDecimal.valueOf(vo.getRechargeAmount() == null ? 0 : vo.getRechargeAmount()));
		agencyRechargeLog.setRechargeSourseAccount(vo.getRechargeSourseAccount());
		agencyRechargeLog.setRechargeUserId(dto.getRechargeUserId());
		agencyRechargeLog.setTransferType(vo.getTransferType());
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog.setCreateTime(new Date());
		agencyRechargeLog.setCreateUser(vo.getOperator());
		agencyRechargeLog.setoIdPartner(oIdPartner);
		agencyRechargeLog.setBankAccount(vo.getBankAccount());
		return agencyRechargeLog;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询充值订单")
	@GetMapping("/queryRechargeOrder")
	@ResponseBody
	public Result queryRechargeOrder(@RequestParam("oidPartner") String oidPartner,
			@RequestParam("cmOrderNo") String cmOrderNo) {
		try {
			if (StringUtil.isEmpty(cmOrderNo) || StringUtil.isEmpty(oidPartner)) {
				return Result.error("500", "订单号或资产端唯一编号不能为空");
			}
			agencyRechargeLogService.queryRechargeOrder(oidPartner, cmOrderNo, loginUserInfoHelper.getUserId());
			return Result.success();
		} catch (Exception e) {
			LOG.error("查询充值订单失败", e);
			return Result.error("500", "查询充值订单失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "对外提供充值接口")
	@PostMapping("/rechargeAmount")
	@ResponseBody
	public Result rechargeAmount(@RequestBody RechargeModalVO vo) {
		try {

			if (vo == null) {
				return Result.error(INVALID_PARAM_CODE, INVALID_PARAM_DESC);
			}

			LOG.info("接收参数：{}", vo);

			if (StringUtil.isEmpty(vo.getOrigBusinessId())) {
				return Result.error(INVALID_PARAM_CODE, "origBusinessId" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getAfterId())) {
				return Result.error(INVALID_PARAM_CODE, "afterId" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getTransferType())) {
				return Result.error(INVALID_PARAM_CODE, "transferType" + INVALID_PARAM_DESC);
			}
			if (vo.getRechargeAmount() == null || vo.getRechargeAmount() < 100) {
				return Result.error(INVALID_PARAM_CODE, "rechargeAmount" + INVALID_PARAM_DESC + "，或者充值金额不能小于100");
			}
			if (StringUtil.isEmpty(vo.getBankCode())) {
				return Result.error(INVALID_PARAM_CODE, "bankCode" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getRechargeUserId())) {
				return Result.error(INVALID_PARAM_CODE, "rechargeUserId" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getChargeType())) {
				return Result.error(INVALID_PARAM_CODE, "chargeType" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getClientIp())) {
				return Result.error(INVALID_PARAM_CODE, "clientIp" + INVALID_PARAM_DESC);
			}
			if (StringUtil.isEmpty(vo.getOperator())) {
				return Result.error(INVALID_PARAM_CODE, "operator" + INVALID_PARAM_DESC);
			}

			BasicBusiness business = basicBusinessService.selectById(vo.getOrigBusinessId());
			vo.setRechargeAccountType(
					String.valueOf(BusinessTypeEnum.getRechargeAccountId(business.getBusinessType())));

			SysParameter sysParameter = tdrepayRechargeService.queryRechargeAccountSysParams(BusinessTypeEnum
					.getRechargeAccountNameByRechargeAccountId(Integer.parseInt(vo.getRechargeAccountType())));
			if (sysParameter == null) {
				return Result.error("没有找到代充值账户的参数配置，请检查充值账户类型是否正确");
			}

			String oIdPartner = sysParameter.getParamValue2();

			if (StringUtil.isEmpty(oIdPartner)) {
				return Result.error(INVALID_PARAM_CODE, "找不到对应的商户号，请检查充值账户类型是否正确");
			}
			return quickRechargeAmount(vo, oIdPartner);
		} catch (Exception e) {
			LOG.error("查询充值订单失败", e);
			return Result.error("查询充值订单失败！");
		}
	}
}
