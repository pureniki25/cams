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
 * 
 * </p>
 *
 * @author 刘正全
 * @since 2018-08-01
 */
@ApiModel
@TableName("tb_issue")
public class Issue extends Model<Issue> {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty(required= true,value = "")
	private String IssueId;
	@ApiModelProperty(required= true,value = "")
	private String NickName;
	@ApiModelProperty(required= true,value = "")
	private String TelNo;
	@ApiModelProperty(required= true,value = "")
	private String Email;
	@ApiModelProperty(required= true,value = "")
	private String IdentityCard;
	@ApiModelProperty(required= true,value = "")
	private String RealName;
	@ApiModelProperty(required= true,value = "")
	private String BankAccountNo;
	@ApiModelProperty(required= true,value = "")
	private Integer BankType;
	@ApiModelProperty(required= true,value = "")
	private String BankProvice;
	@ApiModelProperty(required= true,value = "")
	private String BankCity;
	@ApiModelProperty(required= true,value = "")
	private String OpenBankName;
	@ApiModelProperty(required= true,value = "")
	private String Title;
	@ApiModelProperty(required= true,value = "")
	private Integer PeriodMonth;
	@ApiModelProperty(required= true,value = "")
	private Integer RepaymentType;
	@ApiModelProperty(required= true,value = "")
	private BigDecimal Amount;
	@ApiModelProperty(required= true,value = "")
	private BigDecimal LowerUnit;
	@ApiModelProperty(required= true,value = "")
	private String BranchCompanyId;
	@ApiModelProperty(required= true,value = "")
	private String ControlDesc;
	@ApiModelProperty(required= true,value = "")
	private String ImageUrl;
    /**
     * [标题图片编号]
     */
	@ApiModelProperty(required= true,value = "[标题图片编号]")
	private String TitleImageId;
	@ApiModelProperty(required= true,value = "")
	private String Remark;
	@ApiModelProperty(required= true,value = "")
	private String CreateUser;
	@ApiModelProperty(required= true,value = "")
	private Date CreateTime;
	@ApiModelProperty(required= true,value = "")
	private String UpdateUser;
	@ApiModelProperty(required= true,value = "")
	private Date UpdateTime;
    /**
     * 上标状态(0:暂存 1:待审 2:审核成功 3:审核失败)
     */
	@ApiModelProperty(required= true,value = "上标状态(0:暂存 1:待审 2:审核成功 3:审核失败)")
	private Integer Status;
    /**
     * 业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目)
     */
	@ApiModelProperty(required= true,value = "业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目)")
	private Integer ProjectType;
	@ApiModelProperty(required= true,value = "")
	private String ResultContent;
	@ApiModelProperty(required= true,value = "")
	private String EnterpriseUserId;
	@ApiModelProperty(required= true,value = "")
	private BigDecimal AviCreditGrantingAmount;
	@ApiModelProperty(required= true,value = "")
	private BigDecimal InterestRate;
	@ApiModelProperty(required= true,value = "")
	private BigDecimal OverRate;
	@ApiModelProperty(required= true,value = "")
	private Integer Sex;
	@ApiModelProperty(required= true,value = "")
	private Integer CredTypeId;
	@ApiModelProperty(required= true,value = "")
	private Date Birthday;
    /**
     * [风险评估意见]
     */
	@ApiModelProperty(required= true,value = "[风险评估意见]")
	private String RiskAssessment;
    /**
     * 团贷ID
     */
	@ApiModelProperty(required= true,value = "团贷ID")
	private String UserId;
    /**
     * 客户类型 1:个人 2:企业
     */
	@ApiModelProperty(required= true,value = "客户类型 1:个人 2:企业")
	private Integer UserTypeId;
    /**
     * 婚姻状况, 已婚、未婚
     */
	@ApiModelProperty(required= true,value = "婚姻状况, 已婚、未婚")
	private String Marriage;
    /**
     * 居住地址,详细地址，包括省份城市
     */
	@ApiModelProperty(required= true,value = "居住地址,详细地址，包括省份城市")
	private String Address;
    /**
     * 是否有房
     */
	@ApiModelProperty(required= true,value = "是否有房")
	private Integer IsHaveHouse;
    /**
     * 是否有车
     */
	@ApiModelProperty(required= true,value = "是否有车")
	private Integer IsHaveCar;
    /**
     * 团贷比例
     */
	@ApiModelProperty(required= true,value = "团贷比例")
	private BigDecimal TuandaiRate;
    /**
     * 担保比例
     */
	@ApiModelProperty(required= true,value = "担保比例")
	private BigDecimal GuaranteeRate;
    /**
     * 分公司比例
     */
	@ApiModelProperty(required= true,value = "分公司比例")
	private BigDecimal SubCompanyRate;
    /**
     * 团贷预计佣金
     */
	@ApiModelProperty(required= true,value = "团贷预计佣金")
	private BigDecimal TuandaiAmount;
    /**
     * 担保预计收入
     */
	@ApiModelProperty(required= true,value = "担保预计收入")
	private BigDecimal GuaranteeAmount;
    /**
     * 分公司预计金额
     */
	@ApiModelProperty(required= true,value = "分公司预计金额")
	private BigDecimal SubCompanyCharge;
    /**
     * 保证金金额
     */
	@ApiModelProperty(required= true,value = "保证金金额")
	private BigDecimal DepositAmount;
    /**
     * 押金
     */
	@ApiModelProperty(required= true,value = "押金")
	private BigDecimal FreedAmount;
    /**
     * 借款人实际金额
     */
	@ApiModelProperty(required= true,value = "借款人实际金额")
	private BigDecimal BorrowAmount;
    /**
     * 抵押权人(委托人)
     */
	@ApiModelProperty(required= true,value = "抵押权人(委托人)")
	private String CreditorId;
    /**
     * 是否委托人提现
     */
	@ApiModelProperty(required= true,value = "是否委托人提现")
	private Boolean IsBailorWithdraw;
    /**
     * 支付类型  null 或者1都是 宝付提现   2是存管提现
     */
	@ApiModelProperty(required= true,value = "支付类型  null 或者1都是 宝付提现   2是存管提现")
	private Integer PayType;
    /**
     * 中介Id或担保
     */
	@ApiModelProperty(required= true,value = "中介Id或担保")
	private String AgencyId;
    /**
     * 中介比例
     */
	@ApiModelProperty(required= true,value = "中介比例")
	private BigDecimal AgencyRate;
    /**
     * 中介金额
     */
	@ApiModelProperty(required= true,value = "中介金额")
	private BigDecimal AgencyAmount;
    /**
     * 合作公司所属团贷网分公司编号
     */
	@ApiModelProperty(required= true,value = "合作公司所属团贷网分公司编号")
	private String BusinessID;
    /**
     * 合作公司所属团贷网分公司费用比例
     */
	@ApiModelProperty(required= true,value = "合作公司所属团贷网分公司费用比例")
	private BigDecimal BusinessRate;
    /**
     * 合作公司所属团贷网分公司金额
     */
	@ApiModelProperty(required= true,value = "合作公司所属团贷网分公司金额")
	private BigDecimal BusinessAmount;
    /**
     * 押金费率
     */
	@ApiModelProperty(required= true,value = "押金费率")
	private BigDecimal FreedRate;
    /**
     * 借款人比例
     */
	@ApiModelProperty(required= true,value = "借款人比例")
	private BigDecimal BorrowerRate;
    /**
     * 标的来源0小贷系统 1一点车贷
     */
	@ApiModelProperty(required= true,value = "标的来源0小贷系统 1一点车贷")
	private Integer ProjectFrom;
    /**
     * 资金用途 (指资金流向信息、使用信息及计划等资金运用情况)
     */
	@ApiModelProperty(required= true,value = "资金用途 (指资金流向信息、使用信息及计划等资金运用情况)")
	private String FundUse;
    /**
     * 还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)
     */
	@ApiModelProperty(required= true,value = "还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)")
	private String RepaymentAssure;
    /**
     * 个人信息扩展字段
     */
	@ApiModelProperty(required= true,value = "个人信息扩展字段")
	private String Ext;
    /**
     * [每月还本金额]
     */
	@TableField("Month_Principal_Amount")
	@ApiModelProperty(required= true,value = "[每月还本金额]")
	private BigDecimal MonthPrincipalAmount;
    /**
     * 标状态码(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "标状态码(标的状态查询接口)")
	private Integer queryProjectStatus;
    /**
     * 标状态说明(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "标状态说明(标的状态查询接口)")
	private String queryStatusDesc;
    /**
     * 已申购金额(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "已申购金额(标的状态查询接口)")
	private BigDecimal queryCastedAmount;
    /**
     * 审核时间(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "审核时间(标的状态查询接口)")
	private Date queryAuditDate;
    /**
     * 审核结果,标审核失败时这里有原因(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "审核结果,标审核失败时这里有原因(标的状态查询接口)")
	private String queryResultContent;
    /**
     * 满标时间(标的状态查询接口)
     */
	@ApiModelProperty(required= true,value = "满标时间(标的状态查询接口)")
	private Date queryFullsuccessDate;
    /**
     * 1 团贷网  2你我金融
     */
	@TableField("platform_type")
	@ApiModelProperty(required= true,value = "1 团贷网  2你我金融")
	private Integer platformType;
	@TableField("request_no")
	@ApiModelProperty(required= true,value = "")
	private String requestNo;
    /**
     * 籍贯 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "籍贯 (信用贷时必填)")
	private String OriginPlace;
    /**
     * 月均收入 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "月均收入 (信用贷时必填)")
	private String MonthlyIncome;
    /**
     * 其他收入 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "其他收入 (信用贷时必填)")
	private String OtherIncome;
    /**
     * 住房情况 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "住房情况 (信用贷时必填)")
	private String Housing;
    /**
     * 所属职业 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "所属职业 (信用贷时必填)")
	private String OfficeDomain;
    /**
     * 学历 (信用贷时必填)
     */
	@ApiModelProperty(required= true,value = "学历 (信用贷时必填)")
	private String Education;


	public String getIssueId() {
		return IssueId;
	}

	public void setIssueId(String IssueId) {
		this.IssueId = IssueId;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String NickName) {
		this.NickName = NickName;
	}

	public String getTelNo() {
		return TelNo;
	}

	public void setTelNo(String TelNo) {
		this.TelNo = TelNo;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String Email) {
		this.Email = Email;
	}

	public String getIdentityCard() {
		return IdentityCard;
	}

	public void setIdentityCard(String IdentityCard) {
		this.IdentityCard = IdentityCard;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String RealName) {
		this.RealName = RealName;
	}

	public String getBankAccountNo() {
		return BankAccountNo;
	}

	public void setBankAccountNo(String BankAccountNo) {
		this.BankAccountNo = BankAccountNo;
	}

	public Integer getBankType() {
		return BankType;
	}

	public void setBankType(Integer BankType) {
		this.BankType = BankType;
	}

	public String getBankProvice() {
		return BankProvice;
	}

	public void setBankProvice(String BankProvice) {
		this.BankProvice = BankProvice;
	}

	public String getBankCity() {
		return BankCity;
	}

	public void setBankCity(String BankCity) {
		this.BankCity = BankCity;
	}

	public String getOpenBankName() {
		return OpenBankName;
	}

	public void setOpenBankName(String OpenBankName) {
		this.OpenBankName = OpenBankName;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public Integer getPeriodMonth() {
		return PeriodMonth;
	}

	public void setPeriodMonth(Integer PeriodMonth) {
		this.PeriodMonth = PeriodMonth;
	}

	public Integer getRepaymentType() {
		return RepaymentType;
	}

	public void setRepaymentType(Integer RepaymentType) {
		this.RepaymentType = RepaymentType;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public BigDecimal getLowerUnit() {
		return LowerUnit;
	}

	public void setLowerUnit(BigDecimal LowerUnit) {
		this.LowerUnit = LowerUnit;
	}

	public String getBranchCompanyId() {
		return BranchCompanyId;
	}

	public void setBranchCompanyId(String BranchCompanyId) {
		this.BranchCompanyId = BranchCompanyId;
	}

	public String getControlDesc() {
		return ControlDesc;
	}

	public void setControlDesc(String ControlDesc) {
		this.ControlDesc = ControlDesc;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String ImageUrl) {
		this.ImageUrl = ImageUrl;
	}

	public String getTitleImageId() {
		return TitleImageId;
	}

	public void setTitleImageId(String TitleImageId) {
		this.TitleImageId = TitleImageId;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	public String getCreateUser() {
		return CreateUser;
	}

	public void setCreateUser(String CreateUser) {
		this.CreateUser = CreateUser;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date CreateTime) {
		this.CreateTime = CreateTime;
	}

	public String getUpdateUser() {
		return UpdateUser;
	}

	public void setUpdateUser(String UpdateUser) {
		this.UpdateUser = UpdateUser;
	}

	public Date getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Date UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Integer getProjectType() {
		return ProjectType;
	}

	public void setProjectType(Integer ProjectType) {
		this.ProjectType = ProjectType;
	}

	public String getResultContent() {
		return ResultContent;
	}

	public void setResultContent(String ResultContent) {
		this.ResultContent = ResultContent;
	}

	public String getEnterpriseUserId() {
		return EnterpriseUserId;
	}

	public void setEnterpriseUserId(String EnterpriseUserId) {
		this.EnterpriseUserId = EnterpriseUserId;
	}

	public BigDecimal getAviCreditGrantingAmount() {
		return AviCreditGrantingAmount;
	}

	public void setAviCreditGrantingAmount(BigDecimal AviCreditGrantingAmount) {
		this.AviCreditGrantingAmount = AviCreditGrantingAmount;
	}

	public BigDecimal getInterestRate() {
		return InterestRate;
	}

	public void setInterestRate(BigDecimal InterestRate) {
		this.InterestRate = InterestRate;
	}

	public BigDecimal getOverRate() {
		return OverRate;
	}

	public void setOverRate(BigDecimal OverRate) {
		this.OverRate = OverRate;
	}

	public Integer getSex() {
		return Sex;
	}

	public void setSex(Integer Sex) {
		this.Sex = Sex;
	}

	public Integer getCredTypeId() {
		return CredTypeId;
	}

	public void setCredTypeId(Integer CredTypeId) {
		this.CredTypeId = CredTypeId;
	}

	public Date getBirthday() {
		return Birthday;
	}

	public void setBirthday(Date Birthday) {
		this.Birthday = Birthday;
	}

	public String getRiskAssessment() {
		return RiskAssessment;
	}

	public void setRiskAssessment(String RiskAssessment) {
		this.RiskAssessment = RiskAssessment;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String UserId) {
		this.UserId = UserId;
	}

	public Integer getUserTypeId() {
		return UserTypeId;
	}

	public void setUserTypeId(Integer UserTypeId) {
		this.UserTypeId = UserTypeId;
	}

	public String getMarriage() {
		return Marriage;
	}

	public void setMarriage(String Marriage) {
		this.Marriage = Marriage;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public Integer getIsHaveHouse() {
		return IsHaveHouse;
	}

	public void setIsHaveHouse(Integer IsHaveHouse) {
		this.IsHaveHouse = IsHaveHouse;
	}

	public Integer getIsHaveCar() {
		return IsHaveCar;
	}

	public void setIsHaveCar(Integer IsHaveCar) {
		this.IsHaveCar = IsHaveCar;
	}

	public BigDecimal getTuandaiRate() {
		return TuandaiRate;
	}

	public void setTuandaiRate(BigDecimal TuandaiRate) {
		this.TuandaiRate = TuandaiRate;
	}

	public BigDecimal getGuaranteeRate() {
		return GuaranteeRate;
	}

	public void setGuaranteeRate(BigDecimal GuaranteeRate) {
		this.GuaranteeRate = GuaranteeRate;
	}

	public BigDecimal getSubCompanyRate() {
		return SubCompanyRate;
	}

	public void setSubCompanyRate(BigDecimal SubCompanyRate) {
		this.SubCompanyRate = SubCompanyRate;
	}

	public BigDecimal getTuandaiAmount() {
		return TuandaiAmount;
	}

	public void setTuandaiAmount(BigDecimal TuandaiAmount) {
		this.TuandaiAmount = TuandaiAmount;
	}

	public BigDecimal getGuaranteeAmount() {
		return GuaranteeAmount;
	}

	public void setGuaranteeAmount(BigDecimal GuaranteeAmount) {
		this.GuaranteeAmount = GuaranteeAmount;
	}

	public BigDecimal getSubCompanyCharge() {
		return SubCompanyCharge;
	}

	public void setSubCompanyCharge(BigDecimal SubCompanyCharge) {
		this.SubCompanyCharge = SubCompanyCharge;
	}

	public BigDecimal getDepositAmount() {
		return DepositAmount;
	}

	public void setDepositAmount(BigDecimal DepositAmount) {
		this.DepositAmount = DepositAmount;
	}

	public BigDecimal getFreedAmount() {
		return FreedAmount;
	}

	public void setFreedAmount(BigDecimal FreedAmount) {
		this.FreedAmount = FreedAmount;
	}

	public BigDecimal getBorrowAmount() {
		return BorrowAmount;
	}

	public void setBorrowAmount(BigDecimal BorrowAmount) {
		this.BorrowAmount = BorrowAmount;
	}

	public String getCreditorId() {
		return CreditorId;
	}

	public void setCreditorId(String CreditorId) {
		this.CreditorId = CreditorId;
	}

	public Boolean getIsBailorWithdraw() {
		return IsBailorWithdraw;
	}

	public void setIsBailorWithdraw(Boolean IsBailorWithdraw) {
		this.IsBailorWithdraw = IsBailorWithdraw;
	}

	public Integer getPayType() {
		return PayType;
	}

	public void setPayType(Integer PayType) {
		this.PayType = PayType;
	}

	public String getAgencyId() {
		return AgencyId;
	}

	public void setAgencyId(String AgencyId) {
		this.AgencyId = AgencyId;
	}

	public BigDecimal getAgencyRate() {
		return AgencyRate;
	}

	public void setAgencyRate(BigDecimal AgencyRate) {
		this.AgencyRate = AgencyRate;
	}

	public BigDecimal getAgencyAmount() {
		return AgencyAmount;
	}

	public void setAgencyAmount(BigDecimal AgencyAmount) {
		this.AgencyAmount = AgencyAmount;
	}

	public String getBusinessID() {
		return BusinessID;
	}

	public void setBusinessID(String BusinessID) {
		this.BusinessID = BusinessID;
	}

	public BigDecimal getBusinessRate() {
		return BusinessRate;
	}

	public void setBusinessRate(BigDecimal BusinessRate) {
		this.BusinessRate = BusinessRate;
	}

	public BigDecimal getBusinessAmount() {
		return BusinessAmount;
	}

	public void setBusinessAmount(BigDecimal BusinessAmount) {
		this.BusinessAmount = BusinessAmount;
	}

	public BigDecimal getFreedRate() {
		return FreedRate;
	}

	public void setFreedRate(BigDecimal FreedRate) {
		this.FreedRate = FreedRate;
	}

	public BigDecimal getBorrowerRate() {
		return BorrowerRate;
	}

	public void setBorrowerRate(BigDecimal BorrowerRate) {
		this.BorrowerRate = BorrowerRate;
	}

	public Integer getProjectFrom() {
		return ProjectFrom;
	}

	public void setProjectFrom(Integer ProjectFrom) {
		this.ProjectFrom = ProjectFrom;
	}

	public String getFundUse() {
		return FundUse;
	}

	public void setFundUse(String FundUse) {
		this.FundUse = FundUse;
	}

	public String getRepaymentAssure() {
		return RepaymentAssure;
	}

	public void setRepaymentAssure(String RepaymentAssure) {
		this.RepaymentAssure = RepaymentAssure;
	}

	public String getExt() {
		return Ext;
	}

	public void setExt(String Ext) {
		this.Ext = Ext;
	}

	public BigDecimal getMonthPrincipalAmount() {
		return MonthPrincipalAmount;
	}

	public void setMonthPrincipalAmount(BigDecimal MonthPrincipalAmount) {
		this.MonthPrincipalAmount = MonthPrincipalAmount;
	}

	public Integer getQueryProjectStatus() {
		return queryProjectStatus;
	}

	public void setQueryProjectStatus(Integer queryProjectStatus) {
		this.queryProjectStatus = queryProjectStatus;
	}

	public String getQueryStatusDesc() {
		return queryStatusDesc;
	}

	public void setQueryStatusDesc(String queryStatusDesc) {
		this.queryStatusDesc = queryStatusDesc;
	}

	public BigDecimal getQueryCastedAmount() {
		return queryCastedAmount;
	}

	public void setQueryCastedAmount(BigDecimal queryCastedAmount) {
		this.queryCastedAmount = queryCastedAmount;
	}

	public Date getQueryAuditDate() {
		return queryAuditDate;
	}

	public void setQueryAuditDate(Date queryAuditDate) {
		this.queryAuditDate = queryAuditDate;
	}

	public String getQueryResultContent() {
		return queryResultContent;
	}

	public void setQueryResultContent(String queryResultContent) {
		this.queryResultContent = queryResultContent;
	}

	public Date getQueryFullsuccessDate() {
		return queryFullsuccessDate;
	}

	public void setQueryFullsuccessDate(Date queryFullsuccessDate) {
		this.queryFullsuccessDate = queryFullsuccessDate;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getOriginPlace() {
		return OriginPlace;
	}

	public void setOriginPlace(String OriginPlace) {
		this.OriginPlace = OriginPlace;
	}

	public String getMonthlyIncome() {
		return MonthlyIncome;
	}

	public void setMonthlyIncome(String MonthlyIncome) {
		this.MonthlyIncome = MonthlyIncome;
	}

	public String getOtherIncome() {
		return OtherIncome;
	}

	public void setOtherIncome(String OtherIncome) {
		this.OtherIncome = OtherIncome;
	}

	public String getHousing() {
		return Housing;
	}

	public void setHousing(String Housing) {
		this.Housing = Housing;
	}

	public String getOfficeDomain() {
		return OfficeDomain;
	}

	public void setOfficeDomain(String OfficeDomain) {
		this.OfficeDomain = OfficeDomain;
	}

	public String getEducation() {
		return Education;
	}

	public void setEducation(String Education) {
		this.Education = Education;
	}

	@Override
	protected Serializable pkVal() {
		return this.IssueId;
	}

	@Override
	public String toString() {
		return "Issue{" +
			", IssueId=" + IssueId +
			", NickName=" + NickName +
			", TelNo=" + TelNo +
			", Email=" + Email +
			", IdentityCard=" + IdentityCard +
			", RealName=" + RealName +
			", BankAccountNo=" + BankAccountNo +
			", BankType=" + BankType +
			", BankProvice=" + BankProvice +
			", BankCity=" + BankCity +
			", OpenBankName=" + OpenBankName +
			", Title=" + Title +
			", PeriodMonth=" + PeriodMonth +
			", RepaymentType=" + RepaymentType +
			", Amount=" + Amount +
			", LowerUnit=" + LowerUnit +
			", BranchCompanyId=" + BranchCompanyId +
			", ControlDesc=" + ControlDesc +
			", ImageUrl=" + ImageUrl +
			", TitleImageId=" + TitleImageId +
			", Remark=" + Remark +
			", CreateUser=" + CreateUser +
			", CreateTime=" + CreateTime +
			", UpdateUser=" + UpdateUser +
			", UpdateTime=" + UpdateTime +
			", Status=" + Status +
			", ProjectType=" + ProjectType +
			", ResultContent=" + ResultContent +
			", EnterpriseUserId=" + EnterpriseUserId +
			", AviCreditGrantingAmount=" + AviCreditGrantingAmount +
			", InterestRate=" + InterestRate +
			", OverRate=" + OverRate +
			", Sex=" + Sex +
			", CredTypeId=" + CredTypeId +
			", Birthday=" + Birthday +
			", RiskAssessment=" + RiskAssessment +
			", UserId=" + UserId +
			", UserTypeId=" + UserTypeId +
			", Marriage=" + Marriage +
			", Address=" + Address +
			", IsHaveHouse=" + IsHaveHouse +
			", IsHaveCar=" + IsHaveCar +
			", TuandaiRate=" + TuandaiRate +
			", GuaranteeRate=" + GuaranteeRate +
			", SubCompanyRate=" + SubCompanyRate +
			", TuandaiAmount=" + TuandaiAmount +
			", GuaranteeAmount=" + GuaranteeAmount +
			", SubCompanyCharge=" + SubCompanyCharge +
			", DepositAmount=" + DepositAmount +
			", FreedAmount=" + FreedAmount +
			", BorrowAmount=" + BorrowAmount +
			", CreditorId=" + CreditorId +
			", IsBailorWithdraw=" + IsBailorWithdraw +
			", PayType=" + PayType +
			", AgencyId=" + AgencyId +
			", AgencyRate=" + AgencyRate +
			", AgencyAmount=" + AgencyAmount +
			", BusinessID=" + BusinessID +
			", BusinessRate=" + BusinessRate +
			", BusinessAmount=" + BusinessAmount +
			", FreedRate=" + FreedRate +
			", BorrowerRate=" + BorrowerRate +
			", ProjectFrom=" + ProjectFrom +
			", FundUse=" + FundUse +
			", RepaymentAssure=" + RepaymentAssure +
			", Ext=" + Ext +
			", MonthPrincipalAmount=" + MonthPrincipalAmount +
			", queryProjectStatus=" + queryProjectStatus +
			", queryStatusDesc=" + queryStatusDesc +
			", queryCastedAmount=" + queryCastedAmount +
			", queryAuditDate=" + queryAuditDate +
			", queryResultContent=" + queryResultContent +
			", queryFullsuccessDate=" + queryFullsuccessDate +
			", platformType=" + platformType +
			", requestNo=" + requestNo +
			", OriginPlace=" + OriginPlace +
			", MonthlyIncome=" + MonthlyIncome +
			", OtherIncome=" + OtherIncome +
			", Housing=" + Housing +
			", OfficeDomain=" + OfficeDomain +
			", Education=" + Education +
			"}";
	}
}
