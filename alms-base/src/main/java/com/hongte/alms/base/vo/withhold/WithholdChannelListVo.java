package com.hongte.alms.base.vo.withhold;

import lombok.Data;

@Data
public class WithholdChannelListVo {

    private String channelLevel;//渠道优先级
    private Integer channelId;//编号

    private String platformName;//渠道名称
    private String subPlatformId;//渠道编号


    private String channelStatus;//渠道状态

    private Integer failTimes;//每日失败最大次数

}
