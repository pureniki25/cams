package com.hongte.alms.webui.controller.System;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户管理的区域表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-03-20
 */
@Controller
@RequestMapping("/sys/userarea")
public class SysUserAreaController {

	@RequestMapping("/index")
	public String index() {
		return "/System/userarea" ;
	}
}

