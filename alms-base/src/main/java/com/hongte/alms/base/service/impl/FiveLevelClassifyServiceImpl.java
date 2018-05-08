package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.base.mapper.FiveLevelClassifyMapper;
import com.hongte.alms.base.service.FiveLevelClassifyService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 五级分类设置信息表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-15
 */
@Service("FiveLevelClassifyService")
public class FiveLevelClassifyServiceImpl extends BaseServiceImpl<FiveLevelClassifyMapper, FiveLevelClassify> implements FiveLevelClassifyService {
	
	@Autowired
	private FiveLevelClassifyMapper fiveLevelClassifyMapper;
	
	@Override
	public List<FiveLevelClassify> queryDataByCondition(Map<String, Object> paramMap) {
		return fiveLevelClassifyMapper.queryDataByCondition(paramMap);
	}
	
	@Override
	public Integer queryMayBeUsed(String businessType, String className) {
		return fiveLevelClassifyMapper.queryMayBeUsed(businessType, className);
	}

}
