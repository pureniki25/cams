var businessId = document.getElementById("businessId").getAttribute("value");
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
	    	   note:''
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
	    		// \alert(event.currentTarget);
	    		 $('#'+fileId).fileupload({
	    			    dataType: 'json',
	                    singleFileUploads: true,
	                    acceptFileTypes: '', // Allowed file types
	    		        url: baseCorePath+'doc/singleUpload',// 文件的后台接受地址
	    		        // 设置进度条
	    		        // 上传完成之后的操作，显示在img里面
	    		        done: function (e, data){
	    		        	var reFile=data._response.result.docItemListJson;
	    		        	var reFiles=eval("("+reFile+")"); 
	    		        	vm.returnRegFiles[index].oldDocId=reFiles.docId;
	    		        	vm.returnRegFiles[index].originalName=reFiles.originalName;
	    		        	vm.returnRegFiles[index].name=reFiles.originalName;
	
	    		        }
	    		    });
	             $('#'+fileId).bind('fileuploadsubmit', function (e, data) {
	                 if (data && data.originalFiles && data.originalFiles.length > 0) {
	                     data.formData = {fileName: data.originalFiles[0].name};
	                     data.formData.businessId = businessId;
	                     // alert(data.originalFiles);
	                     data.formData.busType='AfterLoan_Material_ReturnReg';
	                     data.formData.file=data.originalFiles[0];
	                 }
	             });
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
	                    layer.msg("查询城市信息发生异常，请联系管理员。");
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
	                    layer.msg("查询县区信息发生异常，请联系管理员。");
	                    console.error(message);
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
		                url: baseCorePath+'doc/delOneDoc?docId='+docId,
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
	
	    	viewData: function(){
	
	            $.ajax({
	                type: "POST",
	                url: baseCorePath+'car/getReturnReg',
	                data: {"businessId":businessId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                	// alert(JSON.stringify(data));
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
		   	               			 oldDocId:docFiles[i].docId
		   	               		 });
		                		}else{
			                		vm.returnRegFiles[i].oldDocId=docFiles[i].docId;
			                		vm.returnRegFiles[i].originalName=docFiles[i].originalName;
			                		vm.returnRegFiles[i].name=docFiles[i].originalName;
		                		}
		                		// i++;
		                	}
	                	}
	                },
	                error: function (message) {
	                    layer.msg("查询车辆信息发生异常，请联系管理员。");
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
		            		   layer.msg("保存成功。"); 
		            	   }
		               },
		               error: function (message) {
		                   layer.msg("异常，请联系管理员。");
		                   console.error(message);
		               }
		           });
	    	}
	    }
	
	});
	vm.viewData();
});
});






