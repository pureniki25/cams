package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
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
 * 业务上标车贷信息表
 * </p>
 *
 * @author 王继光
 * @since 2018-04-23
 */
@ApiModel
@TableName("tb_tuandai_project_car")
public class TuandaiProjectCar extends Model<TuandaiProjectCar> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号
     */
    @TableId("project_id")
	@ApiModelProperty(required= true,value = "项目编号")
	private String projectId;
    /**
     * 车贷贷款金额
     */
	@ApiModelProperty(required= true,value = "车贷贷款金额")
	private BigDecimal ForecastBorrowAmount;
    /**
     * 车辆品牌
     */
	@TableField("car_brand")
	@ApiModelProperty(required= true,value = "车辆品牌")
	private String carBrand;
    /**
     * 汽车型号
     */
	@TableField("car_type")
	@ApiModelProperty(required= true,value = "汽车型号")
	private String carType;
    /**
     * 车辆价格
     */
	@TableField("car_price")
	@ApiModelProperty(required= true,value = "车辆价格")
	private BigDecimal carPrice;
    /**
     * 汽车产地
     */
	@TableField("car_origin")
	@ApiModelProperty(required= true,value = "汽车产地")
	private String carOrigin;
    /**
     * 公里数
     */
	@TableField("car_KM")
	@ApiModelProperty(required= true,value = "公里数")
	private BigDecimal carKM;
    /**
     * 有无大修
     */
	@TableField("car_is_big_repair")
	@ApiModelProperty(required= true,value = "有无大修")
	private Integer carIsBigRepair;
    /**
     * 车辆属性
     */
	@TableField("car_place")
	@ApiModelProperty(required= true,value = "车辆属性")
	private String carPlace;
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


	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getForecastBorrowAmount() {
		return ForecastBorrowAmount;
	}

	public void setForecastBorrowAmount(BigDecimal ForecastBorrowAmount) {
		this.ForecastBorrowAmount = ForecastBorrowAmount;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public BigDecimal getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(BigDecimal carPrice) {
		this.carPrice = carPrice;
	}

	public String getCarOrigin() {
		return carOrigin;
	}

	public void setCarOrigin(String carOrigin) {
		this.carOrigin = carOrigin;
	}

	public BigDecimal getCarKM() {
		return carKM;
	}

	public void setCarKM(BigDecimal carKM) {
		this.carKM = carKM;
	}

	public Integer getCarIsBigRepair() {
		return carIsBigRepair;
	}

	public void setCarIsBigRepair(Integer carIsBigRepair) {
		this.carIsBigRepair = carIsBigRepair;
	}

	public String getCarPlace() {
		return carPlace;
	}

	public void setCarPlace(String carPlace) {
		this.carPlace = carPlace;
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
		return this.projectId;
	}

	@Override
	public String toString() {
		return "TuandaiProjectCar{" +
			", projectId=" + projectId +
			", ForecastBorrowAmount=" + ForecastBorrowAmount +
			", carBrand=" + carBrand +
			", carType=" + carType +
			", carPrice=" + carPrice +
			", carOrigin=" + carOrigin +
			", carKM=" + carKM +
			", carIsBigRepair=" + carIsBigRepair +
			", carPlace=" + carPlace +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
