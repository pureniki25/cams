/**
 *
 */
package com.hongte.alms.webui.controller.finance;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hongte.alms.common.util.ExportFileUtil;

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
	
	@RequestMapping("/repaymentPlanInfo")
	public String repaymentPlanInfo() {
		return "/finance/repaymentPlanInfo" ;
	}
	
	@RequestMapping("/confirmWithhold")
	public String confirmWithhold() {
		return "/finance/confirmWithhold" ;
	}
	
	@RequestMapping("/settle")
	public String settle() {
		return "/finance/settle" ;
	}
	
	@RequestMapping("/moneyPool")
	public String moneyPool() {
		return "/finance/moneyPool" ;
	}

	@RequestMapping("/downloadTemplate")
	public ResponseEntity<byte[]> moneyPoolTemplate() throws IOException {
		final String SEPARATOR = File.separator ;
		final String root = ResourceUtils.getURL("classpath:").getPath();
		String path = SEPARATOR+"templates"+SEPARATOR + "finance" + SEPARATOR + "moneyPoolTemplate.xlsx" ;
		return ExportFileUtil.download(root+path, "款项池银行流水模板.xlsx");
//		return ExportFileUtil.download("src"+SEPARATOR+"main"+SEPARATOR+"resources"+SEPARATOR+"templates"+SEPARATOR+"finance"+SEPARATOR+"moneyPoolTemplate.xlsx", "款项池银行流水模板.xlsx");
	}


	/**
	 * 财务人员跟单设置
	 * @return
	 */
	@RequestMapping("/orderSet")
	public String orderSet(){return "/finance/orderSet";	}



}
