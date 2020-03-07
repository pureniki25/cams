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
 * 合规化还款结清状态变更记录表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-11-26
 */
@ApiModel
@TableName("tb_tdrepay_recharge_settle_change_log")
public class TdrepayRechargeSettleChangeLog extends Model<TdrepayRechargeSettleChangeLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键id")
	private Integer id;
    /**
     * tb_tdrepay_recharge_log表id
     */
	@TableField("log_id")
	@ApiModelProperty(required= true,value = "tb_tdrepay_recharge_log表id")
	private String logId;
    /**
     * 接收时的结清类型
     */
	@TableField("receive_settle_type")
	@ApiModelProperty(required= true,value = "接收时的结清类型")
	private Integer receiveSettleType;
    /**
     * 变更后的提前结清类型
     */
	@TableField("change_settle_type")
	@ApiModelProperty(required= true,value = "变更后的提前结清类型")
	private Integer changeSettleType;
    /**
     * 接收参数
     */
	@TableField("receive_param")
	@ApiModelProperty(required= true,value = "接收参数")
	private String receiveParam;
    /**
     * 调用提前结清接口发送的参数JSON
     */
	@TableField("send_json")
	@ApiModelProperty(required= true,value = "调用提前结清接口发送的参数JSON")
	private String sendJson;
    /**
     * 接口返回的结果JSON
     */
	@TableField("result_json")
	@ApiModelProperty(required= true,value = "接口返回的结果JSON")
	private String resultJson;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public Integer getReceiveSettleType() {
		return receiveSettleType;
	}

	public void setReceiveSettleType(Integer receiveSettleType) {
		this.receiveSettleType = receiveSettleType;
	}

	public Integer getChangeSettleType() {
		return changeSettleType;
	}

	public void setChangeSettleType(Integer changeSettleType) {
		this.changeSettleType = changeSettleType;
	}

	public String getReceiveParam() {
		return receiveParam;
	}

	public void setReceiveParam(String receiveParam) {
		this.receiveParam = receiveParam;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeSettleChangeLog{" +
			", id=" + id +
			", logId=" + logId +
			", receiveSettleType=" + receiveSettleType +
			", changeSettleType=" + changeSettleType +
			", receiveParam=" + receiveParam +
			", sendJson=" + sendJson +
			", resultJson=" + resultJson +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
