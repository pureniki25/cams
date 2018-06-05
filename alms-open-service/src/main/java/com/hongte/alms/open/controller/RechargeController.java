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
import com.hongte.alms.common.result.Result;

@Controller
@RequestMapping("/recharge")
public class RechargeController {

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;

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
			return Result.error("500", "系统异常，接口调用失败！");
		}
	}
}
