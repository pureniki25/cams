package com.hongte.alms.finance.service;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
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
    /**
     * 全部业务结清的场景下,查找当前期的planListId
     * @author 王继光
     * 2018年7月25日 下午5:19:58
     * @param now
     * @return
     */
    List<String> curPeriod(RepaymentBizPlanList now) ;
}
