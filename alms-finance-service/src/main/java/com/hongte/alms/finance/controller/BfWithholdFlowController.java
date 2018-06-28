package com.hongte.alms.finance.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.customer.vo.BankWithholdFlowVo;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Api(tags = "BfWithholdFlowController", description = "宝付代扣流水", hidden = true)
public class BfWithholdFlowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepayFlowController.class);

    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService;
    /**
     * 宝付代扣流水列表
     *
     * @param withholdFlowReq
     * @return
     */
    @ApiOperation(value = "宝付代扣流水列表")
    @GetMapping("/getBfWithholdFlowPageList")
    @ResponseBody
    public PageResult<List<BankWithholdFlowVo>> getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
        LOGGER.info("====>>>>>宝付代扣流水列表[{}]", JSON.toJSONString(withholdFlowReq));


        try {
            Page<BankWithholdFlowVo> pages = withholdingRepaymentLogService.getBfWithholdFlowPageList(withholdFlowReq);

            return PageResult.success(pages.getRecords(), pages.getTotal());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }


}
