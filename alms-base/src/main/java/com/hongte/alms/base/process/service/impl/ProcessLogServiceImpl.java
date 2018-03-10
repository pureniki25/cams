package com.hongte.alms.base.process.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.mapper.ProcessLogMapper;
import com.hongte.alms.base.process.service.ProcessLogService;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.vo.ProcessWorkLogRequest;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.hongte.alms.base.process.entity.Process;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程运行日志 服务实现类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
@Service("ProcessLogService")
public class ProcessLogServiceImpl extends BaseServiceImpl<ProcessLogMapper, ProcessLog> implements ProcessLogService {

    @Autowired
    private ProcessLogMapper processLogMapper;
    @Autowired
    @Qualifier("ProcessTypeService")
    private ProcessTypeService processTypeService;
    @Autowired
    @Qualifier("ProcessService")
    private ProcessService processService;

    /**
     * TODO<新增流程日志><br>
     *
     * @param processLog 流程日志实体
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/15 15:52
     */
    @Override
    public void addProcessLog(ProcessLog processLog) {
        processLogMapper.insert(processLog);
    }

    /**
     * 获取流程日志列表
     *
     * @Date 2018/1/15 15:52
     */
    @Override
    public List<ProcessLog> getProcessLogList(String process_id) {
        List<ProcessLog> list = processLogMapper.selectList(new EntityWrapper<ProcessLog>().eq("process_id", process_id).orderBy("create_time"));
        return list;
    }

    /**
     * 获取最近的一条日志
     *
     * @Date 2018/1/15 15:52
     */
    @Override
    public ProcessLog getLastProcessLog(String process_id) {
        ProcessLog lastProcessLog = null;
        List<ProcessLog> list = processLogMapper.selectList(new EntityWrapper<ProcessLog>().eq("process_id", process_id).orderBy("create_time desc"));
        if (list != null && list.size() > 0) {
            lastProcessLog = list.get(0);
        }
        return lastProcessLog;
    }

    /**
     * 返回已运行的流程节点
     *
     * @author 伦惠峰
     * @Date 2018/1/20 15:55
     */
    @Override
    public List<ProcessLog> getCurrentRuningProcessLog(String process_id) {
        List<ProcessLog> list = processLogMapper.getCurrentRuningProcessLog(process_id);
        return list;
    }

    @Override
    public List<Map<String, Object>> getProcessWorkLogList(ProcessWorkLogRequest request) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        if (request == null) {
            throw new Exception("请求包不能为空");
        }
        String processID = request.getProcessID();
        if (processID == null || processID == "") {
            String typeCode = request.getTypeCode();
            String businessId = request.getBusinessId();
            if (typeCode == "" || typeCode == null) {
                throw new Exception("typeCode不能为空");
            }
            if (businessId == "" || businessId == null) {
                throw new Exception("businessId不能为空");
            }

            ProcessType processType = processTypeService.getProcessTypeByCode(typeCode);
            if (processType == null) {
                throw new Exception("业务类型:" + typeCode + "不存在");
            }
            //根据typeCode和businessId匹配合适的processID(如果有多条，只返回最新的那条)
             Process process= processService.getLastProcess(businessId,processType.getTypeId());
             if(process!=null)
             {
                 processID=process.getProcessId();
             }
             result = getProcessWorkLogList(processID,processType.getTypeId());
        }
        else
        {
            result = getProcessWorkLogList(processID,"");
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getProcessWorkLogList(String process_id,String typeID) {
        List<Map<String, Object>> list = processLogMapper.getProcessWorkLogList(process_id,typeID);
        return list;
    }


}
