/**
 * Name:app.js
 * Author:Van
 * E-mail:zheng_jinfan@126.com
 * Website:http://kit.zhengjinfan.cn/
 * LICENSE:MIT
 */
var tab;
layui.define(['element', 'nprogress', 'form', 'table', 'loader', 'tab', 'navbar', 'onelevel', 'laytpl', 'spa'], function(exports) {
    var $ = layui.jquery,
        element = layui.element,
        layer = layui.layer,
        _win = $(window),
        _doc = $(document),
        _body = $('.kit-body'),
        form = layui.form,
        table = layui.table,
        loader = layui.loader,
        navbar = layui.navbar,
        _componentPath = 'components/',
        spa = layui.spa;
    tab = layui.tab
    var app = {
        hello: function(str) {
            layer.alert('Hello ' + (str || 'test'));
        },
        config: {
            type: 'iframe'
        },
        set: function(options) {
            var that = this;
            $.extend(true, that.config, options);
            return that;
        },
        init: function() {
            var that = this,
                _config = that.config;
            if (_config.type === 'spa') {
                navbar.bind(function(data) {
                    spa.set({
                        // openWait: true
                    }).render(data.url, function() {
                        console.log('渲染完成..');
                    });
                });
            }
            if (_config.type === 'page') {
                tab.set({
                    renderType: 'page',
                    mainUrl: 'table.html',
                    elem: '#container',
                    onSwitch: function(data) { //选项卡切换时触发
                        //console.log(data.layId); //lay-id值
                        //console.log(data.index); //得到当前Tab的所在下标
                        //console.log(data.elem); //得到当前的Tab大容器
                    },
                    closeBefore: function(data) { //关闭选项卡之前触发
                        // console.log(data);
                        // console.log(data.icon); //显示的图标
                        // console.log(data.id); //lay-id
                        // console.log(data.title); //显示的标题
                        // console.log(data.url); //跳转的地址
                        return true; //返回true则关闭
                    }
                }).render();
                //navbar加载方式一，直接绑定已有的dom元素事件                
                navbar.bind(function(data) {
                    tab.tabAdd(data);
                });
            }
            if (_config.type === 'iframe') {
                tab.set({
                    //renderType: 'iframe',
                    //mainUrl: 'table.html',
                    //openWait: false,
                    elem: '#container',
                    onSwitch: function(data) { //选项卡切换时触发
                        //console.log(data.layId); //lay-id值
                        //console.log(data.index); //得到当前Tab的所在下标
                        //console.log(data.elem); //得到当前的Tab大容器
                    },
                    closeBefore: function(data) { //关闭选项卡之前触发
                        // console.log(data);
                        // console.log(data.icon); //显示的图标
                        // console.log(data.id); //lay-id
                        // console.log(data.title); //显示的标题
                        // console.log(data.url); //跳转的地址
                        return true; //返回true则关闭
                    }
                }).render();
                //navbar加载方式一，直接绑定已有的dom元素事件                
                /*navbar.bind(function(data) {
                    tab.tabAdd(data);
                });*/
                //navbar加载方式二，设置远程地址加载
              /*  navbar.set({
                    remote: {
                        url:  '/alms-core-service/modules'

                    }
                }).render(function(data) {
                    tab.tabAdd(data);
                });*/
                //navbar加载方式三，设置data本地数据
                // navbar.render(function(data) {
                //     tab.tabAdd(data);
                // });

                //处理顶部一级菜单
                var onelevel = layui.onelevel;
                if (onelevel.hasElem()) {
                    onelevel.set({
                        remote: {
                            url: '/core/modules/onelevel' //远程地址
                        },
                        onClicked: function(id) {
                            var url="/core/modules/children?parentId="+id;
                            navbar.set({
                                remote: {
                                    url:url
                                }
                            }).render(function(data) {
                                tab.tabAdd(data);
                            });
                        },
                        renderAfter: function(elem) {
                            //模拟点击第一个一级目录
                            var $firtElem=elem.find('li').eq(0);
                            if($firtElem!=null && $firtElem.length!=0){
                                $firtElem.addClass("layui-this");
                                $firtElem.click();
                            }
                        }
                    }).render();
                }
            }

            // ripple start
            var addRippleEffect = function(e) {
                // console.log(e);
                layui.stope(e)
                var target = e.target;
                if (target.localName !== 'button' && target.localName !== 'a') return false;
                var rect = target.getBoundingClientRect();
                var ripple = target.querySelector('.ripple');
                if (!ripple) {
                    ripple = document.createElement('span');
                    ripple.className = 'ripple'
                    ripple.style.height = ripple.style.width = Math.max(rect.width, rect.height) + 'px'
                    target.appendChild(ripple);
                }
                ripple.classList.remove('show');
                var top = e.pageY - rect.top - ripple.offsetHeight / 2 - document.body.scrollTop;
                var left = e.pageX - rect.left - ripple.offsetWidth / 2 - document.body.scrollLeft;
                ripple.style.top = top + 'px'
                ripple.style.left = left + 'px'
                ripple.classList.add('show');
                return false;
            }
            document.addEventListener('click', addRippleEffect, false);
            // ripple end

            return that;
        }
    };

    //输出test接口
    exports('app', app);
});


