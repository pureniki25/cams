package com.hongte.alms.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.entity.WithholdingPlatform;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingPlatformService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.withhold.WithholdChannelListReq;
import com.hongte.alms.base.vo.withhold.WithholdChannelListVo;
import com.hongte.alms.base.vo.withhold.WithholdChannelOptReq;
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

    @ApiOperation(value = "新增/编辑代扣渠道")
    @RequestMapping("/addOrEditWithholdChannel")
    public Result addOrEditWithholdChannel(WithholdChannelOptReq withholdChannelOptReq) {
        LOGGER.info("====>>>>>新增/编辑代扣渠道开始[{}]", withholdChannelOptReq);
        Result result = null;
        try {
            withholdingChannelService.addOrEditWithholdChannel(withholdChannelOptReq);

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

}
