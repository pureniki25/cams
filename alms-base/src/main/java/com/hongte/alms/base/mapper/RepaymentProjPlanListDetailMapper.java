package com.hongte.alms.base.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 标的还款计划应还项目明细表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanListDetailMapper extends SuperMapper<RepaymentProjPlanListDetail> {

	/**
	 * 某个标的某期各个项剩余未还的金额
	 * @author 王继光
	 * 2018年5月17日 下午2:48:48
	 * @param proj_plan_list_id
	 * @return Map[(projPlanDetailId(string),feeId(string),planItemName(string),planItemType(string),residueAmount(bigdecimal))]
	 */
	public List<Map<String, Object>> selectProjDetailResidueAmount(String projPlanListId) ;
	
	/**
	 * 计算某期还款计划某项未还金额
	 * @author 王继光
	 * 2018年7月5日 下午7:41:01
	 * @param bizPlanListId
	 * @return
	 */
	public BigDecimal calcBizPlanListUnpaid(@Param("bizPlanListId")String bizPlanListId,@Param("planItemType")String planItemType,@Param("feeId")String feeId);
	
	/**
	 * 结清计算未还本金
	 * @author 王继光
	 * 2018年7月18日 下午3:18:37
	 * @param businessId
	 * @param planId
	 * @return
	 */
	public BigDecimal calcUnpaidPrincipal(@Param("businessId")String businessId,@Param("planId")String planId) ;
	
	/**
	 * 根据projectId和planId计算应还
	 * @author 王继光
	 * 2018年7月24日 下午3:55:17
	 * @param projectId
	 * @return
	 */
	public BigDecimal calcProjectPlanAmount(@Param("projectId")String projectId,@Param("planId")String planId,@Param("planItemType")String planItemType,@Param("feeId")String feeId);
	
	
	/**
	 * 结清结算剩余服务费金额
	 * @author 王继光
	 * 2018年8月2日 下午3:28:07
	 * @param businessId
	 * @param planId
	 * @param period
	 * @return
	 */
	BigDecimal calcSurplusService(@Param("businessId")String businessId,@Param("projectId")String projectId,@Param("planId")String planId,@Param("period")Integer period);
	
	/**
	 * 结清计算某期月收服务费金额
	 * @author 王继光
	 * 2018年8月2日 下午4:22:05
	 * @param businessId
	 * @param projectId
	 * @param planId
	 * @param period
	 * @return
	 */
	BigDecimal calcService(@Param("businessId")String businessId,@Param("projectId")String projectId,@Param("planId")String planId,@Param("period")Integer period);
	/**
	 * 结清计算某期月收平台费金额
	 * @author 王继光
	 * 2018年8月3日 上午9:37:00
	 * @param businessId
	 * @param projectId
	 * @param planId
	 * @param period
	 * @return
	 */
	BigDecimal calcPlatFee(@Param("businessId")String businessId,@Param("projectId")String projectId,@Param("planId")String planId,@Param("period")Integer period);
}
