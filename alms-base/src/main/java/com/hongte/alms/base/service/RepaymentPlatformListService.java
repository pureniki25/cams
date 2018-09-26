package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.RepaymentPlatformList;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 平台还款流水表 服务类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-25
 */
public interface RepaymentPlatformListService extends BaseService<RepaymentPlatformList> {

	List<Map<String, Object>> selectPushPlatformRepayFlowList(Map<String, Object> paramBusinessMap);

	List<Map<String, Object>> selectPushAdvancePayFlowList(Map<String, Object> paramBusinessMap);

	List<Map<String, Object>> selectPushAdvanceRepayFlowList(Map<String, Object> paramBusinessMap);

	List<Map<String, Object>> selectPushPlatformRepayFlow(Map<String, Object> paramFlowMap);

	List<Map<String, Object>> selectPushAdvancePayFlow(Map<String, Object> paramFlowMap);

	List<Map<String, Object>> selectPushAdvanceRepayFlow(Map<String, Object> paramFlowMap);

	List<Map<String, Object>> selectPushPlatformRepayFlowItem(Map<String, Object> paramFlowItemMap);

	List<Map<String, Object>> selectPushAdvancePayFlowItem(Map<String, Object> paramFlowItemMap);

	List<Map<String, Object>> selectPushAdvanceRepayFlowItem(Map<String, Object> paramFlowItemMap);

}
