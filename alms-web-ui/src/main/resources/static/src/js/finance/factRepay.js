var layer, table, basePath, coreBasePath, vm;


window.layinit(function (htConfig) {
    basePath = htConfig.financeBasePath;
    coreBasePath = htConfig.coreBasePath;
    table = layui.table;

    vm = new Vue({
        el: "#app",
        data: {
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
            table: {
                loading:false,
                total: 0,
                data:[],
                col:[
                    {
                        title: '业务编号',
                        key:'businessId'
                    },
                    {
                        title: '期数',
                        key:'afterId'
                    },
                    {
                        title: '业务类型',
                        key:'extBusinessCtype'
                    },
                    {
                        title: '所属分公司',
                        key:'extCompanyName'
                    },
                    {
                        title: '客户名称',
                        key:'extCustomerName'
                    },
                    {
                        title: '所属投资端',
                        key:'extPlatform'
                    },
                    {
                        title: '还款方式',
                        key:'extRepayType'
                    },
                    {
                        title: '实收日期',
                        key:'repayDate'
                    },
                    {
                        title: '实收金额',
                        key:'factAmount'
                    },
                    {
                        title: '本金',
                        key:'extItem10'
                    },
                    {
                        title: '利息',
                        key:'extItem20'
                    },
                    {
                        title: '分公司服务费',
                        key:'extItem30'
                    },
                    {
                        title: '平台服务费',
                        key:'extItem50'
                    },
                    {
                        title: '滞纳金',
                        render:function(h,p){
                            return h('span',p.row.extItem60offline+p.row.extItem60online)
                        }
                    },
                    {
                        title: '其他费用',
                        key:'extOtherFee'
                    },
                    {
                        title: '操作人员',
                        key:'createUser'
                    },
                    
                ]
            },
            companys: [],
            businessTypes: [],
            paymentPlatformList:[]
        },
        methods: {
            paging: function (page) {
                if (page) {
                    this.form.curPage = page;
                }
                if (this.form.confirmTime) {
                    if (this.form.confirmTime[0] && this.form.confirmTime[0] != "") {
                        this.form.confirmStart = this.form.confirmTime[0];
                    }
                    if (this.form.confirmTime[1] && this.form.confirmTime[1] != "") {
                        this.form.confirmEnd = this.form.confirmTime[1];
                    }
                }
                vm.table.loading = true ;
                axios.post(basePath + 'factRepay/list', this.form).then(res => {
                    
                    console.log(res);
                    if(res.data.code){
                        vm.$Message.error({content:'查询实还记录失败'})
                    }else{
                        vm.table.data = res.data.records
                        vm.table.total = res.data.total
                    }

                    vm.table.loading = false ;
                })
            },
            exportExcel: function () {
                if (this.form.confirmTime) {
                    if (this.form.confirmTime[0] && this.form.confirmTime[0] != "") {
                        this.form.confirmStart = this.form.confirmTime[0];
                    }
                    if (this.form.confirmTime[0] && this.form.confirmTime[0] != "") {
                        this.form.confirmEnd = this.form.confirmTime[1];
                    }
                }
                axios.post(basePath + 'factRepay/export', this.form, {
                    responseType: 'blob'
                }).then(res => {
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
            queryPaymentPlatform: function(){
				axios.get(coreBasePath + 'collection/queryPaymentPlatform', {timeout: 0})
				.then(function(result){
					if (result.data.code == "1") {
						vm.paymentPlatformList = result.data.data;
					} else {
						vm.$Modal.error({ content: result.data.msg });
					}
				}).catch(function (error) {
					vm.$Modal.error({content: '接口调用异常!'});
            	});
            },
            getCompanys:function(){
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
            getBusinessType:function(){
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
            refresh:function(){
                axios.get(basePath + 'factRepay/synch')
                .then(function (res) {
                    if (res.data.code == '1') {
                        // vm.$Message.info({
                        //     content: '刷新成功'
                        // })
                    } else {
                        vm.$Message.error({
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
            this.refresh()
            this.queryPaymentPlatform()
            this.getCompanys()
            this.getBusinessType()
        },
        updated: function () {
            this.loading = false;
        }
    })

})
