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
 * 车辆归还登记
 * </p>
 *
 * @author cj
 * @since 2018-03-02
 */
@ApiModel
@TableName("tb_car_return_reg")
public class CarReturnReg extends Model<CarReturnReg> {

    private static final long serialVersionUID = 1L;

    /**
     * 资产端业务编号 
     */
    @TableId("business_id")
	@ApiModelProperty(required= true,value = "资产端业务编号 ")
	private String businessId;
    /**
     * 归还日期
     */
	@TableField("return_date")
	@ApiModelProperty(required= true,value = "归还日期")
	private Date returnDate;
    /**
     * 归还经办人
     */
	@TableField("return_operator")
	@ApiModelProperty(required= true,value = "归还经办人")
	private String returnOperator;
    /**
     * 归还地点(省)
     */
	@TableField("return_proId")
	@ApiModelProperty(required= true,value = "归还地点(省)")
	private Integer returnProId;
    /**
     * 归还地点(市)
     */
	@TableField("return_cityId")
	@ApiModelProperty(required= true,value = "归还地点(市)")
	private Integer returnCityId;
    /**
     * 归还地点(县区)
     */
	@TableField("return_countyId")
	@ApiModelProperty(required= true,value = "归还地点(县区)")
	private Integer returnCountyId;
    /**
     * 归还地点(详细地址)
     */
	@TableField("return_addr")
	@ApiModelProperty(required= true,value = "归还地点(详细地址)")
	private String returnAddr;
    /**
     * 是否缴纳拖车费用
     */
	@TableField("is_pay_trailer_cost")
	@ApiModelProperty(required= true,value = "是否缴纳拖车费用")
	private Boolean isPayTrailerCost;
    /**
     * 已缴纳拖车费用
     */
	@TableField("pay_trailer_cost")
	@ApiModelProperty(required= true,value = "已缴纳拖车费用")
	private BigDecimal payTrailerCost;
    /**
     * 是否缴纳拖车其他费用
     */
	@TableField("pay_trailer_other_cost")
	@ApiModelProperty(required= true,value = "是否缴纳拖车其他费用")
	private BigDecimal payTrailerOtherCost;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String note;
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

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnOperator() {
		return returnOperator;
	}

	public void setReturnOperator(String returnOperator) {
		this.returnOperator = returnOperator;
	}

	public Integer getReturnProId() {
		return returnProId;
	}

	public void setReturnProId(Integer returnProId) {
		this.returnProId = returnProId;
	}

	public Integer getReturnCityId() {
		return returnCityId;
	}

	public void setReturnCityId(Integer returnCityId) {
		this.returnCityId = returnCityId;
	}

	public Integer getReturnCountyId() {
		return returnCountyId;
	}

	public void setReturnCountyId(Integer returnCountyId) {
		this.returnCountyId = returnCountyId;
	}

	public String getReturnAddr() {
		return returnAddr;
	}

	public void setReturnAddr(String returnAddr) {
		this.returnAddr = returnAddr;
	}

	public Boolean getIsPayTrailerCost() {
		return isPayTrailerCost;
	}

	public void setIsPayTrailerCost(Boolean isPayTrailerCost) {
		this.isPayTrailerCost = isPayTrailerCost;
	}

	public BigDecimal getPayTrailerCost() {
		return payTrailerCost;
	}

	public void setPayTrailerCost(BigDecimal payTrailerCost) {
		this.payTrailerCost = payTrailerCost;
	}

	public BigDecimal getPayTrailerOtherCost() {
		return payTrailerOtherCost;
	}

	public void setPayTrailerOtherCost(BigDecimal payTrailerOtherCost) {
		this.payTrailerOtherCost = payTrailerOtherCost;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
		return "CarReturnReg{" +
			", businessId=" + businessId +
			", returnDate=" + returnDate +
			", returnOperator=" + returnOperator +
			", returnProId=" + returnProId +
			", returnCityId=" + returnCityId +
			", returnCountyId=" + returnCountyId +
			", returnAddr=" + returnAddr +
			", isPayTrailerCost=" + isPayTrailerCost +
			", payTrailerCost=" + payTrailerCost +
			", payTrailerOtherCost=" + payTrailerOtherCost +
			", note=" + note +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
