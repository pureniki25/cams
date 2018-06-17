package com.hongte.alms.base.dto.core;

import java.util.Map;

/**
 * 专用于LayuiTable的后端分页类
 * <p>
 * Created by 张贵宏 on 2018/6/17 10:25
 */
public class LayTableQuery {
    private Map<String, String> search;
    private Integer limit;
    private Integer page;
    private String orderField = "create_time";
    private String orderDirection = "desc";

    public Map<String, String> getSearch() {
        return search;
    }

    public void setSearch(Map<String, String> search) {
        this.search = search;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page - 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
