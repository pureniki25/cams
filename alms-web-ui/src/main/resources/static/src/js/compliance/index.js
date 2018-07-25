var basePath;
var coreBasePath;
var vm;
var table;

var rechargeModalFormRules = {
    transferType: [
    	{required: true, message: '必选', trigger: 'blur'}
	],
	rechargeAmount: [
		{pattern:/^\d{0,9}(\.\d{1,2})?$/, required: true,  message: '请填写整数位数不大于9位，小数位数不超过两位的数字', trigger: 'blur'}
	],
	bankCode: [
		{required: true, message: '必选', trigger: 'blur'}
	],
};

window.layinit(function(htConfig) {
	basePath = htConfig.platRepayBasePath;
	coreBasePath = htConfig.coreBasePath;
	table = layui.table;

	vm = new Vue({
		el : '#app',
		data : {
			rechargeModal: false, // 代充值账户充值弹窗控制
			bankCodeList:['GDB','ICBC','BOC','CMBCHINA','PINGAN','HXYH','BOCO','POST','CCB','SPDB','CMBC','BCCB','CEB','CIB','ECITIC','ABC'],	// 银行代码
			bankAccountList: [],// 所有银行账户
			selectAmountList: [], // 已勾选待分发金额集合
			selectAmount: 0, // 已勾选待分发金额
			ComplianceRepaymentData:[],// 储存查询到的数据
			rechargeAccountType: '车贷', // 代充值账户类型
			rechargeAccountBalance: 0, // 代充值账户余额
			allBusinessCompanyList: [], // 所有分公司
			tdrepayRechargeInfoReqList: [], // 
			infoFlag: false,	// 详情弹窗控制
			repaymentProjPlan: '', // 标还款计划信息
			projectId:'', // 标ID
			rechargeRecordFlag: false, // 查看充值记录弹窗控制
			rechargeAccountBalanceFlag: false, // 查看代充值账户余额弹窗控制
			queryRechargeAccountBalanceLoading: false, // 查看代充值账户余额按钮加载状态控制
			infoTabValue:'fundDistributionRecord',//详情tab值
			businessTypeList:[], // 业务类型集合
			rechargeAccountTypeList:[], // 代充值账户类型集合
				
			/*
			 *  详情基础信息
			 */
			infoBasicData:{
				businessId: '',
				projectId: '',
				customerName: '',
				amount: 0,
			},
			
			/*
			 * 合规化还款主页面查询条件
			 */
			queryConditionModel: {
				confirmTimeStart: '',	// 财务确认开始日期
				confirmTimeEnd: '', // 财务确认结束日期
				processStatus: '', // 分发状态
				repaySource: '', // 还款方式
				settleType: '', // 期数类型
				origBusinessId: '', // 编号
				businessType: '', // 业务类型
				platStatus: '', // 平台状态
				companyName: '', // 分公司
				page: 1, // 当前页
			},
			
			/*
			 * 代充值账户充值form
			 */
			rechargeModalForm: {
				rechargeAccountType: '', // 代充值账户
				transferType: '', // 转账类型
				rechargeAmount: '', // 充值金额（元）
				rechargeSourseAccount: '', // 充值来源账户
				bankCode: '', // 选择银行
				rechargeAccountBalance: '', //代充值账户余额
				bankAccount: '', // 银行账号
			},
			/*
			 * 初始化代充值账户充值form
			 */
			initRechargeModalForm: {
				rechargeAccountType: '', // 代充值账户
				transferType: '', // 转账类型
				rechargeAmount: '', // 充值金额（元）
				rechargeSourseAccount: '', // 充值来源账户
				bankCode: '', // 选择银行
				rechargeAccountBalance: '', //代充值账户余额
				bankAccount: '', // 银行账号
			},
			
			/*
			 * 查询充值记录model
			 */
			queryRechargeRecordModel: {
				rechargeAccountType: '', // 代充值账户
				cmOrderNo:'', // 代充值订单号
				rechargeSourseAccount:'', //充值来源账户
				createTimeStart:'', //创建时间 开始
				createTimeEnd:'',// 创建时间 结束
				page: 1,
                limit: 10,
			},
			/*
			 * 初始化查询充值记录model
			 */
			initQueryRechargeRecordModel: {
				rechargeAccountType: '', // 代充值账户
				cmOrderNo:'', // 代充值订单号
				rechargeSourseAccount:'', //充值来源账户
				createTimeStart:'', //创建时间 开始
				createTimeEnd:'',// 创建时间 结束
			},
			
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
        	
        	distributeFundRecordList: [], // 资金分发记录
        	
        	/*
             * 查看充值记录表头
             */
        	queryRechargeRecordColumns:[
	        	{
	        		title: '订单号',
	        		key: 'cmOrderNo',
	        		align: 'center',
	        	},
	        	{
	        		title: '代充值账户',
	        		key: 'rechargeAccountType',
	        		align: 'center',
	        	},
	        	{
	        		title: '代充值银行',
	        		key: 'bankName',
	        		align: 'center',
	        	},
	        	{
	        		title: '充值来源银行',
	        		key: 'rechargeSourseAccount',
	        		align: 'center',
	        	},
	        	{
	        		title: '充值来源银行卡(后4位)',
	        		key: 'subBankAccount',
	        		align: 'center',
	        	},
	        	{
	        		title: '充值金额(元)',
	        		key: 'rechargeAmount',
	        		align: 'center',
	        	},
	        	{
	        		title: '状态',
	        		key: 'handleStatusStr',
	        		align: 'center',
	        	},
	        	{
	        		title: '创建人',
	        		key: 'createUsername',
	        		align: 'center',
	        	},
	        	{
	        		title: '创建时间',
	        		key: 'createTime',
	        		align: 'center',
	        	},
        	],
        	
        	/*
        	 * 查看充值记录数据
        	 */
        	queryRechargeRecordData:[],
        	rechargeRecordTotal: 0, // 充值记录总数
        	
        	/*
             * 查看所有代充值账户余额表头
             */
        	queryRechargeAccountBalanceColumns:[
	        	{
	        		title: '序号',
	        		key: 'num',
	        		align: 'center',
	        	},
	        	{
	        		title: '账户名称',
	        		key: 'rechargeAccountType',
	        		align: 'center',
	        	},
	        	{
	        		title: '余额',
	        		key: 'balance',
	        		align: 'center',
	        	},
        	],
        	
        	/*
        	 * 查看所有代充值账户余额数据
        	 */
        	queryRechargeAccountBalanceData:[],
        	
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
        	
		},

		methods : {
			/*
			 * 打开代充值账户充值弹窗
			 */
			openRechargeModal: function(){
				if (this.bankAccountList.length == 0) {
					this.listAllDepartmentBank();
				}
				this.rechargeModal = true;
			},
			/*
			 * 打开详情弹窗
			 */
			openInfoModal: function(data){
				this.getInfoBasicData(data);
				this.infoFlag = true;
			},
			/*
			 * 打开充值记录弹窗
			 */
			openRechargeRecordModal: function(data){
				if (this.bankAccountList.length == 0) {
					this.listAllDepartmentBank();
				}
				this.rechargeRecordFlag = true;
			},
			/*
			 * 打开充值记录弹窗
			 */
			openInfoTabModal: function(){
				this.initFunction(this.infoTabValue);
			},
			/*
			 * 获取所有线下还款账户
			 */
			listAllDepartmentBank: function(){
				axios.get(basePath + 'recharge/listAllDepartmentBank', {timeout: 0})
				.then(function(result){
					if (result.data.code == "1") {
						vm.bankAccountList = result.data.data;
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			/*
			 * 获取代充值账户余额
			 */
			queryUserAviMoney: function(){
				var rechargeAccountType;
				if (this.rechargeModal) {
					rechargeAccountType = this.rechargeModalForm.rechargeAccountType;
				}else {
					rechargeAccountType = this.rechargeAccountType;
				}
				axios.get(basePath + 'recharge/queryUserAviMoney?rechargeAccountType=' + rechargeAccountType, {timeout: 0})
				.then(function(result){
					if (result.data.code == "1" && result.data.data.data != null) {
						vm.rechargeModalForm.rechargeAccountBalance = result.data.data.data.aviMoney;
						vm.rechargeAccountBalance = result.data.data.data.AviMoney;
					} else {
						vm.rechargeModalForm.rechargeAccountBalance = '';
						vm.rechargeAccountBalance = '';
						vm.$Message.error({ 
							content: '没有查到对应的账户余额',
						});
					}
				}).catch(function (error) {
					vm.rechargeModalForm.rechargeAccountBalance = '';
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			/*
			 * 获取银行卡号
			 */
			queryBankAccount: function(financeName) {
				if (vm.bankAccountList != null && vm.bankAccountList.length > 0) {
					for (var i = 0; i < vm.bankAccountList.length; i++) {
						if (vm.bankAccountList[i].financeName == financeName) {
							vm.rechargeModalForm.bankAccount = vm.bankAccountList[i].repaymentId;
							break;
						}
					}
				}
			},
			/*
			 * 代充值账户充值弹窗提交
			 */
			rechargeModalCommit: function(){
				vm.$refs['rechargeModalForm'].validate((valid) => {
		            if(valid){
						axios.post(basePath + 'recharge/commitRechargeData', vm.rechargeModalForm, {timeout: 0})
						.then(function(result){
							if (result.data.code == "1") {
								vm.rechargeModalForm = vm.initRechargeModalForm;
								vm.$Modal.success({
				                    content: "提交成功",
				                    onOk: () => {
										vm.rechargeModal = false;
										window.open(result.data.data.data.result);
				                    },
				                });
							} else {
								vm.$Modal.error({ content: result.data.msg });
							}
						}).catch(function (error) {
							vm.$Modal.error({content: '接口调用异常!'});
		            	});
		            }else{
		                vm.$Message.error({content: '表单校验失败!'});
		            }
		        })
			},
			
			/*
			 * 当业务类型改变时触发
			 */
			onChangeBusinessType: function(value){
				axios.get(basePath +"recharge/queryRechargeAccountTypeByBusinessType?businessType=" + value)
				.then(function (res) {
					if (res.data.data != null && res.data.code == 1) {
						vm.rechargeAccountType = res.data.data;
						vm.queryUserAviMoney();
					}else {
						vm.rechargeAccountBalance = 0;
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
			},
			
			/*
			 * 拼接银行图片地址
			 */
			handleImgUri: function(item){
				return '/src/images/bankLogo/'+item+'.jpg'
			},
			/*
			 * 关闭弹窗通用方法
			 */
			closeModal: function(){
				vm.rechargeModalForm = vm.initRechargeModalForm;
				vm.queryRechargeRecordModel = vm.initQueryRechargeRecordModel;
				this.rechargeModal = false;
				this.rechargeRecordFlag = false;
			},
			
			/*
			 * 查询合规化还款主页面列表
			 */
			queryComplianceRepaymentData: function(){
				
				this.tdrepayRechargeInfoReqList = [];
				
				/*
				 * 合规化还款主页面列表
				 */
	            table.render({
			    	elem: "#complianceRepaymentData",
			    	height: 600, 
			    	cols: [[
			    		{
			    			type: 'checkbox',
			    			field: 'select',
			    			title: '全选',
			    			align: 'center',
							width:52
			    		}, {
			    			field: 'origBusinessId',
			    			title: '业务编号',
			    			align: 'center',
							width:200
			    		}, {
			    			field: 'businessTypeStr',
			    			title: '业务类型',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'repaymentTypeStr',
			    			title: '还款方式',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'customerName',
			    			title: '借款人',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'afterId',
			    			title: '还款期数',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'factRepayDateStr',
			    			title: '实还日期',
			    			align: 'center',
							width:100
			    		},{
			    			field: 'companyName',
			    			title: '分公司',
			    			align: 'center',
							width:100
			    		},{
			    			field: 'periodTypeStr',
			    			title: '状态',
			    			align: 'center',
							width:100
			    		},{
			    			field: 'resourceAmount',
			    			title: '流水合计',
			    			align: 'center',
							width:100
			    		},{
			    			field: 'factRepayAmount',
			    			title: '实收总额',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'rechargeAmount',
			    			title: '充值金额',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'platformTypeStr',
			    			title: '平台状态',
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'processStatusStr',
			    			title: '分发状态',
			    			align: 'center',
							width:100
			    		}, {
			    			title: '操作',
			    			toolbar: "#toolbar",
			    			align: 'center',
							width:100
			    		}, {
			    			field: 'remark',
			    			title: '备注',
			    			align: 'center',
							width:100
			    		}]],
			    		url: basePath + 'tdrepayRecharge/queryComplianceRepaymentData', 
			    		page: true,
			    		where: {
			    			confirmTimeStart:this.queryConditionModel.confirmTimeStart, 
			    			confirmTimeEnd:this.queryConditionModel.confirmTimeEnd, 
			    			processStatus:this.queryConditionModel.processStatus, 
			    			repaySource:this.queryConditionModel.repaySource, 
			    			settleType:this.queryConditionModel.settleType, 
			    			origBusinessId:this.queryConditionModel.origBusinessId, 
			    			businessType:this.queryConditionModel.businessType, 
			    			platStatus:this.queryConditionModel.platStatus, 
			    			companyName:this.queryConditionModel.companyName, 
			            },
			    		done: (res, curr, count) => {
			    			this.selectAmount = 0;
			    			this.ComplianceRepaymentData = res.data;
			    			this.queryConditionModel.page = curr;
			    		}
			    })
			},
			
			/*
			 * 查询资金分发处理状态
			 */
			queryDistributeFund: function(){
				axios.get(basePath + 'tdrepayRecharge/queryDistributeFund')
				.then(function(result){
					if (result.data.code == "1") {
						vm.$Modal.success(
							{   
								content: '执行成功',
								onOk: () => {
									vm.queryComplianceRepaymentData();
			                    },
		                    }
						);
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			
			/*
			 * 导出资金分发数据
			 */
			exportComplianceRepaymentData: function(){
				postDownLoadFile({
					url: coreBasePath + 'downLoadController/exportComplianceRepaymentData',
					data: vm.queryConditionModel,
					method: 'post'
				});
			},
			
			/*
			 * 导出充值记录
			 */
			exportRechargeRecordData: function(){
				this.$refs.rechargeRecord.exportCsv({
                    filename: '充值记录'
                });
			},
			
			/*
			 * 查询所有分公司
			 */
			queryAllBusinessCompany: function(){
				axios.get(basePath + 'tdrepayRecharge/queryAllBusinessCompany')
				.then(function(result){
					if (result.data.code == "1") {
						vm.allBusinessCompanyList = result.data.data
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			
			/*
			 * 执行资金分发
			 */
			userDistributeFund: function(){
				if (vm.tdrepayRechargeInfoReqList == null || vm.tdrepayRechargeInfoReqList.length == 0) {
					vm.$Modal.error({ content: "请选择要资金分发的数据" });
					return;
				}
				axios.post(basePath + 'tdrepayRecharge/userDistributeFund', vm.tdrepayRechargeInfoReqList)
				.then(function(result){
					if (result.data.code == "1") {
						vm.$Modal.success(
							{   
								content: '执行成功',
								onOk: () => {
									vm.queryComplianceRepaymentData();
			                    },
		                    }
						);
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			
			/*
			 * 赋值详情信息基础信息
			 */
			getInfoBasicData: function(data){
				vm.infoBasicData.businessId = data.origBusinessId;
				vm.infoBasicData.projectId = data.projectId;
				vm.infoBasicData.customerName = data.customerName;
				
				/*
				 * 根据标ID获取对应的标还款计划信息
				 */
				axios.get(basePath + 'tdrepayRecharge/queryRepaymentProjPlan?projectId=' + data.projectId)
				.then(function(result){
					if (result.data.code == "1") {
						vm.repaymentProjPlan = result.data.data;
						vm.infoBasicData.amount = result.data.data.borrowMoney;
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			
			/*
             * 初始化方法
             */
            initFunction: function(event){
            	this.infoTabValue = event;
            	if (event == 'platformRealRepayment') {
					this.getProjectPayment();
				}else if (event == 'advancesRecord') {
					this.returnAdvanceShareProfit();
				}else if (event == 'fundDistributionRecord') {
					this.queryDistributeFundRecord();
				}
            },
            
			/*
			 * 根据标ID获取垫付记录
			 */
			returnAdvanceShareProfit: function(){
				axios.get(basePath +"tdrepayRecharge/returnAdvanceShareProfit?projectId=" + this.projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.advancePaymentInfoData = res.data.data.returnAdvanceShareProfits;
    	            	vm.queryGuaranteePaymentData = res.data.data.tdGuaranteePaymentVOs;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 标的还款信息查询接口
			 */
			getProjectPayment: function(){
				axios.get(basePath +"tdrepayRecharge/getProjectPayment?projectId=" + this.projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.platformRepaymentInfoData = res.data.data.periodsList;
    	            	vm.platformActualRepaymentInfoData = res.data.data.tdProjectPaymentDTOs;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 查询资金分发记录
			 */
			queryDistributeFundRecord: function(){
				axios.get(basePath +"tdrepayRecharge/queryDistributeFundRecord?projectId=" + this.projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.distributeFundRecordList = res.data.data;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 查看充值记录
			 */
			queryRechargeRecord:function(){
				axios.post(basePath + 'tdrepayRecharge/queryRechargeRecord', vm.queryRechargeRecordModel)
				.then(function(result){
					console.log(result);
					if (result.data.code == 0) {
						vm.queryRechargeRecordModel = vm.initQueryRechargeRecordModel;
						vm.queryRechargeRecordData = result.data.data;
						vm.rechargeRecordTotal = result.data.count;
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
			},
			
			/*
			 * 查询所有代充值账户余额
			 */
			queryRechargeAccountBalance: function(){
				vm.queryRechargeAccountBalanceLoading = true;
				axios.get(basePath +"tdrepayRecharge/queryRechargeAccountBalance")
    	        .then(function (res) {
    	        	vm.rechargeAccountBalanceFlag = true;
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.queryRechargeAccountBalanceData = res.data.data;
    	            	vm.queryRechargeAccountBalanceLoading = false;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            	vm.rechargeAccountBalanceFlag = false;
    	            	vm.queryRechargeAccountBalanceLoading = false;
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        	vm.rechargeAccountBalanceFlag = false;
    	        	vm.queryRechargeAccountBalanceLoading = false;
    	        });
			},
			
			/*
			 * 查询所有业务类型集合
			 */
			queryBusinessTypes: function(){
				axios.get(basePath +"tdrepayRecharge/queryBusinessTypes")
				.then(function (res) {
					if (res.data.data != null && res.data.code == 1) {
						vm.businessTypeList = res.data.data;
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
			},
			
			/*
			 * 查询所有业务类型集合
			 */
			queryRechargeAccountTypes: function(){
				axios.get(basePath +"recharge/queryRechargeAccountTypes")
				.then(function (res) {
					if (res.data.data != null && res.data.code == 1) {
						vm.rechargeAccountTypeList = res.data.data;
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
			},
			
			/*
			 * 查看充值记录分页
			 */
			paging: function (page) {
         	  	this.$refs['pages'].currentPage = page;
	            this.queryRechargeRecordModel.page = page;
	            this.queryRechargeRecord();
			},
		},

		mounted: function() {
			this.queryBusinessTypes();
			this.queryRechargeAccountTypes();
			this.queryUserAviMoney();
			this.queryAllBusinessCompany();
			this.queryComplianceRepaymentData();
		},

	});
	
	table.on('tool(complianceRepaymentData)', function (obj) {
		let event = obj.event;
		if (event == 'info') {
			vm.projectId = obj.data.projectId;
			vm.queryDistributeFundRecord();
			vm.openInfoModal(obj.data);
		}
	});
	
	table.on('checkbox(complianceRepaymentData)', function(obj){
		if (obj && obj.type != 'all' && obj.checked) {
			vm.tdrepayRechargeInfoReqList.push(obj.data)
			vm.selectAmount += obj.data.rechargeAmount;
		}else if (obj && obj.type != 'all' && !obj.checked) {
			if (vm.tdrepayRechargeInfoReqList.length > 0) {
				let dataIndex = vm.tdrepayRechargeInfoReqList.indexOf(obj.data);
				vm.tdrepayRechargeInfoReqList.splice(dataIndex, 1);
			}
			vm.selectAmount -= obj.data.rechargeAmount;
		}else if (obj && obj.type == 'all' && obj.checked) {
			vm.selectAmount = 0;
			vm.tdrepayRechargeInfoReqList = [];
			if (vm.ComplianceRepaymentData && vm.ComplianceRepaymentData.length > 0) {
				for (var i = 0; i < vm.ComplianceRepaymentData.length; i++) {
					vm.tdrepayRechargeInfoReqList.push(vm.ComplianceRepaymentData[i]);
					vm.selectAmount += vm.ComplianceRepaymentData[i].rechargeAmount;
				}
			}
		}else {
			vm.tdrepayRechargeInfoReqList = [];
			vm.selectAmount = 0;
		}
		console.log(obj)
	});

});
