package com.hongte.alms.core.controller;



import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.feignClient.CollectionSynceToXindaiRemoteApi;
import com.hongte.alms.base.service.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.collection.entity.CollectionPersonSetDetail;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.StaffPersonType;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionPersonSetDetailService;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.PhoneUrgeService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.StaffBusinessReq;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.PaymentPlatformEnums;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.enums.SysRoleEnums;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.storage.StorageService;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/*
 * <p>
 * 贷后移交前端控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
@RestController
@RequestMapping("/collection")
@Api(tags = "TbCollectionControllerApi", description = "贷后催收相关API", hidden = true)
@RefreshScope
public class CollectionController {
    private Logger logger = LoggerFactory.getLogger(CollectionController.class);

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
//    @Qualifier("storageService")
    StorageService storageService;

    @Autowired
    @Qualifier("SysUserService")
    SysUserService  sysUserService;
    

    
    @Autowired
    @Qualifier("SysUserAreaService")
    SysUserAreaService sysUserAreaService;

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;

    @Autowired
    CollectionSynceToXindaiRemoteApi collectionRemoteApi;
    
    @Autowired
    @Qualifier("SysUserRoleService")
    SysUserRoleService sysUserRoleService;
    
    @Autowired
    @Qualifier("CollectionPersonSetDetailService")
    CollectionPersonSetDetailService collectionPersonSetDetailService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListSynchService")
    RepaymentBizPlanListSynchService repaymentBizPlanListSynchService;
    
    @Autowired
    @Qualifier("CollectionPersonSetService")
    CollectionPersonSetService collectionPersonSetService;
    
//    private final StorageService storageService;
//
//    @Autowired
//    public CollectionController(StorageService storageService) {
//        this.storageService = storageService;
//    }
    /**
     *
     *
     * @return
     */
    @ApiOperation(value="取得分页贷后管理首页台账界面下拉选项框数据")
//    @CrossOrigin(allowCredentials="true", allowedHeaders="*", origins="*")
    @GetMapping("getALStandingBookVoPageSelectsData")
    public Result<Map<String,JSONArray>> getALStandingBookVoPageSelectsData() {
        logger.info("@贷后管理首页@取得分贷后管理首页台账界面下拉选项框数据--开始[{}]" , "");
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.AREA_LEVEL.getKey()));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
        //业务类型
        List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
        retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));
        //业务状态(贷后状态)
        List<SysParameter> businessStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.COLLECTION_STATUS.getKey()).orderBy("row_Index"));
        retMap.put("businessStatusList",(JSONArray) JSON.toJSON(businessStatusList, JsonUtil.getMapping()));
        //还款状态
        List<SysParameter> repayStatusList =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.REPAY_STATUS.getKey()).eq("status",1).orderBy("row_Index"));
        //判断角色的区域类型
        Wrapper<SysUserRole> wrapperDataType = new EntityWrapper<>();
        wrapperDataType.eq("user_id",loginUserInfoHelper.getUserId());
        wrapperDataType.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE data_type = 1 AND page_type = 1 ) ");
        List<SysUserRole> sysUserRoleDataRight = sysUserRoleService.selectList(wrapperDataType);
        if(null != sysUserRoleDataRight && !sysUserRoleDataRight.isEmpty()) {
        	repayStatusList = repayStatusList.stream().filter(e->e.getParamName().equals("逾期")).collect(Collectors.toList());
        }
        retMap.put("repayStatusList",(JSONArray) JSON.toJSON(repayStatusList, JsonUtil.getMapping()));
        //催收级别
        List<SysParameter> collectLevelList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.COLLECTION_LEVERS.getKey()).orderBy("row_Index"));
        retMap.put("collectLevelList",(JSONArray) JSON.toJSON(collectLevelList, JsonUtil.getMapping()));
        //userId
        List<String> userlist=new ArrayList();
        String userId = loginUserInfoHelper.getUserId();
        userlist.add(userId);
        retMap.put("userId",(JSONArray) JSON.toJSON(userlist, JsonUtil.getMapping()));
 
        List<SysParameter> carStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.CAR_STATUS.getKey()).orderBy("row_Index"));
        retMap.put("carStatusList",(JSONArray) JSON.toJSON(carStatusList, JsonUtil.getMapping()));

        logger.info("@贷后管理首页@取得分贷后管理首页台账界面下拉选项框数据--结束[{}]" , "");
        return Result.success(retMap);
    }


    /**
     * 获取分页贷后首页台账
     * @param req 分页请求数据
     * @author zengkun
     * @date 2018年01月30日
     * @return 菜单分页数据
     */
    @ApiOperation(value = "获取分页贷后首页台账")
    @GetMapping("/selectALStandingBookVoPage")
    @ResponseBody
    public PageResult<List<AfterLoanStandingBookVo>> selectALStandingBookVoPage(@ModelAttribute AfterLoanStandingBookReq req){

        try{
			/*if (req.getShowRepayDateBegin() == null && req.getShowRepayDateEnd() == null 
					&& req.getDelayDaysEnd() == null
					&& req.getRealRepayDateBegin() == null && req.getRealRepayDateEnd() == null
					&& req.getCollectLevel() == null && StringUtil.isEmpty(req.getBusinessId())) {
				Date nowDate = new Date();
				req.setShowRepayDateBegin(DateUtil.addDay2Date(-93, nowDate));
				req.setShowRepayDateEnd(nowDate);
			}
			if (req.getShowRepayDateBegin() != null && req.getShowRepayDateEnd() != null
					&& DateUtil.getDiffDays(req.getShowRepayDateBegin(), req.getShowRepayDateEnd()) > 93) {
				return PageResult.error(500, "应还日期选择范围不能超过93天！");
			}
			if (req.getRealRepayDateBegin() != null && req.getRealRepayDateEnd() != null
					&& DateUtil.getDiffDays(req.getRealRepayDateBegin(), req.getRealRepayDateEnd()) > 93) {
				return PageResult.error(500, "实还日期选择范围不能超过93天！");
			}*/
            if(req.getRepayStatus()!=null&&req.getRepayStatus().equals(""))req.setRepayStatus(null);
            long startTime = System.currentTimeMillis();
            
            //判断角色的区域类型
            Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<>();
            wrapperSysUserRole.eq("user_id",loginUserInfoHelper.getUserId());
            wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 1 ) ");
            List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
            if(null != userRoles && !userRoles.isEmpty()) {
            	req.setNeedPermission(0);//全局用户 不需要验证权限
            }
            
            //判断角色的区域类型
            Wrapper<SysUserRole> wrapperDataType = new EntityWrapper<>();
            wrapperDataType.eq("user_id",loginUserInfoHelper.getUserId());
            wrapperDataType.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE data_type = 1 AND page_type = 1 ) ");
            List<SysUserRole> sysUserRoleDataRight = sysUserRoleService.selectList(wrapperDataType);
            if(null != sysUserRoleDataRight && !sysUserRoleDataRight.isEmpty()) {
            	req.setRepayStatus("逾期");
            }
            
            if (StringUtil.notEmpty(req.getPaymentPlatform())) {
            	req.setPaymentPlatformCode(PaymentPlatformEnums.getValueByName(req.getPaymentPlatform()));
			}
            
            Page<AfterLoanStandingBookVo> pages = phoneUrgeService.selectAfterLoanStandingBookPage(req);
//            System.out.println(JSON.toJSONString(pages));
            long end = System.currentTimeMillis();
            System.out.println(end - startTime);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return PageResult.error(500, "数据库访问异常");
        }
    }
    
    
    
    /**
     * 获取分页待贷后首页台账
     * @param req 分页请求数据
     * @author zengkun
     * @date 2018年01月30日
     * @return 菜单分页数据
     */
    @ApiOperation(value = "获取分页代扣管理台账")
    @GetMapping("/selectRepayManage")
    @ResponseBody
    public PageResult<List<AfterLoanStandingBookVo>> selectRepayManage(@ModelAttribute AfterLoanStandingBookReq req){

        try{
	    	 Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<>();
	         wrapperSysUserRole.eq("user_id",loginUserInfoHelper.getUserId());
	         wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 4 ) ");
	         List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
	         if(null != userRoles && !userRoles.isEmpty()) {
	         	req.setNeedPermission(0);//全局用户 不需要验证权限
	         }
        	
            long startTime = System.currentTimeMillis();
//            List<String> companyIds= sysUserAreaService.selectUserAreas(loginUserInfoHelper.getUserId());
//            if(companyIds.size()>0) {
//                req.setCommIds(companyIds);
//            }
            Page<AfterLoanStandingBookVo> pages = phoneUrgeService.selectRepayManage(req);
            long end = System.currentTimeMillis();
            System.out.println(end - startTime);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return PageResult.error(500, "数据库访问异常");
        }
    }
    

    @ApiOperation(value = "贷后首页台账 导出成excel")
//    @RequestMapping("/download")
    @PostMapping("/download")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response,@ModelAttribute AfterLoanStandingBookReq req) throws Exception {
        logger.info("@贷后管理首页@贷后首页台账--导出成excel--结束[{}]" , req);
        EasyPoiExcelExportUtil.setResponseHead(response,"AfterLoanStandingBook.xls");
        List<AfterLoanStandingBookVo> list = phoneUrgeService.selectAfterLoanStandingBookList(req);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), AfterLoanStandingBookVo.class, list);

        workbook.write(response.getOutputStream());
        logger.info("@贷后管理首页@贷后首页台账--导出成excel---结束[{}]" , req);
    }



    @Value("${ht.excel.file.save.path:/tmp/}")
    private  String excelSavePath;

    @ApiOperation(value = "贷后首页台账 存储Excel  ")
    @PostMapping("/saveExcel")
    public Result saveExcel(HttpServletRequest request, HttpServletResponse response,@RequestBody AfterLoanStandingBookReq req) throws Exception {

        logger.info("@贷后管理首页@贷后首页台账--存储Excel--开始[{}]" , req);
        req.setUserId(loginUserInfoHelper.getUserId());
        EasyPoiExcelExportUtil.setResponseHead(response,"AfterLoanStandingBook.xls");
        
        Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<>();
        wrapperSysUserRole.eq("user_id",loginUserInfoHelper.getUserId());
        wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 1 ) ");
        List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
        if(null != userRoles && !userRoles.isEmpty()) {
        	req.setNeedPermission(0);//全局用户 不需要验证权限
        }
        
        if (StringUtil.notEmpty(req.getPaymentPlatform())) {
        	req.setPaymentPlatformCode(PaymentPlatformEnums.getValueByName(req.getPaymentPlatform()));
		}
        
        List<AfterLoanStandingBookVo> list = phoneUrgeService.selectAfterLoanStandingBookList(req);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), AfterLoanStandingBookVo.class, list);

        String fileName =  UUID.randomUUID().toString()+".xls";
        System.out.println(fileName);


        Map<String,String> retMap = storageService.storageExcelWorkBook(workbook,fileName);
        String docUrl=retMap.get("docUrl");

        retMap.put("errorInfo","");
        retMap.put("sucFlage","true");

        if(retMap.get("sucFlage").equals("true")){
            logger.info("@贷后管理首页@贷后首页台账--存储Excel---结束[{}]" , fileName);
            return  Result.success(docUrl);
        }else{
            logger.info("@贷后管理首页@贷后首页台账--存储Excel---失败[{}]" ,  retMap.get("errorInfo"));
            return Result.error("500", retMap.get("errorInfo"));
        }
//        workbook.write(response.getOutputStream());


    }





   /* *//**
     * 获取分页待分配电催的逾期还款列表
     * @param req 分页请求数据
     * @author zengkun
     * @date 2018年01月24日
     * @return 菜单分页数据
     *//*
    @ApiOperation(value = "获取待分配电催的逾期还款")
    @GetMapping("/queryNeedCollectBusinessList")
    @ResponseBody
    public PageResult<List<StaffBusinessVo>> queryNeedCollectBusinessList(@ModelAttribute StaffBusinessReq req){
        Page<StaffBusinessVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());
        try{
//            pages = phoneUrgeService.selectPhoneUrgePage(pages,req);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }*/




    /**
     *
     *
     * @return
     */
    @ApiOperation(value="取得分配电催界面下拉选项框数据")
    @GetMapping("getPhoneUrgeSelectsData")
    public Result<Map<String,JSONArray>> getPhoneUrgeSelectsData(
            @RequestParam("staffType")  String staffType,
            @RequestParam("crpIds")  String crpIds
    ) {
        logger.info("@分配电催界面@取得分配电催界面下拉选项框数据--开始[{}]" , staffType);
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.AREA_LEVEL.getKey()));
        retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
        //业务类型
        List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
        retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));

        List<String> listTeam1 = new ArrayList<>();
        List<String> listTeam2 = new ArrayList<>();
        
        if(crpIds != null && StringUtils.isNotEmpty(crpIds)) {
        	//1找出业务单号  业务还款计划列表
        	//2找出单号区域信息  基础业务信息表
        	List<RepaymentBizPlanListSynch> listRepaymentBizPlanListSynch = repaymentBizPlanListSynchService.selectList(new EntityWrapper<RepaymentBizPlanListSynch>().in("plan_list_id", crpIds.split(",")));
        	List<String> companyList = new ArrayList<>();
        	List<String> areaList = new ArrayList<>();
        	for(RepaymentBizPlanListSynch repaymentBizPlanListSynch : listRepaymentBizPlanListSynch) {
        		if(StringUtils.isNotBlank(repaymentBizPlanListSynch.getCompanyIdExt())) {
        			companyList.add(repaymentBizPlanListSynch.getCompanyIdExt());
        		}else {
        			areaList.add(repaymentBizPlanListSynch.getDistrictIdExt());
        		}
        	}
        	
        	//3找出区域电催设置  tb_collection_person_set
        	List<String> listColPersonId = new ArrayList<>();
        	List<CollectionPersonSet> listCollectionPersonSet = new ArrayList<>();
        	
        	if(!companyList.isEmpty()) {
        		listCollectionPersonSet = collectionPersonSetService.selectList(new EntityWrapper<CollectionPersonSet>().in("company_code", companyList));
        	}
        	if(!areaList.isEmpty()) {
        		List<CollectionPersonSet> listAreaCollectionPersonSet = collectionPersonSetService.selectList(new EntityWrapper<CollectionPersonSet>().in("area_code", areaList));
        		listCollectionPersonSet.addAll(listAreaCollectionPersonSet);
        	}
        	for (CollectionPersonSet collectionPersonSet : listCollectionPersonSet) {
        		listColPersonId.add(collectionPersonSet.getColPersonId());
			}
        	
        	//4找出区域电催负责人 tb_collection_person_set_detail
        	List<CollectionPersonSetDetail> listCollectionPersonSetDetail = new ArrayList<>();
        	if(!listColPersonId.isEmpty()) {
        		listCollectionPersonSetDetail = collectionPersonSetDetailService.selectList(new EntityWrapper<CollectionPersonSetDetail>().in("col_person_id", listColPersonId));
        	}
        	for (CollectionPersonSetDetail collectionPersonSetDetail : listCollectionPersonSetDetail) {
        		//清算一组
        		if(collectionPersonSetDetail.getTeam() == 1) {
        			listTeam1.add(collectionPersonSetDetail.getUserId());
        		}
        		//清算二组
        		else {
        			listTeam2.add(collectionPersonSetDetail.getUserId());
        		}
			}
        }
        //跟进人员
        List<Map<String,String>> opr_list = new LinkedList<Map<String,String>>();
        List<SysUser> oprList =new LinkedList<>();
        if(staffType.equals(StaffPersonType.PHONE_STAFF.getKey())){
            //跟进人员
            oprList =  sysUserService.selectUsersByRole(SysRoleEnums.HD_LIQ_COMMISSIONER.getKey());
            List<SysUser> tempList =new LinkedList<>();
            for (SysUser sysUser : oprList) {
            	if(!listTeam1.contains(sysUser.getUserId())) {
            		tempList.add(sysUser);
            	}
			}
            oprList.removeAll(tempList);
            
        }else{
            oprList =  sysUserService.selectUsersByRole(SysRoleEnums.HD_ASSET_COMMISSIONE.getKey());
            List<SysUser> tempList =new LinkedList<>();
            for (SysUser sysUser : oprList) {
            	if(!listTeam2.contains(sysUser.getUserId())) {
            		tempList.add(sysUser);
            	}
			}
            oprList.removeAll(tempList);
        }
        for(SysUser user:oprList){
            Map<String,String> opr1 = new HashMap<String, String>();
            opr1.put("userId",user.getUserId());
            opr1.put("userName",user.getUserName());
            opr_list.add(opr1);
        }

        retMap.put("operators",(JSONArray) JSON.toJSON(opr_list));
        logger.info("@贷后管理首页@取得分配电催界面下拉选项框数据--结束[{}]" , retMap);
        return Result.success(retMap);
    }

    /**
     * 为逾期的业务设置电催人员
     * @param taffVoList  需设置业务列表
     * @param staffUserId
     * @param describe
     * @return
     */
    @ApiOperation(value="设置电催人员")
    @GetMapping("setBusinessBusinessStaff")
    public  Result setBusinessPhoneStaff(
            @RequestParam("taffVoList") String taffVoList,
            @RequestParam("staffUserId") String staffUserId,
            @RequestParam("describe") String describe,
            @RequestParam("staffType") String staffType
    ){
        logger.info("@分配电催页面@设置电催人员--开始[{}]" ,"taffVoList:"+ taffVoList +"   staffUserId:"+staffUserId+"    describe:"+describe+"    staffType"+staffType);
        List<StaffBusinessVo> voList =  JSON.parseArray(taffVoList,StaffBusinessVo.class);

        try{
            collectionStatusService.setBusinessStaff(voList,staffUserId,describe,staffType, CollectionSetWayEnum.MANUAL_SET);
            //同步设置信息到信贷
            collectionStatusService.SyncBusinessColStatusToXindai(voList,staffUserId,describe,staffType);

            logger.info("@分配电催页面@设置电催人员--结束[{}]","");
            return Result.success();
        }catch(Exception e){
            logger.error("@分配电催页面@设置电催人员 异常[{}]",e);
            return Result.error("erroe","数据库存储异常"+e.getMessage());
        }

    }

   /**
     * 查询员工跟进催收记录
    * @param businessId   业务ID
    * @param crpId  分期表ID
     * @return
     */
    @ApiOperation(value="查询员工跟进催收记录")
    @GetMapping("selectStaffCollectLogsPage")
    public  PageResult<List<StaffBusinessVo>> selectStaffCollectLogsPage(@ModelAttribute StaffBusinessReq req,
            @RequestParam("businessId") String businessId,
            @RequestParam("crpId") String crpId
    ){
        logger.info("@贷后管理@查询员工跟进催收记录--开始[{}]","   businessId："+businessId+"   crpId:"+crpId);
        Page<StaffBusinessVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());
        try{
//            pages = phoneUrgeService.selectPhoneUrgePage(pages,req);
            logger.info("@贷后管理@查询员工跟进催收记录--结束[{}]",pages);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(" @贷后管理@查询员工跟进催收记录--异常"+ex);
            return PageResult.error(500, "数据库访问异常");
        }
/*
        List<StaffBusinessVo> voList =  JSON.parseArray(phoneUrgeVoList,StaffBusinessVo.class);

        try{
            collectionStatusService.setBusinessStaff(voList,phoneStaffUserId,describe,staffType);
            return Result.success();
        }catch(Exception e){
            return Result.error("erroe","数据库存储异常"+e.getMessage());
        }*/

    }

//    public Result


    @ApiOperation(value = "分配所有业务的电催、催收信息")
    @GetMapping("/setAllBizCollection")
    public Result setAllBizCollection(){
        try{
            collectionStatusService.autoSetBusinessStaff();
            return Result.success();
        }catch (Exception ex){
            ex.printStackTrace();
            return Result.error("500", "执行电催、催收分配异常:"+ ex.getMessage());
        }
    }




  /*  *//**
     * 获取分页待分配电催的逾期还款列表
     * @param req 分页请求数据
     * @author zengkun
     * @date 2018年01月24日
     * @return 菜单分页数据
     *//*
    @ApiOperation(value = "获取待分配电催的逾期需还款列表")
    @GetMapping("/queryNeedDistributList")
    public PageResult<List<StaffBusinessVo>> queryNeedDistributList(@ModelAttribute StaffBusinessReq req){
        Page<StaffBusinessVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());
        try{
            pages = phoneUrgeService.selectPhoneUrgePage(pages,req);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }*/
    
    /**
     * @author huweiqian
     * @return
     */
    @ApiOperation(value = "获取所有投资端名称")
    @GetMapping("/queryPaymentPlatform")
    public Result<List<String>> queryPaymentPlatform(){
        try{
        	List<String> resultList = new ArrayList<>();
            PaymentPlatformEnums[] paymentPlatformEnums = PaymentPlatformEnums.values();
            for (PaymentPlatformEnums enums : paymentPlatformEnums) {
            	resultList.add(enums.getName());
			}
            return Result.success(resultList);
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            return Result.error("-99", ex.getMessage());
        }
    }
 

}

