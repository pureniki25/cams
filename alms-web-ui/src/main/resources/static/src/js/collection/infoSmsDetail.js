var layer;
	var table;
	var basePath;
	var vm;
	var ajax_data;
	var logId = document.getElementById("logId").getAttribute("value");
	var originalBusinessId = document.getElementById("originalBusinessId").getAttribute("value");
	var afterId = document.getElementById("afterId").getAttribute("value");
	var recipient = document.getElementById("recipient").getAttribute("value");
	var phoneNumber = document.getElementById("phoneNumber").getAttribute("value");
	var sendDate = document.getElementById("sendDate").getAttribute("value");
	var status = document.getElementById("status").getAttribute("value");
	var content = document.getElementById("content").getAttribute("value");
	window.layinit(function (htConfig) {
		var _htConfig = htConfig;
        basePath = _htConfig.coreBasePath;
        ajax_data = {
        		logId:logId,
        		originalBusinessId:originalBusinessId,
        		afterId:afterId,
        		recipient:recipient,
        		phoneNumber:phoneNumber,
        		sendDate:sendDate,
        		status:status,
        		content:content,
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
                        key: '业务编号',
                        value: ajax_data.originalBusinessId
                    },
                    {
                    	key: '期数',
                    	value: ajax_data.afterId
                    },
                    {
                    	key: '短信接收人',
                    	value: ajax_data.recipient
                    },
                    {
                    	key: '接收手机号',
                    	value: ajax_data.phoneNumber
                    },
                    {
                    	key: '发送时间',
                    	value: ajax_data.sendDate
                    },
                    {
                    	key: '短信内容',
                    	value: ajax_data.content
                    }
                ],
                
          
            },
            methods: {
                getLast(){
                	getLastSms();
                	
                },
                getNext(){
                	
                	getNextSms();
                }
          
            
            }
       
        });
	});
	
	
	var getLastSms=function(){
		
		
        var self = this;
        var reqStr =basePath+ "InfoController/selectLastInfoSmsDetail?logId=" + ajax_data.logId
        axios.get(reqStr)
            .then(function (result) {debugger
                if (result.data.code == "1") {
                	
          
                    ajax_data=result.data.data;
                    vm.data1[0].value=ajax_data.originalBusinessId
                    vm.data1[1].value=ajax_data.afterId
                    vm.data1[2].value=ajax_data.recipient
                    vm.data1[3].value=ajax_data.phoneNumber
                    vm.data1[4].value=ajax_data.sendDate
                    vm.data1[5].value=ajax_data.content
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}
	
	
	var getNextSms=function(){
		
		
        var self = this;
        var reqStr =basePath+ "InfoController/selectLastInfoSmsDetail?logId=" + ajax_data.logId
        axios.get(reqStr)
            .then(function (result) {debugger
                if (result.data.code == "1") {
                	
          
                    ajax_data=result.data.data;
                    vm.data1[0].value=ajax_data.originalBusinessId
                    vm.data1[1].value=ajax_data.afterId
                    vm.data1[2].value=ajax_data.recipient
                    vm.data1[3].value=ajax_data.phoneNumber
                    vm.data1[4].value=ajax_data.sendDate
                    vm.data1[5].value=ajax_data.content
                } else {
                    self.$Modal.error({content: '获取数据失败：' + result.data.msg});
                }
            })
   
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
		
   
	}