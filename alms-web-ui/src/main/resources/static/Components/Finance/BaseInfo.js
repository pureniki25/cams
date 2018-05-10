; (function (win, Vue) {
    'use strict';

    let BaseInfo = Vue.extend({
        data:{
            info:{}
        },
        created: function () {
            axios.get(htConfig.financeBasePath+'finance/repayBaseInfo', {
                params: {
                    businessId: businessId,
                    afterId: afterId
                }
            })
                .then(function (res) {
                    if (res.data.code == '1') {
                        app.info = res.data.data;
                    }else{
                        app.$Message.error({content:res.data.msg})
                    }
                    app.spinShow=false
                })
                .catch(function (err) {
                    app.spinShow=false
                })
        },
        template:`
        <Card title="还款基本信息">
            <row>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        业务编号：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.businessId}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        期数：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.afterId}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        业务类型：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.businessType}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        业务部门：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.companyName}}
                    </div>
                </i-col>
            </row>
            <row>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        业务主办：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.operatorName}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        客户名：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.customerName}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        还款方式：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.repaymentType}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        还款日期：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.repayDate}}
                    </div>
                </i-col>
            </row>
            <row>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        应还金额：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.repayAmount}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        借款金额：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.borrowAmount}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        借款期限：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.borrowLimit}}
                    </div>
                </i-col>
                <i-col span="6">
                    <div style="width: 50%;display:inline-block;text-align: right">
                        线下出款费率：
                    </div>
                    <div style="width: 49%;display:inline-block;text-align: left">
                        {{info.borrowRate}}
                    </div>
                </i-col>
            </row>
        </Card>
        `
    })
    Vue.component("BaseInfo", BaseInfo)
})(window, Vue);