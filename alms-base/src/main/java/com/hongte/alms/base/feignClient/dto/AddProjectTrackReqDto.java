package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * @author:喻尊龙
 * @date: 2018/3/2
 */

public class AddProjectTrackReqDto implements Serializable {
    private static final long serialVersionUID = -4095682638492039883L;

    /**
     *
     */
    private String projectId;
    /**
     *
     */
    private String content;
    /**
     *
     */
    private String addDate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}

