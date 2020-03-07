let app
window.layinit(function (htConfig) {
	
    let financeBasePath = htConfig.financeBasePath;
    let platRepayBasePath = htConfig.platRepayBasePath;
    let businessId = getQueryStr('businessId');	// 业务编号
    let afterId = getQueryStr('afterId');	// 贷后期数
    let display = getQueryStr('display');	// 是否显示代扣记录
    
    app = new Vue({
        el: "#app",
        data: {
        	baseInfo:{
        		businessId:'',
        		customer:'',
        		phoneNumber:'',
        		repaymentType:'',
        		borrowMoney:'',
        		borrowLimit:'',
        		accountBalance:'',
        		settleTotalAmount:'',
        		principal:'',
        		interest:'',
        		platformAmount:'',
        		orgAmount:'',
        		liquidatedDamage:'',
        	},	// 业务基本信息
        	srcTypeFlag: false, // 生产还款计划源
        	bizRepaymentPlanList: [],	// 还款计划信息
        	projOtherFeeList: [],	// 标还款计划其他费用
        	bizOtherFeeList: [],	// 业务还款计划其他费用
        	repayPlanFlag: false, 	// 计划还款弹窗控制标识
        	repayActualFlag: false, 	// 实际还款弹窗控制标识
        	repayDifferenceFlag: false, 	// 差额弹窗控制标识
        	repayOtherFeeFlag: false, // 其他费用弹窗控制标识(业务维度)
        	repayProjOtherFeeFlag: false, // 其他费用弹窗控制标识（标维度）
        	businessSurplus:0, // 用户账户结余
        	plateTypeFlag: '', // 数据来源：1、团贷网；2、你我金融；0、线下出款；
        	isOldDataFlag: false, // 是否6.28之前上标的数据
        	display:1, // 是否显示除代扣记录以外的tab（0、不显示、1、显示）
        	repayDerateMoneyFlag:false, // 减免明细弹窗控制标识
        	repayDerateMoneyList: [],	// 减免费用明细
        	
        	// -- 实还流水 --
        	actualPaymentRecordList: [],
        	
        	actualPaymentRecordAfterIdNullList: [],	// -- 实还流水afterId为空的数据 --
        	
        	projectInfoList: [], 	//  标信息LIST
        	
        	oldSurplusGuaranteePaymentModal: false, // 6.28之前上标数据垫付记录弹窗控制
        	
            tableHeight:450,
            paneHeight:"height:450px",
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
                    	if(p.row.repayment=='实际还款' || p.row.repayment=='计划还款'){
                    		return h('a',{
                        		style:{
                        			textDecoration:'underline'
                        		},
                        		on:{
                        			click:function(){
                        				if (p.row.projPlanListId  != null) {
											app.openProjRepayOtherFee(p.row)
										}else {
											app.openRepayOtherFee(p.row)
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
                    className: 'demo-table-info-column-yuqi',
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
                    key: 'confirmFlagStr',
                    align: 'center',
                },
        	],
        	// 还款计划表头  -- end --
        	
        	// 还款计划表头  -- start --
        	repayBizPlanColumns: [
        		{
        			title: '期数',
        			key: 'afterId',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '还款类型',
        			key: 'repayment',
        			align: 'center',
        			width: 100,
        			render:(h,p)=>{
    					return h('a',{
    						style:{
    							textDecoration:'underline'
    						},
    						on:{
    							click:function(){
    								if (p.row.repayment=='计划还款') {
    									app.openRepayPlan(p.row.planListId);
									}else if (p.row.repayment=='实际还款') {
										app.openRepayActual(p.row.planListId);
									}else {
										app.openRepayDifference(p.row.planListId);
									}
    							}
    						}
    					},p.row.repayment)
        				
        			}
        		},
        		{
        			title: '还款日期',
        			key: 'repaymentDate',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '本金',
        			key: 'principal',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '利息',
        			key: 'accrual',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '月收分公司服务费',
        			key: 'serviceCharge',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '月收平台费',
        			key: 'platformCharge',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '其他费用',
        			key: 'otherFee',
        			align: 'center',
        			width: 100,
        			render:(h,p)=>{
        				if(p.row.repayment=='实际还款' || p.row.repayment=='计划还款'){
        					return h('a',{
        						style:{
        							textDecoration:'underline'
        						},
        						on:{
        							click:function(){
    									app.openRepayOtherFee(p.row)
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
        			width: 100,
        		},
        		{
        			title: '逾期天数',
        			key: 'overdueDays',
        			className: 'demo-table-info-column-yuqi',
        			align: 'center',
        			width: 100,
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
        			width: 100,
        		},
        		{
        			title: '线下滞纳金',
        			key: 'offlineLateFee',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '结余',
        			key: 'surplus',
        			align: 'center',
        			width: 100,
        		},
        		{
                	title: '减免',
                	key: 'derateMoney',
                    align: 'center',
                    render:(h,p)=>{
                    	if(p.row.repayment=='计划还款'){
                    		return h('a',{
                        		style:{
                        			textDecoration:'underline'
                        		},
                        		on:{
                        			click:function(){
                        				app.openRepayDerateMoney(p.row)
                        			}
                        		}
                        	},p.row.derateMoney)
                    	}else{
                    		return p.row.derateMoney
                    	}
                    	
                    },
                    width: 100,
                },
        		{
        			title: '还款合计（含滞纳金）',
        			key: 'total',
        			align: 'center',
        			width: 100,
        		},
        		{
        			title: '状态',
        			key: 'confirmFlagStr',
					width: 800,
					render:(h,p)=>{
						return h('pre',{
							style:{
								overflow:'hidden',
							},
							attrs:{
								title:p.row.confirmFlagStr,
							}
						},p.row.confirmFlagStr)
					}
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
                    key: 'period',
                    align: 'center',
                },
                {
                    title: '应还日期',
                    key: 'cycDate',
                    align: 'center',
                },
                {
                    title: '应还合计',
                    key: 'totalAmount',
                    align: 'center',
                },
                {
                    title: '还款本金',
                    key: 'amount',
                    align: 'center',
                },
                {
                    title: '还款利息',
                    key: 'interestAmount',
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
                	title: '担保费',
                	key: 'guaranteeAmount',
                	align: 'center',
                },
                {
                	title: '滞纳金',
                	key: 'agencyAmount',
                	align: 'center',
                },
                {
                	title: '保证金',
                	key: 'depositAmount',
                	align: 'center',
                },
                {
                	title: '仲裁费',
                	key: 'arbitrationAmount',
                	align: 'center',
                },
                {
                	title: '中介服务费',
                	key: 'agencyAmount',
                	align: 'center',
                },
                {
                    title: '其他费用',
                    key: 'otherAmount',
                    align: 'center',
                },
                {
                	title: '还款状态',
                	key: 'repaymentStatus',
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
            	
            	distributeFundRecordList: [], // 资金分发记录
            	
            	/*
            	 * 查看担保公司垫付记录表头
            	 */
            	queryGuaranteePaymentColumns:[
            		{
            			title: '期数',
            			key: 'period',
            			align: 'center',
            		},
            		{
            			title: '还款状态',
            			key: 'status',
            			align: 'center',
            		},
            		{
            			title: '还款日期',
            			key: 'addDate',
            			align: 'center',
            		},
            		{
            			title: '本金利息',
            			key: 'principalAndInterest',
            			align: 'center',
            		},
            		{
            			title: '滞纳金',
            			key: 'penaltyAmount',
            			align: 'center',
            		},
            		{
            			title: '实还平台服务费',
            			key: 'tuandaiAmount',
            			align: 'center',
            		},
            		{
            			title: '实还资产端服务费',
            			key: 'orgAmount',
            			align: 'center',
            		},
            		{
            			title: '实还担保公司服务费',
            			key: 'guaranteeAmount',
            			align: 'center',
            		},
            		{
            			title: '实还仲裁服务费',
            			key: 'arbitrationAmount',
            			align: 'center',
            		},
            		{
            			title: '实还中介服务费',
            			key: 'agencyAmount',
            			align: 'center',
            		},
            		{
            			title: '合计',
            			key: 'total',
            			align: 'center',
            		},
            	],
            		
        		/*
        		 * 查看担保公司垫付记录数据
        		 */
        		queryGuaranteePaymentData:[],
        		
        		/*
        		 * 6.28之前的数据 全部垫付记录 表头
        		 */
        		oldAllGuaranteePaymentColumns:[
        			{
        				title: '期数',
            			key: 'periodMerge',
            			align: 'center',
        			},
        			{
        				title: '总垫付金额',
        				key: 'advanceAmountTotal',
        				align: 'center',
        			},
        			{
        				title: '总垫付滞纳金',
        				key: 'penaltyTotal',
        				align: 'center',
        			},
        			{
        				title: '已还垫付金额',
        				key: 'advanceAmountFactTotal',
        				align: 'center',
        			},
        			{
        				title: '已还垫付滞纳金',
        				key: 'penaltyFactTotal',
        				align: 'center',
        			},
        			{
        				title: '未还垫付金额',
        				key: 'advanceAmountSurplus',
        				align: 'center',
        			},
        		],
        	
        		/*
        		 * 6.28之前的数据 全部垫付记录 表体
        		 */
        		oldAllGuaranteePaymentData: [],
        		
        		/*
        		 * 6.28之前的数据 未还垫付记录 表头
        		 */
        		oldSurplusGuaranteePaymentColumns:[
        			{
        				title: '期数',
        				key: 'periodMerge',
        				align: 'center',
        			},
        			{
        				title: '未还垫付金额',
        				key: 'advanceAmountSurplus',
        				align: 'center',
        			},
        			{
        				title: '滞纳金',
        				key: 'penaltySurplus',
        				align: 'center',
        			},
        			{
        				title: '垫付人ID',
        				key: 'overDueUserIds',
        				align: 'center',
        				render:(h,p)=>{
        					return h('a',{
                        		style:{
                        			textDecoration:'underline'
                        		},
                        		on:{
                        			click:function(){
                        				app.openOldSurplusGuaranteePaymentModal(p.row);
                        			}
                        		}
                        	},p.row.overDueUserIds)
                        }
        			},
        			{
        				title: '垫付时间',
        				key: 'addDate',
        				align: 'center',
        			},
    			],
        			
    			/*
    			 * 6.28之前的数据 未还垫付记录 表体
    			 */
    			oldSurplusGuaranteePaymentData: [],
    			
    			/*
    			 * 代扣记录 表头
    			 */
    			withholdingRepaymentLogColumns:[
    				{
    					title: '期数',
    					key: 'afterId',
    					align: 'center',
    				},
    				{
    					title: '代扣金额',
    					key: 'currentAmount',
    					align: 'center',
    				},
    				{
    					title: '支付公司',
    					key: 'platformName',
    					align: 'center',
    				},
    				{
    					title: '代扣人',
    					key: 'createUser',
    					align: 'center',
    				},
    				{
    					title: '代扣时间',
    					key: 'createTime',
    					align: 'center',
    				},
    				{
    					title: '代扣结果',
    					key: 'repayStatusStr',
    					align: 'center',
    				},
    				{
    					title: '备注',
    					key: 'remark',
    					align: 'center',
    				},
				],
				
				/*
				 * 代扣记录 表体
				 */
				withholdingRepaymentLogData: [],
				
				/*
				 * 6.28之前的数据 垫付人垫付记录明细 表头
				 */
				oldGuaranteePaymentDetailColumns:[
					{
						title: '期数',
						key: 'period',
						align: 'center',
					},
					{
						title: '垫付ID',
						key: 'advanceId',
						align: 'center',
					},
					{
						title: '垫付金额',
						key: 'advanceAmount',
						align: 'center',
					},
					{
						title: '滞纳金',
						key: 'penalty',
						align: 'center',
					},
					{
						title: '借款人ID',
						key: 'borrowUserid',
						align: 'center',
					},
					{
						title: '垫付人ID',
						key: 'overDueUserId',
						align: 'center',
					},
					{
						title: '垫付时间',
						key: 'addDate',
						align: 'center',
					},
					{
						title: '还款时间',
						key: 'refundDate',
						align: 'center',
					},
					{
						title: '已还垫付金额',
						key: 'refundAmount',
						align: 'center',
					},
					{
						title: '是否已还',
						key: 'isRefund',
						align: 'center',
						render:(h,p)=>{
							if (p.row.isRefund == 1) {
								return '已还';
							}else if (p.row.isRefund == 0) {
								return '未还';
							}
	                    }
					},
					{
						title: '是否结清',
						key: 'isComplete',
						align: 'center',
						render:(h,p)=>{
							if (p.row.isComplete == 1) {
								return '已结清';
							}else if (p.row.isComplete == 0) {
								return '未结清';
							}
	                    }
					},
				],
				
				/*
				 * 6.28之前的数据 垫付人垫付记录明细 表体
				 */
				oldGuaranteePaymentDetailData: [],
					
        },
        methods: {
        	/*
        	 * 初始化业务基本信息
        	 */
            initBaseInfo: function(){
            	this.display = display; 
            	// 若是 00 期，取上一次的业务编号
				if (afterId.indexOf("00") != -1) {
					axios.get(platRepayBasePath +"tdrepayRecharge/getLastBusinessId?businessId=" + businessId)
	    	        .then(function (res) {
	    	            if (res.data.data != null && res.data.code == 1) {
	    	            	businessId = res.data.data;
	    	            	app.queryBaseInfoByBusinessId(businessId);
	    	            	app.queryRepaymentPlanInfoByBusinessId();
	    	            	app.getProjectInfoByBusinessId();
	    	            } else {
	    	            	app.$Modal.error({content: res.data.msg });
	    	            }
	    	        })
	    	        .catch(function (error) {
	    	        	app.$Modal.error({content: '接口调用异常!'});
	    	        });
				}else {
					this.queryBaseInfoByBusinessId(businessId);
					this.queryRepaymentPlanInfoByBusinessId();
	            	this.getProjectInfoByBusinessId();
				}
            },
            
            queryBaseInfoByBusinessId: function(param){
            	axios.get(financeBasePath +"finance/queryBaseInfoByBusinessId?businessId=" + param)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.baseInfo.businessId = res.data.data.businessId;
    	            	app.baseInfo.customer = res.data.data.customerName == 'null' ? '' : res.data.data.customerName;
    	            	app.baseInfo.phoneNumber = res.data.data.phoneNumber == 'null' ? '' : res.data.data.phoneNumber;
    	            	app.baseInfo.repaymentType = res.data.data.repaymentTypeName;
    	            	app.baseInfo.borrowMoney = res.data.data.borrowMoney;
    	            	app.baseInfo.borrowLimit = res.data.data.borrowLimit + '个月';
    	            	app.plateTypeFlag = res.data.data.plateType;
    	            	app.baseInfo.accountBalance = res.data.data.accountBalance;
    	            	app.baseInfo.settleTotalAmount = res.data.data.settleTotalAmount;
    	            	app.baseInfo.principal = res.data.data.principal;
    	            	app.baseInfo.interest = res.data.data.interest;
    	            	app.baseInfo.platformAmount = res.data.data.platformAmount;
    	            	app.baseInfo.orgAmount = res.data.data.orgAmount;
    	            	app.baseInfo.liquidatedDamage = res.data.data.liquidatedDamage;
    	            	if (res.data.data.srcType != null && res.data.data.srcType == 2) {
    	            		app.srcTypeFlag = true;
						}
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
            },
            /*
             * 初始化方法
             */
            initFunction: function(event){
                this.paneHeight="height:"+this.tableHeight+"px";
                if (event == 'realRepaymentRecord') {
                    this.paneHeight="";
					this.queryActualPaymentByBusinessId();
				}else if (event == 'platformRealRepayment') {
                    this.paneHeight="";
					if (this.firstProjectId != '') {
						this.getProjectPayment(this.firstProjectId);
					}
				}else if (event == 'advancesRecord') {
                    this.paneHeight="";
					if (this.firstProjectId != '') {
						this.returnAdvanceShareProfit(this.firstProjectId);
					}
				}else if (event == 'fundDistributionRecord') {
                    this.paneHeight="";
					if (this.firstProjectId != '') {
						this.queryDistributeFundRecord(this.firstProjectId);
					}
				}else if (event == 'withholdingRepaymentLog') {
                    this.paneHeight="";
					this.queryWithholdingRepaymentLogByCondition();
				}else{
                    var that = this;
                    setTimeout(function(){
                        that.paneHeight = "height:"+(that.tableHeight+2)+"px";
                    },500)
				}

            },
            /*
             * 标的初始化方法
             */
            initProjectFunction: function(event){
            	this.getProjectPayment(event);
            },
            
            initAdvancePaymentFunction: function(event){
            	this.returnAdvanceShareProfit(event);
            },
            
            initDistributeFundRecordFunction: function(event){
            	this.queryDistributeFundRecord(event);
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
        	            	app.businessSurplus = res.data.data.businessSurplus;
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
            queryProjOtherFee: function(obj){
            	axios.get(financeBasePath +"finance/queryProjOtherFee?projPlanListId=" + obj.projPlanListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			if (obj.repayment == '实际还款') {
            				app.projOtherFeeList = res.data.data.factAmountResultList;
						}
            			if (obj.repayment == '计划还款') {
            				app.projOtherFeeList = res.data.data.planAmountResultList;
						}
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
            queryBizOtherFee: function(obj){
            	axios.get(financeBasePath +"finance/queryBizOtherFee?planListId=" + obj.planListId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			if (obj.repayment == '实际还款') {
            				app.bizOtherFeeList = res.data.data.factAmountResultList;
						}
            			if (obj.repayment == '计划还款') {
            				app.bizOtherFeeList = res.data.data.planAmountResultList;
						}
            		} else {
            			app.$Modal.error({content: res.data.msg });
            		}
            	})
            	.catch(function (error) {
            		app.$Modal.error({content: '接口调用异常!'});
            	});
            },
            /*
             * 查询减免费用明细
             */
            queryDerateMoneyGroupByDerateTypeName: function(obj){
            	axios.get(financeBasePath +"finance/queryDerateMoneyGroupByDerateTypeName?planListId=" + obj.planListId + "&businessId=" + businessId)
            	.then(function (res) {
            		if (res.data.data != null && res.data.code == 1) {
            			app.repayDerateMoneyList = res.data.data;
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
        		return '<a href="#" onclick="app.openRepayOtherFee(`'+item+'`)" style="text-decoration:underline ">' + item.otherFee + '</a>';
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
            openRepayOtherFee: function(obj){
            	this.repayOtherFeeFlag = true;
            	this.queryBizOtherFee(obj);
            },
            /*
             * 打开其他费用，查看其他费用明细项，相应的点击标的中的其他费用也看查看相应的其他费用项（标维度）
             */
            openProjRepayOtherFee: function(obj){
            	this.repayProjOtherFeeFlag = true;
            	this.queryProjOtherFee(obj);
            },
            /*
             * 打开其他费用，查看其他费用明细项，相应的点击标的中的其他费用也看查看相应的其他费用项（标维度）
             */
            openRepayDerateMoney: function(obj){
            	this.repayDerateMoneyFlag = true;
            	this.queryDerateMoneyGroupByDerateTypeName(obj);
            },
            
            /*
			 * 根据业务ID获取标信息
			 */
			getProjectInfoByBusinessId: function(){
				axios.get(platRepayBasePath +"tdrepayRecharge/getProjectInfoByBusinessId?businessId=" + businessId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.projectInfoList = res.data.data;
    	            	if (res.data.data.length > 0) {
    	            		app.firstProjectId = res.data.data[0].projectId;
    	            		app.firstTdUserId = res.data.data[0].tdUserId;
						}
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
    	            	if (res.data.data.isOldDataFlag != null) {
    	            		app.isOldDataFlag = res.data.data.isOldDataFlag;
						}
    	            	if (res.data.data.aviMoney != null) {
    	            		app.platformRepaymentInfoaviMoney = res.data.data.aviMoney.aviMoney;
						}
    	            	if (res.data.data.principal == null) {
    	            		res.data.data.principal = 0;
						}
    	            	if (res.data.data.interest == null) {
    	            		res.data.data.interest = 0;
						}
    	            	if (res.data.data.platformCharge == null) {
    	            		res.data.data.platformCharge = 0;
						}
    	            	app.platformPrincipal = res.data.data.principal;
    	            	app.platformInterest = res.data.data.interest;
    	            	app.platformRepaymentInfoPlatformCharge = res.data.data.platformCharge;
    	            	var principalAndInterest = 0;
    	            	if (res.data.data.dueOutMoney != null) {
    	            		app.platformPrincipalAndInterest = res.data.data.dueOutMoney;
    	            		principalAndInterest = res.data.data.dueOutMoney;
						}else {
							principalAndInterest = res.data.data.principal + res.data.data.interest;
						}
    	            	app.platformPrincipalAndInterest = principalAndInterest;
    	            	app.platformPrincipalAndInterestAndPlatformCharge = principalAndInterest + res.data.data.platformCharge;
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
    	            	if (res.data.data.isOldDataFlag != null) {
    	            		app.isOldDataFlag = res.data.data.isOldDataFlag;
						}
    	            	if (res.data.data.returnAdvanceShareProfits != null) {
    	            		app.advancePaymentInfoData = res.data.data.returnAdvanceShareProfits;
						}
    	            	if (res.data.data.tdGuaranteePaymentVOs != null) {
    	            		app.queryGuaranteePaymentData = res.data.data.tdGuaranteePaymentVOs;
						}
    	            	if (res.data.data.advanceRecordDTOs != null) {
    	            		app.oldAllGuaranteePaymentData = res.data.data.advanceRecordDTOs;
    	            		app.oldSurplusGuaranteePaymentData = res.data.data.advanceRecordDTOs;
    	            	}
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 查询资金分发记录
			 */
			queryDistributeFundRecord: function(projectId){
				axios.get(platRepayBasePath +"tdrepayRecharge/queryDistributeFundRecord?projectId=" + projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	app.distributeFundRecordList = res.data.data;
    	            } else {
    	            	app.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	app.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			/*
			 * 查询资金分发记录
			 */
			queryWithholdingRepaymentLogByCondition: function(projectId){
				axios.get(financeBasePath +"finance/queryWithholdingRepaymentLogByCondition?businessId=" + businessId + "&afterId=" + afterId)
				.then(function (res) {
					if (res.data.data != null && res.data.code == 1) {
						app.withholdingRepaymentLogData = res.data.data;
					} else {
						app.$Modal.error({content: res.data.msg });
					}
				})
				.catch(function (error) {
					app.$Modal.error({content: '接口调用异常!'});
				});
			},
			
			/**
			 * 根据overDueUserIds查询垫付记录明细
			 */
			openOldSurplusGuaranteePaymentModal: function(obj){
				axios.get(platRepayBasePath +"tdrepayRecharge/queryOldSurplusGuaranteePaymentDetail?projectId=" + obj.projectId 
						+ "&period=" + obj.periodMerge + "&overDueUserIds=" + obj.overDueUserIds)
				.then(function (res) {
					if (res.data.data != null && res.data.code == 1) {
						app.oldGuaranteePaymentDetailData = res.data.data;
						app.oldSurplusGuaranteePaymentModal = true;
					}else {
						app.$Modal.error({ content: result.data.msg });
						app.oldSurplusGuaranteePaymentModal = false;
					}
				})
				.catch(function (error) {
					app.$Modal.error({content: '接口调用异常!'});
					app.oldSurplusGuaranteePaymentModal = false;
				});
			},
			/*
			 * 定义背景颜色
			 */
			rowClassName (row) {
                if (row.repayment === '差额') {
                    return 'demo-table-info-row-cha';
                } 
                /*else if (row.repayment === '实际还款') {
                    return 'demo-table-info-row-shi';
                } else if (row.repayment === '计划还款') {
                    return 'demo-table-info-row-ying';
                }*/
                return '';
            },
        },
        created: function() {
        	this.initBaseInfo();
            this.tableHeight = window.innerHeight-180;
            this.paneHeight = "height:"+this.tableHeight+"px";
		}
    })
})