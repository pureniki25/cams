package com.hongte.alms.base.process.vo;

/**
 * @author zengkun
 * @since 2018/3/2
 */
public class ProcessSaveReq {

    //业务ID  必填
    private String businessId;
    //标题  必填
    private String title;
    //流程状态  新建或开始 必填
    private Integer processStatus;
    //流程ID 填则更新  不填则新增
    private String processId;
    //描述  非必填
    private String desc;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
