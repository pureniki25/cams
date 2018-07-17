package com.hongte.alms.finance.service;

import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.finance.req.FinanceSettleReq;

import java.util.List;

public interface FinanceSettleService {
    List<CurrPeriodProjDetailVO> financeSettle(FinanceSettleReq financeSettleReq);
}
