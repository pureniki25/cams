package com.hongte.alms.open.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.open.dto.repayPlan.PlanReturnInfoDto;
import com.hongte.alms.open.feignClient.CreatRepayPlanRemoteApi;
import com.hongte.alms.open.req.repayPlan.CreatRepayPlanReq;
import com.hongte.alms.open.req.repayPlan.trial.TrailRepayPlanReq;
import com.hongte.alms.open.util.TripleDESDecrypt;

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
    private CreatRepayPlanRemoteApi creatRepayPlanRemoteService;


    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
        return creatRepayPlanRemoteService.creatRepayPlan(creatRepayPlanReq);
    }


    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
    @PostMapping("/creatAndSaveRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
        return creatRepayPlanRemoteService.creatAndSaveRepayPlan(creatRepayPlanReq);
    }



    @ApiOperation(value = "试算还款计划接口, 精简字段")
    @PostMapping("/trailRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq){
        return creatRepayPlanRemoteService.trailRepayPlan(trailRepayPlanReq);
    }
    
    @ApiOperation(value = "根据businessId查询还款计划")
	@GetMapping("/queryRepayPlanByBusinessId")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(String businessId){
    	return creatRepayPlanRemoteService.queryRepayPlanByBusinessId(businessId);
    }
    
    @ApiOperation(value = "根据businessId查询还款计划")
    @GetMapping("/deleteRepayPlanByConditions")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> deleteRepayPlanByConditions(String businessId, String repaymentBatchId){
    	return creatRepayPlanRemoteService.deleteRepayPlanByConditions(businessId, repaymentBatchId);
    }



//
//    @Autowired
//    @Qualifier("CreatRepayPlanService")
//    CreatRepayPlanService creatRepayPlanService;
//
//
//    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
//    @PostMapping("/creatRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
//        PlanReturnInfoDto  planReturnInfoDto;
////        List<RepaymentBizPlanDto>  list ;
//        try{
//            planReturnInfoDto = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(planReturnInfoDto));
//
//        return Result.success(planReturnInfoDto);
//    }
//
////    @ApiOperation(value = "测试Ret")
////    @GetMapping("/testRet")
////    @ResponseBody
////    public  Result testRet(){
////        return Result.error("111","111");
////    }
//
//    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
//    @PostMapping("/creatAndSaveRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
//        PlanReturnInfoDto  planReturnInfoDto;
//        List<RepaymentBizPlanDto>  list ;
//        try{
//            planReturnInfoDto = creatRepayPlanService.creatAndSaveRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(creatRepayPlanReq));
//
//        return  Result.success(planReturnInfoDto);
//    }
//
//
//    @ApiOperation(value = "试算还款计划接口, 精简字段")
//    @PostMapping("/trailRepayPlan")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> trailRepayPlan(TrailRepayPlanReq trailRepayPlanReq){
//        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
//        CreatRepayPlanReq creatRepayPlanReq= null;
//        try{
//            creatRepayPlanReq =  ClassCopyUtil.copy(trailRepayPlanReq,TrailRepayPlanReq.class,CreatRepayPlanReq.class);
//
//            TrailBizInfoReq trailBizInfoReq = trailRepayPlanReq.getTrailBizInfoReq();
//
//            BusinessBasicInfoReq businessBasicInfoReq = ClassCopyUtil.copy(trailBizInfoReq,TrailBizInfoReq.class,BusinessBasicInfoReq.class);
//            creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);
//            businessBasicInfoReq.setBusinessId("111");
//            businessBasicInfoReq.setOrgBusinessId(businessBasicInfoReq.getBusinessId());
//
//            List<TrailProjInfoReq> trailProjInfoReqs = trailRepayPlanReq.getProjInfoReqs();
//
//            List<ProjInfoReq>  projInfoReqs = new LinkedList<>();
//            creatRepayPlanReq.setProjInfoReqs(projInfoReqs);
//            for(TrailProjInfoReq trailProjInfoReq:trailProjInfoReqs){
//                ProjInfoReq projInfoReq =ClassCopyUtil.copy(trailProjInfoReq,TrailProjInfoReq.class,ProjInfoReq.class);
//                projInfoReqs.add(projInfoReq);
//
//                List<TrailProjFeeReq> trailProjFeeReqs = trailProjInfoReq.getProjFeeInfos();
//                List<ProjFeeReq> projFeeReqs = new LinkedList<>();
//                projInfoReq.setProjFeeInfos(projFeeReqs);
//
//                for(TrailProjFeeReq trailProjFeeReq:trailProjFeeReqs){
//                    ProjFeeReq projFeeReq = ClassCopyUtil.copy(trailProjFeeReq,TrailProjFeeReq.class,ProjFeeReq.class);
//                    projFeeReqs.add(projFeeReq);
//                }
//
//            }
//        }catch (Exception e){
//            logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
////        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , trailRepayPlanReq);
//        return creatRepayPlan(creatRepayPlanReq);

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
//    }


}
