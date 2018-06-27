package com.hongte.alms.base.RepayPlan.dto.app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author czs
 * @since 2018/5/31
 */
public class BizPlanListMessageDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String businessId;// 业务编号
	private String name;// 主借款人姓名
	private String cardId;// 主借款人身份证号码
	private String type;// 业务类型
	private Date date;// 出款日期
	private BigDecimal balance;// 借款金额
	private Integer periods;// 期数
	private Integer totalPeriods;//总期数
	private String carNumber;//如果是车易贷，返回车牌号
	private BigDecimal amount;// 应还金额
	private Date cycDate;// 应还日期

	
	
	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public Integer getTotalPeriods() {
		return totalPeriods;
	}

	public void setTotalPeriods(Integer totalPeriods) {
		this.totalPeriods = totalPeriods;
	}

	public BizPlanListMessageDto(String businessId, String name, String cardId, String type, Date date,
			BigDecimal balance, Integer periods, BigDecimal amount, Date cycDate,Integer totalPeriods) {
		super();
		this.businessId = businessId;
		this.name = name;
		this.cardId = cardId;
		this.type = type;
		this.date = date;
		this.balance = balance;
		this.periods = periods;
		this.amount = amount;
		this.cycDate = cycDate;
		this.totalPeriods=totalPeriods;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getPeriods() {
		return periods;
	}

	public void setPeriods(Integer periods) {
		this.periods = periods;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCycDate() {
		return cycDate;
	}

	public void setCycDate(Date cycDate) {
		this.cycDate = cycDate;
	}

}
