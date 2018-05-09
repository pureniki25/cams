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
                console.log(app.form);
                let params = {}


                Object.keys(this.form).forEach(element => {
                    if (this.form[element] &&
                        (this.form[element] != '' || this.form[element].length != 0)) {
                        params[element] = this.form[element]
                    }
                })
                params.businessId = businessId;
                params.afterId = afterId;
                axios.post(cpath+'moneyPool/addCustomerRepayment',params)
                .then(function(res){
                    if(res.data.code=='1'){
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
                window.parent.app.closeModal('manualAddBankSatementsShow')
            }
        },
        computed: {
            tradeType() {
                return this.form.tradeType;
            },
            acceptAcount(){
                return this.form.acceptAcount;
            }
        },
        watch: {
            tradeType(n, o) {
                this.computeAcceptAccountLabel()
            },
            acceptAcount(n,o){
                this.bankAccountList.forEach(e=>{
                    if(n==e.financeName){
                        app.curBankAccount = e.repaymentId ;
                        console.log(e.repaymentId);
                    }
                })
            }
        },
        created: function () {
            this.listDepartmentBank()
        }
    })


})