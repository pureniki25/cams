/**
 * 
 */

let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    let businessId = getQueryStr('businessId')


    app = new Vue({
        el: "#app",
        data: {
            form: {
                repayDate: '',
                repayType: '',
                repayAmount: 0,
                acceptAcount: '',
                certUrl:''
            },
            acceptAccountLabel: '',
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
            },
            bankAccountList:[],
            curBankAccount:''
        },
        methods: {
            computeAcceptAccountLabel: function () {
                let res;
                switch (this.form.repayType) {
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
                app.form.certUrl = docItemListJson.docUrl;
            }, onUploadError: function (error, file, fileList) {
                app.$Message.error({ content: '上传失败,请稍后再试' });
            }, onExceededSize: function (file, fileList) {
                app.$Message.error({ content: '只能上传2MB以内的图片' });
            }, onFormatError: function (file, fileList) {
                app.$Message.error({ content: '只能上传图片格式文件' });
            },
            listDepartmentBank:function(){
                axios.get('http://localhost:30606/'+'moneyPool/listDepartmentBank',{params:{
                    businessId:businessId
                }}).then(function(res){
                    app.bankAccountList = res.data.data
                    console.log(res.data.data)
                }).catch(function(err){

                })
            }
        },
        computed: {
            repayType() {
                return this.form.repayType;
            },
            acceptAcount(){
                return this.form.acceptAcount;
            }
        },
        watch: {
            repayType(n, o) {
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