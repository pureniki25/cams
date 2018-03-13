package com.hongte.alms.webui.controller.ExpenseSettle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/expenseSettle")
public class ExpenseSettleController {

	@RequestMapping("/houseLoan")
	public String houseLoan() {
		return "/ExpenseSettle/houseLoan" ;
	}
}
