package com.hongte.alms.base.vo.module;

/**
 * @author dengzhiming
 * @date 2018/2/26 10:08
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域市项
 */
public class AreaCityItemVo  implements Serializable {

    public AreaCityItemVo()
    {
      this.children=new ArrayList<AreaCountyItemVo>();
    }

    private String value;
    private String label;
    private List<AreaCountyItemVo> children;

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

    public List<AreaCountyItemVo> getChildren() {
        return children;
    }

    public void setChildren(List<AreaCountyItemVo> children) {
        this.children = children;
    }


}
