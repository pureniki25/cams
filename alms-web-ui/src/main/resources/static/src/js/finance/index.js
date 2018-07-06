/**
 * 
 */
let app
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    console.log(cpath);
    console.log(fpath);
    app = new Vue({
        el: "#app",
        data: {
            show: true,
            accountingConfirmStatus: [],
            areaList: [],
            companys: [],
            companyList: [],
            businessTypes: [],
            collectionStatus: [],
            form: {
                curPage: 1,
                pageSize: 10,
                customer: '',
                businessId: '',
                principalDeadLine: '',
                accountantConfirmStatus: '',
                areaId: '',
                companyId: '',
                businessType: '',
                repayDate: '',
                regDate: '',
                regDateStart: '',
                regDateEnd: '',
                status: '',
                planListId:''
            },
            table: {
                col: [{
                        title: '业务编号',
                        key: 'businessId',
                    },
                    {
                        title: '期数',
                        key: 'afterId'
                    },
                    {
                        title: '部门',
                        key: 'dept',
                    },
                    {
                        title: '主办',
                        key: 'operator'
                    },
                    {
                        title: '客户姓名',
                        key: 'customer'
                    },
                    {
                        title:'是否主借款人',
                        key:'ismainCustomer',
                        render:(h,p)=>{
                            return h('span',p.row.ismainCustomer?'是':'否')
                        }
                    },
                    {
                        title: '业务类型',
                        key: 'businessType'
                    },
                    {
                        title: '应还日期',
                        key: 'planRepayDate',
                    },
                    {
                        title: '应还金额',
                        key: 'planRepayAmount',
                        align: 'right',
                        render: (h, p) => {

                            let firstPeriod = h('Tag', {
                                props: {
                                    color: 'yellow'
                                }
                            }, '首期');
                            let benjinPeriod = h('Tag', {
                                props: {
                                    color: 'yellow'
                                }
                            }, '本金期');
                            let lastPeriod = h('Tag', {
                                props: {
                                    color: 'yellow'
                                }
                            }, '末期');
                            let text = h('span', numeral(p.row.planRepayAmount).format('0,0.00'));

                            let res = []
                            let isZQ = p.row.afterId.indexOf('ZQ') > -1 ? true : false;
                            if (p.row.periods == 1 && !isZQ) {
                                res.push(firstPeriod)
                            }
                            if (p.row.repaymentTypeId == 2 && p.row.borrowLimit+1 == p.row.periods) {
                                res.push(benjinPeriod)
                            }
                            if (p.row.repaymentTypeId == 5 && p.row.borrowLimit == p.row.periods) {
                                res.push(lastPeriod)
                            }
                            res.push(text)
                            return h('div', res)
                        }
                    },
                    {
                        title: '已还金额',
                        key: 'repaidAmount',
                        align: 'right',
                        render: (h, p) => {
                            return h('span', numeral(p.row.repaidAmount).format('0,0.00'))
                        }
                    },
                    {
                        title: '会计确认状态',
                        key: 'financeConfirmStatus',
                        render: (h, p) => {
                            let s = p.row.financeConfirmStatus;
                            let color = 'blue'
                            let content = ''
                            // '会计确认状态，0或null:待审核;1:已审核;2:已退回;3:已返审核;4:导入;',
                            if (!s || s == 0) {
                                content = '待审核'
                            } else if (s == 1) {
                                content = '已审核'
                            } else if (s == 2) {
                                content = '已退回'
                            } else if (s == 3) {
                                content = '已返审核'
                            } else if (s == 4) {
                                content = '导入'
                            }
                            return h('Tag', {
                                props: {
                                    color: color
                                }
                            }, content)
                        }
                    },
                    {
                        title: '状态',
                        key: 'status',
                        width:200,
                        render: (h, p) => {
                            var s1 = p.row.planRepayDate;
                            s1 = new Date(s1.replace(/-/g, "/"));
                            s2 = new Date(); //当前日期：2017-04-24
                            var days = s2.getTime() - s1.getTime();
                            var time = parseInt(days / (1000 * 60 * 60 * 24));
                            let color = 'blue'
                            let content = p.row.repayStatus||p.row.status

                            if (p.row.status == '逾期') {
                                if (time <= 15 && time > 1) {
                                    color = 'red'
                                }
                                if (time > 15) {
                                    color = 'black'
                                }
                            }
                            if (time < 0 && time > -4 && p.row.status == '还款中') {
                                color = 'yellow'
                            }
                            if (p.row.status == '已还款') {
                                color = 'green'
                            }
                            return h('Tag', {
                                props: {
                                    color: color
                                }
                            }, content)
                        }
                    },
                    {
                        title: '是否支持代扣',
                        key: 'canWithhold',
                        render: (h, p) => {
                            // return h('span', (p.row.canWithhold && p.row.canWithhold == true ? '支持代扣' : '不支持代扣'));
                            return h('p', [
                                h('Tooltip', {
                                    props: {
                                        placement: 'top',
                                        transfer: true
                                    }
                                }, [//这个中括号表示是Tooltip标签的子标签
                                    (p.row.canWithhold && p.row.canWithhold == true ? '是' : '否'),//表格列显示文字
                                    h('p', {
                                        slot: 'content',
                                        style: {
                                            whiteSpace: 'normal'
                                        }
                                    }, p.row.canWithholdDesc)
                                ])
                            ]);
                        }
                    },
                    {
                        title: '操作',
                        render: (h, p) => {

                            function initMenuItem(title, link, call) {
                                return h('li', [
                                    h('i-button', {
                                        class: ['menuItem'],
                                        props: {
                                            long: true
                                        },
                                        on: {
                                            click: function () {
                                                if (link) {

                                                    if (p.row.status == '全部已还款') {
                                                        app.$Message.warning({
                                                            content: '当前计划已还款'
                                                        });
                                                        return;
                                                    }
                                                    let lastRepayConfirm = true
                                                    $.ajax({
                                                        type : 'GET',
                                                        async : false,
                                                        url : fpath +'finance/lastRepayConfirm?businessId='+p.row.businessId+'&afterId='+p.row.afterId,
                                                        headers : {
                                                            app : 'ALMS',
                                                            Authorization : "Bearer " + getToken()
                                                        }, 
                                                        success : function(data) {
                                                                console.log(data);
                                                                if(data.code=='1'){
                                                                    if(data.data==0||data.data==10||data.data==11||data.data==21||data.data==31){
                                                                        lastRepayConfirm = true ;
                                                                    }else{
                                                                        lastRepayConfirm = false ;
                                                                    }
                                                                }else{
                                                                    app.$Message.error({content:data.msg})
                                                                }
                                                            },
                                                        error : function() {
                                                            console.log(data);
                                                        }
                                                    });

                                                    if(!lastRepayConfirm){
                                                        app.$Message.warning({content:'上次自动代扣的业务此次不能线下还款'})
                                                        return ;
                                                    }
                                                    // window.location.href = link ;
                                                    layer.open({
                                                        type: 2,
                                                        title: title,
                                                        content: [link],
                                                        area: ['95%', '95%'],
                                                        success: function (layero, index) {
                                                            curIndex = index;
                                                        }
                                                    })
                                                } else {
                                                    if (call == 'revokeConfirm') {
                                                        app.revokeConfirm(p.row)
                                                    }
                                                    if (call == 'confirmWithhold') {
                                                        let url = '/finance/confirmWithhold?businessId=' + p.row.businessId + '&afterId=' + p.row.afterId
                                                        layer.open({
                                                            type: 2,
                                                            title: title,
                                                            content: [url],
                                                            area: ['95%', '95%'],
                                                            success: function (layero, index) {
                                                                curIndex = index;
                                                            }
                                                        })
                                                    }
                                                    if (call == 'settle') {
                                                        let url = '/finance/settle?businessId=' + p.row.businessId + '&afterId=' + p.row.afterId
                                                        layer.open({
                                                            type: 2,
                                                            title: title,
                                                            content: [url],
                                                            area: ['95%', '95%'],
                                                            success: function (layero, index) {
                                                                curIndex = index;
                                                            }
                                                        })
                                                    }
                                                    if (call == 'planSettle') {
                                                        let url = '/finance/settle?businessId=' + p.row.businessId + '&afterId=' + p.row.afterId + '&planId=' + p.row.planId
                                                        layer.open({
                                                            type: 2,
                                                            title: title,
                                                            content: [url],
                                                            area: ['95%', '95%'],
                                                            success: function (layero, index) {
                                                                curIndex = index;
                                                            }
                                                        })
                                                    }
                                             
                                                    if (call == 'withhold') {debugger
                                                    	 if (p.row.status == '已还款') {
                                                             app.$Message.warning({
                                                                 content: '已还款不能代扣'
                                                             });
                                                             return;
                                                         }
			                                         
                                                        let url = getDeductionUrl(p.row.planListId);
                                                        layer.open({
                                                            type: 2,
                                                            title: title,
                                                            content: [url],
                                                            area: ['1600px', '800px'],
                                                            success: function (layero, index) {
                                                                curIndex = index;
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                        }
                                    }, title)
                                ])
                            }
                            /* 根据p.row状态封装菜单 */
                            let menu = []
                            let repayConfirm = initMenuItem('财务人员确认还款', '/finance/repayConfirm?businessId=' +
                                p.row.businessId + '&afterId=' +
                                p.row.afterId + '&planListId=' +
                                p.row.planListId);
                            //let businessAllSettle = initMenuItem('业务全部结清确认', '/ssssxx')
                            let revokeConfirm = initMenuItem('撤销还款确认', null, 'revokeConfirm')
                            let confirmWithhold = initMenuItem('代扣确认', null, 'confirmWithhold')
                            let settle = initMenuItem('财务结清', null, 'settle')
                            let planSettle = initMenuItem('还款计划结清', null, 'planSettle')
                            let withhold = initMenuItem('支付公司代扣', null, 'withhold')

                            if (p.row.srcType == 2) {
                                menu.push(repayConfirm)
                                //menu.push(businessAllSettle)
                                menu.push(revokeConfirm)
                                menu.push(confirmWithhold)
                                menu.push(planSettle)
                                menu.push(settle)
                                   menu.push(withhold)
                            }
                            let poptipContent;
                            if (p.row.srcType == 2) {
                                poptipContent = [
                                    h('i-button', '操作'),
                                    h('ul', {
                                        slot: 'content'
                                    }, menu)
                                ]
                            } else {
                                poptipContent = '暂不能处理信贷生成的业务'
                            }


                            return h('Poptip', {
                                props: {
                                    placement: 'left'
                                }
                            }, poptipContent)
                        }
                    },
                    {
                        title: '详情',
                        render: (h, p) => {
                            return h('i-button', {
                                on: {
                                    click: function () {
                                        console.log(p)
                                        let url = '/finance/repaymentPlanInfo?businessId=' + p.row.businessId + '&customer=' + p.row.customer + '&phoneNumber=' + p.row.phoneNumber +
                                            '&repaymentType=' + p.row.repaymentType + '&borrowMoney=' + p.row.borrowMoney + '&borrowLimit=' + p.row.borrowLimit;
                                        layer.open({
                                            type: 2,
                                            title: '计划详情',
                                            content: url,
                                            area: ['1600px', '800px'],
                                            success: function (layero, index) {
                                                console.log(layero, index);
                                            }
                                        })
                                    }
                                }
                            }, '详情')
                        }
                    }

                ],
                data: [],
                total: 0,
                loading: false
            }
        },
        watch: {
            'form.areaId': function (n) {
                if (n) {
                    app.companys = []
                    app.companyList.forEach(e => {
                        if (e.areaPid === n) {
                            app.companys.push(e)
                        }
                    })
                } else {
                    app.companyList.forEach(e => {
                        app.companys.push(e)
                    })
                }

            }
        },
        methods: {
            search: function () {
            	
                let params = {}

                Object.keys(this.form).forEach(element => {
                    if (this.form[element] &&
                        (this.form[element] != '' || this.form[element].length != 0)) {
                        params[element] = this.form[element]
                    }
                })
                this.$Message.loading({
                    duration: 0,
                    content: '数据加载中...',
                })

                this.table.loading = true
                axios.get(fpath + 'finance/getFinanceMangerList', {
                        params: params
                    })
                    .then(function (res) {
                        app.table.loading = false
                        app.$Message.destroy()
                        if (res.data.code == '0') {
                            app.table.data = res.data.data
                            app.table.total = res.data.count
                        } else {
                            app.$Message.error({
                                content: '获取列表数据失败'
                            })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        app.table.loading = false
                        app.$Message.destroy()
                        app.$Message.error({
                            content: '获取列表数据失败'
                        })
                    })
            },
         
            paging: function (page) {
            	   this.$refs['pages'].currentPage = page;
                app.form.curPage = page;
                app.search()
            },
            revokeConfirm(p) {debugger
//             	 if (p.bankRepay== true) {
//                     app.$Message.warning({
//                         content: '银行代扣不能撤销'
//                     });
//                     return;
//                 }
                app.$Modal.confirm({
                    content:'是否确认取消还款?',
                    onOk(){
                        axios.get(fpath + 'finance/revokeConfirm', {
                            params: {
                                businessId: p.businessId,
                                afterId: p.afterId
                            }
                        })
                        .then(function (res) {
                            if (res.data.code == '1') {
                                app.$Message.success({
                                    content: "撤销还款成功!"
                                })
                                app.search()
                            } else {
                                app.$Message.error({
                                    content: res.data.msg
                                })
                            }
                        })
                        .catch(function (err) {
                            console.log(err);
                        })
                    }
                })

                
            }
        },
        created: function () {


            //取区域列表
            axios.get(fpath + 'finance/getAreaCompany')
                .then(function (res) {
                    if (res.data.code == "1") {
                        app.areaList = res.data.data.area;
                        app.companyList = res.data.data.company;
                    } else {
                        app.$Message.error({
                            content: '操作失败，消息：' + res.data.msg
                        });
                    }
                })
                .catch(function (error) {
                    vm.$Message.error({
                        content: '接口调用异常!'
                    });
                });


            /*  axios.get(cpath + 'sys/param/getParam', { params: { paramType: '会计确认状态' } })
                 .then(function (res) {
                     if (res.data.code == '1') {
                         app.accountingConfirmStatus = res.data.data
                     } else {
                         app.$Message.error({ content: '获取会计确认状态失败' })
                         console.log(res.data.msg);
                     }
                 })
                 .catch(function (err) {
                     app.$Message.error({ content: '获取会计确认状态失败' })
                 }) */

            axios.get(fpath + 'finance/getCompanys')
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.companys = res.data.data
                    } else {
                        app.$Message.error({
                            content: '获取分公司失败'
                        })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({
                        content: '获取分公司失败'
                    })
                })

            axios.get(fpath + 'finance/getBusinessType')
                .then(function (res) {
                    if (res.data.code == '1') {
                        res.data.data.forEach((e) => {
                            //过滤房贷车贷展期
                            if (e.businessTypeId != 1 && e.businessTypeId != 2) {
                                app.businessTypes.push(e)
                            }
                        })
                        // app.businessTypes = res.data.data
                    } else {
                        app.$Message.error({
                            content: '获取业务类型失败'
                        })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({
                        content: '获取业务类型失败'
                    })
                })

            /*  axios.get(cpath + 'sys/param/getParam', { params: { paramType: '贷后状态' } })
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.collectionStatus = res.data.data
                    } else {
                        app.$Message.error({ content: '获取贷后状态失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取贷后状态失败' })
                })
 */
            this.search()
        }
    })
    
    
  //获取执行代扣的URL路径
    function getDeductionUrl(planListId){debugger
        var url
        $.ajax({
            type : 'GET',
            async : false,
            url : cpath +'DeductionController/selectDeductionInfoByPlayListId?planListId='+planListId,
            headers : {
                app : 'ALMS',
                Authorization : "Bearer " + getToken()
            }, 
            success : function(data) {debugger
                var deduction = data.data;
                url =  '/collectionUI/deductionUI?planListId='+planListId
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
    window.app = app ;
})