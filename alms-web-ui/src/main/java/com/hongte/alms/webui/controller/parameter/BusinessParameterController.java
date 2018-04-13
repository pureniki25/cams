package com.hongte.alms.webui.controller.parameter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/businessParameter")
public class BusinessParameterController {
	
	@RequestMapping("/fiveLevelClassify")
	public String fiveLevelClassify() {
		return "/businessParameter/fiveLevelClassify";
	}
}
