package com.hongte.alms.base.process.service;


import com.alibaba.fastjson.JSONArray;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 流程类型步骤 服务类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
public interface ProcessTypeStepService extends BaseService<ProcessTypeStep> {
    List<ProcessTypeStep> getProcessTypeStep(String processtypeID);
    List<ProcessTypeStep> getProcessTypeStepByCode(String pTypeCode);

//    JSONArray getProcessTypeStepArray(String processtypeID,Process process);
    JSONArray getProcessTypeStepArray(List<ProcessTypeStep>  stepList,Process process,List<ProcessLog> logs);

    /**
     * 取回退的步骤列表
     * @param stepList
     * @param process
     * @return
     */
    JSONArray getDeractBackStepArray(List<ProcessTypeStep>  stepList,Process process);

    ProcessTypeStep findCurrentStepInfo(List<ProcessTypeStep>  stepList, Process process);


    ProcessTypeStep getProcessTypeStep(String typeID, Integer step);

    ProcessTypeStep getProcessTypeBeginStep(String typeID);


}
