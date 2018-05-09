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
                        key: 'moneyPoolId'
                    }, {
                        title: '还款日期',
                        key: 'tradeDate',
                        render: (h, p) => {
                            return h('span', moment(p.row.tradeDate).format('YYYY-MM-DD'))
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
                                        window.open(p.row.certificatePictureUrl);
                                    }
                                }
                            }, '查看凭证')

                            let Null = h('span', {
                            }, '无')

                            let content = [];
                            if (p.row.certificatePictureUrl) {
                                content.push(checkCert)
                            }else{
                                content.push(Null)
                            }
                            return h('div', content)
                        }
                    },{
                        title:'操作',
                        width:160,
                        render:(h,p)=>{
                            let match = h('i-button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                }
                            }, '匹配')
                            let dismatch = h('i-button', {
                                props: {
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                }
                            }, '拒绝')
                            let content = []
                            if(p.row.state=='未关联银行流水'){
                                content.push(match)
                                content.push(dismatch)
                            }
                            return h('div',content)
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
                    console.log(res);
                    if (res.data.code == '1') {
                        app.table.data = res.data.data
                        let style = {
                            height: (app.table.data.length ? (app.table.data.length + 2) * 50 : 175 )+ 'px',
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