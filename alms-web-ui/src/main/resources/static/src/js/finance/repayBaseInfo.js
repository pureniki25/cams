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
        el: "#baseInfo",
        data: {
            info: {}
            ,spinShow:true
        },
        methods: {

        },
        beforeCreate: function () {
        },
        created: function () {
            axios.get('http://localhost:30621/finance/repayBaseInfo', {
                params: {
                    businessId: businessId,
                    afterId: afterId
                }
            })
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.info = res.data.data;
                    }
                    app.spinShow=false
                })
                .catch(function (err) {
                    app.spinShow=false
                })
        }
    })
})