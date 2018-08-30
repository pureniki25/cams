package com.hongte.alms.platrepay.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/toPlatRepay")
public class ToPlatRepayController {
	private static final Logger LOG = LoggerFactory.getLogger(ToPlatRepayController.class);

	@Autowired
	@Qualifier("SysParameterService")
	private SysParameterService sysParameterService;

	@ApiOperation(value = "银行代扣对应的业务资产端唯一编号")
	@GetMapping("/getOIdPartner")
	@ResponseBody
	public Result<Map<String, Object>> getOIdPartner(@RequestParam("businessType") Integer businessType) {
		
		try {
			String rechargeAccountType = BusinessTypeEnum.getRechargeAccountName(businessType);
			
	        SysParameter sysParameter = sysParameterService.queryRechargeAccountSysParams(rechargeAccountType);
	        
	        Map<String, Object> resultMap = new HashMap<>();
	        if (sysParameter != null) {
	        	resultMap.put("oIdPartner", sysParameter.getParamValue2());
	        	resultMap.put("tdUserName", sysParameter.getParamValue3());
                resultMap.put("orgType", BusinessTypeEnum.getOrgTypeByValue(businessType));
			}
	        
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("系统异常：" + e.getMessage());
		}
	}
}
