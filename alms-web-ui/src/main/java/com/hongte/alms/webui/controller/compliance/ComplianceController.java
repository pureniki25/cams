package com.hongte.alms.webui.controller.compliance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/compliance")
@Api(tags = "ComplianceController", description = "合规化还款管理接口")
public class ComplianceController {
	/**
	 * 合规化还款管理主页
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return "/compliance/index";
	}
}
