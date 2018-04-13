
var _loadBtnAndTabUrl="";

var htConfig;

window.layinit = function (cb) {
    axios.defaults.headers.common['app'] = 'ALMS';
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + getToken();

    


    // axios.defaults.headers.common['instanceId'] = coreInstancedId;
    // if()

    // axios.defaults.headers.common['instanceId'] = 'Bearer ' + getToken();
 /*   $.ajax({
        type : 'GET',
        async : false,
        // url : "http://10.110.1.240:30111/uc/auth/loadBtnAndTab",
        // url : "http://localhost:30111/uc/auth/loadBtnAndTab",
        url : "/properties/config.js",
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            htConfig = JSON.parse(str)
            // allAuth = data;
        },
        error : function() {
/!*            layer.confirm('Navbar error:AJAX请求出错!', function(index) {
                top.location.href = loginUrl;
                layer.close(index);
            });*!/
            return false;
        }
    });*/

    layui.config({
        base : '/plugins/layui/extend/modules/',
        version : '1.0.11'
    }).use(['layer', 'table','ht_ajax', 'ht_auth', 'ht_config','laydate','upload','numeral','moment'], function () {
        // htConfig = window.properties.config;
		// if(!(window.properties.config.useGateWayflage)){
         //    axios.defaults.headers.common['userId'] = window.properties.config.loginUserId;
		// }
        htConfig = layui.ht_config;
        _loadBtnAndTabUrl =  htConfig.loadBtnAndTabUrl;

       if(!htConfig.useGateWayflage){
           /*  $.ajax({
                type : 'GET',
                async : false,
                // url : "http://10.110.1.240:30111/uc/auth/loadBtnAndTab",
                // url : "http://localhost:30111/uc/auth/loadBtnAndTab",
                url : htConfig.basePath +"core/" +"SysUser/getUserIdByToken",
                headers : {
                    app : 'ALMS',
                    Authorization : "Bearer " + getToken()
                },
                success : function(data) {
                    axios.defaults.headers.common['userId'] = data;
                },
                error : function() {
                    axios.defaults.headers.common['userId'] = htConfig.defaultUser;
                }
            });*/
            htConfig.basePath = htConfig.localBasePath;
            htConfig.coreBasePath = htConfig.localBasePath;
            htConfig.uiBasePath = htConfig.uiBasePath;
        axios.defaults.headers.common['userId'] = htConfig.defaultUser;
        }else{
            // htConfig.basePath =  htConfig.basePath +"core/"
            htConfig.coreBasePath = htConfig.basePath +"core/";
            htConfig.openBasePath = htConfig.basePath +"open/";
            htConfig.uiBasePath = htConfig.uiBasePath;
        }
        axios.interceptors.request.use(function (config) {
            layui.ht_ajax.validationAndRefreshToken()
            return config;
        }, function (error) {
            // 对请求错误做些什么
            return Promise.reject(error);
        });
        cb(htConfig)
    });


}


var getCookieValue = function(name) {
	var name = escape(name);
	// 读cookie属性，这将返回文档的所有cookie
	var allcookies = document.cookie;
	// 查找名为name的cookie的开始位置
	name += "=";
	var pos = allcookies.indexOf(name);
	// 如果找到了具有该名字的cookie，那么提取并使用它的值
	// 如果pos值为-1则说明搜索"version="失败
	if (pos != -1) {
		// cookie值开始的位置
		var start = pos + name.length;
		// 从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置
		var end = allcookies.indexOf(";", start);
		// 如果end值为-1说明cookie列表里只有一个cookie
		if (end == -1)
			end = allcookies.length;
		// 提取cookie的值
		var value = allcookies.substring(start, end);
		// 对它解码
		return unescape(value);
	} else
		return "";
}

var getToken = function() {
	return getCookieValue("token");
}


var allAuth;

var authValid = function(param) {
	if(!htConfig.useGateWayflage){

	}


	getAuth();
	for (var i = 0; i < allAuth.length; i++) {
		if (allAuth[i].resContent == param) {
			return true;
		}
	}
	return false;
}
/**
 * 取得头部的需要添加的信息（ajax）
 * @param serviceFlage
 */
/*var getAjaxHead = function(serviceFlage){
	var heads = {
        app : 'ALMS',
        Authorization : "Bearer " + getToken()
	}
	if(serviceFlage=="open"){
        headers.instanceId = openInstancedId;
	}else{
        headers.instanceId = coreInstancedId;
	}
}*/
/**
 * 如果axios请求的不是默认的服务类型就在这
 * @param xhr
 * @param serviceFlage
 */
/*var setXiosReqHead =  function (xhr,serviceFlage) {
    xhr.setRequestHeader("app", config.app);
    if(serviceFlage=="open"){
        xhr.setRequestHeader("instanceId", openInstancedId);
    }else{
        xhr.setRequestHeader("instanceId", coreInstancedId);
	}
}*/

var getAuth = function() {

	$.ajax({
		type : 'GET',
		async : false,
		// url : "http://10.110.1.240:30111/uc/auth/loadBtnAndTab",
		// url : "http://localhost:30111/uc/auth/loadBtnAndTab",
		url : _loadBtnAndTabUrl,
		headers : {
			app : 'ALMS',
			Authorization : "Bearer " + getToken()
		},
		success : function(data) {
			allAuth = data;
		},
		error : function() {
			layer.confirm('Navbar error:AJAX请求出错!', function(index) {
				top.location.href = loginUrl;
				layer.close(index);
			});
			return false;
		}
	});
}
