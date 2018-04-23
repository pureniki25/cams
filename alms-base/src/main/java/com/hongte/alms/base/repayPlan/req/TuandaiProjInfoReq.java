package com.hongte.alms.base.repayPlan.req;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.hongte.alms.base.entity.TuandaiProjectCar;
import com.hongte.alms.base.entity.TuandaiProjectHouse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@ApiModel("创建还款计划团贷上标信息")
public class TuandaiProjInfoReq {


    /**
     * 标的车辆信息
     */
    @ApiModelProperty(value = "标的车辆信息")
    private  List<TuandaiProjectCarReq> projCarInfos;

    /**
     * 标的房源信息
     */
    @ApiModelProperty(value = "标的房源信息")
    private  List<TuandaiProjectCarReq> projHouseInfos;


    /**
     * 项目编号
     */
    @ApiModelProperty(required= true,value = "项目编号")
    private String projectId;
    /**
     * 业务编号
     */
    @ApiModelProperty(required= true,value = "业务编号")
    private String businessId;
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
     * 借款期限
     */
    @ApiModelProperty(required= true,value = "借款期限")
    private Integer borrowLimit;
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
     * 团贷网放款时间
     */
    @ApiModelProperty(required= true,value = "团贷网放款时间")
    private Date tdLoanTime;
    /**
     * 团贷网放款金额
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
     * 展期标对应的原业务上标编号(仅展期业务)
     */
    @ApiModelProperty(required= true,value = "展期标对应的原业务上标编号(仅展期业务)")
    private String orgIssueId;
    /**
     * 主借标ID
     */
    @ApiModelProperty(required= true,value = "主借标ID")
    private String masterIssueId;
    /**
     * 超额拆标共借项目的序号
     */
    @ApiModelProperty(required= true,value = "超额拆标共借项目的序号")
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
    /**
     * 业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)
     */
    @ApiModelProperty(required= true,value = "业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷  39 车全 47 闪贷 48 扶贫贷)")
    private Integer projectType;
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
    /**
     * 年化利率
     */
    @ApiModelProperty(required= true,value = "年化利率")
    private BigDecimal interestRate;
    /**
     * 逾期年利率
     */
    @ApiModelProperty(required= true,value = "逾期年利率")
    private BigDecimal overRate;
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
     * 团贷用户ID
     */
    @ApiModelProperty(required= true,value = "团贷用户ID")
    private String tdUserId;
    /**
     * 客户类型 1:个人 2:企业
     */
    @ApiModelProperty(required= true,value = "客户类型 1:个人 2:企业")
    private Integer userTypeId;
    /**
     * 婚姻状况, 已婚、未婚 (信用贷时必填)
     */
    @ApiModelProperty(required= true,value = "婚姻状况, 已婚、未婚 (信用贷时必填)")
    private String marriage;
    /**
     * 居住地址,详细地址，包括省份城市 (信用贷时必填)
     */
    @ApiModelProperty(required= true,value = "居住地址,详细地址，包括省份城市 (信用贷时必填)")
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
    /**
     * 团贷比例(期初收取平台费上标比例)
     */
    @ApiModelProperty(required= true,value = "团贷比例(期初收取平台费上标比例)")
    private BigDecimal tuandaiRate;
    /**
     * 团贷预计佣金(期初收取平台费总金额)
     */
    @ApiModelProperty(required= true,value = "团贷预计佣金(期初收取平台费总金额)")
    private BigDecimal tuandaiAmount;
    /**
     * 担保比例(期初收取担保公司费比例)
     */
    @ApiModelProperty(required= true,value = "担保比例(期初收取担保公司费比例)")
    private BigDecimal guaranteeRate;
    /**
     * 担保预计收入(期初收取担保公司费用总金额)
     */
    @TableField("guarantee_amount")
    @ApiModelProperty(required= true,value = "担保预计收入(期初收取担保公司费用总金额)")
    private BigDecimal guaranteeAmount;
    /**
     * 分公司比例(期初收取分公司费用比例)
     */
    @TableField("sub_company_rate")
    @ApiModelProperty(required= true,value = "分公司比例(期初收取分公司费用比例)")
    private BigDecimal subCompanyRate;
    /**
     * 分公司预计金额(期初收取分公司费用总金额)
     */
    @ApiModelProperty(required= true,value = "分公司预计金额(期初收取分公司费用总金额)")
    private BigDecimal subCompanyCharge;
    /**
     * 中介Id或担保
     */
    @ApiModelProperty(required= true,value = "中介Id或担保")
    private String agencyId;
    /**
     * 中介比例
     */
    @ApiModelProperty(required= true,value = "中介比例")
    private BigDecimal agencyRate;
    /**
     * 中介金额
     */
    @ApiModelProperty(required= true,value = "中介金额")
    private BigDecimal agencyAmount;
    /**
     * 保证金金额
     */
    @ApiModelProperty(required= true,value = "保证金金额")
    private BigDecimal depositAmount;
    /**
     * 押金
     */
    @ApiModelProperty(required= true,value = "押金")
    private BigDecimal freedAmount;
    /**
     * 押金费率
     */
    @ApiModelProperty(required= true,value = "押金费率")
    private BigDecimal freedRate;
    /**
     * 合作公司所属团贷网分公司编号
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司编号")
    private String cooperativeTdComUserId;
    /**
     * 合作公司所属团贷网分公司费用比例
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司费用比例")
    private BigDecimal cooperativeTdComRate;
    /**
     * 合作公司所属团贷网分公司金额
     */
    @ApiModelProperty(required= true,value = "合作公司所属团贷网分公司金额")
    private BigDecimal cooperativeTdComAmount;
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
    /**
     * 每月还本金额
     */
    @ApiModelProperty(required= true,value = "每月还本金额")
    private BigDecimal monthPrincipalAmount;
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

    public List<TuandaiProjectCarReq> getProjCarInfos() {
        return projCarInfos;
    }

    public void setProjCarInfos(List<TuandaiProjectCarReq> projCarInfos) {
        this.projCarInfos = projCarInfos;
    }

    public List<TuandaiProjectCarReq> getProjHouseInfos() {
        return projHouseInfos;
    }

    public void setProjHouseInfos(List<TuandaiProjectCarReq> projHouseInfos) {
        this.projHouseInfos = projHouseInfos;
    }
}
