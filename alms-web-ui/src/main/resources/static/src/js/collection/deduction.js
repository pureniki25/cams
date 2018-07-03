var layer;
	var table;
	var basePath;
	var vm;
	var ajax_data;
	var planListId = document.getElementById("planListId").getAttribute("value");

	

	
	window.layinit(function (htConfig) {
		var _htConfig = htConfig;
        basePath = _htConfig.coreBasePath;
        openPath=_htConfig.openBasePath;
        withholdPath=_htConfig.withholdBasePath;
        ajax_data={
        	
        	
        };
        getDeductionInfo();
        getDeductionPlatformInfo();
      
        vm = new Vue({
        	el: '#app',
        	data:{
        		loading: false,
        		isBankFlag:false,
        		ajax_data:{
                    businessId:""	, //业务编号
                    originalBusinessId:""  ,  
                    borrowLimit:""    ,       
                    borrowLimitUnit:"" ,      
                    customerName:"",          
                    identifyCard:"" ,         
                    planPrincipal:"" ,        
                    platformCharge:"" ,       
                    afterId:"",               
                    bankCard:"",              
                    bankName:"" ,             
                    planAccrual:"",           
                    planGuaranteeCharg:"",   
                    otherFee:"",              
                    total:"",                 
                    borrowMoney:"",           
                    phoneNumber:"",           
                    platformName:"",          
                    planServiceCharge:"",     
                    planOverDueMoney:"", 
                    onLineOverDueMoney:"",//线上逾期费
                    underLineOverDueMoney:"",//线下逾期费
                    underLineFactOverDueMoney:"",//线下逾期费实际扣除
                    factPayAllMoney:"",//本次代扣全部金额
                    nowdate:"",               
                    operatorName:"",          
                    repaymentTypeName:"",
                    currentStatus:"",
                    planAllAmount:"",//应还总额
                    repayAllAmount:"",//已还总额
                    restAmount:"",
                    repayAmount:"",//本次代扣金额
                    repayingAmount:'',//代扣中金额
                    issueSplitType:'',
                    isCanUseThirty:'',
                    business:[],
                    bankCardInfo:[],
                    pList:[]

                    

                },
                platformList:[],
                platformId:5
                
         
        	},
            methods: {  
            	bankLimit(){
                	getBankLimit();
                	
                },
                withHolding(){
                	
                	if(ajax_data.currentStatus!='已还款'){
                		withHoldingRecord();
                	}
                	
                },
                withHoldingWithout(){
                	if(ajax_data.currentStatus!='已还款'){
                	withHoldingRecordWithoutOverMoeny();
                	}
                	
                }
            }
        });
        
     

	});

	//从后台获取下拉框数据
	var getBankLimit = function () {

		
	    layer.open({
	        type: 2,
	        title: "各支付平台代扣限额列表",
	        maxmin: true,
	        area: ['1150px', '700px'],
	        content:'/collectionUI/bankLimitUI'
	    });

	}
	function withHoldingRecord(){
//		if(vm.ajax_data.issueSplitType==1&&vm.platformId==5){debugger
//			vm.$Modal.error({content: '共借标不能银行代扣'});
//		   return;
//		}
	    vm.loading = true;
		var isAmountWithheld="false";
		if(vm.ajax_data.total<vm.ajax_data.restAmount){
			isAmountWithheld="true";//部分代扣
		}else if(vm.ajax_data.total=vm.ajax_data.restAmount){
			isAmountWithheld="false";//全部代扣
		}
	   if(vm.ajax_data.total>vm.ajax_data.restAmount){debugger
		   vm.$Modal.error({content:"代扣金额不能大于本次最大可代扣金额"});
	   vm.loading = false;
	   return;
	   }
		   
	   if(vm.ajax_data.underLineFactOverDueMoney>vm.ajax_data.planOverDueMoney){
		   vm.$Modal.error({content:"本次代扣线下逾期费不能大于线下逾期费"});
		   vm.loading = false;
		   return;
			   }
	    if(vm.ajax_data.canUseThirty==false&&vm.platformId!=5){debugger//如果是第三方代扣，但是第三方代扣标识为false，
	    	 vm.$Modal.error({content:"银行部分代扣情况下，没有还清线上费用不能用第三方平台代扣线下费用"});
	        vm.loading = false;
	    	return;
	    }
		   vm.ajax_data.platformId=vm.platformId;
	       if(vm.ajax_data.platformId=='5'&&vm.ajax_data.underLineFactOverDueMoney>0&&vm.ajax_data.repayAmount==0){
	    		vm.$Modal.error({content:"线下逾期费大于0不能银行代扣"});
	    		  vm.loading = false;
	    		return;
	       }
	       
	       if(vm.ajax_data.repayAmount==0){
	    		vm.$Modal.error({content:"本期应该金额为0不能代扣"});
	    		  vm.loading = false;
	    		return;
	       }
	   //贷后生成的走贷后的代扣接口,否则走信贷的代扣接口
	   if(vm.ajax_data.strType==2){debugger
		   vm.ajax_data.platformId=vm.platformId;
		    var url=withholdPath+ "repay/handRepay"
		    $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.ajax_data),
                contentType: "application/json; charset=utf-8",
                success: function (data) {debugger
                	   vm.loading = false;
                    if(data.code=='1'){
                    	vm.$Modal.success({content:"执行成功，请稍后查询结果"});
		             }else{
		                 vm.$Modal.error({content:data.msg});
		             }
                },
                error: function (message) {
                	   vm.loading = false;
                	 layer.confirm('Navbar error:AJAX请求出错!', function(index) {
 		                top.location.href = loginUrl;
 		                layer.close(index);
 		                
 		            });
 		            return false;
                }
            });
	   }else{
		    var url=openPath+ "WithHoldingController/withholding?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId+"&total="+vm.ajax_data.total+"&planOverDueMoney="+vm.ajax_data.underLineFactOverDueMoney+"&platformId="+vm.platformId+"&type=0"+"&nowdate="+vm.ajax_data.nowdate+"&isAmountWithheld="+isAmountWithheld
		    $.ajax({
		        type : 'GET',
		        async : false,
		        url : url,
		        headers : {
		            app : 'ALMS',
		            Authorization : "Bearer " + getToken()
		        },
		        success : function(data) {debugger
		       	   vm.loading = false;
		             if(data.code=='1'){
		            	 vm.$Modal.success({content:data.data});
		             }else{
		                 vm.$Modal.error({content:data.data});
		             }
		        },
		        error : function() {
		       	   vm.loading = false;
		            layer.confirm('Navbar error:AJAX请求出错!', function(index) {
		                top.location.href = loginUrl;
		                layer.close(index);
		            });
		            return false;
		        }
		    });
	   }
	  
	

	}
	

	/*
	var withHoldingRecordWithoutOverMoeny=function(){ debugger
	
		if(vm.ajax_data.issueSplitType==1&&vm.platformId==5){
			vm.$Modal.error({content: '共借标不能银行代扣'});
		}
		
		var isAmountWithheld="false";//全部代扣
	if(vm.ajax_data.factPayAllMoney<vm.ajax_data.total){
		isAmountWithheld="true";//部分代扣
	}
        var self = this;
        var reqStr =openPath+ "WithHoldingController/withholding?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId+"&total="+vm.ajax_data.total+"&planOverDueMoney="+vm.ajax_data.underLineFactOverDueMoney+"&platformId="+vm.platformId+"&type=0"+"&nowdate="+vm.ajax_data.nowdate+"&isAmountWithheld="+isAmountWithheld
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {
                	
          
                	 vm.$Modal.success({
                         title: '',
                         content: '代扣正在处理中,请稍后查看代扣结果'
                     });
                } else {
                	vm.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}
*/
	
	var searchRepayLog=function(){
		
		
        var self = this;
        var reqStr =openPath+ "WithHoldingController/searchRepayLog?originalBusinessId=" +vm.ajax_data.originalBusinessId;
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {
                	
                	result.data.data;
                
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}
	var getDeductionInfo=function(){debugger
		
		  var url="";
        var self = this;   
        var reqStr =basePath+ "DeductionController/selectDeductionInfoByPlayListId?planListId=" + planListId
        
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {debugger
                	
                      if(result.data.data==null){
                          vm.$Modal.error({content: '没有找到数据'});
                          return;
                      }
                    vm.loading = false;
                    vm.ajax_data=result.data.data; 
                    vm.details=result.data.data.details;
                    vm.platformId=result.data.data.platformId;
                    vm.platformId=5;
                    vm.isBankFlag=true;
                    
                 
                    if(result.data.data.underLineOverDueMoney>0){
                     	vm.ajax_data.planOverDueMoney=result.data.data.underLineOverDueMoney;
                     	vm.ajax_data.onLineOverDueMoney=result.data.data.onLineOverDueMoney;
               
                    }
                 	vm.platformId='5';
                 	doOperate();
                      if(vm.ajax_data.strType==2){
                      	url=basePath+ "RepaymentLogController/searchAfterRepayLog?businessId="+vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId;
                      }else{
                      	url:openPath+ "WithHoldingController/searchRepayLog?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId;
                      }
                    //使用layerUI的表格组件
                    layui.use(['layer', 'table','ht_ajax', 'ht_auth', 'ht_config'], function () {
                        layer = layui.layer;
                        table = layui.table;
                        // var config = layui.ht_config;
                        // basePath = config.basePath;
                        //执行渲染
                        table.render({
                            elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                            , id: 'listTable'
                            , height: 550 //容器高度
                            , cols: [[


                                {
                                    field: 'listId',
                                    title: '序号'
                                }, {
                                    field: 'updateTime',
                                    title: '代扣时间'
                                },{
                                    field: 'currentAmount',
                                    title: '代扣金额',
                                    align:'right',
                                    templet:function(d){
                                        return d.currentAmount?numeral(d.currentAmount).format('0,0.00'):0
                                    }
                                }, {
                                    field: 'customerName',
                                    title: '代扣人'
                                }, {
                                    field: 'bankCard',
                                    title: '代扣银行卡'
                                },{
                                    field: 'bindPlatform',
                                    title: '代扣公司'
                                },
                                {
                                    field: 'repayStatus',
                                    title: '代扣结果'
                                },
                                {
                                    field: 'remark',
                                    title: '备注'
                                }
                                
                          
                            ]], //设置表头
                          
                            url: url,
                            //method: 'post' //如果无需自定义HTTP类型，可不加该参数
                            //request: {} //如果无需自定义请求参数，可不加该参数
                            //response: {} //如果无需自定义数据响应名称，可不加该参数
                            page: false,
                            done: function (res, curr, count) {debugger
                            	  var list=res.data;
                                  var repayMoney=0;
                   	            	if(list != null && list.length > 0){
		                   	           	for (var i = 0; i < list.length; i++){
		                   	           		if(list[i].repayStatus!="失败"){
		                   	           	 	repayMoney=repayMoney+Number(list[i].currentAmount);
		                   	           		}
		                   	           	}
		                   	       	}
                   	            	//vm.ajax_data.repayAllAmount=repayMoney;
                   	            	//getTotalShouldPay();
                                vm.loading = false;
                            }
                        });

                    


                    });
                    
                } else {debugger
                    vm.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
        	vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}
	
	
	
	var getTotalShouldPay = function () {debugger
	var total=0;
		vm.ajax_data.total=0;
	
	vm.ajax_data.total=vm.ajax_data.planPrincipal+vm.ajax_data.planAccrual+vm.ajax_data.planServiceCharge+vm.ajax_data.platformCharge+Number(vm.ajax_data.onLineOverDueMoney)+Number(vm.ajax_data.underLineOverDueMoney)-vm.ajax_data.repayAllAmount;
	var total=vm.ajax_data.total;
	if(total<0){
		total=0;
	}
	vm.ajax_data.total=total.toFixed(2);
	vm.ajax_data.repayAmount=total.toFixed(2);

	return vm.ajax_data.total;
	}

	
	
	
	var getDeductionPlatformInfo=function(){
		
		
        var self = this;  
        var reqStr =basePath+ "DeductionController/getDeductionPlatformInfo"
        axios.get(reqStr)
            .then(function (result) {debugger
                if (result.data.code == "1") {
                	
          
                    vm.platformList = result.data.data.platformList;
               
           
                    
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}
	var doOperate = function () {debugger
        if(vm.platformId=='5'){debugger
        	vm.isBankFlag=true;
        	vm.ajax_data.total=getTotalShouldPay();
        	vm.ajax_data.repayAmount=vm.ajax_data.repayAmount-vm.ajax_data.planOverDueMoney;
        	if(	vm.ajax_data.repayAmount<0){
        		vm.ajax_data.repayAmount=0;
        	}
        	vm.ajax_data.repayAmount=vm.ajax_data.repayAmount.toFixed(2);
       
        }else{
        	vm.isBankFlag=false;
        	vm.ajax_data.total=getTotalShouldPay();
        }
	}
	
