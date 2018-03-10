package com.hongte.alms.open.controller;


import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.vo.module.DepartmentBankVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.base.vo.module.doc.DocUploadRequest;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 * 财务款项池表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-02-28
 */
@RestController
@RequestMapping(value = "/repayment", method = RequestMethod.POST)
@Api(value = "moenyPool", description = "贷后系统 还款登记 对外接口")
public class MoneyPoolController {
	private Logger logger = LoggerFactory.getLogger(MoneyPoolController.class);
	
	@Autowired
    @Qualifier("MoneyPoolService")
    MoneyPoolService moneyPoolService;
	
	@Autowired
    @Qualifier("DepartmentBankService")
	DepartmentBankService departmentBankService ;
	
	@Autowired
    LoginUserInfoHelper loginUserInfoHelper;
	
	@Autowired
	@Qualifier("DocService")
	DocService docService ;
	
    @PostMapping("/update")
    @ResponseBody
	public Result update(String jsonData) {
		ResponseEncryptData resp = JSON.parseObject(jsonData, ResponseEncryptData.class);
		String decryptStr = new DESC().Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		System.out.println(JSON.toJSONString(respData));
		return Result.success(jsonData);
	}
	
    @PostMapping("/delete")
    @ResponseBody
	public Result delete(String jsonData) {
		ResponseEncryptData resp = JSON.parseObject(jsonData, ResponseEncryptData.class);
		String decryptStr = new DESC().Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		System.out.println(JSON.toJSONString(respData));
		return Result.success(jsonData);
	}
	
}

