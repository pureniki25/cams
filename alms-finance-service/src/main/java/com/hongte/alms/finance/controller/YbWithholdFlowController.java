package com.hongte.alms.finance.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.service.WithholdingFlowRecordService;
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
@Api(tags = "YbWithholdFlowController", description = "易宝代扣流水", hidden = true)
public class YbWithholdFlowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepayFlowController.class);

    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService;

    @Autowired
    @Qualifier("WithholdingFlowRecordService")
    private WithholdingFlowRecordService withholdingFlowRecordService;

    /**
     * 易宝代扣流水列表
     *
     * @param withholdFlowReq
     * @return
     */
    @ApiOperation(value = "易宝代扣流水列表")
    @GetMapping("/getYbWithholdFlowPageList")
    @ResponseBody
    // public PageResult<List<YbWithholdFlowVo>>
    // getYbWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
    public PageResult<List<WithholdingFlowRecord>> getYbWithholdFlowPageList(WithholdFlowReq withholdFlowReq) {
        LOGGER.info("====>>>>>易宝代扣流水列表[{}]", JSON.toJSONString(withholdFlowReq));
        try {
            // Page<YbWithholdFlowVo> pages =
            // withholdingRepaymentLogService.getYbWithholdFlowPageList(withholdFlowReq);
            EntityWrapper<WithholdingFlowRecord> ew = new EntityWrapper<>();
            ew.eq("withholding_platform", PlatformEnum.YB_FORM.getValue());
            ew.ge("liquidation_date", withholdFlowReq.getStartTime());
            ew.le("liquidation_date", withholdFlowReq.getEndTime());

            Page<WithholdingFlowRecord> pages = new Page<>(withholdFlowReq.getPage(), withholdFlowReq.getLimit());
            withholdingFlowRecordService.selectByPage(pages, ew);

            return PageResult.success(pages.getRecords(), pages.getTotal());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }

}
