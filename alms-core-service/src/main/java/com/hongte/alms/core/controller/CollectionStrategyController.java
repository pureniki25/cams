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
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysRoleEnums;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.BatchSavePersonReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.base.vo.module.CollectionStrategySinglePersonSettingReq;
import com.hongte.alms.base.vo.module.doc.BasicCompanyVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
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
public class CollectionStrategyController {
    private Logger logger = LoggerFactory.getLogger(CollectionStrategyController.class);

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

    @ApiOperation(value="获取催收人员设置列表页")
    @GetMapping("getCollectionUserSettingList")
    @ResponseBody
    public PageResult<List<CollectionStrategyPersonSettingVo>> getCollectionUserSettingList(CollectionStrategyPersonSettingReq req)
    {
        try {
            if(StringUtils.isNotBlank(req.getUserName())){
               List<String> names =  Arrays.asList(req.getUserName().split(","));
//               req.setUserNames(names);
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

//    @ApiOperation(value="通过公司id获取清算人员")
//    @GetMapping("getSingleCompanyCollectionUserSettingByComopanyId")
//   public Result<SingleCompanyCollectionPersonSettingVo> getSingleCompanyCollectionUserSettingByComopanyId(String companyId)
//   {
//       try {
//           SingleCompanyCollectionPersonSettingVo settingVo = new SingleCompanyCollectionPersonSettingVo();
//           settingVo.setCompanyId(companyId);
//           CollectionPersonSet setting = collectionPersonSettingService.selectOne(new EntityWrapper<CollectionPersonSet>().eq("company_code", companyId));
//           if (setting.getCollect1Person() != null && setting.getCollect1Person() != "") {
//               settingVo.setCollection1UserIds(Arrays.asList(setting.getCollect1Person().split(",")));
//           } else {
//               settingVo.setCollection1UserIds(new ArrayList<String>());
//           }
//           if (setting.getCollect2Person() != null && setting.getCollect2Person() != "") {
//               settingVo.setCollection2UserIds(Arrays.asList(setting.getCollect1Person().split(",")));
//           } else {
//               settingVo.setCollection2UserIds(new ArrayList<String>());
//           }
//           return Result.success(settingVo);
//       }
//       catch(Exception ex)
//       {
//           logger.error(ex.getMessage());
//           return Result.error("500",ex.getMessage());
//       }
//   }

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
        List<SysUser> sysUserList = sysUserService.selectUsersByRole(SysRoleEnums.HD_LIQ_COMMISSIONER.getKey());

        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.AREA_LEVEL.getKey()));

        List<BasicCompany> companyList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));

        retMap.put("sysUser", (JSONArray) JSON.toJSON(sysUserList, JsonUtil.getMapping()));
        retMap.put("areaList", (JSONArray) JSON.toJSON(area_list, JsonUtil.getMapping()));
        retMap.put("companyList", (JSONArray) JSON.toJSON(companyList, JsonUtil.getMapping()));
        return Result.success(retMap);
    }

    @ApiOperation(value="保存清算人员")
    @PostMapping("/saveSingleCompanyCollectionUserSetting")
    @ResponseBody
    public Result saveSingleCompanyCollectionUserSetting(@RequestBody CollectionStrategySinglePersonSettingReq req, @RequestHeader HttpHeaders headers){
        System.out.println(req.getCollectionGroup1Users());
        List<String> collectionGroup1Users = new ArrayList<>();
        List<String> collectionGroup2Users = new ArrayList<>();
        List<String> companyList = new ArrayList<>();
        collectionGroup1Users = req.getCollectionGroup1Users();
        collectionGroup2Users = req.getCollectionGroup2Users();
        companyList = req.getCompanyId();


        for (String com:companyList) {
            CollectionPersonSet personSet = collectionPersonSettingService.selectOne(new EntityWrapper<CollectionPersonSet>().eq("company_code",req.getCompanyId()));
            if(personSet !=null){
                personSet.setUpdateTime(new Date());
                personSet.setUpdateUser("admin");
                personSet.setCreatUser("admin");
                collectionPersonSettingService.update(personSet,new EntityWrapper<CollectionPersonSet>().eq("col_person_id",personSet.getColPersonId()));
            }else {
                BasicCompany basicCompany = basicCompanyService.selectOne(new EntityWrapper<BasicCompany>().eq("area_id",com).eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
                personSet = new CollectionPersonSet();
                personSet.setAreaCode(basicCompany.getAreaPid());
                personSet.setCompanyCode(com);
                personSet.setCreateTime(new Date());
                personSet.setCreatUser("admin");
                collectionPersonSettingService.insert(personSet);
            }

            String colPersonId = personSet.getColPersonId();

            List<CollectionPersonSetDetail> personSetDetails = new ArrayList<>();

        /*
        构建第一组人员详情设置
         */
            for (String firstGroup:collectionGroup1Users ) {
                CollectionPersonSetDetail personSetDetail = new CollectionPersonSetDetail();
                personSetDetail.setColPersonId(colPersonId);
                personSetDetail.setTeam(1);
                personSetDetail.setUserId(firstGroup);
                personSetDetail.setCreateUser("admin");
                personSetDetail.setCreateTime(new Date());
                personSetDetails.add(personSetDetail);
            }

        /*
        构建第二组人员详情设置
         */
            for (String secendGroup:collectionGroup2Users ) {

                CollectionPersonSetDetail personSetDetail = new CollectionPersonSetDetail();
                personSetDetail.setColPersonId(colPersonId);
                personSetDetail.setTeam(2);
                personSetDetail.setUserId(secendGroup);
                personSetDetail.setCreateUser("admin");
                personSetDetail.setCreateTime(new Date());
                personSetDetails.add(personSetDetail);
            }

            collectionPersonSetDetailService.delete(new EntityWrapper<CollectionPersonSetDetail>().eq("col_person_id",colPersonId));
            collectionPersonSetDetailService.insertBatch(personSetDetails);
        }


        return Result.success();
    }


    @ApiOperation(value="查询催收策略时间设置列表")
    @GetMapping("/getCollectionTimeSettingList")
    @ResponseBody
    public PageResult<List<CollectionTimeSet>> getCollectionTimeSettingList(){

        Page<CollectionTimeSet> page = new Page<>();

        page = collectionTimeSetService.selectPage(page);
        return PageResult.success(page.getRecords(),page.getTotal());
    }

    @ApiOperation(value="保存催收时间策略设置")
    @RequestMapping("/saveTimeSet")
    @ResponseBody
    public Result saveTimeSet(@RequestBody CollectionTimeSet set,@RequestHeader HttpHeaders headers){
        System.out.println(set);

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
        return Result.success(companyList);
    }



}
