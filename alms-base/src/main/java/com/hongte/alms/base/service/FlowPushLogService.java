package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.common.service.BaseService;
import com.ht.ussp.core.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
public interface FlowPushLogService extends BaseService<FlowPushLog> {

	Result queryDistributeFundRecord(Map<String, Object> map);
	
	Result pullAdvanceRepayInfo(Map<String, Object> map);

	void pushPlatformRepayFlowToCams(String projectId);

	void pushAdvancePayFlowToCams(String projectId);

	void pushAdvanceRepayFlowToCams(String projectId);

	List<Map<String, Object>> selectPushProjectList();

	void pushNiWoRepayFlowToCams(String planListId);

	void pushRechargeFlowToCams(String logId);

	

}
