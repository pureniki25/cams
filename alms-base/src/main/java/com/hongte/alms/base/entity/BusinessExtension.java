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
 * [业务基本信息扩展表]
 * </p>
 *
 * @author 刘正全
 * @since 2018-08-01
 */
@ApiModel
@TableName("tb_business_extension")
public class BusinessExtension extends Model<BusinessExtension> {

    private static final long serialVersionUID = 1L;

    /**
     * [业务单号]
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "[业务单号]")
	private String businessId;
    /**
     * [是否开票,null或0:未开票,1:已开票]
     */
	@TableField("invoice_type")
	@ApiModelProperty(required= true,value = "[是否开票,null或0:未开票,1:已开票]")
	private Integer invoiceType;
    /**
     * [开票类型ID（具体的开票类型名称需要关联配置表获取），1：普通发票，2：增值税发票]
     */
	@TableField("invoice_category")
	@ApiModelProperty(required= true,value = "[开票类型ID（具体的开票类型名称需要关联配置表获取），1：普通发票，2：增值税发票]")
	private String invoiceCategory;
    /**
     * [开票经办人]
     */
	@TableField("invoice_user")
	@ApiModelProperty(required= true,value = "[开票经办人]")
	private String invoiceUser;
    /**
     * [开票日期]
     */
	@TableField("invoice_date")
	@ApiModelProperty(required= true,value = "[开票日期]")
	private Date invoiceDate;
    /**
     * [开票备注]
     */
	@TableField("invoice_remark")
	@ApiModelProperty(required= true,value = "[开票备注]")
	private String invoiceRemark;
    /**
     * [开票操作人ID]
     */
	@TableField("invoice_user_id")
	@ApiModelProperty(required= true,value = "[开票操作人ID]")
	private String invoiceUserId;
    /**
     * [逾期费率]
     */
	@TableField("overdue_ratio")
	@ApiModelProperty(required= true,value = "[逾期费率]")
	private BigDecimal overdueRatio;
    /**
     * [线上逾期费率]
     */
	@TableField("online_overdue_ratio")
	@ApiModelProperty(required= true,value = "[线上逾期费率]")
	private BigDecimal onlineOverdueRatio;
    /**
     * [线下逾期费率]
     */
	@TableField("offline_overdue_ratio")
	@ApiModelProperty(required= true,value = "[线下逾期费率]")
	private BigDecimal offlineOverdueRatio;
    /**
     * [财务是否已提现，null或0为未提现登记，1为已提现登记]
     */
	@TableField("finance_withdraw_type")
	@ApiModelProperty(required= true,value = "[财务是否已提现，null或0为未提现登记，1为已提现登记]")
	private Integer financeWithdrawType;
    /**
     * [财务提现登记对应的日志ID]
     */
	@TableField("finance_withdraw_log_id")
	@ApiModelProperty(required= true,value = "[财务提现登记对应的日志ID]")
	private Integer financeWithdrawLogId;
    /**
     * [合作车行是否已提现，null或0为未提现登记，1为已提现登记]
     */
	@TableField("cooperation_withdraw_type")
	@ApiModelProperty(required= true,value = "[合作车行是否已提现，null或0为未提现登记，1为已提现登记]")
	private Integer cooperationWithdrawType;
    /**
     * [合作车行提现登记对应的日志ID]
     */
	@TableField("cooperation_withdraw_log_id")
	@ApiModelProperty(required= true,value = "[合作车行提现登记对应的日志ID]")
	private Integer cooperationWithdrawLogId;
    /**
     * [0或null表示未上传存管标附件到FTP，1表示已上传]
     */
	@TableField("is_upload_attachment")
	@ApiModelProperty(required= true,value = "[0或null表示未上传存管标附件到FTP，1表示已上传]")
	private Integer isUploadAttachment;
    /**
     * [存管标附件服务器路径]
     */
	@TableField("upload_attachment_path")
	@ApiModelProperty(required= true,value = "[存管标附件服务器路径]")
	private String uploadAttachmentPath;
    /**
     * [存管标附件上传日期]
     */
	@TableField("upload_attachment_date")
	@ApiModelProperty(required= true,value = "[存管标附件上传日期]")
	private Date uploadAttachmentDate;
    /**
     * [存管标附件上传操作人]
     */
	@TableField("upload_attachment_user")
	@ApiModelProperty(required= true,value = "[存管标附件上传操作人]")
	private String uploadAttachmentUser;
    /**
     * 是否结清，0为未结清，1为已结清
     */
	@TableField("is_settle")
	@ApiModelProperty(required= true,value = "是否结清，0为未结清，1为已结清")
	private Integer isSettle;
    /**
     * 其他
     */
	@TableField("other_text")
	@ApiModelProperty(required= true,value = "其他")
	private String otherText;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 结清情况
     */
	@TableField("settle_status")
	@ApiModelProperty(required= true,value = "结清情况")
	private String settleStatus;
    /**
     * [月还逾期比例]
     */
	@TableField("overdue_month_ratio")
	@ApiModelProperty(required= true,value = "[月还逾期比例]")
	private BigDecimal overdueMonthRatio;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceCategory() {
		return invoiceCategory;
	}

	public void setInvoiceCategory(String invoiceCategory) {
		this.invoiceCategory = invoiceCategory;
	}

	public String getInvoiceUser() {
		return invoiceUser;
	}

	public void setInvoiceUser(String invoiceUser) {
		this.invoiceUser = invoiceUser;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public String getInvoiceUserId() {
		return invoiceUserId;
	}

	public void setInvoiceUserId(String invoiceUserId) {
		this.invoiceUserId = invoiceUserId;
	}

	public BigDecimal getOverdueRatio() {
		return overdueRatio;
	}

	public void setOverdueRatio(BigDecimal overdueRatio) {
		this.overdueRatio = overdueRatio;
	}

	public BigDecimal getOnlineOverdueRatio() {
		return onlineOverdueRatio;
	}

	public void setOnlineOverdueRatio(BigDecimal onlineOverdueRatio) {
		this.onlineOverdueRatio = onlineOverdueRatio;
	}

	public BigDecimal getOfflineOverdueRatio() {
		return offlineOverdueRatio;
	}

	public void setOfflineOverdueRatio(BigDecimal offlineOverdueRatio) {
		this.offlineOverdueRatio = offlineOverdueRatio;
	}

	public Integer getFinanceWithdrawType() {
		return financeWithdrawType;
	}

	public void setFinanceWithdrawType(Integer financeWithdrawType) {
		this.financeWithdrawType = financeWithdrawType;
	}

	public Integer getFinanceWithdrawLogId() {
		return financeWithdrawLogId;
	}

	public void setFinanceWithdrawLogId(Integer financeWithdrawLogId) {
		this.financeWithdrawLogId = financeWithdrawLogId;
	}

	public Integer getCooperationWithdrawType() {
		return cooperationWithdrawType;
	}

	public void setCooperationWithdrawType(Integer cooperationWithdrawType) {
		this.cooperationWithdrawType = cooperationWithdrawType;
	}

	public Integer getCooperationWithdrawLogId() {
		return cooperationWithdrawLogId;
	}

	public void setCooperationWithdrawLogId(Integer cooperationWithdrawLogId) {
		this.cooperationWithdrawLogId = cooperationWithdrawLogId;
	}

	public Integer getIsUploadAttachment() {
		return isUploadAttachment;
	}

	public void setIsUploadAttachment(Integer isUploadAttachment) {
		this.isUploadAttachment = isUploadAttachment;
	}

	public String getUploadAttachmentPath() {
		return uploadAttachmentPath;
	}

	public void setUploadAttachmentPath(String uploadAttachmentPath) {
		this.uploadAttachmentPath = uploadAttachmentPath;
	}

	public Date getUploadAttachmentDate() {
		return uploadAttachmentDate;
	}

	public void setUploadAttachmentDate(Date uploadAttachmentDate) {
		this.uploadAttachmentDate = uploadAttachmentDate;
	}

	public String getUploadAttachmentUser() {
		return uploadAttachmentUser;
	}

	public void setUploadAttachmentUser(String uploadAttachmentUser) {
		this.uploadAttachmentUser = uploadAttachmentUser;
	}

	public Integer getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Integer isSettle) {
		this.isSettle = isSettle;
	}

	public String getOtherText() {
		return otherText;
	}

	public void setOtherText(String otherText) {
		this.otherText = otherText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}

	public BigDecimal getOverdueMonthRatio() {
		return overdueMonthRatio;
	}

	public void setOverdueMonthRatio(BigDecimal overdueMonthRatio) {
		this.overdueMonthRatio = overdueMonthRatio;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "BusinessExtension{" +
			", businessId=" + businessId +
			", invoiceType=" + invoiceType +
			", invoiceCategory=" + invoiceCategory +
			", invoiceUser=" + invoiceUser +
			", invoiceDate=" + invoiceDate +
			", invoiceRemark=" + invoiceRemark +
			", invoiceUserId=" + invoiceUserId +
			", overdueRatio=" + overdueRatio +
			", onlineOverdueRatio=" + onlineOverdueRatio +
			", offlineOverdueRatio=" + offlineOverdueRatio +
			", financeWithdrawType=" + financeWithdrawType +
			", financeWithdrawLogId=" + financeWithdrawLogId +
			", cooperationWithdrawType=" + cooperationWithdrawType +
			", cooperationWithdrawLogId=" + cooperationWithdrawLogId +
			", isUploadAttachment=" + isUploadAttachment +
			", uploadAttachmentPath=" + uploadAttachmentPath +
			", uploadAttachmentDate=" + uploadAttachmentDate +
			", uploadAttachmentUser=" + uploadAttachmentUser +
			", isSettle=" + isSettle +
			", otherText=" + otherText +
			", remark=" + remark +
			", settleStatus=" + settleStatus +
			", overdueMonthRatio=" + overdueMonthRatio +
			"}";
	}
}
