package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 财务款项池表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-02-28
 */
public interface MoneyPoolMapper extends SuperMapper<MoneyPool> {
	List<MoneyPoolVO> listMoneyPool(@Param("businessId")String businessId , @Param("afterId")String afterId , @Param("page")Integer page,@Param("limit")Integer limit) ;
	int listMoneyPoolCount(@Param("businessId")String businessId , @Param("afterId")String afterId);
}
