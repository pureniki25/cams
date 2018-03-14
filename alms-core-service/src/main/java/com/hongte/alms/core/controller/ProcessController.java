package com.hongte.alms.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.vo.ProcessReq;
import com.hongte.alms.base.process.vo.ProcessStatusVo;
import com.hongte.alms.base.process.vo.ProcessVo;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/2/11
 * 审批流程的交互控制器
 */
@RestController
@RequestMapping("/processController")
public class ProcessController {

    Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    @Qualifier("ProcessService")
    ProcessService  processService;

    @Autowired
    @Qualifier("ProcessTypeService")
    ProcessTypeService  processTypeService;

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;


    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @ApiOperation(value = "获得待我审批的流程分页数据")
    @GetMapping("/selectProcessWaitToApproveVoPage")
    @ResponseBody
    public PageResult<List<ProcessVo>> selectProcessWaitToApproveVoPage(
            @ModelAttribute("reqPageeType") String reqPageeType,
            @ModelAttribute ProcessReq req){

        try {
            req.setReqPageeType(reqPageeType);
//            req.setWaitTpApproveFlage(true);


//            req.setCurrentUserId("quyudaihouzhuguan1");
            req.setCurrentUserId(loginUserInfoHelper.getUserId());
            Page<ProcessVo>  page = processService.selectProcessVoPage(req);
            return PageResult.success(page.getRecords(),page.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return PageResult.error(500,e.getMessage());
        }


//        return null;

    }

    /**
     *
     *
     * @return
     */
    @ApiOperation(value="取得流程管理界面下拉选项框数据")
    @GetMapping("getProcessVoPageSelectsData")
    public Result<Map<String,JSONArray>> getProcessVoPageSelectsData() {

        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

        //流程类型
        List<ProcessType> types = processTypeService.selectList(new EntityWrapper<ProcessType>());
        retMap.put("processTypes", (JSONArray) JSON.toJSON(types, JsonUtil.getMapping()));

        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        retMap.put("companyList",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));

        //流程状态
        List<ProcessStatusVo>  status = ProcessStatusEnums.getProcessStatusList();
        retMap.put("processStatusList",(JSONArray) JSON.toJSON(status,JsonUtil.getMapping()));
//        //区域
//        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
//        retMap.put("area", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));

//        //业务类型
//        List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
//        retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));

        return Result.success(retMap);
    }

//    getProcessVoPageSelectsData

}
