package com.hongte.alms.base.RepayPlan.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @NotNull(message="的费用信息列表(projFeeInfos)不能为空")
    @Valid
    private List<ProjFeeReq> projFeeInfos;



    @ApiModelProperty(value = "标的的额外费用信息列表")
    private  List<ProjExtRateReq> projExtRateReqs;

    //标的额外费用，按feeId分好分类
    private Map<String,List<ProjExtRateReq>> projExtRateReqMap;

    /**
     *利率
     */
    @ApiModelProperty(required= true,value = "利率")
    @NotNull(message="ProjInfoReq 利率(rate)不能为空")
    private BigDecimal rate;

    @ApiModelProperty(required= true,value = "利率单位：1 年利率; 2 月利率; 3 日利率")
    @NotNull(message="ProjInfoReq 利率单位(rateUnitType)不能为空")
    private Integer rateUnitType;


    @ApiModelProperty(required= true,value = "线下期内逾期滞纳金费率(%)")
    @NotNull(message="ProjInfoReq 线下期内逾期滞纳金费率(offLineInOverDueRate)不能为空")
    private BigDecimal offLineInOverDueRate;

    @ApiModelProperty(required= true,value = "线下期内逾期滞纳金费率类型，1：按标的总借款金额乘以比例每天 \n" +
            "2：按标的剩余本金乘以比例每天 \n" +
            "3：按固定金额每天")
    @NotNull(message="ProjInfoReq 线下期内逾期滞纳金费率类型(offLineInOverDueRateType)不能为空")
    private  Integer offLineInOverDueRateType;

    @ApiModelProperty(required= true,value = "线下期外逾期滞纳金费率(%)")
    @NotNull(message="ProjInfoReq 线下期外逾期滞纳金费率(offLineOutOverDueRate)不能为空")
    private BigDecimal offLineOutOverDueRate;

    @ApiModelProperty(required= true,value = "线下期外逾期滞纳金费率类型，1：按标的总借款金额乘以比例每天 \n" +
            "2：按标的剩余本金乘以比例每天 \n" +
            "3：按固定金额每天")
    @NotNull(message="ProjInfoReq 线下期外逾期滞纳金费率类型(offLineOutOverDueRateType)不能为空")
    private  Integer offLineOutOverDueRateType;

    @ApiModelProperty(required= true,value = "线上逾期滞纳金费率(%)")
    @NotNull(message="ProjInfoReq 线上逾期滞纳金费率(onLineOverDueRate)不能为空")
    private BigDecimal onLineOverDueRate;

    @ApiModelProperty(required= true,value = "线上逾期滞纳金费率类型，1：按标的总借款金额乘以比例每天 \n" +
            "2：按标的剩余本金乘以比例每天 \n" +
            "3：按固定金额每天\n" +
            "4：按标的当前应还本息乘以比例每天\n" +
            "5：按标的垫付本息乘以比例每天")
    @NotNull(message="ProjInfoReq 线上逾期滞纳金费率类型(onLineOverDueRateType)不能为空")
    private  Integer onLineOverDueRateType;


    @ApiModelProperty(required= true,value = "还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息")
    @NotNull(message="ProjInfoReq 还款方式(repayType)不能为空")
    private Integer repayType;

    @ApiModelProperty(value = "每期还本列表  List<PrincipleReq> 当还款方式为“分期还本付息”时，必须填写此列表 ")
    private List<PrincipleReq> principleReqList;


    /**
     * 项目编号
     */
    @ApiModelProperty(required= true,value = "项目编号")
    @NotNull(message="ProjInfoReq 项目编号(projectId)不能为空")
    private String projectId;
//    /**
//     * 业务编号
//     */
//    @ApiModelProperty(required= true,value = "业务编号")
//    @NotNull(message="业务编号(businessId)不能为空")
//    private String businessId;
    /**
     * 资产端用户ID
     */
    @ApiModelProperty(required= true,value = "资产端用户ID")
    @NotNull(message="ProjInfoReq 资产端用户ID(customerId)不能为空")
    private String customerId;
    /**
     * 上标状态(-100:业务员出款申请,-50:财务未上标,0:财务不同意上标,1:财务同意上标,2:已满标,3:财务确认出款计划可放款,4:团贷网已提现给借款人,5:提现失败,6:展期资金分配完成,7:部分提现给借款人)
     */
    @ApiModelProperty(required= true,value = "上标状态(-100:业务员出款申请,-50:财务未上标,0:财务不同意上标,1:财务同意上标,2:已满标,3:财务确认出款计划可放款,4:团贷网已提现给借款人,5:提现失败,6:展期资金分配完成,7:部分提现给借款人)")
    @NotNull(message="ProjInfoReq 上标状态(statusFlag)不能为空")
    private String statusFlag;
    /**
     * 启标时间(用于生成还款计划)
     */
    @ApiModelProperty(required= true,value = "启标时间(用于生成还款计划)")
    @NotNull(message="ProjInfoReq 启标时间(beginTime)不能为空")
    private Date beginTime;
    /**
     * 满标金额(元)
     */
    @ApiModelProperty(required= true,value = "满标金额(元)")
    @NotNull(message="ProjInfoReq 满标金额(fullBorrowMoney)不能为空")
    private BigDecimal fullBorrowMoney;
    /**
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    @NotNull(message="ProjInfoReq 借款期限(borrowLimit)不能为空")
    private Integer borrowLimit;


    /**
     * 是否是展期(0:不是展期,1:是展期)
     */
    @ApiModelProperty(required= true,value = "是否是展期(0:不是展期,1:是展期)")
    @NotNull(message="ProjInfoReq 是否是展期(extendFlag)不能为空")
    private Integer extendFlag;
    /**
     * 展期标对应的原业务上标编号(仅展期业务)
     */
    @ApiModelProperty(value = "展期标对应的原业务上标编号(仅展期业务)")
    @NotNull(message="ProjInfoReq 展期标对应的原业务上标编号(orgIssueId)不能为空")
    private String orgIssueId;
    /**
     * 主借标ID
     */
    @ApiModelProperty(required= true,value = "主借标ID")
    @NotNull(message="ProjInfoReq 主借标ID(masterIssueId)不能为空")
    private String masterIssueId;
    /**
     * 超额拆标共借项目的序号
     */
    @ApiModelProperty(required= true,value = "超额拆标共借项目的序号")
    @NotNull(message="ProjInfoReq 超额拆标共借项目的序号(issueOrder)不能为空")
    private Integer issueOrder;

    /**
     * 满标时间
     */
    @ApiModelProperty(required= true,value = "满标时间")
    @NotNull(message="ProjInfoReq 满标时间(queryFullsuccessDate)不能为空")
    private Date queryFullsuccessDate;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 手机号码
     */
    @ApiModelProperty(required= true,value = "手机号码")
    @NotNull(message="ProjInfoReq 手机号码(telNo)不能为空")
    private String telNo;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 身份证号码
     */
    @ApiModelProperty(required= true,value = "身份证号码")
    @NotNull(message="ProjInfoReq 身份证号码(identityCard)不能为空")
    private String identityCard;
    /**
     * 真实姓名
     */
    @ApiModelProperty(required= true,value = "真实姓名")
    @NotNull(message="ProjInfoReq 真实姓名(realName)不能为空")
    private String realName;
    /**
     * 银行卡
     */
    @ApiModelProperty(required= true,value = "银行卡")
    @NotNull(message="ProjInfoReq 银行卡(bankAccountNo)不能为空")
    private String bankAccountNo;
    /**
     * 银行类型
     */
    @ApiModelProperty(required= true,value = "银行类型")
    @NotNull(message="ProjInfoReq 银行类型(bankType)不能为空")
    private Integer bankType;
    /**
     * 银行卡归属地省
     */
    @ApiModelProperty(required= true,value = "银行卡归属地省")
    @NotNull(message="ProjInfoReq 银行卡归属地省(bankProvice)不能为空")
    private String bankProvice;
    /**
     * 银行卡归属地市
     */
    @ApiModelProperty(required= true,value = "银行卡归属地市")
    @NotNull(message="ProjInfoReq 银行卡归属地市(bankCity)不能为空")
    private String bankCity;
    /**
     * 开户银行名称
     */
    @ApiModelProperty(required= true,value = "开户银行名称")
    @NotNull(message="ProjInfoReq 开户银行名称(openBankName)不能为空")
    private String openBankName;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    @NotNull(message="ProjInfoReq 借款期限(periodMonth)不能为空")
    private Integer periodMonth;
    /**
     * 平台还款方式ID(到期还本息用1表示， 每月付息到期还本用2表示)
     */
    @ApiModelProperty(value = "平台还款方式ID(到期还本息用1表示， 每月付息到期还本用2表示)")
    private Integer repaymentType;
    /**
     * 总金额(元)
     */
    @ApiModelProperty(required= true,value = "总金额(元)")
    @NotNull(message="ProjInfoReq 总金额(amount)不能为空")
    private BigDecimal amount;
    /**
     * 最小投资单位(元)
     */
    @ApiModelProperty(value = "最小投资单位(元)")
    private BigDecimal lowerUnit;
    /**
     * 标的来源(所属分公司的分润用户ID)
     */
    @ApiModelProperty(required= true,value = "标的来源(所属分公司的分润用户ID)")
    @NotNull(message="ProjInfoReq 标的来源(branchCompanyId)不能为空")
    private String branchCompanyId;
    /**
     * 风险控制措施
     */
    @ApiModelProperty(required= true,value = "风险控制措施")
    @NotNull(message="ProjInfoReq 风险控制措施(controlDesc)不能为空")
    private String controlDesc;
    /**
     * 标题图片
     */
    @ApiModelProperty(value = "标题图片")
    private String imageUrl;
    /**
     * 标题图片编号
     */
    @ApiModelProperty(value = "标题图片编号")
    private String titleImageId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 上标状态(0:暂存 1:待审 2:审核成功 3:审核失败,4:待上标)
     */
    @ApiModelProperty(required= true,value = "上标状态(0:暂存 1:待审 2:审核成功 3:审核失败,4:待上标)")
    @NotNull(message="ProjInfoReq 上标状态(tdStatus)不能为空")
    private Integer tdStatus;
    /**
     * 团贷网业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)
     */
    @ApiModelProperty(required= true,value = "团贷网业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)")
    @NotNull(message="ProjInfoReq 团贷网业务类型(projectType)不能为空")
    private Integer projectType;
    /**
     * 上标结果
     */
    @ApiModelProperty(value = "上标结果")
    private String resultContent;
    /**
     * 担保方编号(所属担保公司分润用户ID)
     */
    @ApiModelProperty(required= true,value = "担保方编号(所属担保公司分润用户ID)")
    @NotNull(message="ProjInfoReq 担保方编号(enterpriseUserId)不能为空")
    private String enterpriseUserId;
    /**
     * 担保公司可用金额
     */
    @ApiModelProperty(required= true,value = "担保公司可用金额")
    @NotNull(message="ProjInfoReq 担保公司可用金额(aviCreditGrantingAmount)不能为空")
    private BigDecimal aviCreditGrantingAmount;
//    /**
//     * 年化利率
//     */
//    @ApiModelProperty(required= true,value = "年化利率")
//    private BigDecimal interestRate;
    /**
     * 逾期年利率
     */
    @ApiModelProperty(required= true,value = "逾期年利率")
    @NotNull(message="ProjInfoReq 逾期年利率(overRate)不能为空")
    private BigDecimal overRate;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer sex;
    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型")
    private Integer credTypeId;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    /**
     * 风险评估意见
     */
    @ApiModelProperty(value = "风险评估意见")
    private String riskAssessment;
    /**
     * 团贷用户ID(资金端用户ID)
     */
    @ApiModelProperty(required= true,value = "团贷用户ID(资金端用户ID)")
    @NotNull(message="ProjInfoReq 团贷用户ID(tdUserId)不能为空")
    private String tdUserId;
    /**
     * 客户类型 1:个人 2:企业
     */
    @ApiModelProperty(required= true,value = "客户类型 1:个人 2:企业")
    @NotNull(message="ProjInfoReq 客户类型(userTypeId)不能为空")
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
    @ApiModelProperty(value = "是否有房产 1：有，0：无")
    private Integer isHaveHouse;
    /**
     * 是否有车产
     */
    @ApiModelProperty(value = "是否有车产 1：有， 0：无")
    private Integer isHaveCar;
    /**
     * 团贷比例(期初收取平台费上标比例)
     */
    @ApiModelProperty(required= true,value = "团贷比例(期初收取平台费上标比例)")
    @NotNull(message="ProjInfoReq 团贷比例(tuandaiRate)不能为空")
    private BigDecimal tuandaiRate;
    /**
     * 团贷预计佣金(期初收取平台费总金额)
     */
    @ApiModelProperty(required= true,value = "团贷预计佣金(期初收取平台费总金额)")
    @NotNull(message="ProjInfoReq 团贷预计佣金(tuandaiAmount)不能为空")
    private BigDecimal tuandaiAmount;
    /**
     * 担保比例(期初收取担保公司费比例)
     */
    @ApiModelProperty(required= true,value = "担保比例(期初收取担保公司费比例)")
    @NotNull(message="ProjInfoReq 担保比例(guaranteeRate)不能为空")
    private BigDecimal guaranteeRate;
    /**
     * 担保预计收入(期初收取担保公司费用总金额)
     */
    @ApiModelProperty(required= true,value = "担保预计收入(期初收取担保公司费用总金额)")
    @NotNull(message="ProjInfoReq 担保预计收入(guaranteeAmount)不能为空")
    private BigDecimal guaranteeAmount;
    /**
     * 分公司比例(期初收取分公司费用比例)
     */
    @ApiModelProperty(required= true,value = "分公司比例(期初收取分公司费用比例)")
    @NotNull(message="ProjInfoReq 分公司比例(subCompanyRate)不能为空")
    private BigDecimal subCompanyRate;
    /**
     * 分公司预计金额(期初收取分公司费用总金额)
     */
    @ApiModelProperty(required= true,value = "分公司预计金额(期初收取分公司费用总金额)")
    @NotNull(message="ProjInfoReq 分公司预计金额(subCompanyCharge)不能为空")
    private BigDecimal subCompanyCharge;
    /**
     * 中介Id或担保
     */
    @ApiModelProperty(required= true,value = "中介Id或担保")
    @NotNull(message="ProjInfoReq 中介Id或担保(agencyId)不能为空")
    private String agencyId;
    /**
     * 中介比例
     */
    @ApiModelProperty(required= true,value = "中介比例")
    @NotNull(message="ProjInfoReq 中介比例(agencyRate)不能为空")
    private BigDecimal agencyRate;
    /**
     * 中介金额
     */
    @ApiModelProperty(required= true,value = "中介金额")
    @NotNull(message="ProjInfoReq 中介金额(agencyAmount)不能为空")
    private BigDecimal agencyAmount;
    /**
     * 保证金金额
     */
    @ApiModelProperty(required= true,value = "保证金金额")
    @NotNull(message="ProjInfoReq 保证金金额(depositAmount)不能为空")
    private BigDecimal depositAmount;
    /**
     * 押金
     */
    @ApiModelProperty(required= true,value = "押金")
    @NotNull(message="ProjInfoReq 押金(freedAmount)不能为空")
    private BigDecimal freedAmount;
    /**
     * 押金费率
     */
    @ApiModelProperty(required= true,value = "押金费率")
    @NotNull(message="ProjInfoReq 押金费率(freedRate)不能为空")
    private BigDecimal freedRate;
    /**
     * 合作公司所属团贷网分公司编号
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司编号")
    @NotNull(message="ProjInfoReq 合作公司所属团贷网分公司编号(cooperativeTdComUserId)不能为空")
    private String cooperativeTdComUserId;
    /**
     * 合作公司所属团贷网分公司费用比例
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司费用比例")
    @NotNull(message="ProjInfoReq 合作公司所属团贷网分公司费用比例(cooperativeTdComRate)不能为空")
    private BigDecimal cooperativeTdComRate;
    /**
     * 合作公司所属团贷网分公司金额
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司金额")
    @NotNull(message="ProjInfoReq 合作公司所属团贷网分公司金额(cooperativeTdComAmount)不能为空")
    private BigDecimal cooperativeTdComAmount;
    /**
     * 借款人所得比例
     */
    @ApiModelProperty(required= true,value = "借款人所得比例")
    @NotNull(message="ProjInfoReq 借款人所得比例(borrowerRate)不能为空")
    private BigDecimal borrowerRate;
    /**
     * 借款人实际金额
     */
    @ApiModelProperty(required= true,value = "借款人实际金额")
    @NotNull(message="ProjInfoReq 借款人实际金额(borrowAmount)不能为空")
    private BigDecimal borrowAmount;
    /**
     * 抵押权人(委托人)的团贷用户ID
     */
    @ApiModelProperty(value = "抵押权人(委托人)的团贷用户ID")
    private String creditorId;
    /**
     * 是否委托人提现
     */
    @ApiModelProperty(value = "是否委托人提现")
    private Boolean isBailorWithdraw;

    /**
     * 标的来源0小贷系统 1一点车贷
     */
    @ApiModelProperty(required= true,value = "标的来源0小贷系统 1一点车贷")
    @NotNull(message="ProjInfoReq 标的来源(projectFrom)不能为空")
    private Integer projectFrom;
    /**
     * 资金用途 (指资金流向信息、使用信息及计划等资金运用情况)
     */
    @ApiModelProperty(value = "资金用途 (指资金流向信息、使用信息及计划等资金运用情况)")
    private String fundUse;
    /**
     * 还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)
     */
    @ApiModelProperty(value = "还款保障措施 (如：第一还款来源：xxx 第二还款来源：xxx)")
    private String repaymentAssure;
    /**
     * 个人信息扩展字段
     */
    @ApiModelProperty(value = "个人信息扩展字段")
    private String Ext;
    /**
     * 每月还本金额
     */
    @ApiModelProperty(required= true,value = "每月还本金额")
    @NotNull(message="ProjInfoReq 每月还本金额(monthPrincipalAmount)不能为空")
    private BigDecimal monthPrincipalAmount;


    /**
     * 审核时间(标的状态查询接口)
     */
    @ApiModelProperty(value = "审核时间(标的状态查询接口)")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryAuditDate;
    /**
     * 审核结果,标审核失败时这里有原因(标的状态查询接口)
     */
    @ApiModelProperty(value = "审核结果,标审核失败时这里有原因(标的状态查询接口)")
    private String queryResultContent;
    /**
     * 平台标志位：1，团贷网； 2，你我金融
     */
    @ApiModelProperty(required= true,value = "平台标志位：1，团贷网； 2，你我金融")
    @NotNull(message="ProjInfoReq 平台标志位(plateType)不能为空")
    private Integer plateType;
//    /**
//     * 创建日期
//     */
//    @ApiModelProperty(required= true,value = "创建日期")
//    private Date createTime;
//    /**
//     * 创建人
//     */
//    @ApiModelProperty(required= true,value = "创建人")
//    private String createUser;
//    /**
//     * 更新日期
//     */
//    @ApiModelProperty(required= true,value = "更新日期")
//    private Date updateTime;
//    /**
//     * 更新人
//     */
//    @ApiModelProperty(required= true,value = "更新人")
//    private String updateUser;

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


    public Integer getExtendFlag() {
        return extendFlag;
    }

    public void setExtendFlag(Integer extendFlag) {
        this.extendFlag = extendFlag;
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

//    public String getBusinessAfterGuid() {
//        return businessAfterGuid;
//    }
//
//    public void setBusinessAfterGuid(String businessAfterGuid) {
//        this.businessAfterGuid = businessAfterGuid;
//    }

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
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

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

//    public String getPlateUserId() {
//        return plateUserId;
//    }
//
//    public void setPlateUserId(String plateUserId) {
//        this.plateUserId = plateUserId;
//    }

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

//    public Integer getPayType() {
//        return payType;
//    }
//
//    public void setPayType(Integer payType) {
//        this.payType = payType;
//    }

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

//    public Integer getQueryProjectStatus() {
//        return queryProjectStatus;
//    }
//
//    public void setQueryProjectStatus(Integer queryProjectStatus) {
//        this.queryProjectStatus = queryProjectStatus;
//    }
//
//    public String getQueryStatusDesc() {
//        return queryStatusDesc;
//    }
//
//    public void setQueryStatusDesc(String queryStatusDesc) {
//        this.queryStatusDesc = queryStatusDesc;
//    }
//
//    public BigDecimal getQueryCastedAmount() {
//        return queryCastedAmount;
//    }
//
//    public void setQueryCastedAmount(BigDecimal queryCastedAmount) {
//        this.queryCastedAmount = queryCastedAmount;
//    }

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
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getCreateUser() {
//        return createUser;
//    }
//
//    public void setCreateUser(String createUser) {
//        this.createUser = createUser;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    public String getUpdateUser() {
//        return updateUser;
//    }
//
//    public void setUpdateUser(String updateUser) {
//        this.updateUser = updateUser;
//    }

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

    public BigDecimal getOffLineInOverDueRate() {
        return offLineInOverDueRate;
    }

    public void setOffLineInOverDueRate(BigDecimal offLineInOverDueRate) {
        this.offLineInOverDueRate = offLineInOverDueRate;
    }

    public Integer getOffLineInOverDueRateType() {
        return offLineInOverDueRateType;
    }

    public void setOffLineInOverDueRateType(Integer offLineInOverDueRateType) {
        this.offLineInOverDueRateType = offLineInOverDueRateType;
    }

    public List<ProjFeeReq> getProjFeeInfos() {
        return projFeeInfos;
    }

    public void setProjFeeInfos(List<ProjFeeReq> projFeeInfos) {
        this.projFeeInfos = projFeeInfos;
    }

    public  List<PrincipleReq> getPrincipleReqList() {
        return principleReqList;
    }

    public void setPrincipleReqList(List<PrincipleReq> principleReqList) {
        this.principleReqList = principleReqList;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }

    public BigDecimal getOnLineOverDueRate() {
        return onLineOverDueRate;
    }

    public void setOnLineOverDueRate(BigDecimal onLineOverDueRate) {
        this.onLineOverDueRate = onLineOverDueRate;
    }

    public Integer getOnLineOverDueRateType() {
        return onLineOverDueRateType;
    }

    public void setOnLineOverDueRateType(Integer onLineOverDueRateType) {
        this.onLineOverDueRateType = onLineOverDueRateType;
    }

    public BigDecimal getOffLineOutOverDueRate() {
        return offLineOutOverDueRate;
    }

    public void setOffLineOutOverDueRate(BigDecimal offLineOutOverDueRate) {
        this.offLineOutOverDueRate = offLineOutOverDueRate;
    }

    public Integer getOffLineOutOverDueRateType() {
        return offLineOutOverDueRateType;
    }

    public void setOffLineOutOverDueRateType(Integer offLineOutOverDueRateType) {
        this.offLineOutOverDueRateType = offLineOutOverDueRateType;
    }

    public String getTdUserId() {
        return tdUserId;
    }

    public void setTdUserId(String tdUserId) {
        this.tdUserId = tdUserId;
    }

    public BigDecimal getTuandaiRate() {
        return tuandaiRate;
    }

    public void setTuandaiRate(BigDecimal tuandaiRate) {
        this.tuandaiRate = tuandaiRate;
    }

    public BigDecimal getTuandaiAmount() {
        return tuandaiAmount;
    }

    public void setTuandaiAmount(BigDecimal tuandaiAmount) {
        this.tuandaiAmount = tuandaiAmount;
    }

    public BigDecimal getGuaranteeRate() {
        return guaranteeRate;
    }

    public void setGuaranteeRate(BigDecimal guaranteeRate) {
        this.guaranteeRate = guaranteeRate;
    }

    public BigDecimal getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(BigDecimal guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public BigDecimal getSubCompanyRate() {
        return subCompanyRate;
    }

    public void setSubCompanyRate(BigDecimal subCompanyRate) {
        this.subCompanyRate = subCompanyRate;
    }

    public BigDecimal getSubCompanyCharge() {
        return subCompanyCharge;
    }

    public void setSubCompanyCharge(BigDecimal subCompanyCharge) {
        this.subCompanyCharge = subCompanyCharge;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public BigDecimal getAgencyRate() {
        return agencyRate;
    }

    public void setAgencyRate(BigDecimal agencyRate) {
        this.agencyRate = agencyRate;
    }

    public BigDecimal getAgencyAmount() {
        return agencyAmount;
    }

    public void setAgencyAmount(BigDecimal agencyAmount) {
        this.agencyAmount = agencyAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getFreedAmount() {
        return freedAmount;
    }

    public void setFreedAmount(BigDecimal freedAmount) {
        this.freedAmount = freedAmount;
    }

    public BigDecimal getFreedRate() {
        return freedRate;
    }

    public void setFreedRate(BigDecimal freedRate) {
        this.freedRate = freedRate;
    }

    public BigDecimal getCooperativeTdComRate() {
        return cooperativeTdComRate;
    }

    public void setCooperativeTdComRate(BigDecimal cooperativeTdComRate) {
        this.cooperativeTdComRate = cooperativeTdComRate;
    }

    public BigDecimal getCooperativeTdComAmount() {
        return cooperativeTdComAmount;
    }

    public void setCooperativeTdComAmount(BigDecimal cooperativeTdComAmount) {
        this.cooperativeTdComAmount = cooperativeTdComAmount;
    }

    public List<ProjExtRateReq> getProjExtRateReqs() {
        return projExtRateReqs;
    }

    public void setProjExtRateReqs(List<ProjExtRateReq> projExtRateReqs) {
        this.projExtRateReqs = projExtRateReqs;
    }

    public Map<String, List<ProjExtRateReq>> getProjExtRateReqMap() {
        return projExtRateReqMap;
    }

    public void setProjExtRateReqMap(Map<String, List<ProjExtRateReq>> projExtRateReqMap) {
        this.projExtRateReqMap = projExtRateReqMap;
    }

//    public String getBusinessId() {
//        return businessId;
//    }
//
//    public void setBusinessId(String businessId) {
//        this.businessId = businessId;
//    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOverRate() {
        return overRate;
    }

    public void setOverRate(BigDecimal overRate) {
        this.overRate = overRate;
    }

    public BigDecimal getMonthPrincipalAmount() {
        return monthPrincipalAmount;
    }

    public void setMonthPrincipalAmount(BigDecimal monthPrincipalAmount) {
        this.monthPrincipalAmount = monthPrincipalAmount;
    }

    public Integer getBorrowLimit() {
        return borrowLimit;
    }

    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }
}
