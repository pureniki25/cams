let app
let dateStart
let dateEnd
window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    let table = layui.table 
    let laydate = layui.laydate


    let testData = {
        deptIds:[
            {label:'A公司',value:'A公司'},
            {label:'B公司',value:'B公司'},
            {label:'C公司',value:'C公司'},
            {label:'D公司',value:'D公司'},
            {label:'E公司',value:'E公司'},
            {label:'F公司',value:'F公司'},
            {label:'G公司',value:'G公司'},
            {label:'H公司',value:'H公司'},
            {label:'I公司',value:'I公司'},
            {label:'J公司',value:'J公司'},
            {label:'K公司',value:'K公司'},
        ],
        businessTypes:[

            {label:'车易贷展期',value:'1'},
            {label:'房速贷展期',value:'2'},
            {label:'金融仓储',value:'3'},
            {label:'三农金融',value:'4'},
            {label:'车易贷',value:'9'},
            {label:'房速贷',value:'11'},
            {label:'车全垫资代采',value:'12'},
            {label:'扶贫贷',value:'13'},
            {label:'汽车融资租赁',value:'14'},
            {label:'二手车商贷',value:'15'}
        ]
    }

    app = new Vue({
        el:"#app",
        data:{
            schForm:{
                dateStart:'',
                dateEnd:'',
                deptId:'',
                businessId:'',
                salesman:'',
                customer:'',
                businessType:'',
                extensionId:''
            },
            deptIds:testData.deptIds,
            businessTypes:testData.businessTypes,
            dateEndOption:{
                disabledDate (date) {
                    if(dateStart){
                        return date < new Date(dateStart)
                    }else{
                        return false
                    }
                }
            },
            dateStartOption:{
                disabledDate (date) {
                    if(dateEnd){
                        return date > new Date(dateEnd)
                    }else{
                        return false
                    }
                }
            },
            loanDetailModal:{
                show:false,
                url:''
            }
        },
        methods:{
            onDateStartChange:function(date){
                app.schForm.dateStart = date
            },
            onDateEndChange:function(date){
                app.schForm.dateEnd = date
            },
            resetForm:function(){
                this.$ref['schForm'].resetFields()
            },
            search:function(){
                table.reload('loanExt', {
                    url: basePath + 'renewalBusiness/list',
                    where: app.schForm //设定异步数据接口的额外参数
                    //,height: 300
                    ,page: {
                        curr: 1 //重新从第 1 页开始
                      }
                  });
            }

        },
        created:function(){
            let arry = []
            axios.get(basePath+"renewalBusiness/deptIds").then(function(res){
                if(res.data.code=='1'){
//                    $.each(res.data.data,function(i,o){
//                        arry.push({value:o.orgNameCn,label:o.orgNameCn})
//                    });
                    app.deptIds = res.data.data.company;
                }
            }).catch(function(error){

            })

        }
    })

    table.render({
        elem: '#loanExt' //指定原始表格元素选择器（推荐id选择器）
        ,height: 600 //容器高度
        ,cols: [[
            {
                field:'id',
                width:200,
                title:'展期编号'
            },{
                field:'customer',
                title:'客户姓名'
            },{
                field:'salesman',
                title:'业务获取'
            },{
                field:'businessType',
                title:'业务类型'
            },{
                field:'loanAmount',
                title:'借款金额',
                align: 'right',
                templet:function(d){
                    return d.loanAmount?numeral(d.loanAmount).format('0,0.00'):''
                }
            },{
                field:'loanTerm',
                title:'借款期限'
            },{
                field:'repayAmount',
                title:'已还本金',
                align: 'right',
                templet:function(d){
                    return d.repayAmount?numeral(d.repayAmount).format('0,0.00'):''
                }
            },{
                field:'extensionAmount',
                title:'展期金额',
                align: 'right',
                templet:function(d){
                    return d.extensionAmount?numeral(d.extensionAmount).format('0,0.00'):''
                }
            },{
                field:'extensionTerm',
                title:'展期期限'
            },{
                field:'status',
                title:'状态'
            },{
                title:'操作',
                toolbar:'#toolbar'
            }
        ]], //设置表头
        url: basePath + 'renewalBusiness/list',
        page:true,
        done:(res, curr, count)=>{
        }
      });

      table.on('tool(loanExt)',function(o){
          let data = o.data;
          let event = o.event ;
          console.log(o)
          if(event=='check'){
                axios.get(basePath+'api/getXindaiDeferView',{params:{businessId:data.id}})
                .then(function(res){
                    if(res.data.code=='1'){
                        app.loanDetailModal.url = res.data.data;
                        app.loanDetailModal.show = true ;
                    }else{
                        app.$Modal.error({content: res.msg||'接口调用异常!'});
                    }
                })
                .catch(function(error){
                    app.$Modal.error({content: '接口调用异常!'});
                })
          }
      })
})