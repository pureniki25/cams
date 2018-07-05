var layer;
var table;
var financePath;
var basePath;
var vm;
var cookie


window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    financePath = _htConfig.financeBasePath;
    basePath = _htConfig.coreBasePath;
    uiBasePath = _htConfig.uiBasePath;
    vm = new Vue({
        el: '#app',
        data: {
            loading: false,
            dictParamField: false,
            searchForm: {
                startTime: '', //完成日期开始时间
                endTime: '', //完成日期开始时间

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
                        startTime: vm.searchForm.startTime, //登记开始时间
                        endTime: vm.searchForm.endTime,      //登记结束时间

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
                                field: 'merchOrderId',
                                title: '宝付订单号'
                            },
                            {
                                field: 'thirdOrderId',
                                title: '商户支付订单号'
                            },
                            {
                                field: 'merchantAccount',
                                title: '商户号',
                            },
                            {
                                field: 'bankCode',
                                title: '终端号',
                            },
                            {
                                field: 'repayStatus',
                                title: '交易类型',
                                templet: function (d) {
                                    var content = "";
                                    if (d.repayStatus == 0) {
                                        content = '代扣失败'
                                    } else if (d.repayStatus == 1) {
                                        content = '代扣成功'
                                    } else if (d.repayStatus == 2) {
                                        content = '处理中'
                                    }
                                    return content
                                }
                            },
                            {
                                field: 'currentAmount',
                                title: '金额(元)',
                            },
                            {
                                field: 'createTime',
                                title: '清算日期',

                            }
                        ]], //设置表头
                        url: financePath + 'customer/getBfWithholdFlowPageList',
                        page: true,
                        done: function (res, curr, count) {
                            //数据渲染完的回调。你可以借此做一些其它的操作
                            //如果是异步请求数据方式，res即为你接口返回的信息。
                            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                            self.loading = false;

                        }
                    });


                })
            },

            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                this.searchForm.startTime = '';
                this.searchForm.endTime = '';
                this.searchForm.startTimeV = '';
                this.searchForm.endTimeV = '';


                vm.toLoading();
            },


            startTime: function (starttime) {
                this.searchForm.startTime = starttime;
            },
            endTime: function (endtime) {
                this.searchForm.endTime = endtime;
            },


        },
        mounted: function () {
            this.init();
        }
    });
});