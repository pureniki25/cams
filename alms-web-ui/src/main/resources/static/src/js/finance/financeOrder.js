/**
 * 财务跟单管理 js
 * @author zgh
 */
var layer, table, basePath, vm;

//从后台获取下拉框数据
var getSelectsData = function () {
    //取区域列表
    axios.get(basePath + 'finance/getOrderSetSearchInfo')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
                vm.users = res.data.data.users;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}


window.layinit(function (htConfig) {
    let _htConfig = htConfig;
    basePath = htConfig.financeBasePath;
    table = layui.table;
    let laydate = layui.laydate;
    getSelectsData();

    vm = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () {
        }
    })


    table.render({
        elem: "#main_table",
        id: 'main_table',
        height: 600, //容器高度
        url: basePath + 'finance/search',
        page: true,
        //done: (res, curr, count) => { }
        cols: [
            [{
                field: 'areaName',
                title: '区域'
            }, {
                field: 'companyName',
                title: '分公司',
            }, {
                field: 'businessTypeName',
                title: '业务类型'
            }, {
                field: 'userName',
                title: '出纳',
            }, {
                title: '操作',
                toolbar: "#bar_tools"
            }]
        ], //设置表头
    })

    table.on('tool(main_table)', function (obj) {
        let data = obj.data;
        let event = obj.event;
        if (event == 'del') {
            layer.confirm('确定要删除?', function (index) {
                vm.delete(data.id, data.userId);
                layer.close(index);
            });
        }
    })
})

let data = {
    loading: false,
    saving: false, //存储标志位
    exporting: false, //导出标志位
    submitLoading: false,
    showDialog: false,
    search: {
        userName: '',
        areaId: '',
        companyId: '',
        businessTypeId: ''
    },
    addFinancialOrderModal: {
        show: false
    },
    addFinancialOrderForm: {
        userId: '',
        areaLevel: '',
        areaId: '',
        areaList: []
    },
    areaList: [],
    companyList: [],
    businessTypeList: [],
    users: [],
    ruleValidate: {
        userId:[{required:true, message:'请选择跟单人名', trigger:'blur'}],
        areaId:[{required:true, message:'请选择区域', trigger:'blur'}],
        companyId:[{required:true, message:'请选择公司', trigger:'blur'}],
        businessTypeId:[{required:true, message:'请选择业务类型', trigger:'blur'}],
    }, //表单验证
    selectedRowInfo: '', //存储当前选中行信息
    edit_modal: false, //控制编辑项选择modal显示的标志位
    menu_modal: false, //

    edit: {
        id: '',
        areaId: '',
        companyId: '',
        businessTypeId: '',
        userId: '',
        userName: '',
    }
}

//查询表单验证
var setSearchFormValidate = {
    derateMoneyBegin: [
        {pattern: /^[0-9]+(.[0-9]{1,2})?$/, message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if (vm.searchForm.derateMoneyBegin != "" && vm.searchForm.derateMoneyEnd != "") {
                    if (parseInt(vm.searchForm.derateMoneyBegin) > parseInt(vm.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        vm.searchForm.derateMoneyEnd = vm.searchForm.derateMoneyEnd;
                        callback();//校验通过
                    }
                } else {
                    vm.searchForm.derateMoneyEnd = vm.searchForm.derateMoneyEnd;
                    callback();
                }

            }, trigger: 'blur'
        }
    ],
    derateMoneyEnd: [
        {pattern: /^[0-9]+(.[0-9]{1,2})?$/, message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if (vm.searchForm.derateMoneyBegin != "" && vm.searchForm.derateMoneyEnd != "") {
                    if (parseInt(vm.searchForm.derateMoneyBegin) > parseInt(vm.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        callback();//校验通过
                    }
                } else {
                    callback();
                }

            }, trigger: 'blur'
        }
    ]
};


let methods = {
    delete: function (id, userId) {
        axios.get(basePath + 'finance/delete', {params: {id: id, userId: userId}})
            .then(function (res) {
                if (res.data.code == '1') {
                    vm.tableReload()
                } else {
                    vm.$Modal.error({content: '删除失败!'})
                }
            })
            .catch(function (err) {
                vm.$Modal.error({content: '出现异常，请联系管理员!'})
            })
    },
    tableReload: function () {
        /*let p = {}
        Object.keys(vm.schForm).forEach(function(val, i){
            console.log(val)
            if(vm.schForm[val]!=''){
                p[val]=vm.schForm[val]
            }
        });*/
        table.reload('main_table', {
            where: vm.search,
            page: {curr: 1}
        })
    },
    listArea: function () {
        axios.get(basePath + 'sys/userarea/listArea', {params: {areaLevel: vm.addForm.areaLevel}})
            .then(function (res) {
                if (res.data.code == '1') {
                    vm.addForm.areaList = res.data.data
                } else {
                    vm.$Modal.error({content: '调用sys/userarea/del接口失败'})
                }
            })
            .catch(function (err) {
                vm.$Modal.error({content: '调用sys/userarea/del接口失败'})
            })
    },
    // openAddModal:function(){
    //     vm.$refs['addForm'].resetFields()
    //     vm.addForm.areaList = []
    //     vm.addForm.areaLevel = ''
    //     vm.addForm.userId = ''
    //     vm.addSysUserAreaModal.show = true
    // },
    setUserName: function (d) {
        this.edit.userName = d.label;
    },
    showEditDialog: function () {
        this.showDialog = true;
    },
    submitEditDialog: function () {
        this.$refs['edit'].validate((valid) => {
            if (valid) {
                axios.get(basePath + 'finance/edit', {params: this.edit})
                    .then(res => {
                        if (res.data.code == '1') {
                            this.tableReload();
                            this.showDialog = false;
                            this.$Modal.success({content:'操作成功!'});
                            this.$refs['edit'].resetFields();
                        } else {
                            this.$Modal.error({content: '操作失败: ' + res.data.msg});
                        }
                    }).catch(err => {
                    vm.$Modal.error({content: '出现异常，请联系管理员!'})
                });
                this.$Message.success('提交成功!');
            } else {
                this.$Message.error('表单验证失败!');
            }
        })



    }
}