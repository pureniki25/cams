let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    let businessId = getQueryStr("businessId")
    let afterId = getQueryStr("afterId")
    let table = layui.table 
    let laydate = layui.laydate
    let upload = layui.upload


    let validateRules = {
        repaymentDate:[
            {required:true,type:'date',pattern: /^([\d]{4})-([\d]{2})-([\d]{2})$/,message:'还款日期格式错误',trigger:'change'}
        ],
        repaymentMoney:[
            {type:'string',required:true,message:'还款金额不能为空',trigger:'blur'},
        ],
        realRepaymentUser:[
            {required:true,message:'实际还款人不能为空',trigger:'blur'}
        ],
        tradeType:[
            {required:true,message:'交易类型不能为空',trigger:'blur'}
        ],
        acceptBankU:[
            {required:true,message:'收款人不能为空',trigger:'blur'}
        ],
        acceptBank:[
            {required:true,message:'转入账号不能为空',trigger:'blur'}
        ]

    }

    app = new Vue({
        el:"#app",
        data: {
            DateOptions: {
                disabledDate(date) {
                    return date>new Date()
                }
            },
            validateRules:validateRules,
            add_modal:false,
            cert_modal:false,
            cert_modal_url:'',
            bank_account_list:[],
            editForm:{
                moneyPoolId:'',
                repaymentDate:'',
                repaymentMoney:'',
                realRepaymentUser:'',
                acceptBank:'',
                acceptBankU:'',
                acceptBankAccount:'',
                tradeType:'',
                tradePlace:'',
                certUrl:'',
                remark:''
            },
            submitLoading:false,
            upload:{
                url:basePath+"moneyPool/uploadCert",
                headers:{
                    app:axios.defaults.headers.common['app'],
                    Authorization:axios.defaults.headers.common['Authorization'],
                    userId:axios.defaults.headers.common['userId']
                },
                data:{
                    businessId:businessId
                },
                maxSize:1024*2,
                accept:'image/*'
            }
        },
        methods:{
            openAddModal:function(){
                this.add_modal = true 
                this.$refs['editForm'].resetFields()
                console.log(this.editForm)
            },
            beforeUpload:function(file){
                this.upload.data.fileName=file.name
            },onUploadSuccess:function(response, file, fileList){
                let docItemListJson = JSON.parse(response.docItemListJson )
                app.editForm.certUrl = docItemListJson.docUrl ;
            },onUploadError:function(error, file, fileList){
                app.$Message.error({content: '上传失败,请稍后再试'});
            },onExceededSize:function(file, fileList){
                app.$Message.error({content: '只能上传2MB以内的图片'});
            },onFormatError:function(file, fileList){
                app.$Message.error({content: '只能上传图片格式文件'});
            },
            openEditModal:function(mpid){
                let _this = this 
                axios.get(basePath+'moneyPool/get', {params: {moneyPoolId: mpid}})
                .then(function (res) {
                    if (res.data.code == "1") {
                        console.log(res)
                        data = res.data.data 
                        _this.editForm.moneyPoolId = data.id 
                        _this.editForm.repaymentDate = data.repaymentDate 
                        _this.editForm.repaymentMoney = data.repaymentMoney 
                        _this.editForm.realRepaymentUser = data.realRepaymentUser 
                        _this.editForm.acceptBank = data.acceptBank 
                        _this.editForm.tradeType = data.tradeType 
                        if(data.tradeType=='现金'){
                            _this.editForm.acceptBankU = data.acceptBank 
                        }else{
                            _this.editForm.acceptBank = data.acceptBank
                            $.each(_this.bank_account_list,function(i,o){
                                if(o.financeName==data.acceptBank){
                                    $("#acceptBankAccount input").attr('disabled',false).val(o.repaymentId).attr('disabled',true)
                                    return false
                                }
                            })
                        }
                        _this.editForm.tradePlace = data.tradePlace 
                        _this.editForm.certUrl = data.certUrl 
                        _this.editForm.remark = data.remark 

                        _this.add_modal = true 
                    } else {
                        app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                    }
                })
                .catch(function (error) {
                    app.$Modal.error({content: '接口调用异常!'});
                });
            },
            openCertModal:function(url){
                if(url&&url.length>0){
                    this.cert_modal = true
                    this.cert_modal_url = 'http://xiaodaioa.oss-cn-beijing.aliyuncs.com/'+url
                }else{
                    this.cert_modal = false
                    this.cert_modal_url = ''
                }
            },
            closeModal:function(){
                this.add_modal = false 
                this.$refs['editForm'].resetFields();
            },
            handleParams:function(){
                let o = {}
                let ef = this.editForm 
                if(ef.moneyPoolId){
                    o.moneyPoolId = ef.moneyPoolId
                }
                o.repaymentDate = ef.repaymentDate
                o.repaymentMoney = ef.repaymentMoney 
                o.factRepaymentUser = ef.realRepaymentUser 
                o.tradePlace = ef.tradePlace
                o.tradeType = ef.tradeType
                o.cert = ef.certUrl
                o.remark = ef.remark
                o.businessId = businessId 
                o.afterId = afterId 
                if(ef.tradeType=='现金'){
                    o.acceptBank = ef.acceptBankU
                }else if(ef.tradeType=='转账'||ef.tradeType=='刷卡'){
                    o.acceptBank = ef.acceptBank
                }else{
                    return ;
                }
                return o
            },
            saveRepayment:function(){
                let _this = this 
                
                if(this.editForm.tradeType=='现金'){
                    delete validateRules.acceptBank
                    validateRules.acceptBankU = [
                        {required:true,message:'收款人不能为空',trigger:'blur'}
                    ]
                }else {
                    delete validateRules.acceptBankU
                    validateRules.acceptBank = [
                        {required:true,message:'转入账号不能为空',trigger:'blur'}
                    ]
                }
                _this.validateRules = validateRules

                this.$refs['editForm'].validate((valid)=>{
                    if(valid){
                        _this.submitLoading = true
                        axios.get(basePath+'moneyPool/save',{params:_this.handleParams()})
                        .then(function (res) {
                            if (res.data.code == "1") {
                                console.log(res)
                                _this.add_modal = false 
                                table.reload('moneyPool',{
                                    where:{
                                        businessId:businessId,
                                        afterId:afterId
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
            deleteRepayment:function(mpid){
                this.$Modal.confirm({content: '是否确认删除本条还款记录',loading:true,onOk:function(){
                    let _this = this 
                    axios.get(basePath+'moneyPool/del',{params:{moneyPoolId:mpid}})
                    .then(function (res) {
                        if (res.data.code == "1") {
                            console.log(res)
                            table.reload('moneyPool',{
                                where:{
                                    businessId:businessId,
                                    afterId:afterId
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
            listDepartmentBank:function(){
                axios.get(basePath+'moneyPool/listDepartmentBank',{params:{businessId:businessId}})
                .then(function(res){
                    if(res.data.code=='1'){
                        app.bank_account_list = res.data.data
                    }else{
                        app.$Modal.error({content:'接口调用异常'})
                    }
                })
                .catch(function(error){
                    app.$Modal.error({content:'接口调用异常'})
                })
            },
            onDepartmentBankChange:function(cb){
                let _this = this
                $.each(_this.bank_account_list,function(i,o){
                    if(o.financeName==cb){
                        _this.editForm.acceptBankAccount=o.repaymentId
                        return false
                    }
                })
                console.log(cb)
            },
            onTradeTypeChange:function(cb){
                if(cb=='现金'){
                    this.editForm.acceptBankAccount=''
                    this.editForm.acceptBank=''
                    // $('#acceptBank .ivu-select-selected-value').text('')
                }else{
                    this.editForm.acceptBankU=''
                }
            }
        },
        created:function(){
           this.listDepartmentBank()
        }
    });

    table.render({
        elem: '#moneyPool' //指定原始表格元素选择器（推荐id选择器）
        ,height: 600 //容器高度
        ,cols: [[
            {
                field:'id',
                title:'编号'
            },{
                field:'repaymentDate',
                title:'还款日期'
            },{
                field:'repaymentMoney',
                title:'还款金额'
            },{
                field:'realRepaymentUser',
                title:'实际转款人'
            },{
                field:'tradeType',
                title:'交易类型'
            },{
                field:'tradePlace',
                title:'交易场所'
            },{
                field:'acceptBank',
                title:'转入账号'
            },{
                field:'status',
                title:'状态'
            },{
                title:'操作',
                toolbar:'#toolbar'
            }
        ]], //设置表头
        url: basePath + 'moneyPool/page',
        where:{
            businessId:businessId,
            afterId:afterId
        },
        page:true,
        done:(res, curr, count)=>{
        }
      });

      table.on('tool(moneyPool)',function(data){
            console.log(data)
            if(data.event=='del'){
                app.deleteRepayment(data.data.id)
            }else if(data.event=='edit'){
                app.openEditModal(data.data.id);
            }else if(data.event=='checkCert'){
                app.openCertModal(data.data.certUrl)
            }
      });

      laydate.render({
        elem: '#repaymentDate', //指定元素,
        max:0
      });

      upload.render({
        elem: '#certUrl' //绑定元素
        ,url: '/upload/' //上传接口
        ,done: function(res){
          //上传完毕回调
        }
        ,error: function(){
          //请求异常回调
        }
      });
});