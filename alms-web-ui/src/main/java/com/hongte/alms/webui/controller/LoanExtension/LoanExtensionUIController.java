package com.hongte.alms.webui.controller.LoanExtension;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王继光
 *
 * @since 2018年3月6日 上午9:28:53
 */
@Controller
@RequestMapping("/loanExtUI")
public class LoanExtensionUIController {
	@RequestMapping("index")
    public String index(){
//        map.addAttribute("staffType","phoneStaff");
        return "/LoanExtension/index";
    }
}
