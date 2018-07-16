package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.SettleService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.finance.req.FinanceBaseDto;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
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

    @Autowired
    @Qualifier("SettleService")
    private SettleService settleService;

    @Override
    public void financeSettle(FinanceSettleReq financeSettleReq) {
        FinanceSettleBaseDto financeSettleBaseDto = new FinanceSettleBaseDto();

        //查询所有的应还
//        countRepayPlanAmount(baseDto, financeSettleReq);
        //结算流水ID
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);

        //生成流水消费记录
        makeMonePoolRepayment(financeSettleBaseDto, financeSettleReq);

        //数据填充及bak
        makeRepaymentPlan(financeSettleBaseDto, financeSettleReq);

        //开始标的结清
        shareProjSettleMoney(financeSettleBaseDto, financeSettleReq);
        //开始业务的结清
        shareBizSettleMoney(financeSettleBaseDto, financeSettleReq);



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

        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();

        List<String> mprIds = financeSettleReq.getMprIds();
        List<MoneyPoolRepayment> moneyPoolRepaymentList = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().in("id", mprIds));
        if (!CollectionUtils.isEmpty(moneyPoolRepaymentList)) {
            for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepaymentList) {
                RepaymentResource repaymentResource = new RepaymentResource();


                if (moneyPoolRepayment.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
                    throw new ServiceRuntimeException("已还款的银行流水不能再还款,请重新检查");
                }
                UUID uuid = UUID.randomUUID();
                repaymentResource.setResourceId(String.valueOf(uuid));


                repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
                repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
                repaymentResource.setOrgBusinessId(moneyPoolRepayment.getOriginalBusinessId());
                repaymentResource.setCreateDate(new Date());
                repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
                repaymentResource.setIsCancelled(0);
                repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
                repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
                repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.OFFLINE_TRANSFER.getValue().toString());
                repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());

                repaymentResource.setSettleLogId(financeSettleBaseDto.getUuid());


                repaymentResources.add(repaymentResource);
            }
        }
        financeSettleBaseDto.setRepaymentResources(repaymentResources);
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

                        //标的应还项目明细ID
                        String projPlanListId = repaymentProjPlanList.getProjPlanListId();
                        String currentStatus = repaymentProjPlanList.getCurrentStatus(); //标的还款状态

                        if (RepayCurrentStatusEnums.还款中.toString().equals(currentStatus) || RepayCurrentStatusEnums.逾期.toString().equals(currentStatus)) {
                            RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
                            BigDecimal unpaid = repaymentProjPlanListDto.getUnpaid();

                            //备份标的还款计划列表
                            RepaymentProjPlanListBak repaymentProjPlanListBak = new RepaymentProjPlanListBak();
                            BeanUtils.copyProperties(repaymentProjPlanList, repaymentProjPlanListBak);
                            repaymentProjPlanListBak.setSettleLogId(uuid);
//                        repaymentProjPlanListBakMapper.insert(repaymentProjPlanListBak);
                            repaymentProjPlanListBaks.add(repaymentProjPlanListBak);

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

                                }
                                baseDto.setRepaymentProjPlanListDetailBaks(repaymentProjPlanListDetailBaks);
                            }
                            //填充标的还款计划列表信息
                            repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
                            repaymentProjPlanListDto.setUnpaid(unpaid);
                            projPlanListDtos.add(repaymentProjPlanListDto);
                            shouldDetailAmount = shouldDetailAmount.add(unpaid);
                        }


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
     * 分配规则变更，重新写
     */
    private void shareProjSettleMoney(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();
        //整个业务还款计划DTO
        List<RepaymentBizPlanDto> planDtoList = financeSettleBaseDto.getPlanDtoList();
        String lastProjectId = null;
        if (CollectionUtils.isNotEmpty(planDtoList)) {

            // 上一次还款计划是否成功的标志位
            boolean lastPaySuc = true;
            for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
                //单个还款计划标的还款计划
                List<RepaymentProjPlanDto> projPlanDtos = repaymentBizPlanDto.getProjPlanDtos();
                if (CollectionUtils.isNotEmpty(projPlanDtos)) {

                    //扣款排序
                    settleSort(projPlanDtos);

                    // 按核销顺序还金额（先还核销顺序小于1200的费用）
                    for (int i = 0; i < projPlanDtos.size(); i++) {
                        if (lastPaySuc == false)
                            return;
                        RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
                        //填充planId
                        RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
                        financeSettleBaseDto.setPlanId(repaymentProjPlan.getPlanId());
                        financeSettleBaseDto.setBusinessId(repaymentProjPlan.getBusinessId());
                        financeSettleBaseDto.setOrgBusinessId(repaymentProjPlan.getOriginalBusinessId());


                        String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
                        lastProjectId = projectId;


                        List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
                        // 遍历标的还款计划
                        for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {

                            RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListDto.getRepaymentProjPlanList();
                            financeSettleBaseDto.setAfterId(repaymentProjPlanList.getAfterId());


                            List<RepaymentProjPlanListDetail> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto.getProjPlanListDetails();
                            //遍历这个标的每一期还款计划，费用细项
                            for (RepaymentProjPlanListDetail detail : repaymentProjPlanListDetailDtos) {
                                if (detail.getShareProfitIndex().compareTo(1200) >= 0) {
                                    continue;
                                }
                                //进行还款分配
                                boolean detailFlag = payOneFeeDetail(detail, null, financeSettleBaseDto);

                                //详情未还完 进行下一次 true 证明本次详情已还清  false流水不够了 退出循环
                                if (!detailFlag) {
                                    lastPaySuc = false;
                                    break;
                                }
                            }
                        }
                    }

                    //再还核销顺序大于等于1200的费用项
                    for (int i = 0; i < projPlanDtos.size(); i++) {
                        if (lastPaySuc)
                            return;
                        RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
                        String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
                        lastProjectId = projectId;


                        List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
                        // 遍历标的还款计划

                        for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                            List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDto.getProjPlanListDetails();
                            RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListDto.getRepaymentProjPlanList();
                            Boolean projFlag = true;
                            // //遍历这个标的每一期还款计划，费用细项
                            for (RepaymentProjPlanListDetail detail : repaymentProjPlanListDetails) {
                                if (detail.getShareProfitIndex().compareTo(1200) < 0) {
                                    continue;
                                }
                                boolean detailFlag = payOneFeeDetail(detail, null, financeSettleBaseDto);
                                //流水不够了 退出循环
                                if (!detailFlag) {
                                    lastPaySuc = false;
                                    projFlag = false; //还有未还清的
                                    break;
                                }
                            }

                            if (projFlag) {  //线下部分都还清 该期标全部还款
                                repaymentProjPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
                            }
                        }
                    }

                }

            }
            // 结余
            BigDecimal surplusFund = new BigDecimal("0");
            // 如果最后一次还款都还足了，就计算结余
            if (lastPaySuc) {
                Integer idex = financeSettleBaseDto.getResourceIndex(); //还完以后流水坐标
                for (int i = 0; i < repaymentResources.size(); i++) {
                    if (i == idex.intValue() || i > idex.intValue()) { //最后一次的资源角标 或者还存在未核销的流水  累加金额
                        surplusFund = surplusFund.add(financeSettleBaseDto.getCuralDivideAmount());
                    }
                }


            }
            //计算提前结清违约金
            String businessId = financeSettleReq.getBusinessId();
            String planId = financeSettleReq.getPlanId();
            String afterId = financeSettleReq.getAfterId();

            SettleInfoVO settleInfoVO = settleService.settleInfoVO(businessId, afterId, planId);
            if (settleInfoVO.getPenalty() != null) {
                BigDecimal penalty = settleInfoVO.getPenalty();
                //结余大于提前违约金
            }
        }
        //整个还款计划 或者业务还完

    }


    /**
     * 填充biz业务还款计划
     */
    public void shareBizSettleMoney( FinanceSettleBaseDto financeSettleBaseDto,FinanceSettleReq req) {
        List<RepaymentBizPlanDto> planDtoList = financeSettleBaseDto.getPlanDtoList();
        if (CollectionUtils.isNotEmpty(planDtoList)) {
            for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
                RepaymentBizPlan repaymentBizPlan = repaymentBizPlanDto.getRepaymentBizPlan();
                String planId = repaymentBizPlan.getPlanId();

                Map<String, List<RepaymentProjFactRepay>> stringListMap = financeSettleBaseDto.getProjFactRepays().get(planId);


                //一个业务下的planId List
                List<RepaymentBizPlanListDto> bizPlanListDtos = repaymentBizPlanDto.getBizPlanListDtos();

                for (RepaymentBizPlanListDto repaymentBizPlanListDto : bizPlanListDtos) {

                    for (RepaymentBizPlanListDetail planListDetail : repaymentBizPlanListDto.getBizPlanListDetails()) {
                        String planDetailId = planListDetail.getPlanDetailId();
                        List<RepaymentProjFactRepay> repaymentProjFactRepays = stringListMap.get(planDetailId);
                        if (CollectionUtils.isNotEmpty(repaymentProjFactRepays)) {
                            BigDecimal factAmount = planListDetail.getFactAmount() == null ? BigDecimal.ZERO : planListDetail.getFactAmount();
                            BigDecimal projFactAmount = factAmount;
                            for (RepaymentProjFactRepay repaymentProjFactRepay : repaymentProjFactRepays) {
                                projFactAmount = projFactAmount.add(repaymentProjFactRepay.getFactAmount());
                                Date factRepayDate = repaymentProjFactRepay.getFactRepayDate();
                                Integer repaySource = repaymentProjFactRepay.getRepaySource();
                                planListDetail.setFactRepayDate(factRepayDate);
                                planListDetail.setRepaySource(repaySource);

                            }
                            planListDetail.setFactAmount(projFactAmount);
                            planListDetail.setUpdateDate(new Date());
                            planListDetail.setUpdateUser(financeSettleBaseDto.getUserId());
                            planListDetail.updateById();
                        }


                    }
                }
            }

        }
    }


    /**
     * 还一个费用详情的费用
     *
     * @param detail               本次还的费用项
     * @param financeSettleBaseDto 基础dto
     * @param surplusMoney         上次调用剩余金额
     * @return boolean
     */

    private boolean payOneFeeDetail(RepaymentProjPlanListDetail detail, BigDecimal surplusMoney, FinanceSettleBaseDto financeSettleBaseDto) {

        Integer rIdex = financeSettleBaseDto.getResourceIndex();
        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();
        if (rIdex > repaymentResources.size() - 1) {
            return false;
        } else {
            RepaymentResource repaymentResource = repaymentResources.get(rIdex);
            financeSettleBaseDto.setCuralResource(repaymentResource);
            financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
        }


        // 本次调用此方法实还金额总和
        BigDecimal realPayed = BigDecimal.ZERO;


        BigDecimal unpaid = detail.getProjPlanAmount()
                .subtract(detail.getDerateAmount() == null ? new BigDecimal("0") : detail.getDerateAmount())
                .subtract(detail.getProjFactAmount());
        //上次还完剩余金额不为空 上次还款不足
        if (surplusMoney != null) {
            //为避免重复扣款  不可能存在 剩余金额大于应还金额
            unpaid = surplusMoney;
        }
        // 实还金额
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal curalDivideAmount = financeSettleBaseDto.getCuralDivideAmount(); //剩余流水

        int c = curalDivideAmount.compareTo(unpaid);
        if (c > 0) {//剩余流水大于应还
            logger.info("curalDivideAmount 大于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            financeSettleBaseDto.setCuralDivideAmount(curalDivideAmount.subtract(unpaid));
            logger.info("curalDivideAmount变为{}", curalDivideAmount);
            createProjFactRepay(money, detail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            return true;
        } else if (c == 0) { //剩余流水等于应还
            logger.info("curalDivideAmount等于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            logger.info("curalDivideAmount变为null", detail.getPlanItemName());
            // 创建实还流水
            createProjFactRepay(money, detail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            // 上一条还款来源的可用金额已用完，找下一条还款来源来用
            financeSettleBaseDto.setCuralDivideAmount(null);
            financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex() + 1);
            return true;
        } else { //剩余流水小于应还
            logger.info("curalDivideAmount少于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, curalDivideAmount,
                    detail.getPlanItemName());
            money = financeSettleBaseDto.getCuralDivideAmount();

            createProjFactRepay(money, detail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed); //本次已还金额

            //还完还欠多少
            unpaid = unpaid.subtract(money);
            financeSettleBaseDto.setCuralDivideAmount(null);
            financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex() + 1);
            // 如果成功取到下一条还款流水 剩余未还完的继续还
            boolean pRet = payOneFeeDetail(detail, unpaid, financeSettleBaseDto);
            if (pRet && financeSettleBaseDto.getRealPayedAmount() != null) {
                realPayed = realPayed.add(financeSettleBaseDto.getRealPayedAmount()); //递归累加实还金额
            }
            financeSettleBaseDto.setRealPayedAmount(realPayed);
            return pRet;
        }

    }


    /**
     * 根据RepaymentProjPlanListDetail和实还金额创建RepaymentProjFactRepay
     *
     * @param divideAmount
     * @param detail
     * @return
     * @author 王继光 2018年5月24日 下午11:45:26
     */
    private RepaymentProjFactRepay createProjFactRepay(BigDecimal divideAmount, RepaymentProjPlanListDetail detail, RepaymentResource resource, FinanceSettleBaseDto financeSettleBaseDto) {
        //填充实还记录
        RepaymentProjFactRepay fact = new RepaymentProjFactRepay();
        fact.setAfterId(financeSettleBaseDto.getAfterId());
        fact.setBusinessId(financeSettleBaseDto.getBusinessId());
        fact.setCreateDate(new Date());
        fact.setCreateUser(financeSettleBaseDto.getUserId());
        fact.setOrigBusinessId(detail.getOrigBusinessId());
        fact.setProjectId(financeSettleBaseDto.getProjectId());
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
        fact.setRepaySource(Integer.valueOf(resource.getRepaySource()));// 结清还款来源类别

        fact.setSettleLogId(financeSettleBaseDto.getUuid());
        fact.setFactAmount(divideAmount); //实还


        fact.setProjPlanDetailRepayId(UUID.randomUUID().toString()); //实还主键ID

        addProjFactRepays(financeSettleBaseDto, detail.getPlanDetailId(), fact);


        //回填detail
        detail.setProjFactAmount(detail.getProjFactAmount().add(divideAmount));
        detail.setRepaySource(Integer.valueOf(resource.getRepaySource()));
        detail.setFactRepayDate(resource.getRepayDate());
        detail.setUpdateDate(new Date());
        detail.setUpdateUser(loginUserInfoHelper.getUserId());
        detail.updateById();


        return fact;
    }


    /**
     * 将RepaymentProjFactRepay保存在内存中
     *
     * @param financeSettleBaseDto
     * @param bizPlanListDetailId
     * @param fact
     */
    private void addProjFactRepays(FinanceSettleBaseDto financeSettleBaseDto, String bizPlanListDetailId, RepaymentProjFactRepay fact) {
        String planId = financeSettleBaseDto.getPlanId(); //planId
        Map<String, List<RepaymentProjFactRepay>> stringListMap = financeSettleBaseDto.getProjFactRepays().get(planId);
        List<RepaymentProjFactRepay> list = stringListMap.get(bizPlanListDetailId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(fact);
        stringListMap.put(bizPlanListDetailId, list);

    }


}
