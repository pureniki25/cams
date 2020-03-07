/**
 * 还款账号管理 js
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
	debugger
    var _htConfig = htConfig;
    basePath = _htConfig.gatewayUrl;
    coreBasePath = _htConfig.gatewayUrl;
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
    editModalTitle: '新增',
    editModalLoading: true,
    detailModal: false,
    companys: [],
	upload: { 
		url: coreBasePath + 'departmentBank/importCompanyExcel',
		headers: {
		 Authorization: axios.defaults.headers.common['Authorization'],
		},
   },
    provinces: [],
    cities: [],
    banks: [],
    searchForm: {
        condition: {
            LIKE_company_name: ''
        },
        current: 1,
        size: 100
    },
    editForm: {
        companyId: '',
        companyName: '',
        createTime: '',
        companyNo:'',
        companyOwner:'',
        companyStatus:'',
        companyRule:'',
        companyType:''
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
            title: '序号',
            key: 'idx'
        },  {
            title: '公司名称',
            key: 'companyName'
        }, {
            title: '纳税识别码',
            key: 'companyNo'
        },{
            title: '纳税法人',
            key: 'companyOwner'
        }, {
            title: '公司状态',
            key: 'companyStatus',
            render:(h, params) => {
                let companyStatus = params.row.companyStatus; //公司状态：1：小规模，2：一般纳税人，3：个体户
                if (companyStatus==='1'){ return "小规模" };
                if (companyStatus==='2'){ return "一般纳税人"};
                if (companyStatus==='3'){ return "个体户" }
            }
        }, {
            title: '财务制度',
            key: 'companyRule',
            render:(h, params) => {
                let companyRule = params.row.companyRule; //公司制度：1：企业会计制度，2：小企业会计制度
                if (companyRule==='1'){ return "企业会计制度" };
                if (companyRule==='2'){ return "小企业会计制度"};
            }
        }, {
            title: '公司类型',
            key: 'companyType',
            render:(h, params) => {
                let companyType = params.row.companyType; //公司类型：1：生产型，2：贸易型，3：进出口型
                if (companyType==='1'){ return "生产型" };
                if (companyType==='2'){ return "贸易型"};
                if (companyType==='3'){ return "进出口型" }
            }	
        }, {
            title: '创建时间',
            key: 'createTime'
        },{
            title: '操作',
            key: 'action',
            // fixed: 'right',
            align: 'center',
            render: (h, params) => {
                let detail = h('Button', {
                    props: {
                        type: '',
                        size: 'small'
                    },
                    style: {
                        marginRight: '10px'
                    },
                    on: {
                        click: () => {
                            vm.detail(params.row)
                        }
                    }
                }, '编辑');

                let edit = h('Button', {
                    props: {
                        type: '',
                        size: 'small'
                    },
                    style: {
                        marginRight: '10px'
                    },
                    on: {
                        click: () => {
                            vm.edit(params.row);
                        }
                    },
                }, '编辑');

                let del = h('Button', {
                    props: {type: '', size: 'small'},
                    style: {marginRight: '10px'},
                    on: {
                        click: () => {
                            vm.delete(params.row);
                        }
                    }
                }, '删除');

                let btnArr = [];
                btnArr.push(detail);
                btnArr.push(del);

              
                
                return h('div', btnArr);
            }
        }],
        data: [],
        loading: false,
        total: 0
    }
}

let methods = {
	
    beforeUpLoadFile() {
   debugger
        //  vm.condata.companyId=vm.editForm.companyId;
        //  vm.condata.productPropertiesId.companyId=vm.editForm.productPropertiesId;

    },

    uploadSuccess(response) {
        if (response.code == '1') {
            vm.$Modal.success({
                content: "导入成功"
            })
        } else {
            vm.$Modal.error({
                content: response.msg
            })
        }

    },
    showEditModal() {
        this.editModal = true;
    },
    hideEditModal() {
        this.editModalTitle = '新增';
        this.editModal = false;
        this.$refs['editForm'].resetFields();
    },
    showDetailModal() {
        this.detailModal = true;
    },
    //初始化数据
    initData() {
        var self = this;
        self.upload.url=coreBasePath + 'departmentBank/importCompanyExcel';
        //公司列表
        axios.get(coreBasePath + 'departmentBank/findAll')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.companys = res.data.data;
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

    paging(current) {
        this.searchForm.current = current;
        this.searchData();
    },
    search(){
        this.searchForm.current = 1;
        this.searchData();
    },
    searchData() {debugger
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'departmentBank/search', this.searchForm).then(res => {
            self.table.loading = false;
            if (!!res.data && res.data.code == 0) {debugger
                self.table.data = res.data.data;
                self.table.total = res.data.count;
            } else {
                self.$Message.error({content: res.data.msg})
            }
        }
    ).catch(err => {
        self.table.loading = false;
    }
);
// self.loading =  false;
},
submitEditForm() {
    var self = this;
    this.$refs['editForm'].validate( valid => {
        if(valid){
            axios.post(basePath + 'departmentBank/edit', self.editForm)
            .then(res => {
                if (!!res.data && res.data.code == '1') {debugger
                    self.search();
                    self.hideEditModal();
                    this.editModalLoading = true;
                } else {
                    self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                    this.editModalLoading = false;
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
    this.editModalTitle = '编辑';
    this.$refs['editForm'].resetFields();
    Object.assign(this.editForm, row);
    this.editForm.deptIds = !!row.deptId ? row.deptId.split(',') : [];
    this.showEditModal();
},
detail(row) {
	debugger
    Object.assign(this.editForm, row);
    this.showDetailModal();
},
delete(row) {
    var self = this;
    this.$Modal.confirm({
        title: '提示',
        content: '确定要删除吗?',
        onOk: () => {debugger
            axios.get(basePath + 'departmentBank/delete', {params: {id: row.companyId}})
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    
                    self.search()
                } else {
                    self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'})
            })
        }
    });
},
}