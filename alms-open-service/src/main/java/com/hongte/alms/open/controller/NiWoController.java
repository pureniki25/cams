package com.hongte.alms.open.controller;


import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;
import com.hongte.alms.common.result.Result;

import com.hongte.alms.open.feignClient.NiwoRepayPlanRemoteApi;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/niwo")
@Api(tags = "NiWoController", description = "你我金融控制器", hidden = true)
@RefreshScope
public class NiWoController {
	private Logger logger = LoggerFactory.getLogger(NiWoController.class);
	
	
	@Autowired
	NiwoRepayPlanRemoteApi niwoRepayPlanRemoteApi;

	

	@ApiOperation(value = "你我金融还款计划回调")
	@PostMapping("/sycRepayPlan")
	public Result sycRepayPlan(@RequestBody HashMap<String, Object> map){

		logger.info("@sycRepayPlan@同步你我金融还款计划--开始[{},{}]");
		niwoRepayPlanRemoteApi.sycRepayPlan(map);
		logger.info("@sycRepayPlan@同步你我金融还款计划--结束[{}]");
		return Result.success("回调成功");
	}
	
	
	


}
