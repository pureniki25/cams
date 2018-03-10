package com.hongte.alms.scheduled.vo;

/**
 * 通用k-v模型，可用于前端select控件
 * @Author: 黄咏康
 * @Date: 2018/1/17 0017 下午 11:44
 */
public class labelValue {

    /**
     * 文本
     */
    private String label;
    /**
     * 值
     */
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
