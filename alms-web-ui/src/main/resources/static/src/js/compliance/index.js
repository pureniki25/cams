var basePath;
var vm;

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

	vm = new Vue({
		el : '#app',
		data : {
			rechargeModal: false, // 代充值账户充值弹窗控制
			bankCodeList:['GDB','ICBC','BOC','CMBCHINA','PINGAN','HXYH','BOCO','POST','CCB','SPDB','CMBC','BCCB','CEB','CIB','ECITIC','ABC'],	// 银行代码
			bankAccountList: [],// 所有银行账户
			
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
				axios.get(basePath + 'recharge/queryUserAviMoney?rechargeAccountType=' + vm.rechargeModalForm.rechargeAccountType, {timeout: 0})
				.then(function(result){
					if (result.data.code == "1") {
						vm.rechargeModalForm.rechargeAccountBalance = result.data.data.data.aviMoney;
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
								vm.$Modal.success({
				                    content: "提交成功",
				                    onOk: () => {
										vm.rechargeModal = false;
										vm.rechargeModalForm = vm.initRechargeModalForm;
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
		},

		created : function() {
		},

	});

});
