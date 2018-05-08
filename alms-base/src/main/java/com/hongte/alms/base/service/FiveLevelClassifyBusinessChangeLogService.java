package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 业务类别变更记录表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-04-20
 */

public interface FiveLevelClassifyBusinessChangeLogService extends BaseService<FiveLevelClassifyBusinessChangeLog> {

	void businessChangeLog(String className, List<String> borrowerConditionDescList,
			List<String> guaranteeConditionDescList, String uniqueId, String origBusinessId);
}
