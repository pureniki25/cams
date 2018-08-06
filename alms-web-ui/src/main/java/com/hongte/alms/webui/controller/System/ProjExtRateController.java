package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标的额外信息管理 
 * Created by 张贵宏 on 2018/7/24 09:16
 */
@Controller
@RequestMapping("/projExtRate")
public class ProjExtRateController {
    @RequestMapping("/list")
    public String list(){
        return "/System/projExtRate";
    }
}