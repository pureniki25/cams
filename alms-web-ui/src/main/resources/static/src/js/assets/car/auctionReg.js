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
	    		 regId:''//id
				,businessId:''// 业务编号
	    		,auctionId:''// 拍卖id
	    		,offerAmount:''// 出价金额
	    		,auctionSuccess:''// 是否竞拍成功
	    		,transPrice:''//成交价格
				,username:''//用户名
				,regCertId:''//身份证号
				,regTel:''//联系方式
	    	}
	    },
     computed:
		 {
		 	formatTransPrice:function()
			{
				return (this.auctionReg.transPrice==''|| this.auctionReg.transPrice==null)?'':numeral(auctionReg.transPrice).format();
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
	                	vm.auctionReg=data.data;
	                },
	                error: function (message) {
	                    layer.msg("查询拍卖信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	 
	    	},
	    	auctionRegClose:function(){// 关闭窗口
	    		var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	    		parent.layer.close(index);
	    	},
	    	carAuctionReg:function (){
	    		var self=this;
		          $.ajax({
		               type: "POST",
		               url: basePath+'car/updateAuctionReg',
		               data: {
		               	    regId:self.auctionReg.regId
                           ,businessId:self.auctionReg.businessId// 业务编号
                           ,auctionId:self.auctionReg.auctionId// 拍卖id
                           ,offerAmount:self.auctionReg.offerAmount// 出价金额
                           ,auctionSuccess:self.auctionReg.auctionSuccess// 是否竞拍成功
                           ,transPrice:self.auctionReg.transPrice//成交价格
                           ,username:self.auctionReg.username//用户名
                           ,regCertId:self.auctionReg.regCertId//身份证号
                           ,regTel:self.auctionReg.regTel//联系方式
                       },
		               success: function (res) {
		            	   if (res.code == "0"){
		            		   layer.msg("保存成功。",{icon:1,shade: [0.8, '#393D49'],time:3000}); 
		            	   }
		            	   else
						   {
                               layer.msg("保存失败，"+res.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});
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