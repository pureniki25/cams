package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务类别变更记录表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-20
 */
public interface FiveLevelClassifyBusinessChangeLogMapper extends SuperMapper<FiveLevelClassifyBusinessChangeLog> {
	void updateValidStatusByBusinessId(@Param(value = "businessIds") List<String> businessIds);
}
