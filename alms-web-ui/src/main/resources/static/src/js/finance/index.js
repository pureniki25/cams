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
            areaList:[],
            companys: [],
            companyList:[],
            businessTypes: [],
            collectionStatus: [],
            form: {
                curPage: 1,
                pageSize: 10,
                customer: '',
                businessId: '',
                principalDeadLine: '',
                accountantConfirmStatus: '',
                areaId:'',
                companyId: '',
                businessType: '',
                repayDate: '',
                regDate: '',
                regDateStart: '',
                regDateEnd: '',
                status: ''
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
                        if (p.row.repaymentTypeId == 2 && p.row.borrowLimit == p.row.periods) {
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
                    render:(h,p)=>{
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
                    render: (h, p) => {
                        var s1 = p.row.planRepayDate;
                        s1 = new Date(s1.replace(/-/g, "/"));
                        s2 = new Date();//当前日期：2017-04-24
                        var days = s2.getTime() - s1.getTime();
                        var time = parseInt(days / (1000 * 60 * 60 * 24));
                        let color = 'blue'
                        let content = p.row.status

                        if(content=='逾期'){
                            if(time<=15&&time>1){
                                color = 'red'
                            }if(time>15){
                                color = 'black'
                            }
                        }
                        if(time<0&&time>-4&&content=='还款中'){
                            color = 'yellow'
                        }
                        if(content=='已还款'){
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
                    render:(h,p)=>{
                        return h('span',(p.row.canWithhold&&p.row.canWithhold==true?'支持代扣':'不支持代扣'));
                    }
                },
                {
                    title: '操作',
                    render: (h, p) => {

                        function initMenuItem(title, link,call) {
                            return h('li', [
                                h('i-button', {
                                    class: ['menuItem'],
                                    on: {
                                        click: function () {
                                            if(link){
                                                // window.location.href = link ;
                                                layer.open({
                                                    type: 2,
                                                    title: title,
                                                    content: [link],
                                                    area: ['1600px', '800px'],
                                                    success: function (layero, index) {
                                                        curIndex = index;
                                                    }
                                                })
                                            }else{
                                                if(call=='revokeConfirm'){
                                                    app.revokeConfirm(p.row)
                                                }
                                                if(call=='confirmWithhold'){
                                                    let url = '/finance/confirmWithhold?businessId='+p.row.businessId+'&afterId='+p.row.afterId
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
                                                if(call=='settle'){
                                                    let url = '/finance/settle?businessId='+p.row.businessId+'&afterId='+p.row.afterId
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
                                                if(call=='planSettle'){
                                                    let url = '/finance/settle?businessId='+p.row.businessId+'&afterId='+p.row.afterId+'&planId='+p.row.planId
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
                        let repayConfirm = initMenuItem('财务人员确认还款', '/finance/repayConfirm?businessId='
                                                +p.row.businessId+'&afterId='
                                                +p.row.afterId+'&planListId='
                                                +p.row.planListId);
                        //let businessAllSettle = initMenuItem('业务全部结清确认', '/ssssxx')
                        let revokeConfirm = initMenuItem('撤销还款确认',null,'revokeConfirm')
                        let confirmWithhold = initMenuItem('代扣确认',null,'confirmWithhold')
                        let settle = initMenuItem('财务结清',null,'settle')
                        let planSettle = initMenuItem('还款计划结清',null,'planSettle')
                        menu.push(repayConfirm)
                        //menu.push(businessAllSettle)
                        menu.push(revokeConfirm)
                        menu.push(confirmWithhold)
                        menu.push(planSettle)
                        menu.push(settle)
                        
                        return h('Poptip', {
                            props: {
                                placement: 'left'
                            }
                        }, [
                                h('i-button', '操作'),
                                h('ul', {
                                    slot: 'content'
                                }, menu)
                            ])
                    }
                },
                {
                    title: '详情',
                    render: (h, p) => {
                        return h('i-button', {
                            on: {
                                click: function () {
                                	console.log(p)
                                	let url = '/finance/repaymentPlanInfo?businessId=' + p.row.businessId + '&customer=' + p.row.customer + '&phoneNumber=' + p.row.phoneNumber
                                	 + '&repaymentType=' + p.row.repaymentType + '&borrowMoney=' + p.row.borrowMoney + '&borrowLimit=' + p.row.borrowLimit;
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
                total: 0
            }
        },
        watch:{
            'form.areaId':function(n){
                if(n){
                    app.companys = []
                    app.companyList.forEach(e=>{
                        if(e.areaPid===n){
                            app.companys.push(e)
                        }
                    })
                }else{
                    app.companyList.forEach(e=>{
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

                axios.get(fpath + 'finance/getFinanceMangerList', {
                    params: params
                })
                    .then(function (res) {
                        if (res.data.code == '0') {
                            app.table.data = res.data.data
                            app.table.total = res.data.count
                        } else {
                            app.$Message.error({ content: '获取列表数据失败' })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        app.$Message.error({ content: '获取列表数据失败' })
                    })
            },
            paging: function (page) {
                app.form.curPage = page;
                app.search()
            },
            revokeConfirm(p){
                axios.get(fpath+'finance/revokeConfirm',{ params: { businessId: p.businessId,afterId:p.afterId }})
                .then(function(res){
                    if(res.data.code=='1'){
                        app.$Message.success({content:res.data.msg})
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){

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
                            app.$Message.error({content: '操作失败，消息：' + res.data.msg});
                        }
                    })
                    .catch(function (error) {
                        vm.$Message.error({content: '接口调用异常!'});
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
                        app.$Message.error({ content: '获取分公司失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取分公司失败' })
                })

            axios.get(fpath + 'finance/getBusinessType')
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.businessTypes = res.data.data
                    } else {
                        app.$Message.error({ content: '获取业务类型失败' })
                        console.log(res.data.msg);
                    }
                })
                .catch(function (err) {
                    app.$Message.error({ content: '获取业务类型失败' })
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
})