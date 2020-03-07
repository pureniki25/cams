package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 产品中间表
 * </p>
 *
 * @author czs
 * @since 2019-03-24
 */
@ApiModel
@TableName("tb_temp_product_dat")
public class TempProductDat extends Model<TempProductDat> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
    /**
     * 商品编号
     */
	@TableField("product_code")
	@ApiModelProperty(required= true,value = "商品编号")
	private String productCode;
    /**
     * 公司名称
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;
    /**
     * 商品名称
     */
	@TableField("product_name")
	@ApiModelProperty(required= true,value = "商品名称")
	private String productName;
    /**
     * 规格型号
     */
	@TableField("product_type")
	@ApiModelProperty(required= true,value = "规格型号")
	private String productType;
    /**
     * 单位
     */
	@TableField("unit")
	@ApiModelProperty(required= true,value = "单位")
	private String unit;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "")
	private Date updateTime;

	/**
     * 商品名称
     */
	@TableField("temp_product_name")
	@ApiModelProperty(required= true,value = "商品中转名称")
	private String tempProductName;
	
	public String getTempProductName() {
		return tempProductName;
	}

	public void setTempProductName(String tempProductName) {
		this.tempProductName = tempProductName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TempProductDat{" +
			", id=" + id +
			", productCode=" + productCode +
			", companyName=" + companyName +
			", productName=" + productName +
			", productType=" + productType +
			", unit=" + unit +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			"}";
	}
}
