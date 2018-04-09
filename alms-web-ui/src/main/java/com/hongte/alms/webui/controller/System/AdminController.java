package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class AdminController {
	@RequestMapping("/admin")
	public String syncCollection() {
		return "/System/admin";
	}

	@RequestMapping("/admin/setSingleUserPermissons")
	public String setSingleUserPermissons() {
		return "/System/singleUserPermissons";
	}
}
