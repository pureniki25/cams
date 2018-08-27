/**
 * 通用报表模板驱动js，表格列手动切换排序功能暂未实现
 */
var useModules = [
    'ht_config',
    'ht_cookie',
    'ht_auth',
    'form',
    'laydate',
    'handsontable',
    'echarts.echarts',
    'echarts.theme.shine',
    'resource',
    'element',
    'select2',
    'laypage',
    'utils',
    'jquery_ajax_blob_arraybuffer'
];
layui.define(useModules, function (exports) {

    var Report = function (code) {
        // 页面全局预定义变量 START
        var TAG = '报表模板驱动',
            RESOURCE = 'bu/report';
        // 页面全局预定义变量 END
        console.log('TAG: ' + TAG);
        // 页面全局变量初始化 START
        var $ = layui.jquery,
            config = layui.ht_config,
            cookie = layui.ht_cookie,
            ht_auth = layui.ht_auth,
            form = layui.form,
            laydate = layui.laydate,
            handsontable = layui.handsontable,
            echarts = layui.echarts,
            resource = layui.resource,
            element = layui.element,
            select2 = layui.select2,
            laypage = layui.laypage,
            utils = layui.utils,
            isInit = true,
            queryParams = { code: code },
            codes = {},
            illustrate,
            loading,
            defaultOptions = {},
            options = {},
            widgets = {},
            echartsPngs = {}, /* Echarts图 */
            DISABLED = 'layui-btn-disabled',
            timer = 0,
            log = {
                info: function (msg) {
                    if (typeof console !== 'undefined' && console && console.info) {
                        console.info(msg);
                    }
                }, 
                error: function (msg) {
                    if (typeof console !== 'undefined' && console && console.error) {
                        console.error(msg);
                    }
                }
            },
            laypageConf = {
                limit: 20,
                limits: [20, 30, 40, 50, 100],
                layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                jump: function (obj, first) {
                    //首次不执行
                    if (!first) {
                        var qps = [];
                        var id = $(obj.elem).parent().attr('id');
                        var sort=$('div[lay-filter="' + code + '_auth"] form select[name=ranking]').val();
                        var sorts={};
                        if(sort != undefined && sort != '' ){
                            sorts[sort] = 1;
                        }
                        qps.push(makeQueryParam(id, obj.curr - 1, obj.limit, options,sorts));
                        loadData(obj.curr - 1, obj.limit, options, qps);
                    }
                }
            },
            active = {
                reset: function () {
                    options = {};
                    $.each(defaultOptions, function (name, value) {
                        $('div[lay-filter="' + code + '_auth"] form').find('input[name=' + name + '],select[name=' + name + ']').each(function () {
                            var that = $(this);
                            if (that.is('select') && 'undefined' != that.attr('lay-ignore')) {
                                if (value) {
                                    that.empty();
                                    that.append(value.opt);
                                    that.trigger('change');
                                    that.trigger({
                                        type: 'select2:select',
                                        params: {
                                            data: value.data
                                        }
                                    });
                                } else {
                                    that.empty();
                                    that.val('').trigger("change");
                                }
                            } else {
                                that.val(value);
                            }
                        });
                    });
                },
                search: function () {
                    $('div[lay-filter="' + code + '_auth"] form').find('input,select').each(function name() {
                        options[$(this).attr('name')] = null == $(this).val() ? '' : $(this).val();
                    });
                    // 发起报表搜索，搜索数据如果页码大于可搜索的，后端接口自动缩小页码，并返回页码信息
                    var qps = [];
                    var sort=$('div[lay-filter="' + code + '_auth"] form select[name=ranking]').val();
                    var sorts={};
                    if(sort != undefined && sort != '' ){
                        sorts[sort] = 1;
                    }
                    var currPage=0;
                    $.each(codes, function (id, value) {
                        currPage=value.curr - 1;
                        qps.push(makeQueryParam(id, value.curr - 1, value.limit, options,sorts));
                    });
                    loadData(currPage, laypageConf.limit, options, qps);
                },
                exportRequest: function () {
                    // 添加Echarts图导出功能，这个比较费时间，所以启动异步调用，优化前端操作体验
                    $.each(echartsPngs, function (id, value) {
                        if (value) {
                            queryParams.options[id] = value;
                        }
                    });
                    $.ajax({
                        type: 'POST',
                        url: config.basePath1 + RESOURCE + '/export',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        dataType: 'json',
                        data: JSON.stringify(queryParams),
                        async: true,
                        success: function (result) {
                            successHandler(result, function (data) {
                                var uuid = data, fileName = $('div[lay-filter="' + code + '_auth"]').attr('name') + '_' + (new Date()).Format('yyyyMMddHHmmss') + '.xlsx', n = 0, t = 0;
                                timer = setInterval(function () {
                                    var progress = $('div[lay-filter="' + code + '_auth"]').find('.layui-progress');
                                    var costTime = $('div[lay-filter="' + code + '_auth"]').find('.costTime');
                                    costTime.remove();
                                    progress.parent().append('<div class="costTime"><span>耗时：' + (t++) + '秒</span></div>');
                                    $.ajax({
                                        type: 'GET',
                                        url: config.basePath1 + RESOURCE + '/export/progress?uuid=' + uuid,
                                        async: false,
                                        success: function (result) {
                                            successHandler(result, function (data) {
                                                var othis = $('div[lay-filter="' + code + '_auth"] button[data-type=export]');
                                                if ('done' == data) {
                                                    clearInterval(timer);
                                                    enableBtn(othis);
                                                    $.ajax({
                                                        type: 'GET',
                                                        url: config.basePath1 + RESOURCE + '/download?uuid=' + uuid + '&fileName=' + fileName,
                                                        dataType: "blob"
                                                    }).done(function (data, status, jqXHR) {
                                                        var reader = new window.FileReader();
                                                        reader.readAsDataURL(data);
                                                        reader.onload = function (e) {
                                                            var blob = dataURItoBlob(e.target.result);
                                                            if (window.navigator.msSaveOrOpenBlob) {
                                                                navigator.msSaveBlob(blob, fileName);
                                                            } else {
                                                                var downloadElement = document.createElement('a');
                                                                var href = window.URL.createObjectURL(blob); //创建下载的链接
                                                                downloadElement.href = href;
                                                                downloadElement.download = fileName;
                                                                document.body.appendChild(downloadElement);
                                                                downloadElement.click(); //点击下载
                                                                document.body.removeChild(downloadElement); //下载完成移除元素
                                                                window.URL.revokeObjectURL(href); //释放掉blob对象
                                                            }
                                                        };
                                                    }).fail(function (jqXHR, textStatus) {
                                                        console.warn(jqXHR);
                                                        console.warn(textStatus);
                                                        });
                                                    element.progress(code + '_export_progress', '100%');
                                                } else {
                                                    if (n < 98) {
                                                        n = n + Math.random() * 2 | 0;
                                                    }
                                                    element.progress(code + '_export_progress', n + '%');
                                                }
                                            });
                                        },
                                        error: function name(message) {
                                            clearInterval(timer);
                                            errorHandler(message, '获取报表初始化数据发生异常，请联系管理员。');
                                        }
                                    });
                                }, 1000);
                            });
                        },
                        error: function (message) {
                            errorHandler(message, '获取报表初始化数据发生异常，请联系管理员。');
                        }
                    });
                },
                doExport: function () {
                    var othis = $('div[lay-filter="' + code + '_auth"] button[data-type=export]');
                    disableBtn(othis);
                    var progress = othis.parent().parent().parent().find('.layui-progress');
                    progress.remove();
                    othis.parent().parent().parent().append('<div class="layui-progress" lay-filter="' + code + '_export_progress" lay-showPercent="true"><div class="layui-progress-bar" lay-percent="0%"></div></div>');
                },
                export: function () {
                    var pv = 0;
                    layer.open({
                        pv: 0,
                        type: 1,
                        area: ['400px', '200px'],
                        shadeClose: true,
                        title: '报表导出设置',
                        content: '<div class="layui-form form-horizontal clearfix layui-form-item" lay-filter="' + code + '_filter_export_form">' +
                                    '<label class= "layui-form-label" style="padding: 5px 9px 0px 0px;" > 导出页选择：</label>' +
                                    '<div class="layui-input-block">' +
                                        '<input type="radio" lay-filter="' + code + '_pages" name="' + code + '_pages" value="cur" title="当前页">' +
                                        '<input type="radio" lay-filter="' + code + '_pages" name="' + code + '_pages" value="all" title="所有" checked>' +
                                        '<!-- 暂时不支持 input type="radio" lay-filter="' + code + '_pages" name="' + code + '_pages" value="cut" title="指定页" -->' +
                                    '</div>' +
                                '</div>' +
                                '<div class="layui-form form-horizontal clearfix layui-form-item" style="margin: 0px 20px 0px 20px;">' +
                                    '<input type="text" name="' + code + '_cutPages" autocomplete="off" lay-verType="tips" class="layui-input layui-input-sm" placeholder="4 or 1,2,3 or 1-4,6,9-10">' +
                                '</div>',
                        btn: ['确定', '取消'],
                        yes: function (index, layero) {
                            pv = $('input[name=' + code + '_pages]:checked', layero).val();
                            layer.close(index);
                        },
                        btn2: function () {
                            layer.closeAll();
                        },
                        success: function (layero, index) {
                            $('input[name=' + code + '_cutPages]', layero).hide();
                            form.render(null, code + '_filter_export_form');
                            form.on('radio(' + code + '_pages)', function (data) {
                                if ('cut' == data.value) {
                                    $('input[name=' + code + '_cutPages]', layero).show();
                                } else {
                                    $('input[name=' + code + '_cutPages]', layero).hide();
                                }
                            });
                        },
                        end: function () {
                            if (pv) {
                                var costTime = $('div[lay-filter="' + code + '_auth"]').find('.costTime');
                                costTime.remove();
                                if ('cur' == pv) {
                                    var qps = [];
                                    $.each(codes, function (id, value) {
                                        qps.push(makeQueryParam(id, value.curr - 1, value.limit, options));
                                    });
                                    makeQueryParams(0, laypageConf.limit, options, qps);
                                } else {
                                    makeQueryParams(null, null, options, null);
                                }
                                active.doExport();
                                active.exportRequest();
                            }
                        }
                    });
                },
                illustrate: function () {
                    layer.open({
                        type: 1,
                        area: ['800px', '600px'],
                        shadeClose: true,
                        title: '报表取值口径说明',
                        content: '<div style="padding-left: 15px; padding-right: 15px;">' + illustrate + '</div>',
                        btn: ['确定'],
                        yes: function (index, layero) {
                            layer.close(index);
                        }
                    });
                }
            },
            evil = function (fn) {
                var Fn = Function;
                return new Fn('return ' + fn)();
            },
            dataURItoBlob = function (dataURI, callback) {
                // convert base64 to raw binary data held in a string
                // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
                var byteString = atob(dataURI.split(',')[1]);

                // write the bytes of the string to an ArrayBuffer
                var ab = new ArrayBuffer(byteString.length);
                var ia = new Uint8Array(ab);
                for (var i = 0; i < byteString.length; i++) {
                    ia[i] = byteString.charCodeAt(i);
                }

                // write the ArrayBuffer to a blob, and you're done
                var bb = new Blob([ab]);
                return bb;
            },
            successHandler = function (result, callback) {
                if (result.returnCode == '0000') {
                    if (typeof callback == 'function')
                        return callback(result.data);
                } else {
                    clearInterval(timer);
                    layer.msg(result.returnCode + ':' + result.codeDesc);
                }
            },
            errorHandler = function (message, msg) {
                try {
                    if (message.responseJSON.returnCode) {
                        layer.msg(message.responseJSON.returnCode + ':' + message.responseJSON.codeDesc);
                        console.error(message.responseText);
                    } else if (message.responseJSON.exception && 'com.netflix.zuul.exception.ZuulException' == message.responseJSON.exception) {
                        layer.msg('网络请求超时，操作可能成功!<br/>请确认后再尝试重新操作，请谅解。');
                        console.error(message);
                    } else {
                        throw new Error();
                    }
                } catch (err) {
                    if (msg)
                        layer.msg(msg);
                    else
                        layer.msg('数据请求发生异常，请联系管理员。');
                    console.error(message);
                }
            },
            disableBtn = function (othis) {
                if (othis) {
                    othis.addClass(DISABLED);
                    othis.removeClass('btn');
                    othis.removeClass('btn-primary');
                }
            },
            enableBtn = function (othis) {
                if (othis) {
                    othis.removeClass(DISABLED);
                    othis.addClass('btn');
                    othis.addClass('btn-primary');
                }
            },
            clearQueryParams = function () {
                queryParams = { code: code };
            },
            makeQueryParams = function (_page, _size, _options, _qps) {
                clearQueryParams();
                if (_page || 0 == _page)
                    queryParams.page = _page;
                if (_size)
                    queryParams.size = _size;
                if (_options)
                    queryParams.options = _options;
                if (_qps)
                    queryParams.queryParams = _qps;
            },
            makeQueryParam = function (_code, _page, _size, _options, _sorts) {
                if (!_code) return;
                var queryParam = { code: _code };
                if (_page || 0 == _page)
                    queryParam.page = _page;
                if (_size)
                    queryParam.size = _size;
                if (_options)
                    queryParam.options = _options;
                if (_sorts)
                    queryParam.sorts = _sorts;
                return queryParam;
            },
            renderSelect2 = function () {
                $('div[lay-filter="' + code + '_auth"] select[data-source]').each(function () {
                    // 变量初始化
                    var that = $(this);
                    var size = 10;
                    if (that.attr('size') && 0 < that.attr('size').trim().length && 0 < that.attr('size').trim())
                        size = that.attr('size').trim();
                    var searchKey = that.attr('data-source').trim();
                    if (!searchKey || 0 == searchKey.length)
                        searchKey = 'search';
                    // 组件配置
                    that.select2({
                        dropdownAutoWidth: true,
                        allowClear: true,
                        placeholder: {
                            id: '',
                            placeholder: 'Leave blank to ...'
                        },
                        ajax: {
                            type: 'POST',
                            url: config.basePath1 + RESOURCE + '/param',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            dataType: 'json',
                            data: function (params) {
                                var opts = {};
                                opts[searchKey] = '';
                                if (params.term)
                                    opts[searchKey] = params.term;
                                makeQueryParams(null == params.page ? 0 : params.page, size, opts);
                                return JSON.stringify(queryParams);
                            },
                            processResults: function (result, params) {
                                if (result.returnCode == '0000') {
                                    var data = [];
                                    if (result.data && result.data.data)
                                        data = result.data.data;
                                    for (var i = 0; i < data.length; i++) {
                                        data[i].id = data[i].name;
                                        data[i].text = data[i].text;
                                    }
                                    var count = data.length;
                                    if (result.data && result.data.count)
                                        count = result.data.count;
                                    params.page = params.page || 0;
                                    return {
                                        results: data,
                                        pagination: {
                                            more: ((params.page + 1) * size) < count
                                        }
                                    };
                                } else {
                                    layer.msg(result.returnCode + ':' + result.codeDesc);
                                }
                            },
                            cache: false
                        }
                    });
                    // 组件默认值赋值
                    if (that.attr('defaultValue') && 0 < that.attr('defaultValue').trim().length) {
                        var defaultValue = that.attr('defaultValue').trim();
                        var opts = {};
                        opts[searchKey] = defaultValue;
                        makeQueryParams(0, size, opts);
                        $.ajax({
                            type: 'POST',
                            url: config.basePath1 + RESOURCE + '/param',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            dataType: 'json',
                            data: JSON.stringify(queryParams),
                            success: function (result) {
                                successHandler(result, function (data) {
                                    if (data && data.data) {
                                        var option;
                                        if (defaultValue) {
                                            for (var i = 0; i < data.data.length; i++) {
                                                if (defaultValue instanceof Array) {
                                                    for (var j = 0; j < defaultValue.length; j++) {
                                                        if (-1 < data.data[i].name.indexOf(defaultValue)) {
                                                            option = new Option(data.data[i].text, data.data[i].name, true, true);
                                                            that.append(option);
                                                        }
                                                    }
                                                } else {
                                                    if (-1 < data.data[i].name.indexOf(defaultValue)) {
                                                        option = new Option(data.data[i].text, data.data[i].name, true, true);
                                                        that.append(option);
                                                    }
                                                }
                                            }
                                            if (option) {
                                                defaultOptions[that.attr('name')] = { opt: that.html(), data: data.data };
                                                that.trigger('change');
                                                that.trigger({
                                                    type: 'select2:select',
                                                    params: {
                                                        data: data.data
                                                    }
                                                });
                                            }
                                        }
                                        if (!option) {
                                            defaultOptions[that.attr('name')] = null;
                                            that.val('').trigger("change");
                                        }
                                    }
                                });
                            },
                            error: function (message) {
                                errorHandler(message, '获取报表初始化数据发生异常，请联系管理员。');
                            }
                        });
                    }
                    defaultOptions[that.attr('name')] = null;
                });
            },
            renderPageBar = function (count, curr, limit, elem) {
                var id = '';
                if (elem && 0 < elem.trim().length)
                    id = '#' + elem.trim() + ' '; 
                $(id + 'div[laypage]').each(function () {
                    if ('' != id)
                        codes[elem.trim()] = { count: count, curr: curr + 1, limit: limit };
                    laypage.render($.extend({}, laypageConf, {
                        elem: this, count: count, curr: curr + 1, limit: limit
                    }));
                });
            },
            dateTimeInput = function (selector) {
                if (!selector)
                    selector = '';
                else
                    selector += ' ';
                var laydateTypes = ['yyyy', 'yyyy-MM', 'yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss', 'HH:mm:ss', 'yyyy - yyyy', 'yyyy-MM - yyyy-MM', 'yyyy-MM-dd - yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss - yyyy-MM-dd HH:mm:ss'];
                for (var j = 0; j < laydateTypes.length; j++) {
                    var laydateType = laydateTypes[j];
                    var laydateInputs = $(selector  + 'input[placeholder="' + laydateType + '"]');
                    for (var i = 0; i < laydateInputs.length; i++) {
                        var laydateConfig = {
                            elem: laydateInputs[i]
                        };
                        switch (j) {
                            case 0:
                                laydateConfig.type = 'year';
                                break;
                            case 1:
                                laydateConfig.type = 'month';
                                break;
                            case 2:
                                laydateConfig.type = 'date';
                                break;
                            case 3:
                                laydateConfig.type = 'datetime';
                                break;
                            case 4:
                                laydateConfig.type = 'time';
                                break;
                            case 5:
                                laydateConfig.type = 'year';
                                break;
                            case 6:
                                laydateConfig.type = 'month';
                                break;
                            case 7:
                                laydateConfig.type = 'date';
                                break;
                            case 8:
                                laydateConfig.type = 'datetime';
                                break;
                            default:
                                laydateConfig.type = 'date';
                                break;
                        }
                        if (4 < j) {
                            laydateConfig.range = true;
                        }
                        laydate.render(laydateConfig);
                    }
                }
            },
            renderForm = function () {
                form.render();
                renderSelect2();
                dateTimeInput('div[lay-filter="' + code + '_auth"]');
                $('div[lay-filter="' + code + '_auth"] form button, div[lay-filter="' + code + '_auth"] form .layui-icon-help').on('click', function () {
                    if ($(this).hasClass(DISABLED)) {
                        $(this).blur();
                        return false;
                    }
                    var type = $(this).data('type');
                    if (active[type])
                        active[type].call(this);
                });
            },
            loadParam = function () {
                clearQueryParams();
                $.ajax({
                    type: 'POST',
                    url: config.basePath1 + RESOURCE + '/init/param',
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    data: JSON.stringify(queryParams),
                    success: function (result) {
                        successHandler(result, renderParam);
                    },
                    error: function (message) {
                        errorHandler(message, '获取报表参数数据发生异常，请联系管理员。');
                    }
                });
            },
            renderParam = function (data) {
                // TODO: 检查页面是否有有取值口径说明div，有的话在form右上角加个？来点击弹出
                illustrate = $('div[lay-filter="' + code + '_auth"] [illustrate]');
                if (illustrate && 0 < illustrate.length) {
                    illustrate.hide();
                    illustrate = illustrate.html();
                } else
                    illustrate = null;
                if (0 < data.length) {
                    $('div[lay-filter="' + code + '_auth"] form').each(function () {
                        if (illustrate)
                            $(this).append('<a class="layui-icon layui-icon-help pull-right" data-type="illustrate"></a>');
                        $(this).append('<div class="layui-form-item"></div>');
                    });
                }
                for (var i = 0; i < data.length; i++) {
                    var dt = data[i];
                    var dfv = dt.defaultValue;
                    var htmlContent = '<div class="layui-inline">' +
                        '<label class="layui-form-label">' +
                        '${label}：</label>' +
                        '<div class="layui-input-inline">${param}</div' +
                        '</div>';
                    htmlContent = htmlContent.replace('${label}', dt.label);
                    var param = '';
                    if ('TIME' == dt.sourceType) {
                        param = '<input type="tel" name="' +
                            dt.name +
                            '" autocomplete="off" lay-verType="tips" class="layui-input layui-input-sm" value="' +
                            dfv +
                            '" placeholder="' +
                            dt.content.substring(1, dt.content.length - 1) +
                            '">';
                    } else if ('TEXT' == dt.sourceType) {
                        param = '<select name="' +
                            dt.name +
                            '" class="form-control input-sm">';
                        var option = '<option value="">--请选择--</option>';
                        if (dt.content && 0 < dt.content.trim().length) {
                            var ds = dt.content.split('|');
                            for (var j = 0; j < ds.length; j++) {
                                if (ds[j]) {
                                    var d = ds[j].split(',');
                                    var value = d[0], name = d[0];
                                    if (1 < d.length)
                                        name = d[1];
                                    var selected = '';
                                    if (dfv && dfv == value)
                                        selected = 'selected';
                                    option += '<option value="' + value + '" ' + selected + '>' + name + '</option>';
                                }
                            }
                        }
                        param += option + '</select>';
                    } else if ('SQL' == dt.sourceType) {
                        param = '<select name="' +
                            dt.name +
                            '" class="form-control input-sm" data-source="' +
                            dt.name +
                            '" defaultValue="' +
                            dfv +
                            '" lay-ignore style="width:100%;"></select>';
                    } else {
                        param = '<input type="text" name="' +
                            dt.name +
                            '" autocomplete="off" lay-verType="tips" class="layui-input layui-input-sm" value="' +
                            dfv +
                            '">';
                    }
                    htmlContent = htmlContent.replace('${param}', param);
                    $('div[lay-filter="' + code + '_auth"] form .layui-form-item').append(htmlContent);
                    defaultOptions[dt.name] = dfv;
                }
                if (0 < data.length) {
                    var buttonBar = '<div class="form-group">' +
                        '<div class="btn-group pull-right" style="margin-right: 15px;">' +
                        '<button type="button" class="btn btn-primary btn-sm" data-type="reset" ht-auth="reset">' +
                        '<i class="layui-icon" style="font-size: 10px;">&#xe6b2;</i> 重置' +
                        '</button>' +
                        '<button type="button" class="btn btn-primary btn-sm" data-type="search" ht-auth="search">' +
                        '<i class="layui-icon" style="font-size: 10px;">&#xe615;</i> 查询' +
                        '</button>' +
                        '<button type="button" class="btn btn-primary btn-sm" data-type="export" ht-auth="export">' +
                        '<i class="layui-icon" style="font-size: 10px;">&#xe601;</i> 导出' +
                        '</button>' +
                        '</div>' +
                        '</div>';
                    $('div[lay-filter="' + code + '_auth"] form').each(function () {
                        $(this).append(buttonBar);
                    });
                }
                renderForm();
            },
            loadData = function (_page, _size, _options, _qps) {
                makeQueryParams(_page, _size, _options, _qps);
                loading = layer.load(0, {
                    shade: [0.3, '#333']
                });
                $.ajax({
                    type: 'POST',
                    url: config.basePath1 + RESOURCE + '/load',
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    data: JSON.stringify(queryParams),
                    success: function (result) {
                        setTimeout(function () {
                            if (loading) {
                                layer.close(loading);
                                loading = null;
                            }
                        }, 500);
                        successHandler(result, renderData);
                    },
                    error: function (message) {
                        setTimeout(function () {
                            if (loading) {
                                layer.close(loading);
                                loading = null;
                            }
                        }, 500);
                        errorHandler(message, '获取报表内容数据发生异常，请联系管理员。');
                    }
                });
            },
            hotCellsRenderer = function (row, col, prop) {
                var that = this;
                if (null != that.cusCells && 0 < that.cusCells.length) {
                    this.renderer = function (instance, td, row, col, prop, value, cellProperties) {
                        Handsontable.renderers.TextRenderer.apply(this, arguments);
                        for (var i = 0; i < that.cusCells.length; i++) {
                            var cusCell = that.cusCells[i];
                            if ((null == cusCell.srow || null != cusCell.srow && cusCell.srow <= row) &&
                                (null == cusCell.erow || null != cusCell.erow && cusCell.erow >= row) &&
                                (null == cusCell.scol || null != cusCell.scol && cusCell.scol <= col) &&
                                (null == cusCell.ecol || null != cusCell.ecol && cusCell.ecol >= col)) {
                                if (null != cusCell.cusFun && 'function' == typeof cusCell.cusFun) {
                                    cusCell.cusFun(instance, td, row, col, prop, value, cellProperties);
                                    return;
                                }
                                if (null != cusCell.css) {
                                    for (var key in cusCell.css) {
                                        $(td).css(key, cusCell.css[key]);
                                    }
                                }
                                if (null != cusCell.className)
                                    cellProperties = $.extend({}, cellProperties, {
                                        className: cusCell.className
                                    });
                                if (null != cusCell.pattern)
                                    cellProperties = $.extend({}, cellProperties, {
                                        numericFormat: {
                                            pattern: cusCell.pattern
                                        }
                                    });
                                if (null != cusCell.culture)
                                    cellProperties = $.extend({}, cellProperties, {
                                        numericFormat: {
                                            pattern: cusCell.pattern,
                                            culture: cusCell.culture
                                        }
                                    });
                                if (null != cusCell.type) {
                                    cellProperties = $.extend({}, cellProperties, {
                                        type: cusCell.type
                                    });
                                    if ('numeric' == cusCell.type)
                                        Handsontable.renderers.NumericRenderer.apply(this, arguments);
                                }
                                return;
                            }
                        }
                    };
                }
            },
            renderData = function (data) {
                // 按照数据，渲染页码上每个数据展示块，按照类型和id来
                if (data) {
                    var cacmd = [];
                    var cellMouseDown = function (event, e, td) {
                        for (var i = 0; i < cacmd.length; i++) {
                            if (cacmd[i].row == e.row && cacmd[i].col == e.col) {
                                if (0 < $(td).find('.ascending').length) {
                                    $(td).find('.ascending').removeClass('ascending').addClass('descending');
                                } else if (0 < $(td).find('.descending').length) {
                                    $(td).find('.descending').removeClass('descending');
                                } else {
                                    $(td).find('.columnSorting').addClass('ascending');
                                }
                            }
                        }
                    };
                    for (var i = 0; i < data.length; i++) {
                        var id = data[i].id ? data[i].id.code : null;
                        var type = data[i].type;
                        if (id && type) {
                            var option = data[i].option;
                            if (option) {
                                // 这样做是因为option里这个json对象，可能有些数据是个js函数，需要适应执行，所以要更好的转成js对象
                                var _eval_set_data = evil(('string' == typeof (option) ? option : JSON.stringify(option)));
                                if ('Table' == type) {
                                    _eval_set_data = $.extend({}, _eval_set_data, { cells: hotCellsRenderer });
                                    console.log(_eval_set_data);
                                    if(widgets[id]) {
                                        widgets[id].updateSettings(_eval_set_data);
                                    } else {
                                        $('#' + id).append('<div table></div><div laypage class="pull-right"></div>');
                                        $('#' + id + ' [table]').handsontable(_eval_set_data);
                                        widgets[id] = $('#' + id + ' [table]').handsontable('getInstance');
                                        widgets[id].type = type;
                                    }
                                    if (_eval_set_data.cusAfterOnCellMouseDown) {
                                        cacmd = _eval_set_data.cusAfterOnCellMouseDown;
                                        widgets[id].addHook('afterOnCellMouseDown', cellMouseDown);
                                    }
                                    if ((data[i].count || 0 == data[i].count) && (data[i].page || 0 == data[i].page) && data[i].size)
                                        renderPageBar(data[i].count, data[i].page, data[i].size, id);
                                } else {
                                    var iEcharts;
                                    if (widgets[id]) {
                                        iEcharts = widgets[id];
                                    } else {
                                        var shtml = $($('#' + id)[0]).prop("outerHTML");
                                        iEcharts = echarts.init($('#' + id)[0], 'shine');
                                        widgets[id] = iEcharts;
                                        widgets[id].type = type;
                                        widgets[id].shtml = shtml;
                                    }
                                    _eval_set_data = $.extend({}, _eval_set_data, { animation: false });
                                    iEcharts.setOption(_eval_set_data);
                                    codes[id] = { count: data[i].size, curr: 1, limit: data[i].size };
                                }
                            }
                        }
                    }
                    $.each(widgets, function (id, value) {
                        if (value) {
                            var wt = value.type;
                            if ('Table' != wt) {
                                echartsPngs[id] = value.getDataURL({
                                    type: 'png',
                                    pixelRatio: 5,
                                    backgroundColor: '#fff'
                                });
                            }
                        }
                    });
                }
                tabOn();
            },
            tabOn = function () {
                $('div[lay-filter="' + code + '_auth"] div[class=layui-tab]').each(function () {
                    var its = $(this).find('.layui-show');
                    for (var i = 1; i < its.length; i++) {
                        $(its[i]).removeClass('layui-show');
                    }
                });
                $('div[lay-filter="' + code + '_auth"] div[class=layui-tab]').each(function () {
                    element.on('tab(' + $(this).attr('lay-filter') + ')', function (data) {
                        var id = $(data.elem.find('div[class=layui-tab-content]').find('.layui-tab-item')[data.index]).children('div:first').attr('id');
                        if (widgets[id]) {
                            var wt = widgets[id].type;
                            if ('Table' == wt) {
                                widgets[id].render();
                            } else {
                                var _eval_set_data = widgets[id].getOption();
                                var shtml = widgets[id].shtml;
                                var parent = $('#' + id).parent();
                                $('#' + id).remove();
                                parent.append(shtml);
                                var iEcharts = echarts.init($('#' + id)[0], 'shine');
                                widgets[id] = iEcharts;
                                widgets[id].type = wt;
                                widgets[id].shtml = shtml;
                                iEcharts.setOption(_eval_set_data);
                            }
                        }
                    });
                });
            },
            load = function () {
                loadParam();
                loadData(0, laypageConf.limit);
            };
        load();
        ht_auth.render(code + '_auth');
    };

    exports('report', function (code) {
        return new Report(code);
    });
});