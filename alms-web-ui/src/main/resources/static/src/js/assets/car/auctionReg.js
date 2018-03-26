var regId = document.getElementById("regId").getAttribute("value");
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    layui.use(['form','laydate','element', 'ht_config', 'ht_auth'], function () {
		
	var element = layui.element;
	var form = layui.form;
	var config = layui.ht_config;
	var laydate = layui.laydate;
	var vm = new Vue({
	    el: '#app',
	    data: {

	    	auctionReg:{
	    		 businessId:''// 业务编号
	    		,auctionId:''// 拍卖id
	    		,payDeposit:''// 是否缴纳保证金
	    		,offerAmount:''// 出价金额
	    		,auctionSuccess:''// 是否竞拍成功
	    		,transPrice:''//成交价格
	    	},
	    	bidder:{
	    		bidderName:''//用户名
	    		,bidderCertId:''//身份证号
	    		,bidderTel:''//联系方式
	    	}
	    },
	    methods: {
	    viewData: function(){
	            $.ajax({
	                type: "POST",
	                url: basePath+'car/getAuctionReg',
	                data: {"regId":regId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (data) {
	                	vm.auctionReg=data.data.auctionReg;
	                	vm.bidder=data.data.bidder;
	                	//alert(JSON.stringify(vm.auctionReg));
	                },
	                error: function (message) {
	                    layer.msg("查询拍卖信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	 
	    	},
	    	auctionRegClose(){// 关闭窗口
	    		var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	    		parent.layer.close(index);
	    	},
	    	carAuctionReg:function (){
		          $.ajax({
		               type: "POST",
		               url: basePath+'car/updateAuctionReg',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"auctionReg":vm.auctionReg}),
		               success: function (res) {
		            	   if (res.code == "0000"){
		            		   layer.msg("保存成功。",{icon:1,shade: [0.8, '#393D49'],time:3000}); 
		            	   }
		               },
		               error: function (message) {
		                   layer.msg("异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
		                   console.error(message);
		               }
		           });
	    	}
	    }
	    });
	
	vm.viewData();
});
});