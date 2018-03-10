
/*
* 贷后管理首页 台账 js
*
* */
var layer;
var table
var basePath
var vm
//移交催收类型
// var staffType = document.getElementById("staffType").getAttribute("value");



function getMousePos(event) {
    var e =  window.event;
    return {'x':e.clientX,'y':e.clientY}
}

//从后台获取下拉框数据
var getSelectsData = function () {
    //取区域列表
    axios.get(basePath+'collection/getALStandingBookVoPageSelectsData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
                vm.businessStatusList = res.data.data.businessStatusList;
                vm.repayStatusList = res.data.data.repayStatusList;
                vm.collectLevelList = res.data.data.collectLevelList;

            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {

            vm.$Modal.error({content: '接口调用异常!'});
        });
}



window.layinit(function (ht_config) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    getSelectsData();
    vm = new Vue({
        el: '#app',
        data: {
            loading: false,
            saving:false, //存储标志位
            exporting:false,//导出标志位
            submitLoading: false,
            searchForm: {
                businessId:'',
                customerName:'',  //客户名称
                licensePlateNumber:'',
                model:'',
                areaId:'',  //区域ID
                companyId:'', //分公司ID
                trailerStartDate:'',//拖车开始日期
                trailerEndDate:'',//拖车结束日期

                businessStatus:''   //业务状态


            },
            areaList:'',//area_array,   //区域列表
            companyList: '',//company_array,  //公司列表
            businessTypeList:'',//btype_array,   //业务类型列表
            businessStatusList:'',//business_status_array,        //业务状态列表
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
                if (table == null) return;
                vm.$refs['searchForm'].validate((valid) =>{
                    if (valid) {
                        this.loading = true;
                        console.log(vm.searchForm);

                        //var dateObj = getData();

                        table.reload('listTable', {
                            where: {
                                businessId:vm.searchForm.businessId,//业务编号
                                customerName:vm.searchForm.customerName,  //客户名称
                                licensePlateNumber:vm.searchForm.licensePlateNumber,  //车牌号
                                edition:vm.searchForm.model,  //车辆型号
                                areaId:vm.searchForm.areaId,  //区域ID
                                companyId:vm.searchForm.companyId, //分公司ID
                                trailerStartDate:vm.searchForm.trailerStartDate,//拖车开始时间
                                trailerEndDate:vm.searchForm.trailerEndDate,//结束时间
                                businessStatus:vm.searchForm.status//状态

                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });

                    }

                    // this.loading = false;
                })
            },
            handleReset (name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },
            ////  ----   单行操作界面显示  结束 -----------------
            clickExport() {//导出Excel表格
                // layui.config({
                //     base: '/plugins/layui/extend/modules/',
                //     version: false
                // }).use(['layer', 'table','ht_config'], function () {
                //     config = layui.ht_config;
                    vm.$refs['searchForm'].validate((valid) => {

                        if (valid) {
                            vm.exporting = true;
                            //var dateObj = getData();
                            var ExportForm = document.createElement("FORM");
                            document.body.appendChild(ExportForm);
                            ExportForm.method = "POST";
                            ExportForm.action = basePath+"car/download";
                            ExportForm.target = "iframe";

                            addInput(ExportForm, "text", "areaId", vm.searchForm.areaId);//区域ID
                            addInput(ExportForm, "text", "companyId", vm.searchForm.companyId); //分公司ID
                            addInput(ExportForm, "text", "trailerStartDate", vm.searchForm.trailerStartDate); //拖车日期  开始
                            addInput(ExportForm, "text", "trailerEndDate", vm.searchForm.trailerEndDate);  //拖车日期 结束
                            addInput(ExportForm, "text", "businessId", vm.searchForm.businessId);    //业务编号
                            addInput(ExportForm, "text", "customerName", vm.searchForm.customerName);     //客户名称*/
                            addInput(ExportForm, "text", "licensePlateNumber", vm.searchForm.licensePlateNumber);     //车牌号*/
                            addInput(ExportForm, "text", "model", vm.searchForm.model);     //车辆型号*/
                            ExportForm.submit();
                            document.body.removeChild(ExportForm);

                            vm.exporting = false;
                        }
                    })
                // });
            }
        },
        mounted:function(){
            //使用layerUI的表格组件
            layui.config({
                base: '/plugins/layui/extend/modules/',
                version: false
            }).use(['layer', 'table','ht_config'], function () {
                // layer = layui.layer;
                // config = layui.ht_config;
                table = layui.table;
                //执行渲染
                table.render({
                    elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                    //, id: 'carList'
                    , height: 550 //容器高度
                    , cols: [[
                        {
                            type: 'checkbox',
                            fixed: 'left'
                        },{
                            field: 'businessId',
                            title: '业务编号'
                        }, {
                            field: 'districtAreaName',
                            title: '地区'
                        }, {
                            field: 'companyName',
                            title: '分公司'
                        },  {
                            field: 'customerName',
                            title: '客户名称'
                        }, {
                            field: 'borrowMoney',
                            title: '借款金额'
                        }, {

                            field: 'repaidAmount',
                            title: '应还金额'
                        }, {

                            field: 'licensePlateNumber',
                            title: '车牌号'
                        }, {

                            field: 'model',
                            title: '车辆型号'
                        }, {
                            field: 'evaluationAmount',
                            title: '评估价'
                        }, {
                            field: 'evaluationDate',
                            title: '评估日期'
                        }, {
                            field: 'trailerDate',
                            title: '拖车日期'
                        }, {

                            field: 'businessStatus',
                            title: '状态'
                        }, {
                            fixed: 'right',
                            title: '操作',
                            width: 178,
                            align: 'left',
                            toolbar: '#barTools'
                        }
                    ]], //设置表头
                    url: basePath+'car/carList',
                    //method: 'post' //如果无需自定义HTTP类型，可不加该参数
                    //request: {} //如果无需自定义请求参数，可不加该参数
                    //response: {} //如果无需自定义数据响应名称，可不加该参数
                    width: window.innerWidth - 30,
                    page: true,
                    done: function (res, curr, count) {
                        //alert($('#'+this.id).next().find(''));
                        var dom = $('#'+this.id).next();

                        dom.find('[lay-event="operate"]').optionsPopover({
                            title: "操作",
                            //disableHeader:false,
                            id:'operate',
                            tableid: 'listTable',
                            contents: [
                                {"name": "发起拍卖", click: function (e, currentItem) {

                                    showNewTab(currentItem,"carAuction","车牌号-车辆型号 拍卖申请");
                                }},
                                {"name": "转公车申请", click: function (e, currentItem) {
                                    showNewTab(currentItem,"convBusAply","车牌号-车辆型号 转公车申请（"+currentItem.companyName+"）");
                                }},
                                {"name": "拍卖登记", click: function (e, currentItem) {
                                    showNewTab(currentItem,"auctionReg","拍卖登记");
                                }},
                                {"name": "车辆归还登记", click: function (e, currentItem) {
                                    showNewTab(currentItem,"returnReg","车辆归还登记");
                                }},
                                {"name": "重新评估", click: function (e, currentItem) {
                                    showNewTab(currentItem,"againAssess","车辆评估");
                                }},
                                {"name": "查看附件", click: function (e, currentItem) {

                                }}
                            ],
                        });

                    }
                });

                //监听工具条
                table.on('tool(listTable)', function (obj) {
                    vm.selectedRowInfo = obj.data;
                    if (obj.event === 'edit') {
                        // vm.edit_modal = true;
                    }else  if(obj.event ==='info'){
                        layer.open({
                            title:'业务信息',
                            type: 2,
                            content: '/assets/car/carDetail?businessId='+obj.data.businessId,
                            area: ['95%', '95%']
                        });
                    }
                });
                showNewTab=function(currentItem,reqType,reqTypeName){

                    var url="/assets/car/"+reqType+"?businessId="+currentItem.businessId;
                    var openIndex= layer.open({
                        type: 2,
                        area: ['95%', '95%'],
                        fixed: false,
                        maxmin: true,
                        title:reqTypeName,
                        content: url
                    });
                }
            });
        }
    });
})




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
        {pattern: '^[0-9]*$', message: '请输入数字',trigger: 'blur'  },
    ],
    delayDaysEnd: [
        {pattern: '^[0-9]*$', message: '请输入数字',trigger: 'blur'  }
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




//
/*

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
*/

//为表单添加输入
var addInput = function(form, type,name,value){
    var input=document.createElement("input");
    input.type=type;
    input.name=name;
    input.value = value;
    form.appendChild(input);
}
//取查询的时间间隔
/**
var getData = function(){
    var dataObject ={
        showRepayDateBegin:'',
        showRepayDateEnd:'',
        realRepayDateBegin:'',
        realRepayDateEnd:''
    }
    if(vm.searchForm.showRepayDateRange.length>0){
        dataObject.showRepayDateBegin = vm.searchForm.showRepayDateRange[0].getTime();
        dataObject.showRepayDateEnd = vm.searchForm.showRepayDateRange[1].getTime();
    }
    if(vm.searchForm.realRepayDateRange.length>0){
        dataObject.realRepayDateBegin = vm.searchForm.realRepayDateRange[0].getTime();
        dataObject.realRepayDateEnd = vm.searchForm.realRepayDateRange[1].getTime();
    }
    return dataObject;
}
**/


//导出Excel文档
var downloadExcel = function (href, title) {
    const a = document.createElement('a');
    a.setAttribute('href', href);
    a.setAttribute('download', title);
    a.click();
}




/*//提交催收人员信息
var submitFollowUsers = function () {
    var checkStatus = table.checkStatus('listTable');

    if(!checkStatus.data.length>0){
        vm.$Message.error({content: '请选择需要分配的贷款!'});
        return;
    }
    // console.log(JSON.stringify(checkStatus.data).toString())
    // JSON.stringify();
    vm.$refs['setPhoneUrgeForm'].validate((valid) =>{
        if (valid) {
            axios.get('/core/collection/setBusinessBusinessStaff',
                {params: {
                    phoneStaffVoList:JSON.stringify(checkStatus.data).toString(),
                    phoneStaffUserId:vm.setPhoneUrgeForm.followOprId,
                    describe:vm.setPhoneUrgeForm.discript,
                    staffType:staffType
                }})
                .then(function (res) {
                    if (res.data.code == "1") {
                        vm.add_modal = true;
                        vm.toLoading();
                        vm.$Modal.error({content:"保存成功"});
                    } else {
                        vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                    }

                })
                .catch(function (error) {
                    vm.saving = false;
                    vm.$Modal.error({content: '接口调用异常!' + error});
                });
        } else {
            vm.$Message.error({content: '表单校验失败!'});
        }

    });



console.log(checkStatus.data.toString()) //获取选中行的数据
console.log(checkStatus.data.length) //获取选中行数量，可作为是否有选中行的条件
console.log(checkStatus.isAll ) //表格是否全选
}*/



