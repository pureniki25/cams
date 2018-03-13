let app 
let baseData = ''
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;
    app = new Vue({
        el:"#app",
        data:{
            business:{
                info:{},
                repaymentType:'',
                output:{
                    user:'',
                    date:'',
                    money:''
                },
                repay:{
                    money:''
                }
            },
            preSettleDate:{
                date:'',
                disabledDate:{
                    disabledDate(date) {
                        return date.getTime() < new Date().getTime() 
                    }
                }
            }
        },
        created:function(){
            axios.get(basePath + '/expenseSettle/business', {
                params: {
                    businessId: getQueryStr('businessId')
                }
            }
            ).then(function(res){
                if(res.data.code=='1'){
                    console.log(res.data.data)
                    baseData = res.data.data
                    app.business.info = res.data.data.business
                    app.business.repaymentType = res.data.data.repaymentType
                    let outPutCount = 0 
                    $.each(res.data.data.output,function(i,o){
                        if(i==0){
                            app.business.output.user = o.outputUserName
                            app.business.output.date = o.factOutputDate
                        }
                        outPutCount += o.factOutputMoney
                    })
                    app.business.output.money = outPutCount

                    let factRepayCount = 0 
                    $.each(res.data.data.repaymentDetails,function(i,o){
                        if(o.planItemType==10){
                           factRepayCount =  o.factAmount||0
                        }
                    })
                    app.business.repay.money = factRepayCount

                }else{
                    app.$Modal.error({content:'接口调用失败'})
                }
            }).catch(function(error){
                app.$Modal.error({content:'接口调用失败'})
            })
        },
        methods:{
            onPreSettleDateChange:function(date){
                axios.get(basePath + '/expenseSettle/calByPreSettleDate', {
                    params: {
                        businessId: getQueryStr('businessId'),
                        afterId: getQueryStr('afterId'),
                        preSettleDate: date
                    }
                }
                ).then(function(res){
                    if(res.data.code=='1'){
                        console.log(res.data.data)
                    }else{
                        app.$Modal.error({content:'接口调用失败'})
                    }
                }).catch(function(error){
                    app.$Modal.error({content:'接口调用失败'})
                })
            }
        }

    })


})