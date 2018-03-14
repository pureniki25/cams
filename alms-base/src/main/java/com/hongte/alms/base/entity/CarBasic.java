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
 * @author 曾坤
 * @since 2018-03-12
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
     * 评估金额
     */
	@TableField("evaluation_amount")
	@ApiModelProperty(required= true,value = "评估金额")
	private BigDecimal evaluationAmount;
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
     * 品牌型号 
     */
	@ApiModelProperty(required= true,value = "品牌型号 ")
	private String model;
    /**
     * 车型及版本配置 
     */
	@ApiModelProperty(required= true,value = "车型及版本配置 ")
	private String edition;
    /**
     * 车辆抵押状态（false表示未抵押，true表示抵押) 
     */
	@TableField("is_mortgage")
	@ApiModelProperty(required= true,value = "车辆抵押状态（false表示未抵押，true表示抵押) ")
	private Boolean isMortgage;
    /**
     * 过户次数 
     */
	@TableField("transfer_times")
	@ApiModelProperty(required= true,value = "过户次数 ")
	private Integer transferTimes;
    /**
     * 汽车产地0:国产1:进口 
     */
	@TableField("origin_place")
	@ApiModelProperty(required= true,value = "汽车产地0:国产1:进口 ")
	private Integer originPlace;
    /**
     * 车辆属地(上牌地) 
     */
	@TableField("license_location")
	@ApiModelProperty(required= true,value = "车辆属地(上牌地) ")
	private String licenseLocation;
    /**
     * 排放标准，0或者空：表示没有填排放标准,1:国Ⅳ,2:国Ⅴ 
     */
	@TableField("emission_standard")
	@ApiModelProperty(required= true,value = "排放标准，0或者空：表示没有填排放标准,1:国Ⅳ,2:国Ⅴ ")
	private Integer emissionStandard;
    /**
     * 车架号 
     */
	@ApiModelProperty(required= true,value = "车架号 ")
	private String vin;
    /**
     * 发动机号（发动机型号） 
     */
	@TableField("engine_model")
	@ApiModelProperty(required= true,value = "发动机号（发动机型号） ")
	private String engineModel;
    /**
     * 变速箱(0表示手动,1表示自动，2表示无级变速箱) 
     */
	@TableField("gear_box")
	@ApiModelProperty(required= true,value = "变速箱(0表示手动,1表示自动，2表示无级变速箱) ")
	private Integer gearBox;
    /**
     * 里程表读数 
     */
	@ApiModelProperty(required= true,value = "里程表读数 ")
	private BigDecimal odometer;
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
     * 核对行驶证资料一致 
     */
	@TableField("is_driving_license_consistent")
	@ApiModelProperty(required= true,value = "核对行驶证资料一致 ")
	private Boolean isDrivingLicenseConsistent;
    /**
     * 行驶证资料不一致说明 
     */
	@TableField("driving_license_inconsistent_description")
	@ApiModelProperty(required= true,value = "行驶证资料不一致说明 ")
	private String drivingLicenseInconsistentDescription;
    /**
     * 发票价/新车指导价 
     */
	@TableField("invoice_cost")
	@ApiModelProperty(required= true,value = "发票价/新车指导价 ")
	private BigDecimal invoiceCost;
    /**
     * 燃油量0表示空，1表示1/4量，2表示1/2量，3表示3/4量，4表示满
     */
	@TableField("fuel_left")
	@ApiModelProperty(required= true,value = "燃油量0表示空，1表示1/4量，2表示1/2量，3表示3/4量，4表示满")
	private Integer fuelLeft;
    /**
     * 燃油形式，0表示汽油，1表示柴油
     */
	@TableField("fuel_type")
	@ApiModelProperty(required= true,value = "燃油形式，0表示汽油，1表示柴油")
	private Integer fuelType;
    /**
     * 汽车注册日期 
     */
	@TableField("vehicle_license_registration_date")
	@ApiModelProperty(required= true,value = "汽车注册日期 ")
	private Date vehicleLicenseRegistrationDate;
    /**
     * 违章情况 
     */
	@TableField("traffic_violation_situation")
	@ApiModelProperty(required= true,value = "违章情况 ")
	private String trafficViolationSituation;
    /**
     * 保险到期日 
     */
	@TableField("insurance_expiration_date")
	@ApiModelProperty(required= true,value = "保险到期日 ")
	private Date insuranceExpirationDate;
    /**
     * 保险受益人 
     */
	@TableField("insurance_beneficiary")
	@ApiModelProperty(required= true,value = "保险受益人 ")
	private String insuranceBeneficiary;
    /**
     * 车辆保险公司 
     */
	@TableField("insurance_company")
	@ApiModelProperty(required= true,value = "车辆保险公司 ")
	private String insuranceCompany;
    /**
     * 保险期是否续保 
     */
	@TableField("is_renewal")
	@ApiModelProperty(required= true,value = "保险期是否续保 ")
	private Boolean isRenewal;
    /**
     * 年审到期日 
     */
	@TableField("annual_verification_expiration_date")
	@ApiModelProperty(required= true,value = "年审到期日 ")
	private String annualVerificationExpirationDate;
    /**
     * 违章费用 
     */
	@TableField("traffic_violation_fee")
	@ApiModelProperty(required= true,value = "违章费用 ")
	private BigDecimal trafficViolationFee;
    /**
     * 车船税费用 
     */
	@TableField("vehicle_vessel_tax")
	@ApiModelProperty(required= true,value = "车船税费用 ")
	private BigDecimal vehicleVesselTax;
    /**
     * 统缴费用 
     */
	@TableField("annual_ticket_fee")
	@ApiModelProperty(required= true,value = "统缴费用 ")
	private BigDecimal annualTicketFee;
    /**
     * 总费用 
     */
	@TableField("total_fee")
	@ApiModelProperty(required= true,value = "总费用 ")
	private BigDecimal totalFee;
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
    /**
     * 状态:01  待处置，02拍卖中、03已拍卖、04已结清、05已转公车、06已移交法务、07已撤销、08转公车申请中
     */
	@ApiModelProperty(required= true,value = "状态:01  待处置，02拍卖中、03已拍卖、04已结清、05已转公车、06已移交法务、07已撤销、08转公车申请中")
	private String status;
    /**
     * 最新评估金额
     */
	@TableField("last_evaluation_amount")
	@ApiModelProperty(required= true,value = "最新评估金额")
	private BigDecimal lastEvaluationAmount;
    /**
     * 最新评估时间
     */
	@TableField("last_evaluation_time")
	@ApiModelProperty(required= true,value = "最新评估时间")
	private Date lastEvaluationTime;
    /**
     * 使用权转移价
     */
	@TableField("borrowing_money")
	@ApiModelProperty(required= true,value = "使用权转移价")
	private BigDecimal borrowingMoney;
    /**
     * 车辆使用性质
     */
	@TableField("user_nature")
	@ApiModelProperty(required= true,value = "车辆使用性质")
	private String userNature;
    /**
     * 随车工具
     */
	@TableField("tool_with_car")
	@ApiModelProperty(required= true,value = "随车工具")
	private String toolWithCar;
    /**
     * 最后一次过户时间
     */
	@TableField("last_transfer_date")
	@ApiModelProperty(required= true,value = "最后一次过户时间")
	private Date lastTransferDate;
    /**
     * 提供文件
     */
	@TableField("related_docs")
	@ApiModelProperty(required= true,value = "提供文件")
	private String relatedDocs;
    /**
     * 车型及版本配置
     */
	@TableField("car_version_config")
	@ApiModelProperty(required= true,value = "车型及版本配置")
	private String carVersionConfig;
    /**
     * 交易方式
     */
	@TableField("transaction_mode")
	@ApiModelProperty(required= true,value = "交易方式")
	private String transactionMode;
    /**
     * 车辆位置
     */
	@TableField("car_position")
	@ApiModelProperty(required= true,value = "车辆位置")
	private String carPosition;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;


	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public BigDecimal getEvaluationAmount() {
		return evaluationAmount;
	}

	public void setEvaluationAmount(BigDecimal evaluationAmount) {
		this.evaluationAmount = evaluationAmount;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public Boolean getMortgage() {
		return isMortgage;
	}

	public void setMortgage(Boolean isMortgage) {
		this.isMortgage = isMortgage;
	}

	public Integer getTransferTimes() {
		return transferTimes;
	}

	public void setTransferTimes(Integer transferTimes) {
		this.transferTimes = transferTimes;
	}

	public Integer getOriginPlace() {
		return originPlace;
	}

	public void setOriginPlace(Integer originPlace) {
		this.originPlace = originPlace;
	}

	public String getLicenseLocation() {
		return licenseLocation;
	}

	public void setLicenseLocation(String licenseLocation) {
		this.licenseLocation = licenseLocation;
	}

	public Integer getEmissionStandard() {
		return emissionStandard;
	}

	public void setEmissionStandard(Integer emissionStandard) {
		this.emissionStandard = emissionStandard;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getEngineModel() {
		return engineModel;
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}

	public Integer getGearBox() {
		return gearBox;
	}

	public void setGearBox(Integer gearBox) {
		this.gearBox = gearBox;
	}

	public BigDecimal getOdometer() {
		return odometer;
	}

	public void setOdometer(BigDecimal odometer) {
		this.odometer = odometer;
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

	public Boolean getDrivingLicenseConsistent() {
		return isDrivingLicenseConsistent;
	}

	public void setDrivingLicenseConsistent(Boolean isDrivingLicenseConsistent) {
		this.isDrivingLicenseConsistent = isDrivingLicenseConsistent;
	}

	public String getDrivingLicenseInconsistentDescription() {
		return drivingLicenseInconsistentDescription;
	}

	public void setDrivingLicenseInconsistentDescription(String drivingLicenseInconsistentDescription) {
		this.drivingLicenseInconsistentDescription = drivingLicenseInconsistentDescription;
	}

	public BigDecimal getInvoiceCost() {
		return invoiceCost;
	}

	public void setInvoiceCost(BigDecimal invoiceCost) {
		this.invoiceCost = invoiceCost;
	}

	public Integer getFuelLeft() {
		return fuelLeft;
	}

	public void setFuelLeft(Integer fuelLeft) {
		this.fuelLeft = fuelLeft;
	}

	public Integer getFuelType() {
		return fuelType;
	}

	public void setFuelType(Integer fuelType) {
		this.fuelType = fuelType;
	}

	public Date getVehicleLicenseRegistrationDate() {
		return vehicleLicenseRegistrationDate;
	}

	public void setVehicleLicenseRegistrationDate(Date vehicleLicenseRegistrationDate) {
		this.vehicleLicenseRegistrationDate = vehicleLicenseRegistrationDate;
	}

	public String getTrafficViolationSituation() {
		return trafficViolationSituation;
	}

	public void setTrafficViolationSituation(String trafficViolationSituation) {
		this.trafficViolationSituation = trafficViolationSituation;
	}

	public Date getInsuranceExpirationDate() {
		return insuranceExpirationDate;
	}

	public void setInsuranceExpirationDate(Date insuranceExpirationDate) {
		this.insuranceExpirationDate = insuranceExpirationDate;
	}

	public String getInsuranceBeneficiary() {
		return insuranceBeneficiary;
	}

	public void setInsuranceBeneficiary(String insuranceBeneficiary) {
		this.insuranceBeneficiary = insuranceBeneficiary;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public Boolean getRenewal() {
		return isRenewal;
	}

	public void setRenewal(Boolean isRenewal) {
		this.isRenewal = isRenewal;
	}

	public String getAnnualVerificationExpirationDate() {
		return annualVerificationExpirationDate;
	}

	public void setAnnualVerificationExpirationDate(String annualVerificationExpirationDate) {
		this.annualVerificationExpirationDate = annualVerificationExpirationDate;
	}

	public BigDecimal getTrafficViolationFee() {
		return trafficViolationFee;
	}

	public void setTrafficViolationFee(BigDecimal trafficViolationFee) {
		this.trafficViolationFee = trafficViolationFee;
	}

	public BigDecimal getVehicleVesselTax() {
		return vehicleVesselTax;
	}

	public void setVehicleVesselTax(BigDecimal vehicleVesselTax) {
		this.vehicleVesselTax = vehicleVesselTax;
	}

	public BigDecimal getAnnualTicketFee() {
		return annualTicketFee;
	}

	public void setAnnualTicketFee(BigDecimal annualTicketFee) {
		this.annualTicketFee = annualTicketFee;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLastEvaluationAmount() {
		return lastEvaluationAmount;
	}

	public void setLastEvaluationAmount(BigDecimal lastEvaluationAmount) {
		this.lastEvaluationAmount = lastEvaluationAmount;
	}

	public Date getLastEvaluationTime() {
		return lastEvaluationTime;
	}

	public void setLastEvaluationTime(Date lastEvaluationTime) {
		this.lastEvaluationTime = lastEvaluationTime;
	}

	public BigDecimal getBorrowingMoney() {
		return borrowingMoney;
	}

	public void setBorrowingMoney(BigDecimal borrowingMoney) {
		this.borrowingMoney = borrowingMoney;
	}

	public String getUserNature() {
		return userNature;
	}

	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}

	public String getToolWithCar() {
		return toolWithCar;
	}

	public void setToolWithCar(String toolWithCar) {
		this.toolWithCar = toolWithCar;
	}

	public Date getLastTransferDate() {
		return lastTransferDate;
	}

	public void setLastTransferDate(Date lastTransferDate) {
		this.lastTransferDate = lastTransferDate;
	}

	public String getRelatedDocs() {
		return relatedDocs;
	}

	public void setRelatedDocs(String relatedDocs) {
		this.relatedDocs = relatedDocs;
	}

	public String getCarVersionConfig() {
		return carVersionConfig;
	}

	public void setCarVersionConfig(String carVersionConfig) {
		this.carVersionConfig = carVersionConfig;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public String getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(String carPosition) {
		this.carPosition = carPosition;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	protected Serializable pkVal() {
		return this.businessId;
	}

	@Override
	public String toString() {
		return "CarBasic{" +
			", businessId=" + businessId +
			", evaluationAmount=" + evaluationAmount +
			", ownerName=" + ownerName +
			", licensePlateNumber=" + licensePlateNumber +
			", color=" + color +
			", brandName=" + brandName +
			", model=" + model +
			", edition=" + edition +
			", isMortgage=" + isMortgage +
			", transferTimes=" + transferTimes +
			", originPlace=" + originPlace +
			", licenseLocation=" + licenseLocation +
			", emissionStandard=" + emissionStandard +
			", vin=" + vin +
			", engineModel=" + engineModel +
			", gearBox=" + gearBox +
			", odometer=" + odometer +
			", displacement=" + displacement +
			", displacementUnit=" + displacementUnit +
			", isDrivingLicenseConsistent=" + isDrivingLicenseConsistent +
			", drivingLicenseInconsistentDescription=" + drivingLicenseInconsistentDescription +
			", invoiceCost=" + invoiceCost +
			", fuelLeft=" + fuelLeft +
			", fuelType=" + fuelType +
			", vehicleLicenseRegistrationDate=" + vehicleLicenseRegistrationDate +
			", trafficViolationSituation=" + trafficViolationSituation +
			", insuranceExpirationDate=" + insuranceExpirationDate +
			", insuranceBeneficiary=" + insuranceBeneficiary +
			", insuranceCompany=" + insuranceCompany +
			", isRenewal=" + isRenewal +
			", annualVerificationExpirationDate=" + annualVerificationExpirationDate +
			", trafficViolationFee=" + trafficViolationFee +
			", vehicleVesselTax=" + vehicleVesselTax +
			", annualTicketFee=" + annualTicketFee +
			", totalFee=" + totalFee +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", status=" + status +
			", lastEvaluationAmount=" + lastEvaluationAmount +
			", lastEvaluationTime=" + lastEvaluationTime +
			", borrowingMoney=" + borrowingMoney +
			", userNature=" + userNature +
			", toolWithCar=" + toolWithCar +
			", lastTransferDate=" + lastTransferDate +
			", relatedDocs=" + relatedDocs +
			", carVersionConfig=" + carVersionConfig +
			", transactionMode=" + transactionMode +
			", carPosition=" + carPosition +
			", remark=" + remark +
			"}";
	}
}
