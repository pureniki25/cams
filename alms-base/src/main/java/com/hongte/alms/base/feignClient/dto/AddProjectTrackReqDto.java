package com.hongte.alms.base.feignClient.dto;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/2
 */
public class AddProjectTrackReqDto {

    /**
     *
     */
    private List<ProjectTrackList> projectTrackList;
    /**
     *
     */
    private String projectId;

    public List<ProjectTrackList> getProjectTrackList() {
        return projectTrackList;
    }

    public void setProjectTrackList(List<ProjectTrackList> projectTrackList) {
        this.projectTrackList = projectTrackList;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
