package com.hongte.alms.finance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hongte.alms.base.feignClient.MsgRemote;
import com.hongte.alms.base.feignClient.dto.MsgRequestDto;
import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysCountyService;

import com.hongte.alms.base.vo.module.AreaProvinceItemVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.service.NiWoRepayPlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/16
 */
@RestController
@RequestMapping("/test")
@Api(tags = "testController", description = "测试财务模块工程")
public class testController {
    private Logger logger = LoggerFactory.getLogger(testController.class);

//    @Autowired
//    @Qualifier("SysProvinceService")
//    private SysProvinceService provinceService;

    @Autowired
    @Qualifier("SysCityService")
    private SysCityService cityService;

    @Autowired
    @Qualifier("SysCountyService")
    private SysCountyService countyService;
    
    @Autowired
    @Qualifier("NiWoRepayPlanService")
    NiWoRepayPlanService  searchNiWoRepayPlanService;
    
    @Autowired
    MsgRemote msgRemote;

    @ApiOperation(value="获取区域信息")
    @GetMapping("getArea")
    public Result<List<AreaProvinceItemVo>> queryApplyOrder()
    {
//        try {
//            List<AreaProvinceItemVo> provinceItemList = provinceService.getAreaInfo();
//            return Result.success(provinceItemList);
//        }
//        catch(Exception ex)
//        {
//            logger.error(ex.getMessage());
//            return Result.error("500",ex.getMessage());
//        }

        return null;
    }
    
    
    @ApiOperation(value="获你我金融标的还款计划")
    @GetMapping("getNiWoRepayPlan")
    public void getNiWoRepayPlan(@RequestParam("requesetNo") String requesetNo)
    {
     	searchNiWoRepayPlanService.sycNiWoRepayPlan(requesetNo,null);

    }
    
    
    @ApiOperation(value="测试短信")
    @GetMapping("testMsg")
    public void testMsg()
    {
    
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("贷后你我金融扣款成功提示");
		dto.setMsgModelId(Long.valueOf("201806260001"));
		dto.setMsgTo("17665237459");
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", "测试");
		data.put("date", "2018年6月1日");
		data.put("borrowAmount", 3500);
		data.put("tailCardNum", "0124");
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);

    }

}
