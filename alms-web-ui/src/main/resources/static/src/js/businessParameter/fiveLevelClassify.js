var basePath;
var vm;
var table;

//检测是否为数字
var isNumber = function(num){
	var reg = /^\d{0,9}?$/;
	if (reg.test(num)) {
		return true;
	}
	return false;
}

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    table = layui.table
	vm = new Vue({
	   el: '#app',
	   data: {
		    key: '',
		    showBusinessParamModal:false,
	    	addBusinessParamModal:false,
	    	editBusinessParamModal:false,
	    	setConditionCatalogue:false,
	    	showConditionModal:false,
	    	addConditionModal:false,
	    	updateConditionModal:false,
	    	
	    	commitBusinessTypeForm:{
	    		businessType: '',
	    		className: '',
	    		classLevel: '',
	    		remark: ''
	    	},
	    	
	    	commitQueryConditionModel:{
	    		businessType: '',
	    		className: '',
	    	},
	    	
	    	commitSetConditionModel:{
	    		parentId:'',
	    		subClassName: '',
	    		className: '',
	    		businessType: '',
	    		executeCondition:'',
	    		commitSetCondition: [
	    			{
	    				conditionType:'',
		    			relation:'',
		    			conditionDesc:'',
		    			conditionTypeList:[],
			    		conditionDescList:[],
			    		relationList:[],
	    			}
	    		]
	    	},
	    	
	    	updateCommitSetCondition: [
    			{
    				conditionType:'',
	    			relation:'',
	    			conditionDesc:'',
	    			conditionTypeList:[],
		    		conditionDescList:[],
		    		relationList:[],
    			}
    		],
	    	
	    	fiveLevelClassifyModel:{
	    		id:'',
	    		editBusinessTypeModalTitle:'',
	    		businessType:'',
	    		className:'',
	    		conditionTypeList:[],
	    		businessTypeList: [],
	    		conditionDescList_01:[],	// 本金逾期
	    		conditionDescList_02:[],	// 首月逾期
	    		conditionDescList_03:[],	// 利息逾期
	    		conditionDescList_04:[],	// 主借款人情况
	    		conditionDescList_05:[],	// 抵押物情况
	    		conditionDescList_06:[],	// 档案资料情况
	    		conditionDescList_07:[],	// 案件情况
	    		relationList_01:[
	    			{value:'5', label:'='}
	    		],
	    		relationList_02:[
	    			{value:'1', label:'>='},
	    			{value:'2', label:'<='},
	    			{value:'3', label:'>'},
	    			{value:'4', label:'<'},
	    			{value:'5', label:'='}
	    		]
	    	},
	    	
    		ruleValidate: {
    			businessType: [
    	            { required: true, message: '业务类型不能为空', trigger: 'blur' }
    	        ],
    	        className: [
    	            { required: true, message: '分类名称不能为空', trigger: 'blur' }
    	        ],
    	        classLevel:[
    	        	{ required: true, message: '类别级别不能为空', trigger: 'blur' }
    	        ]
    	    },
	   },
	       
	   methods: {
		   handleParams:function(paramTypeName){
				if (paramTypeName == '本金逾期') {
					return vm.fiveLevelClassifyModel.conditionDescList_01;
				}else if (paramTypeName== '首月逾期') {
					return vm.fiveLevelClassifyModel.conditionDescList_02;
				}else if (paramTypeName == '利息逾期') {
					return vm.fiveLevelClassifyModel.conditionDescList_03;
				}else if (paramTypeName == '主借款人情况') {
					return vm.fiveLevelClassifyModel.conditionDescList_04;
				}else if (paramTypeName == '抵押物情况') {
					return vm.fiveLevelClassifyModel.conditionDescList_05;
				}else if (paramTypeName == '档案资料情况') {
					return vm.fiveLevelClassifyModel.conditionDescList_06;
				}else if (paramTypeName == '案件情况') {
					return vm.fiveLevelClassifyModel.conditionDescList_07;
				}
		   },
		   openAddOrEidtBusinessParamModal:function(data, add){
		    	if (add) {
		            this.$refs['commitBusinessTypeForm'].resetFields();
		            this.commitBusinessTypeForm.className = '';
		            this.commitBusinessTypeForm.classLevel = '';
		            this.commitBusinessTypeForm.remark = '';
		            this.fiveLevelClassifyModel.editBusinessTypeModalTitle = '新增类别';
		            this.addBusinessParamModal = true;
		            this.editBusinessParamModal = false;
		            this.showBusinessParamModal = true;
		        } else {
		        	this.commitBusinessTypeForm.id = data.id;
		        	this.commitBusinessTypeForm.businessType = data.businessType;
		        	this.commitBusinessTypeForm.classLevel = data.classLevel;
		            this.commitBusinessTypeForm.className = data.className;
		            this.commitBusinessTypeForm.remark = data.remark;
		            this.fiveLevelClassifyModel.editBusinessTypeModalTitle = '编辑类别';
		            this.addBusinessParamModal = false;
		            this.editBusinessParamModal = true;
		            this.showBusinessParamModal = true;
		        }
		   },
		   openSetConditionModal:function(data, add){
			   this.commitSetConditionModel.businessType = this.fiveLevelClassifyModel.businessType;
			   this.commitSetConditionModel.className = this.fiveLevelClassifyModel.className;
			   this.commitSetConditionModel.parentId = this.fiveLevelClassifyModel.id;
			   this.commitSetConditionModel.commitSetCondition = [];
			   
			   if (add) {
				   this.commitSetConditionModel.subClassName = '';
				   this.commitSetConditionModel.executeCondition = '';
				   this.commitSetConditionModel.commitSetCondition = [
		    			{
		    				conditionType:'',
			    			relation:'',
			    			conditionDesc:'',
			    			conditionTypeList:this.fiveLevelClassifyModel.conditionTypeList,
				    		conditionDescList:[],
				    		relationList:[],
		    			}
		    		];
				   this.setConditionCatalogue = false;
				   this.addConditionModal = true;
				   this.showConditionModal = true;
			   }else {
				   this.commitSetConditionModel.subClassName = data.subClassName;
				   $.ajax({
		                type: "GET",
		                url: basePath + 'businessParameter/queryConditionForClassify?className=' + data.className + '&businessType=' + data.businessType + '&subClassName=' + data.subClassName,
		                async:true,
		                success: function (res) {
		                	if (res.code == "1") {
							   var classifyConditions = res.data.classifyConditions;
							   vm.commitSetConditionModel.commitSetCondition = [];
							   if (classifyConditions != null && classifyConditions.length > 0) {
								   
								   vm.commitSetConditionModel.executeCondition = classifyConditions[0].executeCondition;
								   
								   for (var i = 0; i < classifyConditions.length; i++) {
									   vm.commitSetConditionModel.commitSetCondition.push(
											{
											    conditionType:classifyConditions[i].paramType,
												relation:classifyConditions[i].typeNameRelation,
												conditionDesc:classifyConditions[i].paramName,
												conditionTypeList:vm.fiveLevelClassifyModel.conditionTypeList,
												conditionDescList:vm.handleParams(classifyConditions[i].paramType),
												relationList: isNumber(classifyConditions[i].paramName) ? vm.fiveLevelClassifyModel.relationList_02 : vm.fiveLevelClassifyModel.relationList_01,
								    		}
									   )
								   }
							   }
							   vm.showConditionModal = true;
						   } else {
							   vm.$Modal.error({ content: res.msg });
						   }
		                },
		                error: function (message) {
		                	vm.$Modal.error({ content: message });
		                }
		            });
				   
				   this.setConditionCatalogue = false;
				   this.updateConditionModal = true;
			   }
		   },
		   openSetConditionCatalogue:function(data){
			   this.fiveLevelClassifyModel.id = data.id;
			   this.fiveLevelClassifyModel.businessType = data.businessType;
			   this.fiveLevelClassifyModel.className = data.className;
			   table.render({
			    	elem: "#condition",
			    	height: 600 // 容器高度
			    	,
			    	cols: [
			    		[{
			    			title: '序号',
			    			templet: function(d){
			    				return d.LAY_INDEX
			    			},
			    			align: 'center',
							width:200
			    		}, {
			    			field: 'subClassName',
			    			title: '条件名称',
			    			align: 'center',
							width:200
			    		}, {
			    			field: 'updateTime',
			    			title: '更新时间',
			    			align: 'center',
							width:200
			    		}, {
			    			title: '操作',
			    			toolbar: "#toolbar2",
			    			align: 'center',
							width:200
			    		}]
			    		], // 设置表头
			    		url: basePath + 'businessParameter/queryFiveLevelClassifyCondition?className=' + vm.fiveLevelClassifyModel.className + "&businessType=" + vm.fiveLevelClassifyModel.businessType,
			    		done: (res, curr, count) => {}
			    })
			   this.setConditionCatalogue = true;
		   },
		   deleteBusinessParamModal:function(data){
			   axios.get(basePath + 'businessParameter/queryMayBeUsed?businessType=' + data.businessType + '&className=' + data.className)
			   .then(function (result) {
				   if (result.data.code == "1") {
					   vm.$Modal.confirm({content: '您确认要删除 ' + data.businessType + '---' + data.className + ' 吗？',
						   onOk:function(){
							   axios.post(basePath + 'businessParameter/deleteFiveLevelClassify', data)
							   .then(function (res) {
								   if (res.data.code == "1") {
									   tableReload(vm.key && vm.key != '' ? { key: vm.key } : {}, { curr: 1 })
								   } else {
									   vm.$Modal.error({ content: res.data.msg });
								   }
							   })
							   .catch(function (err) {
								   vm.$Modal.error({ content: '接口调用异常!' });
							   })
						   }
					   });
				   } else {
					   vm.$Modal.error({ content: result.data.msg });
				   }
			   })
			   .catch(function (err) {
				   vm.$Modal.error({ content: '接口调用异常!' });
			   })
			   
		   },
		   deleteConditionParamModal:function(data){
			   vm.$Modal.confirm({content: '您确认要删除 ' + data.businessType + '---' + data.className + '---' + data.subClassName + ' 吗？',
				   onOk:function(){
					   axios.post(basePath + 'businessParameter/deleteConditionParamModal', data)
					   .then(function (res) {
						   if (res.data.code == "1") {
							   tableReloadCondition(vm.key && vm.key != '' ? { key: vm.key } : {}, { curr: 1 })
						   } else {
							   vm.$Modal.error({ content: res.data.msg });
						   }
					   })
					   .catch(function (err) {
						   vm.$Modal.error({ content: '接口调用异常!' });
					   })
				   }
			   });
		   },
		   saveConditionForClassify:function(data){
			   if (this.addConditionModal) {
				   axios.post(basePath + 'businessParameter/saveConditionForClassify', this.commitSetConditionModel)
				   .then(function (res) {
					   if (res.data.code == "1") {
						   vm.showConditionModal = false;
					   } else {
						   vm.$Modal.error({ content: res.data.msg });
					   }
				   })
				   .catch(function (err) {
					   vm.$Modal.error({ content: err.msg });
				   })
			   }else if(this.updateConditionModal) {
				   axios.post(basePath + 'businessParameter/updateConditionForClassify', this.commitSetConditionModel)
				   .then(function (res) {
					   if (res.data.code == "1") {
						   vm.showConditionModal = false;
					   } else {
						   vm.$Modal.error({ content: res.data.msg });
					   }
				   })
				   .catch(function (err) {
					   vm.$Modal.error({ content: err.msg });
				   })
			   }
		   },
		   queryDataByCondition:function(){
			   table.render({
			        elem: "#param",
			        height: 600 // 容器高度
			        ,
			        cols: [
					    [{
					        title: '序号',
					        templet: function(d){
					        	return d.LAY_INDEX
					        },
					        align: 'center'
					    }, {
					        field: 'businessType',
					        title: '业务类型',
					        align: 'center'
					    }, {
					        field: 'className',
					        title: '分类名称',
					        align: 'center'
					    }, {
					        field: 'updateTime',
					        title: '更新时间',
					        align: 'center'
					    }, {
					        field: 'updateUser',
					        title: '更新人',
					        align: 'center'
					    }, {
					        title: '操作',
					        toolbar: "#toolbar",
					        align: 'center'
					    }]
					], 
					url: basePath + 'businessParameter/queryDataByCondition',
					where: {
                        businessType: this.commitQueryConditionModel.businessType, 
                        className: this.commitQueryConditionModel.className
	                },
					page: false,
					done: (res, curr, count) => { }
			    })
		   },
		   saveFiveLevelClassify:function(){
			   if (this.addBusinessParamModal) {
				   this.$refs['commitBusinessTypeForm'].validate((valid) => {
					   axios.post(basePath + 'businessParameter/saveFiveLevelClassify', this.commitBusinessTypeForm)
					   .then(function (res) {
						   console.log(res)
						   if (res.data.code == "1") {
							   vm.showBusinessParamModal = false
							   tableReload(vm.key && vm.key != '' ? { key: vm.key } : {}, { curr: 1 })
						   } else {
							   vm.$Modal.error({ content: res.data.msg });
						   }
					   })
					   .catch(function (err) {
						   vm.$Modal.error({ content: '接口调用异常!' });
					   })
				   })
			   }else if (this.editBusinessParamModal) {
				   this.$refs['commitBusinessTypeForm'].validate((valid) => {
					   axios.post(basePath + 'businessParameter/updateFiveLevelClassify', this.commitBusinessTypeForm)
					   .then(function (res) {
						   console.log(res)
						   if (res.data.code == "1") {
							   vm.showBusinessParamModal = false
							   tableReload(vm.key && vm.key != '' ? { key: vm.key } : {}, { curr: 1 })
						   } else {
							   vm.$Modal.error({ content: res.data.msg });
						   }
					   })
					   .catch(function (err) {
						   vm.$Modal.error({ content: '接口调用异常!' });
					   })
				   })
			   }
		   },
		   initConditionDescList:function(){
			   axios.get(basePath + 'businessParameter/queryConditionDesc')
               .then(function (res) {
                   console.log(res)
                   if (res.data.code == "1") {
                	   if (res.data.data) {
                		   vm.fiveLevelClassifyModel.conditionDescList_01 = res.data.data.fiveLevelClassify_01;
		               	   vm.fiveLevelClassifyModel.conditionDescList_02 = res.data.data.fiveLevelClassify_02;
		               	   vm.fiveLevelClassifyModel.conditionDescList_03 = res.data.data.fiveLevelClassify_03;
		               	   vm.fiveLevelClassifyModel.conditionDescList_04 = res.data.data.fiveLevelClassify_04;
		               	   vm.fiveLevelClassifyModel.conditionDescList_05 = res.data.data.fiveLevelClassify_05;
		               	   vm.fiveLevelClassifyModel.conditionDescList_06 = res.data.data.fiveLevelClassify_06;
		               	   vm.fiveLevelClassifyModel.conditionDescList_07 = res.data.data.fiveLevelClassify_07;
                	   }
                   } else {
                       vm.$Modal.error({ content: res.data.msg });
                   }
               })
               .catch(function (err) {
                   vm.$Modal.error({ content: '接口调用异常!' });
               })
		   },
               
		   initModelList:function(){
			   axios.get(basePath + 'businessParameter/queryTypes')
               .then(function (res) {
                   console.log(res)
                   if (res.data.code == "1") {
                	   if (res.data.data) {
            			   vm.fiveLevelClassifyModel.businessTypeList = res.data.data.basicBusinessTypes;
            			   vm.fiveLevelClassifyModel.conditionTypeList = res.data.data.sysParameters;
                	   }
                   } else {
                       vm.$Modal.error({ content: res.data.msg });
                   }
               })
               .catch(function (err) {
                   vm.$Modal.error({ content: '接口调用异常!' });
               })
		   },
		   cancelCommit:function(){
			   this.addBusinessParamModal = false;
			   this.editBusinessParamModal = false;
			   this.showBusinessParamModal = false;
			   this.setConditionCatalogue = false;
			   this.showConditionModal = false;
			   this.addConditionModal = false;
			   this.updateConditionModal = false;
		   },
		   addSelectOp:function(){
	    		this.commitSetConditionModel.commitSetCondition.push({
					conditionType: '',
    				relation: '',
    				conditionDesc: '',
    				conditionTypeList: vm.fiveLevelClassifyModel.conditionTypeList,
    				conditionDescList:[],
    				relationList:[],
	    		});
	 	    },
	 	    removeSelectOp:function(event, index){
	 	    	this.commitSetConditionModel.commitSetCondition.splice(index, 1);
	 	    },
		   conditionTypeChange:function(value, index){
			   if(value == '主借款人情况' || value == '抵押物情况' || value == '档案资料情况' || value == '案件情况'){
				   vm.commitSetConditionModel.commitSetCondition[index].relationList = vm.fiveLevelClassifyModel.relationList_01;
			   }else{
				   vm.commitSetConditionModel.commitSetCondition[index].relationList = vm.fiveLevelClassifyModel.relationList_02; 
			   }
			   
			   if (value == '本金逾期') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_01;
			   }
			   if (value == '首月逾期') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_02;
			   }
			   if (value == '利息逾期') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_03;
			   }
			   if (value == '主借款人情况') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_04;
			   }
			   if (value == '抵押物情况') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_05;
			   }
			   if (value == '档案资料情况') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_06;
			   }
			   if (value == '案件情况') {
				   vm.commitSetConditionModel.commitSetCondition[index].conditionDescList = vm.fiveLevelClassifyModel.conditionDescList_07;
			   }
		   }
	   },
	   
	   created:function(){
		   this.initModelList();
		   this.initConditionDescList();
	   },
	   
	})
    
    table.render({
        elem: "#param",
        height: 600 // 容器高度
        ,
        cols: [
		    [{
		        title: '序号',
		        templet: function(d){
		        	return d.LAY_INDEX
		        },
		        align: 'center'
		    }, {
		        field: 'businessType',
		        title: '业务类型',
		        align: 'center'
		    }, {
		        field: 'className',
		        title: '分类名称',
		        align: 'center'
		    }, {
		        field: 'updateTime',
		        title: '更新时间',
		        align: 'center'
		    }, {
		        field: 'updateUser',
		        title: '更新人',
		        align: 'center'
		    }, {
		        title: '操作',
		        toolbar: "#toolbar",
		        align: 'center'
		    }]
		], // 设置表头
		url: basePath + 'businessParameter/queryFiveLevelClassifys',
		page: true,
		done: (res, curr, count) => { }
    })
    
    table.on('tool(param)', function (obj) {
        let data = obj.data;
        let event = obj.event;
        if (event == 'edit') {
            vm.openAddOrEidtBusinessParamModal(data, false)
        }
        if (event == 'delete') {
            vm.deleteBusinessParamModal(data);
        }
        if (event == 'setCondition') {
            vm.openSetConditionCatalogue(data)
        }
    })
    
    table.on('tool(condition)', function (obj) {
    	let data = obj.data;
    	let event = obj.event;
    	if (event == 'edit') {
    		vm.openSetConditionModal(data, false)
    	}
    	if (event == 'delete') {
    		vm.deleteConditionParamModal(data);
    	}
    })
    
});

var tableReload = function (where, page) {
    let playload = {}
    if(where && where.length > 0){
        playload.where = where
    }
    playload.page = page 
    table.reload('param', playload)
};

var tableReloadCondition = function (where) {
	let playload = {}
	if(where && where.length > 0){
		playload.where = where
	}
	table.reload('condition', playload)
};
