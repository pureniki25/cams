var basePath;
var vm;

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    
	vm = new Vue({
	   el: '#app',
	   methods: {
		   syncCollection:function(){
			   axios.get(basePath +"alms/transfer")
		        .then(function (res) {
		            if (res.data.data != null && res.data.code == 1) {
		            	vm.$Modal.success({
		                    content: res.data.msg
		                });
		            } else {
		                vm.$Modal.error({content: res.data.msg });
		            }
		        })
		        .catch(function (error) {
		            vm.$Modal.error({content: '接口调用异常!'});
		        });
		   },
	   }
	});
    
});
