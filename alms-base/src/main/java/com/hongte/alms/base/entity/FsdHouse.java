package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
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
 * 房速贷房产信息表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-14
 */
@ApiModel
@TableName("tb_fsd_house")
public class FsdHouse extends Model<FsdHouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 房产编号,guid
     */
    @TableId("house_id")
	@ApiModelProperty(required= true,value = "房产编号,guid")
	private String houseId;
    /**
     * 业务编号
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 信贷房产编号
     */
	@TableField("xd_house_id")
	@ApiModelProperty(required= true,value = "信贷房产编号")
	private Integer xdHouseId;
    /**
     * 房产价值
     */
	@TableField("house_value")
	@ApiModelProperty(required= true,value = "房产价值")
	private BigDecimal houseValue;
    /**
     * 房产权所属人
     */
	@TableField("house_name")
	@ApiModelProperty(required= true,value = "房产权所属人")
	private String houseName;
    /**
     * 房产位置
     */
	@TableField("house_address")
	@ApiModelProperty(required= true,value = "房产位置")
	private String houseAddress;
    /**
     * 房产证号
     */
	@TableField("house_no")
	@ApiModelProperty(required= true,value = "房产证号")
	private String houseNo;
    /**
     * 房产面积
     */
	@TableField("house_area")
	@ApiModelProperty(required= true,value = "房产面积")
	private BigDecimal houseArea;
    /**
     * 评估单价
     */
	@TableField("house_price")
	@ApiModelProperty(required= true,value = "评估单价")
	private BigDecimal housePrice;
    /**
     * 一抵贷款原始金额
     */
	@TableField("house_total")
	@ApiModelProperty(required= true,value = "一抵贷款原始金额")
	private BigDecimal houseTotal;
    /**
     * 购买年份
     */
	@TableField("buy_time")
	@ApiModelProperty(required= true,value = "购买年份")
	private String buyTime;
    /**
     * 登记年月
     */
	@TableField("register_time")
	@ApiModelProperty(required= true,value = "登记年月")
	private String registerTime;
    /**
     * 所属楼盘
     */
	@TableField("house_belong")
	@ApiModelProperty(required= true,value = "所属楼盘")
	private String houseBelong;
    /**
     * 开盘年份
     */
	@TableField("open_time")
	@ApiModelProperty(required= true,value = "开盘年份")
	private String openTime;
    /**
     * 一抵贷款时间
     */
	@TableField("borrow_time")
	@ApiModelProperty(required= true,value = "一抵贷款时间")
	private String borrowTime;
    /**
     * 一抵贷款类型
     */
	@TableField("borrow_type")
	@ApiModelProperty(required= true,value = "一抵贷款类型")
	private String borrowType;
    /**
     * 一抵贷款总额
     */
	@TableField("borrow_total")
	@ApiModelProperty(required= true,value = "一抵贷款总额")
	private BigDecimal borrowTotal;
    /**
     * 一抵贷款余额
     */
	@TableField("borrow_balance")
	@ApiModelProperty(required= true,value = "一抵贷款余额")
	private BigDecimal borrowBalance;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;
    /**
     * 购买单价
     */
	@TableField("buy_price")
	@ApiModelProperty(required= true,value = "购买单价")
	private BigDecimal buyPrice;
    /**
     * 其他网站
     */
	@TableField("other_website")
	@ApiModelProperty(required= true,value = "其他网站")
	private String otherWebsite;
    /**
     * 其他网站价格
     */
	@TableField("other_website_price")
	@ApiModelProperty(required= true,value = "其他网站价格")
	private BigDecimal otherWebsitePrice;
    /**
     * 搜房网价格
     */
	@TableField("soufan_price")
	@ApiModelProperty(required= true,value = "搜房网价格")
	private BigDecimal soufanPrice;
    /**
     * 房屋类型
     */
	@TableField("house_type")
	@ApiModelProperty(required= true,value = "房屋类型")
	private String houseType;
    /**
     * 房产所在省
     */
	@TableField("house_sheng")
	@ApiModelProperty(required= true,value = "房产所在省")
	private String houseSheng;
    /**
     * 房产所在市
     */
	@TableField("house_city")
	@ApiModelProperty(required= true,value = "房产所在市")
	private String houseCity;
    /**
     * 房产所在县
     */
	@TableField("house_xian")
	@ApiModelProperty(required= true,value = "房产所在县")
	private String houseXian;
    /**
     * 房产抵押银行
     */
	@TableField("house_pledged_bank")
	@ApiModelProperty(required= true,value = "房产抵押银行")
	private String housePledgedBank;
    /**
     * 中介名称
     */
	@TableField("agent_name")
	@ApiModelProperty(required= true,value = "中介名称")
	private String agentName;
    /**
     * 中介电话
     */
	@TableField("agent_phone")
	@ApiModelProperty(required= true,value = "中介电话")
	private String agentPhone;
    /**
     * 异地风控核实房价
     */
	@TableField("approved_rate")
	@ApiModelProperty(required= true,value = "异地风控核实房价")
	private BigDecimal approvedRate;
    /**
     * 借款金额占剩余价值成数(借款金额/房产剩余价值)
     */
	@TableField("house_remaining_space")
	@ApiModelProperty(required= true,value = "借款金额占剩余价值成数(借款金额/房产剩余价值)")
	private BigDecimal houseRemainingSpace;
    /**
     * 房产户型
     */
	@TableField("house_apartment")
	@ApiModelProperty(required= true,value = "房产户型")
	private String houseApartment;
    /**
     * 房产朝向
     */
	@TableField("house_face")
	@ApiModelProperty(required= true,value = "房产朝向")
	private String houseFace;
    /**
     * 房产目前状态()
     */
	@TableField("house_status")
	@ApiModelProperty(required= true,value = "房产目前状态()")
	private String houseStatus;
    /**
     * 小区别名
     */
	@TableField("community_name")
	@ApiModelProperty(required= true,value = "小区别名")
	private String communityName;
    /**
     * 网站1名称
     */
	@TableField("website_1")
	@ApiModelProperty(required= true,value = "网站1名称")
	private String website1;
    /**
     * 抵押类型详情，1:一抵 2:二抵
     */
	@TableField("pledge_type_detail")
	@ApiModelProperty(required= true,value = "抵押类型详情，1:一抵 2:二抵")
	private String pledgeTypeDetail;
    /**
     * 房产负债率对应区域
     */
	@TableField("debt_ratio_region")
	@ApiModelProperty(required= true,value = "房产负债率对应区域")
	private String debtRatioRegion;
    /**
     * 负债率
     */
	@TableField("debt_ratio")
	@ApiModelProperty(required= true,value = "负债率")
	private BigDecimal debtRatio;
    /**
     * 房屋年限
     */
	@TableField("house_year")
	@ApiModelProperty(required= true,value = "房屋年限")
	private Integer houseYear;
    /**
     * 一抵贷款期限
     */
	@TableField("loan_year")
	@ApiModelProperty(required= true,value = "一抵贷款期限")
	private Integer loanYear;
    /**
     * 抵押开始期限
     */
	@TableField("pledge_start_time")
	@ApiModelProperty(required= true,value = "抵押开始期限")
	private Date pledgeStartTime;
    /**
     * 抵押结束期限
     */
	@TableField("pledge_end_time")
	@ApiModelProperty(required= true,value = "抵押结束期限")
	private Date pledgeEndTime;
    /**
     * 公证开始时间
     */
	@TableField("notarization_start_time")
	@ApiModelProperty(required= true,value = "公证开始时间")
	private Date notarizationStartTime;
    /**
     * 公证结束时间
     */
	@TableField("notarization_end_time")
	@ApiModelProperty(required= true,value = "公证结束时间")
	private Date notarizationEndTime;
    /**
     * 评分表放款成数
     */
	@TableField("borrow_intoseveral")
	@ApiModelProperty(required= true,value = "评分表放款成数")
	private BigDecimal borrowIntoseveral;
    /**
     * 二抵贷款时间
     */
	@TableField("second_mortgage_time")
	@ApiModelProperty(required= true,value = "二抵贷款时间")
	private Date secondMortgageTime;
    /**
     * 二抵贷款期限
     */
	@TableField("second_mortgage_year")
	@ApiModelProperty(required= true,value = "二抵贷款期限")
	private Integer secondMortgageYear;
    /**
     * 二抵贷款类型
     */
	@TableField("second_mortgage_type")
	@ApiModelProperty(required= true,value = "二抵贷款类型")
	private String secondMortgageType;
    /**
     * 二抵贷款银行
     */
	@TableField("second_mortgage_bank")
	@ApiModelProperty(required= true,value = "二抵贷款银行")
	private String secondMortgageBank;
    /**
     * 二抵贷款总额
     */
	@TableField("second_mortgage_total")
	@ApiModelProperty(required= true,value = "二抵贷款总额")
	private BigDecimal secondMortgageTotal;
    /**
     * 二抵贷款余额
     */
	@TableField("second_mortgage_balance")
	@ApiModelProperty(required= true,value = "二抵贷款余额")
	private BigDecimal secondMortgageBalance;
    /**
     * 三抵贷款时间
     */
	@TableField("third_mortgage_time")
	@ApiModelProperty(required= true,value = "三抵贷款时间")
	private Date thirdMortgageTime;
    /**
     * 三抵贷款期限
     */
	@TableField("third_mortgage_year")
	@ApiModelProperty(required= true,value = "三抵贷款期限")
	private Integer thirdMortgageYear;
    /**
     * 三抵贷款类型
     */
	@TableField("third_mortgage_type")
	@ApiModelProperty(required= true,value = "三抵贷款类型")
	private String thirdMortgageType;
    /**
     * 三抵贷款银行
     */
	@TableField("third_mortgage_bank")
	@ApiModelProperty(required= true,value = "三抵贷款银行")
	private String thirdMortgageBank;
    /**
     * 三抵贷款总额
     */
	@TableField("third_mortgage_total")
	@ApiModelProperty(required= true,value = "三抵贷款总额")
	private String thirdMortgageTotal;
    /**
     * 三抵贷款余额
     */
	@TableField("third_mortgage_balance")
	@ApiModelProperty(required= true,value = "三抵贷款余额")
	private String thirdMortgageBalance;
    /**
     * 四抵贷款时间
     */
	@TableField("fourth_mortgage_time")
	@ApiModelProperty(required= true,value = "四抵贷款时间")
	private String fourthMortgageTime;
    /**
     * 四抵贷款期限
     */
	@TableField("fourth_mortgage_year")
	@ApiModelProperty(required= true,value = "四抵贷款期限")
	private String fourthMortgageYear;
    /**
     * 四抵贷款类型
     */
	@TableField("fourth_mortgage_type")
	@ApiModelProperty(required= true,value = "四抵贷款类型")
	private String fourthMortgageType;
    /**
     * 四抵贷款银行
     */
	@TableField("fourth_mortgage_bank")
	@ApiModelProperty(required= true,value = "四抵贷款银行")
	private String fourthMortgageBank;
    /**
     * 四抵贷款总额
     */
	@TableField("fourth_mortgage_total")
	@ApiModelProperty(required= true,value = "四抵贷款总额")
	private BigDecimal fourthMortgageTotal;
    /**
     * 四抵贷款余额
     */
	@TableField("fourth_mortgage_balance")
	@ApiModelProperty(required= true,value = "四抵贷款余额")
	private BigDecimal fourthMortgageBalance;
    /**
     * 公积金贷款时间
     */
	@TableField("providentfund_loan_time")
	@ApiModelProperty(required= true,value = "公积金贷款时间")
	private Date providentfundLoanTime;
    /**
     * 贷款金额
     */
	@TableField("loan_amount")
	@ApiModelProperty(required= true,value = "贷款金额")
	private BigDecimal loanAmount;
    /**
     * 贷款余额
     */
	@TableField("loan_balance")
	@ApiModelProperty(required= true,value = "贷款余额")
	private BigDecimal loanBalance;
    /**
     * 房产权属人身份证号码
     */
	@TableField("house_ower_idnum")
	@ApiModelProperty(required= true,value = "房产权属人身份证号码")
	private String houseOwerIdnum;
    /**
     * 隐形共有人
     */
	@TableField("hidden_ower")
	@ApiModelProperty(required= true,value = "隐形共有人")
	private String hiddenOwer;
    /**
     * 隐形共有人身份证号码
     */
	@TableField("hidden_ower_idnum")
	@ApiModelProperty(required= true,value = "隐形共有人身份证号码")
	private String hiddenOwerIdnum;
    /**
     * 房产权属人联系电话
     */
	@TableField("house_ower_phone")
	@ApiModelProperty(required= true,value = "房产权属人联系电话")
	private String houseOwerPhone;
    /**
     * 房产权属人联系地址
     */
	@TableField("house_ower_address")
	@ApiModelProperty(required= true,value = "房产权属人联系地址")
	private String houseOwerAddress;
    /**
     * 房产证地址
     */
	@TableField("house_prov_address")
	@ApiModelProperty(required= true,value = "房产证地址")
	private String houseProvAddress;
    /**
     * 土地年限,限定只能填入非零非负数的数字
     */
	@TableField("land_age_limit")
	@ApiModelProperty(required= true,value = "土地年限,限定只能填入非零非负数的数字")
	private Integer landAgeLimit;
    /**
     * 是否单签，0：否，1：是
     */
	@TableField("is_single_sign")
	@ApiModelProperty(required= true,value = "是否单签，0：否，1：是")
	private Integer isSingleSign;
    /**
     * 二抵否需要垫资
     */
	@TableField("second_mortgage_isNeedAdvance")
	@ApiModelProperty(required= true,value = "二抵否需要垫资")
	private Integer secondMortgageIsNeedAdvance;
    /**
     * 三抵否需要垫资
     */
	@TableField("third_mortgage_isNeedAdvance")
	@ApiModelProperty(required= true,value = "三抵否需要垫资")
	private Integer thirdMortgageIsNeedAdvance;
    /**
     * 四抵否需要垫资
     */
	@TableField("fourth_mortgage_isNeedAdvance")
	@ApiModelProperty(required= true,value = "四抵否需要垫资")
	private Integer fourthMortgageIsNeedAdvance;
    /**
     * 房产确认单价
     */
	@TableField("check_house_price")
	@ApiModelProperty(required= true,value = "房产确认单价")
	private BigDecimal checkHousePrice;
    /**
     * 房产确认总价
     */
	@TableField("check_house_amount")
	@ApiModelProperty(required= true,value = "房产确认总价")
	private BigDecimal checkHouseAmount;
    /**
     * 房产剩余价值
     */
	@TableField("house_remaining_space_money")
	@ApiModelProperty(required= true,value = "房产剩余价值")
	private BigDecimal houseRemainingSpaceMoney;
    /**
     * 装修情况
     */
	@TableField("renovation_status")
	@ApiModelProperty(required= true,value = "装修情况")
	private String renovationStatus;
    /**
     * 房产位置
     */
	@TableField("house_position")
	@ApiModelProperty(required= true,value = "房产位置")
	private String housePosition;
    /**
     * 房屋暂作价
     */
	@TableField("house_provisional_price")
	@ApiModelProperty(required= true,value = "房屋暂作价")
	private BigDecimal houseProvisionalPrice;
    /**
     * 是否婚内房产
     */
	@TableField("is_marriage_inner_house")
	@ApiModelProperty(required= true,value = "是否婚内房产")
	private Integer isMarriageInnerHouse;
    /**
     * 房产情况，0：全款房，1：按揭房
     */
	@TableField("house_situation")
	@ApiModelProperty(required= true,value = "房产情况，0：全款房，1：按揭房")
	private Integer houseSituation;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getXdHouseId() {
		return xdHouseId;
	}

	public void setXdHouseId(Integer xdHouseId) {
		this.xdHouseId = xdHouseId;
	}

	public BigDecimal getHouseValue() {
		return houseValue;
	}

	public void setHouseValue(BigDecimal houseValue) {
		this.houseValue = houseValue;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public BigDecimal getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(BigDecimal houseArea) {
		this.houseArea = houseArea;
	}

	public BigDecimal getHousePrice() {
		return housePrice;
	}

	public void setHousePrice(BigDecimal housePrice) {
		this.housePrice = housePrice;
	}

	public BigDecimal getHouseTotal() {
		return houseTotal;
	}

	public void setHouseTotal(BigDecimal houseTotal) {
		this.houseTotal = houseTotal;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getHouseBelong() {
		return houseBelong;
	}

	public void setHouseBelong(String houseBelong) {
		this.houseBelong = houseBelong;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getBorrowTime() {
		return borrowTime;
	}

	public void setBorrowTime(String borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public BigDecimal getBorrowTotal() {
		return borrowTotal;
	}

	public void setBorrowTotal(BigDecimal borrowTotal) {
		this.borrowTotal = borrowTotal;
	}

	public BigDecimal getBorrowBalance() {
		return borrowBalance;
	}

	public void setBorrowBalance(BigDecimal borrowBalance) {
		this.borrowBalance = borrowBalance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getOtherWebsite() {
		return otherWebsite;
	}

	public void setOtherWebsite(String otherWebsite) {
		this.otherWebsite = otherWebsite;
	}

	public BigDecimal getOtherWebsitePrice() {
		return otherWebsitePrice;
	}

	public void setOtherWebsitePrice(BigDecimal otherWebsitePrice) {
		this.otherWebsitePrice = otherWebsitePrice;
	}

	public BigDecimal getSoufanPrice() {
		return soufanPrice;
	}

	public void setSoufanPrice(BigDecimal soufanPrice) {
		this.soufanPrice = soufanPrice;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getHouseSheng() {
		return houseSheng;
	}

	public void setHouseSheng(String houseSheng) {
		this.houseSheng = houseSheng;
	}

	public String getHouseCity() {
		return houseCity;
	}

	public void setHouseCity(String houseCity) {
		this.houseCity = houseCity;
	}

	public String getHouseXian() {
		return houseXian;
	}

	public void setHouseXian(String houseXian) {
		this.houseXian = houseXian;
	}

	public String getHousePledgedBank() {
		return housePledgedBank;
	}

	public void setHousePledgedBank(String housePledgedBank) {
		this.housePledgedBank = housePledgedBank;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public BigDecimal getApprovedRate() {
		return approvedRate;
	}

	public void setApprovedRate(BigDecimal approvedRate) {
		this.approvedRate = approvedRate;
	}

	public BigDecimal getHouseRemainingSpace() {
		return houseRemainingSpace;
	}

	public void setHouseRemainingSpace(BigDecimal houseRemainingSpace) {
		this.houseRemainingSpace = houseRemainingSpace;
	}

	public String getHouseApartment() {
		return houseApartment;
	}

	public void setHouseApartment(String houseApartment) {
		this.houseApartment = houseApartment;
	}

	public String getHouseFace() {
		return houseFace;
	}

	public void setHouseFace(String houseFace) {
		this.houseFace = houseFace;
	}

	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public String getWebsite1() {
		return website1;
	}

	public void setWebsite1(String website1) {
		this.website1 = website1;
	}

	public String getPledgeTypeDetail() {
		return pledgeTypeDetail;
	}

	public void setPledgeTypeDetail(String pledgeTypeDetail) {
		this.pledgeTypeDetail = pledgeTypeDetail;
	}

	public String getDebtRatioRegion() {
		return debtRatioRegion;
	}

	public void setDebtRatioRegion(String debtRatioRegion) {
		this.debtRatioRegion = debtRatioRegion;
	}

	public BigDecimal getDebtRatio() {
		return debtRatio;
	}

	public void setDebtRatio(BigDecimal debtRatio) {
		this.debtRatio = debtRatio;
	}

	public Integer getHouseYear() {
		return houseYear;
	}

	public void setHouseYear(Integer houseYear) {
		this.houseYear = houseYear;
	}

	public Integer getLoanYear() {
		return loanYear;
	}

	public void setLoanYear(Integer loanYear) {
		this.loanYear = loanYear;
	}

	public Date getPledgeStartTime() {
		return pledgeStartTime;
	}

	public void setPledgeStartTime(Date pledgeStartTime) {
		this.pledgeStartTime = pledgeStartTime;
	}

	public Date getPledgeEndTime() {
		return pledgeEndTime;
	}

	public void setPledgeEndTime(Date pledgeEndTime) {
		this.pledgeEndTime = pledgeEndTime;
	}

	public Date getNotarizationStartTime() {
		return notarizationStartTime;
	}

	public void setNotarizationStartTime(Date notarizationStartTime) {
		this.notarizationStartTime = notarizationStartTime;
	}

	public Date getNotarizationEndTime() {
		return notarizationEndTime;
	}

	public void setNotarizationEndTime(Date notarizationEndTime) {
		this.notarizationEndTime = notarizationEndTime;
	}

	public BigDecimal getBorrowIntoseveral() {
		return borrowIntoseveral;
	}

	public void setBorrowIntoseveral(BigDecimal borrowIntoseveral) {
		this.borrowIntoseveral = borrowIntoseveral;
	}

	public Date getSecondMortgageTime() {
		return secondMortgageTime;
	}

	public void setSecondMortgageTime(Date secondMortgageTime) {
		this.secondMortgageTime = secondMortgageTime;
	}

	public Integer getSecondMortgageYear() {
		return secondMortgageYear;
	}

	public void setSecondMortgageYear(Integer secondMortgageYear) {
		this.secondMortgageYear = secondMortgageYear;
	}

	public String getSecondMortgageType() {
		return secondMortgageType;
	}

	public void setSecondMortgageType(String secondMortgageType) {
		this.secondMortgageType = secondMortgageType;
	}

	public String getSecondMortgageBank() {
		return secondMortgageBank;
	}

	public void setSecondMortgageBank(String secondMortgageBank) {
		this.secondMortgageBank = secondMortgageBank;
	}

	public BigDecimal getSecondMortgageTotal() {
		return secondMortgageTotal;
	}

	public void setSecondMortgageTotal(BigDecimal secondMortgageTotal) {
		this.secondMortgageTotal = secondMortgageTotal;
	}

	public BigDecimal getSecondMortgageBalance() {
		return secondMortgageBalance;
	}

	public void setSecondMortgageBalance(BigDecimal secondMortgageBalance) {
		this.secondMortgageBalance = secondMortgageBalance;
	}

	public Date getThirdMortgageTime() {
		return thirdMortgageTime;
	}

	public void setThirdMortgageTime(Date thirdMortgageTime) {
		this.thirdMortgageTime = thirdMortgageTime;
	}

	public Integer getThirdMortgageYear() {
		return thirdMortgageYear;
	}

	public void setThirdMortgageYear(Integer thirdMortgageYear) {
		this.thirdMortgageYear = thirdMortgageYear;
	}

	public String getThirdMortgageType() {
		return thirdMortgageType;
	}

	public void setThirdMortgageType(String thirdMortgageType) {
		this.thirdMortgageType = thirdMortgageType;
	}

	public String getThirdMortgageBank() {
		return thirdMortgageBank;
	}

	public void setThirdMortgageBank(String thirdMortgageBank) {
		this.thirdMortgageBank = thirdMortgageBank;
	}

	public String getThirdMortgageTotal() {
		return thirdMortgageTotal;
	}

	public void setThirdMortgageTotal(String thirdMortgageTotal) {
		this.thirdMortgageTotal = thirdMortgageTotal;
	}

	public String getThirdMortgageBalance() {
		return thirdMortgageBalance;
	}

	public void setThirdMortgageBalance(String thirdMortgageBalance) {
		this.thirdMortgageBalance = thirdMortgageBalance;
	}

	public String getFourthMortgageTime() {
		return fourthMortgageTime;
	}

	public void setFourthMortgageTime(String fourthMortgageTime) {
		this.fourthMortgageTime = fourthMortgageTime;
	}

	public String getFourthMortgageYear() {
		return fourthMortgageYear;
	}

	public void setFourthMortgageYear(String fourthMortgageYear) {
		this.fourthMortgageYear = fourthMortgageYear;
	}

	public String getFourthMortgageType() {
		return fourthMortgageType;
	}

	public void setFourthMortgageType(String fourthMortgageType) {
		this.fourthMortgageType = fourthMortgageType;
	}

	public String getFourthMortgageBank() {
		return fourthMortgageBank;
	}

	public void setFourthMortgageBank(String fourthMortgageBank) {
		this.fourthMortgageBank = fourthMortgageBank;
	}

	public BigDecimal getFourthMortgageTotal() {
		return fourthMortgageTotal;
	}

	public void setFourthMortgageTotal(BigDecimal fourthMortgageTotal) {
		this.fourthMortgageTotal = fourthMortgageTotal;
	}

	public BigDecimal getFourthMortgageBalance() {
		return fourthMortgageBalance;
	}

	public void setFourthMortgageBalance(BigDecimal fourthMortgageBalance) {
		this.fourthMortgageBalance = fourthMortgageBalance;
	}

	public Date getProvidentfundLoanTime() {
		return providentfundLoanTime;
	}

	public void setProvidentfundLoanTime(Date providentfundLoanTime) {
		this.providentfundLoanTime = providentfundLoanTime;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(BigDecimal loanBalance) {
		this.loanBalance = loanBalance;
	}

	public String getHouseOwerIdnum() {
		return houseOwerIdnum;
	}

	public void setHouseOwerIdnum(String houseOwerIdnum) {
		this.houseOwerIdnum = houseOwerIdnum;
	}

	public String getHiddenOwer() {
		return hiddenOwer;
	}

	public void setHiddenOwer(String hiddenOwer) {
		this.hiddenOwer = hiddenOwer;
	}

	public String getHiddenOwerIdnum() {
		return hiddenOwerIdnum;
	}

	public void setHiddenOwerIdnum(String hiddenOwerIdnum) {
		this.hiddenOwerIdnum = hiddenOwerIdnum;
	}

	public String getHouseOwerPhone() {
		return houseOwerPhone;
	}

	public void setHouseOwerPhone(String houseOwerPhone) {
		this.houseOwerPhone = houseOwerPhone;
	}

	public String getHouseOwerAddress() {
		return houseOwerAddress;
	}

	public void setHouseOwerAddress(String houseOwerAddress) {
		this.houseOwerAddress = houseOwerAddress;
	}

	public String getHouseProvAddress() {
		return houseProvAddress;
	}

	public void setHouseProvAddress(String houseProvAddress) {
		this.houseProvAddress = houseProvAddress;
	}

	public Integer getLandAgeLimit() {
		return landAgeLimit;
	}

	public void setLandAgeLimit(Integer landAgeLimit) {
		this.landAgeLimit = landAgeLimit;
	}

	public Integer getIsSingleSign() {
		return isSingleSign;
	}

	public void setIsSingleSign(Integer isSingleSign) {
		this.isSingleSign = isSingleSign;
	}

	public Integer getSecondMortgageIsNeedAdvance() {
		return secondMortgageIsNeedAdvance;
	}

	public void setSecondMortgageIsNeedAdvance(Integer secondMortgageIsNeedAdvance) {
		this.secondMortgageIsNeedAdvance = secondMortgageIsNeedAdvance;
	}

	public Integer getThirdMortgageIsNeedAdvance() {
		return thirdMortgageIsNeedAdvance;
	}

	public void setThirdMortgageIsNeedAdvance(Integer thirdMortgageIsNeedAdvance) {
		this.thirdMortgageIsNeedAdvance = thirdMortgageIsNeedAdvance;
	}

	public Integer getFourthMortgageIsNeedAdvance() {
		return fourthMortgageIsNeedAdvance;
	}

	public void setFourthMortgageIsNeedAdvance(Integer fourthMortgageIsNeedAdvance) {
		this.fourthMortgageIsNeedAdvance = fourthMortgageIsNeedAdvance;
	}

	public BigDecimal getCheckHousePrice() {
		return checkHousePrice;
	}

	public void setCheckHousePrice(BigDecimal checkHousePrice) {
		this.checkHousePrice = checkHousePrice;
	}

	public BigDecimal getCheckHouseAmount() {
		return checkHouseAmount;
	}

	public void setCheckHouseAmount(BigDecimal checkHouseAmount) {
		this.checkHouseAmount = checkHouseAmount;
	}

	public BigDecimal getHouseRemainingSpaceMoney() {
		return houseRemainingSpaceMoney;
	}

	public void setHouseRemainingSpaceMoney(BigDecimal houseRemainingSpaceMoney) {
		this.houseRemainingSpaceMoney = houseRemainingSpaceMoney;
	}

	public String getRenovationStatus() {
		return renovationStatus;
	}

	public void setRenovationStatus(String renovationStatus) {
		this.renovationStatus = renovationStatus;
	}

	public String getHousePosition() {
		return housePosition;
	}

	public void setHousePosition(String housePosition) {
		this.housePosition = housePosition;
	}

	public BigDecimal getHouseProvisionalPrice() {
		return houseProvisionalPrice;
	}

	public void setHouseProvisionalPrice(BigDecimal houseProvisionalPrice) {
		this.houseProvisionalPrice = houseProvisionalPrice;
	}

	public Integer getIsMarriageInnerHouse() {
		return isMarriageInnerHouse;
	}

	public void setIsMarriageInnerHouse(Integer isMarriageInnerHouse) {
		this.isMarriageInnerHouse = isMarriageInnerHouse;
	}

	public Integer getHouseSituation() {
		return houseSituation;
	}

	public void setHouseSituation(Integer houseSituation) {
		this.houseSituation = houseSituation;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.houseId;
	}

	@Override
	public String toString() {
		return "FsdHouse{" +
			", houseId=" + houseId +
			", businessId=" + businessId +
			", xdHouseId=" + xdHouseId +
			", houseValue=" + houseValue +
			", houseName=" + houseName +
			", houseAddress=" + houseAddress +
			", houseNo=" + houseNo +
			", houseArea=" + houseArea +
			", housePrice=" + housePrice +
			", houseTotal=" + houseTotal +
			", buyTime=" + buyTime +
			", registerTime=" + registerTime +
			", houseBelong=" + houseBelong +
			", openTime=" + openTime +
			", borrowTime=" + borrowTime +
			", borrowType=" + borrowType +
			", borrowTotal=" + borrowTotal +
			", borrowBalance=" + borrowBalance +
			", remark=" + remark +
			", buyPrice=" + buyPrice +
			", otherWebsite=" + otherWebsite +
			", otherWebsitePrice=" + otherWebsitePrice +
			", soufanPrice=" + soufanPrice +
			", houseType=" + houseType +
			", houseSheng=" + houseSheng +
			", houseCity=" + houseCity +
			", houseXian=" + houseXian +
			", housePledgedBank=" + housePledgedBank +
			", agentName=" + agentName +
			", agentPhone=" + agentPhone +
			", approvedRate=" + approvedRate +
			", houseRemainingSpace=" + houseRemainingSpace +
			", houseApartment=" + houseApartment +
			", houseFace=" + houseFace +
			", houseStatus=" + houseStatus +
			", communityName=" + communityName +
			", website1=" + website1 +
			", pledgeTypeDetail=" + pledgeTypeDetail +
			", debtRatioRegion=" + debtRatioRegion +
			", debtRatio=" + debtRatio +
			", houseYear=" + houseYear +
			", loanYear=" + loanYear +
			", pledgeStartTime=" + pledgeStartTime +
			", pledgeEndTime=" + pledgeEndTime +
			", notarizationStartTime=" + notarizationStartTime +
			", notarizationEndTime=" + notarizationEndTime +
			", borrowIntoseveral=" + borrowIntoseveral +
			", secondMortgageTime=" + secondMortgageTime +
			", secondMortgageYear=" + secondMortgageYear +
			", secondMortgageType=" + secondMortgageType +
			", secondMortgageBank=" + secondMortgageBank +
			", secondMortgageTotal=" + secondMortgageTotal +
			", secondMortgageBalance=" + secondMortgageBalance +
			", thirdMortgageTime=" + thirdMortgageTime +
			", thirdMortgageYear=" + thirdMortgageYear +
			", thirdMortgageType=" + thirdMortgageType +
			", thirdMortgageBank=" + thirdMortgageBank +
			", thirdMortgageTotal=" + thirdMortgageTotal +
			", thirdMortgageBalance=" + thirdMortgageBalance +
			", fourthMortgageTime=" + fourthMortgageTime +
			", fourthMortgageYear=" + fourthMortgageYear +
			", fourthMortgageType=" + fourthMortgageType +
			", fourthMortgageBank=" + fourthMortgageBank +
			", fourthMortgageTotal=" + fourthMortgageTotal +
			", fourthMortgageBalance=" + fourthMortgageBalance +
			", providentfundLoanTime=" + providentfundLoanTime +
			", loanAmount=" + loanAmount +
			", loanBalance=" + loanBalance +
			", houseOwerIdnum=" + houseOwerIdnum +
			", hiddenOwer=" + hiddenOwer +
			", hiddenOwerIdnum=" + hiddenOwerIdnum +
			", houseOwerPhone=" + houseOwerPhone +
			", houseOwerAddress=" + houseOwerAddress +
			", houseProvAddress=" + houseProvAddress +
			", landAgeLimit=" + landAgeLimit +
			", isSingleSign=" + isSingleSign +
			", secondMortgageIsNeedAdvance=" + secondMortgageIsNeedAdvance +
			", thirdMortgageIsNeedAdvance=" + thirdMortgageIsNeedAdvance +
			", fourthMortgageIsNeedAdvance=" + fourthMortgageIsNeedAdvance +
			", checkHousePrice=" + checkHousePrice +
			", checkHouseAmount=" + checkHouseAmount +
			", houseRemainingSpaceMoney=" + houseRemainingSpaceMoney +
			", renovationStatus=" + renovationStatus +
			", housePosition=" + housePosition +
			", houseProvisionalPrice=" + houseProvisionalPrice +
			", isMarriageInnerHouse=" + isMarriageInnerHouse +
			", houseSituation=" + houseSituation +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
