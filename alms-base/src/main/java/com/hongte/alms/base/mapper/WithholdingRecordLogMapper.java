package com.hongte.alms.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 执行代扣记录日志表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
public interface WithholdingRecordLogMapper extends SuperMapper<WithholdingRecordLog> {
	
	 WithholdingRecordLog selectWithholdingRecordLog(@Param("originalBusinessId")String originalBusinessId,@Param("afterId") String afterId);
		 
	 

}
