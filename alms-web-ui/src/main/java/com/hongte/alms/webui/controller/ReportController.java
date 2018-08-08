package com.hongte.alms.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

	/**
	 * 应收实收台账表
	 * @return
	 */
	@RequestMapping("/receivableAccountReport")
	public String receivableAccountReport() {
		return "/report/receivableAccountReport";
	}
	
	/**
	 * 逾期业务汇总表
	 * @return
	 */
	@RequestMapping("/overdueBusinessReport")
	public String overdueBusinessReport() {
		return "/report/overdueBusinessReport";
	}
}
