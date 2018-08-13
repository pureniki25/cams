package com.hongte.alms.base.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 还款计划应还项目明细表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentBizPlanListDetailMapper extends SuperMapper<RepaymentBizPlanListDetail> {

	Map<String, Object> totalRepaymentFactAmount(@Param(value = "origBusinessId") String origBusinessId);

	List<SettleFeesVO> selectLastPlanListLackFees(@Param("businessId") String businessId,@Param("dueDate")Date dueDate,
			@Param("planId") String planId);

	List<SettleFeesVO> selectLastPlanListDerateFees(@Param("businessId") String businessId,@Param("dueDate") Date dueDate,
			@Param("planId") String planId);
}
