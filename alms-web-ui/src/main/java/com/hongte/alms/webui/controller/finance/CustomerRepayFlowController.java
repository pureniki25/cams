package com.hongte.alms.webui.controller.finance;

import com.hongte.alms.common.util.ExportFileUtil;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;


@Controller
@RequestMapping("/customer")
@Api(tags = "CustomerRepayFlowController", description = "客户还款登记流水列表页")
public class CustomerRepayFlowController {
    /**
     * 客户还款流水主页
     * @return
     */
    @RequestMapping("/flowList")
    public String flowList() {
        return "/finance/customerRepayFlowList";
    }

    /**
     * 银行还款流水主页
     * @return
     */
    @RequestMapping("/bankFlow")
    public String bankFlow() {
        return "/finance/bankWithholdRepayFlowList";
    }


    /**
     * 宝付流水
     * @return
     */
    @RequestMapping("/bfFlow")
    public String bfFlow() {
        return "/finance/bfWithholdRepayFlowList";
    }

    /**
     * 易宝流水
     * @return
     */
    @RequestMapping("/ybFlow")
    public String ybFlow() {
        return "/finance/ybWithholdRepayFlowList";
    }




}
