package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.vo.RechargeModalVO;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/toPlatRepay")
public class ToPlatRepayController {
	private static final Logger LOG = LoggerFactory.getLogger(ToPlatRepayController.class);

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


	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService repaymentProjPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询代充值账户余额")
	@GetMapping("/queryUserAviMoney")
	@ResponseBody
	public Result queryUserAviMoney(@RequestParam("rechargeAccountType") String rechargeAccountType) {

		Map<String, Object> paramMap = new HashMap<>();

		String userId = tdrepayRechargeService.handleAccountType(rechargeAccountType);

		paramMap.put("userId", userId);
		
		com.ht.ussp.core.Result result = null;
		try {
			result = eipRemote.queryUserAviMoney(paramMap);
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
			return Result.error("500", "不能空数据！");
		}

		String clientIp = CommonUtil.getClientIp();

		String rechargeAccountType = vo.getRechargeAccountType();

		String rechargeUserId = tdrepayRechargeService.handleAccountType(rechargeAccountType);

		if (StringUtil.isEmpty(rechargeUserId)) {
			return Result.error("500", "没有找到代充值账户用户ID");
		}

		String cmOrderNo = UUID.randomUUID().toString();

		RechargeModalDTO dto = new RechargeModalDTO();
		dto.setAmount(new BigDecimal(vo.getRechargeAmount().toString()));
		// 若转账类型为1 对公，则在银行编码后 + "2B"
		dto.setBankCode("1".equals(vo.getTransferType()) ? vo.getBankCode() + "2B" : vo.getBankCode());
		dto.setClientIp(clientIp);
		dto.setChargeType("3"); // 1：网关、2：快捷、3：代充值
		dto.setCmOrderNo(cmOrderNo);
		String oIdPartner = tdrepayRechargeService.handleOIdPartner(rechargeAccountType);
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
