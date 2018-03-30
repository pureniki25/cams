var businessId = document.getElementById("businessId").getAttribute("value");
var processStatus = document.getElementById("processStatus").getAttribute("value");
var processId = document.getElementById("processId").getAttribute("value");
var ex = /^[1-9]\d*$/; 
var amt=/^(([1-9]\d*)|\d)(\.\d{1,2})?$/;
var mobi= /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var tel = /^\d{3,4}-?\d{7,9}$/;
var carNum=/^([1-9]{1})(\d{15}|\d{16}|\d{17}|\d{18})$/;
var currentDate=new Date();
var basePath;
var vm;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    layui.use(['form','laydate','element', 'ht_config', 'ht_auth'], function () {
		
	var element = layui.element;
	var form = layui.form;
	var config = layui.ht_config;
	var laydate = layui.laydate;
	var vm = new Vue({
	    el: '#app',
	    data: {
	    	provs:'',
	    	citys:'',
	        countys:'',
	    	returnRegFiles:[{
	    		file: '',
	    		name:'',
	    		originalName: '',
	 			downloadFileName:'',
	   			docUrl:'',
	    		oldDocId:''
	    	}],
	    	reqRegFiles:[{
	    		originalName: '',
	    		oldDocId:''
	    	}],
	    	business:{
	    		 businessId:''// 业务编号
	    		,customerName:''// 主借款人的客户姓名
	    		,borrowMoney:''// 借款金额
	    		,borrowLimit:''// 借款期限
	    		,repaymentTypeId:''// 还款方式
	    		,borrowRate:''// 借款利率
	    		,operatorName:''//业务主办人
	    	}
		    ,repayPlan:{
		    	payedPrincipal:''//已还本金
		    	,payedInterest:''//已还利
		    	,lastPayDate:''//利息交至日期
		    	,overdueDays:''//逾期天数
		    }
	    	,outputRecord:{//出款
	    		 outputUserName:''//出款人
	    		,factOutputDate:''//出款日期
	    	}
	    	,carBasic:{
	    		vin:''// 车架号
	    		,engineModel:''//发动机号
	    		,displacement:''//排量
	    		,displacementUnit:''//排量单位
	    		,brandName:''//汽车品牌
	    		,licensePlateNumber:''// 车牌号
	    		,model:''// 品牌型号
	    		,originPlace:''//汽车产地
	    		,licenseLocation:''//车辆属地
	    		,color:''//颜色
	    		//,vehicleLicenseRegistrationDate:''//汽车注册日期
	    		//,mortgage:''//车辆抵押状态
	    		//,evaluationAmount:''//抵押时车辆评估价值
	    		//,createTime:''//抵押时车辆评估时间
	    		//,lastEvaluationAmount:''//最新车辆评估价值
	    		//,lastEvaluationTime:''//最新车辆评估时间
	    		//,trafficViolationFee:''//违章费用
	    		//,annualVerificationExpirationDate:''//年审到期日期
	    		//,odometer:''//里程表读数
	    		//,fuelLeft:''//燃油量（约余）
	    		,transferTimes:''//过户次数
	    		,lastTransferDate:''//最后一次过户时间
	    		//,:''//车辆评估价值
	    		//,differAmount:''//价值差额
	    		//,insuranceExpirationDate:''//保险到期日
	    		//,carVersionConfig:''//车型及版本配置
	    		//,userNature:''//车辆使用性质
	    		//,toolWithCar:''//随车工具
	    		//,relatedDocs:''//提供文件
	    		//,trafficViolationSituation:''//违章情况
	    		//,transactionMode:''//交易方式
	    		//,carPosition:''//车辆位置
	    		//,remark:''//备注
	    		//,viewLastEvaluationAmount:''//只展示的最新评估值
	    		//,viewInsuranceExpirationDate:''//只展示的最近评估保险到期日
	    	}
		       ,detection:{
			    	id:''//评估id
			   		,vehicleLicenseRegistrationDate:''//汽车注册日期
			    	,mortgage:''//车辆抵押状态
			    	,evaluationAmount:''//最新车辆评估价值
			    	,createTime:''//最新车辆评估时间 
			    	,trafficViolationFee:''//违章费用
			    	,annualVerificationExpirationDate:''//年审到期日期
			    	,odometer:''//里程表读数
			    	,fuelLeft:''//燃油量（约余）
			    	,insuranceExpirationDate:''//保险到期日
			    	,userNature:''//车辆使用性质
				    ,toolWithCar:''//随车工具
				    ,relatedDocs:''//提供文件
				    ,trafficViolationSituation:''//违章情况
				    ,transactionMode:''//交易方式
				    ,carPosition:''//车辆位置
				    ,remark:''//备注
				    ,differAmount:''//价值差额
				    ,viewEvaluationAmount:''//展示
				    ,assessOdometer:''//评估时的里程数，只针对页面的验证
				    ,viewInsuranceExpirationDate:''//只为展示最近评估的保险到期日期
			       }
			       ,mortgageDetection:{
			    	evaluationAmount:''//抵押时车辆评估价值
				    ,createTime:''//抵押时车辆评估时间 
			       }
	       ,drag:{
	    	   dragDate:''// 拖车日期
	       }
	       ,carAuction:{
	    	   businessId:''//业务编号
	    	   ,auctionId:''//拍卖id
    	   	   ,startingPrice:''//起拍价
    		   ,deposit:''//保证金
    		   ,fareRange:''//加价幅度
    		   ,reservePrice:''//保留价
    		   ,auctionStartTime:''//拍卖开始时间
    		   ,auctionEndTime:''//拍卖截止时间
    		   ,transType:''//交易类型
    		   ,priorityPurchaser:''//优先购权人
    		   ,takeWay:''//取货方式
    		   ,buyStartTime:''//竞买开始时间
    		   ,buyEndTime:''//竞买结束时间
    		   ,auctionPosition:''//拍卖物所在位置
    		   ,handleUnit:''//处置单位
    		   ,unitAddr:''//单位地址
    		   ,consultant:''//咨询人
    		   ,consultantTel:''//咨询电话
    		   ,acountName:''//账户姓名
    		   ,acountNum:''//银行卡号
    		   ,payWay:''//支付方式
    		   ,consStartTime:''//咨询开始时间
    		   ,consStartTime:''//咨询结束时间
    		   ,viewSampleStartTime:''//看样开始时间
    		   ,viewSampleStartTime:''//看样结束时间
    		   ,auctionRules:''//竞价规则
    		   ,openBank:''//开户银行
    		   ,paymentEndTime:''//缴款截止时间
    		   //,assessOdometer:''//评估时的里程数，只针对页面的验证
    		   ,viewSampleAddr:''//看样地址
	 		   ,delayPeriod:''//延时周期
	    	   ,remark:''//备注
	    	   ,transFree:''//交易税费
	       }
	       
	       ,  //可发送审批信息的用户列表
	       originOptions : [
	    	   {id:'1',name:'lemon'},
	    	   {id:'2',name:'mike'},
	    	   {id:'3',name:'lara'},
	    	   {id:'4',name:'zoe'},
	    	   {id:'5',name:'steve'},
	    	   {id:'6',name:'nolan'}
	    	   ]
          ,selectedList : ''
	      ,
          step:300,// 流程状态标志位
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
          
          validCommitInfoForm: '',//setFormValidate// 流程显示
          myProcess: [
              { title: '分公司负责人审批',       content: '拍卖申请'},
              { title: '区域贷后主管审批',       content: '上标审批'},
              { title: '贷后中心总监审批',   content: '上标审批', inActive: true}
          ],
          // 当前审批信息
          approvalInfoForm:{
              // 当前审批者是否是创建者标志位
              isCreaterFlage:true,
              // 审批信息
              process:{},

              /* 当前审批人信息 */
              approvalUserInfo:'总部综合岗审批',// 审批人职务信息
              isPass			:'',// 是否同意
              isPassBoolean:true,    // 是否同意 Boolean标志位
             // isPassFlage			:'',// 是否同意界面显示标志位
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
                  //isPassFlage			:'是',// 是否同意 界面显示标志位
                  approveContent	:'柔柔弱弱若若若若若若若若若',// 审批意见
                  approveUserName	:'test11',// 审批人员姓名
                  approveDate		:'2018-2-3'// 审批日期
                  ,actionDesc:''
              },
              {
                  id:'2', // id
                  approvalUserInfo:'贷后清算主管审批',    // 审批人职务信息
                  isPass		:'否',// 是否同意
                 // isPassFlage			:'否',// 是否同意 界面显示标志位
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
          havedCanSendUserList:[
        	  {
                  userId:'',          // 用户ID
                  userName:''         // 用户名字
              }
          ]
          ,audit:{
        	  processId:''
        	  ,currentStep:''
        	  ,isDirectBack:''
        	  ,nextStep:''
        	  ,backStep:''
        	  ,isPass:''
        	  ,processName:''
        	  ,sendUserIds:[ {
                  userId:'',          // 用户ID
                  userName:''         // 用户名字
              }]
              ,remark:''
          }
	       },
	       watch:{ 
		    	'detection.evaluationAmount':function(val,oldval){
		    		if(val==null||val==''){
		    			$("#lastEvaluationAmount").css("border","1px solid #FF3030");
		    			return;
		    		}
		    		if (!amt.test(val)) {  
		    			$("#lastEvaluationAmount").css("border","1px solid #FF3030");
		    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
		    		this.detection.differAmount=Math.abs(vm.detection.viewEvaluationAmount-val);
		    	}
	       },
	    mounted: function () {
	    	$("#annualVerificationExpirationDate").focus(function(){
	    		  $("#annualVerificationExpirationDate").css("border","1px solid #ccc");
	    		});
	      	$("#odometer").focus(function(){
	    		  $("#odometer").css("border","1px solid #ccc");
	    		});
	    	$("#fuelLeft").focus(function(){
	    		  $("#fuelLeft").css("border","1px solid #ccc");
	    		});
	    	$("#transferTimes").focus(function(){
	    		  $("#transferTimes").css("border","1px solid #ccc");
	    		});
	    	$("#lastTransferDate").focus(function(){
	    		  $("#lastTransferDate").css("border","1px solid #ccc");
	    		});
	    	/**$("#lastEvaluationAmount").blur(function(){
	    		var evaluationAmount=$("#lastEvaluationAmount").val();
	    		if (!amt.test(evaluationAmount)) {  
	    			$("#lastEvaluationAmount").css("border","1px solid #FF3030");
	    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
	    			return;
	    		}
	    		var differAmount=Math.abs(vm.detection.viewEvaluationAmount-evaluationAmount);
	    		$("#differAmount").val(differAmount);
	    	});**/

	    	$("#lastEvaluationAmount").focus(function(){
	    		  $("#lastEvaluationAmount").css("border","1px solid #ccc");
	    		});
	    	$("#insuranceExpirationDate").focus(function(){
	    		  $("#insuranceExpirationDate").css("border","1px solid #ccc");
	    		});
	    	$("#userNature").focus(function(){
	    		  $("#userNature").css("border","1px solid #ccc");
	    		});
	    	$("#toolWithCar").focus(function(){
	    		  $("#toolWithCar").css("border","1px solid #ccc");
	    		});
	    	$("#relatedDocs").focus(function(){
	    		  $("#relatedDocs").css("border","1px solid #ccc");
	    		});
	    	$("#trafficViolationSituation").focus(function(){
	    		  $("#trafficViolationSituation").css("border","1px solid #ccc");
	    		});
	    	$("#transactionMode").focus(function(){
	    		  $("#transactionMode").css("border","1px solid #ccc");
	    		});
	    	$("#carPosition").focus(function(){
	    		  $("#carPosition").css("border","1px solid #ccc");
	    		});
	    	$("#startingPrice").focus(function(){
	    		  $("#startingPrice").css("border","1px solid #ccc");
	    		});
	    	$("#deposit").focus(function(){
	    		  $("#deposit").css("border","1px solid #ccc");
	    		});
	    	$("#fareRange").focus(function(){
	    		  $("#fareRange").css("border","1px solid #ccc");
	    		});
	    	$("#reservePrice").focus(function(){
	    		  $("#reservePrice").css("border","1px solid #ccc");
	    		});
	    	$("#auctionStartTime").focus(function(){
	    		  $("#auctionStartTime").css("border","1px solid #ccc");
	    		});
	       	$("#auctionEndTime").focus(function(){
	    		  $("#auctionEndTime").css("border","1px solid #ccc");
	    		});
	       	$("#transType").focus(function(){
	    		  $("#transType").css("border","1px solid #ccc");
	    		});
	       	$("#priorityPurchaser").focus(function(){
	    		  $("#priorityPurchaser").css("border","1px solid #ccc");
	    		});
	       	$("#takeWay").focus(function(){
	    		  $("#takeWay").css("border","1px solid #ccc");
	    		});
	       	$("#buyStartTime").focus(function(){
	    		  $("#buyStartTime").css("border","1px solid #ccc");
	    		});
	       	$("#buyEndTime").focus(function(){
	    		  $("#buyEndTime").css("border","1px solid #ccc");
	    		});
	       	$("#payWay").focus(function(){
	    		  $("#payWay").css("border","1px solid #ccc");
	    		});
	       	$("#consStartTime").focus(function(){
	    		  $("#consStartTime").css("border","1px solid #ccc");
	    		});
	      	$("#consEndTime").focus(function(){
	    		  $("#consEndTime").css("border","1px solid #ccc");
	    		});
	      	$("#consultant").focus(function(){
	    		  $("#consultant").css("border","1px solid #ccc");
	    		});
	      	$("#viewSampleStartTime").focus(function(){
	    		  $("#viewSampleStartTime").css("border","1px solid #ccc");
	    		});
	      	$("#viewSampleEndTime").focus(function(){
	    		  $("#viewSampleEndTime").css("border","1px solid #ccc");
	    		});
	      	$("#consultantTel").focus(function(){
	    		  $("#consultantTel").css("border","1px solid #ccc");
	    		});
	      	$("#handleUnit").focus(function(){
	    		  $("#handleUnit").css("border","1px solid #ccc");
	    		});
	      	$("#unitAddr").focus(function(){
	    		  $("#unitAddr").css("border","1px solid #ccc");
	    		});
	      	$("#acountName").focus(function(){
	    		  $("#acountName").css("border","1px solid #ccc");
	    		});
	       	$("#openBank").focus(function(){
	    		  $("#openBank").css("border","1px solid #ccc");
	    		});
	       	$("#acountNum").focus(function(){
	    		  $("#acountNum").css("border","1px solid #ccc");
	    		});
	       	$("#paymentEndTime").focus(function(){
	    		  $("#paymentEndTime").css("border","1px solid #ccc");
	    		});
	       	$("#viewSampleAddr").focus(function(){
	    		  $("#viewSampleAddr").css("border","1px solid #ccc");
	    		});
	    	$("#transFree").blur(function(){
	    		var transFree=$("#transFree").val();
	    		if (!amt.test(transFree)) {  
	    			$("#transFree").css("border","1px solid #FF3030");
	    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
	    			return;
	    		}
	    	});
	       	$("#transFree").focus(function(){
	    		  $("#transFree").css("border","1px solid #ccc");
	    		});

	    	this.queryData();
	   	    laydate.render({
		        elem: '#annualVerificationExpirationDate',
		        type:'month',
		        done: (value) => {
		          this.detection.annualVerificationExpirationDate = value
		        }
		    });
	   	    laydate.render({
		        elem: '#lastTransferDate',
		        type:'date',
		        done: (value) => {
		          this.carBasic.lastTransferDate = value
		        }
		    });
	   	    laydate.render({
		        elem: '#insuranceExpirationDate',
		        type:'date',
		        done: (value) => {
		          this.detection.insuranceExpirationDate = value
		        }
		    });
	   	    laydate.render({
		        elem: '#auctionStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.auctionStartTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#auctionEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.auctionEndTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#buyStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.buyStartTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#buyEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.buyEndTime = value
		        }
		    });
	 	    laydate.render({
		        elem: '#consStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.consStartTime = value
		        }
		    });
	 	    laydate.render({
		        elem: '#consEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.consEndTime = value
		        }
		    });
		    laydate.render({
		        elem: '#viewSampleStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.viewSampleStartTime = value
		        }
		    });
		    laydate.render({
		        elem: '#viewSampleEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.viewSampleEndTime = value
		        }
		    });
		    laydate.render({
		        elem: '#paymentEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.paymentEndTime = value
		        }
		    });
	    }, 
	    
	    methods: {
	    	//审核是否通过
	    	isPassClick:function(flage){
                this.rockBackShowFlage = !flage
                this.approvalInfoForm.isPassBoolean = flage;  //是否同意 Boolean标志位
                this.rockBackStepShowFlage = !flage&&vm.approvalInfoForm.isDirectBackBoolean ;
	    	},
	    	//是否回退
	    	isRockBackClick:function(flage){
	    		   this.approvalInfoForm.isDirectBackBoolean = flage;  //是否回退 Boolean标志位
	                this.rockBackStepShowFlage = !vm.approvalInfoForm.isPassBoolean&&flage
	    	},
			queryData: function(){
	            var mySelf = this
	            //do ajax here
	            //mySelf.originOptions=vm.originOptions;
	            //mySelf.selectedList=vm.selectedList;
	            // 多选
	            mySelf.originOptions = [{"id":"1","name":"lemon"},{"id":"2","name":"mike"},{"id":"3","name":"lara"},{"id":"4","name":"zoe"},{"id":"5","name":"steve"},{"id":"6","name":"nolan"}];
	            mySelf.selectedList = '';

	       

	            this.$nextTick(function(){

	            })
			},

	        multipleCallback: function(data){
	            this.selectedList = data;
	            console.log('父级元素调用multipleSelected 选中的是' + JSON.stringify(data))
	            
	        },
	 
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
	    		        // 上传完成之后的操作，显示在img里面
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
	   	                     data.formData.busType='AfterLoan_Material_CarAuction';
	   	                     data.formData.file=data.originalFiles[0];
		    		        	var size=data.originalFiles[0].size;
		    		        	 if(size/1024/1024 > 5){
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
	    	downloadFile: function(info){
	    		if(info==null||info.downloadFileName==null||info.downloadFileName==''
	    			||info.docUrl==null||info.docUrl==''){
	    			 layer.msg("文件不存在",{icon:5,shade: [0.8, '#393D49'],time:3000});
	    			 return;
	    		}
				var url = basePath+'downLoadController/download?downloadFile='+info.downloadFileName + '&docUrl=' + info.docUrl;
				window.open(url);
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
		                    layer.msg("删除文件失败。",{icon:5,shade: [0.8, '#393D49'],time:3000});
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
	
	    	viewData: function(){
	
	            $.ajax({
	                type: "POST",
	                url: basePath+'car/auctionAuditDetail',
	                data: {"businessId":businessId,"processId":processId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                	// alert(JSON.stringify(data));
	                	vm.carBasic=data.data.carBasic;
	                	vm.business=data.data.business;
	                	vm.drag=data.data.drag;
	                 	vm.detection=data.data.detection;
	                	vm.mortgageDetection=data.data.mortgageDetection;
	                	if(data.data.repayPlan!=null&&data.data.repayPlan!=''){
	                		vm.repayPlan=data.data.repayPlan;
	                	}
	                 	if(vm.repayPlan.payedPrincipal==null||vm.repayPlan.payedPrincipal==''){
	                		vm.repayPlan.payedPrincipal=0;
	                	}
	                	vm.outputRecord=data.data.outputRecord;
	                	if(data.data.detection.evaluationAmount==null||data.data.detection.evaluationAmount==''){
	                		vm.carBasic.differAmount=0;
	                	}else{
	                		vm.detection.viewEvaluationAmount=data.data.detection.evaluationAmount;
	                		vm.detection.differAmount=Math.abs(data.data.detection.evaluationAmount-data.data.mortgageDetection.evaluationAmount);
	                	}
	                	vm.detection.assessOdometer=vm.detection.odometer;//为页面验证用
	                	vm.detection.viewInsuranceExpirationDate=vm.detection.insuranceExpirationDate;
	                	vm.carAuction=data.data.carAuction;
	                	if(vm.carAuction.fareRange==null){
	                		vm.carAuction.fareRange='';
	                	}
	                	if(vm.carAuction.takeWay==null){
	                		vm.carAuction.takeWay='';
	                	}
	                	if(vm.carAuction.transType==null){
	                		vm.carAuction.transType='';
	                	}
	                	//alert(JSON.stringify(vm.carAuction));
	                	var docFiles=data.data.returnRegFiles;
	                	
	                	if(docFiles!=null&&docFiles.length>0){
	                		vm.returnRegFiles[docFiles.length];
		                	for (var i=0;i<docFiles.length;i++){
		                		if(i>0){
		                			vm.returnRegFiles.push({
		   	               			 file: '',
		   	               			 name:docFiles[i].originalName,
		   	               			 originalName: docFiles[i].originalName,
		   	               			 oldDocId:docFiles[i].docId,
		   	                    	 downloadFileName:docFiles[i].originalName,
				   	               	 docUrl:docFiles[i].docUrl
		   	               		 });
		                		}else{
			                		vm.returnRegFiles[i].oldDocId=docFiles[i].docId;
			                		vm.returnRegFiles[i].originalName=docFiles[i].originalName;
			                		vm.returnRegFiles[i].name=docFiles[i].originalName;
			                		vm.returnRegFiles[i].downloadFileName = docFiles[i].originalName;
			                		vm.returnRegFiles[i].docUrl = docFiles[i].docUrl;
		                		}
		                		// i++;
		                	}
	                	}
	                	//---------------------流程赋值
	                	 vm.myProcess = data.data.stepArray;
	                     //抄送用户
	                     vm.canSendUserList =data.data.canSendUserList;
	                     //回退步骤列表
	                     vm.rockBackStepList =data.data.rockBackStepList;

	                     //设置当前审批人信息
	                     vm.approvalInfoForm.approvalUserInfo =data.data.currentStepName;
	                     vm.approvalInfoForm.approveUserName = data.data.approveUserName;
	                     vm.approvalInfoForm.approveDate = data.data.approveDate;
	                     //审批信息显示标志位
	                     vm.applyOprShowFlage =data.data.canApproveFlage;
	                     //当前用户能否审批此流程的标志位
	                     vm.canApproveFlage =data.data.canApproveFlage;
	                     //是否是创建者标志位
	                     vm.approvalInfoForm.isCreaterFlage =data.data.isCreaterFlage;
	                     
	                     //赋值流程信息
	                     var p = data.data.process;
	                     if(p!=null&& p.length>0){
	                         vm.approvalInfoForm.process = p[0];
	                    	 vm.audit=p[0];
	                         vm.approvalInfoFormShowFlage = true;
	                         vm.approvalInfoList = data.data.processLogs;
	                        // alert(vm.approvalInfoList.length);
	                        // for(var i = 0;i<vm.approvalInfoList.length;i++){
	                         //    var t = vm.approvalInfoList[i];
	                           //  vm.approvalInfoList[i].isPassFlage = t.isPass=="1"?"是":"否";
	                        // }
	                         processStatus = vm.approvalInfoForm.process.status;
	                     }
	       
	                    // alert(JSON.stringify(  vm.canSendUserList));
	                     //赋值发送人人选
	                     if(vm.canSendUserList!=null&&vm.canSendUserList.length>0){
	                    	 //vm.originOptions[vm.canSendUserList.length];
	                    	 vm.originOptions= new Array()
	                    	 for(var j=0;j<vm.canSendUserList.length;j++){
	                    		 var vu=new Object();
	                    		 vu.id=vm.canSendUserList[j].userId;
	                    		 vu.name=vm.canSendUserList[j].userName;
	                    		 vm.originOptions.push(vu);
	                    	 }
	                     }
	                     //赋值已发送人选
	                  
	                     if(vm.havedCanSendUserList!=null&&vm.havedCanSendUserList.length>0){
	                    	 vm.selectedList=new Array();
	                    	 for(var k=0;k<vm.havedCanSendUserList.length;k++){
	                    		 var selectUser=new Object();
	                    		 selectUser.id=vm.havedCanSendUserList[k].userId;
	                    		 selectUser.name=vm.havedCanSendUserList[k].userName;
	                    		 vm.selectedList.push(selectUser);
	                    	 }
	                     }

	                },
	                error: function (message) {
	                    layer.msg("查询车辆信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	 
	    	},
	    	carAuctionAuditClose(){// 关闭窗口

	    		var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	    		parent.layer.close(index);
	    	},
	    	carAuctionAudit (){
	    	
	    		vm.carAuction.businessId=businessId;
	    		
	    		//页面信息验证

	    		if(vm.detection.annualVerificationExpirationDate==''||vm.detection.annualVerificationExpirationDate==null){
	    			$("#annualVerificationExpirationDate").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		
	    		if(vm.detection.odometer==''||vm.detection.odometer==null){
	    			$("#odometer").css("border","1px solid #FF3030");
	    			return ;
	    		}
	     	
	    		if (!ex.test(vm.detection.odometer)) {  
	    			$("#odometer").css("border","1px solid #FF3030");
	    			layer.msg("请输入整整数！",{icon:5,shade: [0.8, '#393D49']});
	    			return;
	    		}
	    		if(vm.detection.odometer<vm.detection.assessOdometer){
	    			$("#odometer").css("border","1px solid #FF3030");
	    			layer.msg("里程数小于评估时里程数！",{icon:5,shade: [0.8, '#393D49']});
	    			return;
	    		}
	    		if(vm.detection.fuelLeft==''||vm.detection.fuelLeft==null){
	    			$("#fuelLeft").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carBasic.transferTimes !=null&&vm.carBasic.transferTimes!=''){
		    		if (!ex.test(vm.carBasic.transferTimes)) {  
		    			$("#transferTimes").css("border","1px solid #FF3030");
		    			layer.msg("请输入整整数！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
	    		}
	    		if(vm.carBasic.lastTransferDate !=null&&vm.carBasic.lastTransferDate!=''){
	    			var inputDate=new Date(vm.carBasic.lastTransferDate.replace("-", "/").replace("-", "/"));  
	    		
	    			if(inputDate>currentDate){
	    				$("#lastTransferDate").css("border","1px solid #FF3030");
	    				layer.msg("不能大于当前日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	      		if(vm.detection.evaluationAmount==''||vm.detection.evaluationAmount==null){
	    			$("#lastEvaluationAmount").css("border","1px solid #FF3030");
	    			return ;
	    		}
	
	    		if(vm.detection.insuranceExpirationDate !=null&&vm.detection.insuranceExpirationDate!=''){
	    			
	    			var inputDate=new Date(vm.detection.insuranceExpirationDate.replace("-", "/").replace("-", "/")); 
	    			var lastDate=new Date(vm.detection.viewInsuranceExpirationDate.replace("-", "/").replace("-", "/"));
	    			if(inputDate<lastDate){
	    				$("#insuranceExpirationDate").css("border","1px solid #FF3030");
	    				layer.msg("不能小于最后评估时保险到期日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.detection.userNature==''||vm.detection.userNature==null){
	    			$("#userNature").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.detection.toolWithCar==''||vm.detection.toolWithCar==null){
	    			$("#toolWithCar").css("border","1px solid #FF3030");
	    			return ;
	    		}
	       		if(vm.detection.relatedDocs==''||vm.detection.relatedDocs==null){
	    			$("#relatedDocs").css("border","1px solid #FF3030");
	    			return ;
	    		}
	       		if(vm.detection.trafficViolationSituation==''||vm.detection.trafficViolationSituation==null){
	    			$("#trafficViolationSituation").css("border","1px solid #FF3030");
	    			return ;
	    		}
	       		if(vm.detection.transactionMode==''||vm.detection.transactionMode==null){
	    			$("#transactionMode").css("border","1px solid #FF3030");
	    			return ;
	    		}
	       		if(vm.detection.carPosition==''||vm.detection.carPosition==null){
	    			$("#carPosition").css("border","1px solid #FF3030");
	    			return ;
	    		}
	       		//拍卖信息
	      		if(vm.carAuction.startingPrice==''||vm.carAuction.startingPrice==null){
	    			$("#startingPrice").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
		    		if (!amt.test(vm.carAuction.startingPrice)) {  
		    			$("#startingPrice").css("border","1px solid #FF3030");
		    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
	    		}
	     		if(vm.carAuction.deposit==''||vm.carAuction.deposit==null){
	    			$("#deposit").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
		    		if (!amt.test(vm.carAuction.deposit)) {  
		    			$("#deposit").css("border","1px solid #FF3030");
		    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
	    		}
	       		if(vm.carAuction.fareRange==''||vm.carAuction.fareRange==null){
	    			$("#fareRange").css("border","1px solid #FF3030");
	    			return ;
	    		}
	     		if(vm.carAuction.reservePrice==''||vm.carAuction.reservePrice==null){
	    			$("#reservePrice").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
		    		if (!amt.test(vm.carAuction.reservePrice)) {  
		    			$("#reservePrice").css("border","1px solid #FF3030");
		    			layer.msg("请输入有效金额！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
	    		}
	     		if(vm.carAuction.auctionStartTime==''||vm.carAuction.auctionStartTime==null){
	    			$("#auctionStartTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    	
	    			var inputDate=new Date(vm.carAuction.auctionStartTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<currentDate){
	    				$("#auctionStartTime").css("border","1px solid #FF3030");
	    				layer.msg("不能小于当前日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.auctionEndTime==''||vm.carAuction.auctionEndTime==null){
	    			$("#auctionEndTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var startDate=new Date(vm.carAuction.auctionStartTime.replace("-", "/").replace("-", "/"));
	    			var inputDate=new Date(vm.carAuction.auctionEndTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<=startDate){
	    				$("#auctionEndTime").css("border","1px solid #FF3030");
	    				layer.msg("结束时间不能小于等于开始时间！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	      		if(vm.carAuction.transType==''||vm.carAuction.transType==null){
	    			$("#transType").css("border","1px solid #FF3030");
	    			return ;
	    		}
	     		if(vm.carAuction.priorityPurchaser==''||vm.carAuction.priorityPurchaser==null){
	    			$("#priorityPurchaser").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.takeWay==''||vm.carAuction.takeWay==null){
	    			$("#takeWay").css("border","1px solid #FF3030");
	    			return ;
	    		}
	     		if(vm.carAuction.buyStartTime==''||vm.carAuction.buyStartTime==null){
	    			$("#buyStartTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var inputDate=new Date(vm.carAuction.buyStartTime.replace("-", "/").replace("-", "/"));  
	    		
	    			var auctionStartDate=new Date(vm.carAuction.auctionStartTime.replace("-", "/").replace("-", "/"));
	    			var auctionEndTime=new Date(vm.carAuction.auctionEndTime.replace("-", "/").replace("-", "/"));
	    			if(inputDate<auctionStartDate||inputDate>auctionEndTime){
	    				$("#buyStartTime").css("border","1px solid #FF3030");
	    				layer.msg("不能小于拍卖开始时间和大于拍卖结束时间！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.buyEndTime==''||vm.carAuction.buyEndTime==null){
	    			$("#buyEndTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var startDate=new Date(vm.carAuction.buyStartTime.replace("-", "/").replace("-", "/"));
	    			var inputDate=new Date(vm.carAuction.buyEndTime.replace("-", "/").replace("-", "/"));  
	    			var auctionEndTime=new Date(vm.carAuction.auctionEndTime.replace("-", "/").replace("-", "/"));
	    			if(inputDate<=startDate||inputDate>auctionEndTime){
	    				$("#buyEndTime").css("border","1px solid #FF3030");
	    				layer.msg("结束时间不能小于等于开始时间！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.payWay==''||vm.carAuction.payWay==null){
	    			$("#payWay").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.consStartTime==''||vm.carAuction.consStartTime==null){
	    			$("#consStartTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var inputDate=new Date(vm.carAuction.consStartTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<currentDate){
	    				$("#consStartTime").css("border","1px solid #FF3030");
	    				layer.msg("不能小于当前日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.consEndTime==''||vm.carAuction.consEndTime==null){
	    			$("#consEndTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var startDate=new Date(vm.carAuction.consStartTime.replace("-", "/").replace("-", "/"));
	    			var inputDate=new Date(vm.carAuction.consEndTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<=startDate){
	    				$("#consEndTime").css("border","1px solid #FF3030");
	    				layer.msg("结束时间不能小于等于开始时间！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.consultant==''||vm.carAuction.consultant==null){
	    			$("#consultant").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.viewSampleStartTime==''||vm.carAuction.viewSampleStartTime==null){
	    			$("#viewSampleStartTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var inputDate=new Date(vm.carAuction.viewSampleStartTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<currentDate){
	    				$("#viewSampleStartTime").css("border","1px solid #FF3030");
	    				layer.msg("不能小于当前日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.viewSampleEndTime==''||vm.carAuction.viewSampleEndTime==null){
	    			$("#viewSampleEndTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var startDate=new Date(vm.carAuction.viewSampleStartTime.replace("-", "/").replace("-", "/"));
	    			var inputDate=new Date(vm.carAuction.viewSampleEndTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<=startDate){
	    				$("#viewSampleEndTime").css("border","1px solid #FF3030");
	    				layer.msg("结束时间不能小于等于开始时间！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.consultantTel==''||vm.carAuction.consultantTel==null){
	    			$("#consultantTel").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
		    		if (!mobi.test(vm.carAuction.consultantTel)&&!tel.test(vm.carAuction.consultantTel)) {  
		    			$("#consultantTel").css("border","1px solid #FF3030");
		    			layer.msg("电话格式不正确！",{icon:5,shade: [0.8, '#393D49']});
		    			return;
		    		}
	    		}
	    		if(vm.carAuction.handleUnit==''||vm.carAuction.handleUnit==null){
	    			$("#handleUnit").css("border","1px solid #FF3030");
	    			return ;
	    		}
	     		if(vm.carAuction.unitAddr==''||vm.carAuction.unitAddr==null){
	    			$("#unitAddr").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.unitAddr==''||vm.carAuction.unitAddr==null){
	    			$("#unitAddr").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.acountName==''||vm.carAuction.acountName==null){
	    			$("#acountName").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.openBank==''||vm.carAuction.openBank==null){
	    			$("#openBank").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.acountNum==''||vm.carAuction.acountNum==null){
	    			$("#acountNum").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			if(!carNum.test(vm.carAuction.acountNum)){
	    				$("#acountNum").css("border","1px solid #FF3030");
	    				layer.msg("卡号格式有误！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    		}
	    	
	    		if(vm.carAuction.paymentEndTime==''||vm.carAuction.paymentEndTime==null){
	    			$("#paymentEndTime").css("border","1px solid #FF3030");
	    			return ;
	    		}else{
	    			var inputDate=new Date(vm.carAuction.paymentEndTime.replace("-", "/").replace("-", "/"));  
	    			if(inputDate<currentDate){
	    				$("#paymentEndTime").css("border","1px solid #FF3030");
	    				layer.msg("不能小于当前日期！",{icon:5,shade: [0.8, '#393D49']});
	    				return ;
	    			}
	    			
	    		}
	    		if(vm.carAuction.viewSampleAddr==''||vm.carAuction.viewSampleAddr==null){
	    			$("#viewSampleAddr").css("border","1px solid #FF3030");
	    			return ;
	    		}
	    		if(vm.carAuction.transFree==''||vm.carAuction.transFree==null){
	    			$("#transFree").css("border","1px solid #FF3030");
	    			return ;
	    		}

	    		if((vm.approvalInfoForm.isPass==''||vm.approvalInfoForm.isPass==null)&&vm.approvalInfoForm.process.status!=-1){
	    			//alert(processStatus);
	    			layer.msg("请输入是否同意审批",{icon:5,shade: [0.8, '#393D49']});
	    			return ;
	    		}

	    		for(var i=0;i<vm.returnRegFiles.length;i++){
	    			vm.returnRegFiles[i].file='';
	    			vm.reqRegFiles[i]=vm.returnRegFiles[i];

	    		}
	    
	    	
	    		//alert(JSON.stringify(vm.approvalInfoForm));
	    		vm.audit.isPass=vm.approvalInfoForm.isPass;
	    		vm.audit.remark=vm.approvalInfoForm.remark;
	    		vm.audit.isDirectBack=vm.approvalInfoForm.isDirectBack;
	    		vm.audit.nextStep=vm.approvalInfoForm.nextStep;
	    		//vm.audit.remark=vm.approvalInfoForm.remark;
	               if(vm.selectedList!=null&&vm.selectedList.length>0){
	            	   vm.audit.sendUserIds=new Array();
                  	 for(var k=0;k<vm.selectedList.length;k++){
                  		 var u=new Object();
                  		u.userId=vm.selectedList[k].id;
 
                  		u.userName=vm.selectedList[k].name;
                  		vm.audit.sendUserIds.push(u.userId);
                  	 }
                   }
	    	
	    		//alert(JSON.stringify(vm.audit));
		          $.ajax({
		               type: "POST",
		               url: basePath+'car/auctionAudit',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"carBasic":vm.carBasic,"detection":vm.detection,"carAuction":vm.carAuction,"returnRegFiles":vm.reqRegFiles,"auditVo":vm.audit}),
		               success: function (res) {
		            	   if (res.code == "0000"){
		            		   vm.carAuction=res.data.carAuction;
		            		   layer.msg("保存成功。",{icon:1,shade: [0.8, '#393D49'],time:3000}); 
		            		   
		            	   }else{
		            		   layer.msg("提交失败:"+res.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});  
		            	   }
		               },
		               error: function (message) {
		                   layer.msg("异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
		                   console.error(message);
		               }
		           });
		          
	    	}
	    }
	
	});
	vm.viewData();
});
});







