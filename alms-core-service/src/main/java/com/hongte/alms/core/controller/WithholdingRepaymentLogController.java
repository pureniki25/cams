package com.hongte.alms.core.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.base.vo.module.RepaymentLogReq;
import com.hongte.alms.base.vo.module.RepaymentLogVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.storage.StorageService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.util.DateUtil;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 还款计划代扣日志流水表 前端控制器
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-08
 */
@RestController
@RequestMapping("/RepaymentLogController")
public class WithholdingRepaymentLogController {

    private Logger logger = LoggerFactory.getLogger(WithholdingRepaymentLogController.class);
	
	  @Autowired
	    @Qualifier("WithholdingRepaymentLogService")
	  WithholdingRepaymentLogService withholdingRepaymentlogService;
	  
	    @Autowired
	    StorageService storageService;
	  @Autowired
      LoginUserInfoHelper loginUserInfoHelper;

	    /**
	     * 获取分页短信查询列表
	     * @param req 分页请求数据
	     * @author chenzs
	     * @date 2018年3月8日
	     * @return 菜单分页数据
	     */
	    @ApiOperation(value = "获取分页代扣查询列表")
	    @GetMapping("/selectRepaymentLogList")
	    @ResponseBody
	    public PageResult<List<RepaymentLogVO>> selectRepaymentLogList(@ModelAttribute RepaymentLogReq  req){

	        try{
	        	req.setUserId(loginUserInfoHelper.getUserId());
	            Page<RepaymentLogVO> pages = withholdingRepaymentlogService.selectRepaymentLogPage(req);
	       
	            return PageResult.success(pages.getRecords(),pages.getTotal());
	        }catch (Exception ex){
	            ex.printStackTrace();
	            logger.error(ex.getMessage());
	            return PageResult.error(500, "数据库访问异常");
	        }
	    }
	    
	    
	    /**
	     * 获取用户Id
	     * @param req 分页请求数据
	     * @author chenzs
	     * @date 2018年3月22日
	     * @return 菜单分页数据
	     */

	    @ApiOperation(value = "获取用户Id")
	    @GetMapping("/getUserId")
	    @ResponseBody
	    public Result<String> getUserId(
	    ){
	    	String userId=loginUserInfoHelper.getUserId();

	        if(userId!=null&&!userId.equals("")){
	            return Result.success(userId);
	        }else{
	            return Result.error("500","无数据");
	        }

	        
	    }
	    
	    /**
	     * 获取详情
	     * @author chenzs
	     * @date 2018年3月9日
	     * @return 详情
	     */
	    @ApiOperation(value = "根据LogId获取短信详情")
	    @GetMapping("/getRepayLogDetailById")
	    @ResponseBody
	    public Result<WithholdingRepaymentLog> getInfoSmsDetailById(
	            @RequestParam("logId") String logId
	    ){
	        List<WithholdingRepaymentLog>  pList = withholdingRepaymentlogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("log_id",logId));

	        if(pList.size()>0){
	            return Result.success(pList.get(0));
	        }else{
	            return Result.error("500","无数据");
	        }

	        
	    }
	    
	    /**
	     * 获取代扣业务条数，成功代扣流水数，成功代扣总额，成功代扣业务条数
	     * @author chenzs
	     * @date 2018年3月12日
	     * @return 详情
	     */
	    @ApiOperation(value = "获取代扣业务条数，成功代扣流水数，成功代扣总额，成功代扣业务条数")
	    @GetMapping("/getCountInfo")
	    @ResponseBody
	    public Result<Map<String,String>>getCountInfo( @RequestParam("companyId") String companyId,@RequestParam("keyName")String keyName,@RequestParam("platformId")String platformId,
	    		
	    		@RequestParam("dateBegin")Date dateBegin,@RequestParam("dateEnd")Date dateEnd,@RequestParam("repayStatus")String repayStatus,@RequestParam("businessTypeId")String businessTypeId){
	    	   Map<String,String> retMap = new HashMap<String,String>();
	    	   RepaymentLogReq req=new RepaymentLogReq();
	    	   req.setBusinessTypeId(businessTypeId);
	    	   req.setCompanyId(companyId);
	    	   req.setDateBegin(dateBegin);
	    	   req.setDateEnd(dateEnd);
	    	   req.setRepayStatus(repayStatus);
	    	   req.setPlatformId(platformId);
	       	String userId=loginUserInfoHelper.getUserId();
	    	   req.setUserId(userId);
	    	   RepaymentLogVO  repaymentLogVO=null;
	 
	    	//查找代扣业务总条数
	        repaymentLogVO=withholdingRepaymentlogService.selectSumByBusinessId(req);
	        String countByBusinessId=repaymentLogVO.getCountByBusinessId();
	        
	      //查找代扣成功流水总条数
	        req.setRepayStatus("1");
	        repaymentLogVO=withholdingRepaymentlogService.selectSumByLogId(req);
	        String countbyLogId=repaymentLogVO.getCountbyLogId();
	        //查找代扣成功总额
	        String SumRepayAmount=repaymentLogVO.getSumRepayAmount();
	    	//查找代扣业务成功总条数
            repaymentLogVO= withholdingRepaymentlogService.selectSumByBusinessId(req);
            String countByBusinessIdSucess=repaymentLogVO.getCountByBusinessId();
	
	        retMap.put("countByBusinessIdSucess",countByBusinessIdSucess);
	        retMap.put("countByBusinessId",countByBusinessId);
	        retMap.put("countbyLogId",countbyLogId);
	        retMap.put("SumRepayAmount",SumRepayAmount);

	        
	        return Result.success(retMap);
	        
	    }
	    
	    
	    @Value("${ht.excel.file.save.path}")
	    private  String excelSavePath;
 
	    @ApiOperation(value = "还款计划日志表导出Excel  ")
	    @PostMapping("/saveExcel")  
	    public Result saveExcel(HttpServletRequest request, HttpServletResponse response,@RequestBody RepaymentLogReq req) throws Exception {
	    	req.setUserId(loginUserInfoHelper.getUserId());
	        EasyPoiExcelExportUtil.setResponseHead(response,"repaylogmengt.xls");
	        List<RepaymentLogVO> list = withholdingRepaymentlogService.selectRepaymentLogExcel(req);
 
	        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), RepaymentLogVO.class, list);


	        String fileName =  UUID.randomUUID().toString()+".xls";
	        System.out.println(fileName);


	        Map<String,String> retMap = storageService.storageExcelWorkBook(workbook,fileName);

	        retMap.put("errorInfo","");
	        retMap.put("sucFlage","true");

	        if(retMap.get("sucFlage").equals("true")){
	            return  Result.success(fileName);
	        }else{
	            return Result.error("500", retMap.get("errorInfo"));
	        }

	    }

	  
	  
}

