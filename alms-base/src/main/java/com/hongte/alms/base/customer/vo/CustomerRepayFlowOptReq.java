package com.hongte.alms.base.customer.vo;

import io.swagger.annotations.ApiModel;

@ApiModel(value="贷后客户还款登记审核或拒绝请求参数对象",description="贷后客户还款登记审核或拒绝请求参数对象")
public class CustomerRepayFlowOptReq {
    private String opt;//2为审核通过 3 为审核拒绝

    private String idsStr; //已逗号分隔的id

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getIdsStr() {
        return idsStr;
    }

    public void setIdsStr(String idsStr) {
        this.idsStr = idsStr;
    }
}
