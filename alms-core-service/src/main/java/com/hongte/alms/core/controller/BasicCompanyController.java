package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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

}

