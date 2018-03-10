package com.hongte.alms.open.vo;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by huanghui on 2018/1/15.
 */
public class BusinessRepaymentPlan {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    /**
     *主键
     */
    private String Id;

    public String getBusinessId() {
        return BusinessId;
    }

    public BigDecimal getBorrowAmount() {
        return BorrowAmount;
    }

    public BigDecimal getAccrualAmount() {
        return AccrualAmount;
    }

    public BigDecimal getActualPaymentAmount() {
        return ActualPaymentAmount;
    }

    public Data getBorrowDate() {
        return BorrowDate;
    }

    public DateTimeLiteralExpression.DateTime getActualTime() {
        return ActualTime;
    }

    public Integer getStatus() {
        return Status;
    }

    public String getBorrowUserId() {
        return BorrowUserId;
    }

    public DateTimeLiteralExpression.DateTime getCreateTime() {
        return CreateTime;
    }

    public void setBusinessId(String businessId) {
        BusinessId = businessId;
    }

    public void setBorrowAmount(BigDecimal borrowAmount) {
        BorrowAmount = borrowAmount;
    }

    public void setAccrualAmount(BigDecimal accrualAmount) {
        AccrualAmount = accrualAmount;
    }

    public void setActualPaymentAmount(BigDecimal actualPaymentAmount) {
        ActualPaymentAmount = actualPaymentAmount;
    }

    public void setBorrowDate(Data borrowDate) {
        BorrowDate = borrowDate;
    }

    public void setActualTime(DateTimeLiteralExpression.DateTime actualTime) {
        ActualTime = actualTime;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public void setBorrowUserId(String borrowUserId) {
        BorrowUserId = borrowUserId;
    }

    public void setCreateTime(DateTimeLiteralExpression.DateTime createTime) {
        CreateTime = createTime;
    }

    public String BusinessId;

    public BigDecimal BorrowAmount;

    public BigDecimal AccrualAmount;

    public BigDecimal ActualPaymentAmount;

    public Data BorrowDate;

    public DateTimeLiteralExpression.DateTime ActualTime;

    public Integer Status;

    public String BorrowUserId;

    public DateTimeLiteralExpression.DateTime CreateTime;
}
