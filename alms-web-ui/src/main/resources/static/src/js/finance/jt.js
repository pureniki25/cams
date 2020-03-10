/**
 * 还款账号管理 js
 * @author zgh
 */
var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    basePath = htConfig.gatewayUrl;
    coreBasePath = htConfig.gatewayUrl;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data:{
            loading: false,
            saving: false, //存储标志位
            exporting: false, //导出标志位
            submitLoading: false,
            editModal: false,
            guDingModal:false,
            subjectModal:false,
            editModalTitle: '新增',
            editModalLoading: true,
            subjectModalLoading:true,
            detailModal: false,
            companys: [],
            bigSubjects:[],
            smallSubjects:[],
            camsTaxs: [],
            camsProductProperties:[],
            upload: {
                url: coreBasePath + 'jtDatController/importTaxExcel',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },
            guDingUpload: {
                url: coreBasePath + 'jtDatController/importZiChan',
                headers: {
                    Authorization: axios.defaults.headers.common['Authorization'],
                },
            },
            provinces: [],
            cities: [],
            banks: [],
        

            searchForm: {
                condition: {
                    LIKE_company_name: '',
                    GE_create_time:'',
                    LE_create_time:'',
                    GE_ping_zheng_ri_qi:'',
                    LE_ping_zheng_ri_qi:'',
                    EQ_jt_type:''
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
                feeType:'1' //费用银现
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
                    title: '期间',
                    key: 'qiJian',
                    sortable: true,//开启排序
                }, {
                    title: '凭证号',
                    key: 'pingZhengHao',
                    sortable: true,//开启排序
                },  {
                    title: '摘要',
                    key: 'zhaiYao',
                    sortable: true,//开启排序
                }, 
                  {
                    title: '科目代码',
                    width: 100,
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
                    sortable: true,//开启排序
                }, {
                    title: '借款金额',
                    width: 100,
                    key: 'borrowAmount',
                    sortable: true,//开启排序
                }, {
                    title: '贷款金额',
                    key: 'almsAmount',
                    sortable: true,//开启排序
                },{
                    title: '行号',
                    key: 'hangHao',
                    sortable: true,//开启排序
                },{
                    title: '单位',
                    key: 'danWei',
                    sortable: true,//开启排序
                },{
                    title: '导入时间',
                    key: 'createTime',
                    sortable: true,//开启排序
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
        this.guDingModal=false;
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
                    self.smallSubjects=res.data.data.smallSubjects;
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
               axios.post(basePath + 'jtDatController/delete', vm.selectIds)
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
        ExportForm.action = basePath+"jtDatController/export";
        ExportForm.target = "iframe";

        addInput(ExportForm, "text", "companyName", vm.searchForm.condition.LIKE_company_name); 
        addInput(ExportForm, "text", "openBeginTime", vm.searchForm.condition.GE_ping_zheng_ri_qi); 
        addInput(ExportForm, "text", "openEndTime", vm.searchForm.condition.LE_ping_zheng_ri_qi); 
        addInput(ExportForm, "text", "jtType", vm.searchForm.condition.EQ_jt_type); 
        ExportForm.submit();
        document.body.removeChild(ExportForm);

    },
    searchData() {debugger
        /* table.reload('main_table', {
             where: vm.search,
             page: {curr: 1}
         })*/

    if(vm.searchForm.condition.GE_ping_zheng_ri_qi!='') {
    	vm.searchForm.condition.GE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.GE_ping_zheng_ri_qi);
    }
    if(vm.searchForm.condition.LE_ping_zheng_ri_qi!='') {
    	vm.searchForm.condition.LE_ping_zheng_ri_qi = getSearchDate(vm.searchForm.condition.LE_ping_zheng_ri_qi);
    }
	
	
        var self = this;
        //self.loading =  true;
        //self.table.loading = true;
        axios.post(basePath + 'jtDatController/search', this.searchForm).then(res => {
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
            axios.post(basePath + 'FeeDatController/editSubject?smallSubjectId='+self.subjectForm.smallId+"&feeId="+self.editForm.id)
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