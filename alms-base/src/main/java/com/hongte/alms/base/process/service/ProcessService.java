package com.hongte.alms.base.process.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.vo.*;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程信息 服务类
 * </p>
 *
 * @author 张鹏
 * @since 2018-01-08
 */
public interface ProcessService extends BaseService<Process> {


    /**
     * 存储审批信息
     * @param processSaveReq
     * @param processType
     * @return
     */
    Process saveProcess(ProcessSaveReq  processSaveReq, ProcessTypeEnums  processType);



    /**
     * 存储审批记录并并修改流程审批信息
     * @param req
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void saveProcessApprovalResult(ProcessLogReq req ,ProcessTypeEnums processTypeEnums) throws IllegalAccessException, InstantiationException ;


    /**
     * 取流程显示信息
     * @param retMap
     * @param processId
     */
    public void getProcessShowInfo(Map<String,Object> retMap,String processId,ProcessTypeEnums processTypeEnums);


    /**
     * 根据流程类型和流程步骤查找流程
     * @param processTypeEnums
     * @param stepCode
     * @return
     */
    public ProcessTypeStep getStepByPTypeStepCode(ProcessTypeEnums processTypeEnums ,Integer stepCode);




    Page<ProcessVo> GetList(Page<ProcessVo> pageInfo, String key);

    void addProcess(Process process);

//    void updateProcess(Process process);

    List<Process> getRelatedProcess(String businessId, String processTypeID);

    Process getProcess(String processID);

    void updateAllColumnProcess(Process process);

    void updateProcessStep(String processID, String approveUserID, Integer currentStep);

    void updateProcessBack(String processID, Integer backStep, Integer isDirectBack);

    Page<Map<String,Object>> getProcessManagerList(Page<Map<String, Object>> pageInfo, ProcessPageRequest requestInfo);

    /**
     *动态SQL查询
     */
    List<Map<String, Object>> querySql(String key);

    /**
     *返回流程审核节点绑定的内容信息
     */
//    ProcessStepApprovalPageInfo getProcessStepApprovalPageInfo(String processID);

    /**
     *根据业务编码和流程类别，返回该业务最新流程信息
     */
    Process  getLastProcess(String processId, String typeId);

    /**
     *流程发起查询页
     */
    Page<Map<String, Object>> getProcessStartList(Page<Map<String, Object>> pages, ProcessStartPageRequest requestInfo);


    public  String getApproveUserId(ProcessTypeStep step, String createUser,Process process);

    /**
     * 分页查询
     * @param key
     * @return
     */
    Page<ProcessVo> selectProcessVoPage(ProcessReq key);


    /**
     * 查询所有，不分页
     * @param key
     * @return
     */
    List<ProcessVo> selectProcessVoList(ProcessReq key);

}
