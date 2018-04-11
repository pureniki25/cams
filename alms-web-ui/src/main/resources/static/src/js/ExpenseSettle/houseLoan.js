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
            },
            settleDate:'',
            arrearageDetailTitle: [
				{
					title: '期数',
					key: 'period'
				},
				{
					title: '服务费',
					key: 'servicecharge'
                },
                {
					title: '平台费',
					key: 'platFormFee'
				},
				{
					title: '滞纳金',
					key: 'lateFee'
				},
				{
					title: '利息',
					key: 'interest'
				}
			]
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
                            
                            app.business.output.date = o.factOutputDate.substring(0,10)
                        }
                        outPutCount += o.factOutputMoney
                    })
                    app.business.output.money = outPutCount.toFixed(2)

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
                this.settleDate = date ;
            },
            cal:function(date){

                if(!this.settleDate){
                    app.$Modal.error({content:'请选择结清日期!'})
                    return ;
                }

                axios.get(basePath + 'expenseSettle/calByPreSettleDate', {
                    params: {
                        businessId: getQueryStr('businessId'),
                        afterId: getQueryStr('afterId'),
                        preSettleDate: this.settleDate
                    }
                }
                ).then(function(res){
                    if(res.data.code=='1'){
                        app.business.info = res.data.data
                    }else{
                        app.$Modal.error({content:res.data.msg})
                    }
                }).catch(function(error){
                    app.$Modal.error({content:res.data.msg})
                })
            },
            sumIncome: function () {
                let value = (this.business.info.principal
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
                if(isNaN(value)){
                    return 0 
                }else{
                    return value
                }
            },
            sum: function () {
                let value = (this.sumIncome()
                - (this.business.info.balance
                    + this.business.deposit)).toFixed(2)
                if(isNaN(value))  {
                    return 0 
                }else{
                    return value
                }
            }
        }

    })


})