/**
 * 还款账号管理 js
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;



window.layinit(function (htConfig) {
    basePath = htConfig.financeBasePath;
    coreBasePath = htConfig.coreBasePath;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () {
            this.initData();
            this.search();
        },
        updated: function () {
            this.loading = false;
        }
    })


    /*table.render({
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
                title: '跟单员',
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
    })*/
})

let data = {
    loading: false,
    saving: false, //存储标志位
    exporting: false, //导出标志位
    submitLoading: false,
    editModal: false,
    companyList: [],
    searchForm: {
        condition: {
            EQ_dept_id: '',
            EQ_repayment_name: '',
        },
        current: 1,
        size: 10
    },
    editForm: {
        id: '',
        areaId: '',
        companyId: '',
        businessTypeId: '',
        userId: '',
        userName: '',
    },
    ruleValidate: {
        userId:[{required:true, message:'请选择跟单人名', trigger:'blur'}],
        areaId:[{required:true, message:'请选择区域', trigger:'blur'}],
        companyId:[{required:true, message:'请选择公司', trigger:'blur'}],
        businessTypeId:[{required:true, message:'请选择业务类型', trigger:'blur'}],
    }, //表单验证
    table:{
        col:[{
            type:'selection',
            width:60,
            align:'center'
        },{
            title:'财务导入流水账号',
            key:'financeName'
        }, {
            title:'账户名',
            key:'repaymentName'
        },{
            title:'账号',
            key:'repaymentId'
        },{
            title:'开户行',
            key:'repaymentBank'
        },{
            title:'支行名',
            key:'repaymentSubBank'
        },{
            title:'更新时间',
            key:'updateTime'
        },{
            title: '操作',
            key: 'action',
            fixed: 'right',
            align:'center',
            render: (h, params) => {
                return h('div', [
                    h('Button', {
                        props: {
                            type: '',
                            size: 'small'
                        },
                        style: {
                            marginRight: '5px'
                        },
                    }, '编辑'),
                    h('Button', {
                        props: {
                            type: '',
                            size: 'small'
                        }
                    }, '详情')
                ]);
            }
        }],
        data:[],
        loading:false,
        total: 0
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
    showEditModal: function () {
        this.editModal = true;
    },
    //初始化数据
    initData : function () {
        var self = this;
        //省
        axios.get(coreBasePath +'sysProvince/findAll')
            .then(function (res) {
                if(!!res.data && res.data.code=='1'){
                    self.provinces = res.data.data;
                }else{
                    self.$Modal.error({content:'调用接口异常,消息:' + res.data.msg})
                }
            })
            .catch(function (err) {
                self.$Modal.error({content:'操作失败!'})
            });
        //
        //分司列表
        axios.get(coreBasePath + 'basicCompany/findAllBranchCompany')
            .then(function (res) {
                if (res.data.code == "1") {
                    self.compines = res.data.data;
                } else {
                    self.$Modal.error({content:'调用接口异常,消息:' + res.data.msg})
                }
            })
            .catch(function (error) {
                self.$Modal.error({content:'操作失败!'})
            });
    },
    delete: function (id, userId) {
        var self = this;
        axios.get(basePath + 'finance/delete', {params: {id: id, userId: userId}})
            .then(function (res) {
                if (res.data.code == '1') {
                    self.tableReload()
                } else {
                    self.$Modal.error({content: '删除失败!'})
                }
            })
            .catch(function (err) {
                self.$Modal.error({content: '出现异常，请联系管理员!'})
            })
    },
    paging: function(current){
      this.searchForm.current = current;
      this.search();
    },
    search: function () {
        /*let p = {}
        Object.keys(vm.schForm).forEach(function(val, i){
            console.log(val)
            if(vm.schForm[val]!=''){
                p[val]=vm.schForm[val]
            }
        });*/

        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
        var self = this;
        //self.loading =  true;
        self.table.loading = true;
        axios.post(basePath + 'departmentBank/search', this.searchForm ).then(
           function (res) {
               self.table.loading = false;
               if(!!res.data && res.data.code==0){
                   self.table.data = res.data.data;
                   self.table.total = res.data.count;
               }else{
                   self.$Message.error({content: res.data.msg})
               }
           }
        ).catch(
            function (err) {
                self.table.loading = false;
                self.$Message.error({content: err})
            }
        );
       // self.loading =  false;
    },
    submitEdit: function () {
        var self = this;
        // this.$refs['edit'].validate((valid) => {
        //     if (valid) {
        //         axios.get(basePath + 'finance/edit', {params: this.edit})
        //             .then(res => {
        //                 if (res.data.code == '1') {
        //                     this.tableReload();
        //                     this.showDialog = false;
        //                     this.$Modal.success({content:'操作成功!'});
        //                     this.$refs['edit'].resetFields();
        //                 } else {
        //                     this.$Modal.error({content: '操作失败: ' + res.data.msg});
        //                 }
        //             }).catch(err => {
        //             vm.$Modal.error({content: '出现异常，请联系管理员!'})
        //         });
        //         this.$Message.success('提交成功!');
        //     } else {
        //         this.showDialog = true;
        //     }
        // })


        axios.get(basePath + 'finance/edit', {params: this.edit})
            .then(res => {
                if (res.data.code == '1') {
                    this.tableReload();
                    this.showDialog = false;
                    this.$Modal.success({content:'操作成功!'});
                    self.$refs['editForm'].resetFields();
                } else {
                    this.$Modal.error({content: '操作失败: ' + res.data.msg});
                }
            }).catch(err => {
            vm.$Modal.error({content: '出现异常，请联系管理员!'})
        });
        // this.$Message.success('提交成功!');
    }
}