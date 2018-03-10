var businessId = document.getElementById("businessId").getAttribute("value");

window.layinit(function (htConfig) {
    var config = layui.ht_config;
    var baseCorePath = config.coreBasePath;
    layui.config({
        base: '/plugins/layui/extend/modules/',
        version: false
    }).use(['laydate','element', 'ht_config', 'ht_auth'], function () {
        // var element = layui.element;
        // var config = layui.ht_config;
        var laydate = layui.laydate;

        var vm = new Vue({
            el: '#app',
            data: {
                carBasic:{
                    vin:''//车架号
                    ,engineModel:''//发动机号
                    ,displacement:''	//排量
                    ,invoiceCost:''//发票价
                    ,trafficViolationSituation:''//违章情况
                    ,trafficViolationFee:''//违章费用
                    ,vehicleVesselTax:''	//车船税费用
                    ,annualTicketFee:''//统缴费用
                    ,drivingLicenseConsistent:''//核对行驶资料

                    ,renewal:''//借款期内是否续保
                    ,fuelType:''//燃油形式
                    ,vehicleLicenseRegistrationDate:''//汽车注册日期
                    ,annualVerificationExpirationDate:''//年审到期日
                    ,mortgage:''//车辆抵押状态
                    ,odometer:''//里程表读数
                    ,fuelLeft:'' //燃油量（约余）
                    ,gearBox:''//变速箱
                    ,drivingLicenseInconsistentDescription:''

                }
                ,drivingLicenseInconsistentDescription:''//["有差异","有改装","有改色","有翻新"]
                ,carDetection:{
                    centerPanelNormal:''//中控台

                    ,ventilatorNormal:''//空调
                    ,interiorNormal:''//车厢内饰
                    ,windowGlassNormal:''//玻璃
                    ,radiatorNormal:''//水箱
                    ,engineNormal:''//发动机
                    ,frameNormal:''//车大梁
                    ,tireNormal:''//轮胎
                    ,spareTireNormal:''//备用胎
                    ,doorNormal:''//车门

                    //,  :''其他情况说明
                    ,evaluationAmount:''//评估金额
                    ,accelerationPerformanceNormal:''//发动机性能
                    ,brakingPerformanceNormal:''//刹车性能
                    ,brakingBalancePerformanceNormal:''//刹车平衡性能
                    ,steerPerformanceNormal:''//方向性能
                    ,gearPerformanceNormal:''//挂档性能
                    ,otherDriveDescription:''//其他情况说明
                    ,vinConsistent:''//车架号是否一致
                    ,engineModelConsistent:''//发动机号是否一致
                    ,accident:''//是否有事故痕迹
                    ,otherTrouble:''//是否有其他问题
                    ,otherTroubleDescription:''//其他情况说明
                    ,centerPanelAbnormalDescription:''
                    ,ventilatorAbnormalDescription:''
                    ,interiorAbnormalDescription:''
                    ,windowGlassAbnormalDescription:''
                    ,radiatorAbnormalDescription:''
                    ,engineAbnormalDescription:''
                    ,frameAbnormalDescription:''
                    ,tireAbnormalDescription:''
                    ,spareTireAbnormalDescrioption:''
                    ,doorAbnormalDescription:''
                    ,accelerationPerformanceAbnormalDescription:''
                    ,brakingPerformanceAbnormalDescription:''
                    ,brakingBalancePerformanceAbnormalDescription:''
                    ,steerPerformanceAbnormalDescription:''
                    ,gearPerformanceAbnormalDescription:''

                }
                ,centerPanelAbnormalDescription:''//["有破损","松动"]
                ,ventilatorAbnormalDescription:''//["有制冷","制冷不明显","无制冷","异响"]
                ,interiorAbnormalDescription:''//["有破损"]
                ,windowGlassAbnormalDescription:''//["破裂","气泡","升降不正常","渗水"]
                ,radiatorAbnormalDescription:''//["修补","变形"]
                ,engineAbnormalDescription:''//["抖动明显","异响","渗漏油","有硬伤"]
                ,frameAbnormalDescription:''//["有损伤痕迹","严重撞击"]
                ,tireAbnormalDescription:''//["破损","开裂","破损严重"]
                ,spareTireAbnormalDescrioption:''//["没有配备"]
                ,doorAbnormalDescription:''//["密封性差","封条松动"]
                ,accelerationPerformanceAbnormalDescription:''//["抖动明显","异响","渗漏油","有硬伤"]
                ,brakingPerformanceAbnormalDescription:''//["有损伤痕迹","严重撞击"]
                ,brakingBalancePerformanceAbnormalDescription:''//["破损","开裂","破损严重"]
                ,steerPerformanceAbnormalDescription:''//["没有配备"]
                ,gearPerformanceAbnormalDescription:''//["密封性差","封条松动"]
            },
            mounted: function () {
                laydate.render({
                    elem: '#vehicleLicenseRegistrationDate',
                    type:'date',
                    done: (value) => {
                        this.carBasic.vehicleLicenseRegistrationDate = value
                    }
                });
                laydate.render({
                    elem: '#annualVerificationExpirationDate',
                    type:'date',
                    done: (value) => {
                        this.carBasic.annualVerificationExpirationDate = value
                    }
                });
            },
            methods: {

                viewData: function(){

                    /*    	    laydate.render({
                                    elem: '#vehicleLicenseRegistrationDate'
                                  });
                                laydate.render({
                                    elem: '#annualVerificationExpirationDate'
                                  });*/
                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/carDetail',
                        data: {"businessId":businessId},
                        //contentType: "application/json; charset=utf-8",
                        success: function (data) {
                            //alert(JSON.stringify(data));
                            vm.carBasic=data.data.carBasic;
                            vm.carDetection=data.data.carDetection;
                            //因数组在对象中不能渲染故提出来
                            if(data.data.carDetection!=null){
                                vm.drivingLicenseInconsistentDescription=StrToArr(data.data.carBasic.drivingLicenseInconsistentDescription);
                                vm.centerPanelAbnormalDescription=StrToArr(data.data.carDetection.centerPanelAbnormalDescription);

                                vm.ventilatorAbnormalDescription=StrToArr(data.data.carDetection.ventilatorAbnormalDescription);
                                vm.interiorAbnormalDescription=StrToArr(data.data.carDetection.interiorAbnormalDescription);

                                vm.windowGlassAbnormalDescription=StrToArr(data.data.carDetection.windowGlassAbnormalDescription);
                                vm.radiatorAbnormalDescription=StrToArr(data.data.carDetection.radiatorAbnormalDescription);

                                vm.engineAbnormalDescription=StrToArr(data.data.carDetection.engineAbnormalDescription);
                                vm.frameAbnormalDescription=StrToArr(data.data.carDetection.frameAbnormalDescription);

                                vm.tireAbnormalDescription=StrToArr(data.data.carDetection.tireAbnormalDescription);
                                vm.spareTireAbnormalDescrioption=StrToArr(data.data.carDetection.spareTireAbnormalDescrioption);

                                vm.doorAbnormalDescription=StrToArr(data.data.carDetection.doorAbnormalDescription);
                                vm.accelerationPerformanceAbnormalDescription=StrToArr(data.data.carDetection.accelerationPerformanceAbnormalDescription);

                                vm.brakingPerformanceAbnormalDescription=StrToArr(data.data.carDetection.brakingPerformanceAbnormalDescription);
                                vm.brakingBalancePerformanceAbnormalDescription=StrToArr(data.data.carDetection.brakingBalancePerformanceAbnormalDescription);

                                vm.steerPerformanceAbnormalDescription=StrToArr(data.data.carDetection.steerPerformanceAbnormalDescription);
                                vm.gearPerformanceAbnormalDescription=StrToArr(data.data.carDetection.gearPerformanceAbnormalDescription);

                            }
                        },
                        error: function (message) {
                            layer.msg("查询车辆信息发生异常，请联系管理员。");
                            console.error(message);
                        }
                    });
                    //	});

                },
                againAssesClose(){//关闭窗口
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.layer.close(index);
                },
                againAsses(){
                    var v=getAgainAssessData(vm);
                    /*
                                layui.config({
                                    base: '/plugins/layui/extend/modules/',
                                    version: false
                                }).use(['laydate','element', 'ht_config', 'ht_auth'], function () {
                                    var config = layui.ht_config;*/
                    $.ajax({
                        type: "POST",
                        url: baseCorePath+'car/againAssess',
                        contentType: "application/json; charset=utf-8",
                        data: JSON.stringify({"carBasic":v.carBasic,"carDetection":v.carDetection}),
                        success: function (res) {
                            if (res.code == "0000"){
                                layer.msg("保存成功。");
                            }
                        },
                        error: function (message) {
                            layer.msg("异常，请联系管理员。");
                            console.error(message);
                        }
                    });

                    // });
                }
            }
        });

        vm.viewData();
        function StrToArr(str){
            if(str==null||str==""){
                return [""];
            }
            return str.split(",");
        }
//将多选择框封装到对象属性里面
        function getAgainAssessData(vm){
            if(vm.drivingLicenseInconsistentDescription!=null){
                vm.carBasic.drivingLicenseInconsistentDescription=vm.drivingLicenseInconsistentDescription.toString();
            }
            if(vm.centerPanelAbnormalDescription!==null){
                vm.carDetection.centerPanelAbnormalDescription=vm.centerPanelAbnormalDescription.toString();
            }
            if(vm.ventilatorAbnormalDescription!==null){
                vm.carDetection.ventilatorAbnormalDescription=vm.ventilatorAbnormalDescription.toString();
            }
            if(vm.interiorAbnormalDescription!==null){
                vm.carDetection.interiorAbnormalDescription=vm.interiorAbnormalDescription.toString();
            }
            if(vm.windowGlassAbnormalDescription!==null){
                vm.carDetection.windowGlassAbnormalDescription=vm.windowGlassAbnormalDescription.toString();
            }
            if(vm.radiatorAbnormalDescription!==null){
                vm.carDetection.radiatorAbnormalDescription=vm.radiatorAbnormalDescription.toString();
            }
            if(vm.engineAbnormalDescription!==null){
                vm.carDetection.engineAbnormalDescription=vm.engineAbnormalDescription.toString();
            }
            if(vm.frameAbnormalDescription!==null){
                vm.carDetection.frameAbnormalDescription=vm.frameAbnormalDescription.toString();
            }
            if(vm.tireAbnormalDescription!==null){
                vm.carDetection.tireAbnormalDescription=vm.tireAbnormalDescription.toString();
            }
            if(vm.spareTireAbnormalDescrioption!==null){
                vm.carDetection.spareTireAbnormalDescrioption=vm.spareTireAbnormalDescrioption.toString();
            }
            if(vm.doorAbnormalDescription!==null){
                vm.carDetection.doorAbnormalDescription=vm.doorAbnormalDescription.toString();
            }
            if(vm.accelerationPerformanceAbnormalDescription!==null){
                vm.carDetection.accelerationPerformanceAbnormalDescription=vm.accelerationPerformanceAbnormalDescription.toString();
            }
            if(vm.brakingPerformanceAbnormalDescription!==null){
                vm.carDetection.brakingPerformanceAbnormalDescription=vm.brakingPerformanceAbnormalDescription.toString();
            }
            if(vm.brakingBalancePerformanceAbnormalDescription!==null){
                vm.carDetection.brakingBalancePerformanceAbnormalDescription=vm.brakingBalancePerformanceAbnormalDescription.toString();
            }
            if(vm.steerPerformanceAbnormalDescription!==null){
                vm.carDetection.steerPerformanceAbnormalDescription=vm.steerPerformanceAbnormalDescription.toString();
            }
            if(vm.gearPerformanceAbnormalDescription!==null){
                vm.carDetection.gearPerformanceAbnormalDescription=vm.gearPerformanceAbnormalDescription.toString();
            }
            return vm;
        }

    });
})




