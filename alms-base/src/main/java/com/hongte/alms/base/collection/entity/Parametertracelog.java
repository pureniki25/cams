package com.hongte.alms.base.collection.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
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
	private String CarBusinessId;
	@TableField("Car_Business_After_Id")
	@ApiModelProperty(required= true,value = "")
	private String CarBusinessAfterId;
    /**
     * [跟踪记录内容]
     */
	@TableField("Trance_Content")
	@ApiModelProperty(required= true,value = "[跟踪记录内容]")
	private String TranceContent;
	@TableField("Trance_Name")
	@ApiModelProperty(required= true,value = "")
	private String TranceName;
	@TableField("Trance_Date")
	@ApiModelProperty(required= true,value = "")
	private Date TranceDate;
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
		return CarBusinessId;
	}

	public void setCarBusinessId(String CarBusinessId) {
		this.CarBusinessId = CarBusinessId;
	}

	public String getCarBusinessAfterId() {
		return CarBusinessAfterId;
	}

	public void setCarBusinessAfterId(String CarBusinessAfterId) {
		this.CarBusinessAfterId = CarBusinessAfterId;
	}

	public String getTranceContent() {
		return TranceContent;
	}

	public void setTranceContent(String TranceContent) {
		this.TranceContent = TranceContent;
	}

	public String getTranceName() {
		return TranceName;
	}

	public void setTranceName(String TranceName) {
		this.TranceName = TranceName;
	}

	public Date getTranceDate() {
		return TranceDate;
	}

	public void setTranceDate(Date TranceDate) {
		this.TranceDate = TranceDate;
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
			", CarBusinessId=" + CarBusinessId +
			", CarBusinessAfterId=" + CarBusinessAfterId +
			", TranceContent=" + TranceContent +
			", TranceName=" + TranceName +
			", TranceDate=" + TranceDate +
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
