package com.hongte.alms.base.process.mapper;

import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程运行日志 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
public interface ProcessLogMapper extends SuperMapper<ProcessLog> {

    /**
     *返回当前流程运行的日志
     */
    List<ProcessLog> getCurrentRuningProcessLog(@Param(value = "process_id") String process_id);

    /**
     *返回流程节点的审核及待审节点日志
     */
    List<Map<String, Object>> getProcessWorkLogList(@Param(value = "process_id") String process_id, @Param(value = "typeID") String typeID);
}
