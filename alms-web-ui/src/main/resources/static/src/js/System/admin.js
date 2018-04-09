var basePath;
var vm;

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    
	vm = new Vue({
	   el: '#app',
	   data: {
		   // --- 按钮控制标识 start---
		   syncCollectionLoading:false,				
		   setUserPermissonsLoading:false,		
		   // --- 按钮控制标识 end---
	   },
	   methods: {
		   // 同步催收数据
		   syncCollection:function(){
			   this.syncCollectionLoading = true;
			   axios.get(basePath +"alms/transfer")
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
		   // 设置所有用户可访问业务对照关系
		   setUserPermissons:function(){
			   this.setUserPermissonsLoading = true;
			   axios.get(basePath +"sys/admin/userPermission")
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
		   // 设置指定用户可访问业务对照关系
		   setSingleUserPermissons:function(){
			   layer.open({
                   type: 2,
                   title: '设置指定用户可访问业务对照关系',
                   area: ['1250px', '800px'],
                   content: '/system/admin/setSingleUserPermissons'
               });
		   },
	   }
	});
    
});
