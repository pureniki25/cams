package com.hongte.alms.base.collection.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 贷后跟踪记录表
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
@ApiModel
@TableName("tb_collection_track_log")
public class CollectionTrackLog extends Model<CollectionTrackLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 跟踪记录日志主键
	 */
	@TableId(value = "track_log_id", type = IdType.AUTO)
	@ApiModelProperty(required = true, value = "跟踪记录日志主键")
	private Integer trackLogId;
	/**
	 * 还款计划编号，对应tb_repayment_business_plan_list.plan_list_id
	 */
	@TableField("rbp_id")
	@ApiModelProperty(required = true, value = "还款计划编号，对应tb_repayment_business_plan_list.plan_list_id")
	private String rbpId;
	/**
	 * 记录者UserId
	 */
	@TableField("recorder_user")
	@ApiModelProperty(required = true, value = "记录者UserId")
	private String recorderUser;
	/**
	 * 记录日期
	 */
	@TableField("record_date")
	@ApiModelProperty(required = true, value = "记录日期")
	private Date recordDate;
	/**
	 * 状态ID，对应tb_sys_parameter的ID
	 */
	@TableField("track_status_id")
	@ApiModelProperty(required = true, value = "状态ID，对应tb_sys_parameter的ID")
	private String trackStatusId;
	/**
	 * 状态名称，缓存当时保存的状态
	 */
	@TableField("track_status_name")
	@ApiModelProperty(required = true, value = "状态名称，缓存当时保存的状态")
	private String trackStatusName;
	/**
	 * 是否传输平台，0：否，1：是
	 */
	@TableField("is_send")
	@ApiModelProperty(required = true, value = "是否传输平台，0：否，1：是")
	private Integer isSend;
	/**
	 * 传输平台状态，若非传输平台此字段为null，0：传送失败，1：传送成功
	 */
	@TableField("send_status")
	@ApiModelProperty(required = true, value = "传输平台状态，若非传输平台此字段为null，0：传送失败，1：传送成功")
	private Integer sendStatus;
	/**
	 * 记录内容
	 */
	@ApiModelProperty(required = true, value = "记录内容")
	private String content;
	/**
	 * 创建日期
	 */
	@TableField("create_time")
	@ApiModelProperty(required = true, value = "创建日期")
	private Date createTime;
	/**
	 * 创建人用户ID
	 */
	@TableField("create_user")
	@ApiModelProperty(required = true, value = "创建人用户ID")
	private String createUser;
	/**
	 * 更新日期
	 */
	@TableField("update_time")
	@ApiModelProperty(required = true, value = "更新日期")
	private Date updateTime;
	/**
	 * 更新人用户ID
	 */
	@TableField("update_user")
	@ApiModelProperty(required = true, value = "更新人用户ID")
	private String updateUser;
	/**
	 * 成功的标的id
	 */
	@TableField("success_project_id")
	@ApiModelProperty(required = true, value = "成功的标的id")
	private String successProjectId;
	/**
	 * 失败的标的id
	 */
	@TableField("fail_project_id")
	@ApiModelProperty(required = true, value = "失败的标的id")
	private String failProjectId;

	/**
	 * 是否重发
	 */
	@TableField("is_resend")
	@ApiModelProperty(required= true,value = "是否重发")
	private Integer isResend;

	/**
	 * 唯一标识ID
	 */
	@TableField("unique_id")
	@ApiModelProperty(required = true, value = "唯一标识ID")
	private String uniqueId;

	/**
	 * 借款人情况描述
	 */
	@TableField(exist = false)
	private List<String> borrowerConditionDescList;

	/**
	 * 抵押物情况描述
	 */
	@TableField(exist = false)
	private List<String> guaranteeConditionDescList;

	/**
	 * 五级分类名称
	 */
	@TableField(exist = false)
	private String className;

	/**
	 * 信贷的主键Id，只有从信贷同步过来的历史跟踪记录才
	 */
	@TableField("xd_index_id")
	@ApiModelProperty(required= true,value = "信贷的主键Id，只有从信贷同步过来的历史跟踪记录才")
	private Integer xdIndexId;

	@Override
	protected Serializable pkVal() {
		return this.trackLogId;
	}

	public Integer getTrackLogId() {
		return trackLogId;
	}

	public void setTrackLogId(Integer trackLogId) {
		this.trackLogId = trackLogId;
	}

	public String getRbpId() {
		return rbpId;
	}

	public void setRbpId(String rbpId) {
		this.rbpId = rbpId;
	}

	public String getRecorderUser() {
		return recorderUser;
	}

	public void setRecorderUser(String recorderUser) {
		this.recorderUser = recorderUser;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getTrackStatusId() {
		return trackStatusId;
	}

	public void setTrackStatusId(String trackStatusId) {
		this.trackStatusId = trackStatusId;
	}

	public String getTrackStatusName() {
		return trackStatusName;
	}

	public void setTrackStatusName(String trackStatusName) {
		this.trackStatusName = trackStatusName;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getSuccessProjectId() {
		return successProjectId;
	}

	public void setSuccessProjectId(String successProjectId) {
		this.successProjectId = successProjectId;
	}

	public String getFailProjectId() {
		return failProjectId;
	}

	public void setFailProjectId(String failProjectId) {
		this.failProjectId = failProjectId;
	}

	public Integer getIsResend() {
		return isResend;
	}

	public void setIsResend(Integer isResend) {
		this.isResend = isResend;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<String> getBorrowerConditionDescList() {
		return borrowerConditionDescList;
	}

	public void setBorrowerConditionDescList(List<String> borrowerConditionDescList) {
		this.borrowerConditionDescList = borrowerConditionDescList;
	}

	public List<String> getGuaranteeConditionDescList() {
		return guaranteeConditionDescList;
	}

	public void setGuaranteeConditionDescList(List<String> guaranteeConditionDescList) {
		this.guaranteeConditionDescList = guaranteeConditionDescList;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "CollectionTrackLog [trackLogId=" + trackLogId + ", rbpId=" + rbpId + ", recorderUser=" + recorderUser
				+ ", recordDate=" + recordDate + ", trackStatusId=" + trackStatusId + ", trackStatusName="
				+ trackStatusName + ", isSend=" + isSend + ", sendStatus=" + sendStatus + ", content=" + content
				+ ", createTime=" + createTime + ", createUser=" + createUser + ", updateTime=" + updateTime
				+ ", updateUser=" + updateUser + ", successProjectId=" + successProjectId + ", failProjectId="
				+ failProjectId + ", isResend=" + isResend + ", uniqueId=" + uniqueId + ", borrowerConditionDescList="
				+ borrowerConditionDescList + ", guaranteeConditionDescList=" + guaranteeConditionDescList
				+ ", className=" + className + ", xdIndexId=" + xdIndexId +"]";
	}

	public Integer getXdIndexId() {
		return xdIndexId;
	}

	public void setXdIndexId(Integer xdIndexId) {
		this.xdIndexId = xdIndexId;
	}
}
