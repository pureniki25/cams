package com.hongte.alms.core.vo.modules;

import java.util.ArrayList;
import java.util.List;

public class NavMenuVo {
    private String id;
    private String title;
    private String icon;
    private boolean spread;
    private String url;
    private List<NavMenuVo> children;

    public NavMenuVo()
    {
     this.children=new ArrayList<NavMenuVo>();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public List<NavMenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<NavMenuVo> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
