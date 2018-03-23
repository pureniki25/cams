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
        repaymentDate: [
            { required: true, type: 'date', pattern: /^([\d]{4})-([\d]{2})-([\d]{2})$/, message: '还款日期格式错误', trigger: 'change' }
        ],
        repaymentMoney: [
            // {  required: true, message: '还款金额不能为空', trigger: 'blur' },
            { pattern: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^(0){1}$)|(^\d\.\d{1,2}?$)/, message: '还款金额格式不正确', trigger: 'blur' },
        ],
        realRepaymentUser: [
            { required: true, message: '实际还款人不能为空', trigger: 'blur' },
            { pattern: /^[\u4e00-\u9fa5]{1,20}$/, message: '实际还款人应为最长20个汉字', trigger: 'blur' }
        ],
        tradeType: [
            { required: true, message: '交易类型不能为空', trigger: 'blur' }
        ],
        acceptBankU: [
            { required: true, message: '收款人不能为空', trigger: 'blur' },
            { pattern: /^[\u4e00-\u9fa5]{1,20}$/, message: '收款人应为最长20个汉字', trigger: 'blur' }
        ],
        acceptBank: [
            { required: true, message: '转入账号不能为空', trigger: 'blur' }
        ],
        remark:[
            {max: 500,message:'备注不能超过500字', trigger: 'blur'}
        ],
        tradePlace:[
            {max: 50,message:'交易场所不能超过50字', trigger: 'blur'}
        ]
    }

    app = new Vue({
        el: "#app",
        data: {
            DateOptions: {
                disabledDate(date) {
                    return date > new Date()
                }
            },
            validateRules: validateRules,
            add_modal: false,
            cert_modal: false,
            cert_modal_url: '',
            bank_account_list: [],
            editForm: {
                moneyPoolId: '',
                repaymentDate: '',
                repaymentMoney: 0,
                realRepaymentUser: '',
                acceptBank: '',
                acceptBankU: '',
                acceptBankAccount: '',
                tradeType: '',
                tradePlace: '',
                certUrl: '',
                remark: ''
            },
            submitLoading: false,
            upload: {
                url: basePath + "moneyPool/uploadCert",
                headers: {
                    app: axios.defaults.headers.common['app'],
                    Authorization: axios.defaults.headers.common['Authorization'],
                    userId: axios.defaults.headers.common['userId']
                },
                data: {
                    businessId: businessId
                },
                maxSize: 1024 * 2,
                accept: 'image/*'
            }
        },
        watch:{
            editForm:{
                handler(c,o){
                },
                deep:true
            }
        },
        methods: {
            openAddModal: function () {
                this.add_modal = true
                this.addmodel = true 
                this.$refs['editForm'].resetFields()
                this.$refs['upload'].clearFiles()
                this.editForm =  {
                    moneyPoolId: '',
                    repaymentDate: '',
                    repaymentMoney: 0,
                    realRepaymentUser: '',
                    acceptBank: '',
                    acceptBankU: '',
                    acceptBankAccount: '',
                    tradeType: '',
                    tradePlace: '',
                    certUrl: '',
                    remark: ''
                }
                console.log(this.editForm)
            },
            beforeUpload: function (file) {
                this.upload.data.fileName = file.name
            }, onUploadSuccess: function (response, file, fileList) {
                let docItemListJson = JSON.parse(response.docItemListJson)
                app.editForm.certUrl = docItemListJson.docUrl;
            }, onUploadError: function (error, file, fileList) {
                app.$Message.error({ content: '上传失败,请稍后再试' });
            }, onExceededSize: function (file, fileList) {
                app.$Message.error({ content: '只能上传2MB以内的图片' });
            }, onFormatError: function (file, fileList) {
                app.$Message.error({ content: '只能上传图片格式文件' });
            },
            openEditModal: function (mpid) {
                let _this = this
                if(mpid){
                    app.addmodel = false 
                    axios.get(basePath + 'moneyPool/getCustomerRepayment', { params: { id: mpid } })
                    .then(function (res) {
                        if (res.data.code == "1") {
                            console.log(res)
                            data = res.data.data
                            _this.editForm.moneyPoolId = data.id
                            _this.editForm.repaymentDate = data.tradeDate
                            _this.editForm.repaymentMoney = data.accountMoney
                            _this.editForm.realRepaymentUser = data.factTransferName
                            _this.editForm.tradeType = data.tradeType
                            if (data.tradeType == '现金') {
                                _this.editForm.acceptBankU = data.bankAccount
                            } else {
                                _this.editForm.acceptBank = data.bankAccount
                                $.each(_this.bank_account_list, function (i, o) {
                                    if (o.financeName == data.bankAccount) {
                                        _this.editForm.acceptBankAccount = o.repaymentId
                                        // $("#acceptBankAccount input").attr('disabled', false).val(o.repaymentId).attr('disabled', true)
                                        return false
                                    }
                                })
                            }
                            _this.editForm.tradePlace = data.tradePlace
                            _this.editForm.certUrl = data.certificatePictureUrl
                            _this.editForm.remark = data.remark

                            _this.add_modal = true
                        } else {
                            app.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                        }
                    })
                    .catch(function (error) {
                        app.$Modal.error({ content: '接口调用异常!' });
                    });
                }else{
                    app.addmodel = true 
                }
                
            },
            openCertModal: function (url) {
                if (url && url.length > 0) {
                    this.cert_modal = true
                    this.cert_modal_url = 'http://xiaodaioa.oss-cn-beijing.aliyuncs.com/' + url
                } else {
                    this.$Modal.warning({content:'此登记没有上传凭证'})
                    this.cert_modal = false
                    this.cert_modal_url = ''
                }
            },
            closeModal: function () {
                this.add_modal = false
                this.$refs['editForm'].resetFields();
            },
            handleParams: function () {
                let o = {}
                let ef = this.editForm
                if (ef.moneyPoolId) {
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
                if (ef.tradeType == '现金') {
                    o.acceptBank = ef.acceptBankU
                } else if (ef.tradeType == '转账' || ef.tradeType == '刷卡') {
                    o.acceptBank = ef.acceptBank
                } else {
                    return;
                }
                return o
            },
            saveRepayment: function () {
                let _this = this

                if (this.editForm.tradeType == '现金') {
                    delete validateRules.acceptBank
                    validateRules.acceptBankU = [
                        { required: true, message: '收款人不能为空', trigger: 'blur' },
                        { pattern: /^[\u4e00-\u9fa5]{1,20}$/, message: '收款人应为最长20个汉字', trigger: 'blur' }
                    ]
                } else {
                    delete validateRules.acceptBankU
                    validateRules.acceptBank = [
                        { required: true, message: '转入账号不能为空', trigger: 'blur' },
                        // { pattern: /^[\u4e00-\u9fa5]{1,20}$/, message: '转入账号应为最长20个汉字', trigger: 'blur' }
                    ]
                }
                _this.validateRules = validateRules

                this.$refs['editForm'].validate((valid) => {
                    if (valid) {
                        _this.submitLoading = true


                        if(_this.addmodel){
                            axios.post(basePath + 'moneyPool/addCustomerRepayment', _this.handleParams())
                            .then(function (res) {
                                if (res.data.code == "1") {
                                    console.log(res)
                                    _this.add_modal = false
                                    table.reload('moneyPool', {
                                        where: {
                                            businessId: businessId,
                                            afterId: afterId
                                        }
                                    })
                                } else {
                                    app.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                                }
                                _this.submitLoading = false
                            })
                            .catch(function (error) {
                                app.$Modal.error({ content: '接口调用异常!' });
                            });
                        }else{
                            axios.post(basePath + 'moneyPool/updateCustomerRepayment', _this.handleParams())
                            .then(function (res) {
                                if (res.data.code == "1") {
                                    console.log(res)
                                    _this.add_modal = false
                                    table.reload('moneyPool', {
                                        where: {
                                            businessId: businessId,
                                            afterId: afterId
                                        }
                                    })
                                } else {
                                    app.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                                }
                                _this.submitLoading = false
                            })
                            .catch(function (error) {
                                app.$Modal.error({ content: '接口调用异常!' });
                            });
                        }
                        
                    } else {
                        _this.$Message.error({ content: '表单校验失败!' });
                    }
                })


            },
            deleteRepayment: function (mpid) {
                this.$Modal.confirm({
                    content: '是否确认删除本条还款记录', onOk: function () {
                        let _this = this
                        axios.post(basePath + 'moneyPool/deleteCustomerRepayment', { moneyPoolId: mpid })
                            .then(function (res) {
                                if (res.data.code == "1") {
                                    console.log(res)
                                    
                                } else {
                                    app.$Message.error({ content: '操作失败，消息：' + res.data.msg ,duration:0,closable:true});
                                }
                                table.reload('moneyPool', {
                                    where: {
                                        businessId: businessId,
                                        afterId: afterId
                                    }
                                })
                            })
                            .catch(function (error) {
                                app.$Message.error({ content: '接口调用异常!',duration:0,closable:true });
                            });
                    }
                });
            },
            listDepartmentBank: function () {
                axios.get(basePath + 'moneyPool/listDepartmentBank', { params: { businessId: businessId } })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.bank_account_list = res.data.data
                        } else {
                            app.$Modal.error({ content: '接口调用异常' })
                        }
                    })
                    .catch(function (error) {
                        app.$Modal.error({ content: '接口调用异常' })
                    })
            },
            onDepartmentBankChange: function (cb) {
                 let _this = this
                $.each(_this.bank_account_list, function (i, o) {
                    if (o.financeName == cb) {
                        _this.editForm.acceptBankAccount = o.repaymentId
                        return false
                    }
                }) 
                console.log(cb)
            },
            onTradeTypeChange: function (cb) {
                if (cb == '现金') {
                    this.editForm.acceptBankAccount = ''
                    this.editForm.acceptBank = ''
                    // $('#acceptBank .ivu-select-selected-value').text('')
                } else {
                    this.editForm.acceptBankU = ''
                }
            },
            addCustomerRepayment:function(){
            }
        },
        created: function () {
            this.listDepartmentBank()
        }
    });

    table.render({
        elem: '#moneyPool' //指定原始表格元素选择器（推荐id选择器）
        , height: 600 //容器高度
        , cols: [[
            {
                field: 'id',
                title: '编号'
            }, {
                field: 'tradeDate',
                title: '还款日期',
                width: 110
            }, {
                field: 'accountMoney',
                title: '还款金额'
            }, {
                field: 'factTransferName',
                title: '实际转款人'
            }, {
                field: 'tradeType',
                title: '交易类型'
            }, {
                field: 'tradePlace',
                title: '交易场所',
                width: 150
            }, {
                field: 'bankAccount',
                title: '转入账号'
            }, {
                field: 'state',
                title: '状态',
                width: 150
            }, {
                title: '操作',
                toolbar: '#toolbar',
                fixed: "right"
            }
        ]], //设置表头
        url: basePath + 'moneyPool/pageCustomerRepayment',
        where: {
            businessId: businessId,
            afterId: afterId
        },
        page: true,
        done: (res, curr, count) => {
        }
    });

    table.on('tool(moneyPool)', function (data) {
        console.log(data)
        if (data.event == 'del') {
            app.deleteRepayment(data.data.id)
        } else if (data.event == 'edit') {
            app.openEditModal(data.data.id);
        } else if (data.event == 'checkCert') {
            app.openCertModal(data.data.certUrl)
        }
    });

    laydate.render({
        elem: '#repaymentDate', //指定元素,
        max: 0
    });

    upload.render({
        elem: '#certUrl' //绑定元素
        , url: '/upload/' //上传接口
        , done: function (res) {
            //上传完毕回调
        }
        , error: function () {
            //请求异常回调
        }
    });
});