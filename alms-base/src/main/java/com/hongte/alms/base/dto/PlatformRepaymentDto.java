package com.hongte.alms.base.dto;

import lombok.Data;

@Data
public class PlatformRepaymentDto {
    private String period;//	期次
    private String isValid;//	是否撤销还款
    private String afterId;//	资产端期数
    private String confirmLogId;//	实还流水ID
    private String projectId;//	标的ID
    private Integer processStatus;//0：待分发，1：分发处理中，2：分发成功，3，分发失败

}
