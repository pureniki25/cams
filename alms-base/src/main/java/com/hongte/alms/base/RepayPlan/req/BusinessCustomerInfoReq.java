package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author zengkun
 * @since 2018/5/11
 * 生成还款计划   业务用户信息
 */
@ApiModel("业务用户信息")
public class BusinessCustomerInfoReq {

    /**
     * 客户ID，资产端主键
     */
    @ApiModelProperty(required= true,value = "客户ID，资产端主键")
    @NotNull(message = "客户ID，资产端主键(customerId)不能为空")
    private String customerId;

    /**
     * 是否主借款人，0：否，1：是
     */
    @ApiModelProperty(required= true,value = "是否主借款人，0：否，1：是")
    private Integer ismainCustomer;
    /**
     *  客户名称，个人则填个人名称，企业则填企业名称 
     */
    @ApiModelProperty(required= true,value = " 客户名称，个人则填个人名称，企业则填企业名称 ")
    private String customerName;
    /**
     *  是否收款账户 
     */
    @ApiModelProperty(required= true,value = "是否收款账户，0：否，1：是")
    private Integer isReceiptAccount;
    /**
     *  客户类型：个人，企业 
     */
    @ApiModelProperty(required= true,value = " 客户类型：个人，企业 ")
    private String customerType;
    /**
     *  客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号 
     */
    @ApiModelProperty(required= true,value = " 客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号 ")
    private String identifyCard;
    /**
     *  客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码 
     */
    @ApiModelProperty(required= true,value = " 客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码 ")
    private String phoneNumber;
    /**
     *  客户是否大陆居民，0或null：否，1：是 
     */
    @ApiModelProperty(value = " 客户是否大陆居民，0或null：否，1：是 ")
    private Integer ismainlandResident;
    /**
     *  是否提供对公账号,null或0为否，1为是 客户为企业需要填写 
     */
    @ApiModelProperty(value = " 是否提供对公账号,null或0为否，1为是 客户为企业需要填写 ")
    private Integer isCompanyBankAccount;
    /**
     *  是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填） 
     */
    @ApiModelProperty(value = " 是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填） ")
    private Integer isMergedCertificate;
    /**
     *  统一社会信用代码 客户为企业并且为三证合一时必须填写并作为tb_business表，绑卡表的外键 
     */
    @ApiModelProperty(value = " 统一社会信用代码 客户为企业并且为三证合一时必须填写 ")
    private String unifiedCode;
    /**
     *  营业执照号 客户为企业时并且非三证合一时必须填写，并作为tb_business，绑卡表的外键 
     */
    @ApiModelProperty(value = " 营业执照号 客户为企业时并且非三证合一时必须填写")
    private String businessLicence;
    /**
     *  企业注册地址所在省份 客户为企业时需要填写 
     */
    @ApiModelProperty(value = " 企业注册地址所在省份 客户为企业时需要填写 ")
    private String registerProvince;
    /**
     *  企业法人 客户为企业时需要填写 
     */
    @ApiModelProperty(value = " 企业法人 客户为企业时需要填写 ")
    private String companyLegalPerson;
    /**
     *  企业法人身份证 客户为企业且提供对公账号时需要填写 
     */
    @ApiModelProperty(value = " 企业法人身份证 客户为企业且提供对公账号时需要填写 ")
    private String legalPersonIdentityCard;
    /**
     *  企业法人是否大陆居民 客户为企业时需要填写 
     */
    @ApiModelProperty(value = " 企业法人是否大陆居民 客户为企业时需要填写 ")
    private Boolean legalPersonIsmainCustomer;

    public Integer getIsmainCustomer() {
        return ismainCustomer;
    }

    public void setIsmainCustomer(Integer ismainCustomer) {
        this.ismainCustomer = ismainCustomer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getIsReceiptAccount() {
        return isReceiptAccount;
    }

    public void setIsReceiptAccount(Integer isReceiptAccount) {
        this.isReceiptAccount = isReceiptAccount;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIdentifyCard() {
        return identifyCard;
    }

    public void setIdentifyCard(String identifyCard) {
        this.identifyCard = identifyCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getIsmainlandResident() {
        return ismainlandResident;
    }

    public void setIsmainlandResident(Integer ismainlandResident) {
        this.ismainlandResident = ismainlandResident;
    }

    public Integer getIsCompanyBankAccount() {
        return isCompanyBankAccount;
    }

    public void setIsCompanyBankAccount(Integer isCompanyBankAccount) {
        this.isCompanyBankAccount = isCompanyBankAccount;
    }

    public Integer getIsMergedCertificate() {
        return isMergedCertificate;
    }

    public void setIsMergedCertificate(Integer isMergedCertificate) {
        this.isMergedCertificate = isMergedCertificate;
    }

    public String getUnifiedCode() {
        return unifiedCode;
    }

    public void setUnifiedCode(String unifiedCode) {
        this.unifiedCode = unifiedCode;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getRegisterProvince() {
        return registerProvince;
    }

    public void setRegisterProvince(String registerProvince) {
        this.registerProvince = registerProvince;
    }

    public String getCompanyLegalPerson() {
        return companyLegalPerson;
    }

    public void setCompanyLegalPerson(String companyLegalPerson) {
        this.companyLegalPerson = companyLegalPerson;
    }

    public String getLegalPersonIdentityCard() {
        return legalPersonIdentityCard;
    }

    public void setLegalPersonIdentityCard(String legalPersonIdentityCard) {
        this.legalPersonIdentityCard = legalPersonIdentityCard;
    }

    public Boolean getLegalPersonIsmainCustomer() {
        return legalPersonIsmainCustomer;
    }

    public void setLegalPersonIsmainCustomer(Boolean legalPersonIsmainCustomer) {
        this.legalPersonIsmainCustomer = legalPersonIsmainCustomer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
