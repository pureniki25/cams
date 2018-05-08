package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.base.entity.FiveLevelClassifyCondition;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyConditionVO;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyVO;

public interface BusinessParameterService {
	/**
	 * 获取五级分类设置信息列表
	 * @return
	 */
	Page<FiveLevelClassify> queryFiveLevelClassifys(Integer page, Integer limit);
	
	void saveFiveLevelClassify(FiveLevelClassifyVO param);
	
	void updateFiveLevelClassify(FiveLevelClassifyVO param);
	
	List<FiveLevelClassifyConditionVO> queryFiveLevelClassifyCondition(String className, String businessType);
	
	void saveConditionForClassify(Map<String, Object> paramMap);
	
	void updateConditionForClassify(Map<String, Object> paramMap);
	
	FiveLevelClassifyConditionVO queryConditionForClassify(String className, String businessType, String subClassName);
	
	void deleteConditionParamModal(FiveLevelClassifyCondition condition);
	
	String fiveLevelClassifyForBusiness(ClassifyConditionVO classifyConditionVO);
	
	Integer queryMayBeUsed(String businessType, String className);
	
	void deleteFiveLevelClassify(Map<String, Object> paramMap);
	
}
