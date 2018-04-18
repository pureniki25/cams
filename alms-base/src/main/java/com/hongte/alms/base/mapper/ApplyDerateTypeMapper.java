package com.hongte.alms.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 申请减免项目类型表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-08
 */
public interface ApplyDerateTypeMapper extends SuperMapper<ApplyDerateType> {
    ApplyTypeVo getApplyTypeVo(@Param(value="processId") String processId);
}
