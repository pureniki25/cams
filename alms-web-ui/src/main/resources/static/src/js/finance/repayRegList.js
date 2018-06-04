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
    app = new Vue({
        el: "#app",
        data: {
            spinShow: true,
            table: {
                col: [
                    {
                        title: '流水号',
                        key: 'moneyPoolId',
                        ellipsis:true,
                        width:250
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
                                        window.open('http://xiaodaioa.oss-cn-beijing.aliyuncs.com/'+p.row.certificatePictureUrl);
                                    }
                                }
                            }, '查看凭证')

                            let Null = h('span', {
                            }, '无')

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
                                        console.log(p.row);
                                        let repayDate = moment(p.row.tradeDate).format('YYYY-MM-DD');
                                        let accountMoney = p.row.accountMoney;
                                        let acceptBank = p.row.bankAccount;
                                        let mrpid = p.row.id
                                        window.parent.app.manualMatchBankSatements.show = true;
                                        window.parent.app.manualMatchBankSatements.url = '/finance/manualMatchBankSatements?businessId=' 
                                        + businessId + "&afterId=" 
                                        + afterId + '&repayDate='
                                        + repayDate + '&accountMoney='
                                        + accountMoney + '&acceptBank=' 
                                        + acceptBank  +'&timestamp=' 
                                        + new Date().getTime()+'&mprid='
                                        + mrpid;
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
                                on:{
                                    click(){
                                        let mprid = p.row.id 
                                        axios.post(fpath+'finance/rejectRepayReg',{mprid:mprid})
                                        .then(function(res){
                                            if(res.data.code=='1'){
                                                window.location.reload()
                                            }else{
                                                window.parent.app.$Message.error({content:res.data.msg})
                                            }
                                        })
                                        .catch(function(err){
                                            window.parent.app.$Message.error({content:'拒绝还款登记失败'})
                                            console.log(err);
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
                    }
                ],
                data: []
            }
        },
        methods: {
            handleParentStyle: function (style) {
                window.parent.app.configModalStyle('repayRegList', style)
            }
        },
        created: function () {
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
                        app.table.data = res.data.data
                        app.table.data.push(sum)
                        let style = {
                            height: (app.table.data.length ? (app.table.data.length + 2) * 50 : 175) + 'px',
                            width: '100%'
                        }
                        app.handleParentStyle(style)
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