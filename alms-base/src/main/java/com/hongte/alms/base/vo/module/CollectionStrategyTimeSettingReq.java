package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;

/**
 * 催收策略时间设置查询请求对象
 * @author dengzhiming
 * @date 2018/3/6 9:25
 */
@ApiModel(value="催收策略时间设置查询请求对象",description="催收策略时间设置查询请求对象")
public class CollectionStrategyTimeSettingReq extends PageRequest {
    private String areaId;
    private String companyId;
    private String userName;
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
