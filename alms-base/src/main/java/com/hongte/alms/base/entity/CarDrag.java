package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 拖车表
 * </p>
 *
 * @author cj
 * @since 2018-02-28
 */
@ApiModel
@TableName("tb_car_drag")
public class CarDrag extends Model<CarDrag> {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(required= true,value = "")
	private String id;
    /**
     * [业务单号]
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "[业务单号]")
	private String businessId;
    /**
     * [拖车日期]
     */
	@TableField("drag_date")
	@ApiModelProperty(required= true,value = "[拖车日期]")
	private Date dragDate;
    /**
     * [拖车经办人]
     */
	@TableField("drag_handler")
	@ApiModelProperty(required= true,value = "[拖车经办人]")
	private String dragHandler;
    /**
     * [拖车费]
     */
	@ApiModelProperty(required= true,value = "[拖车费]")
	private BigDecimal fee;
    /**
     * 其他相关费用
     */
	@TableField("other_fee")
	@ApiModelProperty(required= true,value = "其他相关费用")
	private BigDecimal otherFee;
    /**
     * [备注]
     */
	@ApiModelProperty(required= true,value = "[备注]")
	private String remark;
    /**
     * [拖车省份]
     */
	@ApiModelProperty(required= true,value = "[拖车省份]")
	private String province;
    /**
     * [拖车市]
     */
	@ApiModelProperty(required= true,value = "[拖车市]")
	private String city;
    /**
     * [拖车县]
     */
	@ApiModelProperty(required= true,value = "[拖车县]")
	private String county;
    /**
     * [拖车地址]
     */
	@TableField("detail_address")
	@ApiModelProperty(required= true,value = "[拖车地址]")
	private String detailAddress;
    /**
     * 拖车的地区id
     */
	@TableField("region_id")
	@ApiModelProperty(required= true,value = "拖车的地区id")
	private Integer regionId;
    /**
     * [创建者]
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "[创建者]")
	private String createUser;
    /**
     * [创建时间]
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "[创建时间]")
	private Date createTime;
    /**
     * [修改者]
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "[修改者]")
	private String updateUser;
    /**
     * [修改时间]
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "[修改时间]")
	private Date updateTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Date getDragDate() {
		return dragDate;
	}

	public void setDragDate(Date dragDate) {
		this.dragDate = dragDate;
	}

	public String getDragHandler() {
		return dragHandler;
	}

	public void setDragHandler(String dragHandler) {
		this.dragHandler = dragHandler;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CarDrag{" +
			", id=" + id +
			", businessId=" + businessId +
			", dragDate=" + dragDate +
			", dragHandler=" + dragHandler +
			", fee=" + fee +
			", otherFee=" + otherFee +
			", remark=" + remark +
			", province=" + province +
			", city=" + city +
			", county=" + county +
			", detailAddress=" + detailAddress +
			", regionId=" + regionId +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
