package com.hongte.alms.base.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.AuditVo;
import com.hongte.alms.base.baseException.AlmsBaseExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.ProcessEngineFlageEnums;
import com.hongte.alms.base.enums.SysRoleAreaTypeEnums;
import com.hongte.alms.base.enums.SysRoleEnums;
import com.hongte.alms.base.process.entity.*;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.process.service.*;
import com.hongte.alms.base.process.vo.*;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.JsonUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 流程信息 服务实现类
 * </p>
 *
 * @author 张鹏
 * @since 2018-01-08
 */
@Service("ProcessService")
public class ProcessServiceImpl extends BaseServiceImpl<ProcessMapper, Process> implements ProcessService {

    Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    @Qualifier("ProcessTypeStepService")
    private ProcessTypeStepService processTypeStepService;


    @Autowired
    @Qualifier("ProcessTypeService")
    private ProcessTypeService processTypeService;

    @Autowired
    @Qualifier("ProcessLogService")
    private ProcessLogService processLogService;

    @Autowired
    private LoginUserInfoHelper  loginUserInfoHelper;

    @Autowired
    @Qualifier("ProcessLogCopySendService")
    private ProcessLogCopySendService processLogCopySendService;

    @Autowired
    @Qualifier("SysUserService")
    private SysUserService sysUserService;

    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;

    @Autowired
    @Qualifier("SysOrgService")
    private SysOrgService sysOrgService;
//    @Autowired
//    @Qualifier("SysUserService")
//    private SysUserService sysUserService;

    @Autowired
    @Qualifier("SysRoleService")
    private SysRoleService sysRoleService;

//    @Autowired
//    @Qualifier("SysUserAreaService")
//    private SysUserAreaService sysUserAreaService;

    @Autowired
    @Qualifier("SysUserRoleService")
    private SysUserRoleService sysUserRoleService;

    @Autowired
    @Qualifier("BasicCompanyService")
    private  BasicCompanyService basicCompanyService;


    /**
     * 存储流程信息，所有异常都回滚
     * @param processSaveReq
     * @param processTypeEnums
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Process saveProcess(ProcessSaveReq  processSaveReq, ProcessTypeEnums processTypeEnums){

//        if(!processSaveReq.getProcessStatus().equals(ProcessStatusEnums.NEW.getKey())&&!processSaveReq.getProcessStatus().equals(ProcessStatusEnums.BEGIN.getKey())){
//            throw  new RuntimeException("流程状态应为新建或起始");
//        }
        if(processSaveReq.getBusinessId() == null){
            logger.error("业务ID不能为空  "+processTypeEnums.getName());
            throw new RuntimeException("业务ID不能为空  "+processTypeEnums.getName());
        }

        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            logger.error("流程类型未定义  "+processTypeEnums.getName());
            throw new RuntimeException("流程类型未定义"  +processTypeEnums.getName());
        }


        //判断流程是否有进行中的  流程运行运行几次，是否超出了运行次数
        //1.查询出此业务运行中的流程有几条
        List<Process> pList =  selectList(new EntityWrapper<Process>()
                .eq("business_id",processSaveReq.getBusinessId())
                .eq("status",ProcessStatusEnums.RUNNING.getKey()).eq("process_typeid", processType.getTypeId()));
        if(pList.size()>=processType.getCanItemRunningCount()){
            throw new RuntimeException("此业务已有"+pList.size()+"个"+processType.getTypeName()+"流程在审批中，请已有流程完成后再试！！");
        }

        //2.查出此业务已运行并执行结果为成功的流程有几条
        List<Process> sucPList = selectList(new EntityWrapper<Process>()
        .eq("business_id",processSaveReq.getBusinessId())
        .eq("status",ProcessStatusEnums.END.getKey())
        .eq("process_result",ProcessResultEnums.PASS.getKey())
        .eq("process_typeid", processType.getTypeId()));
        if(sucPList.size()>=processType.getCanItemTotalCount()){
            throw new RuntimeException("此业务已有"+sucPList.size()+"个"+processType.getTypeName()+"流程通过审批，不能再申请！！");
        }


        //取得 流程的起始步骤
        ProcessTypeStep beginStep = processTypeStepService.getProcessTypeBeginStep(processType.getTypeId());
        if(beginStep==null){
            logger.error("流程未设置起始节点  "+processTypeEnums.getName());
            throw  new RuntimeException("流程未设置起始节点"+ processTypeEnums.getName());
        }
        ProcessTypeStep step = processTypeStepService.getProcessTypeStep(processType.getTypeId(),beginStep.getNextStep());
//        if (step.getStepType().equals(ProcessStepTypeEnums.BEGIN_STEP.getKey())) {//如果是起始节点，则跳过到下一个节点
////            createStep = step;
//            step = processTypeStepService.getProcessTypeStep(processType.getTypeId(),step.getNextStep());
//        }
//        if(step.getApproveUserType().equals(ProcessApproveUserType.CREATER.getKey())){//如果流程的审核人为创造者，则跳过到下一个节点
//            step = processTypeStepService.getProcessTypeStep(processType.getTypeId(),step.getNextStep());
//        }


        if(step == null){
            logger.error("流程起始步骤的下一步未定义  "+ processTypeEnums.getName());
            throw new RuntimeException("流程起始步骤的下一步未定义" + processTypeEnums.getName());
        }


        Process process = null;

        if(StringUtils.isEmpty(processSaveReq.getProcessId()) ){
            process = new Process();
            process.setProcessId(UUID.randomUUID().toString());
            process.setCreateTime(new Date());
            process.setCreateUser(loginUserInfoHelper.getUserId());
            process.setCurrentStep(step.getStep());

            process.setProcessEngineFlage(ProcessEngineFlageEnums.LOCAL_SIMPLE_ENGINE.getKey());
            process.setProcessDesc(processSaveReq.getDesc());


            process.setProcessName(processSaveReq.getTitle());
            process.setProcessTypeid(processType.getTypeId());
            process.setStatus(processSaveReq.getProcessStatus());
            process.setBusinessId(processSaveReq.getBusinessId());
            //设置审核人
            process.setApproveUserId(getApproveUserId(step,process.getCreateUser(),process));
            process.setIsDirectBack(ProcessIsDerateBackEnums.NO.getKey());
            process.setStartTime( new Date());
            process.setStartUserId(loginUserInfoHelper.getUserId());
//            if(processSaveReq.getProcessStatus() == ProcessStatusEnums.RUNNING.getKey()){
//                process.setStartTime( new Date());
//                process.setStartUserId(Constant.DEV_DEFAULT_USER);
//            }
//            insert(process);
        }else{
            process = selectById(processSaveReq.getProcessId());
            if(processSaveReq.getProcessStatus()!= process.getStatus()){
                process.setStatus(processSaveReq.getProcessStatus());
                process.setCreateTime(new Date());
//                process.setStartTime( new Date());
//                process.setStartUserId(Constant.DEV_DEFAULT_USER);
//                updateProcess(process);

            }
        }
        //开始 则设置起始时间
//        if(processSaveReq.getProcessStatus() == ProcessStatusEnums.RUNNING.getKey()){
//            process.setStartTime( new Date());
//            process.setStartUserId(loginUserInfoHelper.getUserId());
//        }else{
//            process.setStartTime( null);
//            process.setStartUserId(null);
//        }
        //更新或插入
        insertOrUpdateAllColumn(process);
        return process;
    }
    
    
    /**
     * 减免申请存储流程审批记录，所有异常都回滚
     * @param req
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Process saveProcessApprovalResultDerate(ProcessLogReq req ,ProcessTypeEnums processTypeEnums,boolean isFinish,ApplyTypeVo vo) throws IllegalAccessException, InstantiationException{


        ProcessLog log =  ClassCopyUtil.copyObject(req,ProcessLog.class);
        //流程信息
        Process process = selectById(req.getProcess().getProcessId());

        if(!process.getCurrentStep().equals(req.getProcess().getCurrentStep())){
            throw  new RuntimeException("当前流程状态与界面流程状态不一致，请刷新后重新提交!");
        }

        //判断登录用户是否是当前步骤的审批人之一
//        if(!canApprove(process)){  调试暂时屏蔽
//            throw  new RuntimeException("您不是流程的当前审批人!");
//        }

        //流程类型
        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            throw new RuntimeException("流程类型未定义");
        }

        //当前节点定义
        ProcessTypeStep currentStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getProcess().getCurrentStep());
        if(currentStep == null){
            throw new  RuntimeException("找不到 当前流程节点定义！");
        }

        //后一个节点定义
        ProcessTypeStep nextStep = null;

        //如果回退则取回退的步骤
        if(req.getIsPass().equals(ProcessApproveResult.REFUSE.getKey())&&req.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){
            if(req.getNextStep()==null){
                throw new  RuntimeException("应该设置回退到第几步！");
            }
            nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getNextStep());
            process.setIsDirectBack(ProcessIsDerateBackEnums.YES.getKey());//标识回退
            process.setBackStep(currentStep.getStep());//记录回退的步骤
        }else{
           if(process.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())) {
                //如果流程的是上一步回退的,则取流程中存储的应该跳转的节点
                nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),process.getBackStep());
            }else{//否则直接取后一个
                if(currentStep.getNextStep()!=null){
                    nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),currentStep.getNextStep());
                }
            }
            process.setIsDirectBack(ProcessIsDerateBackEnums.NO.getKey());//取消回退标志
            process.setBackStep(null);//清除回退步骤

        }
        if(isFinish==true) {
        	currentStep.setNextStep(null);
        }



        //1.添加log记录
        log.setProcessLogId(UUID.randomUUID().toString());
        log.setProcessId(req.getProcess().getProcessId());
        log.setProcessName(req.getProcessName());
        log.setTypeId(processType.getTypeId());
        log.setTypeStepId(currentStep.getTypeStepId());
        log.setStepName(currentStep.getStepName());
        log.setApproveUserId(loginUserInfoHelper.getUserId());
        log.setCurrentStep(process.getCurrentStep());
        log.setNextStep(currentStep.getNextStep());
        log.setCreateUser(loginUserInfoHelper.getUserId());
        log.setCreateTime(new Date());
        processLogService.insert(log);
        
        
        List<SysUser> users=null;
      if(!log.getIsPass().equals(ProcessApproveResult.REFUSE.getKey())) {
        if(isFinish==true) {
        	  //车贷减免流程：减免金额<= 10000 要默认抄送贷后综合岗人员及车贷业务车贷出纳人员
        	if((vo.getBusinessTypeId()==BusinessTypeEnum.CYD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("10000"))<=0)){
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_CAR_TELLER.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        		
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_GENERAL_APPROVE.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        	
        	}else if((vo.getBusinessTypeId()==BusinessTypeEnum.CYD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("10000"))>0)){
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_CAR_TELLER.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        	}
        	
        	
        	    //房贷，减免金额<= 20000 ，要默认抄送贷后综合岗人员及车贷业务车贷出纳人员
        	if((vo.getBusinessTypeId()==BusinessTypeEnum.FSD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("20000"))<=0)){
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_HOUSE_TELLER.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_GENERAL_APPROVE.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        	}else if((vo.getBusinessTypeId()==BusinessTypeEnum.FSD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("20000"))>0)) {
        		users=sysUserService.selectUsersByRole(SysRoleEnums.DH_HOUSE_TELLER.getKey());
        		users.forEach(item->{
        			req.setSendUserIds(addElementToArray(req.getSendUserIds(), item.getUserId()));
        		});
        	
        	}
      
        }
      }
        
        //添加抄送记录
        for(int i=0;i<req.getSendUserIds().length;i++){
            ProcessLogCopySend copySend = new ProcessLogCopySend();
            copySend.setProcessLogId(log.getProcessLogId());
            copySend.setProcessSendId(UUID.randomUUID().toString());
            copySend.setReceiveUserId(req.getSendUserIds()[i]);
            SysUser sysUser = sysUserService.selectById(req.getSendUserIds()[i]);
            if(sysUser!=null){
                copySend.setReceiveUserName(sysUser.getUserName());
            }else{
                copySend.setReceiveUserName(Constant.ADMIN_ID);
            }
            processLogCopySendService.insert(copySend);
        }


        //更新状态
        //如果审批不通过且未定向打回则结束流程
        if(log.getIsPass().equals(ProcessApproveResult.REFUSE.getKey())&&!log.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
            process.setProcessResult(ProcessApproveResult.REFUSE.getKey());
        }else{
            if(log.getIsDirectBack()!=null&&!log.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){//定向打回
                if(currentStep.getStepType().equals(ProcessStepTypeEnums.END_STEP.getKey())//节点类型为结束节点则结束流程
                        || currentStep.getNextStep()== null||isFinish==true){//没有下一步节点
                    process.setCurrentStep(null);
                    process.setApproveUserId(null);
                    process.setStatus(ProcessStatusEnums.END.getKey());
                    process.setProcessResult(ProcessApproveResult.PASS.getKey());
                }
            }
        }
        //更新当前审核人 和当前审核步骤
        if(nextStep !=null){//如果还有下一步
            process.setCurrentStep(nextStep.getStep());
            process.setApproveUserId(getApproveUserId(nextStep,process.getCreateUser(),process));
        }
        /*else{
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
        }*/

        //更新操作人和操作时间
        process.setUpdateTime(new Date());
        process.setUpdateUser(loginUserInfoHelper.getUserId());

        updateAllColumnProcess(process);

        return process;
        //updateProcess(process);

    }

    
    

    /**
     * 存储流程审批记录，所有异常都回滚
     * @param req
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Process saveProcessApprovalResult(ProcessLogReq req ,ProcessTypeEnums processTypeEnums) throws IllegalAccessException, InstantiationException{


        ProcessLog log =  ClassCopyUtil.copyObject(req,ProcessLog.class);
        //流程信息
        Process process = selectById(req.getProcess().getProcessId());

        if(!process.getCurrentStep().equals(req.getProcess().getCurrentStep())){
            throw  new RuntimeException("当前流程状态与界面流程状态不一致，请刷新后重新提交!");
        }

        //判断登录用户是否是当前步骤的审批人之一
//        if(!canApprove(process)){  调试暂时屏蔽
//            throw  new RuntimeException("您不是流程的当前审批人!");
//        }

        //流程类型
        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            throw new RuntimeException("流程类型未定义");
        }

        //当前节点定义
        ProcessTypeStep currentStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getProcess().getCurrentStep());
        if(currentStep == null){
            throw new  RuntimeException("找不到 当前流程节点定义！");
        }

        //后一个节点定义
        ProcessTypeStep nextStep = null;

        //如果回退则取回退的步骤
        if(req.getIsPass().equals(ProcessApproveResult.REFUSE.getKey())&&req.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){
            if(req.getNextStep()==null){
                throw new  RuntimeException("应该设置回退到第几步！");
            }
            nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getNextStep());
            process.setIsDirectBack(ProcessIsDerateBackEnums.YES.getKey());//标识回退
            process.setBackStep(currentStep.getStep());//记录回退的步骤
        }else{
           if(process.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())) {
                //如果流程的是上一步回退的,则取流程中存储的应该跳转的节点
                nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),process.getBackStep());
            }else{//否则直接取后一个
                if(currentStep.getNextStep()!=null){
                    nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),currentStep.getNextStep());
                }
            }
            process.setIsDirectBack(ProcessIsDerateBackEnums.NO.getKey());//取消回退标志
            process.setBackStep(null);//清除回退步骤

        }



        //1.添加log记录
        log.setProcessLogId(UUID.randomUUID().toString());
        log.setProcessId(req.getProcess().getProcessId());
        log.setProcessName(req.getProcessName());
        log.setTypeId(processType.getTypeId());
        log.setTypeStepId(currentStep.getTypeStepId());
        log.setStepName(currentStep.getStepName());
        log.setApproveUserId(loginUserInfoHelper.getUserId());
        log.setCurrentStep(process.getCurrentStep());
        log.setNextStep(currentStep.getNextStep());
        log.setCreateUser(loginUserInfoHelper.getUserId());
        log.setCreateTime(new Date());
        processLogService.insert(log);

        //添加抄送记录
        for(int i=0;i<req.getSendUserIds().length;i++){
            ProcessLogCopySend copySend = new ProcessLogCopySend();
            copySend.setProcessLogId(log.getProcessLogId());
            copySend.setProcessSendId(UUID.randomUUID().toString());
            copySend.setReceiveUserId(req.getSendUserIds()[i]);
            SysUser sysUser = sysUserService.selectById(req.getSendUserIds()[i]);
            if(sysUser!=null){
                copySend.setReceiveUserName(sysUser.getUserName());
            }else{
                copySend.setReceiveUserName(Constant.ADMIN_ID);
            }
            processLogCopySendService.insert(copySend);
        }


        //更新状态
        //如果审批不通过且未定向打回则结束流程
        if(log.getIsPass().equals(ProcessApproveResult.REFUSE.getKey())&&!log.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
            process.setProcessResult(ProcessApproveResult.REFUSE.getKey());
        }else{
            if(log.getIsDirectBack()!=null&&!log.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){//定向打回
                if(currentStep.getStepType().equals(ProcessStepTypeEnums.END_STEP.getKey())//节点类型为结束节点则结束流程
                        || currentStep.getNextStep()== null){//没有下一步节点
                    process.setCurrentStep(null);
                    process.setApproveUserId(null);
                    process.setStatus(ProcessStatusEnums.END.getKey());
                    process.setProcessResult(ProcessApproveResult.PASS.getKey());
                }
            }
        }
        //更新当前审核人 和当前审核步骤
        if(nextStep !=null){//如果还有下一步
            process.setCurrentStep(nextStep.getStep());
            process.setApproveUserId(getApproveUserId(nextStep,process.getCreateUser(),process));
        }
        /*else{
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
        }*/

        //更新操作人和操作时间
        process.setUpdateTime(new Date());
        process.setUpdateUser(loginUserInfoHelper.getUserId());

        updateAllColumnProcess(process);

        return process;
        //updateProcess(process);

    }

    /**
     * 判断当前用户是否可审批此流程
     * @param process
     * @return
     */


    /**
     *  取流程显示信息
     * @param retMap
     * @param processId
     */
    @Override
    public void getProcessShowInfo(Map<String,Object> retMap,String processId,ProcessTypeEnums processTypeEnums){


        //流程类型
        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            throw new RuntimeException("流程类型未定义");
        }

        //流程步骤列表
        List<ProcessTypeStep>  stepList = getStepList(processTypeEnums);


        //流程基本信息
        Process process = null;
        List<ProcessLog> logs = null;
        if(processId !=null){
            process =  getProcess(processId);
            if(process !=null){
                List<Process> l = new LinkedList<>();
                l.add(process);
                retMap.put("process",(JSONArray) JSON.toJSON(l, JsonUtil.getMapping()));
            }

            //历史审核记录  开始
            logs =  processLogService.getProcessLogList(processId);
            List<ProcessLogVo>  logsVo = new LinkedList<ProcessLogVo>();
            for(ProcessLog log : logs){
                ProcessLogVo vo  = null;
                try {
                    vo = ClassCopyUtil.copyObject(log,ProcessLogVo.class);
                } catch (InstantiationException e) {
                    e.printStackTrace();

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                SysUser user =  sysUserService.selectById(log.getApproveUserId());
                vo.setApproveUserName(user != null?user.getUserName():log.getApproveUserId());
                logsVo.add(vo);
            }
            retMap.put("processLogs",(JSONArray) JSON.toJSON(logsVo, JsonUtil.getMapping()));
            //历史审核记录  结束




        }
        //流程步骤信息
        JSONArray stepArray = processTypeStepService.getProcessTypeStepArray(stepList,process,logs);
        retMap.put("stepArray",stepArray);

        //可回退的步骤 列表
        JSONArray rockBackStepArray = processTypeStepService.getDeractBackStepArray(stepList,process);
        retMap.put("rockBackStepList",rockBackStepArray);


        //当前审核信息
        if(process!=null){
//            SysUser sysUser =  sysUserService.selectById(process.getApproveUserId());
//            retMap.put("approveUserName",sysUser==null?process.getApproveUserId():sysUser.getUserName());

            LoginInfoDto loginInfoDto = loginUserInfoHelper.getLoginInfo();
            String approvalUesrName = "";
            if(loginInfoDto==null){
                SysUser sysUser =  sysUserService.selectById(loginUserInfoHelper.getUserId());
                if(sysUser != null){
                    approvalUesrName =sysUser.getUserName();
                }else{
                    approvalUesrName = loginUserInfoHelper.getUserId();
                }
            }else{
                approvalUesrName = loginInfoDto.getUserName();
            }
//            String approvalUesrName = loginUserInfoHelper.getLoginInfo()!=null?loginUserInfoHelper.getLoginInfo().getUserName():loginUserInfoHelper.getUserId();
            retMap.put("approveUserName",approvalUesrName);
            ProcessTypeStep currentStepInfo  =  processTypeStepService.findCurrentStepInfo(stepList,process);
            retMap.put("currentStepName",currentStepInfo!=null?currentStepInfo.getStepName():"");
        }else{
            retMap.put("approveUserName","");
            retMap.put("currentStepName","");
        }
        retMap.put("approveDate",new Date());

        //可选抄送人列表
        List<SysUser> users = sysUserService.selectList(new EntityWrapper<>());
        retMap.put("canSendUserList",users);

        //登录用户能够审批此流程的标志位
        Boolean  canApproveFlage = process!=null&&process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())?canApprove(process):false;
        retMap.put("canApproveFlage",canApproveFlage);

        //当前审核人是否是创建者的标志位
        Boolean isCreaterFlage = true;
        if(process == null){
            isCreaterFlage = true;
        }else{
            if(process.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())){
                //当前节点定义
                ProcessTypeStep currentStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),process.getCurrentStep());
                if(currentStep == null){
                    throw new  RuntimeException("找不到 当前流程节点定义！");
                }
                isCreaterFlage =currentStep.getApproveUserType().equals(ProcessApproveUserType.CREATER.getKey());
            }
        }
        retMap.put("isCreaterFlage",isCreaterFlage);

    }

    @Override
    public ProcessTypeStep getStepByPTypeStepCode(ProcessTypeEnums processTypeEnums ,Integer stepCode){

        //流程类型
                ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
            if(processType == null){
            throw new RuntimeException("流程类型未定义");
        }

        //当前节点定义
        ProcessTypeStep currentStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),stepCode);
            if(currentStep == null){
            throw new  RuntimeException("找不到 当前流程节点定义！");
        }
        return currentStep;
    }

//    @Override
    public List<ProcessTypeStep> getStepList(ProcessTypeEnums processTypeEnums){

        //流程类型
        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
            if(processType == null){
            throw new RuntimeException("流程类型未定义");
        }

        //流程步骤
        List<ProcessTypeStep> list = processTypeStepService.getProcessTypeStep(processType.getTypeId());

        return list;
    }

    private boolean canApprove(Process process){

        //判断登录用户是否是当前步骤的审批人之一
        String[] userIds =  process.getApproveUserId().split(",");
        boolean canApplyFlage = false;
        for(int i = 0;i<userIds.length;i++){
            if(userIds[i].equals(loginUserInfoHelper.getUserId())){
                canApplyFlage = true;
            }
        }
//        if(!canApplyFlage){
//            throw  new RuntimeException("您不是流程的当前审批人!");
//        }
        return canApplyFlage;
    }



    /**
     * 获取分页数据       Transactional( rollbackFor = Exception.class)所有异常都回滚
     *
     * @param pageInfo
     * @param key
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<ProcessVo> GetList(Page<ProcessVo> pageInfo, String key) {
        List<ProcessVo> list = this.processMapper.GetList(pageInfo, key);
        pageInfo.setRecords(list);
        return pageInfo;
    }

    /**
     * TODO<返回关联的进程信息><br>
     * TODO<功能详细描述><br>
     *
     * @param businessId    业务ID
     * @param processTypeID 业务类型ID
     * @return java.util.List<com.ht.litigation.service.entity.Process>
     * @author 伦惠峰
     * @Date 2018/1/15 10:39
     */
    @Override
    public List<Process> getRelatedProcess(String businessId, String processTypeID) {
        return processMapper.getRelatedProcess(businessId, processTypeID);
    }

    /**
     * TODO<新增流程记录><br>
     *
     * @param process 需要新增的流程信息
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/15 15:50
     */
    @Override
    public void addProcess(Process process) {
        processMapper.insert(process);
    }

    /**
     * TODO<新增流程记录><br>
     *
     * @param process 更新流程信息
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/15 15:50
     */
/*    @Override
    public void updateProcess(Process process) {
        processMapper.updateById(process);
    }*/


    /**
     * TODO<新增流程记录><br>
     *更新所有字段
     * @param process 更新流程信息
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/15 15:50
     */
    @Override
    public void updateAllColumnProcess(Process process) {
        processMapper.updateAllColumnById(process);
    }

    @Override
    public void updateProcessStep(String processID, String approveUserID, Integer currentStep) {
        processMapper.updateProcessStep(processID,approveUserID,currentStep);
    }


    /**
     *更新审核节点回退信息
     */
    @Override
    public void updateProcessBack(String processID, Integer backStep, Integer isDirectBack) {
        processMapper.updateProcessBack(processID,backStep,isDirectBack);
    }

    @Override
    public Page<Map<String, Object>> getProcessManagerList(Page<Map<String, Object>> pageInfo, ProcessPageRequest requestInfo) {
        List<Map<String,Object>> result=  this.processMapper.getProcessManagerList(pageInfo,requestInfo);
        pageInfo.setRecords(result);
        return pageInfo;
    }

    /**
     * 返回SQL查询结果
     */
    @Override
    public List<Map<String, Object>> querySql(String sql) {
        List<Map<String ,Object>> result=this.processMapper.querySql(sql);
        return result;
    }


//    public List<Map<String,Object>> selectApprovalUsersByRoleAndArea(String roleId,List<String> areaIds){
//
//    }





    /**
     * 返回流程审核界面相关的信息
     */
//    @Override
//    public ProcessStepApprovalPageInfo getProcessStepApprovalPageInfo(String processID) {
//        ProcessStepApprovalPageInfo pageInfo=new ProcessStepApprovalPageInfo();
//        List<ProcessTypeStep> canReturnList=new ArrayList<>();
//        if(processID!=null&&processID!="")
//        {
//             List<Process> processList= processMapper.selectList(new EntityWrapper<Process>().eq("process_id",processID));
//             if(processList!=null&&processList.size()==1)
//             {
//                 Process process=processList.get(0);
//                 pageInfo.setProcess(process);
//                 if(process.getStatus()==0)
//                 {
//                     //查找可以回退的列表
//                     List<ProcessTypeStep> stepList=   processTypeStepService.getProcessTypeStep(process.getProcessTypeid());
//                     if(stepList!=null)
//                     {
//                         for (ProcessTypeStep processTypeStep : stepList) {
//                             if(processTypeStep.getStep()<process.getCurrentStep())
//                             {
//                                 canReturnList.add(processTypeStep);
//                             }
//                         }
//                     }
//                 }
//             }
//        }
//        pageInfo.setCanReturnStepList(canReturnList);
//        return pageInfo;
//    }

    /**
     * 返回最近的流程信息
     * @param processId 流程ID
     * @param typeId 类别
     * @return com.ht.litigation.service.entity.Process
     * @author 伦惠峰
     * @Date 2018/1/30 11:10
     */
    @Override
    public Process getLastProcess(String processId, String typeId) {
        return processMapper.getLastProcess(processId,typeId);
    }

    /**
     * 获取流程发起相关信息列表
     * @param pages 查询查询实体
     * @param requestInfo 查询条件实体
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author 伦惠峰
     * @Date 2018/1/30 11:07
     */
    @Override
    public Page<Map<String, Object>> getProcessStartList(Page<Map<String, Object>> pages, ProcessStartPageRequest requestInfo) {
        List<Map<String,Object>> result=  this.processMapper.getProcessStartList(pages,requestInfo);
        pages.setRecords(result);
        return pages;
    }

    /**
     * 获取流程信息
     * @param processID
     * @return com.ht.litigation.service.entity.Process
     * @author 伦惠峰
     * @Date 2018/1/16 11:42
     */
    @Override
    public Process getProcess(String processID) {
        Process process = null;
        List<Process> processList = processMapper.selectList(new EntityWrapper<Process>().eq("process_id" , processID));
        if (processList != null && processList.size()>0) {
            process = processList.get(0);
        }
        return process;
    }

    /**
     * 根据流程步骤定义取得当前审批人
     * @param step
     * @param createUser
     * @return
     */
    @Override
    public  String getApproveUserId(ProcessTypeStep step,String createUser,Process process){
        if(step.getApproveUserType()== ProcessApproveUserType.FIXED.getKey()){
            return step.getApproveUserId();
        }else if(step.getApproveUserType()== ProcessApproveUserType.CREATER.getKey()){
            return createUser;
        }else if(step.getApproveUserType()== ProcessApproveUserType.BY_SQL.getKey()){
            List<Map<String, Object>> userIdList  = querySql(step.getApproveUserIdSelectSql());
            StringBuilder sb = new StringBuilder();
            int i=0;
            for(Map<String,Object>  map: userIdList){
                if(i>0){
                    sb.append(",");
                }
                sb.append((String)map.get("id"));
                i++;
            }
            return sb.toString();
        }else if(step.getApproveUserType()== ProcessApproveUserType.BY_ROLE.getKey()){
            String roleCode = step.getApproveUserRole();
            SysRole role =  sysRoleService.selectOne(new EntityWrapper<SysRole>().eq("role_code", roleCode.trim()).eq("page_type", 1));//贷后管理页面权限的
            if(role==null) {
            	logger.error("找不到该角色,role="+roleCode.trim());
            	 throw new RuntimeException("该角色在系统中不存在");
            }
            StringBuilder sb = new StringBuilder();
            int i=0;
            if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.OVERALL.getKey())){//如果角色是全局的
                List<SysUserRole> userRoles  = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("role_code",roleCode).groupBy("user_id"));
                List<SysUserRole> listSysUserRole = new ArrayList<>();
                for(SysUserRole userRole:userRoles){
                	SysUser sysUser = sysUserService.selectById(userRole.getUserId());
                	if(null != sysUser && sysUser.getUserStatus() == 0) {
                		listSysUserRole.add(userRole);
                	}
                }
                if(listSysUserRole.size()==0){
                    logger.error("流程审批角色对应的用户找不到"+"       roleCode:"+roleCode + "" +
                            "    processTypeId: " + step.getTypeId()+  "         step "+step.getStep());
                    throw new RuntimeException("流程审批角色对应的用户找不到");
                }
                for(SysUserRole userRole:listSysUserRole){
                    if(i>0){
                        sb.append(",");
                    }
                    sb.append(userRole.getUserId());
                    i++;
                }
            }else if(role.getRoleAreaType().equals(SysRoleAreaTypeEnums.AREA.getKey())){
                String  businessId = process.getBusinessId();
                BasicBusiness business = basicBusinessService.selectById(businessId);

               // List<String> areas = sysOrgService.getParentsOrgs(business.getCompanyId());

//                List<String> users = sysUserService.selectUsersByRoleAndEare(roleCode,areas);
                List<String> users = sysUserService.selectUserByRoleAndComm(business.getCompanyId(),roleCode);
                if(users.size()==0){
                    logger.error("流程审批角色对应的用户找不到:companyId:"+business.getCompanyId()+"       roleCode:"+roleCode + "" +
                            "    processTypeId: " + step.getTypeId()+  "         step "+step.getStep());
                    throw new RuntimeException("流程审批角色对应的用户找不到");
                }
                for(String  map: users){
                    if(i>0){
                        sb.append(",");
                    }
                    sb.append(map);
                    i++;
                }
            }else{
                throw new RuntimeException("角色的区域范围类型未定义");
            }


            return sb.toString();
        }else{
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param key
     * @return
     */
    @Override
    public Page<ProcessVo> selectProcessVoPage(ProcessReq key){

        Page<ProcessVo> pages = new Page<>();
        pages.setSize(key.getLimit());
        pages.setCurrent(key.getPage());
        serReqInfo(key);
        List<ProcessVo> list = processMapper.selectProcessVoList(pages,key);

        for (ProcessVo processVo : list) {
			if (processVo.getApproveUserId()!=null && processVo.getApproveUserId().equals(key.getCurrentUserId())) {
				processVo.setMyApprove(true);
			}else {
				processVo.setMyApprove(false);
			}
			
		}
        pages.setRecords(setProcessVoListInfo(list));


        return pages;
    }

    private  void  serReqInfo(ProcessReq key){
        if(key.getKeyWord()!=null){
           List<SysUser> list =  sysUserService.selectList(new EntityWrapper<SysUser>().like("user_name",key.getKeyWord()));
           if(list.size()>0){
               List<String> creatUserIds = new LinkedList<>();
               for(SysUser user: list){
                   creatUserIds.add(user.getUserId());
               }
               key.setStartUserIds(creatUserIds);
           }
        }

        List<String> comids = null;
        if(key.getCompanyId()!=null && key.getCompanyId()!=""){
            comids = new LinkedList<>();
            comids.add(key.getCompanyId());
        }
//        List<String> comIds =  basicCompanyService.selectUserSearchComIds(loginUserInfoHelper.getUserId(),null,comids);
        List<String> comIds =  basicCompanyService.selectSearchComids(null,comids);
        if(comIds!=null&&comIds.size()>0){
            key.setCompanyIds(comIds);
        }

        key.setUserId(loginUserInfoHelper.getUserId());
    }

    @Override
    public List<ProcessVo> selectProcessVoList(ProcessReq key){
        serReqInfo(key);
        List<ProcessVo> list = processMapper.selectProcessVoList(key);

        setProcessVoListInfo(list);

        return list;
    }

    //设置流程信息的额外信息
    private List<ProcessVo>  setProcessVoListInfo(List<ProcessVo> list){

        for(ProcessVo vo: list){

//            String statusStr = ProcessStatusEnums.nameOf(vo.getStatus())+"("+ Constant.DEV_DEFAULT_USER+")";

            String statusStr;
            if(vo.getStatus().equals(ProcessStatusEnums.RUNNING.getKey())){
                StringBuilder usb = new StringBuilder();
                if(vo.getApproveUserId()!=null){
                    String[]  userIds = vo.getApproveUserId().split(",");

                    if(userIds.length>0){
                        int i = 0;
                        for(String userId:userIds){
                            SysUser sysUser = sysUserService.selectById(userId);
                            if(sysUser!=null){
                                if(i>0){
                                    usb.append(",");
                                }
                                usb.append(sysUser.getUserName());
                                i++;
                            }
                        }
                    }
                }
                String name = usb.length()>0?usb.toString():vo.getApproveUserId();
                statusStr = ProcessStatusEnums.nameOf(vo.getStatus())+"("+name +")";
            }else if(vo.getStatus().equals(ProcessStatusEnums.END.getKey())){
                statusStr = ProcessStatusEnums.nameOf(vo.getStatus())+"("+ ProcessResultEnums.nameOf(vo.getpResult())+")";
            }else {
                statusStr = ProcessStatusEnums.nameOf(vo.getStatus());
            }
            vo.setProcessStatus(statusStr);
//            vo.setCreateUserName(vo.getCreateUser());

        }
        return list;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveProcessApprovalResult(AuditVo req ,ProcessTypeEnums processTypeEnums) throws IllegalAccessException, InstantiationException{


//        ProcessLog log =  ClassCopyUtil.copyObject(req,ProcessLog.class);
        //流程信息
        Process process = selectById(req.getProcessId());

        if(!process.getCurrentStep().equals(req.getCurrentStep())){
            throw  new AlmsBaseExcepiton("当前流程状态与界面流程状态不一致，请刷新后重新提交!");
        }

        //判断登录用户是否是当前步骤的审批人之一
//        if(!canApprove(process)){  调试暂时屏蔽
//            throw  new RuntimeException("您不是流程的当前审批人!");
//        }

        //流程类型
        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            throw new AlmsBaseExcepiton("流程类型未定义");
        }
        //后一个节点定义
        ProcessTypeStep nextStep = null;

        if(req.getCurrentStep()!=-1) {
        //当前节点定义
        ProcessTypeStep currentStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getCurrentStep());
        if(currentStep == null){
            throw new  AlmsBaseExcepiton("找不到 当前流程节点定义！");
        }

     
        //如果回退则取回退的步骤
        if(Integer.valueOf(ProcessApproveResult.REFUSE.getKey()).equals(req.getIsPass())&&Integer.valueOf(ProcessIsDerateBackEnums.YES.getKey()).equals(req.getIsDirectBack())){
            if(req.getNextStep()==null){
                throw new  AlmsBaseExcepiton("应该设置回退到第几步！");
            }
            nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),req.getNextStep());
            process.setIsDirectBack(ProcessIsDerateBackEnums.YES.getKey());//标识回退
            process.setBackStep(currentStep.getStep());//记录回退的步骤
        }else{
           if(Integer.valueOf(ProcessIsDerateBackEnums.YES.getKey()).equals(process.getIsDirectBack())) {
                //如果流程的是上一步回退的,则取流程中存储的应该跳转的节点
                nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),process.getBackStep());
            }else{//否则直接取后一个
                if(currentStep.getNextStep()!=null){
                    nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),currentStep.getNextStep());
                }
            }
            process.setIsDirectBack(ProcessIsDerateBackEnums.NO.getKey());//取消回退标志
            process.setBackStep(null);//清除回退步骤

        }



        //1.添加log记录
        ProcessLog log=new ProcessLog();
        log.setProcessLogId(UUID.randomUUID().toString());
        log.setProcessId(req.getProcessId());
        log.setProcessName(req.getProcessName());
        log.setTypeId(processType.getTypeId());
        log.setTypeStepId(currentStep.getTypeStepId());
        log.setStepName(currentStep.getStepName());
        log.setApproveUserId(loginUserInfoHelper.getUserId());
        log.setCurrentStep(process.getCurrentStep());
        log.setNextStep(currentStep.getNextStep());
        log.setCreateUser(loginUserInfoHelper.getUserId());
        log.setCreateTime(new Date());
        log.setActionDesc(req.getRemark());
        log.setIsPass(req.getIsPass());
        log.setIsDirectBack(req.getIsDirectBack());
        processLogService.insert(log);

        //添加抄送记录
        for(int i=0;i<req.getSendUserIds().length;i++){
            ProcessLogCopySend copySend = new ProcessLogCopySend();
            copySend.setProcessLogId(log.getProcessLogId());
            copySend.setProcessSendId(UUID.randomUUID().toString());
            copySend.setReceiveUserId(req.getSendUserIds()[i]);
            LoginInfoDto infoDto = loginUserInfoHelper.getUserInfoByUserId(req.getSendUserIds()[i],null);
            copySend.setReceiveUserName(infoDto!=null?infoDto.getUserName():Constant.ADMIN_ID);
            processLogCopySendService.insert(copySend);
        }


        //更新状态
        //如果审批不通过且未定向打回则结束流程
        if(Integer.valueOf(ProcessApproveResult.REFUSE.getKey()).equals(log.getIsPass())&&!Integer.valueOf(ProcessIsDerateBackEnums.YES.getKey()).equals(log.getIsDirectBack())){
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
            process.setProcessResult(ProcessApproveResult.REFUSE.getKey());
        }else{
            if(log.getIsDirectBack()!=null&&!log.getIsDirectBack().equals(ProcessIsDerateBackEnums.YES.getKey())){//定向打回
                if(currentStep.getStepType().equals(ProcessStepTypeEnums.END_STEP.getKey())//节点类型为结束节点则结束流程
                        || currentStep.getNextStep()== null){//没有下一步节点
                    process.setCurrentStep(null);
                    process.setApproveUserId(null);
                    process.setStatus(ProcessStatusEnums.END.getKey());
                    process.setProcessResult(ProcessApproveResult.PASS.getKey());
                }
            }
        }
        }else {
        	process.setStatus(ProcessStatusEnums.RUNNING.getKey());
        	process.setUpdateTime(new Date());
        	process.setUpdateUser(loginUserInfoHelper.getUserId());
        	nextStep = processTypeStepService.getProcessTypeStep(processType.getTypeId(),ProcessTypeEnums.Aply_CarAuction.getBeginStep());
        }
        //更新当前审核人 和当前审核步骤
        if(nextStep !=null){//如果还有下一步
            process.setCurrentStep(nextStep.getStep());
            process.setApproveUserId(getApproveUserId(nextStep,process.getCreateUser(),process));
        }else {
        	process.setStatus(ProcessStatusEnums.END.getKey());
        	//更新流程状态
	        if(ProcessApproveResult.PASS.getKey()==req.getIsPass()) {//如果最后节点通过则整个流程审核通过
	        	process.setProcessResult(ProcessResultEnums.PASS.getKey());
	        }else {
	        	process.setProcessResult(ProcessResultEnums.REFUSE.getKey());
	        }	
        }
        /*else{
            process.setCurrentStep(null);
            process.setApproveUserId(null);
            process.setStatus(ProcessStatusEnums.END.getKey());
        }*/

        //更新操作人和操作时间
        process.setUpdateTime(new Date());
        process.setUpdateUser(loginUserInfoHelper.getUserId());

        updateAllColumnProcess(process);

        //updateProcess(process);

    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Process insertOrUpdateProcess(ProcessSaveReq  processSaveReq, ProcessTypeEnums processTypeEnums){

//        if(!processSaveReq.getProcessStatus().equals(ProcessStatusEnums.NEW.getKey())&&!processSaveReq.getProcessStatus().equals(ProcessStatusEnums.BEGIN.getKey())){
//            throw  new RuntimeException("流程状态应为新建或起始");
//        }
        if(processSaveReq.getBusinessId() == null){
            logger.error("业务ID不能为空  "+processTypeEnums.getName());
            throw new RuntimeException("业务ID不能为空  "+processTypeEnums.getName());
        }

        ProcessType processType = processTypeService.getProcessTypeByCode(processTypeEnums.getKey());
        if(processType == null){
            logger.error("流程类型未定义  "+processTypeEnums.getName());
            throw new RuntimeException("流程类型未定义"  +processTypeEnums.getName());
        }


        //取得 流程的起始步骤
        ProcessTypeStep beginStep = processTypeStepService.getProcessTypeBeginStep(processType.getTypeId());
        if(beginStep==null){
            logger.error("流程未设置起始节点  "+processTypeEnums.getName());
            throw  new RuntimeException("流程未设置起始节点"+ processTypeEnums.getName());
        }
//        ProcessTypeStep step = processTypeStepService.getProcessTypeStep(processType.getTypeId(),beginStep.getNextStep());
//
//        if(step == null){
//            logger.error("流程起始步骤的下一步未定义  "+ processTypeEnums.getName());
//            throw new RuntimeException("流程起始步骤的下一步未定义" + processTypeEnums.getName());
//        }


        Process process = null;

        if(StringUtils.isEmpty(processSaveReq.getProcessId()) ){//保存草稿
            process = new Process();
            process.setProcessId(UUID.randomUUID().toString());
            process.setCreateTime(new Date());
            process.setCreateUser(loginUserInfoHelper.getUserId());
            if(ProcessStatusEnums.RUNNING.getKey()==processSaveReq.getProcessStatus()) {//直接提交审核
            	process.setCurrentStep(beginStep.getStep());
            }else {
            process.setCurrentStep(-1);//保存草稿设置当前步骤为-1
            }
            process.setProcessEngineFlage(ProcessEngineFlageEnums.LOCAL_SIMPLE_ENGINE.getKey());
            process.setProcessDesc(processSaveReq.getDesc());


            process.setProcessName(processSaveReq.getTitle());
            process.setProcessTypeid(processType.getTypeId());
            process.setStatus(processSaveReq.getProcessStatus());
            process.setBusinessId(processSaveReq.getBusinessId());
            process.setStartTime( new Date());
            process.setStartUserId(loginUserInfoHelper.getUserId());
            //设置审核人
            process.setApproveUserId(getApproveUserId(beginStep,process.getCreateUser(),process));
            process.setIsDirectBack(ProcessIsDerateBackEnums.NO.getKey());
        }else{//提交审核
            process = selectById(processSaveReq.getProcessId());
            if(process==null) {
            	logger.error("该流程在系统中不存在  "+ processSaveReq.getProcessId());
                throw new RuntimeException("该流程在系统中不存在" + processSaveReq.getProcessId());
            }
            if(process.getCurrentStep()==-1) {
            	process.setCurrentStep(beginStep.getStep());
            }
            if(processSaveReq.getProcessStatus()!= process.getStatus()){
                process.setStatus(processSaveReq.getProcessStatus());


            }
        }

        //更新或插入
        insertOrUpdateAllColumn(process);
        return process;
    }
    
    private String[] addElementToArray(String[] arr,String id) {
    	String[] result=new String[arr.length+1];
    	for(int i=0;i<arr.length;i++) {
    		result[i]=arr[i];
    	}
    	result[result.length-1]=id;
    	return result;
    }
}
