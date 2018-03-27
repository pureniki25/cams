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
 * 车辆基本信息状态变更记录表
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-26
 */
@ApiModel
@TableName("tb_car_basic_status_log")
public class CarBasicStatusLog extends Model<CarBasicStatusLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键记录ID
     */
    @TableId("car_basic_log_id")
	@ApiModelProperty(required= true,value = "主键记录ID")
	private String carBasicLogId;
    /**
     * 资产端业务编号 
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 车辆改变前的状态 0默认,01待处置,02拍卖中,03已拍卖,04,已结清,05已转公车,06已移交法务,07已撤销,08转公车申请中,09拍卖审核中,10转公车审核中
     */
	@TableField("old_status")
	@ApiModelProperty(required= true,value = "车辆改变前的状态 0默认,01待处置,02拍卖中,03已拍卖,04,已结清,05已转公车,06已移交法务,07已撤销,08转公车申请中,09拍卖审核中,10转公车审核中")
	private String oldStatus;
    /**
     * 车辆改变后的状态  0默认,01待处置,02拍卖中,03已拍卖,04,已结清,05已转公车,06已移交法务,07已撤销,08转公车申请中,09拍卖审核中,10转公车审核中
     */
	@TableField("new_status")
	@ApiModelProperty(required= true,value = "车辆改变后的状态  0默认,01待处置,02拍卖中,03已拍卖,04,已结清,05已转公车,06已移交法务,07已撤销,08转公车申请中,09拍卖审核中,10转公车审核中")
	private String newStatus;
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


	public String getCarBasicLogId() {
		return carBasicLogId;
	}

	public void setCarBasicLogId(String carBasicLogId) {
		this.carBasicLogId = carBasicLogId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
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

	@Override
	protected Serializable pkVal() {
		return this.carBasicLogId;
	}

	@Override
	public String toString() {
		return "CarBasicStatusLog{" +
			", carBasicLogId=" + carBasicLogId +
			", businessId=" + businessId +
			", oldStatus=" + oldStatus +
			", newStatus=" + newStatus +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
