var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    basePath = htConfig.financeBasePath;
    coreBasePath = htConfig.coreBasePath;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
            syncing: false,
            form: {
                curPage: 1,
                pageSize: 10,
                key: null,
                repayType: null,
                confirmTime: null,
                companyId: null,
                businessType: null,
                srcType: null,
            },
            dateOptions:{
                shortcuts:[
                    {
                        text:'今天',
                        value(){
                            let today = moment().format("YYYY-MM-DD");
                            return [today,today];
                        },
                        onClick(dp){
                            let today = moment().format("YYYY-MM-DD");
                            vm.form.confirmTime=[today,today]
                        }
                    },
                    {
                        text:'昨天',
                        value(){
                            let yesterday = moment().subtract(1, 'days').format("YYYY-MM-DD");
                            return [yesterday,yesterday];
                        },
                        onClick(dp){
                            let yesterday = moment().subtract(1, 'days').format("YYYY-MM-DD");
                            vm.form.confirmTime=[yesterday,yesterday]
                        }
                    },
                    {
                        text:'本月',
                        value(){
                            let start = moment().add('month', 0).format('YYYY-MM') + '-01'
                            let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
                            return [start,end];
                        },
                        onClick(dp){
                            let start = moment().add('month', 0).format('YYYY-MM') + '-01'
                            let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
                            vm.form.confirmTime=[start,end]
                        }
                    },
                    {
                        text:'上个月',
                        value(){
                            let start = moment().add('month', -1).format('YYYY-MM') + '-01'
                            let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
                            return [start,end];
                        },
                        onClick(dp){
                            let start = moment().add('month', -1).format('YYYY-MM') + '-01'
                            let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
                            vm.form.confirmTime=[start,end]
                        }
                    }
                ],
                disabledDate: function (date) {
                    return date > new Date();
                },
            },
            table: {
                loading: false,
                total: 0,
                data: [],
                current:1,
                col: [{
                        title: '业务编号',
                        key: 'businessId'
                    },
                    {
                        title: '期数',
                        key: 'afterId'
                    },
                    {
                        title: '业务类型',
                        key: 'extBusinessCtype'
                    },
                    {
                        title: '所属分公司',
                        key: 'extCompanyName'
                    },
                    {
                        title: '客户名称',
                        key: 'extCustomerName'
                    },
                    {
                        title: '所属投资端',
                        key: 'extPlatform'
                    },
                    {
                        title: '还款方式',
                        key: 'extRepayType'
                    },
                    {
                        title: '实收日期',
                        key: 'repayDate'
                    },
                    {
                        title: '确认日期',
                        key: 'createTime'
                    },
                    {
                        title: '实收金额',
                        key: 'factAmount'
                    },
                    {
                        title: '本金',
                        key: 'extItem10'
                    },
                    {
                        title: '利息',
                        key: 'extItem20'
                    },
                    {
                        title: '分公司服务费',
                        key: 'extItem30'
                    },
                    {
                        title: '平台服务费',
                        key: 'extItem50'
                    },
                    {
                        title: '滞纳金',
                        render: function (h, p) {
                            return h('span', p.row.extItem60offline + p.row.extItem60online)
                        }
                    },
                    {
                        title: '其他费用',
                        key: 'extOtherFee'
                    },
                    {
                        title: '操作人员',
                        key: 'createUser'
                    },

                ]
            },
            companys: [],
            businessTypes: [],
            paymentPlatformList: []
        },
        watch:{
            'form.confirmTime':function(){
                vm.form.curPage = 1
            }
        },
        methods: {
            // today: function () {
            //     let today = moment().format("YYYY-MM-DD");
            //     // $ref
            //     vm.form.confirmTime=[today,today]
            //     vm.paging(1)
            // },
            // yesterday: function () {
            //     let yesterday = moment().subtract(1, 'days').format("YYYY-MM-DD");
            //     vm.form.confirmTime=[yesterday,yesterday]
            //     vm.paging(1)
            // },
            // thisMonth: function () {
            //     let start = moment().add('month', 0).format('YYYY-MM') + '-01'
            //     let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
            //     vm.form.confirmTime=[start,end]
            //     vm.paging(1)
            // },
            // lastMonth: function () {
            //     let start = moment().add('month', -1).format('YYYY-MM') + '-01'
            //     let end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')
            //     vm.form.confirmTime=[start,end]
            //     vm.paging(1)
            // },
            paging: function (page) {
                if (this.syncing) {
                    return;
                }
                if (page) {
                    this.form.curPage = page;
                }
                if (this.form.confirmTime) {
                    if (this.form.confirmTime[0] && this.form.confirmTime[0] != "") {
                        this.form.confirmStart = this.form.confirmTime[0];
                    }else{
                        this.form.confirmStart = null;
                    }
                    if (this.form.confirmTime[1] && this.form.confirmTime[1] != "") {
                        this.form.confirmEnd = this.form.confirmTime[1];
                    }else{
                        this.form.confirmEnd = null;
                    }
                }
                vm.table.loading = true;
                axios.post(basePath + 'factRepay/list', this.form).then(res => {

                    console.log(res);
                    if (res.data.code) {
                        vm.$Message.error({
                            content: '查询实还记录失败'
                        })
                        vm.table.current = 1
                    } else {
                        vm.table.data = res.data.records
                        vm.table.total = res.data.total
                        vm.table.current = res.data.current
                    }

                    vm.table.loading = false;
                })
            },
            exportExcel: function () {
                if (this.form.confirmTime) {
                    if (this.form.confirmTime[0] && this.form.confirmTime[0] != "") {
                        this.form.confirmStart = this.form.confirmTime[0];
                    }else{
                        this.form.confirmStart = null;
                    }
                    if (this.form.confirmTime[1] && this.form.confirmTime[1] != "") {
                        this.form.confirmEnd = this.form.confirmTime[1];
                    }else{
                        this.form.confirmEnd = null;
                    }
                }
                axios.post(basePath + 'factRepay/export', this.form, {
                    responseType: 'blob'
                }).then(res => {
                    console.log(res);

                    if(res.data && res.data.size==0){
                        vm.$Message.error({content:'下载失败,请调整查询参数'})
                        return ;
                    }

                    let blob = new Blob([res.data], {
                        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
                    })
                    const downloadElement = document.createElement('a')
                    const href = window.URL.createObjectURL(blob)
                    downloadElement.href = href
                    downloadElement.download = '实还记录.xlsx'
                    document.body.appendChild(downloadElement)
                    downloadElement.click()
                    document.body.removeChild(downloadElement) // 下载完成移除元素
                    window.URL.revokeObjectURL(href) // 释放掉blob对象
                })
            },
            /**
             * 获取所有投资端名称
             */
            queryPaymentPlatform: function () {
                axios.get(coreBasePath + 'collection/queryPaymentPlatform', {
                        timeout: 0
                    })
                    .then(function (result) {
                        if (result.data.code == "1") {
                            vm.paymentPlatformList = result.data.data;
                        } else {
                            vm.$Modal.error({
                                content: result.data.msg
                            });
                        }
                    }).catch(function (error) {
                        vm.$Modal.error({
                            content: '接口调用异常!'
                        });
                    });
            },
            getCompanys: function () {
                axios.get(basePath + 'finance/getCompanys')
                    .then(function (res) {
                        if (res.data.code == '1') {
                            vm.companys = res.data.data
                        } else {
                            vm.$Message.error({
                                content: '获取分公司失败'
                            })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        vm.$Message.error({
                            content: '获取分公司失败'
                        })
                    })
            },
            getBusinessType: function () {
                axios.get(basePath + 'finance/getBusinessType')
                    .then(function (res) {
                        if (res.data.code == '1') {
                            res.data.data.forEach((e) => {
                                //过滤房贷车贷展期
                                if (e.businessTypeId != 1 && e.businessTypeId != 2) {
                                    vm.businessTypes.push(e)
                                }
                            })
                        } else {
                            avmpp.$Message.error({
                                content: '获取业务类型失败'
                            })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        vm.$Message.error({
                            content: '获取业务类型失败'
                        })
                    })
            },
            refresh: function () {
                vm.syncing = true;
                vm.$Message.loading({
                    content: '数据同步中,请稍等...',
                    duration: 0
                })
                axios.get(basePath + 'factRepay/synch', {
                        timeout: 0
                    })
                    .then(function (res) {
                        vm.$Message.destroy()
                        vm.syncing = false
                        if (res.data.code == '1') {
                            vm.$Modal.success({
                                content: '同步成功'
                            })
                        } else {
                            vm.$Modal.error({
                                content: '刷新失败'
                            })
                            console.log(res.data.msg);
                        }
                    })
                    .catch(function (err) {
                        vm.$Message.error({
                            content: '刷新失败'
                        })
                    })
            }
        },
        created: function () {
            // this.refresh()
            this.queryPaymentPlatform()
            this.getCompanys()
            this.getBusinessType()
        },
        updated: function () {
            this.loading = false;
        }
    })

})
