package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysCity;
import com.hongte.alms.base.entity.SysCounty;
import com.hongte.alms.base.entity.SysProvince;
import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysCountyService;
import com.hongte.alms.base.service.SysProvinceService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.core.vo.modules.AreaCityItemVo;
import com.hongte.alms.core.vo.modules.AreaCountyItemVo;
import com.hongte.alms.core.vo.modules.AreaProvinceItemVo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengzhiming
 * @date 2018/2/26 9:58
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    private Logger logger = LoggerFactory.getLogger(AreaController.class);

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
            List<AreaProvinceItemVo> provinceItemList = new ArrayList<AreaProvinceItemVo>();
            List<SysProvince> provinceList = provinceService.selectList(new EntityWrapper<SysProvince>().orderBy("id"));
            for (SysProvince province : provinceList) {
                List<AreaCityItemVo> cityItemList = new ArrayList<AreaCityItemVo>();
                List<SysCity> cityList = cityService.selectList(new EntityWrapper<SysCity>().eq("province_id", province.getId()).orderBy("id"));
                for (SysCity city : cityList) {
                    List<AreaCountyItemVo> countyItemList = new ArrayList<AreaCountyItemVo>();
                    List<SysCounty> countyList = countyService.selectList(new EntityWrapper().eq("city_id", city.getId()).orderBy("id"));
                    for (SysCounty county : countyList) {
                        AreaCountyItemVo countyItem = new AreaCountyItemVo();
                        countyItem.setValue(county.getName());
                        countyItem.setLabel(county.getName());
                        countyItemList.add(countyItem);
                    }
                    AreaCityItemVo cityItem = new AreaCityItemVo();
                    cityItem.setValue(city.getName());
                    cityItem.setLabel(city.getName());
                    cityItem.setChildren(countyItemList);
                    cityItemList.add(cityItem);
                }
                AreaProvinceItemVo provinceItem = new AreaProvinceItemVo();
                provinceItem.setValue(province.getName());
                provinceItem.setLabel(province.getName());
                provinceItem.setChildren(cityItemList);
                provinceItemList.add(provinceItem);
            }
            return Result.success(provinceItemList);
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
            return Result.error("500",ex.getMessage());
        }
    }

}
