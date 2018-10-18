package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author wangjiguang
 * @since 2018-10-09
 */
@ApiModel
@TableName("tb_repayment_confirm_log_synch")
@ExcelTarget("RepaymentConfirmLogSynch")
public class RepaymentConfirmLogSynch extends Model<RepaymentConfirmLogSynch> {

    private static final long serialVersionUID = 1L;

    @TableId("confirm_log_id")
	@ApiModelProperty(required= true,value = "")
    @ExcelIgnore
	private String confirmLogId;
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="业务编号")
	private String businessId;
	@TableField("org_business_id")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String orgBusinessId;
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="期数")
	private String afterId;
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer period;
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer idx;
	@TableField("can_revoke")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer canRevoke;
	@TableField("is_cancelled")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer isCancelled;
	@TableField("proj_plan_json")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String projPlanJson;
	@TableField("repay_date")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="实还日期",format="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date repayDate;
	@TableField("fact_amount")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="实还金额")
	private BigDecimal factAmount;
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer repaySource;
	@TableField("surplus_amount")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="结余金额")
	private BigDecimal surplusAmount;
	@TableField("surplus_ref_id")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String surplusRefId;
	@TableField("surplus_use_ref_id")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String surplusUseRefId;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="财务确认日期",format="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="操作人员")
	private String createUser;
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Date updateTime;
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String updateUser;
	@TableField("create_user_name")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String createUserName;
	@TableField("last_push_status")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Integer lastPushStatus;
	@TableField("last_push_datetime")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private Date lastPushDatetime;
	@TableField("last_push_remark")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String lastPushRemark;
	
	@TableField("ext_business_type")
	@ApiModelProperty(required= true,value = "")
	@ExcelIgnore
	private String extBusinessType;
	
	@TableField("ext_business_ctype")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="业务类型")
	private String extBusinessCtype;
	@TableField("ext_company_name")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="所属分公司")
	private String extCompanyName;
	@TableField("ext_customer_name")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="客户姓名")
	private String extCustomerName;
	@TableField("ext_platform")
	@ApiModelProperty(required= true,value = "")
	@Excel(name="所属投资端")
	private String extPlatform;
    /**
     * 还款方式
     */
	@TableField("ext_repay_type")
	@ApiModelProperty(required= true,value = "还款方式")
	@Excel(name="还款方式")
	private String extRepayType;
    /**
     * 本金
     */
	@TableField("ext_item10")
	@ApiModelProperty(required= true,value = "本金")
	@Excel(name="本金")
	private BigDecimal extItem10;
    /**
     * 利息
     */
	@TableField("ext_item20")
	@ApiModelProperty(required= true,value = "利息")
	@Excel(name="利息")
	private BigDecimal extItem20;
    /**
     * 服务费
     */
	@TableField("ext_item30")
	@ApiModelProperty(required= true,value = "服务费")
	@Excel(name="服务费")
	private BigDecimal extItem30;
    /**
     * 平台费
     */
	@TableField("ext_item50")
	@ApiModelProperty(required= true,value = "平台费")
	@Excel(name="平台费")
	private BigDecimal extItem50;
    /**
     * 线上滞纳金
     */
	@TableField("ext_item60online")
	@ApiModelProperty(required= true,value = "线上滞纳金")
	@Excel(name="线上滞纳金")
	private BigDecimal extItem60online;
    /**
     * 线下滞纳金
     */
	@TableField("ext_item60offline")
	@ApiModelProperty(required= true,value = "线下滞纳金")
	@Excel(name="线下滞纳金")
	private BigDecimal extItem60offline;
    /**
     * 本金违约金
     */
	@TableField("ext_item70_bj")
	@ApiModelProperty(required= true,value = "本金违约金")
	@Excel(name="本金违约金")
	private BigDecimal extItem70Bj;
    /**
     * 平台违约金
     */
	@TableField("ext_item70_pt")
	@ApiModelProperty(required= true,value = "平台违约金")
	@Excel(name="平台违约金")
	private BigDecimal extItem70Pt;
    /**
     * 服务费违约金
     */
	@TableField("ext_item70_fw")
	@ApiModelProperty(required= true,value = "服务费违约金")
	@Excel(name="服务费违约金")
	private BigDecimal extItem70Fw;
    /**
     * 其他费用合计
     */
	@TableField("ext_other_fee")
	@ApiModelProperty(required= true,value = "其他费用合计")
	@Excel(name="其他费用合计")
	private BigDecimal extOtherFee;
    /**
     * 备注
     */
	@TableField("ext_remark")
	@ApiModelProperty(required= true,value = "备注")
	@Excel(name="备注")
	private String extRemark;


	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOrgBusinessId() {
		return orgBusinessId;
	}

	public void setOrgBusinessId(String orgBusinessId) {
		this.orgBusinessId = orgBusinessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public Integer getCanRevoke() {
		return canRevoke;
	}

	public void setCanRevoke(Integer canRevoke) {
		this.canRevoke = canRevoke;
	}

	public Integer getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Integer isCancelled) {
		this.isCancelled = isCancelled;
	}

	public String getProjPlanJson() {
		return projPlanJson;
	}

	public void setProjPlanJson(String projPlanJson) {
		this.projPlanJson = projPlanJson;
	}

	public Date getRepayDate() {
		return repayDate;
	}

	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}

	public BigDecimal getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	public Integer getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	public BigDecimal getSurplusAmount() {
		return surplusAmount;
	}

	public void setSurplusAmount(BigDecimal surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	public String getSurplusRefId() {
		return surplusRefId;
	}

	public void setSurplusRefId(String surplusRefId) {
		this.surplusRefId = surplusRefId;
	}

	public String getSurplusUseRefId() {
		return surplusUseRefId;
	}

	public void setSurplusUseRefId(String surplusUseRefId) {
		this.surplusUseRefId = surplusUseRefId;
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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Integer getLastPushStatus() {
		return lastPushStatus;
	}

	public void setLastPushStatus(Integer lastPushStatus) {
		this.lastPushStatus = lastPushStatus;
	}

	public Date getLastPushDatetime() {
		return lastPushDatetime;
	}

	public void setLastPushDatetime(Date lastPushDatetime) {
		this.lastPushDatetime = lastPushDatetime;
	}

	public String getLastPushRemark() {
		return lastPushRemark;
	}

	public void setLastPushRemark(String lastPushRemark) {
		this.lastPushRemark = lastPushRemark;
	}

	public String getExtBusinessCtype() {
		return extBusinessCtype;
	}

	public void setExtBusinessCtype(String extBusinessCtype) {
		this.extBusinessCtype = extBusinessCtype;
	}

	public String getExtCompanyName() {
		return extCompanyName;
	}

	public void setExtCompanyName(String extCompanyName) {
		this.extCompanyName = extCompanyName;
	}

	public String getExtCustomerName() {
		return extCustomerName;
	}

	public void setExtCustomerName(String extCustomerName) {
		this.extCustomerName = extCustomerName;
	}

	public String getExtPlatform() {
		return extPlatform;
	}

	public void setExtPlatform(String extPlatform) {
		this.extPlatform = extPlatform;
	}

	public String getExtRepayType() {
		return extRepayType;
	}

	public void setExtRepayType(String extRepayType) {
		this.extRepayType = extRepayType;
	}

	public BigDecimal getExtItem10() {
		return extItem10;
	}

	public void setExtItem10(BigDecimal extItem10) {
		this.extItem10 = extItem10;
	}

	public BigDecimal getExtItem20() {
		return extItem20;
	}

	public void setExtItem20(BigDecimal extItem20) {
		this.extItem20 = extItem20;
	}

	public BigDecimal getExtItem30() {
		return extItem30;
	}

	public void setExtItem30(BigDecimal extItem30) {
		this.extItem30 = extItem30;
	}

	public BigDecimal getExtItem50() {
		return extItem50;
	}

	public void setExtItem50(BigDecimal extItem50) {
		this.extItem50 = extItem50;
	}

	public BigDecimal getExtItem60online() {
		return extItem60online;
	}

	public void setExtItem60online(BigDecimal extItem60online) {
		this.extItem60online = extItem60online;
	}

	public BigDecimal getExtItem60offline() {
		return extItem60offline;
	}

	public void setExtItem60offline(BigDecimal extItem60offline) {
		this.extItem60offline = extItem60offline;
	}

	public BigDecimal getExtItem70Bj() {
		return extItem70Bj;
	}

	public void setExtItem70Bj(BigDecimal extItem70Bj) {
		this.extItem70Bj = extItem70Bj;
	}

	public BigDecimal getExtItem70Pt() {
		return extItem70Pt;
	}

	public void setExtItem70Pt(BigDecimal extItem70Pt) {
		this.extItem70Pt = extItem70Pt;
	}

	public BigDecimal getExtItem70Fw() {
		return extItem70Fw;
	}

	public void setExtItem70Fw(BigDecimal extItem70Fw) {
		this.extItem70Fw = extItem70Fw;
	}

	public BigDecimal getExtOtherFee() {
		return extOtherFee;
	}

	public void setExtOtherFee(BigDecimal extOtherFee) {
		this.extOtherFee = extOtherFee;
	}

	public String getExtRemark() {
		return extRemark;
	}

	public void setExtRemark(String extRemark) {
		this.extRemark = extRemark;
	}

	@Override
	protected Serializable pkVal() {
		return this.confirmLogId;
	}

	@Override
	public String toString() {
		return "RepaymentConfirmLogSynch{" +
			", confirmLogId=" + confirmLogId +
			", businessId=" + businessId +
			", orgBusinessId=" + orgBusinessId +
			", afterId=" + afterId +
			", period=" + period +
			", idx=" + idx +
			", canRevoke=" + canRevoke +
			", isCancelled=" + isCancelled +
			", projPlanJson=" + projPlanJson +
			", repayDate=" + repayDate +
			", factAmount=" + factAmount +
			", repaySource=" + repaySource +
			", surplusAmount=" + surplusAmount +
			", surplusRefId=" + surplusRefId +
			", surplusUseRefId=" + surplusUseRefId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", createUserName=" + createUserName +
			", lastPushStatus=" + lastPushStatus +
			", lastPushDatetime=" + lastPushDatetime +
			", lastPushRemark=" + lastPushRemark +
			", extBusinessCtype=" + extBusinessCtype +
			", extCompanyName=" + extCompanyName +
			", extCustomerName=" + extCustomerName +
			", extPlatform=" + extPlatform +
			", extRepayType=" + extRepayType +
			", extItem10=" + extItem10 +
			", extItem20=" + extItem20 +
			", extItem30=" + extItem30 +
			", extItem50=" + extItem50 +
			", extItem60online=" + extItem60online +
			", extItem60offline=" + extItem60offline +
			", extItem70Bj=" + extItem70Bj +
			", extItem70Pt=" + extItem70Pt +
			", extItem70Fw=" + extItem70Fw +
			", extOtherFee=" + extOtherFee +
			", extRemark=" + extRemark +
			"}";
	}
}
