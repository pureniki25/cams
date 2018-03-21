package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.entity.Doc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="车辆拍卖信息",description="返回车辆拍卖信息到app端")
public class AuctionRespVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	   @ApiModelProperty(value="业务编号id",name="businessId")
	    private String businessId;  //拍卖ID
	   
	   @ApiModelProperty(value="区域ID",name="priceID")
	    private String priceID;  //拍卖ID

	    @ApiModelProperty(value="起拍价",name="StartPrice")
	    private BigDecimal StartPrice; //起拍价
	    
	    @ApiModelProperty(value="保证金",name="bond")
	    private BigDecimal bond; //起拍价
	    
	    @ApiModelProperty(value="保证金",name="priceincrease")
	    private BigDecimal priceincrease; //加价幅度

	    @ApiModelProperty(value="拍卖开始时间",name="startPriceDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date startPriceDate; //拍卖开始时间
	    
	    @ApiModelProperty(value="拍卖结束时间",name="etartPriceDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date etartPriceDate;  //拍卖结束时间
	    
	    
	    @ApiModelProperty(value="竞价开始时间",name="starBidTime")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date starBidTime; //拍卖开始时间
	    
	    @ApiModelProperty(value="竞价结束时间",name="endBidTime")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date endBidTime;  //拍卖结束时间
	    
	    
	    @ApiModelProperty(value="咨询开始时间",name="conStartDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date conStartDate; //咨询开始时间
	    
	    @ApiModelProperty(value="咨询结束时间",name="conEndDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date conEndDate;  //咨询结束时间
	    
	    @ApiModelProperty(value="看样开始时间",name="vieStartDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date vieStartDate;  //咨询结束时间
	    
	    @ApiModelProperty(value="看样结束时间",name="vieEndDate")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date vieEndDate;  //咨询结束时间

	    @ApiModelProperty(value="看样地点",name="vie ")
	    private String  vie ;  //看样地点


	    @ApiModelProperty(value="联系地点",name="contact")
	    private String contact;   //联系地点

	    @ApiModelProperty(value="交款时间",name="paymentTime")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date paymentTime;  //咨询结束时间

	    @ApiModelProperty(value="交易类型",name="tranType")
	    private String tranType;   //交易类型
	    
	    @ApiModelProperty(value="取货方式",name="pickupMethod")
	    private String pickupMethod;   //取货方式
	    
	    @ApiModelProperty(value="支付方式",name="paymentMethod")
	    private String paymentMethod;   //支付方式
	    
	    @ApiModelProperty(value="竞价规则",name="bidRule")
	    private String bidRule;   //竞价规则
	    
	    @ApiModelProperty(value="处置单位",name="disposalUnit")
	    private String disposalUnit;   //处置单位
	    
	    @ApiModelProperty(value="咨询联系人",name="contacts")
	    private String contacts;   //咨询联系人
	    
	    @ApiModelProperty(value="咨询电话",name="telephone")
	    private String telephone;   //咨询联系人
	    
	    @ApiModelProperty(value="账户名称",name="account")
	    private String account;   //账户名称
	    
	    @ApiModelProperty(value="开户银行",name="bank")
	    private String bank;  
	    
	    @ApiModelProperty(value="账户银行卡卡号",name="cardNo")
	    private String cardNo;  
	    
	    @ApiModelProperty(value="缴款截止时间",name="paymentEndTime")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private String paymentEndTime;  
	    
	    @ApiModelProperty(value="车辆品牌",name="vehicleBrand")
	    private String vehicleBrand; 
	    
	    @ApiModelProperty(value="汽车产地",name="carproduction")
	    private String carproduction; 
	    
	    
	    @ApiModelProperty(value="车辆颜色",name="carColour")
	    private String carColour; 
	    
	    
	    @ApiModelProperty(value="车辆型号",name="carModel")
	    private String carModel; 
	    
	    @ApiModelProperty(value="排量",name="displacement ")
	    private String displacement ; 
	    
	    @ApiModelProperty(value="发动机号",name="engineNumber ")
	    private String engineNumber ; 
	    
	    @ApiModelProperty(value="车架号",name="frameNumber ")
	    private String frameNumber ; 
	    
	    @ApiModelProperty(value="车辆属地",name="vehicleTerritory ")
	    private String vehicleTerritory ; 
	    
	    @ApiModelProperty(value="使用性质",name="useProperty ")
	    private String useProperty ; 
	    
	    @ApiModelProperty(value="保险到期日",name="insuranceDate ")
	    @JsonFormat( pattern = "yyyy-MM-dd ")
	    private Date insuranceDate ; 
	    
	    @ApiModelProperty(value="年检到期日",name="inspectionDate ")
	    private String inspectionDate ; 
	    
	    @ApiModelProperty(value="车显里程",name="mileage ")
	    private int mileage ; 
	    
	    
	    @ApiModelProperty(value="首次登记年月",name="registerDate ")
	    @JsonFormat( pattern = "yyyy-MM-dd ")
	    private Date registerDate ; 
	    @ApiModelProperty(value="车辆抵押状态",name="mortgageState ")
	    private String mortgageState ; 
	    
	    @ApiModelProperty(value="违章未处理记录",name="lllegal ")
	    private String Illegal ; 
	    
	    @ApiModelProperty(value="随车工具",name="tools ")
	    private String tools ; 
	    
	    @ApiModelProperty(value="交易税费",name="taxation ")
	    private String taxation ; 
	    
	    @ApiModelProperty(value="车牌号",name="licensePlateNumber ")
	    private String licensePlateNumber ; 
	    

	    @ApiModelProperty(value="交易方式",name="transactionMode ")
	    private String transactionMode ; 
	    @ApiModelProperty(value="车辆位置",name="position ")
	    private String position ; 
	    @ApiModelProperty(value="提供文件",name="files ")
	    private String files ; 
	    
	    @ApiModelProperty(value="备注",name="remarks  ")
	    private String remarks  ;
	    
	    @ApiModelProperty(value="审核通过时间",name="auditTime")
	    @JsonFormat( pattern = "yyyy-MM-dd HH:mm")
	    private Date auditTime;
	    
	    private  List<Doc> docs;
	    
		@ApiModelProperty(required= true,value = "用户姓名 ")
		private String bidderName;
	    /**
	     * 身份证号码 
	     */
		@ApiModelProperty(required= true,value = "身份证号码 ")
		private String bidderCertId;
	    /**
	     * 联系方式 
	     */
		@ApiModelProperty(required= true,value = "联系方式 ")
		private String bidderTel;
	    /**
	     * 转账账户 
	     */
		@ApiModelProperty(required= true,value = "转账账户 ")
		private String transAccountName;
	    /**
	     * 转账卡号
	     */
		@ApiModelProperty(required= true,value = "转账卡号")
		private String transAccountNum;
	    /**
	     * 转账银行
	     */
		@ApiModelProperty(required= true,value = "转账银行")
		private String transBank;
		
	    /**
	     * 拍卖登记id
	     */
		@ApiModelProperty(required= true,value = "拍卖登记id")
		private String regId;

	    /**
	     * 是否缴纳保证金 
	     */
		@ApiModelProperty(required= true,value = "是否缴纳保证金 ")
		private Boolean isPayDeposit;
	    /**
	     * 出价金额
	     */
		@ApiModelProperty(required= true,value = "出价金额")
		private BigDecimal offerAmount;
	    /**
	     * 是否竞拍成功 
	     */
		@ApiModelProperty(required= true,value = "是否竞拍成功 ")
		private Boolean isAuctionSuccess;
	    /**
	     * 成交价格
	     */
		@ApiModelProperty(required= true,value = "成交价格")
		private BigDecimal transPrice;
	    /**
	     * 竞拍id
	     */
		@ApiModelProperty(required= true,value = "竞拍id")
		private String auctionId;
		
	    /**
	     *评估金额
	     */
		@ApiModelProperty(required= true,value = "评估金额")
		private BigDecimal lastEvaluationAmount;
		
		
		public List<Doc> getDocs() {
			return docs;
		}

		public void setDocs(List<Doc> docs) {
			this.docs = docs;
		}

		public String getPriceID() {
			return priceID;
		}

		public void setPriceID(String priceID) {
			this.priceID = priceID;
		}

		public BigDecimal getStartPrice() {
			return StartPrice;
		}

		public void setStartPrice(BigDecimal startPrice) {
			StartPrice = startPrice;
		}

		public BigDecimal getBond() {
			return bond;
		}

		public void setBond(BigDecimal bond) {
			this.bond = bond;
		}

		public BigDecimal getPriceincrease() {
			return priceincrease;
		}

		public void setPriceincrease(BigDecimal priceincrease) {
			this.priceincrease = priceincrease;
		}

		public Date getStartPriceDate() {
			return startPriceDate;
		}

		public void setStartPriceDate(Date startPriceDate) {
			this.startPriceDate = startPriceDate;
		}


		public Date getEtartPriceDate() {
			return etartPriceDate;
		}

		public void setEtartPriceDate(Date etartPriceDate) {
			this.etartPriceDate = etartPriceDate;
		}

		public Date getStarBidTime() {
			return starBidTime;
		}

		public void setStarBidTime(Date starBidTime) {
			this.starBidTime = starBidTime;
		}

		public Date getEndBidTime() {
			return endBidTime;
		}

		public void setEndBidTime(Date endBidTime) {
			this.endBidTime = endBidTime;
		}

		public Date getConStartDate() {
			return conStartDate;
		}

		public void setConStartDate(Date conStartDate) {
			this.conStartDate = conStartDate;
		}

		public Date getConEndDate() {
			return conEndDate;
		}

		public void setConEndDate(Date conEndDate) {
			this.conEndDate = conEndDate;
		}

		public Date getVieStartDate() {
			return vieStartDate;
		}

		public void setVieStartDate(Date vieStartDate) {
			this.vieStartDate = vieStartDate;
		}

	
		public Date getVieEndDate() {
			return vieEndDate;
		}

		public void setVieEndDate(Date vieEndDate) {
			this.vieEndDate = vieEndDate;
		}

		public String getVie() {
			return vie;
		}

		public void setVie(String vie) {
			this.vie = vie;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public Date getPaymentTime() {
			return paymentTime;
		}

		public void setPaymentTime(Date paymentTime) {
			this.paymentTime = paymentTime;
		}

		public String getTranType() {
			return tranType;
		}

		public void setTranType(String tranType) {
			this.tranType = tranType;
		}

		public String getPickupMethod() {
			return pickupMethod;
		}

		public void setPickupMethod(String pickupMethod) {
			this.pickupMethod = pickupMethod;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

		public String getBidRule() {
			return bidRule;
		}

		public void setBidRule(String bidRule) {
			this.bidRule = bidRule;
		}

		public String getDisposalUnit() {
			return disposalUnit;
		}

		public void setDisposalUnit(String disposalUnit) {
			this.disposalUnit = disposalUnit;
		}

		public String getContacts() {
			return contacts;
		}

		public void setContacts(String contacts) {
			this.contacts = contacts;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getCardNo() {
			return cardNo;
		}

		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}

		public String getVehicleBrand() {
			return vehicleBrand;
		}

		public void setVehicleBrand(String vehicleBrand) {
			this.vehicleBrand = vehicleBrand;
		}

		public String getCarproduction() {
			return carproduction;
		}

		public void setCarproduction(String carproduction) {
			this.carproduction = carproduction;
		}

		public String getCarColour() {
			return carColour;
		}

		public void setCarColour(String carColour) {
			this.carColour = carColour;
		}

		public String getCarModel() {
			return carModel;
		}

		public void setCarModel(String carModel) {
			this.carModel = carModel;
		}

		public String getDisplacement() {
			return displacement;
		}

		public void setDisplacement(String displacement) {
			this.displacement = displacement;
		}

		public String getEngineNumber() {
			return engineNumber;
		}

		public void setEngineNumber(String engineNumber) {
			this.engineNumber = engineNumber;
		}

		public String getFrameNumber() {
			return frameNumber;
		}

		public void setFrameNumber(String frameNumber) {
			this.frameNumber = frameNumber;
		}





		public String getVehicleTerritory() {
			return vehicleTerritory;
		}

		public void setVehicleTerritory(String vehicleTerritory) {
			this.vehicleTerritory = vehicleTerritory;
		}

		public String getUseProperty() {
			return useProperty;
		}

		public void setUseProperty(String useProperty) {
			this.useProperty = useProperty;
		}

		public Date getInsuranceDate() {
			return insuranceDate;
		}

		public void setInsuranceDate(Date insuranceDate) {
			this.insuranceDate = insuranceDate;
		}



		public String getInspectionDate() {
			return inspectionDate;
		}

		public void setInspectionDate(String inspectionDate) {
			this.inspectionDate = inspectionDate;
		}

		public int getMileage() {
			return mileage;
		}

		public void setMileage(int mileage) {
			this.mileage = mileage;
		}

		public Date getRegisterDate() {
			return registerDate;
		}

		public void setRegisterDate(Date registerDate) {
			this.registerDate = registerDate;
		}

		public String getMortgageState() {
			return mortgageState;
		}

		public void setMortgageState(String mortgageState) {
			this.mortgageState = mortgageState;
		}

		public String getIllegal() {
			return Illegal;
		}

		public void setIllegal(String illegal) {
			Illegal = illegal;
		}

		public String getTools() {
			return tools;
		}

		public void setTools(String tools) {
			this.tools = tools;
		}

		public String getTaxation() {
			return taxation;
		}

		public void setTaxation(String taxation) {
			this.taxation = taxation;
		}

		public String getTransactionMode() {
			return transactionMode;
		}

		public void setTransactionMode(String transactionMode) {
			this.transactionMode = transactionMode;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getFiles() {
			return files;
		}

		public void setFiles(String files) {
			this.files = files;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getPaymentEndTime() {
			return paymentEndTime;
		}

		public void setPaymentEndTime(String paymentEndTime) {
			this.paymentEndTime = paymentEndTime;
		}

		public String getBusinessId() {
			return businessId;
		}

		public void setBusinessId(String businessId) {
			this.businessId = businessId;
		}

		public String getLicensePlateNumber() {
			return licensePlateNumber;
		}

		public void setLicensePlateNumber(String licensePlateNumber) {
			this.licensePlateNumber = licensePlateNumber;
		}

		public String getBidderName() {
			return bidderName;
		}

		public void setBidderName(String bidderName) {
			this.bidderName = bidderName;
		}

		public String getBidderCertId() {
			return bidderCertId;
		}

		public void setBidderCertId(String bidderCertId) {
			this.bidderCertId = bidderCertId;
		}

		public String getBidderTel() {
			return bidderTel;
		}

		public void setBidderTel(String bidderTel) {
			this.bidderTel = bidderTel;
		}

		public String getTransAccountName() {
			return transAccountName;
		}

		public void setTransAccountName(String transAccountName) {
			this.transAccountName = transAccountName;
		}

		public String getTransAccountNum() {
			return transAccountNum;
		}

		public void setTransAccountNum(String transAccountNum) {
			this.transAccountNum = transAccountNum;
		}

		public String getTransBank() {
			return transBank;
		}

		public void setTransBank(String transBank) {
			this.transBank = transBank;
		}

		public String getRegId() {
			return regId;
		}

		public void setRegId(String regId) {
			this.regId = regId;
		}

		public Boolean getIsPayDeposit() {
			return isPayDeposit;
		}

		public void setIsPayDeposit(Boolean isPayDeposit) {
			this.isPayDeposit = isPayDeposit;
		}

		public BigDecimal getOfferAmount() {
			return offerAmount;
		}

		public void setOfferAmount(BigDecimal offerAmount) {
			this.offerAmount = offerAmount;
		}

		public Boolean getIsAuctionSuccess() {
			return isAuctionSuccess;
		}

		public void setIsAuctionSuccess(Boolean isAuctionSuccess) {
			this.isAuctionSuccess = isAuctionSuccess;
		}

		public BigDecimal getTransPrice() {
			return transPrice;
		}

		public void setTransPrice(BigDecimal transPrice) {
			this.transPrice = transPrice;
		}

		public String getAuctionId() {
			return auctionId;
		}

		public void setAuctionId(String auctionId) {
			this.auctionId = auctionId;
		}

		public BigDecimal getLastEvaluationAmount() {
			return lastEvaluationAmount;
		}

		public void setLastEvaluationAmount(BigDecimal lastEvaluationAmount) {
			this.lastEvaluationAmount = lastEvaluationAmount;
		}

		public Date getAuditTime() {
			return auditTime;
		}

		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		} 
	    
	 
}
