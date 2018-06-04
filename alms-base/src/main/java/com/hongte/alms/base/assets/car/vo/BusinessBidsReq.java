package com.hongte.alms.base.assets.car.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dengzhiming
 * @date 2018/6/1 15:19
 */
public class BusinessBidsReq extends PageRequest {
    @ApiModelProperty(value = "业务编号", name = "businessId")
    private String businessId;
    @ApiModelProperty(value = "关键字", name = "keyword")
    private String keyword;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
