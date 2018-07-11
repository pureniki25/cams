package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanListSynch;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 业务还款计划列表 服务类
 * </p>
 *
 * @author 刘正全
 * @since 2018-07-09
 */
public interface RepaymentBizPlanListSynchService extends BaseService<RepaymentBizPlanListSynch> {

	public List<Map<String, Object>> selectAddList(Wrapper<RepaymentBizPlanListSynch> wrapperRepaymentBizPlanListSynch);

	public List<String> selectdelList(Wrapper<RepaymentBizPlanListSynch> wrapperDelId);

	public void addRepaymentBizPlanListSynch();

	public void updateRepaymentBizPlanList();

	public void updateBasicBusinessg();

	public void updateCollectionStatus();

	public void updateRepaymentBizPlan();

	public void updateFiveLevelClassifyBusinessChangeLog();

	public void updateBasicBusinessCustomer();

	public void updateRepaymentBizPlanListDetail();

	public void updateTuandaiProjectInfo();

	public void updateBasicCompany();

	public void updateMoneyPoolRepayment();

}
