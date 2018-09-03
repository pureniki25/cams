package com.hongte.alms.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 标的还款计划列表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanListService extends BaseService<RepaymentProjPlanList> {
	Double getOnLinePlanAmountSum(String projListId);
	
	Double getOnLineFactAmountSum(String projListId);
	Double getFactAmountSum(String projPlanId);
	Double getPrincipalAndinterestPeriod(@Param("projListId") String projListId);
	void calLateFee();
	/**
	 * 找出符合条件的记录进行滞纳金计算
	 * @return
	 */
	List<RepaymentProjPlanList> getProListForCalLateFee(String projListId);

	/**
	 * 计算每期pList得滞纳金
	 * @param pList
	 * @param type 1:结清重算 ,其他
	 * @return
	 */

	RepaymentBizPlanList calLateFeeForPerPList(RepaymentBizPlanList pList,Integer type);

	/**
	 * 预览计算每期pList得滞纳金(计算后的数据不保存进数据库)
	 * @author 王继光
	 * 2018年8月28日 下午4:12:36
	 * @param pList
	 * @param type
	 * @return
	 */
	Map<String, Object> previewCalLateFeeForPerPList(RepaymentBizPlanList pList,Integer type);

	


    /**
     * 提前结清重新计算滞纳金
     * @param dto
     * @param pList
     * @return
     */
    RepaymentBizPlanDto updateLateFee(RepaymentBizPlanDto dto);


}
