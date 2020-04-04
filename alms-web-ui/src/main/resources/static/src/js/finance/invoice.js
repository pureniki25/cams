/**
 * 还款账号管理 js
 * 
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
	  var _htConfig = htConfig;
    basePath = _htConfig.gatewayUrl;
    coreBasePath = _htConfig.gatewayUrl;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
            loading: false,
            saving: false, // 存储标志位
            exporting: false, // 导出标志位
            submitLoading: false,
            editModal: false,
            editModalTitle: '新增',
            editModalLoading: true,
            detailModal: false,
            updateModal: false,
            selectIds:[],
            companys: [],
            camsTaxs: [],
            camsProductProperties:[],
          
            provinces: [],
            cities: [],
            banks: [],
        	upload: {
        			url: coreBasePath + 'InvoiceController/importCustomerFlowExcel',
        			headers: {
        			 Authorization: axios.defaults.headers.common['Authorization']
        			},
             },
            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    GE_create_time:'',
                    LE_create_time:'',
                    GE_open_date:'',
                    LE_open_date:''
                },
                current: 1,
                size: 500
            },
            editForm: {
                companyId: '',
                companyName: '',
                createTime: '',
                importType: '',
                productPropertiesId:'',
                template: ''
                
                
            },
            updateForm:{
            cash:''
            },
            condata:{
          	  companyId:"123",
          	  productPropertiesId:"234"
              },
            ruleValidate: {
                // 表单验证
                // financeName: [{required: true, message: '必填项', trigger:
				// 'blur'}],
                repaymentName: [{required: true, message: '请输入账户名', trigger: 'blur'}],
                repaymentId: [{required: true, message: '请输入账号', trigger: 'blur'}],
                repaymentBank: [{required: true, message: '请选择开户行', trigger: 'blur'}],
                // repaymentSubBank: [{required: true, message: '请选择业务类型',
				// trigger: 'blur'}],
                // mainType: [{required: true, message: '请选择业务类型', trigger:
				// 'blur'}],
                // mainId: [{required: true, message: '请选择业务类型', trigger:
				// 'blur'}],
                // bankProvinceCity: [{required: true, message: '请选择业务类型',
				// trigger: 'blur'}],
                // phoneNumber: [{required: true, message: '请选择业务类型', trigger:
				// 'blur'}],
                // deptIds: [{required: true, message: '请至少关联一个分公司', trigger:
				// 'blur'}],
                deptIds:[{
                    validator:function(rule, value, callback, source, options){
                        if(vm.editForm.deptIds.length == 0 ){
                            callback(new Error('请至少关联一个分公司'));
                        }else{
                            callback();
                        }
                    },
                    trigger:'blur'
                }]
            },
            table: {
                col: [
                      
                      {
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },  
                  {
                    title: '公司名称',
                    width: 100,
                    key: 'companyCode',
                    sortable: true// 开启排序
                },
                      {
                    title: '发票号码',
                    width: 100,
                    key: 'invoiceNumber',
                    sortable: true// 开启排序
                }, {
                    title: '会计期间',
                    key: 'accountPeriod',
                    sortable: true// 开启排序
                }, {
                    title: '单据号',
                    key: 'documentNo',
                    sortable: true// 开启排序
                },  {
                    title: '客户名称',
                    key: 'customerCode',
                    sortable: true// 开启排序
                }, 
                  {
                    title: '导入时间',
                    width: 100,
                    key: 'createTime',
                    sortable: true// 开启排序
                }, {
                    title: '开票日期',
                    width: 100,
                    key: 'openDate',
                    sortable: true// 开启排序
                }, {
                    title: '行号',
                    key: 'rowNumber',
                    sortable: true// 开启排序
                }, {
                    title: '商品',
                    key: 'produceCode',
                    sortable: true// 开启排序
                }, {
                    title: '计量单位',
                    key: 'calUnit',
                    sortable: true// 开启排序
                },{
                    title: '数量',
                    key: 'number',
                    sortable: true// 开启排序
                },{
                    title: '原币单位',
                    key: 'unitPrice',
                    sortable: true// 开启排序
                },{
                    title: '原币金额',
                    key: 'originalAmount',
                    sortable: true// 开启排序
                },{
                    title: '税率',
                    key: 'taxRate',
                    sortable: true// 开启排序
                },{
                    title: '原币税额',
                    key: 'originalTax',
                    sortable: true// 开启排序
                },{
                    title: '现金',
                    key: 'cash',
                    sortable: true// 开启排序
                }
				 ,{
				 title: '操作',
				 key: 'action',
				 // fixed: 'right',
				 align: 'center',
				 render: (h, params) => {
					    let update = h('Button', {
                            props: {
                                type: '',
                                size: 'small'
                            },
                            style: {
                                marginRight: '10px'
                            },
                            on: {
                                click: () => {
                                    vm.update(params.row);
                                }
                            },
                        }, '编辑');

              

                        let btnArr = [];
                        btnArr.push(update);
                        return h('div', btnArr);
				 }
				 }
                ],
                data: [],
                loading: false,
                total: 0
            } 
        },
        methods: methods,
        created: function () {
            this.initData();
            this.search();
        },
        updated: function () {
            this.loading = false;
        }
     
    })


    /*
	 * table.render({ elem: "#main_table", id: 'main_table', height: 600, //容器高度
	 * url: basePath + 'finance/search', page: true, //done: (res, curr, count) => { }
	 * cols: [ [{ field: 'areaName', title: '区域' }, { field: 'companyName',
	 * title: '分公司', }, { field: 'businessTypeName', title: '业务类型' }, { field:
	 * 'userName', title: '跟单员', }, { title: '操作', toolbar: "#bar_tools" }] ],
	 * //设置表头 })
	 * 
	 * table.on('tool(main_table)', function (obj) { let data = obj.data; let
	 * event = obj.event; if (event == 'del') { layer.confirm('确定要删除?', function
	 * (index) { vm.delete(data.id, data.userId); layer.close(index); }); } })
	 */
})


 
let methods = {
	submitUpdateForm(){
	   	 var self = this;
        axios.post(basePath + 'InvoiceController/update', self.updateForm)
        .then(res => {
            if (!!res.data && res.data.code == '1') {
                self.$Modal.success({
                    content: "编辑成功"
                })
                self.search();
                self.hideEditModal();
            } else {
                self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
            }
        })
        .catch(err => {
            self.$Modal.error({content: '操作失败!'})
        });
	},
	update(row) {
		debugger
	    this.$refs['updateForm'].resetFields();
	    Object.assign(this.updateForm, row);
	  
	    this.showUpdateModal();
	},
	  showUpdateModal() {
        this.updateModal= true;
    },
    hideUpdateModal() {
        this.updateModal = false;
        this.$refs['updateForm'].resetFields();
    },

beforeUpLoadFile() {debugger// 导入Excel表格
	vm.loading =  true;
// vm.condata.companyId=vm.editForm.companyId;
// vm.condata.productPropertiesId.companyId=vm.editForm.productPropertiesId;

}
,

uploadSuccess(response) {
	vm.loading =  false;
    if (response.code == '1') {
        vm.$Modal.success({content: "导入成功"})
    } else {
        vm.$Modal.error({content: response.msg})
    }

   },
    showEditModal() {
        this.editModal = true;
    },
    hideEditModal() {
        this.editModalTitle = '新增';
        this.editModal = false;
        this.$refs['editForm'].resetFields();
    },
    showDetailModal() {
        this.detailModal = true;
    },
    // 初始化数据
    initData() {
        var self = this;
        // 公司列表
        axios.get(coreBasePath + 'InvoiceController/findAll')
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.companys = res.data.data.companys;
                    self.camsTaxs = res.data.data.camsTaxs;
                    self.camsProductProperties=res.data.data.camsProductProperties;
                } else {
                    self.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            });

  
    },
    initCityData() {
        var self = this;
        // 根据所选择的省查询市列表
        axios.get(coreBasePath + 'sysCity/findAllByProvinceName?provinceName=' + this.editForm.bankProvince)
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.cities = res.data.data;
                } else {
                    self.$Modal.error({content: '调用接口失败，消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            })
    },

    paging(current) {
        this.searchForm.current = current;
        this.searchData();
    },
    search(){
        this.searchForm.current = 1;
        this.searchData();
    },
    daochu(){
        this.searchForm.current = 1;
        this.exportData();
    },
    deleteAll(){
   	 var self = this;
    	if(vm.selectIds.length==0){
    		 self.$Modal.error({content: '请选择要删除的数据'})
    		 return
    	}
    
        this.$Modal.confirm({
            title: '提示',
            content: '确定要删除吗?',
            onOk: () => {
                axios.post(basePath + 'InvoiceController/delete', vm.selectIds)
                .then(res => {debugger
                    if (!!res.data && res.data.code == '1') {
                        
                        self.search()
                        self.$Modal.success({content: "删除成功"})
                    } else {
                        self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                    }
                })
                .catch(err => {
                    self.$Modal.error({content: '操作失败!'})
                })
            }
        });
    },
    generateByInvoice(){
      	 var self = this;
       	if(vm.selectIds.length==0){
       		 self.$Modal.error({content: '请选择要生成凭证的数据'})
       		 return
       	}
       
           this.$Modal.confirm({
               title: '提示',
               content: '确定生成凭证吗?',
               onOk: () => {
                   axios.post(basePath + 'InvoiceController/generateByInvoice', vm.selectIds)
                   .then(res => {debugger
                       if (!!res.data && res.data.code == '1') {
                           
                           self.search()
                           self.$Modal.success({content: "生成凭证成功,请到费用单管理查看销售采购凭证"})
                       } else {
                           self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                       }
                   })
                   .catch(err => {
                       self.$Modal.error({content: '操作失败!'})
                   })
               }
           });
       },
       generateByCompany(){
    	   	 var self = this;
    	    	if(vm.selectIds.length==0){
    	    		 self.$Modal.error({content: '请选择要生成凭证的数据'})
    	    		 return
    	    	}
    	    
    	        this.$Modal.confirm({
    	            title: '提示',
    	            content: '确定生成凭证吗?',
    	            onOk: () => {
    	                axios.post(basePath + 'InvoiceController/generateByCompany', vm.selectIds)
    	                .then(res => {debugger
    	                    if (!!res.data && res.data.code == '1') {
    	                        
    	                        self.search()
    	                         self.$Modal.success({content: "生成凭证成功,请到费用单管理查看销售采购凭证"})
    	                    } else {
    	                        self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
    	                    }
    	                })
    	                .catch(err => {
    	                    self.$Modal.error({content: '操作失败!'})
    	                })
    	            }
    	        });
    	    },
    exportData() {debugger
        var ExportForm = document.createElement("FORM");
        document.body.appendChild(ExportForm);
        ExportForm.method = "POST";
        ExportForm.action = basePath+"InvoiceController/export";
        ExportForm.target = "iframe";

        addInput(ExportForm, "text", "companyCode", vm.searchForm.condition.LIKE_company_code); 
        addInput(ExportForm, "text", "beginTime", vm.searchForm.condition.GE_create_time); 
        addInput(ExportForm, "text", "endTime", vm.searchForm.condition.LE_create_time); 
        addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_open_date); 
        addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_open_date); 
        ExportForm.submit();
        document.body.removeChild(ExportForm);

    },
    searchData() {debugger
        /*
		 * table.reload('main_table', { where: vm.search, page: {curr: 1} })
		 */
    	
		// shijianchuo是整数，否则要parseInt转换
    	vm.searchForm.condition.GE_create_time = getSearchDate(vm.searchForm.condition.GE_create_time);
    vm.searchForm.condition.LE_create_time=getSearchDate(vm.searchForm.condition.LE_create_time);
    if(vm.searchForm.condition.GE_open_date!='') {
    	vm.searchForm.condition.GE_open_date = getSearchDate(vm.searchForm.condition.GE_open_date);
    }
    if(vm.searchForm.condition.LE_open_date!='') {
    	vm.searchForm.condition.LE_open_date = getSearchDate(vm.searchForm.condition.LE_open_date);
    }
		
        var self = this;
        // self.loading = true;
        // self.table.loading = true;
        axios.post(basePath + 'InvoiceController/sellSearch', this.searchForm).then(res => {
            self.table.loading = false;
            if (!!res.data && res.data.code == 0) {
                self.table.data = res.data.data;
                self.table.total = res.data.count;
            } else {
                self.$Message.error({content: res.data.msg})
            }
        }
    ).catch(err => {
        self.table.loading = false;
    }
);
// self.loading = false;
},
submitEditForm() {
    var self = this;
    this.$refs['editForm'].validate( valid => {
        if(valid){
            axios.post(basePath + 'InvoiceController/edit', self.editForm)
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.search();
                    self.hideEditModal();
                    this.editModalLoading = true;
                } else {
                    self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                    this.editModalLoading = false;
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'})
            });
        }else{
            setTimeout(() => {
                this.editModalLoading = false;
                this.$nextTick(() => {
                    this.editModalLoading = true;
                });
            }, 1000);
        }
    });
},
edit(row) {
    this.editModalTitle = '编辑';
    this.$refs['editForm'].resetFields();
    Object.assign(this.editForm, row);
    this.editForm.deptIds = !!row.deptId ? row.deptId.split(',') : [];
    this.showEditModal();
},
detail(row) {
    Object.assign(this.editForm, row);
    this.showDetailModal();
},
requires(value) {
    vm.selectIds = value;
},

delete(row) {
    var self = this;
    this.$Modal.confirm({
        title: '提示',
        content: '确定要删除吗?',
        onOk: () => {
            axios.get(basePath + 'InvoiceController/delete', {params: {id: row.companyId}})
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    
                    self.search()
                     vm.$Modal.success({content: "删除成功"})
                } else {
                    self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'})
            })
        }
    });
},
}
function getSearchDate(data)  {debugger
	var y = data.getFullYear();
	var m = data.getMonth()+1;		
	var d = data.getDate();
	
	if(m<10) {
		m='0'+m; 
	}
	if(d<10) {
		d='0'+d; 
	}
	
	return  y+'-'+m+'-'+d;
    }