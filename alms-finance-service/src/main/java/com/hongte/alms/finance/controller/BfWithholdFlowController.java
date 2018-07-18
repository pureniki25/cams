package com.hongte.alms.finance.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.service.WithholdingFlowRecordService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/customer")
@Api(tags = "BfWithholdFlowController", description = "宝付代扣流水", hidden = true)
public class BfWithholdFlowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepayFlowController.class);

    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService;

    @Autowired
    @Qualifier("WithholdingFlowRecordService")
    private WithholdingFlowRecordService withholdingFlowRecordService;

    /**
     * 宝付代扣流水列表
     *
     * @param withholdFlowReq
     * @return
     */
    @ApiOperation(value = "宝付代扣流水列表")
    @GetMapping("/getBfWithholdFlowPageList")
    @ResponseBody
    // public PageResult<List<BfWithholdFlowVo>>
    // getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
    public PageResult<List<WithholdingFlowRecord>> getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
        LOGGER.info("====>>>>>宝付代扣流水列表[{}]", JSON.toJSONString(withholdFlowReq));
        try {
            // Page<BfWithholdFlowVo> pages =
            // withholdingRepaymentLogService.getBfWithholdFlowPageList(withholdFlowReq);

            EntityWrapper<WithholdingFlowRecord> ew = new EntityWrapper<>();
            ew.eq("withholding_platform", PlatformEnum.BF_FORM.getValue());
            if (StringUtils.isNotBlank(withholdFlowReq.getStartTime()))
                ew.ge("liquidation_date", withholdFlowReq.getStartTime());
            if (StringUtils.isNotBlank(withholdFlowReq.getEndTime()))
                ew.le("liquidation_date", withholdFlowReq.getEndTime());

            // 查分页数据
            Page<WithholdingFlowRecord> pages = new Page<>(withholdFlowReq.getPage(), withholdFlowReq.getLimit());
            withholdingFlowRecordService.selectByPage(pages, ew);

            return PageResult.success(pages.getRecords(), pages.getTotal());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }

    /**
     * 查询宝付汇总数据
     */
    @ApiOperation("查询宝付汇总数据")
    @RequestMapping("/queryBfWithholdFlowSummary")
    @ResponseBody
    public Result<WithholdingFlowRecordSummaryVo> querySummary(WithholdFlowReq withholdFlowReq) {
        try {
            // 查汇总数据
            withholdFlowReq.setWithholdingPlatform((Integer) PlatformEnum.BF_FORM.getValue());
            WithholdingFlowRecordSummaryVo summaryVo = withholdingFlowRecordService.querySummary(withholdFlowReq);
            return Result.success(summaryVo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}
