package com.hongte.alms.open.vo;




/**
 * Created by chenzs on 2018/3/08.
 */
public class WithHoldingInfo {
    private String BusinessId;
    
    private String AfterId;

	private Integer Type;
    
    private String FactDate;

    private String Overduemoney;
    
    private String isAmountWithheld;
    
    private String nowRepayMoney;
    
    private String userID; 
 
	public String getIsAmountWithheld() {
		return isAmountWithheld;
	}

	public void setIsAmountWithheld(String isAmountWithheld) {
		this.isAmountWithheld = isAmountWithheld;
	}

	public String getNowRepayMoney() {
		return nowRepayMoney;
	}

	public void setNowRepayMoney(String nowRepayMoney) {
		this.nowRepayMoney = nowRepayMoney;
	}

	public String getBusinessId() {
		return BusinessId;
	}

	public void setBusinessId(String businessId) {
		BusinessId = businessId;
	}

	public String getAfterId() {
		return AfterId;
	}

	public void setAfterId(String afterId) {
		AfterId = afterId;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer type) {
		Type = type;
	}

	public String getFactDate() {
		return FactDate;
	}

	public void setFactDate(String factDate) {
		FactDate = factDate;
	}

	public String getOverduemoney() {
		return Overduemoney;
	}

	public void setOverduemoney(String overduemoney) {
		Overduemoney = overduemoney;
	}

	public Integer getRepayplatform() {
		return Repayplatform;
	}

	public void setRepayplatform(Integer repayplatform) {
		Repayplatform = repayplatform;
	}

	private Integer Repayplatform;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
    


    
  

}
