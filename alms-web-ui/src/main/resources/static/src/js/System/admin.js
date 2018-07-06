var basePath;
var platRepayBasePath;
var vm;

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    financeBasePath=htConfig.financeBasePath;
    platRepayBasePath=htConfig.platRepayBasePath;
    withholdBasePath=htConfig.withholdBasePath;

	vm = new Vue({
	   el: '#app',
	   data: {
		   // --- 按钮控制标识 start---
		   syncCollectionLoading:false,  //同步电催分配数据的加载标志位
           synOneCollectionLoading:false, //同步指定电催分配数据的加载标志位
           synOneUserCollectionLoading:false, //同步指定用户电催分配数据的加载标志位
           syncVisitCollectionLoading:false,  //同步上门催收分配数据的加载标志位
           setSuserPByUidloanding:false,  //根据用户Id设置单个用户可访问的业务加载标志位
           synOneListColLoading:false,
           setUserPermissonsLoading:false,
           fiveLevelClassifyLoading:false,
           syncTrackLogLoading:false,
           calLateFeeLoading:false,
           updateAgencyRechargeHandleStatusLoading:false, // 更新代充值处理状态
           setCollectionLoading:false, //分配贷后跟进人员  标志位
           autoRepayLoading:false,
           tdrepyChargeLoading:false,	// 合规化还款加载标识
		   // --- 按钮控制标识 end---

		   onePListCollogBId:"",
           onePListCollogAfterId:"",

           //同步指定电催信息的业务Id
           oneCollectionBId:"",
           //同步指定电催信息的afterId
           oneCollectionAfterId:"",

           //同步指定用户的电催信息用户信贷Id
           oneColUserXDId:"",

           //设置指定用户的权限对照关系用户Id（UC的用户ID）
           userId:""

	   },
	   methods: {
		   // 同步电催催收数据
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
           // 同步指定电催催收数据
           transferOneCollection:function(){
               this.synOneCollectionLoading = true;
               var url = basePath +"alms/transferOneCollection?businessId="+vm.oneCollectionBId
               if(vm.oneCollectionAfterId!=null &&vm.oneCollectionAfterId!=''){
                   url = url+"&afterId="+vm.oneCollectionAfterId;
               }
               axios.get(url,{timeout: 0})
                   .then(function (res) {
                       vm.synOneCollectionLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.synOneCollectionLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
           // 同步指定用户电催催收数据
           transferOneUserCollection:function(){
               this.synOneUserCollectionLoading = true;
               var url = basePath +"alms/transferOneUserCollection?userId="+vm.oneColUserXDId
               axios.get(url,{timeout: 0})
                   .then(function (res) {
                       vm.synOneUserCollectionLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.synOneUserCollectionLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
           // 同步上门催收数据
           syncVisitCollection:function(){
               this.syncVisitCollectionLoading = true;
               axios.get(basePath +"alms/transferVisit",{timeout: 0})
                   .then(function (res) {
                       vm.syncVisitCollectionLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.syncVisitCollectionLoading = false;
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
           setSingleUserPermissonsByUserId:function(){
               this.setSuserPByUidloanding = true;
               axios.get(basePath +"sys/admin/singleUserPermission?userId=" + vm.userId,{timeout: 0})
                   .then(function (res) {
                       vm.setSuserPByUidloanding = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.syncTrackLogLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });

           },
           // 同步贷后跟踪记录
           syncTrackLog:function(){
               this.syncTrackLogLoading = true;
               axios.get(basePath +"collectionTrackLog/transfer",{timeout: 0})
                   .then(function (res) {
                       vm.syncTrackLogLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.syncTrackLogLoading = false;
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
           //分配电催、催收
           setCollection:function(){
               this.setCollectionLoading = true;
               axios.get(basePath +"collection/setAllBizCollection",{timeout: 0})
                   .then(function (res) {
                       vm.setCollectionLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.setCollectionLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
           //自动代扣
           autoRepay:function(){
               this.autoRepayLoading = true;
               axios.get(withholdBasePath +"repay/autoRepay",{timeout: 0})
                   .then(function (res) {
                       vm.autoRepayLoading = false;
                       if (res.data.data != null && res.data.code == 1) {
                           vm.$Modal.success({
                               content: res.data.msg
                           });
                       } else {
                           vm.$Modal.error({content: res.data.msg });
                       }
                   })
                   .catch(function (error) {
                       vm.autoRepayLoading = false;
                       vm.$Modal.error({content: '接口调用异常!'});
                   });
           },
           // 合规化还款
           tdrepyCharge:function(){
        	   this.tdrepyChargeLoading = true;
        	   axios.post(platRepayBasePath +"tdrepayRecharge/repayComplianceWithRequirements")
        	   .then(function (res) {
        		   vm.tdrepyChargeLoading = false;
    			   vm.$Modal.success({
    				   content: res.data.msg
    			   });
        	   })
        	   .catch(function (error) {
        		   vm.tdrepyChargeLoading = false;
        		   vm.$Modal.error({content: '接口调用异常!'});
        	   });
           }




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
