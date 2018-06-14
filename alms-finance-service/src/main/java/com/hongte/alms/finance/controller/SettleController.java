/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 王继光
 * 2018年6月12日 下午3:28:17
 */
@RestController
@RefreshScope
@RequestMapping(value = "/settle")
@Api(tags = "SettleController", description = "财务结清控制器")
public class SettleController {

	private Logger logger = LoggerFactory.getLogger(SettleController.class);
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService ;
	
	@GetMapping(value="/listRepaymentSettleListVOs")
	@ApiOperation(value="还款计划")
	public Result<List<RepaymentSettleListVO>> listRepaymentSettleListVOs(String businessId,String afterId,String planId) {
		try {
			logger.info("@listRepaymentSettleListVOs@还款计划--开始[{}]", businessId);
			Result<List<RepaymentSettleListVO>> result = null;
			List<RepaymentSettleListVO> list = repaymentBizPlanService.listRepaymentSettleListVOs(businessId,afterId, planId);
			result = Result.success(list);
			logger.info("@listRepaymentSettleListVOs@还款计划--结束[{}]", JSON.toJSONString(result));
			return result;
		} catch (Exception e) {
			logger.error("@listRepaymentSettleListVOs@还款计划--[{}]", e);
			e.printStackTrace();
			return Result.error("500", "系统异常:还款计划失败");
		}
	}
}
