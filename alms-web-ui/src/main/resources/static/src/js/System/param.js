let app
let dateStart
let dateEnd
let table 
let readTimeValid = function () {
    return app && app.type == '必读'
}
window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    table = layui.table
    let laydate = layui.laydate

    app = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () { }

    })

    table.render({
        elem: "#param",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'paramType',
                title: '参数编号'
            }, {
                field: 'paramTypeName',
                title: '参数类型名称',
            }, {
                field: 'remark',
                title: '参数类型说明'
            }, {
                field: 'status',
                title: '状态',
                templet: function (d) {
                    return d.status == '0' ? '禁用' : '启用';
                }
            }, {
                field: 'updateUser',
                title: '更新人id',
                templet:function(d){
                    return (d.updateUser||d.createUser)||''
                }
            }, {
                field: 'updateTime',
                title: '更新时间',
                templet:function(d){
                    return (d.updateTime||d.createTime)||''
                }
            }, {
                title: '操作',
                toolbar: "#toolbar"
            }]
        ], //设置表头
        url: basePath + 'sys/param/page',
        page: true,
        done: (res, curr, count) => { }
    })

    table.on('tool(param)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'edit') {
            app.openEidtTypeModal(data, false)
        }
        if (event == 'delete') {
            app.deleteParamType(data)
        }
        if (event == 'list') {
            app.openParamListModal(data)
        }
    })
})

let data = {
    key: '',
    editTypeModal: {
        show: false
    },
    editTypeForm: {},
    ruleValidate: {
        paramType: [
            { required: true, message: '参数类型编号不能为空', trigger: 'blur' }
        ],
        paramTypeName: [
            { required: true, message: '参数类型名称不能为空', trigger: 'blur' }
        ],
        status: [
            // { required: true, message: '状态不能为空', trigger: 'blur' }

        ]
    },
    paramListModal: {
        show: false,
        list: []
    },
    editParamModal: {
        show: false,
        add: true,
    },
    editParamForm: {
        param: {
            paramId: '',
            paramName: '',
            remark: '',
            paramValue: '',
            paramValue2: '',
            paramValue3: '',
            paramValue4: '',
            paramValue5: ''
        }
    },
    batchAddParmModal: {
        show: false,
        param: ''
    }
}

let methods = {
    openEidtTypeModal: function (data, add) {
        if (add) {
            this.$refs['editTypeForm'].resetFields();
            this.editTypeForm = {
                paramType: '',
                paramTypeName: '',
                status: '',
                remark: ''
            }
            this.editTypeModal.title = '新增参数类型'
            this.editTypeModal.add = true
            this.editTypeModal.show = true;
        } else {
            this.editTypeForm = data;
            this.editTypeModal.title = '编辑参数类型'
            this.editTypeModal.show = true;
            this.editTypeModal.add = false
        }
    },
    saveParamType: function () {
        this.$refs['editTypeForm'].validate((valid) => {
            if (app.editTypeModal.add) {
                axios.post(basePath + 'sys/param/addParamType', this.editTypeForm)
                    .then(function (res) {
                        console.log(res)
                        if (res.data.code == "1") {
                            app.editTypeModal.show = false
                            app.tableReload(app.key && app.key != '' ? { key: app.key } : {}, { curr: 1 })
                        } else {
                            app.$Modal.error({ content: '接口调用异常!' + (res.data.msg || res.data.status_code || '') });
                        }
                    })
                    .catch(function (err) {
                        app.$Modal.error({ content: '接口调用异常!' });
                    })
            } else {
                axios.post(basePath + 'sys/param/updateParamType', this.editTypeForm)
                    .then(function (res) {
                        console.log(res)
                        if (res.data.code == "1") {
                            app.editTypeModal.show = false
                            app.tableReload(app.key && app.key != '' ? { key: app.key } : {}, { curr: 1 })
                        } else {
                            app.$Modal.error({ content: '接口调用异常!' + (res.msg || res.status_code || '') });
                        }
                    })
                    .catch(function (err) {
                        app.$Modal.error({ content: '接口调用异常!' });
                    })
            }
        })

    },
    tableReload: function (where, page) {
        let playload = {}
        if(where){
            playload.where = where
        }
        playload.page = page 
        table.reload('param', playload)
    },
    deleteParamType: function (data) {
        axios.post(basePath + 'sys/param/delParamType', data)
            .then(function (res) {
                if (res.data.code == '1') {
                    app.tableReload(app.key && app.key != '' ? { key: app.key } : {}, { curr: 1 })
                } else {
                    app.$Modal.error({ content: '接口调用异常!' + (res.data.msg || res.data.status_code || '') });
                }
            })
            .catch(function (err) {
                app.$Modal.error({ content: '接口调用异常!' });
            })
    },
    openParamListModal: function (data) {
        app.currentParamList = data
        app.listParam(data)
    },
    openEditParamModal: function (data) {
        if (data) {
            app.editParamForm = data;
            app.editParamModal.show = true;
            app.editParamModal.title = "编辑参数"
            app.editParamModal.add = false;
        } else {
            app.editParamModal.title = "新增参数"
            app.editParamModal.add = true;
            app.editParamModal.show = true;
            app.$refs['editParamForm'].resetFields()
        }
    },
    saveParm: function () {
        if (app.editParamModal.add) {
            app.editParamForm.paramType = app.currentParamList.paramType
            app.editParamForm.paramTypeName = app.currentParamList.paramTypeName
            axios.post(basePath + 'sys/param/addParm', [app.editParamForm])
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.editParamModal.show = false;
                        app.listParam(app.currentParamList);

                    } else {
                        app.$Modal.error({ content: '接口调用异常!' + (res.data.msg || res.data.status_code || '') });
                    }
                })
                .catch(function (err) {

                })
        } else {
            axios.post(basePath + 'sys/param/updateParm', app.editParamForm)
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.editParamModal.show = false;

                        app.listParam(app.currentParamList);

                    } else {
                        app.$Modal.error({ content: '接口调用异常!' + (res.data.msg || res.data.status_code || '') });
                    }
                })
                .catch(function (err) {

                })
        }
    },
    deleteParam: function (data) {
        axios.post(basePath + 'sys/param/delParm', data)
            .then(function (res) {
                if (res.data.code == '1') {
                    app.listParam(app.currentParamList);
                } else {
                }
            })
            .catch(function (err) {

            })
    },
    listParam: function (data) {
        axios.get(basePath + 'sys/param/listParam', {
            params: {
                paramType: data.paramType
            }
        })
            .then(function (res) {
                if (res.data.code == '1') {
                    app.paramListModal.list = res.data.data;
                    app.paramListModal.show = true

                } else {

                }
            })
            .catch(function (err) {

            })
    },
    openBatchAddParmModal: function () {
        app.editParamModal.add = true
        app.batchAddParmModal.show = true
        app.batchAddParmModal.param = ''
    },
    batchSaveParam: function () {
        app.editParamForm.paramType = app.currentParamList.paramType
        app.editParamForm.paramTypeName = app.currentParamList.paramTypeName
        console.log(app.batchAddParmModal.param)
        let t = app.batchAddParmModal.param.split('\n');
        if (t.length == 0) {
            return;
        }
        let params = []
        let cancelFlag = false
        t.forEach(element => {
            if (element && element != '') {
                let param = {}
                let value = element.split(':')
                if (value.length == 0) {
                    param.paramName = element;
                } else {
                	console.log(value)
                	if(value.length>6){
                		cancelFlag = true
                		app.$Message.error({content:element+'有错,请检查'});
                		return ;
                	}
                    value.forEach((e, i) => {
                        if (i == 0) {
                            param.paramName = e
                        } else if (i == 1) {
                            param['paramValue'] = e
                        } else {
                            param['paramValue' + (i)] = e
                        }
                    });
                }
                param.paramType = app.editParamForm.paramType;
                param.paramTypeName = app.editParamForm.paramTypeName;
                params.push(param)
            }
        });
        if(cancelFlag){
        	return ;
        }
        axios.post(basePath + 'sys/param/addParm', params)
            .then(function (res) {
                if (res.data.code == '1') {
                    app.batchAddParmModal.show = false;

                    app.listParam(app.currentParamList);

                } else {
                    app.$Modal.error({ content: '接口调用异常!' + (res.data.msg || res.data.status_code || '') });
                }
            })
            .catch(function (err) {

            })
    }
}