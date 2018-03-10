package com.hongte.alms.base.collection.vo;

import com.hongte.alms.base.enums.RepayPlanStatus;

import java.util.Date;

/**
 * 移交催收业务数据vo
 */
public class StaffBusinessVo {
    private String businessId; //业务编号
    private Integer periods; //期数
    private String districtId;  // 顶级区域ID 对应tb_basic_company中 district_id
    private String districtAreaName; //顶级区域名称
    private String companyId;//分公司ID 对应tb_basic_business 中分公司ID
    private String companyName;//分公司 对应 tb_basic_company中
    private String operatorId; //业务主办人ID 对应 tb_basic_business业务主办人ID
    private String operatorName; //业务获取 对应tb_basic_business表中业务主办人姓名
    private String customerId; //用户ID 对应 tb_basic_business 表中 客户ID
    private String customerName; //用户姓名  对应 tb_basic_business 表中 客户名称
    private Integer businessTypeId; //业务类型Id  对应 tb_basic_business_type 表中 业务类型Id
    private String businessTypeName; //业务类型名称  对应 tb_basic_business_type 表中 业务类型名称
    private double borrowMoney;    //借款金额  对应tb_basic_business表中 借款金额(元)字段
    private double totalBorrowAmount;    //本期应还款金额  对应tb_repayment_business_plan_list.total_borrow_amount  总计划应还金额(元)，不含滞纳金
    private String crpId;           //还款计划表ID  对应业务还款计划列表主键(tb_repayment_business_plan_list.plan_list_id)
    private String repaymentBatchId; //业务的还款计划批次编号  对应 tb_repayment_business_plan.repayment_batch_id 还款计划批次号



    private Date dueDate;   //应还日期  对应 tb_repayment_business_plan_list 表中 应还日期 due_date
    private Integer overDueDays;//逾期天数  对应 tb_repayment_business_plan_list 表中  overdue_days
//    private Integer status; //还款状态  1:还款中 2:已还款 3逾期 4:部分还款  对应 tb_customer_repayment_plan 表中 status
    private String statusName; //还款状态 名称


//    public static StaffBusinessVo getDefaultVo(){
//        StaffBusinessVo vo = new StaffBusinessVo();
//        vo.setBorrowDate(new Date());
//        vo.setBorrowMoney(100);
//        vo.setBusinessId("bb");
//        vo.setBusinessTypeId(1);
//        vo.setBusinessTypeName("业务类型");
//        vo.setCompanyId("companyid");
//        vo.setCompanyName("公司名称");
//        vo.setCustomerId("cId");
//        vo.setCustomerName("客户名称");
//        vo.setPeriods(2);
//        vo.setDistrictAreaName("区域名称");
//        vo.setDistrictId("disId");
//        vo.setOperatorId("oprId");
//        vo.setOperatorName("操作人");
//        vo.setTotalBorrowAmount(10);
////        vo.setStatus(1);
//        vo.setCrpId("crp1");
//        vo.setStatusName(RepayPlanStatus.nameOf(vo.getStatus()));
//        return vo;
//    }

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

    public String getRepaymentBatchId() {
        return repaymentBatchId;
    }

    public void setRepaymentBatchId(String repaymentBatchId) {
        this.repaymentBatchId = repaymentBatchId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getOverDueDays() {
        return overDueDays;
    }

    public void setOverDueDays(Integer overDueDays) {
        this.overDueDays = overDueDays;
    }

    public String getCrpId() {
        return crpId;
    }

    public void setCrpId(String crpId) {
        this.crpId = crpId;
    }
}
