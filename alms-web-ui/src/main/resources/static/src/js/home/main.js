let main
let approval
let business
let notice
let aboutSys
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    basePath = htConfig.coreBasePath;
    let table = layui.table
    let element = layui.element
    main = new Vue({
        el: "#main",
        data: {
            notices: [],
            aboutSyss: [
                {
                    title: '贷后管理拍卖流程已上线',
                    date: '2018.3.30'
                }, {
                    title: '贷后管理减免流程功能已上线',
                    date: '2018.3.29'
                }
            ],
            noticeModal: {
                show: false,
                fileList: [
                    {
                        fileName: '贷后系统操作手册', url: 'http://www.baidu.com'
                    }
                ], notice: {

                }
            },
            approvalModal: {
                show: false,
                url: ''
            }
        },
        created: function () {
            axios.get(basePath + 'notice/list', {
                headers: { 'userId': 'admin-alms' },
            }).then(function (res) {
                if (res.data.code == '1') {
                    main.notices = res.data.data;
                } else {
                    main.$Modal.error({ content: '接口调用异常!' });
                }
            }).catch(function (e) {
                main.$Modal.error({ content: '接口调用异常!' });
            })
        },
        methods: {
            openNoticeModal: function (id) {
                console.log(id)
                axios.get(basePath + 'notice/get', {
                    params: { noticeId: id },
                }).then(function (res) {
                    console.log(res.data.data)
                    if (res.data.code == '1') {
                        main.noticeModal.notice = res.data.data.notice;
                        main.noticeModal.fileList = res.data.data.fileList;
                        main.noticeModal.show = true;
                    } else {
                        main.$Modal.error({ content: '接口调用异常!' });
                    }
                }).catch(function (e) {
                    main.$Modal.error({ content: '接口调用异常!' });
                })
            },
            openMore: function (url, title) {
                if (url && title) {
                    clickNavByText(title)
                    clickNavByUrl(url)
                }
            }

        },
        mounted: function () {
            console.log("开始调用用户信息接口");
            console.log(htConfig);
            $.ajax({
                type: "POST",
                url: "http://localhost:30606/"+"login/saveloginInfo",
                contentType: "application/json; charset=utf-8",
                async: false,
                dataType: "json",
                success: function () {
                    console.log("success");
                }
            });

        }
    })

    table.render({
        elem: '#approvalTable' //指定原始表格元素选择器（推荐id选择器）
        , id: 'approvalTable'
        , cols: [[

            {
                field: 'businessId',
                title: '业务编号'
            }, {
                field: 'customerName',
                title: '客户名称'
            }, {
                field: 'companyId',
                title: '所属分公司'
            }, {
                field: 'processTypeName',
                title: '流程类型'
            }, {
                fixed: 'right',
                title: '查看',
                align: 'center',
                toolbar: '#approvalTable_tool_bar'
            }
        ]], //设置表头
        url: basePath + 'processController/selectProcessWaitToApproveVoPage?reqPageeType=waitToApprove',
        //method: 'post' //如果无需自定义HTTP类型，可不加该参数
        //如果无需自定义请求参数，可不加该参数
        //response: {} //如果无需自定义数据响应名称，可不加该参数
        page: true,
        done: function (res, curr, count) {
            //数据渲染完的回调。你可以借此做一些其它的操作
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
        }
    });

    table.on('tool(approvalTable)', function (obj) {

        //设置申请减免的url路径
        var getDerateProcessUrl = function () {
            var url
            $.ajax({
                type: 'GET',
                async: false,
                url: basePath + 'ApplyDerateController/getApplyDerateInfoByProcessId?processId=' + obj.data.processId,
                headers: {
                    app: 'ALMS',
                    Authorization: "Bearer " + getToken()
                },
                success: function (data) {
                    if(data.code=='1'){
                        var applyDerateProcess = data.data;
                        url = '/collectionUI/applyDerateUI?businessId=' + obj.data.businessId + '&crpId=' + applyDerateProcess.crpId + "&processStatus=" + obj.data.processStatus + "&processId=" + obj.data.processId
                        main.approvalModal.show = true;
                    }else{
                        main.$Modal.error({ content: '接口调用异常!'+(data.msg||'') });
                    }
                },
                error: function () {
                    main.$Modal.error({ content: '接口调用异常!' });
                }
            });

            return url;
        }

        //设置车贷移交法务申请的url路径
        var getCarLoanProcessUrl = function () {
            var url
            $.ajax({
                type: 'GET',
                async: false,
                url: basePath + 'transferOfLitigation/getTransferLitigationCarByProcessId?processId=' + obj.data.processId,
                headers: {
                    app: 'ALMS',
                    Authorization: "Bearer " + getToken()
                },
                success: function (data) {
                    if(data.code=='1'){
                        url = '/transferOfLitigation/carLoan?businessId=' + obj.data.businessId + "&processStatus=" + obj.data.status + "&processId=" + obj.data.processId
                        main.approvalModal.show = true;
                    }else{
                        main.$Modal.error({ content: '接口调用异常!'+(data.msg||'') });
                    }
                },
                error: function () {
                    main.$Modal.error({ content: '接口调用异常!' });
                }
            });

            return url;
        }

        //设置房贷移交法务申请的url路径
        var getHouseLoanProcessUrl = function () {
            var url
            $.ajax({
                type: 'GET',
                async: false,
                url: basePath + 'transferOfLitigation/getTransferLitigationHouseByProcessId?processId=' + obj.data.processId,
                headers: {
                    app: 'ALMS',
                    Authorization: "Bearer " + getToken()
                },
                success: function (data) {
                    if(data.code=='1'){
                        url = '/transferOfLitigation/houseLoan?businessId=' + obj.data.businessId + "&processStatus=" + obj.data.processStatus + "&processId=" + obj.data.processId
                        main.approvalModal.show = true;
                    }else{
                        main.$Modal.error({ content: '接口调用异常!'+(data.msg||'') });
                    }
                },
                error: function () {
                    main.$Modal.error({ content: '接口调用异常!' });
                }
            });

            return url;
        }

        if (obj.event === 'info') {
            //设置申请减免的url路径
            if (obj.data.processTypeCode == "derate") {
                main.approvalModal.url = getDerateProcessUrl();
            } else if (obj.data.processTypeCode == "houseLoanLitigation") {
                main.approvalModal.url = getHouseLoanProcessUrl();
            } else if (obj.data.processTypeCode == "carLoanLitigation") {
                main.approvalModal.url = getCarLoanProcessUrl();
            }else if(obj.data.processTypeCode=="carAuctionAply"){
				var title ="车辆拍卖申请审核";
				var url = '/assets/car/carAuctionAudit?businessId='+obj.data.businessId+"&processStatus="+obj.data.processStatus+"&processId="+obj.data.processId;
				layer.open({
                    type: 2,
                    area: ['95%', '95%'],
                    fixed: false,
                    maxmin: true,
                    title: title,
                    content: url
                });
            }
        }

        
    });

    table.render({
        elem: '#businessTable' //指定原始表格元素选择器（推荐id选择器）
        , id: 'businessTable'
        , cols: [[
            {
                field: 'businessId',
                title: '业务编号',
                width: 100
            }, {
                field: 'customerName',
                title: '客户名称',
                width: 100
            }, {
                field: 'periods',
                title: '期数',
                width: 80
            }, {
                field: 'companyId',
                title: '所属分公司',
                width: 100
            }, {

                field: 'totalBorrowAmount',
                title: '本期应还金额',
                width: 100
            }, {

                field: 'delayDays',
                title: '逾期天数',
                width: 100
            }, {
                title: '操作',
                align: 'center',
                toolbar: '#businessTable_tool_bar'
            }
        ]], //设置表头
        url: basePath + 'collection/selectALStandingBookVoPage',
        //method: 'post' //如果无需自定义HTTP类型，可不加该参数
        request: {}, //如果无需自定义请求参数，可不加该参数
        //response: {} //如果无需自定义数据响应名称，可不加该参数
        page: true,
        done: function (res, curr, count) {
            //数据渲染完的回调。你可以借此做一些其它的操作
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
        }
    });

    table.on('tool(businessTable)', function (obj) {
        if (obj.event === 'info') {
            var showOneLineOprLayer = function (url, title) {
                // vm.edit_modal = false;
                var openIndex = layer.open({
                    type: 2,
                    area: ['95%', '95%'],
                    fixed: false,
                    maxmin: true,
                    title: title,
                    content: url
                });


                // layer.open({
                //     type: 2,
                //     title: title,
                //     maxmin: true,
                //     // area: ['1450px', '800px'],
                //     // area: ['95%', '95%'],
                //     content:url
                // });
            }

            if (obj.data.businessTypeId == 9) {
                //车贷
                axios.get(basePath + 'api/getXindaiCarView?businessId=' + obj.data.businessId)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            showOneLineOprLayer(res.data.data, "车贷详情");
                        } else {
                        	main.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                        }
                    })
                    .catch(function (error) {
                    	main.$Modal.error({ content: '接口调用异常!' });
                    });
            } else if (obj.data.businessTypeId == 11) {
                //房贷
                axios.get(basePath + 'api/getXindaiHouseView?businessId=' + obj.data.businessId)
                    .then(function (res) {
                        if (res.data.code == "1") {
                            showOneLineOprLayer(res.data.data, "房贷详情");
                        } else {
                        	main.$Modal.error({ content: '操作失败，消息：' + res.data.msg });
                        }
                    })
                    .catch(function (error) {
                        main.$Modal.error({ content: '接口调用异常!' });
                    });
            }
        }
    });

});
