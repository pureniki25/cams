package com.hongte.alms.core.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.ProcessStepSearchReq;
import com.hongte.alms.base.vo.module.ProcessTypeReq;
import com.hongte.alms.base.vo.module.ProcessTypeStepVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 流程设置前端控制器
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-20
 */
@RestController
@RequestMapping("/ProcessSetUpController")
@Api(tags = "ProcessSetUpController", description = "流程设置相关接口")
public class ProcessSetUpController {

    private Logger logger = LoggerFactory.getLogger(ProcessSetUpController.class);
	
	  @Autowired
	   @Qualifier("ProcessTypeService")
	  ProcessTypeService processTypeService;
	  @Autowired
	  @Qualifier("ProcessTypeStepService")
	  ProcessTypeStepService processTypeStepService;

	  @Autowired
      LoginUserInfoHelper loginUserInfoHelper;
	  
	  @Autowired
	  @Qualifier("SysRoleService")
	  SysRoleService sysRoleService;
	  
	  @Autowired
	  @Qualifier("SysUserService")
	  SysUserService sysUserService;
	  
	  

	    /**
	     *
	     * @param 获取流程类型列表
	     * @author chenzs
	     * @date 2018年3月20日
	     * @return 菜单分页数据
	     */
	    @ApiOperation(value = "获取流程类型列表")
	    @GetMapping("/getProcessTypeList")
	    @ResponseBody
	    public PageResult<List<ProcessType>> selectProcessTypeList(@ModelAttribute ProcessTypeReq  req){

	        try{
	            Page<ProcessType> pages = processTypeService.getProcessTypeListByPage(req);
	       
	            return PageResult.success(pages.getRecords(),pages.getTotal());
	        }catch (Exception ex){
	            ex.printStackTrace();
	            logger.error(ex.getMessage());
	            return PageResult.error(500, "数据库访问异常");
	        }
	    }
	    
	    
	    /**
	     *
	     * @param 流程类型步骤列表
	     * @author chenzs
	     * @date 2018年3月21日
	     * @return 
	     */
	    @ApiOperation(value = "获取流程类型")
	    @GetMapping("/getProcessType")
	    @ResponseBody
	    public Result<ProcessType> getProcessType(String typeId){
	        try{
	            ProcessType type = processTypeService.getProcessTypeByID(typeId);
	       
	            return Result.success(type);
	        }catch (Exception ex){
	            ex.printStackTrace();
	            logger.error(ex.getMessage());
	            return Result.error("500", "数据库访问异常");
	        }
	    }
	    
	    
		@ApiOperation(value = "新增/编辑 流程类型(若参数有typeId，则为编辑，否则为新增)")
	    @GetMapping("/save")
	    @ResponseBody
		public Result<Boolean> saveProcessType(ProcessType processType) {
			Result<Boolean> result = null ;
			String userId = loginUserInfoHelper.getUserId() ;
			if (userId==null) {
				return Result.error("500", "userId can't be null");
			}
			try {
				processType.setCreateUser(userId);
				processType.setCreateTime(new Date());
				Boolean res = processTypeService.insertOrUpdate(processType);
				result = Result.success(res);
			} catch (Exception e) {
				result = Result.error("500", e.getMessage());
			}
			return result; 
		}
	    
		@ApiOperation(value = "删除 流程类型")
		@GetMapping("/del")
		@ResponseBody
		public Result<Boolean> deleteProcessType(String typeId) {
			if (typeId==null) {
				return Result.error("500", "typeId can't be null");
			}
		
			try {
				boolean res = processTypeService.deleteById(typeId);
				return Result.success(res);
			} catch (Exception e) {
				return Result.error("500", e.getMessage());
			}
		}
	  
	    /**
	     * 
	     * @param 获取流程步骤列表
	     * @author chenzs
	     * @date 2018年3月20日
	     * @return 菜单分页数据
	     */
	    @ApiOperation(value = "获取流程步骤列表")
	    @GetMapping("/getProcessStepList")
	    @ResponseBody
	    public PageResult<List<ProcessTypeStepVO>> selectProcessStepList(@ModelAttribute ProcessStepSearchReq  req){

	        try{
	            Page<ProcessTypeStepVO> pages = processTypeStepService.getProcessTypeStepList(req);
	       
	            return PageResult.success(pages.getRecords(),pages.getTotal());
	        }catch (Exception ex){
	            ex.printStackTrace();
	            logger.error(ex.getMessage());
	            return PageResult.error(500, "数据库访问异常");
	        }
	    }
	    
	    /**
	     *
	     * @param 流程步骤
	     * @author chenzs
	     * @date 2018年3月30日
	     * @return 
	     */
	    @ApiOperation(value = "获取流程步骤")
	    @GetMapping("/getProcessTypeStep")
	    @ResponseBody
	    public Result<ProcessTypeStepVO> getProcessTypeStep(String typeStepId){
	        try{
	        	ProcessStepSearchReq req=new ProcessStepSearchReq();
	        	req.setTypeStepId(typeStepId);
	        	  Page<ProcessTypeStepVO> pages = processTypeStepService.getProcessTypeStepList(req);
	        	  ProcessTypeStepVO vo=pages.getRecords().get(0);
	            return Result.success(vo);
	        }catch (Exception ex){
	            ex.printStackTrace();
	            logger.error(ex.getMessage());
	            return Result.error("500", "数据库访问异常");
	        }
	    }
	    

	    /**
	     *
	     *
	     * @return
	     */
	    @ApiOperation(value="取得下拉框数据")
	  @GetMapping("getSelectData")
	  public Result<Map<String,JSONArray>> getSelectData() {

 	      Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
	      List<SysRole> roleList = sysRoleService.selectList(new EntityWrapper<SysRole>());
	      retMap.put("roleList",(JSONArray) JSON.toJSON(roleList,JsonUtil.getMapping()));
          List<ProcessType>  processTypeList=processTypeService.selectList(new EntityWrapper<ProcessType>());
          retMap.put("processTypeList",(JSONArray) JSON.toJSON(processTypeList,JsonUtil.getMapping()));
          
         List<SysUser> userList =sysUserService.selectList(new EntityWrapper<SysUser>());
         retMap.put("userList",(JSONArray) JSON.toJSON(userList,JsonUtil.getMapping()));
	      return Result.success(retMap);
	  }
	    
	    
		@ApiOperation(value = "新增/编辑 流程步骤(若参数有stepid，则为编辑，否则为新增)")
	    @GetMapping("/saveStep")
	    @ResponseBody
		public Result<Boolean> saveProcessStep(ProcessTypeStep processTypeStep) {
			Result<Boolean> result = null ;
		
			try {
				Boolean res = processTypeStepService.insertOrUpdate(processTypeStep);
				result = Result.success(res);
			} catch (Exception e) {
				result = Result.error("500", e.getMessage());
			}
			return result; 
		}
		
		@ApiOperation(value = "删除 流程类型")
		@GetMapping("/delStep")
		@ResponseBody
		public Result<Boolean> deleteProcessStep(String stepId) {
			if (stepId==null) {
				return Result.error("500", "typeId can't be null");
			}
		
			try {
				boolean res = processTypeStepService.deleteById(stepId);
				return Result.success(res);
			} catch (Exception e) {
				return Result.error("500", e.getMessage());
			}
		}
	    
}

