package com.hongte.alms.base.process.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 流程查询实体类
 */
public class ProcessVo {



    private String processId;
    /**
     * 流程名称
     */
    private String processName;

    /**
     * 业务编号
     */
    private String businessId;
    private  String  customerName; //客户名称
    /**
     * 流程注释
     */
    private String processDesc;
    /**
     * 来源流程类别ID
     */
    private String processTypeID;
    /**
     * 来源流程类别名称
     */
    private String processTypeName;

    /**
     * 来源流程编码
     */
    private String processTypeCode;

    private String approveUserId;  //当前审批人Id   （不显示）
    private String approveUserName;  //当前审批人名称  （）
    /**
     * 状态
     */
    private Integer status;
    private  String processStatus;  //流程状态

    /**
     * 审批结果
     */
    private Integer pResult;

    /**
     * 发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 创建人
     */
    private String createUser;
    private String createUserName;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 所属分公司
     */
    private String companyId;


    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessDesc() {
        return processDesc;
    }

    public void setProcessDesc(String processDesc) {
        this.processDesc = processDesc;
    }

    public String getProcessTypeID() {
        return processTypeID;
    }

    public void setProcessTypeID(String processTypeID) {
        this.processTypeID = processTypeID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }

    public String getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(String approveUserId) {
        this.approveUserId = approveUserId;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getProcessTypeCode() {
        return processTypeCode;
    }

    public void setProcessTypeCode(String processTypeCode) {
        this.processTypeCode = processTypeCode;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    public Integer getpResult() {
        return pResult;
    }

    public void setpResult(Integer pResult) {
        this.pResult = pResult;
    }
}
