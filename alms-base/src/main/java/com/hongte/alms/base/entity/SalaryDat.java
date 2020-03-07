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
 * 薪资表
 * </p>
 *
 * @author czs
 * @since 2019-10-13
 */
@ApiModel
@TableName("tb_salary_dat")
public class SalaryDat extends Model<SalaryDat> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer id;
    /**
     * 公司
     */
	@TableField("company_name")
	@ApiModelProperty(required= true,value = "公司")
	private String companyName;
    /**
     * 姓名
     */
	@ApiModelProperty(required= true,value = "姓名")
	private String name;
    /**
     * 身份号码
     */
	@TableField("idcard_no")
	@ApiModelProperty(required= true,value = "身份号码")
	private String idcardNo;
    /**
     * 所得期间起
     */
	@TableField("suo_de_qi_jian_qi")
	@ApiModelProperty(required= true,value = "所得期间起")
	private String suoDeQiJianQi;
    /**
     * 所得期间止
     */
	@TableField("suo_de_qi_jian_zhi")
	@ApiModelProperty(required= true,value = "所得期间止")
	private String suoDeQiJianZhi;
    /**
     * 本期收入
     */
	@TableField("ben_qi_shou_ru")
	@ApiModelProperty(required= true,value = "本期收入")
	private String benQiShouRu;
    /**
     * 本期免税收入
     */
	@TableField("ben_qi_mian_shui_shou_ru")
	@ApiModelProperty(required= true,value = "本期免税收入")
	private String benQiMianShuiShouRu;
    /**
     * 基本养老保险
     */
	@TableField("ji_ben_yang_lao_bao_xian")
	@ApiModelProperty(required= true,value = "基本养老保险")
	private String jiBenYangLaoBaoXian;
    /**
     * 基本医疗保险
     */
	@TableField("ji_ben_yi_liao_bao_xian")
	@ApiModelProperty(required= true,value = "基本医疗保险")
	private String jiBenYiLiaoBaoXian;
    /**
     * 失业保险
     */
	@TableField("shi_ye_bao_xian")
	@ApiModelProperty(required= true,value = "失业保险")
	private String shiYeBaoXian;
    /**
     * 住房公积金
     */
	@TableField("zhu_fang_gong_ji_jin")
	@ApiModelProperty(required= true,value = "住房公积金")
	private String zhuFangGongJiJin;
    /**
     * 累计子女教育
     */
	@TableField("lei_ji_zi_nv_jiao_yu")
	@ApiModelProperty(required= true,value = "累计子女教育")
	private String leiJiZiNvJiaoYu;
    /**
     * 累计住房贷款利息
     */
	@TableField("lei_ji_zhu_fang_dai_kuan_li_xi")
	@ApiModelProperty(required= true,value = "累计住房贷款利息")
	private String leiJiZhuFangDaiKuanLiXi;
    /**
     * 累计住房租金
     */
	@TableField("lei_ji_zhu_fang_zu_jin")
	@ApiModelProperty(required= true,value = "累计住房租金")
	private String leiJiZhuFangZuJin;
    /**
     * 累计赡养老人
     */
	@TableField("lei_ji_shan_yang_lao_ren")
	@ApiModelProperty(required= true,value = "累计赡养老人")
	private String leiJiShanYangLaoRen;
    /**
     * 累计继续教育
     */
	@TableField("lei_ji_ji_xu_jiao_yu")
	@ApiModelProperty(required= true,value = "累计继续教育")
	private String leiJiJiXuJiaoYu;
    /**
     * 企业年金
     */
	@TableField("qi_ye_nian_jin")
	@ApiModelProperty(required= true,value = "企业年金")
	private String qiYeNianJin;
    /**
     * 商业健康保险
     */
	@TableField("shang_ye_jian_kang_bao_xian")
	@ApiModelProperty(required= true,value = "商业健康保险")
	private String shangYeJianKangBaoXian;
    /**
     * 税延养老保险
     */
	@TableField("shui_yan_yang_lao_bao_xian")
	@ApiModelProperty(required= true,value = "税延养老保险")
	private String shuiYanYangLaoBaoXian;
    /**
     * 其他
     */
	@TableField("qi_ta")
	@ApiModelProperty(required= true,value = "其他")
	private String qiTa;
    /**
     * 对应的科目类型
     */
	@TableField("ke_mu_dai_ma")
	@ApiModelProperty(required= true,value = "对应的科目类型")
	private String keMuDaiMa;
    /**
     * 准予扣除的捐赠额
     */
	@TableField("zhun_yu_kou_chu_juan_zeng_kuan")
	@ApiModelProperty(required= true,value = "准予扣除的捐赠额")
	private String zhunYuKouChuJuanZengKuan;
    /**
     * 税前扣除项目合计
     */
	@TableField("shui_qian_kou_chu_xiang_mu_he_ji")
	@ApiModelProperty(required= true,value = "税前扣除项目合计")
	private String shuiQianKouChuXiangMuHeJi;
    /**
     * 减免税额
     */
	@TableField("jian_mian_shui_e")
	@ApiModelProperty(required= true,value = "减免税额")
	private String jianMianShuiE;
    /**
     * 减除费用标准
     */
	@TableField("jian_chu_fei_yong_biao_zhun")
	@ApiModelProperty(required= true,value = "减除费用标准")
	private String jianChuFeiYongBiaoZhun;
    /**
     * 已扣缴税额
     */
	@TableField("yi_kou_jiao_shui_e")
	@ApiModelProperty(required= true,value = "已扣缴税额")
	private String yiKouJiaoShuiE;
    /**
     * 本期已扣税额
     */
	@TableField("tax")
	@ApiModelProperty(required= true,value = "本期已扣税额")
	private String tax;
    /**
     * 单位社保合计
     */
	@TableField("dan_wei_she_bao_sum")
	@ApiModelProperty(required= true,value = "单位社保合计")
	private String danWeiSheBaoSum;
    /**
     * 个人社保合计
     */
	@TableField("ge_ren_she_bao_sum")
	@ApiModelProperty(required= true,value = "个人社保合计")
	private String geRenSheBaoSum;
    /**
     * 工资日期
     */
	@TableField("salary_date")
	@ApiModelProperty(required= true,value = "工资日期")
	private String salaryDate;
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "")
	private Date createTime;


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcardNo() {
		return idcardNo;
	}

	public void setIdcardNo(String idcardNo) {
		this.idcardNo = idcardNo;
	}

	public String getSuoDeQiJianQi() {
		return suoDeQiJianQi;
	}

	public void setSuoDeQiJianQi(String suoDeQiJianQi) {
		this.suoDeQiJianQi = suoDeQiJianQi;
	}

	public String getSuoDeQiJianZhi() {
		return suoDeQiJianZhi;
	}

	public void setSuoDeQiJianZhi(String suoDeQiJianZhi) {
		this.suoDeQiJianZhi = suoDeQiJianZhi;
	}

	public String getBenQiShouRu() {
		return benQiShouRu;
	}

	public void setBenQiShouRu(String benQiShouRu) {
		this.benQiShouRu = benQiShouRu;
	}

	public String getBenQiMianShuiShouRu() {
		return benQiMianShuiShouRu;
	}

	public void setBenQiMianShuiShouRu(String benQiMianShuiShouRu) {
		this.benQiMianShuiShouRu = benQiMianShuiShouRu;
	}

	public String getJiBenYangLaoBaoXian() {
		return jiBenYangLaoBaoXian;
	}

	public void setJiBenYangLaoBaoXian(String jiBenYangLaoBaoXian) {
		this.jiBenYangLaoBaoXian = jiBenYangLaoBaoXian;
	}

	public String getJiBenYiLiaoBaoXian() {
		return jiBenYiLiaoBaoXian;
	}

	public void setJiBenYiLiaoBaoXian(String jiBenYiLiaoBaoXian) {
		this.jiBenYiLiaoBaoXian = jiBenYiLiaoBaoXian;
	}

	public String getShiYeBaoXian() {
		return shiYeBaoXian;
	}

	public void setShiYeBaoXian(String shiYeBaoXian) {
		this.shiYeBaoXian = shiYeBaoXian;
	}

	public String getZhuFangGongJiJin() {
		return zhuFangGongJiJin;
	}

	public void setZhuFangGongJiJin(String zhuFangGongJiJin) {
		this.zhuFangGongJiJin = zhuFangGongJiJin;
	}

	public String getLeiJiZiNvJiaoYu() {
		return leiJiZiNvJiaoYu;
	}

	public void setLeiJiZiNvJiaoYu(String leiJiZiNvJiaoYu) {
		this.leiJiZiNvJiaoYu = leiJiZiNvJiaoYu;
	}

	public String getLeiJiZhuFangDaiKuanLiXi() {
		return leiJiZhuFangDaiKuanLiXi;
	}

	public void setLeiJiZhuFangDaiKuanLiXi(String leiJiZhuFangDaiKuanLiXi) {
		this.leiJiZhuFangDaiKuanLiXi = leiJiZhuFangDaiKuanLiXi;
	}

	public String getLeiJiZhuFangZuJin() {
		return leiJiZhuFangZuJin;
	}

	public void setLeiJiZhuFangZuJin(String leiJiZhuFangZuJin) {
		this.leiJiZhuFangZuJin = leiJiZhuFangZuJin;
	}

	public String getLeiJiShanYangLaoRen() {
		return leiJiShanYangLaoRen;
	}

	public void setLeiJiShanYangLaoRen(String leiJiShanYangLaoRen) {
		this.leiJiShanYangLaoRen = leiJiShanYangLaoRen;
	}

	public String getLeiJiJiXuJiaoYu() {
		return leiJiJiXuJiaoYu;
	}

	public void setLeiJiJiXuJiaoYu(String leiJiJiXuJiaoYu) {
		this.leiJiJiXuJiaoYu = leiJiJiXuJiaoYu;
	}

	public String getQiYeNianJin() {
		return qiYeNianJin;
	}

	public void setQiYeNianJin(String qiYeNianJin) {
		this.qiYeNianJin = qiYeNianJin;
	}

	public String getShangYeJianKangBaoXian() {
		return shangYeJianKangBaoXian;
	}

	public void setShangYeJianKangBaoXian(String shangYeJianKangBaoXian) {
		this.shangYeJianKangBaoXian = shangYeJianKangBaoXian;
	}

	public String getShuiYanYangLaoBaoXian() {
		return shuiYanYangLaoBaoXian;
	}

	public void setShuiYanYangLaoBaoXian(String shuiYanYangLaoBaoXian) {
		this.shuiYanYangLaoBaoXian = shuiYanYangLaoBaoXian;
	}

	public String getQiTa() {
		return qiTa;
	}

	public void setQiTa(String qiTa) {
		this.qiTa = qiTa;
	}

	public String getKeMuDaiMa() {
		return keMuDaiMa;
	}

	public void setKeMuDaiMa(String keMuDaiMa) {
		this.keMuDaiMa = keMuDaiMa;
	}

	public String getZhunYuKouChuJuanZengKuan() {
		return zhunYuKouChuJuanZengKuan;
	}

	public void setZhunYuKouChuJuanZengKuan(String zhunYuKouChuJuanZengKuan) {
		this.zhunYuKouChuJuanZengKuan = zhunYuKouChuJuanZengKuan;
	}

	public String getShuiQianKouChuXiangMuHeJi() {
		return shuiQianKouChuXiangMuHeJi;
	}

	public void setShuiQianKouChuXiangMuHeJi(String shuiQianKouChuXiangMuHeJi) {
		this.shuiQianKouChuXiangMuHeJi = shuiQianKouChuXiangMuHeJi;
	}

	public String getJianMianShuiE() {
		return jianMianShuiE;
	}

	public void setJianMianShuiE(String jianMianShuiE) {
		this.jianMianShuiE = jianMianShuiE;
	}

	public String getJianChuFeiYongBiaoZhun() {
		return jianChuFeiYongBiaoZhun;
	}

	public void setJianChuFeiYongBiaoZhun(String jianChuFeiYongBiaoZhun) {
		this.jianChuFeiYongBiaoZhun = jianChuFeiYongBiaoZhun;
	}

	public String getYiKouJiaoShuiE() {
		return yiKouJiaoShuiE;
	}

	public void setYiKouJiaoShuiE(String yiKouJiaoShuiE) {
		this.yiKouJiaoShuiE = yiKouJiaoShuiE;
	}


	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getDanWeiSheBaoSum() {
		return danWeiSheBaoSum;
	}

	public void setDanWeiSheBaoSum(String danWeiSheBaoSum) {
		this.danWeiSheBaoSum = danWeiSheBaoSum;
	}

	public String getGeRenSheBaoSum() {
		return geRenSheBaoSum;
	}

	public void setGeRenSheBaoSum(String geRenSheBaoSum) {
		this.geRenSheBaoSum = geRenSheBaoSum;
	}

	public String getSalaryDate() {
		return salaryDate;
	}

	public void setSalaryDate(String salaryDate) {
		this.salaryDate = salaryDate;
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
		return "SalaryDat{" +
			", id=" + id +
			", companyName=" + companyName +
			", name=" + name +
			", idcardNo=" + idcardNo +
			", suoDeQiJianQi=" + suoDeQiJianQi +
			", suoDeQiJianZhi=" + suoDeQiJianZhi +
			", benQiShouRu=" + benQiShouRu +
			", benQiMianShuiShouRu=" + benQiMianShuiShouRu +
			", jiBenYangLaoBaoXian=" + jiBenYangLaoBaoXian +
			", jiBenYiLiaoBaoXian=" + jiBenYiLiaoBaoXian +
			", shiYeBaoXian=" + shiYeBaoXian +
			", zhuFangGongJiJin=" + zhuFangGongJiJin +
			", leiJiZiNvJiaoYu=" + leiJiZiNvJiaoYu +
			", leiJiZhuFangDaiKuanLiXi=" + leiJiZhuFangDaiKuanLiXi +
			", leiJiZhuFangZuJin=" + leiJiZhuFangZuJin +
			", leiJiShanYangLaoRen=" + leiJiShanYangLaoRen +
			", leiJiJiXuJiaoYu=" + leiJiJiXuJiaoYu +
			", qiYeNianJin=" + qiYeNianJin +
			", shangYeJianKangBaoXian=" + shangYeJianKangBaoXian +
			", shuiYanYangLaoBaoXian=" + shuiYanYangLaoBaoXian +
			", qiTa=" + qiTa +
			", keMuDaiMa=" + keMuDaiMa +
			", zhunYuKouChuJuanZengKuan=" + zhunYuKouChuJuanZengKuan +
			", shuiQianKouChuXiangMuHeJi=" + shuiQianKouChuXiangMuHeJi +
			", jianMianShuiE=" + jianMianShuiE +
			", jianChuFeiYongBiaoZhun=" + jianChuFeiYongBiaoZhun +
			", yiKouJiaoShuiE=" + yiKouJiaoShuiE +
			", tax=" + tax +
			", danWeiSheBaoSum=" + danWeiSheBaoSum +
			", geRenSheBaoSum=" + geRenSheBaoSum +
			", salaryDate=" + salaryDate +
			", createTime=" + createTime +
			"}";
	}
}
