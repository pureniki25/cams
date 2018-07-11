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
    editModalLoading: true,
    detailModal: false,
    companies: [],
    provinces: [],
    cities: [],
    banks: [],
    searchForm: {
        condition: {
            LIKE_dept_id: '',
            LIKE_repayment_name: '',
        },
        current: 1,
        size: 10
    },
    editForm: {
        accountId: '',
        financeName: '',
        repaymentName: '',
        repaymentId: '',
        repaymentBank: '',
        repaymentSubBank: '',
        mainType: '2',
        mainId: '',
        bankProvince: '',
        bankCity: '',
        phoneNumber: '',
        deptId: '',
        deptIds: [],
    },
    ruleValidate: {
        //表单验证
        //financeName: [{required: true, message: '必填项', trigger: 'blur'}],
        repaymentName: [{required: true, message: '请输入账户名', trigger: 'blur'}],
        repaymentId: [{required: true, message: '请输入账号', trigger: 'blur'}],
        repaymentBank: [{required: true, message: '请选择开户行', trigger: 'blur'}],
        //repaymentSubBank: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
        //mainType: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
        //mainId: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
        //bankProvinceCity: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
        //phoneNumber: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
        // deptIds: [{required: true, message: '请至少关联一个分公司', trigger: 'blur'}],
        deptIds:[{
            validator:function(rule, value, callback, source, options){
                if(vm.editForm.deptIds.length == 0 ){
                    callback(new Error('请至少关联一个分公司'));
                }else{
                    callback();
                }
            },
            trigger:'blur'
        }]
    },
    table: {
        col: [{
            type: 'selection',
            width: 60,
            align: 'center'
        }, {
            title: '财务导入流水账号',
            key: 'financeName'
        }, {
            title: '账户名',
            key: 'repaymentName'
        }, {
            title: '账号',
            key: 'repaymentId'
        }, {
            title: '开户行',
            key: 'repaymentBank'
        }, {
            title: '支行名',
            key: 'repaymentSubBank'
        }, {
            title: '更新时间',
            key: 'updateTime'
        }, {
            title: '操作',
            key: 'action',
            fixed: 'right',
            align: 'center',
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
                        on: {
                            click: () => {
                                vm.edit(params.row);
                            }
                        }
                    }, '编辑'),
                    h('Button', {
                        props: {
                            type: '',
                            size: 'small'
                        },
                        on: {
                            click: () => {
                                vm.detail(params.row)
                            }
                        }
                    }, '详情')
                ]);
            }
        }],
        data: [],
        loading: false,
        total: 0
    }
}

let methods = {
    showEditModal() {
        this.editModal = true;
    },
    hideEditModal() {
        this.editModal = false;
        this.$refs['editForm'].resetFields();
    },
    showDetailModal() {
        this.detailModal = true;
    },
    //初始化数据
    initData() {
        var self = this;
        //省
        axios.get(coreBasePath + 'sysProvince/findAll')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.provinces = res.data.data;
                } else {
                    self.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            });

        //银行
        axios.get(coreBasePath + 'sysBank/findAll')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.banks = res.data.data;
                } else {
                    self.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            });

        //分公司列表
        axios.get(coreBasePath + 'basicCompany/findAllBranchCompany')
            .then(res => {
                if (res.data.code == "1") {
                    self.companies = res.data.data;
                } else {
                    self.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            });
    },
    initCityData() {
        var self = this;
        //根据所选择的省查询市列表
        axios.get(coreBasePath + 'sysCity/findAllByProvinceName?provinceName=' + this.editForm.bankProvince)
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.cities = res.data.data;
                } else {
                    self.$Modal.error({content: '调用接口失败，消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            })
    },
    delete(id, userId) {
        var self = this;
        axios.get(basePath + 'finance/delete', {params: {id: id, userId: userId}})
            .then(res => {
                if (res.data.code == '1') {
                    self.tableReload()
                } else {
                    self.$Modal.error({content: '删除失败!'})
                }
            })
            .catch(err => {
                self.$Modal.error({content: '出现异常，请联系管理员!'})
            })
    },
    paging(current) {
        this.searchForm.current = current;
        this.search();
    },
    search() {
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'departmentBank/search', this.searchForm).then(res => {
                self.table.loading = false;
                if (!!res.data && res.data.code == 0) {
                    self.table.data = res.data.data;
                    self.table.total = res.data.count;
                } else {
                    self.$Message.error({content: res.data.msg})
                }
            }
        ).catch(err => {
                self.table.loading = false;
                self.$Message.error({content: err})
            }
        );
        // self.loading =  false;
    },
    handleSubmit (name) {
        setTimeout(() => {
            this.editModal = false;
        }, 2000);

        this.$refs[name].validate((valid) => {
            if (valid) {
                this.$Message.success('Success!');
            } else {
                this.$Message.error('Fail!');
            }
        })
    },
    submitEditForm() {
        var self = this;
        this.$refs['editForm'].validate( valid => {
            if(valid){
                axios.post(basePath + 'departmentBank/edit', self.editForm)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {
                            self.search();
                            self.hideEditModal();
                            this.editModalLoading = true;
                        } else {
                            self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({content: '操作失败!'})
                    });
            }else{
                setTimeout(() => {
                    this.editModalLoading = false;
                    this.$nextTick(() => {
                        this.editModalLoading = true;
                    });
                }, 1000);
            }
        });
    },
    edit(row) {
        this.$refs['editForm'].resetFields();
        Object.assign(this.editForm, row);
        this.editForm.deptIds = !!row.deptId ? row.deptId.split(',') : [];
        this.showEditModal();
    },
    detail(row) {
        Object.assign(this.editForm, row);
        this.showDetailModal();
    }
}