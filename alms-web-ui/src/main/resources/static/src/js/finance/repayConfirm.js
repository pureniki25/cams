/**
 * 
 */
window.app;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    app = new Vue({
        el: "#app",
        data:{
            repayInfoUrl:'/finance/repayBaseInfo?businessId='+businessId+"&afterId="+afterId,
            repayRegListUrl:'/finance/repayRegList?planListId='+planListId,
            manualAddBankSatements:'/finance/manualAddBankSatements?businessId='+businessId,
            manualAddBankSatementsShow:true
        },
        methods:{
            parentMethod:function(o){
                console.log(o);
            }
        },
        created:function(){
        }
    })
})
