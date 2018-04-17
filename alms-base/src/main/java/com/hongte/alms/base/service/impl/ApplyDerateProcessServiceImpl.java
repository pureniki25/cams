package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.controller.ExpenseSettleController;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.ApplyDerateProcessOtherFees;
import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.ProcessEngineFlageEnums;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.process.entity.*;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.enums.ProcessStepTypeEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.*;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.ApplyDerateProcessOtherFeesService;
import com.hongte.alms.base.service.ApplyDerateProcessService;
import com.hongte.alms.base.service.ApplyDerateTypeService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

import feign.Feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.hongte.alms.base.entity.SysParameter;
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
	
	private Logger logger = LoggerFactory.getLogger(ApplyDerateProcessServiceImpl.class);

	@Value("${bmApi.apiUrl}")
	String xindaiAplUrlUrl ;
    @Autowired
    @Qualifier("ProcessTypeService")
    ProcessTypeService processTypeService;
    @Autowired
    @Qualifier("ApplyDerateTypeService")
    ApplyDerateTypeService applyDerateTypeService;
    
    @Autowired
    @Qualifier("ApplyDerateProcessOtherFeesService")
    ApplyDerateProcessOtherFeesService applyDerateProcessOtherFeesService;

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
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

    

    @Autowired
//    @Qualifier("loginUserInfoHelper")
    LoginUserInfoHelper loginUserInfoHelper;


    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService; 


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveApplyDerateProcess(ApplyDerateProcessReq req,List<ApplyDerateType>  types,List<SysParameter> params,String outsideInterest,String generalReturnRate,String preLateFees) throws IllegalAccessException, InstantiationException {
    
        ApplyDerateProcess applyInfo = null;
        
        String businessId = req.getBusinessId();
        String crpId = req.getCrpId();
        String currentStatus = applyDerateProcessMap.queryCurrentStatusByCondition(businessId, crpId);
        if ("已还款".equals(currentStatus)) {
			throw new ServiceException("当期已还款，不能发起减免申请！");
		}

        ProcessSaveReq processSaveReq = new ProcessSaveReq();
		processSaveReq.setBusinessId(businessId);
        processSaveReq.setProcessStatus(req.getProcessStatus());
        processSaveReq.setTitle(req.getTitle());
        processSaveReq.setProcessId(req.getProcessId());

        Process process = processService.saveProcess(processSaveReq,ProcessTypeEnums.Apply_Derate);

         applyInfo = ClassCopyUtil.copyObject(req,ApplyDerateProcess.class);

        if(req.getApplyDerateProcessId()==null){
            applyInfo.setApplyDerateProcessId(UUID.randomUUID().toString());
            applyInfo.setProcessId(process.getProcessId());
            applyInfo.setCreateTime(new Date());
            applyInfo.setCreateUser(loginUserInfoHelper.getUserId());
            applyInfo.setGeneralReturnRate(generalReturnRate);//综合收益率
            applyInfo.setOutsideInterest(StringUtil.notEmpty(outsideInterest)?BigDecimal.valueOf(Double.valueOf(outsideInterest)):BigDecimal.valueOf(0.0));//应付逾期利息
            applyInfo.setPreLateFees(StringUtil.notEmpty(preLateFees)?BigDecimal.valueOf(Double.valueOf(preLateFees)):BigDecimal.valueOf(0.0));//提前还款违约金
        }
        applyInfo.setUpdateTime(new Date());
        applyInfo.setUpdateUser(loginUserInfoHelper.getUserId());

        insertOrUpdate(applyInfo);
        RepaymentBizPlanList pList= repaymentBizPlanListService.selectById(crpId);
        //减免金额        
        ApplyDerateType applyDerateType = null;
        ArrayList<ApplyDerateType> newTypes=new ArrayList<ApplyDerateType>();
        for(ApplyDerateType item:types) {
        	applyDerateType = ClassCopyUtil.copyObject(item,ApplyDerateType.class);
        	if(item.getApplyDerateTypeId()==null|"".equals(item.getApplyDerateTypeId())) {
        		applyDerateType.setApplyDerateTypeId(UUID.randomUUID().toString());
        		applyDerateType.setApplyDerateProcessId(applyInfo.getApplyDerateProcessId());
        		RepaymentBizPlanListDetail detail=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", req.getCrpId()).eq("business_id", pList.getBusinessId()).eq("fee_id", applyDerateType.getFeeId())).get(0);
        		applyDerateType.setFeeId(applyDerateType.getFeeId());
        		applyDerateType.setDerateType(detail.getPlanItemType().toString());
        		applyDerateType.setCreateTime(new Date());
        		applyDerateType.setBeforeDerateMoney(detail.getPlanAmount());
        		applyDerateType.setCreateUser(loginUserInfoHelper.getUserId());
        	}
        
        	applyDerateType.setUpdateTime(new Date());
        	applyDerateType.setUpdateUser(loginUserInfoHelper.getUserId());
        	
        	newTypes.add(applyDerateType);
        }
  
        applyDerateTypeService.insertOrUpdateBatch(newTypes);
        //保存其他费用
        List<ApplyDerateProcessOtherFees> fees=new ArrayList(); 

        
        params.forEach(item->{
        	if(StringUtil.notEmpty(item.getParamValue2())) {
        		  ApplyDerateProcessOtherFees fee=new ApplyDerateProcessOtherFees();
        		  fee.setPlanDetailId(UUID.randomUUID().toString());
        		  fee.setBusinessId(pList.getBusinessId());
        		  fee.setPlanListId(pList.getPlanListId());
        		  fee.setPeriod(pList.getPeriod());
        		  fee.setPlanItemName(item.getParamName());
        		  fee.setPlanItemType(Integer.valueOf(item.getParamValue3()));
        		  fee.setFeeId(item.getParamValue());
        		  fee.setPlanAmount(BigDecimal.valueOf(Double.valueOf(item.getParamValue2())));
        		  fee.setAccountStatus(0);//0：不线上分账
        		  fee.setCreateDate(new Date());
        		  fee.setCreateUser(loginUserInfoHelper.getUserId());
        		  fees.add(fee);
        	}
        	
        });
        if(fees.size()>0) {
        	  applyDerateProcessOtherFeesService.insertBatch(fees);	
        }
        
        
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
        boolean isFinish=false;
        ApplyTypeVo vo=null;
        //当前节点定义
        ProcessTypeStep currentStep = processService.getStepByPTypeStepCode(ProcessTypeEnums.Apply_Derate,req.getProcess().getCurrentStep());
        if(null!=currentStep.getNextStepSelectSql()&&(!"".equals(currentStep.getNextStepSelectSql().trim()))) {
        	vo=applyDerateTypeService.getApplyTypeVo(req.getProcess().getProcessId());
        	//车贷减免流程：减免金额<= 10000或者房贷减免流程：减免金额<= 20000 ，流程到区域贷后主管审批就结束
        	if((vo.getBusinessTypeId()==BusinessTypeEnum.CYD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("10000"))<=0)
        			||vo.getBusinessTypeId()==BusinessTypeEnum.FSD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("20000"))<=0) {
        		currentStep.setStepType(ProcessStepTypeEnums.END_STEP.getKey());
        		isFinish=true;
        	}

        }
        if(currentStep.getStepType()==ProcessStepTypeEnums.END_STEP.getKey()) {
        	isFinish=true;
        }
        

        //存储审批结果信息
        processService.saveProcessApprovalResultDerate(req ,ProcessTypeEnums.Apply_Derate,isFinish,vo);

      

        //存储减免后实收（最后一个步骤填写）
        if(currentStep.getStepType()==ProcessStepTypeEnums.END_STEP.getKey()){
            ApplyDerateProcess applyDerateProcess = selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",req.getProcess().getProcessId())).get(0);
            if(req.getRealReceiveMoney() == null){
                throw new RuntimeException("实收金额不能为空");
            }
            applyDerateProcess.setRealReceiveMoney(req.getRealReceiveMoney());
            updateById(applyDerateProcess);
            
            //把新增的费用项插入到还款计划detail表里
            RepaymentBizPlanList pList=repaymentBizPlanListService.selectById(req.getCrpId());
            if(pList!=null) {
            List<ApplyDerateProcessOtherFees> otherFeesList=applyDerateProcessOtherFeesService.selectList(new EntityWrapper<ApplyDerateProcessOtherFees>().eq("business_id", pList.getBusinessId()).eq("plan_list_id", pList.getPlanListId()));
            for(ApplyDerateProcessOtherFees fee:otherFeesList) {	
            RepaymentBizPlanListDetail detail = ClassCopyUtil.copyObject(fee,RepaymentBizPlanListDetail.class);
            repaymentBizPlanListDetailService.insert(detail);
            
            }
            
        }
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

	public ResponseData callRemoteService(String businessId) throws RuntimeException {
		logger.info("调用callRemoteService");
		if (xindaiAplUrlUrl==null) {
			logger.error("xindaiAplUrlUrl==null!!!");
			return null ;
		}
		logger.info("xindaiAplUrlUrl:"+xindaiAplUrlUrl);
		DESC desc = new DESC();
		RequestData requestData = new RequestData();
		requestData.setMethodName("AfterLoanRepayment_GetFeeList");
		JSONObject data = new JSONObject() ;
		data.put("businessId", businessId);
		requestData.setData(data.toJSONString());
		logger.info("原始数据-开始");
		logger.info(JSON.toJSONString(requestData));
		logger.info("原始数据-结束");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = desc.Encryption(encryptStr);
		logger.info("请求数据-开始");
		logger.info(encryptStr);
		logger.info("请求数据-结束");
		XindaiService xindaiService = Feign.builder().target(XindaiService.class, xindaiAplUrlUrl);
		String response = xindaiService.dod(encryptStr);

		// 返回数据解密
		ResponseEncryptData resp = JSON.parseObject(response, ResponseEncryptData.class);
		String decryptStr = desc.Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		
		logger.info("信贷返回数据解密-开始");
		logger.info(JSON.toJSONString(respData));
		logger.info("信贷返回数据解密-结束");
		return respData ;
	}
}
