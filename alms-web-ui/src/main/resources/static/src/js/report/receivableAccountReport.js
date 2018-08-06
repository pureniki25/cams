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
		   receivableAccountReport: function(){
			   axios.get(basePath +"/rp-app-service/bu/report?gateway=1&code=281086264518344704")
				.then(function (res) {
					console.log('RP平台返回数据：', res);
					if (res != null) {
						$(document).ready(function(){
					    	$("#receivableAccountReportDiv").html(res.data);
						});
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
		   }
	   },
	   mounted: function() {
		   this.receivableAccountReport();
	   },
	});
    
});
