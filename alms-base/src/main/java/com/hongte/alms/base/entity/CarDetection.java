package com.hongte.alms.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 车辆检测
 * </p>
 *
 * @author cj
 * @since 2018-02-28
 */
@ApiModel
@TableName("tb_car_detection")
public class CarDetection extends Model<CarDetection> {

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
		return this.businessId;
	}

	@Override
	public String toString() {
		return "CarDetection{" +
			", businessId=" + businessId +
			", evaluationAmount=" + evaluationAmount +
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
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
