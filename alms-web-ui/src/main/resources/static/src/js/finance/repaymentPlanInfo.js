let app
window.layinit(function (htConfig) {
	
    let financeBasePath = htConfig.financeBasePath;
    let platRepayBasePath = htConfig.platRepayBasePath;
    let businessId = getQueryStr('businessId');	// 业务编号
    let customer = decodeURI(getQueryStr('customer'));	// 客户姓名
    let phoneNumber = getQueryStr('phoneNumber');	// 电话号码
    let repaymentType = decodeURI(getQueryStr('repaymentType'));	// 还款方式
    let borrowMoney = getQueryStr('borrowMoney');	// 借款金额
    let borrowLimit = getQueryStr('borrowLimit');	// 借款期限
    
    app = new Vue({
        el: "#app",
        data: {
        	baseInfo:{},	// 业务基本信息
        	bizRepaymentPlanList: [],	// 还款计划信息
        	projOtherFeeList: [],	// 标还款计划其他费用
        	bizOtherFeeList: [],	// 业务还款计划其他费用
        	repayPlanFlag: false, 	// 计划还款弹窗控制标识
        	repayActualFlag: false, 	// 实际还款弹窗控制标识
        	repayDifferenceFlag: false, 	// 差额弹窗控制标识
        	repayOtherFeeFlag: false, // 其他费用弹窗控制标识(业务维度)
        	repayProjOtherFeeFlag: false, // 其他费用弹窗控制标识（标维度）
        	
        	// -- 实还流水 --
        	actualPaymentRecordList: [],
        	
        	actualPaymentRecordAfterIdNullList: [],	// -- 实还流水afterId为空的数据 --
        	
        	projectInfoList: [], 	//  标信息LIST
        	
        	// 还款计划表头  -- start --
        	repayPlanColumns: [
        		{
                    title: '客户姓名',
                    key: 'realName',
                    align: 'center',
                },
                {
                	title: '还款类型',
                    key: 'repayment',
                    align: 'center',
                },
                {
                	title: '还款日期',
                    key: 'repaymentDate',
                    align: 'center',
                },
                {
                    title: '本金',
                    key: 'principal',
                    align: 'center',
                },
                {
                	title: '利息',
                    key: 'accrual',
                    align: 'center',
                },
                {
                	title: '月收分公司服务费',
                    key: 'serviceCharge',
                    align: 'center',
                },
                {
                    title: '月收平台费',
                    key: 'platformCharge',
                    align: 'center',
                },
                {
                	title: '其他费用',
                    key: 'otherFee',
                    align: 'center',
                    render:(h,p)=>{
                    	if(p.row.repayment=='实际还款'){
                    		return h('a',{
                        		style:{
                        			textDecoration:'underline'
                        		},
                        		on:{
                        			click:function(){
                        				if (p.row.projPlanListId  != null) {
											app.openProjRepayOtherFee(p.row.projPlanListId)
										}else {
											app.openRepayOtherFee(p.row.planListId)
										}
                        			}
                        		}
                        	},p.row.otherFee)
                    	}else{
                    		return p.row.otherFee
                    	}
                    	
                    }
                },
                {
                	title: '小计',
                    key: 'subtotal',
                    align: 'center',
                },
                {
                    title: '逾期天数',
                    key: 'overdueDays',
                    align: 'center',
                    render:(h,p)=>{
                    	if(p.row.repayment=='差额'){
                    		return ''
                    	}else{
                    		return p.row.overdueDays
                    	}
                    	
                    }
                },
                {
                	title: '线上滞纳金',
                    key: 'onlineLateFee',
                    align: 'center',
                },
                {
                	title: '线下滞纳金',
                    key: 'offlineLateFee',
                    align: 'center',
                },
                {
                    title: '结余',
                    key: 'surplus',
                    align: 'center',
                },
                {
                	title: '还款合计（含滞纳金）',
                    key: 'total',
                    align: 'center',
                },
                {
                	title: '状态',
                    key: 'confirmFlag',
                    align: 'center',
                },
        	],
        	// 还款计划表头  -- end --
        	
        	repayPlanColumnsData: [], // 计划还款数据
    		repayActualColumnsData: [], // 实际还款数据
    		repayDifferenceColumnsData: [], // 差额数据
    		
    		/*
    		 * 平台标的应还计划
    		 */
    		platformRepaymentInfoColumns:[
        		{
                    title: '期数',
                    key: 'periods',
                    align: 'center',
                },
                {
                    title: '应还日期',
                    key: 'cycDate',
                    align: 'center',
                },
                {
                    title: '应还合计',
                    key: 'total',
                    align: 'center',
                },
                {
                    title: '还款本金',
                    key: 'amount',
                    align: 'center',
                },
                {
                    title: '还款利息',
                    key: 'interestAmout',
                    align: 'center',
                },
                {
                    title: '还款状态（借款人还款状态）',
                    key: 'statusStr',
                    align: 'center',
                },
            ],
            /*
    		 * 平台标的应还计划
    		 */
            platformRepaymentInfoData:[],
            
            /*
             * 平台标的实还计划
             */
            platformActualRepaymentInfoColumns:[
            	{
            		title: '期数',
            		key: 'period',
            		align: 'center',
            	},
            	{
            		title: '实还日期',
            		key: 'addDate',
            		align: 'center',
            	},
            	{
            		title: '还款合计',
            		key: 'totalAmount',
            		align: 'center',
            	},
            	{
            		title: '还款本息',
            		key: 'principalAndInterest',
            		align: 'center',
            	},
            	{
            		title: '平台费',
            		key: 'tuandaiAmount',
            		align: 'center',
            	},
            	{
            		title: '资产端服务费',
            		key: 'orgAmount',
            		align: 'center',
            	},
            	{
            		title: '担保公司服务费',
            		key: 'guaranteeAmount',
            		align: 'center',
            	},
            	{
            		title: '仲裁服务费',
            		key: 'arbitrationAmount',
            		align: 'center',
            	},
            	{
            		title: '中介服务费',
            		key: 'agencyAmount',
            		align: 'center',
            	},
            	{
            		title: '滞纳金',
            		key: 'penaltyAmount',
            		align: 'center',
            	},
            	{
            		title: '还款状态（借款人还款状态）',
            		key: 'statusStrActual',
            		align: 'center',
            	},
        	],
        	/*
        	 * 平台标的实还计划
        	 */
        	platformActualRepaymentInfoData:[],
        	
        	/*
             * 垫付记录表头
             */
        	advancePaymentInfoColumns:[
            	{
            		title: '期数',
            		key: 'period',
            		align: 'center',
            	},
            	{
            		title: '还款日期',
            		key: 'refundDate',
            		align: 'center',
            	},
            	{
            		title: '还款总金额',
            		key: 'totalAmount',
            		align: 'center',
            	},
            	{
            		title: '本息',
            		key: 'principalAndInterest',
            		align: 'center',
            	},
            	{
            		title: '平台服务费',
            		key: 'tuandaiAmount',
            		align: 'center',
            	},
            	{
            		title: '资产端服务费',
            		key: 'orgAmount',
            		align: 'center',
            	},
            	{
            		title: '担保公司服务费',
            		key: 'guaranteeAmount',
            		align: 'center',
            	},
            	{
            		title: '仲裁服务费',
            		key: 'arbitrationAmount',
            		align: 'center',
            	},
            	{
            		title: '逾期费用',
            		key: 'overDueAmount',
            		align: 'center',
            	},
            	{
            		title: '状态',
            		key: 'statusStrActual',
            		align: 'center',
            	},
            	],
            	/*
            	 * 垫付记录数据
            	 */
            	advancePaymentInfoData:[],
            	
            	platformPrincipalAndInterest: '',	// 平台本息
            	platformPrincipalAndInterestAndPlatformCharge: '',	// 平台费用合计
            	platformRepaymentInfoPlatformCharge: '',	// 平台服务费
            	platformRepaymentInfoaviMoney: '', // 账户余额
            	
            	firstProjectId: '', // 第一个projectId
            	firstTdUserId: '', // 第一个tdUserId
        },
        methods: {
        	/*
        	 * 初始化业务基本信息
        	 */
            initBaseInfo: function(){
            	this.baseInfo.businessId = businessId;
            	this.baseInfo.customer = customer == 'null' ? '' : customer;
            	this.baseInfo.phoneNumber = phoneNumber == 'null' ? '' : phoneNumber;
            	this.baseInfo.repaymentType = repaymentType;
            	this.baseInfo.borrowMoney = borrowMoney;
            	this.baseInfo.borrowLimit = borrowLimit + '个月';
            },
            /*
             * 初始化方法
             */
            initFunction: function(event){
            	if (event == 'realRepaymentRecord') {
					this.queryActualPaymentByBusinessId();
				}else if (event == 'platformRealRepayment') {
					if (this.firstProjectId != '') {
						this.getProjectPayment(this.firstProjectId);
					}
				}else if (event == 'advancesRecord') {
					if (this.firstProjectId != '') {
						this.returnAdvanceShareProfit(this.firstProjectId);
					}
				}else if (event == 'fundDistributionRecord') {
					
				}
            },
            /*
             * 标的初始化方法
             */
            initProjectFunction: function(event){
            	this.getProjectPayment(event);
            },
            
            initAdvancePaymentFunction: function(event){
            	this.getProjectPayment(event);
            },
            /*
             * 根据业务编号获取业务维度的还款计划信息
             */
            queryRepaymentPlanInfoByBusinessId: function(){
            	if (this.bizRepaymentPlanList == null || this.bizRepaymentPlanList.length == 0) {
            		axios.get(financeBasePath +"finance/queryRepaymentPlanInfoByBusinessId?businessId=" + businessId)
        	        .then(function (res) {
        	            if (res.data.data != null && res.data.code == 1) {
        	            	app.bizRepaymentPlanList = res.data.data.resultList;
        	            } else {
        	            	app.$Modal.error({content: res.data.msg });
        	            }
        	        })
        	        .catch(function (error) {
        	        	app.$Modal.error({content: '接口调用异常!'});
        	        });
				}
            },
            /*
             * 根据业务还款计划列表ID获取所有对应的标的应还还款计划信息
             */
            queryPlanRepaymentProjInfoByPlanListId: function(planListId){
        		axios.get(financeBasePath +"finance/queryPlanRepaymentProjInfoByPlanListId?planListId=" + planListId)
        		.then(function (res) {
        			if (res.data.data != null && res.data.code == 1) {
        				app.repayPlanColumnsData = res.data.data.resultProjInfo;
        			} else {
        				app.$Modal.error({content: res.data.msg });
        			}
        		})
        		.catch(function (error) {
        			app.$Modal.error({content: '接口调用异常!'});
        		});
            },
            /*
             * 根据业务还款计划列表ID获取所有对应的标的应还还款计划信息
             */
            queryActualRepaymentProjInfoByPlanListId: function(planListId){
            	axios.get(financeBasePath +"finance/queryActualRepaymentProjInfoByPlanListId?planListId=" + planListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			app.repayActualColumnsData = res.data.data.resultProjInfo;
            		} else {
            			app.$Modal.error({content: res.data.msg });
            		}
            	})
            	.catch(function (error) {
            		app.$Modal.error({content: '接口调用异常!'});
            	});
            },
            /*
             * 获取标还款计划差额
             */
            queryDifferenceRepaymentProjInfo: function(planListId){
            	axios.get(financeBasePath +"finance/queryDifferenceRepaymentProjInfo?planListId=" + planListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			app.repayDifferenceColumnsData = res.data.data.resultProjInfo;
            		} else {
            			app.$Modal.error({content: res.data.msg });
            		}
            	})
            	.catch(function (error) {
            		app.$Modal.error({content: '接口调用异常!'});
            	});
            },
            /*
             * 获取标还款计划其他费用
             */
            queryProjOtherFee: function(projPlanListId){
            	axios.get(financeBasePath +"finance/queryProjOtherFee?projPlanListId=" + projPlanListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			app.projOtherFeeList = res.data.data;
            		} else {
            			app.$Modal.error({content: res.data.msg });
            		}
            	})
            	.catch(function (error) {
            		app.$Modal.error({content: '接口调用异常!'});
            	});
            },
            /*
             * 获取业务还款计划其他费用
             */
            queryBizOtherFee: function(planListId){
            	axios.get(financeBasePath +"finance/queryBizOtherFee?planListId=" + planListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			app.bizOtherFeeList = res.data.data;
            		} else {
            			app.$Modal.error({content: res.data.msg });
            		}
            	})
            	.catch(function (error) {
            		app.$Modal.error({content: '接口调用异常!'});
            	});
            },
            /*
             * 根据业务编号查找实还流水
             */
            queryActualPaymentByBusinessId: function(){
            	if (this.actualPaymentRecordList == null || this.actualPaymentRecordList.length == 0) {
            		axios.get(financeBasePath +"finance/queryActualPaymentByBusinessId?businessId=" + businessId)
                	.then(function (res) {
                		if (res.data.data != null && res.data.code == 1) {
                			
                			app.actualPaymentRecordList = res.data.data.actualPaymentLogDTOs;
                			app.actualPaymentRecordAfterIdNullList = res.data.data.singleLogDTOs;
                		
                			if (res.data.data.actualPaymentLogDTOs != null && res.data.data.actualPaymentLogDTOs.length > 0) {
                				
    							for (var i = 0; i < res.data.data.actualPaymentLogDTOs.length; i++) {
    								
    								app.actualPaymentRecordList[i].actualPaymentSingleRecordList = app.actualPaymentRecordList[i].actualPaymentSingleLogDTOs;
    								
    							}
    						}
                		} else {
                			app.$Modal.error({content: res.data.msg });
                		}
                	})
                	.catch(function (error) {
                		app.$Modal.error({content: '接口调用异常!'});
                	});
				}
            },
            /*
             * 为类型增加点击事件	
             */
            addEventForType: function(item){
            	var planListId = item.planListId;
            	if (item.repayment == '计划还款') {
					return '<a href="#" onclick="app.openRepayPlan(`'+planListId+'`)" style="text-decoration:underline ">计划还款</a>';
				}else if (item.repayment == '实际还款') {
					return '<a href="#" onclick="app.openRepayActual(`'+planListId+'`)" style="text-decoration:underline ">实际还款</a>';
				}else if (item.repayment == '差额'){
					return '<a href="#" onclick="app.openRepayDifference(`'+planListId+'`)" style="text-decoration:underline ">差额</a>';
				}
            },
            /*
             * 为其他费用增加点击事件
             */
            addEventForOtherFee: function(item){
            	var planListId = item.planListId;
        		return '<a href="#" onclick="app.openRepayOtherFee(`'+planListId+'`)" style="text-decoration:underline ">' + item.otherFee + '</a>';
            },
            /*
             * 打开计划还款，查看本期还款计划所属的标的计划
             */
            openRepayPlan: function(planListId){
            	this.repayPlanFlag = true;
            	this.queryPlanRepaymentProjInfoByPlanListId(planListId);
            },
            /*
             * 打开实际还款，查看本期实际还款的标的计划
             */
            openRepayActual: function(planListId){
            	this.repayActualFlag = true;
            	this.queryActualRepaymentProjInfoByPlanListId(planListId);
            },
            /*
             * 打开差额，查看本期应还减去实还
             */
            openRepayDifference: function(planListId){
            	this.repayDifferenceFlag = true;
            	this.queryDifferenceRepaymentProjInfo(planListId);
            },
            /*
             * 打开其他费用，查看其他费用明细项，相应的点击标的中的其他费用也看查看相应的其他费用项（业务维度）
             */
            openRepayOtherFee: function(planListId){
            	this.repayOtherFeeFlag = true;
            	this.queryBizOtherFee(planListId);
            },
            /*
             * 打开其他费用，查看其他费用明细项，相应的点击标的中的其他费用也看查看相应的其他费用项（标维度）
             */
            openProjRepayOtherFee: function(projPlanListId){
            	this.repayProjOtherFeeFlag = true;
            	this.queryProjOtherFee(projPlanListId);
            },
            
            /*
			 * 根据业务ID获取标信息
			 */
			getProjectInfoByBusinessId: function(){
				axios.get(platRepayBasePath +"tdrepayRecharge/getProjectInfoByBusinessId?businessId=" + businessId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1 && res.data.data.length > 0) {
    	            	app.projectInfoList = res.data.data;
	            		app.firstProjectId = res.data.data[0].projectId;
	            		app.firstTdUserId = res.data.data[0].tdUserId;
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 标的还款信息查询接口
			 */
			getProjectPayment: function(projectId){
				axios.get(platRepayBasePath +"tdrepayRecharge/getProjectPayment?projectId=" + projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.platformRepaymentInfoData = res.data.data.periodsList;
    	            	app.platformActualRepaymentInfoData = res.data.data.tdProjectPaymentDTOs;
    	            	app.platformRepaymentInfoaviMoney = res.data.data.aviMoney.aviMoney;
    	            	if (res.data.data.principal == null) {
    	            		res.data.data.principal = 0;
						}
    	            	if (res.data.data.interest == null) {
    	            		res.data.data.interest = 0;
						}
    	            	if (res.data.data.platformCharge == null) {
    	            		res.data.data.platformCharge = 0;
						}
    	            	app.platformPrincipalAndInterest = res.data.data.principal + res.data.data.interest;
    	            	app.platformRepaymentInfoPlatformCharge = res.data.data.platformCharge;
    	            	app.platformPrincipalAndInterestAndPlatformCharge = res.data.data.principal + res.data.data.interest + res.data.data.platformCharge;
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			/*
			 * 根据标ID获取垫付记录
			 */
			returnAdvanceShareProfit: function(projectId){
				axios.get(platRepayBasePath +"tdrepayRecharge/returnAdvanceShareProfit?projectId=" + projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.advancePaymentInfoData = res.data.data;
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
			},
        },
        created: function () {
        	this.initBaseInfo();
        	this.queryRepaymentPlanInfoByBusinessId();
        	this.getProjectInfoByBusinessId();
        }
    })
})