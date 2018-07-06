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

        data() {
            const validateChannelNumber = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('不能为空'));
                } else if (!Number.isInteger(value)) {
                    callback(new Error('必须为数字'));
                }

            };
            return {
                disabledFlag: true,
                withHoldChanelModel: false,
                platformType: [],
                loading: false,
                searchForm: {
                    platformId: '', //渠道类型

                },
                withHoldChanelForm: {
                    platformId: '', //渠道类型
                    state: 1,
                    failTimes: '',
                    remark: '',
                    channelLevel: '',
                    channelId: '',
                    subPlatformId:'',
                    subPlatformName:''
                },

                withHoldChanelValidate: {
                    platformId: [
                        {required: true, type: 'number', message: '渠道不能为空且必须为数字', trigger: 'blur'},
                    ],
                    failTimes: [
                        {required: true, type: 'number', message: '每日失败次数不为空且必须为数字', trigger: 'blur'},
                    ],
                    channelLevel: [
                        {required: true, type: 'number', message: '代扣优先级不为空且必须为数字', trigger: 'blur'},
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
                                title: '代扣优先级'
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
                            console.log("选择的行数据：", selectedRowInfo.channelId);

                            self.editWithholdChannel(selectedRowInfo.channelId);
                        }
                    });


                })
            },

            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },
            editWithholdChannel(id) {
                vm.withHoldChanelModel = true;
                axios.get(basePath + 'withholdManage/getWithholdChannel?channelId=' + id)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            var data = res.data.data;
                            vm.withHoldChanelForm.platformId = data.platformId;
                            vm.withHoldChanelForm.state = data.channelStatus;
                            vm.withHoldChanelForm.failTimes = data.failTimes;
                            vm.withHoldChanelForm.remark = data.remark;
                            vm.withHoldChanelForm.channelId = data.channelId;
                            vm.withHoldChanelForm.channelLevel = data.level;
                            vm.withHoldChanelForm.subPlatformId = data.subPlatformId;
                            vm.withHoldChanelForm.subPlatformName = data.subPlatformName;
                        }
                    })
                    .catch(function (error) {
                        vm.$Modal.error({content: '接口调用异常!'});
                    });
            },
            platformOnChange(e){

                if(e ==5){
                    this.disabledFlag = false;
                }else {
                    this.disabledFlag = true;
                }

            },
            addHoldChannel(name) {
                // console.log("addHoldChannel",addHoldChannel);
                this.$refs[name].resetFields();
                vm.withHoldChanelModel = true;
            },
            submitHoldChanel(name) {
                var platformId = vm.withHoldChanelForm.platformId;
                var channelStatus = vm.withHoldChanelForm.state;
                var failTimes = vm.withHoldChanelForm.failTimes;
                var remark = vm.withHoldChanelForm.remark;
                var channelId = vm.withHoldChanelForm.channelId;
                var level = vm.withHoldChanelForm.channelLevel;
                var subPlatformId = vm.withHoldChanelForm.subPlatformId;
                var subPlatformName = vm.withHoldChanelForm.subPlatformName;

                console.log("platformId", typeof platformId);
                console.log("channelStatus", typeof channelStatus);
                console.log("failTimes", typeof failTimes);
                console.log("channelLevel", typeof channelLevel);
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        //提交
                        axios.get(basePath + 'withholdManage/addOrEditWithholdChannel',
                            {
                                params: {
                                    platformId: platformId,
                                    channelStatus: channelStatus,
                                    failTimes: failTimes,
                                    remark: remark,
                                    channelId: channelId,
                                    level: level,
                                    subPlatformId: subPlatformId,
                                    subPlatformName: subPlatformName
                                }
                            })
                            .then(function (res) {
                                if (res.data.code == "1") {
                                    console.log(res)
                                    vm.$Modal.success({
                                        title: '',
                                        content: '操作成功'
                                    });
                                    vm.withHoldChanelModel = false;
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
            cancelHoldChanel(name) {
                this.withHoldChanelModel = false;
                this.$refs[name].resetFields();
                vm.toLoading();
            }


        },
        mounted: function () {
            this.init();
        }
    });
});