package com.hongte.alms.finance.controller;

import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.finance.dto.repayPlan.RepaymentBizPlanDto;
import com.hongte.alms.finance.req.repayPlan.CreatRepayPlanReq;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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
    @Qualifier("CreatRepayPlanService")
    CreatRepayPlanService creatRepayPlanService;


    @ApiOperation(value = "创建还款计划接口,对业务和标的还款计划进行试算")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    public Result<List<RepaymentBizPlanDto>> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , creatRepayPlanReq);

        List<RepaymentBizPlanDto>  list ;
        try{
            list = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        }catch (CreatRepaymentExcepiton e){
            logger.info("传入数据格式不对[{}]" , e);
            return Result.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.info("程序异常[{}]" , e);
            return Result.error("9889",e.getMessage());
        }
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , creatRepayPlanReq);

        return Result.success();
    }


    public Result creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq){


        return  null;
    }


}
