package com.hongte.alms.core.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.collection.entity.CollectionPersonSetDetail;
import com.hongte.alms.base.collection.entity.CollectionTimeSet;
import com.hongte.alms.base.collection.service.CollectionPersonSetDetailService;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.collection.service.CollectionTimeSetService;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysRoleEnums;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.BatchSavePersonReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.base.vo.module.CollectionStrategySinglePersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionTimeSetVO;
import com.hongte.alms.base.vo.module.doc.BasicCompanyVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.service.CollectionStrategyPersonService;
import com.ht.ussp.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 催收策略
 * @author dengzhiming
 * @date 2018/3/5 16:04
 */
@RestController
@RequestMapping("/collectionStrategy")
@Api(tags = "CollectionStrategyController", description = "催收设置相关接口")
public class CollectionStrategyController {
    private Logger logger = LoggerFactory.getLogger(CollectionStrategyController.class);

    @Autowired
    private CollectionStrategyPersonService collectionStrategyPersonService;

    @Autowired
    @Qualifier("CollectionPersonSetService")
    private CollectionPersonSetService collectionPersonSettingService;

    @Autowired
    @Qualifier("CollectionTimeSetService")
    private CollectionTimeSetService collectionTimeSetService;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("CollectionPersonSetDetailService")
    private CollectionPersonSetDetailService collectionPersonSetDetailService;

    @Autowired
    @Qualifier("SysUserService")
    private SysUserService sysUserService;

    @Autowired
    @Qualifier("BasicBusinessTypeService")
    private BasicBusinessTypeService basicBusinessTypeService;


    @ApiOperation(value="获取催收人员设置列表页")
    @GetMapping("getCollectionUserSettingList")
    @ResponseBody
    public PageResult<List<CollectionStrategyPersonSettingVo>> getCollectionUserSettingList(CollectionStrategyPersonSettingReq req)
    {
        try {
            if(StringUtils.isNotBlank(req.getUserName())){
               req.setUserNames(req.getUserName().split("/"));
            }
            Page<CollectionStrategyPersonSettingVo> personSettingPages=collectionPersonSettingService.getCollectionPersonSettingList(req);
            return PageResult.success(personSettingPages.getRecords(),personSettingPages.getTotal());
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
            return PageResult.error(500,ex.getMessage());
        }
    }

    @ApiOperation(value="获取区域信息")
    @GetMapping("/company/getCompanyAreaList")
    @ResponseBody
    public Result<Map<String,JSONArray>> getCompanyAreaList(@RequestParam("id") String id) {
        int type ;
        if("1".equals(id)){//区域
            type = AreaLevel.AREA_LEVEL.getKey();
        }else {//分公司
            type = AreaLevel.COMPANY_LEVEL.getKey();
        }
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",type));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));

        return Result.success(retMap);
    }

    @ApiOperation(value="获取清算人员")
    @GetMapping("/company/getClearingPersons")
    @ResponseBody
    public Result<Map<String,JSONArray>> getClearingPersons (){
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
        List<SysUser> sysUser1List = sysUserService.selectUsersByRole(SysRoleEnums.HD_LIQ_COMMISSIONER.getKey());
        List<SysUser> sysUser2List = sysUserService.selectUsersByRole(SysRoleEnums.HD_ASSET_COMMISSIONE.getKey());

        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.AREA_LEVEL.getKey()));

        List<BasicCompany> companyList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));

        //业务类型
        List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
        BasicBusinessType basicBusinessType = new BasicBusinessType();
        basicBusinessType.setBusinessTypeId(0);
        basicBusinessType.setBusinessTypeName("全部");
        btype_list.add(basicBusinessType);
        retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));


        retMap.put("sysUser1List", (JSONArray) JSON.toJSON(sysUser1List, JsonUtil.getMapping()));
        retMap.put("sysUser2List", (JSONArray) JSON.toJSON(sysUser2List, JsonUtil.getMapping()));
        retMap.put("areaList", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));
        retMap.put("companyList", (JSONArray) JSON.toJSON(companyList, JsonUtil.getMapping()));
        return Result.success(retMap);
    }

    @ApiOperation(value="保存清算人员")
    @PostMapping("/saveSingleCompanyCollectionUserSetting")
    @ResponseBody
    public Result saveSingleCompanyCollectionUserSetting(@RequestBody CollectionStrategySinglePersonSettingReq req, @RequestHeader HttpHeaders headers){

        return collectionStrategyPersonService.saveStrategyPerson(req,headers);

    }

    @ApiOperation(value="删除清算人员配置")
    @PostMapping("/deletePersonSet")
    @ResponseBody
    public Result deletePersonSet( String id){
        return collectionStrategyPersonService.deleteColPersonSet(id);
    }


    @ApiOperation(value="查询催收策略时间设置列表")
    @GetMapping("/getCollectionTimeSettingList")
    @ResponseBody
    public PageResult<List<CollectionTimeSetVO>> getCollectionTimeSettingList(){
    	   Page<CollectionTimeSet> page = new Page<>();
           Page<CollectionTimeSetVO> pageVO = new Page<>();
        try {
     
        page = collectionTimeSetService.selectPage(page);
        page.getRecords();
        List<CollectionTimeSetVO> list=new ArrayList();
        CollectionTimeSetVO vo=null;
        for(int i=0;i<page.getRecords().size();i++) {
       
			vo =  ClassCopyUtil.copyObject(page.getRecords().get(i),CollectionTimeSetVO.class);
		 	list.add(vo);
	
       
        }
        pageVO.setRecords(list);
    	} catch (Exception e) {
			logger.error("查询出错"+e.getMessage());
		}
		return PageResult.success(pageVO.getRecords(),pageVO.getTotal());
 
    }

    @ApiOperation(value="保存催收时间策略设置")
    @RequestMapping("/saveTimeSet")
    @ResponseBody
    public Result saveTimeSet(@RequestBody CollectionTimeSet set,@RequestHeader HttpHeaders headers){
        System.out.println(set);
        System.out.println(DateUtil.formatDate("yyyy-MM-dd",set.getStartTime()));
        collectionTimeSetService.updateById(set);
        return Result.success();
    }

    @ApiOperation(value="获取片区信息数")
    @RequestMapping("/getAreaList")
    @ResponseBody
    public Result getAreaList(){

        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
        List<BasicCompanyVo> voList = new ArrayList<>();
        for (BasicCompany basicCompany:area_list) {
            BasicCompanyVo vo = new BasicCompanyVo();
            BeanUtils.copyProperties(basicCompany,vo);
            vo.setTitle(basicCompany.getAreaName());
            voList.add(vo);
        }
        return Result.success(voList);
    }

    @ApiOperation(value="获取分公司")
    @RequestMapping("/getCompanyList")
    @ResponseBody
    public Result getCompanyList(@RequestParam("areaPid") String areaPid){
        //分公司
        List<BasicCompany> companyList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()).eq("area_pid",areaPid));
        List<String> companyNames = new ArrayList<>();
        for (BasicCompany basicCompany:companyList) {
            companyNames.add(basicCompany.getAreaName());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("allcompanys",companyList);
        map.put("allcompanyNames",companyNames);

        return Result.success(companyList);
    }



}
