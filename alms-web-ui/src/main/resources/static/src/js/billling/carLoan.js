var basePath;
var vm;
// 业务ID
var businessId = document.getElementById("businessId").getAttribute("value");

//设置表单验证

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    
	vm = new Vue({
	    el: '#app',
	    data: {
	    	
	    	preLateFeesFlag:false,	//提前还款违约金标识，非上标业务，等额本息为true
	    	carLoanBilLoading:false,
	    	
	        baseInfoForm:{
	        	customerName:""        , // 客户名称
	        	borrowMoney:""           , // 借款金额
	        	surplusPrincipal:""           , // 剩余本金
	        	borrowLimit:""  		, // 借款期限
	        	repaymentTypeId:""        , // 还款方式
	        	borrowRate:""	, 		// 年费率
	        	factOutputDate:"" 		, // 出款日期
	        	factOutputMoney:"" 		, // 出款金额
	        	outputUserName:""   	, // 出款人
	        	planAccrual:"" 			, // 本期利息
	        	planServiceCharge:"" 	, // 服务费
	        	planGuaranteeCharge:""  , // 担保公司费用
	        	planPlatformCharge:""   , // 平台费
	        	innerLateFees:""  	, // 期内滞纳金
	        	outsideInterest:""  	, // 期外逾期利息
	        	preLateFees:""  	, // 提前还款违约金
	        	balanceDue:""  	, // 往期少交费用（含滞纳金）
	        	overdueDefault:""  	, // 逾期违约金
	        	receivableTotal: '',//	应收合计
	        	overRepayMoney: '',		//	结余
	        	refundMoney: '',		//	押金转还款金额
				squaredUp: '',		//	最终结清金额
	        },
	        
	        lastRepayDate: '',	// 最后一次结清日期
	        
	        commitInfoForm: {
				businessId:""		   	, // 业务编号
				billDate: '',	// 预计结清日期
				innerLateFees: '',	// 期内滞纳金
				outsideInterest: '',	// 期外逾期利息
				preLateFees: '',	// 提前还款违约金
				parkingFees: '',	// 停车费
				gpsFees: '',	// GPS费
				dragFees: '',	// 拖车费
				otherFees: '',	// 其他费用
				attorneyFees: ''	// 律师费
			},
			
			arrearageDetailTitle: [
				{
					title: '期数',
					key: 'afterId'
				},
				{
					title: '服务费',
					key: 'previousPlanServiceCharge',
					render:(h,params)=>{
						return h('span',params.row.previousPlanServiceCharge?numeral(params.row.previousPlanServiceCharge).format('0,0.00'):0)
					}
				},
				{
					title: '滞纳金',
					key: 'previousLateFees',
					render:(h,params)=>{
						return h('span',params.row.previousLateFees?numeral(params.row.previousLateFees).format('0,0.00'):0)
					}
				},
				{
					title: '利息',
					key: 'previousPlanAccrual',
					render:(h,params)=>{
						return h('span',params.row.previousPlanAccrual?numeral(params.row.previousPlanAccrual).format('0,0.00'):0)
					}
				}
			],
			
			arrearageDetailData: [],
			
			preSettleDate:{
                disabledDate:{
                	disabledDate(date) {
                        var DATE = new Date();
                        var year = DATE.getFullYear();
                        var month = DATE.getMonth()+1;
                        var day = DATE.getDate();
                        return date.getTime() < (vm.lastRepayDate ? new Date(vm.lastRepayDate).getTime():new Date(year+"-"+month+"-"+day+" 00:00:00").getTime()) || (new Date(year+"-"+month+"-"+day+" 00:00:00").getTime() + 1000*60*60*24*15 ) <= date.getTime()
                    }
                }
            }
	    },
	   methods: {
		   carLoanBilling:function(){
			   vm.carLoanBilLoading = true;
			   vm.commitInfoForm.businessId = businessId;
			   axios.post(basePath +"transferOfLitigation/carLoanBilling", vm.commitInfoForm)
		        .then(function (res) {
		        	vm.carLoanBilLoading = false;
		            if (res.data.data != null && res.data.code == 1) {
		            	vm.baseInfoForm = res.data.data;
		            	vm.baseInfoForm.borrowRate = res.data.data.borrowRate + '%/年';
		            	vm.arrearageDetailData = res.data.data.previousFees;
		            } else {
		                vm.$Modal.error({content: res.data.msg });
		            }
		        })
		        .catch(function (error) {
		        	vm.carLoanBilLoading = false;
		            vm.$Modal.error({content: '接口调用异常!'});
		        });
		   },
		   
	   },
	   
	   created:function(){
		   
			var reqUrl = basePath + 'transferOfLitigation/queryCarLoanBilDetail?businessId=' + businessId
			
			axios.get(reqUrl).then(function(res){
				if(res.data.code=='1'){
					vm.baseInfoForm = res.data.data;
					vm.lastRepayDate = res.data.data.lastRepayDate;
					vm.baseInfoForm.borrowRate = res.data.data.borrowRate + '%/年';
					if (res.data.data.outputPlatformId == 0 && res.data.data.repaymentTypeId == '等额本息') {
						vm.preLateFeesFlag = true;
					}
				}else{
					vm.$Modal.error({content: res.data.msg });
				}
			}).catch(function(error){
				app.$Modal.error({content:'接口调用失败'})
			})
		},
	   
	});
    
});
