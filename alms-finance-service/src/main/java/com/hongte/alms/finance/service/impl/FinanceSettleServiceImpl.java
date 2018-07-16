package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanPayedTypeEnum;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.finance.req.FinanceBaseDto;
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
//        countRepayPlanAmount(baseDto, financeSettleReq);
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);

        //匹配资金流水
        makeMonePoolRepayment(financeSettleBaseDto, financeSettleReq);
        BigDecimal moneyPoolAmount = financeSettleBaseDto.getMoneyPoolAmount(); //流水总金额

        //填充标的还款计划数据及应还金额
        makeRepaymentPlan(financeSettleBaseDto, financeSettleReq);
        BigDecimal repayFactAmount = financeSettleBaseDto.getRepayPlanAmount(); //应还


        //开始结清


//        insertRepaymentSettleLog(financeSettleBaseDto, financeSettleReq);

    }


    public void settleCanceMoney(RepaymentSettlePlanListDetailDto repaymentSettlePlanListDetailDto) {
        List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentSettlePlanListDetailDto.getRepaymentProjPlanListDetails();

        if (CollectionUtils.isNotEmpty(repaymentProjPlanListDetails)) {

        }
        RepaymentBizPlanListDetail repaymentBizPlanListDetail = repaymentSettlePlanListDetailDto.getRepaymentBizPlanListDetail();


    }

    //拆分还款的规则应优先还共借标的，在还主借标的，若有多个共借标，则有优先还上标金额较小的标的，若共借标中的金额先等，则优先还满标时间较早的标的。
    public void settleSort(List<RepaymentProjPlanDto> repaymentProjPlanDtos) {
        Collections.sort(repaymentProjPlanDtos, new Comparator<RepaymentProjPlanDto>() {
            // 排序规则说明 需补充 从小标到大标，再到主借标
            //同等
            @Override
            public int compare(RepaymentProjPlanDto arg0, RepaymentProjPlanDto arg1) {
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


                List<RepaymentProjPlanList> repaymentProjPlanListList = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlanId).orderBy("period"));
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

    //进行分润
    public void shareSettleMoney(FinanceSettleBaseDto baseDto) {
        List<RepaymentBizPlanDto> planDtoLists = baseDto.getPlanDtoList();
        BigDecimal moneyPoolAmount = baseDto.getMoneyPoolAmount();

        if (CollectionUtils.isNotEmpty(planDtoLists)) {
            for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoLists) {

                List<RepaymentProjPlanDto> projPlanDtos = repaymentBizPlanDto.getProjPlanDtos();
                //进行排序
                settleSort(projPlanDtos);


               //


            }
        }
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

    /**
     * 分配规则变更，重新写
     */
    private void fillNew(RepaymentBizPlanDto repaymentBizPlanDto,BigDecimal realPayedAmount) {
//        RepaymentBizPlanDto dto = repaymentBizPlanDto.getRepaymentBizPlan();
        List<RepaymentProjPlanDto> projPlanDtos = repaymentBizPlanDto.getProjPlanDtos();

        // 上一次还款是否成功的标志位
        boolean lastPaySuc = true;


        // 3.最后按核销顺序还金额（先还核销顺序小于1200的费用）
        String lastProjectId = null;
        for (int i = 0; i < projPlanDtos.size(); i++) {
            if (lastPaySuc == false)
                return;
            RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
            String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
            lastProjectId = projectId;
//            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
//            CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId, projListDetails);

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            // 遍历标的还款计划
            for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto.getRepaymentProjPlanListDetailDtos();
                // //遍历这个标的每一期还款计划，费用细项
                for (RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                    RepaymentProjPlanListDetail detail = repaymentProjPlanListDetailDto.getRepaymentProjPlanListDetail();
                    if(detail.getShareProfitIndex().compareTo(1200)>=0){
                        continue;
                    }
//                    boolean bl = payOneFeeDetail(detail, null, repaymentBizPlanDto);
//                    if (!bl && realPayedAmount != null) {
//                        lastPaySuc = false;
//                        break;
//                    }
                }
            }
        }

        //再还核销顺序大于等于1200的费用项
        for (int i = 0; i < projPlanDtos.size(); i++) {
            if (lastPaySuc == false)
                return;
            RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
            String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
            lastProjectId = projectId;
//            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
//            CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId, projListDetails);

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            // 遍历标的还款计划
            for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto.getRepaymentProjPlanListDetailDtos();
                // //遍历这个标的每一期还款计划，费用细项
                for (RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                    RepaymentProjPlanListDetail detail = repaymentProjPlanListDetailDto.getRepaymentProjPlanListDetail();
                    if(detail.getShareProfitIndex().compareTo(1200)<0){
                        continue;
                    }
//                    boolean bl = payOneFeeDetail(detail,  null, repaymentBizPlanDto);
//                    if (!bl && realPayedAmount != null) {
//                        lastPaySuc = false;
//                        break;
//                    }
                }
            }
        }

//        // 结余
//        BigDecimal surplusFund = new BigDecimal("0");
//        // 如果最后一次还款都还足了，就计算结余
//        if (lastPaySuc) {
//            surplusFund = surplusFund.add(financeBaseDto.getCuralDivideAmount());
//            boolean setBl = true;
//            while (setBl == true) {
//                setBl = setNewRepaymentResource(financeBaseDto.getResourceIndex() + 1, financeBaseDto);
//                if (setBl) {
//                    surplusFund = surplusFund.add(financeBaseDto.getCuralDivideAmount());
//                }
//            }
//        }
        // 将结余设置到最后一个标
//        if (lastProjectId != null) {
//            CurrPeriodProjDetailVO lastPeriodProjDetailVO = getCurrPeriodProjDetailVO(lastProjectId, financeBaseDto.getProjListDetails());
//            lastPeriodProjDetailVO.setSurplus(surplusFund);
//        }

    }


    /**
     * 还一个费用详情的费用
     *
     * @param detail                 本次还的费用项
     * @param currPeriodProjDetailVO 当前还的标的详情
     * @param maxPayMoney            本次调用可还的最大金额
     * @return boolean
     */
     /*
    private boolean payOneFeeDetail(RepaymentProjPlanListDetail detail,BigDecimal maxPayMoney, RepaymentBizPlanDto repaymentBizPlanDto) {

//        realPayedAmount.set(null);
        financeBaseDto.setRealPayedAmount(null);
        Integer rIdex = financeBaseDto.getResourceIndex();
        if (rIdex == null || rIdex < 0) {
            boolean bl = setNewRepaymentResource(0, financeBaseDto);
            if (!bl)
                return bl;
        }

        // 本次调用此方法实还金额总和
        BigDecimal realPayed = new BigDecimal(0);

        // 当前用于分账的还款来源
        // RepaymentResource curalResource = rResources.get(resourceIndex);
        // //当前用于分账的金额（对应还款来源）
        // BigDecimal curalDivideAmount = curalResource.getRepayAmount();
        // 未支付金额
        BigDecimal unpaid = detail.getProjPlanAmount()
                .subtract(detail.getDerateAmount() == null ? new BigDecimal("0") : detail.getDerateAmount())
                .subtract(detail.getProjFactAmount());
        // 如果有最大金额限制，则取未支付金额和最大限制金额的小值
        if (maxPayMoney != null) {
            if (maxPayMoney.compareTo(unpaid) < 0) {
                unpaid = maxPayMoney;
            }
        }
        // 实还金额
        BigDecimal money = new BigDecimal("0");
        BigDecimal curalDivideAmount = financeBaseDto.getCuralDivideAmount();

        int c = curalDivideAmount.compareTo(unpaid);
        if (c > 0) {
            logger.info("divideAmount大于unpaid");
            logger.info("@@从divideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            financeBaseDto.setCuralDivideAmount(curalDivideAmount.subtract(unpaid));
//            curalDivideAmount.set(curalDivideAmount.subtract(unpaid));
            logger.info("divideAmount变为{}", curalDivideAmount);
            createProjFactRepay(money, detail, currPeriodProjDetailVO, financeBaseDto.getCuralResource(), financeBaseDto);
            realPayed = money;
            financeBaseDto.setRealPayedAmount(realPayed);
//            realPayedAmount.set(realPayed);
            return true;
        } else if (c == 0) {
            logger.info("divideAmount等于unpaid");
            logger.info("@@从divideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            logger.info("divideAmount变为null", detail.getPlanItemName());
            // 创建实还流水
            createProjFactRepay(money, detail, currPeriodProjDetailVO, financeBaseDto.getCuralResource(), financeBaseDto);
            realPayed = money;
            // 上一条还款来源的可用金额已用完，找下一条还款来源来用
//            curalDivideAmount.set(null);
            financeBaseDto.setCuralDivideAmount(null);
            boolean setBl = setNewRepaymentResource(financeBaseDto.getResourceIndex() + 1, financeBaseDto);
            financeBaseDto.setRealPayedAmount(realPayed);
//            realPayedAmount.set(realPayed);
            return setBl ;
        } else {
            logger.info("divideAmount少于unpaid");
            logger.info("@@从divideAmount={}分unpaid={}到{}", curalDivideAmount, curalDivideAmount,
                    detail.getPlanItemName());
            money = financeBaseDto.getCuralDivideAmount();
//            money = curalDivideAmount.get();
            unpaid = unpaid.subtract(money);
            createProjFactRepay(money, detail, currPeriodProjDetailVO, financeBaseDto.getCuralResource(), financeBaseDto);
//            curalDivideAmount.set(null);
            financeBaseDto.setCuralDivideAmount(null);
            boolean setBl = setNewRepaymentResource(financeBaseDto.getResourceIndex() + 1, financeBaseDto);
            realPayed = money;
            // 如果成功取到下一条还款流水 剩余未还完的继续还
            if (setBl) {
                boolean pRet = payOneFeeDetail(detail, currPeriodProjDetailVO, unpaid, financeBaseDto);
                if (pRet && financeBaseDto.getRealPayedAmount() != null) {
                    realPayed = realPayed.add(financeBaseDto.getRealPayedAmount());
                }
                financeBaseDto.setRealPayedAmount(realPayed);
//                realPayedAmount.set(realPayed);
                return pRet;
            } else {// 取不到下一条流水
                financeBaseDto.setRealPayedAmount(realPayed);
//                realPayedAmount.set(realPayed);
                return false;
            }
        }

    }

 */
    private CurrPeriodProjDetailVO getCurrPeriodProjDetailVO(String projectId, List<CurrPeriodProjDetailVO> projListDetails) {
        for (CurrPeriodProjDetailVO currPeriodProjDetailVO : projListDetails) {
            if (currPeriodProjDetailVO.getProject().equals(projectId)) {
                return currPeriodProjDetailVO;
            }
        }
        return null;
    }


    /**
     * 根据RepaymentProjPlanListDetail和实还金额创建RepaymentProjFactRepay
     *
     * @param divideAmount
     * @param detail
     * @param vo
     * @return
     * @author 王继光 2018年5月24日 下午11:45:26
     */
    private RepaymentProjFactRepay createProjFactRepay(BigDecimal divideAmount, RepaymentProjPlanListDetail detail,
                                                       CurrPeriodProjDetailVO vo, RepaymentResource resource, FinanceBaseDto financeBaseDto) {
        RepaymentProjFactRepay fact = new RepaymentProjFactRepay();
        fact.setAfterId(financeBaseDto.getAfterId());
        fact.setBusinessId(financeBaseDto.getBusinessId());
        fact.setCreateDate(new Date());
        fact.setCreateUser(financeBaseDto.getUserId());
        fact.setOrigBusinessId(detail.getOrigBusinessId());
        fact.setProjectId(vo.getProject());
        fact.setPeriod(detail.getPeriod());
        fact.setPlanItemName(detail.getPlanItemName());
        fact.setPlanItemType(detail.getPlanItemType());
        fact.setFeeId(detail.getFeeId());
        fact.setPlanListId(detail.getPlanListId());
        fact.setProjPlanDetailId(detail.getProjPlanDetailId());
        fact.setProjPlanListId(detail.getProjPlanListId());
        fact.setFactRepayDate(resource.getRepayDate());// 还款来源日期
        fact.setRepayRefId(resource.getRepaySourceRefId());// 还款来源id
        fact.setRepaySourceId(resource.getResourceId());
        fact.setRepaySource(Integer.valueOf(resource.getRepaySource()));// 还款来源类别
        fact.setConfirmLogId(financeBaseDto.getConfirmLog().getConfirmLogId());
        fact.setFactAmount(divideAmount);
        detail.setProjFactAmount(detail.getProjFactAmount().add(divideAmount));
        rendCurrPeriodProjDetailVO(divideAmount, detail, vo);
        if (financeBaseDto.getSave()) {
            fact.setProjPlanDetailRepayId(UUID.randomUUID().toString());
            fact.insert();
            addProjFactRepays(financeBaseDto,detail.getPlanDetailId(),fact);

            detail.setRepaySource(Integer.valueOf(resource.getRepaySource()));
            detail.setFactRepayDate(resource.getRepayDate());
            detail.setUpdateDate(new Date());
            detail.setUpdateUser(loginUserInfoHelper.getUserId());
            detail.updateById();
//            financeBaseDto.getUpdatedProjPlanDetails().add(detail);
//            updatedProjPlanDetails.get().add(detail);
            // update(detail);
        }

        return fact;
    }

    /**
     * 将填充到实还的资金拷贝一份填充到CurrPeriodProjDetailVO
     *
     * @param amount
     * @param detail
     * @param vo
     * @author 王继光 2018年5月24日 下午11:44:50
     */
    private void rendCurrPeriodProjDetailVO(BigDecimal amount, RepaymentProjPlanListDetail detail,
                                            CurrPeriodProjDetailVO vo) {
        switch (detail.getPlanItemType()) {
            case 10:
                vo.setItem10(vo.getItem10().add(amount));
                break;
            case 20:
                vo.setItem20(vo.getItem20().add(amount));
                break;
            case 30:
                vo.setItem30(vo.getItem30().add(amount));
                break;
            case 50:
                vo.setItem50(vo.getItem50().add(amount));
                break;
            case 60:
                if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                    vo.setOnlineOverDue(vo.getOnlineOverDue().add(amount));
                }
                if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                    vo.setOfflineOverDue(vo.getOfflineOverDue().add(amount));
                }
                break;
            default:
                logger.info("难道是这里!!!{}||{}||{}", detail.getPlanItemName(), detail.getPlanItemType(), amount);
                break;
        }
    }

    /**
     * 将RepaymentProjFactRepay保存在内存中
     * @author 王继光
     * 2018年7月9日 下午9:59:35
     * @param financeBaseDto
     * @param bizPlanListDetailId
     * @param fact
     */
    private void addProjFactRepays(FinanceBaseDto financeBaseDto,String bizPlanListDetailId,RepaymentProjFactRepay fact) {
        List<RepaymentProjFactRepay> list = financeBaseDto.getProjFactRepays().get(bizPlanListDetailId);
        if (list==null) {
            list = new ArrayList<>();
        }
        list.add(fact);
        financeBaseDto.getProjFactRepays().put(bizPlanListDetailId, list);
    }

    /**
     * 设置新的还款来源信息，成功获取并设置则返回true，没有还款来源了，返回false
     *
     * @param reIndex
     */
    private boolean setNewRepaymentResource(Integer reIndex, FinanceBaseDto financeBaseDto) {
        List<RepaymentResource> rResources = financeBaseDto.getRepaymentResources();

        // 超出了还款来源列表的长度，则返回false
        if (reIndex > rResources.size() - 1) {
            financeBaseDto.setCuralResource(null);
            financeBaseDto.setCuralDivideAmount(null);

//            curalResource.set(null);
//            curalDivideAmount.set(null);
            return false;
        }

        financeBaseDto.setResourceIndex(reIndex);
//        resourceIndex.set(reIndex);
        RepaymentResource resource = rResources.get(reIndex);
        financeBaseDto.setCuralResource(resource);
//        curalResource.set(resource);
        financeBaseDto.setCuralDivideAmount(resource.getRepayAmount());
//        curalDivideAmount.set(resource.getRepayAmount());

        return true;
    }
    
    /**
     * 更新还款计划状态并持久化
     * @author 王继光
     * 2018年7月16日 上午10:43:30
     * @param dto
     */
    private void updateStatus(FinanceSettleBaseDto dto,FinanceSettleReq req) {
    	if (req.getPreview()) {
			return ;
		}
    	
    	List<RepaymentBizPlanDto> planDtoList = dto.getPlanDtoList();
    	for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
			//先更新标的
    		updateProjPlanList(repaymentBizPlanDto.getProjPlanDtos());
    		//再更新业务
    		updateBizPlanList(repaymentBizPlanDto.getBizPlanListDtos());
		}
    }
    
    private void updateProjPlanList(List<RepaymentProjPlanDto> projPlanDtos) {
    	for (RepaymentProjPlanDto repaymentProjPlanDto : projPlanDtos) {
    		RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
    		boolean deficitSettle = false ;
			List<RepaymentProjPlanListDto> projPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				RepaymentProjPlanList projPlanList = projPlanListDto.getRepaymentProjPlanList();
				BigDecimal projPlanAmount = projPlanList.getTotalBorrowAmount() ;
				BigDecimal projFactAmount = BigDecimal.ZERO ;
				BigDecimal projOverAmount = projPlanList.getOverdueAmount()!=null?projPlanList.getOverdueAmount():BigDecimal.ZERO;
				BigDecimal projDerateAmout = projPlanList.getDerateAmount()!=null?projPlanList.getDerateAmount():BigDecimal.ZERO;
				
				List<RepaymentProjPlanListDetail> projPlanListDetails = projPlanListDto.getProjPlanListDetails();
				for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDetails) {
					projFactAmount = projFactAmount.add(projPlanListDetail.getProjFactAmount()==null?BigDecimal.ZERO:projPlanListDetail.getProjFactAmount());
				}
				Collections.sort(projPlanListDetails, new Comparator<RepaymentProjPlanListDetail>() {
					@Override
					public int compare(RepaymentProjPlanListDetail arg0, RepaymentProjPlanListDetail arg1) {
						if (arg0.getFactRepayDate().before(arg1.getFactRepayDate())) {
							return -1 ;
						}
						if (arg0.getFactRepayDate().after(arg1.getFactRepayDate())) {
							return 1 ;
						}
						return 0;
					}
				});
				projPlanList.setFactRepayDate(projPlanListDetails.get(0).getFactRepayDate());
				if (projPlanAmount.add(projOverAmount).compareTo(projDerateAmout.add(projFactAmount)) <= 0) {
					projPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
					projPlanList.setCurrentSubStatus(RepayCurrentStatusEnums.已还款.toString());
					projPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
				}else {
					projPlanList.setRepayFlag(RepayPlanPayedTypeEnum.OFFLINE_CHECK_SETTLE.getValue());
					deficitSettle = true ;
				}
				
			}
			
			
			if (deficitSettle) {
			}
    	}
    }
    
    private void updateBizPlanList(List<RepaymentBizPlanListDto>  bizPlanListDtos) {
    	for (RepaymentBizPlanListDto repaymentBizPlanListDto : bizPlanListDtos) {
			List<RepaymentBizPlanListDetail> bizPlanListDetails = repaymentBizPlanListDto.getBizPlanListDetails();
			RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListDto.getRepaymentBizPlanList() ;
			BigDecimal planAmount = repaymentBizPlanList.getTotalBorrowAmount() ;
			BigDecimal planFactAmount = BigDecimal.ZERO ;
			BigDecimal planOverAmount = repaymentBizPlanList.getOverdueAmount()!=null?repaymentBizPlanList.getOverdueAmount():BigDecimal.ZERO;
			BigDecimal planDerateAmout = repaymentBizPlanList.getDerateAmount()!=null?repaymentBizPlanList.getDerateAmount():BigDecimal.ZERO;
			
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : bizPlanListDetails) {
				planFactAmount = planFactAmount.add(repaymentBizPlanListDetail.getFactAmount()==null?BigDecimal.ZERO:repaymentBizPlanListDetail.getFactAmount());
			}
			Collections.sort(bizPlanListDetails, new Comparator<RepaymentBizPlanListDetail>() {
				@Override
				public int compare(RepaymentBizPlanListDetail arg0, RepaymentBizPlanListDetail arg1) {
					if (arg0.getFactRepayDate().before(arg1.getFactRepayDate())) {
						return -1 ;
					}
					if (arg0.getFactRepayDate().after(arg1.getFactRepayDate())) {
						return 1 ;
					}
					return 0;
				}
			});
			repaymentBizPlanList.setFactRepayDate(bizPlanListDetails.get(0).getFactRepayDate());
			if (planAmount.add(planOverAmount).compareTo(planDerateAmout.add(planFactAmount)) <= 0) {
				repaymentBizPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
				repaymentBizPlanList.setCurrentSubStatus(RepayCurrentStatusEnums.已还款.toString());
				repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
			}else {
				repaymentBizPlanList.setRepayFlag(RepayPlanPayedTypeEnum.OFFLINE_CHECK_SETTLE.getValue());
			}
		}
    }
}
