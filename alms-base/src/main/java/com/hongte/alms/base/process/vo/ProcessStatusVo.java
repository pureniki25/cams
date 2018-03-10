package com.hongte.alms.base.process.vo;

/**
 * @author zengkun
 * @since 2018/3/1
 */
public class ProcessStatusVo {

    private Integer statusKey; // 数据保存的值
    private String statusName; // 名称

    public Integer getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(Integer statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
