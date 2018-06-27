package com.hongte.alms.base.vo.withhold;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "代扣额度列表请求参数对象", description = "代扣额度列表请求参数对象")
@Data
public class WithholdLimitListReq extends PageRequest {
    @ApiModelProperty(value = "platformId", name = "渠道ID", required = false)
    private Integer platformId;

    @ApiModelProperty(value = "bankCode", name = "银行编码", required = false)
    private String bankCode;
}



