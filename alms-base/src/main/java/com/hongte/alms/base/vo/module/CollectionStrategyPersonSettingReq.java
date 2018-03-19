package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 催收策略人员设置查询请求对象
 * @author dengzhiming
 * @date 2018/3/6 9:25
 */
@ApiModel(value="催收策略人员设置查询请求对象",description="催收策略人员设置查询请求对象")
public class CollectionStrategyPersonSettingReq extends PageRequest {
    private String areaId;
    private String companyId;
    private String userName;
    private String[] userNames = new String[]{};

    public String[] getUserNames() {
        return userNames;
    }

    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
