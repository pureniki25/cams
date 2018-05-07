/**
 * 
 */

let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let basePath = htConfig.basePath;

    app = new Vue({
        el: "#app",
        data: {
            businessId: getQueryStr('businessId'),
            afterId: getQueryStr('afterId'),
            repayDate: getQueryStr('repayDate'),
            accountMoney: getQueryStr('accountMoney'),
            acceptBank: decodeURI(getQueryStr('acceptBank')),
            moenyPoolId: getQueryStr('moenyPoolId'),
            status: decodeURI(getQueryStr('status')),
            bankAccounts: [],
            selections:[],
            table: {
                col: [
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '款项码',
                        key: 'code'
                    },
                    {
                        title: '转入账号',
                        key: 'acceptBank'
                    },
                    {
                        title: '转入时间',
                        key: 'tradeDate'
                    },
                    {
                        title: '交易类型',
                        key: 'tradeType'
                    },
                    {
                        title: '摘要',
                        key: 'summary'
                    },
                    {
                        title: '交易场所',
                        key: 'tradePlace'
                    },
                    {
                        title: '记账金额',
                        key: 'accountMoney'
                    },
                    {
                        title: '交易备注',
                        key: 'tradeRemark'
                    },
                    {
                        title: '状态',
                        key: 'status',
                        render: (h, p) => {
                            return h('Tag', {
                                props: {
                                    color: 'green'
                                }
                            }, p.row.status)
                        }
                    }
                ],
                data: []
            }
        },
        methods: {
            listDepartmentBank: function () {
                axios.get(basePath + 'core/moneyPool/listDepartmentBank', { params: { businessId: this.businessId } })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.bankAccounts = res.data.data
                        } else {
                            app.$Modal.error({ content: '接口调用异常' + res.data.msg })
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },
            listMoneyPool: function () {
                let params = {}

                if (this.businessId) {
                    params.businessId = this.businessId;
                }
                if (this.afterId) {
                    params.afterId = this.afterId;
                }
                if (this.moenyPoolId) {
                    params.moenyPoolId = this.moenyPoolId;
                }
                if (this.accountMoney) {
                    params.accountMoney = this.accountMoney;
                }
                if (this.status) {
                    params.status = this.status;
                }
                if (this.repayDate) {
                    params.repayDate = this.repayDate;
                }
                if (this.acceptBank) {
                    params.acceptBank = this.acceptBank;
                }
                axios.get(basePath + 'finance/finance/moneyPool', { params: params })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.table.data = res.data.data
                        } else {
                            app.$Modal.error({ content: '接口调用异常' + res.data.msg })
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },
            search: function () {
                this.table.data = []
                this.listMoneyPool()
            },
            onSelectChange:function(selections){
                this.selections = selections;
            },
            submit:function(){
                if(this.selections.length==0){
                    this.$Message.warning({content:'未选择需要匹配的流水'})
                    return ;
                }
                axios.post(basePath + 'finance/finance/matchBankStatement',{
                    req:{
                        array:this.selections,
                        businessId:this.businessId,
                        afterId:this.afterId
                    }
                }).then(function(res){

                }).catch(function(err){

                })
                console.log(this.selections)
            }
        },
        watch: {
            bankAccount(o, n) {
                console.log(o)
                console.log(n)
            }
        },
        created: function () {
            this.listDepartmentBank();
            this.listMoneyPool();
        }
    })
})