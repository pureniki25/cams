package com.hongte.alms.base.collection.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/1/30
 * 贷后台账查询参数
 */
@ApiModel(value="贷后台账查询请求对象",description="贷后台账查询请求对象")
public class AfterLoanStandingBookReq extends PageRequest {
    @ApiModelProperty(value="区域ID",name="areaId",example="test")
    private String areaId;  //区域ID

    @ApiModelProperty(value="分公司ID",name="companyId",example="test",dataType = "string")
    private String companyId; //分公司ID

    @ApiModelProperty(value="应还日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Date showRepayDateBegin; //应还日期  开始
    @ApiModelProperty(value="应还日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Date showRepayDateEnd;  //应还日期 结束
//   private Date   showRepayDateRange;//应还日期 区间 包含开始和结束时间
    @ApiModelProperty(value="应还日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Date realRepayDateBegin; //实还日期 开始

//    private Date realRepayDateRange;//实还日期  区间 包含开始和结束时间
    @ApiModelProperty(value="应还日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Integer  delayDaysBegin;    //逾期天数 开始

    @ApiModelProperty(value="实还日期 结束",name="realRepayDateEnd",dataType = "java.util.Date")
    private Date realRepayDateEnd; //实还日期 结束

    @ApiModelProperty(value="逾期天数 结束",name="delayDaysEnd",dataType = "int")
    private Integer delayDaysEnd; //逾期天数 结束

    @ApiModelProperty(value="催收级别",name="collectLevel",dataType = "int")
    private Integer collectLevel;  //催收级别

    @ApiModelProperty(value="业务获取",name="operatorName",dataType = "String")
    private String  operatorName;  //业务获取

    @ApiModelProperty(value="业务编号",name="businessId",dataType = "String")
    private String  businessId;  //业务编号

    @ApiModelProperty(value="业务类型",name="businessType",dataType = "int")
    private Integer businessType;  //业务类型

    @ApiModelProperty(value="清算一",name="liquidationOne",dataType = "String")
    private String  liquidationOne; //清算一

    @ApiModelProperty(value="清算二",name="liquidationTow",dataType = "String")
    private String  liquidationTow;  //清算二

    @ApiModelProperty(value="业务状态",name="businessStatus",dataType = "int")
    private Integer businessStatus;   //业务状态

    @ApiModelProperty(value="还款状态",name="repayStatus",dataType = "int")
    private Integer repayStatus;      //还款状态

    @ApiModelProperty(value="客户名称",name="customerName",dataType = "String")
    private String  customerName;  //客户名称


    @ApiModelProperty(value="还款计划ID多个ID以逗号分隔",name="crpIds",dataType = "String")
    private String  crpIds;  //还款计划ID字符串  用于接收界面传参
    private String[]  crpIdsArray;  //还款计划ID数组  用于数据库查询



    private List<String> customerIds; //客户ID列表

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

    public Date getShowRepayDateBegin() {
        return showRepayDateBegin;
    }

    public void setShowRepayDateBegin(Date showRepayDateBegin) {
        this.showRepayDateBegin = showRepayDateBegin;
    }

    public Date getShowRepayDateEnd() {
        return showRepayDateEnd;
    }

    public void setShowRepayDateEnd(Date showRepayDateEnd) {
        this.showRepayDateEnd = showRepayDateEnd;
    }

    public Date getRealRepayDateBegin() {
        return realRepayDateBegin;
    }

    public void setRealRepayDateBegin(Date realRepayDateBegin) {
        this.realRepayDateBegin = realRepayDateBegin;
    }

    public Date getRealRepayDateEnd() {
        return realRepayDateEnd;
    }

    public void setRealRepayDateEnd(Date realRepayDateEnd) {
        this.realRepayDateEnd = realRepayDateEnd;
    }

    public Integer getDelayDaysBegin() {
        return delayDaysBegin;
    }

    public void setDelayDaysBegin(Integer delayDaysBegin) {
        this.delayDaysBegin = delayDaysBegin;
    }

    public Integer getDelayDaysEnd() {
        return delayDaysEnd;
    }

    public void setDelayDaysEnd(Integer delayDaysEnd) {
        this.delayDaysEnd = delayDaysEnd;
    }

    public Integer getCollectLevel() {
        return collectLevel;
    }

    public void setCollectLevel(Integer collectLevel) {
        this.collectLevel = collectLevel;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getLiquidationOne() {
        return liquidationOne;
    }

    public void setLiquidationOne(String liquidationOne) {
        this.liquidationOne = liquidationOne;
    }

    public String getLiquidationTow() {
        return liquidationTow;
    }

    public void setLiquidationTow(String liquidationTow) {
        this.liquidationTow = liquidationTow;
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Integer getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<String> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<String> customerIds) {
        this.customerIds = customerIds;
    }

    public String getCrpIds() {
        return crpIds;
    }

    public void setCrpIds(String crpIds) {
        this.crpIds = crpIds;
        this.crpIdsArray = getCrpIds().split(",");
    }

    public String[] getCrpIdsArray() {
        return crpIdsArray;
    }

    public void setCrpIdsArray(String[] crpIdsArray) {
        this.crpIdsArray = crpIdsArray;
    }
}
