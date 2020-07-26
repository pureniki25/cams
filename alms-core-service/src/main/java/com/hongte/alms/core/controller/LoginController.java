package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.LoginUser;
import com.hongte.alms.base.entity.Menu;
import com.hongte.alms.base.service.LoginService;
import com.hongte.alms.base.service.LoginUserService;
import com.hongte.alms.base.service.MenuService;
import com.hongte.alms.base.vo.user.MenuVo;
import com.hongte.alms.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import com.hongte.alms.common.util.TokenUtil;
import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:喻尊龙
 * @date: 2018/3/13
 */
@RestController
@RequestMapping("/login")
@Api(tags = "LoginController", description = "用户登录相关接口")
public class LoginController {

	@Autowired
	@Qualifier("LoginService")
	private LoginService loginService;

	@Autowired
	@Qualifier("LoginUserService")
	private LoginUserService loginUserService;

	@RequestMapping("/saveloginInfo")
	public void saveloginInfo() {
		loginService.saveloginInfo();
	}

	@Autowired
	@Qualifier("MenuService")
	private MenuService menuService;


	@RequestMapping("/login")
	public Result login(@RequestBody Map<String, Object> params) {
		Result result = new Result();
		String userName = params.get("userName").toString();
		String password = params.get("password").toString();
		LoginUser loginUser = loginUserService
				.selectOne(new EntityWrapper<LoginUser>().eq("user_name", userName).eq("user_password", password));
		if (loginUser != null) {
			return result.success(loginUser);
		} else {
			return result.error("9999", "密码错误或用户不存在");
		}
	}

	@RequestMapping("/findUser")
	public Result findUser(HttpServletRequest request) {
		String token= TokenUtil.getToekn(request);
		LoginUser loginUser = loginUserService
				.selectOne(new EntityWrapper<LoginUser>().eq("token", token));
		if (loginUser != null) {
			return Result.success(loginUser);
		} else {
			return Result.error("9999", "用户不存在");
		}
	}

	@RequestMapping("/loadMenu")
	public List<MenuVo> loadMenu() {
		List<Menu> menus = menuService.selectList(new EntityWrapper<Menu>().eq("spread", 0));
       		List<MenuVo> menuVos=new ArrayList();
		for(Menu menu:menus) {
			MenuVo vo=new MenuVo();
			BeanUtil.copyProperties(menu, vo);
			if(menu.getSpread()==0) {
				menuVos.add(vo);
			}
		}
		List<Menu> childrenMenus = menuService.selectList(new EntityWrapper<Menu>().eq("spread", 1));
		for(Menu menu:childrenMenus) {
			for(MenuVo menuVo:menuVos) {
				if(menu.getParentId().equals(menuVo.getId())) {
					MenuVo childrenVo=new MenuVo();
					BeanUtil.copyProperties(menu, childrenVo);
					menuVo.getChildren().add(childrenVo);
				}
			}
		}
		return menuVos;

	}
}
