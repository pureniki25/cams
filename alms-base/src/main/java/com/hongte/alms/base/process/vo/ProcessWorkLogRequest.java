/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: ProcessWorkLogRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/24 10:39
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.process.vo;

/**
 *流程进度条信息
 * @author 伦惠峰
 * @Date 2018/1/24 10:39
 */
public class ProcessWorkLogRequest {
    /**
     *流程ID
     */
    private  String processID;
    /**
     *业务编码
     */
    private String businessId;

    /**
     *流程类型
     */
    private String typeCode;

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
