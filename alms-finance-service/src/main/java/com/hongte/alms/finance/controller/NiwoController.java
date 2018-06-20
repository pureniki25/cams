/**
 * 
 */
package com.hongte.alms.finance.controller;

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


import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.service.NiWoRepayPlanService;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

/**
 * @author  2018年6月20日 下午6:00:37
 */
@RestController
@RefreshScope
@RequestMapping(value = "/niwoController")
public class NiwoController {

	private static Logger logger = LoggerFactory.getLogger(NiwoController.class);


	@Autowired
	@Qualifier("NiWoRepayPlanService")
	 NiWoRepayPlanService niWoRepayPlanService ;
	
	

	@GetMapping(value = "/sycRepayPlan")
	@ApiOperation(value = "同步你我金融还款计划")
	public void sycRepayPlan(String projId) {
		logger.info("@sycRepayPlan@同步你我金融还款计划--开始[{},{}]");
		niWoRepayPlanService.sycNiWoRepayPlan(projId);
		logger.info("@sycRepayPlan@同步你我金融还款计划--结束[{}]");
	}

	
}
