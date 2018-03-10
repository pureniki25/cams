package com.hongte.alms.webui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zengkun
 * @since 2018/2/11
 * 流程UI界面
 */
@Controller
@RequestMapping("/ProcessUI")
public class ProcessUI {

    //我的审批

    /**
     *
     * @param reqPageeType waitToApprove 需要我审批的;
     *                     Approved 我已审批的;
     *                     SelfStart 我发起的;
     *                     CopySendToMe 抄送我的;
     *                     Search 审批查询;
     * @return
     */
    @RequestMapping("ProcessWaitToApproveUI/{status}")
    public String ProcessWaitToApprove(
            @PathVariable(value = "status") String reqPageeType  ,
            ModelMap map
//            @RequestParam(value="processId",required=false) String processId,
    ){
        map.addAttribute("reqPageeType",reqPageeType);
        return "/Collection/ProcessWaitToApprove";
    }

//    ProcessWaitToApprove
}
