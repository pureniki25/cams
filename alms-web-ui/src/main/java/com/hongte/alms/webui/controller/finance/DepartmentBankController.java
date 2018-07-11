package com.hongte.alms.webui.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 张贵宏 on 2018/7/6 18:07
 */
@Controller
@RequestMapping("/departmentBank")
public class DepartmentBankController {
    @RequestMapping("/departmentBank")
    public String index() {
        return "/finance/departmentBank" ;
    }
}
