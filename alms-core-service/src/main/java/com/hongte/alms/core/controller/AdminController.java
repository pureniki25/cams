package com.hongte.alms.core.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.service.AdminService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.AdminVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageRequest;
import com.hongte.alms.common.vo.PageResult;

import feign.Param;

@RestController
@RequestMapping("/sys/admin")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	@Qualifier("SysUserPermissionService")
	private SysUserPermissionService sysUserPermissionService;

	@Autowired
	@Qualifier("SysUserService")
	private SysUserService sysUserService;
	
	@Autowired
	@Qualifier("AdminService")
	private AdminService adminService;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/userPermission")
	public Result userPermission() {
		try {
			List<SysUser> list = sysUserService.selectList(new EntityWrapper<SysUser>());

			for (SysUser user : list) {
				sysUserPermissionService.setUserPermissons(user.getUserId());
			}
			return Result.success();
		} catch (Exception e) {
			LOGGER.error("--AdminController--设置所有用户可访问业务对照关系失败！", e);
			return Result.error("500", e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("/singleUserPermission")
	public Result singleUserPermission(@Param(value = "userId") String userId) {
		try {
			sysUserPermissionService.setUserPermissons(userId);
			return Result.success();
		} catch (Exception e) {
			LOGGER.error("--AdminController--设置所有用户可访问业务对照关系失败！", e);
			return Result.error("500", e.getMessage());
		}
	}
	
	@RequestMapping("/queryAllUserInfo")
	public PageResult<List<AdminVO>> queryAllUserInfo(PageRequest request) {
		try {
			return adminService.queryAllUserInfo(request);
		} catch (Exception e) {
			LOGGER.error("--AdminController--获取用户信息失败！", e);
			throw new ServiceException("系统异常，获取用户信息失败！");
		}
	}
	
	@RequestMapping("/queryUserInfoByUsername")
	public List<AdminVO> queryUserInfoByUsername(@Param(value = "username") String username) {
		try {
			return adminService.queryUserInfoByUsername(username);
		} catch (Exception e) {
			LOGGER.error("--AdminController--获取用户信息失败！", e);
			throw new ServiceException("系统异常，获取用户信息失败！");
		}
	}
}
