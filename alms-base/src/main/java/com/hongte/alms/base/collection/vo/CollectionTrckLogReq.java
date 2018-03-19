package com.hongte.alms.base.collection.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/2/1
 * 贷后跟踪记录列表查询对象
 */
@ApiModel(value="贷后跟踪记录列表查询对象",description="贷后跟踪记录列表查询对象")
public class CollectionTrckLogReq extends PageRequest {

    @ApiModelProperty(value="还款计划ID",name="rbpId",example="test")
    private String rbpId;  //还款计划ID

    @ApiModelProperty(value="业务编号",name="businessId",example="test")
    private String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRbpId() {
        return rbpId;
    }

    public void setRbpId(String rbpId) {
        this.rbpId = rbpId;
    }
}
