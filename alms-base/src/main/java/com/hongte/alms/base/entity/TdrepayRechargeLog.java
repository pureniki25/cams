package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 团贷网合规化还款标的充值记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-06
 */
@ApiModel
@TableName("tb_tdrepay_recharge_log")
public class TdrepayRechargeLog extends Model<TdrepayRechargeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="log_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer logId;
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
     * 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:信用贷)
     */
	@TableField("business_type")
	@ApiModelProperty(required= true,value = "业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:信用贷)")
	private Integer businessType;
    /**
     * 还款方式(1:到期还本息,2:每月付息到期还本,5:等额本息,9:分期还本付息)
     */
	@TableField("repayment_type")
	@ApiModelProperty(required= true,value = "还款方式(1:到期还本息,2:每月付息到期还本,5:等额本息,9:分期还本付息)")
	private Integer repaymentType;
    /**
     * 实还日期
     */
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
     * 标记当前资金分发操作是否为结清操作，0：非结清，1：正常结清，2：展期原标结清，3：坏账结清
     */
	@TableField("settle_type")
	@ApiModelProperty(required= true,value = "标记当前资金分发操作是否为结清操作，0：非结清，1：正常结清，2：展期原标结清，3：坏账结清")
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
     * 0：只垫付本息，1：全额垫付
     */
	@TableField("advance_type")
	@ApiModelProperty(required= true,value = "0：只垫付本息，1：全额垫付")
	private Integer advanceType;
    /**
     * 当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）
     */
	@TableField("is_complete")
	@ApiModelProperty(required= true,value = "当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）")
	private Integer isComplete;
    /**
     * 标的还款计划列表ID（实还流水ID）
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "标的还款计划列表ID（实还流水ID）")
	private String projPlanListId;
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
     * 平台状态
     */
	@TableField("plat_status")
	@ApiModelProperty(required= true,value = "平台状态")
	private String platStatus;
    /**
     * 平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)
     */
	@TableField("platform_repay_status")
	@ApiModelProperty(required= true,value = "平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)")
	private Integer platformRepayStatus;
    /**
     * 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
     */
	@TableField("process_status")
	@ApiModelProperty(required= true,value = "分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）")
	private Integer processStatus;
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


	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
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

	public Integer getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(Integer repaymentType) {
		this.repaymentType = repaymentType;
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

	public Integer getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(Integer advanceType) {
		this.advanceType = advanceType;
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

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
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
			", repaymentType=" + repaymentType +
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
			", advanceType=" + advanceType +
			", isComplete=" + isComplete +
			", projPlanListId=" + projPlanListId +
			", requestNo=" + requestNo +
			", userIp=" + userIp +
			", oidPartner=" + oidPartner +
			", platStatus=" + platStatus +
			", platformRepayStatus=" + platformRepayStatus +
			", processStatus=" + processStatus +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
