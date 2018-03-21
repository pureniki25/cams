package com.hongte.alms.core.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.PhoneUrgeService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.service.ProcessLogService;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.process.vo.ProcessLogVo;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.storage.StorageService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

import feign.Request;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;

import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 消息管理
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-02
 */
@RestController
@RequestMapping("/InfoController")
public class InfoController {


    private Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    @Qualifier("InfoSmsService")
    InfoSmsService infoSmsService;

    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;



    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("BasicBusinessTypeService")
    BasicBusinessTypeService basicBusinessTypeService;
 

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;


    /**
     *
     *
     * @return
     */
    @ApiOperation(value="取得短信类型")
//  @CrossOrigin(allowCredentials="true", allowedHeaders="*", origins="*")
  @GetMapping("getSmsSelectData")
  public Result<Map<String,JSONArray>> getALStandingBookVoPageSelectsData() {

      Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

     
      //业务状态(贷后状态)
      List<SysParameter> businessStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.SMS_TYPE.getKey()).orderBy("row_Index"));
      retMap.put("smsType",(JSONArray) JSON.toJSON(businessStatusList, JsonUtil.getMapping()));

      //区域
      List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
      retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
      //公司
      List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
      retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
      //业务类型
      List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
      retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));


      return Result.success(retMap);
  }
    /**
     * 获取分页短信查询列表
     * @param req 分页请求数据
     * @author chenzs
     * @date 2018年3月2日
     * @return 菜单分页数据
     */
    @ApiOperation(value = "获取分页短信查询列表")
    @GetMapping("/selectInfoSmsVoPage")
    @ResponseBody
    public PageResult<List<InfoSmsListSearchVO>> selectInfoSmsVoPage(@ModelAttribute InfoSmsListSearchReq  req){

        try{
        	
        	Map<String,BasicCompany> companyIds  =  basicCompanyService.selectUserCanSeeCompany(loginUserInfoHelper.getUserId());
        	List companys=new ArrayList();
            if (companyIds != null) {
                for (String key : companyIds.keySet()) {
                	companys.add(key);
                }
            }
            req.setCompanyIds(companys);
           	req.setUserId(loginUserInfoHelper.getUserId());
           	
            Page<InfoSmsListSearchVO> pages = infoSmsService.selectInfoSmsPage(req);
            
            List<InfoSmsListSearchVO> list=pages.getRecords();
            
     
          
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }
    @ApiOperation(value="取得查询后的logId集合")
//  @CrossOrigin(allowCredentials="true", allowedHeaders="*", origins="*")
  @GetMapping("getLogIdList")
  public Result<Map<String,JSONArray>> getLogIdList(@ModelAttribute InfoSmsListSearchReq  req) {

      Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();

     
      //业务状态(贷后状态)
      List<SysParameter> businessStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.SMS_TYPE.getKey()).orderBy("row_Index"));
      retMap.put("smsType",(JSONArray) JSON.toJSON(businessStatusList, JsonUtil.getMapping()));

      //区域
      List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
      retMap.put("area", (JSONArray) JSON.toJSON(area_list,JsonUtil.getMapping()));
      //公司
      List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level",AreaLevel.COMPANY_LEVEL.getKey()));
      retMap.put("company",(JSONArray) JSON.toJSON(company_list,JsonUtil.getMapping()));
      //业务类型
      List<BasicBusinessType> btype_list =  basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
      retMap.put("businessType",(JSONArray) JSON.toJSON(btype_list, JsonUtil.getMapping()));


      return Result.success(retMap);
  }
    /**
    /**
     * 获取短信详情
     * @param req 分页请求数据
     * @author chenzs
     * @date 2018年3月3日
     * @return 短信详情
     */
    @ApiOperation(value = "根据LogId获取短信详情")
    @GetMapping("/getInfoSmsDetailById")
    @ResponseBody
    public Result<InfoSmsListSearchVO> getInfoSmsDetailById(
            @RequestParam("logId") String logId
    ){
    	InfoSmsListSearchVO  vo = infoSmsService.selectLastInfoSmsDetail(logId);
        if(vo!=null){
            return Result.success(vo);
        }else{
            return Result.error("500","无数据");
        }

    }
    
    
    
    /**
     * 获取上一条短信详情
     * @author chenzs
     * @date 2018年3月3日
     * @return 短信详情
     */
    @ApiOperation(value = "根据LogId获取上一条短信详情")
    @GetMapping("/selectLastInfoSmsDetail")
    @ResponseBody
    public Result<InfoSmsListSearchVO> selectLastInfoSmsDetail(
            @RequestParam("logId") String logId
    ){
    	InfoSmsListSearchVO  vo = infoSmsService.selectLastInfoSmsDetail(logId);

        if(vo!=null){
            return Result.success(vo);
        }else{
            return Result.error("500","无数据");
        }

    }
    
    
    /**
     * 获取下一条短信详情
     * @author chenzs
     * @date 2018年3月3日
     * @return 短信详情
     */
    @ApiOperation(value = "根据LogId获取下一条短信详情")
    @GetMapping("/selectNextInfoSmsDetail")
    @ResponseBody
    public Result<InfoSmsListSearchVO> selectNextInfoSmsDetail(
            @RequestParam("logId") String logId
    ){
    	InfoSmsListSearchVO  vo = infoSmsService.selectNextInfoSmsDetail(logId);

        if(vo!=null){
            return Result.success(vo);
        }else{
            return Result.error("500","无数据");
        }

    }
    
    
    


}

