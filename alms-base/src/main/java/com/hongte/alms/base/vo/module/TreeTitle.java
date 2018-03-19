package com.hongte.alms.base.vo.module;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/16
 */
public class TreeTitle {

    private String title;
    private boolean loading = false;
    private List<Object> children;
    private boolean checked ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
