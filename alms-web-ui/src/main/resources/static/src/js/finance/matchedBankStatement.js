/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    app = new Vue({
        el: "#app",
        data: {
            spinShow: false,
            table: {
                col: [
                    {
                        title: '流水号',
                        key: 'moneyPoolId'
                    }, {
                        title: '款项码',
                        key: 'repaymentCode'
                    }, {
                        title: '转入账号',
                        key: 'bankAccount'
                    }, {
                        title: '转入时间',
                        key: 'tradeDate'
                    }, {
                        title: '交易类型',
                        key: 'tradeType'
                    }, {
                        title: '摘要',
                        key: 'summary'
                    }, {
                        title: '交易场所',
                        key: 'tradePlace'
                    }, {
                        title: '记账金额',
                        key: 'accountMoney',
                        render: (h, p) => {
                            let content = numeral(p.row.accountMoney).format('0,0.00') + ''
                            return h('span', content)
                        }
                    }, {
                        title: '交易备注',
                        key: 'remark'
                    }, {
                        title: '状态',
                        key: 'status',
                        render: (h, p) => {
                            let s = p.row.status;
                            let color = 'blue'
                            let content = s
                            // '会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;',
                            return h('Tag', {
                                props: {
                                    color: color
                                }
                            }, content)
                        }
                    }, {
                        title: '操作',
                        render: (h, p) => {
                            return h('i-button', {
                                on: {
                                    click: function () {
                                        console.log(p.row);
                                    }
                                }
                            },
                                '取消关联'
                            )
                        }
                    }
                ],
                data: [{
                    moneyPoolId: 'test',
                    status: '待领取',
                    accountMoney: 565.56
                }]
            }
        },
        methods: {
            handleParentStyle: function (style) {
                window.parent.app.configModalStyle('matchedBankStatement', style)
            }
        },
        created: function () {
            axios.get(cpath + 'moneyPool/checkMoneyPool', {
                params: {
                    businessId: businessId,
                    afterId: afterId,
                    isMatched: true,
                }
            })
                .then(function (res) {
                    console.log(res);
                    if (res.data.code == '1') {
                        app.table.data = res.data.data
                        let style = {
                            height: (app.table.data.length?(app.table.data.length+2)*50:175 )+ 'px',
                            width: '100%'
                        }
                        app.handleParentStyle(style)
                    } else {
                        app.$Message.error({ content: res.data.msg })
                    }
                    app.spinShow = false;
                }).catch(function (err) {
                    console.log(err);
                    app.$Message.error({ content: '获取匹配的款项池银行流水数据失败' })
                    app.spinShow = false;
                })
        }
    })
})