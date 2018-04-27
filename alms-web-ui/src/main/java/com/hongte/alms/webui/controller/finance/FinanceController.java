package com.hongte.alms.webui.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/finance")
public class FinanceController {

	@RequestMapping("/index")
	public String houseLoan() {
		return "/finance/index" ;
	}
}
