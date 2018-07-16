package com.hongte.alms.finance.req;

import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.entity.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class FinanceSettleBaseDto {


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

//    private RepaymentBizPlanDto planDto=new RepaymentBizPlanDto();// 业务还款计划dto

    private List<RepaymentBizPlanDto> planDtoList=new ArrayList<>();

    private RepaymentSettleLog repaymentSettleLog;
    private List<RepaymentBizPlanBak> repaymentBizPlanBaks=new ArrayList<>();
    private List<RepaymentBizPlanListBak> repaymentBizPlanListBaks=new ArrayList<>();
    private List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks=new ArrayList<>();
    private List<RepaymentProjPlanBak> repaymentProjPlanBaks=new ArrayList<>();
    private List<RepaymentProjPlanListBak> repaymentProjPlanListBaks=new ArrayList<>();
    private List<RepaymentProjPlanListDetailBak> repaymentProjPlanListDetailBaks=new ArrayList<>();



    private List<RepaymentResource> repaymentResources;

    private String userId;

    private String userName;


    private String uuid;

    private String businessId;
    private String planId;

    /**
     * 当前期标志实体,根据此实体判断是否提前结清
     */
    private RepaymentBizPlanList curPeriod ;

}
