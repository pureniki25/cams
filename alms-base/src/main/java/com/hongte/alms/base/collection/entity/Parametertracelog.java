package com.hongte.alms.base.collection.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.enums.IdType;
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
 * 
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-15
 */
@ApiModel
@TableName("tb_parametertracelog")
public class Parametertracelog extends Model<Parametertracelog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="ID", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
	@TableField("Car_Business_Id")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessId;
	@TableField("Car_Business_After_Id")
	@ApiModelProperty(required= true,value = "")
	private String carBusinessAfterId;
    /**
     * [跟踪记录内容]
     */
	@TableField("Trance_Content")
	@ApiModelProperty(required= true,value = "[跟踪记录内容]")
	private String tranceContent;
	@TableField("Trance_Name")
	@ApiModelProperty(required= true,value = "")
	private String tranceName;
	@TableField("Trance_Date")
	@ApiModelProperty(required= true,value = "")
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date tranceDate;
    /**
     * 状态
     */
	@ApiModelProperty(required= true,value = "状态")
	private Integer status;
    /**
     * 贷后跟踪状态
     */
	@TableField("litigation_status")
	@ApiModelProperty(required= true,value = "贷后跟踪状态")
	private String litigationStatus;
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
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
    /**
     * 1是贷后跟踪记录，2是催收跟踪记录
     */
	@TableField("log_type")
	@ApiModelProperty(required= true,value = "1是贷后跟踪记录，2是催收跟踪记录")
	private Integer logType;
    /**
     * 是否删除（null或0:否，1:是）
     */
	@TableField("is_delete")
	@ApiModelProperty(required= true,value = "是否删除（null或0:否，1:是）")
	private Integer isDelete;
    /**
     * 是否传输平台(null或者0:否,1:是)
     */
	@TableField("is_transmission_platform")
	@ApiModelProperty(required= true,value = "是否传输平台(null或者0:否,1:是)")
	private Integer isTransmissionPlatform;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarBusinessId() {
		return carBusinessId;
	}

	public void setCarBusinessId(String CarBusinessId) {
		this.carBusinessId = CarBusinessId;
	}

	public String getCarBusinessAfterId() {
		return carBusinessAfterId;
	}

	public void setCarBusinessAfterId(String CarBusinessAfterId) {
		this.carBusinessAfterId = CarBusinessAfterId;
	}

	public String getTranceContent() {
		return tranceContent;
	}

	public void setTranceContent(String TranceContent) {
		this.tranceContent = TranceContent;
	}

	public String getTranceName() {
		return tranceName;
	}

	public void setTranceName(String TranceName) {
		this.tranceName = TranceName;
	}

	public Date getTranceDate() {
		return tranceDate;
	}

	public void setTranceDate(Date TranceDate) {
		this.tranceDate = TranceDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLitigationStatus() {
		return litigationStatus;
	}

	public void setLitigationStatus(String litigationStatus) {
		this.litigationStatus = litigationStatus;
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

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsTransmissionPlatform() {
		return isTransmissionPlatform;
	}

	public void setIsTransmissionPlatform(Integer isTransmissionPlatform) {
		this.isTransmissionPlatform = isTransmissionPlatform;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Parametertracelog{" +
			", id=" + id +
			", carBusinessId=" + carBusinessId +
			", carBusinessAfterId=" + carBusinessAfterId +
			", tranceContent=" + tranceContent +
			", tranceName=" + tranceName +
			", tranceDate=" + tranceDate +
			", status=" + status +
			", litigationStatus=" + litigationStatus +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", logType=" + logType +
			", isDelete=" + isDelete +
			", isTransmissionPlatform=" + isTransmissionPlatform +
			"}";
	}
}
