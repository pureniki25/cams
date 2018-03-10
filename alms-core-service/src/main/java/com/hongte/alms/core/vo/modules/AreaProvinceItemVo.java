package com.hongte.alms.core.vo.modules;

/**
 * @author dengzhiming
 * @date 2018/2/26 10:08
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 区域省项
 */
public class AreaProvinceItemVo {

    public AreaProvinceItemVo()
    {
      this.children=new ArrayList<AreaCityItemVo>();
    }

    private String value;
    private String label;
    private List<AreaCityItemVo> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AreaCityItemVo> getChildren() {
        return children;
    }

    public void setChildren(List<AreaCityItemVo> children) {
        this.children = children;
    }


}
