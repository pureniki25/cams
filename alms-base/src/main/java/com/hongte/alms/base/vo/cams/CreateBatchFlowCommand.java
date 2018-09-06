package com.hongte.alms.base.vo.cams;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.ussp.core.Result;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class CreateBatchFlowCommand extends MessageCommand {

    private String batchId;

    private Integer actionId;

    private Integer businessFrom;

    private Integer businessFlag;

    private String createUser;

    private Business business;

    private List<Flow> flows=new ArrayList<>();

    private List<FlowDetail> flowDetails=new ArrayList<>();

    private List<FlowAccountIdentifier> accountIdentifiers=new ArrayList<>();

    public FlowAccountIdentifier getFlowAccountIdentifier(String accountIdentifierId){
        return accountIdentifiers.stream().filter(m->accountIdentifierId.compareToIgnoreCase(m.getIdentifierId())==0).findFirst().get();
    }
    
    public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

//    @JsonIgnore
    public String getBatchId() {
        return this.batchId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }


    public Integer getBusinessFrom() {
        return businessFrom;
    }

    public void setBusinessFrom(Integer businessFrom) {
        this.businessFrom = businessFrom;
    }

    public Integer getBusinessFlag() {
        return businessFlag;
    }

    public void setBusinessFlag(Integer businessFlag) {
        this.businessFlag = businessFlag;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }

    public List<FlowDetail> getFlowDetails() {
        return flowDetails;
    }

    public void setFlowDetails(List<FlowDetail> flowDetails) {
        this.flowDetails = flowDetails;
    }

    public List<FlowAccountIdentifier> getAccountIdentifiers() {
        return accountIdentifiers;
    }

    public void setAccountIdentifiers(List<FlowAccountIdentifier> accountIdentifiers) {
        this.accountIdentifiers = accountIdentifiers;
    }

    public static class Business{
        private String customerName;

        private String businessId;

        private String exhibitionId;

        private String businessType;

        private String businessTypeId;

        private String branchId;

        private String branchName;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public String getExhibitionId() {
            return exhibitionId;
        }

        public void setExhibitionId(String exhibitionId) {
            this.exhibitionId = exhibitionId;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getBusinessTypeId() {
            return businessTypeId;
        }

        public void setBusinessTypeId(String businessTypeId) {
            this.businessTypeId = businessTypeId;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }
    }

    public static class Flow{
        @JsonProperty("sid")
        private String sourceAccountIdentifierId;

        @JsonProperty("tid")
        private String targetAccountIdentifierId;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @JSONField(format="yyyy-MM-dd HH:mm:ss")
        private Date accountTime;

        private BigDecimal amount;

        private Integer inOut;

        private String remark;

        private String externalId;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        @JSONField(format="yyyy-MM-dd")
        private Date segmentationDate;

        private String afterId;

        private String issueId;

        private Integer repayType;
        
        private String memo;

        public String getSourceAccountIdentifierId() {
            return sourceAccountIdentifierId;
        }

        public void setSourceAccountIdentifierId(String sourceAccountIdentifierId) {
            this.sourceAccountIdentifierId = sourceAccountIdentifierId;
        }

        public String getTargetAccountIdentifierId() {
            return targetAccountIdentifierId;
        }

        public void setTargetAccountIdentifierId(String targetAccountIdentifierId) {
            this.targetAccountIdentifierId = targetAccountIdentifierId;
        }

        public Date getAccountTime() {
            return accountTime;
        }

        public void setAccountTime(Date accountTime) {
            this.accountTime = accountTime;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Integer getInOut() {
            return inOut;
        }

        public void setInOut(Integer inOut) {
            this.inOut = inOut;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public Date getSegmentationDate() {
            return segmentationDate;
        }

        public void setSegmentationDate(Date segmentationDate) {
            this.segmentationDate = segmentationDate;
        }

        public String getAfterId() {
            return afterId;
        }

        public void setAfterId(String afterId) {
            this.afterId = afterId;
        }

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public Integer getRepayType() {
            return repayType;
        }

        public void setRepayType(Integer repayType) {
            this.repayType = repayType;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

    }

    public static class FlowDetail{
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @JSONField(format="yyyy-MM-dd HH:mm:ss")
        private Date accountTime;

        private BigDecimal amount;

        private String feeId;

        private String feeName;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        @JSONField(format="yyyy-MM-dd")
        private Date segmentationDate;

        private String afterId;

        private String issueId;

        private Integer registerType;

        public Date getAccountTime() {
            return accountTime;
        }

        public void setAccountTime(Date accountTime) {
            this.accountTime = accountTime;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getFeeId() {
            return feeId;
        }

        public void setFeeId(String feeId) {
            this.feeId = feeId;
        }

        public String getFeeName() {
            return feeName;
        }

        public void setFeeName(String feeName) {
            this.feeName = feeName;
        }

        public Date getSegmentationDate() {
            return segmentationDate;
        }

        public void setSegmentationDate(Date segmentationDate) {
            this.segmentationDate = segmentationDate;
        }

        public String getAfterId() {
            return afterId;
        }

        public void setAfterId(String afterId) {
            this.afterId = afterId;
        }

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public Integer getRegisterType() {
            return registerType;
        }

        public void setRegisterType(Integer registerType) {
            this.registerType = registerType;
        }
    }

    /**
     * 流水账户标识
     */
    public static class FlowAccountIdentifier{

        @JsonIgnore
        private Result<Account> account;

        @NotBlank
        @ApiModelProperty(value = "唯一标识")
        private String identifierId;

        @Length(max=50)
        @ApiModelProperty(value = "存管编号")
        private String depositoryId;

        @Length(max=100)
        @ApiModelProperty(value = "账户名称")
        private String accountName;

        @ApiModelProperty( value = "账户类型")
        private Integer accountType;

        @ApiModelProperty(value = "账户主体编号,个人是身份证,企业是统一信用编码或者营业执照")
        @Length(max=50)
        private String mainId;

        @Pattern(regexp = "(\\d|\\s){10,50}", message = "银行卡格式错误")
        @ApiModelProperty(value = "银行卡号")
        private String bankCardNo;

        @ApiModelProperty(value = "开户行")
        @Length(max=50)
        private String openBank;

        @JsonProperty("isPersonal")
        @ApiModelProperty(value = "是否是个人账户")
        private Boolean isPersonal;

        @Override
        public String toString() {
            StringBuilder sb=new StringBuilder();
            Field[] fields= this.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object val= null;
                try {
                    val = field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(val==null||val=="") continue;

                sb.append(String.format("%s:%s,",field.getName(),val));
            }

            if(sb.length()==0) return "所有标识均为空";

            return sb.toString();
        }

        public Result<Account> getAccount() {
            return account;
        }

        public void setAccount(Result<Account> account) {
            this.account = account;
        }

        @JsonProperty("isPersonal")
        public Boolean isPersonal() {
            return isPersonal;
        }

        @JsonProperty("isPersonal")
        public void setPersonal(Boolean personal) {
            isPersonal = personal;
        }

        public String getIdentifierId() {
            return identifierId;
        }

        public void setIdentifierId(String identifierId) {
            this.identifierId = identifierId;
        }

        public String getDepositoryId() {
            return depositoryId;
        }

        public void setDepositoryId(String depositoryId) {
            this.depositoryId = depositoryId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public Integer getAccountType() {

            if(this.accountType!=null) return accountType;

            if(this.isPersonal()) return 0;

            return -1;
        }

        public void setAccountType(Integer accountType) {
            this.accountType = accountType;
        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public String getBankCardNo() {
            return bankCardNo;
        }

        public void setBankCardNo(String bankCardNo) {
            this.bankCardNo =  bankCardNo;
        }

        public String getOpenBank() {
            return openBank;
        }

        public void setOpenBank(String openBank) {
            this.openBank = openBank;
        }
    }
}
