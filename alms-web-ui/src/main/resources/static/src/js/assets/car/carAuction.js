var businessId = document.getElementById("businessId").getAttribute("value");
window.layinit(function (htConfig) {
    var config = layui.ht_config;
    var baseCorePath = config.coreBasePath;
    layui.config({
        base: '/plugins/layui/extend/modules/',
        version: false
    }).use(['form','laydate','element', 'ht_config', 'ht_auth'], function () {

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
                reqRegFiles:[{
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
                ,carAuction:{
                    businessId:''//业务编号
                    ,auctionId:''//拍卖id
                    ,startingPrice:''//起拍价
                    ,deposit:''//保证金
                    ,fareRange:''//加价幅度
                    ,reservePrice:''//保留价
                    ,auctionStartTime:''//拍卖开始时间
                    ,auctionEndTime:''//拍卖截止时间
                    ,transType:''//交易类型
                    ,priorityPurchaser:''//优先购权人
                    ,takeWay:''//取货方式
                    ,buyStartTime:''//竞买开始时间
                    ,buyEndTime:''//竞买结束时间
                    ,auctionPosition:''//拍卖物所在位置
                    ,handleUnit:''//处置单位
                    ,unitAddr:''//单位地址
                    ,consultant:''//咨询人
                    ,consultantTel:''//咨询电话
                    ,acountName:''//账户姓名
                    ,acountNum:''//银行卡号
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
                    elem: '#auctionStartTime',
                    type:'datetime',
                    done: (value) => {
                        this.carAuction.auctionStartTime = value
                    }
                });
                laydate.render({
                    elem: '#auctionEndTime',
                    type:'datetime',
                    done: (value) => {
                        this.carAuction.auctionEndTime = value
                    }
                });
                laydate.render({
                    elem: '#buyStartTime',
                    type:'datetime',
                    done: (value) => {
                        this.carAuction.buyStartTime = value
                    }
                });
                laydate.render({
                    elem: '#buyEndTime',
                    type:'datetime',
                    done: (value) => {
                        this.carAuction.buyEndTime = value
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
                uploadFile:function(event,index){
                    var fileId=event.currentTarget.id;
                    //alert(fileId);
                    // \alert(event.currentTarget);
                    $('#'+fileId).fileupload({
                        dataType: 'json',
                        singleFileUploads: true,
                        acceptFileTypes: '', // Allowed file types
                        url: baseCorePath+'doc/singleUpload',// 文件的后台接受地址
                        // 设置进度条
                        // 上传完成之后的操作，显示在img里面
                        done: function (e, data){
                            //alert("bbb");
                            var reFile=data._response.result.docItemListJson;
                            var reFiles=eval("("+reFile+")");
                            //alert(JSON.stringify(reFiles));
                            vm.returnRegFiles[index].oldDocId=reFiles.docId;
                            vm.returnRegFiles[index].originalName=reFiles.originalName;

                        }
                    });
                    $('#'+fileId).bind('fileuploadsubmit', function (e, data) {
                        if (data && data.originalFiles && data.originalFiles.length > 0) {
                            data.formData = {fileName: data.originalFiles[0].name};
                            data.formData.businessId = businessId;
                            data.formData.busType='AfterLoan_Material_CarAuction';
                            data.formData.file=data.originalFiles[0];
                        }
                    });
                },
                removeTabTr: function (event, index) {
                    var docId=$('#docId'+index).val();

                    var deled=false;
                    if(docId==null||docId==""){
                        deled=true;
                    }
                    else{
                        $.ajax({
                            type: "GET",
                            url: baseCorePath+'doc/delOneDoc?docId='+docId,
                            // data: {"businessId":businessId},
                            // contentType: "application/json; charset=utf-8",
                            success: function (data) {
                                deled=true;

                            },
                            error: function (message) {
                                layer.msg("删除文件失败。");
                                console.error(message);
                            }
                        });
                    }
                    //alert("bbb");
                    if(deled){
                        //	alert("aaaa");
                        this.returnRegFiles.splice(index, 1);
                    }
                },
                addTabTr :function(event){
                    this.returnRegFiles.push({
                        file: '',
                        originalName: '',
                        oldDocId:''
                    })
                },

                viewData: function(){

                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/AuctionDetail',
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

                            vm.carAuction=data.data.carAuction;
                            if(vm.carAuction.fareRange==null){
                                vm.carAuction.fareRange='';
                            }
                            if(vm.carAuction.takeWay==null){
                                vm.carAuction.takeWay='';
                            }
                            if(vm.carAuction.transType==null){
                                vm.carAuction.transType='';
                            }
                            //alert(JSON.stringify(vm.carAuction));
                            var docFiles=data.data.returnRegFiles;

                            if(docFiles!=null&&docFiles.length>0){
                                vm.returnRegFiles[docFiles.length];
                                for (var i=0;i<docFiles.length;i++){
                                    if(i>0){
                                        vm.returnRegFiles.push({
                                            file: '',
                                            originalName: docFiles[i].originalName,
                                            oldDocId:docFiles[i].docId
                                        });
                                    }else{
                                        vm.returnRegFiles[i].oldDocId=docFiles[i].docId;
                                        vm.returnRegFiles[i].originalName=docFiles[i].originalName;
                                    }
                                    // i++;
                                }
                            }
                        },
                        error: function (message) {
                            layer.msg("查询车辆信息发生异常，请联系管理员。");
                            console.error(message);
                        }
                    });

                },
                carAuctionAplyClose(){// 关闭窗口

                    vm.carAuction.businessId=businessId;
                    //form.verify({});
                    //alert(JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.returnRegFiles}));

                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/auctionCancel',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify({"businessId":businessId,"auctionId":vm.carAuction.auctionId,"processId":vm.carAuction.processId}),
                        success: function (res) {
                            if (res.code == "0000"){
                                vm.carAuction=res.data.carAuction;
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
                carAuctionAply:function (event,subType){

                    vm.carAuction.businessId=businessId;
                    //form.verify({});
                    //alert(JSON.stringify({"returnReg":vm.returnReg,"returnRegFiles":vm.returnRegFiles}));
                    //alert(subType);
                    //vm.reqRegFiles=vm.returnRegFiles;
                    for(var i=0;i<vm.returnRegFiles.length;i++){
                        vm.returnRegFiles[i].file='';
                        vm.reqRegFiles[i]=vm.returnRegFiles[i];

                    }

                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/auctionAply',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify({"carBasic":vm.carBasic,"carAuction":vm.carAuction,"returnRegFiles":vm.reqRegFiles,"subType":subType}),
                        success: function (res) {
                            if (res.code == "0000"){
                                vm.carAuction=res.data.carAuction;
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








