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
 * 转公车申请信息
 * </p>
 *
 * @author 王继光
 * @since 2018-03-08
 */
@ApiModel
@TableName("tb_car_conv_bus_aply")
public class CarConvBusAply extends Model<CarConvBusAply> {

    private static final long serialVersionUID = 1L;

    /**
     * 资产端业务编号 
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 申请人 
     */
	@TableField("aply_user")
	@ApiModelProperty(required= true,value = "申请人 ")
	private String aplyUser;
    /**
     * 申请时间
     */
	@TableField("aply_date")
	@ApiModelProperty(required= true,value = "申请时间")
	private Date aplyDate;
    /**
     * 申请说明 
     */
	@TableField("aply_explain")
	@ApiModelProperty(required= true,value = "申请说明 ")
	private String aplyExplain;
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
     * 申请状态：01 保存草稿，02 提交审核,03 撤销
     */
	@ApiModelProperty(required= true,value = "申请状态：01 保存草稿，02 提交审核,03 撤销")
	private String status;
    @TableId("conv_bus_id")
	@ApiModelProperty(required= true,value = "")
	private String convBusId;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAplyUser() {
		return aplyUser;
	}

	public void setAplyUser(String aplyUser) {
		this.aplyUser = aplyUser;
	}

	public Date getAplyDate() {
		return aplyDate;
	}

	public void setAplyDate(Date aplyDate) {
		this.aplyDate = aplyDate;
	}

	public String getAplyExplain() {
		return aplyExplain;
	}

	public void setAplyExplain(String aplyExplain) {
		this.aplyExplain = aplyExplain;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConvBusId() {
		return convBusId;
	}

	public void setConvBusId(String convBusId) {
		this.convBusId = convBusId;
	}

	@Override
	protected Serializable pkVal() {
		return this.convBusId;
	}

	@Override
	public String toString() {
		return "CarConvBusAply{" +
			", businessId=" + businessId +
			", aplyUser=" + aplyUser +
			", aplyDate=" + aplyDate +
			", aplyExplain=" + aplyExplain +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", status=" + status +
			", convBusId=" + convBusId +
			"}";
	}
}
