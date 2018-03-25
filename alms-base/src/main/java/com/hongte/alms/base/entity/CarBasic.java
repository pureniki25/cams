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
 * 车辆基本信息
 * </p>
 *
 * @author 王继光
 * @since 2018-03-25
 */
@ApiModel
@TableName("tb_car_basic")
public class CarBasic extends Model<CarBasic> {

    private static final long serialVersionUID = 1L;

    /**
     * 资产端业务编号 
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 所属人 
     */
	@TableField("owner_name")
	@ApiModelProperty(required= true,value = "所属人 ")
	private String ownerName;
    /**
     * 车牌号 
     */
	@TableField("license_plate_number")
	@ApiModelProperty(required= true,value = "车牌号 ")
	private String licensePlateNumber;
    /**
     * 车身颜色 
     */
	@ApiModelProperty(required= true,value = "车身颜色 ")
	private String color;
    /**
     * 汽车品牌 
     */
	@TableField("brand_name")
	@ApiModelProperty(required= true,value = "汽车品牌 ")
	private String brandName;
    /**
     * 发票价/新车指导价 
     */
	@TableField("invoice_cost")
	@ApiModelProperty(required= true,value = "发票价/新车指导价 ")
	private BigDecimal invoiceCost;
    /**
     * 品牌型号 
     */
	@ApiModelProperty(required= true,value = "品牌型号 ")
	private String model;
    /**
     * 排量 
     */
	@ApiModelProperty(required= true,value = "排量 ")
	private BigDecimal displacement;
    /**
     * 排量单位（L和T） 
     */
	@TableField("displacement_unit")
	@ApiModelProperty(required= true,value = "排量单位（L和T） ")
	private String displacementUnit;
    /**
     * 车架号 
     */
	@ApiModelProperty(required= true,value = "车架号 ")
	private String vin;
    /**
     * 排放标准，0或者空：表示没有填排放标准,1:国Ⅳ,2:国Ⅴ 
     */
	@TableField("emission_standard")
	@ApiModelProperty(required= true,value = "排放标准，0或者空：表示没有填排放标准,1:国Ⅳ,2:国Ⅴ ")
	private Integer emissionStandard;
    /**
     * 汽车产地0:国产1:进口 
     */
	@TableField("origin_place")
	@ApiModelProperty(required= true,value = "汽车产地0:国产1:进口 ")
	private Integer originPlace;
    /**
     * 发动机号（发动机型号） 
     */
	@TableField("engine_model")
	@ApiModelProperty(required= true,value = "发动机号（发动机型号） ")
	private String engineModel;
    /**
     * 车辆属地(上牌地) 
     */
	@TableField("license_location")
	@ApiModelProperty(required= true,value = "车辆属地(上牌地) ")
	private String licenseLocation;
    /**
     * 过户次数 
     */
	@TableField("transfer_times")
	@ApiModelProperty(required= true,value = "过户次数 ")
	private Integer transferTimes;
    /**
     * 最后一次过户时间
     */
	@TableField("last_transfer_date")
	@ApiModelProperty(required= true,value = "最后一次过户时间")
	private Date lastTransferDate;
    /**
     * 车辆状态0默认,1待处置,2拍卖中,3已拍卖,4已转公车,5转公车申请中,6已移交法务,7已撤销
     */
	@ApiModelProperty(required= true,value = "车辆状态0默认,1待处置,2拍卖中,3已拍卖,4已转公车,5转公车申请中,6已移交法务,7已撤销")
	private String status;
    /**
     * 最新评估的id，与tb_car_detection的id关联
     */
	@TableField("last_detection_id")
	@ApiModelProperty(required= true,value = "最新评估的id，与tb_car_detection的id关联")
	private String lastDetectionId;
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


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public BigDecimal getInvoiceCost() {
		return invoiceCost;
	}

	public void setInvoiceCost(BigDecimal invoiceCost) {
		this.invoiceCost = invoiceCost;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public BigDecimal getDisplacement() {
		return displacement;
	}

	public void setDisplacement(BigDecimal displacement) {
		this.displacement = displacement;
	}

	public String getDisplacementUnit() {
		return displacementUnit;
	}

	public void setDisplacementUnit(String displacementUnit) {
		this.displacementUnit = displacementUnit;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Integer getEmissionStandard() {
		return emissionStandard;
	}

	public void setEmissionStandard(Integer emissionStandard) {
		this.emissionStandard = emissionStandard;
	}

	public Integer getOriginPlace() {
		return originPlace;
	}

	public void setOriginPlace(Integer originPlace) {
		this.originPlace = originPlace;
	}

	public String getEngineModel() {
		return engineModel;
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}

	public String getLicenseLocation() {
		return licenseLocation;
	}

	public void setLicenseLocation(String licenseLocation) {
		this.licenseLocation = licenseLocation;
	}

	public Integer getTransferTimes() {
		return transferTimes;
	}

	public void setTransferTimes(Integer transferTimes) {
		this.transferTimes = transferTimes;
	}

	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	public void setLastTransferDate(Date lastTransferDate) {
		this.lastTransferDate = lastTransferDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastDetectionId() {
		return lastDetectionId;
	}

	public void setLastDetectionId(String lastDetectionId) {
		this.lastDetectionId = lastDetectionId;
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
		return this.businessId;
	}

	@Override
	public String toString() {
		return "CarBasic{" +
			", businessId=" + businessId +
			", ownerName=" + ownerName +
			", licensePlateNumber=" + licensePlateNumber +
			", color=" + color +
			", brandName=" + brandName +
			", invoiceCost=" + invoiceCost +
			", model=" + model +
			", displacement=" + displacement +
			", displacementUnit=" + displacementUnit +
			", vin=" + vin +
			", emissionStandard=" + emissionStandard +
			", originPlace=" + originPlace +
			", engineModel=" + engineModel +
			", licenseLocation=" + licenseLocation +
			", transferTimes=" + transferTimes +
			", lastTransferDate=" + lastTransferDate +
			", status=" + status +
			", lastDetectionId=" + lastDetectionId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
