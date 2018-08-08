let app
//从后台获取下拉框数据
var getSelectData = function () {

    //取区域列表
    axios.get(basePath +'ProcessSetUpController/getSelectData')
        .then(function (res) {
            if (res.data.code == "1") {
            	app.roleList = res.data.data.roleList;
                app.processTypeList = res.data.data.processTypeList;
                app.userList=res.data.data.userList;
            } else {
                app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            app.$Modal.error({content: '接口调用异常!'});
        });
}
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    let typeId = getQueryStr("typeId");
    let typeStepId = getQueryStr("typeStepId")
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
    getSelectData();
    app = new Vue({
        el:"#app",
        data: {
   
            validateRules:validateRules,
            add_modal:false,
            add_modal_step:false,
            editForm:{
            	typeCode:'',
            	typeName:'',
            	canItemRunningCount:'',
            	canItemTotalCount:''
            },
            editStepForm:{
            	typeId:'',
            	typeStepId:'',
            	stepName:'',
            	approveUserId:'',
            	approveUserIdSelectSql:'',
            	isCanEdit:'',
            	step:'',
            	nextStep:'',
            	nextStepSelectSql:'',
            	stepType:'',
            	approveUserRole:''
                
            },
            searchForm:{
              typeId:'',
              stepName:''
            },
            processTypeList:[],
            userList:[],
            roleList:[],
            sqlShowFlage:false,
            roleShowFlage:false,
            submitLoading:false
    
        },
        methods:{
        	 toLoading() {
                 if (table == null) return;
                 app.$refs['searchForm'].validate((valid) =>{
                     if (valid) {
                         console.log(app.searchForm);


                         table.reload('processTypeStep', {
                             where: {
                                 typeId:app.searchForm.typeId,  
                                 stepName:app.searchForm.stepName
                             }
                             , page: {
                                 curr: 1 //重新从第 1 页开始
                             }
                         });

                     }

                     // this.loading = false;
                 })
             }
                 ,
            openAddModal:function(){
                this.add_modal = true 
                this.$refs['editForm'].resetFields()
                console.log(this.editForm)
            },
            openAddModalStep:function(){
            	getSelectData();
                this.add_modal_step = true 
                this.$refs['editStepForm'].resetFields()
                console.log(this.editStepForm)
            },
        
            openEditModal:function(typeId){
                let _this = this 
                axios.get(basePath+'ProcessSetUpController/getProcessType', {params: {typeId: typeId}})
                .then(function (res) {
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
            
            openEditStepModal:function(typeStepId){
            	getSelectData();
                let _this = this 
                axios.get(basePath+'ProcessSetUpController/getProcessTypeStep', {params: {typeStepId: typeStepId}})
                .then(function (res) {
                    if (res.data.code == "1") {
                        console.log(res)
                        data = res.data.data, 
                        _this.editStepForm.typeId = data.typeId, 
                        _this.editStepForm.typeStepId = data.typeStepId, 
                        _this.editStepForm.stepName = data.stepName, 
                        _this.editStepForm.approveUserId = data.approveUserId+'', 
                        _this.editStepForm.approveUserIdSelectSql = data.approveUserIdSelectSql, 
                        _this.editStepForm.isCanEdit = data.isCanEdit+'', 
                        _this.editStepForm.step = data.step, 
                        _this.editStepForm.nextStep = data.nextStep, 
                        _this.editStepForm.nextStepSelectSql = data.nextStepSelectSql, 
                        _this.editStepForm.stepType = data.stepType+'', 
                        _this.editStepForm.approveUserRole = data.approveUserRole, 
                        _this.editStepForm.approveUserType = data.approveUserType+'',  
                        _this.add_modal_step = true 
                    } else {
                        app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                    }
                })
                .catch(function (error) {
                    app.$Modal.error({content: '接口调用异常!'});
                });
            },
            
            listUserType:function(){
            	 
                if(app.editStepForm.approveUserType==3){
                	app.sqlShowFlage=true,
                	app.roleShowFlage=false
                }else if(app.editStepForm.approveUserType==5){
                	app.roleShowFlage=true,
                	app.sqlShowFlage=false
	            }else{
	            	app.roleShowFlage=false,
	            	app.sqlShowFlage=false
	            }
            },
      
            closeModal:function(){
                this.add_modal = false 
                this.$refs['editForm'].resetFields();
            },
            
            closeStepModal:function(){
                this.add_modal_step = false 
                this.$refs['editStepForm'].resetFields();
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
            stepParams:function(){
                let o = {}
                let ef = this.editStepForm 
                o.typeStepId = ef.typeStepId
                o.typeId = ef.typeId
                o.stepName = ef.stepName 
                o.approveUserId = ef.approveUserId 
                o.approveUserType = ef.approveUserType
                o.approveUserIdSelectSql = ef.approveUserIdSelectSql
                o.isCanEdit = ef.isCanEdit
                o.step = ef.step
                o.nextStep = ef.nextStep
                o.nextStepSelectSql = ef.nextStepSelectSql
                o.stepType = ef.stepType
                o.approveUserRole = ef.approveUserRole
                return o
            },
            saveRepayment:function(){
                let _this = this 
                        _this.submitLoading = true
                        axios.get(basePath+'ProcessSetUpController/save',{params:_this.handleParams()})
                        .then(function (res) {
                            if (res.data.code == "1") {
                                console.log(res)
                                _this.add_modal = false 
                           	 app.$Modal.success({
                                 title: '',
                                 content: '操作成功'
                             });
                                table.reload('processType',{
                                	   page: {
   	                                    curr: 1 //重新从第 1 页开始
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
                 
              

                
            },
            saveStep:function(){
                let _this = this 
                this.$refs['editStepForm'].validate((valid)=>{
                    if(valid){
                        _this.submitLoading = true
                        axios.get(basePath+'ProcessSetUpController/saveStep',{params:_this.stepParams()})
                        .then(function (res) {
                            if (res.data.code == "1") {
                                console.log(res)
                                _this.add_modal_step = false 

                           	 app.$Modal.success({
                                    title: '',
                                    content: '操作成功'
                                });
	                           table.reload('processTypeStep', {
	                                page: {
	                                    curr: 1 //重新从第 1 页开始
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
                              	 app.$Modal.success({
                                    title: '',
                                    content: '操作成功'
                                });
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
            },
            
            deleteStep:function(stepId){
                this.$Modal.confirm({content: '是否确认删除本条还款记录',loading:true,onOk:function(){
                    let _this = this 
                    axios.get(basePath+'ProcessSetUpController/delStep',{params:{stepId:stepId}})
                    .then(function (res) {
                        if (res.data.code == "1") {
                            console.log(res)
                            	 app.$Modal.success({
                                    title: '',
                                    content: '操作成功'
                                });
                            table.reload('processTypeStep',{
                                where:{
                                	stepId:stepId
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
      
      
      
      
      
      table.render({
          elem: '#processTypeStep' //指定原始表格元素选择器（推荐id选择器）
          , id: 'processTypeStep'

          , cols: [[
              {
                  field: 'typeName',
                  width: 200,
                  title: '流程类型名称'
  
              },

              {
                  field: 'stepName',
                  width: 200,
                  title: '流程步骤名称'
              
              }, {
                  field: 'approveUserName',
                  title: '审核人名称',
                  width: 100,
              }, {
                  field: 'approveUserTypeName',
                  title: '审核人类型',
                  width: 100,
                  align:'center',

              }, {
                  field: 'approveUserIdSelectSql',
                  title: '动态SQL获取',
                  width: 220,
                  align:'center',

              }, {
                  field: 'isCanEditName',
                  title: '是否允许编辑',
                  width: 120,
                  align:'center',

              }, {
                  field: 'step',
                  title: '所在步骤',
                  width: 100,
                  align:'center',

              },  {
                  field: 'nextStep',
                  title: '下一步节点',
                  width: 100,
                  align:'center',

              }, {
                  field: 'stepTypeName',
                  title: '步骤类型',
                  width: 100,
                  align:'center',

              },{
                  field: 'approveUserRole',
                  title: '审批者角色定义',
                  width: 220,
                  align:'center',

              },{
                  fixed: 'right',
                  title: '操作',
                  width: 200,
                  align: 'center',
                  toolbar: '#toolbar2'
              }

          ]], //设置表头
          url: basePath + 'ProcessSetUpController/getProcessStepList',
          where:{
              typeStepId:typeStepId
          },
          page: true,
          done: function (res, curr, count) {
              //数据渲染完的回调。你可以借此做一些其它的操作
              //如果是异步请求数据方式，res即为你接口返回的信息。
              //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
              self.loading = false;
          }
      });

      table.on('tool(processTypeStep)',function(data){
          console.log(data)
          if(data.event=='del'){
              app.deleteStep(data.data.typeStepId)
          }else if(data.event=='edit' && data.data.isCanEditName=='允许'){
              app.openEditStepModal(data.data.typeStepId);
          }
    });
    


});