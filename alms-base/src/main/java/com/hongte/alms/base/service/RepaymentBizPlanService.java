package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.vo.module.RepaymentOpenServiceVO;
import com.hongte.alms.base.vo.module.api.RepayDetailResultRespData;
import com.hongte.alms.base.vo.module.api.RepayResultRespData;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 业务还款计划信息 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
public interface RepaymentBizPlanService extends BaseService<RepaymentBizPlan> {
	
	
	public   List<RepaymentOpenServiceVO> selectRepaymentOpenServiceList(String originalBusinessId,String afterId);
   
	public void repayResultUpdateRecord(RepayResultRespData data);
}
