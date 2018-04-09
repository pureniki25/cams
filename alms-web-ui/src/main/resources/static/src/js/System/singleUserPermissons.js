let app
let dateStart
let dateEnd
let table

window.layinit(function (htConfig) {
    basePath = htConfig.coreBasePath;
    table = layui.table;
    
    app = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () { }
    })
    
    table.render({
        elem: "#singleUserPermisson",
        height: 600 //容器高度
        ,
        cols: [
            [{
                title: '用户编号',
                field: 'userId'
            },
            {
                title: '用户姓名',
                field: 'userName'
            },
            {
                title: '业务编号',
                field: 'businessId'
            },
            {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/admin/queryAllUserInfo',
        page: true,
        done: (res, curr, count) => { }
    })

    table.on('tool(singleUserPermisson)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'set') {
            app.setSingleUserPermissons(data.userId)
        }
    })
});

let data = {
    schForm:{
        username:''
    }
}

let methods = {
	// 设置指定用户可访问业务对照关系	
	setSingleUserPermissons:function(userId){
        axios.get(basePath + 'sys/admin/singleUserPermission?userId=' + userId)
        .then(function(res){
            if(res.data.code=='1'){
            	app.$Modal.success({
				   content: '设置成功！'
			    });
            }else{
                app.$Modal.error({content:res.data.msg})
            }
        })
        .catch(function(err){
            app.$Modal.error({content: '接口调用失败！'})
        })
    },
    queryUserInfoByUsername:function(){
    	axios.get(basePath + 'sys/admin/queryUserInfoByUsername?username=' + data.schForm.username)
        .then(function(res){
            if(res.data.code!='1'){
            	app.$Modal.error({content:res.data.msg})
            }
        })
        .catch(function(err){
            app.$Modal.error({content: '接口调用失败！'})
        })
    }
}
