package com.hongte.alms.open.req.repayPlan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("业务上标房贷信息表")
public class ProjectHouseInfoReq {


//    /**
//     * 项目编号
//     */
//    @ApiModelProperty(value = "项目编号")
//    private String projectId;
    /**
     * 购买日期
     */
    @ApiModelProperty(value = "购买日期")
    private Date houseBuyDate;
    /**
     * 购买面积
     */

    @ApiModelProperty(value = "购买面积")
    private BigDecimal houseArea;
    /**
     * 购买金额
     */

    @ApiModelProperty(value = "购买金额")
    private BigDecimal housePrice;
    /**
     * 房屋年限
     */

    @ApiModelProperty(value = "房屋年限")
    private Integer houseYears;
    /**
     * 是否装修
     */

    @ApiModelProperty(value = "是否装修")
    private Integer isHouseRenovation;
    /**
     * 是否有房贷
     */

    @ApiModelProperty(value = "是否有房贷")
    private Integer isHouseLoan;
    /**
     * 房屋贷款金额
     */

    @ApiModelProperty(value = "房屋贷款金额")
    private BigDecimal houseLoanAmount;
    /**
     * 贷款期限
     */

    @ApiModelProperty(value = "贷款期限")
    private Integer houseLoanYears;
//    /**
//     * 创建日期
//     */
//
//    @ApiModelProperty(value = "创建日期")
//    private Date createTime;
//    /**
//     * 创建人
//     */
//
//    @ApiModelProperty(value = "创建人")
//    private String createUser;
//    /**
//     * 更新日期
//     */
//
//    @ApiModelProperty(value = "更新日期")
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

    public Date getHouseBuyDate() {
        return houseBuyDate;
    }

    public void setHouseBuyDate(Date houseBuyDate) {
        this.houseBuyDate = houseBuyDate;
    }

    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public BigDecimal getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(BigDecimal housePrice) {
        this.housePrice = housePrice;
    }

    public Integer getHouseYears() {
        return houseYears;
    }

    public void setHouseYears(Integer houseYears) {
        this.houseYears = houseYears;
    }

    public Integer getIsHouseRenovation() {
        return isHouseRenovation;
    }

    public void setIsHouseRenovation(Integer isHouseRenovation) {
        this.isHouseRenovation = isHouseRenovation;
    }

    public Integer getIsHouseLoan() {
        return isHouseLoan;
    }

    public void setIsHouseLoan(Integer isHouseLoan) {
        this.isHouseLoan = isHouseLoan;
    }

    public BigDecimal getHouseLoanAmount() {
        return houseLoanAmount;
    }

    public void setHouseLoanAmount(BigDecimal houseLoanAmount) {
        this.houseLoanAmount = houseLoanAmount;
    }

    public Integer getHouseLoanYears() {
        return houseLoanYears;
    }

    public void setHouseLoanYears(Integer houseLoanYears) {
        this.houseLoanYears = houseLoanYears;
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
