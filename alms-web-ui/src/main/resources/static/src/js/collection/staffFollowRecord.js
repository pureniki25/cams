// //引入vue
// document.write("<script type=\"text/javascript\" src=\"/plugins/vue/vue.min.js\"></script>");
// //iview js
// document.write("<script type=\"text/javascript\" src=\"/plugins/iview/js/iview.js\"></script>");
// //引入layui.js
// document.write("<script src=\"/plugins/layui/layui.js\"></script>");
// //引入axios.js 用于ajax调用
// document.write("<script src=\"/plugins/axios/axios.min.js\"></script>");
/*
* 催收跟踪记录界面 js
* */

var layer;
var table
var basePath;
var vm;

var businessId = document.getElementById("businessId").getAttribute("value");
var crpId = document.getElementById("crpId").getAttribute("value");

window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;

    getBusinessData();

    vm = new Vue({
        el: '#app',
        data: {
            // loading: false,
            // saving:false, //存储标志位
            submitLoading: false,
            businessInfForm: editLogFormDefaultVal,


            statusList:status_array,        //状态列表

            addLogModal:false,//控制增加、修改框显示的标志位
            //编辑跟踪历史记录
            editLogForm:{
                trackLogId:'',//跟踪记录ID
                status:'', //状态
                trackStatusId:'',//状态ID
                trackStatusName:'',//状态名
                ifSendToPlat:'', //是否提交平台
                content:'', //跟踪记录描述
                isSend:'',//是否提交平台 int标志位
                rbpId:''//还款计划ID
            },
            ruleValidate:setFormValidate
        },
        methods: {
            toLoading() {
                if (table == null) return;

                table.reload('listTable', {
                    where: {}
                    , page: {
                        curr: 1 //重新从第 1 页开始
                    }
                });
            },
            handleReset (name) {
                var tt =this.$refs[name];
                tt.resetFields();
            },
            submit() {
                editLogFormSubmit();

            },
            hideEditForm() {
                this.addLogModal = false;
                this.$refs['editLogForm'].resetFields();
                Object.assign(this.editLogForm, editLogFormDefaultVal)

            },
            toShowEditModal(){
                this.addLogModal = true;
                this.editLogForm.ifSendToPlat = '否'
            }
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
                {field: 'sortId', title: '序号', width:80, sort: true, fixed: 'left'},
                {
                    field: 'periods',
                    title: '期数'
                }, {
                    field: 'recordDate',
                    title: '记录日期'
                }, {
                    field: 'userName',
                    title: '记录者'
                }, {
                    field: 'fields',
                    title: '附件'
                }, {
                    field: 'trackStatusName',
                    title: '状态'
                }, {
                    field: 'content',
                    title: '记录内容'
                }, {
                    fixed: 'right',
                    title: '操作',
                    width: 178,
                    align: 'left',
                    toolbar: '#barTools'
                }
            ]], //设置表头
            url:basePath + 'collectionTrackLog/selectCollectionTrackLogPage?rbpId='+crpId,
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
        table.on('tool(listTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                layer.confirm('真的删除行么？', {
                    btn: ['确定', '取消'] //按钮
                }, function (index) {
                    layer.close(index);
                    axios.delete( basePath +'collectionTrackLog/deleteLog?id=' + data.trackLogId)
                        .then(function (res) {
                            if (res.data.code == "1") {
                                vm.$Modal.success({
                                    title: '',
                                    content: '删除成功'
                                });
                                vm.toLoading();
                            } else {
                                vm.$Message.error('删除失败，消息：' + res.data.msg);
                            }
                        })
                        .catch(function (error) {
                            vm.$Message.error('接口调用异常!');
                        });
                });

            } else if(obj.event === 'edit'){
                queryEditModule(data.trackLogId)
            }
        });


    });


})


//从后台获取下拉框数据
var getBusinessData = function () {

    //取区域列表
    axios.get(basePath +'BasicBusinessController/selectById?id='+businessId)
        .then(function (res) {
            if (res.data.code == "1") {

                vm.businessInfForm.businessId = res.data.data.businessId;
                vm.businessInfForm.customerName = res.data.data.customerName;
                vm.businessInfForm.borrowMoney = res.data.data.borrowMoney;
                vm.businessInfForm.repaymentType = res.data.data.repaymentTypeName;
                vm.businessInfForm.borrowLimit = res.data.data.borrowLimitStr;
                vm.statusList = res.data.data.collectionTackLogStatu;


                // queryVaildParentModule();
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}






//设置表单验证
var setFormValidate = {

    status: [
        {required: true, message: '请选择状态', trigger: 'change'}
    ],
    ifSendToPlat: [
        {required: true, message: '请选择是否传输平台', trigger: 'change'}
    ],
    content: [
        {required: true, message: '请填写跟踪记录',max:'200', trigger: 'blur'}
    ]
};

//状态列表数组
var status_array = [{
    paramValue: '1',
    paramName: '状态一'
}, {
    paramValue: '2',
    paramName: '状态二'
}, {
    paramValue: '3',
    paramName: '状态三'
}];

var editLogFormDefaultVal={
    trackLogId:'',//跟踪记录ID
    status:'', //状态
    trackStatusId:'',//状态ID
    trackStatusName:'',//状态名
    ifSendToPlat:'', //是否提交平台
    content:'', //跟踪记录描述
    isSend:'',//是否提交平台 int标志位
    rbpId:''//还款计划ID
};






//提交保存
var editLogFormSubmit = function () {
    vm.$refs['editLogForm'].validate((valid) => {
        if (valid) {
            //vm.$Message.success('表单校验成功!');
            /*
            * axios的post方法，传送的content-type为"application/json"，
            * 传输参数放在http请求的body内，后台需使用@RequestBody进行参数匹配
            * */

            vm.editLogForm.isSend = vm.editLogForm.ifSendToPlat=="是"?1:0;
            for(var i=0;i<vm.statusList.length;i++){
                var item = vm.statusList[i];
                if(item.paramValue == vm.editLogForm.status){
                    vm.editLogForm.trackStatusId = item.paramValue;
                    vm.editLogForm.trackStatusName = item.paramName;
                }
            }
            // vm.editLogForm.trackStatusId = vm.editLogForm.status.paramValue;
            // vm.editLogForm.trackStatusName = vm.editLogForm.status.paramName;
            vm.editLogForm.rbpId = crpId;

            axios.post(basePath +'collectionTrackLog/addOrUpdateLog', vm.editLogForm)
                .then(function (res) {
                    if (res.data.code == "1") {
                        vm.hideEditForm();
                        vm.$Modal.success({
                            title: '',
                            content: '操作成功'
                        });
                        vm.toLoading();
                    } else {
                        vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
                    }
                })
                .catch(function (error) {
                    vm.$Modal.error({content: '接口调用异常!'});
                });
        }else{
            vm.$Message.error({content: '表单校验失败!'});
        }
    });
}



var queryEditModule = function (trackLogId) {
    axios.get(basePath +'collectionTrackLog/selectById', {params: {trackLogId: trackLogId}})
        .then(function (res) {
            if (res.data.code == "1") {
                vm.editLogForm.trackLogId = res.data.data.trackLogId;
                vm.editLogForm.trackStatusName = res.data.data.trackStatusName;
                vm.editLogForm.trackStatusId = res.data.data.trackStatusId;
                vm.editLogForm.status = res.data.data.trackStatusId;
                vm.editLogForm.ifSendToPlat = (res.data.data.isSend==0)?"否":"是";
                vm.editLogForm.isSend = res.data.data.isSend;
                vm.editLogForm.content = res.data.data.content;

                vm.addLogModal = true;
            } else {
                vm.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            vm.$Modal.error({content: '接口调用异常!'});
        });
}

