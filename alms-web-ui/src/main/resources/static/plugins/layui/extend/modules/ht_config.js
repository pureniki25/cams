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
        // , basePath: base + "alms/"
        , basePath: "http://localhost:30606/"
        , basePath1: base + "rp-app-service/"
        , gatewayUrl:base
        , loginPath: "/login"
        , indexPath: "/index"
        , loadMenuUrl: base + "uc/auth/loadMenu"
        //, baseInfo: base + "uc/user/in/selfinfo"
        , loadBtnAndTabUrl: base + "uc/auth/loadBtnAndTab"
        , loginUrl: base + "uaa/auth/login"
        , refreshTokenUrl: base + "uaa/auth/token"
        , loadSelfinfoUrl: base + "uc/user/in/selfinfo"   //查询用户个人信息
        ,instanceId:instanceId

        //--------------   切换是否使用网关 --------------
        //是否使用网关的标志位
        ,useGateWayflage:useGateWayflage
        // ,useGateWayflage:true
        // ,defaultUser:"admin-alms"
        //,defaultUser:"0101297103"   //张娟
        // ,defaultUser:"0101264424"   //温睿
         ,defaultUser:"0111130000"   //贷后中心清算一部主管
        //,defaultUser:"0111200049"//何靖
        // ,defaultUser:"test_leader"  //区域贷后主管
        ,localBasePath:"http://192.168.14.121:30606/"
        ,financeBasePath:"http://192.168.14.121:30621/"
    	,platRepayBasePath:"http://192.168.14.121:30631/"
        ,openBasePath:"http://192.168.14.121:30616/"
        ,uiBasePath:"http://192.168.14.121:30601/"
        ,withholdBasePath:"http://192.168.14.121:30626/"
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