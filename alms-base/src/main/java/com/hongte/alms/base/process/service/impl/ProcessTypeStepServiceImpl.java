package com.hongte.alms.base.process.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessIsDerateBackEnums;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessStepTypeEnums;
import com.hongte.alms.base.process.mapper.ProcessTypeStepMapper;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程类型步骤 服务实现类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
@Service("ProcessTypeStepService")
public class ProcessTypeStepServiceImpl extends BaseServiceImpl<ProcessTypeStepMapper, ProcessTypeStep> implements ProcessTypeStepService {
    @Autowired
    private  ProcessTypeStepMapper processTypeStepMapper;

    /**
     * TODO<返回流程步骤集合><br>
     * TODO<功能详细描述><br>
     *
     * @param processtypeID
     * @return java.util.List<com.ht.litigation.service.entity.ProcessTypeStep>
     * @author 伦惠峰
     * @Date 2018/1/15 14:51
     */
    @Override
    public List<ProcessTypeStep> getProcessTypeStep(String processtypeID)
    {
        List<ProcessTypeStep> processTypeStepList=new ArrayList<>();
        processTypeStepList=processTypeStepMapper
                .selectList(new EntityWrapper<ProcessTypeStep>().eq("type_id",processtypeID).orderBy("step"));
        return processTypeStepList;
    }
    @Override
    public List<ProcessTypeStep> getProcessTypeStepByCode(String pTypeCode)
    {
        List<ProcessTypeStep> processTypeStepList=new ArrayList<>();
        processTypeStepList=processTypeStepMapper
                .selectList(new EntityWrapper<ProcessTypeStep>().eq("type_code",pTypeCode).orderBy("step"));
        return processTypeStepList;
    }



    /**
     * 查找流程步骤并转化为JSONArray
     * @param processtypeID
     * @return
     */
//    @Override
//    public JSONArray getProcessTypeStepArray(String processtypeID,Process process){
//        List<ProcessTypeStep>  stepList = getProcessTypeStep(processtypeID);
//        return getProcessTypeStepArray(stepList,process);
///*        JSONArray stepArray  = new JSONArray();
//        for(ProcessTypeStep step:stepList){
//            JSONObject stepobj = new JSONObject();
//            stepobj.put("title",step.getStepName());
//            stepobj.put("step", step.getStep());
//            stepobj.put("content","");
//            stepArray.add(stepobj);
//            if (process!=null && process.getStatus() == ProcessStatusEnums.RUNNING.getKey()){
//                if(process.getCurrentStep() == step.getStep()){
//                    stepobj.put("inActive",true);
//                }
//            }
//
//        }
//        return stepArray;*/
//    }

    @Override
    public JSONArray getProcessTypeStepArray(List<ProcessTypeStep>  stepList,Process process,List<ProcessLog> logs){
        JSONArray stepArray  = new JSONArray();
        Integer step = 0;
        //历史的节点
        if(logs!=null){
            for(ProcessLog log : logs){
                JSONObject stepobj = new JSONObject();
                stepobj.put("title",log.getStepName());
                stepobj.put("step", step++);
                stepobj.put("content",log.getCreateTime());
                stepArray.add(stepobj);
            }
        }

        int nextStepIndex=0;
        //运行中的流程才会有当前节点，后续节点
        if(process!=null) {
            if (process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())) {
                //当前的节点
                for (int i = 0; i < stepList.size(); i++) {
                    ProcessTypeStep stepType = stepList.get(i);
                    if (process.getCurrentStep().equals(stepType.getStep())) {
                        JSONObject stepobj = new JSONObject();
                        stepobj.put("title", stepType.getStepName());
                        stepobj.put("step", step++);
                        stepobj.put("content", "");
                        stepobj.put("inActive", true);
                        stepArray.add(stepobj);
                        nextStepIndex = i + 1;
                        break;
                    }
                }
                //如果是回退的则需要加上回退的那个节点
                if (process.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())) {
                    for (int i = 0; i < stepList.size(); i++) {
                        ProcessTypeStep stepType = stepList.get(i);
                        if (process.getBackStep().equals(stepType.getStep())) {
                            JSONObject stepobj = new JSONObject();
                            stepobj.put("title", stepType.getStepName());
                            stepobj.put("step", step++);
                            stepobj.put("content", "");
                            stepArray.add(stepobj);
                            nextStepIndex = i + 1;
                            break;
                        }
                    }
                }
            }else if(process.getStatus().equals(ProcessStatusEnums.END.getKey())||process.getStatus().equals(ProcessStatusEnums.CNACL.getKey())){
                nextStepIndex = stepList.size();
            }
        }

        //后续的节点
        if(nextStepIndex<stepList.size()){
            for(int i=nextStepIndex; i<stepList.size();i++){
                ProcessTypeStep stepType = stepList.get(i);
                    JSONObject stepobj = new JSONObject();
                    stepobj.put("title",stepType.getStepName());
                    stepobj.put("step", step++);
                    stepobj.put("content","");
                    stepArray.add(stepobj);
                }
            }


//      无回退 顺序的写法
//        for(ProcessTypeStep step:stepList){
//            JSONObject stepobj = new JSONObject();
//            stepobj.put("title",step.getStepName());
//            stepobj.put("step", step.getStep());
//            stepobj.put("content","");
//            stepArray.add(stepobj);
//            if (process!=null && process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())){
//                if(process.getCurrentStep().equals(step.getStep())){
//                    stepobj.put("inActive",true);
//                }
//            }
//
//        }
        return stepArray;
    }

    public JSONArray getDeractBackStepArray(List<ProcessTypeStep>  stepList,Process process){
        JSONArray stepArray  = new JSONArray();
        if(process == null) return stepArray;

        for(ProcessTypeStep step:stepList){

            if (process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())){
                if(process.getCurrentStep() > step.getStep()){
                    JSONObject stepobj = new JSONObject();
                    stepobj.put("stepName",step.getStepName());
                    stepobj.put("step", step.getStep());
                    stepArray.add(stepobj);
                }
            }
        }
        return stepArray;
    }



    @Override
    public ProcessTypeStep findCurrentStepInfo(List<ProcessTypeStep>  stepList, Process process){
        for(ProcessTypeStep step:stepList){
            if (process!=null && process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())){
                if(process.getCurrentStep().equals(step.getStep())){
                    return step;
                }
            }
        }
        return null;
    }


    @Override
    public ProcessTypeStep getProcessTypeStep(String typeID,Integer step )
    {
        ProcessTypeStep processTypeStep=null;
        List<ProcessTypeStep> processTypeStepList=processTypeStepMapper
                .selectList(new EntityWrapper<ProcessTypeStep>().eq("type_id",typeID).eq("step",step));
        if(processTypeStepList!=null&&processTypeStepList.size()>0)
        {
            processTypeStep=processTypeStepList.get(0);
        }
        return processTypeStep;
    }

    @Override
    public ProcessTypeStep getProcessTypeBeginStep(String typeID)
    {
        ProcessTypeStep processTypeStep=null;
        List<ProcessTypeStep> processTypeStepList=processTypeStepMapper
                .selectList(new EntityWrapper<ProcessTypeStep>().
                        eq("type_id",typeID).
                        eq("step_type", ProcessStepTypeEnums.BEGIN_STEP.getKey()));
        if(processTypeStepList!=null&&processTypeStepList.size()>0)
        {
            processTypeStep=processTypeStepList.get(0);
        }
        return processTypeStep;
    }





}
