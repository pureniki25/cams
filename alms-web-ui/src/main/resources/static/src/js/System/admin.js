var basePath;
var financeBasePath;
var platRepayBasePath;
var vm;

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    financeBasePath=htConfig.financeBasePath;
    platRepayBasePath=htConfig.platRepayBasePath;
    
	vm = new Vue({
	   el: '#app',
	   data: {
		   // --- 按钮控制标识 start---
		   syncCollectionLoading:false,
           synOneListColLoading:false,
           setUserPermissonsLoading:false,
           fiveLevelClassifyLoading:false,
           calLateFeeLoading:false,
           updateAgencyRechargeHandleStatusLoading:false, // 更新代充值处理状态
           
		   // --- 按钮控制标识 end---

		   onePListCollogBId:"",
           onePListCollogAfterId:"",


	   },
	   methods: {
		   // 同步催收数据
		   syncCollection:function(){
			   this.syncCollectionLoading = true;
			   axios.get(basePath +"alms/transfer",{timeout: 0})
		        .then(function (res) {
		        	vm.syncCollectionLoading = false;
		            if (res.data.data != null && res.data.code == 1) {
		            	vm.$Modal.success({
		                    content: res.data.msg
		                });
		            } else {
		                vm.$Modal.error({content: res.data.msg });
		            }
		        })
		        .catch(function (error) {
		        	vm.syncCollectionLoading = false;
		            vm.$Modal.error({content: '接口调用异常!'});
		        });
		   },
		   // 同步指定还款计划催收数据
		   syncOneCollection:function(){
			   this.synOneListColLoading = true;
			   axios.get(basePath +"alms/setCollectionStatus?businessId="+vm.onePListCollogBId+"&afterId="+vm.onePListCollogAfterId,{timeout: 0})
		        .then(function (res) {
		        	vm.synOneListColLoading = false;
		            if (res.data.data != null && res.data.code == 1) {
		            	vm.$Modal.success({
		                    content: res.data.msg
		                });
		            } else {
		                vm.$Modal.error({content: res.data.msg });
		            }
		        })
		        .catch(function (error) {
		        	vm.synOneListColLoading = false;
		            vm.$Modal.error({content: '接口调用异常!'});
		        });
		   },
		   // 设置所有用户可访问业务对照关系
		   setUserPermissons:function(){
			   this.setUserPermissonsLoading = true;
			   axios.get(basePath +"sys/admin/userPermission",{timeout: 0})
			   .then(function (res) {
				   vm.setUserPermissonsLoading = false;
				   if (res.data.data != null && res.data.code == 1) {
					   vm.$Modal.success({
						   content: res.data.msg
					   });
				   } else {
					   vm.$Modal.error({content: res.data.msg });
				   }
			   })
			   .catch(function (error) {
				   vm.setUserPermissonsLoading = false;
				   vm.$Modal.error({content: '接口调用异常!'});
			   });
		   },
		   // 五级分类设置
		   fiveLevelClassify:function(){
			   this.fiveLevelClassifyLoading = true;
			   axios.get(basePath +"businessParameter/fiveLevelClassifySchedule",{timeout: 0})
			   .then(function (res) {
				   vm.fiveLevelClassifyLoading = false;
				   if (res.data.code == 1) {
					   vm.$Modal.success({
						   content: res.data.msg
					   });
				   } else {
					   vm.$Modal.error({content: res.data.msg });
				   }
			   })
			   .catch(function (error) {
				   vm.fiveLevelClassifyLoading = false;
				   vm.$Modal.error({content: '接口调用异常!'});
			   });
		   },
		   // 设置指定用户可访问业务对照关系
		   setSingleUserPermissons:function(){
			   layer.open({
                   type: 2,
                   title: '设置指定用户可访问业务对照关系',
                   area: ['1250px', '800px'],
                   content: '/system/admin/setSingleUserPermissons'
               });
		   },
           // 同步贷后跟踪记录
           syncTrackLog:function(){
               this.syncCollectionLoading = true;
               axios.get(basePath +"collectionTrackLog/transfer",{timeout: 0})
                   .then(function (res) {
                       vm.syncCollectionLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.syncCollectionLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
           // 
           calLateFee:function(){
               this.calLateFeeLoading = true;
               axios.get(financeBasePath +"calLateFeeController/calLateFee",{timeout: 0})
                   .then(function (res) {
                       vm.calLateFeeLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.syncCollectionLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
/*           // 更新代充值处理状态
           updateAgencyRecharge:function(){
        	   this.updateAgencyRechargeHandleStatusLoading = true;
        	   axios.get(platRepayBasePath +"recharge/queryRechargeOrder",{timeout: 0})
        	   .then(function (res) {
        		   vm.updateAgencyRechargeHandleStatusLoading = false;
        		   if (res.data.data != null && res.data.code == 1) {
        			   vm.$Modal.success({
        				   content: res.data.msg
        			   });
        		   } else {
        			   vm.$Modal.error({content: res.data.msg });
        		   }
        	   })
        	   .catch(function (error) {
        		   vm.updateAgencyRechargeHandleStatusLoading = false;
        		   vm.$Modal.error({content: '接口调用异常!'});
        	   });
           },
*/	   }
	});
    
});
