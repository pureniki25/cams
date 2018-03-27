var businessId = document.getElementById("businessId").getAttribute("value");


window.layinit(function (htConfig) {
    var config = layui.ht_config;
    var baseCorePath = config.coreBasePath;


   layui.use(['form','laydate','element', 'ht_config', 'ht_auth'], function () {

        var element = layui.element;
        var form = layui.form;
        // var config = layui.ht_config;
        var laydate = layui.laydate;
        var vm = new Vue({
            el: '#app',
            data: {
                provs:'',
                citys:'',
                countys:'',
                returnRegFiles:[{
                    file: '',
                    originalName: '',
                    oldDocId:''
                }],
                business:{
                    businessId:''// 业务编号
                    ,customerName:''// 主借款人的客户姓名
                    ,borrowMoney:''// 借款金额
                    ,borrowLimit:''// 借款期限
                    ,repaymentTypeId:''// 还款方式
                    ,borrowRate:''// 借款利率
                    ,operatorName:''//业务主办人
                }
                ,repayPlan:{
                    payedPrincipal:''//已还本金
                    ,payedInterest:''//已还利
                    ,lastPayDate:''//利息交至日期
                    ,overdueDays:''//逾期天数
                }
                ,outputRecord:{//出款
                    outputUserName:''//出款人
                    ,factOutputDate:''//出款日期
                }
                ,carBasic:{
                    vin:''// 车架号
                    ,engineModel:''//发动机号
                    ,displacement:''//排量
                    ,displacementUnit:''//排量单位
                    ,brandName:''//汽车品牌
                    ,licensePlateNumber:''// 车牌号
                    ,model:''// 品牌型号
                    ,originPlace:''//汽车产地
                    ,licenseLocation:''//车辆属地
                    ,color:''//颜色
                    ,vehicleLicenseRegistrationDate:''//汽车注册日期
                    ,mortgage:''//车辆抵押状态
                    ,evaluationAmount:''//抵押时车辆评估价值
                    ,createTime:''//抵押时车辆评估时间
                    ,lastEvaluationAmount:''//最新车辆评估价值
                    ,lastEvaluationTime:''//最新车辆评估时间
                    ,trafficViolationFee:''//违章费用
                    ,annualVerificationExpirationDate:''//年审到期日期
                    ,odometer:''//里程表读数
                    ,fuelLeft:''//燃油量（约余）
                    ,transferTimes:''//过户次数
                    ,lastTransferDate:''//最后一次过户时间
                    //,:''//车辆评估价值
                    ,differAmount:''//价值差额
                    ,insuranceExpirationDate:''//保险到期日
                    ,carVersionConfig:''//车型及版本配置
                }
                ,drag:{
                    dragDate:''// 拖车日期
                }
                ,carConvBusAply:{
                    businessId:''//业务编号
                    ,convBusId:''//转公车申请id
                    ,aplyUser:''//申请人
                    ,aplyDate:''//申请时间
                    ,aplyExplain:''//申请理由
                    ,status:''//申请状态
                }
                ,  //可发送审批信息的用户列表
                originOptions : [
                    {id:'1',name:'lemon'},
                    {id:'2',name:'mike'},
                    {id:'3',name:'lara'},
                    {id:'4',name:'zoe'},
                    {id:'5',name:'steve'},
                    {id:'6',name:'nolan'}
                ]
                ,selectedList : [
                    {id:'1',name:'lemon'},
                    {id:'3',name:'lara'}]

            },

            mounted: function () {
                this.queryData();
                laydate.render({
                    elem: '#annualVerificationExpirationDate',
                    type:'date',
                    done: (value) => {
                        this.carBasic.annualVerificationExpirationDate = value
                    }
                });
                laydate.render({
                    elem: '#lastTransferDate',
                    type:'date',
                    done: (value) => {
                        this.carBasic.lastTransferDate = value
                    }
                });
                laydate.render({
                    elem: '#insuranceExpirationDate',
                    type:'date',
                    done: (value) => {
                        this.carBasic.insuranceExpirationDate = value
                    }
                });

                laydate.render({
                    elem: '#aplyDate',
                    type:'date',
                    done: (value) => {
                        this.carConvBusAply.aplyDate = value
                    }
                });

            },

            methods: {

                queryData: function(){
                    var mySelf = this
                    //do ajax here
                    //mySelf.originOptions=vm.originOptions;
                    //mySelf.selectedList=vm.selectedList;
                    // 多选
                    mySelf.originOptions = [{"id":"1","name":"lemon"},{"id":"2","name":"mike"},{"id":"3","name":"lara"},{"id":"4","name":"zoe"},{"id":"5","name":"steve"},{"id":"6","name":"nolan"}];
                    mySelf.selectedList = [{"id":"1","name":"lemon"},{"id":"3","name":"lara"}]



                    this.$nextTick(function(){

                    })
                },

                multipleCallback: function(data){
                    this.selectedList = data;
                    console.log('父级元素调用multipleSelected 选中的是' + JSON.stringify(data))
                },
                getDifferAmount:function(event){
                    var evaluationAmount=event.currentTarget.value;
                    var differAmount=vm.carBasic.evaluationAmount-evaluationAmount;
                    $("#differAmount").val(differAmount);
                },

                viewData: function(){

                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/convBusAplyDetail',
                        data: {"businessId":businessId},
                        // contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            // alert(JSON.stringify(data));
                            vm.carBasic=data.data.carBasic;
                            vm.business=data.data.business;
                            vm.drag=data.data.drag;
                            vm.repayPlan=data.data.repayPlan;
                            vm.outputRecord=data.data.outputRecord;
                            if(data.data.carBasic.lastEvaluationAmount==null){
                                vm.carBasic.differAmount=0;
                            }else{
                                vm.carBasic.differAmount=data.data.carBasic.evaluationAmount-data.data.carBasic.lastEvaluationAmount;
                            }
                            vm.carConvBusAply=data.data.carConvBusAply;
                        },
                        error: function (message) {
                            layer.msg("查询车辆信息发生异常，请联系管理员。",{icon:5,shade: [0.8, '#393D49'],time:3000});
                            console.error(message);
                        }
                    });

                },
                carConvBusClose(){// 关闭窗口

                    vm.carConvBusAply.businessId=businessId;
                    //form.verify({});
                    //alert(JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.returnRegFiles}));
                    alert()
                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/convBusCancel',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify({"businessId":businessId,"convBusId":vm.carConvBusAply.convBusId,"processId":''}),
                        success: function (res) {
                            if (res.code == "0000"){
                                vm.carConvBusAply=res.data.carConvBusAply;
                                layer.msg("保存成功。");
                            }
                        },
                        error: function (message) {
                            layer.msg("异常，请联系管理员。");
                            console.error(message);
                        }
                    });

                    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                    parent.layer.close(index);
                },
                convBusAply:function (event,subType){
                    vm.carConvBusAply.businessId=businessId;
                    //form.verify({});
                    //alert(JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.returnRegFiles}));
                    //alert(subType);
                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/convBusAply',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify({"carBasic":vm.carBasic,"carConvBusAply":vm.carConvBusAply,"subType":subType}),
                        success: function (res) {
                            if (res.code == "0000"){
                                vm.carConvBusAply=res.data.carConvBusAply;
                                layer.msg("保存成功。");
                            }
                        },
                        error: function (message) {
                            layer.msg("异常，请联系管理员。");
                            console.error(message);
                        }
                    });
                }
            }

        });
        vm.viewData();
    });

})








