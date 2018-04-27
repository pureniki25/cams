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
        ajax_data={
        	
        	
        };
        getDeductionInfo();
        getDeductionPlatformInfo();
        vm = new Vue({
        	el: '#app',
        	data:{
        		
        		
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
                    nowdate:"",               
                    operatorName:"",          
                    repaymentTypeName:"",
                    currentStatus:""
                   

                },
                platformList:[],
                platformId:1
                
         
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

	
	var withHoldingRecord=function(){debugger
		
		
        var self = this;
        var reqStr =openPath+ "WithHoldingController/withholding?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId+"&total="+vm.ajax_data.total+"&planOverDueMoney="+vm.ajax_data.planOverDueMoney+"&platformId="+vm.platformId+"&type=1"+"&nowdate="+vm.ajax_data.nowdate
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {
                	
          
                	 vm.$Modal.success({
                         title: '',
                         content:result.data.data
                     });
                } else {
                    self.$Modal.error({content:result.data.msg});
                }
            })
   
        .catch(function (error) {
        
        		   vm.$Modal.error({content: '接口调用异常!'});
        
        
        });
		
   
	}
	
	var withHoldingRecordWithoutOverMoeny=function(){ 
		
		
        var self = this;
        var reqStr =openPath+ "WithHoldingController/withholding?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId+"&total="+vm.ajax_data.total+"&planOverDueMoney="+vm.ajax_data.planOverDueMoney+"&platformId="+vm.platformId+"&type=0"+"&nowdate="+vm.ajax_data.nowdate
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {
                	
          
                	 vm.$Modal.success({
                         title: '',
                         content: '代扣正在处理中,请稍后查看代扣结果'
                     });
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}

	
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
	var getDeductionInfo=function(){
		
		
        var self = this;   
        var reqStr =basePath+ "DeductionController/selectDeductionInfoByPlayListId?planListId=" + planListId
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {debugger
                	
                      if(result.data.data==null){
                          vm.$Modal.error({content: '没有找到数据'});
                          return;
                      }
                    vm.ajax_data=result.data.data;
                    vm.platformId=result.data.data.platformId;
//                    searchRepayLog();
                    
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
                            url: openPath+ "WithHoldingController/searchRepayLog?originalBusinessId=" +vm.ajax_data.originalBusinessId+"&afterId="+vm.ajax_data.afterId,
                            //method: 'post' //如果无需自定义HTTP类型，可不加该参数
                            //request: {} //如果无需自定义请求参数，可不加该参数
                            //response: {} //如果无需自定义数据响应名称，可不加该参数
                            page: false,
                            done: function (res, curr, count) {
                                //数据渲染完的回调。你可以借此做一些其它的操作
                                //如果是异步请求数据方式，res即为你接口返回的信息。
                                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                                vm.loading = false;
                            }
                        });

                    


                    });
                    
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
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
	
	
