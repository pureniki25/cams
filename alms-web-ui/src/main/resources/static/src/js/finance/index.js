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
            form: {
                name: '',
                businessId: '',
                isExpired: '',
                confirmStatus: '',
                deptId: '',
                businessTypeId: '',
                repayDate: '',
                repayRegDate: '',
                status:''
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
                    key: 'deptId'
                },
                {
                    title: '主办',
                    key: 'operatorId'
                },
                {
                    title: '客户姓名',
                    key: 'customerName'
                },
                {
                    title: '业务类型',
                    key: 'businessTypeId'
                },
                {
                    title: '还款日期',
                    key: 'repayDate',
                    render: (h, p) => {
                        return h('span', moment(p.row.repayDate).format('YYYY-MM-DD'))
                    }
                },
                {
                    title: '还款登记日期',
                    key: 'repayRegDate'
                },
                {
                    title: '还款金额',
                    key: 'repayAmount',
                    align:'right',
                    width:200,
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
                        let text = h('span',numeral(p.row.repayAmount).format('0,0.00'));

                        let res = []
                        let isZQ = p.row.afterId.indexOf('ZQ')>-1?true:false;
                        if(p.row.periods==1&&!isZQ){
                            res.push(firstPeriod)
                        }
                        if(p.row.repaymentTypeId==2&&p.row.borrowLimit==p.row.periods){
                            res.push(benjinPeriod)
                        }
                        if(p.row.repaymentTypeId==5&&p.row.borrowLimit==p.row.periods){
                            res.push(lastPeriod)
                        }
                        res.push(text)
                        return h('div', res)
                    }
                },
                {
                    title: '会计确认状态',
                    key: 'confirmStatus',
                    render: (h, p) => {
                        return h('Tag', {
                            props: {
                                color: "blue"
                            }
                        }, p.row.confirmStatus)
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
                    title: '操作'
                },
                {
                    title: '详情'
                }

                ],
                data: [
                    {
                        businessId: 'asdasdas',
                        confirmStatus: '待确定',
                        repayDate:new Date(),
                        periods:1,
                        afterId:'1-01',
                        repaymentTypeId:2,
                        borrowLimit:1,
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdasASSSSSSSSSSSS',
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdas',
                        confirmStatus: '待确定',
                        repayDate:new Date(),
                        periods:1,
                        afterId:'1-01',
                        repaymentTypeId:2,
                        borrowLimit:1,
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdasASSSSSSSSSSSS',
                        repayAmount:456312.12
                    },{
                        businessId: 'asdasdas',
                        confirmStatus: '待确定',
                        repayDate:new Date(),
                        periods:1,
                        afterId:'1-01',
                        repaymentTypeId:2,
                        borrowLimit:1,
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdasASSSSSSSSSSSS',
                        repayAmount:456312.12
                    },{
                        businessId: 'asdasdas',
                        confirmStatus: '待确定',
                        repayDate:new Date(),
                        periods:1,
                        afterId:'1-01',
                        repaymentTypeId:2,
                        borrowLimit:1,
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdasASSSSSSSSSSSS',
                        repayAmount:456312.12
                    },{
                        businessId: 'asdasdas',
                        confirmStatus: '待确定',
                        repayDate:new Date(),
                        periods:1,
                        afterId:'1-01',
                        repaymentTypeId:2,
                        borrowLimit:1,
                        repayAmount:456312.12
                    },
                    {
                        businessId: 'asdasdasASSSSSSSSSSSS',
                        repayAmount:456312.12
                    }
                ]
            }
        },
        methods: {},
        created: function () { }
    })
})