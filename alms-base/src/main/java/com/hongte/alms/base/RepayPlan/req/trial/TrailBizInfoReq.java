package com.hongte.alms.base.RepayPlan.req.trial;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   业务的基本信息
 */
@ApiModel("创建还款计划的请求信息")
public class TrailBizInfoReq {


    /**
     * 资产端业务编号
     */
    @ApiModelProperty(value = "资产端业务编号")
    private String businessId;
//
//    /**
//     * 资产端原业务编号
//     */
//    @ApiModelProperty(required= true,value = "资产端业原务编号")
//    private String orgBusinessId;
//
//    /**
//     * 进件日期
//     */
//    @ApiModelProperty(required= true,value = "进件日期")
//    private Date inputTime;

    /**
     * 业务类型
     */
    @ApiModelProperty(required= true,value = "业务类型: 1,车易贷展期;  2, 房速贷展期;   3,金融仓储;   4, 三农金融; 9,车易贷; 11,房速贷;  12,车全垫资代采;  13,扶贫贷;  14,汽车融资租赁;  15,二手车商贷; 20,一点车贷;25,信用贷")
    @NotNull(message = "业务类型(businessType)不能为空")
    private Integer businessType;


    /**
     * 业务所属子类型，若无则为空     *
     */
    @ApiModelProperty(value = "业务所属子类型，如：业主信用贷，小微企业贷，若无则为空")
    private String businessCtype;
//    /**
//     * 业务所属孙类型，若无则为空
//     * 备注：这个业务所属的子类型，对应的信贷的字段是什么 需要与咏康核对一下
//     */
//    @ApiModelProperty(required= true,value = "业务所属孙类型，若无则为空")
//    private String businessStype;
//    /**
//     * 客户资产端唯一编号
//     */
//    @ApiModelProperty(required= true,value = "客户资产端唯一编号")
//    private String customerId;
//    /**
//     * 客户姓名
//     */
//    @ApiModelProperty(required= true,value = "客户姓名")
//    private String customerName;
//    /**
//     * 还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
//     */
//    @ApiModelProperty(required= true,value = "还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息")
//    private Integer repaymentTypeId;
    /**
     * 借款金额(元)
     */
    @ApiModelProperty(required= true,value = "借款金额(元)")
    @NotNull(message = "借款金额(borrowMoney)不能为空")
    private BigDecimal borrowMoney;
//    /**
//     * 借款期限  以月为单位
//     */
//    @ApiModelProperty(required= true,value = "借款期限")
//    private Integer borrowLimit;
//    /**
//     * 借款期限单位，1：月，2：天
//     */
//    @ApiModelProperty(required= true,value = "借款期限单位，1：月，2：天")
//    private Integer borrowLimitUnit;
//    /**
//     * 借款利率(%)，如11%则存11.0
//     */
//    @ApiModelProperty(required= true,value = "借款利率(%)，如11%则存11.0")
//    private BigDecimal borrowRate;
//    /**
//     * 借款利率类型，1：年利率，2：月利率，3：日利率
//     */
//    @ApiModelProperty(required= true,value = "借款利率类型，1：年利率，2：月利率，3：日利率")
//    private Integer borrowRateUnit;


//    /**
//     * 业务主办人ID
//     */
//    @ApiModelProperty(required= true,value = "业务主办人ID")
//    private String operatorId;
//    /**
//     * 业务主办人姓名
//     */
//    @ApiModelProperty(required= true,value = "业务主办人姓名")
//    private String operatorName;
//    /**
//     * 业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)
//     */
//    @ApiModelProperty(required= true,value = "业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)")
//    private String assetId;
//    /**
//     * 业务所属分公司编号
//     */
//    @ApiModelProperty(required= true,value = "业务所属分公司编号")
//    private String companyId;
//    /**
//     * 出款平台ID，0：线下出款，1：团贷网P2P上标
//     */
//    @ApiModelProperty(required= true,value = "出款平台ID，0：线下出款，1：团贷网P2P上标")
//    private Integer outputPlatformId;
//    /**
//     * 标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
//     */
//    @ApiModelProperty(required= true,value = "标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务")
//    private Integer issueSplitType;
//    /**
//     * 业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
//     */
//    @ApiModelProperty(required= true,value = "业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信")
//    private Integer sourceType;
//    /**
//     * 原始来源业务的业务编号(当业务来源为结清再贷时，必填)
//     */
//    @ApiModelProperty(value = "原始来源业务的业务编号(当业务来源为结清再贷时，必填)")
//    private String sourceBusinessId;
//
//    /**
//     * 是否需要进行平台还款，1：是，0：否
//     */
//    @ApiModelProperty(required= true,value = "是否需要进行平台还款，1：是，0：否")
//    private Integer isTuandaiRepay;
//
//    /**
//     * 是否展期业务，1：是，0：否
//     */
//    @ApiModelProperty(required= true,value = "是否展期业务，1：是，0：否")
//    private Integer isRenewBusiness;


//    public String getBusinessId() {
//        return businessId;
//    }
//
//    public void setBusinessId(String businessId) {
//        this.businessId = businessId;
//    }
//
//    public Date getInputTime() {
//        return inputTime;
//    }
//
//    public void setInputTime(Date inputTime) {
//        this.inputTime = inputTime;
//    }

//    public Integer getBusinessType() {
//        return businessType;
//    }
//
//    public void setBusinessType(Integer businessType) {
//        this.businessType = businessType;
//    }

//    public String getBusinessCtype() {
//        return businessCtype;
//    }
//
//    public void setBusinessCtype(String businessCtype) {
//        this.businessCtype = businessCtype;
//    }
//
//    public String getBusinessStype() {
//        return businessStype;
//    }
//
//    public void setBusinessStype(String businessStype) {
//        this.businessStype = businessStype;
//    }
//
//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }
//
//    public String getCustomerName() {
//        return customerName;
//    }
//
//    public void setCustomerName(String customerName) {
//        this.customerName = customerName;
//    }

//    public Integer getRepaymentTypeId() {
//        return repaymentTypeId;
//    }
//
//    public void setRepaymentTypeId(Integer repaymentTypeId) {
//        this.repaymentTypeId = repaymentTypeId;
//    }
//
    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
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
//
//    public Integer getBorrowLimit() {
//        return borrowLimit;
//    }
//
//    public void setBorrowLimit(Integer borrowLimit) {
//        this.borrowLimit = borrowLimit;
//    }

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

//    public Integer getBorrowLimitUnit() {
//        return borrowLimitUnit;
//    }
//
//    public void setBorrowLimitUnit(Integer borrowLimitUnit) {
//        this.borrowLimitUnit = borrowLimitUnit;
//    }
//
//    public BigDecimal getBorrowRate() {
//        return borrowRate;
//    }
//
//    public void setBorrowRate(BigDecimal borrowRate) {
//        this.borrowRate = borrowRate;
//    }
//
//    public Integer getBorrowRateUnit() {
//        return borrowRateUnit;
//    }
//
//    public void setBorrowRateUnit(Integer borrowRateUnit) {
//        this.borrowRateUnit = borrowRateUnit;
//    }
////
//    public String getOperatorId() {
//        return operatorId;
//    }
//
//    public void setOperatorId(String operatorId) {
//        this.operatorId = operatorId;
//    }
//
//    public String getOperatorName() {
//        return operatorName;
//    }
//
//    public void setOperatorName(String operatorName) {
//        this.operatorName = operatorName;
//    }
//
//    public String getAssetId() {
//        return assetId;
//    }
//
//    public void setAssetId(String assetId) {
//        this.assetId = assetId;
//    }
//
//    public String getCompanyId() {
//        return companyId;
//    }
//
//    public void setCompanyId(String companyId) {
//        this.companyId = companyId;
//    }
//
//    public Integer getOutputPlatformId() {
//        return outputPlatformId;
//    }
//
//    public void setOutputPlatformId(Integer outputPlatformId) {
//        this.outputPlatformId = outputPlatformId;
//    }
//
//    public Integer getIssueSplitType() {
//        return issueSplitType;
//    }
//
//    public void setIssueSplitType(Integer issueSplitType) {
//        this.issueSplitType = issueSplitType;
//    }
//
//    public Integer getSourceType() {
//        return sourceType;
//    }
//
//    public void setSourceType(Integer sourceType) {
//        this.sourceType = sourceType;
//    }
//
//    public String getSourceBusinessId() {
//        return sourceBusinessId;
//    }
//
//    public void setSourceBusinessId(String sourceBusinessId) {
//        this.sourceBusinessId = sourceBusinessId;
//    }
//
//    public Integer getIsTuandaiRepay() {
//        return isTuandaiRepay;
//    }
//
//    public void setIsTuandaiRepay(Integer isTuandaiRepay) {
//        this.isTuandaiRepay = isTuandaiRepay;
//    }
//
//
//    public Integer getIsRenewBusiness() {
//        return isRenewBusiness;
//    }
//
//    public void setIsRenewBusiness(Integer isRenewBusiness) {
//        this.isRenewBusiness = isRenewBusiness;
//    }
//
//    public String getOrgBusinessId() {
//        return orgBusinessId;
//    }
//
//    public void setOrgBusinessId(String orgBusinessId) {
//        this.orgBusinessId = orgBusinessId;
//    }
}
