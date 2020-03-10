/**
 * 还款账号管理 js
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
        saving: false, //存储标志位
        exporting: false, //导出标志位
        submitLoading: false,
        editModal: false,
        updateModal:false,
        editModalTitle: '新增',
        editModalLoading: true,
        detailModal: false,
        companys: [],
        camsTaxs: [],
        camsProductProperties:[],
      
        provinces: [],
        cities: [],
        banks: [],
    	upload: {
    			url: coreBasePath + 'InvoiceController/importBuyFlowExcel',
    			headers: {
    			 Authorization: axios.defaults.headers.common['Authorization'],
    			},
         },
   
        searchForm: {
            condition: {
                LIKE_company_code: '',
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
            productPropertiesId:'',
            openDate:'',
            isAssets:'1',
            subject:'',
            period:'',
            type:'',
            isTake:'',
            buyDate:'',
            jtDate:'',
            buyId:'',
            id:''
        },
        updateForm: {
            companyId: '',
            companyName: '',
            createTime: '',
            productPropertiesId:'',
            openDate:'',
            isAssets:'1',
            subject:'',
            period:'',
            type:'',
            isTake:'',
            buyDate:'',
            jtDate:'',
            buyId:'',
            id:''
        },
        condata:{
      	  companyId:"123",
      	  productPropertiesId:"234"
          },
        ruleValidate: {
            //表单验证
            //financeName: [{required: true, message: '必填项', trigger: 'blur'}],
            repaymentName: [{required: true, message: '请输入账户名', trigger: 'blur'}],
            repaymentId: [{required: true, message: '请输入账号', trigger: 'blur'}],
            repaymentBank: [{required: true, message: '请选择开户行', trigger: 'blur'}],
            //repaymentSubBank: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
            //mainType: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
            //mainId: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
            //bankProvinceCity: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
            //phoneNumber: [{required: true, message: '请选择业务类型', trigger: 'blur'}],
            // deptIds: [{required: true, message: '请至少关联一个分公司', trigger: 'blur'}],
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
                align: 'center',
                sortable: true,//开启排序
            },  
              {
                title: '公司名称',
                width: 100,
                key: 'companyName',
                sortable: true,//开启排序
            },
                  {
                title: '发票号码',
                width: 100,
                key: 'invoiceNumber',
                sortable: true,//开启排序
            }, {
                title: '产品编码',
                key: 'productCode',
                sortable: true,//开启排序
            }, {
                title: '资产原值',
                key: 'amount',
                sortable: true,//开启排序
            },{
                title: '资产净值',
                key: 'restAmount',
                sortable: true,//开启排序
            },{
                title: '年限',
                key: 'period',
                sortable: true,//开启排序
            },{
                title: '购买日期',
                key: 'buyDate',
                sortable: true,//开启排序
            },
            {
                title: '计提折旧日期',
                key: 'jtDate',
                sortable: true,//开启排序
            }, {
                title: '科目代码',
                key: 'subject',
                sortable: true,//开启排序
            },
            {
                title: '操作',
                key: 'action',
                // fixed: 'right',
                align: 'center',
                render: (h, params) => {


                    let edit = h('Button', {
                        props: {
                            type: '',
                            size: 'small'
                        },
                        style: {
                            marginRight: '10px'
                        },
                        on: {
                            click: () => {
                                vm.edit(params.row);
                            }
                        },
                    }, '编辑');



                    let btnArr = [];
                    btnArr.push(edit);



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


    /*table.render({
        elem: "#main_table",
        id: 'main_table',
        height: 600, //容器高度
        url: basePath + 'finance/search',
        page: true,
        //done: (res, curr, count) => { }
        cols: [
            [{
                field: 'areaName',
                title: '区域'
            }, {
                field: 'companyName',
                title: '分公司',
            }, {
                field: 'businessTypeName',
                title: '业务类型'
            }, {
                field: 'userName',
                title: '跟单员',
            }, {
                title: '操作',
                toolbar: "#bar_tools"
            }]
        ], //设置表头
    })

    table.on('tool(main_table)', function (obj) {
        let data = obj.data;
        let event = obj.event;
        if (event == 'del') {
            layer.confirm('确定要删除?', function (index) {
                vm.delete(data.id, data.userId);
                layer.close(index);
            });
        }
    })*/
})


 
let methods = {
	   handleSubmit(updateForm) { 
           debugger
           var self = this;
           self.updateForm.buyDate=getSearchDate(self.updateForm.buyDate)
              self.updateForm.jtDate=getSearchDate(self.updateForm.jtDate)
           axios.post(coreBasePath + 'buyDatExe/edit', self.updateForm)
               .then(res => {
                   if (!!res.data && res.data.code == '1') {
                       debugger
                       self.search();
                       self.hideEditModal();
                       this.editModalLoading = true;
                   } else {
                       self.$Modal.error({
                           content: '请求接口失败,消息:' + res.data.msg
                       })
                       this.editModalLoading = false;
                   }
               })
               .catch(err => {
                   self.$Modal.error({
                       content: '操作失败!'
                   })
               });
       },
	detail(row) {
	    Object.assign(this.editForm, row);
	    this.showDetailModal();
	},
beforeUpLoadFile() {debugger//导入Excel表格 
//  vm.condata.companyId=vm.editForm.companyId;
//  vm.condata.productPropertiesId.companyId=vm.editForm.productPropertiesId;

}
,

uploadSuccess(response) {
    if (response.code == '1') {
        vm.$Modal.success({content: "导入成功"})
    } else {
        vm.$Modal.error({content: response.msg})
    }

   },
    showEditModal() {
        this.editModal = true;
    },
    showUpdateModal(){
    	this.updateModal=true;
    },
    hideEditModal() {
        this.editModalTitle = '新增';
        this.editModal = false;
        this.updateModal=false;
        this.$refs['editForm'].resetFields();
    },
    showDetailModal() {
        this.detailModal = true;
    },
    //初始化数据
    initData() {
        var self = this;
        //公司列表
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
        //根据所选择的省查询市列表
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
       this.$Modal.confirm({
           title: '提示',
           content: '确定要删除吗?',
           onOk: () => {
               axios.post(basePath + 'InvoiceController/deleteBuy', vm.selectIds)
               .then(res => {debugger
                   if (!!res.data && res.data.code == '1') {
                       
                       self.search()
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
        ExportForm.action = basePath+"InvoiceController/exportBuy";
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
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/
    vm.searchForm.condition.GE_create_time = getSearchDate(vm.searchForm.condition.GE_create_time);
    vm.searchForm.condition.LE_create_time=getSearchDate(vm.searchForm.condition.LE_create_time);
    if(vm.searchForm.condition.GE_open_date!='') {
    	vm.searchForm.condition.GE_open_date = getSearchDate(vm.searchForm.condition.GE_open_date);
    }
    if(vm.searchForm.condition.LE_open_date!='') {
    	vm.searchForm.condition.LE_open_date = getSearchDate(vm.searchForm.condition.LE_open_date);
    }
	
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'buyDatExe/assetsSearch', this.searchForm).then(res => {
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
// self.loading =  false;
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


getSearchDate(data)  {
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
    },
edit(row) {
    	debugger
    this.editModalTitle = '编辑';
    this.$refs['updateForm'].resetFields();
    Object.assign(this.updateForm, row);
    this.showUpdateModal();
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