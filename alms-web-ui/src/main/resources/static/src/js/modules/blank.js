/*
* layui入口文件
* */

/**
 空白页模板
 仅依赖layui的layer和form模块
 **/
layui.define(['layer', 'form'], function(exports){
    var layer = layui.layer
        ,form = layui.form;
    layer.msg('Hello World');
    exports('default', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});