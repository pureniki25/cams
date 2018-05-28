package com.hongte.alms.open.controller;


import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hongte.alms.open.feignClient.CreatRepayPlanRemoteApi;
import com.hongte.alms.open.util.TripleDESDecrypt;
import com.hongte.alms.open.vo.RepayPlanReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

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
    public Result<PlanReturnInfoDto> creatRepayPlan(@RequestBody CreatRepayPlanReq creatRepayPlanReq){
        return creatRepayPlanRemoteService.creatRepayPlan(creatRepayPlanReq);
    }


    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
    @PostMapping("/creatAndSaveRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(@RequestBody CreatRepayPlanReq creatRepayPlanReq){
        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        Result<PlanReturnInfoDto> ret= creatRepayPlanRemoteService.creatAndSaveRepayPlan(creatRepayPlanReq);
        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--结束[{}]" , JSON.toJSONString(ret));
        return ret;

    }

//    @ApiOperation(value = "测试时间转换")
//    @PostMapping("/testTime")
//    public Result<PlanReturnInfoDto> testTime(){
//         Result<PlanReturnInfoDto> ret= creatRepayPlanRemoteService.testTime();
//         return ret;
//
//    }



    @ApiOperation(value = "试算还款计划接口, 精简字段")
    @PostMapping("/trailRepayPlan")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> trailRepayPlan(@RequestBody TrailRepayPlanReq trailRepayPlanReq){
        logger.info("试算还款计划接口, 精简字段--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
        Result<PlanReturnInfoDto> ret = creatRepayPlanRemoteService.trailRepayPlan(trailRepayPlanReq);
        logger.info("试算还款计划接口, 精简字段--结束[{}]" , JSON.toJSONString(ret));



        return ret;
    }

    @ApiOperation(value = "根据businessId查询还款计划")
    @PostMapping("/queryRepayPlanByBusinessId")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(@RequestBody RepayPlanReq req){
        return creatRepayPlanRemoteService.queryRepayPlanByBusinessId(req);
    }

    @ApiOperation(value = "撤销还款计划")
    @PostMapping("/deleteRepayPlanByConditions")
    @ResponseBody
    @TripleDESDecrypt
    public Result<PlanReturnInfoDto> deleteRepayPlanByConditions(@RequestParam(value = "businessId") String businessId,
                                                                 @RequestParam(value = "repaymentBatchId") String repaymentBatchId){
        return creatRepayPlanRemoteService.deleteRepayPlanByConditions(businessId, repaymentBatchId);
    }

    @ApiOperation(value = "根据businessId查询还款计划")
    @PostMapping("/getRepayList")
    @ResponseBody
//    @TripleDESDecrypt
    Result<List<BizDto>> getRepayList(@RequestParam(value = "businessIds") List<String> businessIds){
        return creatRepayPlanRemoteService.getRepayList(businessIds);
    }


     @ApiOperation(value = "根据业务ID查找此业务的历史账单")
    @PostMapping("/getLogBill")
    @ResponseBody
    Result<BizDto> getLogBill(@RequestParam(value = "businessId")String businessId){
        return creatRepayPlanRemoteService.getLogBill(businessId);
    }

    @ApiOperation(value = "根据还款计划ID查找出此还款计划的详情账单信息")
    @PostMapping("/getBizPlanBill")
    @ResponseBody
     Result<BizPlanDto> getBizPlanBill(@RequestParam(value = "planId") String planId){
        return creatRepayPlanRemoteService.getBizPlanBill(planId);
    }


//    @RequestMapping(value = "/RepayPlan/getRepayList",headers = {"app=ALMS", "content-type=application/json"},method = RequestMethod.POST)
//    Result<PlanReturnInfoDto> getRepayList(@RequestParam(value = "businessIds") List<String> businessIds) ;



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
