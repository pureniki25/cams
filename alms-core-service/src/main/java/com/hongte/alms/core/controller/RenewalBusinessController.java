package com.hongte.alms.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.base.vo.module.LoanExtListReq;
import com.hongte.alms.base.vo.module.LoanExtListVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
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
@Api(tags = "RenewalBusinessController", description = "展期业务相关接口")
public class RenewalBusinessController {
	@Autowired
	@Qualifier("SysUserService")
	SysUserService sysUserService;

	@Autowired
	@Qualifier("RenewalBusinessService")
	RenewalBusinessService renewalBusinessService;

	@Autowired
	LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("SysUserRoleService")
    SysUserRoleService sysUserRoleService;
	
	@GetMapping("/list")
	@ApiOperation(value = "展期信息列表查询")
	public PageResult<List<LoanExtListVO>> listLoanExt(LoanExtListReq req) {
		
		 Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<>();
         wrapperSysUserRole.eq("user_id",loginUserInfoHelper.getUserId());
         req.setUserId(loginUserInfoHelper.getUserId());
         wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 7 ) ");
         List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
         if(null != userRoles && !userRoles.isEmpty()) {
         	req.setNeedPermission(0);//全局用户 不需要验证权限
         }
		
		Page<LoanExtListVO> page = renewalBusinessService.listLoanExt(req);
		/*
		 * for (LoanExtListVO loanExtListVO : page.getRecords()) { if
		 * (loanExtListVO.getLoanTermUnit().equals("1")) {
		 * loanExtListVO.setLoanTerm(loanExtListVO.getLoanTerm()+"个月"); }else {
		 * loanExtListVO.setLoanTerm(loanExtListVO.getLoanTerm()+"天"); } }
		 */
		return PageResult.success(page.getRecords(), page.getTotal());
	}

	@GetMapping("/deptIds")
	@ApiOperation(value = "分公司信息列表查询")
	public Result<Map<String, JSONArray>> listDeptIds() {
		String userId = loginUserInfoHelper.getUserId();
		if (userId == null) {
			return Result.error("500", "userId can't be null");
		}

		List<SysOrg> list = new ArrayList();
		Map<String, SysOrg> map = sysUserService.selectCompanyByUserId(userId);
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			list.add((SysOrg) value);
		}
		Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();
		CompanySortByPINYINUtil.sortByPINYIN_SysOrg(list);
		retMap.put("company", (JSONArray) JSON.toJSON(list, JsonUtil.getMapping()));
		return Result.success(retMap);
	}
}
