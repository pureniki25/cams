package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 库存余额表
 * </p>
 *
 * @author czs
 * @since 2020-03-22
 */
@ApiModel
@TableName("tb_product_rest_dat")
public class ProductRestDat extends Model<ProductRestDat> {

    private static final long serialVersionUID = 1L;

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
     * 开票日期
     */
	@TableField("open_date")
	@ApiModelProperty(required= true,value = "开票日期")
	private String openDate;
    /**
     * 计量单位
     */
	@TableField("cal_unit")
	@ApiModelProperty(required= true,value = "计量单位")
	private String calUnit;
    /**
     * 期初数量
     */
	@TableField("qi_chu_shu_linag")
	@ApiModelProperty(required= true,value = "期初数量")
	private BigDecimal qiChuShuLinag;
    /**
     * 期初金额
     */
	@TableField("qi_chu_jine")
	@ApiModelProperty(required= true,value = "期初金额")
	private BigDecimal qiChuJine;
    /**
     * 本期收入数量
     */
	@TableField("ben_qi_shou_ru_shu_linag")
	@ApiModelProperty(required= true,value = "本期收入数量")
	private BigDecimal benQiShouRuShuLinag;
    /**
     * 本期收入金额
     */
	@TableField("ben_qi_shou_ru_jine")
	@ApiModelProperty(required= true,value = "本期收入金额")
	private BigDecimal benQiShouRuJine;
    /**
     * 本期发出数量
     */
	@TableField("ben_qi_fa_chu_shu_linag")
	@ApiModelProperty(required= true,value = "本期发出数量")
	private BigDecimal benQiFaChuShuLinag;
    /**
     * 本期发出金额
     */
	@TableField("ben_qi_fa_chu_jine")
	@ApiModelProperty(required= true,value = "本期发出金额")
	private BigDecimal benQiFachuJine;
    /**
     * 期末结存数量
     */
	@TableField("qi_mo_jie_cun_shu_linag")
	@ApiModelProperty(required= true,value = "期末结存数量")
	private BigDecimal qiMoJieCunShuLinag;
    /**
     * 期末单价
     */
	@TableField("qi_mo_dan_jia")
	@ApiModelProperty(required= true,value = "期末单价")
	private BigDecimal qiMoDanJia;
    /**
     * 期末结存金额
     */
	@TableField("qi_mo_jie_cun_jine")
	@ApiModelProperty(required= true,value = "期末结存金额")
	private BigDecimal qiMoJieCunJine;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


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

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getCalUnit() {
		return calUnit;
	}

	public void setCalUnit(String calUnit) {
		this.calUnit = calUnit;
	}

	public BigDecimal getQiChuShuLinag() {
		return qiChuShuLinag;
	}

	public void setQiChuShuLinag(BigDecimal qiChuShuLinag) {
		this.qiChuShuLinag = qiChuShuLinag;
	}

	public BigDecimal getQiChuJine() {
		return qiChuJine;
	}

	public void setQiChuJine(BigDecimal qiChuJine) {
		this.qiChuJine = qiChuJine;
	}

	public BigDecimal getBenQiShouRuShuLinag() {
		return benQiShouRuShuLinag;
	}

	public void setBenQiShouRuShuLinag(BigDecimal benQiShouRuShuLinag) {
		this.benQiShouRuShuLinag = benQiShouRuShuLinag;
	}

	public BigDecimal getBenQiShouRuJine() {
		return benQiShouRuJine;
	}

	public void setBenQiShouRuJine(BigDecimal benQiShouRuJine) {
		this.benQiShouRuJine = benQiShouRuJine;
	}

	public BigDecimal getBenQiFaChuShuLinag() {
		return benQiFaChuShuLinag;
	}

	public void setBenQiFaChuShuLinag(BigDecimal benQiFaChuShuLinag) {
		this.benQiFaChuShuLinag = benQiFaChuShuLinag;
	}

	public BigDecimal getBenQiFachuJine() {
		return benQiFachuJine;
	}

	public void setBenQiFachuJine(BigDecimal benQiFachuJine) {
		this.benQiFachuJine = benQiFachuJine;
	}

	public BigDecimal getQiMoJieCunShuLinag() {
		return qiMoJieCunShuLinag;
	}

	public void setQiMoJieCunShuLinag(BigDecimal qiMoJieCunShuLinag) {
		this.qiMoJieCunShuLinag = qiMoJieCunShuLinag;
	}

	public BigDecimal getQiMoDanJia() {
		return qiMoDanJia;
	}

	public void setQiMoDanJia(BigDecimal qiMoDanJia) {
		this.qiMoDanJia = qiMoDanJia;
	}

	public BigDecimal getQiMoJieCunJine() {
		return qiMoJieCunJine;
	}

	public void setQiMoJieCunJine(BigDecimal qiMoJieCunJine) {
		this.qiMoJieCunJine = qiMoJieCunJine;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	@Override
	public String toString() {
		return "ProductRestDat{" +
			", productCode=" + productCode +
			", companyName=" + companyName +
			", productName=" + productName +
			", productType=" + productType +
			", openDate=" + openDate +
			", calUnit=" + calUnit +
			", qiChuShuLinag=" + qiChuShuLinag +
			", qiChuJine=" + qiChuJine +
			", benQiShouRuShuLinag=" + benQiShouRuShuLinag +
			", benQiShouRuJine=" + benQiShouRuJine +
			", benQiFaChuShuLinag=" + benQiFaChuShuLinag +
			", benQiFachuJine=" + benQiFachuJine +
			", qiMoJieCunShuLinag=" + qiMoJieCunShuLinag +
			", qiMoDanJia=" + qiMoDanJia +
			", qiMoJieCunJine=" + qiMoJieCunJine +
			", createTime=" + createTime +
			"}";
	}

	@Override
	protected Serializable pkVal() {
		// TODO Auto-generated method stub
		return null;
	}
}
