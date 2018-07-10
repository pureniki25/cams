package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.hongte.alms.base.entity.SysProvince;
import com.hongte.alms.base.service.SysProvinceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * [省份信息表] 前端控制器
 * </p>
 *
 * @author 张贵宏
 * @since 2018-7-10
 */
@RestController
@RequestMapping("/sysProvince")
@Api(description = "省份管理控制器")
public class SysProvinceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysProvinceController.class);

    @Autowired
    @Qualifier("SysProvinceService")
    private SysProvinceService sysProvinceService;

    /**
     * 查询所有省份
     * @return
     */
    @ApiOperation(value = "查询所有省份")
    @RequestMapping("/findAll")
    public List<SysProvince> findAll(){
        List<SysProvince> provinces = Lists.newArrayList();
        try {
            provinces =  sysProvinceService.selectList(new EntityWrapper<SysProvince>().orderBy("name",true));
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return provinces;
    }

}

