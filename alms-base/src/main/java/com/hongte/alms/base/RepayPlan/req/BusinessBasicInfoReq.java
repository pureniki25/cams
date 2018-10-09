package com.hongte.alms.base.RepayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/4/20
 * 生成还款计划   业务的基本信息
 */
@ApiModel("创建还款计划的请求信息")
public class BusinessBasicInfoReq {


    /**
     * 资产端业务编号
     */
    @ApiModelProperty(required= true,value = "资产端业务编号")
    @NotNull(message = "BusinessBasicInfoReq资产端业务编号(businessId)不能为空")
    private String businessId;

    /**
     * 资产端原业务编号，非展期业务则与资产端业务编号一致，展期业务则存储展期业务的来源业务编号
     */
    @ApiModelProperty(required= true,value = "资产端原业务编号，非展期业务则与资产端业务编号一致，展期业务则存储展期业务的来源业务编号")
    @NotNull(message = "BusinessBasicInfoReq资产端原业务编号(orgBusinessId)不能为空")
    private String orgBusinessId;

    /**
     * 进件日期
     */
    @ApiModelProperty(required= true,value = "进件日期")
    @NotNull(message = "BusinessBasicInfoReq进件日期(inputTime)不能为空")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inputTime;

    /**
     * 业务类型
     */
    @ApiModelProperty(required= true,value = "业务类型: 1,车易贷展期;  2, 房速贷展期;   3,金融仓储;   4, 三农金融; 9,车易贷; 11,房速贷;  12,车全垫资代采;  13,扶贫贷;  14,汽车融资租赁;  15,二手车商贷; 20,一点车贷;25,信用贷")
    @NotNull(message = "BusinessBasicInfoReq业务类型(businessType)不能为空")
    private Integer businessType;


    /**
     * 业务所属子类型，若无则为空     *
     */
    @ApiModelProperty(value = "业务所属子类型，如：业主信用贷，小微企业贷，若无则为空")
    private String businessCtype;
    /**
     * 业务所属孙类型，若无则为空
     * 备注：这个业务所属的子类型，对应的信贷的字段是什么 需要与咏康核对一下
     */
    @ApiModelProperty(value = "业务所属孙类型，若无则为空")
    private String businessStype;
//    /**
//     * 客户资产端唯一编号
//     */
//    @ApiModelProperty(required= true,value = "客户资产端唯一编号")
//    @NotNull(message = "BusinessBasicInfoReq客户资产端唯一编号(customerId)不能为空")
//    private String customerId;
    /**
     * 客户姓名
     */
    @ApiModelProperty(required= true,value = "客户姓名")
    @NotNull(message = "BusinessBasicInfoReq客户姓名(customerName)不能为空")
    private String customerName;
    /**
     * 还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
     */
    @ApiModelProperty(required= true,value = "还款方式ID，1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息")
    @NotNull(message = "BusinessBasicInfoReq还款方式ID(repaymentTypeId)不能为空")
    private Integer repaymentTypeId;
    /**
     * 借款金额(元)
     */
    @ApiModelProperty(required= true,value = "借款金额(元)")
    @NotNull(message = "BusinessBasicInfoReq借款金额(borrowMoney)不能为空")
    private BigDecimal borrowMoney;
    /**
     * 借款期限  以月为单位
     */
    @ApiModelProperty(required= true,value = "借款期限")
    @NotNull(message = "BusinessBasicInfoReq借款期限(borrowLimit)不能为空")
    private Integer borrowLimit;

    /**
     * 借款利率(%)，如11%则存11.0
     */
    @ApiModelProperty(required= true,value = "借款利率(%)，如11%则存11.0")
    @NotNull(message = "BusinessBasicInfoReq借款利率(borrowRate)不能为空")
    private BigDecimal borrowRate;
    /**
     * 借款利率类型，1：年利率，2：月利率，3：日利率
     */
    @ApiModelProperty(required= true,value = "借款利率类型，1：年利率，2：月利率，3：日利率")
    @NotNull(message = "BusinessBasicInfoReq借款利率类型(borrowRateUnit)不能为空")
    private Integer borrowRateUnit;

    /**
     * 业务获取人ID
     */
    @ApiModelProperty(required= true,value = "业务获取人ID")
    @NotNull(message = "BusinessBasicInfoReq业务获取人ID(originalUserid)不能为空")
    private String originalUserid;
    /**
     * 业务获取人姓名
     */
    @ApiModelProperty(required= true,value = "业务获取人姓名")
    @NotNull(message = "BusinessBasicInfoReq业务获取人姓名(originalName)不能为空")
    private String originalName;


    /**
     * 业务主办人ID
     */
    @ApiModelProperty(required= true,value = "业务主办人ID")
    @NotNull(message = "BusinessBasicInfoReq业务主办人ID(operatorId)不能为空")
    private String operatorId;
    /**
     * 业务主办人姓名
     */
    @ApiModelProperty(required= true,value = "业务主办人姓名")
    @NotNull(message = "BusinessBasicInfoReq业务主办人姓名(operatorName)不能为空")
    private String operatorName;
//    /**
//     * 业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)
//     */
//    @ApiModelProperty(required= true,value = "业务所属资产端编号(对应tb_basic_asset_side的asset_side_id)")
//    private String assetId;
    /**
     * 业务所属分公司编号
     */
    @ApiModelProperty(required= true,value = "业务所属分公司编号")
    @NotNull(message = "BusinessBasicInfoReq业务所属分公司编号(companyId)不能为空")
    private String companyId;


    /**
     * 业务所属分公司名称
     */
    @ApiModelProperty(required= true,value = "业务所属分公司名称")
    @NotNull(message = "BusinessBasicInfoReq业务所属分公司名称(companyName)不能为空")
    private String companyName;

    /**
     * 业务所属片区编号
     */
    @ApiModelProperty(required= true,value = "业务所属片区编号")
    @NotNull(message = "BusinessBasicInfoReq业务所属片区编号(districtId)不能为空")
    private String districtId;
    /**
     * 业务所属片区名称
     */
    @ApiModelProperty(required= true,value = "业务所属片区名称")
    @NotNull(message = "BusinessBasicInfoReq业务所属片区名称(districtName)不能为空")
    private String districtName;

    /**
     * 标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
     */
    @ApiModelProperty(required= true,value = "标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务")
    @NotNull(message = "BusinessBasicInfoReq标识是否P2P拆标业务(issueSplitType)不能为空")
    private Integer issueSplitType;
    /**
     * 业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
     */
    @ApiModelProperty(required= true,value = "业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信")
    @NotNull(message = "BusinessBasicInfoReq业务来源(sourceType)不能为空")
    private Integer sourceType;
    /**
     * 原始来源业务的业务编号(当业务来源为结清再贷时，必填)
     */
    @ApiModelProperty(value = "原始来源业务的业务编号(当业务来源为结清再贷时，必填)")
    private String sourceBusinessId;


    /**
     * 是否展期业务，1：是，0：否
     */
    @ApiModelProperty(required= true,value = "是否展期业务，1：是，0：否")
    @NotNull(message = "BusinessBasicInfoReq是否展期业务(isRenewBusiness)不能为空")
    private Integer isRenewBusiness;
    
    /**
     * 资金端类型，1：团贷网 2：你我金融 3：粤财
     */
    @ApiModelProperty(value = "资金端类型，1：团贷网 2：你我金融 3粤财")
    private Integer outputPlatformId;
    
    


  
	
	public Integer getOutputPlatformId() {
		return outputPlatformId;
	}

	public void setOutputPlatformId(Integer outputPlatformId) {
		this.outputPlatformId = outputPlatformId;
	}

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

//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

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

//    public String getAssetId() {
//        return assetId;
//    }
//
//    public void setAssetId(String assetId) {
//        this.assetId = assetId;
//    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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


    public Integer getIsRenewBusiness() {
        return isRenewBusiness;
    }

    public void setIsRenewBusiness(Integer isRenewBusiness) {
        this.isRenewBusiness = isRenewBusiness;
    }

    public String getOrgBusinessId() {
        return orgBusinessId;
    }

    public void setOrgBusinessId(String orgBusinessId) {
        this.orgBusinessId = orgBusinessId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getOriginalUserid() {
        return originalUserid;
    }

    public void setOriginalUserid(String originalUserid) {
        this.originalUserid = originalUserid;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
