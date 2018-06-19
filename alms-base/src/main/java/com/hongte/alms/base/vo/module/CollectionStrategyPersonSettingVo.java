package com.hongte.alms.base.vo.module;

/**
 * 催收策略人员设置对象
 * @author dengzhiming
 * @date 2018/3/6 9:25
 */
public class CollectionStrategyPersonSettingVo {


    private String id;  //主键
    private String areaId; //区域ID
    private String areaName; //区域名称
    private String companyId; //公司ID
    private String companyName; //公司名称
    private String collect1Person; //清算一人员（电催）
    private String col1Pids;//清算一人员ID（电催）
    private String collect2Person; //清算二人员（催收）
    private String col2Pids;//清算二人员ID（催收）
    private Integer businessType; //业务类型Id
    private String businessTypeName; //业务类型名称


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



    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getCol1Pids() {
        return col1Pids;
    }

    public void setCol1Pids(String col1Pids) {
        this.col1Pids = col1Pids;
    }

    public String getCol2Pids() {
        return col2Pids;
    }

    public void setCol2Pids(String col2Pids) {
        this.col2Pids = col2Pids;
    }
}
