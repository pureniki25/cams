/**
 * 
 */

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    
let t1 = new Vue({
    el:"#t1",
    data:{
        self:this,
        cert_modal:false,
        cert_modal_url:'',
        columns1: [
            {
                title: '流水号',
                key: 'id'
            },
            {
                title: '还款日期',
                key: 'repaymentDate'
            },
            {
                title: '还款金额',
                key: 'repaymentMoney'
            },
            {
                title: '实际还款人',
                key: 'realRepaymentUser'
            },
            {
                title: '转入账号',
                key: 'acceptBank'
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
                key: 'status'
            },
            {
                title: '更新用户',
                key: 'updateUser'
            },
            {
                title: '更新时间',
                key: 'updateTime'
            },
            {
                title: '操作',
                key: 'operate',
                render: (h, params) => {
                    return h('i-button', {
                        props: {
                            type: 'success',
                            size: 'small'
                        },
                        on: {
                            click: () => {
                                if(params.row.certUrl&&params.row.certUrl.length){
                                    t1.cert_modal_url = 'http://xiaodaioa.oss-cn-beijing.aliyuncs.com/' + params.row.certUrl
                                    t1.cert_modal = true
                                }
                            }
                        }
                    }, '查看凭证');
                }
            }

        ],
        data1: []
    },
    created:()=>{
        console.log(this.businessId)
        axios.get(basePath + 'moneyPool/list',{params:{businessId:getQueryStr("businessId"),afterId:getQueryStr("afterId")}})
        .then(function (res) {
            if(res.data.data&&res.data.data.length){
                t1.data1 = res.data.data
            }
        })
        .catch(function (error) {
            console.log(error);
        });
    },
    methods:{

    }
})

let t2 = new Vue({
    el:"#t2",
    data:{
        self:this,
        columns1: [
            {
                title: '流水号',
                key: 'id'
            },
            {
                title: '款项码',
                key: 'repaymentCode'
            },
            {
                title: '转入账户',
                key: 'acceptBank'
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
                key: 'repaymentMoney'
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
    created:()=>{
        //取区域列表
        axios.get(basePath + 'moneyPool/listMatched',{params:{businessId:getQueryStr("businessId"),afterId:getQueryStr("afterId")}})
        .then(function (res) {
            console.log(res);
            if(res.data.data&&res.data.data.length){
                t2.data1 = res.data.data
            }
        })
        .catch(function (error) {
            console.log(error);
        });
    },
    methods:{

    }
})

});