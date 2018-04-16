package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysCity;
import com.hongte.alms.base.entity.SysCounty;
import com.hongte.alms.base.entity.SysProvince;
import com.hongte.alms.base.mapper.SysProvinceMapper;
import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysCountyService;
import com.hongte.alms.base.service.SysProvinceService;
import com.hongte.alms.base.vo.module.AreaCityItemVo;
import com.hongte.alms.base.vo.module.AreaCountyItemVo;
import com.hongte.alms.base.vo.module.AreaProvinceItemVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * [省份信息表] 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-26
 */
@Service("SysProvinceService")
public class SysProvinceServiceImpl extends BaseServiceImpl<SysProvinceMapper, SysProvince> implements SysProvinceService {
//    @Autowired
//    @Qualifier("SysProvinceService")
//    private SysProvinceService provinceService;

    @Autowired
    @Qualifier("SysCityService")
    private SysCityService cityService;

    @Autowired
    @Qualifier("SysCountyService")
    private SysCountyService countyService;

   public  List<AreaProvinceItemVo>  getAreaInfo(){
       List<AreaProvinceItemVo> provinceItemList = new ArrayList<AreaProvinceItemVo>();
       List<SysProvince> allProvinceList = selectList(new EntityWrapper<SysProvince>().orderBy("id"));
       List<SysCity> allCityList = cityService.selectList(new EntityWrapper<SysCity>().orderBy("id"));
       List<SysCounty> allCountyList = countyService.selectList(new EntityWrapper<SysCounty> ().orderBy("id"));
       for (SysProvince province : allProvinceList) {
           List<AreaCityItemVo> cityItemList = new ArrayList<AreaCityItemVo>();
           List<SysCity> cityList=allCityList.stream().filter(p->p.getProvinceId().equals(province.getId())).sorted(Comparator.comparing(SysCity::getId)).collect(Collectors.toList());
           for (SysCity city : cityList) {
               List<AreaCountyItemVo> countyItemList = new ArrayList<AreaCountyItemVo>();
               List<SysCounty> countyList = allCountyList.stream().filter(p->p.getCityId().equals(city.getId())).sorted(Comparator.comparing(SysCounty::getId)).collect(Collectors.toList());
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
       return provinceItemList;
   }
}
