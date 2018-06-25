package com.hongte.alms.webui.controller.finance;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


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
     * 客户还款流水主页
     * @return
     */
    @RequestMapping("/bankFlow")
    public String bankFlow() {
        return "/finance/bankWithholdRepayFlowList";
    }

}
