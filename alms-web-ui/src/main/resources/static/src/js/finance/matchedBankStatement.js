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
                col: [{
                    title: '流水号',
                    key: 'moneyPoolId',
                    ellipsis:true,
                    width:250
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
                    width:150,
                    render: (h, p) => {

                        if (p.row.moneyPoolId!='合计'&&p.row.status=='待领取') {
                            return h('i-button', {
                                    props:{
                                        size:'small'
                                    },
                                    on: {
                                        click: function () {
                                            app.disMatchedStatement(p)
                                        }
                                    }
                                },
                                '取消关联'
                            )
                        } else if(p.row.moneyPoolId!='合计'&&p.row.status=='已领取') {
                            return h('div',[
                                h('i-button',{
                                    props:{
                                        size:'small'
                                    },
                                    on:{
                                        click(){
                                            app.openEditBankStatementModal(p.row.mprId)
                                        }
                                    },
                                    style:{
                                        marginRight:'10px'
                                    }
                                },'编辑流水'),
                                h('i-button',{
                                    props:{
                                        size:'small'
                                    },
                                    on:{
                                        click(){
                                            app.deleteMoneyPool(p)
                                        }
                                    }
                                },'删除')
                            ])
                        }

                    }
                }],
                data: []
            }
        },
        methods: {
            handleParentStyle: function (style) {
                window.parent.app.configModalStyle('matchedBankStatement', style)
            },
            openMatchBankStatementModal(){
                window.parent.app.openMatchBankStatementModal({
                    businessId:businessId,
                    afterId:afterId
                })
            },
            openAddBankStatementModal(){
                parent.app.openAddBankStatementModal();
            }
            ,openEditBankStatementModal(mprid){
                parent.app.openEditBankStatementModal(mprid);
            },
            disMatchedStatement(p){
                app.$Modal.confirm({
                    content:'确认取消关联此条流水?',
                    onOk() {
                        axios.post(fpath + 'finance/disMatchBankStatement', {
                                mpid: p.row.moneyPoolId
                            })
                            .then(function (r) {
                                if (r.data.code == "1") {
                                    parent.location.reload()
                                } else {
                                    parent.app.$Message.error({
                                        content: r.data.msg
                                    })
                                }
                            })
                            .catch(function (e) {
                                parent.app.$Message.error({
                                    content: '取消关联银行流水失败'
                                })
                            })
                    }
                })
            },
            deleteMoneyPool(p){
                app.$Modal.confirm({
                    content:'确认删除此条流水?',
                    onOk() {
                        axios.get(fpath + 'finance/deleteMoneyPool', {
                                params: {
                                    mprId: p.row.mprId
                                }
                            })
                            .then(function (r) {
                                if (r.data.code == "1") {
                                    parent.location.reload()
                                } else {
                                    parent.app.$Message.error({
                                        content: r.data.msg
                                    })
                                }
                            })
                            .catch(function (e) {
                                parent.app.$Message.error({
                                    content: '删除此条流水失败'
                                })
                            })
                    }
                })
            }
            
        },
        created: function () {
            axios.get(cpath + 'moneyPool/checkMoneyPool', {
                    params: {
                        businessId: businessId,
                        afterId: afterId,
                        isMatched: true,
                        noConfirmed:true
                    }
                })
                .then(function (res) {
                    if (res.data.code == '1') {
                        //parent.app.matchedBankStatement.data = res.data.data.slice(0) ;
                        res.data.data.forEach(e=>{
                            if(e.status!='完成'&&e.status!='已完成'){
                                parent.app.matchedBankStatement.data.push(e)
                            }
                        })
                        app.table.data = res.data.data.slice(0);
                        let t = 0;
                        app.table.data.forEach(element => {
                            t += element.accountMoney
                        });
                        let sum = {
                            moneyPoolId: '合计',
                            accountMoney: t
                        }

                        app.table.data.push(sum)

                        let height = 20 ;
                        app.table.data.forEach(e=>{
                            if(e.remark&&e.remark.length>16){
                                height = height*(e.remark.length/16)
                            }
                        })

                        console.log(res.data.data);
                        let style = {
                            height: (app.table.data.length ? (app.table.data.length + 2) * height : 175) + 'px',
                            width: '100%'
                        }
                        app.handleParentStyle(style)
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
        }
    })
})