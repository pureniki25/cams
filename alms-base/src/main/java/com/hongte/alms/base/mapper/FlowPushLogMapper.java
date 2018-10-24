package com.hongte.alms.base.mapper;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
public interface FlowPushLogMapper extends SuperMapper<FlowPushLog> {

	List<Map<String, Object>> selectPushProjectList(Map<String, Object> mapParam);

}
