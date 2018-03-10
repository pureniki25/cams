package com.hongte.alms.base.process.vo;


import java.util.Date;

/**
 * 流程查看页面请求包
 */
public class ProcessPageRequest {
    /**
     * 每页行数

     */
    private Integer limit;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 流程发起时间
     */
    private Date processStartTime;

    /**
     * 流程结束时间
     */
    private Date processEndTime;

    /**
     * 业务编码
     */
    private String businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 状态 (0:运行中,1:开始,2:结束,3:注销)
     */
    private Integer flowStatus;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 分公司ID
     */
    private String companyId;


    /**
     * 流程类型
     */
    private String process_typeid;




    public Date getProcessEndTime() {
        return processEndTime;
    }

    public void setProcessEndTime(Date processEndTime) {
        this.processEndTime = processEndTime;
    }








    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getProcessStartTime() {
        return processStartTime;
    }

    public void setProcessStartTime(Date processStartTime) {
        this.processStartTime = processStartTime;
    }

    public String getProcess_typeid() {
        return process_typeid;
    }

    public void setProcess_typeid(String process_typeid) {
        this.process_typeid = process_typeid;
    }
}
