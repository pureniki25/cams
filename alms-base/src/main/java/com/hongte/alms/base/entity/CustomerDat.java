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
 * 客户与供应商表
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@ApiModel
@TableName("tb_customer_dat")
public class CustomerDat extends Model<CustomerDat> {

    private static final long serialVersionUID = 1L;

    /**
     * 单位编号
     */
    @TableId("customer_code")
	@ApiModelProperty(required= true,value = "单位编号")
	private String customerCode;
    
    /**
     * 开票日期
     */
    @TableField("open_date")
	@ApiModelProperty(required= true,value = "开票日期")
    private String openDate;
    /**
     * 1 客户  2 供应商
     */
	@ApiModelProperty(required= true,value = "1 客户  2 供应商")
	private Integer type;
    /**
     * 公司编码
     */
	@TableField("company_code")
	@ApiModelProperty(required= true,value = "公司编码")
	private String companyCode;
    /**
     * 单位名称
     */
	@TableField("customer_name")
	@ApiModelProperty(required= true,value = "单位名称")
	private String customerName;
    /**
     * 单位类型
     */
	@TableField("customer_type")
	@ApiModelProperty(required= true,value = "单位类型")
	private String customerType;
    /**
     * 单位类别
     */
	@TableField("customer_category")
	@ApiModelProperty(required= true,value = "单位类别")
	private String customerCategory;
	@TableField("fu_kuan_tiao_jian")
	@ApiModelProperty(required= true,value = "")
	private String fuKuanTiaoJian;
	@TableField("xin_yong_e_du")
	@ApiModelProperty(required= true,value = "")
	private String xinYongEDu="0";
	@TableField("zhe_kou_lv")
	@ApiModelProperty(required= true,value = "")
	private String zheKouLv="100";
	@TableField("yuan_gong")
	@ApiModelProperty(required= true,value = "")
	private String yuanGong;
	@TableField("di_qu")
	@ApiModelProperty(required= true,value = "")
	private String diQu;
	@TableField("lian_xi_ren")
	@ApiModelProperty(required= true,value = "")
	private String lianXiRen;
	@TableField("zhi_wu")
	@ApiModelProperty(required= true,value = "")
	private String zhiWu;
	@TableField("dian_hua1")
	@ApiModelProperty(required= true,value = "")
	private String dianHua1;
	@TableField("dian_hua2")
	@ApiModelProperty(required= true,value = "")
	private String dianHua2;
	@TableField("chuan_zhen")
	@ApiModelProperty(required= true,value = "")
	private String chuanZhen;
	@TableField("dian_zi_you_jian")
	@ApiModelProperty(required= true,value = "")
	private String dianZiYouJian;
	@TableField("shui_hao")
	@ApiModelProperty(required= true,value = "")
	private String shuiHao;
	@TableField("di_zhi")
	@ApiModelProperty(required= true,value = "")
	private String diZhi;
	@TableField("you_bian")
	@ApiModelProperty(required= true,value = "")
	private String youBian;
	@TableField("bei_zhu")
	@ApiModelProperty(required= true,value = "")
	private String beiZhu;
    /**
     * 应收科目
     */
	@TableField("yingshou_category")
	@ApiModelProperty(required= true,value = "应收科目")
	private String yingshouCategory;
    /**
     * 应付科目
     */
	@TableField("yingfu_category")
	@ApiModelProperty(required= true,value = "应付科目")
	private String yingfuCategory;
	@TableField("yingshou_zhekou_category")
	@ApiModelProperty(required= true,value = "")
	private String yingshouZhekouCategory;
	@TableField("yingfu_zhekou_category")
	@ApiModelProperty(required= true,value = "")
	private String yingfuZhekouCategory;
	@TableField("kai_hu_ri_qi")
	@ApiModelProperty(required= true,value = "")
	private String kaiHuRiQi="2018-01-01";
	@TableField("guan_bi_ri_qi")
	@ApiModelProperty(required= true,value = "")
	private String guanBiRiQi="0";
	@TableField("ting_yong_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String tingYongBiaoZhi;
	@TableField("tiexi_zhekou_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String tiexiZhekouBiaoZhi;
	@TableField("jishi_tongxun_hao")
	@ApiModelProperty(required= true,value = "")
	private String jishiTongxunHao="0";
	@TableField("xie_tong_she_zhi")
	@ApiModelProperty(required= true,value = "")
	private String xieTongSheZhi="0,,0,,0,0,,0,1";
	@TableField("jia_ge_deng_ji")
	@ApiModelProperty(required= true,value = "")
	private String jiaGeDengJi;
	@TableField("shou_ji")
	@ApiModelProperty(required= true,value = "")
	private String shouJi;
	@TableField("ying_ye_zhi_zhao")
	@ApiModelProperty(required= true,value = "")
	private String yingYeZhiZhao;
	@TableField("yao_ye_xu_ke")
	@ApiModelProperty(required= true,value = "")
	private String yaoYeXuKe;
	@TableField("yao_ye_he_ge")
	@ApiModelProperty(required= true,value = "")
	private String yaoYeHeGe;
	@TableField("qi_ye_wang_zhan")
	@ApiModelProperty(required= true,value = "")
	private String qiYeWangZhan;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "")
	private Date updateTime;


	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public String getFuKuanTiaoJian() {
		return fuKuanTiaoJian;
	}

	public void setFuKuanTiaoJian(String fuKuanTiaoJian) {
		this.fuKuanTiaoJian = fuKuanTiaoJian;
	}

	public String getXinYongEDu() {
		return xinYongEDu;
	}

	public void setXinYongEDu(String xinYongEDu) {
		this.xinYongEDu = xinYongEDu;
	}

	public String getZheKouLv() {
		return zheKouLv;
	}

	public void setZheKouLv(String zheKouLv) {
		this.zheKouLv = zheKouLv;
	}

	public String getYuanGong() {
		return yuanGong;
	}

	public void setYuanGong(String yuanGong) {
		this.yuanGong = yuanGong;
	}

	public String getDiQu() {
		return diQu;
	}

	public void setDiQu(String diQu) {
		this.diQu = diQu;
	}

	public String getLianXiRen() {
		return lianXiRen;
	}

	public void setLianXiRen(String lianXiRen) {
		this.lianXiRen = lianXiRen;
	}

	public String getZhiWu() {
		return zhiWu;
	}

	public void setZhiWu(String zhiWu) {
		this.zhiWu = zhiWu;
	}

	public String getDianHua1() {
		return dianHua1;
	}

	public void setDianHua1(String dianHua1) {
		this.dianHua1 = dianHua1;
	}

	public String getDianHua2() {
		return dianHua2;
	}

	public void setDianHua2(String dianHua2) {
		this.dianHua2 = dianHua2;
	}

	public String getChuanZhen() {
		return chuanZhen;
	}

	public void setChuanZhen(String chuanZhen) {
		this.chuanZhen = chuanZhen;
	}

	public String getDianZiYouJian() {
		return dianZiYouJian;
	}

	public void setDianZiYouJian(String dianZiYouJian) {
		this.dianZiYouJian = dianZiYouJian;
	}

	public String getShuiHao() {
		return shuiHao;
	}

	public void setShuiHao(String shuiHao) {
		this.shuiHao = shuiHao;
	}

	public String getDiZhi() {
		return diZhi;
	}

	public void setDiZhi(String diZhi) {
		this.diZhi = diZhi;
	}

	public String getYouBian() {
		return youBian;
	}

	public void setYouBian(String youBian) {
		this.youBian = youBian;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.beiZhu = beiZhu;
	}

	public String getYingshouCategory() {
		return yingshouCategory;
	}

	public void setYingshouCategory(String yingshouCategory) {
		this.yingshouCategory = yingshouCategory;
	}

	public String getYingfuCategory() {
		return yingfuCategory;
	}

	public void setYingfuCategory(String yingfuCategory) {
		this.yingfuCategory = yingfuCategory;
	}

	public String getYingshouZhekouCategory() {
		return yingshouZhekouCategory;
	}

	public void setYingshouZhekouCategory(String yingshouZhekouCategory) {
		this.yingshouZhekouCategory = yingshouZhekouCategory;
	}

	public String getYingfuZhekouCategory() {
		return yingfuZhekouCategory;
	}

	public void setYingfuZhekouCategory(String yingfuZhekouCategory) {
		this.yingfuZhekouCategory = yingfuZhekouCategory;
	}

	public String getKaiHuRiQi() {
		return kaiHuRiQi;
	}

	public void setKaiHuRiQi(String kaiHuRiQi) {
		this.kaiHuRiQi = kaiHuRiQi;
	}

	public String getGuanBiRiQi() {
		return guanBiRiQi;
	}

	public void setGuanBiRiQi(String guanBiRiQi) {
		this.guanBiRiQi = guanBiRiQi;
	}

	public String getTingYongBiaoZhi() {
		return tingYongBiaoZhi;
	}

	public void setTingYongBiaoZhi(String tingYongBiaoZhi) {
		this.tingYongBiaoZhi = tingYongBiaoZhi;
	}

	public String getTiexiZhekouBiaoZhi() {
		return tiexiZhekouBiaoZhi;
	}

	public void setTiexiZhekouBiaoZhi(String tiexiZhekouBiaoZhi) {
		this.tiexiZhekouBiaoZhi = tiexiZhekouBiaoZhi;
	}

	public String getJishiTongxunHao() {
		return jishiTongxunHao;
	}

	public void setJishiTongxunHao(String jishiTongxunHao) {
		this.jishiTongxunHao = jishiTongxunHao;
	}

	public String getXieTongSheZhi() {
		return xieTongSheZhi;
	}

	public void setXieTongSheZhi(String xieTongSheZhi) {
		this.xieTongSheZhi = xieTongSheZhi;
	}

	public String getJiaGeDengJi() {
		return jiaGeDengJi;
	}

	public void setJiaGeDengJi(String jiaGeDengJi) {
		this.jiaGeDengJi = jiaGeDengJi;
	}

	public String getShouJi() {
		return shouJi;
	}

	public void setShouJi(String shouJi) {
		this.shouJi = shouJi;
	}

	public String getYingYeZhiZhao() {
		return yingYeZhiZhao;
	}

	public void setYingYeZhiZhao(String yingYeZhiZhao) {
		this.yingYeZhiZhao = yingYeZhiZhao;
	}

	public String getYaoYeXuKe() {
		return yaoYeXuKe;
	}

	public void setYaoYeXuKe(String yaoYeXuKe) {
		this.yaoYeXuKe = yaoYeXuKe;
	}

	public String getYaoYeHeGe() {
		return yaoYeHeGe;
	}

	public void setYaoYeHeGe(String yaoYeHeGe) {
		this.yaoYeHeGe = yaoYeHeGe;
	}

	public String getQiYeWangZhan() {
		return qiYeWangZhan;
	}

	public void setQiYeWangZhan(String qiYeWangZhan) {
		this.qiYeWangZhan = qiYeWangZhan;
	}
    
	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
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
		return this.customerCode;
	}

	@Override
	public String toString() {
		return "CustomerDat{" +
			", customerCode=" + customerCode +
			", type=" + type +
			", companyCode=" + companyCode +
			", customerName=" + customerName +
			", customerType=" + customerType +
			", customerCategory=" + customerCategory +
			", fuKuanTiaoJian=" + fuKuanTiaoJian +
			", xinYongEDu=" + xinYongEDu +
			", zheKouLv=" + zheKouLv +
			", yuanGong=" + yuanGong +
			", diQu=" + diQu +
			", lianXiRen=" + lianXiRen +
			", zhiWu=" + zhiWu +
			", dianHua1=" + dianHua1 +
			", dianHua2=" + dianHua2 +
			", chuanZhen=" + chuanZhen +
			", dianZiYouJian=" + dianZiYouJian +
			", shuiHao=" + shuiHao +
			", diZhi=" + diZhi +
			", youBian=" + youBian +
			", beiZhu=" + beiZhu +
			", yingshouCategory=" + yingshouCategory +
			", yingfuCategory=" + yingfuCategory +
			", yingshouZhekouCategory=" + yingshouZhekouCategory +
			", yingfuZhekouCategory=" + yingfuZhekouCategory +
			", kaiHuRiQi=" + kaiHuRiQi +
			", guanBiRiQi=" + guanBiRiQi +
			", tingYongBiaoZhi=" + tingYongBiaoZhi +
			", tiexiZhekouBiaoZhi=" + tiexiZhekouBiaoZhi +
			", jishiTongxunHao=" + jishiTongxunHao +
			", xieTongSheZhi=" + xieTongSheZhi +
			", jiaGeDengJi=" + jiaGeDengJi +
			", shouJi=" + shouJi +
			", yingYeZhiZhao=" + yingYeZhiZhao +
			", yaoYeXuKe=" + yaoYeXuKe +
			", yaoYeHeGe=" + yaoYeHeGe +
			", qiYeWangZhan=" + qiYeWangZhan +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			"}";
	}
}
