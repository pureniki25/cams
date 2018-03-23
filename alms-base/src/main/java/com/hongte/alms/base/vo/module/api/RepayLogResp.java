package com.hongte.alms.base.vo.module.api;


public class RepayLogResp {


      private String listId;                //流水号
      private String merchOrderId;         //商户订单号
      private String ybOrderId;            //易宝付交易流水号
      private String currentAmount;           //还款金额
      private String identityCard;         //身份证号码
      private String customerName;         //客户姓名
      private String phoneNumber;          //电话号码
      private String bankCard;             //银行卡号
      private String businessId;          //业务单号
      private String businessAfterId;     //还款期数
      private String remark;              //备注
      private String updateUser;
      private String updateTime;
      private String merchantAccount;    //	商户号
      private String smsConfirm;         //短信验证码是否被验证
      private String userSign;           //签名
      private String errorCode;           
      private String errorMsg;
      private String codeSender;          //验证码发送者
      private String validateCode;
      private String repayStatus;           //代扣状态(1:成功,0:失败;2:处理中)
      private String settlementType;       //[还款类型，null或0：正常还款，1:提前结清]
      private String BindPlatform;         //绑卡平台ID，每个ID对应的绑卡平台，0为易宝绑卡，1为银盛绑卡，2为富友绑卡,3为宝付代扣,4为爱农代扣，请看配置表(PARA_TYPE=代扣平台)
      private String boolPartRepay;         //[表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣]
      private String boolLastRepay;         //[表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣]
      private String planTotalRepayMoney;  //[表示本期代扣总金额(若财务选择不含违约金代扣，则此字段不含违约金)，若本期为多笔代扣，此字段存本次多笔代扣的总金额，若非多笔代扣，则此字段存本次代扣总金额]
      private String createTime;            //代扣时间
      private String createUser;
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public String getMerchOrderId() {
		return merchOrderId;
	}
	public void setMerchOrderId(String merchOrderId) {
		this.merchOrderId = merchOrderId;
	}
	public String getYbOrderId() {
		return ybOrderId;
	}
	public void setYbOrderId(String ybOrderId) {
		this.ybOrderId = ybOrderId;
	}
    
	public String getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getBusinessAfterId() {
		return businessAfterId;
	}
	public void setBusinessAfterId(String businessAfterId) {
		this.businessAfterId = businessAfterId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getMerchantAccount() {
		return merchantAccount;
	}
	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}
	public String getSmsConfirm() {
		return smsConfirm;
	}
	public void setSmsConfirm(String smsConfirm) {
		this.smsConfirm = smsConfirm;
	}
	public String getUserSign() {
		return userSign;
	}
	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getCodeSender() {
		return codeSender;
	}
	public void setCodeSender(String codeSender) {
		this.codeSender = codeSender;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public String getRepayStatus() {
		return repayStatus;
	}
	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}
	public String getSettlementType() {
		return settlementType;
	}
	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}
	public String getBindPlatform() {
		return BindPlatform;
	}
	public void setBindPlatform(String bindPlatform) {
		BindPlatform = bindPlatform;
	}
	public String getBoolPartRepay() {
		return boolPartRepay;
	}
	public void setBoolPartRepay(String boolPartRepay) {
		this.boolPartRepay = boolPartRepay;
	}
	public String getBoolLastRepay() {
		return boolLastRepay;
	}
	public void setBoolLastRepay(String boolLastRepay) {
		this.boolLastRepay = boolLastRepay;
	}
	public String getPlanTotalRepayMoney() {
		return planTotalRepayMoney;
	}
	public void setPlanTotalRepayMoney(String planTotalRepayMoney) {
		this.planTotalRepayMoney = planTotalRepayMoney;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
      
      



}
