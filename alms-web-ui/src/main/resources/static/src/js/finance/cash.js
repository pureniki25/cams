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
            subjectModal: false,
            editModalTitle: '新增',
            editModalLoading: true,
            subjectModalLoading: true,
            detailModal: false,
            companys: [],
            bigSubjects: [],
            smallSubjects: [],
            cashSubjects: [],
            cash2Subjects: [],
            camsTaxs: [],
            camsProductProperties: [],
            provinces: [],
            cities: [],
            banks: [],
            upload: {
                url: basePath + 'FeeDatController/importFeeFlowExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },
            uploadCertify: {
                url: basePath + 'FeeDatController/importFeeCertifyFlowExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },

            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    GE_create_time: '',
                    LE_create_time: '',
                    GE_ping_zheng_ri_qi: '',
                    LE_ping_zheng_ri_qi: ''
                },
                current: 1,
                size: 100
            },
            editForm: {
                id: '',
                ids: '',
                companyId: '',
                companyName: '',
                createTime: '',
                productPropertiesId: '',
                openDate: '',
                cashSubjects: [],
                cash2Subjects: [],
                cash3Subjects: [],
            },
            subjectForm: {
                id: '',
                smallId: ''
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
                ids: [{
                    validator: function (rule, value, callback, source, options) {
                        if (vm.editForm.ids.length == 0) {
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
                        align: 'center',
                        sortable: true,//开启排序
                    },
                    {
                        title: '公司名称',
                        width: 100,
                        key: 'companyName',
                        sortable: true,//开启排序
                    },
                    {
                        title: '发票号码',
                        width: 100,
                        key: 'invoiceNumber',
                        sortable: true,//开启排序
                    }, {
                        title: '期间',
                        key: 'qiJian',
                        sortable: true,//开启排序
                    }, {
                        title: '凭证号',
                        key: 'pingZhengHao',
                        sortable: true,//开启排序
                    }, {
                        title: '摘要',
                        key: 'zhaiYao',
                        sortable: true,//开启排序
                    },
                    {
                        title: '科目代码',
                        width: 100,
                        key: 'keMuDaiMa',
                        render: (h, params) => {

                            if (params.row.keMuDaiMa != '') {
                                return h('span', {
                                    style: {
                                        'font-weight': 'bold',
                                        'background-color': ' #C7EDCC'
                                    }
                                }, params.row.keMuDaiMa)
                            } else {
                                return h('span', {}, params.row.keMuDaiMa)
                            }
                        }
                    }, {
                        title: '原币金额',
                        width: 100,
                        key: 'localAmount',
                        sortable: true,//开启排序
                    }, {
                        title: '借款金额',
                        width: 100,
                        key: 'borrowAmount',
                        sortable: true,//开启排序
                    }, {
                        title: '贷款金额',
                        key: 'almsAmount',
                        sortable: true,//开启排序
                    }, {
                        title: '行号',
                        key: 'hangHao',
                        sortable: true,//开启排序
                    }, {
                        title: '单位',
                        key: 'danWei',
                        sortable: true,//开启排序
                    }, {
                        title: '导入时间',
                        key: 'createTime',
                        sortable: true,//开启排序
                    }, {
                        title: '操作',
                        key: 'action',
                        // fixed: 'right',
                        align: 'center',
                        render: (h, params) => {


                            let subject = h('Button', {
                                props: {
                                    type: '',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                },
                                on: {
                                    click: () => {
                                        vm.subject(params.row);
                                    }
                                },
                            }, '设置科目');



                            let btnArr = [];
                            btnArr.push(subject);



                            return h('div', btnArr);
                        }
                    }
                ],
                data: [],
                loading: false,
                total: 0
            }
        },
        methods: {
            addCash() {
                debugger
                console.log(123)
                this.editModal = true;


                this.$refs.editForm2.fields.forEach(function (e) {
                    e.resetField()
                })
                this.$refs.editForm2._props.model.cashSubjects.forEach(function (e) {
                    e.jine = ''
                })
                this.$refs.editForm2._props.model.cash2Subjects.forEach(function (e) {
                    e.jine = ''
                })
                this.$refs.editForm2._props.model.cash3Subjects.forEach(function (e) {
                    e.jine = ''
                })


            },
            handleSubmit(editForm) { 
                debugger
                var self = this;
                self.editForm.openDate=getSearchDate(self.editForm.openDate)
                axios.post(coreBasePath + 'FeeDatController/saveCash', self.editForm)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {
                            debugger
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
            showSubjectModal() {
                this.subjectModal = true;
            },
            hideSubjectModal() {
                this.subjectModal = '设置科目';
                this.subjectModal = false;
                this.$refs['subjectForm'].resetFields();
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
                            self.companys = res.data.data.companys
                            self.camsTaxs = res.data.data.camsTaxs
                            self.camsProductProperties = res.data.data.camsProductProperties
                            self.smallSubjects = res.data.data.smallSubjects
                            self.cashSubjects = res.data.data.cashSubjects
                            self.editForm.cashSubjects = res.data.data.cashSubjects
                            self.cash2Subjects = res.data.data.cash2Subjects
                            self.editForm.cash2Subjects = res.data.data.cash2Subjects
                            self.editForm.cash3Subjects = res.data.data.cash3Subjects
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
                this.searchForm.current = current;
                this.searchData();
            },
            search() {
                vm.searchForm.current = 1;
                this.searchData();
            },
            daochu() {
                this.searchForm.current = 1;
                this.exportData();
            },
            deleteAll() {
                var self = this;
                this.$Modal.confirm({
                    title: '提示',
                    content: '确定要删除吗?',
                    onOk: () => {
                        axios.post(basePath + 'FeeDatController/deleteCash', vm.selectIds)
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
            exportData() {

                var ExportForm = document.createElement("FORM");
                document.body.appendChild(ExportForm);
                ExportForm.method = "POST";
                ExportForm.action = basePath + "FeeDatController/exportCash";
                ExportForm.target = "iframe";

                addInput(ExportForm, "text", "companyName", vm.searchForm.condition.LIKE_company_name);
                addInput(ExportForm, "text", "beginTime", vm.searchForm.condition.GE_create_time);
                addInput(ExportForm, "text", "endTime", vm.searchForm.condition.LE_create_time);
                addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_ping_zheng_ri_qi);
                addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_ping_zheng_ri_qi);
                addInput(ExportForm, "text", "feeType", "2");
                ExportForm.submit();
                document.body.removeChild(ExportForm);

            },
            searchData() {

                /* table.reload('main_table', {
                     where: vm.search,
                     page: {curr: 1}
                 })*/
                if (vm.searchForm.condition.GE_create_time === '') {
                    return
                }
                vm.searchForm.condition.GE_create_time = getSearchDate(vm.searchForm.condition.GE_create_time);
                vm.searchForm.condition.LE_create_time = getSearchDate(vm.searchForm.condition.LE_create_time);
                if (vm.searchForm.condition.GE_ping_zheng_ri_qi != '') {
                    vm.searchForm.condition.GE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.GE_ping_zheng_ri_qi);
                }
                if (vm.searchForm.condition.LE_ping_zheng_ri_qi != '') {
                    vm.searchForm.condition.LE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.LE_ping_zheng_ri_qi);
                }


                var self = this;
                //self.loading =  true;
                //self.table.loading = true;
                axios.post(basePath + 'FeeDatController/cashSearch', this.searchForm).then(res => {
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
            getSmallSubject() {
                axios.get(coreBasePath + 'FeeDatController/findSmallSubject?id=' + vm.subjectForm.id)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {


                            vm.smallSubjects = res.data.data.smallSubjects;
                        } else {
                            vm.$Modal.error({
                                content: '调用接口失败,消息:' + res.data.msg
                            });
                        }
                    })
                    .catch(err => {
                        vm.$Modal.error({
                            content: '操作失败!'
                        });
                    });
            },
            submitSubjectForm() {
                var self = this;
                axios.post(basePath + 'FeeDatController/editSubject?smallSubjectId=' + self.subjectForm.smallId + "&feeId=" + self.editForm.id)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {

                            self.search();
                            self.hideSubjectModal();
                            this.subjectModalLoading = true;
                        } else {
                            self.$Modal.error({
                                content: '请求接口失败,消息:' + res.data.msg
                            })
                            this.subjectModalLoading = false;
                        }
                    })
                    .catch(err => {
                        self.$Modal.success({
                            content: '操作成功!'
                        })
                    });

            },

            getSearchDate(data) {
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
            },
            edit(row) {
                this.editModalTitle = '编辑';
                this.$refs['editForm'].resetFields();
                Object.assign(this.editForm, row);
                this.editForm.ids = !!row.id ? row.id.split(',') : [];
                this.showEditModal();
            },

            subject(row) {

                this.ModalTitle = '设置科目';
                this.$refs['editForm'].resetFields();
                Object.assign(this.editForm, row);
                this.editForm.id = row.id;
                this.showSubjectModal();
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
        },
        created: function () {
            this.initData();
            this.search();
        },
        updated: function () {
            this.loading = false;
        }

    })


})





function getSearchDate(data) {

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