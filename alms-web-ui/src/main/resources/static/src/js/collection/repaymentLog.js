
/*
* 代扣查询页面
*
* */
var layer;
var table;
var basePath;
var vm;
var dateBegin;
var dateEnd;
var dateObj;
var userId;
var dataObject ={
        dateBegin:'',
        dateEnd:'',
    }

//为表单添加输入
var addInput = function(form, type,name,value){
    var input=document.createElement("input");
    input.type=type;
    input.name=name;
    input.value = value;
    form.appendChild(input);
}
//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath +'InfoController/getSmsSelectData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
                vm.smsTypeList=res.data.data.smsType;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}



var getUserId = function () {

    //取区域列表
    axios.get(basePath +'RepaymentLogController/getUserId')
        .then(function (res) {debugger
            if (res.data.code == "1") {
                userId=res.data.data;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    getDeductionPlatformInfo();
    getSelectsData();
 
    vm = new Vue({
        el: '#app',
        data: {
            loading: false, //查询标志位
            exporting:false,//导出标志位
            searchForm:{
		        	companyId:'', //分公司ID
		        	keyName :'',      //业务编号 或客户名称
		            platformId :'',  //代扣平台
		            /*
		            dateRange:[{
		            	date1:new Date(),
		            	date2:new Date()
		            }],	//发送时间范围
		            */
		            dateRange:[new Date(new Date(new Date().toLocaleDateString()).getTime()-48*60*60*1000-1),new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1)],
		            repayStatus	:'',    //状态
		            businessTypeId:'',//业务类型
            },
            ruleValidate:setSearchFormValidate, //表单验证


            loading: false, //查询标志位
            saving:false, //存储标志位
            exporting:false,//导出标志位
            submitLoading: false,
            companyList:[],
            smsTypeList:[],
            businessTypeList:[],
            platformList:[],
            countByBusinessIdSucess:[],
            countByBusinessId:[],
            countbyLogId:[],
            SumRepayAmount:[],
            repayStatusList: [],
            selectedRowInfo:'',//存储当前选中行信息
            edit_modal:false,//控制编辑项选择modal显示的标志位
            menu_modal:false,


        },
        methods: {
        	
                 handleReset (name) {
                     var tt =this.$refs[name];
                     tt.resetFields();
                 },
            //重载表格
            toLoading() {
                if (table == null) return;
                vm.$refs['searchForm'].validate((valid) =>{
                    if (valid) {debugger
                        this.loading = true;
                        console.log(vm.searchForm);
                             
                       var  dateObj = getData();
                       getCountInfo();
                        table.reload('listTable', {
                            where: {
                                companyId:vm.searchForm.companyId, //分公司ID
                                keyName:vm.searchForm.keyName,   //业务编号 或客户名称
                                platformId:vm.searchForm.platformId,  //代扣平台
                                repayStatus:vm.searchForm.repayStatus,//代扣状态
                                dateBegin:dateObj.dateBegin, //发送时间  开始
                                dateEnd:dateObj.dateEnd, //发送时间 结束
                                businessTypeId:vm.searchForm.businessTypeId//业务类型

                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });

                    }

                    // this.loading = false;
                })
            },
            ////  ----   单行操作界面显示  结束 -----------------
         
            
            clickExport() {//导出Excel表格
                /**layui.config({
                    base: '/plugins/layui/extend/modules/',
                    version: false
                })**/
                layui.use(['layer', 'table','ht_config'], function () {
                	//config = layui.ht_config;
                vm.$refs['searchForm'].validate((valid) => {debugger
               
                        vm.exporting = true;
                        expoertExcel(basePath + "RepaymentLogController/saveExcel",vm.searchForm);
                        vm.exporting = false;
                })
                });
            }
        
        },
        mounted:function(){

        },
        created: function () {
    
        },
        
       
    });

    getCountInfo();

    //使用layerUI的表格组件
    layui.use(['layer', 'table','ht_ajax', 'ht_auth', 'ht_config'], function () {
        layer = layui.layer;
        table = layui.table;
        // var config = layui.ht_config;
        // basePath = config.basePath;
        //执行渲染
        table.render({
            elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
            , id: 'listTable'
            , height: 550 //容器高度
            , cols: [[


                {
                    field: 'originalBusinessId',
                    title: '业务编号',
                    width:165
                }, {
                    field: 'afterId',
                    title: '期数'
                },{
                    field: 'businessTypeName',
                    title: '业务类型'
                }, {
                    field: 'companyId',
                    title: '所属分公司'
                }, {
                    field: 'operatorName',
                    title: '业务获取'
                },{
                    field: 'customerName',
                    title: '客户名'
                },
                {
                    field: 'platformName',
                    title: '支付公司'
                },
                {
                    field: 'currentAmount',
                    title: '代扣金额',
                    align: 'right'
                },
                {
                    field: 'userName',
                    title: '代扣人'
                },        
                {
                    field: 'createTime',
                    title: '代扣时间'
                },
                {
                    field: 'repayStatus',
                    title: '代扣结果'
                },
                {
                    field: 'remark',
                    title: '备注'
                },
                
                
                {
                        fixed: 'right',
                        title: '详情',
                        width: 178,
                        align: 'left',
                        toolbar: '#barTools'
                    }
            ]], //设置表头
            url: basePath +'RepaymentLogController/selectRepaymentLogList',
            //method: 'post' //如果无需自定义HTTP类型，可不加该参数
            //request: {} //如果无需自定义请求参数，可不加该参数
            //response: {} //如果无需自定义数据响应名称，可不加该参数
            page: true,
            done: function (res, curr, count) {
                
                //数据渲染完的回调。你可以借此做一些其它的操作
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                vm.loading = false;
            }
          
        
        });

        //监听工具条
        table.on('tool(listTable)', function (obj) {
            vm.selectedRowInfo = obj.data;
            if (obj.event === 'edit') {
                // vm.edit_modal = true;
            }else  if(obj.event ==='info'){
                    title ="详情";
                    url = getDetailUrl();

                layer.open({
                    type: 2,
                    title: title,
                    maxmin: true,
                    area: ['1250px', '800px'],
                    content:url
                });
            }
        });


    });


})

//获取详情的URL路径
function getDetailUrl(){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'RepaymentLogController/getRepayLogDetailById?logId='+vm.selectedRowInfo.logId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            var repayLogDetail = data.data;
            url =  '/collectionUI/repaymentLogDetailUI?platformName='+vm.selectedRowInfo.platformName+'&thirdOrderId='+repayLogDetail.thirdOrderId+"&merchOrderId="+repayLogDetail.merchOrderId+"&currentAmount="+repayLogDetail.currentAmount+"&repayStatus="+repayLogDetail.repayStatus+"&createTime="+repayLogDetail.createTime+"&remark="+repayLogDetail.remark
        },
        error : function() {
            layer.confirm('Navbar error:AJAX请求出错!', function(index) {
                top.location.href = loginUrl;
                layer.close(index);
            });
            return false;
        }
    });

    return url;
}

//获取代扣业务条数，成功代扣流水数，成功代扣总额，成功代扣业务条数
var getCountInfo=function(){debugger
    var self = this;   
  if(dataObject.dateBegin==''){
	  dataObject.dateBegin=new Date(new Date(new Date().toLocaleDateString()).getTime()-48*60*60*1000-1).getTime();
		vm.searchForm.dateRange[0]=new Date(new Date(new Date().toLocaleDateString()).getTime()-48*60*60*1000-1);
  }
  
  if(dataObject.dateEnd==''){
	  dataObject.dateEnd=new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1).getTime();
		vm.searchForm.dateRange[1]=new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1);
  }
$.ajax({
    type : 'GET',
    async : false,
    url : basePath+ "RepaymentLogController/getCountInfo?companyId="+vm.searchForm.companyId+"&keyName="+vm.searchForm.keyName+"&platformId="+vm.searchForm.platformId+"&dateBegin="+dataObject.dateBegin+"&dateEnd="+dataObject.dateEnd+"&repayStatus="+vm.searchForm.repayStatus+"&businessTypeId="+vm.searchForm.businessTypeId,

    headers : {
        app : 'ALMS',
        Authorization : "Bearer " + getToken()
    },
    success : function(result) {debugger
        if (result.code == "1") {
        	
            
            vm.countByBusinessIdSucess = result.data.countByBusinessIdSucess;
            vm.countByBusinessId = result.data.countByBusinessId;
            vm.countbyLogId = result.data.countbyLogId;
            vm.SumRepayAmount = result.data.SumRepayAmount;
   
            
        } else {
            self.$Modal.error({content: '获取数据失败：' + result.data.msg});
        }   },
    error : function() {
        layer.confirm('Navbar error:AJAX请求出错!', function(index) {
            top.location.href = loginUrl;
            layer.close(index);
        });
        return false;
    }
});
    


}

//获取详情的URL路径
function getDetailUrl(){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'RepaymentLogController/getRepayLogDetailById?logId='+vm.selectedRowInfo.logId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            var repayLogDetail = data.data;
            url =  '/collectionUI/repaymentLogDetailUI?platformName='+vm.selectedRowInfo.platformName+'&thirdOrderId='+repayLogDetail.thirdOrderId+"&merchOrderId="+repayLogDetail.merchOrderId+"&currentAmount="+repayLogDetail.currentAmount+"&repayStatus="+repayLogDetail.repayStatus+"&createTime="+repayLogDetail.createTime+"&remark="+repayLogDetail.remark
        },
        error : function() {
            layer.confirm('Navbar error:AJAX请求出错!', function(index) {
                top.location.href = loginUrl;
                layer.close(index);
            });
            return false;
        }
    });

    return url;
}

function getMousePos(event) {
    var e =  window.event;
    return {'x':e.clientX,'y':e.clientY}
}






//查询表单验证
var setSearchFormValidate = {
    derateMoneyBegin: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ],
    derateMoneyEnd: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ]
};


var getDeductionPlatformInfo=function(){
	
	
    var self = this;  
    var reqStr =basePath+ "DeductionController/getDeductionPlatformInfo"
    axios.get(reqStr)
        .then(function (result) {
            if (result.data.code == "1") {
            	
      
                vm.platformList = result.data.data.platformList;
       
                
            } else {
                self.$Modal.error({content: '获取数据失败：' + result.data.msg});
            }
        })

    .catch(function (error) {
        vm.$Modal.error({content: '接口调用异常!'});
    });
	

}




//取查询的时间间隔
var getData = function(){debugger

     dataObject ={
        dateBegin:'',
        dateEnd:'',
    }
    if(vm.searchForm.dateRange.length>0){
    	if(vm.searchForm.dateRange[0]!=null){
    		 dataObject.dateBegin = vm.searchForm.dateRange[0].getTime();
    	}else{
    		var date1=new Date();
    		vm.searchForm.dateRange[0]=date1;
    		vm.searchForm.dateRange[0].setTime(new Date(new Date(new Date().toLocaleDateString()).getTime()-48*60*60*1000-1));
    		 dataObject.dateBegin=vm.searchForm.dateRange[0].getTime();
    	}
     	if(vm.searchForm.dateRange[1]!=null){
            var date =vm.searchForm.dateRange[1];
            dataObject.dateEnd=date.getTime();
   	   }else{
   		   var date2=new Date();
   		vm.searchForm.dateRange[1]=date2;
		vm.searchForm.dateRange[1].setTime(new Date(new Date(new Date().toLocaleDateString()).getTime()+24*60*60*1000-1));
		 dataObject.dateEnd=vm.searchForm.dateRange[1].getTime();
   		
   	   }

    }
    return dataObject;
}





