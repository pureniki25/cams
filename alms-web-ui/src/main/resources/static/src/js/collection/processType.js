let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    let typeId = getQueryStr("typeId")
    let table = layui.table 
    let laydate = layui.laydate
    let upload = layui.upload


    let validateRules = {
    
  
    	typeCode:[
            {required:true,message:'流程类别编码',trigger:'blur'}
        ],
        typeName:[
            {required:true,message:'类别名称',trigger:'blur'}
        ],
        canItemRunningCount:[
            {required:true,message:'单个业务该流程支持同时发起次数',trigger:'blur'}
        ],
        canItemTotalCount:[
            {required:true,message:'单个业务该流程支持发起总次',trigger:'blur'}
        ]

    }

    app = new Vue({
        el:"#app",
        data: {
   
            validateRules:validateRules,
            add_modal:false,
            editForm:{
            	typeCode:'',
            	typeName:'',
            	canItemRunningCount:'',
            	canItemTotalCount:''
            },
            submitLoading:false
    
        },
        methods:{
            openAddModal:function(){
                this.add_modal = true 
                this.$refs['editForm'].resetFields()
                console.log(this.editForm)
            },
        
            openEditModal:function(typeId){
                let _this = this 
                axios.get(basePath+'ProcessSetUpController/getProcessType', {params: {typeId: typeId}})
                .then(function (res) {debugger
                    if (res.data.code == "1") {
                        console.log(res)
                        data = res.data.data 
                        _this.editForm.typeId = data.typeId 
                        _this.editForm.typeCode = data.typeCode 
                        _this.editForm.typeName = data.typeName 
                        _this.editForm.canItemRunningCount = data.canItemRunningCount 
                        _this.editForm.canItemTotalCount = data.canItemTotalCount 
                
                        _this.add_modal = true 
                    } else {
                        app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                    }
                })
                .catch(function (error) {
                    app.$Modal.error({content: '接口调用异常!'});
                });
            },
      
            closeModal:function(){
                this.add_modal = false 
                this.$refs['editForm'].resetFields();
            },
            handleParams:function(){
                let o = {}
                let ef = this.editForm 
                if(ef.typeId){
                    o.typeId = ef.typeId
                }
                o.typeCode = ef.typeCode
                o.typeName = ef.typeName 
                o.canItemRunningCount = ef.canItemRunningCount 
                o.canItemTotalCount = ef.canItemTotalCount
                return o
            },
            saveRepayment:function(){
                let _this = this 
                
                
            

                this.$refs['editForm'].validate((valid)=>{
                    if(valid){
                        _this.submitLoading = true
                        axios.get(basePath+'ProcessSetUpController/save',{params:_this.handleParams()})
                        .then(function (res) {
                            if (res.data.code == "1") {
                                console.log(res)
                                _this.add_modal = false 
                                table.reload('processType',{
                                    where:{
                                    	typeId:typeId
                                    }
                                })
                            } else {
                                app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                            }
                            _this.submitLoading = false
                        })
                        .catch(function (error) {
                            app.$Modal.error({content: '接口调用异常!'});
                        });
                    }else {
                        _this.$Message.error({content: '表单校验失败!'});
                    }
                })

                
            },
            deleteRepayment:function(typeId){
                this.$Modal.confirm({content: '是否确认删除本条还款记录',loading:true,onOk:function(){
                    let _this = this 
                    axios.get(basePath+'ProcessSetUpController/del',{params:{typeId:typeId}})
                    .then(function (res) {
                        if (res.data.code == "1") {
                            console.log(res)
                            table.reload('processType',{
                                where:{
                                    typeId:typeId
                                }
                            })
                        } else {
                            app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                        }
                        _this.submitLoading = false
                    })
                    .catch(function (error) {
                        app.$Modal.error({content: '接口调用异常!'});
                    });
                    this.$Modal.remove()
                }});
            }
    
    
        }
       
    });

    table.render({
        elem: '#processType' //指定原始表格元素选择器（推荐id选择器）
        ,height: 600 //容器高度
        ,cols: [[
            {
                field:'typeCode',
                title:'流程类别编码'
            },{
                field:'typeName',
                title:'类别名称'
            },{
                field:'canItemRunningCount',
                title:'单个业务该流程支持同时发起次数'
            },{
                field:'canItemTotalCount',
                title:'单个业务该流程支持发起次数'
            },{
                title:'操作',
                toolbar:'#toolbar'
            }
        ]], //设置表头
        url: basePath + 'ProcessSetUpController/getProcessTypeList',
        where:{
            typeId:typeId
        },
        page:true,
        done:(res, curr, count)=>{
        }
      });

      table.on('tool(processType)',function(data){
            console.log(data)
            if(data.event=='del'){
                app.deleteRepayment(data.data.typeId)
            }else if(data.event=='edit'){
                app.openEditModal(data.data.typeId);
            }
      });


});