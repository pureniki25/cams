var layer;
var table;
var financePath;
var basePath;
var vm;
var cookie
var tableColum;
//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath + 'withholdManage/getWithholdChannelType')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.platformType = res.data.data.platformType;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    financePath = _htConfig.financeBasePath;
    basePath = _htConfig.coreBasePath;
    uiBasePath = _htConfig.uiBasePath;
    getSelectsData();
    vm = new Vue({
        el: '#app',

        data: {
            platformType:[],
            loading: false,
            searchForm: {
                platformId: '', //渠道类型

            },

        },

        methods: {
            //重载表格
            toLoading: function () {
                if (table == null) {
                    return;
                }
                var self = this;
                self.loading = true;
                table.reload('listTable', {
                    where: {
                        platformId: vm.searchForm.platformId, //登记开始时间
                    }
                    , page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            },
            ////  ----   单行操作界面显示  结束 -----------------
            init: function () {
                var self = this;
                //使用layerUI的表格组件
                layui.use(['layer', 'table', 'ht_ajax', 'ht_auth', 'ht_cookie', 'ht_config'], function () {
                    layer = layui.layer;
                    cookie = layui.ht_cookie;
                    table = layui.table;
                    //执行渲染
                    table.render({
                        elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                        , id: 'listTable'
                        , cols: [[

                            {
                                field: 'channelLevel',
                                title: '渠道优先级'
                            },
                            {
                                field: 'channelId',
                                title: '编号'
                            },
                            {
                                field: 'platformName',
                                title: '渠道名称',
                            },
                            {
                                field: 'channelStatus',
                                title: '渠道状态',
                                templet: function (d) {
                                    var content = "";
                                    if (d.channelStatus == 0) {
                                        content = '停用'
                                    } else if (d.channelStatus == 1) {
                                        content = '启用'
                                    }
                                    return content
                                }
                            },
                            {
                                field: 'failTimes',
                                title: '每日失败最大次数',
                            },
                            {
                                fixed: 'right',
                                title: '操作',
                                width: 178,
                                align: 'left',
                                toolbar: '#barTools'
                            }
                        ]], //设置表头
                        url: basePath + 'withholdManage/getWithholdChannelPageList',
                        page: true,
                        done: function (res, curr, count) {
                            //数据渲染完的回调。你可以借此做一些其它的操作
                            //如果是异步请求数据方式，res即为你接口返回的信息。
                            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                            self.loading = false;
                        }
                    });
                    table.on('tool(listTable)', function (obj) {
                        var selectedRowInfo = obj.data;

                        console.log("obj.event", obj.event);
                        if (obj.event === 'edit') {
                            console.log("选择的行数据：", selectedRowInfo);

                            // self.editWithholdChannel(selectedRowInfo.id);
                        }
                    });


                })
            },

            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },




        },
        mounted: function () {
            this.init();
        }
    });
});