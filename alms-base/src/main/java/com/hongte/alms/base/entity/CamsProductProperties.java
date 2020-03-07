package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 商品性质表
 * </p>
 *
 * @author czs
 * @since 2019-02-03
 */
@ApiModel
@TableName("tb_cams_product_properties")
public class CamsProductProperties extends Model<CamsProductProperties> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
    @TableId("product_properties_id")
	@ApiModelProperty(required= true)
	private String productPropertiesId;
    /**
     * 商品性质名称
     */
	@TableField("product_properties_name")
	@ApiModelProperty(required= true,value = "商品性质名称")
	private String productPropertiesName;
    /**
     * 税率
     */
	@TableField("tax_name")
	@ApiModelProperty(required= true,value = "税率")
	private String taxName;
    /**
     * 出入类别名称
     */
	@TableField("category_name")
	@ApiModelProperty(required= true,value = "出入类别名称")
	private String categoryName;
    /**
     * 收入科目
     */
	@TableField("in_subject")
	@ApiModelProperty(required= true,value = "收入科目")
	private String inSubject;
    /**
     * 成本科目
     */
	@TableField("cost_subject")
	@ApiModelProperty(required= true,value = "成本科目")
	private String costSubject;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


	public String getProductPropertiesId() {
		return productPropertiesId;
	}

	public void setProductPropertiesId(String productPropertiesId) {
		this.productPropertiesId = productPropertiesId;
	}

	public String getProductPropertiesName() {
		return productPropertiesName;
	}

	public void setProductPropertiesName(String productPropertiesName) {
		this.productPropertiesName = productPropertiesName;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getInSubject() {
		return inSubject;
	}

	public void setInSubject(String inSubject) {
		this.inSubject = inSubject;
	}

	public String getCostSubject() {
		return costSubject;
	}

	public void setCostSubject(String costSubject) {
		this.costSubject = costSubject;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.productPropertiesId;
	}

	@Override
	public String toString() {
		return "CamsProductProperties{" +
			", productPropertiesId=" + productPropertiesId +
			", productPropertiesName=" + productPropertiesName +
			", taxName=" + taxName +
			", categoryName=" + categoryName +
			", inSubject=" + inSubject +
			", costSubject=" + costSubject +
			", createTime=" + createTime +
			"}";
	}
}
