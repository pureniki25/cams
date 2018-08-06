/**
 * 
 */
package com.hongte.alms.finance.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;

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
	
	@Autowired
	@Qualifier("FinanceSettleService")
	FinanceSettleService financeSettleService ;
	
	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService ;
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService ;
	
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
	
	@RequestMapping(value="/settleInfo")
	@ApiOperation(value="结清应还信息")
	public Result<SettleInfoVO> settleInfo(@RequestBody FinanceSettleReq req) {
		try {
			logger.info("@settleInfo@结清应还信息--开始[{}]", JSON.toJSONString(req));
			SettleInfoVO infoVO = financeSettleService.settleInfoVO(req);
			logger.error("@settleInfo@结清应还信息--结束[{}]", JSONObject.toJSONString(infoVO));
			return Result.success(infoVO);
		} catch (Exception e) {
			logger.error("@settleInfo@结清应还信息--结束[{}]", e);
			e.printStackTrace();
			return Result.error("500", "系统异常:还款计划失败");
		}
	}
	
	@GetMapping(value="/listOtherFee")
	@ApiOperation(value="获取其他费用项")
	public Result listOtherFee(String businessId,String planId) {
		try {
			logger.info("@listOtherFee@获取其他费用项--开始[{}{}]", businessId,planId);
			BasicBusiness business = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
			if (business==null) {
				return Result.error("查不到对应的业务编号");
			}
			if (!BusinessTypeEnum.CYD_TYPE.getValue().equals(business.getBusinessType()) 
					&& !BusinessTypeEnum.FSD_TYPE.getValue().equals(business.getBusinessType())) {
				logger.info("@listOtherFee@获取其他费用项--结束[]");
				return Result.success(new ArrayList<>());
			}
			/*判断是否最后一次结清,最后一次才能取到其他费用项,否则取不到*/
			boolean isFinalRepayPlanSettle = repaymentBizPlanService.isFinalRepayPlanSettle(businessId, planId);
			if (!isFinalRepayPlanSettle) {
				logger.info("@listOtherFee@获取其他费用项--结束[]");
				return Result.success(new ArrayList<>());
			}
			
			List<SysParameter> selectList = new ArrayList<>() ;
			if (BusinessTypeEnum.CYD_TYPE.getValue().equals(business.getBusinessType())) {
				/*作者开发时车贷其他费用类型=13，参考param_type=13的数据,后期维护按实际情况来更改*/
				selectList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", 13));
			}
			
			if (BusinessTypeEnum.FSD_TYPE.getValue().equals(business.getBusinessType())) {
				/*作者开发时房贷其他费用类型=14，参考param_type=14的数据,后期维护按实际情况来更改*/
				selectList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", 14));
			}
			List<SettleFeesVO> otherFees = new ArrayList<>() ;
			
			for (SysParameter parameter : selectList) {
				SettleFeesVO fee = new SettleFeesVO() ;
				fee.setFeeName(parameter.getParamName());
				fee.setFeeId(parameter.getParamValue());
				fee.setPlanItemType(parameter.getParamType());
				fee.setShareProfitIndex(1201);
				otherFees.add(fee);
			}
			
			logger.info("@listOtherFee@获取其他费用项--结束[{}]",JSON.toJSONString(otherFees));
			return Result.success(otherFees);
		} catch (Exception e) {
			logger.error("@listOtherFee@获取其他费用项--异常",e);
			return Result.error("获取其他费用项失败");
		}
	}
}
