package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 还款计划用户信息表（tb_basic_business_customer表的替代表）
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-11
 */
@ApiModel
@TableName("tb_basic_biz_customer")
public class BasicBizCustomer extends Model<BasicBizCustomer> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增ID
     */
	@ApiModelProperty(required= true,value = "主键自增ID")
	private String id;
    /**
     * 客户ID，资产端主键
     */
	@TableField("customer_id")
	@ApiModelProperty(required= true,value = "客户ID，资产端主键")
	private String customerId;
    /**
     * 业务编号
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 是否主借款人，0：否，1：是
     */
	@TableField("ismain_customer")
	@ApiModelProperty(required= true,value = "是否主借款人，0：否，1：是")
	private Integer ismainCustomer;
    /**
     * [客户名称，个人则填个人名称，企业则填企业名称]
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "[客户名称，个人则填个人名称，企业则填企业名称]")
	private String customerName;
    /**
     * [是否收款账户]
     */
	@TableField("is_receipt_account")
	@ApiModelProperty(required= true,value = "[是否收款账户]")
	private Integer isReceiptAccount;
    /**
     * [客户类型：个人，企业]
     */
	@TableField("customer_type")
	@ApiModelProperty(required= true,value = "[客户类型：个人，企业]")
	private String customerType;
    /**
     * [客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号]
     */
	@TableField("identify_card")
	@ApiModelProperty(required= true,value = "[客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号]")
	private String identifyCard;
    /**
     * [客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码]
     */
	@TableField("phone_number")
	@ApiModelProperty(required= true,value = "[客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码]")
	private String phoneNumber;
    /**
     * [客户是否大陆居民，0或null：否，1：是]
     */
	@TableField("ismainland_resident")
	@ApiModelProperty(required= true,value = "[客户是否大陆居民，0或null：否，1：是]")
	private Integer ismainlandResident;
    /**
     * [是否提供对公账号,null或0为否，1为是 客户为企业需要填写]
     */
	@TableField("is_company_bank_account")
	@ApiModelProperty(required= true,value = "[是否提供对公账号,null或0为否，1为是 客户为企业需要填写]")
	private Integer isCompanyBankAccount;
    /**
     * [是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填）]
     */
	@TableField("is_merged_certificate")
	@ApiModelProperty(required= true,value = "[是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填）]")
	private Integer isMergedCertificate;
    /**
     * [统一社会信用代码 客户为企业并且为三证合一时必须填写并作为tb_business表，绑卡表的外键]
     */
	@ApiModelProperty(required= true,value = "[统一社会信用代码 客户为企业并且为三证合一时必须填写并作为tb_business表，绑卡表的外键]")
	private String unifiedCode;
    /**
     * [营业执照号 客户为企业时并且非三证合一时必须填写，并作为tb_business，绑卡表的外键]
     */
	@TableField("business_licence")
	@ApiModelProperty(required= true,value = "[营业执照号 客户为企业时并且非三证合一时必须填写，并作为tb_business，绑卡表的外键]")
	private String businessLicence;
    /**
     * [企业注册地址所在省份 客户为企业时需要填写]
     */
	@TableField("register_province")
	@ApiModelProperty(required= true,value = "[企业注册地址所在省份 客户为企业时需要填写]")
	private String registerProvince;
    /**
     * [企业法人 客户为企业时需要填写]
     */
	@TableField("company_legal_person")
	@ApiModelProperty(required= true,value = "[企业法人 客户为企业时需要填写]")
	private String companyLegalPerson;
    /**
     * [企业法人身份证 客户为企业且提供对公账号时需要填写]
     */
	@TableField("legal_person_identity_card")
	@ApiModelProperty(required= true,value = "[企业法人身份证 客户为企业且提供对公账号时需要填写]")
	private String legalPersonIdentityCard;
    /**
     * [企业法人是否大陆居民 客户为企业时需要填写]
     */
	@TableField("legal_person_ismain_customer")
	@ApiModelProperty(required= true,value = "[企业法人是否大陆居民 客户为企业时需要填写]")
	private Boolean legalPersonIsmainCustomer;
    /**
     * [创建日期]
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "[创建日期]")
	private Date createTime;
    /**
     * [创建人]
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "[创建人]")
	private String createUser;
    /**
     * [更新人]
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "[更新人]")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

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

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BasicBizCustomer{" +
			", id=" + id +
			", customerId=" + customerId +
			", businessId=" + businessId +
			", ismainCustomer=" + ismainCustomer +
			", customerName=" + customerName +
			", isReceiptAccount=" + isReceiptAccount +
			", customerType=" + customerType +
			", identifyCard=" + identifyCard +
			", phoneNumber=" + phoneNumber +
			", ismainlandResident=" + ismainlandResident +
			", isCompanyBankAccount=" + isCompanyBankAccount +
			", isMergedCertificate=" + isMergedCertificate +
			", unifiedCode=" + unifiedCode +
			", businessLicence=" + businessLicence +
			", registerProvince=" + registerProvince +
			", companyLegalPerson=" + companyLegalPerson +
			", legalPersonIdentityCard=" + legalPersonIdentityCard +
			", legalPersonIsmainCustomer=" + legalPersonIsmainCustomer +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
