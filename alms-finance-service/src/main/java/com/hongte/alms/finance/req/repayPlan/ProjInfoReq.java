package com.hongte.alms.finance.req.repayPlan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("上标信息")
public class ProjInfoReq {


    /**
     * 标的车辆信息
     */
    @ApiModelProperty(value = "标的车辆信息")
    private  List<ProjectCarInfoReq> projCarInfos;

    /**
     * 标的房源信息
     */
    @ApiModelProperty(value = "标的房源信息")
    private  List<ProjectHouseInfoReq> projHouseInfos;


    /**
     * 标的费用信息列表
     */
    @ApiModelProperty(required= true,value = "标的的出款费用信息列表")
    private List<ProjFeeReq> projFeeInfos;

    /**
     * 项目编号
     */
    @ApiModelProperty(required= true,value = "项目编号")
    private String projectId;
//    /**
//     * 业务编号
//     */
//    @ApiModelProperty(required= true,value = "业务编号")
//    private String businessId;
    /**
     * 上标状态(-100:业务员出款申请,-50:财务未上标,0:财务不同意上标,1:财务同意上标,2:已满标,3:财务确认出款计划可放款,4:团贷网已提现给借款人,5:提现失败,6:展期资金分配完成,7:部分提现给借款人)
     */
    @ApiModelProperty(required= true,value = "上标状态(-100:业务员出款申请,-50:财务未上标,0:财务不同意上标,1:财务同意上标,2:已满标,3:财务确认出款计划可放款,4:团贷网已提现给借款人,5:提现失败,6:展期资金分配完成,7:部分提现给借款人)")
    private String statusFlag;
    /**
     * 启标时间(用于生成还款计划)
     */
    @ApiModelProperty(required= true,value = "启标时间(用于生成还款计划)")
    private Date beginTime;
    /**
     * 满标金额(元)
     */
    @ApiModelProperty(required= true,value = "满标金额(元)")
    private BigDecimal fullBorrowMoney;

    /**
     * 财务确认放款用户编号
     */
    @ApiModelProperty(required= true,value = "财务确认放款用户编号")
    private String accounterConfirmUserId;
    /**
     * 财务确认放款人名称
     */
    @ApiModelProperty(required= true,value = "财务确认放款人名称")
    private String accounterConfirmUserName;
    /**
     * 放款时间
     */
    @ApiModelProperty(required= true,value = "团贷网放款时间")
    private Date tdLoanTime;
    /**
     * 放款金额
     */
    @ApiModelProperty(required= true,value = "团贷网放款金额")
    private BigDecimal tdLoanMoney;
    /**
     * 是否是展期(0:不是展期,1:是展期)
     */
    @ApiModelProperty(required= true,value = "是否是展期(0:不是展期,1:是展期)")
    private Integer extendFlag;
    /**
     * 投资者已投金额
     */
    @ApiModelProperty(required= true,value = "投资者已投金额")
    private BigDecimal catsedAmount;
    /**
     * 原业务上标编号
     */
    @ApiModelProperty(value = "原业务上标编号")
    private String orgIssueId;
    /**
     * 主借标ID
     */
    @ApiModelProperty(required= true,value = "主借标ID")
    private String masterIssueId;
    /**
     * 超额拆标共借项目的序号
     */
    @ApiModelProperty(value = "超额拆标共借项目的序号")
    private Integer issueOrder;
    /**
     * 还款计划guid,只适合房贷
     */
    @ApiModelProperty(required= true,value = "还款计划guid,只适合房贷")
    private String businessAfterGuid;
    /**
     * 满标时间(标的状态查询接口)
     */
    @ApiModelProperty(required= true,value = "满标时间(标的状态查询接口)")
    private Date queryFullsuccessDate;
    /**
     * 昵称
     */
    @ApiModelProperty(required= true,value = "昵称")
    private String nickName;
    /**
     * 手机号码
     */
    @ApiModelProperty(required= true,value = "手机号码")
    private String TelNo;
    /**
     * 邮箱
     */
    @ApiModelProperty(required= true,value = "邮箱")
    private String Email;
    /**
     * 身份证号码
     */
    @ApiModelProperty(required= true,value = "身份证号码")
    private String identityCard;
    /**
     * 真实姓名
     */
    @ApiModelProperty(required= true,value = "真实姓名")
    private String realName;
    /**
     * 银行卡
     */
    @ApiModelProperty(required= true,value = "银行卡")
    private String bankAccountNo;
    /**
     * 银行类型
     */
    @ApiModelProperty(required= true,value = "银行类型")
    private Integer bankType;
    /**
     * 银行卡归属地省
     */
    @ApiModelProperty(required= true,value = "银行卡归属地省")
    private String bankProvice;
    /**
     * 银行卡归属地市
     */
    @ApiModelProperty(required= true,value = "银行卡归属地市")
    private String bankCity;
    /**
     * 开户银行名称
     */
    @ApiModelProperty(required= true,value = "开户银行名称")
    private String openBankName;
    /**
     * 标题
     */
    @ApiModelProperty(required= true,value = "标题")
    private String title;
    /**
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    private Integer periodMonth;
    /**
     * 平台还款方式ID(到期还本息用1表示， 每月付息到期还本用2表示)
     */
    @ApiModelProperty(required= true,value = "平台还款方式ID(到期还本息用1表示， 每月付息到期还本用2表示)")
    private Integer repaymentType;
    /**
     * 总金额(元)
     */
    @ApiModelProperty(required= true,value = "总金额(元)")
    private BigDecimal amount;
    /**
     * 最小投资单位(元)
     */
    @ApiModelProperty(required= true,value = "最小投资单位(元)")
    private BigDecimal lowerUnit;
    /**
     * 标的来源(所属分公司的分润用户ID)
     */
    @ApiModelProperty(required= true,value = "标的来源(所属分公司的分润用户ID)")
    private String branchCompanyId;
    /**
     * 风险控制措施
     */
    @ApiModelProperty(required= true,value = "风险控制措施")
    private String controlDesc;
    /**
     * 标题图片
     */
    @ApiModelProperty(required= true,value = "标题图片")
    private String imageUrl;
    /**
     * 标题图片编号
     */
    @ApiModelProperty(required= true,value = "标题图片编号")
    private String titleImageId;
    /**
     * 备注
     */
    @ApiModelProperty(required= true,value = "备注")
    private String remark;
    /**
     * 上标状态(0:暂存 1:待审 2:审核成功 3:审核失败,4:待上标)
     */
    @ApiModelProperty(required= true,value = "上标状态(0:暂存 1:待审 2:审核成功 3:审核失败,4:待上标)")
    private Integer tdStatus;
//    /**
//     * 业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)
//     */
//    @ApiModelProperty(required= true,value = "业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)")
//    private Integer projectType;
    /**
     * 上标结果
     */
    @ApiModelProperty(required= true,value = "上标结果")
    private String resultContent;
    /**
     * 担保方编号(所属担保公司分润用户ID)
     */
    @ApiModelProperty(required= true,value = "担保方编号(所属担保公司分润用户ID)")
    private String enterpriseUserId;
    /**
     * 担保公司可用金额
     */
    @ApiModelProperty(required= true,value = "担保公司可用金额")
    private BigDecimal aviCreditGrantingAmount;
//    /**
//     * 年化利率
//     */
//    @ApiModelProperty(required= true,value = "年化利率")
//    private BigDecimal interestRate;


    /**
     *利率
     */
    @ApiModelProperty(required= true,value = "利率")
    private BigDecimal rate;

    @ApiModelProperty(required= true,value = "利率单位：1 年利率; 2 月利率; 3 日利率")
    private Integer rateUnitType;

    @ApiModelProperty(required= true,value = "逾期滞纳金费率(%)")
    private BigDecimal overDueRate;

    @ApiModelProperty(required= true,value = "逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率")
    private  Integer overDueRateUnit;


    @ApiModelProperty(required= true,value = "还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息")
    private Integer repayType;


    /**
     * 性别
     */
    @ApiModelProperty(required= true,value = "性别")
    private Integer sex;
    /**
     * 证件类型
     */
    @ApiModelProperty(required= true,value = "证件类型")
    private Integer credTypeId;
    /**
     * 生日
     */
    @ApiModelProperty(required= true,value = "生日")
    private Date birthday;
    /**
     * 风险评估意见
     */
    @ApiModelProperty(required= true,value = "风险评估意见")
    private String riskAssessment;
    /**
     * 资金平台用户ID
     */
    @ApiModelProperty(required= true,value = "资金平台用户ID")
    private String plateUserId;
    /**
     * 客户类型 1:个人 2:企业
     */
    @ApiModelProperty(required= true,value = "客户类型 1:个人 2:企业")
    private Integer userTypeId;
    /**
     * 婚姻状况, 已婚、未婚 (信用贷时必填)
     */
    @ApiModelProperty(value = "婚姻状况, 已婚、未婚 (信用贷时必填)")
    private String marriage;
    /**
     * 居住地址,详细地址，包括省份城市 (信用贷时必填)
     */
    @ApiModelProperty(value = "居住地址,详细地址，包括省份城市 (信用贷时必填)")
    private String address;
    /**
     * 是否有房产
     */
    @ApiModelProperty(required= true,value = "是否有房产")
    private Integer isHaveHouse;
    /**
     * 是否有车产
     */
    @ApiModelProperty(required= true,value = "是否有车产")
    private Integer isHaveCar;
//    /**
//     * 团贷比例(期初收取平台费上标比例)
//     */
//    @ApiModelProperty(required= true,value = "团贷比例(期初收取平台费上标比例)")
//    private BigDecimal tuandaiRate;
//    /**
//     * 团贷预计佣金(期初收取平台费总金额)
//     */
//    @ApiModelProperty(required= true,value = "团贷预计佣金(期初收取平台费总金额)")
//    private BigDecimal tuandaiAmount;
//    /**
//     * 担保比例(期初收取担保公司费比例)
//     */
//    @ApiModelProperty(required= true,value = "担保比例(期初收取担保公司费比例)")
//    private BigDecimal guaranteeRate;
//    /**
//     * 担保预计收入(期初收取担保公司费用总金额)
//     */
//    @TableField("guarantee_amount")
//    @ApiModelProperty(required= true,value = "担保预计收入(期初收取担保公司费用总金额)")
//    private BigDecimal guaranteeAmount;
//    /**
//     * 分公司比例(期初收取分公司费用比例)
//     */
//    @TableField("sub_company_rate")
//    @ApiModelProperty(required= true,value = "分公司比例(期初收取分公司费用比例)")
//    private BigDecimal subCompanyRate;
//    /**
//     * 分公司预计金额(期初收取分公司费用总金额)
//     */
//    @ApiModelProperty(required= true,value = "分公司预计金额(期初收取分公司费用总金额)")
//    private BigDecimal subCompanyCharge;
    /**
     * 中介Id或担保
     */
    @ApiModelProperty(required= true,value = "中介Id或担保")
    private String agencyId;
//    /**
//     * 中介比例
//     */
//    @ApiModelProperty(required= true,value = "中介比例")
//    private BigDecimal agencyRate;
//    /**
//     * 中介金额
//     */
//    @ApiModelProperty(required= true,value = "中介金额")
//    private BigDecimal agencyAmount;
//    /**
//     * 保证金金额
//     */
//    @ApiModelProperty(required= true,value = "保证金金额")
//    private BigDecimal depositAmount;
//    /**
//     * 押金
//     */
//    @ApiModelProperty(required= true,value = "押金")
//    private BigDecimal freedAmount;
//    /**
//     * 押金费率
//     */
//    @ApiModelProperty(required= true,value = "押金费率")
//    private BigDecimal freedRate;
    /**
     * 合作公司所属团贷网分公司编号
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司编号")
    private String cooperativeTdComUserId;
//    /**
//     * 合作公司所属团贷网分公司费用比例
//     */
//    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司费用比例")
//    private BigDecimal cooperativeTdComRate;
//    /**
//     * 合作公司所属团贷网分公司金额
//     */
//    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司金额")
//    private BigDecimal cooperativeTdComAmount;
    /**
     * 借款人所得比例
     */
    @ApiModelProperty(required= true,value = "借款人所得比例")
    private BigDecimal borrowerRate;
    /**
     * 借款人实际金额
     */
    @ApiModelProperty(required= true,value = "借款人实际金额")
    private BigDecimal borrowAmount;
    /**
     * 抵押权人(委托人)的团贷用户ID
     */
    @ApiModelProperty(required= true,value = "抵押权人(委托人)的团贷用户ID")
    private String creditorId;
    /**
     * 是否委托人提现
     */
    @ApiModelProperty(required= true,value = "是否委托人提现")
    private Boolean isBailorWithdraw;
    /**
     * 支付类型  null 或者1都是 宝付提现   2是存管提现
     */
    @ApiModelProperty(required= true,value = "支付类型  null 或者1都是 宝付提现   2是存管提现")
    private Integer payType;
    /**
     * 标的来源0小贷系统 1一点车贷
     */
    @ApiModelProperty(required= true,value = "标的来源0小贷系统 1一点车贷")
    private Integer projectFrom;
    /**
     * 资金用途 (指资金流向信息、使用信息及计划等资金运用情况)
     */
    @ApiModelProperty(required= true,value = "资金用途 (指资金流向信息、使用信息及计划等资金运用情况)")
    private String fundUse;
    /**
     * 还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)
     */
    @ApiModelProperty(required= true,value = "还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)")
    private String repaymentAssure;
    /**
     * 个人信息扩展字段
     */
    @ApiModelProperty(required= true,value = "个人信息扩展字段")
    private String Ext;
//    /**
//     * 每月还本金额
//     */
//    @ApiModelProperty(required= true,value = "每月还本金额")
//    private BigDecimal monthPrincipalAmount;
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
     * 创建日期
     */
    @ApiModelProperty(required= true,value = "创建日期")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(required= true,value = "创建人")
    private String createUser;
    /**
     * 更新日期
     */
    @ApiModelProperty(required= true,value = "更新日期")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(required= true,value = "更新人")
    private String updateUser;

    public List<ProjectCarInfoReq> getProjCarInfos() {
        return projCarInfos;
    }

    public void setProjCarInfos(List<ProjectCarInfoReq> projCarInfos) {
        this.projCarInfos = projCarInfos;
    }

    public List<ProjectHouseInfoReq> getProjHouseInfos() {
        return projHouseInfos;
    }

    public void setProjHouseInfos(List<ProjectHouseInfoReq> projHouseInfos) {
        this.projHouseInfos = projHouseInfos;
    }



    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

//    public String getBusinessId() {
//        return businessId;
//    }
//
//    public void setBusinessId(String businessId) {
//        this.businessId = businessId;
//    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public BigDecimal getFullBorrowMoney() {
        return fullBorrowMoney;
    }

    public void setFullBorrowMoney(BigDecimal fullBorrowMoney) {
        this.fullBorrowMoney = fullBorrowMoney;
    }

    public String getAccounterConfirmUserId() {
        return accounterConfirmUserId;
    }

    public void setAccounterConfirmUserId(String accounterConfirmUserId) {
        this.accounterConfirmUserId = accounterConfirmUserId;
    }

    public String getAccounterConfirmUserName() {
        return accounterConfirmUserName;
    }

    public void setAccounterConfirmUserName(String accounterConfirmUserName) {
        this.accounterConfirmUserName = accounterConfirmUserName;
    }

    public Date getTdLoanTime() {
        return tdLoanTime;
    }

    public void setTdLoanTime(Date tdLoanTime) {
        this.tdLoanTime = tdLoanTime;
    }

    public BigDecimal getTdLoanMoney() {
        return tdLoanMoney;
    }

    public void setTdLoanMoney(BigDecimal tdLoanMoney) {
        this.tdLoanMoney = tdLoanMoney;
    }

    public Integer getExtendFlag() {
        return extendFlag;
    }

    public void setExtendFlag(Integer extendFlag) {
        this.extendFlag = extendFlag;
    }

    public BigDecimal getCatsedAmount() {
        return catsedAmount;
    }

    public void setCatsedAmount(BigDecimal catsedAmount) {
        this.catsedAmount = catsedAmount;
    }

    public String getOrgIssueId() {
        return orgIssueId;
    }

    public void setOrgIssueId(String orgIssueId) {
        this.orgIssueId = orgIssueId;
    }

    public String getMasterIssueId() {
        return masterIssueId;
    }

    public void setMasterIssueId(String masterIssueId) {
        this.masterIssueId = masterIssueId;
    }

    public Integer getIssueOrder() {
        return issueOrder;
    }

    public void setIssueOrder(Integer issueOrder) {
        this.issueOrder = issueOrder;
    }

    public String getBusinessAfterGuid() {
        return businessAfterGuid;
    }

    public void setBusinessAfterGuid(String businessAfterGuid) {
        this.businessAfterGuid = businessAfterGuid;
    }

    public Date getQueryFullsuccessDate() {
        return queryFullsuccessDate;
    }

    public void setQueryFullsuccessDate(Date queryFullsuccessDate) {
        this.queryFullsuccessDate = queryFullsuccessDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    public String getBankProvice() {
        return bankProvice;
    }

    public void setBankProvice(String bankProvice) {
        this.bankProvice = bankProvice;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLowerUnit() {
        return lowerUnit;
    }

    public void setLowerUnit(BigDecimal lowerUnit) {
        this.lowerUnit = lowerUnit;
    }

    public String getBranchCompanyId() {
        return branchCompanyId;
    }

    public void setBranchCompanyId(String branchCompanyId) {
        this.branchCompanyId = branchCompanyId;
    }

    public String getControlDesc() {
        return controlDesc;
    }

    public void setControlDesc(String controlDesc) {
        this.controlDesc = controlDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitleImageId() {
        return titleImageId;
    }

    public void setTitleImageId(String titleImageId) {
        this.titleImageId = titleImageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTdStatus() {
        return tdStatus;
    }

    public void setTdStatus(Integer tdStatus) {
        this.tdStatus = tdStatus;
    }

//    public Integer getProjectType() {
//        return projectType;
//    }
//
//    public void setProjectType(Integer projectType) {
//        this.projectType = projectType;
//    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getEnterpriseUserId() {
        return enterpriseUserId;
    }

    public void setEnterpriseUserId(String enterpriseUserId) {
        this.enterpriseUserId = enterpriseUserId;
    }

    public BigDecimal getAviCreditGrantingAmount() {
        return aviCreditGrantingAmount;
    }

    public void setAviCreditGrantingAmount(BigDecimal aviCreditGrantingAmount) {
        this.aviCreditGrantingAmount = aviCreditGrantingAmount;
    }


    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getCredTypeId() {
        return credTypeId;
    }

    public void setCredTypeId(Integer credTypeId) {
        this.credTypeId = credTypeId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getPlateUserId() {
        return plateUserId;
    }

    public void setPlateUserId(String plateUserId) {
        this.plateUserId = plateUserId;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsHaveHouse() {
        return isHaveHouse;
    }

    public void setIsHaveHouse(Integer isHaveHouse) {
        this.isHaveHouse = isHaveHouse;
    }

    public Integer getIsHaveCar() {
        return isHaveCar;
    }

    public void setIsHaveCar(Integer isHaveCar) {
        this.isHaveCar = isHaveCar;
    }
//
//    public BigDecimal getTuandaiRate() {
//        return tuandaiRate;
//    }
//
//    public void setTuandaiRate(BigDecimal tuandaiRate) {
//        this.tuandaiRate = tuandaiRate;
//    }
//
//    public BigDecimal getTuandaiAmount() {
//        return tuandaiAmount;
//    }
//
//    public void setTuandaiAmount(BigDecimal tuandaiAmount) {
//        this.tuandaiAmount = tuandaiAmount;
//    }
//
//    public BigDecimal getGuaranteeRate() {
//        return guaranteeRate;
//    }
//
//    public void setGuaranteeRate(BigDecimal guaranteeRate) {
//        this.guaranteeRate = guaranteeRate;
//    }
//
//    public BigDecimal getGuaranteeAmount() {
//        return guaranteeAmount;
//    }
//
//    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
//        this.guaranteeAmount = guaranteeAmount;
//    }
//
//    public BigDecimal getSubCompanyRate() {
//        return subCompanyRate;
//    }
//
//    public void setSubCompanyRate(BigDecimal subCompanyRate) {
//        this.subCompanyRate = subCompanyRate;
//    }
//
//    public BigDecimal getSubCompanyCharge() {
//        return subCompanyCharge;
//    }
//
//    public void setSubCompanyCharge(BigDecimal subCompanyCharge) {
//        this.subCompanyCharge = subCompanyCharge;
//    }
//
//    public String getAgencyId() {
//        return agencyId;
//    }
//
//    public void setAgencyId(String agencyId) {
//        this.agencyId = agencyId;
//    }
//
//    public BigDecimal getAgencyRate() {
//        return agencyRate;
//    }
//
//    public void setAgencyRate(BigDecimal agencyRate) {
//        this.agencyRate = agencyRate;
//    }
//
//    public BigDecimal getAgencyAmount() {
//        return agencyAmount;
//    }
//
//    public void setAgencyAmount(BigDecimal agencyAmount) {
//        this.agencyAmount = agencyAmount;
//    }
//
//    public BigDecimal getDepositAmount() {
//        return depositAmount;
//    }
//
//    public void setDepositAmount(BigDecimal depositAmount) {
//        this.depositAmount = depositAmount;
//    }
//
//    public BigDecimal getFreedAmount() {
//        return freedAmount;
//    }
//
//    public void setFreedAmount(BigDecimal freedAmount) {
//        this.freedAmount = freedAmount;
//    }
//
//    public BigDecimal getFreedRate() {
//        return freedRate;
//    }
//
//    public void setFreedRate(BigDecimal freedRate) {
//        this.freedRate = freedRate;
//    }

    public String getCooperativeTdComUserId() {
        return cooperativeTdComUserId;
    }

    public void setCooperativeTdComUserId(String cooperativeTdComUserId) {
        this.cooperativeTdComUserId = cooperativeTdComUserId;
    }

//    public BigDecimal getCooperativeTdComRate() {
//        return cooperativeTdComRate;
//    }
//
//    public void setCooperativeTdComRate(BigDecimal cooperativeTdComRate) {
//        this.cooperativeTdComRate = cooperativeTdComRate;
//    }
//
//    public BigDecimal getCooperativeTdComAmount() {
//        return cooperativeTdComAmount;
//    }
//
//    public void setCooperativeTdComAmount(BigDecimal cooperativeTdComAmount) {
//        this.cooperativeTdComAmount = cooperativeTdComAmount;
//    }

    public BigDecimal getBorrowerRate() {
        return borrowerRate;
    }

    public void setBorrowerRate(BigDecimal borrowerRate) {
        this.borrowerRate = borrowerRate;
    }

    public BigDecimal getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(BigDecimal borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(String creditorId) {
        this.creditorId = creditorId;
    }

    public Boolean getBailorWithdraw() {
        return isBailorWithdraw;
    }

    public void setBailorWithdraw(Boolean bailorWithdraw) {
        isBailorWithdraw = bailorWithdraw;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getProjectFrom() {
        return projectFrom;
    }

    public void setProjectFrom(Integer projectFrom) {
        this.projectFrom = projectFrom;
    }

    public String getFundUse() {
        return fundUse;
    }

    public void setFundUse(String fundUse) {
        this.fundUse = fundUse;
    }

    public String getRepaymentAssure() {
        return repaymentAssure;
    }

    public void setRepaymentAssure(String repaymentAssure) {
        this.repaymentAssure = repaymentAssure;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }

//    public BigDecimal getMonthPrincipalAmount() {
//        return monthPrincipalAmount;
//    }
//
//    public void setMonthPrincipalAmount(BigDecimal monthPrincipalAmount) {
//        this.monthPrincipalAmount = monthPrincipalAmount;
//    }

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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getRateUnitType() {
        return rateUnitType;
    }

    public void setRateUnitType(Integer rateUnitType) {
        this.rateUnitType = rateUnitType;
    }

    public Integer getRepayType() {
        return repayType;
    }

    public void setRepayType(Integer repayType) {
        this.repayType = repayType;
    }

    public BigDecimal getOverDueRate() {
        return overDueRate;
    }

    public void setOverDueRate(BigDecimal overDueRate) {
        this.overDueRate = overDueRate;
    }

    public Integer getOverDueRateUnit() {
        return overDueRateUnit;
    }

    public void setOverDueRateUnit(Integer overDueRateUnit) {
        this.overDueRateUnit = overDueRateUnit;
    }

    public List<ProjFeeReq> getProjFeeInfos() {
        return projFeeInfos;
    }

    public void setProjFeeInfos(List<ProjFeeReq> projFeeInfos) {
        this.projFeeInfos = projFeeInfos;
    }
}
