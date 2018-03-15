let app
let dateStart
let dateEnd
window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    let table = layui.table
    let laydate = layui.laydate

    app = new Vue({
        el: "#app",
        data: {
            checkLogModal: {
                show: false
            },
            editModal: {
                show: true
            },
            previewModal: {
                show: false
            },
            schForm: {
                title: '',
                startDate: '',
                endDate: ''
            },
            editForm:{
                noticeId:'',
                title:'',
                type:'',
                readTime:'',
                readTimeUnit:1,
                orgCode:'',
                publishChannel:'',
                publishDate:'',
                content:'',
                attachment:''
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
            }
        },
        created: function () {

        },
        methods: {
            onDateStartChange: function (date) {
                dateStart = date
            },
            onDateEndChange: function (date) {
                dateEnd = date
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
                toolbar:"#toolbar"
            }
        ]], //设置表头
        url: basePath + 'notice/page',
        page: true,
        done: (res, curr, count) => {
        }
    })
})