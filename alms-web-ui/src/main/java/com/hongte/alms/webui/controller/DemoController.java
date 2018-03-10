package com.hongte.alms.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/12 0012 上午 12:21
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/blank")
    public String blank(){
        return "/Demo/blank";
    }
}
