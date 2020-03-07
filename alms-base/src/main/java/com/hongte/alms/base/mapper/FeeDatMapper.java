package com.hongte.alms.base.mapper;

import java.util.List;

import com.hongte.alms.base.entity.FeeDat;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 费用表 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2019-03-02
 */
public interface FeeDatMapper extends SuperMapper<FeeDat> {
	int insertForeach(List<FeeDat> list);
}
