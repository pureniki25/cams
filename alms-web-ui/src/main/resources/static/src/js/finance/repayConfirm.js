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

            // style: {
            //     repayRegList: {},
            //     matchedBankStatement: {}
            // },

            table: {
                currPeriodRepayment: {
                    col: [{
                        title: '展开',
                        type: 'expand',
                        render: (h, p) => {
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
                        key: 'subtotal'
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
                projRepayment: projRepayment
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
                surplus: 0
            }
        },
        watch: {
            'matchedBankStatement.data': function (n) {
                let moneyPoolAccount = 0
                if (n && n.length > 0) {
                    n.forEach(element => {
                        moneyPoolAccount += element.accountMoney
                        app.factRepaymentInfo.mprIds.push(element.mprId);
                    });
                    let o = n[n.length - 1]
                    app.factRepaymentInfo.repayDate = o.tradeDate
                }
                app.factRepaymentInfo.moneyPoolAccount = moneyPoolAccount
                app.factRepaymentInfo.repayAccount = parseFloat(app.factRepaymentInfo.moneyPoolAccount)  + parseFloat(app.factRepaymentInfo.surplusFund || 0)
            },
            'factRepaymentInfo.useSurplusflag':function(n,o){
                if(o==''){
                    return ;
                }
                app.factRepaymentInfo.surplusFund = 0
            },
            'factRepaymentInfo.surplusFund': function (n) {
                if (n && !isNaN(n)) {
                    if(n>app.factRepaymentInfo.canUseSurplus){
                        app.$Modal.warning({content:'可使用结余金额不能大于'+app.factRepaymentInfo.canUseSurplus})
                        app.factRepaymentInfo.surplusFund = 0
                        return;
                    }
                    app.factRepaymentInfo.repayAccount = parseFloat(app.factRepaymentInfo.moneyPoolAccount) + parseFloat(app.factRepaymentInfo.surplusFund || 0)
                }
            },
            // 'factRepayPreview.offlineOverDue':function(n){
            //     if(n&&!isNaN(n)){
            //         if(n>app.thisTimeRepaymentInfo.offlineOverDue){
            //             app.$Message.warning({content:'线下逾期费不能超过'+app.thisTimeRepaymentInfo.offlineOverDue+'元'});
            //             app.factRepayPreview.offlineOverDue = 0 ;
            //             return ;
            //         }
            //         app.factRepaymentInfo.offlineOverDue = n ;
            //         if(app.factRepayPreview.flag){
            //             app.factRepayPreview.flag = !app.factRepayPreview.flag
            //             return ;
            //         }
            //         app.previewConfirmRepayment()
            //     }
            // },
            // 'factRepayPreview.onlineOverDue':function(n){
            //     if(n&&!isNaN(n)){
            //         if(n>app.thisTimeRepaymentInfo.onlineOverDue){
            //             app.$Message.warning({content:'线上逾期费不能超过'+app.thisTimeRepaymentInfo.onlineOverDue+'元'});
            //             app.factRepayPreview.onlineOverDue = 0 ;
            //             return ;
            //         }
            //         app.factRepaymentInfo.onlineOverDue = n ;
            //         if(app.factRepayPreview.flag){
            //             app.factRepayPreview.flag = !app.factRepayPreview.flag
            //             return ;
            //         }
            //         app.previewConfirmRepayment()
            //     }
            // },
            // thisTimeRepaymentInfo.onlineOverDue
            'factRepaymentInfo.repayAccount': function (n) {
                app.previewConfirmRepayment()
            }
        },
        methods: {
            closePage(){
                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
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
                    area: ['80%', '90%'],
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
                let url = '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId+"&mprid="+mprid;
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
                            res.data.data.forEach((e)=>{
                                app.table.currPeriodRepayment.data.push(e)
                            })

                            let chaer = {}
                            chaer.type = '差额'
                            let e1 = res.data.data[0]

                            if(res.data.data.length>1){
                                res.data.data.forEach((element,index) => {
                                    if (index > 0) {
                                        chaer.item10 = e1.item10 - element.item10
                                        chaer.item20 = e1.item20 - element.item20
                                        chaer.item30 = e1.item30 - element.item30
                                        chaer.item50 = e1.item50 - element.item50
                                        chaer.subtotal = e1.subtotal - element.subtotal
                                        chaer.offlineOverDue = e1.offlineOverDue - element.offlineOverDue
                                        chaer.onlineOverDue = e1.onlineOverDue - element.onlineOverDue
                                        chaer.total = e1.total - element.total
                                    } 
                                });
                            }else {
                                chaer.item10 = e1.item10
                                chaer.item20 = e1.item20
                                chaer.item30 = e1.item30
                                chaer.item50 = e1.item50
                                chaer.subtotal = e1.subtotal
                                chaer.offlineOverDue = e1.offlineOverDue
                                chaer.onlineOverDue = e1.onlineOverDue
                                chaer.total = e1.total
                            }
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

                axios.post(fpath + 'finance/previewConfirmRepayment', param)
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.handleConfirmRepaymentResult(res)
                        }else{
                            app.factRepaymentInfo.surplusFund = 0
                            app.$Message.error({content:res.data.msg})
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
                        app.factRepayPreview.surplus += parseFloat(e.surplus.toFixed(2))
                        app.factRepayPreview.item10 += parseFloat(e.item10.toFixed(2))
                        app.factRepayPreview.item20 += parseFloat(e.item20.toFixed(2))
                        app.factRepayPreview.item30 += parseFloat(e.item30.toFixed(2))
                        app.factRepayPreview.item50 += parseFloat(e.item50.toFixed(2))
                        app.factRepayPreview.offlineOverDue += parseFloat(e.offlineOverDue.toFixed(2))
                        app.factRepayPreview.onlineOverDue += parseFloat(e.onlineOverDue.toFixed(2))
                        app.factRepayPreview.subTotal += parseFloat(e.subTotal.toFixed(2))
                        app.factRepayPreview.total += parseFloat(e.total.toFixed(2))
                    })

                    // app.factRepayPreview.surplus = parseFloat(app.factRepayPreview.surplus.toFixed(2))
                    // app.factRepayPreview.subTotal = parseFloat(app.factRepayPreview.subTotal.toFixed(2))
                    app.factRepayPreview.total = app.factRepayPreview.subTotal + app.factRepayPreview.surplus
                },
                confirmRepayment() {
                    let param = {};
                    param.businessId = businessId;
                    param.afterId = afterId;
                    param = Object.assign(app.factRepaymentInfo, param);
                    
                    app.$Modal.confirm({
                        content:'确认本次还款?',
                        onOk(){
                            axios.post(fpath + 'finance/confirmRepayment', param)
                            .then(function (res) {
                                if (res.data.code == '1') {
                                    app.handleConfirmRepaymentResult(res)
                                    
                                    window.location.reload()
                                }else{
                                    app.factRepaymentInfo.surplusFund = 0
                                    app.$Message.error({content:res.data.msg})
                                }
                            })
                            .catch(function (err) {
                                console.log(err);
                            })
                        }
                    })
                    
                    
            },
            onOfflineOverDueChange(e){
                console.log(e.target.value);
            }
        },
        created: function () {
            this.getThisTimeRepayment()
            this.getThisPeroidRepayment()
            this.getSurplusFund()
        }
    })
})
