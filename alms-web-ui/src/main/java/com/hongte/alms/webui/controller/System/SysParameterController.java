/**
 * 
 */
package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王继光
 * 2018年3月19日 上午8:51:28
 */
@Controller
@RequestMapping(value = "/sys/param")
public class SysParameterController {

	@RequestMapping("/index")
	public String index() {
		return "/System/param"; 
	}
}
