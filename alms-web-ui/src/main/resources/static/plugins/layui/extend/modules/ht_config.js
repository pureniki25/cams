/**
 * add by tanrq 2018/1/21
 */
layui.define(function (exports) {
    // var base = "http://172.16.200.110:30111/";
    var base = gateWayUrl;
    // "http://172.16.200.110:30111/";
//     var base = "http://localhost:30111/";
    exports('ht_config', {
        app: "ALMS"
        , basePath: base + "alms/"
        , basePath1: base + "xxx/"
        , gatewayUrl:base
        , loginPath: "/login"
        , indexPath: "/index"
        , loadMenuUrl: base + "uc/auth/loadMenu"
        //, baseInfo: base + "uc/user/in/selfinfo"
        , loadBtnAndTabUrl: base + "uc/auth/loadBtnAndTab"
        , loginUrl: base + "uaa/auth/login"
        , refreshTokenUrl: base + "uaa/auth/token"
        ,instanceId:instanceId

        //--------------   切换是否使用网关 --------------
        //是否使用网关的标志位
        ,useGateWayflage:useGateWayflage
        // ,useGateWayflage:true
        // ,defaultUser:"admin-alms"
        ,defaultUser:"0111130000"   //贷后中心清算一部主管
        //,defaultUser:"0111200049"//何靖
        // ,defaultUser:"test_leader"  //区域贷后主管
        ,localBasePath:"http://localhost:30606/"
        ,openBasePath:"http://localhost:30616/"
        ,uiBasePath:"http://localhost:30601/"
    	/*
    	Dev：http://172.16.200.110:30131/html/user/changePwdOther.html
    	Sit: http://172.16.200.112:30131/html/user/changePwdOther.html
    	UAT：http://120.79.154.198:30131/html/user/changePwdOther.html
    	PRD：http://120.79.162.165:30131/html/user/changePwdOther.html
    	*/
        ,changePwdPath:"http://172.16.200.110:30131/html/user/changePwdOther.html"
        //--------------   切换是否使用网关 --------------
    });
    window.YYYYMMDD="YYYY年MM月DD日"
    window.YYYYMM="YYYY年MM月"
});