
/*
* 代扣查询页面
*
* */
var layer;
var table;
var basePath;
var vm;


window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = _htConfig.coreBasePath;
    getDeductionPlatformInfo();
    getBank();
    vm = new Vue({
        el: '#app',
        data: {
            loading: false, //查询标志位
            exporting:false,//导出标志位
            searchForm:{
		        	bankCode:'', //分公司ID
		            platformId :'' //代扣平台
            },
            ruleValidate:setSearchFormValidate, //表单验证


            loading: false, //查询标志位
            saving:false, //存储标志位
            exporting:false,//导出标志位
            submitLoading: false,
            bankList:[],
            platformList:[],
       owInfo:'',//存储当前选中行信息
            edit_modal:false,//控制编辑项选择modal显示的标志位
            menu_modal:false,


        },
        methods: {
        	
                 handleReset (name) {
                     var tt =this.$refs[name];
                     tt.resetFields();
                 },
            //重载表格
            toLoading() {
                if (table == null) return;
                vm.$refs['searchForm'].validate((valid) =>{
                    if (valid) {
                        this.loading = true;
                        console.log(vm.searchForm);


                        table.reload('listTable', {
                            where: {
                                bankCode:vm.searchForm.bankCode, //分公司ID
                                platformId:vm.searchForm.platformId  //代扣平台

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
                    field: 'bankName',
                    title: '支持银行'
                }, {
                    field: 'platformName',
                    title: '代扣平台'
                },{
                    field: 'onceLimit',
                    title: '单笔限额'
                }, {
                    field: 'dayLimit',
                    title: '每日限额'
                }
                
            
            ]], //设置表头
            url: basePath +'DeductionController/getBankLimit',
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
                    title ="详情";
                    url = getDetailUrl();

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

//获取详情的URL路径
function getDetailUrl(){
    var url
    $.ajax({
        type : 'GET',
        async : false,
        url : basePath +'RepaymentLogController/getRepayLogDetailById?logId='+vm.selectedRowInfo.logId,
        headers : {
            app : 'ALMS',
            Authorization : "Bearer " + getToken()
        },
        success : function(data) {
            var repayLogDetail = data.data;
            url =  '/collectionUI/repaymentLogDetailUI?platformName='+vm.selectedRowInfo.platformName+'&thirdOrderId='+repayLogDetail.thirdOrderId+"&merchOrderId="+repayLogDetail.merchOrderId+"&currentAmount="+repayLogDetail.currentAmount+"&repayStatus="+repayLogDetail.repayStatus+"&createTime="+repayLogDetail.createTime+"&remark="+repayLogDetail.remark
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






//查询表单验证
var setSearchFormValidate = {
    derateMoneyBegin: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ],
    derateMoneyEnd: [
        {pattern:/^[0-9]+(.[0-9]{1,2})?$/,   message: '请填写不超过两位小数的数字', trigger: 'blur'}
    ]
};


var getDeductionPlatformInfo=function(){
	
	
    var self = this;  
    var reqStr =basePath+ "DeductionController/getDeductionPlatformInfo"
    axios.get(reqStr)
        .then(function (result) {
            if (result.data.code == "1") {
            	
      
                vm.platformList = result.data.data.platformList;
       
                
            } else {
                self.$Modal.error({content: '获取数据失败：' + result.data.msg});
            }
        })

    .catch(function (error) {
        vm.$Modal.error({content: '接口调用异常!'});
    });
	

}



var getBank=function(){
	
	
    var self = this;  
    var reqStr =basePath+ "DeductionController/getBank"
    axios.get(reqStr)
        .then(function (result) {
            if (result.data.code == "1") {
            	
      
                vm.bankList = result.data.data.bankList;
       
                
            } else {
                self.$Modal.error({content: '获取数据失败：' + result.data.msg});
            }
        })

    .catch(function (error) {
        vm.$Modal.error({content: '接口调用异常!'});
    });
	

}






