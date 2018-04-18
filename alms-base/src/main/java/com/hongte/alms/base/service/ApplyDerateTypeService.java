package com.hongte.alms.base.service;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 申请减免项目类型表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-08
 */
public interface ApplyDerateTypeService extends BaseService<ApplyDerateType> {
	 ApplyTypeVo getApplyTypeVo(@Param(value="processId") String processId);
}
