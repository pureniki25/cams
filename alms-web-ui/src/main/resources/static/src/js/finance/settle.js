/**
 * 
 */
window.app;
window.layinit(function (htConfig) {
    let _htConfig = htConfig;
    let businessId = getQueryStr('businessId')
    let afterId = getQueryStr('afterId')
    let planId = getQueryStr('planId')
    let planListId = getQueryStr('planListId')
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    let layer = layui.layer;
    let curIndex;
    app = new Vue({
        el: "#app",
        data: {
            spinShow: true,
            info: {},
            curIndex: {},
            matchedBankStatement: [],
            factRepayPreview: {},
            factRepaymentInfo: {},
            thisTimeRepaymentInfo: {},
            table: {
                reg: {
                    col: [{
                        title: '流水号',
                        key: 'moneyPoolId'
                    }, {
                        title: '还款日期',
                        key: 'tradeDate',
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
                        key: 'state',

                    }, {
                        title: '凭证',
                        width: 200,
                        render: (h, p) => {
                            let checkCert = h('i-button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                },
                                on: {
                                    click: function () {
                                        window.open(p.row.certificatePictureUrl);
                                    }
                                }
                            }, '查看凭证')

                            let Null = h('span', {}, '无')

                            let content = [];
                            if (p.row.certificatePictureUrl) {
                                content.push(checkCert)
                            } else {
                                content.push(Null)
                            }
                            return h('div', content)
                        }
                    }, {
                        title: '操作',
                        width: 160,
                        render: (h, p) => {
                            let match = h('i-button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                },
                                on: {
                                    click() {
                                        console.log(p.row);
                                        let repayDate = moment(p.row.tradeDate).format('YYYY-MM-DD');
                                        let accountMoney = p.row.accountMoney;
                                        let acceptBank = p.row.bankAccount;
                                        let mrpid = p.row.id

                                        let url = '/finance/manualMatchBankSatements?businessId=' +
                                            businessId + "&afterId=" +
                                            afterId + '&repayDate=' +
                                            repayDate + '&accountMoney=' +
                                            accountMoney + '&acceptBank=' +
                                            acceptBank + '&timestamp=' +
                                            new Date().getTime() + '&mprid=' +
                                            mrpid;
                                        layer.open({
                                            type: 2,
                                            title: '手动匹配流水',
                                            content: [url, 'no'],
                                            area: ['1600px', '800px'],
                                            success: function (layero, index) {
                                                curIndex = index;
                                            }
                                        })
                                    }
                                }
                            }, '匹配')
                            let dismatch = h('i-button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                },
                                on: {
                                    click() {
                                        let mprid = p.row.id
                                        axios.post(fpath + 'finance/rejectRepayReg', {
                                                mprid: mprid
                                            })
                                            .then(function (res) {
                                                if (res.data.code == '1') {
                                                    window.location.reload()
                                                } else {
                                                    app.$Message.error({
                                                        content: res.data.msg
                                                    })
                                                }
                                            })
                                            .catch(function (err) {
                                                app.$Message.error({
                                                    content: '拒绝还款登记失败'
                                                })
                                            })
                                    }
                                }
                            }, '拒绝')
                            let content = []
                            if (p.row.state == '未关联银行流水') {
                                content.push(match)
                                content.push(dismatch)
                            }
                            return h('div', content)
                        }
                    }],
                    data: []
                },
                matched: {
                    col: [{
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
                            if (!s) {
                                return h('span', '')
                            }
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

                            if (p.row.moneyPoolId != '合计' && !p.row.status == '完成') {
                                return h('i-button', {
                                        on: {
                                            click: function () {
                                                axios.post(fpath + 'finance/disMatchBankStatement', {
                                                        mpid: p.row.moneyPoolId
                                                    })
                                                    .then(function (r) {
                                                        if (r.data.code == "1") {
                                                            location.reload()
                                                        } else {
                                                            app.$Message.error({
                                                                content: r.data.msg
                                                            })
                                                        }
                                                    })
                                                    .catch(function (e) {
                                                        app.$Message.error({
                                                            content: '取消关联银行流水失败'
                                                        })
                                                    })
                                            }
                                        }
                                    },
                                    '取消关联'
                                )
                            } else {
                                return h('span', '')
                            }

                        }
                    }],
                    data: [{
                        "moneyPoolId": "73b90613df4943fa8e09935aa2f25f2c",
                        "repaymentCode": null,
                        "bankAccount": " 陈树华招行",
                        "tradeDate": "2018-05-22",
                        "tradeType": "转账",
                        "tradePlace": null,
                        "accountMoney": 2500.00,
                        "remark": null,
                        "summary": null,
                        "status": "完成"
                    }, {
                        "moneyPoolId": "4939b7bc86154b789c1f5b21fcc56e78",
                        "repaymentCode": null,
                        "bankAccount": " 陈树华招行",
                        "tradeDate": "2018-05-22",
                        "tradeType": "转账",
                        "tradePlace": null,
                        "accountMoney": 257.51,
                        "remark": null,
                        "summary": null,
                        "status": "完成"
                    }, {
                        "moneyPoolId": "0b53e114dd4f4443bc68c8db061271e5",
                        "repaymentCode": null,
                        "bankAccount": " 陈树华招行",
                        "tradeDate": "2018-05-20",
                        "tradeType": "转账",
                        "tradePlace": null,
                        "accountMoney": 2500.00,
                        "remark": null,
                        "summary": null,
                        "status": "完成"
                    }]
                },
                projRepayment: {
                    col: [],
                    data: []
                },
                plan: {
                    col: [{
                        title: '期数',
                        key: 'afterId'
                    }, {
                        title: '还款日期',
                        key: 'repayDate'
                    }, {
                        title: '本金',
                        key: 'item10'
                    }, {
                        title: '利息',
                        key: 'item20'
                    }, {
                        title: '分公司服务费',
                        key: 'item30'
                    }, {
                        title: '平台费',
                        key: 'item50'
                    }, {
                        title: '线下滞纳金',
                        key: 'offlineOverDue'
                    }, {
                        title: '线上滞纳金',
                        key: 'onlineOverDue'
                    }, {
                        title: '应还合计',
                        key: 'planAmount'
                    }, {
                        title: '差额',
                        key: 'lack'
                    }, {
                        title: '实际还款金额',
                        key: 'factAmount'
                    }, {
                        title: '提前结清违约金',
                        key: 'penalty'
                    }, {
                        title: '备注',
                        key: 'remark'
                    }, {
                        title: '状态',
                        key: 'status'
                    }, ],
                    data: []
                }
            }
        },
        watch: {},
        methods: {
            getBaseInfo() {
                axios.get(fpath + 'finance/repayBaseInfo', {
                        params: {
                            businessId: businessId,
                            afterId: afterId
                        }
                    })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.info = res.data.data;
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                        app.spinShow = false
                    })
                    .catch(function (err) {
                        app.spinShow = false
                    })
            },
            getMatched() {
                axios.get(cpath + 'moneyPool/checkMoneyPool', {
                        params: {
                            businessId: businessId,
                            afterId: afterId,
                            isMatched: true,
                            noConfirmed: true
                        }
                    })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            res.data.data.forEach(e => {
                                if (e.status != '完成' && e.status != '已完成') {
                                    app.matchedBankStatement.push(e)
                                }
                                app.table.matched.data.push(e)
                            });

                            // app.table.matched.data = res.data.data.slice(0);
                            let t = 0;
                            app.table.matched.data.forEach(element => {
                                t += element.accountMoney
                            });
                            let sum = {
                                moneyPoolId: '合计',
                                accountMoney: t
                            }

                            app.table.matched.data.push(sum)

                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                        app.spinShow = false;
                    }).catch(function (err) {
                        app.$Message.error({
                            content: '获取匹配的款项池银行流水数据失败'
                        })
                        app.spinShow = false;
                    })
            },
            openMatchBankStatementModal(p) {
                let url = '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动匹配流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        curIndex = index;
                    }
                })
            },
            openAddBankStatementModal() {
                let url = '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动新增流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        curIndex = index;
                    }
                })
            },
            listRepayment(){
                axios.get(fpath+'settle/listRepaymentSettleListVOs?businessId='+businessId+'&afterId='+afterId+(planId?('&planId='+planId):''))
                .then(function(res){
                    if(res.data.code=='1'){
                        app.table.plan.data = res.data.data
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){
                    app.$Message.error({content:err})
                })
            },
            closePage(){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                parent.layer.close(index); //再执行关闭   
            }
        },
        created: function () {
            this.getBaseInfo()
            this.getMatched()
            this.listRepayment()
        }
    })
})
