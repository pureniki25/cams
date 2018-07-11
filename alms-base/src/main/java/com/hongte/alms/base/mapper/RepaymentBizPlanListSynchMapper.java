package com.hongte.alms.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanListSynch;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务还款计划列表 Mapper 接口
 * </p>
 *
 * @author 刘正全
 * @since 2018-07-09
 */
public interface RepaymentBizPlanListSynchMapper extends SuperMapper<RepaymentBizPlanListSynch> {

	List<Map<String, Object>> selectAddList(@Param("ew") Wrapper<RepaymentBizPlanListSynch> wrapperRepaymentBizPlanListSynch);

	List<String> selectdelList(@Param("ew") Wrapper<RepaymentBizPlanListSynch> wrapperDelId);

	void addRepaymentBizPlanListSynch();

	void updateRepaymentBizPlanList();

	void updateBasicBusinessg();

	void updateCollectionStatus();

	void updateRepaymentBizPlan();

	void updateFiveLevelClassifyBusinessChangeLog();

	void updateBasicBusinessCustomer();

	void updateRepaymentBizPlanListDetail();

	void updateTuandaiProjectInfo();

	void updateBasicCompany();

	void updateMoneyPoolRepayment();

}
