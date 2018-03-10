package com.hongte.alms.core.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.LoanExtListReq;
import com.hongte.alms.base.vo.module.LoanExtListVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 业务展期续借信息表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/renewalBusiness")
public class RenewalBusinessController {
	@Autowired
	@Qualifier("SysUserService")
	SysUserService sysUserService ;
	
	@Autowired
	@Qualifier("RenewalBusinessService")
	RenewalBusinessService renewalBusinessService ;
	
	@Autowired
    LoginUserInfoHelper loginUserInfoHelper;
	
	@GetMapping("/list")
    @ApiOperation(value = "展期信息列表查询")
	public PageResult<List<LoanExtListVO>> listLoanExt(LoanExtListReq req){
		Page<LoanExtListVO> page = renewalBusinessService.listLoanExt(req);
		/*for (LoanExtListVO loanExtListVO : page.getRecords()) {
			if (loanExtListVO.getLoanTermUnit().equals("1")) {
				loanExtListVO.setLoanTerm(loanExtListVO.getLoanTerm()+"个月");
			}else {
				loanExtListVO.setLoanTerm(loanExtListVO.getLoanTerm()+"天");
			}
		}*/
		return PageResult.success(page.getRecords(), page.getTotal());
	}
	
	@GetMapping("/deptIds")
    @ApiOperation(value = "分公司信息列表查询")
	public Result<Map<String,SysOrg>> listDeptIds(){
		String userId = loginUserInfoHelper.getUserId() ;
		if (userId==null) {
			return Result.error("500", "userId can't be null");
		}
		Map<String, SysOrg> map = sysUserService.selectCompanyByUserId(userId);
		return Result.success(map) ;
	}
}

