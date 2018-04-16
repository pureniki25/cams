package com.hongte.alms.finance.controller;


import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysCountyService;
import com.hongte.alms.base.service.SysProvinceService;

import com.hongte.alms.base.vo.module.AreaProvinceItemVo;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.service.SysProvinceService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zengkun
 * @since 2018/4/16
 */
@RestController
@RequestMapping("/test")
@Api(tags = "testController", description = "测试财务模块工程")
public class testController {
    private Logger logger = LoggerFactory.getLogger(testController.class);

    @Autowired
    @Qualifier("SysProvinceService")
    private SysProvinceService provinceService;

    @Autowired
    @Qualifier("SysCityService")
    private SysCityService cityService;

    @Autowired
    @Qualifier("SysCountyService")
    private SysCountyService countyService;

    @ApiOperation(value="获取区域信息")
    @GetMapping("getArea")
    public Result<List<AreaProvinceItemVo>> getArea()
    {



        try {
            List<AreaProvinceItemVo> provinceItemList = provinceService.getAreaInfo();
//            List<AreaProvinceItemVo> provinceItemList = new ArrayList<AreaProvinceItemVo>();
//            List<SysProvince> allProvinceList = provinceService.selectList(new EntityWrapper<SysProvince>().orderBy("id"));
//            List<SysCity> allCityList = cityService.selectList(new EntityWrapper<SysCity>().orderBy("id"));
//            List<SysCounty> allCountyList = countyService.selectList(new EntityWrapper<SysCounty> ().orderBy("id"));
//            for (SysProvince province : allProvinceList) {
//                List<AreaCityItemVo> cityItemList = new ArrayList<AreaCityItemVo>();
//                List<SysCity> cityList=allCityList.stream().filter(p->p.getProvinceId().equals(province.getId())).sorted(Comparator.comparing(SysCity::getId)).collect(Collectors.toList());
//                for (SysCity city : cityList) {
//                    List<AreaCountyItemVo> countyItemList = new ArrayList<AreaCountyItemVo>();
//                    List<SysCounty> countyList = allCountyList.stream().filter(p->p.getCityId().equals(city.getId())).sorted(Comparator.comparing(SysCounty::getId)).collect(Collectors.toList());
//                    for (SysCounty county : countyList) {
//                        AreaCountyItemVo countyItem = new AreaCountyItemVo();
//                        countyItem.setValue(county.getName());
//                        countyItem.setLabel(county.getName());
//                        countyItemList.add(countyItem);
//                    }
//                    AreaCityItemVo cityItem = new AreaCityItemVo();
//                    cityItem.setValue(city.getName());
//                    cityItem.setLabel(city.getName());
//                    cityItem.setChildren(countyItemList);
//                    cityItemList.add(cityItem);
//                }
//                AreaProvinceItemVo provinceItem = new AreaProvinceItemVo();
//                provinceItem.setValue(province.getName());
//                provinceItem.setLabel(province.getName());
//                provinceItem.setChildren(cityItemList);
//                provinceItemList.add(provinceItem);
//            }
            return Result.success(provinceItemList);
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
            return Result.error("500",ex.getMessage());
        }
    }

}
