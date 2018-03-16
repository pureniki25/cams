let app
let dateStart
let dateEnd

let readTimeValid = function () {
    return app && app.type == '必读'
}
window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    let table = layui.table
    let laydate = layui.laydate


    app = new Vue({
        el: "#app",
        data: {
            deptIds: '',
            checkLogModal: {
                show: false
            },
            editModal: {
                show: false,
                title: ''
            },
            previewModal: {
                show: false
            },
            schForm: {
                title: '',
                startDate: '',
                endDate: ''
            },
            editForm: {
                noticeId: '',
                title: '',
                type: '',
                readTime: '',
                readTimeUnit: 1,
                orgCode: [],
                publishChannel: '',
                publishDate: '',
                content: '',
                attachment: [

                ]
            }, dateEndOption: {
                disabledDate(date) {
                    if (dateStart) {
                        return date < new Date(dateStart)
                    } else {
                        return false
                    }
                }
            },
            dateStartOption: {
                disabledDate(date) {
                    if (dateEnd) {
                        return date > new Date(dateEnd)
                    } else {
                        return false
                    }
                }
            },
            readTimeRequired: '',
            ruleValidate: {
                title: [
                    { required: true, message: '标题不能为空', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '公告类型不能为空', trigger: 'blur' },
                ],
                readTime: [
                    { required: false, message: '必读时间不能为空', trigger: 'blur' }
                ],
                orgCode: [
                    { required: true, type: 'array', message: '公告范围不能为空', trigger: 'blur' }
                ],
                publishChannel: [
                    { required: true, message: '公告发布渠道不能为空', trigger: 'blur' },
                ],
                publishDate: [
                    { required: true, type: 'date', message: '公告发布时间不能为空', trigger: 'blur' }
                ],
                content: [
                    { required: true, message: '公告内容不能为空', trigger: 'change' }
                ]
            },
            upload: {
                url: basePath + "notice/uploadAttachment",
                headers: {
                    app: axios.defaults.headers.common['app'],
                    Authorization: axios.defaults.headers.common['Authorization'],
                    userId: axios.defaults.headers.common['userId']
                },
                data: {
                    businessId: '通知公告',

                },
            }
        },
        watch: {
            // 监听 edtiForm.type 变化修改校验规则
            editForm: {
                //注意：当观察的数据为对象或数组时，curVal和oldVal是相等的，因为这两个形参指向的是同一个数据对象
                handler(curVal, oldVal) {
                    if (curVal.type == "1") {
                        this.ruleValidate.readTime = [
                            { required: true, message: '必读时间不能为空', trigger: 'blur' }
                        ],
                            this.readTimeRequired = 'ivu-form-item-required'
                    } else {
                        this.ruleValidate.readTime = [
                            { required: false, message: '必读时间不能为空', trigger: 'blur' }
                        ]
                        this.readTimeRequired = ''
                    }
                },
                deep: true
            },
        },
        created: function () {
            let arry = []
            axios.get(basePath + "renewalBusiness/deptIds").then(function (res) {
                if (res.data.code == '1') {
                    $.each(res.data.data, function (i, o) {
                        arry.push({ value: o.orgNameCn, label: o.orgNameCn })
                    });
                    app.deptIds = arry
                }
            }).catch(function (error) {

            })
        },
        methods: {
            onDateStartChange: function (date) {
                dateStart = date
            },
            onDateEndChange: function (date) {
                dateEnd = date
            },
            openEditorModal: function (action, noticeId) {
                if (action == 'add') {
                    this.$refs['editForm'].resetFields();
                    this.editModal.show = true
                    this.editModal.title = '新增公告'
                } else if (action == 'update') {
                    this.editModal.show = true
                    this.editModal.title = '编辑公告'
                }
            },
            onSaveNotice: function () {
                this.$refs['editForm'].validate((valid) => {
                    if (valid) {
                        let f = false
                        app.editForm.attachment.forEach(function (item, i) {
                            if (item.noticeFileUrl == '') {
                                app.$Message.error('附件' + (i + 1) + "没有上传附件");
                                f = true
                            }
                        })
                        if (f) {
                            app.$Message.error('保存失败!');
                            return false;
                        }

                        let fileJsonString = []
                        app.editForm.attachment.forEach(function (item) {
                            let t = {
                                noticeFileId: item.noticeFileId || '',
                                fileUrl: item.noticeFileUrl || '',
                                fileName: item.noticeFileNewName || item.noticeFileName
                            }
                            fileJsonString.push(t)
                        })
                        console.log(fileJsonString)
                    	/*$.post(basePath+'notice/save',{
                            notice:app.editForm,
                            fileJsonString:fileJsonString
                        },function(json){

                        });*/
                        let oc = ''
                        app.editForm.orgCode.forEach(function (item, i) {
                            if (i == 0) {
                                oc += item
                            } else {
                                oc += ',' + item
                            }
                        })
                        let notice = {
                            noticeId: app.editForm.noticeId,
                            noticeTitle: app.editForm.title,
                            noticeContent: app.editForm.content,
                            hasRead: parseInt(app.editForm.type),
                            hasReadTime: app.editForm.readTime == '' ? 0 : parseInt(app.editForm.readTime),
                            hasReadTimeUnit: 1,
                            orgCode: oc,
                            publishChannel: app.editForm.publishChannel,
                            publishTime: app.editForm.publishDate
                        }
                        let files = fileJsonString
                        var DATE = new Date();
                        var year = DATE.getFullYear();
                        var month = DATE.getMonth()+1;
                        var day = DATE.getDate();
                        function dateFormat(DATE) {
                            var DATE = new Date();
                            var year = DATE.getFullYear();
                            var month = DATE.getMonth() + 1;
                            var day = DATE.getDate();
                            return year + "-" + month + "-" + day
                        }
                        let dto = {
                        	noticeId: app.editForm.noticeId,
                            noticeTitle: app.editForm.title,
                            noticeContent: app.editForm.content,
                            hasRead: parseInt(app.editForm.type),
                            hasReadTime: app.editForm.readTime == '' ? 0 : parseInt(app.editForm.readTime),
                            hasReadTimeUnit: 1,
                            orgCode: oc,
                            publishChannel: app.editForm.publishChannel,
                            publishTime: dateFormat(app.editForm.publishDate),
                        	files:files
                        }
                        $.ajax({
                            type: "POST",
                            url:basePath+'notice/save',
                            data: JSON.stringify(dto),
                            contentType: "application/json; charset=utf-8",
                            success: function (message) {
                                if(message.code=='1'){
                                    app.$Message.success('保存成功!');
                                    app.editModal.show = false
                                }else{
                                    app.$Message.erro('保存失败!');
                                }
                            },
                            error: function (message) {
                                console.error(message);
                            }
                        });
                    } else {
                        app.$Message.error('表单验证失败!');
                    }
                })
            },
            addUploadItem: function () {
                let uploadItem = {
                    id: (this.editForm.attachment.length + 1),
                    noticeFileId: '',
                    noticeFileUrl: '',
                    noticeFileName: '',
                    noticeFileNewName: ''
                }
                this.editForm.attachment.push(uploadItem);
            },
            delUploadItem: function (id) {
                this.editForm.attachment = this.editForm.attachment.filter(function (item) {
                    return item.id != id
                })

                this.editForm.attachment.forEach(function (item, i) {
                    item.id = i + 1
                })
            },
            beforeUpload: function (file) {
                this.upload.data.fileName = file.name
            },
            onUploadSuccess: function (response, file, fileList) {
                let uploadItemId = response.message;
                let docItemListJson = JSON.parse(response.docItemListJson)
                this.editForm.attachment.forEach(function (item, i) {
                    if (item.id == uploadItemId) {
                        item.noticeFileName = file.name
                        item.noticeFileUrl = docItemListJson.docUrl
                        return false
                    }
                })
            },
            onUploadError: function () { },
            onFormatError: function () { },
            onExceededSize: function () { },
            closeEditModal: function () {
            },
            openReviewModal: function () {

            },
            onDateChange:function(date){
            	app.editForm.publishDate = date
            }
        },
    })
    table.render({
        elem: "#notice"
        , height: 600 //容器高度
        , cols: [[
            {
                field: 'noticeTitle',
                title: '标题'
            }, {
                field: 'hasRead',
                title: '公告类型',
                templet: function (d) {
                    return d.hasRead && d.hasRead == 1 ? '必读' : '选读'
                }
            }, {
                field: 'publishUserId',
                title: '发布人'
            }, {
                field: 'publishTime',
                title: '发布人'
            }, {
                title: '操作',
                toolbar: "#toolbar"
            }
        ]], //设置表头
        url: basePath + 'notice/page',
        page: true,
        done: (res, curr, count) => {
        }
    })
})