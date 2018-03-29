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
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
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
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
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
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;

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
            @RequestParam(value = "processId",required = false) String processId
    ){
        Map<String,Object> retMap = new HashMap<>();




        try{
            //基本信息
            List<BusinessInfoForApplyDerateVo> businessVoList =  basicBusinessService.selectBusinessInfoForApplyDerateVo(crpId);
            //还款方式类型列表
            List<SysParameter> repayTypeList =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.REPAYMENT_TYPE.getKey()).orderBy("row_Index"));

            if (!CollectionUtils.isEmpty(businessVoList)) {
            	//赋值还款方式
            	for(SysParameter sys:repayTypeList) {
            		if(sys.getParamValue().equals(businessVoList.get(0).getRepaymentTypeId().toString())) {
            			businessVoList.get(0).setRepaymentTypeName(sys.getParamName());
            		}
            	}
			}
            retMap.put("baseInfo", (JSONArray) JSON.toJSON(businessVoList, JsonUtil.getMapping()));
//            derateTypeList
            //申请类型列表
            List<SysParameter> derateTypeList =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.DERATE_TYPE.getKey()).orderBy("row_Index"));
            retMap.put("derateTypeList",(JSONArray) JSON.toJSON(derateTypeList, JsonUtil.getMapping()));


            //申请减免的信息
            if(processId!=null){
                List<ApplyDerateProcess>  applyList = applyDerateProcessService.selectList(new EntityWrapper<ApplyDerateProcess>().eq("process_id",processId));
                retMap.put("applyList",(JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
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
        	if (!CollectionUtils.isEmpty(req)) {
        		applyDerateProcessService.saveApplyDerateProcess(req.get(0));
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

