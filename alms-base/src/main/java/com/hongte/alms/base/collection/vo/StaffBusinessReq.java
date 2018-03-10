package com.hongte.alms.base.collection.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zengkun
 * @since 2018/1/26
 * 查询员工跟进催收记录参数定义
 */
@ApiModel(value="查询员工跟进催收记录参数定义",description="查询员工跟进催收记录参数定义")
public class StaffBusinessReq extends PageRequest {

    @ApiModelProperty(value="业务ID",name="businessId",example="test",dataType = "int")
    private String businessId;
    @ApiModelProperty(value="分级",name="moduleLevel",example="test",dataType = "int")
    private String crpId;
}