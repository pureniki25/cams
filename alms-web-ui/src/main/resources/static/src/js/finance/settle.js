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
            thisTimeRepaymentInfo: {
                repayDate: '',
                item10: '',
                item20: '',
                item30: '',
                item50: '',
                subtotal: '',
                overDays: '',
                offlineOverDue: '',
                onlineOverDue: '',
                penalty: '',
                derate: '',
                planRepayBalance: '',
                total: '',
            },
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
                    data: [/* {
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
                    } */]
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
                        key: 'item10',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.item10).format('0,0.00'))
                        }
                    }, {
                        title: '利息',
                        key: 'item20',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.item20).format('0,0.00'))
                        }
                    }, {
                        title: '分公司服务费',
                        key: 'item30',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.item30).format('0,0.00'))
                        }
                    }, {
                        title: '平台费',
                        key: 'item50',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.item50).format('0,0.00'))
                        }
                    }, {
                        title: '线下滞纳金',
                        key: 'offlineOverDue',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.offlineOverDue).format('0,0.00'))
                        }
                    }, {
                        title: '线上滞纳金',
                        key: 'onlineOverDue',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.onlineOverDue).format('0,0.00'))
                        }
                    }, {
                        title: '应还合计',
                        key: 'planAmount',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.planAmount).format('0,0.00'))
                        }
                    }, {
                        title: '实际还款金额',
                        key: 'factAmount',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.factAmount).format('0,0.00'))
                        }
                    }, {
                        title: '差额',
                        key: 'lack',
                        render:(h,p)=>{
                            return h('span',numeral(p.row.lack).format('0,0.00'))
                        }
                    }, /* {
                        title: '提前结清违约金',
                        key: 'penalty'
                    }, */ {
                        title: '备注',
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
                            //还款中，逾期，已还款
                            if(content=='还款中'){
                                color='green'
                            }
                            if(content=='逾期'){
                                color='#ed3f14'
                            }
                            if(content=='已还款'){
                                color='blue'
                            }
                            // '会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;',
                            return h('Tag', {
                                props: {
                                    color: color
                                }
                            }, content)
                        }
                    }, ],
                    data: []
                }
            },
            factRepaymentInfo: {
                repayDate: '',
                surplusFund: 0,
                onlineOverDuel: '',
                offlineOverDue: '',
                remark: '',
                canUseSurplus: 0,
                moneyPoolAccount: 0,
                repayAccount: 0,
                mprIds: []
            },
            factRepayPreview: {
                item10: 0,
                item20: 0,
                item30: 0,
                item50: 0,
                offlineOverDue: 0,
                onlineOverDue: 0,
                subTotal: 0,
                total: 0,
                surplus: 0
            }
        },
        watch: {
            'matchedBankStatement': function (n) {
                let moneyPoolAccount = 0
                if (n && n.length > 0) {
                    n.forEach(element => {
                        moneyPoolAccount = (moneyPoolAccount*10000+element.accountMoney*10000)/10000
                        app.factRepaymentInfo.mprIds.push(element.mprId);
                    });
                    let o = n[n.length - 1]
                    app.factRepaymentInfo.repayDate = o.tradeDate
                }
                app.factRepaymentInfo.moneyPoolAccount = moneyPoolAccount
                
                // app.factRepaymentInfo.repayAccount = parseFloat(app.factRepaymentInfo.moneyPoolAccount.toFixed(2))  + parseFloat(app.factRepaymentInfo.surplusFund.toFixed(2) || 0)
                app.factRepaymentInfo.repayAccount = (app.factRepaymentInfo.moneyPoolAccount*10000+(app.factRepaymentInfo.surplusFund||0)*10000)/10000
            },
            'factRepaymentInfo.useSurplusflag':function(n,o){
                if(o==''){
                    return ;
                }
                app.factRepaymentInfo.surplusFund = 0
            },
            'factRepaymentInfo.surplusFund': function (n) {
                if ((n||n==0 )&& !isNaN(n)) {
                    if(n>app.factRepaymentInfo.canUseSurplus){
                        app.$Modal.warning({content:'可使用结余金额不能大于'+app.factRepaymentInfo.canUseSurplus})
                        app.factRepaymentInfo.surplusFund = 0
                        return;
                    }
                    app.factRepaymentInfo.repayAccount = (app.factRepaymentInfo.moneyPoolAccount*10000+(app.factRepaymentInfo.surplusFund||0)*10000)/10000
                }
            },
            'factRepaymentInfo.repayAccount': function (n) {
                app.previewConfirmRepayment()
            }
        },
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
            },
            getSurplusFund() {
                axios.get(fpath + 'finance/getSurplusFund?businessId=' + businessId + "&afterId=" + afterId)
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.factRepaymentInfo.canUseSurplus = res.data.data
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: '获取结余信息失败'
                        })
                    })
            },
            getSettleInfo() {
                axios.get(fpath + 'settle/settleInfo?businessId=' + businessId + "&afterId=" + afterId +(planId?('&planId='+planId):''))
                    .then(function (res) {
                        if (res.data.code == '1') {
                            let data = res.data.data;
                            app.thisTimeRepaymentInfo.repayDate = data.repayPlanDate
                            app.thisTimeRepaymentInfo.item10 = data.item10
                            app.thisTimeRepaymentInfo.item20 = data.item20
                            app.thisTimeRepaymentInfo.item30 = data.item30
                            app.thisTimeRepaymentInfo.item50 = data.item50
                            app.thisTimeRepaymentInfo.subtotal = data.subtotal
                            app.thisTimeRepaymentInfo.overDays = data.overDueDays
                            app.thisTimeRepaymentInfo.offlineOverDue = data.offlineOverDue
                            app.thisTimeRepaymentInfo.onlineOverDue = data.onlineOverDue
                            app.thisTimeRepaymentInfo.penalty = data.penalty
                            app.thisTimeRepaymentInfo.derate = data.derate
                            app.thisTimeRepaymentInfo.planRepayBalance = data.planRepayBalance
                            app.thisTimeRepaymentInfo.total = data.total

                            console.log(res.data);
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: '获取结清应还信息失败'
                        })
                    })
            },
            
        },
        created: function () {
            this.getBaseInfo()
            this.getMatched()
            this.listRepayment()
            this.getSettleInfo()
        }
    })
})
