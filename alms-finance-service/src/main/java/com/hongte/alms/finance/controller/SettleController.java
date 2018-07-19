/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.util.Date;
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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SettleService;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

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
	
	@Qualifier("RepaymentBizPlanListService")
	@Autowired
	RepaymentBizPlanListService repaymentBizPlanListService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;
	
	@Qualifier("SettleService")
	@Autowired
	SettleService settleService ;
	
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
	
	@GetMapping(value="/settleInfo")
	@ApiOperation(value="结清应还信息")
	public Result<SettleInfoVO> settleInfo(String businessId,String afterId,String planId,String factRepayDate) {
		try {
			logger.info("@settleInfo@结清应还信息--开始[{}]", businessId);
			if (StringUtil.isEmpty(planId)) {
				planId = null ;
			}
			Date repayDate = null ;
			if (!StringUtil.isEmpty(factRepayDate)) {
				repayDate = DateUtil.getDate(factRepayDate);
			}
			SettleInfoVO infoVO = settleService.settleInfoVO(businessId, afterId, planId,repayDate);
			logger.error("@settleInfo@结清应还信息--结束[{}]", JSONObject.toJSONString(infoVO));
			return Result.success(infoVO);
		} catch (Exception e) {
			logger.error("@settleInfo@结清应还信息--结束[{}]", e);
			e.printStackTrace();
			return Result.error("500", "系统异常:还款计划失败");
		}
	}
}
