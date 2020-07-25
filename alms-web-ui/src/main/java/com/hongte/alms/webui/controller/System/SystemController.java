package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemController {
	@RequestMapping("/companySetUp")
	public String syncCollection() {
		return "/System/companySetUp";
	}
}
