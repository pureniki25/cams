layui.define(['form', 'table'], function (exports) {

    var $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        config = {
            width: 800,
            height: 640,
            name: '',
            mult: false,
            idcode: 'code.code'
        },
        content = '' +
        '<!-- 表头工具栏 START -->' +
        '<div class="row row-lg" id="x_resource_table_tools" style="width: ${width}px">' +
        '<div class="pull-right col-xs-4">' +
        '<div class="input-group input-group-sm">' +
        '<input type="text" class="form-control" id="x_resource_search_keyword" placeholder="模糊搜索资源" />' +
        '<div class="input-group-btn">' +
        '<button type="button" class="layui-btn layui-btn-sm" data-type="search">' +
        '<i class="fa fa-search"></i>' +
        '</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<!-- 表头工具栏 END -->' +
        '<!-- 数据表 START -->' +
        '<div class="row-lg" style="width: ${width}px">' +
        '<table id="x_resource_datatable" lay-filter="filter_x_resource_datatable">' +
        '</table>' +
        '<!-- 表记录数据模板群 START -->' +
        '<script type="text/html" id="x_resource_code_laytpl">' +
        '{{ d.code.code }}' +
        '</script>' +
        '<!-- 表记录数据模板群 END -->' +
        '</div>' +
        '<!-- 数据表 END -->',
        tableObj,
        tableParams = {
            id: 'x_resource_datatable',
            elem: '#x_resource_datatable',
            contentType: 'application/json; charset=utf-8',
            page: true
        };

    var resource = {
        open: function (_config) {
            var cf = $.extend({}, config, _config),
                cols = [
                    []
                ];
            if (!cf.url) {
                layer.alert('资源框打开失败，缺少资源加载地址配置信息。');
                return;
            }
            tableParams.url = cf.url;
            if (cf.mult) {
                cols[0].push({
                    type: 'checkbox'
                });
            } else {
                cols[0].push({
                    type: 'radio'
                });
            }
            if (cf.cols && 0 < cf.cols.length) {
                for (var i = 0; i < cf.cols.length; i++) {
                    if ('code' == cf.cols[i].field) {
                        cols[0].push({
                            field: cf.cols[i].field,
                            title: cf.cols[i].title,
                            templet: '#code_laytpl'
                        });
                    } else {
                        cols[0].push({
                            field: cf.cols[i].field,
                            title: cf.cols[i].title
                        });
                    }
                }
            } else {
                cols[0].push({
                    field: 'code',
                    title: '编码',
                    templet: '#code_laytpl'
                });
                cols[0].push({
                    field: 'name',
                    title: cf.name
                });
            }
            tableParams.cols = cols;
            content = content.replace(/\$\{width\}/g, (cf.width - 5));
            return layer.open({
                type: 1,
                area: [cf.width + 'px', cf.height + 'px'],
                shadeClose: true,
                title: cf.name + '资源选择',
                content: content,
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    // 获取选中的数据调用回调接口
                    if (cf.callback && typeof cf.callback == 'function') {
                        cf.callback(table.cache[tableParams.id]);
                    }
                    layer.closeAll();
                },
                btn2: function () {
                    layer.closeAll();
                },
                end: function () {
                    //清理操作
                },
                success: function (layero) {
                    tableParams.done = function (params) {
                        if (cf.values && 0 < cf.values.length) {
                            var i = 0;
                            if (cf.mult) {
                                var values = cf.values;
                                for (i = 0; i < table.cache[tableParams.id].length; i++) {
                                    for (var j = 0; j < values.length; j++) {
                                        if (values[j] == tableObj.that.resData(table.cache[tableParams.id][i], cf.idcode)) {
                                            table.cache[tableParams.id][i].LAY_CHECKED = true;
                                        }
                                    }
                                }
                                $(tableParams.elem, layero).next().find('tr').each(function (index, item) {
                                    var i = index - 1;
                                    var checkbox = $(item).children('td:first').find('input[type=checkbox]');
                                    if (checkbox && 0 < checkbox.length) {
                                        if (i < table.cache[tableParams.id].length && table.cache[tableParams.id][i].LAY_CHECKED) {
                                            checkbox.attr('checked', true);
                                        }
                                    }
                                });
                                tableObj.that.renderForm('checkbox');
                                tableObj.that.syncCheckAll();
                            } else {
                                var value = cf.values[0];
                                for (i = 0; i < table.cache[tableParams.id].length; i++) {
                                    if (value == tableObj.that.resData(table.cache[tableParams.id][i], cf.idcode)) {
                                        table.cache[tableParams.id][i].LAY_CHECKED = true;
                                    }
                                }
                                $(tableParams.elem, layero).next().find('tr').each(function (index, item) {
                                    var i = index - 1;
                                    var radio = $(item).children('td:first').find('input[type=radio]');
                                    if (radio && 0 < radio.length) {
                                        if (i < table.cache[tableParams.id].length && table.cache[tableParams.id][i].LAY_CHECKED) {
                                            radio.attr('checked', true);
                                        }
                                    }
                                });
                                tableObj.that.renderForm('radio');
                            }
                        }
                    };
                    tableObj = table.render($.extend({}, tableParams, {
                        elem: $(tableParams.elem, layero)
                    }));
                    $('#x_resource_table_tools .layui-btn').on('click', function () {
                        var type = $(this).data('type');
                        if ('search' == type) {
                            var keyWord = $('#x_resource_search_keyword', layero).val();
                            var data = {
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            };
                            var where = {};
                            if (keyWord || '' == keyWord) {
                                where.keyWord = keyWord;
                            }
                            if (keyWord || '' == keyWord) {
                                table.clearWhere('x_resource_datatable');
                                data.where = where;
                            }
                            table.reload('x_resource_datatable', data);
                        }
                    });
                }
            });
        }
    };

    exports('resource', resource);
});