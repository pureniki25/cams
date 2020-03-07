package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 资产表
 * </p>
 *
 * @author czs
 * @since 2019-11-06
 */
@ApiModel
@TableName("tb_buy_dat_exe")
@Data
public class BuyDatExe extends Model<BuyDatExe> {

    private static final long serialVersionUID = 1L;

    /**
     * 固定资产
     */
	@ApiModelProperty(required= true,value = "固定资产")
	private String id;
    /**
     * 采购单ID
     */
	@TableField("buy_id")
	@ApiModelProperty(required= true,value = "采购单ID")
	private String buyId;
    /**
     * 公司名称
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;
    /**
     * 类型 1:固定资产 2:无形资产 3:待摊费用
     */
	@ApiModelProperty(required= true,value = "类型 1:固定资产 2:无形资产 3:待摊费用")
	private String type;
	
	 /**
     * 银行账户科目
     */
	@TableField("bank_subject")
	@ApiModelProperty(required= true,value = "银行账户科目")
	private String bankSubject;
	

	 /**
    * 发票号码
    */
	@TableField("invoice_number")
	@ApiModelProperty(required= true,value = "发票号码")
	private String invoiceNumber;
	

	 /**
    * 产品编码
    */
	@TableField("product_code")
	@ApiModelProperty(required= true,value = "产品编码")
	private String productCode;
	
	

	 /**
    * 产品名称
    */
	@TableField("product_name")
	@ApiModelProperty(required= true,value = "产品名称")
	private String productName;
    /**
     * 资产原值
     */
	@ApiModelProperty(required= true,value = "资产原值")
	private String amount;
    /**
     * 资产净值
     */
	@TableField("rest_amount")
	@ApiModelProperty(required= true,value = "资产净值")
	private String restAmount;
	
	  /**
     * 科目代码
     */
	@TableField("subject")
	@ApiModelProperty(required= true,value = "科目代码")
	private String subject;
    /**
     * 年限
     */
	@ApiModelProperty(required= true,value = "年限")
	private String period;
    /**
     * 购买日期
     */
	@TableField("buy_date")
	@ApiModelProperty(required= true,value = "购买日期")
	private String buyDate;
    /**
     * 计提折旧日期
     */
	@TableField("jt_date")
	@ApiModelProperty(required= true,value = "计提折旧日期")
	private String jtDate;
    /**
     * 进项税额
     */
	@ApiModelProperty(required= true,value = "进项税额")
	private String tax;
    /**
     * 每次折旧金额
     */
	@TableField("discount_amount")
	@ApiModelProperty(required= true,value = "每次折旧金额")
	private String discountAmount;
    /**
     * 0：购买资产 1：接手资产
     */
	@TableField("is_take")
	@ApiModelProperty(required= true,value = "0：购买资产 1：接手资产")
	private Integer isTake;
	
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;

	
    /**
     *   是否开始计提 0：否 1：是
     */
	@TableField("is_jt")
	@ApiModelProperty(required= true,value = "0：否 1：是")
	private Integer isJt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(String restAmount) {
		this.restAmount = restAmount;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getJtDate() {
		return jtDate;
	}

	public void setJtDate(String jtDate) {
		this.jtDate = jtDate;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getIsTake() {
		return isTake;
	}

	public void setIsTake(Integer isTake) {
		this.isTake = isTake;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BuyDatExe{" +
			", id=" + id +
			", buyId=" + buyId +
			", companyName=" + companyName +
			", type=" + type +
			", amount=" + amount +
			", invoiceNumber=" + invoiceNumber +
			", productName=" + productName +
				", productCode=" + productCode +
			", restAmount=" + restAmount +
			", period=" + period +
			", buyDate=" + buyDate +
			", jtDate=" + jtDate +
			", bankSubject=" + bankSubject +
			", tax=" + tax +
			", discountAmount=" + discountAmount +
			", isTake=" + isTake +
			", createTime=" + createTime +
			"}";
	}
}
