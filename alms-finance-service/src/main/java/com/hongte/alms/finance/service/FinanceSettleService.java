package com.hongte.alms.finance.service;

import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;

import java.util.Date;
import java.util.List;

public interface FinanceSettleService {
    List<CurrPeriodProjDetailVO> financeSettle(FinanceSettleReq financeSettleReq);
    void makeRepaymentPlanAllPlan(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq);
    SettleInfoVO settleInfoVO(FinanceSettleReq financeSettleReq) ;
}
