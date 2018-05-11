/**
 * 
 */

let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;

    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    console.log(typeof getQueryStr('repayDate'));
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
            selections: [],
            curPage: 1,
            pageSize: 10,
            table: {
                col: [{
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
                data: [],
                total: 0
            }
        },
        methods: {
            onDateChange: function (date) {
                console.log(date);
                this.repayDate = date;
            },
            listDepartmentBank: function () {
                axios.get(cpath + 'moneyPool/listDepartmentBank', {
                        params: {
                            businessId: this.businessId
                        }
                    })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.bankAccounts = res.data.data
                        } else {
                            app.$Modal.error({
                                content: '接口调用异常' + res.data.msg
                            })
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },
            listMoneyPool: function () {
                let params = {}
                params.curPage = this.curPage;
                params.pageSize = this.pageSize;
                if (this.businessId) {
                    params.businessId = this.businessId;
                }
                if (this.afterId) {
                    params.afterId = this.afterId;
                }
                if (this.accountMoney) {
                    params.accountMoney = this.accountMoney;
                }
                if (this.repayDate) {
                    params.repayDate = this.repayDate;
                }
                if (this.acceptBank && this.acceptBank.length > 0) {
                    params.acceptBank = this.acceptBank;
                }

                axios.get(fpath + 'finance/moneyPool', {
                        params: params
                    })
                    .then(function (res) {
                        if (res.data.code == '0') {
                            app.table.data = res.data.data
                            app.table.total = res.data.count
                        } else {
                            app.$Modal.error({
                                content: '接口调用异常' + res.data.msg
                            })
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            },
            search: function (page) {
                if (page) {
                    this.curPage = 1
                }
                this.table.data = []
                this.listMoneyPool()
            },
            onSelectChange: function (selections) {
                this.selections = selections;
            },
            submit: function () {
                if (this.selections.length == 0) {
                    this.$Message.warning({
                        content: '未选择需要匹配的流水'
                    })
                    return;
                }
                let params = {
                    array: this.selections,
                    businessId: this.businessId,
                    afterId: this.afterId
                }

                let mprid = getQueryStr('mprid')
                if(mprid&&mprid.length>0){
                    if(params.array.length>1){
                        app.$Message.error({
                            content:'只能匹配1条流水,请重新检查'
                        })
                        return;
                    }
                    params.mprid = mprid
                }
                axios.post(fpath + 'finance/matchBankStatement', params).then(function (res) {
                    if(res.data.code=='1'){
                        window.parent.location.reload()
                        //parent.app.closeModal()
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                    console.log(res.data);
                }).catch(function (err) {
                    console.log(err);
                })
            },
            paging: function (page) {
                app.search(page)
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