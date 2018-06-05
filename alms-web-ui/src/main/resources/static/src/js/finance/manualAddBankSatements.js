/**
 * 
 */

let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;

    let businessId = getQueryStr('businessId')
    let afterId = getQueryStr('afterId')
    let mprid = getQueryStr('mprid')

    app = new Vue({
        el: "#app",
        data: {
            form: {
                repaymentDate: '',
                tradeType: '',
                repaymentMoney: 0,
                acceptBank: '',
                cert:'',
                remark:''
            },
            ruleValidate: {
                repaymentDate: [
                    { required: true, message: '还款日期不能为空', trigger: 'blur' }
                ],
                repaymentMoney: [
                    { required: true, message: '还款金额不能为空', trigger: 'blur' },
                ],
                tradeType: [
                    { required: true, message: '支付类型不能为空', trigger: 'blur' }
                ],
                acceptBank: [
                    { required: true, message: '', trigger: 'blur' }
                ],
            },
            acceptAccountLabel: '',
            upload: {
                url: cpath + "moneyPool/uploadCert",
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
            },
            bankAccountList:[],
            curBankAccount:''
        },
        methods: {
            computeAcceptAccountLabel: function () {
                let res;
                switch (this.form.tradeType) {
                    case '转账':
                        res = '转入账户'
                        break;
                    case '现金':
                        res = '收款人'
                        break;
                    case '刷卡':
                        res = '转入账户'
                        break;
                    default:
                        res = '请选择支付类型'
                        break;
                }
                this.acceptAccountLabel = res;
            },
            beforeUpload: function (file) {
                this.upload.data.fileName = file.name
            }, onUploadSuccess: function (response, file, fileList) {
                let docItemListJson = JSON.parse(response.docItemListJson)
                app.form.cert = docItemListJson.docUrl;
            }, onUploadError: function (error, file, fileList) {
                app.$Message.error({ content: '上传失败,请稍后再试' });
            }, onExceededSize: function (file, fileList) {
                app.$Message.error({ content: '只能上传2MB以内的图片' });
            }, onFormatError: function (file, fileList) {
                app.$Message.error({ content: '只能上传图片格式文件' });
            },
            listDepartmentBank:function(){
                axios.get(cpath+'moneyPool/listDepartmentBank',{params:{
                    businessId:businessId
                }}).then(function(res){
                    if(res.data.code=='1'){
                        app.bankAccountList = res.data.data
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                }).catch(function(err){
                    app.$Message.error({content:'获取银行转入账户数据失败'})
                })
            },
            submit:function(){
                let params = {}
                Object.keys(this.form).forEach(element => {
                    if (this.form[element] &&
                        (this.form[element] != '' || this.form[element].length != 0)) {
                        params[element] = this.form[element]
                    }
                })
                params.businessId = businessId;
                params.afterId = afterId;
                if(mprid){
                    params.mprid = mprid
                }
                if(!params.repaymentDate){
                    app.$Message.error({content:'还款日期不能为空'})
                    return ;
                }
                if(typeof params.repaymentDate!='string'){
                    params.repaymentDate = moment(params.repaymentDate).format('YYYY-MM-DD')
                }
                if(!/^(\d{4})-(\d{2})-(\d{2})$/.test(params.repaymentDate)){
                    app.$Message.error({content:'还款日期格式错误'})
                    return ;
                }
                if(!params.repaymentMoney||params.repaymentMoney==0){
                    app.$Message.error({content:'转账金额不能为0'})
                    return ;
                }
                if(!params.tradeType){
                    app.$Message.error({content:'交易类型不能为空'})
                    return ;
                }
                if(!params.acceptBank){
                    app.$Message.error({content:app.acceptAccountLabel+'不能为空'})
                    return ;
                }
                if(!params.factRepaymentUser){
                    app.$Message.error({content:(params.tradeType=='转账'?'实际转款人':'支付人')+'不能为空'})
                    return ;
                }
                let msg = mprid?"确认编辑此流水?":"确认新增此流水?"
                let url = mprid?"确认编辑此流水?":"确认新增此流水?"
                app.$Modal.confirm({
                    content:msg,
                    onOk(){
                        axios.post(fpath+'finance/appointBankStatement',params)
                        .then(function(res){
                            if(res.data.code=='1'){
                                parent.location.reload()
                                app.cancel()
                            }else{
                                app.$Message.error({content:res.data.msg})
                            }
                        })
                        .catch(function(err){
                            app.$Message.error({content:'提交还款登记失败'})
                        })
                    }
                })
            },
            cancel:function(){
                app.$refs['form'].resetFields()
                //当你在iframe页面关闭自身时
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭 
                // window.parent.app.closeModal('manualAddBankSatementsShow')
            },
            getMoneyPool(){
                axios.get(cpath + 'moneyPool/getCustomerRepayment', { params: { id: mprid } })
                    .then(function (res) {
                        if (res.data.code == "1") {

                            data = res.data.data
                            app.form.moneyPoolId = data.id
                            app.form.repaymentDate = data.tradeDate
                            app.form.repaymentMoney = data.accountMoney
                            app.form.realRepaymentUser = data.factTransferName
                            app.form.tradeType = data.tradeType
                            app.form.factRepaymentUser = data.factTransferName
                            if (data.tradeType == '现金') {
                                app.form.acceptBank = data.bankAccount
                            } else {
                                app.form.acceptBank = data.bankAccount
                                $.each(app.bankAccountList, function (i, o) {
                                    if (o.financeName == data.bankAccount) {
                                        app.curBankAccount = o.repaymentId
                                        // $("#acceptBankAccount input").attr('disabled', false).val(o.repaymentId).attr('disabled', true)
                                        return false
                                    }
                                })
                            }
                            app.form.tradePlace = data.tradePlace
                            app.form.cert = data.certificatePictureUrl
                            app.form.remark = data.remark

                            
                        } else {
                            app.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                        }
                    })
                    .catch(function (error) {
                        app.$Modal.error({ content: '接口调用异常!' });
                    });
            }
        },
        watch: {
            'form.tradeType':function(n, o) {
                this.computeAcceptAccountLabel()
                if(o==""){
                    return ;
                }
                app.form.acceptBank = ''
                app.curBankAccount = ''
            },
            'form.acceptBank':function(n,o){
                console.log(this.bankAccountList);
                this.bankAccountList.forEach(e=>{
                    if(n==e.financeName){
                        app.curBankAccount = e.repaymentId||'' ;
                    }
                })
            },
            'form.repaymentMoney':function(n,o){
                var reg = /^\d{1,10}(\.\d{1,2})?$/;
                if (!reg.test(n)) {
                    app.$Message.error({content:'输入金额不符合要求，小数点前最多10位，小数点后最多2位'})
                    app.form.repaymentMoney = 0
                }
            },
            'form.remark':function(n,o){
                if(o==""){
                    return ;
                }
                if(n.length>50){
                    app.$Message.error({content:'输入字数不符合要求，最多50字'})
                    app.form.remark = ''
                }
            },
            'form.tradePlace':function(n,o){
                if(o==""){
                    return ;
                }
                if(n.length>15){
                    app.$Message.error({content:'输入字数不符合要求，最多15字'})
                    app.form.tradePlace = ''
                }
            },
            'form.factRepaymentUser':function(n,o){
                if(o==""){
                    return ;
                }
                if(n.length>10){
                    app.$Message.error({content:'输入字数不符合要求，最多10字'})
                    app.form.factRepaymentUser = ''
                }
            }
        },
        created: function () {
            this.listDepartmentBank()
            if(mprid){
                this.getMoneyPool()
            }
        }
    })


})