/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    app = new Vue({
        el: "#app",
        data: {
            spinShow: true,
            table: {
                col: [
                    {
                        title: '流水号',
                        key: 'moneyPoolId'
                    }, {
                        title: '还款日期',
                        key: 'tradeDate'
                    }, {
                        title: '还款金额',
                        key: 'accountMoney'
                    }, {
                        title: '实际转款人',
                        key: 'factTransferName'
                    }, {
                        title: '转入账号',
                        key: 'bankAccount'
                    }, {
                        title: '交易类型',
                        key: 'tradeType'
                    }, {
                        title: '交易场所',
                        key: 'tradePlace'
                    }, {
                        title: '备注',
                        key: 'remark'
                    }, {
                        title: '状态',
                        key: 'state'
                    }, {
                        title: '操作',
                        key: ''
                    }
                ],
                data: []
            }
        },
        methods: {

        },
        created: function () {
            axios.get('http://localhost:30621/' + 'finance/regRepayInfoList', {
                params: {
                    planListId: planListId
                }
            })
                .then(function (res) {
                    console.log(res);
                    if (res.data.code == '1') {
                        app.table.data = res.data.data
                    } else {
                        app.$Message.error({ content: res.data.msg })
                    }
                    app.spinShow = false;
                }).catch(function (err) {
                    console.log(err);
                    app.$Message.error({ content: '获取登记还款信息列表数据失败' })
                    app.spinShow = false;
                })
        }
    })
})