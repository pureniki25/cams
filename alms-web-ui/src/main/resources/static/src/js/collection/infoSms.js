
/*
* 短信查询页面
*
* */
var layer;
var table;
var basePath;
var vm;
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
            	  companyId:'', //分公司ID
            	keyName      	:'',      //业务编号 或客户名称
            	smsType  			:'',  //短信类型
                sendDateRange		:'',	//发送时间范围
                status	:'',    //状态
                recipient:''//短信接收人
            },
            ruleValidate:setSearchFormValidate, //表单验证


            loading: false, //查询标志位
            saving:false, //存储标志位
            exporting:false,//导出标志位
            submitLoading: false,
            companyList:[],
            smsTypeList:[],

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
                                companyId:vm.searchForm.companyId, //分公司ID
                                keyName:vm.searchForm.keyName,   //业务编号 或客户名称
                                smsType:vm.searchForm.smsType,  //类型ID
                                status:vm.searchForm.status,
                                sendDateBegin:dateObj.sendDateBegin, //发送时间  开始
                                sendDateEnd:dateObj.sendDateEnd, //发送时间 结束
                                recipient:vm.searchForm.recipient//短信接收人

                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });

                    }

                    // this.loading = false;
                })
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
                    field: 'originalBusinessId',
                    title: '业务编号'
                }, {
                    field: 'afterId',
                    title: '期数'
                },{
                    field: 'recipient',
                    title: '短信接收人'
                }, {
                    field: 'phoneNumber',
                    title: '接收短信手机号码'
                }, {
                    field: 'smsType',
                    title: '短信类型'
                },{
                    field: 'sendDate',
                    title: '发送时间'
                },
                {
                    field: 'companyId',
                    title: '分公司'
                },
                {
                    field: 'status',
                    title: '状态'
                },
                
                {
                        fixed: 'right',
                        title: '操作',
                        width: 178,
                        align: 'left',
                        toolbar: '#barTools'
                    }
            ]], //设置表头
            url: basePath +'InfoController/selectInfoSmsVoPage',
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
            if (obj.event === 'business') {debugger
                if(obj.data.businessTypeId == 9 || obj.data.businessTypeId == 1){
                    //车贷  车贷展期
                     axios.get(basePath + 'api/getXindaiCarView?businessId='+obj.data.originalBusinessId)
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
                     axios.get(basePath + 'api/getXindaiHouseView?businessId='+obj.data.originalBusinessId)
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

            }else  if(obj.event ==='info'){
                    title ="短信详情";
                    url = getInfoSmsDetailUrl();

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

//获取短信详情的URL路径
function getInfoSmsDetailUrl(){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'InfoController/getInfoSmsDetailById?logId='+vm.selectedRowInfo.logId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            var infoSmsDetail = data.data;
            url =  '/infoUI/infoSmsDetailUI?originalBusinessId='+vm.selectedRowInfo.originalBusinessId+'&afterId='+infoSmsDetail.afterId+"&recipient="+vm.selectedRowInfo.recipient+"&phoneNumber="+vm.selectedRowInfo.phoneNumber+"&sendDate="+vm.selectedRowInfo.sendDate+"&status="+vm.selectedRowInfo.status+"&content="+infoSmsDetail.content+"&logId="+vm.selectedRowInfo.logId
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
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ],
    derateMoneyEnd: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ]
};






//取查询的时间间隔
var getData = function(){debugger

    var dataObject ={
        derateDateBegin:'',
        derateDateEnd:'',
    }
    if(vm.searchForm.sendDateRange.length>0){
    	if(vm.searchForm.sendDateRange[0]!=null){
    		   dataObject.sendDateBegin = vm.searchForm.sendDateRange[0].getTime();
    	}
    	if(vm.searchForm.sendDateRange[1]!=null){
      	   var date =vm.searchForm.sendDateRange[1];
           date.setDate(date.getDate() + 1);
           dataObject.sendDateEnd=date.getTime();
    	}

    }
    return dataObject;
}





