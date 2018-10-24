package com.hongte.alms.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hongte.alms.base.entity.RepaymentPlatformList;
import com.hongte.alms.base.mapper.RepaymentPlatformListMapper;
import com.hongte.alms.base.service.RepaymentPlatformListService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

/**
 * <p>
 * 平台还款流水表 服务实现类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-25
 */
@Service("RepaymentPlatformListService")
public class RepaymentPlatformListServiceImpl extends BaseServiceImpl<RepaymentPlatformListMapper, RepaymentPlatformList> implements RepaymentPlatformListService {

	@Autowired
	RepaymentPlatformListMapper repaymentPlatformListMapper;
	
	@Override
	public List<Map<String, Object>> selectPushPlatformRepayFlowList(Map<String, Object> paramBusinessMap) {
		return repaymentPlatformListMapper.selectPushPlatformRepayFlowList(paramBusinessMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvancePayFlowList(Map<String, Object> paramBusinessMap) {
		return repaymentPlatformListMapper.selectPushAdvancePayFlowList(paramBusinessMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvanceRepayFlowList(Map<String, Object> paramBusinessMap) {
		return repaymentPlatformListMapper.selectPushAdvanceRepayFlowList(paramBusinessMap);
	}

	@Override
	public List<Map<String, Object>> selectPushPlatformRepayFlow(Map<String, Object> paramFlowMap) {
		return repaymentPlatformListMapper.selectPushPlatformRepayFlow(paramFlowMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvancePayFlow(Map<String, Object> paramFlowMap) {
		return repaymentPlatformListMapper.selectPushAdvancePayFlow(paramFlowMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvanceRepayFlow(Map<String, Object> paramFlowMap) {
		return repaymentPlatformListMapper.selectPushAdvanceRepayFlow(paramFlowMap);
	}

	@Override
	public List<Map<String, Object>> selectPushPlatformRepayFlowItem(Map<String, Object> paramFlowItemMap) {
		return repaymentPlatformListMapper.selectPushPlatformRepayFlowItem(paramFlowItemMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvancePayFlowItem(Map<String, Object> paramFlowItemMap) {
		return repaymentPlatformListMapper.selectPushAdvancePayFlowItem(paramFlowItemMap);
	}

	@Override
	public List<Map<String, Object>> selectPushAdvanceRepayFlowItem(Map<String, Object> paramFlowItemMap) {
		return repaymentPlatformListMapper.selectPushAdvanceRepayFlowItem(paramFlowItemMap);
	}

	@Override
	public List<Map<String, Object>> selectPushNiWoRepayFlowList(Map<String, Object> paramBusinessMap) {
		return repaymentPlatformListMapper.selectPushNiWoRepayFlowList(paramBusinessMap);
	}

	@Override
	public List<Map<String, Object>> selectPushNiWoRepayFlow(Map<String, Object> paramFlowMap) {
		return repaymentPlatformListMapper.selectPushNiWoRepayFlow(paramFlowMap);
	}

	@Override
	public List<Map<String, Object>> selectPushRechargeFlowList(Map<String, Object> paramBusinessMap) {
		return repaymentPlatformListMapper.selectPushRechargeFlowList(paramBusinessMap);
	}

	@Override
	public List<Map<String, Object>> selectPushRechargeFlow(Map<String, Object> paramFlowMap) {
		return repaymentPlatformListMapper.selectPushRechargeFlow(paramFlowMap);
	}

}
