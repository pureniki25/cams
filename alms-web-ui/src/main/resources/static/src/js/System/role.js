let app
let dateStart
let dateEnd
let table
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
        elem: "#role",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'roleCode',
                title: '角色编码'
            }, {
                field: 'roleName',
                title: '角色名称',
            }, {
                field: 'roleAreaType',
                title: '角色区域控制类型',
                templet: function (d) {
                    if(d.roleAreaType == 1){
                        return "全局性";
                    }else  if(d.roleAreaType == 2){
                        return "区域性";
                    }else  if(d.roleAreaType == 3){
                        return "只查看自己跟进";
                    }else  if(d.roleAreaType == 4){
                        return "只查看车贷业务";
                    }else  if(d.roleAreaType == 5){
                        return "只查看房贷业务";
                    }else{
                        return "未定义的类型";
                    }
                }
            },
            {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/role/list',
        page: false,
        done: (res, curr, count) => { }
    })

    table.on('tool(role)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'delete') {
            app.delete(data)
        }
        if (event == 'edit') {
            app.openModal(false, data)
        }
    })
})

let data = {
    editModal: {
        show: false,
        type: ''
    },
    editForm: {
        roleCode: '',
        roleName: '',
        roleAreaType: '',
    },
}

let methods = {
    tableReload: function () {
        table.reload('role')
    },
    delete: function (data) {
        axios.post(basePath + 'sys/role/delete', data)
            .then(function (r) {
                if (r.data.code == '1') {
                    app.tableReload()
                }
            })
            .catch(function (r) {
                app.$Modal.error({content:'调用接口失败'})
            })
    },
    openModal: function (add, data) {
        if (add) {
            app.$refs['editForm'].resetFields()
            app.editModal.show = true
            app.editModal.type = 'add'
        } else {
            app.editModal.show = true
            app.editModal.type = 'update'
            app.editForm = data
            app.editForm.roleAreaType = data.roleAreaType + ''
        }
    },closeModal:function(){
        app.editModal.show = false
        app.$refs['editForm'].resetFields()
    },
    save: function () {
        if (app.editModal.type == 'add') {
            axios.post(basePath + 'sys/role/add',app.editForm)
                .then(function (r) {
                    if (r.data.code == '1') {
                        app.closeModal()
                        app.tableReload()
                    } else {
                        app.$Modal.error({content:'调用接口失败'})
                    }
                })
                .catch(function (e) {
                    app.$Modal.error({content:'调用接口失败'})
                })
            } else {
                axios.post(basePath + 'sys/role/update',app.editForm)
                .then(function (r) {
                    if (r.data.code == '1') {
                        app.closeModal()
                        app.tableReload()
                    } else {
                        app.$Modal.error({content:'调用接口失败'})
                    }
                })
                .catch(function (e) {
                    app.$Modal.error({content:'调用接口失败'})
                })
        }
    }
}