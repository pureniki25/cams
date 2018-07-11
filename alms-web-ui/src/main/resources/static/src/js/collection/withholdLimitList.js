var layer;
var table;
var financePath;
var basePath;
var vm;
var cookie
//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath + 'withholdManage/getWithholdLimitType')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.platformType = res.data.data.platformType;
                vm.bankType = res.data.data.bankType;
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

        data() {
            return {
                withHoldLimitModel: false,
                subTitle:'',
                platformType: [],
                bankType: [],
                loading: false,
                searchForm: {
                    platformId: '', //渠道类型
                    bankCode: '', //银行类型

                },
                withHoldLimitForm: {
                    platformId: '', //渠道类型
                    bankCode:'',
                    dayLimit:'',
                    onceLimit:'',
                    monthLimit:'',
                    status: "1",
                    limitId:'',

                },

                withHoldLimitValidate: {
                    platformId: [
                        {required: true, type: 'number', message: '渠道不能为空且必须为数字', trigger: 'blur'},
                    ],
                    bankCode: [
                        {required: true, type: 'string', message: '银行名称不为空且必须为数字', trigger: 'blur'},
                    ],
                    dayLimit: [
                        {required: true, type: 'number', message: '单日限额不为空且必须为数字', trigger: 'blur'},
                    ],
                    onceLimit: [
                        {required: true, type: 'number', message: '单笔限额不为空且必须为数字', trigger: 'blur'},
                    ],
                    monthLimit: [
                        {required: true, type: 'number', message: '单月限额不为空且必须为数字', trigger: 'blur'},
                    ],
                    status: [
                        {required: true, type: 'string', message: '状态不能为空', trigger: 'blur'},
                    ],
                }
            }


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
                        bankCode: vm.searchForm.bankCode, //登记开始时间
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
                                field: 'platformName',
                                title: '渠道名称'
                            },
                            {
                                field: 'bankName',
                                title: '银行名称'
                            },
                            {
                                field: 'onceLimit',
                                title: '单笔限额',
                            },
                            {
                                field: 'dayLimit',
                                title: '单日限额',
                            },
                            {
                                field: 'monthLimit',
                                title: '单月限额',
                            },

                            {
                                field: 'status',
                                title: '启用状态',
                                templet: function (d) {
                                    var content = "";
                                    if (d.status == 0) {
                                        content = '停用'
                                    } else if (d.status == 1) {
                                        content = '启用'
                                    }
                                    return content
                                }
                            },
                            {
                                fixed: 'right',
                                title: '操作',
                                width: 178,
                                align: 'left',
                                toolbar: '#barTools'
                            }
                        ]], //设置表头
                        url: basePath + 'withholdManage/getWithholdLimitPageList',
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
                            console.log("选择的行数据：", selectedRowInfo.limitId);

                            self.editWithholdLimit(selectedRowInfo.limitId);
                        }
                        if (obj.event === 'shutdown') {
                            console.log("选择的行数据：", selectedRowInfo.limitId);
                            layer.confirm('确认停用该额度吗？', {icon: 3, title: '提示'}, function (index) {
                                self.shutdownLimit(selectedRowInfo.limitId,0);
                                layer.close(index);
                            })

                        }else if(obj.event === 'start'){
                            console.log("选择的行数据：", selectedRowInfo.limitId);
                            layer.confirm('确认启用该额度吗？', {icon: 3, title: '提示'}, function (index) {
                                self.shutdownLimit(selectedRowInfo.limitId,1);
                                layer.close(index);
                            })
                        }

                    });


                })
            },
            shutdownLimit(id,status){
                axios.get(basePath + 'withholdManage/updateWithholdLimitStatus?limitId=' + id+"&status="+status)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            vm.$Modal.success({content: '停用成功!'});
                            vm.toLoading();
                        }
                    })
                    .catch(function (error) {
                        vm.$Modal.error({content: '接口调用异常!'});
                    });
            },
            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },
            editWithholdLimit(id) {
                vm.subTitle="编辑额度";
                vm.withHoldLimitModel = true;
                axios.get(basePath + 'withholdManage/getWithholdLimit?limitId=' + id)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            var data = res.data.data;
                            vm.withHoldLimitForm.platformId = data.platformId;
                            vm.withHoldLimitForm.status = data.status.toString();
                            vm.withHoldLimitForm.bankCode = data.bankCode;
                            vm.withHoldLimitForm.dayLimit = data.dayLimit;
                            vm.withHoldLimitForm.onceLimit = data.onceLimit;
                            vm.withHoldLimitForm.monthLimit = data.monthLimit;
                            vm.withHoldLimitForm.limitId = data.limitId;
                        }
                    })
                    .catch(function (error) {
                        vm.$Modal.error({content: '接口调用异常!'});
                    });
            },
            addHoldLimit(name) {
                // console.log("addHoldChannel",addHoldChannel);
                this.$refs[name].resetFields();
                vm.subTitle="新增额度";
                vm.withHoldLimitModel = true;
            },
            submitHoldLimit(name) {


                var platformId =  vm.withHoldLimitForm.platformId;
                var status =   vm.withHoldLimitForm.status ;
                var bankCode =   vm.withHoldLimitForm.bankCode ;
                var dayLimit =   vm.withHoldLimitForm.dayLimit ;
                var onceLimit =   vm.withHoldLimitForm.onceLimit ;
                var monthLimit =  vm.withHoldLimitForm.monthLimit ;
                var limitId =  vm.withHoldLimitForm.limitId ;

                console.log("platformId", typeof platformId);
                console.log("status", typeof status);
                console.log("bankCode", typeof bankCode);
                console.log("dayLimit", typeof dayLimit);
                console.log("onceLimit", typeof onceLimit);
                console.log("monthLimit", typeof monthLimit);
                console.log("limitId", typeof limitId);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        //提交
                        axios.get(basePath + 'withholdManage/addOrEditWithholdLimit',
                            {
                                params: {
                                    platformId: platformId,
                                    status: status,
                                    bankCode: bankCode,
                                    dayLimit: dayLimit,
                                    onceLimit: onceLimit,
                                    monthLimit: monthLimit,
                                    limitId: limitId
                                }
                            })
                            .then(function (res) {
                                if (res.data.code == "1") {
                                    console.log(res)
                                    vm.$Modal.success({
                                        title: '',
                                        content: '操作成功'
                                    });
                                    vm.withHoldLimitModel = false;
                                    vm.toLoading();
                                } else {
                                    vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                                }
                            })
                            .catch(function (error) {
                                vm.$Modal.error({content: '接口调用异常!'});
                            });

                    } else {


                    }
                })
            },
            cancelHoldLimit(name) {
                this.withHoldLimitModel = false;
                this.$refs[name].resetFields();
                vm.toLoading();
            }


        },
        mounted: function () {
            this.init();
        }
    });
});