package com.hongte.alms.core.controller;


import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.vo.module.DepartmentBankVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.base.vo.module.doc.DocUploadRequest;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 财务款项池表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-02-28
 */
@RestController
@RequestMapping("/moneyPool")
public class MoneyPoolController {
	private Logger logger = LoggerFactory.getLogger(MoneyPoolController.class);
	
	@Autowired
    @Qualifier("MoneyPoolService")
    MoneyPoolService moneyPoolService;
	
	@Autowired
    @Qualifier("DepartmentBankService")
	DepartmentBankService departmentBankService ;
	
	@Autowired
    LoginUserInfoHelper loginUserInfoHelper;
	
	@Autowired
	@Qualifier("DocService")
	DocService docService ;
	
	@ApiOperation(value = "获取款项池")
    @GetMapping("/list")
    @ResponseBody
	public Result<List<MoneyPoolVO>> listMoneyPool(@RequestParam("businessId")String businessId , @RequestParam("afterId")String afterId ) {
		Result<List<MoneyPoolVO>> result = new Result<>() ;
		try {
			List<MoneyPoolVO> moneyPoolVOs = moneyPoolService.listMoneyPool(businessId, afterId);
			result.setData(moneyPoolVOs);
		} catch (Exception e) {
			logger.error(e.getMessage());
            result.error("500", e.getMessage());
		} finally {
			return result ;
		}
	}
	
	@ApiOperation(value = "分页获取款项池")
    @GetMapping("/page")
    @ResponseBody
	public PageResult<List<MoneyPoolVO>> listMoneyPoolByPage(@RequestParam("businessId")String businessId , @RequestParam("afterId")String afterId , Integer page , Integer limit) {
		try {
			Page<MoneyPoolVO> pages = moneyPoolService.listMoneyPoolByPage(businessId, afterId,page,limit);
			return PageResult.success(pages.getRecords(), pages.getTotal());
		} catch (Exception e) {
			logger.error(e.getMessage());
            return PageResult.error(500, e.getMessage());
		} 
	}
	
	@ApiOperation(value = "获取匹配的款项池")
    @GetMapping("/listMatched")
    @ResponseBody
	public Result<List<MatchedMoneyPoolVO>> listMatchedMoneyPool(@RequestParam("businessId")String businessId , @RequestParam("afterId")String afterId) {
		Result<List<MatchedMoneyPoolVO>> result = new Result<>() ;
		try {
			List<MatchedMoneyPoolVO> moneyPoolVOs = moneyPoolService.listMatchedMoneyPool(businessId, afterId);
			result.setData(moneyPoolVOs);
		} catch (Exception e) {
			logger.error(e.getMessage());
            result.error("500", e.getMessage());
		} finally {
			return result ;
		}
	}
	
	@ApiOperation(value = "新增/编辑 还款登记(若参数有moneyPoolId，则为编辑，否则为新增)")
    @GetMapping("/save")
    @ResponseBody
	public Result saveRepaymentRegisterInfo(RepaymentRegisterInfoDTO repayInfo) {
		Result result = null ;
		LoginInfoDto loginInfoDto = loginUserInfoHelper.getLoginInfo();
		String userId = loginUserInfoHelper.getUserId() ;
		if (userId==null) {
			return Result.error("500", "userId can't be null");
		}
		try {
			repayInfo.setUserId(userId);
			Boolean res = moneyPoolService.saveRepaymentRegisterInfo(repayInfo);
			result = Result.success(res);
		} catch (Exception e) {
			result = Result.error("500", e.getMessage());
		} finally {
			return result ;
		}
	}
	
	@ApiOperation(value = "删除 还款登记")
	@GetMapping("/del")
	@ResponseBody
	public Result deleteRepaymentRegisterInfo(String moneyPoolId) {
		if (moneyPoolId==null) {
			return Result.error("500", "moneyPoolId can't be null");
		}
		String userId = loginUserInfoHelper.getUserId() ;
		if (userId==null) {
			return Result.error("500", "userId can't be null");
		}
		try {
			boolean res = moneyPoolService.deleteRepaymentRegeisterInfo(moneyPoolId, userId);
			return Result.success(res);
		} catch (Exception e) {
			return Result.error("500", e.getMessage());
		}
	}
	
	@ApiOperation(value = "查询 1条还款登记")
	@GetMapping("/get")
	@ResponseBody
	public Result<MoneyPoolVO> getMoneyPool(String moneyPoolId){
		if (moneyPoolId==null) {
			return Result.error("500", "moneyPoolId can't be null");
		}
		String userId = loginUserInfoHelper.getUserId() ;
		if (userId==null) {
			return Result.error("500", "userId can't be null");
		}
		try {
			MoneyPoolVO res = moneyPoolService.getMoneyPool(moneyPoolId);
			return Result.success(res);
		} catch (Exception e) {
			return Result.error("500", e.getMessage());
		}
	}
	
	@ApiOperation(value = "获取当前业务的线下还款账户")
	@GetMapping("/listDepartmentBank")
	@ResponseBody
	public Result<List<DepartmentBankVO>> listDepartmentBank(String businessId){
		if (businessId==null) {
			return Result.error("500", "businessId can't be null");
		}
		String userId = loginUserInfoHelper.getUserId() ;
		if (userId==null) {
			return Result.error("500", "userId can't be null");
		}
		try {
			List<DepartmentBankVO> list = departmentBankService.listDepartmentBank(businessId);
			return Result.success(list);
		} catch (Exception e) {
			return Result.error("500", e.getMessage());
		}
	}
	
	/**
     * 文件上传具体实现方法（单文件上传）
     */
	@ApiOperation(value = "上传凭证")
	@PostMapping("/uploadCert")
    public UpLoadResult upload(FileVo fileVo) throws FileNotFoundException {
		String userId = loginUserInfoHelper.getUserId() ;
		UpLoadResult upLoadResult = new UpLoadResult() ;
		if (userId==null) {
			upLoadResult.setUploaded(false);
			upLoadResult.setMessage("userId can't be null");
			return upLoadResult;
		}
        return docService.upload(fileVo,userId);
    }
}

