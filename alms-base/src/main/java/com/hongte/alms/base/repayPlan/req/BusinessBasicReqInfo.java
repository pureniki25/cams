package com.hongte.alms.base.repayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   业务的基本
 */
@ApiModel("创建还款计划的请求信息")
public class BusinessBasicReqInfo {



    /**
     * 资产端业务编号
     */
    @ApiModelProperty(required= true,value = "资产端业务编号")
    private String businessId;
    /**
     * 进件日期
     */
    @ApiModelProperty(required= true,value = "进件日期")
    private Date inputTime;

    /**
     * 信贷的业务类型（对应tb_basic_business_type的xd_business_type_id)
     */
    @ApiModelProperty(required= true,value = "信贷的业务类型")
    private String  xdBusinessType;
    /**
     * 业务所属子类型，若无则为空
     * 备注：这个业务所属的子类型，对应的信贷的字段是什么 需要与咏康核对一下
     */
    @ApiModelProperty(required= true,value = "业务所属子类型，若无则为空")
    private String businessCtype;
    /**
     * 业务所属孙类型，若无则为空
     * 备注：这个业务所属的子类型，对应的信贷的字段是什么 需要与咏康核对一下
     */
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

    public String getXdBusinessType() {
        return xdBusinessType;
    }

    public void setXdBusinessType(String xdBusinessType) {
        this.xdBusinessType = xdBusinessType;
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



}
