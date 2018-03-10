package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/14 0014 上午 1:28
 */
@Controller
@RequestMapping("/system")
public class ModuleController {
    @RequestMapping("/module/index")
    public String index(){
        return "/System/Module/index";
    }
}
