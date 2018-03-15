package com.hongte.alms.webui.controller.Collection.strategy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dengzhiming
 * @date 2018/3/5 11:35
 */
@Controller
@RequestMapping("/collectionStrategyUI")
public class CollectionStrategyUIController {
    //催收策略首页
    @RequestMapping("index")
    public String index(){
//        map.addAttribute("staffType","phoneStaff");
        return "/Collection/strategy/index";
    }

}
