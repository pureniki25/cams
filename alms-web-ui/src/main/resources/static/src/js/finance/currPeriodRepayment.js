let currPeriodRepayment = {
    col: [{
        title: '展开',
        type: 'expand',
        render: (h, p) => {
            return h('i-table', {
                props: {
                    stripe: true,
                    columns: expandCol,
                    data: p.row.projs||[]
                }
            })
        }
    }, {
        title: '类型',
        key: 'type'
    }, {
        title: '还款日期',
        key: ''
    }, {
        title: '本金',
        key: ''
    }, {
        title: '利息',
        key: ''
    }, {
        title: '月收分公司服务费',
        key: ''
    }, {
        title: '月收平台费',
        key: ''
    }, {
        title: '小计',
        key: ''
    }, {
        title: '线下逾期费',
        key: ''
    }, {
        title: '线上逾期费',
        key: ''
    }, {
        title: '减免金额',
        key: ''
    }, {
        title: '合计(含滞纳金)',
        key: ''
    }],
    data: [{
        type: 'ces'
    }],
    expandCol: [{
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
    expandData:[{
        
    }]
}
