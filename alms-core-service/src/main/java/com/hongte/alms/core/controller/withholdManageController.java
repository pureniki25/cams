package com.hongte.alms.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.service.SysBankService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingPlatformService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.withhold.*;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/withholdManage")
@Api(tags = "withholdManageController", description = "代扣管理类接口")
public class withholdManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(withholdManageController.class);

    @Autowired
    @Qualifier("WithholdingChannelService")
    private WithholdingChannelService withholdingChannelService;

    @Autowired
    @Qualifier("WithholdingPlatformService")
    private WithholdingPlatformService withholdingPlatformService;

    @Autowired
    @Qualifier("SysBankLimitService")
    private SysBankLimitService sysBankLimitService;


    @Autowired
    @Qualifier("SysBankService")
    private SysBankService sysBankService;
    
  

    @ApiOperation(value = "新增/编辑代扣渠道")
    @RequestMapping("/addOrEditWithholdChannel")
    public Result addOrEditWithholdChannel(WithholdingChannel withholdingChannel) {
        LOGGER.info("====>>>>>新增/编辑代扣渠道开始[{}]", withholdingChannel);
        Result result = null;
        try {
            withholdingChannelService.addOrEditWithholdChannel(withholdingChannel);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>新增/编辑代扣渠道出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>新增/编辑代扣渠道出错{}", e);
        }
        LOGGER.info("====>>>>>新增/编辑代扣渠道结束");
        return result;
    }

    @ApiOperation(value = "获取单个代扣渠道信息")
    @RequestMapping("/getWithholdChannel")
    public Result getWithholdChannel(int channelId) {
        LOGGER.info("====>>>>>获取单个代扣渠道信息开始[{}]", channelId);
        Result result = null;
        try {
            WithholdingChannel withholdingChannel = withholdingChannelService.getWithholdChannel(channelId);

            result = Result.success(withholdingChannel);
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>获取单个代扣渠道信息出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>获取单个代扣渠道信息出错{}", e);
        }
        LOGGER.info("====>>>>>获取单个代扣渠道信息结束");
        return result;
    }


    @ApiOperation(value = "删除单个代扣渠道信息")
    @RequestMapping("/delWithholdChannel")
    public Result delWithholdChannel(int channelId) {
        LOGGER.info("====>>>>>删除单个代扣渠道信息开始[{}]", channelId);
        Result result = null;
        try {
             withholdingChannelService.delWithholdChannel(channelId);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>删除单个代扣渠道信息出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>删除单个代扣渠道信息出错{}", e);
        }
        LOGGER.info("====>>>>>删除单个代扣渠道信息结束");
        return result;
    }


    @ApiOperation(value = "获取代扣渠道列表")
    @GetMapping("/getWithholdChannelPageList")
    @ResponseBody
    public PageResult<List<WithholdChannelListVo>> getWithholdChannelPageList(WithholdChannelListReq withholdChannelListReq) {
        LOGGER.info("====>>>>>获取代扣渠道列表开始[{}]", JSON.toJSONString(withholdChannelListReq));
        PageResult pageResult = null;

        try {
            Page<WithholdChannelListVo> pages = withholdingChannelService.getWithholdChannelPageList(withholdChannelListReq);
            pageResult = PageResult.success(pages.getRecords(), pages.getTotal());


        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            pageResult = PageResult.error(500, "执行异常");
        }
        LOGGER.info("====>>>>>获取代扣渠道列表结束[{}]", JSON.toJSONString(pageResult));
        return pageResult;
    }

    @ApiOperation(value = "获取渠道选项列表")
    @GetMapping("/getWithholdChannelType")
    @ResponseBody
    public Result<Map<String, JSONArray>> getWithholdChannelType() {
        LOGGER.info("====>>>>>获取渠道选项列表开始");
        Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();

        //渠道列表
        List<WithholdingPlatform> platformList = withholdingPlatformService.selectList(new EntityWrapper<WithholdingPlatform>().eq("platform_status", 1));

        retMap.put("platformType", (JSONArray) JSON.toJSON(platformList, JsonUtil.getMapping()));


        LOGGER.info("====>>>>>获取渠道选项列表结束");
        return Result.success(retMap);
    }


    @ApiOperation(value = "获取代扣额度列表")
    @GetMapping("/getWithholdLimitPageList")
    @ResponseBody
    public PageResult<List<WithholdLimitListVo>> getWithholdLimitPageList(WithholdLimitListReq withholdLimitListReq) {
        LOGGER.info("====>>>>>获取代扣额度列表开始[{}]", JSON.toJSONString(withholdLimitListReq));
        PageResult pageResult = null;

        try {
            Page<WithholdLimitListVo> pages = sysBankLimitService.getWithholdLimitPageList(withholdLimitListReq);
            pageResult = PageResult.success(pages.getRecords(), pages.getTotal());


        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            pageResult = PageResult.error(500, "执行异常");
        }
        LOGGER.info("====>>>>>获取代扣额度列表结束[{}]", JSON.toJSONString(pageResult));
        return pageResult;
    }

    @ApiOperation(value = "获取额度选项列表")
    @GetMapping("/getWithholdLimitType")
    @ResponseBody
    public Result<Map<String, JSONArray>> getWithholdLimitType() {
        LOGGER.info("====>>>>>获取额度选项列表开始");
        Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();

        //渠道列表
        List<WithholdingPlatform> platformList = withholdingPlatformService.selectList(new EntityWrapper<WithholdingPlatform>().eq("platform_status", 1));

        retMap.put("platformType", (JSONArray) JSON.toJSON(platformList, JsonUtil.getMapping()));

        //渠道列表
        List<SysBank> sysBankList = sysBankService.selectList(new EntityWrapper<SysBank>());
        
        retMap.put("bankType", (JSONArray) JSON.toJSON(sysBankList, JsonUtil.getMapping()));
        
        //银行代扣子渠道列表
        List<WithholdingChannel> withholdingChannels = withholdingChannelService.selectList(new EntityWrapper<WithholdingChannel>().eq("channel_status", 1).eq("platform_id", PlatformEnum.YH_FORM.getValue()));

        retMap.put("subPlatformType", (JSONArray) JSON.toJSON(withholdingChannels, JsonUtil.getMapping()));


        LOGGER.info("====>>>>>获取额度选项列表结束");
        return Result.success(retMap);
    }

    @ApiOperation(value = "新增/编辑代扣额度信息")
    @RequestMapping("/addOrEditWithholdLimit")
    public Result addOrEditWithholdLimit(SysBankLimit sysBankLimit) {
        LOGGER.info("====>>>>>新增/编辑代扣额度信息开始[{}]", sysBankLimit);
        Result result = null;
        try { 
        	  if(!(sysBankLimit.getPlatformId()==PlatformEnum.YH_FORM.getValue())) {
              	sysBankLimit.setSubPlatformId("");
              }
            sysBankLimitService.addOrEditWithholdChannel(sysBankLimit);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>新增/编辑代扣额度信息出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>新增/编辑代扣额度信息出错{}", e);
        }
        LOGGER.info("====>>>>>新增/编辑代扣额度信息结束");
        return result;
    }

    @ApiOperation(value = "获取单个代扣额度信息")
    @RequestMapping("/getWithholdLimit")
    public Result getWithholdLimit(String limitId) {
        LOGGER.info("====>>>>>获取单个代扣额度信息开始[{}]", limitId);
        Result result = null;
        try {
            SysBankLimit sysBankLimit = sysBankLimitService.getWithholdLimit(limitId);

            result = Result.success(sysBankLimit);
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>获取单个代扣额度信息出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>获取单个代扣额度信息出错{}", e);
        }
        LOGGER.info("====>>>>>获取单个代扣额度信息结束");
        return result;
    }

    @ApiOperation(value = "更新单个代扣额度信息状态")
    @RequestMapping("/updateWithholdLimitStatus")
    public Result updateWithholdLimitStatus(String limitId, int status) {
        LOGGER.info("====>>>>>更新单个代扣额度信息状态开始[{}]", limitId);
        Result result = null;
        try {
            sysBankLimitService.updateWithholdLimitStatus(limitId, status);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>更新单个代扣额度信息状态出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>更新单个代扣额度信息状态出错{}", e);
        }
        LOGGER.info("====>>>>>更新单个代扣额度信息状态结束");
        return result;
    }

    @ApiOperation(value = "按照代扣渠道查询代扣额度接口，用于代扣服务")
    @RequestMapping("/getSysBankLimitByPlatformId")
    public Result getSysBankLimitByPlatformId(int platformId) {
        LOGGER.info("====>>>>>查询代扣额度开始[{}]", platformId);
        Result result = null;
        try {
            List<SysBankLimit> sysBankLimitList = sysBankLimitService.getSysBankLimitByPlatformId(platformId);
            result = Result.success(sysBankLimitList);
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>查询代扣额度出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "执行异常");
            LOGGER.error("====>>>>>查询代扣额度出错{}", e);
        }
        LOGGER.info("====>>>>>查询代扣额度结束");
        return result;
    }


}
