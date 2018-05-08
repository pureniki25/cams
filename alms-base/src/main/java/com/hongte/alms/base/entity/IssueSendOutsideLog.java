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
 * @author 陈泽圣
 * @since 2018-04-27
 */
@ApiModel
@TableName("tb_issue_send_outside_log")
public class IssueSendOutsideLog extends Model<IssueSendOutsideLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "流水号")
	private Integer id;
    /**
     * 系统标志
     */
	@ApiModelProperty(required= true,value = "系统标志")
	private String system;
    /**
     * 发送地址
     */
	@TableField("send_url")
	@ApiModelProperty(required= true,value = "发送地址")
	private String sendUrl;
    /**
     * 发送信息key值
     */
	@TableField("send_key")
	@ApiModelProperty(required= true,value = "发送信息key值")
	private String sendKey;
    /**
     * 发送未加密Json
     */
	@TableField("send_json")
	@ApiModelProperty(required= true,value = "发送未加密Json")
	private String sendJson;
    /**
     * 发送加密的json值
     */
	@TableField("send_json_encrypt")
	@ApiModelProperty(required= true,value = "发送加密的json值")
	private String sendJsonEncrypt;
    /**
     * 返回的json值
     */
	@TableField("return_json")
	@ApiModelProperty(required= true,value = "返回的json值")
	private String returnJson;
    /**
     * 接口编码
     */
	@ApiModelProperty(required= true,value = "接口编码")
	private String Interfacecode;
    /**
     * 接口名称
     */
	@ApiModelProperty(required= true,value = "接口名称")
	private String Interfacename;
    /**
     * [返回json解密后文本]
     */
	@TableField("return_json_decrypt")
	@ApiModelProperty(required= true,value = "[返回json解密后文本]")
	private String returnJsonDecrypt;
    /**
     * 创建人用户编号
     */
	@TableField("create_user_id")
	@ApiModelProperty(required= true,value = "创建人用户编号")
	private String createUserId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public String getSendKey() {
		return sendKey;
	}

	public void setSendKey(String sendKey) {
		this.sendKey = sendKey;
	}

	public String getSendJson() {
		return sendJson;
	}

	public void setSendJson(String sendJson) {
		this.sendJson = sendJson;
	}

	public String getSendJsonEncrypt() {
		return sendJsonEncrypt;
	}

	public void setSendJsonEncrypt(String sendJsonEncrypt) {
		this.sendJsonEncrypt = sendJsonEncrypt;
	}

	public String getReturnJson() {
		return returnJson;
	}

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}

	public String getInterfacecode() {
		return Interfacecode;
	}

	public void setInterfacecode(String Interfacecode) {
		this.Interfacecode = Interfacecode;
	}

	public String getInterfacename() {
		return Interfacename;
	}

	public void setInterfacename(String Interfacename) {
		this.Interfacename = Interfacename;
	}

	public String getReturnJsonDecrypt() {
		return returnJsonDecrypt;
	}

	public void setReturnJsonDecrypt(String returnJsonDecrypt) {
		this.returnJsonDecrypt = returnJsonDecrypt;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
		return "IssueSendOutsideLog{" +
			", id=" + id +
			", system=" + system +
			", sendUrl=" + sendUrl +
			", sendKey=" + sendKey +
			", sendJson=" + sendJson +
			", sendJsonEncrypt=" + sendJsonEncrypt +
			", returnJson=" + returnJson +
			", Interfacecode=" + Interfacecode +
			", Interfacename=" + Interfacename +
			", returnJsonDecrypt=" + returnJsonDecrypt +
			", createUserId=" + createUserId +
			", createTime=" + createTime +
			"}";
	}
}
