/**
 * 
 */

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;

    let t1 = new Vue({
        el: "#t1",
        data: {
            self: this,
            cert_modal: false,
            cert_modal_url: '',
            columns1: [
                {
                    title: '流水号',
                    key: 'moneyPoolId'
                },
                {
                    title: '还款日期',
                    key: 'tradeDate',
                    render:function(h,o){
                        return h('span',o.row.tradeDate?moment(o.row.tradeDate).format('YYYY年MM月DD日'):'')
                    }
                },
                {
                    title: '还款金额',
                    key: 'accountMoney',
                    align:'right',
                    render:function(h,o){
                        return h('span',o.row.accountMoney?numeral(o.row.accountMoney).format('0,0.00'):'')
                    }
                },
                {
                    title: '实际还款人',
                    key: 'factTransferName'
                },
                {
                    title: '转入账号',
                    key: 'bankAccount'
                },
                {
                    title: '交易类型',
                    key: 'tradeType'
                },
                {
                    title: '交易场所',
                    key: 'tradePlace'
                },
                {
                    title: '状态',
                    key: 'state'
                },
                {
                    title: '更新用户',
                    key: 'updateUser'
                },
                {
                    title: '更新时间',
                    key: 'updateTime',
                    render:function(h,o){
                        return h('span',o.row.updateTime?moment(o.row.updateTime).format('YYYY年MM月DD日'):'')
                    }
                },
                {
                    title: '操作',
                    key: 'operate',
                    render: (h, params) => {
                        return h('button', {
                            class:[
                                'layui-btn',
                                'layui-btn-sm'
                            ],
                            props: {
                            },
                            on: {
                                click: () => {
                                    if (params.row.certificatePictureUrl && params.row.certificatePictureUrl.length) {
                                        t1.cert_modal_url = 'http://xiaodaioa.oss-cn-beijing.aliyuncs.com/' + params.row.certificatePictureUrl
                                        t1.cert_modal = true
                                    } else {
                                        t1.$Modal.warning({ content: '此登记暂无凭证' })
                                    }
                                }
                            }
                        }, '查看凭证');
                    }
                }

            ],
            data1: []
        },
       created: () => {
           console.log(this.businessId)
           axios.get(basePath + 'moneyPool/checkMoneyPool', {
               params: {
                   businessId: getQueryStr("businessId"),
                   afterId: getQueryStr("afterId"),
                   isMatched: false
               }
           })
               .then(function (res) {
                   if (res.data.code == '1') {
                       t1.data1 = res.data.data
                   } else {
                       t1.$Modal.error({ content: '接口调用失败' })
                   }
               })
               .catch(function (error) {
                   t1.$Modal.error({ content: '接口调用失败' })
                   console.log(error);
               });
       },
        methods: {

        }
    })

    let t2 = new Vue({
        el: "#t2",
        data: {
            self: this,
            columns1: [
                {
                    title: '流水号',
                    key: 'moneyPoolId'
                },
                {
                    title: '款项码',
                    key: 'repaymentCode'
                },
                {
                    title: '转入账户',
                    key: 'bankAccount'
                },
                {
                    title: '转入时间',
                    key: 'tradeDate',
                    render:function(h,o){
                        return h('span',o.row.tradeDate?moment(o.row.tradeDate).format('YYYY年MM月DD日'):'') ;
                    }
                },
                {
                    title: '交易类型',
                    key: 'tradeType'
                },
                {
                    title: '交易场所',
                    key: 'tradePlace'
                },
                {
                    title: '记账金额',
                    key: 'accountMoney',
                    align:'right',
                    render:function(h,o){
                        return h('span',o.row.accountMoney?numeral(o.row.accountMoney).format('0,0.00'):'')
                    }
                },
                {
                    title: '交易备注',
                    key: 'remark'
                },
                {
                    title: '摘要',
                    key: 'summary'
                },
                {
                    title: '状态',
                    key: 'status'
                }
            ],
            data1: []
        },
        created: () => {
            //取区域列表
            axios.get(basePath + 'moneyPool/checkMoneyPool', {
                params: {
                    businessId: getQueryStr("businessId"),
                    afterId: getQueryStr("afterId"),
                    isMatched:true
                }
            })
            .then(function (res) {
                if (res.data.code == '1') {
                    t2.data1 = res.data.data

                } else {
                    t2.$Modal.error({ content: '接口调用失败' })
                }
            })
            .catch(function (error) {
                t2.$Modal.error({ content: '接口调用失败' })
                console.log(error);
            });
        },
        methods: {

        }
    })

});