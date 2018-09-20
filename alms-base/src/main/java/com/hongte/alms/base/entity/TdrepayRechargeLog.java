package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 团贷网合规化还款标的充值记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-07-03
 */
@ApiModel
@TableName("tb_tdrepay_recharge_log")
public class TdrepayRechargeLog extends Model<TdrepayRechargeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("log_id")
	@ApiModelProperty(required= true,value = "主键ID")
	private String logId;
    /**
     * 标的ID
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 业务所属资产端，1、鸿特信息，2、 一点车贷
     */
	@TableField("asset_type")
	@ApiModelProperty(required= true,value = "业务所属资产端，1、鸿特信息，2、 一点车贷")
	private Integer assetType;
    /**
     * 原业务编号
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "原业务编号")
	private String origBusinessId;
    /**
     * 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:商贸贷,26:业主贷,27:家装分期,28:商贸共借;29:业主共借)
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:商贸贷,26:业主贷,27:家装分期,28:商贸共借;29:业主共借)")
	private Integer businessType;
    /**
     * 实还日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "实还日期")
	private Date factRepayDate;
    /**
     * 借款人
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "借款人")
	private String customerName;
    /**
     * 分公司
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "分公司")
	private String companyName;
    /**
     * 还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
     */
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣")
	private Integer repaySource;
    /**
     * 资产端期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "资产端期数")
	private String afterId;
    /**
     * 财务确认时间或成功代扣时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField("confirm_time")
	@ApiModelProperty(required= true,value = "财务确认时间或成功代扣时间")
	private Date confirmTime;
    /**
     * (代充值资金分发接口参数)团贷网用户唯一编号
     */
	@TableField("td_user_id")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)团贷网用户唯一编号")
	private String tdUserId;
    /**
     * (代充值资金分发接口参数)批次分发总金额(保留两位小数)
     */
	@TableField("total_amount")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)批次分发总金额(保留两位小数)")
	private BigDecimal totalAmount;
    /**
     * (代充值资金分发接口参数)分发批次号(批次唯一标识)
     */
	@TableField("batch_id")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)分发批次号(批次唯一标识)")
	private String batchId;
    /**
     * 本次还款对应团贷网的期次
     */
	@ApiModelProperty(required= true,value = "本次还款对应团贷网的期次")
	private Integer period;
    /**
     * (代充值资金分发接口参数)出款方帐号
     */
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)出款方帐号")
	private String userId;
    /**
     * (代充值资金分发接口参数)充值金额
     */
	@TableField("recharge_amount")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)充值金额")
	private BigDecimal rechargeAmount;
    /**
     * 标记当前资金分发操作是否为结清操作，0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清
     */
	@TableField("settle_type")
	@ApiModelProperty(required= true,value = "标记当前资金分发操作是否为结清操作，0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清")
	private Integer settleType;
    /**
     * 实收总金额
     */
	@TableField("fact_repay_amount")
	@ApiModelProperty(required= true,value = "实收总金额")
	private BigDecimal factRepayAmount;
    /**
     * 流水合计
     */
	@TableField("resource_amount")
	@ApiModelProperty(required= true,value = "流水合计")
	private BigDecimal resourceAmount;
    /**
     * 当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）
     */
	@TableField("is_complete")
	@ApiModelProperty(required= true,value = "当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）")
	private Integer isComplete;
    /**
     * 标的还款计划列表ID
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "标的还款计划列表ID")
	private String projPlanListId;
    /**
     * 实还流水ID
     */
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "实还流水ID")
	private String confirmLogId;
    /**
     * (代充值资金分发接口参数)订单唯一标识
     */
	@TableField("request_no")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)订单唯一标识")
	private String requestNo;
    /**
     * (代充值资金分发接口参数)客户端IP
     */
	@TableField("user_ip")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)客户端IP")
	private String userIp;
    /**
     * (代充值资金分发接口参数)资产端账户唯一编号
     */
	@TableField("oid_partner")
	@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)资产端账户唯一编号")
	private String oidPartner;
    /**
     * 平台状态 1：已还款，2：逾期 3：待还款
     */
	@TableField("plat_status")
	@ApiModelProperty(required= true,value = "平台状态 1：已还款，2：逾期 3：待还款")
	private String platStatus;
    /**
     * 平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)
     */
	@TableField("platform_repay_status")
	@ApiModelProperty(required= true,value = "平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)")
	private Integer platformRepayStatus;
    /**
     * 是否撤销确认还款（1、否；2、是）
     */
	@TableField("is_valid")
	@ApiModelProperty(required= true,value = "是否撤销确认还款（1、否；2、是）")
	private Integer isValid;
    /**
     * 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
     */
	@TableField("process_status")
	@ApiModelProperty(required= true,value = "分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）")
	private Integer processStatus;
    /**
     * 资产端对团贷网通用合规化还款流程处理状态(0:未处理,1:处理中,2:成功,3:失败)
     */
	@ApiModelProperty(required= true,value = "资产端对团贷网通用合规化还款流程处理状态(0:未处理,1:处理中,2:成功,3:失败)")
	private Integer status;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
	
	/**
     * 最后推送状态0未推送1推送成功2推送失败3不需要推送
     */
	@TableField("last_push_status")
	private Integer lastPushStatus;
	
	/**
     * 最后推送时间
     */
	@TableField("last_push_datetime")
	private Date lastPushDatetime;
	
	/**
     * 最后推送备注
     */
	@TableField("last_push_remark")
	private String lastPushRemark;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getTdUserId() {
		return tdUserId;
	}

	public void setTdUserId(String tdUserId) {
		this.tdUserId = tdUserId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public BigDecimal getFactRepayAmount() {
		return factRepayAmount;
	}

	public void setFactRepayAmount(BigDecimal factRepayAmount) {
		this.factRepayAmount = factRepayAmount;
	}

	public BigDecimal getResourceAmount() {
		return resourceAmount;
	}

	public void setResourceAmount(BigDecimal resourceAmount) {
		this.resourceAmount = resourceAmount;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
	}

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getOidPartner() {
		return oidPartner;
	}

	public void setOidPartner(String oidPartner) {
		this.oidPartner = oidPartner;
	}

	public String getPlatStatus() {
		return platStatus;
	}

	public void setPlatStatus(String platStatus) {
		this.platStatus = platStatus;
	}

	public Integer getPlatformRepayStatus() {
		return platformRepayStatus;
	}

	public void setPlatformRepayStatus(Integer platformRepayStatus) {
		this.platformRepayStatus = platformRepayStatus;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Override
	protected Serializable pkVal() {
		return this.logId;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeLog{" +
			", logId=" + logId +
			", projectId=" + projectId +
			", assetType=" + assetType +
			", origBusinessId=" + origBusinessId +
			", businessType=" + businessType +
			", factRepayDate=" + factRepayDate +
			", customerName=" + customerName +
			", companyName=" + companyName +
			", repaySource=" + repaySource +
			", afterId=" + afterId +
			", confirmTime=" + confirmTime +
			", tdUserId=" + tdUserId +
			", totalAmount=" + totalAmount +
			", batchId=" + batchId +
			", period=" + period +
			", userId=" + userId +
			", rechargeAmount=" + rechargeAmount +
			", settleType=" + settleType +
			", factRepayAmount=" + factRepayAmount +
			", resourceAmount=" + resourceAmount +
			", isComplete=" + isComplete +
			", projPlanListId=" + projPlanListId +
			", confirmLogId=" + confirmLogId +
			", requestNo=" + requestNo +
			", userIp=" + userIp +
			", oidPartner=" + oidPartner +
			", platStatus=" + platStatus +
			", platformRepayStatus=" + platformRepayStatus +
			", isValid=" + isValid +
			", processStatus=" + processStatus +
			", status=" + status +
			", remark=" + remark +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
