
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


//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath + 'collection/getALStandingBookVoPageSelectsData')
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


// 获取公共layerui配置
window.layinit(function(htConfig){
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

                        var dateObj = getData();

                        table.reload('listTable', {
                            where: {
                                areaId:vm.searchForm.areaId,  //区域ID
                                companyId:vm.searchForm.companyId, //分公司ID

                                showRepayDateBegin:dateObj.showRepayDateBegin, //应还日期  开始
                                showRepayDateEnd:dateObj.showRepayDateEnd,  //应还日期 结束
                                // showRepayDateRange:vm.searchForm.showRepayDateRange,//应还日期 区间 包含开始和结束时间
                                realRepayDateBegin:dateObj.realRepayDateBegin, //实还日期 开始
                                realRepayDateEnd:dateObj.realRepayDateEnd, //实还日期 结束
                                // realRepayDateRange:vm.searchForm.realRepayDateRange,//实还日期  区间 包含开始和结束时间

                                delayDaysBegin:vm.searchForm.delayDaysBegin,    //逾期天数 开始
                                delayDaysEnd:vm.searchForm.delayDaysEnd, //逾期天数 结束
                                collectLevel:vm.searchForm.collectLevel,  //催收级别
                                operatorName:vm.searchForm.operatorName,  //业务获取
                                businessId:vm.searchForm.businessId,  //业务编号
                                businessType:vm.searchForm.businessType,  //业务类型
                                liquidationOne:vm.searchForm.liquidationOne, //清算一
                                liquidationTow:vm.searchForm.liquidationTow,  //清算二
                                businessStatus:vm.searchForm.businessStatus,   //业务状态
                                repayStatus:vm.searchForm.repayStatus,      //还款状态
                                customerName:vm.searchForm.customerName  //客户名称
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
            showSetPhoneStaff (name) {//显示分配电催人员 界面



                layer.open({
                    type: 2,
                    title: '分配电催',
                    area: ['1250px', '800px'],
                    content: '/collectionUI/setPhoneStaffUI?crpIds='+getSelectedcrpIds()
                });
                /*
                            var tt =this.$refs[name];
                            tt.resetFields();
                */
            },
            //显示移交催收人员 界面
            showSetVisitStaff (name) {
                layer.open({
                    type: 2,
                    title: '移交催收',
                    area: ['1250px', '800px'],
                    content: '/collectionUI/setVisitStaffUI?crpIds='+getSelectedcrpIds()
                });

            },
            //显示委外受理 界面
/*            showSetOutsideCommission (name) {
                /!*          var tt =this.$refs[name];
                          tt.resetFields();*!/
            },*/
            //显示结清试算 界面
/*            showTestCalculation (name) {
                /!*            var tt =this.$refs[name];
                            tt.resetFields();*!/
            },*/
            ////  ----   单行操作界面显示  开始 -----------------
            /*//显示 催收跟踪记录界面
            showCollectionStackLog(){
                vm.edit_modal = false;
                layer.open({
                    type: 2,
                    title: '催收跟踪记录',
                    area: ['1250px', '800px'],
                    content: '/collectionUI/staffTrackRecordUI?businessId='+vm.selectedRowInfo.businessId+'&crpId='+vm.selectedRowInfo.crpId
                });
            },
            //显示 催收跟踪记录界面
            showApplyDerate(){
                vm.edit_modal = false;
                layer.open({
                    type: 2,
                    title: '减免申请',
                    maxmin: true,
                    area: ['1250px', '800px'],
                    content: '/collectionUI/applyDerateUI?businessId='+vm.selectedRowInfo.businessId+'&crpId='+vm.selectedRowInfo.crpId+"&processStatus=-1"
                });
            },
        showCarLoanData(){
            vm.edit_modal = false;
            layer.open({
                type: 2,
                title: '车贷移交诉讼',
                maxmin: true,
                area: ['1250px', '800px'],
                content: '/transferOfLitigation/carLoan?businessId=' + vm.selectedRowInfo.businessId + "&processStatus=-1"+'&crpId='+vm.selectedRowInfo.crpId
            });
        },
        showHouseLoanData(){
        	vm.edit_modal = false;
        	layer.open({
        		type: 2,
        		title: '房贷移交诉讼',
        		maxmin: true,
        		area: ['1250px', '800px'],
        		content: '/transferOfLitigation/houseLoan?businessId=' + vm.selectedRowInfo.businessId + "&processStatus=-1"+'&crpId='+vm.selectedRowInfo.crpId
        	});
        },

            showDeduction(){
                vm.edit_modal = false;
                url = getDeductionUrl();
                layer.open({
                    type: 2,
                    title: '执行代扣',
                    maxmin: true,
                    area: ['1450px', '800px'],
                    content:url
                });
            },

            //显示 拖车登记界面
            showdragRegistration(){
                var title = "拖车登记"
                var url = '/carUI/dragRegistration?businessId='+vm.selectedRowInfo.businessId
                showOneLineOprLayer(title,url)
            },
            //显示 还款登记界面
            showRepaymentRegister(){
                var title = "还款登记"
                var url = '/collectionUI/repaymentRegister?businessId='+vm.selectedRowInfo.businessId+'&afterId='+vm.selectedRowInfo.afterId
                showOneLineOprLayer(title,url)
            },
            //显示 查看款项池界面
            showCheckFundPool(){
                var title = "查看款项池"
                var url = '/collectionUI/checkFundPool?businessId='+vm.selectedRowInfo.businessId+'&afterId='+vm.selectedRowInfo.afterId
                showOneLineOprLayer(title,url)
            },*/

            ////  ----   单行操作界面显示  结束 -----------------
            clickExport() {//导出Excel表格
                vm.$refs['searchForm'].validate((valid) => {
                    if (valid) {

                        vm.exporting = true;
                        expoertExcel(basePath + "collection/saveExcel",vm.searchForm);

                        vm.exporting = false;

                    }
                })
            }
        },
        mounted:function(){
//使用layerUI的表格组件

        }
    });

    //单行操作弹框显示
    var showOneLineOprLayer = function(url,title){
        // vm.edit_modal = false;
        var openIndex= layer.open({
            type: 2,
            area: ['95%', '95%'],
            fixed: false,
            maxmin: true,
            title:title,
            content: url
        });


        // layer.open({
        //     type: 2,
        //     title: title,
        //     maxmin: true,
        //     // area: ['1450px', '800px'],
        //     // area: ['95%', '95%'],
        //     content:url
        // });
    }


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
                    type: 'checkbox',
                    fixed: 'left'
                },{
                    field: 'businessId',
                    title: '业务编号'
                }, {
                    field: 'afterId',
                    title: '期数'
                }, {
                    field: 'districtAreaName',
                    title: '区域'
                }, {
                    field: 'companyName',
                    title: '分公司'
                }, {
                    field: 'operatorName',
                    title: '业务获取'
                }, {
                    field: 'customerName',
                    title: '客户名称'
                }, {
                    field: 'businessTypeName',
                    title: '业务类型'
                }, {
                    field: 'borrowMoney',
                    title: '借款金额'
                }, {
                    field: 'totalBorrowAmount',
                    title: '应还金额'
                }, {

                    field: 'delayDays',
                    title: '逾期'
                }, {

                    field: 'dueDate',
                    title: '应还日期'
                }, {
                    field: 'repaymentDate',
                    title: '实还日期'
                }, {
                    field: 'phoneStaffName',
                    title: '清算一'
                }, {
                    field: 'visitStaffName',
                    title: '清算二'
                }, {

                    field: 'statusName',
                    title: '还款状态'
                },{

                    field: 'afterColStatusName',
                    title: '业务状态'
                },{
                    fixed: 'right',
                    title: '操作',
                    width: 178,
                    align: 'left',
                    toolbar: '#barTools'
                }
            ]], //设置表头
            url: basePath + 'collection/selectALStandingBookVoPage',
            //method: 'post' //如果无需自定义HTTP类型，可不加该参数
            //request: {} //如果无需自定义请求参数，可不加该参数
            //response: {} //如果无需自定义数据响应名称，可不加该参数
            page: true,
            done: function (res, curr, count) {
                console.log(res);
                //数据渲染完的回调。你可以借此做一些其它的操作
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                vm.loading = false;

                var   buttons = [];

                buttons.push(
                    {"name": "贷后跟踪记录", click: function (e, currentItem) {
                    var url = "/collectionUI/staffTrackRecordUI?businessId="+currentItem.businessId+"&crpId="+currentItem.crpId
                    showOneLineOprLayer(url,"贷后跟踪记录");
                    }}
                )
                buttons.push(
                    {"name": "减免申请", click: function (e, currentItem) {
                    var url = '/collectionUI/applyDerateUI?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1"
                    showOneLineOprLayer(url,"减免申请")
                    }}
                )
                buttons.push(
                    {"name": "执行代扣", click: function (e, currentItem) {
                        var url = getDeductionUrl(currentItem);
                        // var url = '/collectionUI/applyDerateUI?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1"
                        showOneLineOprLayer(url,"")
                    }}
                )
                // if(obj.data.businessTypeId == 9 || obj.data.businessTypeId == 1){
                    buttons.push(
                        {"name": "拖车登记", click: function (e, currentItem) {
                            // url = getDeductionUrl(currentItem);
                            if(currentItem.businessTypeId != 9 && currentItem.businessTypeId != 1){
                                vm.$Modal.error({content: '车贷业务才能拖车登记！'});
                            }else{
                                var url = '/carUI/dragRegistration?businessId='+currentItem.businessId
                                showOneLineOprLayer(url,"拖车登记")
                            }
                        }}
                    )
                // }
                buttons.push(
                    {"name": "客户还款登记", click: function (e, currentItem) {
                        var url = '/collectionUI/repaymentRegister?businessId='+currentItem.businessId+'&afterId='+currentItem.afterId
                        showOneLineOprLayer(url,"客户还款登记")
                    }}
                )
                buttons.push(
                    {"name": "查看款项池", click: function (e, currentItem) {
                        var url = '/collectionUI/checkFundPool?businessId='+currentItem.businessId+'&afterId='+currentItem.afterId
                        showOneLineOprLayer(url,"查看款项池")
                    }}
                )
                buttons.push(
                    {"name": "查看附件", click: function (e, currentItem) {
                        $.ajax({
                            type : 'GET',
                            async : false,
                            // url : "http://10.110.1.240:30111/uc/auth/loadBtnAndTab",
                            // url : "http://localhost:30111/uc/auth/loadBtnAndTab",
                            url : basePath  +"api/getXindaiThumbnailView?businessId="+currentItem.businessId,
                            headers : {
                                app : 'ALMS',
                                Authorization : "Bearer " + getToken()
                            },
                            success : function(data) {
                                showOneLineOprLayer(data,"查看附件");
                            },
                            error : function() {
                                vm.$Modal.error({content: '接口调用异常!'});
                            }
                        });
                    }}
                )
                buttons.push(
                    {"name": "上传附件", click: function (e, currentItem) {
                        var url = '/page/doc/upLoad.html?businessId'+currentItem.businessId
                        showOneLineOprLayer(url,"上传附件")
                    }}
                )
                buttons.push(
                    {"name": "结清试算", click: function (e, currentItem) {
                        // var url = '/transferOfLitigation/carLoanBilling?businessId='+currentItem.businessId
                         var url = ''
                         if(currentItem.businessTypeId == 1 || currentItem.businessTypeId == 9){
                             url = '/transferOfLitigation/carLoanBilling?businessId='+currentItem.businessId;
                            }else if(currentItem.businessTypeId == 2 || currentItem.businessTypeId == 11){
                             url = '/expenseSettle/houseLoan?businessId='+currentItem.businessId;
                         }
                        showOneLineOprLayer(url,"结清试算")
                    }}
                )
                buttons.push(
                    {"name": "移交诉讼系统", click: function (e, currentItem) {
//                            	 var url = '/transferOfLitigation/carLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1"
                        var url = ''
                        if(currentItem.businessTypeId == 1 || currentItem.businessTypeId == 9){
                            url = '/transferOfLitigation/carLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
                        }else if(currentItem.businessTypeId == 2 || currentItem.businessTypeId == 11){
                            url = '/transferOfLitigation/houseLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
                        }
                        showOneLineOprLayer(url,"移交诉讼系统")
                    }}
                )
                /*

                    {"name": "移交诉讼系统", click: function (e, currentItem) {
//                            	 var url = '/transferOfLitigation/carLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1"
                        var url = ''
                        if(currentItem.businessTypeId == 1 || currentItem.businessTypeId == 9){
                            url = '/transferOfLitigation/carLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
                        }else if(currentItem.businessTypeId == 2 || currentItem.businessTypeId == 11){
                            url = '/transferOfLitigation/houseLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
                        }
                        showOneLineOprLayer(url,"移交诉讼系统")
                    }}
                ];*/



                    //alert($('#'+this.id).next().find(''));
                    var dom = $('#'+this.id).next();

                    dom.find('[lay-event="operate"]').optionsPopover({
                        title: "操作",
                        //disableHeader:false,
                        id:'operate',
                        tableid: 'listTable',
                        contents: buttons,
                    })
            }
        });
        
//        var getTransferLitigationUrl = function(obj) {
//        	var url = '';
//        	if(obj.data.businessTypeId == 1 || obj.data.businessTypeId == 9){
//        		 url = '/transferOfLitigation/carLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
//             }else if(obj.data.businessTypeId == 2 || obj.data.businessTypeId == 11){
//            	 url = '/transferOfLitigation/houseLoan?businessId='+currentItem.businessId+'&crpId='+currentItem.crpId+"&processStatus=-1";
//             }
//        	return url;
//        }

        //监听工具条
        table.on('tool(listTable)', function (obj) {
            // vm.selectedRowInfo = obj.data;
            if (obj.event === 'edit') {
                // vm.edit_modal = true;

            }else  if(obj.event ==='info'){
                if(obj.data.businessTypeId == 9 || obj.data.businessTypeId == 1){
                   //车贷  车贷展期
                    axios.get(basePath + 'api/getXindaiCarView?businessId ='+obj.data.businessId)
                        .then(function (res) {
                            if (res.data.code == "1") {
                                showOneLineOprLayer(res.data.data,"车贷详情");
                            } else {
                                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                            }
                        })
                        .catch(function (error) {
                            vm.$Modal.error({content: '接口调用异常!'});
                        });
                }else if(obj.data.businessTypeId == 11 || obj.data.businessTypeId == 2){
                    //房贷
                    axios.get(basePath + 'api/getXindaiHouseView?businessId ='+obj.data.businessId)
                        .then(function (res) {
                            if (res.data.code == "1") {
                                showOneLineOprLayer(res.data.data,"房贷详情");
                            } else {
                                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                            }
                        })
                        .catch(function (error) {
                            vm.$Modal.error({content: '接口调用异常!'});
                        });
                }

                // vm.menu_modal = false;
            }
        });



    });

})

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
    delayDaysBegin: [
        {pattern: '^[0-9]*$', message: '请输入数字',trigger: 'blur'  },
        {
            validator: function (rule, value, callback, source, options) {
                if(vm.searchForm.delayDaysBegin!=""&& vm.searchForm.delayDaysEnd!=""){
                    if (parseInt(vm.searchForm.delayDaysBegin) > parseInt(vm.searchForm.delayDaysEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        callback();//校验通过
                    }
                }else{
                    callback();
                }

            }, trigger: 'blur'
        }
    ],
    delayDaysEnd: [
        {pattern: '^[0-9]*$', message: '请输入数字',trigger: 'blur'  },
        {
            validator: function (rule, value, callback, source, options) {
                if(vm.searchForm.delayDaysBegin!=""&& vm.searchForm.delayDaysEnd!=""){
                    if (parseInt(vm.searchForm.delayDaysBegin) > parseInt(vm.searchForm.delayDaysEnd)) {
                        callback(new Error('起始值大于结束值'));
                    } else {
                        callback();//校验通过
                    }
                }else{
                    callback();
                }

            }, trigger: 'blur'
        }
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
    ]
};






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

//取查询的时间间隔
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



//导出Excel文档
var downloadExcel = function (href, title) {
    const a = document.createElement('a');
    a.setAttribute('href', href);
    a.setAttribute('download', title);
    a.click();
}


//获取执行代扣的URL路径
function getDeductionUrl(currentItem){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'DeductionController/selectDeductionInfoByPlayListId?planListId='+currentItem.crpId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        }, 
        success : function(data) {
            var deduction = data.data;
            url =  '/collectionUI/deductionUI?planListId='+currentItem.crpId
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

