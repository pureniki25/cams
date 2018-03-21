var basePath;
var vm;
// 业务ID
var businessId = document.getElementById("businessId").getAttribute("value");
//还款计划ID
var crpId = document.getElementById("crpId").getAttribute("value");
// 流程状态
var processStatus = document.getElementById("processStatus").getAttribute("value");
// 流程实例ID
var processId = document.getElementById("processId").getAttribute("value");

//设置表单验证
var setCommitInfoFormValidate = {
		delayHandover: [
	        {required: true, message: '必填', trigger: 'blur'}
	    ],
		carCondition: [
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
		almsOpinion: [
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
		delayHandoverDesc: [
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
		]
};

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
	
    
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
            
            validCommitInfoForm: setCommitInfoFormValidate,
            
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
	        	borrowMoney:""           , // 借款金额
	        	borrowLimit:""  		, // 借款期限
	        	repaymentTypeId:""        , // 还款方式
	        	borrowRate:""	, 		// 年费率
	        	monthBorrowRate:"",		// 月费率
	        	companyName:""         , // 分公司名称
	        	model:""     , 		// 车辆型号
	        	createTime:""   , 		// 车辆评估时间
	        	evaluationAmount:""   			, // 车辆评估价值
	        	dragDate:""		, // 拖车时间
	        	operatorName: ""         , // 业务主办
	        	factPrincipal:"" 			, // 已还本金
	        	factAccrual:"" 	, // 已还利息
	        	surplusPrincipal:""             , // 剩余本金
	        	dueDate:""           , // 利息交至日期
	        	overdueDays:""  	, // 逾期天数
	        	outputUserName:""   	, // 出款人
	        	factOutputDate:"" 		, // 出款日期
	        },
	        
	        commitInfoForm: {
	        	
				businessId:""		   	, // 业务编号
				estates: '',	// 是否有房产
				carCondition: '',	// 客户车辆目前情况
				almsOpinion: '',	// 贷后意见
				delayHandover: '',	// 是否推迟移交
				delayHandoverDesc: '',	// 是否推迟移交诉讼（最长延期7天及说明理由）
				attachmentUrl: '',	// 附件地址
				processStatus: '',
				crpId:''
			},
			
			componentOption: [
            	{
	            	houseArea: [],
	                detailAddress: '',
	                mortgageSituation: '',// 房产抵押情况
            	}
                 
			],
			
			areaData: [],
			
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
           businessId		:'',// 业务编号
           crpId		:'',// 抄送内容
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
       
       returnRegFiles:[{
   		file: '',
   		name:'',
   		originalName: '',
   		oldDocId:''
	   	}],
	   	reqRegFiles:[{
	   		originalName: '',
	   		oldDocId:''
	   	}],
		
// --------------------//////////////////////
   },
	   methods: {
	       handleReset (name) {
	           var tt =this.$refs[name];
	           tt.resetFields();
	           // closePareantLayer();
	       },
	       resetEdit(){  // 撤销操作
	    	   closePareantLayer();
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
	
	       },
	// //////------------------ 流程审批 响应函数 结束 --------------------////////////
	       
	       uploadFile:function(event,index){
	    		var fileId=event.currentTarget.id;
	    		//alert(JSON.stringify($('#'+fileId)));
	    		// \alert(event.currentTarget);
	    		 $('#'+fileId).fileupload({
	    			    dataType: 'json',
	                    singleFileUploads: true,
	                    acceptFileTypes: '', // Allowed file types
	    		        url: basePath+'doc/singleUpload',// 文件的后台接受地址
	    		        // 设置进度条
	    		        // 上传完成之后的操作，显示在img里面
	    		        done: function (e, data){
	    		        	//alert("bbb");
	    		        	var reFile=data._response.result.docItemListJson;
	    		        	var reFiles=eval("("+reFile+")"); 
	    		        	//alert(JSON.stringify(reFiles));
	    		        	vm.returnRegFiles[index].oldDocId=reFiles.docId;
	    		        	vm.returnRegFiles[index].originalName=reFiles.originalName;
	    		        	vm.returnRegFiles[index].name=reFiles.originalName;
	    		        	//alert(JSON.stringify(vm.returnRegFiles[0]));
	    		        	//$('#'+fileId).val(vm.returnRegFiles[0].file);
	    		        	
	    		        }
	    		    });
	             $('#'+fileId).bind('fileuploadsubmit', function (e, data) {
	                 if (data && data.originalFiles && data.originalFiles.length > 0) {
	                     data.formData = {fileName: data.originalFiles[0].name};
	                     data.formData.businessId = businessId;
	                     data.formData.busType='AfterLoan_Material_Litigation';
	                     data.formData.file=data.originalFiles[0];
	                 }
	             });
	    	},
	    	removeTabTr: function (event, index) {
	    		var docId=$('#docId'+index).val();  
	    		var that = this;
	    		// 如果文档id存在，那么进行ajax
	    		if (docId) {
	    			$.ajax({
		                type: "GET",
		                url: basePath+'doc/delOneDoc?docId='+docId,
		                success: function (data) {
		                	console.log(data, that);
		                	that.returnRegFiles.splice(index, 1);
		    	            if(that.returnRegFiles == ""){
		    	            	that.addTabTr(event);
		    	            }
		                },
		                error: function (message) {
		                    layer.msg("删除文件失败。");
		                    console.error(message);
		                }
		            });
	    		// 否则直接删除
	    		} else {
	    			that.returnRegFiles.splice(index, 1);
	  	            if(that.returnRegFiles==""){
	  	            	that.addTabTr(event);
	  	            }
	    		}
	    		
	    	},
	    	addTabTr :function(event){
	    		 this.returnRegFiles.push({
	    			 file: '',
	    			 originalName: '',
	    			 oldDocId:''
	    		 })
	    	},
	    	
	    	getArea: function () {
            	var self = this;
            	var reqStr = basePath + "area/getArea";
            	
            	$.ajax({
	                type: "GET",
	                url: reqStr,
	                async:false,
	                success: function (data) {
	                	self.areaData = data.data;
	                },
	                error: function (message) {
	                    layer.msg("获取区域信息失败");
	                    console.error(message);
	                }
	            });
            	
            },
	    	
	    	addHouseTabTr :function(event){
	    		this.componentOption.push({
	    			houseArea: [],
	                detailAddress: '',
	                mortgageSituation: ''
	    		});
	 	    },
	 	    removeHouseTabTr :function(event, index){
	 	    	this.componentOption.splice(index, 1);
	 	    },
	 	   
	   },		
	   created: function () {
		   this.getArea();
	       getShowInfo();
	   }
	});
});

///////////// 流程审批相关函数 开始 ///////////////
//撤销流程审批操作信息
var restProcessApprovalInfo = function(){
	vm.approvalInfoForm.isPassFlage='';// 是否同意
	vm.approvalInfoForm.approveContent='';// 审批意见
	
	vm.approvalInfoForm.sendUserIds=[];// 抄送人ID,以逗号间隔
	vm.approvalInfoForm.sendContent='';// 抄送内容
}

///////////// 流程审批相关函数 结束 ///////////////

// 从后台获取显示数据
var getShowInfo = function () {

    // 取显示需要的相关信息
    var reqStr = basePath +"transferOfLitigation/queryCarLoanData?businessId="+businessId
    if(processId !=null){
        reqStr += "&processId="+processId;
    }
    axios.get(reqStr)
        .then(function (res) {
            if (res.data.data != null && res.data.code == 1) {
            	vm.baseInfoForm = res.data.data;
            	vm.baseInfoForm.borrowRate += "%/年";
            	vm.baseInfoForm.monthBorrowRate = "(" + vm.baseInfoForm.monthBorrowRate + "%/月)";
            	vm.baseInfoForm.borrowLimit += "个月";
            	
            	if(res.data.data.carList !=null && res.data.data.carList.length>0){
            		//赋值申请信息
            		vm.commitInfoForm = res.data.data.carList[0];
            		//赋值审批信息初始信息
            		vm.initalApplyInfo = res.data.data.carList[0];
            	} 
            	vm.commitInfoForm.businessId = res.data.data.baseInfo.businessId;
            	if (res.data.data.houseAddress != null && res.data.data.houseAddress.length > 0) {
        			vm.componentOption = res.data.data.houseAddress;
				}
            	
            	var docFiles=res.data.data.returnRegFiles;
            	
            	if(docFiles != null && docFiles.length > 0){
            		vm.returnRegFiles[docFiles.length];
                	for (var i = 0; i < docFiles.length; i++){
                		if(i > 0){
                			vm.returnRegFiles.push({
   	               			 file: '',
   	               			 name:docFiles[i].originalName,
   	               			 originalName: docFiles[i].originalName,
   	               			 oldDocId:docFiles[i].docId
   	               		 });
                		}else{
	                		vm.returnRegFiles[i].oldDocId = docFiles[i].docId;
	                		vm.returnRegFiles[i].originalName = docFiles[i].originalName;
	                		vm.returnRegFiles[i].name = docFiles[i].originalName;
                		}
                		// i++;
                	}
            	}
            	
     ////////// --------------  减免信息  赋值  结束---------------//////////

////////////////////---------------  流程审批信息 赋初始值 开始 -------------------////////////
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


////////////////////---------------  流程审批信息 赋初始值 结束 -------------------////////////

////////// --------------  界面显示控制   结束---------------//////////

	          //控制界面显示
	          if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){//流程状态为新建或开始时
	              //设置申请信息可编辑
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
	//////////--------------  界面显示控制   结束---------------//////////
	          }
            } else {
                vm.$Modal.error({content: '执行失败，没有找到数据！' });
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
                    vm.approvalInfoForm.applyInfo = null;
                    vm.$refs['estatesForm'].validate((valid) => { // 校验申请信息
                    	vm.$refs['commitInfoForm'].validate((valid) => { // 校验申请信息
	                        if(valid){
	                            saveApprovalInfo();
	                        }else{
	                            vm.$Message.error({content: '表单校验失败!'});
	                        }
                    	})
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
    
    if (vm.approvalInfoForm.businessId == null || vm.approvalInfoForm.businessId == '') {
    	vm.approvalInfoForm.businessId = businessId;
	}
    if (vm.approvalInfoForm.crpId == null || vm.approvalInfoForm.crpId == '') {
    	vm.approvalInfoForm.crpId = crpId;
    }
    
    axios.post(basePath +'transferOfLitigation/saveCarApprovalLogInfo', vm.approvalInfoForm )
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
        vm.$refs['estatesForm'].validate((valid) => {
        	vm.$refs['commitInfoForm'].validate((valid) => {
	            if (valid) {
	                if(vm.commitInfoForm.businessId == null){
	                    vm.commitInfoForm.businessId = businessId;
	                }
	                if(vm.commitInfoForm.crpId == null){
	                    vm.commitInfoForm.crpId = crpId;
	                }
	              //赋值 processId 
	                if(vm.approvalInfoForm.process!=null){
	                    vm.commitInfoForm.processId = vm.approvalInfoForm.process.processId;
	                }
	            	
			    	vm.commitInfoForm.processStatus = pStatus;
			    	
			    	for(var i=0;i<vm.returnRegFiles.length;i++){
		    			vm.returnRegFiles[i].file='';
		    			vm.reqRegFiles[i]=vm.returnRegFiles[i];

		    		}
			    	
			    	$.ajax({
			               type: "POST",
			               url: basePath+'transferOfLitigation/saveTransferLitigationCar',
			               contentType: "application/json; charset=utf-8",
			               data: JSON.stringify({"houseData":[vm.commitInfoForm],"reqRegFiles":vm.reqRegFiles,"componentOption":vm.componentOption}),
			               success: function (res) {
			            	   if (res.code == "1"){
			            		   vm.$Modal.success({
		                                // title: title,
		                                content: "保存成功",
		                                onOk: () => {
		                                    closePareantLayer()
		                                },
		                            });
			            	   }else{
			            		   vm.$Modal.error({content: '操作失败，消息：' + res.msg});
			            	   }
			               },
			               error: function (message) {
			            	   vm.$Modal.error({content: '接口调用异常!'});
			                   console.error(message);
			               }
			           });
			    	
	                /*axios.post(basePath+ 'transferOfLitigation/saveTransferLitigationCar', vm.commitInfoForm)
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
	                    });*/
	            }else{
	                vm.$Message.error({content: '表单校验失败!'});
	            }
        	})
        });
    }else{
        vm.$Message.error({content: '流程状态不对，不能保存申请信息!'});
    }
}
