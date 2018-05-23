/**
 * 
 */
window.app;
window.layinit(function (htConfig) {
    var _htConfig = htConfig;
    var businessId = getQueryStr('businessId')
    var afterId = getQueryStr('afterId')
    var planListId = getQueryStr('planListId')
    let cpath = htConfig.coreBasePath;
    let fpath = htConfig.financeBasePath;
    var layer = layui.layer;
    let curIndex ;
    app = new Vue({
        el: "#app",
        data: {
            repayInfo: {
                url: '/finance/repayBaseInfo?businessId=' + businessId + "&afterId=" + afterId,
            },
            repayRegList: {
                url: '/finance/repayRegList?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            confirmedRepayment:{
                url: '/finance/confirmedRepayment?businessId=' + businessId + "&afterId=" + afterId,
                style: {}
            },
            matchedBankStatement: {
                show: false,
                url: '/finance/matchedBankStatement?businessId=' + businessId + "&afterId=" + afterId,
                style: {},
                data:11
            },
            manualAddBankSatements: {
                show: false,
                url: '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId,

            },
            manualMatchBankSatements: {
                show: false,
                url: '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId,
            },

            // style: {
            //     repayRegList: {},
            //     matchedBankStatement: {}
            // },

            table:{
                currPeriodRepayment:currPeriodRepayment,
                projRepayment:projRepayment
            },
            thisTimeRepaymentInfo:{
                derate:'',
                item10:'',
                item20:'',
                item30:'',
                item30:'',
                item50:'',
                offlineOverDue:'',
                onlineOverDue:'',
                overDays:'',
                repayDate:'',
                subtotal:'',
                total:'',
                derateDetails:[]
            },
            factRepaymentInfo:{
                repayDate:'',
                surplusFund:'',
                onlineOverDuel:'',
                offlineOverDue:'',
                remark:'',
                canUseSurplus:0,
                moneyPoolAccount:0,
                repayAccount:0,
                mprids:[]
            },
        },
        watch:{
            'matchedBankStatement.data':function(n){
                let moneyPoolAccount = 0 
                if(n&&n.length>0){
                    n.forEach(element => {
                        moneyPoolAccount += element.accountMoney
                        app.factRepaymentInfo.mprids.push(element.moneyPoolId);
                    });
                    let o = n[n.length-1]
                    app.factRepaymentInfo.repayDate=o.tradeDate
                }
                app.factRepaymentInfo.moneyPoolAccount = moneyPoolAccount 
                app.factRepaymentInfo.repayAccount = app.factRepaymentInfo.moneyPoolAccount + (app.factRepaymentInfo.surplusFund||0)
            },
            'factRepaymentInfo.surplusFund':function(n){
                if(n&&!isNaN(n)){
                    app.factRepaymentInfo.repayAccount = app.factRepaymentInfo.moneyPoolAccount + (app.factRepaymentInfo.surplusFund||0)
                }
            },
            'factRepaymentInfo.repayAccount':function(n){
                app.previewConfirmRepayment()
            }
        },
        methods: {
            closeModal: function (target) {
                if (app[target]) {
                    app[target] = false;
                }else{
                    layer.close(curIndex)
                }
            },
            openModal: function (target) {
                if (!app[target]) {
                    app[target] = true
                }
            },
            configModalStyle: function (target, style) {
                if (app[target].style) {
                    app[target].style = style
                }
            },
            openMatchBankStatementModal(p) {
                let url = '/finance/manualMatchBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动匹配流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        curIndex = index ;
                    }
                })
            },
            openAddBankStatementModal() {
                let url = '/finance/manualAddBankSatements?businessId=' + businessId + "&afterId=" + afterId;
                layer.open({
                    type: 2,
                    title: '手动新增流水',
                    content: [url, 'no'],
                    area: ['1600px', '800px'],
                    success: function (layero, index) {
                        curIndex = index ;
                    }
                })
            },
            getThisTimeRepayment(){
                axios.get(fpath+'finance/thisTimeRepayment?businessId=' + businessId + "&afterId=" + afterId)
                .then(function(res){
                    if(res.data.code=='1'){
                        app.thisTimeRepaymentInfo =  Object.assign(app.thisTimeRepaymentInfo,res.data.data);
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){
                    app.$Message.error({content:'获取本次还款信息失败'})
                })
            },
            getThisPeroidRepayment(){
                axios.get(fpath+'finance/thisPeroidRepayment?businessId=' + businessId + "&afterId=" + afterId)
                .then(function(res){
                    if(res.data.code=='1'){
                        app.table.currPeriodRepayment.data =  Object.assign(app.table.currPeriodRepayment.data,res.data.data);
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){
                    app.$Message.error({content:'获取本期还款信息失败'})
                })
            },
            getSurplusFund(){
                axios.get(fpath+'finance/getSurplusFund?businessId=' + businessId + "&afterId=" + afterId)
                .then(function(res){
                    if(res.data.code=='1'){
                        app.factRepaymentInfo.canUseSurplus = res.data.data
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                })
                .catch(function(err){
                    app.$Message.error({content:'获取结余信息失败'})
                })
            },
            previewConfirmRepayment(){
                // private String businessId ;
                // private String afterId ;
                // private BigDecimal offlineOverDue ;
                // private BigDecimal onlineOverDue ;
                // private BigDecimal surplusFund ;
                // private List<String> mprIds ;
                //TODO 线下代扣ids
                //TODO 银行代扣ids
                // private String remark ;
                let param ;
                param.businessId=businessId;
                param.afterId=afterId;
                param = Object.assign(app.factRepaymentInfo,param);
                
                axios.post(fpath+'finance/previewConfirmRepayment',{param:param})
                .then(function(res){
                    console.log(res);
                })
                .catch(function(err){

                })
            },
        },
        created: function () {
            this.getThisTimeRepayment()
            this.getThisPeroidRepayment()
            this.getSurplusFund()
        }
    })
})
