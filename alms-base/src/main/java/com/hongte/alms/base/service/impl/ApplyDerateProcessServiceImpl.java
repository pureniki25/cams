package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.ProcessEngineFlageEnums;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.process.entity.*;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.enums.ProcessStepTypeEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.*;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.ApplyDerateProcessService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@Service("ApplyDerateProcessService")
@Transactional
public class ApplyDerateProcessServiceImpl extends BaseServiceImpl<ApplyDerateProcessMapper, ApplyDerateProcess> implements ApplyDerateProcessService {

    @Autowired
    @Qualifier("ProcessTypeService")
    ProcessTypeService processTypeService;


    @Autowired
    @Qualifier("ProcessTypeStepService")
    ProcessTypeStepService processTypeStepService;

    @Autowired
    @Qualifier("ProcessService")
    ProcessService processService;

    @Autowired
    @Qualifier("ProcessLogService")
    ProcessLogService processLogService;

    @Autowired
    @Qualifier("ProcessLogCopySendService")
    ProcessLogCopySendService processLogCopySendService;

    @Autowired
    ApplyDerateProcessMapper applyDerateProcessMap;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;

    @Autowired
//    @Qualifier("loginUserInfoHelper")
    LoginUserInfoHelper loginUserInfoHelper;


    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveApplyDerateProcess(ApplyDerateProcessReq req) throws IllegalAccessException, InstantiationException {

        ApplyDerateProcess applyInfo = null;

        ProcessSaveReq processSaveReq = new ProcessSaveReq();
        processSaveReq.setBusinessId(req.getBusinessId());
        processSaveReq.setProcessStatus(req.getProcessStatus());
        processSaveReq.setTitle(req.getTitle());
        processSaveReq.setProcessId(req.getProcessId());

        Process process = processService.saveProcess(processSaveReq,ProcessTypeEnums.Apply_Derate);

        applyInfo = ClassCopyUtil.copyObject(req,ApplyDerateProcess.class);

        if(req.getApplyDerateProcessId()==null){
            applyInfo.setApplyDerateProcessId(UUID.randomUUID().toString());
            applyInfo.setProcessId(process.getProcessId());
            applyInfo.setCreateTime(new Date());
            applyInfo.setCreateUser(Constant.DEV_DEFAULT_USER);

        }
        applyInfo.setUpdateTime(new Date());
        applyInfo.setUpdateUser(Constant.DEV_DEFAULT_USER);

        insertOrUpdate(applyInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveApplyDerateProcessLog(ProcessLogReq req) throws IllegalAccessException, InstantiationException {

        //如果是创建者 需要重新存储审批申请信息
        if(req.getIsCreaterFlage()){
//            ApplyDerateProcess  derateInfo = selectOne(new EntityWrapper<ApplyDerateProcess>().eq("process_id",req.getProcess().getProcessId()));
            ApplyDerateProcess derateInfo = ClassCopyUtil.copyObject(req.getApplyInfo(),ApplyDerateProcess.class);
            if(derateInfo.getApplyDerateProcessId() == null){
                throw  new  RuntimeException("无减免申请信息记录！");
            }
            insertOrUpdate(derateInfo);

        }

        //存储审批结果信息
        processService.saveProcessApprovalResult(req ,ProcessTypeEnums.Apply_Derate);

        //当前节点定义
        ProcessTypeStep currentStep = processService.getStepByPTypeStepCode(ProcessTypeEnums.Apply_Derate,req.getProcess().getCurrentStep());

        //存储减免后实收（最后一个步骤填写）
        if(currentStep.getStepType().equals(ProcessStepTypeEnums.END_STEP)){
            ApplyDerateProcess applyDerateProcess = selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",req.getProcess().getProcessId())).get(0);
            if(req.getRealReceiveMoney() == null){
                throw new RuntimeException("实收金额不能为空");
            }
            applyDerateProcess.setRealReceiveMoney(req.getRealReceiveMoney());
            updateById(applyDerateProcess);
        }

    }

    /**
     * 分页查询
     *
     * @param key
     * @return
     */
    @Override
    public Page<ApplyDerateVo> selectApplyDeratePage(ApplyDerateListSearchReq key){

        Page<ApplyDerateVo> pages = new Page<>();
        pages.setSize(key.getLimit());
        pages.setCurrent(key.getPage());

        String userId = loginUserInfoHelper.getUserId();
        key.setUserId(userId);

        List<ApplyDerateVo> list = applyDerateProcessMap.selectApplyDerateList(pages,key);

        pages.setRecords(setApplyDerateVoListInfo(list));


        return pages;
    }



    @Override
    public List<ApplyDerateVo> selectApplyDerateList(ApplyDerateListSearchReq key){

        List<ApplyDerateVo> list = applyDerateProcessMap.selectApplyDerateList(key);

        setApplyDerateVoListInfo(list);

        return list;
    }

    //设置申请信息的额外信息
    private List<ApplyDerateVo>  setApplyDerateVoListInfo(List<ApplyDerateVo> list){

        for(ApplyDerateVo vo: list){

            List<BasicCompany> arealist =  basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_id",vo.getDistrictId()));
            if(arealist.size()>0){
                vo.setDistrictAreaName(arealist.get(0).getAreaName());
            }

            SysUser sysUser = sysUserService.selectById(vo.getCreaterId());
            String userName = sysUser!=null?sysUser.getUserName():vo.getCreaterId();
            vo.setCreaterName(userName);


//            LoginInfoDto uInfo = loginUserInfoHelper.getLoginInfo();
            vo.setDerateTypeName(sysParameterService.seleByParamTypeAndvalue(SysParameterTypeEnums.DERATE_TYPE, vo.getDerateTypeId()).getParamName());
//            vo.setShowPayMoney("100");     //后续计算
        }
        return list;
    }


}
