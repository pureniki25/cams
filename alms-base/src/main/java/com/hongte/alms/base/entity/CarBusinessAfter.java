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

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-10
 */
@ApiModel
@TableName("tb_car_business_after")
public class CarBusinessAfter extends Model<CarBusinessAfter> {

    private static final long serialVersionUID = 1L;


	@TableId("car_business_id")
	@ApiModelProperty(required= true,value = "业务ID")
	@NotNull(message="业务ID(carBusinessId)不能为空")
	private String carBusinessId;

	@TableField("car_business_after_id")
	@ApiModelProperty(required= true,value = "还款计划afterId")
	@NotNull(message="还款计划afterId(carBusinessAfterId)不能为空")
	private String carBusinessAfterId;


	/**
	 * [催收人]
	 */
	@TableField("collection_user")
	@ApiModelProperty(required= true,value = "[催收人]")
	@NotNull(message="催收人(collectionUser)不能为空")
	private String collectionUser;


	/**
	 * [电催分配备注]
	 */
	@TableField("collection_remark")
	@ApiModelProperty(required= true,value = "[电催分配备注]")
	@NotNull(message="电催分配备注(collectionRemark)不能为空")
	private String collectionRemark;



	public String getCarBusinessId() {
		return carBusinessId;
	}

	public void setCarBusinessId(String carBusinessId) {
		this.carBusinessId = carBusinessId;
	}

	public String getCarBusinessAfterId() {
		return carBusinessAfterId;
	}

	public void setCarBusinessAfterId(String carBusinessAfterId) {
		this.carBusinessAfterId = carBusinessAfterId;
	}

	public String getCollectionUser() {
		return collectionUser;
	}

	public void setCollectionUser(String collectionUser) {
		this.collectionUser = collectionUser;
	}

	public String getCollectionRemark() {
		return collectionRemark;
	}

	public void setCollectionRemark(String collectionRemark) {
		this.collectionRemark = collectionRemark;
	}


	@Override
	protected Serializable pkVal() {
		return this.carBusinessId;
	}

	@Override
	public String toString() {
		return "CarBusinessAfter{" +
			", carBusinessId=" + carBusinessId +
			", carBusinessAfterId=" + carBusinessAfterId +
			", collectionUser=" + collectionUser +
			", collectionRemark=" + collectionRemark +
			"}";
	}
}
