package com.hongte.alms.base.vo.module;

/**
 * 催收策略人员设置对象
 * @author dengzhiming
 * @date 2018/3/6 9:25
 */
public class CollectionStrategyPersonSettingVo {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCollect1Person() {
        return collect1Person;
    }

    public void setCollect1Person(String collect1Person) {
        this.collect1Person = collect1Person;
    }

    public String getCollect2Person() {
        return collect2Person;
    }

    public void setCollect2Person(String collect2Person) {
        this.collect2Person = collect2Person;
    }

    private String id;
private String areaId;
private String areaName;
private String companyId;
private String companyName;
private String collect1Person;
private String collect2Person;
}
