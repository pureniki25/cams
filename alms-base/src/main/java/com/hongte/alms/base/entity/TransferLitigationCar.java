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
 * 车贷移交法务信息表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-09
 */
@ApiModel
@TableName("tb_transfer_litigation_car")
public class TransferLitigationCar extends Model<TransferLitigationCar> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务编号
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 流程id
     */
	@TableField("process_id")
	@ApiModelProperty(required= true,value = "流程id")
	private String processId;
    /**
     * 用户还款计划ID
     */
	@TableField("crp_id")
	@ApiModelProperty(required= true,value = "用户还款计划ID")
	private String crpId;
    /**
     * 是否有房产
     */
	@ApiModelProperty(required= true,value = "是否有房产")
	private String estates;
    /**
     * 房产地址
     */
	@TableField("house_address")
	@ApiModelProperty(required= true,value = "房产地址")
	private String houseAddress;
    /**
     * 房产抵押情况
     */
	@TableField("mortgage_situation")
	@ApiModelProperty(required= true,value = "房产抵押情况")
	private String mortgageSituation;
    /**
     * 附件地址
     */
	@TableField("attachment_url")
	@ApiModelProperty(required= true,value = "附件地址")
	private String attachmentUrl;
    /**
     * 客户车辆目前情况
     */
	@TableField("car_condition")
	@ApiModelProperty(required= true,value = "客户车辆目前情况")
	private String carCondition;
    /**
     * 贷后意见
     */
	@TableField("alms_opinion")
	@ApiModelProperty(required= true,value = "贷后意见")
	private String almsOpinion;
    /**
     * 是否推迟移交
     */
	@TableField("delay_handover")
	@ApiModelProperty(required= true,value = "是否推迟移交")
	private String delayHandover;
    /**
     * 是否推迟移交诉讼（最长延期7天及说明理由）
     */
	@TableField("delay_handover_desc")
	@ApiModelProperty(required= true,value = "是否推迟移交诉讼（最长延期7天及说明理由）")
	private String delayHandoverDesc;
    /**
     * 流程状态
     */
	@TableField("process_status")
	@ApiModelProperty(required= true,value = "流程状态")
	private String processStatus;
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

	public String getCrpId() {
		return crpId;
	}

	public void setCrpId(String crpId) {
		this.crpId = crpId;
	}

	public String getEstates() {
		return estates;
	}

	public void setEstates(String estates) {
		this.estates = estates;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getMortgageSituation() {
		return mortgageSituation;
	}

	public void setMortgageSituation(String mortgageSituation) {
		this.mortgageSituation = mortgageSituation;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}

	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}

	public String getCarCondition() {
		return carCondition;
	}

	public void setCarCondition(String carCondition) {
		this.carCondition = carCondition;
	}

	public String getAlmsOpinion() {
		return almsOpinion;
	}

	public void setAlmsOpinion(String almsOpinion) {
		this.almsOpinion = almsOpinion;
	}

	public String getDelayHandover() {
		return delayHandover;
	}

	public void setDelayHandover(String delayHandover) {
		this.delayHandover = delayHandover;
	}

	public String getDelayHandoverDesc() {
		return delayHandoverDesc;
	}

	public void setDelayHandoverDesc(String delayHandoverDesc) {
		this.delayHandoverDesc = delayHandoverDesc;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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
		return this.businessId;
	}

	@Override
	public String toString() {
		return "TransferLitigationCar{" +
			", businessId=" + businessId +
			", processId=" + processId +
			", crpId=" + crpId +
			", estates=" + estates +
			", houseAddress=" + houseAddress +
			", mortgageSituation=" + mortgageSituation +
			", attachmentUrl=" + attachmentUrl +
			", carCondition=" + carCondition +
			", almsOpinion=" + almsOpinion +
			", delayHandover=" + delayHandover +
			", delayHandoverDesc=" + delayHandoverDesc +
			", processStatus=" + processStatus +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", createUser=" + createUser +
			", createTime=" + createTime +
			"}";
	}
}
