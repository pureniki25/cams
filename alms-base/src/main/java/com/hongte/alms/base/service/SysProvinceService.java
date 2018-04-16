package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysProvince;
import com.hongte.alms.base.vo.module.AreaProvinceItemVo;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * [省份信息表] 服务类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-26
 */
public interface SysProvinceService extends BaseService<SysProvince> {

    /**
     * 取得省市县信息
     * @return
     */
    public List<AreaProvinceItemVo> getAreaInfo();

}
