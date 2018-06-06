
var _loadBtnAndTabUrl="";

var htConfig;

window.layinit = function (cb) {
    axios.defaults.headers.common['app'] = 'ALMS';
    axios.defaults.headers.common['Authorization'] = 'Bearer ' + getToken();

    layui.config({
        base : '/plugins/layui/extend/modules/',
        version : '1.0.11'
    }).use(['layer', 'table','ht_ajax', 'ht_auth', 'ht_config','laydate','upload','numeral','moment'], function () {
        // htConfig = window.properties.config;
		// if(!(window.properties.config.useGateWayflage)){
         //    axios.defaults.headers.common['userId'] = window.properties.config.loginUserId;
        // }
        
        numeral.defaultFormat('0,0.00');
        htConfig = layui.ht_config;
        _loadBtnAndTabUrl =  htConfig.loadBtnAndTabUrl;

       if(!htConfig.useGateWayflage){
            htConfig.basePath = htConfig.localBasePath;
            htConfig.coreBasePath = htConfig.localBasePath;
            htConfig.financeBasePath = htConfig.financeBasePath;
            htConfig.platRepayBasePath = htConfig.platRepayBasePath;
            htConfig.uiBasePath = htConfig.uiBasePath;
        axios.defaults.headers.common['userId'] = htConfig.defaultUser;
        }else{
            // htConfig.basePath =  htConfig.basePath +"core/"
            htConfig.coreBasePath = htConfig.basePath +"core/";
            htConfig.openBasePath = htConfig.basePath +"open/";
            htConfig.financeBasePath = htConfig.basePath +"finance/";
            htConfig.platRepayBasePath = htConfig.basePath +"platRepay/";
            htConfig.uiBasePath = htConfig.uiBasePath;
        }
        //axios 访问前处理token问题
        axios.interceptors.request.use(function (config) {
            layui.ht_ajax.validationAndRefreshToken()
            axios.defaults.headers.common['Authorization'] = 'Bearer ' + getToken();
            return config;
        }, function (error) {
            // 对请求错误做些什么
            return Promise.reject(error);
        });

       //jquery ajax 访问前处理token问题
		if(typeof $ != "undefined"){
            layui.ht_ajax.extendsAjax($);
        }


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
var menuCode = (function(){
    // forEach 是无法中断的。除非用这种hack
    try {
        top.document.querySelectorAll('.layui-nav-tree a[data-options]').forEach(function (e, i) {
            var options = $(e).attr("data-options")
            if (~options.indexOf(window.location.pathname)) {
                throw new Error(eval('(' + options + ')').id)
            }
        });
    } catch(err) {
        return err.message
    }
}());

console.log(menuCode);

var authValid = function(param) {
	if(!htConfig.useGateWayflage){

	}



	getAuth();
	for (var i = 0; i < allAuth.length; i++) {
		if (allAuth[i].resContent == param&&  menuCode == allAuth[i].resParent) {
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

	if(typeof allAuth == "undefined" ){
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
                console.log("权限",JSON.stringify(data));
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
	

}
