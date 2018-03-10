package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 基础业务信息表
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-02
 */
@ApiModel
@TableName("tb_basic_business")
public class BasicBusiness extends Model<BasicBusiness> {

    private static final long serialVersionUID = 1L;

    /**
     * 资产端业务编号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号")
	private String businessId;
    /**
     * 进件日期
     */
	@TableField("input_time")
	@ApiModelProperty(required= true,value = "进件日期")
	private Date inputTime;
    /**
     * 业务类型ID(对应tb_basic_business_type的ID)
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型ID(对应tb_basic_business_type的ID)")
	private Integer businessType;
    /**
     * 业务所属子类型，若无则为空
     */
	@TableField("business_ctype")
	@ApiModelProperty(required= true,value = "业务所属子类型，若无则为空")
	private String businessCtype;
    /**
     * 业务所属孙类型，若无则为空
     */
	@TableField("business_stype")
	@ApiModelProperty(required= true,value = "业务所属孙类型，若无则为空")
	private String businessStype;
    /**
     * 客户资产端唯一编号
     */
	@TableField("customer_id")
	@ApiModelProperty(required= true,value = "客户资产端唯一编号")
	private String customerId;
    /**
     * 客户姓名
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "客户姓名")
	private String customerName;
    /**
     * 还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
     */
	@TableField("repayment_type_id")
	@ApiModelProperty(required= true,value = "还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息")
	private Integer repaymentTypeId;
    /**
     * 借款金额(元)
     */
	@TableField("borrow_money")
	@ApiModelProperty(required= true,value = "借款金额(元)")
	private BigDecimal borrowMoney;
    /**
     * 借款期限
     */
	@TableField("borrow_limit")
	@ApiModelProperty(required= true,value = "借款期限")
	private Integer borrowLimit;
    /**
     * 借款期限单位，1：月，2：天
     */
	@TableField("borrow_limit_unit")
	@ApiModelProperty(required= true,value = "借款期限单位，1：月，2：天")
	private Integer borrowLimitUnit;
    /**
     * 借款利率(%)，如11%则存11.0
     */
	@TableField("borrow_rate")
	@ApiModelProperty(required= true,value = "借款利率(%)，如11%则存11.0")
	private BigDecimal borrowRate;
    /**
     * 借款利率类型，1：年利率，2：月利率，3：日利率
     */
	@TableField("borrow_rate_unit")
	@ApiModelProperty(required= true,value = "借款利率类型，1：年利率，2：月利率，3：日利率")
	private Integer borrowRateUnit;
    /**
     * 业务主办人ID
     */
	@TableField("operator_id")
	@ApiModelProperty(required= true,value = "业务主办人ID")
	private String operatorId;
    /**
     * 业务主办人姓名
     */
	@TableField("operator_name")
	@ApiModelProperty(required= true,value = "业务主办人姓名")
	private String operatorName;
    /**
     * 业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)
     */
	@TableField("asset_id")
	@ApiModelProperty(required= true,value = "业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)")
	private String assetId;
    /**
     * 业务所属分公司编号
     */
	@TableField("company_id")
	@ApiModelProperty(required= true,value = "业务所属分公司编号")
	private String companyId;
    /**
     * 出款平台ID，0：线下出款，1：团贷网P2P上标
     */
	@TableField("output_platform_id")
	@ApiModelProperty(required= true,value = "出款平台ID，0：线下出款，1：团贷网P2P上标")
	private Integer outputPlatformId;
    /**
     * 标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
     */
	@TableField("issue_split_type")
	@ApiModelProperty(required= true,value = "标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务")
	private Integer issueSplitType;
    /**
     * 业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
     */
	@TableField("source_type")
	@ApiModelProperty(required= true,value = "业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信")
	private Integer sourceType;
    /**
     * 原始来源业务的业务编号(当业务来源为结清再贷时，必填)
     */
	@TableField("source_business_id")
	@ApiModelProperty(required= true,value = "原始来源业务的业务编号(当业务来源为结清再贷时，必填)")
	private String sourceBusinessId;
    /**
     * 是否需要进行平台还款，1：是，0：否
     */
	@TableField("is_tuandai_repay")
	@ApiModelProperty(required= true,value = "是否需要进行平台还款，1：是，0：否")
	private Integer isTuandaiRepay;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建用户ID
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户ID")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新用户ID
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户ID")
	private String updateUser;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getBusinessCtype() {
		return businessCtype;
	}

	public void setBusinessCtype(String businessCtype) {
		this.businessCtype = businessCtype;
	}

	public String getBusinessStype() {
		return businessStype;
	}

	public void setBusinessStype(String businessStype) {
		this.businessStype = businessStype;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getRepaymentTypeId() {
		return repaymentTypeId;
	}

	public void setRepaymentTypeId(Integer repaymentTypeId) {
		this.repaymentTypeId = repaymentTypeId;
	}

	public BigDecimal getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(BigDecimal borrowMoney) {
		this.borrowMoney = borrowMoney;
	}

	public Integer getBorrowLimit() {
		return borrowLimit;
	}

	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	public Integer getBorrowLimitUnit() {
		return borrowLimitUnit;
	}

	public void setBorrowLimitUnit(Integer borrowLimitUnit) {
		this.borrowLimitUnit = borrowLimitUnit;
	}

	public BigDecimal getBorrowRate() {
		return borrowRate;
	}

	public void setBorrowRate(BigDecimal borrowRate) {
		this.borrowRate = borrowRate;
	}

	public Integer getBorrowRateUnit() {
		return borrowRateUnit;
	}

	public void setBorrowRateUnit(Integer borrowRateUnit) {
		this.borrowRateUnit = borrowRateUnit;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getOutputPlatformId() {
		return outputPlatformId;
	}

	public void setOutputPlatformId(Integer outputPlatformId) {
		this.outputPlatformId = outputPlatformId;
	}

	public Integer getIssueSplitType() {
		return issueSplitType;
	}

	public void setIssueSplitType(Integer issueSplitType) {
		this.issueSplitType = issueSplitType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceBusinessId() {
		return sourceBusinessId;
	}

	public void setSourceBusinessId(String sourceBusinessId) {
		this.sourceBusinessId = sourceBusinessId;
	}

	public Integer getIsTuandaiRepay() {
		return isTuandaiRepay;
	}

	public void setIsTuandaiRepay(Integer isTuandaiRepay) {
		this.isTuandaiRepay = isTuandaiRepay;
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

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "BasicBusiness{" +
			", businessId=" + businessId +
			", inputTime=" + inputTime +
			", businessType=" + businessType +
			", businessCtype=" + businessCtype +
			", businessStype=" + businessStype +
			", customerId=" + customerId +
			", customerName=" + customerName +
			", repaymentTypeId=" + repaymentTypeId +
			", borrowMoney=" + borrowMoney +
			", borrowLimit=" + borrowLimit +
			", borrowLimitUnit=" + borrowLimitUnit +
			", borrowRate=" + borrowRate +
			", borrowRateUnit=" + borrowRateUnit +
			", operatorId=" + operatorId +
			", operatorName=" + operatorName +
			", assetId=" + assetId +
			", companyId=" + companyId +
			", outputPlatformId=" + outputPlatformId +
			", issueSplitType=" + issueSplitType +
			", sourceType=" + sourceType +
			", sourceBusinessId=" + sourceBusinessId +
			", isTuandaiRepay=" + isTuandaiRepay +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
