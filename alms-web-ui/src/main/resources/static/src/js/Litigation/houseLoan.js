var basePath;
var vm;
// 业务ID
var businessId = document.getElementById("businessId").getAttribute("value");
// 还款计划ID
var crpId = document.getElementById("crpId").getAttribute("value");
// 流程状态
var processStatus = document.getElementById("processStatus").getAttribute("value");
// 流程实例ID
var processId = document.getElementById("processId").getAttribute("value");

//设置表单验证
var setFormValidate = {
	overdueDesc: [
        {required: true, message: '必填', trigger: 'blur'}  ,  
        {
        	validator: function (rule, value, callback, source, options) {
	        	if (value.length > 1000 ) {
	        		callback(new Error('字数长度不能超过1000'));
	        	} else {
	        	callback();//校验通过
	        	}
        	}
        }
    ],
    riskDesc: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    otherPledgeDesc: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    contractDesc: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    firstPecuniaryCondition: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    firstRelatedPersonnel: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    secondPecuniaryCondition: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
    secondRelatedPersonnel: [
    	{required: true, message: '必填', trigger: 'blur'}  ,  
    	{
    		validator: function (rule, value, callback, source, options) {
    			if (value.length > 500 ) {
    				callback(new Error('字数长度不能超过500'));
    			} else {
    				callback();//校验通过
    			}
    		}
    	}
    ],
	transferReceiptCount: [
        {pattern:"^\\d{1,8}?$", message: '请填写整数', trigger: 'blur'}    
    ],
	cashReceiptCount: [
		{pattern:"^\\d{1,8}?$", message: '请填写整数', trigger: 'blur'}    
	],
	ratedPrice: [
		{pattern:"^\\d{1,8}(\\.\\d{1,2})?$", required: true,  message: '请填写整数位数不大于8位，小数位数不超过两位的数字', trigger: 'blur'}  
	],
	liquidate: [
		{required: true, message: '必填', trigger: 'blur'}   
	]
};

window.layinit(function (htConfig) {
    var htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    
	getShowInfo();
	
	vm = new Vue({
	    el: '#app',
	    data: {
	    	
	    	// 流程状态标志位
            step:300,
            // 实收金额是否可编辑的标志位
            realReceiveMoneyEditFlage:false,
            // 审批信息是否显示的标志位
            approvalInfoFormShowFlage:false,
            // 申请信息是否可编辑的标志位
            applyInfoFormEditFlage:true,
            // 审批操作的信息是否显示标志位
            applyOprShowFlage:true,
            // 保存草稿按钮是否显示的标志位
            saveDraftShowFlage:true,
            // 其他按钮是否显示的标志位
            buttonShowFlage:true,
            // 能审批的标志位
            canApproveFlage:true,
            // 是否回退按钮显示标志
            rockBackShowFlage:false,
            // 回退步骤选择框标志位
            rockBackStepShowFlage:false,
            
            validCommitInfoForm: setFormValidate,

            // 流程显示
            myProcess: [
                { title: '信贷专员',       content: '提交上标'},
                { title: '团队主管',       content: '上标审批'},
                { title: '区域复审法务',   content: '上标审批', inActive: true},
                { title: '区域初审法务',   content: '上标审批'},
                { title: '团队主管',       content: '上标审批'},
                { title: '财务出款',       content: '流程结束'}
            ],
	    	
	        baseInfoForm:{
	        	businessId:""		   	, // 业务编号
	        	customerName:""        , // 客户名称
	        	phoneNumber:""        , // 联系电话
	        	businessTypeName:""        , // 业务类型
	        	borrowMoney:""           , // 借款金额
	        	factOutputMoney:""        , // 出款金额
	        	operatorName: ""         , // 业务经办人
	        	originalName:""        , // 业务获取人
	        	lastReviewUserName:""        , // 主审风控
	        	borrowRate:""	, 		// 年费率
	        	borrowLimit:""  		, // 借款期限
	        	notarizationType:""        , // 公证类型
	        	companyName:""        , // 公证类型
	     	    factPrincipal:"" 			, // 已还本金
	     	    surplusPrincipal:""        , // 剩余本金
	     	    overdueDays:""        , // 逾期天数
	     	    housePlanInfos:[ 
	     	    				{
	     	    					repaymentNum:""        , // 还款计划序号
	        						factPrincipal:"" 			, // 已还本金
		     	    				surplusPrincipal:""        , // 剩余本金
		     	    				overdueDays:""        , // 逾期天数
		     	    				dueDate:""			// 利息交至日期
	     	    				}
		     	],	// 还款计划
		     	mortgageInfos:[
		     		{
			     		houseAddress:""        , // 抵押物位置
			        	houseName:""        , // 权属人
			        	ourMortgage:""        , // 我方抵押顺位
			        	lender:""        , // 债权人
			        	pledee:""        , // 抵押权人
			        	mortgageSecurities:""        , // 抵押债权
			        	houseArea:""        , // 抵押物面积
			        	housePrice:""        , // 评估单价
			        	houseTotal:""        , // 评估总价
			        	housePledgedBank:""        , // 第一顺位抵押权人
			        	borrowTotal:""        , // 一抵债权金额
			        	borrowBalance:""        , // 一抵债权余额
			        	secondMortgageBank:""        , // 第二顺位抵押权人
			        	secondMortgageTotal:""        , // 二抵债权金额
			        	secondMortgageBalance:""        , // 二抵债权余额
			        	thirdMortgageBank:""        , // 第三顺位抵押权人
			        	thirdMortgageTotal:""        , // 三抵债权金额
			        	thirdMortgageBalance:""        , // 三抵债权余额
			        	fourthMortgageBank:""        , // 第四顺位抵押权人
			        	fourthMortgageTotal:""        , // 四抵债权金额
			        	fourthMortgageBalance:""        , // 四抵债权余额
			        	surplusValue:""        // 房产剩余空间
		     		}
		     	],	// 抵押物
	        	
	        },
	        
	        mortgageInfoList: [],
	        
	        mortgageInfoTitle:[
	        	{
					title: '序号',
					key: 'repaymentNum'
				},
				{
					title: '抵押物地址',
					key: 'factPrincipal'
				},
				{
					title: '面积（M2）',
					key: 'surplusPrincipal'
				},
				{
					title: '风控审批单价',
					key: 'surplusPrincipal'
				}
	        ],
	        
	        repaymentPlanTitle: [
				{
					title: '还款计划',
					key: 'repaymentNum'
				},
				{
					title: '已还本金',
					key: 'factPrincipal'
				},
				{
					title: '剩余本金',
					key: 'surplusPrincipal'
				},
				{
					title: '逾期天数',
					key: 'overdueDays'
				},
				{
					title: '利息交至',
					key: 'dueDate'
				}
			],
			repaymentPlanTitleData: [
			],
			
			commitInfoForm: {
				businessId:""		   	, // 业务编号
				overdueDesc: '',	// 客户逾期情况描述
				riskDesc: '',	// 未能覆盖的风险口
				otherPledgeDesc: '',	// 其他质押情况
				transferReceiptCount: '',	// 转账收据张数
				cashReceiptCount: '',	// 现金收据张数
				receiptStatus: '',	// 还款收据第二联是否在客户手上
				contractDesc: '',	// 业务合同有无特殊情况
				repaymentIntention: '',	// 客户是否有意向还款
				repaymentIntentionDate: '',	// 意向还款计划
				firstRelatedPersonnel: '',	// 借款人相关人员描述（业务经办调查结果）
				secondRelatedPersonnel: '',	// 借款人相关人员描述（分公司/区域贷后跟进结果）
				firstPecuniaryCondition: '',	// 其他财务情况（业务经办调查结果）
				secondPecuniaryCondition: '',	// 其他财务情况（分公司/区域贷后跟进结果）
				operator: '',	// 移交人
				operateDate: '',	// 移交时间
				ratedPrice: '',	// 额定单价
				liquidate: '',	// 是否易变现
				processStatus: '',
				crpId: ''
			},
// ////////////------------- 基础信息 结束 --------------------//////////////////////

// ////////////------------- 申请减免信息 开始
       
       // 后台返回的初始的减免信息
       initalApplyInfo:{},

// ////////////------------- 申请减免信息 结束
// --------------------//////////////////////

// ////////////------------- 审批流程信息 开始
// --------------------//////////////////////

       // 审批信息
       approvalInfoForm:{
           // 当前审批者是否是创建者标志位
           isCreaterFlage:true,
           // 审批信息
           process:{},

           /* 当前审批人信息 */
           approvalUserInfo:'总部综合岗审批',// 审批人职务信息
           isPass			:'',// 是否同意
           isPassBoolean:true,    // 是否同意 Boolean标志位
           isPassFlage			:'',// 是否同意界面显示标志位
           isDirectBack:'',        // 是否回退
           isDirectBackBoolean:false,        // 是否回退 Boolean标志位
           isDirectBackFlage:'',        // 是否回退标界面显示志位
           nextStep:'',      // 回退的步骤ID
           remark	:'',// 审批意见
           approveUserName	:'',// 审批人员姓名
           approveUserId:'',     // 流程审核人id
           approveDate		:'',// 审批日期
           /* 当前审批人信息 */

           /* 抄送信息 */
           isCopySend:'',      // 是否抄送标志位
           sendUserIds		:[],// 抄送人ID,以逗号间隔
           // sendUserNames :'',//抄送人名字,以逗号间隔
           copySendInfo		:'',// 抄送内容
       },



       /* 历史审批列表 */
       approvalInfoList:[
           {
               id:'1', // id
               approvalUserInfo:'区域贷后主管审批',// 审批人职务信息
               isPass		:'是',// 是否同意
               isPassFlage			:'是',// 是否同意 界面显示标志位
               approveContent	:'柔柔弱弱若若若若若若若若若',// 审批意见
               approveUserName	:'test11',// 审批人员姓名
               approveDate		:'2018-2-3'// 审批日期
           },
           {
               id:'2', // id
               approvalUserInfo:'贷后清算主管审批',    // 审批人职务信息
               isPass		:'否',// 是否同意
               isPassFlage			:'否',// 是否同意 界面显示标志位
               approveContent	:'11111111111',// 审批意见
               approveUserName	:'test12',// 审批人员姓名
               approveDate		:'2018-2-3'// 审批日期
           },
       ],
       // 回退步骤列表
       rockBackStepList:[],

       // 可发送审批信息的用户列表
       canSendUserList:[
           {
               userId:'id1',          // 用户ID
               userName:'张三'         // 用户名字
           },
           {
               userId:'id2',          // 用户ID
               userName:'李四'         // 用户名字
           }
       ],



       approvalInfoFormValidate:approvalInfoFormValidate1,
// ////////////------------- 审批流程信息 结束
// --------------------//////////////////////
   },
	   methods: {
	       handleReset (name) {
	           var tt =this.$refs[name];
	           tt.resetFields();
	           // closePareantLayer();
	       },
	       resetEdit(){  // 撤销操作
	           if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START) {
	               this.handleReset("commitInfoForm");
	           }else{
	               vm.commitInfoForm =  vm.initalApplyInfo;
	           }
	           restProcessApprovalInfo();
	       },
	       saveDeraf(){// 保存草稿
	           saveapplyInfo(PROCESS_STATUS_NEW);
	       },
	       saveAppply(){// 提交审批按钮的响应函数
	           Submit();
	       },
	
	// ////// ------------------ 流程审批 响应函数 开始 --------------------////////////
	       // 判断是否审批通过
	       isPassClick(flage){
	           this.rockBackShowFlage = !flage
	           this.approvalInfoForm.isPassBoolean = flage;  // 是否同意Boolean标志位
	           this.rockBackStepShowFlage = !flage&&vm.approvalInfoForm.isDirectBackBoolean ;
	
	       },
	       // 是否回退
	       isRockBackClick(flage){
	           this.approvalInfoForm.isDirectBackBoolean = flage;  // 是否回退Boolean标志位
	           this.rockBackStepShowFlage = !vm.approvalInfoForm.isPassBoolean&&flage
	
	       }
	// //////------------------ 流程审批 响应函数 结束 --------------------////////////
	   }
	});
    
});


// /////////// 流程审批相关函数 开始 ///////////////
// 撤销流程审批操作信息
var restProcessApprovalInfo = function(){
  vm.approvalInfoForm.isPassFlage='';// 是否同意
  vm.approvalInfoForm.approveContent='';// 审批意见

  vm.approvalInfoForm.sendUserIds=[];// 抄送人ID,以逗号间隔
  vm.approvalInfoForm.sendContent='';// 抄送内容
}

// /////////// 流程审批相关函数 结束 ///////////////

// 从后台获取显示数据
var getShowInfo = function () {

    // 取显示需要的相关信息
    var reqStr = basePath +"transferOfLitigation/queryHouseLoanData?businessId="+businessId
    if(processId !=null){
        reqStr += "&processId="+processId;
    }
    axios.get(reqStr)
        .then(function (res) {
            if (res.data.data.baseInfo != null && res.data.code == 1) {
            	
            	vm.baseInfoForm = res.data.data.baseInfo;
            	vm.baseInfoForm.borrowRate += "%/年";
            	vm.baseInfoForm.borrowLimit += "个月";
            	vm.repaymentPlanTitleData = res.data.data.baseInfo.housePlanInfos;
            	vm.mortgageInfoList = res.data.data.baseInfo.mortgageInfos;
            	
            	if(res.data.data.houseList !=null && res.data.data.houseList.length>0){
            		//赋值申请信息
            		vm.commitInfoForm = res.data.data.houseList[0];
            		//赋值审批信息初始信息
            		vm.initalApplyInfo = res.data.data.houseList[0];
            	} 
            	vm.commitInfoForm.businessId = res.data.data.baseInfo.businessId;
            	

////////////////////  ---------------  流程审批信息 赋初始值 开始 -------------------////////////
                    //流程显示
                    vm.myProcess = res.data.data.stepArray;
                    //抄送用户
                    vm.canSendUserList =res.data.data.canSendUserList;
                    //回退步骤列表
                    vm.rockBackStepList =res.data.data.rockBackStepList;

                    //设置当前审批人信息
                    vm.approvalInfoForm.approvalUserInfo = res.data.data.currentStepName;
                    vm.approvalInfoForm.approveUserName = res.data.data.approveUserName;
                    vm.approvalInfoForm.approveDate = res.data.data.approveDate;
                    //审批信息显示标志位
                    vm.applyOprShowFlage = res.data.data.canApproveFlage;
                    //当前用户能否审批此流程的标志位
                    vm.canApproveFlage = res.data.data.canApproveFlage;
                    //是否是创建者标志位
                    vm.approvalInfoForm.isCreaterFlage = res.data.data.isCreaterFlage;

                    //赋值流程信息
                    var p = res.data.data.process;
                    if(p!=null&& p.length>0){
                        vm.approvalInfoForm.process = p[0];
                        vm.approvalInfoFormShowFlage = true;
                        vm.approvalInfoList = res.data.data.processLogs;
                        for(var i = 0;i<vm.approvalInfoList.length;i++){
                            var t = vm.approvalInfoList[i];
                            vm.approvalInfoList[i].isPassFlage = t.isPass=="1"?"是":"否";
                        }
                        processStatus = vm.approvalInfoForm.process.status;
                    }


////////////////////  ---------------  流程审批信息 赋初始值 结束 -------------------////////////

 ////////// --------------  界面显示控制   结束---------------//////////

                //控制界面显示
                if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){//流程状态为新建或开始时
                    vm.realReceiveMoneyEditFlage = false;
                }else {
                    //控制保存草稿按钮不可见
                    vm.saveDraftShowFlage = false;

                    //状态为进行中
                    if(processStatus == PROCESS_STATUS_RUNNING){
                        //当前审批为贷后中心总监
                        if(vm.approvalInfoForm.process.currentStep == APPLY_DERATE_LAST_STATUS){
                            vm.realReceiveMoneyEditFlage = true;
                        }
                        vm.buttonShowFlage = vm.canApproveFlage
                    }else{
                        //控制按钮不可见
                        vm.buttonShowFlage = false;
                    }
                    //申请信息是否可编辑  标志位设置
                    vm.applyInfoFormEditFlage = vm.approvalInfoForm.isCreaterFlage;
////////// --------------  界面显示控制   结束---------------//////////
                }
            } else {
                vm.$Modal.error({content: '执行失败，没有数据返回！' });
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}

var approvalInfoFormValidate1 = {
	    isPassFlage: [
	        {required: true, message: '请选择是否同意', trigger: 'change'}
	    ]
	};

// 提交审批按钮响应函数
var Submit = function () {
    if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){
        saveapplyInfo(PROCESS_STATUS_RUNNING);
    }else if(processStatus == PROCESS_STATUS_RUNNING){
        if(vm.approvalInfoForm.process.currentStep == APPLY_DERATE_LAST_STATUS){
            vm.approvalInfoForm.realReceiveMoney = '';
        }

        vm.$refs['approvalInfoForm'].validate((valid) => {
            if(valid){
                if(vm.approvalInfoForm.isCreaterFlage){
                    vm.approvalInfoForm.applyInfo = '';
                    vm.$refs['commitInfoForm'].validate((valid) => { // 校验申请信息
                        if(valid){
                            saveApprovalInfo();
                        }else{
                            vm.$Message.error({content: '表单校验失败!'});
                        }
                    })
                }else{
                    saveApprovalInfo();
                }
            }else{
                vm.$Message.error({content: '表单校验失败!'});
            }

        })
    } else{
        vm.$Message.error({content: '流程状态不对，不能保存审批信息!'});
    }

}

var saveApprovalInfo = function(){

    // ////////// --------------- 审批流程 标志位转换 ------------------///////////////
    vm.approvalInfoForm.isPass = vm.approvalInfoForm.isPassFlage=="是"?"1":"2";   // 是否审批通过
    vm.approvalInfoForm.isDirectBack = vm.approvalInfoForm.isDirectBackFlage=="是"?"1":"0";  // 是否回退
    // ////////// --------------- 审批流程 标志位转换 ------------------///////////////

    axios.post(basePath +'transferOfLitigation/saveApprovalLogInfo', vm.approvalInfoForm )
        .then(function (res) {
            if (res.data.code == "1") {
                vm.$Modal.success({
                    // title: title,
                    content: "保存成功",
                    onOk: () => {
                        closePareantLayer()
                    },
                });
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}


// 保存申请信息
var saveapplyInfo = function(pStatus){
    if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){
        vm.$refs['commitInfoForm'].validate((valid) => {
            if (valid) {
            	if(vm.commitInfoForm.businessId == null){
                    vm.commitInfoForm.businessId = businessId;
                }
                if(vm.commitInfoForm.crpId == null || vm.commitInfoForm.crpId == ""){
                    vm.commitInfoForm.crpId = crpId;
                }
            	
		    	vm.commitInfoForm.processStatus = pStatus;
                axios.post(basePath+ 'transferOfLitigation/saveTransferLitigationHouse', vm.commitInfoForm)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            vm.$Modal.success({
                                // title: title,
                                content: "保存成功",
                                onOk: () => {
                                    closePareantLayer()
                                },
                            });
                        } else {
                            vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                        }
                    })
                    .catch(function (error) {
                        vm.$Modal.error({content: '接口调用异常!'});
                    });
            }else{
                vm.$Message.error({content: '表单校验失败!'});
            }
        });
    }else{
        vm.$Message.error({content: '流程状态不对，不能保存申请信息!'});
    }
}