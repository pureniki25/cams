package com.hongte.alms.core.controller;


import java.util.Date;
import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.vo.module.ProcessTypeReq;
import com.hongte.alms.common.result.Result;
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
public class ProcessSetUpController {

    private Logger logger = LoggerFactory.getLogger(ProcessSetUpController.class);
	
	  @Autowired
	    @Qualifier("ProcessTypeService")
	  ProcessTypeService processTypeService;
	  

	  @Autowired
      LoginUserInfoHelper loginUserInfoHelper;

	    /**
	     * 获取分页短信查询列表
	     * @param 获取流程类型列表
	     * @author chenzs
	     * @date 2018年3月20日
	     * @return 菜单分页数据
	     */
	    @ApiOperation(value = "获取流程类型列表")
	    @GetMapping("/getProcessTypeList")
	    @ResponseBody
	    public PageResult<List<ProcessType>> selectRepaymentLogList(@ModelAttribute ProcessTypeReq  req){

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
	     * @param 获取流程类型
	     * @author chenzs
	     * @date 2018年3月20日
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
	  
	  
}

