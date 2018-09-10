package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransferOfLitigationVO implements Serializable {

	private static final long serialVersionUID = -7083361271450973294L;

	/**
	 * 业务编码
	 */
	@ApiModelProperty(required = true, value = "业务编码")
	private String businessId;

	/**
	 * 借款利率
	 */
	@ApiModelProperty(required = true, value = "借款利率")
	private double loanRate;

	/**
	 * 业务类型
	 */
	@ApiModelProperty(required = true, value = "业务类型")
	private String businessType;

	/**
	 * 业务类型名称
	 */
	@ApiModelProperty(required = true, value = "业务类型名称")
	private String businessTypeName;

	/**
	 * 业务类型组别名称
	 */
	@ApiModelProperty(required = true, value = "业务类型组别名称")
	private String businessTypeGroup;

	/**
	 * 业务所属分公司
	 */
	@ApiModelProperty(required = true, value = "业务所属分公司")
	private BusinessCompany businessCompany;

	/**
	 * 客户名称
	 */
	@ApiModelProperty(required = true, value = "客户名称")
	private String customerName;

	/**
	 * 客户类型
	 */
	@ApiModelProperty(required = true, value = "客户类型")
	private String customerType;

	/**
	 * 客户身份证号码
	 */
	@ApiModelProperty(required = true, value = "客户身份证号码")
	private String customerIdentifyCard;

	/**
	 * 业务获取人
	 */
	@ApiModelProperty(required = true, value = "业务获取人")
	private BusinessUser originalUser;

	/**
	 * 借款金额
	 */
	@ApiModelProperty(required = true, value = "借款金额")
	private BigDecimal finalBorrowMoney;

	/**
	 * 借款期限
	 */
	@ApiModelProperty(required = true, value = "借款期限")
	private Integer borrowLimit;

	/**
	 * 还款方式ID，1到期还本息，2每月付息到期还本，5每月等额本息,9是分期还本付息
	 */
	@ApiModelProperty(required = true, value = "还款方式ID，1到期还本息，2每月付息到期还本，5每月等额本息,9是分期还本付息")
	private Integer repaymentTypeId;

	/**
	 * 已还本金(累计)
	 */
	@ApiModelProperty(required = true, value = "已还本金(累计)")
	private BigDecimal repaymentPrincipa;

	/**
	 * 已还期数
	 */
	@ApiModelProperty(required = true, value = "已还期数")
	private Integer repaymentPeriods;

	/**
	 * 总期数
	 */
	@ApiModelProperty(required = true, value = "总期数")
	private Integer totalPeriods;

	/**
	 * 结清状态(财务未结清0 财务已结清10)
	 */
	@ApiModelProperty(required = true, value = "结清状态(财务未结清0 财务已结清10)")
	private Integer accountStatus;

	/**
	 * 诉讼案件来源 0信贷系统 1新贷后系统 2线下业务补录
	 */
	@ApiModelProperty(required = true, value = "诉讼案件来源 0信贷系统 1新贷后系统 2线下业务补录")
	private Integer sourceSystem;

	/**
	 * 相关的车辆信息列表
	 */
	@ApiModelProperty(required = true, value = "相关的车辆信息列表")
	private List<BusinessCar> carList;

	/**
	 * 相关的房屋信息列表
	 */
	@ApiModelProperty(required = true, value = "相关的房屋信息列表")
	private List<BusinessHouse> houseList;

	/**
	 * 还款明细
	 */
	@ApiModelProperty(required = true, value = "还款明细")
	private List<BusinessPayment> paymentList;

	/**
	 * 借款人明细
	 */
	@ApiModelProperty(required = true, value = "借款人明细")
	private List<LitigationBorrowerDetailed> litigationBorrowerDetailedList;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(required = true, value = "创建人ID")
	private String createUserId;
	
	/**
	 * 催收级别
	 */
	private String collectLevel;
	
	/**
	 * 逾期天数
	 */
	private Integer overdueDays;

	/**
	 * 逾期时间
	 */
	private Date dueDate;
}
