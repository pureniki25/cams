let app
let dateStart
let dateEnd
let table
let tableIns
var areaId;
window.layinit(function (htConfig) {
    let _htConfig = htConfig
    basePath = htConfig.coreBasePath
    table = layui.table
    let laydate = layui.laydate

    app = new Vue({
        el: "#app",
        data: data,
        methods: methods,
        created: function () { 
            this.listArea();
        }
    })

    tableIns = table.render({
        elem: "#userarea",
        height: 600 //容器高度
        ,
        cols: [
            [{
                field: 'orgCode',
                title: '所在公司'
            }, {
                field: 'userId',
                title: '用户ID',
            }, {
                field: 'userName',
                title: '姓名'
            }, {
                field: 'areaName',
                title: '管理公司/区域名',
                
            }, {
                field: 'areaId',
                title: '管理公司/区域名id'
            }, {
                field: 'areaLevel',
                title: '类型',
                templet: function (d) {
                    return d.areaLevel == '40' ? '区域' : '公司';
                }
            }, {
                title: '操作',
                toolbar: "#barTools"
            }]
        ], //设置表头
        url: basePath + 'sys/userarea/page',
        page: true,
        done: (res, curr, count) => { }
    })

    table.on('tool(userarea)', function (o) {
        let data = o.data;
        let event = o.event;
        if (event == 'delete') {
            app.delete(data)
        }
    })
})

let data = {
    schForm:{
        userId:'',
        userName:'',
        areaId:'',
        areaLevel:'',
    },
    addSysUserAreaModal:{
        show:false
    },
    addForm :{
        userId:'',
        areaLevel:'',
        areaId:[],
        //areaList:[]
    },
    selectAreaAndCity: false,
    index:'0',
    editCompanyCollectionAssignSet:
    {
        editCompanyCollectionForm:
            {
                areaName:[],
                companyId:[],
                collectionGroup1Users:[],
                collectionGroup2Users:[]
            },
        collectionGroup1AllUsers: [],
        collectionGroup2AllUsers: [],
        areaList: [],
        companyId: [],
        businessTypes:[]
    },
    areaAndCity:{
        allAreas:[{
            title: '鸿特信息咨询有限公司',
            loading: false,
            children: [],
            expand:true
        }],
        test:"",
        allCitys:[],
        allSelectedAreas:[],
        allSelectedCitys:[],
        selectedCitys:[],
        currentSelectedCitys:[],
        allSelectedCityGroup:[],
        currentCitys1:[],
        currentCitys2:[],
        currentCitys3:[],
        currentCitys4:[],
        currentCitys5:[],
        currentCitys6:[],
        currentCitys7:[],
        currentAreaId:""
    },
}

let methods = {
    selectArea(){
        var self = this;
        self.selectAreaAndCity = true;
        self.areaAndCity.allCitys=[];

        //var formData = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.areaName;// 表单区域
        //var treeData = self.areaAndCity.allAreas[0].children;// 下拉区域

        self.getArea();
    },
    getArea:function () {
        var self = this;
        var url = basePath + "sys/userarea/getAreaList";
        $.ajax({
            type: "GET",
            url: url,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                self.areaAndCity.allAreas[0].children = data.data;
                areaId = self.areaAndCity.allAreas[0].children[0].areaId;
                self.getCompany(areaId);
            }

        });
    },
    getCompany: function (areaPid) {
        var self = this;
        var url = basePath + "sys/userarea/getCompanyList?areaPid="+areaPid;
        $.ajax({
            type: "GET",
            url: url,
            contentType: "application/json; charset=utf-8",
//                        async: true,
            dataType: "json",
            success: function (data) {
                self.areaAndCity.allCitys = data.data;
                var formDate = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId;
                for(var i=0;i<formDate.length;i++){
                    var exit = self.areaAndCity.allCitys.indexOf(formDate[i]) != -1;
                    if(exit){
                        self.areaAndCity.currentSelectedCitys.push(formDate[i]);
                    }
                }
            }

        });
    },
 showCitys:function (data) {
        var self = this;
        var index = (function () {
            if (event.target.className === "ivu-checkbox-input") return $(event.target).parents('li').find('ul').index($(event.target).parents('ul')[0]);
        })();
        //保存已选中的区域,清除取消勾选的区域
        var selectedData = self.$refs.tree.getCheckedNodes();

        self.areaAndCity.allSelectedAreas = [];
        for (var i = 0; i < selectedData.length ; i++) {
            self.areaAndCity.allSelectedAreas.push(selectedData[i].areaId);
        }
        this.getCompany(this.areaAndCity.allAreas[0].children[index].areaId);
        self.index = index;
        if(selectedData.length > 0){
            for(var i=0;i < selectedData.length;i++){
                var areaId = selectedData[i].areaId;
                //判断当前树节点是否被选中
                if(self.areaAndCity.allAreas[0].children[index].areaId == areaId){//当前点击的节点id在已被选中的树节点id中，则为已选中
                    //已被选中
                    if(index == 0){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys1;
                    }else if(index == 1){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys2;
                    }else if(index == 2){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys3;
                    }else if(index == 3){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys4;
                    }else if(index == 4){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys5;
                    }else if(index == 5){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys6;
                    }else if(index == 6){
                        self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys7;
                    }
                }else {
                    if(index == 0){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys1 = [];
                    }else if(index == 1){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys2 = [];
                    }else if(index == 2){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys3 = [];
                    }else if(index == 3){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys4 = [];
                    }else if(index == 4){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys5 = [];
                    }else if(index == 5){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys6 = [];
                    }else if(index == 6){
                        self.areaAndCity.currentSelectedCitys = [];
                        self.areaAndCity.currentCitys7 = [];
                    }
                }
            }
        }else {
            self.areaAndCity.currentSelectedCitys = [];
            self.areaAndCity.currentCitys1 = [];
            self.areaAndCity.currentCitys2 = [];
            self.areaAndCity.currentCitys3 = [];
            self.areaAndCity.currentCitys4 = [];
            self.areaAndCity.currentCitys5 = [];
            self.areaAndCity.currentCitys6 = [];
            self.areaAndCity.currentCitys7 = [];
        }

    },
     getAllSelectedCitys:function (data) {
        var self = this;
        var index = self.index;


        if(data.length > 0){
            self.areaAndCity.allAreas[0].children[index].checked = true;
            var exit = self.areaAndCity.allSelectedAreas.indexOf(self.areaAndCity.allAreas[0].children[index].areaId) != -1;
            if(!exit){
                self.areaAndCity.allSelectedAreas.push(self.areaAndCity.allAreas[0].children[index].areaId);
            }

        }else {
            self.areaAndCity.allAreas[0].children[index].checked = false;
            self.areaAndCity.allSelectedAreas.splice(index,1);
        }

        console.log("所有被选中的区域：======",self.areaAndCity.allSelectedAreas);



        if(index == 0){
            self.areaAndCity.currentCitys1 = [];
            self.areaAndCity.currentCitys1 = data;

        }else if(index == 1){
            self.areaAndCity.currentCitys2 = [];
            self.areaAndCity.currentCitys2 = data;
        }else if(index == 2){
            self.areaAndCity.currentCitys3 = [];
            self.areaAndCity.currentCitys3 = data;
        }else if(index == 3){
            self.areaAndCity.currentCitys4 = [];
            self.areaAndCity.currentCitys4 = data;
        }else if(index == 4){
            self.areaAndCity.currentCitys5 = [];
            self.areaAndCity.currentCitys5 = data;
        }else if(index == 5){
            self.areaAndCity.currentCitys6 = [];
            self.areaAndCity.currentCitys6 = data;
        }else if(index == 6){
            self.areaAndCity.currentCitys7 = [];
            self.areaAndCity.currentCitys7 = data;
        }
        console.log("第1组",self.areaAndCity.currentCitys1);
        console.log("第2组",self.areaAndCity.currentCitys2);
        console.log("第3组",self.areaAndCity.currentCitys3);
        console.log("第4组",self.areaAndCity.currentCitys4);
        console.log("第5组",self.areaAndCity.currentCitys5);
        console.log("第6组",self.areaAndCity.currentCitys6);
        console.log("第7组",self.areaAndCity.currentCitys7);


    },
    confirmSelect:function () {
        var self = this;
        self.editCompanyCollectionAssignSet.editCompanyCollectionForm.areaName = self.areaAndCity.allSelectedAreas;
        self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId=[];
        for(var i=0;i<self.areaAndCity.currentCitys1.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys1[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys1[i]);
            }

        }
        for(var i=0;i<self.areaAndCity.currentCitys2.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys2[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys2[i]);
            }
        }
        for(var i=0;i<self.areaAndCity.currentCitys3.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys3[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys3[i]);
            }
        }
        for(var i=0;i<self.areaAndCity.currentCitys4.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys4[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys4[i]);
            }
        }
        for(var i=0;i<self.areaAndCity.currentCitys5.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys5[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys5[i]);
            }
        }
        for(var i=0;i<self.areaAndCity.currentCitys6.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys6[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys6[i]);
            }
        }
        for(var i=0;i<self.areaAndCity.currentCitys7.length;i++){
            var exit = self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.indexOf(self.areaAndCity.currentCitys7[i]) != -1;
            if(!exit){
                self.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId.push(self.areaAndCity.currentCitys7[i]);
            }
        }

    },
    selectChild:function (data) {
        var self = this;
        var indexqj = self.index;
        var num = self.areaAndCity.allAreas[0].children.indexOf(data[0]);

        if(data.length>0){


            self.index = num;
            if(num == 0){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys1;
            }else if(num == 1){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys2;
            }else if(num == 2){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys3;
            }else if(num == 3){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys4;
            }else if(num == 4){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys5;
            }else if(num == 5){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys6;
            }else if(num == 6){
                self.areaAndCity.currentSelectedCitys = self.areaAndCity.currentCitys7;
            }

            this.getCompany(data[0].areaId);
        }


    }, 
    search:function(){
        app.tableReload()
    },
    delete:function(data){
        axios.post(basePath+'sys/userarea/del',data)
        .then(function(res){
            if(res.data.code=='1'){
                app.tableReload()
            }else{
                app.$Modal.error({content:'调用sys/userarea/del接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用sys/userarea/del接口失败'})
        })
    },
    tableReload:function(){
        let p = {}
        Object.keys(app.schForm).forEach(function(val,i){
            console.log(val)
            if(app.schForm[val]!=''){
                p[val]=app.schForm[val]
            }
        });
        tableIns.reload({
            where:p,
            page:{curr:1}
        })
    },
    listArea:function(){
        /* axios.get(basePath+'sys/userarea/listArea',{params:{areaLevel:app.addForm.areaLevel}})
        .then(function(res){
            if(res.data.code=='1'){
                app.addForm.areaList = res.data.data
            }else{
                app.$Modal.error({content:'调用sys/userarea/del接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用sys/userarea/del接口失败'})
        }) */

        //获公司和区域信息
        axios.get(basePath+'sys/userarea/listArea', {params:{areaLevel:40} })
        .then(res=>{
            if(res.data.code == '1'){
               // this.addForm.areaList = res.data.data;
               this.editCompanyCollectionAssignSet.areaList = res.data.data;
            }else{
                this.$Modal.error({content:'请求接口失败, 消息：'+ res.data.msg});
            }
        })
        .catch(err=>{
            this.$Modal.error({content:'操作失败!'});
        });
        axios.get(basePath+'sys/userarea/listArea', {params:{areaLevel:60} })
        .then(res=>{
            if(res.data.code == '1'){
                //this.addForm.areaList = res.data.data;
                this.editCompanyCollectionAssignSet.companyId = res.data.data;
            }else{
                this.$Modal.error({content:'请求接口失败, 消息：'+ res.data.msg});
            }
        })
        .catch(err=>{
            this.$Modal.error({content:'操作失败!'});
        });
    },
    openAddModal:function(){
        app.$refs['addForm'].resetFields()
        app.addForm.areaList = []
        app.addForm.areaLevel = ''
        app.addForm.userId = ''
        app.addSysUserAreaModal.show = true

        app.editCompanyCollectionAssignSet.editCompanyCollectionForm.areaName=[];
        app.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId=[];
    },
    add:function(){
        let p = {}
        p.userId = app.addForm.userId
        if(!p.userId){
            app.$Modal.error({content:"新增的userId不能为空"});
            return
        }
        p.sequence = 40 //app.addForm.areaLevel
        p.orgCode = []//app.addForm.areaId
        let companyIds = this.editCompanyCollectionAssignSet.editCompanyCollectionForm.companyId;
        let areaIds = this.editCompanyCollectionAssignSet.editCompanyCollectionForm.areaName;
        if(areaIds.length==0 && companyIds.length==0){
            this.$Modal.error({content: '请至少选择一个区域或者公司'})
            return
        }
        if(areaIds.length > 0 && companyIds.length == 0){
            p.sequence = 40; //区域
            Object.assign(p.orgCode, areaIds)
        }else{
            p.sequence = 60; //公司
            Object.assign(p.orgCode, companyIds);
        }

        if(!p.sequence){
            this.$Modal.error({content: '新增的sequence不能为空'})
            return
        }

        let ps = [];
        p.orgCode.forEach(e=>{
            let tp = {};
            tp.userId = p.userId;
            tp.sequence = p.sequence;
            tp.orgCode = e;
            ps.push(tp);
        });


        axios.post(basePath+'sys/userarea/add',ps)
        .then(function(res){
            if(res.data.code=='1'){
                app.tableReload()
                app.addSysUserAreaModal.show = false
            }else{
                app.$Modal.error({content:'调用接口失败'})
            }
        })
        .catch(function(err){
            app.$Modal.error({content:'调用接口异常'})
        })
    }
}