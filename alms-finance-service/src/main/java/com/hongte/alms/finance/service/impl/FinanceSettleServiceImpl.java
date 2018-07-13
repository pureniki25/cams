package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("FinanceSettleService")
public class FinanceSettleServiceImpl implements FinanceSettleService {
    private static Logger logger = LoggerFactory.getLogger(FinanceSettleServiceImpl.class);
    @Autowired
    BasicBusinessMapper basicBusinessMapper;
    @Autowired
    RepaymentBizPlanListMapper repaymentBizPlanListMapper;
    @Autowired
    RepaymentBizPlanMapper repaymentBizPlanMapper;
    @Autowired
    RepaymentResourceMapper repaymentResourceMapper;
    @Autowired
    RepaymentProjFactRepayMapper repaymentProjFactRepayMapper;
    @Autowired
    RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;
    @Autowired
    RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper;
    @Autowired
    RepaymentProjPlanListMapper repaymentProjPlanListMapper;
    @Autowired
    TuandaiProjectInfoMapper tuandaiProjectInfoMapper;
    @Autowired
    RepaymentProjPlanMapper repaymentProjPlanMapper;
    @Autowired
    MoneyPoolMapper moneyPoolMapper;
    @Autowired
    ApplyDerateProcessMapper applyDerateProcessMapper;
    @Autowired
    ApplyDerateTypeMapper applyDerateTypeMapper;
    @Autowired
    ProcessMapper processMapper;
    @Autowired
    AccountantOverRepayLogMapper accountantOverRepayLogMapper;
    @Autowired
    RepaymentConfirmLogMapper confirmLogMapper;

    @Autowired
    MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;
    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    RepaymentSettleLogMapper repaymentSettleLogMapper;

    @Autowired
    RepaymentProjPlanBakMapper repaymentProjPlanBakMapper;

    @Autowired
    RepaymentProjPlanListBakMapper repaymentProjPlanListBakMapper;

    @Autowired
    RepaymentProjPlanListDetailBakMapper repaymentProjPlanListDetailBakMapper;

    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    RepaymentConfirmLogService confirmLogService;

    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    private RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

    @Autowired
    @Qualifier("RepaymentProjPlanListDetailService")
    private RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

    @Autowired
    private TransferOfLitigationMapper transferOfLitigationMapper;


    @Override
    public void financeSettle(FinanceSettleReq financeSettleReq) {
        FinanceSettleBaseDto financeSettleBaseDto = new FinanceSettleBaseDto();

        //查询所有的应还
        countRepayPlanAmount(financeSettleBaseDto, financeSettleReq);
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);

        //填充标的还款计划数据及应还金额
//        makeRepaymentPlan(financeSettleBaseDto, financeSettleReq);

        //填充还款计划相关信息
        makeRepaymentPlan(financeSettleBaseDto, financeSettleReq);
        BigDecimal repayFactAmount = financeSettleBaseDto.getRepayPlanAmount(); //应还

        //匹配资金流水
        makeMonePoolRepayment(financeSettleBaseDto, financeSettleReq);
        BigDecimal moneyPoolAmount = financeSettleBaseDto.getMoneyPoolAmount(); //流水总金额
        financeSettleBaseDto.setMoneyPoolAmount(moneyPoolAmount);


        //开始结清


//        insertRepaymentSettleLog(financeSettleBaseDto, financeSettleReq);

    }


    public void ss(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {
        String businessId = financeSettleReq.getBusinessId();
        String planIdReq = financeSettleReq.getPlanId();
        String uuid = financeSettleBaseDto.getUuid();
        List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planIdReq).orderBy("create_time"));
        if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {
            for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
                String planId = repaymentBizPlan.getPlanId();

                List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanMapper.selectList(new EntityWrapper<RepaymentProjPlan>().eq("plan_id", planId));
                if (CollectionUtils.isNotEmpty(repaymentProjPlans)) {
                    List<RepaymentSettleSortDto> settleSortList = new ArrayList<>();
                    for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
                        RepaymentSettleSortDto repaymentSettleSortDto = new RepaymentSettleSortDto();
                        repaymentSettleSortDto.setRepaymentProjPlan(repaymentProjPlan);
                        String projectId = repaymentProjPlan.getProjectId();

                        //填充团贷网平台业务上标信息
                        TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoMapper.selectById(projectId);
                        repaymentSettleSortDto.setTuandaiProjectInfo(tuandaiProjectInfo);
                        settleSortList.add(repaymentSettleSortDto);
                    }

                    if (CollectionUtils.isNotEmpty(settleSortList)) {
                        settleSort(settleSortList);


                        for (RepaymentSettleSortDto repaymentSettleSortDto : settleSortList) {
                            RepaymentProjPlan repaymentProjPlan = repaymentSettleSortDto.getRepaymentProjPlan();
                            String projPlanId = repaymentProjPlan.getProjPlanId();

                            //标的还款计划列表
                            List<String> planListIds = new ArrayList<>();
                            List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlanId));
                            if (CollectionUtils.isNotEmpty(repaymentProjPlanLists)) {
                                for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
                                    String planListId = repaymentProjPlanList.getPlanListId();
                                    String projPlanListId = repaymentProjPlanList.getProjPlanListId();
                                    if (!planListIds.contains(planListId)) {
                                        planListIds.add(planListId);
                                    }

                                    RepaymentSettlePlanListDetailDto repaymentSettlePlanListDetailDto=new RepaymentSettlePlanListDetailDto();
                                    //标的还款计划列表
                                    List<String> planListDetailIds = new ArrayList<>();
                                    List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId));
                                    //填充标的还款计划列表
                                    repaymentSettlePlanListDetailDto.setRepaymentProjPlanListDetails(repaymentProjPlanListDetails);

                                    if(CollectionUtils.isNotEmpty(repaymentProjPlanListDetails)){
                                        for(RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails){
                                            String planDetailId = repaymentProjPlanListDetail.getPlanDetailId();
                                            if (!planListDetailIds.contains(planDetailId)) {
                                                planListDetailIds.add(planDetailId);
                                            }
                                        }
                                        //查询业务还款详情计划
                                        if (planListDetailIds != null && planListDetailIds.size() == 1) {
                                            String planListDetailId = planListDetailIds.get(0);
                                            RepaymentBizPlanListDetail repaymentBizPlanListDetail = repaymentBizPlanListDetailMapper.selectById(planListDetailId);
                                            repaymentSettlePlanListDetailDto.setRepaymentBizPlanListDetail(repaymentBizPlanListDetail);
                                        }
                                    }

                                   //====================================进行详情记录的结清开始
                                    settleCanceMoney(repaymentSettlePlanListDetailDto);
                                    //====================================进行详情记录的结清结束
                                }

                                //查询业务还款计划
                                if (planListIds != null && planListIds.size() == 1) {
                                    String planListId = planListIds.get(0);
                                    RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListMapper.selectById(planListId);


                                }
                            }


                        }

                    }
                }


            }
        }
    }

    public void settleCanceMoney(RepaymentSettlePlanListDetailDto repaymentSettlePlanListDetailDto){
        List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentSettlePlanListDetailDto.getRepaymentProjPlanListDetails();

        if(CollectionUtils.isNotEmpty(repaymentProjPlanListDetails)){

        }
        RepaymentBizPlanListDetail repaymentBizPlanListDetail = repaymentSettlePlanListDetailDto.getRepaymentBizPlanListDetail();




    }

    //拆分还款的规则应优先还共借标的，在还主借标的，若有多个共借标，则有优先还上标金额较小的标的，若共借标中的金额先等，则优先还满标时间较早的标的。
    public void settleSort(List<RepaymentSettleSortDto> repaymentSettleSortDto) {
        Collections.sort(repaymentSettleSortDto, new Comparator<RepaymentSettleSortDto>() {
            // 排序规则说明 需补充 从小标到大标，再到主借标
            //同等
            @Override
            public int compare(RepaymentSettleSortDto arg0, RepaymentSettleSortDto arg1) {
                if (arg0.getTuandaiProjectInfo().getMasterIssueId().equals(arg0.getTuandaiProjectInfo().getProjectId())) {
                    return 1;
                } else if (arg1.getTuandaiProjectInfo().getMasterIssueId().equals(arg1.getTuandaiProjectInfo().getProjectId())) {
                    return -1;
                }
                if (arg0.getRepaymentProjPlan().getBorrowMoney()
                        .compareTo(arg1.getRepaymentProjPlan().getBorrowMoney()) < 0) {
                    return -1;
                }
                if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .before(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
                    return -1;
                } else if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .after(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
                    return 1;
                }
                return 0;
            }

        });
    }


    public void cancelProjMoney(FinanceSettleBaseDto financeSettleBaseDto) {
        String uuid = financeSettleBaseDto.getUuid();
        String userId = financeSettleBaseDto.getUserId();

        BigDecimal moneyPoolAmount = financeSettleBaseDto.getMoneyPoolAmount();

        List<RepaymentBizPlanDto> planDtoList = financeSettleBaseDto.getPlanDtoList();
        if (CollectionUtils.isNotEmpty(planDtoList)) {
            for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
                List<RepaymentProjPlanDto> projPlanDtos = repaymentBizPlanDto.getProjPlanDtos();
//                settleSort(projPlanDtos);//排序
                for (RepaymentProjPlanDto repaymentProjPlanDto : projPlanDtos) {

                    Boolean flag = true;//是否已结清
                    List<RepaymentProjPlanListDto> projPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
                    RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();

                    //备份list表
                    RepaymentProjPlanBak repaymentProjPlanBak = new RepaymentProjPlanBak();
                    repaymentProjPlanBak.setSettleLogId(uuid);
                    BeanUtils.copyProperties(repaymentProjPlan, repaymentProjPlanBak);
                    repaymentProjPlanBakMapper.insert(repaymentProjPlanBak);

                    String businessId = repaymentProjPlan.getBusinessId();
                    String originalBusinessId = repaymentProjPlan.getOriginalBusinessId();
                    String projectId = repaymentProjPlan.getProjectId();


                    if (CollectionUtils.isNotEmpty(projPlanListDtos)) {
                        for (RepaymentProjPlanListDto repaymentProjPlanListDto : projPlanListDtos) {
                            RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListDto.getRepaymentProjPlanList();

                            //备份list表
                            RepaymentProjPlanListBak repaymentProjPlanListBak = new RepaymentProjPlanListBak();
                            repaymentProjPlanListBak.setSettleLogId(uuid);
                            BeanUtils.copyProperties(repaymentProjPlanList, repaymentProjPlanListBak);
                            repaymentProjPlanListBakMapper.insert(repaymentProjPlanListBak);


                            String afterId = repaymentProjPlanList.getAfterId();
                            //业务还款计划列表ID
                            String planListId = repaymentProjPlanList.getPlanListId();
                            //标的期数
                            Integer period = repaymentProjPlanList.getPeriod();

                            BigDecimal unpaid = repaymentProjPlanListDto.getUnpaid();
                            if (moneyPoolAmount.compareTo(unpaid) > 0 || moneyPoolAmount.compareTo(unpaid) == 0) {  //所有还完才进行更改状态
                                repaymentProjPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
                                repaymentProjPlanListMapper.updateById(repaymentProjPlanList);
                            } else {
                                flag = false;//
                            }
                            List<RepaymentProjPlanListDetail> projPlanListDetails = repaymentProjPlanListDto.getProjPlanListDetails();
                            if (CollectionUtils.isNotEmpty(projPlanListDetails)) {
                                for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {

                                    String feeId = repaymentProjPlanListDetail.getFeeId();
                                    String planItemName = repaymentProjPlanListDetail.getPlanItemName();
                                    Integer planItemType = repaymentProjPlanListDetail.getPlanItemType();

                                    BigDecimal projPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
                                    BigDecimal derateAmount = repaymentProjPlanListDetail.getDerateAmount();
                                    BigDecimal projFactAmount = repaymentProjPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : repaymentProjPlanListDetail.getProjFactAmount();

                                    BigDecimal repayAmount = projPlanAmount.subtract(derateAmount).subtract(projFactAmount);
                                    if (moneyPoolAmount.compareTo(repayAmount) < 0) { //如果待减金额小于应还金额
                                        return;
                                    } else {
                                        moneyPoolAmount = moneyPoolAmount.subtract(repayAmount);
                                    }

                                    //备份详情表
                                    RepaymentProjPlanListDetailBak repaymentProjPlanListDetailBak = new RepaymentProjPlanListDetailBak();
                                    repaymentProjPlanListDetailBak.setSettleLogId(uuid);
                                    BeanUtils.copyProperties(repaymentProjPlanListDetail, repaymentProjPlanListDetailBak);
                                    repaymentProjPlanListDetailBakMapper.insert(repaymentProjPlanListDetailBak);

                                    //更新详情
                                    repaymentProjPlanListDetail.setProjPlanAmount(projPlanAmount.subtract(derateAmount));
                                    repaymentProjPlanListDetail.setFactRepayDate(new Date());
                                    repaymentProjPlanListDetailMapper.updateById(repaymentProjPlanListDetail);

                                    RepaymentProjFactRepay repaymentProjFactRepay = new RepaymentProjFactRepay();
                                    repaymentProjFactRepay.setProjPlanDetailId(repaymentProjPlanListDetail.getProjPlanDetailId());
                                    repaymentProjFactRepay.setProjPlanListId(planListId);
                                    repaymentProjFactRepay.setPlanListId(planListId);
                                    repaymentProjFactRepay.setProjectId(projectId);
                                    repaymentProjFactRepay.setOrigBusinessId(originalBusinessId);

                                    repaymentProjFactRepay.setBusinessId(businessId);
                                    repaymentProjFactRepay.setAfterId(afterId);
                                    repaymentProjFactRepay.setPeriod(period);
                                    repaymentProjFactRepay.setFeeId(feeId);
                                    repaymentProjFactRepay.setPlanItemName(planItemName);
                                    repaymentProjFactRepay.setPlanItemType(planItemType);
                                    repaymentProjFactRepay.setFactAmount(repayAmount);
                                    repaymentProjFactRepay.setFactRepayDate(new Date());
                                    //repaymentProjFactRepay.setRepayRefId();
                                    repaymentProjFactRepay.setCreateDate(new Date());
                                    repaymentProjFactRepay.setCreateUser(userId);
                                    repaymentProjFactRepay.setUpdateUser(userId);
                                    repaymentProjFactRepay.setSettleLogId(uuid);

                                    repaymentProjFactRepayMapper.insert(repaymentProjFactRepay);


                                }
                            }

                        }


                    }


                }
            }
        }
    }


    public void insertRepaymentSettleLog(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq
            financeSettleReq) {
        RepaymentSettleLog repaymentSettleLog = new RepaymentSettleLog();

        repaymentSettleLog.setBusinessId(financeSettleReq.getBusinessId());
//        repaymentSettleLog.setOrgBusinessId();

        repaymentSettleLogMapper.insert(repaymentSettleLog);
        financeSettleBaseDto.setRepaymentSettleLog(repaymentSettleLog);
    }


    public void countRepayPlanAmount(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {
        String businessId = financeSettleReq.getBusinessId();
        String planId = financeSettleReq.getPlanId();
        BigDecimal repayPlanAmount = repaymentProjPlanMapper.countRepayPlanAmount(businessId, planId);

//        financeSettleBaseDto.setRepayPlanAmount(repayPlanAmount);
    }


    public void makeRepaymentPlan(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {
        List<RepaymentBizPlanDto> planDtoList = financeSettleBaseDto.getPlanDtoList();

        String businessId = financeSettleReq.getBusinessId();
        String planIdW = financeSettleReq.getPlanId();
        String uuid = financeSettleBaseDto.getUuid();
        List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planIdW));

        if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {
            List<RepaymentBizPlanBak> repaymentBizPlanBaks = financeSettleBaseDto.getRepaymentBizPlanBaks();
            for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
                RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto();
                repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);

                String planId = repaymentBizPlan.getPlanId();
                financeSettleBaseDto.setPlanId(planId);
                financeSettleBaseDto.setBusinessId(businessId);

                //==============================标的还款信息填充开始
                makeRepaymentProjPlan(repaymentBizPlanDto, financeSettleBaseDto);
                //==============================标的还款信息填充结束
                planDtoList.add(repaymentBizPlanDto);


                RepaymentBizPlanBak repaymentBizPlanBak = new RepaymentBizPlanBak();
                BeanUtils.copyProperties(repaymentBizPlan, repaymentBizPlanBak);
                repaymentBizPlanBak.setSettleLogId(uuid);
                repaymentBizPlanBaks.add(repaymentBizPlanBak);


                List<RepaymentBizPlanList> repaymentBizPlanListList = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planId));

                if (CollectionUtils.isNotEmpty(repaymentBizPlanListList)) {
                    List<RepaymentBizPlanListBak> repaymentBizPlanListBaks = financeSettleBaseDto.getRepaymentBizPlanListBaks();
                    List<RepaymentBizPlanListDto> bizPlanListDtos = new ArrayList<>();
                    for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanListList) {

                        RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
                        repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);


                        RepaymentBizPlanListBak repaymentBizPlanListBak = new RepaymentBizPlanListBak();
                        BeanUtils.copyProperties(repaymentBizPlanList, repaymentBizPlanListBak);
                        repaymentBizPlanListBaks.add(repaymentBizPlanListBak);

                        String planListId = repaymentBizPlanList.getPlanListId();

                        String currentStatus = repaymentBizPlanList.getCurrentStatus(); //标的还款状态

                        if (RepayCurrentStatusEnums.还款中.toString().equals(currentStatus) || RepayCurrentStatusEnums.逾期.toString().equals(currentStatus)) {
                            List<RepaymentBizPlanListDetail> repaymentBizPlanListDetailList = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planListId));

                            if (CollectionUtils.isNotEmpty(repaymentBizPlanListDetailList)) {
                                List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks = financeSettleBaseDto.getRepaymentBizPlanListDetailBaks();
                                repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetailList);

                                for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetailList) {

                                    RepaymentBizPlanListDetailBak repaymentBizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
                                    BeanUtils.copyProperties(repaymentBizPlanListDetail, repaymentBizPlanListDetailBak);
                                    repaymentBizPlanListDetailBaks.add(repaymentBizPlanListDetailBak);
                                }
                            }
                        }

                        bizPlanListDtos.add(repaymentBizPlanListDto);
                    }
                    repaymentBizPlanDto.setBizPlanListDtos(bizPlanListDtos);
                }

            }

        }

    }


    public void makeMonePoolRepayment(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {
        BigDecimal moneyPoolAmount = financeSettleBaseDto.getMoneyPoolAmount();
        List<String> mprIds = financeSettleReq.getMprIds();
        List<MoneyPoolRepayment> moneyPoolRepaymentList = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().in("id", mprIds));
        if (!CollectionUtils.isEmpty(moneyPoolRepaymentList)) {
            for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepaymentList) {
                BigDecimal accountMoney = moneyPoolRepayment.getAccountMoney();
                //累加匹配流水
                moneyPoolAmount = moneyPoolAmount.add(accountMoney);
            }
        }

        financeSettleBaseDto.setMoneyPoolAmount(moneyPoolAmount);
    }


    //组装标的还款计划信息
    public void makeRepaymentProjPlan(RepaymentBizPlanDto repaymentBizPlanDto, FinanceSettleBaseDto baseDto) {
        String businessId = baseDto.getBusinessId();
        String planId = baseDto.getPlanId();

        String uuid = baseDto.getUuid();
        BigDecimal shouldDetailAmount = baseDto.getRepayPlanAmount(); //应还金额

        //查询本次业务下的所有还款计划 计算应还项
        List<RepaymentProjPlan> repaymentProjPlansList = repaymentProjPlanMapper.selectList(new EntityWrapper<RepaymentProjPlan>().eq("business_id", businessId).eq("plan_id", planId));

        if (CollectionUtils.isNotEmpty(repaymentProjPlansList)) {
            List<RepaymentProjPlanDto> projPlanDtos = new ArrayList<>();
            List<RepaymentProjPlanBak> repaymentProjPlanBaks = baseDto.getRepaymentProjPlanBaks();

            for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlansList) {
                String projectId = repaymentProjPlan.getProjectId();
                RepaymentProjPlanBak repaymentProjPlanBak = new RepaymentProjPlanBak();
                String projPlanId = repaymentProjPlan.getProjPlanId();
                RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto();
                repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan); //添加还款计划信息

                //填充团贷网平台业务上标信息
                TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoMapper.selectById(projectId);
                repaymentProjPlanDto.setTuandaiProjectInfo(tuandaiProjectInfo);

                //备份标的还款记录表
                BeanUtils.copyProperties(repaymentProjPlan, repaymentProjPlanBak);
                repaymentProjPlanBak.setSettleLogId(uuid);
//                repaymentProjPlanBakMapper.insert(repaymentProjPlanBak);
                repaymentProjPlanBaks.add(repaymentProjPlanBak);


                List<RepaymentProjPlanList> repaymentProjPlanListList = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlanId));
                if (CollectionUtils.isNotEmpty(repaymentProjPlanListList)) {
                    List<RepaymentProjPlanListBak> repaymentProjPlanListBaks = baseDto.getRepaymentProjPlanListBaks();
                    List<RepaymentProjPlanListDto> projPlanListDtos = new ArrayList<>();
                    for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanListList) {
                        RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();

                        //备份标的还款计划列表
                        RepaymentProjPlanListBak repaymentProjPlanListBak = new RepaymentProjPlanListBak();
                        BeanUtils.copyProperties(repaymentProjPlanList, repaymentProjPlanListBak);
                        repaymentProjPlanListBak.setSettleLogId(uuid);
//                        repaymentProjPlanListBakMapper.insert(repaymentProjPlanListBak);
                        repaymentProjPlanListBaks.add(repaymentProjPlanListBak);

                        BigDecimal unpaid = repaymentProjPlanListDto.getUnpaid();


                        //标的应还项目明细ID
                        String projPlanListId = repaymentProjPlanList.getProjPlanListId();
                        String currentStatus = repaymentProjPlanList.getCurrentStatus(); //标的还款状态

                        if (RepayCurrentStatusEnums.还款中.toString().equals(currentStatus) || RepayCurrentStatusEnums.逾期.toString().equals(currentStatus)) {
                            //标的还款计划
                            List<RepaymentProjPlanListDetail> repaymentProjPlanListDetailList = repaymentProjPlanListDetailMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId).orderBy("share_profit_index"));

                            if (CollectionUtils.isNotEmpty(repaymentProjPlanListDetailList)) {
                                List<RepaymentProjPlanListDetailBak> repaymentProjPlanListDetailBaks = baseDto.getRepaymentProjPlanListDetailBaks();
                                repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetailList);

                                for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetailList) {
                                    //备份标的还款计划列表详情
                                    RepaymentProjPlanListDetailBak repaymentProjPlanListDetailBak = new RepaymentProjPlanListDetailBak();
                                    BeanUtils.copyProperties(repaymentProjPlanListDetail, repaymentProjPlanListDetailBak);
                                    repaymentProjPlanListDetailBak.setSettleLogId(uuid);
//                                    repaymentProjPlanListDetailBakMapper.insert(repaymentProjPlanListDetailBak);

                                    repaymentProjPlanListDetailBaks.add(repaymentProjPlanListDetailBak);

                                    //应还
                                    BigDecimal projPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
                                    //减免
                                    BigDecimal derateAmount = repaymentProjPlanListDetail.getDerateAmount();
                                    //应实还
                                    BigDecimal mayAmount = projPlanAmount.subtract(derateAmount);
                                    //实还
                                    BigDecimal projFactAmount = repaymentProjPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : repaymentProjPlanListDetail.getProjFactAmount();

                                    //标的差额
                                    unpaid = unpaid.add(projPlanAmount.subtract(derateAmount).subtract(projFactAmount));

                                    //插入实还信息及更新详情实还
//                                    insertRepaymentProjFactRepay( financeSettleBaseDto, repaymentProjPlan, repaymentProjPlanList, repaymentProjPlanListDetail);

                                }
                                baseDto.setRepaymentProjPlanListDetailBaks(repaymentProjPlanListDetailBaks);
                            }
                        }

                        //填充标的还款计划列表信息
                        repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
                        repaymentProjPlanListDto.setUnpaid(unpaid);
                        projPlanListDtos.add(repaymentProjPlanListDto);
                        shouldDetailAmount = shouldDetailAmount.add(unpaid);
                    }
                    repaymentProjPlanDto.setProjPlanListDtos(projPlanListDtos);
                }
                projPlanDtos.add(repaymentProjPlanDto);
            }
            repaymentBizPlanDto.setProjPlanDtos(projPlanDtos);
        }
        baseDto.setRepayPlanAmount(shouldDetailAmount);
    }


    /**
     * 更新标实还明细表
     *
     * @param repaymentProjPlanListDetail
     */
    public void insertRepaymentProjFactRepay(FinanceSettleBaseDto financeSettleBaseDto, RepaymentProjPlan
            repaymentProjPlan, RepaymentProjPlanList repaymentProjPlanList, RepaymentProjPlanListDetail
                                                     repaymentProjPlanListDetail) {
        String afterId = repaymentProjPlanList.getAfterId();
        //业务还款计划列表ID
        String planListId = repaymentProjPlanList.getPlanListId();
        //标的期数
        Integer period = repaymentProjPlanList.getPeriod();
        String businessId = repaymentProjPlan.getBusinessId();
        String originalBusinessId = repaymentProjPlan.getOriginalBusinessId();
        String projectId = repaymentProjPlan.getProjectId();

        String settleLogId = financeSettleBaseDto.getUuid();
        String userId = financeSettleBaseDto.getUserId();


        //应还
        BigDecimal projPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
        //减免
        BigDecimal derateAmount = repaymentProjPlanListDetail.getDerateAmount();
        //应实还
        BigDecimal mayAmount = projPlanAmount.subtract(derateAmount);
        //实还
        BigDecimal projFactAmount = repaymentProjPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : repaymentProjPlanListDetail.getProjFactAmount();

        //明细项的差额
        BigDecimal reduceAmount = projPlanAmount.subtract(derateAmount).subtract(projFactAmount);


        String feeId = repaymentProjPlanListDetail.getFeeId();
        String planItemName = repaymentProjPlanListDetail.getPlanItemName();
        Integer planItemType = repaymentProjPlanListDetail.getPlanItemType();

        if (projPlanAmount.subtract(derateAmount).compareTo(projFactAmount) > 0) { //证明还有未还清
            RepaymentProjFactRepay repaymentProjFactRepay = new RepaymentProjFactRepay();
            repaymentProjFactRepay.setProjPlanDetailId(repaymentProjPlanListDetail.getProjPlanDetailId());
            repaymentProjFactRepay.setProjPlanListId(planListId);
            repaymentProjFactRepay.setPlanListId(planListId);
            repaymentProjFactRepay.setProjectId(projectId);
            repaymentProjFactRepay.setOrigBusinessId(originalBusinessId);

            repaymentProjFactRepay.setBusinessId(businessId);
            repaymentProjFactRepay.setAfterId(afterId);
            repaymentProjFactRepay.setPeriod(period);
            repaymentProjFactRepay.setFeeId(feeId);
            repaymentProjFactRepay.setPlanItemName(planItemName);
            repaymentProjFactRepay.setPlanItemType(planItemType);
            repaymentProjFactRepay.setFactAmount(reduceAmount);
            repaymentProjFactRepay.setFactRepayDate(new Date());
//            repaymentProjFactRepay.setRepayRefId();
            repaymentProjFactRepay.setCreateDate(new Date());
            repaymentProjFactRepay.setCreateUser(userId);
            repaymentProjFactRepay.setUpdateUser(userId);
            repaymentProjFactRepay.setSettleLogId(settleLogId);

            repaymentProjFactRepayMapper.insert(repaymentProjFactRepay);

            //更新详情标还款记录
            repaymentProjPlanListDetail.setProjFactAmount(mayAmount);
            repaymentProjPlanListDetail.setUpdateDate(new Date());
            repaymentProjPlanListDetailMapper.updateById(repaymentProjPlanListDetail);


        }
    }


}
