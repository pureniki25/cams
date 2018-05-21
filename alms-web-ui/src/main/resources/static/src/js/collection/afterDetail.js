var layer;
	var table;
	var basePath;
	var vm;
	var ajax_data;

	var crpId=getQueryStr("crpId");
	var businessTypeId=getQueryStr("businessTypeId");
	var businessId=getQueryStr("businessId");
	
	window.layinit(function (htConfig) {
		var _htConfig = htConfig;
        basePath = _htConfig.coreBasePath;
        openPath=_htConfig.openBasePath;
        ajax_data={
        	
        	
        };
        selectLiquidationTwo();
        selectLiquidationOne();
        selectLitigationfunction();
        selectCarInfo();
        vm = new Vue({
        	el: '#app',
        	data:{
        		liquidationOneFlag:false,
        		liquidationTwoFlag:false,
        		litigationHouseFlag:false,
        		litigationCarFlag:false,
        		carDragFlag:false,
        		carReturnFlag:false,
        		carDrag:'',
        		carReturnReg:'',
                liquidationOnes:[{
                	liquidationOneId:'',
                	liquidationOneName:'', 
                	liquidationTwoId:'',
                	liquidationTwoName:'',
                	describe:'',
                	setWay:'',
                	setTime:'',
                	setWayStr:'',
                	rowNo:''
                }],
                liquidationTwos:[{
                	liquidationOneId:'',
                	liquidationOneName:'', 
                	liquidationTwoId:'',
                	liquidationTwoName:'',
                	describe:'',
                	setWay:'',
                	setTime:'',
                	setWayStr:'',
                	rowNo:''
                }],
                litigations:[{
                 rowNo:'',// 序号
                 operator:'',// 移交人
                 operatTime:'',// 移交时间
                 pecuniaryCondition:'',// 其他财务情况（分公司/区域贷后跟进结果）
                 relatedPersonnel:'',// 资产管理部经办人催收调查结果（分公司/区域贷后跟进结果）
                 carCondition:'',// 客户车辆目前情况
                 almsOpinion:''// 贷后意见
                }]
        	},
            methods: {  
            
              
            }
        });
        
     

	});

	var selectLiquidationOne=function(){debugger
		
        var self = this;   
        var reqStr =basePath+ "BasicBusinessController/selectLiquidationOne?crpId=" + crpId
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {debugger
                	
                    if(result.data.data.liquidationOnes.length==0){
                        vm.liquidationOneFlag=false;
                     }else{
                   	  vm.liquidationOneFlag=true;
                     }
                    vm.liquidationOnes=result.data.data.liquidationOnes; 
             
                   
                    
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
	}
	
	var selectLiquidationTwo=function(){debugger
		
        var self = this;   
        var reqStr =basePath+ "BasicBusinessController/selectLiquidationTwo?crpId=" + crpId
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {debugger
                	
                      if(result.data.data.liquidationTwos.length==0){
                         vm.liquidationTwoFlag=false;
                      }else{
                    	  vm.liquidationTwoFlag=true;
                      }
                    vm.liquidationTwos=result.data.data.liquidationTwos; 
             
                   
                    
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
	}
	// 获取法务移交信息
	var selectLitigationfunction=function(){debugger
		
        var self = this;   
        var reqStr =basePath+ "BasicBusinessController/selectLitigation?crpId=" + crpId+"&businessTypeId="+businessTypeId
      
        axios.get(reqStr)
            .then(function (result) {
                if (result.data.code == "1") {debugger
                	
                      if(result.data.data.litigations.length==0){
                    		 vm.litigationCarFlag=false;
                         	 vm.litigationHouseFlag=false;
                      }else{
                    	  if(businessTypeId==1||businessTypeId==9){
                         	 vm.litigationCarFlag=true;
                         	 vm.litigationHouseFlag=false;
                         }else{
                         	 vm.litigationCarFlag=false;
                         	 vm.litigationHouseFlag=true;
                         }
                      }
                    vm.litigations=result.data.data.litigations; 
             
                   
                    
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
	}
	

	
	
	// 获取拖车登记信息和车辆归还信息
	var selectCarInfo=function(){debugger
		  if(businessTypeId==1||businessTypeId==9){
		        var self = this;   
		        var reqStr =basePath+ "BasicBusinessController/selectCarInfo?businessId=" + businessId
		      
		        axios.get(reqStr)
		            .then(function (result) {
		                if (result.data.code == "1") {debugger
		                	
		                      if(result.data.data.carDrag==null&&result.data.data.carReturnReg!=null){
		                    		 vm.carDragFlag=false;
		                         	 vm.carReturnFlag=true;
		                         	 vm.carReturnReg=result.data.data.carReturnReg;
		                      }
			                if(result.data.data.carDrag!=null&&result.data.data.carReturnReg==null){
						               	 vm.carDragFlag=true;
				                     	 vm.carReturnFlag=false;
				                     	vm.carDrag=result.data.data.carDrag;
			                }
		                   
		                    
		                } else {
		                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
		                }
		            })
		   
		        .catch(function (error) {
		            vm.$Modal.error({content: '接口调用异常!'});
		        });
        
		  }
	}
	
	
	

	
