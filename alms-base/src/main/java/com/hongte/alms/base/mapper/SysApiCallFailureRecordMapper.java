package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * API调用失败记录表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
public interface SysApiCallFailureRecordMapper extends SuperMapper<SysApiCallFailureRecord> {

	/**
	 * 根据apicode查找调用失败的数据
	 * 
	 * @return
	 */
	List<SysApiCallFailureRecord> queryCallFailedDataByApiCode(@Param(value = "apiCode") String apiCode, @Param(value = "moduleName") String moduleName);
}
