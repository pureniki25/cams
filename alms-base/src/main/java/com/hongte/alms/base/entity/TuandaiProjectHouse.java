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
 * 团贷网业务上标房贷信息表
 * </p>
 *
 * @author 王继光
 * @since 2018-04-23
 */
@ApiModel
@TableName("tb_tuandai_project_house")
public class TuandaiProjectHouse extends Model<TuandaiProjectHouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号
     */
    @TableId("project_id")
	@ApiModelProperty(required= true,value = "项目编号")
	private String projectId;
    /**
     * 购买日期
     */
	@TableField("house_buy_date")
	@ApiModelProperty(required= true,value = "购买日期")
	private Date houseBuyDate;
    /**
     * 购买面积
     */
	@TableField("house_area")
	@ApiModelProperty(required= true,value = "购买面积")
	private BigDecimal houseArea;
    /**
     * 购买金额
     */
	@TableField("house_price")
	@ApiModelProperty(required= true,value = "购买金额")
	private BigDecimal housePrice;
    /**
     * 房屋年限
     */
	@TableField("house_years")
	@ApiModelProperty(required= true,value = "房屋年限")
	private Integer houseYears;
    /**
     * 是否装修
     */
	@TableField("is_house_renovation")
	@ApiModelProperty(required= true,value = "是否装修")
	private Integer isHouseRenovation;
    /**
     * 是否有房贷
     */
	@TableField("is_house_loan")
	@ApiModelProperty(required= true,value = "是否有房贷")
	private Integer isHouseLoan;
    /**
     * 房屋贷款金额
     */
	@TableField("house_loan_amount")
	@ApiModelProperty(required= true,value = "房屋贷款金额")
	private BigDecimal houseLoanAmount;
    /**
     * 贷款期限
     */
	@TableField("house_loan_years")
	@ApiModelProperty(required= true,value = "贷款期限")
	private Integer houseLoanYears;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
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
		return "TuandaiProjectHouse{" +
			", projectId=" + projectId +
			", houseBuyDate=" + houseBuyDate +
			", houseArea=" + houseArea +
			", housePrice=" + housePrice +
			", houseYears=" + houseYears +
			", isHouseRenovation=" + isHouseRenovation +
			", isHouseLoan=" + isHouseLoan +
			", houseLoanAmount=" + houseLoanAmount +
			", houseLoanYears=" + houseLoanYears +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
