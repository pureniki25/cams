package com.hongte.alms.base.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.mapper.FiveLevelClassifyBusinessChangeLogMapper;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;

/**
 * <p>
 * 业务类别变更记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-20
 */
@Service("FiveLevelClassifyBusinessChangeLogService")
public class FiveLevelClassifyBusinessChangeLogServiceImpl
		extends BaseServiceImpl<FiveLevelClassifyBusinessChangeLogMapper, FiveLevelClassifyBusinessChangeLog>
		implements FiveLevelClassifyBusinessChangeLogService {
	
	@Autowired
	private FiveLevelClassifyBusinessChangeLogMapper fiveLevelClassifyBusinessChangeLogMapper;

	@Override
	public void businessChangeLog(ClassifyConditionVO classifyConditionVO) {
		
		try {
			FiveLevelClassifyBusinessChangeLog changeLog = new FiveLevelClassifyBusinessChangeLog();

			String businessId = classifyConditionVO.getBusinessId();
			List<FiveLevelClassifyBusinessChangeLog> oldChangeLogs = this
					.selectList(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>().eq("orig_business_id", businessId)
							.eq("valid_status", "1"));

			if (CollectionUtils.isNotEmpty(oldChangeLogs)) {
				for (FiveLevelClassifyBusinessChangeLog oldChangeLog : oldChangeLogs) {
					oldChangeLog.setValidStatus("0");
					this.updateById(oldChangeLog);
				}
			}

			changeLog.setOrigBusinessId(businessId);
			changeLog.setOpSourseType(classifyConditionVO.getOpSourse());
			changeLog.setOpSourseId(classifyConditionVO.getOpSourseId());
			changeLog.setOpUserId(classifyConditionVO.getOpUserId());
			changeLog.setOpUsername(classifyConditionVO.getOpUsername());
			changeLog.setOpTime(new Date());
			changeLog.setValidStatus("1");
			changeLog.setParamJson(JSON.toJSONString(classifyConditionVO));

			StringBuilder borrowerConditionDescBuilder = new StringBuilder();
			List<String> mainBorrowerConditions = classifyConditionVO.getMainBorrowerConditions();
			if (CollectionUtils.isNotEmpty(mainBorrowerConditions)) {
				for (String string : mainBorrowerConditions) {
					if (mainBorrowerConditions.indexOf(string) == (mainBorrowerConditions.size() - 1)) {
						borrowerConditionDescBuilder.append(string);
					} else {
						borrowerConditionDescBuilder.append((string + Constant.FIVE_LEVEL_CLASSIFY_SPLIT));
					}
				}
			}
			changeLog.setBorrowerConditionDesc(borrowerConditionDescBuilder.toString());

			StringBuilder guaranteeConditionDescBuilder = new StringBuilder();
			List<String> guaranteeConditions = classifyConditionVO.getGuaranteeConditions();
			if (CollectionUtils.isNotEmpty(guaranteeConditions)) {
				for (String string : guaranteeConditions) {
					if (guaranteeConditions.indexOf(string) == (guaranteeConditions.size() - 1)) {
						guaranteeConditionDescBuilder.append(string);
					} else {
						guaranteeConditionDescBuilder.append((string + Constant.FIVE_LEVEL_CLASSIFY_SPLIT));
					}
				}
			}
			changeLog.setGuaranteeConditionDesc(guaranteeConditionDescBuilder.toString());

			changeLog.setClassName(classifyConditionVO.getClassName());
			this.insert(changeLog);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateValidStatusByBusinessId(List<String> businessIds) {
		fiveLevelClassifyBusinessChangeLogMapper.updateValidStatusByBusinessId(businessIds);
	}

}
