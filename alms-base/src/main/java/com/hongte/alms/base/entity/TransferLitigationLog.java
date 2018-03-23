package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-23
 */
@ApiModel
@TableName("tb_transfer_litigation_log")
public class TransferLitigationLog extends Model<TransferLitigationLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "")
	private String businessId;
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "")
	private String processId;
	@TableField("send_json")
	@ApiModelProperty(required= true,value = "")
	private String sendJson;
	@TableField("result_json")
	@ApiModelProperty(required= true,value = "")
	private String resultJson;
	@TableField("result_code")
	@ApiModelProperty(required= true,value = "")
	private Integer resultCode;
	@TableField("result_msg")
	@ApiModelProperty(required= true,value = "")
	private String resultMsg;
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "")
	private String createUser;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getSendJson() {
		return sendJson;
	}

	public void setSendJson(String sendJson) {
		this.sendJson = sendJson;
	}

	public String getResultJson() {
		return resultJson;
	}

	public void setResultJson(String resultJson) {
		this.resultJson = resultJson;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TransferLitigationLog{" +
			", id=" + id +
			", businessId=" + businessId +
			", processId=" + processId +
			", sendJson=" + sendJson +
			", resultJson=" + resultJson +
			", resultCode=" + resultCode +
			", resultMsg=" + resultMsg +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
