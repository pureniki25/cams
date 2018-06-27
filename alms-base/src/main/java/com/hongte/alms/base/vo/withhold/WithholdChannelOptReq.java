package com.hongte.alms.base.vo.withhold;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel(value = "代扣渠道列表新增/修改请求参数对象", description = "代扣渠道列表新增/修改请求参数对象")
@Data
public class WithholdChannelOptReq {
    @ApiModelProperty(value = "渠道ID", name = "platformId", required = false)
    private Integer platformId;

    /**
     * 渠道状态，0：停用，1：启用
     */
    @ApiModelProperty(name = "channelStatus", value = "渠道状态，0：停用，1：启用")
    private Integer channelStatus;
    /**
     * 每日失败最大次数
     */
    @ApiModelProperty(name = "failTimes", value = "每日失败最大次数")
    private Integer failTimes;


    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 操作类型
     */
    @ApiModelProperty(name = "optType", value = "操作类型 1 新增 2编辑")
    private int optType;
}