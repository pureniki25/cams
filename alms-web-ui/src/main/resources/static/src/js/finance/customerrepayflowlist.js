var layer;
var table;
var financePath;
var basePath;
var vm;
var tableColum;
//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(financePath + 'customer/getCustomerRepayFlowSelectsData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.companyList = res.data.data.company;
                vm.bankList = res.data.data.bank;
                console.log("vm.companyList=", vm.companyList);
                console.log("vm.bankList=", vm.bankList);
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
            editSalary: false,
            loading: false,
            exporting: false,//导出标志位
            dictParamField: false,
            companyList: [],
            bankList: [],
            searchForm: {
                regStartTime: '', //登记开始时间
                regEndTime: '',      //登记结束时间
                businessId: '',  //业务编号
                companyId: '',	//所属分公司
                state: '',    //状态
                accountStartTime: '',//转账开始时间
                accountEndTime: '',//转账结束时间
                customerName: '',//客户姓名
                bankAccount: '',//转入账号
                accountMoney: '',//转账金额
            },
            upload: {
                url: financePath + 'customer/importCustomerFlowExcel',
                headers: {
                    app: axios.defaults.headers.common['app'],
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
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
                        regStartTime: vm.searchForm.regStartTime, //登记开始时间
                        regEndTime: vm.searchForm.regEndTime,      //登记结束时间
                        businessId: vm.searchForm.businessId,  //业务编号
                        companyId: vm.searchForm.companyId,	//所属分公司
                        state: vm.searchForm.state,    //状态
                        accountStartTime: vm.searchForm.accountStartTime,//转账开始时间
                        accountEndTime: vm.searchForm.accountEndTime,//转账结束时间
                        customerName: vm.searchForm.customerName,//客户姓名
                        bankAccount: vm.searchForm.bankAccount,//转入账号
                        accountMoney: vm.searchForm.accountMoney,//转账金额
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
                layui.use(['layer', 'table', 'ht_ajax', 'ht_auth', 'ht_config'], function () {
                    layer = layui.layer;
                    table = layui.table;
                    //执行渲染
                    table.render({
                        elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                        , id: 'listTable'
                        , cols: [[
                            {
                                type: 'checkbox',
                                fixed: 'left'
                            },
                            {
                                field: 'createTime',
                                title: '登记日期'
                            },
                            {
                                field: 'moneyPoolId',
                                title: '流水编号'
                            },
                            {
                                field: 'originalBusinessId',
                                title: '业务编号',
                            },
                            {
                                field: 'operateName',
                                title: '客户名称',
                            },
                            {
                                field: 'afterId',
                                title: '期数',
                            },
                            {
                                field: 'companyName',
                                title: '所属分公司',
                            },
                            {
                                field: 'accountMoney',
                                title: '转账金额',
                            },
                            {
                                field: 'totalBorrowAmount',
                                title: '本期应还金额',
                            },
                            {
                                field: 'factTransferName',
                                title: '实际转账人',
                            },
                            {
                                field: 'tradeDate',
                                title: '转账日期',
                            },
                            {
                                field: 'tradeType',
                                title: '交易类型',
                            },
                            {
                                field: 'bankAccount',
                                title: '转入账号',
                            },
                            {
                                field: 'state',
                                title: '状态',
                                templet: function (d) {
                                    var content = "";
                                    if (d.state == "未关联银行流水") {
                                        content = '未审核'
                                    } else if (d.state == "财务确认已还款") {
                                        content = '已审核'
                                    } else if (d.state == "还款登记被财务拒绝") {
                                        content = '拒绝'
                                    } else {
                                        content = d.state

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
                        url: financePath + 'customer/getCustomerRepayFlowList',
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
                        if (obj.event === 'edit') {
                            console.log("选择的行数据：", selectedRowInfo);
                            self.editRecruitInfo(selectedRowInfo.id);
                        } else if (obj.event === 'view') {
                            console.log("选择的行数据：", selectedRowInfo);
                            self.viewRecruitInfo(selectedRowInfo.id);
                        }

                    });
                })
            },
            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },
            ////  ----   单行操作界面显示  结束 -----------------
            clickExport() {//导出Excel表格

                layui.use(['layer', 'table', 'ht_config'], function () {
                    // vm.$refs['searchForm'].validate((valid) => {
                    //     if (valid) {
                    // getData();
                    // vm.exporting = true;
                    var excelName=encodeURI("客户还款登记流水");
                    expoertExcel(financePath + "customer/downloadCustomerFlowExcel", vm.searchForm, excelName+".xls");

                    // vm.exporting = false;

                    // }
                    // })
                });
            },
            beforeUpLoadFile() {//导入Excel表格


            },

            uploadSuccess() {

            },
            // 编辑字段
            editRecruitInfo: function (id) {
                var self = this;
                var eidturl = '/recruit/recruitEdit?id=' + id;
                layer.open({
                    type: 2,
                    title: "编辑招聘",
                    maxmin: true,
                    area: ['80%', '80%'],
                    content: eidturl,
                    end: function () {
                        self.toLoading();
                    }
                });


            },
            // 预览
            viewRecruitInfo: function (id) {
                var self = this;
                var viewurl = '/recruit/recruitView?id=' + id;
                layer.open({
                    type: 2,
                    title: "预览",
                    maxmin: true,
                    area: ['80%', '80%'],
                    content: viewurl,
                    end: function () {

                    }
                });


            },

            addRecruit: function () {
                var self = this;
                var url = '/recruit/recruitEdit';
                layer.open({
                    type: 2,
                    title: "新增招聘",
                    maxmin: true,
                    area: ['80%', '80%'],
                    content: url,
                    end: function () {
                        self.toLoading();
                    }
                });

            },

            regStartTime: function (starttime) {
                this.searchForm.regStartTime = starttime;
            },
            regEndTime: function (endtime) {
                this.searchForm.regEndTime = endtime;
            },
            accountStartTime: function (starttime) {
                this.searchForm.accountStartTime = starttime;
            },
            accountEndTime: function (endtime) {
                this.searchForm.accountEndTime = endtime;
            }


        },
        mounted: function () {
            this.init();
        }
    });
});