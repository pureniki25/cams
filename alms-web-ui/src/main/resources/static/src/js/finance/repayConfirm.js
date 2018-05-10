/**
 * 
 */
window.app;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    app = new Vue({
        el: "#app",
        data: {
            repayInfo: {
                url: '/finance/repayBaseInfo?businessId=' + businessId + "&afterId=" + afterId,
            },
            repayRegList: {
                url: '/finance/repayRegList?businessId=' + businessId + "&afterId=" + afterId,
                style:{}
            },
            matchedBankStatement: {
                show: false,
                url: '/finance/matchedBankStatement?businessId=' + businessId + "&afterId=" + afterId,
                style:{}
            },
            manualAddBankSatements: {
                show: false,
                url: '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId,
                
            },
            manualMatchBankSatements:{
                show:false,
                url:'/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId,
            },
            style: {
                repayRegList: {},
                matchedBankStatement: {}
            }
        },
        methods: {
            closeModal: function (target) {
                if (app[target]) {
                    app[target] = false;
                }
            },
            openModal: function (target) {
                if (!app[target]) {
                    app[target] = true
                }
            },
            configModalStyle: function (target, style) {
                if (app[target].style) {
                    app[target].style = style
                }
            }
        },
        created: function () {
        }
    })
})
