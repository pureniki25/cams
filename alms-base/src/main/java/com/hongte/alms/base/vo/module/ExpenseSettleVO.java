/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 王继光
 * 2018年3月12日 下午5:14:10
 */
public class ExpenseSettleVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1312491103861263898L;

	/**
	 * 本金
	 */
	private BigDecimal principal ;
	/**
	 * 利息
	 */
	private BigDecimal interest ;
	/**
	 * 服务费
	 */
	private BigDecimal servicecharge ;
	/**
	 * 担保费
	 */
	private BigDecimal guaranteeFee ;
	/**
	 * 平台费
	 */
	private BigDecimal platformFee ;
	/**
	 * 滞纳金
	 */
	private BigDecimal lateFee ;
	/**
	 * 逾期费
	 */
	private BigDecimal demurrage ;
	/**
	 * 违约金
	 */
	private BigDecimal penalty ;
	/**
	 * 往期少缴费用
	 */
	private BigDecimal lackFee ;
	
	/**
	 * 结余
	 */
	private BigDecimal balance ;
	
	/**
	 * 押金
	 */
	private BigDecimal deposit ;
	
	private List<ExpenseSettleLackFeeVO> list ;

	/**
	 * @return the principal
	 */
	public BigDecimal getPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	/**
	 * @return the interest
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * @return the servicecharge
	 */
	public BigDecimal getServicecharge() {
		return servicecharge;
	}

	/**
	 * @param servicecharge the servicecharge to set
	 */
	public void setServicecharge(BigDecimal servicecharge) {
		this.servicecharge = servicecharge;
	}

	/**
	 * @return the guaranteeFee
	 */
	public BigDecimal getGuaranteeFee() {
		return guaranteeFee;
	}

	/**
	 * @param guaranteeFee the guaranteeFee to set
	 */
	public void setGuaranteeFee(BigDecimal guaranteeFee) {
		this.guaranteeFee = guaranteeFee;
	}

	/**
	 * @return the platformFee
	 */
	public BigDecimal getPlatformFee() {
		return platformFee;
	}

	/**
	 * @param platformFee the platformFee to set
	 */
	public void setPlatformFee(BigDecimal platformFee) {
		this.platformFee = platformFee;
	}

	/**
	 * @return the lateFee
	 */
	public BigDecimal getLateFee() {
		return lateFee;
	}

	/**
	 * @param lateFee the lateFee to set
	 */
	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee;
	}

	/**
	 * @return the demurrage
	 */
	public BigDecimal getDemurrage() {
		return demurrage;
	}

	/**
	 * @param demurrage the demurrage to set
	 */
	public void setDemurrage(BigDecimal demurrage) {
		this.demurrage = demurrage;
	}

	/**
	 * @return the penalty
	 */
	public BigDecimal getPenalty() {
		return penalty;
	}

	/**
	 * @param penalty the penalty to set
	 */
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	/**
	 * @return the lackFee
	 */
	public BigDecimal getLackFee() {
		return lackFee;
	}

	/**
	 * @param lackFee the lackFee to set
	 */
	public void setLackFee(BigDecimal lackFee) {
		this.lackFee = lackFee;
	}

	/**
	 * @return the list
	 */
	public List<ExpenseSettleLackFeeVO> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ExpenseSettleLackFeeVO> list) {
		this.list = list;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the deposit
	 */
	public BigDecimal getDeposit() {
		return deposit;
	}

	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
}
