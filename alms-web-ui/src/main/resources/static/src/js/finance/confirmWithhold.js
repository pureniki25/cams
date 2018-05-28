/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    let businessId = getQueryStr("businessId")
    let afterId = getQueryStr('afterId')
    console.log(cpath);
    console.log(fpath);
    app = new Vue({
        el: "#app",
        data: {
            table: {
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
                    title: '月收分公司服务费',
                    key: 'item30'
                }, {
                    title: '月收平台费',
                    key: 'item50'
                }, {
                    title: '还款合计',
                    key: 'total'
                }, {
                    title: '状态',
                    render: (h, p) => {
                        let color
                        if (p.row.status == '已确认') {
                            color = 'green'
                        }
                        return h('Tag', {
                            nativeOn: {
                                click() {
                                    if (p.row.status == '待确认') {
                                        app.openConfirmModal(p)
                                    }
                                }
                            },
                            props: {
                                color: color
                            }
                        }, p.row.status)
                    }
                }],
                data: []
            },
            baseInfo: {}
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
                            app.baseInfo = Object.assign(res.data.data, app.baseInfo);
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: err
                        })
                    })
            },
            listData() {
                axios.get(fpath + 'finance/listConfirmWithhold', {
                        params: {
                            businessId: businessId
                        }
                    })
                    .then(function (res) {
                        if (res.data.code == '1') {
                            app.table.data = res.data.data
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            });
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: err
                        });
                    })
            },
            confirmWithhold: function (o) {
                let p = {
                    businessId: businessId
                }
                if (o) {
                    p.afterId = o.row.afterId
                }
                axios.get(fpath + 'finance/confirmWithhold', {
                        params: p
                    })
                    .then(function (res) {
                        if (res.data.code == "1") {
                            app.listData()
                        } else {
                            app.$Message.error({
                                content: res.data.msg
                            })
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({
                            content: err
                        })

                    })
            },
            openConfirmModal(p) {
                let desc = '是否对' + businessId + '业务的代扣金额进行确认?'
                if (p) {
                    desc = '是否对' + p.row.afterId + '期的代扣金额 ' + p.row.total + ' 元进行确认?'
                }

                app.$Modal.confirm({
                    content: desc,
                    onOk: function () {
                        app.confirmWithhold(p)
                    }
                })
            },
            closeModal(){
                window.parent.app.closeModal()
            }
        },
        created: function () {
            this.getBaseInfo()
            this.listData()
        }
    })
})