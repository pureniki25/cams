package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanListSynch;
import com.hongte.alms.base.mapper.RepaymentBizPlanListSynchMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListSynchService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务还款计划列表 服务实现类
 * </p>
 *
 * @author 刘正全
 * @since 2018-07-09
 */
@Service("RepaymentBizPlanListSynchService")
public class RepaymentBizPlanListSynchServiceImpl extends BaseServiceImpl<RepaymentBizPlanListSynchMapper, RepaymentBizPlanListSynch> implements RepaymentBizPlanListSynchService {
	@Autowired
	private RepaymentBizPlanListSynchMapper repaymentBizPlanListSynchMapper;
	
	@Override
	public List<Map<String, Object>> selectAddList(
			Wrapper<RepaymentBizPlanListSynch> wrapperRepaymentBizPlanListSynch) {
		return repaymentBizPlanListSynchMapper.selectAddList(wrapperRepaymentBizPlanListSynch);
	}

	@Override
	public List<String> selectdelList(Wrapper<RepaymentBizPlanListSynch> wrapperDelId) {
		return repaymentBizPlanListSynchMapper.selectdelList(wrapperDelId);
	}

	@Override
	public void addRepaymentBizPlanListSynch() {
		repaymentBizPlanListSynchMapper.addRepaymentBizPlanListSynch();
	}

	@Override
	public void updateRepaymentBizPlanList() {
		repaymentBizPlanListSynchMapper.updateRepaymentBizPlanList();
		
	}

	@Override
	public void updateBasicBusinessg() {
		repaymentBizPlanListSynchMapper.updateBasicBusinessg();
		
	}

	@Override
	public void updateCollectionStatus() {
		repaymentBizPlanListSynchMapper.updateCollectionStatus();
		
	}

	@Override
	public void updateRepaymentBizPlan() {
		repaymentBizPlanListSynchMapper.updateRepaymentBizPlan();
		
	}

	@Override
	public void updateFiveLevelClassifyBusinessChangeLog() {
		repaymentBizPlanListSynchMapper.updateFiveLevelClassifyBusinessChangeLog();
		
	}

	@Override
	public void updateBasicBusinessCustomer() {
		repaymentBizPlanListSynchMapper.updateBasicBusinessCustomer();
		
	}

	@Override
	public void updateRepaymentBizPlanListDetail() {
		repaymentBizPlanListSynchMapper.updateRepaymentBizPlanListDetail();
		
	}

	@Override
	public void updateTuandaiProjectInfo() {
		repaymentBizPlanListSynchMapper.updateTuandaiProjectInfo();
		
	}

	@Override
	public void updateBasicCompany() {
		repaymentBizPlanListSynchMapper.updateBasicCompany();
		
	}

	@Override
	public void updateMoneyPoolRepayment() {
		repaymentBizPlanListSynchMapper.updateMoneyPoolRepayment();
		
	}

	@Override
	public void updateBasicBusinessType() {
		repaymentBizPlanListSynchMapper.updateBasicBusinessType();
		
	}

}
