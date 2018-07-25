package com.hongte.alms.finance.req;

import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FinanceSettleBaseDto {


    private String businessId;
    private String orgBusinessId;
    private String userId;


    private String userName;
    private String uuid;
     //临时变量
    private String projPlanId;
    private String planId;
    private String afterId;
    private String projectId;

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

    private RepaymentBizPlanDto planDto=new RepaymentBizPlanDto();

    private RepaymentSettleLog repaymentSettleLog;
    private List<RepaymentBizPlanBak> repaymentBizPlanBaks=new ArrayList<>();
    private List<RepaymentBizPlanListBak> repaymentBizPlanListBaks=new ArrayList<>();
    private List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks=new ArrayList<>();
    private List<RepaymentProjPlanBak> repaymentProjPlanBaks=new ArrayList<>();
    private List<RepaymentProjPlanListBak> repaymentProjPlanListBaks=new ArrayList<>();
    private List<RepaymentProjPlanListDetailBak> repaymentProjPlanListDetailBaks=new ArrayList<>();


    /**
     * 新增还款金额list  按planId
     * Map<plan_list_detail_id, List<RepaymentProjFactRepay>>
     */
    private Map<String,Map<String, List<RepaymentProjFactRepay>>> projFactRepays =new HashMap<>();


    private Map<String, CurrPeriodProjDetailVO> webFactRepays=new HashMap<>();

    // 调用来源的标志位
    private Integer callFlage;


    // 当前用到第几条还款来源的标志位
    private Integer resourceIndex=0;
    // 当前用于分账的还款来源
    private RepaymentResource curalResource;
    // 当前用于分账的金额（对应还款来源）
    private BigDecimal curalDivideAmount = BigDecimal.ZERO;
    // 本次调用实还金额
    private BigDecimal realPayedAmount ;

    private List<RepaymentResource> repaymentResources=new ArrayList<>();



    /**
     * 当前期标志实体,根据此实体判断是否提前结清
     */
    private RepaymentBizPlanList curPeriod ;
    
    /**
     * 提前结清标志位
     */
    private Boolean preSettle ;
    
    /**
     * 是否预览  true 预览 false 保存
     */
    private Boolean preview ;

  //页面填充Dto
   private List<CurrPeriodProjDetailVO> currPeriodProjDetailVOList=new ArrayList<>();

   private List<RepaymentSettleLogDetail> repaymentSettleLogDetailList=new ArrayList<>();

   private List<RepaymentProjPlanList> ptojPlanList = new ArrayList<>();

}
