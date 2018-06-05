let app
window.layinit(function (htConfig) {
	
    let financeBasePath = htConfig.financeBasePath;
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
        },
        methods: {
        	/*
        	 * 初始化业务基本信息
        	 */
            initBaseInfo: function(){
            	this.baseInfo.businessId = businessId;
            	this.baseInfo.customer = customer;
            	this.baseInfo.phoneNumber = phoneNumber;
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
					
				}else if (event == 'advancesRecord') {
					
				}else if (event == 'fundDistributionRecord') {
					
				}
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
        },
        created: function () {
        	this.initBaseInfo();
        	this.queryRepaymentPlanInfoByBusinessId();
        }
    })
})