package com.hongte.alms.base.process.service;

import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.vo.ProcessWorkLogRequest;
import com.hongte.alms.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程运行日志 服务类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
public interface ProcessLogService extends BaseService<ProcessLog> {
    void addProcessLog(ProcessLog processLog);

    List<ProcessLog> getProcessLogList(String process_id);

    ProcessLog getLastProcessLog(String process_id);

    List<ProcessLog> getCurrentRuningProcessLog(String process_id);

    /**
     * 获取节点明细及相关的运行日志
     * @author 伦惠峰
     * @Date 2018/1/24 9:52
     */
    List<Map<String, Object>> getProcessWorkLogList( ProcessWorkLogRequest request) throws Exception;

    /**
     * 获取节点明细及相关的运行日志
     * @author 伦惠峰
     * @Date 2018/1/24 9:52
     */
    List<Map<String, Object>> getProcessWorkLogList( String process_id,String typeID);
}
