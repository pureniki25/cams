package com.hongte.alms.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 五级分类设置业务类型-类别关系表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-20
 */
public interface FiveLevelClassifyMapper extends SuperMapper<FiveLevelClassify> {

	List<FiveLevelClassify> queryDataByCondition(Map<String, Object> paramMap);

	Integer queryMayBeUsed(@Param(value = "businessType") String businessType,
			@Param(value = "className") String className);
}
