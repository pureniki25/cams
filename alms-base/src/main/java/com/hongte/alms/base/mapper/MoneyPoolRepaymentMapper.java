package com.hongte.alms.base.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 款项池业务关联表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
public interface MoneyPoolRepaymentMapper extends SuperMapper<MoneyPoolRepayment> {

	/**
	 * 根据ids统计财务匹配且未被删除的还款登记的金额合计
	 * @author 王继光
	 * 2018年5月17日 上午11:22:39
	 * @param ids
	 * @return
	 */
	public BigDecimal sumMoneyPoolRepaymentAmountByMprIds(@Param("ids")List<String> ids);
}
