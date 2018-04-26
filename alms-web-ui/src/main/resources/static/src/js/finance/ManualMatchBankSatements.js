/**
 * 
 */

let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;

    app = new Vue({
        el: "#app",
        data: {
            businessId: getQueryStr('businessId'),
            regDate: getQueryStr('regDate'),
            moneyAmount: getQueryStr('moneyAmount'),
            bankAccount: decodeURI(getQueryStr('bankAccount')),
            bankAccounts: [],
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
                        key: 'account'
                    },
                    {
                        title: '转入时间',
                        key: 'date'
                    },
                    {
                        title: '交易类型',
                        key: 'type'
                    },
                    {
                        title: '摘要',
                        key: 'summary'
                    },
                    {
                        title: '交易场所',
                        key: 'place'
                    },
                    {
                        title: '记账金额',
                        key: 'amount'
                    },
                    {
                        title: '交易备注',
                        key: 'remark'
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
                axios.get(basePath + 'moneyPool/listDepartmentBank', { params: { businessId: this.businessId } })
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
        },
        watch:{
            bankAccount(o,n){
                console.log(o)
                console.log(n)
            }
        },
        created: function () {
            this.listDepartmentBank();
            this.table.data = [
                {
                    code: 'abc',
                    account: '黄浩然工行',
                    date: '2018-08-08',
                    type: '',
                    summary: '',
                    place: '网上银行',
                    amount: 10000,
                    remark: 'wu',
                    status: '待领取'
                }, {
                    code: 'abc',
                    account: '黄浩然工行',
                    date: '2018-08-08',
                    type: '',
                    summary: '',
                    place: '网上银行',
                    amount: 10000,
                    remark: 'wu',
                    status: '待领取'
                }, {
                    code: 'abc',
                    account: '黄浩然工行',
                    date: '2018-08-08',
                    type: '',
                    summary: '',
                    place: '网上银行',
                    amount: 10000,
                    remark: 'wu',
                    status: '待领取'
                }
            ]
        }
    })
})