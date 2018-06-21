package com.hongte.alms.base.vo.finance;

import lombok.Data;

import java.util.Date;

/**
 * Created by 张贵宏 on 2018/6/17 14:23
 */
@Data
public class SysFinancialOrderVO {
    private Integer id;
    private String areaId;
    private String areaName;
    private String companyId;
    private String companyName;
    private Integer businessTypeId;
    private String businessTypeName;
    private String userIds;
    private String userNames;
    private String userId;
    private String userName;
    private String updateUser;
    private Date updateTime;
    private String createUser;
    private Date createTime;
}
