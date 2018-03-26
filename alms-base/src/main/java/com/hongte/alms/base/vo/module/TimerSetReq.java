package com.hongte.alms.base.vo.module;

/**
 * @author:喻尊龙
 * @date: 2018/3/26
 */
public class TimerSetReq  {

    private String jobName;

    private int page;

    private int limit;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
