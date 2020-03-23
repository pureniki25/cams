/**
 * 还款账号管理 js
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    basePath = htConfig.gatewayUrl;
    coreBasePath = htConfig.gatewayUrl;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
            loading: false,
            saving: false, //存储标志位
            exporting: false, //导出标志位
            submitLoading: false,
            editModal: false,
            editModalTitle: '新增',
            editModalLoading: true,
            detailModal: false,
            companys: [],
            camsTaxs: [],
            camsProductProperties: [],
            provinces: [],
            cities: [],
            banks: [],
            upload: {
                url: basePath + 'productDat/importProductRestExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },

            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    LIKE_product_name: '',
                    LIKE_product_code: '',
                    GE_create_time: '',
                    LE_create_time: '',
                    GE_open_date: '',
                    LE_open_date: ''
                },
                companyName: '',
                productCode: '',
                productName: '',
                beginDate: '',
                endDate: '',
                page: 1,
                limit: 500
            },
            editForm: {
                companyId: '',
                companyName: '',
                createTime: '',
                productPropertiesId: '',
                importType: ''
            },
            condata: {
                companyId: "123",
                productPropertiesId: "234"
            },
            ruleValidate: {
                //表单验证
                //financeName: [{required: true, message: '必填项', trigger: 'blur'}],
                repaymentName: [{
                    required: true,
                    message: '请输入账户名',
                    trigger: 'blur'
                }],
                repaymentId: [{
                    required: true,
                    message: '请输入账号',
                    trigger: 'blur'
                }],
                repaymentBank: [{
                    required: true,
                    message: '请选择开户行',
                    trigger: 'blur'
                }],
                //repaymentSubBank: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
                //mainType: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
                //mainId: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
                //bankProvinceCity: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
                //phoneNumber: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
                // deptIds: [{required: true, message: '请至少关联一个分公司', trigger: 'blur'}],
                deptIds: [{
                    validator: function (rule, value, callback, source, options) {
                        if (vm.editForm.deptIds.length == 0) {
                            callback(new Error('请至少关联一个分公司'));
                        } else {
                            callback();
                        }
                    },
                    trigger: 'blur'
                }]
            },
            table: {
                col: [

                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '公司名称',
                        width: 100,
                        key: 'companyName',
                        sortable: true//开启排序
                    },
                    {
                        title: '商品编码',
                        width: 100,
                        key: 'productCode',
                        sortable: true//开启排序
                    }, {
                        title: '商品名称',
                        key: 'productName',
                        sortable: true//开启排序
                    }, {
                        title: '规格型号',
                        key: 'productType',
                        sortable: true//开启排序
                    }, {
                        title: '计量',
                        key: 'unit',
                        sortable: true//开启排序
                    },
                    {
                        title: '期初数量',
                        width: 100,
                        key: 'kuCunLiang',
                        sortable: true//开启排序
                    }, {
                        title: '期初金额',
                        width: 100,
                        key: 'qiChuJine',
                        sortable: true//开启排序
                    }, {
                        title: '本期收入数',
                        width: 100,
                        key: 'incomeCount',
                        sortable: true//开启排序
                    }, {
                        title: '本期收入金',
                        key: 'incomeJine',
                        sortable: true//开启排序
                    },  {
                        title: '本期发出数',
                        key: 'outcomeCount',
                        sortable: true//开启排序
                    },{
                        title: '本期发出金',
                        key: 'outcomeJine',
                        sortable: true//开启排序
                    },    {
                        title: '期末单价',
                        key: 'qiMoDanJia',
                        sortable: true//开启排序
                    },{
                        title: '期末结存数量',
                        key: 'restKuCunLiang',
                        sortable: true,//开启排序
                        render:(h,params)=>{debugger
                        	if(Number(params.row.restKuCunLiang)<0) {
            	            		return h('span',{
            	            			style:{
            	            			 'font-weight': 'bold',
            	            			 'background-color': ' #FF9999',
            	            			 'filter': 'Alpha(Opacity=30)'
            	            		}
                        		},params.row.restKuCunLiang)
                        	}else {
                        		return h('span',{},params.row.restKuCunLiang)  
                        	}
                        }
                    }, 
                    {
                        title: '期末结存金额',
                        key: 'restJieCunJine',
                        sortable: true,//开启排序
                        render:(h,params)=>{debugger
                        	if(Number(params.row.restJieCunJine)<0) {
            	            		return h('span',{
            	            			style:{
            	            			 'font-weight': 'bold',
            	            			 'background-color': ' #FF9999',
            	            			 'filter': 'Alpha(Opacity=30)'
            	            		}
                        		},params.row.restJieCunJine)
                        	}else {
                        		return h('span',{},params.row.restJieCunJine)  
                        	}
                        }
                    }
                    
                 
                ],
                data: [],
                loading: false,
                total: 0
            }
        },
        methods: methods,
        created: function () {
            this.initData();
  
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



let methods = {

    beforeUpLoadFile() {
        debugger //导入Excel表格
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
        //公司列表
        axios.get(coreBasePath + 'InvoiceController/findAll')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.companys = res.data.data.companys;
                    self.camsTaxs = res.data.data.camsTaxs;
                    self.camsProductProperties = res.data.data.camsProductProperties;
                } else {
                    self.$Modal.error({
                        content: '调用接口失败,消息:' + res.data.msg
                    });
                }
            })
            .catch(err => {
                self.$Modal.error({
                    content: '操作失败!'
                });
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
                    self.$Modal.error({
                        content: '调用接口失败，消息:' + res.data.msg
                    });
                }
            })
            .catch(err => {
                self.$Modal.error({
                    content: '操作失败!'
                });
            })
    },

    paging(current) {
        this.searchForm.page = current;
        this.searchData();
    },
    search() {
        this.searchForm.page = 1;
        this.searchData();
    },
    daochu() {
        this.searchForm.page = 1;
        this.exportData();
    },
    deleteAll() {
        var self = this;
        this.$Modal.confirm({
            title: '提示',
            content: '确定要删除吗?',
            onOk: () => {
                axios.post(basePath + 'productDat/delete', vm.selectIds)
                    .then(res => {
                        debugger
                        if (!!res.data && res.data.code == '1') {

                            self.search()
                        } else {
                            self.$Modal.error({
                                content: '请求接口失败,消息:' + res.data.msg
                            })
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({
                            content: '操作失败!'
                        })
                    })
            }
        });
    },
    exportData() {
        debugger
        var ExportForm = document.createElement("FORM");
        document.body.appendChild(ExportForm);
        ExportForm.method = "POST";
        ExportForm.action = basePath + "productDat/export";
        ExportForm.target = "iframe";
        ExportForm.action = basePath + "productDat/export";
        addInput(ExportForm, "text", "companyName", vm.searchForm.condition.LIKE_company_name);
        addInput(ExportForm, "text", "productName", vm.searchForm.condition.LIKE_product_name);
        addInput(ExportForm, "text", "productCode", vm.searchForm.condition.LIKE_product_code);
        addInput(ExportForm, "text", "beginTime", vm.searchForm.condition.GE_create_time);
        addInput(ExportForm, "text", "endTime", vm.searchForm.condition.LE_create_time);
        addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_open_date);
        addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_open_date);

        ExportForm.submit();
        document.body.removeChild(ExportForm);


        //        axios.post(basePath + 'productDat/export', this.searchForm).then(res => {
        //            self.table.loading = false;
        //            if (!!res.data && res.data.code == 0) {
        //                self.table.data = res.data.data;
        //                self.table.total = res.data.count;
        //            } else {
        //                self.$Message.error({content: res.data.msg})
        //            }
        //        }
        //        
        //        
        //        
        //        
        //    ).catch(err => {
        //        self.table.loading = false;
        //    }
        //);
        // self.loading =  false;
    },
    searchData() {
        debugger
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'productDat/inventoryPage', this.searchForm).then(res => {
            self.table.loading = false;
            if (!!res.data && res.data.code == 0) {
                self.table.data = res.data.data;
                self.table.total = res.data.count;
            } else {
                self.$Message.error({
                    content: res.data.msg
                })
            }
        }).catch(err => {
            self.table.loading = false;
        });
        // self.loading =  false;
    },
    submitEditForm() {
        var self = this;
        this.$refs['editForm'].validate(valid => {
            if (valid) {
                axios.post(basePath + 'InvoiceController/edit', self.editForm)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {
                            self.search();
                            self.hideEditModal();
                            this.editModalLoading = true;
                        } else {
                            self.$Modal.error({
                                content: '请求接口失败,消息:' + res.data.msg
                            })
                            this.editModalLoading = false;
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({
                            content: '操作失败!'
                        })
                    });
            } else {
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
        Object.assign(this.editForm, row);
        this.showDetailModal();
    },
    requires(value) {
        vm.selectIds = value;
    },
    delete(row) {
        var self = this;
        this.$Modal.confirm({
            title: '提示',
            content: '确定要删除吗?',
            onOk: () => {
                axios.get(basePath + 'InvoiceController/delete', {
                        params: {
                            id: row.companyId
                        }
                    })
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {

                            self.search()
                        } else {
                            self.$Modal.error({
                                content: '请求接口失败,消息:' + res.data.msg
                            })
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({
                            content: '操作失败!'
                        })
                    })
            }
        });
    },

}
var addInput = function (form, type, name, value) {
    var input = document.createElement("input");
    input.type = type;
    input.name = name;
    input.value = value;
    form.appendChild(input);
}

function getSearchDate(data) {
    debugger
    var y = data.getFullYear();
    var m = data.getMonth() + 1;
    var d = data.getDate();

    if (m < 10) {
        m = '0' + m;
    }
    if (d < 10) {
        d = '0' + d;
    }

    return y + '-' + m + '-' + d;
}