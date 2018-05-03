/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
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
	 * 剩余未还本金
	 */
	private BigDecimal principal ;
	/**
	 * 本期利息
	 */
	private BigDecimal interest ;
	/**
	 * 本期服务费
	 */
	private BigDecimal servicecharge ;
	/**
	 * 本期担保费
	 */
	private BigDecimal guaranteeFee ;
	/**
	 * 本期平台费
	 */
	private BigDecimal platformFee ;
	/**
	 * 期内滞纳金
	 */
	private BigDecimal lateFee ;
	/**
	 * 期外逾期费
	 */
	private BigDecimal demurrage ;
	/**
	 * 是否在合同期内
	 */
	private String isInContractDate ;
	/**
	 * 提前还款违约金
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
		this.principal = principal.setScale(2,BigDecimal.ROUND_HALF_UP);
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
		this.interest = interest.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.servicecharge = servicecharge.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.guaranteeFee = guaranteeFee.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.platformFee = platformFee.setScale(2,BigDecimal.ROUND_HALF_UP);;
	}

	/**
	 * @return the lateFee
	 */
	public BigDecimal getLateFee() {
		return lateFee;
	}

	
	public String getIsInContractDate() {
		return isInContractDate;
	}

	public void setIsInContractDate(String isInContractDate) {
		this.isInContractDate = isInContractDate;
	}

	/**
	 * @param lateFee the lateFee to set
	 */
	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.demurrage = demurrage.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.penalty = penalty.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.lackFee = lackFee.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.balance = balance.setScale(2,BigDecimal.ROUND_HALF_UP);;
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
		this.deposit = deposit.setScale(2,BigDecimal.ROUND_HALF_UP);;
	}

	/**
	 * 
	 */
	public ExpenseSettleVO() {
		super();
		setBalance(new BigDecimal(0));
		setDemurrage(new BigDecimal(0));
		setDeposit(new BigDecimal(0));
		setGuaranteeFee(new BigDecimal(0));
		setInterest(new BigDecimal(0));
		setLackFee(new BigDecimal(0));
		setLateFee(new BigDecimal(0));
		setPenalty(new BigDecimal(0));
		setPrincipal(new BigDecimal(0));
		setPlatformFee(new BigDecimal(0));
		setServicecharge(new BigDecimal(0));
		setList(Collections.EMPTY_LIST);
	}
	
	
	
}
