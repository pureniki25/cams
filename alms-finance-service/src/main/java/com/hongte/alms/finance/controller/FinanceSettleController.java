package com.hongte.alms.finance.controller;

import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping(value = "/finance")
@Api(tags = "FinanceSettleController", description = "财务管理模块相关控制器")
public class FinanceSettleController {
    private static Logger logger = LoggerFactory.getLogger(FinanceSettleController.class);


    @Autowired
    private FinanceSettleService financeSettleService;
    @RequestMapping("/financeSettle")
    @ApiOperation(value="资金结清")
    public Result financeSettle(@RequestBody FinanceSettleReq financeSettleReq){
        logger.info("@financeSettle@资金结算开始[{}]");
        Result result = null;
        try {
            financeSettleService.financeSettle(financeSettleReq);




            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            logger.error("@financeSettle@资金结清出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "资金结清出错");
            logger.error("@financeSettle@资金结清出错{}", e);
        }

        logger.info("@financeSettle@资金结清结束{}", result);
        return result;
    }
}
