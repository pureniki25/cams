package com.hongte.alms.core.controller;

import java.util.List;
import java.util.concurrent.Executor;

import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.common.exception.ExceptionCodeEnum;
import com.hongte.alms.common.util.ListUtil;
import io.swagger.annotations.ApiModelProperty;
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

	//设置用户权限对应关系的标志位
	private  static Boolean setUserPermissionFlage = false;

	//异步执行线程1执行标志位
	private  static Boolean setUserPThread1Execute = false;
	//异步执行线程2执行标志位
	private  static Boolean setUserPThread2Execute = false;
	//异步执行线程3执行标志位
	private  static Boolean setUserPThread3Execute = false;
	//异步执行线程4执行标志位
	private  static Boolean setUserPThread4Execute = false;
	//异步执行线程5执行标志位
	private  static Boolean setUserPThread5Execute = false;

	@Autowired
	Executor executor;

	@ApiModelProperty("设置所有用户的用户可访问业务对应关系")
	@SuppressWarnings("rawtypes")
	@RequestMapping("/userPermission")
	public Result userPermission() {
		try {
			if(setUserPermissionFlage){
				return Result.error(ExceptionCodeEnum.RUNING.getValue().toString(),"设置程序执行中，请等待");
			}
			setUserPermissionFlage = true;
			List<SysUser> list = sysUserService.selectList(new EntityWrapper<SysUser>());

			if(list!=null&& list.size()>0){
				List<List<SysUser>>  averageList = ListUtil.averageAssign(list,5);

				//第一条线程
				List<SysUser>  list1 = averageList.get(0);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						setUserPThread1Execute=true;
						try{
							for (SysUser user : list1) {
								sysUserPermissionService.setUserPermissons(user.getUserId());
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("设置用户业务对应关系信息异常："+e.getMessage());
						}
						setUserPThread1Execute =false;
						if(!setUserPThread1Execute&& !setUserPThread2Execute&& !setUserPThread3Execute &&  !setUserPThread4Execute && !setUserPThread5Execute
								){
							setUserPermissionFlage =false;
						}
					}
				});

				//第二条线程
				List<SysUser> list2 = averageList.get(1);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						setUserPThread2Execute=true;
						try{
							for (SysUser user : list2) {
								sysUserPermissionService.setUserPermissons(user.getUserId());
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("设置用户业务对应关系信息异常："+e.getMessage());
						}
						setUserPThread2Execute =false;
						if(!setUserPThread1Execute&& !setUserPThread2Execute&& !setUserPThread3Execute &&  !setUserPThread4Execute && !setUserPThread5Execute
								){
							setUserPermissionFlage =false;
						}
					}
				});


				//第三条线程
				List<SysUser>  list3 = averageList.get(2);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						setUserPThread3Execute=true;
						try{
							for (SysUser user : list3) {
								sysUserPermissionService.setUserPermissons(user.getUserId());
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("设置用户业务对应关系信息异常："+e.getMessage());
						}
						setUserPThread3Execute =false;
						if(!setUserPThread1Execute&& !setUserPThread2Execute&& !setUserPThread3Execute &&  !setUserPThread4Execute && !setUserPThread5Execute
								){
							setUserPermissionFlage =false;
						}
					}
				});

				//第四条线程
				List<SysUser>  list4 = averageList.get(3);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						setUserPThread4Execute=true;
						try{
							for (SysUser user : list4) {
								sysUserPermissionService.setUserPermissons(user.getUserId());
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("设置用户业务对应关系信息异常："+e.getMessage());
						}
						setUserPThread4Execute =false;
						if(!setUserPThread1Execute&& !setUserPThread2Execute&& !setUserPThread3Execute &&  !setUserPThread4Execute && !setUserPThread5Execute
								){
							setUserPermissionFlage =false;
						}
					}
				});


				//第五条线程
				List<SysUser>   list5 = averageList.get(4);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						setUserPThread5Execute=true;
						try{
							for (SysUser user : list5) {
								sysUserPermissionService.setUserPermissons(user.getUserId());
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("设置用户业务对应关系信息异常："+e.getMessage());
						}
						setUserPThread5Execute =false;
						if(!setUserPThread1Execute&& !setUserPThread2Execute&& !setUserPThread3Execute &&  !setUserPThread4Execute && !setUserPThread5Execute
								){
							setUserPermissionFlage =false;
						}
					}
				});


			}else{
				setUserPermissionFlage = false;
			}

//			for (SysUser user : list) {
//				sysUserPermissionService.setUserPermissons(user.getUserId());
//			}

			Thread.sleep(10*1000);


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
			LOGGER.error("--AdminController--设置所指定用户户可访问业务对照关系失败！ 用户ID："+userId, e);
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
