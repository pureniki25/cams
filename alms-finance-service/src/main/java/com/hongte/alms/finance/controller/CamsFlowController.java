package com.hongte.alms.finance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.feignClient.AccountListHandlerClient;
import com.hongte.alms.base.vo.cams.CancelBizAccountListCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.ht.ussp.core.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RefreshScope
@RestController
@RequestMapping("/camsFlowSync")
@Api(tags = "CamsFlowSyncController", description = "核心账号同步测试", hidden = true)
public class CamsFlowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamsFlowController.class);

    @Autowired
    private AccountListHandlerClient accountListHandlerClient;
    /**
     * 批量新增账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "批量新增账户流水")
    @GetMapping("/addBatchFlow")
    @ResponseBody
    public Result<Object> addBatchFlow() {
    	CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    	
    	
    	return accountListHandlerClient.addBatchFlow(command);
    }
    
    /**
     * 撤销业务流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "撤销业务流水")
    @GetMapping("/cancelFlow")
    @ResponseBody
    public Result<Object> cancelFlow() {
    	CancelBizAccountListCommand command = new CancelBizAccountListCommand();
    	return accountListHandlerClient.cancelFlow(command);
    }
}
