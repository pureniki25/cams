/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: ProcessStartPageRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/30 14:11
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.process.vo;

import java.util.Date;

/**
 * 流程发起页面查询请求包
 * @author 伦惠峰
 * @Date 2018/1/30 14:11
 */
public class ProcessStartPageRequest {
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

    /**
     * 立案开始时间
     */
    private Date accreditationBeginTime;

    /**
     * 立案结束时间
     */
    private Date accreditationEndTime;

    /**
     * 诉讼状态
     */
    private Integer lawsuitStatus;

    /**
     * 执行立案开始时间
     */
     private Date executionBeginTime;

    /**
     * 执行立案结束时间
     */
    private Date executionEndTime;

    /**
     * 终结状态
     */
    private Integer breakStatus;

    /**
     * 结案状态
     */
    private Integer closeStatus;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Date getProcessStartTime() {
        return processStartTime;
    }

    public void setProcessStartTime(Date processStartTime) {
        this.processStartTime = processStartTime;
    }

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProcess_typeid() {
        return process_typeid;
    }

    public void setProcess_typeid(String process_typeid) {
        this.process_typeid = process_typeid;
    }

    public Date getAccreditationBeginTime() {
        return accreditationBeginTime;
    }

    public void setAccreditationBeginTime(Date accreditationBeginTime) {
        this.accreditationBeginTime = accreditationBeginTime;
    }

    public Date getAccreditationEndTime() {
        return accreditationEndTime;
    }

    public void setAccreditationEndTime(Date accreditationEndTime) {
        this.accreditationEndTime = accreditationEndTime;
    }

    public Integer getLawsuitStatus() {
        return lawsuitStatus;
    }

    public void setLawsuitStatus(Integer lawsuitStatus) {
        this.lawsuitStatus = lawsuitStatus;
    }

    public Date getExecutionBeginTime() {
        return executionBeginTime;
    }

    public void setExecutionBeginTime(Date executionBeginTime) {
        this.executionBeginTime = executionBeginTime;
    }

    public Date getExecutionEndTime() {
        return executionEndTime;
    }

    public void setExecutionEndTime(Date executionEndTime) {
        this.executionEndTime = executionEndTime;
    }

    public Integer getBreakStatus() {
        return breakStatus;
    }

    public void setBreakStatus(Integer breakStatus) {
        this.breakStatus = breakStatus;
    }

    public Integer getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(Integer closeStatus) {
        this.closeStatus = closeStatus;
    }
}
