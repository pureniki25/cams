package com.hongte.alms.webui.controller.System;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 财务人员跟单设置控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
@Controller
@RequestMapping("/sys/finaincialOrder")
public class SysFinancialOrderController {

	@RequestMapping("/finaincialOrder")
	public String index() {
		return "/System/finaincialOrder" ;
	}
}

