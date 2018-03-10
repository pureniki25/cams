package com.hongte.alms.base.vo.module;

import java.util.List;

/**
 * @author dengzhiming
 * @date 2018/3/6 17:01
 */
public class SingleCompanyCollectionPersonSettingVo {
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getCollection1UserIds() {
        return collection1UserIds;
    }

    public void setCollection1UserIds(List<String> collection1UserIds) {
        this.collection1UserIds = collection1UserIds;
    }

    public List<String> getCollection2UserIds() {
        return collection2UserIds;
    }

    public void setCollection2UserIds(List<String> collection2UserIds) {
        this.collection2UserIds = collection2UserIds;
    }

    private String companyId;
    /**
     * 清算1组用户id
     */
    private List<String> collection1UserIds;
    /**
     * 清算2组用户id
     */
    private List<String> collection2UserIds;



}
