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
 * 车辆检测
 * </p>
 *
 * @author 王继光
 * @since 2018-03-25
 */
@ApiModel
@TableName("tb_car_detection")
public class CarDetection extends Model<CarDetection> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@ApiModelProperty(required= true,value = "id")
	private String id;
    /**
     * 资产端业务编号 
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 评估金额
     */
	@TableField("evaluation_amount")
	@ApiModelProperty(required= true,value = "评估金额")
	private BigDecimal evaluationAmount;
    /**
     * 使用权转移价,转让费
     */
	@TableField("transfer_fee")
	@ApiModelProperty(required= true,value = "使用权转移价,转让费")
	private BigDecimal transferFee;
    /**
     * 中控台是否正常 
     */
	@TableField("is_center_panel_normal")
	@ApiModelProperty(required= true,value = "中控台是否正常 ")
	private Boolean isCenterPanelNormal;
    /**
     * 中控台异常说明 
     */
	@TableField("center_panel_abnormal_description")
	@ApiModelProperty(required= true,value = "中控台异常说明 ")
	private String centerPanelAbnormalDescription;
    /**
     * 空调是否正常 
     */
	@TableField("is_ventilator_normal")
	@ApiModelProperty(required= true,value = "空调是否正常 ")
	private Boolean isVentilatorNormal;
    /**
     * 空调异常说明 
     */
	@TableField("ventilator_abnormal_description")
	@ApiModelProperty(required= true,value = "空调异常说明 ")
	private String ventilatorAbnormalDescription;
    /**
     * 车厢内饰是否正常 
     */
	@TableField("is_interior_normal")
	@ApiModelProperty(required= true,value = "车厢内饰是否正常 ")
	private Boolean isInteriorNormal;
    /**
     * 车厢内饰异常说明 
     */
	@TableField("interior_abnormal_description")
	@ApiModelProperty(required= true,value = "车厢内饰异常说明 ")
	private String interiorAbnormalDescription;
    /**
     * 玻璃是否正常 
     */
	@TableField("is_window_glass_normal")
	@ApiModelProperty(required= true,value = "玻璃是否正常 ")
	private Boolean isWindowGlassNormal;
    /**
     * 玻璃异常说明 
     */
	@TableField("window_glass_abnormal_description")
	@ApiModelProperty(required= true,value = "玻璃异常说明 ")
	private String windowGlassAbnormalDescription;
    /**
     * 水箱是否正常 
     */
	@TableField("is_radiator_normal")
	@ApiModelProperty(required= true,value = "水箱是否正常 ")
	private Boolean isRadiatorNormal;
    /**
     * 水箱异常说明 
     */
	@TableField("radiator_abnormal_description")
	@ApiModelProperty(required= true,value = "水箱异常说明 ")
	private String radiatorAbnormalDescription;
    /**
     * 发动机是否正常 
     */
	@TableField("is_engine_normal")
	@ApiModelProperty(required= true,value = "发动机是否正常 ")
	private Boolean isEngineNormal;
    /**
     * 发动机异常说明 
     */
	@TableField("engine_abnormal_description")
	@ApiModelProperty(required= true,value = "发动机异常说明 ")
	private String engineAbnormalDescription;
    /**
     * 车大梁是否正常 
     */
	@TableField("is_frame_normal")
	@ApiModelProperty(required= true,value = "车大梁是否正常 ")
	private Boolean isFrameNormal;
    /**
     * 车大梁异常说明 
     */
	@TableField("frame_abnormal_description")
	@ApiModelProperty(required= true,value = "车大梁异常说明 ")
	private String frameAbnormalDescription;
    /**
     * 轮胎是否正常 
     */
	@TableField("is_tire_normal")
	@ApiModelProperty(required= true,value = "轮胎是否正常 ")
	private Boolean isTireNormal;
    /**
     * 轮胎异常说明 
     */
	@TableField("tire_abnormal_description")
	@ApiModelProperty(required= true,value = "轮胎异常说明 ")
	private String tireAbnormalDescription;
    /**
     * 备用胎是否正常 
     */
	@TableField("is_spare_tire_normal")
	@ApiModelProperty(required= true,value = "备用胎是否正常 ")
	private Boolean isSpareTireNormal;
    /**
     * 备用胎异常说明 
     */
	@TableField("spare_tire_abnormal_descrioption")
	@ApiModelProperty(required= true,value = "备用胎异常说明 ")
	private String spareTireAbnormalDescrioption;
    /**
     * 车门是否正常 
     */
	@TableField("is_door_normal")
	@ApiModelProperty(required= true,value = "车门是否正常 ")
	private Boolean isDoorNormal;
    /**
     * 车门异常说明 
     */
	@TableField("door_abnormal_description")
	@ApiModelProperty(required= true,value = "车门异常说明 ")
	private String doorAbnormalDescription;
    /**
     * 车架号是否一致 
     */
	@TableField("is_vin_consistent")
	@ApiModelProperty(required= true,value = "车架号是否一致 ")
	private Boolean isVinConsistent;
    /**
     * 发动机号是否一致 
     */
	@TableField("is_engine_model_consistent")
	@ApiModelProperty(required= true,value = "发动机号是否一致 ")
	private Boolean isEngineModelConsistent;
    /**
     * 是否有事故痕迹 
     */
	@TableField("is_accident")
	@ApiModelProperty(required= true,value = "是否有事故痕迹 ")
	private Boolean isAccident;
    /**
     * 事故损伤位置及状况 
     */
	@TableField("accident_description")
	@ApiModelProperty(required= true,value = "事故损伤位置及状况 ")
	private String accidentDescription;
    /**
     * 有无大修 
     */
	@TableField("is_overhaul")
	@ApiModelProperty(required= true,value = "有无大修 ")
	private Boolean isOverhaul;
    /**
     * 是否有其他问题 
     */
	@TableField("is_other_trouble")
	@ApiModelProperty(required= true,value = "是否有其他问题 ")
	private Boolean isOtherTrouble;
    /**
     * 其他问题，车况说明 
     */
	@TableField("other_trouble_description")
	@ApiModelProperty(required= true,value = "其他问题，车况说明 ")
	private String otherTroubleDescription;
    /**
     * 备用钥匙是否确认可用（0表示没备用钥匙，1表示可用，2表示不可用） 
     */
	@TableField("spare_key_status")
	@ApiModelProperty(required= true,value = "备用钥匙是否确认可用（0表示没备用钥匙，1表示可用，2表示不可用） ")
	private Integer spareKeyStatus;
    /**
     * 动力性能是否正常 
     */
	@TableField("is_acceleration_performance_normal")
	@ApiModelProperty(required= true,value = "动力性能是否正常 ")
	private Boolean isAccelerationPerformanceNormal;
    /**
     * 动力性能异常说明 
     */
	@TableField("acceleration_performance_abnormal_description")
	@ApiModelProperty(required= true,value = "动力性能异常说明 ")
	private String accelerationPerformanceAbnormalDescription;
    /**
     * 刹车性能是否正常 
     */
	@TableField("is_braking_performance_normal")
	@ApiModelProperty(required= true,value = "刹车性能是否正常 ")
	private Boolean isBrakingPerformanceNormal;
    /**
     * 刹车性能异常说明 
     */
	@TableField("braking_performance_abnormal_description")
	@ApiModelProperty(required= true,value = "刹车性能异常说明 ")
	private String brakingPerformanceAbnormalDescription;
    /**
     * 刹车平衡性能是否正常 
     */
	@TableField("is_braking_balance_performance_normal")
	@ApiModelProperty(required= true,value = "刹车平衡性能是否正常 ")
	private Boolean isBrakingBalancePerformanceNormal;
    /**
     * 刹车平衡性能异常说明 
     */
	@TableField("braking_balance_performance_abnormal_description")
	@ApiModelProperty(required= true,value = "刹车平衡性能异常说明 ")
	private String brakingBalancePerformanceAbnormalDescription;
    /**
     * 方向性能是否正常 
     */
	@TableField("is_steer_performance_normal")
	@ApiModelProperty(required= true,value = "方向性能是否正常 ")
	private Boolean isSteerPerformanceNormal;
    /**
     * 方向性能异常说明 
     */
	@TableField("steer_performance_abnormal_description")
	@ApiModelProperty(required= true,value = "方向性能异常说明 ")
	private String steerPerformanceAbnormalDescription;
    /**
     * 挂挡性能是否正常 
     */
	@TableField("is_gear_performance_normal")
	@ApiModelProperty(required= true,value = "挂挡性能是否正常 ")
	private Boolean isGearPerformanceNormal;
    /**
     * 挂挡性能异常说明 
     */
	@TableField("gear_performance_abnormal_description")
	@ApiModelProperty(required= true,value = "挂挡性能异常说明 ")
	private String gearPerformanceAbnormalDescription;
    /**
     * 其他驾驶情况说明 
     */
	@TableField("other_drive_description")
	@ApiModelProperty(required= true,value = "其他驾驶情况说明 ")
	private String otherDriveDescription;
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
     * 是否是原始值，如果是则为true，否则为false
     */
	@TableField("is_origin")
	@ApiModelProperty(required= true,value = "是否是原始值，如果是则为true，否则为false")
	private Boolean isOrigin;
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
	@TableField("user_nature")
	@ApiModelProperty(required= true,value = "")
	private String userNature;
	@TableField("tool_with_car")
	@ApiModelProperty(required= true,value = "")
	private String toolWithCar;
	@TableField("related_docs")
	@ApiModelProperty(required= true,value = "")
	private String relatedDocs;
	@TableField("transaction_mode")
	@ApiModelProperty(required= true,value = "")
	private String transactionMode;
	@TableField("car_position")
	@ApiModelProperty(required= true,value = "")
	private String carPosition;
	@ApiModelProperty(required= true,value = "")
	private String remark;


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

	public BigDecimal getEvaluationAmount() {
		return evaluationAmount;
	}

	public void setEvaluationAmount(BigDecimal evaluationAmount) {
		this.evaluationAmount = evaluationAmount;
	}

	public BigDecimal getTransferFee() {
		return transferFee;
	}

	public void setTransferFee(BigDecimal transferFee) {
		this.transferFee = transferFee;
	}

	public Boolean getCenterPanelNormal() {
		return isCenterPanelNormal;
	}

	public void setCenterPanelNormal(Boolean isCenterPanelNormal) {
		this.isCenterPanelNormal = isCenterPanelNormal;
	}

	public String getCenterPanelAbnormalDescription() {
		return centerPanelAbnormalDescription;
	}

	public void setCenterPanelAbnormalDescription(String centerPanelAbnormalDescription) {
		this.centerPanelAbnormalDescription = centerPanelAbnormalDescription;
	}

	public Boolean getVentilatorNormal() {
		return isVentilatorNormal;
	}

	public void setVentilatorNormal(Boolean isVentilatorNormal) {
		this.isVentilatorNormal = isVentilatorNormal;
	}

	public String getVentilatorAbnormalDescription() {
		return ventilatorAbnormalDescription;
	}

	public void setVentilatorAbnormalDescription(String ventilatorAbnormalDescription) {
		this.ventilatorAbnormalDescription = ventilatorAbnormalDescription;
	}

	public Boolean getInteriorNormal() {
		return isInteriorNormal;
	}

	public void setInteriorNormal(Boolean isInteriorNormal) {
		this.isInteriorNormal = isInteriorNormal;
	}

	public String getInteriorAbnormalDescription() {
		return interiorAbnormalDescription;
	}

	public void setInteriorAbnormalDescription(String interiorAbnormalDescription) {
		this.interiorAbnormalDescription = interiorAbnormalDescription;
	}

	public Boolean getWindowGlassNormal() {
		return isWindowGlassNormal;
	}

	public void setWindowGlassNormal(Boolean isWindowGlassNormal) {
		this.isWindowGlassNormal = isWindowGlassNormal;
	}

	public String getWindowGlassAbnormalDescription() {
		return windowGlassAbnormalDescription;
	}

	public void setWindowGlassAbnormalDescription(String windowGlassAbnormalDescription) {
		this.windowGlassAbnormalDescription = windowGlassAbnormalDescription;
	}

	public Boolean getRadiatorNormal() {
		return isRadiatorNormal;
	}

	public void setRadiatorNormal(Boolean isRadiatorNormal) {
		this.isRadiatorNormal = isRadiatorNormal;
	}

	public String getRadiatorAbnormalDescription() {
		return radiatorAbnormalDescription;
	}

	public void setRadiatorAbnormalDescription(String radiatorAbnormalDescription) {
		this.radiatorAbnormalDescription = radiatorAbnormalDescription;
	}

	public Boolean getEngineNormal() {
		return isEngineNormal;
	}

	public void setEngineNormal(Boolean isEngineNormal) {
		this.isEngineNormal = isEngineNormal;
	}

	public String getEngineAbnormalDescription() {
		return engineAbnormalDescription;
	}

	public void setEngineAbnormalDescription(String engineAbnormalDescription) {
		this.engineAbnormalDescription = engineAbnormalDescription;
	}

	public Boolean getFrameNormal() {
		return isFrameNormal;
	}

	public void setFrameNormal(Boolean isFrameNormal) {
		this.isFrameNormal = isFrameNormal;
	}

	public String getFrameAbnormalDescription() {
		return frameAbnormalDescription;
	}

	public void setFrameAbnormalDescription(String frameAbnormalDescription) {
		this.frameAbnormalDescription = frameAbnormalDescription;
	}

	public Boolean getTireNormal() {
		return isTireNormal;
	}

	public void setTireNormal(Boolean isTireNormal) {
		this.isTireNormal = isTireNormal;
	}

	public String getTireAbnormalDescription() {
		return tireAbnormalDescription;
	}

	public void setTireAbnormalDescription(String tireAbnormalDescription) {
		this.tireAbnormalDescription = tireAbnormalDescription;
	}

	public Boolean getSpareTireNormal() {
		return isSpareTireNormal;
	}

	public void setSpareTireNormal(Boolean isSpareTireNormal) {
		this.isSpareTireNormal = isSpareTireNormal;
	}

	public String getSpareTireAbnormalDescrioption() {
		return spareTireAbnormalDescrioption;
	}

	public void setSpareTireAbnormalDescrioption(String spareTireAbnormalDescrioption) {
		this.spareTireAbnormalDescrioption = spareTireAbnormalDescrioption;
	}

	public Boolean getDoorNormal() {
		return isDoorNormal;
	}

	public void setDoorNormal(Boolean isDoorNormal) {
		this.isDoorNormal = isDoorNormal;
	}

	public String getDoorAbnormalDescription() {
		return doorAbnormalDescription;
	}

	public void setDoorAbnormalDescription(String doorAbnormalDescription) {
		this.doorAbnormalDescription = doorAbnormalDescription;
	}

	public Boolean getVinConsistent() {
		return isVinConsistent;
	}

	public void setVinConsistent(Boolean isVinConsistent) {
		this.isVinConsistent = isVinConsistent;
	}

	public Boolean getEngineModelConsistent() {
		return isEngineModelConsistent;
	}

	public void setEngineModelConsistent(Boolean isEngineModelConsistent) {
		this.isEngineModelConsistent = isEngineModelConsistent;
	}

	public Boolean getAccident() {
		return isAccident;
	}

	public void setAccident(Boolean isAccident) {
		this.isAccident = isAccident;
	}

	public String getAccidentDescription() {
		return accidentDescription;
	}

	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	public Boolean getOverhaul() {
		return isOverhaul;
	}

	public void setOverhaul(Boolean isOverhaul) {
		this.isOverhaul = isOverhaul;
	}

	public Boolean getOtherTrouble() {
		return isOtherTrouble;
	}

	public void setOtherTrouble(Boolean isOtherTrouble) {
		this.isOtherTrouble = isOtherTrouble;
	}

	public String getOtherTroubleDescription() {
		return otherTroubleDescription;
	}

	public void setOtherTroubleDescription(String otherTroubleDescription) {
		this.otherTroubleDescription = otherTroubleDescription;
	}

	public Integer getSpareKeyStatus() {
		return spareKeyStatus;
	}

	public void setSpareKeyStatus(Integer spareKeyStatus) {
		this.spareKeyStatus = spareKeyStatus;
	}

	public Boolean getAccelerationPerformanceNormal() {
		return isAccelerationPerformanceNormal;
	}

	public void setAccelerationPerformanceNormal(Boolean isAccelerationPerformanceNormal) {
		this.isAccelerationPerformanceNormal = isAccelerationPerformanceNormal;
	}

	public String getAccelerationPerformanceAbnormalDescription() {
		return accelerationPerformanceAbnormalDescription;
	}

	public void setAccelerationPerformanceAbnormalDescription(String accelerationPerformanceAbnormalDescription) {
		this.accelerationPerformanceAbnormalDescription = accelerationPerformanceAbnormalDescription;
	}

	public Boolean getBrakingPerformanceNormal() {
		return isBrakingPerformanceNormal;
	}

	public void setBrakingPerformanceNormal(Boolean isBrakingPerformanceNormal) {
		this.isBrakingPerformanceNormal = isBrakingPerformanceNormal;
	}

	public String getBrakingPerformanceAbnormalDescription() {
		return brakingPerformanceAbnormalDescription;
	}

	public void setBrakingPerformanceAbnormalDescription(String brakingPerformanceAbnormalDescription) {
		this.brakingPerformanceAbnormalDescription = brakingPerformanceAbnormalDescription;
	}

	public Boolean getBrakingBalancePerformanceNormal() {
		return isBrakingBalancePerformanceNormal;
	}

	public void setBrakingBalancePerformanceNormal(Boolean isBrakingBalancePerformanceNormal) {
		this.isBrakingBalancePerformanceNormal = isBrakingBalancePerformanceNormal;
	}

	public String getBrakingBalancePerformanceAbnormalDescription() {
		return brakingBalancePerformanceAbnormalDescription;
	}

	public void setBrakingBalancePerformanceAbnormalDescription(String brakingBalancePerformanceAbnormalDescription) {
		this.brakingBalancePerformanceAbnormalDescription = brakingBalancePerformanceAbnormalDescription;
	}

	public Boolean getSteerPerformanceNormal() {
		return isSteerPerformanceNormal;
	}

	public void setSteerPerformanceNormal(Boolean isSteerPerformanceNormal) {
		this.isSteerPerformanceNormal = isSteerPerformanceNormal;
	}

	public String getSteerPerformanceAbnormalDescription() {
		return steerPerformanceAbnormalDescription;
	}

	public void setSteerPerformanceAbnormalDescription(String steerPerformanceAbnormalDescription) {
		this.steerPerformanceAbnormalDescription = steerPerformanceAbnormalDescription;
	}

	public Boolean getGearPerformanceNormal() {
		return isGearPerformanceNormal;
	}

	public void setGearPerformanceNormal(Boolean isGearPerformanceNormal) {
		this.isGearPerformanceNormal = isGearPerformanceNormal;
	}

	public String getGearPerformanceAbnormalDescription() {
		return gearPerformanceAbnormalDescription;
	}

	public void setGearPerformanceAbnormalDescription(String gearPerformanceAbnormalDescription) {
		this.gearPerformanceAbnormalDescription = gearPerformanceAbnormalDescription;
	}

	public String getOtherDriveDescription() {
		return otherDriveDescription;
	}

	public void setOtherDriveDescription(String otherDriveDescription) {
		this.otherDriveDescription = otherDriveDescription;
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

	public Boolean getOrigin() {
		return isOrigin;
	}

	public void setOrigin(Boolean isOrigin) {
		this.isOrigin = isOrigin;
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

	public String getRelatedDocs() {
		return relatedDocs;
	}

	public void setRelatedDocs(String relatedDocs) {
		this.relatedDocs = relatedDocs;
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
		return this.id;
	}

	@Override
	public String toString() {
		return "CarDetection{" +
			", id=" + id +
			", businessId=" + businessId +
			", evaluationAmount=" + evaluationAmount +
			", transferFee=" + transferFee +
			", isCenterPanelNormal=" + isCenterPanelNormal +
			", centerPanelAbnormalDescription=" + centerPanelAbnormalDescription +
			", isVentilatorNormal=" + isVentilatorNormal +
			", ventilatorAbnormalDescription=" + ventilatorAbnormalDescription +
			", isInteriorNormal=" + isInteriorNormal +
			", interiorAbnormalDescription=" + interiorAbnormalDescription +
			", isWindowGlassNormal=" + isWindowGlassNormal +
			", windowGlassAbnormalDescription=" + windowGlassAbnormalDescription +
			", isRadiatorNormal=" + isRadiatorNormal +
			", radiatorAbnormalDescription=" + radiatorAbnormalDescription +
			", isEngineNormal=" + isEngineNormal +
			", engineAbnormalDescription=" + engineAbnormalDescription +
			", isFrameNormal=" + isFrameNormal +
			", frameAbnormalDescription=" + frameAbnormalDescription +
			", isTireNormal=" + isTireNormal +
			", tireAbnormalDescription=" + tireAbnormalDescription +
			", isSpareTireNormal=" + isSpareTireNormal +
			", spareTireAbnormalDescrioption=" + spareTireAbnormalDescrioption +
			", isDoorNormal=" + isDoorNormal +
			", doorAbnormalDescription=" + doorAbnormalDescription +
			", isVinConsistent=" + isVinConsistent +
			", isEngineModelConsistent=" + isEngineModelConsistent +
			", isAccident=" + isAccident +
			", accidentDescription=" + accidentDescription +
			", isOverhaul=" + isOverhaul +
			", isOtherTrouble=" + isOtherTrouble +
			", otherTroubleDescription=" + otherTroubleDescription +
			", spareKeyStatus=" + spareKeyStatus +
			", isAccelerationPerformanceNormal=" + isAccelerationPerformanceNormal +
			", accelerationPerformanceAbnormalDescription=" + accelerationPerformanceAbnormalDescription +
			", isBrakingPerformanceNormal=" + isBrakingPerformanceNormal +
			", brakingPerformanceAbnormalDescription=" + brakingPerformanceAbnormalDescription +
			", isBrakingBalancePerformanceNormal=" + isBrakingBalancePerformanceNormal +
			", brakingBalancePerformanceAbnormalDescription=" + brakingBalancePerformanceAbnormalDescription +
			", isSteerPerformanceNormal=" + isSteerPerformanceNormal +
			", steerPerformanceAbnormalDescription=" + steerPerformanceAbnormalDescription +
			", isGearPerformanceNormal=" + isGearPerformanceNormal +
			", gearPerformanceAbnormalDescription=" + gearPerformanceAbnormalDescription +
			", otherDriveDescription=" + otherDriveDescription +
			", gearBox=" + gearBox +
			", odometer=" + odometer +
			", isDrivingLicenseConsistent=" + isDrivingLicenseConsistent +
			", drivingLicenseInconsistentDescription=" + drivingLicenseInconsistentDescription +
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
			", edition=" + edition +
			", isMortgage=" + isMortgage +
			", isOrigin=" + isOrigin +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			", userNature=" + userNature +
			", toolWithCar=" + toolWithCar +
			", relatedDocs=" + relatedDocs +
			", transactionMode=" + transactionMode +
			", carPosition=" + carPosition +
			", remark=" + remark +
			"}";
	}
}
