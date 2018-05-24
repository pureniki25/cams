package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("业务上标车贷信息表")
public class ProjectCarInfoReq {

//    /**
//     * 项目编号
//     */
//    @ApiModelProperty(value = "项目编号")
//    private String projectId;
    /**
     * 车贷贷款金额
     */
    @ApiModelProperty(value = "车贷贷款金额")
    private BigDecimal ForecastBorrowAmount;
    /**
     * 车辆品牌
     */
    @ApiModelProperty(value = "车辆品牌")
    private String carBrand;
    /**
     * 汽车型号
     */
    @ApiModelProperty(value = "汽车型号")
    private String carType;
    /**
     * 车辆价格
     */
    @ApiModelProperty(value = "车辆价格")
    private BigDecimal carPrice;
    /**
     * 汽车产地
     */
    @ApiModelProperty(value = "汽车产地")
    private String carOrigin;
    /**
     * 公里数
     */
    @ApiModelProperty(value = "公里数")
    private BigDecimal carKM;
    /**
     * 有无大修
     */
    @ApiModelProperty(value = "有无大修")
    private Integer carIsBigRepair;
    /**
     * 车辆属性
     */
    @ApiModelProperty(value = "车辆属性")
    private String carPlace;
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value = "创建时间")
//    private Date createTime;
//    /**
//     * 创建人
//     */
//    @ApiModelProperty(value = "创建人")
//    private String createUser;
//    /**
//     * 更新时间
//     */
//    @ApiModelProperty(value = "更新时间")
//    private Date updateTime;
//    /**
//     * 更新人
//     */
//    @ApiModelProperty(value = "更新人")
//    private String updateUser;


//    public String getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(String projectId) {
//        this.projectId = projectId;
//    }

    public BigDecimal getForecastBorrowAmount() {
        return ForecastBorrowAmount;
    }

    public void setForecastBorrowAmount(BigDecimal forecastBorrowAmount) {
        ForecastBorrowAmount = forecastBorrowAmount;
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

//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getCreateUser() {
//        return createUser;
//    }
//
//    public void setCreateUser(String createUser) {
//        this.createUser = createUser;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getUpdateUser() {
//        return updateUser;
//    }
//
//    public void setUpdateUser(String updateUser) {
//        this.updateUser = updateUser;
//    }
}
