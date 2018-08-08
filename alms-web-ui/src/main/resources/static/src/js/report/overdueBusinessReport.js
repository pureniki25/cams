var basePath;
var vm;

window.layinit(function (htConfig) {
	basePath = layui.ht_config.gatewayUrl;
	
	vm = new Vue({
	   el: '#app',
	   data: {
		   overdueBusinessReportData:'', // 逾期业务汇总表
	   },
	   methods: {
		   /**
		    * 逾期业务汇总表
		    */
		   overdueBusinessReport: function(){
			   axios.get(basePath +"/rp-app-service/bu/report?gateway=1&code=281086439898972160")
				.then(function (res) {
					console.log('RP平台返回数据：', res);
					if (res != null) {
						$(document).ready(function(){
					    	$("#overdueBusinessReportDiv").html(res.data);
						});
					}
				})
				.catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
				});
		   }
	   },
	   mounted: function() {
		   this.overdueBusinessReport();
	   },
	});
    
});
