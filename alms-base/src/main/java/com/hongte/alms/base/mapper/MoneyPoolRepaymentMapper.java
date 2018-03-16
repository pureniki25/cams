package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 款项池业务关联表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-15
 */
public interface MoneyPoolRepaymentMapper extends SuperMapper<MoneyPoolRepayment> {
	List<MatchedMoneyPoolVO> listMatchedMoneyPool(@Param("businessId")String businessId , @Param("afterId")String afterId);
}
