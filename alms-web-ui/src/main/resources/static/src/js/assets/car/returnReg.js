var businessId = document.getElementById("businessId").getAttribute("value");
var dragId = document.getElementById("dragId").getAttribute("value");
window.layinit(function (htConfig) {
    var _htConfig = layui.ht_config;
    var baseCorePath = _htConfig.coreBasePath;
    //basePath = _htConfig.coreBasePath;
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
	    	},
	    	carBasic:{
	    		licensePlateNumber:''// 车牌号
	    		,model:''// 品牌型号
	    		,vin:''// 车架号
	
	    	},
	    	drag:{
	    		dragDate:''// 拖车日期
	    		,dragHandler:''// 拖车经办人
	    		,fee:''// 拖车费
	    		,otherFee:''// 其他相关费用
	    		,province:''// 拖车省份
	    		,city:''// 拖车市
	    		,county:''// 拖车县
	    		,detailAddress:''// 拖车地址
	    		
	    	},
	       returnReg:{
	    	   returnDate:'',
	    	   returnOperator:'',
	    	   returnProId:'',
	    	   returnCityId:'',
	    	   returnCountyId:'',
	    	   returnAddr:'',
	    	   isPayTrailerCost:'',
	    	   payTrailerCost:'',
	    	   payTrailerOtherCost:'',
	    	   note:'',
	    	   dragId:'',//拖车id
	    	   businessId:''//业务编号
	       },
	       dragAddr:''
	    },
	    created: function(){
	        this.returnReg.returnProId = 0;
	        this.returnReg.returnCityId=0;
	        this.returnReg.returnCountyId=0;
	    },
	    mounted: function () {
		   	    laydate.render({
			        elem: '#returnDate',
			        type:'date',
			        done: (value) => {
			          this.returnReg.returnDate = value
			        }
			    });
	    }, 
	    methods: {
	    	
	    	uploadFile:function(event,index){
	    		var fileId=event.currentTarget.id;

	    		 $('.progress .bar').css({'background-color':'#E8E8E8'});
	    		 $('.progress .bar').text('0%');
	    		 $('#'+fileId).fileupload({
	    			    dataType: 'json',
	                    singleFileUploads: true,
	                    acceptFileTypes: '', // Allowed file types
	    		        url: baseCorePath+'doc/singleUpload',// 文件的后台接受地址
	    		        maxNumberOfFiles: 1,// 最大上传文件数目
	    		        maxFileSize: 10485760,// 文件不超过5M
	    		        sequentialUploads: true,// 是否队列上传
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
	   	                     data.formData.busType='AfterLoan_Material_ReturnReg';
	   	                     data.formData.file=data.originalFiles[0];
		    		        	var size=data.originalFiles[0].size;
		    		        	 if(size/1024/1024 > 10){
			                    	 layer.msg("文件过大，超过10M不允许上传",{icon:5,shade: [0.8, '#393D49'],time:3000});
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
	    	selectPro:function(el){
	    		var currProId=this.returnReg.returnProId;
	    		// alert(this.provsSelected);
	            $.ajax({
	                type: "POST",
	                url: baseCorePath+'car/getCitysByProId',
	                data: {"proId":currProId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                	vm.citys=data.data.citys;
	                },
	                error: function (message) {
	                    layer.msg("查询城市信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	    	},
	    	selectCity:function(el){
	    		var currCityId=this.returnReg.returnCityId;
	    		// alert(this.provsSelected);
	            $.ajax({
	                type: "POST",
	                url: baseCorePath+'car/getCountysByCityId',
	                data: {"cityId":currCityId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                	vm.countys=data.data.countys;
	                },
	                error: function (message) {
	                    layer.msg("查询县区信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	    	},
	    	viewData: function(){
	
	            $.ajax({
	                type: "POST",
	                url: baseCorePath+'car/getReturnReg',
	                data: {"businessId":businessId,"dragId":dragId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                  	if (data.code == "0000"){
	                	vm.carBasic=data.data.carBasic;
	                	vm.business=data.data.business;
	                	vm.drag=data.data.drag;
	                	if(vm.drag.province=='北京市'||vm.drag.province=='天津市'||vm.drag.province=='重庆市'||vm.drag.province=='上海市'){
	                		vm.dragAddr=(vm.drag.city==null?'':vm.drag.city)+(vm.drag.county==null?'':vm.drag.county)+(vm.drag.detailAddress==null?'':vm.drag.detailAddress);
	                	}
	                	else{
	                		vm.dragAddr=(vm.drag.province==null?'':vm.drag.province)+(vm.drag.city==null?'':vm.drag.city)+(vm.drag.county==null?'':vm.drag.county)+(vm.drag.detailAddress==null?'':vm.drag.detailAddress);
	                		// vm.dragAddr=vm.drag.province+vm.drag.city+vm.drag.city+vm.drag.county+vm.drag.detailAddress;
	                	}
	                	vm.provs=data.data.provs;
	                	vm.citys=data.data.citys;
	                	vm.countys=data.data.countys;
	                	
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
	                  	}else{
	                		layer.msg("查看失败:"+data.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});
	                	}
	                },
	                error: function (message) {
	                    layer.msg("查询车辆信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	 
	    	},
	    	carReturnRegClose(){// 关闭窗口
	    		var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	    		parent.layer.close(index);
	    	},
	    	carReturnReg:function (event){
	    		vm.returnReg.businessId=businessId;
	    		vm.returnReg.dragId=dragId;
	    		// form.verify({});
	    		//alert(JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.returnRegFiles}));
	    		for(var i=0;i<vm.returnRegFiles.length;i++){
	    			vm.returnRegFiles[i].file='';
	    			vm.reqRegFiles[i]=vm.returnRegFiles[i];

	    		}
		          $.ajax({
		               type: "POST",
		               url: baseCorePath+'car/addReturnReg',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.reqRegFiles}),
		               success: function (res) {
		            	   if (res.code == "0000"){
		            		   layer.msg("保存成功。",{icon:1,shade: [0.8, '#393D49'],time:3000}); 
		            	   }else{
		            		   layer.msg(res.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});
		            		   
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






