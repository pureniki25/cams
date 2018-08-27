/**
 * add by tanrq 2018/1/21
 */
layui.define(function (exports) {
    var _basePath = 'http://localhost:30111/';
    try {
        _basePath = gatewayUrl ? gatewayUrl : _basePath;
    } catch (e) {}
    var _changePasswordUrl = 'http://localhost:30111/';
    try {
        _changePasswordUrl = changePasswordUrl ? changePasswordUrl : 'http://localhost:30131/html/user/changePwdOther.html';
    } catch (e) {}
    var _changeUserInfoUrl = 'http://localhost:30111/';
    try {
        _changeUserInfoUrl = changeUserInfoUrl ? changeUserInfoUrl : 'http://localhost:30131/html/user/userInfoOther.html';
    } catch (e) {}
    var rule = 'uc',
    	rule1 = 'rp-app-service',
        rule2 = 'rp/view';
    exports('ht_config', {
        app: 'RP'
        , _basePath: _basePath
        , basePath: _basePath + rule + '/'
        , basePath1: _basePath + rule1 + '/'
        , basePath2: _basePath + rule2 + '/'
        , loginPath: "/login.html"
        , indexPath: "/"
        , changePwdPath: _changePasswordUrl
        , changeUserInfoUrl: _changeUserInfoUrl
        , loadMenuUrl: _basePath + 'uc/auth/loadMenu'
        , loadBtnAndTabUrl: _basePath + 'uc/auth/loadBtnAndTab'
        , loginUrl: _basePath + 'uaa/auth/login'
        , loadSelfinfoUrl: _basePath + 'uc/user/in/selfinfo'
        , refreshTokenUrl: _basePath + 'uaa/auth/token'
        // 全局临时变量容器
        , dataPool: { RP: {} }
    });
});