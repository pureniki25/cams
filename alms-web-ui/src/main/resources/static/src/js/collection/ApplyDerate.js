
// var layer;
// var table;
var basePath;
var vm;

//业务ID
var businessId = document.getElementById("businessId").getAttribute("value");
//还款计划ID
var crpId = document.getElementById("crpId").getAttribute("value");
//流程状态
var processStatus = document.getElementById("processStatus").getAttribute("value");
//流程实例ID
var processId = document.getElementById("processId").getAttribute("value");


//设置表单验证
var setFormValidate = {
    derateMoney: [
        {required: true, message: '请填写申请减免金额'},
        // {pattern:/^^[0-9]{8}$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
        // {pattern:/^([0-9]{1,2})+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
        {pattern:/^\d{1,8}(\.\d{1,2})?$/,   message: '请填写整数位数不大于8位，小数位数不超过两位的数字', trigger: 'change'}
    ],
    derateType: [
        {required: true, message: '请填写申请减免费用项', trigger: 'change'}
    ],
    isSettleFlage: [
        {required: true, message: '请填选择是否结清', trigger: 'change'}
    ],
    realReceiveMoney: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ],
	derateReson: [
		{required: true, message: '必填', trigger: 'change'}  ,  
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

    getShowInfo();

    vm = new Vue({
        el: '#app',
        data: {
            copyTo2: [
                {name: '张三', id:'10086'},
                {name: '李四', id:'10000'}
            ],

            process2: [
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false },
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false },
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false },
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false },
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false },
                { name: 'quyudaihouzhuguan1', time: '2018-03-01', job: '区域贷后主管', content: '我同意这条单，做梦！', confirm: false }
            ],



            //流程状态标志位
            // processStatus:processStatus,
            step:300,
            //实收金额是否显示的标志位
            // realReceiveMoneyShowFlage:false,
            //实收金额是否可编辑的标志位
            realReceiveMoneyEditFlage:false,

            //保存草稿按钮是否显示的标志位
            saveDraftShowFlage:true,
            //其他按钮是否显示的标志位
            buttonShowFlage:true,


//////////////   -------------   基础信息  开始 --------------------//////////////////////
            baseInfoForm:{
                businessId:""		   	, //业务编号
                customerName:""        , //客户名称
                companyId:""           , //所属分公司ID
                companyName:""  		, //所属分公司		需处理
                businessType:""        , //业务类型Id
                businessTypeName:""	, //业务类型   		需处理
                borrowLimit:""         , //借款期限
                repaymentTypeId:""     , //还款方式Id
                repaymentTypeName:""   , //还款方式  		需处理
                borrowRateStr:""   			, //借款利率		需处理
                borrowRateName:"年利率："		, //利率名称 		需处理
                borrowMoney: ""         , //借款金额
                getMoney:"" 			, //出款金额   		待找
                remianderPrincipal:"" 	, //剩余本金   		待计算
                periods:""             , //当前还款期数
                delayDays:""           , //逾期天数
                needPayInterest:""  	, //应付利息    	待找
                needPayPrincipal:""   	, //应付本金    	待找
                needPayPenalty:"" 		, //应付违约金 		待找
                otherPayAmount:"" 		, //应付其他费用	待找
                totalBorrowAmount:""   , //应付总额

            },
            
            returnRegFiles:[{
	    		file: '',
	    		name:'',
	    		originalName: '',
	    		oldDocId:'',
	    		downloadFileName:'',
	   			docUrl:''
	    	}],
	    	reqRegFiles:[{
	    		originalName: '',
	    		oldDocId:''
	    	}],
//////////////   -------------   基础信息  结束 --------------------//////////////////////

//////////////   -------------   申请减免信息  开始 --------------------//////////////////////

            //申请减免信息表单
            applyInfoForm:{
                applyDerateId:'',           //申请减免列表ID
                derateType:'',				//申请减免费用项
                derateMoney:'',				//申请减免金额
                realReceiveMoney:'',		//减免后实收总额
                derateReson:'',				//减免原因
                title:'',                    //减免标题
                isSettle:'',                //是否结清标志位
                isSettleFlage:'',           //是否结清界面显示标志位
            },
            //后台返回的初始的减免信息
            initalApplyInfo:{},

            derateTypeList:[  //申请减免类型
                /*            {
                                paramValue:'1',
                                paramName:'申请类型一'
                            },
                            {
                                paramValue:'2',
                                paramName:'申请类型二'
                            }*/
            ],

            validApplyInfoForm: setFormValidate,
//////////////   -------------   申请减免信息  结束 --------------------//////////////////////

//////////////   -------------   审批流程信息  开始 --------------------//////////////////////

            //审批信息是否显示的标志位
            approvalInfoFormShowFlage:false,
            //申请信息是否可编辑的标志位
            applyInfoFormEditFlage:true,
            //审批操作的信息是否显示标志位
            applyOprShowFlage:true,
            //能审批的标志位
            canApproveFlage:true,

            //是否回退按钮显示标志
            rockBackShowFlage:false,
            //回退步骤选择框标志位
            rockBackStepShowFlage:false,
            //流程显示
            myProcess: [
                { title: '信贷专员',       content: '提交上标'},
                { title: '团队主管',       content: '上标审批'},
                { title: '区域复审法务',   content: '上标审批', inActive: true},
                { title: '区域初审法务',   content: '上标审批'},
                { title: '团队主管',       content: '上标审批'},
                { title: '财务出款',       content: '流程结束'}
            ],

            //审批信息
            approvalInfoForm:{
                //当前审批者是否是创建者标志位
                isCreaterFlage:true,
                //审批信息
                process:{},

                /*当前审批人信息*/
                approvalUserInfo:'总部综合岗审批',//审批人职务信息
                isPass			:'',//是否同意
                isPassBoolean:true,    //是否同意 Boolean标志位
                isPassFlage			:'',//是否同意界面显示标志位
                isDirectBack:'',        //是否回退
                isDirectBackBoolean:false,        //是否回退 Boolean标志位
                isDirectBackFlage:'',        //是否回退标界面显示志位
                nextStep:'',      //回退的步骤ID
                remark	:'',//审批意见
                approveUserName	:'',//审批人员姓名
                approveUserId:'',     //流程审核人id
                approveDate		:'',//审批日期
                /*当前审批人信息*/

                /* 抄送信息 */
                isCopySend:'',      //是否抄送标志位
                sendUserIds		:[],//抄送人ID,以逗号间隔
                // sendUserNames		:'',//抄送人名字,以逗号间隔
                copySendInfo		:'',//抄送内容
            },



            /*历史审批列表*/
            approvalInfoList:[
                {
                    id:'1', //id
                    approvalUserInfo:'区域贷后主管审批',//审批人职务信息
                    isPass		:'是',//是否同意
                    isPassFlage			:'是',//是否同意  界面显示标志位
                    approveContent	:'柔柔弱弱若若若若若若若若若',//审批意见
                    approveUserName	:'test11',//审批人员姓名
                    approveDate		:'2018-2-3'//审批日期
                },
                {
                    id:'2', //id
                    approvalUserInfo:'贷后清算主管审批',    //审批人职务信息
                    isPass		:'否',//是否同意
                    isPassFlage			:'否',//是否同意  界面显示标志位
                    approveContent	:'11111111111',//审批意见
                    approveUserName	:'test12',//审批人员姓名
                    approveDate		:'2018-2-3'//审批日期
                },
            ],
            //回退步骤列表
            rockBackStepList:[],

            //可发送审批信息的用户列表
            canSendUserList:[
                {
                    userId:'id1',          //用户ID
                    userName:'张三'         //用户名字
                },
                {
                    userId:'id2',          //用户ID
                    userName:'李四'         //用户名字
                }
            ],



            approvalInfoFormValidate:approvalInfoFormValidate1,
//////////////   -------------   审批流程信息  结束 --------------------//////////////////////
        },
        methods: {
            handleReset (name) {
                var tt =this.$refs[name];
                tt.resetFields();
                // closePareantLayer();
            },
            resetEdit(){  //撤销操作
            	closePareantLayer();
            },
            saveDeraf(){//保存草稿
                saveapplyInfo(PROCESS_STATUS_NEW);
            },
            saveAppply(){//提交审批按钮的响应函数
                console.log(this)
                Submit();
            },
            //减免类型变化响应事件
            derateTypeChange(){
                for(var i=0;i<this.derateTypeList.length;i++){
                    if(this.derateTypeList[i].paramValue == this.applyInfoForm.derateType){
                        this.applyInfoForm.title = this.baseInfoForm.customerName+this.derateTypeList[i].paramName+"减免申请";
                    }
                }
            },

 ////////  ------------------   流程审批 响应函数 开始 --------------------////////////
            //判断是否审批通过
            isPassClick(flage){
                this.rockBackShowFlage = !flage
                this.approvalInfoForm.isPassBoolean = flage;  //是否同意 Boolean标志位
                this.rockBackStepShowFlage = !flage&&vm.approvalInfoForm.isDirectBackBoolean ;

            },
            //是否回退
            isRockBackClick(flage){
                this.approvalInfoForm.isDirectBackBoolean = flage;  //是否回退 Boolean标志位
                this.rockBackStepShowFlage = !vm.approvalInfoForm.isPassBoolean&&flage

            },

////////  ------------------   流程审批 响应函数 结束 --------------------////////////
            
            uploadFile:function(event,index){
	    		var fileId=event.currentTarget.id;
	    		// alert(JSON.stringify($('#'+fileId)));
	    		 $('.progress .bar').css({'background-color':'#E8E8E8'});
	    		 $('.progress .bar').text('0%');
	    		 $('#'+fileId).fileupload({
	    			    dataType: 'json',
	                    singleFileUploads: true,
	                    acceptFileTypes: '', // Allowed file types
	    		        url: basePath+'doc/singleUpload',// 文件的后台接受地址
	    		        maxNumberOfFiles: 1,// 最大上传文件数目
	    		        maxFileSize: 10485760,// 文件不超过5M
	    		        sequentialUploads: true,// 是否队列上传
	    		        dataType: 'json',// 期望从服务器得到json类型的返回数据
	    		        /*******************************************************
						 * 设置进度条 progressall: function (e, data) { var progress =
						 * parseInt(data.loaded / data.total * 100);
						 * $('#progress .bar').css( 'width', progress + '%' ); },
						 ******************************************************/
	    		        // 进度条
	    		        progressall: function (e, data) {
	    		        
	                        var progress = parseInt(data.loaded / data.total * 100, 10);
	                        $('.progress .bar').css(
	                        		{'width':progress + '%',
	                        			'background-color':'#7CCD7C',
	                        		}
	                            
	                        );
	                        $('.progress .bar').text(progress + '%');
	                    },
	    		        done: function (e, data){
	    		        	// alert("bbb");
	    		        	var reFile=data._response.result.docItemListJson;
	    		        	var reFiles=eval("("+reFile+")"); 
	    		        	// alert(JSON.stringify(reFiles));
	    		        	vm.returnRegFiles[index].oldDocId=reFiles.docId;
	    		        	vm.returnRegFiles[index].originalName=reFiles.originalName;
	    		        	vm.returnRegFiles[index].name=reFiles.originalName;
	    		        	vm.returnRegFiles[index].name=reFiles.originalName;
	    		        	vm.returnRegFiles[index].downloadFileName =reFiles.originalName;
	                		vm.returnRegFiles[index].docUrl = reFiles.docUrl;
	    		        	// alert(JSON.stringify(vm.returnRegFiles[0]));
	    		        	// $('#'+fileId).val(vm.returnRegFiles[0].file);
	                		 $('.progress .bar').text("100%");
	    		        },
	    		        fail: function (event, data) {
	    		        	//alert(JSON.stringify(data));
	    		        },
	    		        add:function (event,data){
	    		            if (data!=null && data.originalFiles!=null && data.originalFiles.length > 0) {
	   	                     data.formData = {fileName: data.originalFiles[0].name};
	   	                     data.formData.businessId = businessId;
	   	                     data.formData.busType='AfterLoan_Material_Reduction';
	   	                     data.formData.file=data.originalFiles[0];
		    		        	var size=data.originalFiles[0].size;
		    		        	 if(size/1024/1024 > 10){
			                    	 layer.msg("文件过大，超过5M不允许上传",{icon:5,shade: [0.8, '#393D49'],time:3000});
			                    	 return;
		                     }
		    		        	 data.submit();

	   	                 } else{
	   	                	 layer.msg("文件不存在",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    	 return;
	    		            }
	    		        }
	    		    });
	           /***************************************************************
				 * $('#'+fileId).bind('fileuploadsubmit', function (e, data) {
				 * if (data && data.originalFiles && data.originalFiles.length >
				 * 0) { data.formData = {fileName: data.originalFiles[0].name};
				 * data.formData.businessId = businessId;
				 * data.formData.busType='AfterLoan_Material_CarAuction';
				 * data.formData.file=data.originalFiles[0];
				 * 
				 * /// alert(data.formData.file.size/1024/1024); } });
				 **************************************************************/
	
	    	},
            
            /*uploadFile:function(event,index){
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
	    	},*/
	    	downloadFile: function(info){
				return basePath+'downLoadController/download?downloadFile='+info.downloadFileName + '&docUrl=' + info.docUrl
	    	},
	    	removeTabTr: function (event, index) {
	    		var docId=$('#docId'+index).val();  
	    		var that = this;
	    		// 如果文档id存在，那么进行ajax
	    		if (docId) {
	    			var url = basePath+'doc/delOneDoc?docId='+docId;
	    			axios.get(url)
	    	        .then(function (res) {
	    	        	console.log(res, that);
	                	that.returnRegFiles.splice(index, 1);
	    	            if(that.returnRegFiles == ""){
	    	            	that.addTabTr(event);
	    	            }
	    	        })
	    	        .catch(function (error) {
	    	        	layer.msg("删除文件失败。");
	    	        	console.error(error);
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
	    	}
        }
    });


})

/////////////  流程审批相关函数 开始  ///////////////
//撤销流程审批操作信息
var restProcessApprovalInfo = function(){
    vm.approvalInfoForm.isPassFlage='';//是否同意
    vm.approvalInfoForm.approveContent='';//审批意见

    vm.approvalInfoForm.sendUserIds=[];//抄送人ID,以逗号间隔
    vm.approvalInfoForm.sendContent='';//抄送内容
}

/////////////  流程审批相关函数 结束  ///////////////

//从后台获取显示数据
var getShowInfo = function () {debugger

    //取显示需要的相关信息
    var reqStr = basePath +"ApplyDerateController/selectApplyDeratePageShowInfo?crpId="+crpId
    if(processId !=null){
        reqStr += "&processId="+processId;
    }
    axios.get(reqStr)
        .then(function (res) {
            if (res.data.code == "1") {
////////// --------------  基本信息  赋值  开始---------------//////////
                //基本信息
                if(res.data.data.baseInfo.length>0){
                    vm.baseInfoForm = res.data.data.baseInfo[0];
                }
 ////////// --------------  基本信息  赋值  结束---------------//////////

////////// --------------  减免信息  赋值  开始---------------//////////
                //减免类型显示
                vm.derateTypeList = res.data.data.derateTypeList;

                if(res.data.data.applyList !=null && res.data.data.applyList.length>0){
                    //赋值申请信息
                    vm.applyInfoForm = res.data.data.applyList[0];

                    vm.applyInfoForm.isSettleFlage = vm.applyInfoForm.isSettle=="1"?"是":"否";
                    //赋值审批信息初始信息
                    vm.initalApplyInfo.isSettleFlage = vm.initalApplyInfo.isSettle=="1"?"是":"否";
                    vm.initalApplyInfo = res.data.data.applyList[0];
                    for(var i=0;i<vm.derateTypeList.length;i++){
                        if(vm.derateTypeList[i].paramValue == vm.applyInfoForm.derateType){
                            vm.applyInfoForm.title = vm.baseInfoForm.customerName+vm.derateTypeList[i].paramName+"减免申请";
                        }
                    }
                }else{
                    vm.applyInfoForm.title = vm.baseInfoForm.customerName+"减免申请";
                }

////////// --------------  减免信息  赋值  结束---------------//////////



                // this.applyInfoForm.title = this.baseInfoForm.customerName+derateTypeList[i].paramName+"减免申请";


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
                    
                    var docFiles=res.data.data.returnRegFiles;
                	
                	if(docFiles != null && docFiles.length > 0){
                		vm.returnRegFiles[docFiles.length];
	                	for (var i = 0; i < docFiles.length; i++){
	                		if(i > 0){
	                			vm.returnRegFiles.push({
		   	               			 file: '',
		   	               			 name:docFiles[i].originalName,
		   	               			 originalName: docFiles[i].originalName,
		   	               			 oldDocId:docFiles[i].docId,
		   	               			 downloadFileName:docFiles[i].originalName,
		   	               			 docUrl:docFiles[i].docUrl
	   	               		 	});
	                		}else{
	                			vm.returnRegFiles[i].oldDocId = docFiles[i].docId;
		                		vm.returnRegFiles[i].originalName = docFiles[i].originalName;
		                		vm.returnRegFiles[i].name = docFiles[i].originalName;
		                		vm.returnRegFiles[i].downloadFileName = docFiles[i].originalName;
		                		vm.returnRegFiles[i].docUrl = docFiles[i].docUrl;
	                		}
	                		// i++;
	                	}
                	}

 ////////// --------------  界面显示控制   结束---------------//////////

                //控制界面显示
                if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){//流程状态为新建或开始时
                    //设置申请信息可编辑
                    // vm.applyInfoFormEditFlage = true;
                    vm.realReceiveMoneyEditFlage = false;

                    //审批信息显示标志位置为false
                    // vm.applyOprShowFlage = false;

                }else {
                    //控制保存草稿按钮不可见
                    vm.saveDraftShowFlage = false;

                    //控制申请信息不可编辑
                    // vm.applyInfoFormEditFlage = false;
                    //修改申请信息校验规则
                    // vm.validApplyInfoForm = setFormValidate2;
                    //修改审批信息校验规则
                    // vm.approvalInfoFormValidate =approvalInfoFormValidate1;

                    //状态为进行中
                    if(processStatus == PROCESS_STATUS_RUNNING){
                        //当前审批为贷后中心总监
                        if(vm.approvalInfoForm.process.currentStep == APPLY_DERATE_LAST_STATUS){
                            vm.realReceiveMoneyEditFlage = true;
                            //修改申请信息校验项
                            // vm.validApplyInfoForm = setFormValidate1;
                        }
                        vm.buttonShowFlage = vm.canApproveFlage
                    }else{
                        //控制审批填写和抄送不可见
                        // vm.applyOprShowFlage = false;
                        //控制按钮不可见
                        vm.buttonShowFlage = false;
                        //修改校验信息为空
                        // vm.approvalInfoFormValidate =approvalInfoFormValidate;
                    }
                    //申请信息是否可编辑  标志位设置
//                    vm.applyInfoFormEditFlage = vm.approvalInfoForm.isCreaterFlage;
                    vm.applyInfoFormEditFlage = false;
////////// --------------  界面显示控制   结束---------------//////////




                }

            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}


/**
 * 只校验 实收金额
 * @type {{realReceiveMoney: [null,null],  : [null], derateType: [null], isSettleFlage: [null]}}
 */
var setFormValidate1 = {
    realReceiveMoney: [
        {required: true, message: '请填写实收金额', trigger: 'blur'},
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ]
    // derateMoney: [
    //     {required: false, message: '', trigger: 'blur'},
    // ],
    // derateType: [
    //     {required: false, message: '', trigger: 'change'}
    // ],
    // isSettleFlage: [
    //     {required: false, message: '', trigger: 'change'}
    // ]
};

// {pattern:/^[0-9]+(\\.[0-9]{1,2})?$/,  message: '请填写不超过两位小数的数字', trigger: 'blur'}
// {pattern:/^(([1-9][0-9]*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$/,  message: '请填写不超过两位小数的数字', trigger: 'blur'}
var setFormValidate2 = {};

var approvalInfoFormValidate = {};

var approvalInfoFormValidate1 = {
    isPassFlage: [
        {required: true, message: '请选择是否同意', trigger: 'change'}
    ]
};





//提交审批按钮响应函数
var Submit = function () {
    if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){
        saveapplyInfo(PROCESS_STATUS_RUNNING);
    }else if(processStatus == PROCESS_STATUS_RUNNING){
        if(vm.approvalInfoForm.process.currentStep == APPLY_DERATE_LAST_STATUS){
            if(vm.applyInfoForm.realReceiveMoney == ''){
                vm.$Message.error({content: '请填写实收金额!'});
                return ;
            }
            vm.approvalInfoForm.realReceiveMoney = vm.applyInfoForm.realReceiveMoney;
        }

        vm.$refs['approvalInfoForm'].validate((valid) => {
            if(valid){
                if(vm.approvalInfoForm.isCreaterFlage){
                    vm.approvalInfoForm.applyInfo = vm.applyInfoForm;
                    vm.$refs['applyInfoForm'].validate((valid) => { //校验申请信息
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

    ////////////  ---------------   审批流程 标志位转换 ------------------///////////////
    vm.approvalInfoForm.isPass = vm.approvalInfoForm.isPassFlage=="是"?"1":"0";   //是否审批通过
    vm.approvalInfoForm.isDirectBack = vm.approvalInfoForm.isDirectBackFlage=="是"?"1":"0";  //是否回退
    ////////////  ---------------   审批流程 标志位转换 ------------------///////////////

    // vm.approvalInfoForm.sendUserIds.length>0?vm.applyInfoForm.isCopySend=1:0;
    axios.post(basePath +'ApplyDerateController/saveApprovalLogInfo',vm.approvalInfoForm )
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


// var setapplyInfoForm

//保存申请信息
var saveapplyInfo = function(pStatus){debugger
    if(processStatus == PROCESS_STATUS_NEW || processStatus == PROCESS_STATUS_START){
        vm.$refs['applyInfoForm'].validate((valid) => {debugger
            if (valid) {
                vm.applyInfoForm.processStatus = pStatus;
                if(vm.applyInfoForm.businessId == null){
                    vm.applyInfoForm.businessId = businessId;
                }
                if(vm.applyInfoForm.crpId == null){
                    vm.applyInfoForm.crpId = crpId;
                }
                // 0 不结清， 1结清
                vm.applyInfoForm.isSettle = vm.applyInfoForm.isSettleFlage=="是"?1:0;
                //赋值 processId
                if(vm.approvalInfoForm.process!=null){
                    vm.applyInfoForm.processId = vm.approvalInfoForm.process.processId;
                }
                
                for(var i=0;i<vm.returnRegFiles.length;i++){
	    			vm.returnRegFiles[i].file='';
	    			vm.reqRegFiles[i]=vm.returnRegFiles[i];

	    		}
                
                $.ajax({
		               type: "POST",
		               url: basePath+'ApplyDerateController/saveApplyDerateInfo',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"applyData":[vm.applyInfoForm],"reqRegFiles":vm.reqRegFiles}),
		               success: function (res) {debugger
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

                // var str = JSON.stringify(vm.applyInfoForm).toString();
                /*axios.post(basePath+ 'ApplyDerateController/saveApplyDerateInfo',vm.applyInfoForm )
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
        });
    }else{
        vm.$Message.error({content: '流程状态不对，不能保存申请信息!'});
    }
}


