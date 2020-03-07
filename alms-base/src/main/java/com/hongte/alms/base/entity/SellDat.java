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
import lombok.Data;
/**
 * <p>
 * 销售单表
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
@Data
@ApiModel
@TableName("tb_sell_dat")
public class SellDat extends Model<SellDat> {

    private static final long serialVersionUID = 1L;

    /**
     * 销售单ID
     */
    @TableId("sell_id")
	@ApiModelProperty(required= true,value = "销售单ID")
	private String sellId;
    
    /**
     * 销售单类型 
     */
    @TableId("import_type")
	@ApiModelProperty(required= true,value = "销售单类型 ")
	private String importType;
    
    
    /**
     * 序号
     */
    @TableId("idx")
	@ApiModelProperty(required= true,value = "序号")
	private Integer idx;
    /**
     * 公司编号
     */
	@TableField("company_code")
	@ApiModelProperty(required= true,value = "公司编号")
	private String companyCode;
	
    /**
     * 销售单类型
     */
	@TableField("sell_type")
	@ApiModelProperty(required= true,value = "销售单类型")
	private String sellType;
    /**
     * 单据类型
     */
	@TableField("dan_ju_lei_xing")
	@ApiModelProperty(required= true,value = "单据类型")
	private String danJuLeiXing;
	@TableField("ye_wu_lei_xing")
	@ApiModelProperty(required= true,value = "")
	private String yeWuLeiXing;
	@TableField("kuai_ji_nian_du")
	@ApiModelProperty(required= true,value = "")
	private String kuaiJiNianDu;
    /**
     * 会计期间
     */
	@TableField("account_period")
	@ApiModelProperty(required= true,value = "会计期间")
	private String accountPeriod;
    /**
     * 单据号
     */
	@TableField("document_no")
	@ApiModelProperty(required= true,value = "单据号")
	private String documentNo;
    /**
     * 客户编号（往来单位）
     */
	@TableField("customer_code")
	@ApiModelProperty(required= true,value = "客户编号（往来单位）")
	private String customerCode;
	@TableField("mo_ban")
	@ApiModelProperty(required= true,value = "")
	private String moBan;
    /**
     * 制单日期
     */
	@TableField("produce_date")
	@ApiModelProperty(required= true,value = "制单日期")
	private String produceDate;
	@TableField("zhi_dan_ren")
	@ApiModelProperty(required= true,value = "")
	private String zhiDanRen;
	@TableField("zhi_yuan")
	@ApiModelProperty(required= true,value = "")
	private String zhiYuan;
	@TableField("bu_men")
	@ApiModelProperty(required= true,value = "")
	private String buMen;
	@TableField("ke_mu")
	@ApiModelProperty(required= true,value = "")
	private String keMu;
	@TableField("hui_lv")
	@ApiModelProperty(required= true,value = "")
	private String huiLv;
	@TableField("bi_zhong")
	@ApiModelProperty(required= true,value = "")
	private String biZhong;
	@TableField("xiang_mu")
	@ApiModelProperty(required= true,value = "")
	private String xiangMu;
	@TableField("tong_ji")
	@ApiModelProperty(required= true,value = "")
	private String tongJi;
    /**
     * 到期日期
     */
	@TableField("due_date")
	@ApiModelProperty(required= true,value = "到期日期")
	private String dueDate;
    /**
     * 开票日期
     */
	@TableField("open_date")
	@ApiModelProperty(required= true,value = "开票日期")
	private String openDate;
	@TableField("fu_kuan_tiao_jian")
	@ApiModelProperty(required= true,value = "")
	private String fuKuanTiaoJian;
    /**
     * 发票号
     */
	@TableField("invoice_number")
	@ApiModelProperty(required= true,value = "发票号")
	private String invoiceNumber;
	@TableField("fa_piao_lei_xing")
	@ApiModelProperty(required= true,value = "")
	private String faPiaoLeiXing;
	@TableField("kai_piao_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String kaiPiaoBiaoZhi;
	@TableField("dan_wei_di_zhi")
	@ApiModelProperty(required= true,value = "")
	private String danWeiDiZhi;
	@TableField("dan_wei_kai_hu_hang")
	@ApiModelProperty(required= true,value = "")
	private String danWeiKaiHuHang;
	@TableField("qi_ye_di_zhi")
	@ApiModelProperty(required= true,value = "")
	private String qiYeDiZhi;
	@TableField("qi_ye_kai_hu_hang")
	@ApiModelProperty(required= true,value = "")
	private String qiYeKaiHuHang;
	@TableField("bei_zhu")
	@ApiModelProperty(required= true,value = "")
	private String beiZhu;
	@TableField("chong_xiao_lai_yuan_dan_ju")
	@ApiModelProperty(required= true,value = "")
	private String chongXiaoLaiYuanDanJu;
    /**
     * 行号
     */
	@TableField("row_number")
	@ApiModelProperty(required= true,value = "行号")
	private String rowNumber;
    /**
     * 商品编号
     */
	@TableField("produce_code")
	@ApiModelProperty(required= true,value = "商品编号")
	private String produceCode;
	@TableField("xuan_ze_dan_ju")
	@ApiModelProperty(required= true,value = "")
	private String xuanZeDanJu;
	@TableField("huo_wei")
	@ApiModelProperty(required= true,value = "")
	private String huoWei;
    /**
     * 计量单位
     */
	@TableField("cal_unit")
	@ApiModelProperty(required= true,value = "计量单位")
	private String calUnit;
    /**
     * 数量
     */
	@ApiModelProperty(required= true,value = "数量")
	private String number;
    /**
     * 原币单价
     */
	@TableField("unit_price")
	@ApiModelProperty(required= true,value = "原币单价")
	private String unitPrice;
	@TableField("yue_ding_ri_qi")
	@ApiModelProperty(required= true,value = "")
	private String yueDingRiQi;
	@TableField("kou_lv")
	@ApiModelProperty(required= true,value = "")
	private String kouLv;
    /**
     * 原币金额
     */
	@TableField("original_amount")
	@ApiModelProperty(required= true,value = "原币金额")
	private String originalAmount;
    /**
     * 本币金额
     */
	@TableField("local_amount")
	@ApiModelProperty(required= true,value = "本币金额")
	private String localAmount;
    /**
     * 税率
     */
	@TableField("tax_rate")
	@ApiModelProperty(required= true,value = "税率")
	private String taxRate;
    /**
     * 原币税额
     */
	@TableField("original_tax")
	@ApiModelProperty(required= true,value = "原币税额")
	private String originalTax;
    /**
     * 本币税额
     */
	@TableField("localhost_tax")
	@ApiModelProperty(required= true,value = "本币税额")
	private String localhostTax;
	@TableField("sheng_chan_pi_hao")
	@ApiModelProperty(required= true,value = "")
	private String shengChanPiHao;
	@TableField("sheng_chan_ri_qi")
	@ApiModelProperty(required= true,value = "")
	private String shengChanRiQi;
	@TableField("dao_qi_ri_qi")
	@ApiModelProperty(required= true,value = "")
	private String daoQiRiQi;
	@TableField("bao_zhi_qi")
	@ApiModelProperty(required= true,value = "")
	private String baoZhiQi;
	@TableField("gong_cheng")
	@ApiModelProperty(required= true,value = "")
	private String gongCheng;
	@TableField("zi_ding_yi_xiang_mu0")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu0;
	@TableField("zi_ding_yi_xiang_mu1")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu1;
	@TableField("zi_ding_yi_xiang_mu2")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu2;
	@TableField("zi_ding_yi_xiang_mu3")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu3;
	@TableField("zi_ding_yi_xiang_mu4")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu4;
	@TableField("zi_ding_yi_xiang_mu5")
	@ApiModelProperty(required= true,value = "")
	private String ziDingYiXiangMu5;
	@TableField("pi_ci_shang_pin_ming_xi")
	@ApiModelProperty(required= true,value = "")
	private String piCiShangPinMingXi;
	@TableField("cheng_ben_cha_yi")
	@ApiModelProperty(required= true,value = "")
	private String chengBenChaYi;
	@TableField("cheng_ben_jin_e")
	@ApiModelProperty(required= true,value = "")
	private String chengBenJinE;
	@TableField("guan_bi_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String guanBiBiaoZhi;
	@TableField("dai_da_yin_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String daiDaYinBiaoZhi;
	@TableField("dai_shi_xian_xiao_xiang_shui")
	@ApiModelProperty(required= true,value = "")
	private String daiShiXianXiaoXiangShui;
	@TableField("ping_jun_cheng_ben_jine")
	@ApiModelProperty(required= true,value = "")
	private String pingJunChengBenJine;
	@TableField("pin_zheng0")
	@ApiModelProperty(required= true,value = "")
	private String pinZheng0;
	@TableField("ping_zheng1")
	@ApiModelProperty(required= true,value = "")
	private String pingZheng1;
	@TableField("chu_ru_lei_bie")
	@ApiModelProperty(required= true,value = "")
	private String chuRuLeiBie;
	@TableField("shen_he_ren")
	@ApiModelProperty(required= true,value = "")
	private String shenHeRen;
	@TableField("he_tong_hao")
	@ApiModelProperty(required= true,value = "")
	private String heTongHao;
	@TableField("zeng_pin_biao_zhi")
	@ApiModelProperty(required= true,value = "")
	private String zengPinBiaoZhi;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getDanJuLeiXing() {
		return danJuLeiXing;
	}

	public void setDanJuLeiXing(String danJuLeiXing) {
		this.danJuLeiXing = danJuLeiXing;
	}

	public String getYeWuLeiXing() {
		return yeWuLeiXing;
	}

	public void setYeWuLeiXing(String yeWuLeiXing) {
		this.yeWuLeiXing = yeWuLeiXing;
	}

	public String getKuaiJiNianDu() {
		return kuaiJiNianDu;
	}

	public void setKuaiJiNianDu(String kuaiJiNianDu) {
		this.kuaiJiNianDu = kuaiJiNianDu;
	}

	public String getAccountPeriod() {
		return accountPeriod;
	}

	public void setAccountPeriod(String accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getMoBan() {
		return moBan;
	}

	public void setMoBan(String moBan) {
		this.moBan = moBan;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getZhiDanRen() {
		return zhiDanRen;
	}

	public void setZhiDanRen(String zhiDanRen) {
		this.zhiDanRen = zhiDanRen;
	}

	public String getZhiYuan() {
		return zhiYuan;
	}

	public void setZhiYuan(String zhiYuan) {
		this.zhiYuan = zhiYuan;
	}

	public String getBuMen() {
		return buMen;
	}

	public void setBuMen(String buMen) {
		this.buMen = buMen;
	}

	public String getKeMu() {
		return keMu;
	}

	public void setKeMu(String keMu) {
		this.keMu = keMu;
	}

	public String getHuiLv() {
		return huiLv;
	}

	public void setHuiLv(String huiLv) {
		this.huiLv = huiLv;
	}

	public String getBiZhong() {
		return biZhong;
	}

	public void setBiZhong(String biZhong) {
		this.biZhong = biZhong;
	}

	public String getXiangMu() {
		return xiangMu;
	}

	public void setXiangMu(String xiangMu) {
		this.xiangMu = xiangMu;
	}

	public String getTongJi() {
		return tongJi;
	}

	public void setTongJi(String tongJi) {
		this.tongJi = tongJi;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getFuKuanTiaoJian() {
		return fuKuanTiaoJian;
	}

	public void setFuKuanTiaoJian(String fuKuanTiaoJian) {
		this.fuKuanTiaoJian = fuKuanTiaoJian;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getFaPiaoLeiXing() {
		return faPiaoLeiXing;
	}

	public void setFaPiaoLeiXing(String faPiaoLeiXing) {
		this.faPiaoLeiXing = faPiaoLeiXing;
	}

	public String getKaiPiaoBiaoZhi() {
		return kaiPiaoBiaoZhi;
	}

	public void setKaiPiaoBiaoZhi(String kaiPiaoBiaoZhi) {
		this.kaiPiaoBiaoZhi = kaiPiaoBiaoZhi;
	}

	public String getDanWeiDiZhi() {
		return danWeiDiZhi;
	}

	public void setDanWeiDiZhi(String danWeiDiZhi) {
		this.danWeiDiZhi = danWeiDiZhi;
	}

	public String getDanWeiKaiHuHang() {
		return danWeiKaiHuHang;
	}

	public void setDanWeiKaiHuHang(String danWeiKaiHuHang) {
		this.danWeiKaiHuHang = danWeiKaiHuHang;
	}

	public String getQiYeDiZhi() {
		return qiYeDiZhi;
	}

	public void setQiYeDiZhi(String qiYeDiZhi) {
		this.qiYeDiZhi = qiYeDiZhi;
	}

	public String getQiYeKaiHuHang() {
		return qiYeKaiHuHang;
	}

	public void setQiYeKaiHuHang(String qiYeKaiHuHang) {
		this.qiYeKaiHuHang = qiYeKaiHuHang;
	}

	public String getBeiZhu() {
		return beiZhu;
	}

	public void setBeiZhu(String beiZhu) {
		this.beiZhu = beiZhu;
	}

	public String getChongXiaoLaiYuanDanJu() {
		return chongXiaoLaiYuanDanJu;
	}

	public void setChongXiaoLaiYuanDanJu(String chongXiaoLaiYuanDanJu) {
		this.chongXiaoLaiYuanDanJu = chongXiaoLaiYuanDanJu;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public String getXuanZeDanJu() {
		return xuanZeDanJu;
	}

	public void setXuanZeDanJu(String xuanZeDanJu) {
		this.xuanZeDanJu = xuanZeDanJu;
	}

	public String getHuoWei() {
		return huoWei;
	}

	public void setHuoWei(String huoWei) {
		this.huoWei = huoWei;
	}

	public String getCalUnit() {
		return calUnit;
	}

	public void setCalUnit(String calUnit) {
		this.calUnit = calUnit;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getYueDingRiQi() {
		return yueDingRiQi;
	}

	public void setYueDingRiQi(String yueDingRiQi) {
		this.yueDingRiQi = yueDingRiQi;
	}

	public String getKouLv() {
		return kouLv;
	}

	public void setKouLv(String kouLv) {
		this.kouLv = kouLv;
	}

	public String getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(String localAmount) {
		this.localAmount = localAmount;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getOriginalTax() {
		return originalTax;
	}

	public void setOriginalTax(String originalTax) {
		this.originalTax = originalTax;
	}

	public String getLocalhostTax() {
		return localhostTax;
	}

	public void setLocalhostTax(String localhostTax) {
		this.localhostTax = localhostTax;
	}

	public String getShengChanPiHao() {
		return shengChanPiHao;
	}

	public void setShengChanPiHao(String shengChanPiHao) {
		this.shengChanPiHao = shengChanPiHao;
	}

	public String getShengChanRiQi() {
		return shengChanRiQi;
	}

	public void setShengChanRiQi(String shengChanRiQi) {
		this.shengChanRiQi = shengChanRiQi;
	}

	public String getDaoQiRiQi() {
		return daoQiRiQi;
	}

	public void setDaoQiRiQi(String daoQiRiQi) {
		this.daoQiRiQi = daoQiRiQi;
	}

	public String getBaoZhiQi() {
		return baoZhiQi;
	}

	public void setBaoZhiQi(String baoZhiQi) {
		this.baoZhiQi = baoZhiQi;
	}

	public String getGongCheng() {
		return gongCheng;
	}

	public void setGongCheng(String gongCheng) {
		this.gongCheng = gongCheng;
	}

	public String getZiDingYiXiangMu0() {
		return ziDingYiXiangMu0;
	}

	public void setZiDingYiXiangMu0(String ziDingYiXiangMu0) {
		this.ziDingYiXiangMu0 = ziDingYiXiangMu0;
	}

	public String getZiDingYiXiangMu1() {
		return ziDingYiXiangMu1;
	}

	public void setZiDingYiXiangMu1(String ziDingYiXiangMu1) {
		this.ziDingYiXiangMu1 = ziDingYiXiangMu1;
	}

	public String getZiDingYiXiangMu2() {
		return ziDingYiXiangMu2;
	}

	public void setZiDingYiXiangMu2(String ziDingYiXiangMu2) {
		this.ziDingYiXiangMu2 = ziDingYiXiangMu2;
	}

	public String getZiDingYiXiangMu3() {
		return ziDingYiXiangMu3;
	}

	public void setZiDingYiXiangMu3(String ziDingYiXiangMu3) {
		this.ziDingYiXiangMu3 = ziDingYiXiangMu3;
	}

	public String getZiDingYiXiangMu4() {
		return ziDingYiXiangMu4;
	}

	public void setZiDingYiXiangMu4(String ziDingYiXiangMu4) {
		this.ziDingYiXiangMu4 = ziDingYiXiangMu4;
	}

	public String getZiDingYiXiangMu5() {
		return ziDingYiXiangMu5;
	}

	public Integer getidx() {
		return idx;
	}

	public void setidx(Integer idx) {
		this.idx = idx;
	}

	public void setZiDingYiXiangMu5(String ziDingYiXiangMu5) {
		this.ziDingYiXiangMu5 = ziDingYiXiangMu5;
	}

	public String getPiCiShangPinMingXi() {
		return piCiShangPinMingXi;
	}

	public void setPiCiShangPinMingXi(String piCiShangPinMingXi) {
		this.piCiShangPinMingXi = piCiShangPinMingXi;
	}

	public String getChengBenChaYi() {
		return chengBenChaYi;
	}

	public void setChengBenChaYi(String chengBenChaYi) {
		this.chengBenChaYi = chengBenChaYi;
	}

	public String getChengBenJinE() {
		return chengBenJinE;
	}

	public void setChengBenJinE(String chengBenJinE) {
		this.chengBenJinE = chengBenJinE;
	}

	public String getGuanBiBiaoZhi() {
		return guanBiBiaoZhi;
	}

	public void setGuanBiBiaoZhi(String guanBiBiaoZhi) {
		this.guanBiBiaoZhi = guanBiBiaoZhi;
	}

	public String getDaiDaYinBiaoZhi() {
		return daiDaYinBiaoZhi;
	}

	public void setDaiDaYinBiaoZhi(String daiDaYinBiaoZhi) {
		this.daiDaYinBiaoZhi = daiDaYinBiaoZhi;
	}

	public String getDaiShiXianXiaoXiangShui() {
		return daiShiXianXiaoXiangShui;
	}

	public void setDaiShiXianXiaoXiangShui(String daiShiXianXiaoXiangShui) {
		this.daiShiXianXiaoXiangShui = daiShiXianXiaoXiangShui;
	}

	public String getPingJunChengBenJine() {
		return pingJunChengBenJine;
	}

	public void setPingJunChengBenJine(String pingJunChengBenJine) {
		this.pingJunChengBenJine = pingJunChengBenJine;
	}

	public String getPinZheng0() {
		return pinZheng0;
	}

	public void setPinZheng0(String pinZheng0) {
		this.pinZheng0 = pinZheng0;
	}

	public String getPingZheng1() {
		return pingZheng1;
	}

	public void setPingZheng1(String pingZheng1) {
		this.pingZheng1 = pingZheng1;
	}

	public String getChuRuLeiBie() {
		return chuRuLeiBie;
	}

	public void setChuRuLeiBie(String chuRuLeiBie) {
		this.chuRuLeiBie = chuRuLeiBie;
	}

	public String getShenHeRen() {
		return shenHeRen;
	}

	public void setShenHeRen(String shenHeRen) {
		this.shenHeRen = shenHeRen;
	}

	public String getHeTongHao() {
		return heTongHao;
	}

	public void setHeTongHao(String heTongHao) {
		this.heTongHao = heTongHao;
	}

	public String getZengPinBiaoZhi() {
		return zengPinBiaoZhi;
	}

	public void setZengPinBiaoZhi(String zengPinBiaoZhi) {
		this.zengPinBiaoZhi = zengPinBiaoZhi;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.sellId;
	}

	@Override
	public String toString() {
		return "SellDat{" +
			", sellId=" + sellId +
			", companyCode=" + companyCode +
			", danJuLeiXing=" + danJuLeiXing +
			", yeWuLeiXing=" + yeWuLeiXing +
			", kuaiJiNianDu=" + kuaiJiNianDu +
			", accountPeriod=" + accountPeriod +
			", documentNo=" + documentNo +
			", customerCode=" + customerCode +
			", moBan=" + moBan +
			", produceDate=" + produceDate +
			", zhiDanRen=" + zhiDanRen +
			", zhiYuan=" + zhiYuan +
			", buMen=" + buMen +
			", keMu=" + keMu +
			", huiLv=" + huiLv +
			", biZhong=" + biZhong +
			", xiangMu=" + xiangMu +
			", tongJi=" + tongJi +
			", dueDate=" + dueDate +
			", openDate=" + openDate +
			", fuKuanTiaoJian=" + fuKuanTiaoJian +
			", invoiceNumber=" + invoiceNumber +
			", faPiaoLeiXing=" + faPiaoLeiXing +
			", kaiPiaoBiaoZhi=" + kaiPiaoBiaoZhi +
			", danWeiDiZhi=" + danWeiDiZhi +
			", danWeiKaiHuHang=" + danWeiKaiHuHang +
			", qiYeDiZhi=" + qiYeDiZhi +
			", qiYeKaiHuHang=" + qiYeKaiHuHang +
			", beiZhu=" + beiZhu +
			", chongXiaoLaiYuanDanJu=" + chongXiaoLaiYuanDanJu +
			", rowNumber=" + rowNumber +
			", produceCode=" + produceCode +
			", xuanZeDanJu=" + xuanZeDanJu +
			", huoWei=" + huoWei +
			", calUnit=" + calUnit +
			", number=" + number +
			", unitPrice=" + unitPrice +
			", yueDingRiQi=" + yueDingRiQi +
			", kouLv=" + kouLv +
			", originalAmount=" + originalAmount +
			", localAmount=" + localAmount +
			", taxRate=" + taxRate +
			", originalTax=" + originalTax +
			", localhostTax=" + localhostTax +
			", shengChanPiHao=" + shengChanPiHao +
			", shengChanRiQi=" + shengChanRiQi +
			", daoQiRiQi=" + daoQiRiQi +
			", baoZhiQi=" + baoZhiQi +
			", gongCheng=" + gongCheng +
			", ziDingYiXiangMu0=" + ziDingYiXiangMu0 +
			", ziDingYiXiangMu1=" + ziDingYiXiangMu1 +
			", ziDingYiXiangMu2=" + ziDingYiXiangMu2 +
			", ziDingYiXiangMu3=" + ziDingYiXiangMu3 +
			", ziDingYiXiangMu4=" + ziDingYiXiangMu4 +
			", ziDingYiXiangMu5=" + ziDingYiXiangMu5 +
			", piCiShangPinMingXi=" + piCiShangPinMingXi +
			", chengBenChaYi=" + chengBenChaYi +
			", chengBenJinE=" + chengBenJinE +
			", guanBiBiaoZhi=" + guanBiBiaoZhi +
			", daiDaYinBiaoZhi=" + daiDaYinBiaoZhi +
			", daiShiXianXiaoXiangShui=" + daiShiXianXiaoXiangShui +
			", pingJunChengBenJine=" + pingJunChengBenJine +
			", pinZheng0=" + pinZheng0 +
			", pingZheng1=" + pingZheng1 +
			", chuRuLeiBie=" + chuRuLeiBie +
			", shenHeRen=" + shenHeRen +
			", heTongHao=" + heTongHao +
			", zengPinBiaoZhi=" + zengPinBiaoZhi +
			", createTime=" + createTime +
			"}";
	}
}
