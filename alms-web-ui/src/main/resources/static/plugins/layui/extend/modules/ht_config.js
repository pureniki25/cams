/**
 * add by tanrq 2018/1/21
 */
layui.define(function (exports) {debugger
    // var base = "http://172.16.200.110:30111/";
    var base = gateWayUrl;
    // "http://172.16.200.110:30111/";
//     var base = "http://localhost:30111/";
    exports('ht_config', {
        app: "ALMS"
        , basePath: base + "alms/"
        , basePath1: base + "rp-app-service/"
        , gatewayUrl:base
        , loginPath: "/login"
        , indexPath: "/index"
        , loadMenuUrl: base + "login/loadMenu"
        //, baseInfo: base + "uc/user/in/selfinfo"
        //, loadBtnAndTabUrl: base + "login/loadBtnAndTab"
        , loginUrl: base + "login/login"
        , refreshTokenUrl: base + "uaa/auth/token"
        , loadSelfinfoUrl: base + "login/findUser"   //查询用户个人信息
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
    	
        ,localBasePath:"http://localhost:30606/"
            ,financeBasePath:"http://localhost:30621/"
            ,platRepayBasePath:"http://localhost:30631/"   
            ,openBasePath:"http://localhost:30616/"
            ,uiBasePath:"http://localhost:30601/"
            ,withholdBasePath:"http://localhost:30626/"
            ,coreBasePath:gateWayUrl
        /*
		 * Dev：http://172.16.200.110:30131/html/user/changePwdOther.html Sit:
		 * http://172.16.200.112:30131/html/user/changePwdOther.html
		 * UAT：http://120.79.154.198:30131/html/user/changePwdOther.html
		 * PRD：http://120.79.162.165:30131/html/user/changePwdOther.html
		 */
        ,changePwdPath:"http://localhost:30601/system/companySetUp.html"
        //--------------   切换是否使用网关 --------------
    });
    window.YYYYMMDD="YYYY年MM月DD日"
    window.YYYYMM="YYYY年MM月"
});