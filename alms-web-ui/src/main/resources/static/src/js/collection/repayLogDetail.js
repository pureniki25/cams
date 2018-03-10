var layer;
	var table;
	var basePath;
	var vm;
	var ajax_data;
	var platformName = document.getElementById("platformName").getAttribute("value");
	var thirdOrderId = document.getElementById("thirdOrderId").getAttribute("value");
	var merchOrderId = document.getElementById("merchOrderId").getAttribute("value");
	var currentAmount = document.getElementById("currentAmount").getAttribute("value");
	var repayStatus = document.getElementById("repayStatus").getAttribute("value");
	
	var createTime = document.getElementById("createTime").getAttribute("value");
	var remark = document.getElementById("remark").getAttribute("value");
	window.layinit(function (htConfig) {
		

		if(repayStatus=='1'){
			repayStatus="成功";
		}else if(repayStatus=='0'){
			repayStatus="失败";
		}else{
			repayStatus="处理中";
		}
		
		var _htConfig = htConfig;
        basePath = _htConfig.coreBasePath;
        ajax_data = {
        		platformName:platformName,
        		thirdOrderId:thirdOrderId,
        		merchOrderId:merchOrderId,
        		currentAmount:currentAmount,
        		repayStatus:repayStatus,
        		createTime:createTime,
        		remark:remark
        };
        
        vm = new Vue({
            el: '#app',
            data:{
            	columns1: [
                    {
                        title: 'key',
                        key: 'key',
                        width:250
                    },
                    {
                        title: 'value',
                        key: 'value'
                    }
                ],
                data1: [
                    {
                        key: '代扣平台',
                        value: ajax_data.platformName
                    },
                    {
                    	key: '商户流水号',
                    	value: ajax_data.thirdOrderId
                    },
                    {
                    	key: '商户订单号',
                    	value: ajax_data.merchOrderId
                    },
                    {
                    	key: '交易金额',
                    	value: ajax_data.currentAmount
                    },
                    {
                    	key: '支付结果',
                    	value: ajax_data.repayStatus
                    }
                    ,
                    {
                    	key: '订单发送时间',
                    	value: ajax_data.createTime
                    }
                    ,
                    {
                    	key: '备注',
                    	value: ajax_data.remark
                    }
                ],
                
          
            }
         
         
        });
	});
	
	
	
		
   
	