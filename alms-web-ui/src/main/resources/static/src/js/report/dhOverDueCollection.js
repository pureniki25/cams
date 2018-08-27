var basePath;
var vm;

window.layinit(function (htConfig) {
	basePath = layui.ht_config.gatewayUrl;
	
	vm = new Vue({
	   el: '#app',
	   data: {
	   },
	   methods: {
		   /**
		    * 应收实收台账表
		    */
		   dhOverDueCollection: function(){
			   axios.get(basePath +"/rp-app-service/bu/report?gateway=1&code=236219151598284800")
				.then(function (res) {
					console.log('RP平台返回数据：', res);
					if (res != null) {
						$(document).ready(function(){
					    	$("#dhOverDueCollection").html(res.data);
						});
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
		   }
	   },
	   mounted: function() {
		   this.dhOverDueCollection();
	   },
	});
    
});
