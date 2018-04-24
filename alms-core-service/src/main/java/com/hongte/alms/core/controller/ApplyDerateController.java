package com.hongte.alms.core.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.PhoneUrgeService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.ApplyDerateProcessOtherFees;
import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessStepTypeEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessLogService;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.process.vo.ProcessLogVo;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.storage.StorageService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import com.ht.ussp.util.BeanUtils;

import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 申请减免 前端控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
@RestController
@RequestMapping("/ApplyDerateController")
public class ApplyDerateController {


    private Logger logger = LoggerFactory.getLogger(ApplyDerateController.class);
	@Autowired
	@Qualifier("TransferOfLitigationService")
	private TransferOfLitigationService transferOfLitigationService;

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;
    
    @Autowired
    @Qualifier("ApplyDerateTypeService")
    ApplyDerateTypeService applyDerateTypeService;
    
    @Autowired
    @Qualifier("ProcessTypeStepService")
    ProcessTypeStepService processTypeStepService;

    @Autowired
    @Qualifier("ProcessTypeService")
    ProcessTypeService processTypeService;

    @Autowired
    @Qualifier("ProcessService")
    ProcessService processService;

    @Autowired
    @Qualifier("ApplyDerateProcessService")
    ApplyDerateProcessService applyDerateProcessService;


    @Autowired
    @Qualifier("ProcessLogService")
    ProcessLogService processLogService;


    @Autowired
    @Qualifier("CollectionStatusService")
    CollectionStatusService collectionStatusService;
    @Autowired
    @Qualifier("CollectionLogService")
    CollectionLogService collectionLogService;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("BasicBusinessTypeService")
    BasicBusinessTypeService basicBusinessTypeService;

    @Autowired
    @Qualifier("PhoneUrgeService")
    PhoneUrgeService phoneUrgeService;

    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;
    
    @Autowired
	@Qualifier("DocTypeService")
	private DocTypeService docTypeService;

    @Autowired
	@Qualifier("DocService")
	private DocService docService;

	@Autowired
	@Qualifier("DocTmpService")
	private DocTmpService docTmpService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;
	
	@Autowired
	@Qualifier("ApplyDerateProcessOtherFeesService")
	ApplyDerateProcessOtherFeesService applyDerateProcessOtherFeesService;

    @Autowired
//    @Qualifier("storageService")
     StorageService storageService;

    @Autowired
//    @Qualifier("loginUserInfoHelper")
            LoginUserInfoHelper loginUserInfoHelper;

    @ApiOperation(value = "查找申请减免界面显示信息")
    @GetMapping("/selectApplyDeratePageShowInfo")
    @ResponseBody
    public Result<Map<String,Object>> selectApplyDeratePageShowInfo(
            @RequestParam("crpId") String crpId,
            @RequestParam("afterId") String afterId,
            
            @RequestParam(value = "processId",required = false) String processId
    ){
        Map<String,Object> retMap = new HashMap<>();



      Integer isDefer=0;
        try{
        	if(afterId!=null&&afterId.startsWith("ZQ")) {
        		isDefer=1;
        	}
            //基本信息
            List<BusinessInfoForApplyDerateVo> businessVoList =  basicBusinessService.selectBusinessInfoForApplyDerateVo(crpId,isDefer);
     
            //还款方式类型列表
            List<SysParameter> repayTypeList =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.REPAYMENT_TYPE.getKey()).orderBy("row_Index"));
            List<SysParameter> otherDerateTypeList=null;
            if (!CollectionUtils.isEmpty(businessVoList)) {
            	//赋值还款方式
            	for(SysParameter sys:repayTypeList) {
            		if(sys.getParamValue().equals(businessVoList.get(0).getRepaymentTypeId().toString())) {
            			businessVoList.get(0).setRepaymentTypeName(sys.getParamName());
            		}
          
            	}
             
       		   //前置费用
        	    BigDecimal preFees=	preFees=basicBusinessService.getPreChargeAndPreFees(businessVoList.get(0).getBusinessId());
    		    businessVoList.get(0).setPreFees(preFees);
    		    //出款后已交月收等费用总额:
    		    businessVoList.get(0).setSumFactAmount(basicBusinessService.getMonthSumFactAmount(businessVoList.get(0).getBusinessId())==null?BigDecimal.valueOf(0):BigDecimal.valueOf(basicBusinessService.getMonthSumFactAmount(businessVoList.get(0).getBusinessId())));
    		    //提前结清该业务实还金额
    		    businessVoList.get(0).setSettleTotalFactAmount(basicBusinessService.getSettleTotalFactSum(businessVoList.get(0).getBusinessId())==null?BigDecimal.valueOf(0):BigDecimal.valueOf(basicBusinessService.getSettleTotalFactSum(businessVoList.get(0).getBusinessId())));
    		    //提前结清（所有还款计划中）的应还利息 ,（所有还款计划中）的应还月收服务费
    		    Map<String,Object> map=basicBusinessService.getNeedPay(businessVoList.get(0).getBusinessId());
    		    businessVoList.get(0).setSettleNeedPayInterest( StringUtil.notEmpty(map.get("settleNeedPayInterest").toString())?BigDecimal.valueOf(Double.valueOf(map.get("settleNeedPayInterest").toString())):BigDecimal.valueOf(0));
    		    businessVoList.get(0).setSettleNeedPayService( StringUtil.notEmpty(map.get("settleNeedPayService").toString())?BigDecimal.valueOf(Double.valueOf(map.get("settleNeedPayService").toString())):BigDecimal.valueOf(0));
    		    businessVoList.get(0).setNoSettleNeedPayInterest(businessVoList.get(0).getNeedPayInterest());
    		    businessVoList.get(0).setNoSettleNeedPayPrincipal(businessVoList.get(0).getNeedPayPrincipal());
    		    businessVoList.get(0).setNoSettleNeedPayService(businessVoList.get(0).getNeedPayService());
    		    
    		    
    		    
    		    //判断是车贷还是房贷的减免费用项
    		    if(businessVoList.get(0).getBusinessType()==BusinessTypeEnum.CYD_TYPE.getValue()||businessVoList.get(0).getBusinessType()==BusinessTypeEnum.CYDZQ_TYPE.getValue()) {
    		    	otherDerateTypeList= sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.CAR_DERATE_TYPE.getKey()).orderBy("row_Index"));
    		    }else if(businessVoList.get(0).getBusinessType()==BusinessTypeEnum.FSD_TYPE.getValue()||businessVoList.get(0).getBusinessType()==BusinessTypeEnum.FSDZQ_TYPE.getValue()) {
    		    	otherDerateTypeList= sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.HOUSE_DERATE_TYPE.getKey()).orderBy("row_Index"));

    		    }
    		    if(otherDerateTypeList.size()%3>0) {
    		    	int length=otherDerateTypeList.size()%3;
    		    	
    		    		while(otherDerateTypeList.size()%3!=0) {
    		    			otherDerateTypeList.add(new SysParameter());
    		    		}
    		    }
    		    retMap.put("otherDerateTypeList", (JSONArray) JSON.toJSON(otherDerateTypeList, JsonUtil.getMapping()));
            	
			}
            retMap.put("baseInfo", (JSONArray) JSON.toJSON(businessVoList, JsonUtil.getMapping()));
//            derateTypeList
            //申请类型列表
            RepaymentBizPlanList pList=repaymentBizPlanListService.selectById(crpId);
            if(pList!=null) {
            List<RepaymentBizPlanListDetail> derateTypeList=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", pList.getBusinessId()).eq("plan_list_id", crpId).and().ne("plan_item_name","本金" ).groupBy("fee_id"));
            //List<SysParameter> derateTypeList =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.DERATE_TYPE.getKey()).orderBy("row_Index"));
            retMap.put("derateTypeList",(JSONArray) JSON.toJSON(derateTypeList, JsonUtil.getMapping()));
             
            }
            
            

            //申请减免的信息
            if(processId!=null){
                List<ApplyDerateProcess>  applyList = applyDerateProcessService.selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",processId));
                List<ApplyDerateType> types=null;
                if(applyList!=null&&applyList.size()>0) {
                    //申请减免的项目类型集合
                	types=applyDerateTypeService.selectList(new EntityWrapper<ApplyDerateType>().eq("apply_derate_process_id",applyList.get(0).getApplyDerateProcessId()));
                }
             
                retMap.put("applyList",(JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
                retMap.put("applyTypes",(JSONArray) JSON.toJSON(types, JsonUtil.getMapping()));
                
                //判断当前节点时候符合结束节点条件
                Process process=processService.getProcess(processId);
                //otherFeeEditFlage 0:新增 -1:草稿  1：修改
                if(process.getStatus()==-1) {
                	
                	   retMap.put("otherFeeEditFlage", JSON.toJSON("-1", JsonUtil.getMapping()));
                }else {
                	  retMap.put("otherFeeEditFlage", JSON.toJSON("1", JsonUtil.getMapping()));
                }
                //如果currentStep=null,说明流程已经结束
                if(process.getCurrentStep()!=null) {
                    ProcessTypeStep currentStep = processService.getStepByPTypeStepCode(ProcessTypeEnums.Apply_Derate,process.getCurrentStep());
                    if(null!=currentStep.getNextStepSelectSql()&&(!"".equals(currentStep.getNextStepSelectSql().trim()))) {
                    	ApplyTypeVo vo=applyDerateTypeService.getApplyTypeVo(process.getProcessId());
                    	//车贷减免流程：减免金额<= 10000或者房贷减免流程：减免金额<= 20000 ，流程到区域贷后主管审批就结束
                    	if((vo.getBusinessTypeId()==BusinessTypeEnum.CYD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("10000"))<=0)
                    			||vo.getBusinessTypeId()==BusinessTypeEnum.FSD_TYPE.getValue()&&vo.getDerateMoney().compareTo(new BigDecimal("20000"))<=0) {
                    		   retMap.put("realReceiveMoneyEditFlage", JSON.toJSON("true", JsonUtil.getMapping()));
                    	}

                    }
                }
           
                //查询其他费用项
                List<ApplyDerateProcessOtherFees> otherFees= applyDerateProcessOtherFeesService.selectList(new EntityWrapper<ApplyDerateProcessOtherFees>().eq("plan_list_id", crpId).eq("business_id", pList.getBusinessId()).eq("apply_derate_process_id", applyList.get(0).getApplyDerateProcessId()));
                if(otherFees!=null&&otherFees.size()%3>0) {
    		    	int length=otherFees.size()%3;
    		    	
    		    		while(otherFees.size()%3!=0) {
    		    			otherFees.add(new ApplyDerateProcessOtherFees());
    		    		}
    		    }
                retMap.put("otherFees",(JSONArray) JSON.toJSON(otherFees, JsonUtil.getMapping()));
                
            }else {
            	  //otherFeeEditFlage 0:新增 
                	
                	   retMap.put("otherFeeEditFlage", JSON.toJSON("0", JsonUtil.getMapping()));
            }

            processService.getProcessShowInfo(retMap,processId, ProcessTypeEnums.Apply_Derate);
            
            String businessId = "";
            
            if (!CollectionUtils.isEmpty(businessVoList)) {
            	businessId = businessVoList.get(0).getBusinessId();
            		
			}
            
            
       
            
         // 查询附件
			List<DocType> docTypes = docTypeService
					.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_Reduction"));
			if (docTypes != null && docTypes.size() == 1) {
				List<Doc> fileList = docService.selectList(new EntityWrapper<Doc>()
						.eq("doc_type_id", docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
				retMap.put("returnRegFiles", fileList);
			}

/*


            //传递流程信息到前端
            Process process = null;
            if(processId !=null){
                process =  processService.getProcess(processId);
                if(process !=null){
                    List<Process> l = new LinkedList<>(); 
                    l.add(process);
                    retMap.put("process",(JSONArray) JSON.toJSON(l, JsonUtil.getMapping()));
                }

                //历史审核记录  开始
                List<ProcessLog> logs =  processLogService.getProcessLogList(processId);
                List<ProcessLogVo>  logsVo = new LinkedList<ProcessLogVo>();
                for(ProcessLog log : logs){
                    ProcessLogVo vo  = ClassCopyUtil.copyObject(log,ProcessLogVo.class);
//                    ProcessTypeStep step = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID,log.getCurrentStep());
//                    vo.setStepName(step.getStepName());
                    vo.setApproveUserName(log.getApproveUserId());
                    logsVo.add(vo);
                }
                retMap.put("processLogs",(JSONArray) JSON.toJSON(logsVo, JsonUtil.getMapping()));
                //历史审核记录  结束

                //申请减免的信息
                List<ApplyDerateProcess>  applyList = applyDerateProcessService.selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",processId));
                retMap.put("applyList",(JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));

            }

            //流程列表
            List<ProcessTypeStep>  stepList = processTypeStepService.getProcessTypeStep(Constant.APPLY_DERATE_PROCEEE_TYPE_ID);
            JSONArray stepArray = processTypeStepService.getProcessTypeStepArray(stepList,process);
            retMap.put("stepArray",stepArray);


            //当前审核信息
            if(process!=null){
                retMap.put("approveUserName",process.getApproveUserId());
                ProcessTypeStep currentStepInfo  =  processTypeStepService.findCurrentStepInfo(stepList,process);
                retMap.put("currentStepName",currentStepInfo!=null?currentStepInfo.getStepName():"");
            }else{
                retMap.put("approveUserName","");
                retMap.put("currentStepName","");
            }
            retMap.put("approveDate",new Date());


            //可选抄送人列表*/

            return Result.success(retMap);

        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }
    
    
    
    
    
    
    
    @ApiOperation(value = "车贷:获取提前结清违约金和滞纳金")
    @GetMapping("/getPreLateFees")
    @ResponseBody
    public Result<Map<String,Object>> getPreLateFees(
            @RequestParam("crpId") String crpId,
            @RequestParam(value="afterId") String afterId,
            @RequestParam(value = "preLateFeesType") String preLateFeesType
    ){
    	
    	
        Map<String,Object> retMap = new HashMap<>();
        try{
        	  Integer isDefer=0;
              	if(afterId!=null&&afterId.startsWith("ZQ")) {
              		isDefer=1;
              	}
            //基本信息
            List<BusinessInfoForApplyDerateVo> businessVoList =  basicBusinessService.selectBusinessInfoForApplyDerateVo(crpId,isDefer);
     
            String businessId="";
            if (!CollectionUtils.isEmpty(businessVoList)) {
            	
            	businessId=businessVoList.get(0).getBusinessId();
            	
			}

            CarLoanBilVO carLoanBilVO=new CarLoanBilVO();
            carLoanBilVO.setBillDate(new Date());
            carLoanBilVO.setBusinessId(businessId);
            if(null!=preLateFeesType&&!"".equals(preLateFeesType)) {
            	 carLoanBilVO.setPreLateFees(Integer.parseInt(preLateFeesType));
            }
           
            //车贷：应付提前结清违约金
        	Map<String, Object> resultMap = transferOfLitigationService.carLoanBilling(carLoanBilVO);
        	if(resultMap!=null) {
        		   retMap.put("preLateFees", JSON.toJSON(resultMap.get("preLateFees"), JsonUtil.getMapping()));// 提前还款违约金
        		   
        		   
         		    retMap.put("previousFees", (JSONArray) JSON.toJSON(resultMap.get("previousFees"), JsonUtil.getMapping()));
                	
        	}else {
        		   retMap.put("preLateFees", JSON.toJSON(0, JsonUtil.getMapping()));// 提前还款违约金
        	}
         

      
            return Result.success(retMap);

        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }
    @ApiOperation(value = "车贷:获取应付逾期利息")
    @GetMapping("/getOutsideInterest")
    @ResponseBody
    public Result<Map<String,Object>> getOutsideInterest(
            @RequestParam("crpId") String crpId,
            @RequestParam("afterId") String afterId,
            @RequestParam(value = "outsideInterestType") String outsideInterestType
    ){
        Map<String,Object> retMap = new HashMap<>();
        try{
        	 Integer isDefer=0;
           	if(afterId!=null&&afterId.startsWith("ZQ")) {
           		isDefer=1;
           	}
        	
            //基本信息
            List<BusinessInfoForApplyDerateVo> businessVoList =  basicBusinessService.selectBusinessInfoForApplyDerateVo(crpId,isDefer);
     
            String businessId="";
            if (!CollectionUtils.isEmpty(businessVoList)) {
            	
            	businessId=businessVoList.get(0).getBusinessId();
            	
			}

            CarLoanBilVO carLoanBilVO=new CarLoanBilVO();
            carLoanBilVO.setBillDate(new Date());
            carLoanBilVO.setBusinessId(businessId);
        
            carLoanBilVO.setOutsideInterest(Double.valueOf(StringUtil.isEmpty(outsideInterestType)?"0":outsideInterestType));
            
           
            //车贷：应付逾期利息
        	Map<String, Object> resultMap = transferOfLitigationService.carLoanBilling(carLoanBilVO);
        	
            retMap.put("outsideInterest", JSON.toJSON(resultMap.get("outsideInterest"), JsonUtil.getMapping()));//

      
            return Result.success(retMap);

        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }
    @ApiOperation(value = "根据流程ID查找减免申请信息")
    @GetMapping("/getApplyDerateInfoByProcessId")
    @ResponseBody
    public Result<ApplyDerateProcess> getApplyDerateInfoByProcessId(
            @RequestParam("processId") String processId
    ){
        List<ApplyDerateProcess>  pList = applyDerateProcessService.selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",processId));

        if(pList.size()>0){
            return Result.success(pList.get(0));
        }else{
            return Result.error("500","无数据");
        }

    }


    @ApiOperation(value = "存储申请减免的信息")
    @PostMapping("/saveApplyDerateInfo")
    @ResponseBody
    public Result<String> saveApplyDerateInfo(
            @RequestBody Map<String, Object> reqMap){
    	
        Result result = new Result();
        try{
        	List<FileVo> files = JsonUtil.map2objList(reqMap.get("reqRegFiles"), FileVo.class);
        	List<ApplyDerateProcessReq> req = JsonUtil.map2objList(reqMap.get("applyData"), ApplyDerateProcessReq.class);
        	List<ApplyDerateType>  types=JsonUtil.map2objList(reqMap.get("applytTypes"), ApplyDerateType.class);
           	List<SysParameter>  otherDerateTypeList=JsonUtil.map2objList(reqMap.get("otherDerateTypeList"), SysParameter.class);
          	String outsideInterest=reqMap.get("outsideInterest")==null?"0":reqMap.get("outsideInterest").toString();//合同期外逾期利息
          	String generalReturnRate=reqMap.get("generalReturnRate").toString();//综合收益率
          	String preLateFees=reqMap.get("preLateFees").toString(); //提前还款违约金:
        	
        	if (!CollectionUtils.isEmpty(req)) {
        		applyDerateProcessService.saveApplyDerateProcess(req.get(0),types,otherDerateTypeList,outsideInterest,generalReturnRate,preLateFees);
			}
        	if (!CollectionUtils.isEmpty(files)) {
    			for (FileVo file : files) {
    				DocTmp tmp = docTmpService.selectById(file.getOldDocId());// 将临时表保存的上传信息保存到主表中
    				if (tmp != null) {
    					Doc doc = new Doc();
    					BeanUtils.copyProperties(tmp, doc);
    					doc.setOriginalName(file.getOriginalName());
    					docService.insertOrUpdate(doc);
    				}
    			}
    		}
            
            return Result.success();
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

   @ApiOperation(value = "存储减免的审批信息")
    @PostMapping("/saveApprovalLogInfo")
    @ResponseBody
    public Result<String> saveApprovalLogInfo(
            @RequestBody ProcessLogReq req){
        Result result = new Result();
        try{
            applyDerateProcessService.saveApplyDerateProcessLog(req);
            return Result.success();
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return Result.error("500", ex.getMessage());
        }

    }



    /**
     *
     *
     * @return
     */
    @ApiOperation(value="取得减免申请管理界面下拉选项框数据")
    @GetMapping("getApplyDeratPageSelectsData")
    public Result<Map<String,JSONArray>> getApplyDeratPageSelectsData() {

        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
        //业务类型
        List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
        retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));

        return Result.success(retMap);
    }


    /**
     * 获取分页减免管理列表
     * @param req 分页请求数据
     * @author zengkun
     * @date 2018年01月30日
     * @return 菜单分页数据
     */
    @ApiOperation(value = "获取分页减免管理列表")
    @GetMapping("/selectApplyDeratVoPage")
    @ResponseBody
    public PageResult<List<ApplyDerateVo>> selectApplyDeratVoPage(@ModelAttribute ApplyDerateListSearchReq req){

        try{
            Page<ApplyDerateVo> pages = applyDerateProcessService.selectApplyDeratePage(req);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }


    @ApiOperation(value = "减免管理 导出成excel")
    @PostMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ApplyDerateListSearchReq req) throws Exception {

        EasyPoiExcelExportUtil.setResponseHead(response,"ApplyDerateList.xls");
        List<ApplyDerateVo> list = applyDerateProcessService.selectApplyDerateList(req);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ApplyDerateVo.class, list);

        workbook.write(response.getOutputStream());
    }


    @ApiOperation(value = "减免管理 存储Excel  ")
    @PostMapping("/saveExcel")
    public Result<String> saveExcel(HttpServletRequest request, HttpServletResponse response,@RequestBody ApplyDerateListSearchReq req) throws Exception {
        EasyPoiExcelExportUtil.setResponseHead(response,"AfterLoanStandingBook.xls");
        List<ApplyDerateVo> list = applyDerateProcessService.selectApplyDerateList(req);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ApplyDerateVo.class, list);

        String fileName =  UUID.randomUUID().toString()+".xls";
        System.out.println(fileName);


        Map<String,String> retMap = storageService.storageExcelWorkBook(workbook,fileName);

//        retMap.put("errorInfo","");
//        retMap.put("sucFlage","true");

        if(retMap.get("sucFlage").equals("true")){
            return  Result.success(fileName);
        }else{
            return Result.error("500", retMap.get("errorInfo"));
        }
//        workbook.write(response.getOutputStream());


    }


}

