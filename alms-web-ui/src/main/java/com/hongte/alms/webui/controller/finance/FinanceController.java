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
	
	@RequestMapping("/manualAddBankSatements")
	public String manualAddBankSatements() {
		return "/finance/manualAddBankSatements" ;
	}
	
	@RequestMapping("/repayConfirm")
	public String repayConfirm() {
		return "/finance/repayConfirm" ;
	}
	
	@RequestMapping("/repayBaseInfo")
	public String repayBaseInfo() {
		return "/finance/repayBaseInfo" ;
	}
	
	@RequestMapping("/repayRegList")
	public String repayRegList() {
		return "/finance/repayRegList" ;
	}
	
	@RequestMapping("/matchedBankStatement")
	public String matchedBankStatement() {
		return "/finance/matchedBankStatement";
	}
	
	@RequestMapping("/thisPeriodRepayment")
	public String thisPeriodRepayment() {
		return "/finance/thisPeriodRepayment" ;
	}
	
	@RequestMapping("/confirmedRepayment")
	public String confirmedRepayment() {
		return "/finance/confirmedRepayment" ;
	}
}
