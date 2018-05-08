package com.hongte.alms.base.vo.module;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/15
 */
public class CollectionStrategySinglePersonSettingReq {

    private List<String> collectionGroup1Users;

    private List<String> collectionGroup2Users;

    private List<String> companyId;
    
    private Integer businessType;

    private String areaId;

    public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public List<String> getCollectionGroup1Users() {
        return collectionGroup1Users;
    }

    public void setCollectionGroup1Users(List<String> collectionGroup1Users) {
        this.collectionGroup1Users = collectionGroup1Users;
    }

    public List<String> getCollectionGroup2Users() {
        return collectionGroup2Users;
    }

    public void setCollectionGroup2Users(List<String> collectionGroup2Users) {
        this.collectionGroup2Users = collectionGroup2Users;
    }

    public List<String> getCompanyId() {
        return companyId;
    }

    public void setCompanyId(List<String> companyId) {
        this.companyId = companyId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
