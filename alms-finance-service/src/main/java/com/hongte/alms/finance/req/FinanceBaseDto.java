package com.hongte.alms.finance.req;

import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class FinanceBaseDto {

    private String businessId;
    private String orgBusinessId;
    private String afterId;
    private List<CurrPeriodProjDetailVO> projListDetails;

    private List<RepaymentResource> repaymentResources;

    private RepaymentBizPlanDto planDto;// 业务还款计划dto
    /**
     * 总应还金额（减去实还后的金额）
     */
    private BigDecimal repayPlanAmount = BigDecimal.ZERO;

    /**
     * 总实还金额
     */
    private BigDecimal repayFactAmount = BigDecimal.ZERO;
    /**
     * 总银行流水金额
     */
    private BigDecimal moneyPoolAmount = BigDecimal.ZERO;
    /**
     * 本次还款后,结余金额
     */
    private BigDecimal surplusAmount = BigDecimal.ZERO;
    /**
     * 本次还款后,缺多少金额
     */
    private BigDecimal lackAmount = BigDecimal.ZERO;

    private String remark;
    private Boolean save;
    private RepaymentConfirmLog confirmLog;
    private RepaymentBizPlanBak repaymentBizPlanBak;
    private RepaymentBizPlanListBak repaymentBizPlanListBak;
    private List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks;
    private List<RepaymentProjPlanBak> repaymentProjPlanBaks;
    private List<RepaymentProjPlanListBak> repaymentProjPlanListBaks;
    private List<RepaymentProjPlanListDetailBak> repaymentProjPlanListDetailBaks;

    // 调用来源的标志位
    private Integer callFlage;
    private Set<RepaymentProjPlanListDetail> updatedProjPlanDetails;
    /**
     * 新增还款金额list
     * Map<plan_list_detail_id, List<RepaymentProjFactRepay>>
     */
    private Map<String, List<RepaymentProjFactRepay>> projFactRepays ;
    
    /**
     * 实还金额list,和projFactRepays意义一样,只是在内存存储方式不一样
     */
    private List<RepaymentProjFactRepay> projFactRepayArray = new ArrayList<>() ;
    
    ///////// 还款过程中相关变量（payOneFeeDetail 函数中赋值） 开始 zk ///////////////////////

    // 当前用到第几条还款来源的标志位
    private Integer resourceIndex;
    // 当前用于分账的还款来源
    private RepaymentResource curalResource;
    // 当前用于分账的金额（对应还款来源）
    private BigDecimal curalDivideAmount = BigDecimal.ZERO;
    // 本次调用实还金额
    private BigDecimal realPayedAmount = BigDecimal.ZERO;

    private String userId;

    private String userName;
    
    /**
     * 减免金额使用记录,initFinanceBase时设置此属性
     */
    private List<DerateUseLog> derateUseLogs ;
    /**
     * 本次还款已完成的标的还款计划
     */
    private List<RepaymentProjPlanList> curTimeRepaidProjPlanList ;
}
