/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;

    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;

    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    app = new Vue({
        el: "#app",
        data: {
            spinShow: true,
            table: {
                col: [
                ],
                data: []
            }
        },
        methods: {
        },
        created: function () {
        }
    })
})