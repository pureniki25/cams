package com.hongte.alms.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

	/**
	 * 应收实收台账表
	 * 
	 * @return
	 */
	@RequestMapping("/receivableAccountReport")
	public String receivableAccountReport() {
		return "/report/receivableAccountReport";
	}

	/**
	 * 逾期业务汇总表
	 * 
	 * @return
	 */
	@RequestMapping("/overdueBusinessReport")
	public String overdueBusinessReport() {
		return "/report/overdueBusinessReport";
	}

	/**
	 * 贷后逾期明细表
	 * @return
	 */
	@RequestMapping("/dhOverDueDetail")
	public String dhOverDueDetail() {
		return "/report/dhOverDueDetail" ;
	}

	/**
	 * 贷后逾期汇总表
	 * @return
	 */
	@RequestMapping("/dhOverDueCollection")
	public String dhOverDueCollection() {
		return "/report/dhOverDueCollection" ;
	}
	/**
	 * 一点车贷逾期汇总表
	 * @author 王继光
	 * 2018年8月30日 下午3:13:19
	 * @return
	 */
	@RequestMapping("/ydOverDueCollection")
	public String ydOverDueCollection() {
		return "/report/ydOverDueCollection" ;
	}
	
	/**
	 * 一点车贷应实收报表
	 * @author 王继光
	 * 2018年8月30日 下午3:16:10
	 * @return
	 */
	@RequestMapping("/ydPFReport")
	public String ydPFReport() {
		return "/report/ydPFReport" ;
	}
}
