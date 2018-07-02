/**
 *
 */
window.app;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    var layer = layui.layer;
    let curIndex;
    app = new Vue({
        el: "#app",
        data: {
            info: {},
            repayInfo: {
                url: '/finance/repayBaseInfo?businessId=' + businessId + "&afterId=" + afterId,
            },
            repayRegList: {
                url: '/finance/repayRegList?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            confirmedRepayment: {
                url: '/finance/confirmedRepayment?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            matchedBankStatement: {
                show: false,
                url: '/finance/matchedBankStatement?businessId=' + businessId + "&afterId=" + afterId,
                style: {},
                data: []
            },
            manualAddBankSatements: {
                show: false,
                url: '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId,

            },
            manualMatchBankSatements: {
                show: false,
                url: '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId,
            },

            table: {
                currPeriodRepayment: {
                    col: [{
                        title: '展开',
                        type: 'expand',
                        render: (h, p) => {
                            if (p.row.type == '差额') {
                                return;
                            }
                            return h('i-table', {
                                props: {
                                    stripe: true,
                                    columns: [{
                                        title: '借款人',
                                        key: 'userName'
                                    },
                                        {
                                            title: '上标金额',
                                            key: 'projAmount'
                                        },
                                        {
                                            title: '本金',
                                            key: 'item10'
                                        }, {
                                            title: '利息',
                                            key: 'item20'
                                        }, {
                                            title: '月收分公司服务费',
                                            key: 'item30'
                                        }, {
                                            title: '月收平台费',
                                            key: 'item50'
                                        }, {
                                            title: '小计',
                                            key: 'subTotal'
                                        }, {
                                            title: '线下逾期费',
                                            key: 'offlineOverDue'
                                        }, {
                                            title: '减免金额',
                                            key: 'onlineOverDue'
                                        }, {
                                            title: '合计（含滞纳金）',
                                            key: 'total'
                                        }
                                    ],
                                    data: p.row.list || []
                                }
                            })
                        }
                    }, {
                        title: '类型',
                        key: 'type'
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
                        title: '月收分公司服务费',
                        key: 'item30'
                    }, {
                        title: '月收平台费',
                        key: 'item50'
                    }, {
                        title: '小计',
                        key: 'subTotal'
                    }, {
                        title: '线下逾期费',
                        key: 'offlineOverDue'
                    }, {
                        title: '线上逾期费',
                        key: 'onlineOverDue'
                    }, {
                        title: '减免金额',
                        key: 'derate'
                    }, {
                        title: '合计(含滞纳金)',
                        key: 'total'
                    }],
                    data: []
                },
                projRepayment: projRepayment,
                repayRegList: {
                    col: [{
                        title: '流水号',
                        key: 'moneyPoolId',
                        ellipsis: true,
                        width: 250
                    }, {
                        title: '还款日期',
                        key: 'tradeDate',
                        render: (h, p) => {
                            if (p.row.tradeDate) {
                                return h('span', moment(p.row.tradeDate).format('YYYY-MM-DD'))
                            }
                        }
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
                        width: 200,
                        key: 'state',

                    }, {
                        title: '凭证',
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
                                        window.open('http://xiaodaioa.oss-cn-beijing.aliyuncs.com/' + p.row.certificatePictureUrl);
                                    }
                                }
                            }, '查看凭证')

                            let Null = h('span', {}, '无')

                            let content = [];
                            if (p.row.certificatePictureUrl) {
                                content.push(checkCert)
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
                                        let repayDate = moment(p.row.tradeDate).format('YYYY-MM-DD');
                                        let accountMoney = p.row.accountMoney;
                                        let acceptBank = p.row.bankAccount;
                                        let mrpid = p.row.id
                                        app.manualMatchBankSatements.show = true;
                                        app.manualMatchBankSatements.url = '/finance/manualMatchBankSatements?businessId=' +
                                            businessId + "&afterId=" +
                                            afterId + '&repayDate=' +
                                            repayDate + '&accountMoney=' +
                                            accountMoney + '&acceptBank=' +
                                            acceptBank + '&timestamp=' +
                                            new Date().getTime() + '&mprid=' +
                                            mrpid;
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
                                        app.repayRegList.rejectModal = true
                                        app.repayRegList.mprid = p.row.id
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
                confirmedRepayment: {
                    col: [{
                        title: '流水号',
                        key: 'moneyPoolId',
                        width: 250,
                        ellipsis: true,
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
                    }],
                    data: []
                },
                matchedBankStatement: {
                    col: [{
                        title: '流水号',
                        key: 'moneyPoolId',
                        ellipsis: true,
                        width: 250
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
                        width: 150,
                        render: (h, p) => {

                            if (p.row.moneyPoolId != '合计' && p.row.status == '待领取') {
                                return h('i-button', {
                                        props: {
                                            size: 'small'
                                        },
                                        on: {
                                            click: function () {
                                                app.disMatchedStatement(p)
                                            }
                                        }
                                    },
                                    '取消关联'
                                )
                            } else if (p.row.moneyPoolId != '合计' && p.row.status == '已领取') {
                                return h('div', [
                                    h('i-button', {
                                        props: {
                                            size: 'small'
                                        },
                                        on: {
                                            click() {
                                                app.openEditBankStatementModal(p.row.mprId)
                                            }
                                        },
                                        style: {
                                            marginRight: '10px'
                                        }
                                    }, '编辑流水'),
                                    h('i-button', {
                                        props: {
                                            size: 'small'
                                        },
                                        on: {
                                            click() {
                                                app.deleteMoneyPool(p)
                                            }
                                        }
                                    }, '删除')
                                ])
                            }

                        }
                    }],
                    data: []
                }
            },
            repayRegList: {
                remark: '',
                rejectModal: false,
                mprid: ''
            },
            thisTimeRepaymentInfo: {
                derate: '',
                item10: '',
                item20: '',
                item30: '',
                item30: '',
                item50: '',
                offlineOverDue: '',
                onlineOverDue: '',
                overDays: '',
                repayDate: '',
                subtotal: '',
                total: '',
                derateDetails: []
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
                surplus: 0,
                mprIds: []
            }
        },
        watch: {
            'matchedBankStatement.data': function (n) {
                let moneyPoolAccount = 0
                if (n && n.length > 0) {
                    n.forEach(element => {
                        moneyPoolAccount = (moneyPoolAccount * 10000 + element.accountMoney * 10000) / 10000
                        app.factRepaymentInfo.mprIds.push(element.mprId);
                    });
                    let o = n[n.length - 1]
                    app.factRepaymentInfo.repayDate = o.tradeDate
                }
                app.factRepaymentInfo.moneyPoolAccount = moneyPoolAccount

                // app.factRepaymentInfo.repayAccount = parseFloat(app.factRepaymentInfo.moneyPoolAccount.toFixed(2))  + parseFloat(app.factRepaymentInfo.surplusFund.toFixed(2) || 0)
                app.factRepaymentInfo.repayAccount = (app.factRepaymentInfo.moneyPoolAccount * 10000 + (app.factRepaymentInfo.surplusFund || 0) * 10000) / 10000
            },
            'factRepaymentInfo.useSurplusflag': function (n, o) {
                if (o == '') {
                    return;
                }
                app.factRepaymentInfo.surplusFund = 0
            },
            'factRepaymentInfo.surplusFund': function (n) {
                if ((n || n == 0 ) && !isNaN(n)) {
                    if (n > app.factRepaymentInfo.canUseSurplus) {
                        app.$Modal.warning({content: '可使用结余金额不能大于' + app.factRepaymentInfo.canUseSurplus})
                        app.factRepaymentInfo.surplusFund = 0
                        return;
                    }
                    app.factRepaymentInfo.repayAccount = (app.factRepaymentInfo.moneyPoolAccount * 10000 + (app.factRepaymentInfo.surplusFund || 0) * 10000) / 10000
                }
            },
            'factRepaymentInfo.repayAccount': function (n) {
                app.previewConfirmRepayment()
            }
        },
        methods: {
            closePage() {
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                console.log("index1==",index);
                console.log("name==",window.name);
                parent.layer.close(index);
            },
            closeModal: function (target) {
                if (app[target]) {
                    app[target] = false;
                } else {
                    layer.close(curIndex)
                }
            },
            openModal: function (target) {
                if (!app[target]) {
                    app[target] = true
                }
            },
            configModalStyle: function (target, style) {
                if (app[target].style) {
                    app[target].style = style
                }
            },
            openMatchBankStatementModal(p) {
                let url = '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动匹配流水',
                    content: [url],
                    area: ['95%', '95%'],
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
                    content: [url],
                    area: ['40%', '90%'],
                    success: function (layero, index) {
                        curIndex = index;
                    }
                })
            },
            openEditBankStatementModal(mprid) {
                let url = '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId + "&mprid=" + mprid;
                layer.open({
                    type: 2,
                    title: '手动编辑流水',
                    content: [url],
                    area: ['40%', '90%'],
                    success: function (layero, index) {
                        curIndex = index;
                    }
                })
            },
            getThisTimeRepayment() {
                axios.get(fpath + 'finance/thisTimeRepayment?businessId=' + businessId + "&afterId=" + afterId)
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.thisTimeRepaymentInfo = Object.assign(app.thisTimeRepaymentInfo, res.data.data);
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: '获取本次还款信息失败'
                        })
                    })
            },
            getThisPeroidRepayment() {
                axios.get(fpath + 'finance/thisPeroidRepayment?businessId=' + businessId + "&afterId=" + afterId)
                    .then(function (res) {
                        if (res.data.code == '1') {
                            res.data.data.forEach((e) => {
                                app.table.currPeriodRepayment.data.push(e)
                            })

                            let chaer = {}
                            chaer.item10 = 0
                            chaer.item20 = 0
                            chaer.item30 = 0
                            chaer.item50 = 0
                            chaer.subTotal = 0
                            chaer.offlineOverDue = 0
                            chaer.onlineOverDue = 0
                            chaer.total = 0
                            chaer.type = '差额'
                            let e1 = res.data.data[0]

                            if (res.data.data.length > 1) {
                                res.data.data.forEach((element, index) => {
                                    if (index > 0) {
                                        chaer.item10 += element.item10 ? element.item10 : 0
                                        chaer.item20 += element.item20 ? element.item20 : 0
                                        chaer.item30 += element.item30 ? element.item30 : 0
                                        chaer.item50 += element.item50 ? element.item50 : 0
                                        chaer.subTotal += element.subTotal ? element.subTotal : 0
                                        chaer.offlineOverDue += element.offlineOverDue ? element.offlineOverDue : 0
                                        chaer.onlineOverDue += element.onlineOverDue ? element.onlineOverDue : 0
                                        chaer.total += element.total ? element.total : 0
                                    }
                                });

                                chaer.item10 = (chaer.item10 * 10000 - e1.item10 * 10000) / 10000
                                chaer.item20 = (chaer.item20 * 10000 - e1.item20 * 10000) / 10000
                                chaer.item30 = (chaer.item30 * 10000 - e1.item30 * 10000) / 10000
                                chaer.item50 = (chaer.item50 * 10000 - e1.item50 * 10000) / 10000
                                chaer.subTotal = (chaer.subTotal * 10000 - e1.subTotal * 10000) / 10000
                                chaer.offlineOverDue = (chaer.offlineOverDue * 10000 - e1.offlineOverDue * 10000) / 10000
                                chaer.onlineOverDue = (chaer.onlineOverDue * 10000 - e1.onlineOverDue * 10000) / 10000
                                chaer.total = (chaer.total * 10000 - e1.total * 10000) / 10000
                            } else {
                                chaer.item10 = -e1.item10
                                chaer.item20 = -e1.item20
                                chaer.item30 = -e1.item30
                                chaer.item50 = -e1.item50
                                chaer.subTotal = -e1.subTotal
                                chaer.offlineOverDue = -e1.offlineOverDue
                                chaer.onlineOverDue = -e1.onlineOverDue
                                chaer.total = -e1.total
                            }

                            app.table.currPeriodRepayment.data.push(chaer)
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: '获取本期还款信息失败'
                        })
                    })
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
            previewConfirmRepayment() {
                let param = {};
                param.businessId = businessId;
                param.afterId = afterId;
                param = Object.assign(app.factRepaymentInfo, param);
                param.callFlage = 10

                axios.post(fpath + 'finance/previewConfirmRepayment', param)
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.handleConfirmRepaymentResult(res)
                        } else {
                            app.factRepaymentInfo.surplusFund = 0
                            app.$Message.error({content: res.data.msg})
                        }
                    })
                    .catch(function (err) {
                        console.log(err);
                    })
            },
            handleConfirmRepaymentResult(res) {
                app.table.projRepayment.data = res.data.data
                app.factRepayPreview.flag = true
                app.factRepayPreview.item10 = 0
                app.factRepayPreview.item20 = 0
                app.factRepayPreview.item30 = 0
                app.factRepayPreview.item50 = 0
                app.factRepayPreview.offlineOverDue = 0
                app.factRepayPreview.onlineOverDue = 0
                app.factRepayPreview.subTotal = 0
                app.factRepayPreview.total = 0
                app.factRepayPreview.surplus = 0
                app.table.projRepayment.data.forEach(e => {
                    app.factRepayPreview.surplus += e.surplus * 10000 / 10000
                    app.factRepayPreview.item10 = (app.factRepayPreview.item10 * 10000 + e.item10 * 10000) / 10000
                    app.factRepayPreview.item20 = (app.factRepayPreview.item20 * 10000 + e.item20 * 10000) / 10000
                    app.factRepayPreview.item30 = (app.factRepayPreview.item30 * 10000 + e.item30 * 10000) / 10000
                    app.factRepayPreview.item50 = (app.factRepayPreview.item50 * 10000 + e.item50 * 10000) / 10000
                    app.factRepayPreview.offlineOverDue = (app.factRepayPreview.offlineOverDue * 10000 + e.offlineOverDue * 10000) / 10000
                    app.factRepayPreview.onlineOverDue = (app.factRepayPreview.onlineOverDue * 10000 + e.onlineOverDue * 10000) / 10000
                    app.factRepayPreview.subTotal = (app.factRepayPreview.subTotal * 10000 + e.subTotal * 10000) / 10000
                    app.factRepayPreview.total += (app.factRepayPreview.total * 10000 + e.total * 10000) / 10000
                })

                app.factRepayPreview.total = (app.factRepayPreview.subTotal * 10000
                    + app.factRepayPreview.offlineOverDue * 10000
                    + app.factRepayPreview.onlineOverDue * 10000
                    + app.factRepayPreview.surplus * 10000) / 10000
            },
            confirmRepayment() {
                let param = {};
                param.businessId = businessId;
                param.afterId = afterId;
                param.reqFlag=1;
                param = Object.assign(app.factRepaymentInfo, param);
                if ((!param.mprIds || param.mprIds == '') && (!param.surplusFund || param.surplusFund == 0)) {
                    app.$Message.error({content: '不能提交没匹配流水且没使用结余金额的还款确认'})
                    return;
                }
                var repayAccount = app.factRepaymentInfo.repayAccount;
                var total = app.factRepayPreview.total;
                console.log("repayAccount=", repayAccount);
                console.log("total=", total);

                if (repayAccount < total) {
                    app.$Message.error({content: '实还明细金额不能大于还款来源金额'})
                    return;
                }

                layer.confirm('确认本次还款?', {icon: 3, title: '提示'}, function (index) {
                    axios.post(fpath + 'finance/confirmRepayment', param)
                        .then(function (res) {
                            if (res.data.code == '1') {
                                app.handleConfirmRepaymentResult(res);

                                app.$Modal.success({
                                    content: '还款确认成功!', onOk() {
                                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                        parent.layer.close(index);
                                        parent.app.search()
                                    }
                                })
                            } else {
                                app.factRepaymentInfo.surplusFund = 0
                                app.$Message.error({content: res.data.msg})
                            }
                        })
                        .catch(function (err) {
                            console.log(err);
                        })
                    layer.close(index);
                })



            },
            onOverDueChange(e) {
                console.log(e.target.value);
                //调用预览还款的计算方法
                previewConfirmRepayment();

            },

            onsurplusFoundChange(e) {

            },
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
                    })
                    .catch(function (err) {
                    })
            },
            reject() {
                axios.post(fpath + 'finance/rejectRepayReg', {
                    mprid: app.repayRegList.mprid,
                    remark: app.repayRegList.remark
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
                        console.log(err);
                    })
            },
            getRepayRegList() {
                axios.get(cpath + 'moneyPool/checkMoneyPool', {
                    params: {
                        businessId: businessId,
                        afterId: afterId,
                        isMatched: false,
                    }
                })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            let sumMoney = 0;
                            res.data.data.forEach(element => {
                                sumMoney += element.accountMoney
                            });
                            let sum = {
                                moneyPoolId: '合计',
                                accountMoney: sumMoney
                            }
                            app.table.repayRegList.data = res.data.data
                            app.table.repayRegList.data.push(sum)

                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    }).catch(function (err) {
                    console.log(err);
                    app.$Message.error({
                        content: '获取登记还款信息列表数据失败'
                    })
                })
            },
            getConfirmedList() {
                axios.get(fpath + 'finance/selectConfirmedBankStatement', {
                    params: {
                        businessId: businessId,
                        afterId: afterId,
                    }
                })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.table.confirmedRepayment.data = res.data.data
                            let t = 0;
                            app.table.confirmedRepayment.data.forEach(element => {
                                t += element.accountMoney
                            });
                            let sum = {
                                moneyPoolId: '合计',
                                accountMoney: t
                            }

                            app.table.confirmedRepayment.data.push(sum)
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    }).catch(function (err) {
                    app.$Message.error({
                        content: '查询已完成的还款信息数据失败'
                    })
                })
            },
            disMatchedStatement(p) {
                app.$Modal.confirm({
                    content: '确认取消关联此条流水?',
                    onOk() {
                        axios.post(fpath + 'finance/disMatchBankStatement', {
                            mpid: p.row.moneyPoolId
                        })
                            .then(function (r) {
                                if (r.data.code == "1") {
                                    window.location.reload()
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
                })
            },
            deleteMoneyPool(p) {
                app.$Modal.confirm({
                    content: '确认删除此条流水?',
                    onOk() {
                        axios.get(fpath + 'finance/deleteMoneyPool', {
                            params: {
                                mprId: p.row.mprId
                            }
                        })
                            .then(function (r) {
                                if (r.data.code == "1") {
                                    window.location.reload()
                                } else {
                                    app.$Message.error({
                                        content: r.data.msg
                                    })
                                }
                            })
                            .catch(function (e) {
                                app.$Message.error({
                                    content: '删除此条流水失败'
                                })
                            })
                    }
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
                            app.matchedBankStatement.data = res.data.data.slice(0)
                            app.table.matchedBankStatement.data = res.data.data.slice(0);
                            let t = 0;
                            app.table.matchedBankStatement.data.forEach(element => {
                                t += element.accountMoney
                            });
                            let sum = {
                                moneyPoolId: '合计',
                                accountMoney: t
                            }

                            app.table.matchedBankStatement.data.push(sum)

                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    }).catch(function (err) {
                    app.$Message.error({
                        content: '获取匹配的款项池银行流水数据失败'
                    })
                })
            }
        },
        created: function () {
            this.getThisTimeRepayment()
            this.getThisPeroidRepayment()
            this.getSurplusFund()
            this.getBaseInfo()
            this.getRepayRegList()
            this.getConfirmedList()
            this.getMatched()
        }
    })
})
