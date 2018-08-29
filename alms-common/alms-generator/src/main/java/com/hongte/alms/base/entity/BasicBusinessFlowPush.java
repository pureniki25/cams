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
 * 业务流水推送记录表
 * </p>
 *
 * @author 刘正全
 * @since 2018-08-24
 */
@ApiModel
@TableName("tb_basic_business_flow_push")
public class BasicBusinessFlowPush extends Model<BasicBusinessFlowPush> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录流水id
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "记录流水id")
	private Long id;
    /**
     * 业务id
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务id")
	private String businessId;
    /**
     * 推送目标1cams核心账务
     */
	@TableField("push_to")
	@ApiModelProperty(required= true,value = "推送目标1cams核心账务")
	private Integer pushTo;
    /**
     * 推送开始时间
     */
	@TableField("push_starttime")
	@ApiModelProperty(required= true,value = "推送开始时间")
	private Date pushStarttime;
    /**
     * 推送结束时间
     */
	@TableField("push_endtime")
	@ApiModelProperty(required= true,value = "推送结束时间")
	private Date pushEndtime;
    /**
     * 推送状态0开始推送1推送成功2推送失败
     */
	@TableField("push_status")
	@ApiModelProperty(required= true,value = "推送状态0开始推送1推送成功2推送失败")
	private Integer pushStatus;
    /**
     * 推送备注
     */
	@TableField("push_remark")
	@ApiModelProperty(required= true,value = "推送备注")
	private String pushRemark;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getPushTo() {
		return pushTo;
	}

	public void setPushTo(Integer pushTo) {
		this.pushTo = pushTo;
	}

	public Date getPushStarttime() {
		return pushStarttime;
	}

	public void setPushStarttime(Date pushStarttime) {
		this.pushStarttime = pushStarttime;
	}

	public Date getPushEndtime() {
		return pushEndtime;
	}

	public void setPushEndtime(Date pushEndtime) {
		this.pushEndtime = pushEndtime;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public String getPushRemark() {
		return pushRemark;
	}

	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BasicBusinessFlowPush{" +
			", id=" + id +
			", businessId=" + businessId +
			", pushTo=" + pushTo +
			", pushStarttime=" + pushStarttime +
			", pushEndtime=" + pushEndtime +
			", pushStatus=" + pushStatus +
			", pushRemark=" + pushRemark +
			"}";
	}
}
