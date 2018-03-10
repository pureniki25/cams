/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: ProcessStepApprovalRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/22 20:52
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.process.vo;

/**
 * 流程审批请求包
 *
 * @author 伦惠峰
 * @Date 2018/1/22 20:52
 */
public class ProcessStepApprovalRequest {
    /**
     *流程ID
     */
    private String processID;
    /**
     *当前审批用户ID
     */
    private String userID;
    /**
     *操作类型 0审核不通过 1审核通过 2撤销
     */
    private int actionType;
    /**
     *审批的意见
     */
    private String remark;
    /**
     *回退的节点
     */
    private Integer backStep;
    /**
     *是否定向打回
     */
    private Integer isDirectBack;

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBackStep() {
        return backStep;
    }

    public void setBackStep(Integer backStep) {
        this.backStep = backStep;
    }

    public Integer getIsDirectBack() {
        return isDirectBack;
    }

    public void setIsDirectBack(Integer isDirectBack) {
        this.isDirectBack = isDirectBack;
    }
}
