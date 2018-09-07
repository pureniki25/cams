package com.hongte.alms.base.vo.cams;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

import javax.validation.constraints.NotBlank;

public class CancelBizAccountListCommand extends MessageCommand {

    @ApiModelProperty(required = true, value = "业务编号")
    @Length(max = 50)
    @NotBlank
    private String businessId;

    @ApiModelProperty(required = true, value = "期数")
    @Length(max = 50)
    @NotBlank
    private String afterId;

    private String externalId;
    
    private String triggerEventType;
    
    private String eventType;
    
    private String triggerEventSystem;
    
    private Date segmentationDate;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

	public String getTriggerEventType() {
		return triggerEventType;
	}

	public void setTriggerEventType(String triggerEventType) {
		this.triggerEventType = triggerEventType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTriggerEventSystem() {
		return triggerEventSystem;
	}

	public void setTriggerEventSystem(String triggerEventSystem) {
		this.triggerEventSystem = triggerEventSystem;
	}

	public Date getSegmentationDate() {
		return segmentationDate;
	}

	public void setSegmentationDate(Date segmentationDate) {
		this.segmentationDate = segmentationDate;
	}
    
}
