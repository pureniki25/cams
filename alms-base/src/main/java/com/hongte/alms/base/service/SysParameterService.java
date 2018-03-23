package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * [系统参数表] 服务类
 * </p>
 *
 * @author 黄咏康
 * @since 2018-02-01
 */
public interface SysParameterService extends BaseService<SysParameter> {

    //根据参数类型枚举查找所有参数列表
    List<SysParameter> selectByParamType(SysParameterTypeEnums enums);

    /**
     * 根据参数类型枚举查找到有效的参数列表
     * @param enums
     * @return
     */
    List<SysParameter> selectActiveByParamType(SysParameterTypeEnums enums);

    /**
     * 根据参数类型枚举和
     * @param enums
     * @param paramName
     * @return
     */
    List<SysParameter> selectByPtypeAndPname(SysParameterTypeEnums enums,String paramName);

    SysParameter seleByParamTypeAndvalue(SysParameterTypeEnums enums,String paramVal);


    Map<String,SysParameter> selectParameterMap(SysParameterTypeEnums enums);

}
