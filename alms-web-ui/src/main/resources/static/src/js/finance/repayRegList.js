/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;
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
                        key: ''
                    }, {
                        title: '还款日期',
                        key: ''
                    }, {
                        title: '还款金额',
                        key: ''
                    }, {
                        title: '实际转款人',
                        key: ''
                    }, {
                        title: '转入账号',
                        key: ''
                    }, {
                        title: '交易类型',
                        key: ''
                    }, {
                        title: '交易场所',
                        key: ''
                    }, {
                        title: '备注',
                        key: ''
                    }, {
                        title: '状态',
                        key: ''
                    }, {
                        title: '操作',
                        key: ''
                    }
                ],
                data: [{
                    
                }]
            }
        },
        methods: {

        },
        created: function () {
        }
    })
})