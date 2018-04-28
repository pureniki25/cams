package com.hongte.alms.finance.controller;

import com.hongte.alms.base.repayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@RestController
@RequestMapping("/RepayPlan")
@Api(tags = "RepayPlanController", description = "还款计划相关控制器")
public class RepayPlanController {


    @ApiOperation(value = "创建还款计划接口")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    public Result creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq){




        return Result.success();
    }


}
