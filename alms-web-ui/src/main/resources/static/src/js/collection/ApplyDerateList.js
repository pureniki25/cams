
/*
* 贷后管理首页 台账 js
*
* */
var layer;
var table;
var basePath;
var vm;
//移交催收类型
// var staffType = document.getElementById("staffType").getAttribute("value");


window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;

    getSelectsData();

    vm = new Vue({
        el: '#app',
        data: {
            loading: false, //查询标志位
            exporting:false,//导出标志位
            searchForm:{
                keyName      	:'',      //业务编号 或客户名称
                areaId  			:'',  //区域ID
                companyId			:'',  //分公司ID
                businessType		:'',  //业务类型
                derateDateRange		:'',	//减免时间 包含开始和结束时间
                derateMoneyBegin	:'',    //减免金额 开始
                derateMoneyEnd		:'', //减免金额 结束
            },
            ruleValidate:setSearchFormValidate, //表单验证


            loading: false, //查询标志位
            saving:false, //存储标志位
            exporting:false,//导出标志位
            submitLoading: false,
            /* searchForm: {
                 areaId:'',  //区域ID
                 companyId:'', //分公司ID
                 // showRepayDateBegin:'', //应还日期  开始
                 // showRepayDateEnd:'',  //应还日期 结束
                 showRepayDateRange:'',//应还日期 区间 包含开始和结束时间
                 // realRepayDateBegin:'', //实还日期 开始
                 // realRepayDateEnd:'', //实还日期 结束
                 realRepayDateRange:'',//实还日期  区间 包含开始和结束时间
                 delayDaysBegin:'',    //逾期天数 开始
                 delayDaysEnd:'', //逾期天数 结束
                 collectLevel:'',  //催收级别
                 operatorName:'',  //业务获取
                 businessId:'',  //业务编号
                 businessType:'',  //业务类型
                 liquidationOne:'', //清算一
                 liquidationTow:'',  //清算二
                 businessStatus:'',   //业务状态
                 repayStatus:'',      //还款状态
                 customerName:''  //客户名称
             },*/
            areaList:'',//area_array,   //区域列表
            companyList: '',//company_array,  //公司列表
            businessTypeList:'',//btype_array,   //业务类型列表
            businessStatusList:'',//business_status_array,        //业务状态列表
            repayStatusList:'',//repay_status_array,        //还款状态列表
            collectLevelList:'',//collect_level_array, //催收级别列表


            selectedRowInfo:'',//存储当前选中行信息
            edit_modal:false,//控制编辑项选择modal显示的标志位
            menu_modal:false,


        },
        methods: {
            //重载表格
            toLoading() {
                if (table == null) return;
                vm.$refs['searchForm'].validate((valid) =>{
                    if (valid) {
                        this.loading = true;
                        console.log(vm.searchForm);

                        var dateObj = getData();

                        table.reload('listTable', {
                            where: {

                                keyName:vm.searchForm.keyName,   //业务编号 或客户名称
                                areaId:vm.searchForm.areaId,  //区域ID
                                companyId:vm.searchForm.companyId, //分公司ID
                                businessType:vm.searchForm.businessType,  //业务类型

                                derateDateBegin:dateObj.derateDateBegin, //减免时间  开始
                                derateDateEnd:dateObj.derateDateEnd,  //减免时间 结束

                                derateMoneyBegin:vm.searchForm.derateMoneyBegin, //减免金额  开始
                                derateMoneyEnd:vm.searchForm.derateMoneyEnd,  //减免金额 结束

                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });

                    }

                    // this.loading = false;
                })
            },
            clickExport() {//导出Excel表格
     
                
                layui.use(['layer', 'table','ht_config'], function () {debugger
                    vm.$refs['searchForm'].validate((valid) => {
    		                    if (valid) {debugger
    		                        vm.exporting = true;
    		                        expoertExcel(basePath + "ApplyDerateController/saveExcel",vm.searchForm);

    		                        vm.exporting = false;
    		
    		                    }
    		                })
                    });
            }
        },
        mounted:function(){

        }
    });



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
                    field: 'businessId',
                    width:200,
                    title: '业务编号',
                    align:'center'
                }, {
                    field: 'periods',
                    title: '期数',
                    align:'center'
                },{
                    field: 'customerName',
                    title: '客户名称',
                    align:'center'
                }, {
                    field: 'businessTypeName',
                    title: '业务类型',
                    align:'center'
                }, {
                    field: 'districtAreaName',
                    title: '区域',
                    align:'center'
                }, {
                    field: 'companyName',
                    title: '分公司',
                    align:'center'
                },{
                    field: 'createrName',
                    title: '发起减免人',
                    align:'center'
                },{
                    field: 'derateTypeName',
                    title: '减免费用项名称',
                    align:'center'
                },{
                    field: 'showPayMoney',
                    title: '应收金额',
                    align: 'right',
                    templet:function(d){
                        return d.showPayMoney?numeral(d.showPayMoney).format('0,0.00'):''
                    }
                },{
                    field: 'derateMoney',
                    title: '减免金额',
                    align: 'right',
                    templet:function(d){
                        return d.derateMoney?numeral(d.derateMoney).format('0,0.00'):''
                    }
                },{
                    field: 'realPayMoney',
                    title: '实收金额',
                    align: 'right',
                    templet:function(d){
                        return d.realPayMoney?numeral(d.realPayMoney).format('0,0.00'):''
                    }
                },{
                    field: 'derateTime',
                    title: '减免时间',
                    templet:function(d){
                        return d.derateTime?moment(d.derateTime).format("YYYY年MM月DD日"):''
                    }
                }/* {
                        fixed: 'right',
                        title: '操作',
                        width: 178,
                        align: 'left',
                        toolbar: '#barTools'
                    }*/
            ]], //设置表头
            url: basePath +'ApplyDerateController/selectApplyDeratVoPage',
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
                // vm.menu_modal = false;
            }
        });


    });


})


function getMousePos(event) {
    var e =  window.event;
    return {'x':e.clientX,'y':e.clientY}
}

//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath +'ApplyDerateController/getApplyDeratPageSelectsData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
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



//查询表单验证
var setSearchFormValidate = {
    derateMoneyBegin: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if(vm.searchForm.derateMoneyBegin!=""&& vm.searchForm.derateMoneyEnd!=""){
                    if (parseInt(vm.searchForm.derateMoneyBegin) > parseInt(vm.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        vm.searchForm.derateMoneyEnd = vm.searchForm.derateMoneyEnd;
                        callback();//校验通过
                    }
                }else{
                    vm.searchForm.derateMoneyEnd =vm.searchForm.derateMoneyEnd;
                    callback();
                }

            }, trigger: 'blur'
        }
    ],
    derateMoneyEnd: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'},
        {
            validator: function (rule, value, callback, source, options) {
                if(vm.searchForm.derateMoneyBegin!=""&& vm.searchForm.derateMoneyEnd!=""){
                    if (parseInt(vm.searchForm.derateMoneyBegin) > parseInt(vm.searchForm.derateMoneyEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        callback();//校验通过
                    }
                }else{
                    callback();
                }

            }, trigger: 'blur'
        }
    ]
};






//取查询的时间间隔
var getData = function(){

    var dataObject ={
        derateDateBegin:'',
        derateDateEnd:'',
    }
    if(vm.searchForm.derateDateRange.length>0){
    	if(vm.searchForm.derateDateRange[0]!=null){
    	      dataObject.derateDateBegin = vm.searchForm.derateDateRange[0].getTime();
    	}
    	
      	if(vm.searchForm.derateDateRange[1]!=null){
      	   var date =vm.searchForm.derateDateRange[1];
           date.setDate(date.getDate() + 1);
           dataObject.derateDateEnd=date.getTime();
  	}
  	
    }
    return dataObject;
}





