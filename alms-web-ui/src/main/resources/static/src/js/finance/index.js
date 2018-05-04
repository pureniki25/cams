/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;

    app = new Vue({
        el: "#app",
        data: {
            show: true,
            accountingConfirmStatus: [],
            companys: [],
            businessTypes: [],
            collectionStatus: [],
            form: {
                curPage: 1,
                pageSize: 10,
                customer: '',
                businessId: '',
                principalDeadLine: '',
                accountantConfirmStatus: '',
                companyId: '',
                businessType: '',
                repayDate: '',
                regDate: '',
                regDateStart: '',
                regDateEnd: '',
                status: ''
            },
            table: {
                col: [{
                    title: '业务编号',
                    key: 'businessId'
                },
                {
                    title: '期数',
                    key: 'afterId'
                },
                {
                    title: '部门',
                    key: 'dept'
                },
                {
                    title: '主办',
                    key: 'operator'
                },
                {
                    title: '客户姓名',
                    key: 'customer'
                },
                {
                    title: '业务类型',
                    key: 'businessType'
                },
                {
                    title: '还款日期',
                    key: 'planRepayDate',
                    // render: (h, p) => {
                    //     return h('span', moment(p.row.repayDate).format('YYYY-MM-DD'))
                    // }
                },
                {
                    title: '还款登记日期',
                    key: 'factRepayDate'
                },
                {
                    title: '还款金额',
                    key: 'planRepayAmount',
                    align: 'right',
                    width: 200,
                    render: (h, p) => {

                        let firstPeriod = h('Tag', {
                            props: {
                                color: 'yellow'
                            }
                        }, '首期');
                        let benjinPeriod = h('Tag', {
                            props: {
                                color: 'yellow'
                            }
                        }, '本金期');
                        let lastPeriod = h('Tag', {
                            props: {
                                color: 'yellow'
                            }
                        }, '末期');
                        let text = h('span', numeral(p.row.planRepayAmount).format('0,0.00'));

                        let res = []
                        let isZQ = p.row.afterId.indexOf('ZQ') > -1 ? true : false;
                        if (p.row.periods == 1 && !isZQ) {
                            res.push(firstPeriod)
                        }
                        if (p.row.repaymentTypeId == 2 && p.row.borrowLimit == p.row.periods) {
                            res.push(benjinPeriod)
                        }
                        if (p.row.repaymentTypeId == 5 && p.row.borrowLimit == p.row.periods) {
                            res.push(lastPeriod)
                        }
                        res.push(text)
                        return h('div', res)
                    }
                },
                {
                    title: '会计确认状态',
                    key: 'financeConfirmStatus',
                    render: (h, p) => {
                        return h('Tag', {
                            props: {
                                color: "blue"
                            }
                        }, p.row.financeConfirmStatus)
                    }
                },
                {
                    title: '状态',
                    key: 'status',
                    render: (h, p) => {
                        return h('Tag', {
                            props: {
                                color: "blue"
                            }
                        }, p.row.status)
                    }
                },
                {
                    title: '是否支持代扣',
                    key: 'canWithhold'
                },
                {
                    title: '操作',
                    render: (h, p) => {

                        function initMenuItem(title, link) {
                            return h('li', [
                                h('i-button', {
                                    class: ['menuItem'],
                                    on: {
                                        click: function () {
                                            console.log(link)
                                        }
                                    }
                                }, title)
                            ])
                        }
                        /* 根据p.row状态封装菜单 */
                        let menu = []
                        let repayConfirm = initMenuItem('财务人员确认还款', '/ssssxx')
                        let businessAllSettle = initMenuItem('业务全部结清确认', '/ssssxx')

                        menu.push(repayConfirm)
                        menu.push(businessAllSettle)

                        return h('Poptip', {
                            props: {
                                placement: 'left'
                            }
                        }, [
                                h('i-button', '操作'),
                                h('ul', {
                                    slot: 'content'
                                }, menu)
                            ])
                    }
                },
                {
                    title: '详情',
                    render: (h, p) => {
                        return h('i-button', {
                            on: {
                                click: function () {
                                    console.log(this)
                                }
                            }
                        }, '详情')
                    }
                }

                ],
                data: [],
                total:0
            }
        },
        methods: {
            search: function (page) {
                let params = this.form
                
                for (const key in params) {
                    if (params.hasOwnProperty(key)) {
                        const element = params[key];
                        if(!element||element==''){
                            console.log(key+element);
                            delete params[key]
                        }
                    }
                }
                console.log(params);
                axios.get('http://localhost:30621/' + 'finance/getFinanceMangerList', {
                    params: params
                })
                    .then(function (res) {
                        if (res.data.code == '0') {
                            app.table.data = res.data.data 
                            app.table.total = res.data.count
                        } else {
                            app.$Message.error({ content: '获取列表数据失败' })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({ content: '获取列表数据失败' })
                    })
            },
            paging:function(page){
                app.form.curPage = page ;
                app.search()
            }
        },
        created: function () {
            axios.get('http://localhost:30606/' + 'sys/param/getParam', { params: { paramType: '会计确认状态' } })
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.accountingConfirmStatus = res.data.data
                    } else {
                        app.$Message.error({ content: '获取会计确认状态失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取会计确认状态失败' })
                })

            axios.get('http://localhost:30621/' + 'finance/getCompanys')
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.companys = res.data.data
                    } else {
                        app.$Message.error({ content: '获取分公司失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取分公司失败' })
                })

            axios.get('http://localhost:30621/' + 'finance/getBusinessType')
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.businessTypes = res.data.data
                    } else {
                        app.$Message.error({ content: '获取业务类型失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取业务类型失败' })
                })

            axios.get('http://localhost:30606/' + 'sys/param/getParam', { params: { paramType: '贷后状态' } })
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.collectionStatus = res.data.data
                    } else {
                        app.$Message.error({ content: '获取贷后状态失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取贷后状态失败' })
                })

            this.search()
        }
    })
})