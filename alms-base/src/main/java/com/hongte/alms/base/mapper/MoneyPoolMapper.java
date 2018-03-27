package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 财务款项池表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
public interface MoneyPoolMapper extends SuperMapper<MoneyPool> {
	List<MatchedMoneyPoolVO> listMatchedMoneyPool(@Param("businessId") String businessId,@Param("afterId") String afterId);
}
