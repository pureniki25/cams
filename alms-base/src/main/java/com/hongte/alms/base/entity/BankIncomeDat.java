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
 * 银收
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
@ApiModel
@TableName("tb_bank_income_dat")
public class BankIncomeDat extends Model<BankIncomeDat> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(required = true, value = "")
	private Integer id;
	/**
	 * 公司名称
	 */
	@TableField("company_name")
	@ApiModelProperty(required = true, value = "公司名称")
	private String companyName;
	/**
	 * 供应商编号
	 */
	@TableField("customer_code")
	@ApiModelProperty(required = true, value = "供应商编号")
	private String customerCode;

	/**
	 *
	 */
	@TableField("uuid")
	@ApiModelProperty(required = true, value = "")
	private String uuid;
	/**
	 * 发票号码
	 */
	@TableField("invoice_number")
	@ApiModelProperty(required = true, value = "发票号码")
	private String invoiceNumber;
	/**
	 * 发票类型 1:普票 2:专票
	 */
	@TableField("buy_type")
	@ApiModelProperty(required = true, value = "发票类型 1:普票 2:专票")
	private String buyType;

	/**
	 * 类型 1:收入 2:支出
	 */
	@TableField("bank_type")
	@ApiModelProperty(required = true, value = "类型 1:收入 2:支出")
	private String bankType;
	/**
	 * 是否显示 0:否 1:是
	 */
	@TableField("deduction_type")
	@ApiModelProperty(required = true, value = "是否显示 0:否 1:是")
	private String deductionType;
	/**
	 * 期间
	 */
	@TableField("qi_jian")
	@ApiModelProperty(required = true, value = "期间")
	private String qiJian;
	/**
	 * 凭证日期
	 */
	@TableField("ping_zheng_ri_qi")
	@ApiModelProperty(required = true, value = "凭证日期")
	private String pingZhengRiQi;
	/**
	 * 凭证字
	 */
	@TableField("ping_zheng_zi")
	@ApiModelProperty(required = true, value = "凭证字")
	private String pingZhengZi;
	/**
	 * 凭证号
	 */
	@TableField("ping_zheng_hao")
	@ApiModelProperty(required = true, value = "凭证号")
	private String pingZhengHao;
	/**
	 * 摘要
	 */
	@TableField("zhai_yao")
	@ApiModelProperty(required = true, value = "摘要")
	private String zhaiYao;
	/**
	 * 科目代码
	 */
	@TableField("ke_mu_dai_ma")
	@ApiModelProperty(required = true, value = "科目代码")
	private String keMuDaiMa;
	/**
	 * RMB
	 */
	@TableField("huo_bi_dai_ma")
	@ApiModelProperty(required = true, value = "RMB")
	private String huoBiDaiMa;
	/**
	 * 汇率
	 */
	@TableField("hui_lv")
	@ApiModelProperty(required = true, value = "汇率")
	private String huiLv;
	/**
	 * 原币金额
	 */
	@TableField("local_amount")
	@ApiModelProperty(required = true, value = "原币金额")
	private String localAmount;
	/**
	 * 借款金额
	 */
	@TableField("borrow_amount")
	@ApiModelProperty(required = true, value = "借款金额")
	private String borrowAmount;
	/**
	 * 贷款金额
	 */
	@TableField("alms_amount")
	@ApiModelProperty(required = true, value = "贷款金额")
	private String almsAmount;
	/**
	 * 数量
	 */
	@TableField("shu_liang")
	@ApiModelProperty(required = true, value = "数量")
	private String shuLiang;
	/**
	 * 单价
	 */
	@TableField("dan_jia")
	@ApiModelProperty(required = true, value = "单价")
	private String danJia;
	/**
	 * 制单人
	 */
	@TableField("zhi_dan_ren")
	@ApiModelProperty(required = true, value = "制单人")
	private String zhiDanRen;

	/**
	 * 对账日期
	 */
	@TableField("dui_zhang_ri_qi")
	@ApiModelProperty(required = true, value = "对账日期")
	private String duiZhangRiQi;
	/**
	 * 审核人
	 */
	@TableField("shen_he_ren")
	@ApiModelProperty(required = true, value = "审核人")
	private String shenHeRen;
	/**
	 * 过账人
	 */
	@TableField("guo_zhang_ren")
	@ApiModelProperty(required = true, value = "过账人")
	private String guoZhangRen;
	/**
	 * 附单据数
	 */
	@TableField("fu_dan_ju_shu")
	@ApiModelProperty(required = true, value = "附单据数")
	private String fuDanJuShu;
	/**
	 * 是否已过账
	 */
	@TableField("shi_fou_yi_guo_zhang")
	@ApiModelProperty(required = true, value = "是否已过账")
	private String shiFouYiGuoZhang;
	/**
	 * 模板
	 */
	@TableField("mo_ban")
	@ApiModelProperty(required = true, value = "模板")
	private String moBan;
	/**
	 * 行号
	 */
	@TableField("hang_hao")
	@ApiModelProperty(required = true, value = "行号")
	private String hangHao;
	/**
	 * 单位
	 */
	@TableField("dan_wei")
	@ApiModelProperty(required = true, value = "单位")
	private String danWei;
	/**
	 * 部门
	 */
	@TableField("bu_men")
	@ApiModelProperty(required = true, value = "部门")
	private String buMen;
	/**
	 * 员工
	 */
	@TableField("yuan_gong")
	@ApiModelProperty(required = true, value = "员工")
	private String yuanGong;
	/**
	 * 统计
	 */
	@TableField("tong_ji")
	@ApiModelProperty(required = true, value = "统计")
	private String tongJi;
	/**
	 * 项目
	 */
	@TableField("xiang_mu")
	@ApiModelProperty(required = true, value = "项目")
	private String xiangMu;
	/**
	 * 付款方法
	 */
	@TableField("fu_kuan_fang_fa")
	@ApiModelProperty(required = true, value = "付款方法")
	private String fuKuanFangFa;
	/**
	 * 票据号
	 */
	@TableField("piao_ju_hao")
	@ApiModelProperty(required = true, value = "票据号")
	private String piaoJuHao;
	/**
	 * 原币付款金额
	 */
	@TableField("yuan_bi_fu_kuan_jin_e")
	@ApiModelProperty(required = true, value = "原币付款金额")
	private String yuanBiFuKuanJinE;
	/**
	 * 凭证来源
	 */
	@TableField("ping_zheng_lai_yuan")
	@ApiModelProperty(required = true, value = "凭证来源")
	private String pingZhengLaiYuan;
	/**
	 * 来源凭证
	 */
	@TableField("lai_yuan_ping_zheng")
	@ApiModelProperty(required = true, value = "来源凭证")
	private String laiYuanPingZheng;
	/**
	 * 待打印
	 */
	@TableField("dai_da_yin")
	@ApiModelProperty(required = true, value = "待打印")
	private String daiDaYin;
	/**
	 * 作废标志
	 */
	@TableField("zuo_fei_biao_zhi")
	@ApiModelProperty(required = true, value = "作废标志")
	private String zuoFeiBiaoZhi;
	/**
	 * 错误标志
	 */
	@TableField("cuo_wu_biao_zhi")
	@ApiModelProperty(required = true, value = "错误标志")
	private String cuoWuBiaoZhi;
	/**
	 * 凭证册号
	 */
	@TableField("ping_zheng_ce_hao")
	@ApiModelProperty(required = true, value = "凭证册号")
	private String pingZhengCeHao;
	/**
	 * 出纳人
	 */
	@TableField("chu_na_ren")
	@ApiModelProperty(required = true, value = "出纳人")
	private String chuNaRen;
	
	@TableField("create_time")
	@ApiModelProperty(required = true, value = "")
	private Date createTime;

	@TableField(exist = false)
	private String beginDate;

	@TableField(exist = false)
	private String endDate;

	@TableField(exist = false)
	private String openBeginTime;

	@TableField(exist = false)
	private String openEndTime;

	
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOpenBeginTime() {
		return openBeginTime;
	}

	public void setOpenBeginTime(String openBeginTime) {
		this.openBeginTime = openBeginTime;
	}

	public String getOpenEndTime() {
		return openEndTime;
	}

	public void setOpenEndTime(String openEndTime) {
		this.openEndTime = openEndTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public String getDeductionType() {
		return deductionType;
	}

	public void setDeductionType(String deductionType) {
		this.deductionType = deductionType;
	}

	public String getQiJian() {
		return qiJian;
	}

	public void setQiJian(String qiJian) {
		this.qiJian = qiJian;
	}

	public String getPingZhengRiQi() {
		return pingZhengRiQi;
	}

	public void setPingZhengRiQi(String pingZhengRiQi) {
		this.pingZhengRiQi = pingZhengRiQi;
	}

	public String getPingZhengZi() {
		return pingZhengZi;
	}

	public void setPingZhengZi(String pingZhengZi) {
		this.pingZhengZi = pingZhengZi;
	}

	public String getPingZhengHao() {
		return pingZhengHao;
	}

	public void setPingZhengHao(String pingZhengHao) {
		this.pingZhengHao = pingZhengHao;
	}

	public String getZhaiYao() {
		return zhaiYao;
	}

	public void setZhaiYao(String zhaiYao) {
		this.zhaiYao = zhaiYao;
	}

	public String getKeMuDaiMa() {
		return keMuDaiMa;
	}

	public void setKeMuDaiMa(String keMuDaiMa) {
		this.keMuDaiMa = keMuDaiMa;
	}

	public String getHuoBiDaiMa() {
		return huoBiDaiMa;
	}

	public void setHuoBiDaiMa(String huoBiDaiMa) {
		this.huoBiDaiMa = huoBiDaiMa;
	}

	public String getHuiLv() {
		return huiLv;
	}

	public void setHuiLv(String huiLv) {
		this.huiLv = huiLv;
	}

	public String getLocalAmount() {
		return localAmount;
	}

	public void setLocalAmount(String localAmount) {
		this.localAmount = localAmount;
	}

	public String getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(String borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public String getAlmsAmount() {
		return almsAmount;
	}

	public void setAlmsAmount(String almsAmount) {
		this.almsAmount = almsAmount;
	}

	public String getShuLiang() {
		return shuLiang;
	}

	public String getDuiZhangRiQi() {
		return duiZhangRiQi;
	}

	public void setDuiZhangRiQi(String duiZhangRiQi) {
		this.duiZhangRiQi = duiZhangRiQi;
	}

	public void setShuLiang(String shuLiang) {
		this.shuLiang = shuLiang;
	}

	public String getDanJia() {
		return danJia;
	}

	public void setDanJia(String danJia) {
		this.danJia = danJia;
	}

	public String getZhiDanRen() {
		return zhiDanRen;
	}

	public void setZhiDanRen(String zhiDanRen) {
		this.zhiDanRen = zhiDanRen;
	}

	public String getShenHeRen() {
		return shenHeRen;
	}

	public void setShenHeRen(String shenHeRen) {
		this.shenHeRen = shenHeRen;
	}

	public String getGuoZhangRen() {
		return guoZhangRen;
	}

	public void setGuoZhangRen(String guoZhangRen) {
		this.guoZhangRen = guoZhangRen;
	}

	public String getFuDanJuShu() {
		return fuDanJuShu;
	}

	public void setFuDanJuShu(String fuDanJuShu) {
		this.fuDanJuShu = fuDanJuShu;
	}

	public String getShiFouYiGuoZhang() {
		return shiFouYiGuoZhang;
	}

	public void setShiFouYiGuoZhang(String shiFouYiGuoZhang) {
		this.shiFouYiGuoZhang = shiFouYiGuoZhang;
	}

	public String getMoBan() {
		return moBan;
	}

	public void setMoBan(String moBan) {
		this.moBan = moBan;
	}

	public String getHangHao() {
		return hangHao;
	}

	public void setHangHao(String hangHao) {
		this.hangHao = hangHao;
	}

	public String getDanWei() {
		return danWei;
	}

	public void setDanWei(String danWei) {
		this.danWei = danWei;
	}

	public String getBuMen() {
		return buMen;
	}

	public void setBuMen(String buMen) {
		this.buMen = buMen;
	}

	public String getYuanGong() {
		return yuanGong;
	}

	public void setYuanGong(String yuanGong) {
		this.yuanGong = yuanGong;
	}

	public String getTongJi() {
		return tongJi;
	}

	public void setTongJi(String tongJi) {
		this.tongJi = tongJi;
	}

	public String getXiangMu() {
		return xiangMu;
	}

	public void setXiangMu(String xiangMu) {
		this.xiangMu = xiangMu;
	}

	public String getFuKuanFangFa() {
		return fuKuanFangFa;
	}

	public void setFuKuanFangFa(String fuKuanFangFa) {
		this.fuKuanFangFa = fuKuanFangFa;
	}

	public String getPiaoJuHao() {
		return piaoJuHao;
	}

	public void setPiaoJuHao(String piaoJuHao) {
		this.piaoJuHao = piaoJuHao;
	}

	public String getYuanBiFuKuanJinE() {
		return yuanBiFuKuanJinE;
	}

	public void setYuanBiFuKuanJinE(String yuanBiFuKuanJinE) {
		this.yuanBiFuKuanJinE = yuanBiFuKuanJinE;
	}

	public String getPingZhengLaiYuan() {
		return pingZhengLaiYuan;
	}

	public void setPingZhengLaiYuan(String pingZhengLaiYuan) {
		this.pingZhengLaiYuan = pingZhengLaiYuan;
	}

	public String getLaiYuanPingZheng() {
		return laiYuanPingZheng;
	}

	public void setLaiYuanPingZheng(String laiYuanPingZheng) {
		this.laiYuanPingZheng = laiYuanPingZheng;
	}

	public String getDaiDaYin() {
		return daiDaYin;
	}

	public void setDaiDaYin(String daiDaYin) {
		this.daiDaYin = daiDaYin;
	}

	public String getZuoFeiBiaoZhi() {
		return zuoFeiBiaoZhi;
	}

	public void setZuoFeiBiaoZhi(String zuoFeiBiaoZhi) {
		this.zuoFeiBiaoZhi = zuoFeiBiaoZhi;
	}

	public String getCuoWuBiaoZhi() {
		return cuoWuBiaoZhi;
	}

	public void setCuoWuBiaoZhi(String cuoWuBiaoZhi) {
		this.cuoWuBiaoZhi = cuoWuBiaoZhi;
	}

	public String getPingZhengCeHao() {
		return pingZhengCeHao;
	}

	public void setPingZhengCeHao(String pingZhengCeHao) {
		this.pingZhengCeHao = pingZhengCeHao;
	}

	public String getChuNaRen() {
		return chuNaRen;
	}

	public void setChuNaRen(String chuNaRen) {
		this.chuNaRen = chuNaRen;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "BankIncomeDat{" + ", id=" + id + ", companyName=" + companyName + ", customerCode=" + customerCode
				+ ", invoiceNumber=" + invoiceNumber + ", buyType=" + buyType + ", deductionType=" + deductionType
				+ ", qiJian=" + qiJian + ", pingZhengRiQi=" + pingZhengRiQi + ", pingZhengZi=" + pingZhengZi
				+ ", pingZhengHao=" + pingZhengHao + ", zhaiYao=" + zhaiYao + ", keMuDaiMa=" + keMuDaiMa
				+ ", huoBiDaiMa=" + huoBiDaiMa + ", huiLv=" + huiLv + ", localAmount=" + localAmount + ", borrowAmount="
				+ borrowAmount + ", almsAmount=" + almsAmount + ", shuLiang=" + shuLiang + ", danJia=" + danJia
				+ ", zhiDanRen=" + zhiDanRen + ", shenHeRen=" + shenHeRen + ", guoZhangRen=" + guoZhangRen
				+ ", fuDanJuShu=" + fuDanJuShu + ", shiFouYiGuoZhang=" + shiFouYiGuoZhang + ", moBan=" + moBan
				+ ", hangHao=" + hangHao + ", danWei=" + danWei + ", buMen=" + buMen + ", yuanGong=" + yuanGong
				+ ", tongJi=" + tongJi + ", xiangMu=" + xiangMu + ", fuKuanFangFa=" + fuKuanFangFa + ", piaoJuHao="
				+ piaoJuHao + ", yuanBiFuKuanJinE=" + yuanBiFuKuanJinE + ", pingZhengLaiYuan=" + pingZhengLaiYuan
				+ ", laiYuanPingZheng=" + laiYuanPingZheng + ", daiDaYin=" + daiDaYin + ", zuoFeiBiaoZhi="
				+ zuoFeiBiaoZhi + ", cuoWuBiaoZhi=" + cuoWuBiaoZhi + ", pingZhengCeHao=" + pingZhengCeHao
				+ ", chuNaRen=" + chuNaRen + ", createTime=" + createTime + "}";
	}
}
