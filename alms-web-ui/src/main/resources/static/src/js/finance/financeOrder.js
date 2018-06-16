/**
 * 财务跟单管理 js
 * @author zgh
 */
var layer, table, basePath, app;

//从后台获取下拉框数据
var getSelectsData = function () {
    //取区域列表
    axios.get(basePath + 'finance/getOrderSetSearchInfo')
        .then(function (res) {
            if (res.data.code == "1") {
                app.areaList = res.data.data.area;
                app.companyList = res.data.data.company;
                app.businessTypeList = res.data.data.businessType;
            } else {
                app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            app.$Modal.error({content: '接口调用异常!'});
        });
}



window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.financeBasePath
    table = layui.table
    let laydate = layui.laydate

    app = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () { }

    })
    getSelectsData();

    table.render({
        elem: "#financialOrder",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'areaName',
                title: '区域'
            }, {
                field: 'companyName',
                title: '分公司',
            }, {
                field: 'businessType',
                title: '业务类型'
            }, {
                field: 'userName',
                title: '出纳',
                
            },{
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'finance/getOrderSetPage',
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
    loading:false,
    searchForm:{
        userId:'',
        userName:'',
        areaId:'',
        areaLevel:'',
    },
    addFinancialOrderModal:{
        show:false
    },
    addFinancialOrderForm :{

        userId:'',
        areaLevel:'',
        areaId:'',
        areaList:[]
    },
    areaList:[],
    companyList:[],
    businessTypeList:[],
    ruleValidate:setSearchFormValidate, //表单验证

}

//查询表单验证
var setSearchFormValidate = {
    derateMoneyBegin: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if(app.searchForm.derateMoneyBegin!=""&& app.searchForm.derateMoneyEnd!=""){
                    if (parseInt(app.searchForm.derateMoneyBegin) > parseInt(app.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        app.searchForm.derateMoneyEnd = app.searchForm.derateMoneyEnd;
                        callback();//校验通过
                    }
                }else{
                    app.searchForm.derateMoneyEnd =app.searchForm.derateMoneyEnd;
                    callback();
                }

            }, trigger: 'blur'
        }
    ],
    derateMoneyEnd: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if(app.searchForm.derateMoneyBegin!=""&& app.searchForm.derateMoneyEnd!=""){
                    if (parseInt(app.searchForm.derateMoneyBegin) > parseInt(app.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        callback();//校验通过
                    }
                }else{
                    callback();
                }

            }, trigger: 'blur'
        }
    ]
};






let methods = {
    toLoading:function(){},
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
        Object.keys(app.schForm).forEach(function(val, i){
            console.log(val)
            if(app.schForm[val]!=''){
                p[val]=app.schForm[val]
            }
        });
        table.reload('userarea',{
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