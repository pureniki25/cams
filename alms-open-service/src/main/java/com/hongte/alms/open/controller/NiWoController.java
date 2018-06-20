package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.base.vo.module.api.RepayLogResp;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.open.service.WithHoldingXinDaiService;
import com.hongte.alms.open.vo.*;
import com.ht.ussp.util.DateUtil;

import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/niwo")
@Api(tags = "WithHoldingController", description = "执行代扣", hidden = true)
@RefreshScope
public class NiWoController {
	private Logger logger = LoggerFactory.getLogger(NiWoController.class);
	@Autowired
	@Qualifier("WithholdingRecordLogService")
	WithholdingRecordLogService WithholdingRecordLogService;

	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;



	@ApiOperation(value = "指定银行卡代扣")
	@PostMapping("/repayAssignBank")
	public Result repayAssignBank(@RequestParam("businessId") String businessId,@RequestParam("afterId") String afterId,@RequestParam("bankCard") String bankCard){
		Result result=new Result();

		return result;
	}
	
	
	


}
