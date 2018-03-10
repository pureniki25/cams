package com.hongte.alms.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("")
public class HomeController {

    @RequestMapping(value ="/index",method = RequestMethod.GET)
    public String index(){
        //主页使用thymeleaf视图模板生成动态首页（动态视图存放在templates文件夹内）
        //其他页面使用静态页面(静态视图存放在static文件夹内)
        return "/index";
    }

    @RequestMapping("/main")
    public String main(){
        return "/Home/main";
    }
    
    @RequestMapping("/login")
    public String login(){
        return "/login";
    }
}
