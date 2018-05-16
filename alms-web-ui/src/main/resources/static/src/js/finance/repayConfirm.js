/**
 * 
 */
window.app;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    var layer = layui.layer;
    let curIndex ;
    app = new Vue({
        el: "#app",
        data: {
            repayInfo: {
                url: '/finance/repayBaseInfo?businessId=' + businessId + "&afterId=" + afterId,
            },
            repayRegList: {
                url: '/finance/repayRegList?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            confirmedRepayment:{
                url: '/finance/confirmedRepayment?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            matchedBankStatement: {
                show: false,
                url: '/finance/matchedBankStatement?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            manualAddBankSatements: {
                show: false,
                url: '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId,

            },
            manualMatchBankSatements: {
                show: false,
                url: '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId,
            },

            style: {
                repayRegList: {},
                matchedBankStatement: {}
            },

            table:{
                currRepayment:currRepayment,
                projRepayment:projRepayment
            }
        },
        methods: {
            closeModal: function (target) {
                if (app[target]) {
                    app[target] = false;
                }else{
                    layer.close(curIndex)
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
            },
            openMatchBankStatementModal(p) {
                let url = '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动匹配流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        console.log(layero, index);
                        curIndex = index ;
                    }
                })
            },
            openAddBankStatementModal() {
                let url = '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动新增流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        console.log(layero, index);
                        curIndex = index ;
                    }
                })
            }
        },
        created: function () {}
    })
})
