package com.hongte.alms.base.vo.module;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/16
 */
public class BatchSavePersonReq {

    private List<String> areaName;
    private List<String> companyName;
    private List<String> collectionGroup1Users;
    private List<String> collectionGroup2Users;

    public List<String> getAreaName() {
        return areaName;
    }

    public void setAreaName(List<String> areaName) {
        this.areaName = areaName;
    }

    public List<String> getCompanyName() {
        return companyName;
    }

    public void setCompanyName(List<String> companyName) {
        this.companyName = companyName;
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
}
