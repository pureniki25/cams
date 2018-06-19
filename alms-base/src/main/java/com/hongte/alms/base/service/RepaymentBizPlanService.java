package com.hongte.alms.base.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanDto;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanVo;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.vo.finance.RepaymentSettleListVO;
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
	 * 查询指定用户还款计划信息  分页
	 * @param identifyCard
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Page<RepayingPlanVo> queryCustomeRepayPlanInfo(String identifyCard, Integer pageIndex, Integer pageSize) throws IllegalAccessException, InstantiationException;

	/**
	 * 根据businessId和planId查询对应还款计划信息
	 * @author 王继光
	 * 2018年5月29日 下午5:02:58
	 * @param businessId
	 * @param planId,若null,则查询所有
	 * @return
	 */
	public List<RepaymentSettleListVO> listRepaymentSettleListVOs(String businessId,String afterId,String planId);
	
	}
