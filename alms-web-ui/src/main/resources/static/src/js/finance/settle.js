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
            submitLoding:false,
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
                otherFees:[]
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
                                            area: ['95%','95%'],
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
                        width:150,
                        render: (h, p) => {

                            if (p.row.moneyPoolId != '合计' && p.row.status == '待领取') {
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
                },
                projRepayment: {
                    col:[
                        {
                            title:'借款人',
                            key:'userName',
                            render:(h,p)=>{
                                return h('span',p.row.master?p.row.userName+'(主借)':p.row.userName)
                            }
                        },{
                            title:'上标金额',
                            key:'projAmount'
                        },{
                            title:'本金',
                            key:'item10'
                        },{
                            title:'利息',
                            key:'item20'
                        },{
                            title:'月收分公司服务费',
                            key:'item30'
                        },{
                            title:'月收平台费',
                            key:'item50'
                        },{
                            title:'线下逾期费',
                            key:'offlineOverDue'
                        },{
                            title:'线上逾期费',
                            key:'onlineOverDue'
                        },{
                            title:'结余',
                            key:'surplus'
                        },{
                            title:'提前结清违约金',
                            key:'item70'
                        },
                        {
                            title:'合计',
                            key:'total'
                        }
                    ],
                    data:[]
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
                surplus: 0,
                penalty:0
            }
        },
        watch: {
            'matchedBankStatement': function (n) {
                let moneyPoolAccount = 0
                if (n && n.length > 0) {
                    n.forEach(element => {
                        moneyPoolAccount = accAdd(moneyPoolAccount,element.accountMoney)
                        app.factRepaymentInfo.mprIds.push(element.mprId);
                    });
                    let o = n[n.length - 1]
                    app.factRepaymentInfo.repayDate = o.tradeDate
                }
                app.factRepaymentInfo.moneyPoolAccount = moneyPoolAccount
                app.factRepaymentInfo.repayAccount = accAdd(app.factRepaymentInfo.moneyPoolAccount,(app.factRepaymentInfo.surplusFund||0))
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
                    app.factRepaymentInfo.repayAccount = accAdd(app.factRepaymentInfo.moneyPoolAccount,(app.factRepaymentInfo.surplusFund||0))
                }
            },
            'factRepaymentInfo.repayAccount': function (n) {
                app.previewSettle()
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
                                sumMoney = accAdd(sumMoney,element.accountMoney)
                            });
                            let sum = {
                                moneyPoolId: '合计',
                                accountMoney: sumMoney
                            }
                            app.table.reg.data = res.data.data
                            app.table.reg.data.push(sum)

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
                                t = accAdd(t,element.accountMoney)
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
            surplusFundEnter(e){
                var n= e.target.value;
                  if ((n || n == 0 ) && !isNaN(n)) {
                      if (n > app.factRepaymentInfo.canUseSurplus) {
                          app.$Modal.warning({content: '可使用结余金额不能大于' + app.factRepaymentInfo.canUseSurplus})
                          app.factRepaymentInfo.surplusFund = 0
                          return;
                      }
                      app.factRepaymentInfo.repayAccount = accAdd(app.factRepaymentInfo.moneyPoolAccount,(app.factRepaymentInfo.surplusFund || 0))
                  }
              },
            openMatchBankStatementModal(p) {
                let url = '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动匹配流水',
                    content: [url, 'no'],
                    area: ['95%','95%'],
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
                    area: ['40%','90%'],
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
            reGetSttleInfo(){
                let param = {}
                if(app.thisTimeRepaymentInfo.otherFees){
                    param.otherFees = app.thisTimeRepaymentInfo.otherFees
                }
                app.getSettleInfo(param)
                if(app.factRepaymentInfo.repayAccount&&app.factRepaymentInfo.repayAccount>0){
                    app.previewSettle()
                }
            },
            getSettleInfo(p) {
                let param = p||{}
                param.businessId = businessId ;
                param.afterId = afterId ;
                if(planId){
                    param.planId = planId
                }
                axios.post(fpath + 'settle/settleInfo',param)
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
                            app.thisTimeRepaymentInfo.penaltyFeesBiz = data.penaltyFeesBiz
                            app.thisTimeRepaymentInfo.other = data.other
                            if(param.otherFees){
                                app.thisTimeRepaymentInfo.otherFees = data.otherFees
                            }
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
            settle(params,cb){
                if(app.thisTimeRepaymentInfo.otherFees){
                    params.otherFees = app.thisTimeRepaymentInfo.otherFees
                }
                app.$Message.loading({
                    content:'请稍后...',
                    duration:0
                })
                app.submitLoding = true
                axios.post(fpath+'finance/financeSettle',params)
                .then(function(res){
                    app.$Message.destroy()
                    app.submitLoding = false
                    cb(res)
                })
                .catch(function(err){
                    app.$Message.error({content:'结清功能异常'})
                })
            },
            handleSettleResult(res){
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
                app.factRepayPreview.item70 = 0 
                app.factRepayPreview.otherMoney = 0 

                app.table.projRepayment.data.forEach(e => {
                    app.factRepayPreview.surplus = accAdd(app.factRepayPreview.surplus,e.surplus)
                    app.factRepayPreview.item10 = accAdd(app.factRepayPreview.item10,e.item10)
                    app.factRepayPreview.item20 = accAdd(app.factRepayPreview.item20,e.item20)
                    app.factRepayPreview.item30 = accAdd(app.factRepayPreview.item30,e.item30)
                    app.factRepayPreview.item50 = accAdd(app.factRepayPreview.item50,e.item50)
                    app.factRepayPreview.offlineOverDue = accAdd(app.factRepayPreview.offlineOverDue,e.offlineOverDue)
                    app.factRepayPreview.onlineOverDue = accAdd(app.factRepayPreview.onlineOverDue,e.onlineOverDue)
                    app.factRepayPreview.subTotal = accAdd(app.factRepayPreview.subTotal,e.subTotal)
                    app.factRepayPreview.item70 = accAdd(app.factRepayPreview.item70,e.item70)
                    app.factRepayPreview.total = accAdd(app.factRepayPreview.total,e.total)
                    app.factRepayPreview.otherMoney = accAdd(app.factRepayPreview.otherMoney,e.otherMoney)
                })
                app.factRepayPreview.total = accAdd(
                accAdd(
                    accAdd(
                        accAdd(
                            app.factRepayPreview.subTotal
                            ,app.factRepayPreview.offlineOverDue)
                        ,accAdd(app.factRepayPreview.onlineOverDue
                            ,app.factRepayPreview.surplus))
                    ,app.factRepayPreview.item70),app.factRepayPreview.otherMoney)
            },
            previewSettle(){
                let params = {}
                params.businessId = businessId
                params.afterId = afterId
                if(planId){
                    params.planId = planId
                }
                params.preview=true

                params = Object.assign(app.factRepaymentInfo, params);
                app.settle(params,app.handleSettleResult)
            },
            submitSettle(){
                
                let params = {}
                params.businessId = businessId
                params.afterId = afterId
                if(planId){
                    params.planId = planId
                }
                params.preview=false

                params = Object.assign(app.factRepaymentInfo, params);

                if(app.thisTimeRepaymentInfo.item10>app.factRepayPreview.item10){
                    app.$Modal.confirm({
                        content:'确认 亏损结清 ?',
                        onOk:function(){
                            params.deficit = true ;
                            app.settle(params,app.handleSettleResult)
                        },
                        onCancel:function(){
                            location.reload()
                        }
                    })
                }else{
                    app.settle(params,app.handleSettleResult)
                }
                

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
            listOtherFee(){
                axios.get(fpath+'settle/listOtherFee?businessId=' 
                + businessId 
                +(planId?('&planId='+planId):''))
                .then(function(res){
                    console.log(res);
                    if(res.data.code=='1'){
                        app.thisTimeRepaymentInfo.otherFees = res.data.data 
                    }else{
                        app.$Message.error({
                            content:res.data.msg
                        })
                    }
                })
                .catch(function(err){
                    console.error(err)
                })
            },
            recalcSettleInfo(){
                /* 重新计算应还信息 */

            }
            
        },
        created: function () {
            this.getBaseInfo()
            this.getRepayRegList()
            this.getMatched()
            this.listRepayment()
            this.getSettleInfo()
            this.listOtherFee()
        }
    })
})
