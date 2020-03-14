/**
 * 还款账号管理 js
 * 
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    basePath = htConfig.gatewayUrl;
    coreBasePath = htConfig.gatewayUrl;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
            loading: false,
            saving: false, // 存储标志位
            exporting: false, // 导出标志位
            submitLoading: false,
            editModal: false,
            subjectModal:false,
            editModalTitle: '新增',
            editModalLoading: true,
            addModal: false,
            addModalTitle: '新增',
            addModalLoading: false,
            subjectModalLoading:true,
            detailModal: false,
            updateModal: false,
            companys: [],
            customers:[],
            bigSubjects:[],
            smallSubjects:[],
            camsTaxs: [],
            camsProductProperties:[],
          
            provinces: [],
            cities: [],
            banks: [],
        	upload: {
        			url: basePath + 'bankInAndOut/importBankFlowExcel',
        			headers: {
        			 Authorization: axios.defaults.headers.common['Authorization'],
        			},
             },
            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    GE_create_time:'',
                    LE_create_time:'',
                    GE_ping_zheng_ri_qi:'',
                    LE_ping_zheng_ri_qi:''
             
                },
                current: 1,
                size: 500
            },
            editForm: {
            	id:'',
            	ids:'',
                companyId: '',
                companyName: '',
                createTime: '',
                productPropertiesId:'',
                openDate:'',
                salaryDate:'',
                customerCode:''
            },
            updateForm:{
            	id:'',
            	invoiceNumber:'',
            	pingZhengHao:'',
            	zhaiYao:'',
            	keMuDaiMa:'',
            	localAmount:'',
            	borrowAmount:'',
            	almsAmount:'',
            	hangHao:''
            	
            	
            	
            	
            },
            subjectForm:{
            	id:'',
                smallId:''
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
                ids:[{
                    validator:function(rule, value, callback, source, options){
                        if(vm.editForm.ids.length == 0 ){
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
                    key: 'companyName',
                    sortable: true// 开启排序
            
                    
                },
                      {
                    title: '发票号码',
                    width: 100,
                    key: 'invoiceNumber',
                    sortable: true,// 开启排序
                }, {
                    title: '期间',
                    key: 'qiJian',
                    sortable: true,// 开启排序
                }, {
                    title: '凭证号',
                    key: 'pingZhengHao',
                    sortable: true,// 开启排序
                },  {
                    title: '摘要',
                    key: 'zhaiYao',
                    sortable: true,// 开启排序
                }, 
                      {
                    title: '科目代码',
                    width: 100,
                    sortable: true,// 开启排序
                    key: 'keMuDaiMa',
                    render:(h,params)=>{debugger
                    	if(params.row.keMuDaiMa!='') {
        	            		return h('span',{
        	            			style:{
        	            			 'font-weight': 'bold',
        	            			 'background-color': ' #C7EDCC'
        	            		}
                    		},params.row.keMuDaiMa)
                    	}else {
                    		return h('span',{},params.row.keMuDaiMa)  
                    	}
                    }
                }, {
                    title: '原币金额',
                    width: 100,
                    key: 'localAmount',
                    sortable: true// 开启排序
                }, {
                    title: '借款金额',
                    width: 100,
                    key: 'borrowAmount',
                    sortable: true// 开启排序
                }, {
                    title: '贷款金额',
                    key: 'almsAmount',
                    sortable: true// 开启排序
                },{
                    title: '行号',
                    key: 'hangHao',
                    sortable: true// 开启排序
                },{
                    title: '单位',
                    key: 'danWei',
                    sortable: true// 开启排序
                },{
                    title: '导入时间',
                    key: 'createTime',
                    sortable: true// 开启排序
                }
                ,{
                    title: '操作',
                    key: 'action',
                    // fixed: 'right',
                    align: 'center',
                    render: (h, params) => {
                       

                        let subject = h('Button', {
                            props: {
                                type: '',
                                size: 'small'
                            },
                            style: {
                                marginRight: '10px'
                            },
                            on: {
                                click: () => {
                                    vm.subject(params.row);
                                }
                            },
                        }, '设置科目');
                        
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
                        btnArr.push(subject);

                      
                        
                        return h('div', btnArr);
                    }
                }
                ],
                addCol: [
                    
                    {
                        type: 'selection',
                        width: 60,
                        align: 'center'
                    },  {
                    	title: '序号',
                        key:'idx',
                        align: 'center',
                    	width:100, 
                    	   render: (h, params) => {
                    	        return h('span', params.index + 1);
                    	    }

                    	},
                      
                          {
                        title: '发票号码',
                        width: 130,
                        align: 'center',
                        key: 'invoiceNumber',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.invoiceNumber
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.invoiceNumber = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.invoiceNumber)
                          }
                
                    },  {
                        title: '摘要',
                        key: 'zhaiYao',
                        width: 130,
                        align: 'center',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.zhaiYao
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.zhaiYao = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.zhaiYao)
                          }
                    }, 
                          {
                        title: '科目代码',
                        width: 130,
                        align: 'center',
                        sortable: true,// 开启排序
                        key: 'keMuDaiMa',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.keMuDaiMa
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.keMuDaiMa = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.keMuDaiMa)
                          }
                    },{
                        title: '原币金额',
                        width: 100,
                        key: 'localAmount',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.localAmount
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.localAmount = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.localAmount)
                          }
                    }, {
                        title: '借款金额',
                        width: 110,
                        key: 'borrowAmount',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.borrowAmount
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.borrowAmount = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.borrowAmount)
                          }
                    }, {
                        title: '贷款金额',
                        key: 'almsAmount',
                        width: 130,
                        align: 'center',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.almsAmount
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.almsAmount = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.almsAmount)
                          }
                    },{
                        title: '行号',
                        key: 'hangHao',
                        width: 130,
                        align: 'center',
                        render: (h,params) => {
                            return h('div',{
                             props: {
                               value:params.row.hangHao
                             },
                             attrs: {
                                contenteditable: true
                             },
                                on: {
                                  "blur"(e) {
                                	  debugger
                                    params.row.hangHao = e.currentTarget.innerText;
                                	    vm.table.addData.splice(params.index,1,params.row)
                                  }}
                            },params.row.hangHao)
                          }
                    },  {
                        title: '操作',
                        key: 'action',
                        // fixed: 'right',
                        align: 'center',
                        render: (h, params) => {
                           

                            let del = h('Button', {
                                props: {
                                    type: '',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '10px'
                                },
                                on: {
                                    click: () => {
                                        vm.deleteRow(params.row);
                                    }
                                },
                            }, '删除');

                  

                            let btnArr = [];
                            btnArr.push(del);

                          
                            
                            return h('div', btnArr);
                        }
                    }
                    ],
                data: [],
                addData:[],
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
         axios.post(basePath + 'bankInAndOut/update', self.updateForm)
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
	change(val){
		debugger
		this.editForm.companyName=val;
		this.getCustomer();
	},
	onSelectionChange (row) {
		  this.selectData = row
		},
	addRow(){
		let obj={
				companyName:null,
				invoiceNumber:null,
				qiJian:null,
				pingZhengHao:null,
				zhaiYao:null,
				keMuDaiMa:null,
				localAmount:null,
				borrowAmount:null,
				almsAmount:null,
				hangHao:null,
				danWei:null,
				createTime:null
				
		}
		vm.table.addData.splice(0,0,obj);
	},
	deleteRow(row){
		debugger
		let index=-1;
		for(var i=0;i<vm.table.addData.length;i++){
			if(row._index===i){
				index=i;
			}
		}
		debugger
		if(index>-1){
			vm.table.addData.splice(index,1);
		}
		
	}, 
	saveAddRows(){
	   	 var self = this;
	     $.ajax({

            url: basePath + 'bankInAndOut/save',

            type: "post",

            dataType: "json",

            data: {
                selects: JSON.stringify(self.table.addData),
                date: vm.editForm.openDate,
                companyName:vm.editForm.companyName,
                customerCode:vm.editForm.customerCode
            },
            success: function (data) { // 请求服务器成功后返回页面时页面可以进行处理，data就是后端返回的数据
                vm.$Modal.success({
                    content: "操作成功"
                })
                self.search();
                self.hideEditModal();
                this.editModalLoading = true;
            },
            error: function (e) {
                self.$Modal.error({
                    content: '请求接口失败,消息:' + res.data.msg
                })
                this.editModalLoading = false;
            }


        })
	              
	     
	},
beforeUpLoadFile() {debugger// 导入Excel表格
// vm.condata.companyId=vm.editForm.companyId;
// vm.condata.productPropertiesId.companyId=vm.editForm.productPropertiesId;

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
    showSubjectModal() {
        this.subjectModal = true;
    },
    hideSubjectModal() {
        this.subjectModal = '设置科目';
        this.subjectModal = false;
        this.$refs['subjectForm'].resetFields();
    },
    hideEditModal() {
        this.editModalTitle = '新增';
        this.editModal = false;
        this.$refs['editForm'].resetFields();
    },
    hideUpdateModal() {
        this.updateModal = false;
        this.$refs['updateForm'].resetFields();
    },
    hideAddModal() {
        this.addModalTitle = '新增';
        this.addModal = false;
     
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
                    self.camsProductProperties=res.data.data.camsProductProperties;
                    self.smallSubjects=res.data.data.smallSubjects;
                    self.bankSubjects=res.data.data.bankSubjects;
                } else {
                    self.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
                }
            })
            .catch(err => {
                self.$Modal.error({content: '操作失败!'});
            });

  
    },
    getCustomer() {
        var self = this;
        // 根据所选择的省查询市列表
        axios.post(coreBasePath + 'customerDat/findAllCustomer',this.editForm)
            .then(res => {
                if (!!res.data && res.data.code == '1') {
                    self.customers = res.data.data.customers;
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
    	debugger
   	 var self = this;
       this.$Modal.confirm({
           title: '提示',
           content: '确定要删除吗?',
           onOk: () => {
               axios.post(basePath + 'bankInAndOut/delete', vm.selectIds)
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
        ExportForm.action = basePath+"bankInAndOut//exportIncome";
        ExportForm.target = "iframe";

        addInput(ExportForm, "text", "companyName", vm.searchForm.condition.LIKE_company_name); 
        addInput(ExportForm, "text", "beginTime", vm.searchForm.condition.GE_create_time); 
        addInput(ExportForm, "text", "endTime", vm.searchForm.condition.LE_create_time); 
      // addInput(ExportForm, "text", "bankType",
		// vm.searchForm.condition.EQ_bank_type);
        addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_ping_zheng_ri_qi); 
        addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_ping_zheng_ri_qi); 
        ExportForm.submit();
        document.body.removeChild(ExportForm);

    },
    searchData() {debugger
        /*
		 * table.reload('main_table', { where: vm.search, page: {curr: 1} })
		 */
    	vm.searchForm.condition.GE_create_time = getSearchDate(vm.searchForm.condition.GE_create_time);
    vm.searchForm.condition.LE_create_time=getSearchDate(vm.searchForm.condition.LE_create_time);
    if(vm.searchForm.condition.GE_ping_zheng_ri_qi!='') {
    	vm.searchForm.condition.GE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.GE_ping_zheng_ri_qi);
    }
    if(vm.searchForm.condition.LE_ping_zheng_ri_qi!='') {
    	vm.searchForm.condition.LE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.LE_ping_zheng_ri_qi);
    }

        var self = this;
        // self.loading = true;
        // self.table.loading = true;
        axios.post(basePath + 'bankInAndOut/incomeSearch', this.searchForm).then(res => {
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
getSmallSubject(){
    axios.get(coreBasePath + 'FeeDatController/findSmallSubject?id='+vm.subjectForm.id)
    .then(res => {
        if (!!res.data && res.data.code == '1') {debugger
          
            vm.smallSubjects=res.data.data.smallSubjects;
        } else {
            vm.$Modal.error({content: '调用接口失败,消息:' + res.data.msg});
        }
    })
    .catch(err => {
        vm.$Modal.error({content: '操作失败!'});
    });
},
submitSubjectForm() {debugger
    var self = this;
            axios.post(basePath + 'bankInAndOut/editSubject?smallSubjectId='+self.subjectForm.smallId+"&id="+self.editForm.id)
            .then(res => {
                if (!!res.data && res.data.code == '1') {debugger
                    self.search();
                    self.hideSubjectModal();
                    this.subjectModalLoading = true;
                } else {
                    self.$Modal.error({content: '请求接口失败,消息:' + res.data.msg})
                    this.subjectModalLoading = false;
                }
            })
            .catch(err => {
                self.$Modal.success({content: '操作成功!'})
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
    this.editModalTitle = '编辑';
    this.$refs['editForm'].resetFields();
    Object.assign(this.editForm, row);
    this.editForm.ids = !!row.id ? row.id.split(',') : [];
    this.showEditModal();
},

subject(row) {debugger
    this.ModalTitle = '设置科目';
    this.$refs['editForm'].resetFields();
    Object.assign(this.editForm, row);
    this.editForm.id=row.id;
    this.showSubjectModal();
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