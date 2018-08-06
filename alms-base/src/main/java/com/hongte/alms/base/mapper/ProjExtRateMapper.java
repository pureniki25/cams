package com.hongte.alms.base.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.ProjExtRate;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 标的额外信息存储表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-26
 */
public interface ProjExtRateMapper extends SuperMapper<ProjExtRate> {
	BigDecimal calcProjextRate(@Param("projectId")String projectId ,@Param("rateType")String rateType,@Param("feeId")String feeId,@Param("peroid")String peroid) ;
}
