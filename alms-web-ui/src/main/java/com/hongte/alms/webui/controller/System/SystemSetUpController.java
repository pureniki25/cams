package com.hongte.alms.webui.controller.System;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/systemSetUpUI")
public class SystemSetUpController {
    //流程类型
    @RequestMapping("processType")
    public String sms(ModelMap map){
        return "/SystemSetUp/processType";
    }
    //短信详情界面
//   @RequestMapping("infoSmsDetailUI")
//    public String infoSmsDetail(
//           @RequestParam("originalBusinessId") String originalBusinessId,
//            @RequestParam("afterId") String afterId,
//            @RequestParam("recipient") String recipient,
//            @RequestParam(value="phoneNumber") String phoneNumber,
//            @RequestParam(value="sendDate") String sendDate,
//            @RequestParam(value="status") String status,
//            @RequestParam(value="content") String content,
//            @RequestParam(value="logId") String logId,
//           ModelMap map
//            )
//   {
//        map.addAttribute("originalBusinessId",originalBusinessId);
//        map.addAttribute("afterId",afterId);
//        map.addAttribute("recipient",recipient);
//        map.addAttribute("phoneNumber",phoneNumber);
//        map.addAttribute("sendDate",sendDate);
//        map.addAttribute("status",status);
//        map.addAttribute("content",content);
//        map.addAttribute("logId",logId);
//        return "/info/smsDetail";
//    }
//   



}
