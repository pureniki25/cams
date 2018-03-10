package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.hongte.alms.base.entity.MoneyPool;

public class MoneyPoolXindaiDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5809596267816308880L;

	private Integer ID	;//	[编号]
	private String  Pay_Code	;//	[款项码]
	private String  Remit_Bank	;//	Remit_Bank
	private String  Trade_Date	;//	[转入时间]
	private String  Trade_Time	;//	[转入时间]
	private String  Trade_Type	;//	[交易类型]
	private String  Abstract	;//	[摘要]
	private String  Trade_Place	;//	[交易场所]
	private BigDecimal  Account_Money	;//	[记账金额]
	private Integer income_type	;//	收入类型（1：收入；2：支出）
	private String  Trade_Remark	;//	[交易备注]
	private String  Gainer_ID	;//	Gainer_ID
	private String  Gainer_Name	;//	Gainer_Name
	private String  Follow_ID	;//	Follow_ID
	private String  Reserve_1	;//	[状态]
	private String  Reserve_2	;//	Reserve_2
	private String  Reserve_3	;//	Reserve_3
	private String  Reserve_4	;//	Reserve_4
	private String  Reserve_5	;//	[领取人]
	private String  Reserve_6	;//	Reserve_6
	private String  Reserve_7	;//	Reserve_7
	private String  Reserve_8	;//	Reserve_8
	private String  Reserve_9	;//	Reserve_9
	private String  Reserve_11	;//	[导入人]
	private String  Reserve_10	;//	[导入时间]
	private String  State	;//	[状态]
	private String  AcceptBank	;//	[转入帐号]
	private String  DeptID	;//	[部门编号]
	private Integer  is_manual_create	;//	是否手动添加(0或null为导入，1为手动添加)
	private Integer  is_temporary	;//	是否暂收款流水(0或null不是，1是)
	private String  alms_pool_id	;//	贷后系统财务款项ID
	public MoneyPoolXindaiDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getPay_Code() {
		return Pay_Code;
	}
	public void setPay_Code(String pay_Code) {
		Pay_Code = pay_Code;
	}
	public String getRemit_Bank() {
		return Remit_Bank;
	}
	public void setRemit_Bank(String remit_Bank) {
		Remit_Bank = remit_Bank;
	}
	public String getTrade_Date() {
		return Trade_Date;
	}
	public void setTrade_Date(String trade_Date) {
		Trade_Date = trade_Date;
	}
	public String getTrade_Time() {
		return Trade_Time;
	}
	public void setTrade_Time(String trade_Time) {
		Trade_Time = trade_Time;
	}
	public String getTrade_Type() {
		return Trade_Type;
	}
	public void setTrade_Type(String trade_Type) {
		Trade_Type = trade_Type;
	}
	public String getAbstract() {
		return Abstract;
	}
	public void setAbstract(String abstract1) {
		Abstract = abstract1;
	}
	public String getTrade_Place() {
		return Trade_Place;
	}
	public void setTrade_Place(String trade_Place) {
		Trade_Place = trade_Place;
	}
	public BigDecimal getAccount_Money() {
		return Account_Money;
	}
	public void setAccount_Money(BigDecimal account_Money) {
		Account_Money = account_Money;
	}
	public Integer getIncome_type() {
		return income_type;
	}
	public void setIncome_type(Integer income_type) {
		this.income_type = income_type;
	}
	public String getTrade_Remark() {
		return Trade_Remark;
	}
	public void setTrade_Remark(String trade_Remark) {
		Trade_Remark = trade_Remark;
	}
	public String getGainer_ID() {
		return Gainer_ID;
	}
	public void setGainer_ID(String gainer_ID) {
		Gainer_ID = gainer_ID;
	}
	public String getGainer_Name() {
		return Gainer_Name;
	}
	public void setGainer_Name(String gainer_Name) {
		Gainer_Name = gainer_Name;
	}
	public String getFollow_ID() {
		return Follow_ID;
	}
	public void setFollow_ID(String follow_ID) {
		Follow_ID = follow_ID;
	}
	public String getReserve_1() {
		return Reserve_1;
	}
	public void setReserve_1(String reserve_1) {
		Reserve_1 = reserve_1;
	}
	public String getReserve_2() {
		return Reserve_2;
	}
	public void setReserve_2(String reserve_2) {
		Reserve_2 = reserve_2;
	}
	public String getReserve_3() {
		return Reserve_3;
	}
	public void setReserve_3(String reserve_3) {
		Reserve_3 = reserve_3;
	}
	public String getReserve_4() {
		return Reserve_4;
	}
	public void setReserve_4(String reserve_4) {
		Reserve_4 = reserve_4;
	}
	public String getReserve_5() {
		return Reserve_5;
	}
	public void setReserve_5(String reserve_5) {
		Reserve_5 = reserve_5;
	}
	public String getReserve_6() {
		return Reserve_6;
	}
	public void setReserve_6(String reserve_6) {
		Reserve_6 = reserve_6;
	}
	public String getReserve_7() {
		return Reserve_7;
	}
	public void setReserve_7(String reserve_7) {
		Reserve_7 = reserve_7;
	}
	public String getReserve_8() {
		return Reserve_8;
	}
	public void setReserve_8(String reserve_8) {
		Reserve_8 = reserve_8;
	}
	public String getReserve_9() {
		return Reserve_9;
	}
	public void setReserve_9(String reserve_9) {
		Reserve_9 = reserve_9;
	}
	public String getReserve_11() {
		return Reserve_11;
	}
	public void setReserve_11(String reserve_11) {
		Reserve_11 = reserve_11;
	}
	public String getReserve_10() {
		return Reserve_10;
	}
	public void setReserve_10(String reserve_10) {
		Reserve_10 = reserve_10;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getAcceptBank() {
		return AcceptBank;
	}
	public void setAcceptBank(String acceptBank) {
		AcceptBank = acceptBank;
	}
	public String getDeptID() {
		return DeptID;
	}
	public void setDeptID(String deptID) {
		DeptID = deptID;
	}
	public Integer getIs_manual_create() {
		return is_manual_create;
	}
	public void setIs_manual_create(Integer is_manual_create) {
		this.is_manual_create = is_manual_create;
	}
	public Integer getIs_temporary() {
		return is_temporary;
	}
	public void setIs_temporary(Integer is_temporary) {
		this.is_temporary = is_temporary;
	}
	public String getAlms_pool_id() {
		return alms_pool_id;
	}
	public void setAlms_pool_id(String alms_pool_id) {
		this.alms_pool_id = alms_pool_id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MoneyPoolXindaiDTO(MoneyPool moneyPool) {
		super();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		ID = moneyPool.getXdPoolId();
		Pay_Code = moneyPool.getPayCode();
		Remit_Bank = moneyPool.getRemitBank();
		Trade_Date = moneyPool.getTradeDate() == null ? null : dateFormat.format(moneyPool.getTradeDate());
		Trade_Time = moneyPool.getTradeDate() == null ? null : timeFormat.format(moneyPool.getTradeDate());
		Trade_Type = moneyPool.getTradeType();
		Abstract = moneyPool.getSummary();
		Trade_Place = moneyPool.getTradePlace();
		Account_Money = moneyPool.getAccountMoney();
		this.income_type = moneyPool.getIncomeType();
		Trade_Remark = moneyPool.getTradeRemark();
		Gainer_ID = moneyPool.getGainerName();
		Gainer_Name = moneyPool.getGainerName();
		Follow_ID = null;
		Reserve_1 = moneyPool.getFinanceStatus();
		Reserve_2 = null;
		Reserve_3 = null;
		Reserve_4 = null;
		Reserve_5 = moneyPool.getGainerName();
		Reserve_6 = null;
		Reserve_7 = null;
		Reserve_8 = null;
		Reserve_9 = null;
		Reserve_11 = moneyPool.getImportUser();
		Reserve_10 = moneyPool.getImportTime()==null ? null : dateTimeFormat.format(moneyPool.getImportTime());
		State = moneyPool.getStatus();
		AcceptBank = moneyPool.getAcceptBank();
		DeptID = null;
		this.is_manual_create = null;
		this.is_temporary = null;
		this.alms_pool_id = moneyPool.getMoneyPoolId();
	}


}
