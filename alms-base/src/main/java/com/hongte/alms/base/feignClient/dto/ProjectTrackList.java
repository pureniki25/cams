package com.hongte.alms.base.feignClient.dto;

/**
 * @author:喻尊龙
 * @date: 2018/3/12
 */
public class ProjectTrackList {

    /**
     *追踪内容
     */
    private String content;
    /**
     *时间
     */
    private String addDate;

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
