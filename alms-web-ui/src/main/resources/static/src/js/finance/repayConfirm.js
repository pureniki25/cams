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
        data:{
            repayInfoUrl:'/finance/repayBaseInfo?businessId='+businessId+"&afterId="+afterId
        },
        methods:{
            
        },
        created:function(){
        }
    })
})