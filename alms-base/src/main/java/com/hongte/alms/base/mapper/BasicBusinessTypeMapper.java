package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务类型表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
public interface BasicBusinessTypeMapper extends SuperMapper<BasicBusinessType> {
	/**
	 * 根据businessId获取businessTypeName
	 * @param businessId
	 * @return
	 */
	String queryBusinessTypeNameByBusinessId(String businessId);
}
