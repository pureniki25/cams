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
        , loginPath: "/login"
        , indexPath: "/index"
        , loadMenuUrl: base + "uc/auth/loadMenu"
        //, baseInfo: base + "uc/user/in/selfinfo"
        , loadBtnAndTabUrl: base + "uc/auth/loadBtnAndTab"
        , loginUrl: base + "uaa/auth/login"
        , refreshTokenUrl: base + "uaa/auth/token"

        //--------------   切换是否使用网关 --------------
        //是否使用网关的标志位
        ,useGateWayflage:useGateWayflage
        // ,useGateWayflage:true
        // ,defaultUser:"admin-alms"
        ,defaultUser:"0101255141"   //贷后中心清算一部主管
        // ,defaultUser:"test_leader"  //区域贷后主管
        ,localBasePath:"http://localhost:30606/"
        ,openBasePath:"http://localhost:30616/"
        ,uiBasePath:"http://localhost:30601/"

        //--------------   切换是否使用网关 --------------
    });
});