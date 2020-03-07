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
 * 税率表
 * </p>
 *
 * @author czs
 * @since 2019-02-01
 */
@ApiModel
@TableName("tb_cams_tax")
public class CamsTax extends Model<CamsTax> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键/自增
     */
    @TableId("tax_id")
	@ApiModelProperty(required= true,value = "主键/自增")
	private Integer taxId;
    /**
     * 税率名称
     */
	@TableField("tax_name")
	@ApiModelProperty(required= true,value = "税率名称")
	private String taxName;
    /**
     * 进项税率
     */
	@TableField("buy_tax")
	@ApiModelProperty(required= true,value = "进项税率")
	private String buyTax;
    /**
     * 销项税率
     */
	@TableField("sell_tax")
	@ApiModelProperty(required= true,value = "销项税率")
	private String sellTax;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getBuyTax() {
		return buyTax;
	}

	public void setBuyTax(String buyTax) {
		this.buyTax = buyTax;
	}

	public String getSellTax() {
		return sellTax;
	}

	public void setSellTax(String sellTax) {
		this.sellTax = sellTax;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.taxId;
	}

	@Override
	public String toString() {
		return "CamsTax{" +
			", taxId=" + taxId +
			", taxName=" + taxName +
			", buyTax=" + buyTax +
			", sellTax=" + sellTax +
			", createTime=" + createTime +
			"}";
	}
}
