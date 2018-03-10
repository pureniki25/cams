package com.hongte.alms.base.dto;

/**
 * 设置用户可访问的业务 业务dto
 * @author zengkun
 * @since 2018/3/5
 */
public class UserPermissionBusinessDto {

    //业务ID
    private String businessId;

    //公司ID
    private String companyId;

    //区域ID
//    private String disticId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

//    public String getDisticId() {
//        return disticId;
//    }
//
//    public void setDisticId(String disticId) {
//        this.disticId = disticId;
//    }
}
