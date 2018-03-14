package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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

    @ApiOperation(value="获取催收人员设置列表页")
    @GetMapping("getCollectionUserSettingList")
    @ResponseBody
    public PageResult<List<CollectionStrategyPersonSettingVo>> getCollectionUserSettingList(CollectionStrategyPersonSettingReq req)
    {
        try {
            if(StringUtils.isNotBlank(req.getUserName())){
               List<String> names =  Arrays.asList(req.getUserName().split(","));
               req.setUserNames(names);
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
}
