/**
 * 
 */
package com.hongte.alms.webui.controller.Notice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王继光
 * 2018年3月14日 下午7:47:19
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
	@RequestMapping("/index")
    public String index(){
//        map.addAttribute("staffType","phoneStaff");
        return "/Notice/index";
    }
}
