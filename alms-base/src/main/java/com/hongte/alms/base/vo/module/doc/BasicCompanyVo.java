package com.hongte.alms.base.vo.module.doc;

import com.hongte.alms.base.vo.module.TreeTitle;

import java.util.Date;

/**
 * @author:喻尊龙
 * @date: 2018/3/16
 */
public class BasicCompanyVo extends TreeTitle {

    private String areaId;
    private String assetSideId;

    private String areaName;

    private String areaShortName;

    private Integer areaLevel;

    private String areaPid;

    private String districtId;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAssetSideId() {
        return assetSideId;
    }

    public void setAssetSideId(String assetSideId) {
        this.assetSideId = assetSideId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaShortName() {
        return areaShortName;
    }

    public void setAreaShortName(String areaShortName) {
        this.areaShortName = areaShortName;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getAreaPid() {
        return areaPid;
    }

    public void setAreaPid(String areaPid) {
        this.areaPid = areaPid;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
