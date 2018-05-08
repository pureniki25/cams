package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.mapper.FiveLevelClassifyBusinessChangeLogMapper;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	LoginUserInfoHelper loginUserInfoHelper;

	@Override
	public void businessChangeLog(String className, List<String> borrowerConditionDescList,
			List<String> guaranteeConditionDescList, String uniqueId, String origBusinessId) {
		FiveLevelClassifyBusinessChangeLog changeLog = new FiveLevelClassifyBusinessChangeLog();

		List<FiveLevelClassifyBusinessChangeLog> oldChangeLogs = this
				.selectList(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>().eq("orig_business_id", origBusinessId)
						.eq("valid_status", "1"));

		if (CollectionUtils.isNotEmpty(oldChangeLogs)) {
			for (FiveLevelClassifyBusinessChangeLog oldChangeLog : oldChangeLogs) {
				oldChangeLog.setValidStatus("0");
				this.updateById(oldChangeLog);
			}
		}

		changeLog.setOrigBusinessId(origBusinessId);
		changeLog.setOpSourseType(Constant.FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_LOG);
		changeLog.setOpSourseId(uniqueId);
		changeLog.setOpUserId(loginUserInfoHelper.getUserId());
		changeLog.setOpUsername(loginUserInfoHelper.getLoginInfo().getUserName());
		changeLog.setOpTime(new Date());
		changeLog.setValidStatus("1");

		StringBuilder borrowerConditionDescBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(borrowerConditionDescList)) {
			for (String string : borrowerConditionDescList) {
				if (borrowerConditionDescList.indexOf(string) == (borrowerConditionDescList.size() - 1)) {
					borrowerConditionDescBuilder.append(string);
				} else {
					borrowerConditionDescBuilder.append((string + Constant.FIVE_LEVEL_CLASSIFY_SPLIT));
				}
			}
		}
		changeLog.setBorrowerConditionDesc(borrowerConditionDescBuilder.toString());

		StringBuilder guaranteeConditionDescBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(guaranteeConditionDescList)) {
			for (String string : guaranteeConditionDescList) {
				if (guaranteeConditionDescList.indexOf(string) == (guaranteeConditionDescList.size() - 1)) {
					guaranteeConditionDescBuilder.append(string);
				} else {
					guaranteeConditionDescBuilder.append((string + Constant.FIVE_LEVEL_CLASSIFY_SPLIT));
				}
			}
		}
		changeLog.setGuaranteeConditionDesc(guaranteeConditionDescBuilder.toString());

		changeLog.setClassName(className);
		this.insert(changeLog);
	}

}
