package com.hongte.alms.base.collection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zengkun
 * @since 2018/1/30
 * 贷后首页台账字段类
 */
//@Entity
//@Table(name = "t_user")
@ExcelTarget("afterLoanStandingBookVo")
public class AfterLoanStandingBookVo implements java.io.Serializable  {

    private Logger logger = LoggerFactory.getLogger(AfterLoanStandingBookVo.class);

    @Excel(name = "业务编号", orderNum = "1",  isImportField = "true_st")
    private String businessId; //业务编号

//    @Excel(name = "期数", orderNum = "2", isImportField = "true_st")
    private Integer periods; //期数

    @Excel(name = "期数", orderNum = "2", isImportField = "true_st")
    private  String afterId;//总期数字段

    private String districtId;  // 顶级区域ID 对应tb_basic_company中 district_id
    @Excel(name = "区域名称", orderNum = "3",   isImportField = "true_st")
    private String districtAreaName; //顶级区域名称

    private String companyId;//分公司ID 对应tb_basic_business 中分公司ID
    @Excel(name = "分公司", orderNum = "4",   isImportField = "true_st")
    private String companyName;//分公司 对应 tb_basic_company中

    private String operatorId; //业务主办人ID 对应 tb_basic_business业务主办人ID
    @Excel(name = "业务获取", orderNum = "5",   isImportField = "true_st")
    private String operatorName; //业务获取 对应tb_basic_business表中业务主办人姓名

//    private String customerId; //用户ID 对应 tb_basic_business 表中 客户ID
    @Excel(name = "用户姓名", orderNum = "6",   isImportField = "true_st")
    private String customerName; //用户姓名  对应 tb_basic_business 表中 客户名称

    private Integer businessTypeId; //业务类型Id  对应 tb_basic_business_type 表中 业务类型Id
    @Excel(name = "业务类型", orderNum = "7",   isImportField = "true_st")
    private String businessTypeName; //业务类型名称  对应 tb_basic_business_type 表中 业务类型名称

    @Excel(name = "借款金额", orderNum = "8",   isImportField = "true_st")
    private double borrowMoney;    //借款金额  对应tb_basic_business表中 borrow_money借款金额(元)字段

    @Excel(name = "应还金额", orderNum = "9",   isImportField = "true_st")
    private double totalBorrowAmount;    //本期应还款金额  对应tb_repayment_biz_plan_list表中 total_borrow_amount 总计划应还金额字段

    @Excel(name = "逾期天数", orderNum = "10",   isImportField = "true_st")
    private Integer delayDays; //逾期天数  对应 当前日期减去应还款日期



    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "应还日期", orderNum = "11", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
    private Date dueDate;   //应还日期  对应 tb_repayment_biz_plan_list 表中 应还日期

    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "实还日期", orderNum = "12", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20)
    private Date repaymentDate;   //实还日期

    @Excel(name = "清算一", orderNum = "13",  isImportField = "true_st")
    private String phoneStaffName;          //电催人员名字  (清算一)
    private String phoneStaffId;           //电催人员ID    (清算一)

    @Excel(name = "清算二", orderNum = "14",   isImportField = "true_st")
    private String visitStaffName;          //上门催收人员名字   (清算二)
    private String visitStaffId;           //上门催收人员ID  (清算二)

    private String crpId;           //还款计划表ID  对应业务还款计划列表主键(tb_repayment_business_plan_list.plan_list_id)


//    private Integer status; //还款状态
    @Excel(name = "还款状态", orderNum = "15",   isImportField = "true_st")
    private String statusName; //还款状态 名称

    @Excel(name = "业务状态", orderNum = "16",   isImportField = "true_st")
    private String afterColStatusName;//贷后状态 名称
    private Integer  colStatus;  //贷后状态 数据库值

    private Integer borrowLimit ;//借款期限,用于判断是否最后一期
    private Integer repaymentTypeId ;//还款方式
    @Excel(name = "业务类别", orderNum = "17",   isImportField = "true_st")
    private String className; // 分类名称
    
    @Excel(name = "期数状态", orderNum = "18",   isImportField = "true_st")
    private String peroidStatus ;//期数状态,首期/本金期/末期
    
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictAreaName() {
        return districtAreaName;
    }

    public void setDistrictAreaName(String districtAreaName) {
        this.districtAreaName = districtAreaName;
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

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Integer businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public double getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(double borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public double getTotalBorrowAmount() {
        return totalBorrowAmount;
    }

    public void setTotalBorrowAmount(double totalBorrowAmount) {
        this.totalBorrowAmount = totalBorrowAmount;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    @JsonFormat(pattern="yyyy-MM-dd" , timezone = "GMT+8")
    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getPhoneStaffName() {
        return phoneStaffName;
    }

    public void setPhoneStaffName(String phoneStaffName) {
        this.phoneStaffName = phoneStaffName;
    }

    public String getPhoneStaffId() {
        return phoneStaffId;
    }

    public void setPhoneStaffId(String phoneStaffId) {
        this.phoneStaffId = phoneStaffId;
    }

    public String getVisitStaffName() {
        return visitStaffName;
    }

    public void setVisitStaffName(String visitStaffName) {
        this.visitStaffName = visitStaffName;
    }

    public String getVisitStaffId() {
        return visitStaffId;
    }

    public void setVisitStaffId(String visitStaffId) {
        this.visitStaffId = visitStaffId;
    }

    public String getCrpId() {
        return crpId;
    }

    @JsonFormat(pattern="yyyy-MM-dd" , timezone = "GMT+8")
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCrpId(String crpId) {
        this.crpId = crpId;
    }


//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getAfterId() {
        return afterId;
    }

    public void setAfterId(String afterId) {
        this.afterId = afterId;
    }

    public String getAfterColStatusName() {
        return afterColStatusName;
    }

    public void setAfterColStatusName(String afterColStatusName) {
        this.afterColStatusName = afterColStatusName;
    }

    public Integer getColStatus() {
        return colStatus;
    }

    public void setColStatus(Integer colStatus) {
        this.colStatus = colStatus;
    }

	/**
	 * @return the borrowLimit
	 */
	public Integer getBorrowLimit() {
		return borrowLimit;
	}

	/**
	 * @param borrowLimit the borrowLimit to set
	 */
	public void setBorrowLimit(Integer borrowLimit) {
		this.borrowLimit = borrowLimit;
	}

	/**
	 * @return the repaymentTypeId
	 */
	public Integer getRepaymentTypeId() {
		return repaymentTypeId;
	}

	/**
	 * @param repaymentTypeId the repaymentTypeId to set
	 */
	public void setRepaymentTypeId(Integer repaymentTypeId) {
		this.repaymentTypeId = repaymentTypeId;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the peroidStatus
	 */
	public String getPeroidStatus() {
		
		return peroidStatus;
	}

	/**
	 * @param peroidStatus the peroidStatus to set
	 */
	public void setPeroidStatus() {
		String res = "" ;



		boolean isZQ = this.afterId.indexOf("ZQ")>-1?true:false;
		if (getPeriods().equals(new Integer(1))&&!isZQ) {
			res = res.concat("首期");
		}
        if(getRepaymentTypeId() ==null || getBorrowLimit() == null ||getPeriods()== null ){
                logger.error("getRepaymentTypeId ,getBorrowLimit,getPeriods 其中一项为空。 ");
            if(getBusinessId() !=null){
                logger.error("getBusinessId 为："+getBusinessId());
            }
            if(getAfterId()!=null){
                logger.error("getAfterId 为："+getAfterId());
            }
            return;
        }
		if (getRepaymentTypeId().equals(new Integer(2))&&getBorrowLimit().equals(getPeriods())) {
			if (res.length()>0) {
				res = res.concat("/");
			}
			res = res.concat("本金期");
		}
		if (getRepaymentTypeId().equals(new Integer(5))&&getBorrowLimit().equals(getPeriods())) {
			if (res.length()>0) {
				res = res.concat("/");
			}
			res = res.concat("末期");
		}
		this.peroidStatus = res;
	}
}
