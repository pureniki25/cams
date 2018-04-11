var businessId = document.getElementById("businessId").getAttribute("value");
/*
* 贷后管理首页 台账 js
*
* */
var layer;
var table
//移交催收类型
// var staffType = document.getElementById("staffType").getAttribute("value");



function getMousePos(event) {
    var e =  window.event;
    return {'x':e.clientX,'y':e.clientY}
}


//区域列表数组
var area_array = [{
    areaId: '1',
    areaName: '区域一'
}, {
    areaId: '2',
    areaName: '区域二'
}, {
    areaId: '3',
    areaName: '区域三'
}];
//公司列表数组
var company_array = [{
    companyId: '1',
    companyName: '公司一'
}, {
    companyId: '2',
    companyName: '公司二'
}, {
    companyId: '3',
    companyName: '公司三'
}];
//业务类型数组
var btype_array = [{
    businessTypeId: '1',
    businessTypeName: '业务类型一'
}, {
    businessTypeId: '2',
    businessTypeName: '业务类型二'
}, {
    businessTypeId: '3',
    businessTypeName: '业务类型三'
}];

//业务状态列表数组
var business_status_array = [{
    paramValue: '1',
    paramName: '状态一'
}, {
    paramValue: '2',
    paramName: '状态二'
}, {
    paramValue: '3',
    paramName: '状态三'
}];
/*<i-option v-for="item in collectLevelList" :value="item.paramValue" :key="item.paramValue">{{ item.paramName }}</i-option>*/

//还款状态列表数组
var repay_status_array = [{
    paramValue: '1',
    paramName: '状态一'
}, {
    paramValue: '2',
    paramName: '状态二'
}, {
    paramValue: '3',
    paramName: '状态三'
}];




//催收级别定义
var  collect_level_array= [{
    paramValue: '1',
    paramName: 'M1(<31天)'
}, {
    paramValue: '2',
    paramName: 'M2(<31-60天)'
}, {
    paramValue: '3',
    paramName: 'M3(<61-90天)'
}];

//查询表单验证
var setSearchFormValidate = {
/**
    delayDaysBegin: [
        {pattern: /^[0-9]*$/, message: '请输入数字',trigger: 'blur'  },
    ],
    delayDaysEnd: [
        {pattern: /^[0-9]*$/, message: '请输入数字',trigger: 'blur'  }
    ],
    operatorName: [
        {type: 'string', message: '请输入文字', trigger: 'blur'}
    ],
    businessId: [
        {type: 'string', message: '请输入文字', trigger: 'blur'}
    ],
    liquidationOne: [
        {type: 'string', message: '请输入文字', trigger: 'blur'}
    ],
    liquidationTow: [
        {type: 'string', message: '请输入文字', trigger: 'blur'}
    ],
    customerName: [
        {type: 'string', message: '请输入文字', trigger: 'blur'}
    ]**/
};

var basePath;
var vm;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    
   // getSelectsData();
 vm = new Vue({
    el: '#app',
    data: {
        loading: false,
        saving:false, //存储标志位
       
        submitLoading: false,
        searchForm: {
        	businessId:'',
        	regName:'',  //客户名称
        	isPayDeposit:''
   
            
        },
        isPayDeposits:[{paramValue: '',paramName: '--请选择--'},{paramValue: true,paramName: '是'},{paramValue: false,paramName: '否'}],
        areaList:'',//area_array,   //区域列表
        companyList: '',//company_array,  //公司列表
        businessTypeList:'',//btype_array,   //业务类型列表
        carStatusList:'',//business_status_array,        //业务状态列表
        repayStatusList:'',//repay_status_array,        //还款状态列表
        collectLevelList:'',//collect_level_array, //催收级别列表


        selectedRowInfo:'',//存储当前选中行信息
        edit_modal:false,//控制编辑项选择modal显示的标志位
        menu_modal:false,
        ruleValidate:setSearchFormValidate //表单验证

    },

    methods: {
        //重载表格
        toLoading() {
        	//alert(vm.searchForm.businessId);
        	//alert($("#trailerStartDate").val());
            if (table == null) return;
            vm.$refs['searchForm'].validate((valid) =>{
                if (valid) {
                    this.loading = true;
                    console.log(vm.searchForm);

                    //var dateObj = getData();

                    table.reload('listTable', {
                        where: {
                        	businessId:businessId,//业务编号
                        	regName:vm.searchForm.regName,  //客户名称
                        	isPayDeposit:vm.searchForm.isPayDeposit  //是否缴纳保证金
  
                        }
                        , page: {
                            curr: 1 //重新从第 1 页开始
                        }
                    });
                    this.loading = false;
                }

                // this.loading = false;
            })
        },
        handleReset (name) { // 重置表单
        	vm.searchForm.trailerStartDate='';
        	vm.searchForm.trailerEndDate='';
        	
        	vm.searchForm.status='';
        	//var a=$("#trailerStartDate");
        	//console.log(a);
        	//alert(JSON.stringify(a));
            var tt = this.$refs[name];
            tt.resetFields();
            vm.toLoading();
        },

    },
    mounted:function(){
        //使用layerUI的表格组件
      layui.use(['layer', 'laydate','table','laytpl','ht_config'], function () {
            layer = layui.layer;
            config = layui.ht_config;
            table = layui.table;
        	 laydate = layui.laydate;
       	    laydate.render({
    	        elem: '#trailerStartDate',
    	        type:'datetime',
    	    
    	    });
      	    laydate.render({
    	        elem: '#trailerEndDate',
    	        type:'datetime',
    	     
    	    });
            //执行渲染
            table.render({
                elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                //, id: 'carList'
                , height: 550 //容器高度
                , cols: [[
                    {
                        field: 'bidderName',
                        title: '用户姓名'
                    }, {
                        field: 'bidderCertId',
                        title: '身份证号码'
                    }, {
                        field: 'bidderTel',
                        title: '联系方式'
                    },  {
                        field: 'payDepositStr',
                        title: '是否缴纳保证金'
                    }, {
                        field: 'offerAmount',
                        title: '出价金额'
                    }, {

                        field: 'auctionSuccessStr',
                        title: '是否竞拍成功'
                    }, {
                        fixed: 'right',
                        title: '操作',
                        width: 178,
                        align: 'left',
                        toolbar: '#barTools'
                    }
                ]], //设置表头
                url: basePath+'car/auctionRegList',
                //method: 'post' //如果无需自定义HTTP类型，可不加该参数
                //request: {} //如果无需自定义请求参数，可不加该参数
                //response: {} //如果无需自定义数据响应名称，可不加该参数
                width: window.innerWidth - 30,
                where: {businessId:businessId},
                page: true

            });

            //监听工具条
            table.on('tool(listTable)', function (obj) {
                vm.selectedRowInfo = obj.data;
                if (obj.event === 'edit') {
                    // vm.edit_modal = true;
                }else  if(obj.event ==='reg'){
                	 layer.open({
                		 title:'车辆拍卖信息登记',
                         type: 2,
                         content: '/assets/car/auctionReg?regId='+obj.data.regId,
                         area: ['40%', '60%']
                     });
                }
            });
	
        });
    }
});
});//

var getSelectedcrpIds = function(){
    var  crpIds = '';
    var checkStatus = table.checkStatus('listTable');


    for(var i=0;i<checkStatus.data.length;i++){
        if(i!=0){
            crpIds +=','
        }
        crpIds +=checkStatus.data[i].crpId;
    }
    return crpIds;
}

//为表单添加输入
var addInput = function(form, type,name,value){
    var input=document.createElement("input");
    input.type=type;
    input.name=name;
    input.value = value;
    form.appendChild(input);
}


//导出Excel文档
var downloadExcel = function (href, title) {
    const a = document.createElement('a');
    a.setAttribute('href', href);
    a.setAttribute('download', title);
    a.click();
}


//从后台获取下拉框数据
/**var getSelectsData = function () {
    //取区域列表
    axios.get('/core/collection/getALStandingBookVoPageSelectsData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
                vm.carStatusList = res.data.data.carStatusList;
                vm.repayStatusList = res.data.data.repayStatusList;
                vm.collectLevelList = res.data.data.collectLevelList;
                
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
   
            vm.$Modal.error({content: '接口调用异常!'});
        });
}**/


