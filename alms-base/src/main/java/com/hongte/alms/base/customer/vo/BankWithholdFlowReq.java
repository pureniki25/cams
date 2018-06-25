package com.hongte.alms.base.customer.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="贷后客户银行代扣流水请求参数对象",description="贷后客户银行代扣流水请求参数对象")
@Data
public class BankWithholdFlowReq  extends PageRequest {
    @ApiModelProperty(value="开始时间",name="startTime",required = false)
    private String startTime;
    @ApiModelProperty(value="结束时间",name="endTime",required = false)
    private String endTime;

}
