/**
 * 工资列表 js
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.gatewayUrl;
    coreBasePath = _htConfig.gatewayUrl;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
            loading: false,
            saving: false, //存储标志位
            exporting: false, //导出标志位
            submitLoading: false,
            editModal: false,
            updateModal: false,
            editModalTitle: '新增',
            editModalLoading: true,
            updateModalTitle: '分配',
            subjectModal:false,
            subjectModalLoading:true,
            bigSubjects:[],
            smallSubjects:[],
            detailModal: false,
            companys: [],
            camsTaxs: [],
            camsProductProperties: [],
            pickSubjects: [],
            provinces: [],
            cities: [],
            banks: [],
            upload: {
                url: coreBasePath + 'salaryDatController/importSalaryExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },
            uploadSheBao: {
                url: coreBasePath + 'salaryDatController/importSheBaoExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },

            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    GE_create_time: '',
                    LE_create_time: '',
                    GE_salary_date: '',
                    LE_salary_date: ''
                },
                current: 1,
                size: 100
            },
            editForm: {
                companyId: '',
                companyName: '',
                createTime: '',
                productPropertiesId: '',
                openDate: ''


            },
            subjectForm:{
            	id:'',
                smallId:''
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
                        title: '公司',
                        key: 'companyName',
                        sortable: true, //开启排序
                    },
                   
                    {
                        title: '姓名',
                        key: 'name',
                        sortable: true, //开启排序
                    },
                    {
                        title: '证件号码',
                        key: 'idcardNo',
                        sortable: true, //开启排序
                    }, {
                        title: '本期收入',
                        key: 'benQiShouRu',
                        sortable: true, //开启排序
                    }, {
                        title: '本期免税收入',
                        key: 'benQiMianShuiShouRu',
                        sortable: true, //开启排序
                    }, {
                        title: '基本养老保险费',
                        key: 'jiBenYangLaoBaoXian',
                        sortable: true, //开启排序
                    },
                    {
                        title: '基本医疗保险费',
                        key: 'jiBenYiLiaoBaoXian',
                        sortable: true, //开启排序
                    }, {
                        title: '失业保险费',
                        key: 'shiYeBaoXian',
                        sortable: true, //开启排序
                    }, {
                        title: '住房公积金',
                        key: 'zhuFangGongJiJin',
                        sortable: true, //开启排序
                    },  {
                        title: '已扣缴税额',
                        key: 'yiKouJiaoShuiE',
                        sortable: true, //开启排序
                    },  {
                        title: '本期税额',
                        key: 'tax',
                    }, {
                        title: '科目代码',
                        key: 'keMuDaiMa',
                        sortable: true, //开启排序
                        render:(h,params)=>{debugger
                            if(params.row.keMuDaiMa!='') {
                                    return h('span',{
                                        style:{
                                         'font-weight': 'bold',
                                         'background-color': ' #C7EDCC'
                                    }
                                },params.row.keMuDaiMa)
                            }else {
                                return h('span',{},params.row.keMuDaiMa)  
                            }
                        }
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



let methods = {
    hideSubjectModal() {
        this.subjectModal = '设置科目';
        this.subjectModal = false;
      
    },
    showSubjectModal() {
        this.subjectModal = true;
    },
    beforeUpLoadFile() {
        this.loading = true
        debugger //导入Excel表格
        //  vm.condata.companyId=vm.editForm.companyId;
        //  vm.condata.productPropertiesId.companyId=vm.editForm.productPropertiesId;

    },
    submitSubjectForm() {debugger
        var self = this;
	    var selectIds=vm.selectIds
	    if(selectIds==undefined||selectIds.length===0){
	    	selectIds=new Array();
	    	selectIds[0]=self.editForm;
	    }
	    
    $.ajax({

        url: basePath + 'salaryDatController/editSubject',

        type: "post",

        dataType: "json",

        data: {
            selects: JSON.stringify(selectIds),
            smallSubjectId: self.subjectForm.smallId
        },
        success: function (data) { //请求服务器成功后返回页面时页面可以进行处理，data就是后端返回的数据
            if (!!data && data.code == '1') {debugger
                self.search();
                self.hideSubjectModal();
                this.subjectModalLoading = true;
            } else {
                self.$Modal.error({content: '请求接口失败,消息:' + data.msg})
                this.subjectModalLoading = false;
            }
            vm.selectIds.length=0
        },
        error: function (e) {
        	  self.$Modal.error({content: '请求接口失败,消息:' + data.msg})
              this.subjectModalLoading = false;
        }


    })
//                axios.post(basePath + 'salaryDatController/editSubject?smallSubjectId='+self.subjectForm.smallId+"&salaryId="+self.editForm.id)
//                .then(res => {
//                    if (!!res.data && res.data.code == '1') {debugger
//                        self.search();
//                        self.hideSubjectModal();
//                        this.subjectModalLoading = true;
//                    } else {
//                        self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
//                        this.subjectModalLoading = false;
//                    }
//                })
//                .catch(err => {
//                    self.$Modal.success({content: '操作成功!'})
//                });
     
    },
    uploadSuccess(response) {
        this.loading = false
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
        this.updateModal = false;

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
                    self.pickSubjects = res.data.data.pickSubjects;
                    self.smallSubjects=res.data.data.smallSubjects;
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
    subject(row) {debugger
        this.ModalTitle = '设置科目';
        this.$refs['editForm'].resetFields();
        Object.assign(this.editForm, row);
        this.editForm.id=row.id;
        this.showSubjectModal();
    },
    search() {
        this.searchForm.current = 1;
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
                axios.post(basePath + 'salaryDatController/delete', vm.selectIds)
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

    addJtSalary(){
        var self = this;
        if(vm.selectIds==undefined||!vm.selectIds.length>0){
            self.$Modal.error({
                content: '请选择工资记录'
            })
            return;
        }
        this.$Modal.confirm({
            title: '提示',
            content: '确定要生成计提工资吗?',
            onOk: () => {
                axios.post(basePath + 'salaryDatController/addJtSalary', vm.selectIds)
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
    createPersonTax(){
        var self = this;
        if(vm.selectIds==undefined||!vm.selectIds.length>0){
            self.$Modal.error({
                content: '请选择工资记录'
            })
            return;
        }
        this.$Modal.confirm({
            title: '提示',
            content: '确定要生成个人所得税吗?',
            onOk: () => {
                axios.post(basePath + 'salaryDatController/createPersonTax', vm.selectIds)
                    .then(res => {
                        debugger
                        if (!!res.data && res.data.code == '1') {
                        	 vm.$Modal.success({
                                 content: "生成成功"

                             })
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
        ExportForm.action = basePath + "pickStoreDatController/export";
        ExportForm.target = "iframe";

        addInput(ExportForm, "text", "companyName", vm.searchForm.condition.LIKE_company_name);
        addInput(ExportForm, "text", "beginTime", vm.searchForm.condition.GE_create_time);
        addInput(ExportForm, "text", "endTime", vm.searchForm.condition.LE_create_time);
        addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_salary_date);
        addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_salary_date);
        addInput(ExportForm, "text", "type", "1");
        ExportForm.submit();
        document.body.removeChild(ExportForm);

    },
    hideUpdateModal() {
        this.editModalTitle = '新增';
        this.updateModal = false;
        this.$refs['editForm'].resetFields();
    },
    submitEditForm() {
        var self = this; 
        var self = this;
        if(vm.selectIds==undefined||!vm.selectIds.length>0){
            self.$Modal.error({
                content: '请选择工资记录'
            })
            return;
        }
        $.ajax({

            url: basePath + 'salaryDatController/devide',

            type: "post",

            dataType: "json",

            data: {
                selects: JSON.stringify(vm.selectIds),
                date: this.editForm.pickDate
            },
            success: function (data) { //请求服务器成功后返回页面时页面可以进行处理，data就是后端返回的数据
                vm.$Modal.success({
                    content: "分配成功"
                })
                self.search();
                self.hideEditModal();
                this.editModalLoading = true;
            },
            error: function (e) {
                self.$Modal.error({
                    content: '请求接口失败,消息:' + res.data.msg
                })
                this.editModalLoading = false;
            }


        })


    },
    searchData() {
        debugger
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
     
        if (vm.searchForm.condition.GE_salary_date != '') {
            vm.searchForm.condition.GE_salary_date = getSearchDate(vm.searchForm.condition.GE_salary_date);
        }
        if (vm.searchForm.condition.LE_salary_date != '') {
            vm.searchForm.condition.LE_salary_date = getSearchDate(vm.searchForm.condition.LE_salary_date);
        }
        vm.searchForm.type = 1

        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'salaryDatController/searchSalary', this.searchForm).then(res => {
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