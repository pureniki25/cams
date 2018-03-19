package com.hongte.alms.core.controller;

import com.hongte.alms.base.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:喻尊龙
 * @date: 2018/3/13
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    @Qualifier("LoginService")
    private LoginService loginService;

    @RequestMapping("/saveloginInfo")
    public void saveloginInfo(){
        loginService.saveloginInfo();
    }
}
