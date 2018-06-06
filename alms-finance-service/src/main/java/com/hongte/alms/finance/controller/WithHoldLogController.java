package com.hongte.alms.finance.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.req.WithHoldLogReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/6/5
 */
@RestController
@RequestMapping("/WithHoldLog")
@Api(tags = "WithHoldLogController", description = "代扣记录流水相关控制器")
public class WithHoldLogController {


    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;

    @ApiOperation(value = "获取代扣记录流水列表，分页")
    @PostMapping("/getWithHoldLogs")
    @ResponseBody
    public PageResult<List<WithholdingRepaymentLog>> getWithHoldLogs( @RequestBody WithHoldLogReq withHoldLogReq){




        Page<WithholdingRepaymentLog> pages = new Page<>();

        Wrapper<WithholdingRepaymentLog> wrapper = new EntityWrapper<>();
        wrapper.eq("identity_card",withHoldLogReq.getIdentifyCard());


        pages.setCurrent(withHoldLogReq.getPage());
        pages.setSize(withHoldLogReq.getSize());

        pages = withholdingRepaymentLogService.selectPage(pages,wrapper);

        return PageResult.success(pages.getRecords(),pages.getTotal());
    }





    


}
