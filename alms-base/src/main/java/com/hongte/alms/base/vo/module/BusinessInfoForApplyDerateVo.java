package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;

/** 申请减免界面的基本信息
 * @author zengkun
 * @since 2018/2/5
 * 业务信息Vo
 */
public class BusinessInfoForApplyDerateVo  {

    private String businessId		   	; //业务编号
    private String zqBusinessId         ;//如果当前期是展期业务,则存展期业务编号，否则存原业务编号
    private String customerName        ; //客户名称
    private String companyId           ; //所属分公司ID
    private String companyName  		; //所属分公司		需处理
    private Integer businessType        ; //业务类型Id
    private String businessTypeName	; //业务类型   		需处理
    private Integer borrowLimit         ; //借款期限
    private Integer repaymentTypeId     ; //还款方式Id
    private String repaymentTypeName   ; //还款方式  		需处理
    private BigDecimal borrowRate   			; //借款利率
    private String borrowRateStr   			; //借款利率
    private Integer borrowRateUnit   			; //借款利率类型ID
    private String borrowRateName		; //借款利率名称 		需处理
    private BigDecimal borrowMoney         ; //借款金额
    private BigDecimal             getMoney 			; //出款金额
    private BigDecimal remianderPrincipal 	; //剩余本金
    private Integer periods             ; //当前还款期数
    private String afterId;
    private Integer delayDays           ; //逾期天数
    private BigDecimal needPayInterest  	; //应付利息
    private BigDecimal needPayPrincipal   	; //应付本金
    private BigDecimal needPayPenalty 		; //应付违约金
    private BigDecimal needPayPlatform 		; //应付平台费
    private BigDecimal needPayGuarantee		; //应付担保费
    private BigDecimal needPaydeposit		; //应付押金
    private BigDecimal needPayRush		; //应付冲应收
    private BigDecimal otherPayAmount 		; //应付其他费用
    private BigDecimal totalBorrowAmount   ; //应付总额
    private  BigDecimal payedPrincipal;//已还金额
    private  BigDecimal preFees;//前置费用
    private BigDecimal sumFactAmount;//出款后已交月收等费用总额 :还款计划中的实收合计（不含本金部分）
    private BigDecimal needPayService;//应付月收服务费
    private int preLateFeesType;//提前还款违约金方式:
    private double outsideInterestType;//合同期外逾期利息类型
    private BigDecimal outsideInterest;//合同期外逾期利息
    private String generalReturnRate;//综合收益率
    private BigDecimal preLateFees;    //提前还款违约金:
    private BigDecimal needPayAgency;//应收中介费
    private BigDecimal totalFactAmount;//实还总额;
    private BigDecimal settleTotalFactAmount;//结清时该业务实还总额;
    private BigDecimal settleNeedPayInterest;//结清时该业应付利息;  
    private BigDecimal settleNeedPayService;//结清时该业应付月收服务费;   
    private BigDecimal noSettleNeedPayInterest;//不结清时该业应付利息;  
    private BigDecimal noSettleNeedPayService;//不结清时该业应付月收服务费; 
    private BigDecimal noSettleNeedPayPrincipal;//不结清时应付本金;
    private Integer noSettleDelayDays;//不结清时的逾期天数
    
    
    public String getZqBusinessId() {
		return zqBusinessId;
	}


	public void setZqBusinessId(String zqBusinessId) {
		this.zqBusinessId = zqBusinessId;
	}


	public Integer getNoSettleDelayDays() {
		return noSettleDelayDays;
	}


	public void setNoSettleDelayDays(Integer noSettleDelayDays) {
		this.noSettleDelayDays = noSettleDelayDays;
	}


	public BigDecimal getNoSettleNeedPayInterest() {
		return noSettleNeedPayInterest;
	}


	public void setNoSettleNeedPayInterest(BigDecimal noSettleNeedPayInterest) {
		this.noSettleNeedPayInterest = noSettleNeedPayInterest;
	}


	public BigDecimal getNoSettleNeedPayService() {
		return noSettleNeedPayService;
	}


	public void setNoSettleNeedPayService(BigDecimal noSettleNeedPayService) {
		this.noSettleNeedPayService = noSettleNeedPayService;
	}


	public BigDecimal getNoSettleNeedPayPrincipal() {
		return noSettleNeedPayPrincipal;
	}


	public void setNoSettleNeedPayPrincipal(BigDecimal noSettleneedPayPrincipal) {
		this.noSettleNeedPayPrincipal = noSettleneedPayPrincipal;
	}


	public BigDecimal getSettleNeedPayInterest() {
		return settleNeedPayInterest;
	}


	public void setSettleNeedPayInterest(BigDecimal settleNeedPayInterest) {
		this.settleNeedPayInterest = settleNeedPayInterest;
	}


	public BigDecimal getSettleNeedPayService() {
		return settleNeedPayService;
	}


	public void setSettleNeedPayService(BigDecimal settleNeedPayService) {
		this.settleNeedPayService = settleNeedPayService;
	}


	public BigDecimal getSettleTotalFactAmount() {
		return settleTotalFactAmount;
	}


	public void setSettleTotalFactAmount(BigDecimal settleTotalFactAmount) {
		this.settleTotalFactAmount = settleTotalFactAmount;
	}


	public BigDecimal getTotalFactAmount() {
		return totalFactAmount;
	}


	public void setTotalFactAmount(BigDecimal totalFactAmount) {
		this.totalFactAmount = totalFactAmount;
	}


	public BigDecimal getNeedPaydeposit() {
		return needPaydeposit;
	}


	public void setNeedPaydeposit(BigDecimal needPaydeposit) {
		this.needPaydeposit = needPaydeposit;
	}


	public BigDecimal getNeedPayRush() {
		return needPayRush;
	}


	public void setNeedPayRush(BigDecimal needPayRush) {
		this.needPayRush = needPayRush;
	}


	public BigDecimal getNeedPayAgency() {
		return needPayAgency;
	}


	public void setNeedPayAgency(BigDecimal needPayAgency) {
		this.needPayAgency = needPayAgency;
	}


	public BigDecimal getNeedPayPlatform() {
		return needPayPlatform;
	}


	public void setNeedPayPlatform(BigDecimal needPayPlatform) {
		this.needPayPlatform = needPayPlatform;
	}


	public BigDecimal getNeedPayGuarantee() {
		return needPayGuarantee;
	}


	public void setNeedPayGuarantee(BigDecimal needPayGuarantee) {
		this.needPayGuarantee = needPayGuarantee;
	}


	public String getAfterId() {
		return afterId;
	}


	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}


	public BigDecimal getOutsideInterest() {
		return outsideInterest;
	}


	public void setOutsideInterest(BigDecimal outsideInterest) {
		this.outsideInterest = outsideInterest;
	}


	public int getPreLateFeesType() {
		return preLateFeesType;
	}


	public void setPreLateFeesType(int preLateFeesType) {
		this.preLateFeesType = preLateFeesType;
	}


	public double getOutsideInterestType() {
		return outsideInterestType;
	}


	public void setOutsideInterestType(double outsideInterestType) {
		this.outsideInterestType = outsideInterestType;
	}


	

	public String getGeneralReturnRate() {
		return generalReturnRate;
	}


	public void setGeneralReturnRate(String generalReturnRate) {
		this.generalReturnRate = generalReturnRate;
	}


	public BigDecimal getPreLateFees() {
		return preLateFees;
	}


	public void setPreLateFees(BigDecimal preLateFees) {
		this.preLateFees = preLateFees;
	}


	public BigDecimal getNeedPayService() {
		return needPayService;
	}


	public void setNeedPayService(BigDecimal needPayService) {
		this.needPayService = needPayService;
	}


	public BigDecimal getSumFactAmount() {
		return sumFactAmount;
	}


	public void setSumFactAmount(BigDecimal sumFactAmount) {
		this.sumFactAmount = sumFactAmount;
	}


	public BigDecimal getPreFees() {
		return preFees;
	}


	public void setPreFees(BigDecimal preFees) {
		this.preFees = preFees;
	}


	public String getBusinessId() {
        return businessId;
    }

     
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

     
    public String getCustomerName() {
        return customerName;
    }

     
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

     
    public String getCompanyId() {
        return companyId;
    }

     
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

     
    public Integer getBusinessType() {
        return businessType;
    }

     
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

     
    public Integer getBorrowLimit() {
        return borrowLimit;
    }

     
    public void setBorrowLimit(Integer borrowLimit) {
        this.borrowLimit = borrowLimit;
    }

     
    public Integer getRepaymentTypeId() {
        return repaymentTypeId;
    }

     
    public void setRepaymentTypeId(Integer repaymentTypeId) {
        this.repaymentTypeId = repaymentTypeId;
    }

    public String getRepaymentTypeName() {
        return repaymentTypeName;
    }

    public void setRepaymentTypeName(String repaymentTypeName) {
        this.repaymentTypeName = repaymentTypeName;
    }

     
    public BigDecimal getBorrowRate() {
        return borrowRate;
    }

    public void setBorrowRate(BigDecimal borrowRate) {
        this.borrowRate = borrowRate;
    }

    public String getBorrowRateName() {
        return borrowRateName;
    }

    public void setBorrowRateName(String borrowRateName) {
        this.borrowRateName = borrowRateName;
    }

     
    public  BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney( BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public  BigDecimal getGetMoney() {
        return getMoney;
    }

    public void setGetMoney( BigDecimal getMoney) {
        this.getMoney = getMoney;
    }

    public  BigDecimal getRemianderPrincipal() {
        return remianderPrincipal;
    }

    public void setRemianderPrincipal( BigDecimal remianderPrincipal) {
        this.remianderPrincipal = remianderPrincipal;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    public  BigDecimal getNeedPayInterest() {
        return needPayInterest;
    }

    public void setNeedPayInterest( BigDecimal needPayInterest) {
        this.needPayInterest = needPayInterest;
    }

    public  BigDecimal getNeedPayPrincipal() {
        return needPayPrincipal;
    }

    public void setNeedPayPrincipal( BigDecimal needPayPrincipal) {
        this.needPayPrincipal = needPayPrincipal;
    }

    public  BigDecimal getNeedPayPenalty() {
        return needPayPenalty;
    }

    public void setNeedPayPenalty( BigDecimal needPayPenalty) {
        this.needPayPenalty = needPayPenalty;
    }

    public  BigDecimal getOtherPayAmount() {
        return otherPayAmount;
    }

    public void setOtherPayAmount( BigDecimal otherPayAmount) {
        this.otherPayAmount = otherPayAmount;
    }

    public  BigDecimal getTotalBorrowAmount() {
        return totalBorrowAmount;
    }

    public void setTotalBorrowAmount( BigDecimal totalBorrowAmount) {
        this.totalBorrowAmount = totalBorrowAmount;
    }

    public Integer getBorrowRateUnit() {
        return borrowRateUnit;
    }

    public void setBorrowRateUnit(Integer borrowRateUnit) {
        this.borrowRateUnit = borrowRateUnit;
    }

    public String getBorrowRateStr() {
        return borrowRateStr;
    }

    public void setBorrowRateStr(String borrowRateStr) {
        this.borrowRateStr = borrowRateStr;
    }

    public BigDecimal getPayedPrincipal() {
        return payedPrincipal;
    }

    public void setPayedPrincipal(BigDecimal payedPrincipal) {
        this.payedPrincipal = payedPrincipal;
    }
}
