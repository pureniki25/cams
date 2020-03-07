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
 * 产品表
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@ApiModel
@TableName("tb_product_dat")
public class ProductDat extends Model<ProductDat> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编号
     */
    @TableId("product_code")
	@ApiModelProperty(required= true,value = "商品编号")
	private String productCode;
    
    
    
    /**
     * 公司名称
     */
    @TableId("company_name")
	@ApiModelProperty(required= true,value = "公司名称")
	private String companyName;
    /**
     * 商品名称
     */
	@TableField("product_name")
	@ApiModelProperty(required= true,value = "商品名称")
	private String productName;
	
    /**
     * 期初单价
     */
	@TableField("qi_chu_dan_jia")
	@ApiModelProperty(required= true,value = "期初单价")
	private String qiChuDanJia;
	
	
    /**
     * 期初金额
     */
	@TableField("qi_chu_jine")
	@ApiModelProperty(required= true,value = "期初金额")
	private String qiChuJine;
	

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
	@TableField("fengcun_biaozhi")
	@ApiModelProperty(required= true,value = "")
	private String fengcunBiaozhi="0";
    /**
     * 商品性质
     */
	@TableField("product_category")
	@ApiModelProperty(required= true,value = "商品类别")
	private String productCategory;
    /**
     * 商品类别
     */
	@TableField("product_properties")
	@ApiModelProperty(required= true,value = "商品性质")
	private String productProperties;
    /**
     * 最小计量单位
     */
	@TableField("min_cal_unit")
	@ApiModelProperty(required= true,value = "最小计量单位")
	private String minCalUnit;
    /**
     * 存货计量单位
     */
	@TableField("rest_cal_unit")
	@ApiModelProperty(required= true,value = "存货计量单位")
	private String restCalUnit;
    /**
     * 货位
     */
	@TableField("product_unit")
	@ApiModelProperty(required= true,value = "货位")
	private String productUnit;
	@TableField("chan_di")
	@ApiModelProperty(required= true,value = "")
	private String chanDi;
	@TableField("gong_ying_shang")
	@ApiModelProperty(required= true,value = "")
	private String gongYingShang;
	@TableField("shang_pin_huo_hao")
	@ApiModelProperty(required= true,value = "")
	private String shangPinHuoHao;
	@TableField("zui_xiao_ku_cun_liang")
	@ApiModelProperty(required= true,value = "")
	private String zuiXiaoKuCunLiang="0";
	@TableField("zui_da_ku_cun_liang")
	@ApiModelProperty(required= true,value = "")
	private String zuiDaKuCunLiang="0";
	@TableField("ti_qian_shi_jian")
	@ApiModelProperty(required= true,value = "")
	private String tiQianShiJian="0";
	@TableField("han_shui_cai_gou_jia")
	@ApiModelProperty(required= true,value = "")
	private String hanShuiCaiGouJia="0";
	@TableField("bu_han_shui_cai_gou_jia")
	@ApiModelProperty(required= true,value = "")
	private String buHanShuiCaiGouJia="0";
	@TableField("han_shui_xiao_shou_jia")
	@ApiModelProperty(required= true,value = "")
	private String hanShuiXiaoShouJia="0";
	@TableField("bu_han_shui_xiaos_shou_jia")
	@ApiModelProperty(required= true,value = "")
	private String buHanShuiXiaosShouJia="0";
	@TableField("bu_han_shui_ji_hua_jia")
	@ApiModelProperty(required= true,value = "")
	private String buHanShuiJiHuaJia="0";
	@TableField("han_shui_ling_shou_jia")
	@ApiModelProperty(required= true,value = "")
	private String hanShuiLingShouJia="0";
	@TableField("bao_zhi_qi")
	@ApiModelProperty(required= true,value = "")
	private String baoZhiQi="0";
	@TableField("pi_ci_guan_li_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String piCiGuanLiBiaoZhi="0";
	@TableField("zu_jian_shang_pin_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String zuJianShangPinBiaoZhi="0";
	@TableField("zu_zhuang_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String zuZhuangBiaoZhi="1";
	@TableField("shou_tuo_shang_pin_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String shouTuoShangPinBiaoZhi="0";
	@TableField("bei_zhu")
	@ApiModelProperty(required= true,value = "")
	private String beiZhu;
	@TableField("zi_ding_xiang_mu_0")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu0;
	@TableField("zi_ding_xiang_mu_1")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu1;
	@TableField("zi_ding_xiang_mu_2")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu2;
	@TableField("zi_ding_xiang_mu_3")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu3;
	@TableField("zi_ding_xiang_mu_4")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu4;
	@TableField("zi_ding_xiang_mu_5")
	@ApiModelProperty(required= true,value = "")
	private String ziDingXiangMu5;
	@TableField("ku_cun_liang")
	@ApiModelProperty(required= true,value = "")
	private String kuCunLiang="0";
	@TableField("bu_han_shui_zui_di_xiao_shou_jia")
	@ApiModelProperty(required= true,value = "")
	private String buHanShuiZuiDiXiaoShouJia="0";
	@TableField("bu_han_xiao_shou_jia_bu_da_zhe_jine")	
	@ApiModelProperty(required= true,value = "")
	private String buHanXiaoShouJiaBuDaZheJine="0";
    /**
     * 计量单位
     */
	@TableField("cal_unit")
	@ApiModelProperty(required= true,value = "计量单位")
	private String calUnit;
	@TableField("fa_piao_pin_ming")
	@ApiModelProperty(required= true,value = "")
	private String faPiaoPinMing;
	@TableField("tiao_ma")
	@ApiModelProperty(required= true,value = "")
	private String tiaoMa;
	@TableField("pi_zhun_wen_hao")
	@ApiModelProperty(required= true,value = "")
	private String piZhunWenHao;
	@TableField("zhu_ce_shang_biao")
	@ApiModelProperty(required= true,value = "")
	private String zhuCeShangBiao;
    /**
     * 包装单位
     */
	@TableField("package_unit")
	@ApiModelProperty(required= true,value = "包装单位")
	private String packageUnit;
	@TableField("bao_zhuang_gui_ge")
	@ApiModelProperty(required= true,value = "")
	private String baoZhuangGuiGe="1";
	@TableField("ti_ji_dan_wei")
	@ApiModelProperty(required= true,value = "")
	private String tiJiDanWei="立方米";
	@TableField("zhong_liang_dan_wei")
	@ApiModelProperty(required= true,value = "")
	private String zhongLiangDanWei="千克";
	@TableField("pi_ci_xiao_shou_jia_jia_lv")
	@ApiModelProperty(required= true,value = "")
	private String piCiXiaoShouJiaJiaLv="0";
	@TableField("shang_pin_ming")
	@ApiModelProperty(required= true,value = "")
	private String shangPinMing;
	@TableField("zui_gao_cai_gou_jia")
	@ApiModelProperty(required= true,value = "")
	private String zuiGaoCaiGouJia="0";
	@TableField("zui_di_cai_gou_jia")
	@ApiModelProperty(required= true,value = "")
	private String zuiDiCaiGouJia="0";
	@TableField("ABC_deng_ji")
	@ApiModelProperty(required= true,value = "")
	private String ABCDengJi;
	@TableField("wang_shang_xiao_shou_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String wangShangXiaoShouBiaoZhi="0";
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "")
	private Date updateTime;


	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	public String getFengcunBiaozhi() {
		return fengcunBiaozhi;
	}

	public void setFengcunBiaozhi(String fengcunBiaozhi) {
		this.fengcunBiaozhi = fengcunBiaozhi;
	}


	public String getProductCategory() {
		return productCategory;
	}
	
	

	public String getQiChuDanJia() {
		return qiChuDanJia;
	}

	public void setQiChuDanJia(String qiChuDanJia) {
		this.qiChuDanJia = qiChuDanJia;
	}

	public String getQiChuJine() {
		return qiChuJine;
	}

	public void setQiChuJine(String qiChuJine) {
		this.qiChuJine = qiChuJine;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}


	public String getProductProperties() {
		return productProperties;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public void setProductProperties(String productProperties) {
		this.productProperties = productProperties;
	}

	public String getMinCalUnit() {
		return minCalUnit;
	}

	public void setMinCalUnit(String minCalUnit) {
		this.minCalUnit = minCalUnit;
	}

	public String getRestCalUnit() {
		return restCalUnit;
	}

	public void setRestCalUnit(String restCalUnit) {
		this.restCalUnit = restCalUnit;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getChanDi() {
		return chanDi;
	}

	public void setChanDi(String chanDi) {
		this.chanDi = chanDi;
	}

	public String getGongYingShang() {
		return gongYingShang;
	}

	public void setGongYingShang(String gongYingShang) {
		this.gongYingShang = gongYingShang;
	}

	public String getShangPinHuoHao() {
		return shangPinHuoHao;
	}

	public void setShangPinHuoHao(String shangPinHuoHao) {
		this.shangPinHuoHao = shangPinHuoHao;
	}

	public String getZuiXiaoKuCunLiang() {
		return zuiXiaoKuCunLiang;
	}

	public void setZuiXiaoKuCunLiang(String zuiXiaoKuCunLiang) {
		this.zuiXiaoKuCunLiang = zuiXiaoKuCunLiang;
	}

	public String getZuiDaKuCunLiang() {
		return zuiDaKuCunLiang;
	}

	public void setZuiDaKuCunLiang(String zuiDaKuCunLiang) {
		this.zuiDaKuCunLiang = zuiDaKuCunLiang;
	}

	public String getTiQianShiJian() {
		return tiQianShiJian;
	}

	public void setTiQianShiJian(String tiQianShiJian) {
		this.tiQianShiJian = tiQianShiJian;
	}

	public String getHanShuiCaiGouJia() {
		return hanShuiCaiGouJia;
	}

	public void setHanShuiCaiGouJia(String hanShuiCaiGouJia) {
		this.hanShuiCaiGouJia = hanShuiCaiGouJia;
	}

	public String getBuHanShuiCaiGouJia() {
		return buHanShuiCaiGouJia;
	}

	public void setBuHanShuiCaiGouJia(String buHanShuiCaiGouJia) {
		this.buHanShuiCaiGouJia = buHanShuiCaiGouJia;
	}

	public String getHanShuiXiaoShouJia() {
		return hanShuiXiaoShouJia;
	}

	public void setHanShuiXiaoShouJia(String hanShuiXiaoShouJia) {
		this.hanShuiXiaoShouJia = hanShuiXiaoShouJia;
	}

	public String getBuHanShuiXiaosShouJia() {
		return buHanShuiXiaosShouJia;
	}

	public void setBuHanShuiXiaosShouJia(String buHanShuiXiaosShouJia) {
		this.buHanShuiXiaosShouJia = buHanShuiXiaosShouJia;
	}

	public String getBuHanShuiJiHuaJia() {
		return buHanShuiJiHuaJia;
	}

	public void setBuHanShuiJiHuaJia(String buHanShuiJiHuaJia) {
		this.buHanShuiJiHuaJia = buHanShuiJiHuaJia;
	}

	public String getHanShuiLingShouJia() {
		return hanShuiLingShouJia;
	}

	public void setHanShuiLingShouJia(String hanShuiLingShouJia) {
		this.hanShuiLingShouJia = hanShuiLingShouJia;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getPiCiGuanLiBiaoZhi() {
		return piCiGuanLiBiaoZhi;
	}

	public void setPiCiGuanLiBiaoZhi(String piCiGuanLiBiaoZhi) {
		this.piCiGuanLiBiaoZhi = piCiGuanLiBiaoZhi;
	}

	public String getZuJianShangPinBiaoZhi() {
		return zuJianShangPinBiaoZhi;
	}

	public void setZuJianShangPinBiaoZhi(String zuJianShangPinBiaoZhi) {
		this.zuJianShangPinBiaoZhi = zuJianShangPinBiaoZhi;
	}

	public String getZuZhuangBiaoZhi() {
		return zuZhuangBiaoZhi;
	}

	public void setZuZhuangBiaoZhi(String zuZhuangBiaoZhi) {
		this.zuZhuangBiaoZhi = zuZhuangBiaoZhi;
	}

	public String getShouTuoShangPinBiaoZhi() {
		return shouTuoShangPinBiaoZhi;
	}

	public void setShouTuoShangPinBiaoZhi(String shouTuoShangPinBiaoZhi) {
		this.shouTuoShangPinBiaoZhi = shouTuoShangPinBiaoZhi;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.beiZhu = beiZhu;
	}

	public String getZiDingXiangMu0() {
		return ziDingXiangMu0;
	}

	public void setZiDingXiangMu0(String ziDingXiangMu0) {
		this.ziDingXiangMu0 = ziDingXiangMu0;
	}

	public String getZiDingXiangMu1() {
		return ziDingXiangMu1;
	}

	public void setZiDingXiangMu1(String ziDingXiangMu1) {
		this.ziDingXiangMu1 = ziDingXiangMu1;
	}

	public String getZiDingXiangMu2() {
		return ziDingXiangMu2;
	}

	public void setZiDingXiangMu2(String ziDingXiangMu2) {
		this.ziDingXiangMu2 = ziDingXiangMu2;
	}

	public String getZiDingXiangMu3() {
		return ziDingXiangMu3;
	}

	public void setZiDingXiangMu3(String ziDingXiangMu3) {
		this.ziDingXiangMu3 = ziDingXiangMu3;
	}

	public String getZiDingXiangMu4() {
		return ziDingXiangMu4;
	}

	public void setZiDingXiangMu4(String ziDingXiangMu4) {
		this.ziDingXiangMu4 = ziDingXiangMu4;
	}

	public String getZiDingXiangMu5() {
		return ziDingXiangMu5;
	}

	public void setZiDingXiangMu5(String ziDingXiangMu5) {
		this.ziDingXiangMu5 = ziDingXiangMu5;
	}

	public String getKuCunLiang() {
		return kuCunLiang;
	}

	public void setKuCunLiang(String kuCunLiang) {
		this.kuCunLiang = kuCunLiang;
	}

	public String getBuHanShuiZuiDiXiaoShouJia() {
		return buHanShuiZuiDiXiaoShouJia;
	}

	public void setBuHanShuiZuiDiXiaoShouJia(String buHanShuiZuiDiXiaoShouJia) {
		this.buHanShuiZuiDiXiaoShouJia = buHanShuiZuiDiXiaoShouJia;
	}

	public String getBuHanXiaoShouJiaBuDaZheJine() {
		return buHanXiaoShouJiaBuDaZheJine;
	}

	public void setBuHanXiaoShouJiaBuDaZheJine(String buHanXiaoShouJiaBuDaZheJine) {
		this.buHanXiaoShouJiaBuDaZheJine = buHanXiaoShouJiaBuDaZheJine;
	}

	public String getCalUnit() {
		return calUnit;
	}

	public void setCalUnit(String calUnit) {
		this.calUnit = calUnit;
	}

	public String getFaPiaoPinMing() {
		return faPiaoPinMing;
	}

	public void setFaPiaoPinMing(String faPiaoPinMing) {
		this.faPiaoPinMing = faPiaoPinMing;
	}

	public String getTiaoMa() {
		return tiaoMa;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setTiaoMa(String tiaoMa) {
		this.tiaoMa = tiaoMa;
	}

	public String getPiZhunWenHao() {
		return piZhunWenHao;
	}

	public void setPiZhunWenHao(String piZhunWenHao) {
		this.piZhunWenHao = piZhunWenHao;
	}

	public String getZhuCeShangBiao() {
		return zhuCeShangBiao;
	}

	public void setZhuCeShangBiao(String zhuCeShangBiao) {
		this.zhuCeShangBiao = zhuCeShangBiao;
	}

	public String getPackageUnit() {
		return packageUnit;
	}

	public void setPackageUnit(String packageUnit) {
		this.packageUnit = packageUnit;
	}

	public String getBaoZhuangGuiGe() {
		return baoZhuangGuiGe;
	}

	public void setBaoZhuangGuiGe(String baoZhuangGuiGe) {
		this.baoZhuangGuiGe = baoZhuangGuiGe;
	}

	public String getTiJiDanWei() {
		return tiJiDanWei;
	}

	public void setTiJiDanWei(String tiJiDanWei) {
		this.tiJiDanWei = tiJiDanWei;
	}

	public String getZhongLiangDanWei() {
		return zhongLiangDanWei;
	}

	public void setZhongLiangDanWei(String zhongLiangDanWei) {
		this.zhongLiangDanWei = zhongLiangDanWei;
	}

	public String getPiCiXiaoShouJiaJiaLv() {
		return piCiXiaoShouJiaJiaLv;
	}

	public void setPiCiXiaoShouJiaJiaLv(String piCiXiaoShouJiaJiaLv) {
		this.piCiXiaoShouJiaJiaLv = piCiXiaoShouJiaJiaLv;
	}

	public String getShangPinMing() {
		return shangPinMing;
	}

	public void setShangPinMing(String shangPinMing) {
		this.shangPinMing = shangPinMing;
	}

	public String getZuiGaoCaiGouJia() {
		return zuiGaoCaiGouJia;
	}

	public void setZuiGaoCaiGouJia(String zuiGaoCaiGouJia) {
		this.zuiGaoCaiGouJia = zuiGaoCaiGouJia;
	}

	public String getZuiDiCaiGouJia() {
		return zuiDiCaiGouJia;
	}

	public void setZuiDiCaiGouJia(String zuiDiCaiGouJia) {
		this.zuiDiCaiGouJia = zuiDiCaiGouJia;
	}

	public String getABCDengJi() {
		return ABCDengJi;
	}

	public void setABCDengJi(String ABCDengJi) {
		this.ABCDengJi = ABCDengJi;
	}

	public String getWangShangXiaoShouBiaoZhi() {
		return wangShangXiaoShouBiaoZhi;
	}

	public void setWangShangXiaoShouBiaoZhi(String wangShangXiaoShouBiaoZhi) {
		this.wangShangXiaoShouBiaoZhi = wangShangXiaoShouBiaoZhi;
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
		return this.productCode;
	}

	@Override
	public String toString() {
		return "ProductDat{" +
			", productCode=" + productCode +
			", productName=" + productName +
			", productType=" + productType +
			", fengcunBiaozhi=" + fengcunBiaozhi +
			", productCategory=" + productCategory +
			", productProperties=" + productProperties +
			", minCalUnit=" + minCalUnit +
			", restCalUnit=" + restCalUnit +
			", productUnit=" + productUnit +
			", chanDi=" + chanDi +
			", gongYingShang=" + gongYingShang +
			", shangPinHuoHao=" + shangPinHuoHao +
			", zuiXiaoKuCunLiang=" + zuiXiaoKuCunLiang +
			", zuiDaKuCunLiang=" + zuiDaKuCunLiang +
			", tiQianShiJian=" + tiQianShiJian +
			", hanShuiCaiGouJia=" + hanShuiCaiGouJia +
			", buHanShuiCaiGouJia=" + buHanShuiCaiGouJia +
			", hanShuiXiaoShouJia=" + hanShuiXiaoShouJia +
			", buHanShuiXiaosShouJia=" + buHanShuiXiaosShouJia +
			", buHanShuiJiHuaJia=" + buHanShuiJiHuaJia +
			", hanShuiLingShouJia=" + hanShuiLingShouJia +
			", baoZhiQi=" + baoZhiQi +
			", piCiGuanLiBiaoZhi=" + piCiGuanLiBiaoZhi +
			", zuJianShangPinBiaoZhi=" + zuJianShangPinBiaoZhi +
			", zuZhuangBiaoZhi=" + zuZhuangBiaoZhi +
			", shouTuoShangPinBiaoZhi=" + shouTuoShangPinBiaoZhi +
			", beiZhu=" + beiZhu +
			", ziDingXiangMu0=" + ziDingXiangMu0 +
			", ziDingXiangMu1=" + ziDingXiangMu1 +
			", ziDingXiangMu2=" + ziDingXiangMu2 +
			", ziDingXiangMu3=" + ziDingXiangMu3 +
			", ziDingXiangMu4=" + ziDingXiangMu4 +
			", ziDingXiangMu5=" + ziDingXiangMu5 +
			", kuCunLiang=" + kuCunLiang +
			", buHanShuiZuiDiXiaoShouJia=" + buHanShuiZuiDiXiaoShouJia +
			", buHanXiaoShouJiaBuDaZheJine=" + buHanXiaoShouJiaBuDaZheJine +
			", calUnit=" + calUnit +
			", faPiaoPinMing=" + faPiaoPinMing +
			", tiaoMa=" + tiaoMa +
			", piZhunWenHao=" + piZhunWenHao +
			", zhuCeShangBiao=" + zhuCeShangBiao +
			", packageUnit=" + packageUnit +
			", baoZhuangGuiGe=" + baoZhuangGuiGe +
			", tiJiDanWei=" + tiJiDanWei +
			", zhongLiangDanWei=" + zhongLiangDanWei +
			", piCiXiaoShouJiaJiaLv=" + piCiXiaoShouJiaJiaLv +
			", shangPinMing=" + shangPinMing +
			", zuiGaoCaiGouJia=" + zuiGaoCaiGouJia +
			", zuiDiCaiGouJia=" + zuiDiCaiGouJia +
			", ABCDengJi=" + ABCDengJi +
			", wangShangXiaoShouBiaoZhi=" + wangShangXiaoShouBiaoZhi +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			"}";
	}
}
