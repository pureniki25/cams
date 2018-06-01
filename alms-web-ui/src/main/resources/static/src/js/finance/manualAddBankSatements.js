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

    app = new Vue({
        el: "#app",
        data: {
            form: {
                repaymentDate: '',
                tradeType: '',
                repaymentMoney: 0,
                acceptBank: '',
                cert:''
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
            },
            cancel:function(){
                app.$refs['form'].resetFields()
                //当你在iframe页面关闭自身时
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭 
                // window.parent.app.closeModal('manualAddBankSatementsShow')
            }
        },
        computed: {
            tradeType() {
                return this.form.tradeType;
            },
            acceptBank(){
                return this.form.acceptBank;
            }
        },
        watch: {
            tradeType(n, o) {
                app.form.acceptBank = ''
                app.curBankAccount = ''
                this.computeAcceptAccountLabel()
            },
            acceptBank(n,o){
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
            }
        },
        created: function () {
            this.listDepartmentBank()
        }
    })


})