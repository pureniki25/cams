package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 五级分类设置业务类型-类别关系表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-20
 */
public interface FiveLevelClassifyService extends BaseService<FiveLevelClassify> {
	
	List<FiveLevelClassify> queryDataByCondition(Map<String, Object> paramMap);
	
	Integer queryMayBeUsed(String businessType, String className);
}
