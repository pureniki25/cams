var basePath;
var vm;

window.layinit(function(htConfig) {
	
	basePath = htConfig.platRepayBasePath;

	vm = new Vue({
		el : '#app',
		data : {
			
			platformRepaymentInfoColumns:[
        		{
                    title: '期数',
                    key: 'periods',
                    align: 'center',
                },
                {
                    title: '应还日期',
                    key: 'cycDate',
                    align: 'center',
                },
                {
                    title: '应还合计',
                    key: 'repayAmount',
                    align: 'center',
                },
                {
                    title: '还款本金',
                    key: 'amount',
                    align: 'center',
                },
                {
                    title: '还款利息',
                    key: 'interestAmout',
                    align: 'center',
                },
                {
                    title: '还款状态（借款人还款状态）',
                    key: 'status',
                    align: 'center',
                },
            ],
            platformRepaymentInfoData:[];
		},

		methods : {
			/*
			 * 标的还款信息查询接口
			 */
			getProjectPayment: function(projectId){
				axios.get(basePath +"tdrepayRecharge/getProjectPayment?projectId=" + projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.platformRepaymentInfoData = res.data.data.resultList;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        });
			},
			
			/*
			 * 根据业务ID获取标信息
			 */
			getProjectInfoByBusinessId: function(businessId){
				axios.get(basePath +"tdrepayRecharge/getProjectInfoByBusinessId?businessId=" + projectId)
    	        .then(function (res) {
    	            if (res.data.data != null && res.data.code == 1) {
    	            	vm.platformRepaymentInfoData = res.data.data.resultList;
    	            } else {
    	            	vm.$Modal.error({content: res.data.msg });
    	            }
    	        })
    	        .catch(function (error) {
    	        	vm.$Modal.error({content: '接口调用异常!'});
    	        });
			}
		},

		mounted: function() {
			this.getProjectInfoByBusinessId(businessId);
		},

	});
	
});
