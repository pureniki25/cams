/**
 *
 */
package com.hongte.alms.webui.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王继光
 * 2018年4月24日 上午9:29:39
 */
@Controller
@RequestMapping("/finance")
public class FinanceController {

	@RequestMapping("/index")
	public String index() {
		return "/finance/index" ;
	}

	@RequestMapping("/manualMatchBankSatements")
	public String manualMatchBankSatements() {
		return "/finance/manualMatchBankSatements" ;
	}
	
	@RequestMapping("/repayConfirm")
	public String repayConfirm() {
		return "/finance/repayConfirm" ;
	}
	
	@RequestMapping("/repayBaseInfo")
	public String repayBaseInfo() {
		return "/finance/repayBaseInfo" ;
	}
}
