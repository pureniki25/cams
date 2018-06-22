var basePath;
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
			rechargeAccountType: '车贷代充值', // 代充值账户类型
			rechargeAccountBalance: 0, // 代充值账户余额
			allBusinessCompanyList: [], // 所有分公司
			tdrepayRechargeInfoReqList: [], // 
			
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

		},

		methods : {
			/*
			 * 打开代充值账户充值弹窗
			 */
			openRechargeModal: function(){
				this.listAllDepartmentBank();
				this.rechargeModal = true;
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
					if (result.data.code == "1") {
						vm.rechargeModalForm.rechargeAccountBalance = result.data.data.data.aviMoney;
						vm.rechargeAccountBalance = result.data.data.data.aviMoney;
					} else {
						vm.rechargeModalForm.rechargeAccountBalance = '';
						vm.$Modal.error({ content: result.data.msg });
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
				if (value == 9) {
					this.rechargeAccountType = '车贷代充值';
				}else if (value == 11) {
					this.rechargeAccountType = '房贷代充值';
				}else if (value == 20) {
					this.rechargeAccountType = '一点车贷代充值';
				}
				this.queryUserAviMoney();
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
				this.rechargeModal = false;
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
			    			field: 'period',
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
			    			field: 'platStatusStr',
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
			    		}
			    })
			},
			/*
			 * 查看对应记录的费用明细
			 */
			queryFeeDetails: function(){
				
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
			}
		},

		mounted: function() {
			this.queryUserAviMoney();
			this.queryAllBusinessCompany();
			this.queryComplianceRepaymentData();
		},

	});
	
	table.on('tool(complianceRepaymentData)', function (obj) {
		let data = obj.data;
		let event = obj.event;
		if (event == 'info') {
			
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
