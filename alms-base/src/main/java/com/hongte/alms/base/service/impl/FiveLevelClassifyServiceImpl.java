package com.hongte.alms.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ServiceException;
import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.FiveLevelClassifyMapper;
import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.service.FiveLevelClassifyService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

/**
 * <p>
 * 五级分类设置信息表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-15
 */
@Service("FiveLevelClassifyService")
public class FiveLevelClassifyServiceImpl extends BaseServiceImpl<FiveLevelClassifyMapper, FiveLevelClassify>
		implements FiveLevelClassifyService {
	
	private static final Logger LOG = LoggerFactory.getLogger(FiveLevelClassifyServiceImpl.class);

	@Autowired
	private FiveLevelClassifyMapper fiveLevelClassifyMapper;

	@Autowired
	private BasicBusinessMapper basicBusinessMapper;
	
	@Autowired
	@Qualifier("BusinessParameterService")
	private BusinessParameterService businessParameterService;
	
	@Autowired
	@Qualifier("FiveLevelClassifyBusinessChangeLogService")
	private FiveLevelClassifyBusinessChangeLogService fiveLevelClassifyBusinessChangeLogService;

	@Override
	public List<FiveLevelClassify> queryDataByCondition(Map<String, Object> paramMap) {
		return fiveLevelClassifyMapper.queryDataByCondition(paramMap);
	}

	@Override
	public Integer queryMayBeUsed(String businessType, String className) {
		return fiveLevelClassifyMapper.queryMayBeUsed(businessType, className);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void fiveLevelClassifySchedule() {
		try {
			long start = System.currentTimeMillis();
			List<String> businessIds = basicBusinessMapper.queryFiveLevelClassify();
			List<FiveLevelClassifyBusinessChangeLog> changeLogs = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(businessIds)) {
				fiveLevelClassifyBusinessChangeLogService.updateValidStatusByBusinessId(businessIds);
				for (String businessId : businessIds) {
					LOG.info("循环开始：{}", businessIds.indexOf(businessId));
					ClassifyConditionVO classifyConditionVO = new ClassifyConditionVO();
					classifyConditionVO.setBusinessId(businessId);
					String className = businessParameterService.fiveLevelClassifyForBusiness(classifyConditionVO);
					FiveLevelClassifyBusinessChangeLog changeLog = new FiveLevelClassifyBusinessChangeLog();
					changeLog.setClassName(className);
					changeLog.setOpSourseType("1");
					changeLog.setOpTime(new Date());
					changeLog.setOrigBusinessId(businessId);
					changeLog.setParamJson(JSON.toJSONString(classifyConditionVO));
					changeLogs.add(changeLog);
				}
				fiveLevelClassifyBusinessChangeLogService.insertBatch(changeLogs);
			}
			long end = System.currentTimeMillis();
			LOG.info("五级分类设置结束，耗时：{}", end - start);
		} catch (Exception e) {
			LOG.error("五级分类设置调度执行失败", e);
			throw new ServiceException(e);
		}
	}

}
