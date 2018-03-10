package com.hongte.alms.base.collection.vo;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/1/25
 */
@ApiModel(value="分配电催请求对象",description="分配电催请求对象")
public class SetPhoneUrgeReq {
    //分配电催的事物列表
    private List<StaffBusinessVo> staffBusinessVoList;

    //电催跟踪人员ID
    private String userId;

    //描述
    private String description;

    public List<StaffBusinessVo> getStaffBusinessVoList() {
        return staffBusinessVoList;
    }

    public void setStaffBusinessVoList(List<StaffBusinessVo> staffBusinessVoList) {
        this.staffBusinessVoList = staffBusinessVoList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //还款计划ID
//    private  String crpId;

}
