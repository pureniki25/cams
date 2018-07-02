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
        elem: "#userarea",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'orgCode',
                title: '所在公司'
            }, {
                field: 'userId',
                title: 'userId',
            }, {
                field: 'userName',
                title: '姓名'
            }, {
                field: 'areaName',
                title: '管理公司/区域名',
                
            }, {
                field: 'areaId',
                title: '管理公司/区域名id'
            }, {
                field: 'areaLevel',
                title: '类型',
                templet: function (d) {
                    return d.areaLevel == '40' ? '区域' : '公司';
                }
            }, {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/userarea/page',
        page: true,
        done: (res, curr, count) => { }
    })

    table.on('tool(userarea)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'delete') {
            app.delete(data)
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
    addSysUserAreaModal:{
        show:false
    },
    addForm :{
        userId:'',
        areaLevel:'',
        areaId:'',
        areaList:[]
    }
}

let methods = {
    search:function(){
        app.tableReload()
    },
    delete:function(data){
        axios.post(basePath+'sys/userarea/del',data)
        .then(function(res){
            if(res.data.code=='1'){
                app.tableReload()
            }else{
                app.$Modal.error({content:'调用sys/userarea/del接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用sys/userarea/del接口失败'})
        })
    },
    tableReload:function(){
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
    listArea:function(){
        axios.get(basePath+'sys/userarea/listArea',{params:{areaLevel:app.addForm.areaLevel}})
        .then(function(res){
            if(res.data.code=='1'){
                app.addForm.areaList = res.data.data
            }else{
                app.$Modal.error({content:'调用sys/userarea/del接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用sys/userarea/del接口失败'})
        })
    },
    openAddModal:function(){
        app.$refs['addForm'].resetFields()
        app.addForm.areaList = []
        app.addForm.areaLevel = ''
        app.addForm.userId = ''
        app.addSysUserAreaModal.show = true
    },
    add:function(){
        let p = {}
        p.userId = app.addForm.userId
        p.sequence = app.addForm.areaLevel
        p.orgCode = app.addForm.areaId
        if(!p.userId){
            app.$Modal.error("新增的userId不能为空")
            return
        }
        if(!p.sequence){
            app.$Modal.error("新增的sequence不能为空")
            return
        }
        if(!p.orgCode){
            app.$Modal.error("新增的sequence不能为空")
            return
        }
        axios.post(basePath+'sys/userarea/add',p)
        .then(function(res){
            if(res.data.code=='1'){
                app.tableReload()
                app.addSysUserAreaModal.show = false
            }else{
                app.$Modal.error({content:'调用sys/userarea/del接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用sys/userarea/del接口失败'})
        })
    }
}