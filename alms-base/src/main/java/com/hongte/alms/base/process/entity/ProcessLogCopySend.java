package com.hongte.alms.base.process.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-07
 */
@ApiModel
@TableName("tb_process_log_copy_send")
public class ProcessLogCopySend extends Model<ProcessLogCopySend> {

	private static final long serialVersionUID = 1L;

	/**
	 * 抄送ID
	 */
	@TableId("process_send_id")
	@ApiModelProperty(required= true,value = "抄送ID")
	private String processSendId;
	/**
	 * 审批记录ID
	 */
	@TableField("process_log_id")
	@ApiModelProperty(required= true,value = "审批记录ID")
	private String processLogId;
	/**
	 * 抄送的用户ID
	 */
	@TableField("receive_user_id")
	@ApiModelProperty(required= true,value = "抄送的用户ID")
	private String receiveUserId;
	/**
	 * 抄送的用户名称
	 */
	@TableField("receive_user_name")
	@ApiModelProperty(required= true,value = "抄送的用户名称")
	private String receiveUserName;


	public String getProcessSendId() {
		return processSendId;
	}

	public void setProcessSendId(String processSendId) {
		this.processSendId = processSendId;
	}

	public String getProcessLogId() {
		return processLogId;
	}

	public void setProcessLogId(String processLogId) {
		this.processLogId = processLogId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	@Override
	protected Serializable pkVal() {
		return this.processSendId;
	}

	@Override
	public String toString() {
		return "ProcessLogCopySend{" +
				", processSendId=" + processSendId +
				", processLogId=" + processLogId +
				", receiveUserId=" + receiveUserId +
				", receiveUserName=" + receiveUserName +
				"}";
	}
}
