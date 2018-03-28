var businessId = document.getElementById("businessId").getAttribute("value");
var ex = /^[1-9]\d*$/; 
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

	    	carAuction:{
	    		businessId:''// 业务编号
	    		,auctionStartTime:''// 拍卖开始时间
	    		,auctionEndTime:''// 延长后拍卖结束时间
	    		,buyStartTime:''// 竞买开始时间
	    		,buyEndTime:''// 延长后竞买结束时间
	    		,auctionId:''//拍卖id
	    		,delayPeriod:''//延时周期
	    		,viewAuctionEndTime:''//当前拍卖结束时间
	    		,viewBuyEndTime:''//当前竞买结束时间
	    	}
	    },  
	    watch:{ 
	    	'carAuction.delayPeriod':function(val,oldval){
	    		if(val==null||val==''){
	    			$("#delayPeriod").css("border","1px solid #FF3030");
	    			return;
	    		}
	     		if (!ex.test(val)) {  
	    			$("#delayPeriod").css("border","1px solid #FF3030");
	    			layer.msg("请输入正整数！",{icon:5,shade: [0.8, '#393D49']});
	    			return;
	    		}
	     		var auctionEndTs=Date.parse(this.carAuction.viewAuctionEndTime);//取拍卖结束时间戳
	     		var buyEndTs=Date.parse(this.carAuction.viewBuyEndTime);//取拍卖结束时间戳
	     		if(buyEndTs+val*60*1000<=auctionEndTs){
	     			this.carAuction.buyEndTime=new Date(buyEndTs+val*60*1000).format("yyyy-MM-dd hh:mm:ss");
	     		}else{
	     			this.carAuction.auctionEndTime=new Date(buyEndTs+val*60*1000).format("yyyy-MM-dd hh:mm:ss");
	     			this.carAuction.buyEndTime=new Date(buyEndTs+val*60*1000).format("yyyy-MM-dd hh:mm:ss");
	     		}
	    		//alert(this.carAuction.buyEndTime);
	    		
            }//键路径必须加上引号  
	    },
	    mounted: function () {
	      	$("#delayPeriod").focus(function(){
	    		  $("#delayPeriod").css("border","1px solid #ccc");
	    		});
	   	    laydate.render({
		        elem: '#auctionStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.auctionStartTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#auctionEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.auctionEndTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#viewAuctionEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.viewAuctionEndTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#buyStartTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.buyStartTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#buyEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.buyEndTime = value
		        }
		    });
	   	    laydate.render({
		        elem: '#viewBuyEndTime',
		        type:'datetime',
		        done: (value) => {
		          this.carAuction.viewBuyEndTime = value
		        }
		    });
	    },
	    methods: {
	    viewData: function(){
	            $.ajax({
	                type: "POST",
	                url: basePath+'car/getAuction',
	                data: {"businessId":businessId},
	                // contentType: "application/json; charset=utf-8",
	                success: function (res) {
	                	if(res.code == "0000"){
		                	vm.carAuction=res.data.carAuction;
		                	if(vm.carAuction.delayPeriod==null||vm.carAuction.delayPeriod==''){
		                		vm.carAuction.delayPeriod=5;
		                	}
		                	vm.carAuction.viewAuctionEndTime=res.data.carAuction.auctionEndTime;
		                	vm.carAuction.viewBuyEndTime=res.data.carAuction.buyEndTime;
	                	}else{
	                		 layer.msg(res.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});
	                	}
	                
	                },
	                error: function (message) {
	                    layer.msg("查询拍卖信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
	                    console.error(message);
	                }
	            });
	 
	    	},
	    	auctionDelayedClose(){// 关闭窗口
	    		var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
	    		parent.layer.close(index);
	    	},
	    	carAuctionDelayed:function (){
		          $.ajax({
		               type: "POST",
		               url: basePath+'car/updateBuyEndTime',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"carAuction":vm.carAuction}),
		               success: function (res) {
		            	   if (res.code == "0000"){
		            		   layer.msg("保存成功。",{icon:1,shade: [0.8, '#393D49'],time:3000}); 
		            	   }else{
		            		   layer.msg(res.msg,{icon:5,shade: [0.8, '#393D49'],time:3000});  
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
//稍后放到公共类库去
Date.prototype.format = function(fmt) {      
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
    }
    return fmt; 
}


const date2date = (time, style="yyyy/MM/dd") => {
    if (time) {
        time = time.replace(/\-/g, "/")
        var oldTime = (new Date(time)).getTime();
        var curTime = new Date(oldTime).format(style);
        return curTime
    } else {
        return ''
    }
}