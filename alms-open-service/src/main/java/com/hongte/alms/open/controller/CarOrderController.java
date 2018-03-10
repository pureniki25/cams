package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.common.open.OpenJsonConvert;
import com.hongte.alms.common.open.OpenRequestContent;
import com.hongte.alms.open.config.OpenServiceConfig;
import com.hongte.alms.open.util.TripleDESDecrypt;
import com.hongte.alms.open.vo.User;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("")
@Api(tags = "CarOrderController", description = "车辆信息", hidden = true)

public class CarOrderController {
    @PostMapping("/carOrders/sync")
    @TripleDESDecrypt
    public String sync(@RequestBody User user)
    {
        try {
            String s="";
            return user.getName()+" from open service"+new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

        }
        catch(Exception ex)
        {
            return ex.getMessage()+ex.getStackTrace();
        }

    }


}
