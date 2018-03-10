package com.hongte.alms.webui.controller.car;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author dengzhiming
 * @date 2018/2/24 14:59
 */
@Controller
@RequestMapping("/carUI")
public class CarUIController {

    //拖车登记
    @RequestMapping("dragRegistration")
    public String dragRegistration(){
        return "/car/dragRegistration";
    }
}
