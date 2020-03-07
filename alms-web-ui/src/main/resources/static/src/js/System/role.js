let app
let dateStart
let dateEnd
let table
let tableIns
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

  tableIns = table.render({
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
                    }else  if(d.roleAreaType == 6){
                        return "根据财务跟单方式控制";
                    }else  if(d.roleAreaType == 7){
                        return "只能查看信用贷";
                    }else  if(d.roleAreaType == 8){
                        return "只能查看优房贷";
                    }else  if(d.roleAreaType == 9){
                        return "只查看自己审核";
                    }
                    else{
                        return "未定义的类型";
                    }
                }
            },
            {
                field: 'pageType',
                title: '页面名称',
                templet: function (d) {
                	var pageTypeName = ["合规化还款",
                		"贷后管理列表",
                		"车辆管理列表",
                		"减免管理列表",
                		"代扣管理列表",
                		"代扣查询列表",
                		"审批查询列表",
                		"展期管理列表",
                		"消息管理列表",
                		"财务管理列表"];
                	return pageTypeName[d.pageType]
                }
            },
            {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/role/page',
        page: true,
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
	schForm:{
        userId:'',
        userName:'',
        areaId:'',
        areaLevel:'',
    },
    editModal: {
        show: false,
        type: ''
    },
    editForm: {
        roleCode: '',
        roleName: '',
        roleAreaType: '',
        pageType:''
    },
}

let methods = {
    tableReload: function () {
        //table.reload('role')
    	let p = {}
        Object.keys(app.schForm).forEach(function(val,i){
            console.log(val)
            if(app.schForm[val]!=''){
                p[val]=app.schForm[val]
            }
        });
        tableIns.reload({
            where:p,
            page:{curr:1}
        })
    	
    },
    search:function(){
        app.tableReload()
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
            console.log(data);
            app.editForm.pageType = data.pageType + ''
            app.editForm.roleAreaType = data.roleAreaType + ''
            app.editForm.dataType = data.dataType + ''
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