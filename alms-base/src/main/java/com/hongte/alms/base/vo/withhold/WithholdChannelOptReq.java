package com.hongte.alms.base.vo.withhold;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "代扣渠道列表新增/修改请求参数对象", description = "代扣渠道列表新增/修改请求参数对象")
@Data
public class WithholdChannelOptReq {
    @ApiModelProperty(value = "渠道ID", name = "platformId", required = false)
    private Integer platformId;


}