package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.hongte.alms.base.entity.SysCity;
import com.hongte.alms.base.entity.SysProvince;
import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysProvinceService;
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
 * [城市] 前端控制器
 * </p>
 *
 * @author 张贵宏
 * @since 2018-7-10
 */
@RestController
@RequestMapping("/sysCity")
public class SysCityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysCityController.class);

    @Autowired
    @Qualifier("SysCityService")
    private SysCityService sysCityService;

    @Autowired
    @Qualifier("SysProvinceService")
    private SysProvinceService sysProvinceService;

    /**
     * 查询所有城市
     * @return
     */
    @ApiOperation(value = "查询所有城市")
    @RequestMapping("/findAll")
    public List<SysCity> findAll(){
        List<SysCity> cities = Lists.newArrayList();
        try {
            cities =  sysCityService.selectList(new EntityWrapper<SysCity>().orderBy("name", true));
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return cities;
    }

    /**
     * 按省id查城市
     * @return
     */
    @ApiOperation(value = "按省id查城市")
    @RequestMapping("/findAllByProvinceId")
    public List<SysCity> findAllByProvinceId(String provinceId){
        List<SysCity> cities = Lists.newArrayList();
        try {
            cities =  sysCityService.selectList(new EntityWrapper<SysCity>().eq("province_id", provinceId).orderBy("name",true));
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return cities;
    }

    /**
     * 按省名查城市
     * @return
     */
    @ApiOperation(value = "按省名查城市")
    @RequestMapping("/findAllByProvinceName")
    public List<SysCity> findAllByProvinceName(String provinceName){
        List<SysCity> cities = Lists.newArrayList();
        try {
            SysProvince province = sysProvinceService.selectOne(new EntityWrapper<SysProvince>().eq("name",provinceName));
            if(province != null) {
                cities = sysCityService.selectList(new EntityWrapper<SysCity>().eq("province_id", province.getId()).orderBy("name", true));
            }
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return cities;
    }
}

