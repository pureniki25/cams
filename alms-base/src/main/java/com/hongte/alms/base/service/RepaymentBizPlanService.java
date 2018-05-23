package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanDto;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanVo;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.vo.module.RepaymentOpenServiceVO;
import com.hongte.alms.base.vo.module.api.RepayDetailResultRespData;
import com.hongte.alms.base.vo.module.api.RepayResultRespData;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;

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


	/**
	 * 查询指定用户还款计划列表 分页
	 * @param identifyCard
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Page<RepayingPlanDto> queryCustomerRepayPlan(String identifyCard, Integer pageIndex, Integer pageSize );


	/**
	 * 查询指定用户还款计划信息  分页
	 * @param identifyCard
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Page<RepayingPlanVo> queryCustomeRepayPlanInfo(String identifyCard, Integer pageIndex, Integer pageSize) throws IllegalAccessException, InstantiationException;


	}
