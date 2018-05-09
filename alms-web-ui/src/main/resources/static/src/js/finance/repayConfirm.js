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
        data:{
            manualAddBankSatementsShow:false,
            repayInfoUrl:'/finance/repayBaseInfo?businessId='+businessId+"&afterId="+afterId,
            repayRegListUrl:'/finance/repayRegList?businessId='+businessId+"&afterId="+afterId,
            manualAddBankSatements:'/finance/manualAddBankSatements?businessId='+businessId+"&afterId="+afterId,
            matchedBankStatementUrl:'/finance/matchedBankStatement?businessId='+businessId+"&afterId="+afterId,
            style:{
                repayRegList:{},
                matchedBankStatement:{}
            }
        },
        methods:{
            closeModal:function(target){
                if(app[target]){
                    app[target]=false;
                }
            },
            configModalStyle:function(target,style){
                console.log(target);
                console.log(style);
                console.log(app.style[target]);
                if(app.style[target]){
                    app.style[target] = style
                }
            }
        },
        created:function(){
        }
    })
})
