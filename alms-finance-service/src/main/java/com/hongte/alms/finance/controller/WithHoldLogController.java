package com.hongte.alms.finance.controller;

import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    


}
