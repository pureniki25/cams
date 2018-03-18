let app 
let baseData = ''
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var basePath = htConfig.coreBasePath;
    app = new Vue({
        el:"#app",
        data:{
            business:{
                base:{},
                info:{},
                repaymentType:'',
                output:{
                    user:'',
                    date:'',
                    money:''
                },
                repay:{
                    money:''
                },
                doorToDoorFee:0,
                lawyerFee:0,
                legalFee:0,
                otherFee:0,
                deposit:0
            },
            preSettleDate:{
                date:'',
                disabledDate:{
                    disabledDate(date) {
                        var DATE = new Date();
                        var year = DATE.getFullYear();
                        var month = DATE.getMonth()+1;
                        var day = DATE.getDate();
                        return date.getTime() < new Date(year+"-"+month+"-"+day+" 00:00:00").getTime() || (new Date(year+"-"+month+"-"+day+" 00:00:00").getTime() + 1000*60*60*24*15 ) <= date.getTime()
                    }
                }
            }
        },
        created:function(){
            axios.get(basePath + 'expenseSettle/business', {
                params: {
                    businessId: getQueryStr('businessId')
                }
            }
            ).then(function(res){
                if(res.data.code=='1'){
                    console.log(res.data.data)
                    baseData = res.data.data
                    app.business.base = Object.assign(res.data.data.business,res.data.data.detail) 
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
                axios.get(basePath + 'expenseSettle/calByPreSettleDate', {
                    params: {
                        businessId: getQueryStr('businessId'),
                        afterId: getQueryStr('afterId'),
                        preSettleDate: date
                    }
                }
                ).then(function(res){
                    if(res.data.code=='1'){
                        app.business.info = res.data.data
                    }else{
                        app.$Modal.error({content:'接口调用失败'})
                    }
                }).catch(function(error){
                    app.$Modal.error({content:'接口调用失败'})
                })
            },
            sumIncome: function () {
                return (this.business.info.principal
                    + this.business.info.interest
                    + this.business.info.servicecharge
                    + this.business.info.guaranteeFee
                    + this.business.info.platformFee
                    + this.business.info.demurrage
                    + this.business.info.penalty
                    + this.business.info.lackFee
                    + this.business.info.lateFee
                    + this.business.doorToDoorFee
                    + this.business.lawyerFee
                    + this.business.legalFee
                    + this.business.otherFee).toFixed(2)
            },
            sum: function () {
                /* doorToDoorFee:0,
                lawyerFee:0,
                legalFee:0,
                otherFee:0 */
                return (this.sumIncome()
                    - (this.business.info.balance
                        + this.business.deposit)).toFixed(2)
            }
        }

    })


})