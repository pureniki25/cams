package com.hongte.alms.finance.service;

import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanSettleDto;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;

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

    /**
     *
     * 财务结清,获取当前期
     * @author 王继光
     * 2018年7月25日 下午9:52:16
     * @param req
     * @return
     */
    List<RepaymentBizPlanSettleDto> getCurrentPeriod(FinanceSettleReq req,FinanceSettleBaseDto financeSettleBaseDto);
    
    /**
     * 获取当前期
     * @param req
     * @return
     */
    List<RepaymentBizPlanList> getCurrenPeroids(com.hongte.alms.base.vo.finance.FinanceSettleReq req);
    
}
