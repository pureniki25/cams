/*
* 分配电催/催收界面  js
*
* */

var layer;
var table
//移交催收类型
var staffType = document.getElementById("staffType").getAttribute("value");
var crpIds = document.getElementById("crpIds").getAttribute("value");
var basePath;
var vm;



window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;

    getSelectsData();

    vm = new Vue({
        el: '#app',
        data: {
            loading: false,
            saving:false, //存储标志位
            submitLoading: false,
            searchForm: {
                returnStatus: '', //退回状态
                areaId: '',        //区域ID
                companyId:'',      //公司ID
                businessType:'',    //业务类型
                businessId:'',      //业务编号
                overDueDays:'',     //逾期天数
                status:'',          //状态
                customerName:'',     //客户名字
                operatorName:''     //业务获取人名字
            },
            areaList:area_array,   //区域列表
            companyList: company_array,  //公司列表
            businessTypeList:btype_array,   //业务类型列表
            statusList:status_array,        //状态列表
            returnStatusList:return_status_array, //退回状态列表
            followOprList:follow_opr_array,//跟进人员列表
            //提交内容
            setPhoneUrgeForm:{
                followOprId:'',     //跟进人员ID
                discript:''        //分配说明
            },
            ruleValidate:setPhoneUrgeFormValidate
        },
        methods: {
            toLoading() {
                if (table == null) return;
                this.loading = true;
                console.log(vm.searchForm);

                table.reload('listTable', {
                    where: {moduleName: vm.searchForm.moduleName, moduleLevel: vm.searchForm.moduleLevel}
                    , page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            },
            handleReset (name) {
                /*       vm.setPhoneUrgeForm.followOprId="";
                       vm.setPhoneUrgeForm.discript="";*/

                var tt =this.$refs[name];
                tt.resetFields();
                closePareantLayer()
            },
            submit() {
                vm.saving = true;
                submitFollowUsers();
                vm.saving = false;

                // editFormSubmit();
            }
            // changeModuleLevel: queryVaildParentModule
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
                    type: 'checkbox',
                    fixed: 'left',

                },{
                    field: 'businessId',
                    title: '业务编号',
                    width:100
                }, {
                    field: 'periods',
                    title: '期数',
                    width:80
                }, {
                    field: 'districtAreaName',
                    title: '区域',
                    width:80
                }, {
                    field: 'companyName',
                    title: '分公司',
                    width:100
                }, {
                    field: 'operatorName',
                    title: '业务获取',
                    width:100
                }, {
                    field: 'customerName',
                    title: '客户名称',
                    width:100
                }, {
                    field: 'businessTypeName',
                    title: '业务类型',
                    width:100
                }, {
                    field: 'borrowMoney',
                    title: '借款金额',
                    width:100
                }, {

                    field: 'totalBorrowAmount',
                    title: '应还金额',
                    width:100
                }, {

                    field: 'totalBorrowAmount',
                    title: '逾期',
                    width:100
                }, {

                    field: 'dueDate',
                    title: '应还日期',
                    width:100
                }, {

                    field: 'statusName',
                    title: '状态',
                    width:100
                }/*, {
                    fixed: 'right',
                    title: '操作',
                    width: 178,
                    align: 'left',
                    toolbar: '#barTools'
                }*/
            ]], //设置表头
            url:basePath+ 'collection/selectALStandingBookVoPage?crpIds='+crpIds,
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
        /*    table.on('tool(listTable)', function (obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    queryEditModule(data.moduleId)
                }
            });*/


    });

})


//从后台获取下拉框数据
var getSelectsData = function () {

    //取区域列表
    axios.get(basePath + 'collection/getPhoneUrgeSelectsData?staffType='+staffType)
        .then(function (res) {
            if (res.data.code == "1") {
                vm.areaList = res.data.data.area;
                vm.companyList = res.data.data.company;
                vm.businessTypeList = res.data.data.businessType;
                vm.followOprList = res.data.data.operators;

                // queryVaildParentModule();
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });


}

//表单验证配置参数
var validateOpt = {
    moduleName: [
        {required: true, message: '请输入菜单名称', trigger: 'blur'}
    ],
    moduleLevel: [
        {required: true, message: '请选择菜单级别', trigger: 'change'}
    ],
    parentModuleName: [
        {required: true, message: '请选择父级菜单', trigger: 'change'},
        {
            validator: function (rule, value, callback, source, options) {
                if (value != '1' && value == '') {
                    callback(new Error('请选择父级菜单'));
                } else {
                    callback();//校验通过
                }

            }, trigger: 'blur'
        }
    ],
    moduleUrl: [
        {required: true, message: '请输入菜单地址', trigger: 'blur'}
    ],
    deviceType: [
        {required: true, message: '请选择设备类型', trigger: 'blur'}
    ],
    moduleOrder: [
        {required: true, message: '请设定菜单排序', trigger: 'blur'}
    ]
};

//区域列表数组
var area_array = [{
    areaId: '1',
    areaName: '公司一'
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
    value: '1',
    label: '业务类型一'
}, {
    value: '2',
    label: '业务类型二'
}, {
    value: '3',
    label: '业务类型三'
}];
//状态列表数组
var status_array = [{
    value: '1',
    label: '状态一'
}, {
    value: '2',
    label: '状态二'
}, {
    value: '3',
    label: '状态三'
}];
//退回状态列表数组
var return_status_array = [{
    value: '1',
    label: '退回状态一'
}, {
    value: '2',
    label: '退回状态二'
}, {
    value: '3',
    label: '退回状态三'
}];

//分配人员列表
var follow_opr_array = [{
    oprId: '1',
    oprName: '跟进者一'
}, {
    oprId: '2',
    oprName: '跟进者二'
}, {
    oprId: '3',
    oprName: '跟进者三'
}];


//设置电催人员表单验证
var setPhoneUrgeFormValidate = {

    followOprId: [
        {required: true, message: '请选择清算-跟进人员', trigger: 'change'}
    ],
    discript: [
        {required: true, message: '请填写分配说明', trigger: 'blur'}
    ]
};



var closePareantLayer = function(){
    if (typeof (parent.layer) != 'undefined') {
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.layer.close(index);
    }

}



//提交催收人员信息
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
             axios.get(basePath + 'collection/setBusinessBusinessStaff',
                 {params: {
                     taffVoList:JSON.stringify(checkStatus.data).toString(),
                     staffUserId:vm.setPhoneUrgeForm.followOprId,
                     describe:vm.setPhoneUrgeForm.discript,
                     staffType:staffType
                 }})
                 .then(function (res) {
                     if (res.data.code == "1") {
                         vm.add_modal = true;
                         vm.toLoading();
                         // vm.$Modal.error({content:"保存成功"});
                         vm.$Modal.success({
                             // title: title,
                             content: "保存成功",
                             onOk: () => {
                                 // vm.$Message.info('Clicked ok');
                                 closePareantLayer()
                             },
                         });

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
}

