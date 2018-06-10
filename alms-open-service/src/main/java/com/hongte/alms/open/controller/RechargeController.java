package com.hongte.alms.open.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.common.result.Result;

@Controller
@RequestMapping("/recharge")
public class RechargeController {

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/callBackAgencyRecharge")
	@ResponseBody
	public Result callBackAgencyRecharge(@RequestBody Map<String, Object> paramMap) {
		try {
			if (paramMap == null || paramMap.get("cmOrderNo") == null || paramMap.get("orderStatus") == null) {
				return Result.error("500", "订单号或处理状态不能为空");
			}
			agencyRechargeLogService.callBackAgencyRecharge((String) paramMap.get("cmOrderNo"),
					(String) paramMap.get("orderStatus"));
			return Result.success();
		} catch (Exception e) {
			return Result.error("500", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/callBackDistributeFund")
	@ResponseBody
	public Result callBackDistributeFund(@RequestBody Map<String, Object> paramMap) {
		try {
			if (paramMap == null || paramMap.get("oIdPartner") == null || paramMap.get("batchId") == null
					|| paramMap.get("orderStatus") == null) {
				return Result.error("500", "资产端账户唯一编号或批次号或处理状态不能为空");
			}
			tdrepayRechargeLogService.callBackDistributeFund((String) paramMap.get("oIdPartner"),
					(String) paramMap.get("batchId"), (String) paramMap.get("requestNo"),
					(String) paramMap.get("orderStatus"));
			return Result.success();
		} catch (Exception e) {
			return Result.error("500", e.getMessage());
		}
	}
}
