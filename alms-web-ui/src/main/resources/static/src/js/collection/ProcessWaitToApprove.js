
/*
* 贷后管理首页 台账 js
*
* */
var layer;
var table;
var basePath;
var vm;
//我的审批  界面类型 （waitToApprove 需要我审批的;
// *                     Approved 我已审批的;
// *                     SelfStart 我发起的;
// *                     CopySendToMe 抄送我的;  Search 审批查询;）
var reqPageeType = document.getElementById("reqPageeType").getAttribute("value");  //界面类型

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath=_htConfig.coreBasePath;

    //获取下拉选择菜单的数据
    getSelectsData();

    vm = new Vue({
        el: '#app',
        data: {
            loading: false,

            searchForm: {
                keyWord      		:'',   //关键字  (标题、业务编号、客户名称、发起人)
                createTimeBegin		:'',	//发起时间  开始
                createTimeEnd		:'',	//发起时间   结束
                createTimeRange		:'',	//发起时间   范围
                finishTimeBegin		:'',	//结束时间  开始
                finishTimeEnd		:'',	//结束时间   结束
                finishTimeRange		:'',	//结束时间   范围
                processTypeId		:'' ,	//审批类型,
                processStatus		:'', 	//审批状态,
                companyId		:'' 	//分公司

            },
            processTypeList:[]   ,        //审批类型列表
            processStatusList:[],        //审批状态列表
            companyList:[],          //分公司列表
            submitLoading: false,



            selectedRowInfo:'',//存储当前选中行信息

            ruleValidate:setSearchFormValidate //表单验证

        },
        methods: {
            //重载表格
            toLoading() {
                if (table == null) return;
                vm.$refs['searchForm'].validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        console.log(vm.searchForm);

                        var dateObj = getData();

                        table.reload('listTable', {
                            where: {

                                keyWord: vm.searchForm.keyWord,   //关键字  (标题、业务编号、客户名称、发起人)
                                createTimeBegin: dateObj.createTimeBegin,	//发起时间  开始
                                createTimeEnd: dateObj.createTimeEnd,	//发起时间   结束
                                processTypeId: vm.searchForm.processTypeId,	//流程类型

                                finishTimeBegin:dateObj.finishTimeBegin,  //结束时间 开始
                                finishTimeEnd:dateObj.finishTimeEnd,   //结束时间   结束

                                processStatus:vm.searchForm.processStatus,  //审批状态,
                                companyId:vm.searchForm.companyId,  //分公司

                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });

                    }

                    // this.loading = false;
                })
            },
            handleReset(name) { // 重置表单
                var tt = this.$refs[name];
                tt.resetFields();
                vm.toLoading();
            },
            openModal(){
                if(vm.selectedRowInfo.processTypeCode=="derate"){
                    title ="减免申请";
                    url = getDerateProcessUrl();
                }else if (vm.selectedRowInfo.processTypeCode=="houseLoanLitigation") {
                    title ="房贷移交法务申请";
                    url = getHouseLoanProcessUrl();
                }else if (vm.selectedRowInfo.processTypeCode=="carLoanLitigation") {
                    title ="车贷移交法务申请";
                    url = getCarLoanProcessUrl();
                }else if(vm.selectedRowInfo.processTypeCode=="carAuctionAply"){
                    title ="车辆拍卖申请审核";
                    url = '/assets/car/carAuctionAudit?businessId='+vm.selectedRowInfo.businessId+"&processStatus="+vm.selectedRowInfo.status+"&processId="+vm.selectedRowInfo.processId;
                }

                layer.open({
                    type: 2,
                    title: title,
                    maxmin: true,
                    area: ['80%', '80%'],
                    content:url
                });
            }
        },

        mounted:function(){
            //使用layerUI的表格组件
            layui.use(['layer', 'table','ht_ajax', 'ht_auth', 'ht_config'], function () {
                layer = layui.layer;
                table = layui.table;
                // var config = layui.ht_config;
                // basePath = config.basePath;

                var  cols = [
                    {
                        field: 'processName',
                        title: '流程名称'
                    },{
                        field: 'businessId',
                        title: '业务编号',
                        width:200
                    }, {
                        field: 'customerName',
                        title: '客户名称'
                    }
                ];
                if(reqPageeType == "Search" ){
                    cols.push(
                        {
                            field: 'companyName',
                            title: '所属分公司'
                        }
                    )
                }
                
                
                cols.push(
                    {
                        field: 'createUserName',
                        title: '发起人'
                    }
                )
                cols.push(
                    {
                        field: 'createTime',
                        title: '发起时间'
                    }
                )
                
                if(reqPageeType == "Approved"||reqPageeType == "SelfStart"||reqPageeType =="CopySendToMe" ){
                    cols.push(
                            {
                                field: 'finishTime',
                                title: '完成时间'
                            }
                        )
                }
                cols.push(
                    {
                        field: 'processTypeName',
                        title: '审批类型'
                    }
                )
                cols.push(
                    {
                        field: 'processStatus',
                        title: '审批状态'
                    }
                )
                cols.push(
                    {
                        fixed: 'right',
                        title: '操作',
                        width: 178,
                        align: 'left',
                        // toolbar: '#barTools',
                        templet:function(d){
                            let sp = '<a class="layui-btn layui-btn-normal layui-btn-xs " lay-event="info" @click="openModal" >审批</a>' 
                            let dt = '<a class="layui-btn layui-btn-normal layui-btn-xs " lay-event="info" @click="openModal" >详情</a>' 
                            let url = window.location.href 
                            if(url.indexOf('waitToApprove')>-1){
                                return sp ;
                            }else{
                                return dt ;
                            }
                        }

                    }
                )
                var cols1 = [cols];

                //执行渲染
                table.render({
                    elem: '#listTable' //指定原始表格元素选择器（推荐id选择器）
                    , id: 'listTable'
                    , height: 550 //容器高度
                    , cols:cols1, //设置表头
                    url: basePath +'processController/selectProcessWaitToApproveVoPage?reqPageeType='+reqPageeType,
                    //method: 'post' //如果无需自定义HTTP类型，可不加该参数
                    request: {}, //如果无需自定义请求参数，可不加该参数
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
                    var title;
                    var url;
                    if(obj.event ==='info'){
                        if(vm.selectedRowInfo.processTypeCode=="derate"){
                            title ="减免申请";
                            url = getDerateProcessUrl();
                        }else if (vm.selectedRowInfo.processTypeCode=="houseLoanLitigation") {
                        	title ="房贷移交法务申请";
                            url = getHouseLoanProcessUrl();
						}else if (vm.selectedRowInfo.processTypeCode=="carLoanLitigation") {
                        	title ="车贷移交法务申请";
                            url = getCarLoanProcessUrl();
						}else if(vm.selectedRowInfo.processTypeCode=="carAuctionAply"){
							title ="车辆拍卖申请审核";
                            url = '/assets/car/carAuctionAudit?businessId='+vm.selectedRowInfo.businessId+"&processStatus="+vm.selectedRowInfo.status+"&processId="+vm.selectedRowInfo.processId;
						}

                        layer.open({
                            type: 2,
                            title: title,
                            maxmin: true,
                            area: ['80%', '80%'],
                            content:url
                        });
                    }
                });

            });
        }
    });

})

//设置申请减免的url路径
function getDerateProcessUrl(){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'ApplyDerateController/getApplyDerateInfoByProcessId?processId='+vm.selectedRowInfo.processId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            var applyDerateProcess = data.data;
            url =  '/collectionUI/applyDerateUI?businessId='+vm.selectedRowInfo.businessId+'&crpId='+applyDerateProcess.crpId+"&processStatus="+vm.selectedRowInfo.status+"&processId="+vm.selectedRowInfo.processId+"&businessTypeId="+vm.selectedRowInfo.businessTypeId
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

//设置房贷移交法务申请的url路径
function getHouseLoanProcessUrl(){
	var url
	$.ajax({
		type : 'GET',
		async : false,
		url : basePath +'transferOfLitigation/getTransferLitigationHouseByProcessId?processId='+vm.selectedRowInfo.processId,
		headers : {
			app : 'ALMS',
			Authorization : "Bearer " + getToken()
		},
		success : function(data) {
			var applyDerateProcess = data.data;
			url =  '/transferOfLitigation/houseLoan?businessId='+vm.selectedRowInfo.businessId+"&processStatus="+vm.selectedRowInfo.status+"&processId="+vm.selectedRowInfo.processId
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

//设置车贷移交法务申请的url路径
function getCarLoanProcessUrl(){
	var url
	$.ajax({
		type : 'GET',
		async : false,
		url : basePath +'transferOfLitigation/getTransferLitigationCarByProcessId?processId='+vm.selectedRowInfo.processId,
		headers : {
			app : 'ALMS',
			Authorization : "Bearer " + getToken()
		},
		success : function(data) {
			var applyDerateProcess = data.data;
			url =  '/transferOfLitigation/carLoan?businessId='+vm.selectedRowInfo.businessId+"&processStatus="+vm.selectedRowInfo.status+"&processId="+vm.selectedRowInfo.processId
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

///////////   控制搜索选项显示 开始  /////////////////
//控制  审批状态  显示
function showPType(){
    //我已审批的：  我发起的： 抄送我的：  三个界面显示
    if(reqPageeType == "Approved" || reqPageeType=="SelfStart" || reqPageeType=="CopySendToMe"){
        return true;
    }
}
// 控制  完成时间 显示
function showFinishTime(){
    //我已审批的：  我发起的： 抄送我的：  三个界面显示
    if(reqPageeType == "Approved" || reqPageeType=="SelfStart" || reqPageeType=="CopySendToMe"){
        return true;
    }
}

// 控制  分公司 显示
function showCompany(){
    //审批查询：  界面显示
    if(reqPageeType == "Search" ){
        return true;
    }
}

///////////   控制搜索选项显示 结束  /////////////////

//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath +'processController/getProcessVoPageSelectsData')
        .then(function (res) {
            if (res.data.code == "1") {
                vm.processTypeList = res.data.data.processTypes;
                vm.processStatusList = res.data.data.processStatusList;
                vm.companyList = res.data.data.companyList;

            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}


//查询表单验证
var setSearchFormValidate = {
/*    delayDaysBegin: [
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
    ]*/
};



//

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
var getData = function(condition){

    var dataObject ={
        createTimeBegin:null,
        createTimeEnd:null,
        finishTimeBegin:null,
        finishTimeEnd:null,
    }
    if(vm.searchForm.createTimeRange.length>0){
    	if(vm.searchForm.createTimeRange[0]!=null){debugger
    		 dataObject.createTimeBegin = vm.searchForm.createTimeRange[0].getTime();
    	}
    	if(vm.searchForm.createTimeRange[1]!=null){
//   		 dataObject.createTimeEnd = vm.searchForm.createTimeRange[1].getTime();
   	   var date =vm.searchForm.createTimeRange[1];
       date.setDate(date.getDate() + 1);
       dataObject.createTimeEnd=date.getTime();
   	}
    }
    if(vm.searchForm.finishTimeRange.length>0){
        if(vm.searchForm.finishTimeRange[0]!=null){
            dataObject.finishTimeBegin = vm.searchForm.finishTimeRange[0].getTime();
        }
        if(vm.searchForm.finishTimeRange[1]!=null){
            
            var date =vm.searchForm.finishTimeRange[1];
            date.setDate(date.getDate() + 1);
            dataObject.finishTimeEnd=date.getTime();
        }

    }

    return dataObject;
}






