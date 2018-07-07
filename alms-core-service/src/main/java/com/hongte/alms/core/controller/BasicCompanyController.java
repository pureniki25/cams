package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 公司区域部分小组基础信息表控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-24
 */
@RestController
@RequestMapping("/basicCompany")
@Api(tags = "BasicCompanyController", description = "公司区域信息相关接口")
public class BasicCompanyController {

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;


    /**
     *
     * @param areaLavel  对应枚举 AreaLevel 里的name值
     * @return
     */
    @ApiOperation(value="根据区域级别查找区域/公司/部门/小组信息")
    @GetMapping("getListByAreaLavel")
    public Result<List<BasicCompany>> getListByAreaLavel(@RequestParam("areaLavel")String areaLavel) {

        Integer level = AreaLevel.keyOf(areaLavel);

        List<BasicCompany> list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",level));
        return Result.success(list);

    }


    /**
     * 获取所有分公司信息
     *
     * @return
     * @author 张贵宏
     */
    @ApiOperation(value = "获取所有分公司信息")
    @RequestMapping("findAllBranchCompany")
    public Result<List<BasicCompany>> findAllBranchCompany() {
        return Result.success(basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey())));
    }

    /**
     * 获取所有区域信息
     *
     * @return
     * @author 张贵宏
     */
    @ApiOperation(value = "获取所有区域信息")
    @RequestMapping("findAllAreaCompany")
    public Result<List<BasicCompany>> findAllAreaCompany() {
        return Result.success(basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey())));
    }
}

