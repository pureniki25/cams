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
    editModalTitle: '新增',
    editModalLoading: true,
    detailModal: false,
    findProjExtRateCalWay: [],
    searchForm: {
        condition: {
            EQ_business_id: '',
            EQ_project_id: '',
        },
        current: 1,
        size: 10
    },
    editForm: {
        businessId: '',
        projectId: '',
        rateType: '',
        rateName: '',
        rateValue: '',
        calcWay: '',
        feeId: '',
        feeName: '',
        beginPeroid: '',
        endPeroid: ''
    },
    ruleValidate: {
        //表单验证
        businessId: [{ required: true, message: '请输入业务ID', trigger: 'blur' }],
        projectId: [{ required: true, message: '请输入标的ID', trigger: 'blur' }],
        rateType: [
//            { type: 'number', required: true, message: '请输入费正确的率类型', trigger: 'blur' },
//            { pattern: /^\\d+(\\.\\d+)?$/, message: '请输入整数', trigger: 'blur' }
            { pattern: /^[0-9]+$/, required:true, message: '请输入整数', trigger: 'blur' }
        ],
        rateName: [{ required: true, message: '请输入费率类型名称', trigger: 'blur' }],
        rateValue: [ 
            //{ type: 'number', required: true, message: '请输入正确的费率值', trigger: 'blur' }, 
//            { pattern: /^[0-9]+(.[0-9]*)$/, message: '请输入数字', trigger: 'blur' }
            { pattern: /^[0-9]+(.[0-9]*)?$/, required: true,message: '请输入数字', trigger: 'blur' }
        ],
        calcWay: [{ type: 'number', required: true, message: '请选择计算方式', trigger: 'blur' }],
        feeId: [{ required: true, message: '请输入费率UUID', trigger: 'blur' }],
        feeName: [{ required: true, message: '请输入费率名称', trigger: 'blur' }],
        beginPeroid: [
            { type: 'number', required: true, message: '请输入正确的开始期数', trigger: 'blur' }, 
            { pattern: /^[0-9]+$/, required: true, message: '请输入整数', trigger: 'blur' }
        ],
        endPeroid: [
            { type: 'number', required: true, message: '请输入正确的结束期数', trigger: 'blur' },
            { pattern: /^[0-9]+$/, required: true, message: '请输入整数', trigger: 'blur' }
        ],
        // deptIds:[{
        //     validator:function(rule, value, callback, source, options){
        //         if(vm.editForm.deptIds.length == 0 ){
        //             callback(new Error('请至少关联一个分公司'));
        //         }else{
        //             callback();
        //         }
        //     },
        //     trigger:'blur'
        // }]
    },
    table: {
        col: [{
            type: 'selection',
            width: 60,
            align: 'center'
        }, {
            title: '业务ID',
            key: 'businessId'
        }, {
            title: '标的ID',
            key: 'projectId'
        }, {
            title: '费率类型',
            key: 'rateType'
        }, {
            title: '费率类型名称',
            key: 'rateName'
        }, {
            title: '费率值',
            key: 'rateValue'
        }, {
            title: '计算方式',
            key: 'calcWayName'
        }, {
            title: '费率UUID',
            key: 'feeId'
        }, {
            title: '费率名称',
            key: 'feeName'
        }, {
            title: '开始期数',
            key: 'beginPeroid'
        }, {
            title: '结束期数',
            key: 'endPeroid'
        }, /* {
            title: '创建日期',
            key: 'createTime'
        }, {
            title: '更新时间',
            key: 'updateTime'
        },  */{
            title: '操作',
            key: 'action',
            // fixed: 'right',
            align: 'center',
            render: (h, params) => {
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
                    props: { type: '', size: 'small' },
                    style: { marginRight: '10px' },
                    on: {
                        click: () => {
                            vm.delete(params.row);
                        }
                    }
                }, '删除');

                let btnArr = [];

                if (authValid('edit')) {
                    btnArr.push(edit);
                }
                if (authValid('delete')) {
                    btnArr.push(del);
                }
                return h('div', btnArr);
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
        axios.post(coreBasePath + 'projExtRate/findProjExtRateCalWay')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.findProjExtRateCalWay = res.data.data;
                } else {
                    self.$Modal.error({ content: '调用接口失败,消息:' + res.data.msg });
                }
            })
            .catch(err => {
                self.$Modal.error({ content: '操作失败!' });
            });

    },
    paging(current) {
        this.searchForm.current = current;
        this.searchData();
    },
    search() {
        this.searchForm.current = 1;
        this.searchData();
    },
    searchData() {
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(coreBasePath + 'projExtRate/search', this.searchForm).then(res => {
            self.table.loading = false;
            if (!!res.data && res.data.code == 0) {
                self.table.data = res.data.data;
                self.table.total = res.data.count;
            } else {
                self.$Message.error({ content: res.data.msg })
            }
        }
        ).catch(err => {
            self.table.loading = false;
            self.$Message.error({ content: err })
        }
        );
        // self.loading =  false;
    },
    submitEditForm() {
        var self = this;
        this.$refs['editForm'].validate(valid => {
            if (valid) {
                axios.post(coreBasePath + 'projExtRate/edit', self.editForm)
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {
                            self.search();
                            self.hideEditModal();
                            this.editModalLoading = true;
                        } else {
                            self.$Modal.error({ content: '请求接口失败,消息:' + res.data.msg })
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({ content: '操作失败!' })
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
        this.showEditModal();
    },
    detail(row) {
        Object.assign(this.editForm, row);
        this.showDetailModal();
    },
    delete(row) {
        var self = this;
        this.$Modal.confirm({
            title: '提示',
            content: '确定要删除吗?',
            onOk: () => {
                axios.get(coreBasePath + 'projExtRate/delete', { params: { id: row.id } })
                    .then(res => {
                        if (!!res.data && res.data.code == '1') {

                            self.search()
                        } else {
                            self.$Modal.error({ content: '请求接口失败,消息:' + res.data.msg })
                        }
                    })
                    .catch(err => {
                        self.$Modal.error({ content: '操作失败!' })
                    })
            }
        });
    },
}