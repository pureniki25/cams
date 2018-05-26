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
        	repayPlanFlag: false, 	// 计划还款弹窗控制标识
        	repayActualFlag: false, 	// 实际还款弹窗控制标识
        	repayOtherFeeFlag: false, // 其他费用弹窗控制标识
        	
        	/*
        	 * 点击计划还款  -- start --
        	 */
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
        	repayPlanColumnsData: [],
        	// 点击计划还款  -- end --
        	
        	/*
        	 * 点击实际还款  -- start --
        	 */
        	repayActualColumns: [
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
    		repayActualColumnsData: []
        	// 点击实际还款  -- end --
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
             * 根据业务编号获取业务维度的还款计划信息
             */
            queryRepaymentPlanInfoByBusinessId: function(){
            	axios.get(financeBasePath +"finance/queryRepaymentPlanInfoByBusinessId?businessId=" + businessId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.bizRepaymentPlanList = res.data.data.resultList;
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.syncCollectionLoading = false;
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
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
				}else {
					return item.repayment;
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
            	
            },
            /*
             * 打开实际还款，查看本期实际还款的标的计划
             */
            openRepayActual: function(planListId){
            	this.repayActualFlag = true;
            },
            /*
             * 打开其他费用，查看其他费用明细项，相应的点击标的中的其他费用也看查看相应的其他费用项
             */
            openRepayOtherFee: function(planListId){
            	this.repayOtherFeeFlag = true;
            },
        },
        created: function () {
        	this.initBaseInfo();
        	this.queryRepaymentPlanInfoByBusinessId();
        }
    })
})