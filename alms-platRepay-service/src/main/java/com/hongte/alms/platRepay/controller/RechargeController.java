package com.hongte.alms.platRepay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.RechargeModalDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platRepay.vo.RechargeModalVO;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/recharge")
public class RechargeController {
	private static final Logger LOG = LoggerFactory.getLogger(RechargeController.class);

	@Autowired
	private EipRemote eipRemote;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Autowired
	@Qualifier("DepartmentBankService")
	private DepartmentBankService departmentBankService;

	@Value("${recharge.account.car.loan}")
	private String carLoanUserId;
	@Value("${recharge.account.house.loan}")
	private String houseLoanUserId;
	@Value("${recharge.account.relief.loan}")
	private String reliefLoanUserId;
	@Value("${recharge.account.quick.loan}")
	private String quickLoanUserId;
	@Value("${recharge.account.car.business}")
	private String carBusinessUserId;
	@Value("${recharge.account.second.hand.car.loan}")
	private String secondHandCarLoanUserId;
	@Value("${recharge.account.yi_dian_car_loan}")
	private String yiDianCarLoanUserId;

	@Value("${recharge.account.car.loan.oid.partner}")
	private String carLoanOidPartner;
	@Value("${recharge.account.house.loan.loan.oid.partner}")
	private String houseLoanOidPartner;
	@Value("${recharge.account.relief.loan.loan.oid.partner}")
	private String reliefLoanOidPartner;
	@Value("${recharge.account.quick.loan.loan.oid.partner}")
	private String quickLoanOidPartner;
	@Value("${recharge.account.car.business.loan.oid.partner}")
	private String carBusinessOidPartner;
	@Value("${recharge.account.second.hand.car.loan.loan.oid.partner}")
	private String secondHandCarLoanOidPartner;
	@Value("${recharge.account.yi_dian_car_loan.loan.oid.partner}")
	private String yiDianCarLoanOidPartner;

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

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询代充值账户余额")
	@GetMapping("/queryUserAviMoney")
	@ResponseBody
	public Result queryUserAviMoney(@RequestParam("rechargeAccountType") String rechargeAccountType) {

		Map<String, Object> paramMap = new HashMap<>();

		String userId = handleAccountType(rechargeAccountType);

		paramMap.put("userId", userId);
		
		com.ht.ussp.core.Result result = null;
		try {
			result = eipRemote.queryUserAviMoney(paramMap);
		} catch (Exception e) {
			return Result.error("500", "调用外联平台查询代充值账户余额接口失败！");
		}
		
		return Result.success(result);
	}

	private String handleAccountType(String rechargeAccountType) {

		String userId = null;

		if (StringUtil.isEmpty(rechargeAccountType)) {
			return userId;
		}

		switch (rechargeAccountType) {
		case "车贷代充值账户":
			userId = carLoanUserId;
			break;
		case "房贷代充值账户":
			userId = houseLoanUserId;
			break;
		case "扶贫贷代充值账户":
			userId = reliefLoanUserId;
			break;
		case "闪贷业务代充值账户":
			userId = quickLoanUserId;
			break;
		case "车全业务代充值账户":
			userId = carBusinessUserId;
			break;
		case "二手车业务代充值账户":
			userId = secondHandCarLoanUserId;
			break;
		case "一点车贷代充值账户":
			userId = yiDianCarLoanUserId;
			break;
		default:
			userId = "";
			break;
		}
		return userId;
	}

	private String handleOIdPartner(String rechargeAccountType) {

		String oIdPartner = null;

		if (StringUtil.isEmpty(rechargeAccountType)) {
			return oIdPartner;
		}

		switch (rechargeAccountType) {
		case "车贷代充值账户":
			oIdPartner = carLoanOidPartner;
			break;
		case "房贷代充值账户":
			oIdPartner = houseLoanOidPartner;
			break;
		case "扶贫贷代充值账户":
			oIdPartner = reliefLoanOidPartner;
			break;
		case "闪贷业务代充值账户":
			oIdPartner = quickLoanOidPartner;
			break;
		case "车全业务代充值账户":
			oIdPartner = carBusinessOidPartner;
			break;
		case "二手车业务代充值账户":
			oIdPartner = secondHandCarLoanOidPartner;
			break;
		case "一点车贷代充值账户":
			oIdPartner = yiDianCarLoanOidPartner;
			break;
		default:
			oIdPartner = "";
			break;
		}
		return oIdPartner;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "提交充值数据")
	@PostMapping("/commitRechargeData")
	@ResponseBody
	public Result commitRechargeData(@RequestBody RechargeModalVO vo) {

		if (vo == null) {
			return Result.error("500", "不能空数据！");
		}

		String clientIp = "";
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		if (request.getHeader("x-forwarded-for") == null) {
			clientIp = request.getRemoteAddr();
		} else {
			clientIp = request.getHeader("x-forwarded-for");
		}

		String rechargeAccountType = vo.getRechargeAccountType();

		String rechargeUserId = handleAccountType(rechargeAccountType);

		if (StringUtil.isEmpty(rechargeUserId)) {
			return Result.error("500", "没有找到代充值账户用户ID");
		}

		String cmOrderNo = UUID.randomUUID().toString();

		RechargeModalDTO dto = new RechargeModalDTO();
		dto.setAmount(vo.getRechargeAmount());
		// 若转账类型为1 对公，则在银行编码后 + "2B"
		dto.setBankCode("1".equals(vo.getTransferType()) ? vo.getBankCode() + "2B" : vo.getBankCode());
		dto.setClientIp(clientIp);
		dto.setChargeType("3"); // 1：网关、2：快捷、3：代充值
		dto.setCmOrderNo(cmOrderNo);
		String oIdPartner = handleOIdPartner(rechargeAccountType);
		dto.setoIdPartner(oIdPartner);
		dto.setRechargeUserId(rechargeUserId);

		
		AgencyRechargeLog agencyRechargeLog = handleAgencyRechargeLog(vo, clientIp, cmOrderNo, dto, oIdPartner);
		
		agencyRechargeLogService.insert(agencyRechargeLog);
		
		com.ht.ussp.core.Result result = null;
		
		try {
			
			result = eipRemote.agencyRecharge(dto);

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
			if ("0000".equals(result.getReturnCode())) {
				
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

	private void handleUpdateLogFail(String cmOrderNo, AgencyRechargeLog agencyRechargeLog) {
		agencyRechargeLog.setUpdateTime(new Date());
		agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
		agencyRechargeLog.setHandleStatus("3");
		agencyRechargeLogService.update(agencyRechargeLog,
				new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
	}

	private AgencyRechargeLog handleAgencyRechargeLog(RechargeModalVO vo, String clientIp, String cmOrderNo,
			RechargeModalDTO dto, String oIdPartner) {
		AgencyRechargeLog agencyRechargeLog = new AgencyRechargeLog();
		agencyRechargeLog.setParamJson(JSONObject.toJSONString(vo) + "|" + JSONObject.toJSONString(dto));
		agencyRechargeLog.setCmOrderNo(cmOrderNo);
		agencyRechargeLog.setBankCode(vo.getBankCode());
		agencyRechargeLog.setChargeType(dto.getChargeType());
		agencyRechargeLog.setClientIp(clientIp);
		agencyRechargeLog.setoIdPartner(dto.getoIdPartner());
		agencyRechargeLog.setRechargeAccountBalance(
				BigDecimal.valueOf(vo.getRechargeAccountBalance() == null ? 0 : vo.getRechargeAccountBalance()));
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog
				.setRechargeAmount(BigDecimal.valueOf(vo.getRechargeAmount() == null ? 0 : vo.getRechargeAmount()));
		agencyRechargeLog.setRechargeSourseAccount(vo.getRechargeSourseAccount());
		agencyRechargeLog.setRechargeUserId(dto.getRechargeUserId());
		agencyRechargeLog.setTransferType(vo.getTransferType());
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog.setCreateTime(new Date());
		agencyRechargeLog.setCreateUser(loginUserInfoHelper.getUserId());
		agencyRechargeLog.setoIdPartner(oIdPartner);
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
}
