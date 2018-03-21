package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 定时器任务设置
 * @author:喻尊龙
 * @date: 2018/3/21
 */
@Controller
@RequestMapping("/sys/timerSet")
public class TimerSetController {

    @RequestMapping("/index")
    public String index() {
        return "/System/timerSet" ;
    }
}
