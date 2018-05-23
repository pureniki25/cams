package com.hongte.alms.finance.controller;

import java.util.LinkedList;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanVo;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.dto.repayPlan.PlanReturnInfoDto;
import com.hongte.alms.finance.req.repayPlan.BusinessBasicInfoReq;
import com.hongte.alms.finance.req.repayPlan.CreatRepayPlanReq;
import com.hongte.alms.finance.req.repayPlan.ProjFeeReq;
import com.hongte.alms.finance.req.repayPlan.ProjInfoReq;
import com.hongte.alms.finance.req.repayPlan.trial.TrailBizInfoReq;
import com.hongte.alms.finance.req.repayPlan.trial.TrailProjFeeReq;
import com.hongte.alms.finance.req.repayPlan.trial.TrailProjInfoReq;
import com.hongte.alms.finance.req.repayPlan.trial.TrailRepayPlanReq;
import com.hongte.alms.finance.service.CreatRepayPlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@RestController
@RequestMapping("/RepayPlan")
@Api(tags = "RepayPlanController", description = "还款计划相关控制器")
public class RepayPlanController {

    Logger  logger = LoggerFactory.getLogger(RepayPlanController.class);

    @Autowired
    @Qualifier("CreatRepayPlanService")
    CreatRepayPlanService creatRepayPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;


    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        PlanReturnInfoDto  planReturnInfoDto;
//        List<RepaymentBizPlanDto>  list ;
        try{
            planReturnInfoDto = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        }catch (CreatRepaymentExcepiton e){
            logger.info("传入数据格式不对[{}]" , e);
            return Result.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.info("程序异常[{}]" , e);
            return Result.error("9889",e.getMessage());
        }
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(planReturnInfoDto));

        return Result.success(planReturnInfoDto);
    }

//    @ApiOperation(value = "测试Ret")
//    @GetMapping("/testRet")
//    @ResponseBody
//    public  Result testRet(){
//        return Result.error("111","111");
//    }

    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
    @PostMapping("/creatAndSaveRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        PlanReturnInfoDto  planReturnInfoDto;
        try{
            planReturnInfoDto = creatRepayPlanService.creatAndSaveRepayPlan(creatRepayPlanReq);
        }catch (CreatRepaymentExcepiton e){
            logger.info("传入数据格式不对[{}]" , e);
            return Result.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.info("程序异常[{}]" , e);
            return Result.error("9889",e.getMessage());
        }

        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(creatRepayPlanReq));

        return  Result.success(planReturnInfoDto);
    }


    @ApiOperation(value = "试算还款计划接口, 精简字段")
    @PostMapping("/trailRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq){
        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
        CreatRepayPlanReq creatRepayPlanReq= null;
        try{
            creatRepayPlanReq =  ClassCopyUtil.copy(trailRepayPlanReq,TrailRepayPlanReq.class,CreatRepayPlanReq.class);

            TrailBizInfoReq trailBizInfoReq = trailRepayPlanReq.getTrailBizInfoReq();

            BusinessBasicInfoReq businessBasicInfoReq = ClassCopyUtil.copy(trailBizInfoReq,TrailBizInfoReq.class,BusinessBasicInfoReq.class);
            creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);
            businessBasicInfoReq.setBusinessId("111");
            businessBasicInfoReq.setOrgBusinessId(businessBasicInfoReq.getBusinessId());

            List<TrailProjInfoReq> trailProjInfoReqs = trailRepayPlanReq.getProjInfoReqs();

            List<ProjInfoReq>  projInfoReqs = new LinkedList<>();
            creatRepayPlanReq.setProjInfoReqs(projInfoReqs);
            for(TrailProjInfoReq trailProjInfoReq:trailProjInfoReqs){
                ProjInfoReq projInfoReq =ClassCopyUtil.copy(trailProjInfoReq,TrailProjInfoReq.class,ProjInfoReq.class);
                projInfoReqs.add(projInfoReq);

                List<TrailProjFeeReq> trailProjFeeReqs = trailProjInfoReq.getProjFeeInfos();
                List<ProjFeeReq> projFeeReqs = new LinkedList<>();
                projInfoReq.setProjFeeInfos(projFeeReqs);

                for(TrailProjFeeReq trailProjFeeReq:trailProjFeeReqs){
                    ProjFeeReq projFeeReq = ClassCopyUtil.copy(trailProjFeeReq,TrailProjFeeReq.class,ProjFeeReq.class);
                    projFeeReqs.add(projFeeReq);
                }

            }
        }catch (Exception e){
            logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--异常[{}]" , e);
            return Result.error("9889",e.getMessage());
        }
//        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , trailRepayPlanReq);
        return creatRepayPlan(creatRepayPlanReq);

//        List<RepaymentBizPlanDto>  list ;
//        try{
//            list = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , list);
//
//        return Result.success(list);
    }
    
	@ApiOperation(value = "根据businessId查询还款计划")
	@GetMapping("/queryRepayPlanByBusinessId")
	@ResponseBody
	public Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(@RequestParam(value = "businessId") String businessId) {
		logger.info("查询还款计划，业务编号：[{}]", businessId);
		try {
			if (StringUtil.isEmpty(businessId)) {
				return Result.error("9889", "业务编号不能为空！");
			}
			PlanReturnInfoDto planReturnInfoDto = creatRepayPlanService.queryRepayPlanByBusinessId(businessId);
			if (planReturnInfoDto == null) {
				return Result.build("1", "没有找到相关数据，请检查业务编号是否输入正确！", planReturnInfoDto);
			}
			return Result.success(planReturnInfoDto);
		} catch (Exception e) {
			logger.error("查询还款计划异常[{}]", e);
			return Result.error("9889", e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据条件撤销还款计划")
	@GetMapping("/deleteRepayPlanByConditions")
	@ResponseBody
	public Result deleteRepayPlanByConditions(@RequestParam(value = "businessId") String businessId,
			@RequestParam(value = "repaymentBatchId") String repaymentBatchId) {
		logger.info("删除还款计划，业务编号：[{}], 还款计划编号[{}]", businessId, repaymentBatchId);
		try {
			if (StringUtil.isEmpty(businessId)  || StringUtil.isEmpty(repaymentBatchId)) {
				return Result.error("9889", "业务编号或还款批次号不能为空！");
			}
			creatRepayPlanService.deleteRepayPlanByConditions(businessId, repaymentBatchId);
			return Result.success();
		} catch (Exception e) {
			logger.error("删除还款计划异常[{}]", e);
			return Result.error("9889", e.getMessage());
		}
	}

    @ApiOperation(value = "根据用户身份证ID获取用户待还款的业务信息列表")
    @GetMapping("/repaymentGetRepayingList")
    @ResponseBody
   public PageResult<Page<RepayingPlanVo>> repaymentGetRepayingList(Integer pageIndex, Integer pageSize, String identifyCard){
        logger.info("根据用户身份证ID获取用户待还款的业务信息列表，客户身份证号：[{}], 页数[{}],每页大小[{}]", identifyCard, pageIndex,pageSize);
        Page<RepayingPlanVo>  page = null;
        try{
            page = repaymentBizPlanService.queryCustomeRepayPlanInfo(identifyCard, pageIndex, pageSize) ;
            logger.info("根据用户身份证ID获取用户待还款的业务信息列表，返回数据：[{}]", JSON.toJSONString(page));
            return  PageResult.success(page,page.getTotal());
        }catch (Exception e){
            logger.error("根据用户身份证ID获取用户待还款的业务信息列表异常[{}]", e);
            return  PageResult.error(9889, e.getMessage());
        }
   }




}
