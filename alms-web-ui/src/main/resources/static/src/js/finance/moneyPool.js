/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    console.log(cpath);
    console.log(fpath);
    app = new Vue({
        el: "#app",
        data: {
            form: {
                acceptBank: '',
                tradeType: '',

            },
            bankList: {},
            table: {
                col: [{
                        type: 'selection',
                        width: 70,
                    },
                    {
                        title: '转入账号',
                        key: ''
                    }, {
                        title: '交易时间',
                        key: ''
                    }, {
                        title: '转入账号',
                        key: ''
                    }, {
                        title: '记账金额',
                        key: ''
                    }, {
                        title: '支付类型',
                        key: ''
                    }, {
                        title: '交易场所',
                        key: ''
                    }, {
                        title: '状态',
                        key: ''
                    }, {
                        title: '更新时间',
                        key: ''
                    }, {
                        title: '交易备注',
                        key: ''
                    }, {
                        title: '领取人',
                        key: ''
                    }, {
                        title: '操作',
                        key: ''
                    },
                ],
                data: [],
                loading: false
            }
        },
        watch: {},
        methods: {
            getMoneyPoolManager(){
                axios.get(fpath+'finance/moneyPoolManager',{
                    params:{

                    }
                })
                .then(function(res){
                    console.log(res);
                })
                .catch(function(err){

                })
            }
        },
        created: function () {
            this.getMoneyPoolManager()
        }
    })
})