/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    console.log(cpath);
    console.log(fpath);
    app = new Vue({
        el: "#app",
        data: {
            form: {
                curPage:1,
                pageSize:10,
                acceptBank: '',
                tradeType: '',
                tradeDateStart:'',
                tradeDateEnd:'',
                updatedateStart:'',
                updateDateEnd:'',
            },
            bankList: {},
            table: {
                col: [{
                        type: 'selection',
                        width: 70,
                    },
                    {
                        title: '转入账号',
                        key: 'acceptBank'
                    }, {
                        title: '交易时间',
                        key: 'tradeDate'
                    }, {
                        title: '转入账号',
                        key: 'bankAccount',
                        width:200
                    }, {
                        title: '记账金额',
                        key: 'accountMoney',
                        align:'right',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.accountMoney).format('0,0.00'))
                        }
                    }, {
                        title: '支付类型',
                        key: 'tradeType'
                    }, {
                        title: '交易场所',
                        key: 'tradePlace'
                    }, {
                        title: '状态',
                        key: 'status'
                    }, {
                        title: '创建时间',
                        key: 'createTime'
                    }, {
                        title: '交易备注',
                        key: 'tradeRemark'
                    }, {
                        title: '领取人',
                        key: 'gainerName'
                    }, {
                        title: '操作',
                        key: '',
                        width:200,
                        render:(h,p)=>{
                            return h('div',[
                                h('i-button',{
                                    style:{
                                        marginRight:'1rem'
                                    },
                                    on:{
                                        click(){
                                            app.getMoneyPool(p.row.moneyPoolId)
                                        }
                                    }
                                },'详情'),
                                h('i-button',{
                                    on:{
                                        click(){
                                            app.deleteMoneyPool(p.row.moneyPoolId)
                                        }
                                    }
                                },'删除'),
                            ])
                        }
                    },
                ],
                data: [],
                loading: false,
                total:0,
            },
            selection:[],
            bankList:[],
            upload:{
                url:fpath+'/finance/importExcel'
            },
            detail:{
                mp:{},
                list:[]
            },
            detailShow:false
        },
        watch: {},
        methods: {
            listDepartmentBank: function () {
                axios.get(fpath + 'finance/listDepartmentBank' )
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.bankList = res.data.data
                        } else {
                            app.$Modal.error({ content: '接口调用异常' })
                        }
                    })
                    .catch(function (error) {
                        app.$Modal.error({ content: '接口调用异常' })
                    })
            },
            page(page){
                this.form.curPage = page ;
                this.getMoneyPoolManager()
            },
            search(){
                this.form.curPage = 1;
                this.getMoneyPoolManager()
            },
            getMoneyPoolManager(){
                this.table.loading = true
                axios.get(fpath+'finance/moneyPoolManager',{
                    params:this.form
                })
                .then(function(res){
                    app.table.loading = false
                    if(res.data.code==0){
                        app.table.data = res.data.data
                        app.table.total = res.data.count
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                    console.log(res);
                })
                .catch(function(err){
                    app.table.loading = false
                    app.$Message.error({content:err})
                })
            },
            deleteMoneyPool(mpid){
                let mpids = []
                if(mpid&&typeof mpid=='string'){
                    mpids.push(mpid)
                }else{
                    this.selection.forEach(element => {
                        mpids.push(element.moneyPoolId)
                    });
                }

                if(mpids.length==0){
                    app.$Message.warning({content:'请选择要删除的流水'})
                    return ;
                }
                app.$Message.loading({
                    duration:0,
                    content:'删除中,请稍后...'
                })
                axios.get(fpath+'finance/delMoneyPool',{
                    params:{
                        mpids:mpids
                    }
                })
                .then(function(res){
                    app.$Message.destroy()
                    if(res.data.code=='1'){
                        app.$Message.success({content:'删除成功!'})
                        app.getMoneyPoolManager()
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){
                    app.$Message.destroy()
                    app.$Message.error({content:err})
                })
            },
            uploadSuccess(response, file, fileList){
                if(response.code=='1'){
                    app.$Message.success({content:"导入成功"})
                    app.search()
                }else{
                    app.$Message.error({content:"导入失败"})
                }
            },
            getMoneyPool(mpid){
                axios.get(cpath+'moneyPool/get',{
                    params:{
                        moneyPoolId:mpid
                    }
                })
                .then(function(res){
                    if(res.data.code=='1'){
                        app.detail = res.data.data ; 
                        app.detailShow = true
                    }else{
                        app.$Message.error({content:'获取详情失败'})
                    }
                })
                .catch(function(err){
                    app.$Message.error({content:'获取详情失败'})
                })
            }
        },
        created: function () {
            this.listDepartmentBank()
            this.search()
        }
    })
})