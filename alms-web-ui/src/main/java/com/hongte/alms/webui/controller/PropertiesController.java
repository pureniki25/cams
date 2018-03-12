package com.hongte.alms.webui.controller;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.common.result.Result;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengkun
 * @since 2018/3/10
 */
@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Resource
    LoginUserInfoHelper loginUserInfoHelper;

    @Value("${ht.config.ui.gatewayUrl}")
    private String gatewayUrl;  //配置的网关
    @Value("${ht.config.ui.coreInstancedId}")
    private String coreInstancedId;  //core服务标识的访问实例
    @Value("${ht.config.ui.openInstancedId}")
    private String openInstancedId;  //open服务标识的访问实例



    @Value("${ht.config.ui.useGateWayflage}")
    private Boolean useGateWayflage;//是否使用网关的标志位

    @Value("${ht.config.ui.defaultUser}")
    private String defaultUser;//不使用网关的默认用户
    @Value("${ht.config.ui.localCoreBasePath}")
    private String coreBasePath;//不使用网关时core的链接地址
    @Value("${ht.config.ui.localOpenBasePath}")
    private String OpenBasePath;//不使用网关时open的链接地址

//    ht.config.ui.useGateWayflage=false
//    ht.config.ui.defaultUser="admin-alms"
//    ht.config.ui.coreBasePath="http://localhost:30606/"
//    ht.config.ui.OpenBasePath="http://localhost:30616/"

    @GetMapping(value = "config.js",produces = "application/javascript")
    public String config (){

        Map<String ,Object> mapper = new HashMap<String,Object>();
        mapper.put("gatewayUrl",gatewayUrl);
        String basePath = gatewayUrl+"alms/";
        if(!useGateWayflage){

//            mapper.put("loginUserId",defaultUser);
            mapper.put("loginUserId",loginUserInfoHelper.getUserId());
            mapper.put("coreBasePath",coreBasePath);
            mapper.put("OpenBasePath",OpenBasePath);
        }else{
            mapper.put("coreBasePath",basePath+"core/");
            mapper.put("OpenBasePath",basePath+"open/");
        }

        mapper.put("useGateWayflage",useGateWayflage);
        mapper.put("basePath",basePath);
        mapper.put("app","ALMS");
        mapper.put("loginPath","/login");
        mapper.put("indexPath","/index");
        mapper.put("loadMenuUrl",gatewayUrl + "uc/auth/loadMenu");
        mapper.put("loadBtnAndTabUrl",gatewayUrl + "uc/auth/loadBtnAndTab");
        mapper.put("refreshTokenUrl",gatewayUrl + "uaa/auth/token");


//        return String.format("var ht_properties=%s;",JSONObject.toJSONString(mapper));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("var gateWayUrl='%s';",gatewayUrl));
        stringBuilder.append("\r");
        stringBuilder.append(String.format("var coreInstancedId='%s';",coreInstancedId));
        stringBuilder.append("\r");
        stringBuilder.append(String.format("var openInstancedId='%s';",openInstancedId));
        stringBuilder.append("\r");
        stringBuilder.append(String.format("var useGateWayflage=%s;",useGateWayflage));
        stringBuilder.append("\r");

        return stringBuilder.toString();
//        return String.format("var gateWayUrl='%s';",gatewayUrl);
//        return
    }


  /*  #layui.define(function (exports) {
#var base = "http://172.16.200.110:30111/";
#//     var base = "http://localhost:30111/";
#exports('ht_config', {
                #app: "ALMS"
#, basePath: base + "alms/"
#, basePath1: base + "xxx/"
#, loginPath: "/login"
#, indexPath: "/index"
#, loadMenuUrl: base + "uc/auth/loadMenu"
#//, baseInfo: base + "uc/user/in/selfinfo"
#, loadBtnAndTabUrl: base + "uc/auth/loadBtnAndTab"
#, loginUrl: base + "uaa/auth/login"
#, refreshTokenUrl: base + "uaa/auth/token"
#
#//--------------   切换是否使用网关 --------------
#//是否使用网关的标志位
#,useGateWayflage:false
#// ,useGateWayflage:true
#// ,defaultUser:"admin-alms"
#,defaultUser:"test_area_hd_leader"   //贷后中心清算一部主管
#// ,defaultUser:"test_leader"  //区域贷后主管
#,localBasePath:"http://localhost:30606/"
#,openBasePath:"http://localhost:30616/"
#//--------------   切换是否使用网关 --------------
#});
#});*/
}
