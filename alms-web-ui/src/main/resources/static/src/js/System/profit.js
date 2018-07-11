let app
let dateStart
let dateEnd
let table
let businessTypeId
let itemType
let profitItemSetId

window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    table = layui.table
    let laydate = layui.laydate

    app = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () { }

    })

    table.render({
        elem: "#profit",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'businessTypeId',
                title: '序号'
            }, {
                field: 'businessTypeName',
                title: '业务类型'
            }, 
            {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/profit/list',
        page: false,
        done: (res, curr, count) => { }
    })

    table.on('tool(profit)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'delete') {
            app.delete(data)
        }
        if (event == 'edit') {debugger
            app.openModal(data)
        }
    })
})

let data = {
	itemFeeFlag:false,
	fee_add_modal:false,
	itemTypeFlag:false,
    add_modal:false,
    submitLoading:false,
    itemTypes:[],
    feeTypes:[]
  
}

let methods = { 
    tableReload: function () {
        table.reload('role')
    }, 
    delete: function (data) {
        axios.post(basePath + 'sys/role/delete', data)
            .then(function (r) {
                if (r.data.code == '0') {
                    app.tableReload()
                }
            })
            .catch(function (r) {
                app.$Modal.error({content:'调用接口失败'})
            })
    },
    openModal: function(data){debugger
        let _this = this 
        businessTypeId=data.businessTypeId;
        axios.get(basePath+'sys/profit/itemTypeList?businessTypeId='+businessTypeId)
        .then(function (res) {debugger
            if (res.data.code == "0") {
                data = res.data.data 
                _this.itemTypes = data
                _this.add_modal = true 
                _this.itemTypeFlag=false
                
            } else if(res.data.code == "1"){
                data = res.data.data 
                _this.itemTypes = data
                _this.add_modal = true 
                _this.itemTypeFlag=true
            }else {
                app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            app.$Modal.error({content: '接口调用异常!'});
        });
    },closeModal:function(){
        app.editModal.show = false
        app.$refs['editForm'].resetFields()
    },saveItem: function () {debugger
    	var type;
        if(app.itemTypeFlag==true){
        	type="edit";
        }else{
        	type="add";
        }
                
                $.ajax({
		               type: "POST",
		               url: basePath+'sys/profit/saveItem',
		               contentType: "application/json; charset=utf-8",
		               data: JSON.stringify({"itemTypes":app.itemTypes,"businessTypeId":businessTypeId,"type":type}),
		               success: function (res) {
		            	   if (res.code == "1"){
		            		   app.$Modal.success({
	                                content: "保存成功",
	                            });
		            		   app.add_modal=false
		            	   }else{
		            		   app.$Modal.error({content: '操作失败，消息：' + res.msg});
		            	   }
		               },
		               error: function (message) {
		            	   app.$Modal.error({content: '接口调用异常!'});
		                   console.error(message);
		               }
		           });
   
    },openFeeModal: function(data){debugger
        let _this = this 
        businessTypeId=data.businessTypeId;
        itemType=data.itemType;
        profitItemSetId=data.profitItemSetId;
        axios.get(basePath+'sys/profit/feeTypeList?businessTypeId='+businessTypeId+"&itemType="+data.itemType+"&profitItemSetId="+data.profitItemSetId)
        .then(function (res) {debugger
            if (res.data.code == "0") {
                data = res.data.data 
                _this.feeTypes = data
                _this.fee_add_modal = true 
                _this.feeTypeFlag=false
                
            } else if(res.data.code == "1"){
                data = res.data.data 
                _this.feeTypes = data
                _this.fee_add_modal = true 
                _this.feeTypeFlag=true
            }else {
                app.$Modal.error({content: '操作失败，消息：' + res.data.msg});
            }
        })
        .catch(function (error) {
            app.$Modal.error({content: '接口调用异常!'});
        });
    },saveFee: function () {debugger
	    var type;
	    if(app.feeTypeFlag==true){
	    	type="edit";
	    }else{
	    	type="add";
	    }
            $.ajax({
	               type: "POST",
	               url: basePath+'sys/profit/saveFee',
	               contentType: "application/json; charset=utf-8",
	               data: JSON.stringify({"feeTypes":app.feeTypes,"businessTypeId":businessTypeId,"profitItemSetId":profitItemSetId,"type":type}),
	               success: function (res) {
	            	   if (res.code == "1"){
	            		   app.$Modal.success({
                                content: "保存成功",
                            });
	            		   app.fee_add_modal=false
	            	   }else{
	            		   app.$Modal.error({content: '操作失败，消息：' + res.msg});
	            	   }
	               },
	               error: function (message) {
	            	   app.$Modal.error({content: '接口调用异常!'});
	                   console.error(message);
	               }
	           });

}
}