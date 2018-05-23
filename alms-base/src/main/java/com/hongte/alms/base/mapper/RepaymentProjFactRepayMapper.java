package com.hongte.alms.base.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 标实还明细表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjFactRepayMapper extends SuperMapper<RepaymentProjFactRepay> {
	public BigDecimal calFactRepay(@Param("itemType")Integer itemType,@Param("feeId")String feeId,@Param("businessId")String businessId,@Param("afterId")String afterId) ;
}
