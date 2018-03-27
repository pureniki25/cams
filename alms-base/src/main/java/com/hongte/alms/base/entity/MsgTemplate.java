package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 消息模板
 * </p>
 *
 * @author 王继光
 * @since 2018-03-27
 */
@ApiModel
@TableName("tb_msg_template")
public class MsgTemplate extends Model<MsgTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * 消息模板id
     */
    @TableId("template_id")
	@ApiModelProperty(required= true,value = "消息模板id")
	private String templateId;
    /**
     * 模板类型 01:短信,02:极光 
     */
	@TableField("template_type")
	@ApiModelProperty(required= true,value = "模板类型 01:短信,02:极光 ")
	private String templateType;
    /**
     * 启用标识
     */
	@TableField("enable_flag")
	@ApiModelProperty(required= true,value = "启用标识")
	private String enableFlag;
    /**
     * 消息内容,格式 xxx{1}xxxxx{2}xxxxx。大括号代表变量
     */
	@ApiModelProperty(required= true,value = "消息内容,格式 xxx{1}xxxxx{2}xxxxx。大括号代表变量")
	private String msg;
    /**
     * 创建时间 
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间 ")
	private Date createTime;
    /**
     * 创建人 
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人 ")
	private String createUser;
    /**
     * 创建时间 
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "创建时间 ")
	private Date updateTime;
    /**
     * 创建人 
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "创建人 ")
	private String updateUser;


	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
		return this.templateId;
	}

	@Override
	public String toString() {
		return "MsgTemplate{" +
			", templateId=" + templateId +
			", templateType=" + templateType +
			", enableFlag=" + enableFlag +
			", msg=" + msg +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
