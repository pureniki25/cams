package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
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
    RepaymentSettleLogDetailMapper repaymentSettleLogDetailMapper;

    @Autowired
    RepaymentProjPlanBakMapper repaymentProjPlanBakMapper;

    @Autowired
    RepaymentProjPlanListBakMapper repaymentProjPlanListBakMapper;

    @Autowired
    RepaymentProjPlanListDetailBakMapper repaymentProjPlanListDetailBakMapper;

    @Autowired
    ProjExtRateMapper projExtRateMapper;

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
    @Qualifier("RepaymentProjPlanListService")
    private RepaymentProjPlanListService repaymentProjPlanListService;

    @Autowired
    @Qualifier("RepaymentProjPlanService")
    private RepaymentProjPlanService repaymentProjPlanService;

    @Autowired
    private TransferOfLitigationMapper transferOfLitigationMapper;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService bizPlanListService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    private RepaymentBizPlanService bizPlanService;

    @Autowired
    @Qualifier("AccountantOverRepayLogService")
    private AccountantOverRepayLogService accountantOverRepayLogService;

    @Override
    public List<CurrPeriodProjDetailVO> financeSettle(FinanceSettleReq financeSettleReq) {
        FinanceSettleBaseDto financeSettleBaseDto = new FinanceSettleBaseDto();
        financeSettleBaseDto.setPreview(financeSettleReq.getPreview());
        //结算流水ID
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);

        financeSettleBaseDto.setBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setAfterId(financeSettleReq.getAfterId());
        financeSettleBaseDto.setOrgBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setPlanId(financeSettleReq.getPlanId());

        makeMonePoolRepayment(financeSettleBaseDto, financeSettleReq);
        /*创建结清记录*/
        createSettleLog(financeSettleBaseDto, financeSettleReq);
        /*计算结余金额*/
        calcSurplus(financeSettleBaseDto, financeSettleReq);
        //生成流水消费记录

        //数据填充及bak
        makeRepaymentPlanAllPlan(financeSettleBaseDto, financeSettleReq);

        //开始标的结清
        shareProjSettleMoney(financeSettleBaseDto, financeSettleReq);

        //开始业务的结清
//        shareBizSettleMoney(financeSettleBaseDto, financeSettleReq);

        /*更新状态*/
//        updateStatus(financeSettleBaseDto, financeSettleReq);
        return financeSettleBaseDto.getCurrPeriodProjDetailVOList();


    }


    /**
     * 计算结余金额
     *
     * @param dto
     * @author 王继光
     * 2018年7月17日 下午3:10:03
     */
    private void calcSurplus(FinanceSettleBaseDto dto, FinanceSettleReq req) {
        SettleInfoVO settleInfoVO = settleInfoVO(req);
        if (dto.getRepayFactAmount().compareTo(settleInfoVO.getTotal()) > 0) {
            BigDecimal surplus = dto.getRepayFactAmount().subtract(settleInfoVO.getTotal());
            AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
            accountantOverRepayLog.setBusinessAfterId(dto.getAfterId());
            accountantOverRepayLog.setBusinessId(dto.getBusinessId());
            accountantOverRepayLog.setCreateTime(new Date());
            accountantOverRepayLog.setCreateUser(dto.getUserId());
            accountantOverRepayLog.setFreezeStatus(0);
            accountantOverRepayLog.setIsRefund(0);
            accountantOverRepayLog.setIsTemporary(0);
            accountantOverRepayLog.setMoneyType(1);
            accountantOverRepayLog.setOverRepayMoney(surplus);
            accountantOverRepayLog
                    .setRemark(String.format("收入于%s的%s期线下财务结清", dto.getBusinessId(), dto.getAfterId()));

            dto.getRepaymentSettleLog().setSurplusAmount(surplus);
            dto.getRepaymentSettleLog().setSurplusRefId(accountantOverRepayLog.getId().toString());
            if (!req.getPreview()) {
                accountantOverRepayLog.insert();
            }
        }
    }

    /**
     * 创建结清记录
     *
     * @param dto
     * @param financeSettleReq
     * @author 王继光
     * 2018年7月17日 上午11:19:50
     */
    private void createSettleLog(FinanceSettleBaseDto dto, FinanceSettleReq financeSettleReq) {
        /*查当前期*/
        RepaymentBizPlanList cur = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", financeSettleReq.getBusinessId()).eq("after_id", financeSettleReq.getAfterId()));
        /*查本次结清的所有还款计划期数*/
        EntityWrapper<RepaymentBizPlanList> eWrapper = new EntityWrapper<RepaymentBizPlanList>();
        eWrapper.eq("orig_business_id", financeSettleReq.getBusinessId()).orderBy("due_date", false);
        if (!StringUtil.isEmpty(financeSettleReq.getPlanId())) {
            eWrapper.eq("plan_id", financeSettleReq.getPlanId());
        }
        List<RepaymentBizPlanList> list = bizPlanListService.selectList(eWrapper);
        dto.setCurPeriod(cur);
        Date now = new Date();
        /*比较当前期与最后一期的关系*/
        if (list.get(0).getAfterId().equals(cur.getAfterId())) {
            int diff = DateUtil.getDiffDays(now, cur.getFactRepayDate());
            if (diff > 0) {
                dto.setPreSettle(true);
            } else {
                dto.setPreSettle(false);
            }
        } else {
            dto.setPreSettle(true);
        }

        RepaymentSettleLog log = new RepaymentSettleLog();
        log.setBusinessId(financeSettleReq.getBusinessId());
        log.setCreateTime(now);
        log.setCreateUser(dto.getUserId());
        log.setCreateUserName(dto.getUserName());
        log.setOrgBusinessId(dto.getOrgBusinessId());
        log.setPlanId(dto.getPlanId());
        log.setPlanListId(cur.getPlanListId());
        log.setSettleLogId(dto.getUuid());

        dto.setRepaymentSettleLog(log);
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


//    public void countRepayPlanAmount(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {
//        String businessId = financeSettleReq.getBusinessId();
//        String planId = financeSettleReq.getPlanId();
//        BigDecimal repayPlanAmount = repaymentProjPlanMapper.countRepayPlanAmount(businessId, planId);
//    }


    public void makeRepaymentPlan(FinanceSettleBaseDto financeSettleBaseDto, RepaymentBizPlanList repaymentBizPlanList, List<RepaymentSettleMoneyDto> beforSettleMoneyDto, List<RepaymentSettleMoneyDto> afterSettleMoneyDto) {
        RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto();

        String businessId = repaymentBizPlanList.getBusinessId();
        String planIdW = repaymentBizPlanList.getPlanId();
        String afterId = repaymentBizPlanList.getAfterId();
        String uuid = financeSettleBaseDto.getUuid();


//        RepaymentBizPlanList repaymentBizPlanList = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId).eq("plan_id", planIdW));
        if (repaymentBizPlanList != null) {
            String planId = repaymentBizPlanList.getPlanId();
            Integer period = repaymentBizPlanList.getPeriod();
            Date dueDate = repaymentBizPlanList.getDueDate();


            List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planId));
            if (repaymentBizPlans == null || repaymentBizPlans.size() > 1) {
                throw new ServiceRuntimeException("数据有误");
            }

            RepaymentBizPlan repaymentBizPlan = repaymentBizPlans.get(0);
            repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);

            financeSettleBaseDto.setPlanId(planId);
            financeSettleBaseDto.setBusinessId(businessId);
            financeSettleBaseDto.setAfterId(afterId);

            //==============================标的还款信息填充开始
            makeRepaymentProjPlan(repaymentBizPlanDto, financeSettleBaseDto, beforSettleMoneyDto, afterSettleMoneyDto);
            //==============================标的还款信息填充结束

            List<RepaymentBizPlanBak> repaymentBizPlanBaks = financeSettleBaseDto.getRepaymentBizPlanBaks();
            RepaymentBizPlanBak repaymentBizPlanBak = new RepaymentBizPlanBak();
            BeanUtils.copyProperties(repaymentBizPlan, repaymentBizPlanBak);
            repaymentBizPlanBak.setSettleLogId(uuid);
            repaymentBizPlanBaks.add(repaymentBizPlanBak);


            //业务还款计划DTO 只保存当期
            List<RepaymentBizPlanListDto> bizPlanListDtos = new ArrayList<>();
            RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
            repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);


            //备份业务还款计划DTO 只保存当期
            List<RepaymentBizPlanListBak> repaymentBizPlanListBaks = financeSettleBaseDto.getRepaymentBizPlanListBaks();
            RepaymentBizPlanListBak repaymentBizPlanListBak = new RepaymentBizPlanListBak();
            BeanUtils.copyProperties(repaymentBizPlanList, repaymentBizPlanListBak);
            repaymentBizPlanListBaks.add(repaymentBizPlanListBak);

            String planListId = repaymentBizPlanList.getPlanListId();
            List<RepaymentBizPlanListDetail> repaymentBizPlanListDetailList = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planListId));

            repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetailList);
            if (CollectionUtils.isNotEmpty(repaymentBizPlanListDetailList)) {
                List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks = financeSettleBaseDto.getRepaymentBizPlanListDetailBaks();

                for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetailList) {
                    RepaymentBizPlanListDetailBak repaymentBizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
                    BeanUtils.copyProperties(repaymentBizPlanListDetail, repaymentBizPlanListDetailBak);
                    repaymentBizPlanListDetailBaks.add(repaymentBizPlanListDetailBak);
                }
            }
            bizPlanListDtos.add(repaymentBizPlanListDto);
            repaymentBizPlanDto.setBizPlanListDtos(bizPlanListDtos);
        }

    }


    @Override
    public void makeRepaymentPlanAllPlan(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

        String planId = financeSettleReq.getPlanId();
        String businessId = financeSettleReq.getBusinessId();
        String afterId = financeSettleReq.getAfterId();


        //查出当前期数的应还日期
        RepaymentBizPlanList repaymentBizPlanList = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));

        if (repaymentBizPlanList != null) {
            Date dueDate = repaymentBizPlanList.getDueDate();

            if (StringUtil.isEmpty(planId)) {// 全部业务结清
                List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));

                if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {

                    for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
                        String planIdInner = repaymentBizPlan.getPlanId();

                        //查出所有标的业务还款计划 按应还日期进行排序
                        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planIdInner).orderBy("due_date", true));
                        if (CollectionUtils.isNotEmpty(repaymentBizPlanLists)) {
                            Boolean flag = false;
                            Set<Integer> repayStatusSet = new HashSet<>();
                            List<RepaymentBizPlanList> list = new ArrayList<>();//保存本次还款计划的当前期
                            for (RepaymentBizPlanList repaymentBizPlanListInner : repaymentBizPlanLists) {
                                Date dueDateInner = repaymentBizPlanListInner.getDueDate();
                                if (!flag && !afterId.equals(repaymentBizPlanListInner.getAfterId())) { //当前期之前 并且不为当前期
                                    repayStatusSet.add(repaymentBizPlanListInner.getRepayStatus() == null ? 0 : repaymentBizPlanListInner.getRepayStatus());
                                }

                                if (dueDateInner.getTime() - dueDate.getTime() >= 0) { //业务期数 应还日期大于当期日期
                                    if (CollectionUtils.isEmpty(list)) {
                                        list.add(repaymentBizPlanListInner);
                                    }
                                    flag = true;
                                }
                                if (flag && (repayStatusSet.contains(0) || repayStatusSet.contains(1))) { // 找到当前期以后开始检查 0代表未还款 1代表部分还款
                                    throw new ServiceRuntimeException("500", "当前期数不能进行结清");
                                }
                            }

                            //通过当期期数查出当前期前面 及后面部分
                            if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
                                RepaymentBizPlanList repaymentBizPlanListNow = list.get(0);

                                makeRepaymentPlanOnePlan(financeSettleBaseDto, repaymentBizPlanListNow, financeSettleReq);
                            }
                        }


                    }


                }

            } else {
                RepaymentBizPlan repaymentBizPlan = bizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planId));

                makeRepaymentPlanOnePlan(financeSettleBaseDto, repaymentBizPlanList, financeSettleReq);
            }

        }

    }


    public void makeRepaymentPlanOnePlan(FinanceSettleBaseDto financeSettleBaseDto, RepaymentBizPlanList repaymentBizPlanListNow, FinanceSettleReq financeSettleReq) {

        String businessId = financeSettleReq.getBusinessId();
        String planIdNow = repaymentBizPlanListNow.getPlanId();
        String afterIdNow = repaymentBizPlanListNow.getAfterId();


        //通过当期的业务list 调用滞纳金生成接口 单独开启事务  数据库字段滞纳金已更新
        RepaymentBizPlanList repaymentBizPlanList = repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanListNow,1);

        //先通过应还时间排序  先还后还的分好类
        //再通过标的还款顺序进行分类

        //查出所有的标的明细项 并按还款时间进行排序 由低到高
        List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtos = repaymentSettleLogMapper.selectProjPlanMoney(businessId, planIdNow);

        List<RepaymentSettleMoneyDto> beforSettleMoneyDto = new ArrayList<>(); //先结算
        List<RepaymentSettleMoneyDto> afterSettleMoneyDto = new ArrayList<>(); //后结算
//        List<RepaymentSettleMoneyDto> nowSettleMoneyDto = new ArrayList<>(); //当前期


        Boolean flagNow = false;
        if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtos)) {
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtos) {
                Integer repayStatus = repaymentSettleMoneyDto.getRepayStatus(); //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
                BigDecimal money = repaymentSettleMoneyDto.getMoney();
                if (!flagNow && !afterIdNow.equals(repaymentSettleMoneyDto.getAfterId()) && !repayStatus.equals(SectionRepayStatusEnum.ALL_REPAID.getKey())) { //添加当前期之前的 不为已还款
                    if (repaymentSettleMoneyDto.getShareProfitIndex().intValue() >= 1200 && money.compareTo(BigDecimal.ZERO) > 0) {
                        afterSettleMoneyDto.add(repaymentSettleMoneyDto);
                    }
                } else if (afterIdNow.equals(repaymentSettleMoneyDto.getAfterId())) { //如果期数 等于当前期数
//                    nowSettleMoneyDto.add(repaymentSettleMoneyDto);
                    //线下部分 添加到后结算 即为差额
                    if (repaymentSettleMoneyDto.getShareProfitIndex().intValue() >= 1200 && money.compareTo(BigDecimal.ZERO) > 0) {
                        afterSettleMoneyDto.add(repaymentSettleMoneyDto);  //线下部分 添加到后结算
                    } else { //小于1200的添加到 前结算
                        beforSettleMoneyDto.add(repaymentSettleMoneyDto); //本期  本金添加到前结算
                    }
                    flagNow = true; //找到当前期
                } else if (flagNow && !afterIdNow.equals(repaymentSettleMoneyDto.getAfterId()) && !repayStatus.equals(SectionRepayStatusEnum.ALL_REPAID.getKey())) { //添加当前期之后的
                    if (repaymentSettleMoneyDto.getPlanItemType().equals(RepayPlanFeeTypeEnum.PRINCIPAL.getValue()) && money.compareTo(BigDecimal.ZERO) > 0) {
                        beforSettleMoneyDto.add(repaymentSettleMoneyDto);
                    }
                }

            }
        }


        //标的还款顺序分类
        List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtoOder = repaymentSettleLogMapper.orderSettleProj(planIdNow);

        //拆分还款的规则应优先还共借标的，在还主借标的，若有多个共借标，则有优先还上标金额较小的标的，若共借标中的金额先等，则优先还满标时间较早的标的
        List<RepaymentSettleMoneyDto> sortBeforMoneyDto = sortProjIdMoneyDto(repaymentSettleMoneyDtoOder, beforSettleMoneyDto);
        List<RepaymentSettleMoneyDto> sortAfterMoneyDto = sortProjIdMoneyDto(repaymentSettleMoneyDtoOder, afterSettleMoneyDto);

        //开始进行结算
        Boolean flag = true;
        if (CollectionUtils.isNotEmpty(sortBeforMoneyDto)) {
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : sortBeforMoneyDto) {
                flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                if (!flag) {
                    break;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(sortAfterMoneyDto) && flag) {

            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : sortAfterMoneyDto) {
                flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                if (!flag) {
                    break;
                }
            }
        }


        //扣减提前违约金额

        //标的实还
        List<CurrPeriodProjDetailVO> currPeriodProjDetailVOList = financeSettleBaseDto.getCurrPeriodProjDetailVOList();
        for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtoOder) {
            String projPlanId = repaymentSettleMoneyDto.getProjPlanId();
            int isMast = repaymentSettleMoneyDto.getIsMast();
            Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
            if (MapUtils.isNotEmpty(webFactRepays)) {
                CurrPeriodProjDetailVO currPeriodProjDetailVO = webFactRepays.get(projPlanId);
                if (currPeriodProjDetailVO != null) {
                    currPeriodProjDetailVO.setMaster(isMast == 1 ? true : false);
                    currPeriodProjDetailVOList.add(currPeriodProjDetailVO);
                }

            }
        }


        //更新tb_repayment_biz_plan_list, tb_repayment_biz_plan_list_detail,tb_repayment_proj_plan_list,tb_repayment_biz_plan_list_detail三个表 及状态
        List<RepaymentSettleLogDetail> repaymentSettleLogDetailLists = financeSettleBaseDto.getRepaymentSettleLogDetailList();


//        if (CollectionUtils.isNotEmpty(nowSettleMoneyDto)) { //当前期
//            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : nowSettleMoneyDto) {
//                String sumProjPlanId = repaymentSettleMoneyDto.getProjPlanId();
//                logger.info("=========sumProjPlanId=" + sumProjPlanId);
//
//                Integer sumPlanItemType = repaymentSettleMoneyDto.getPlanItemType();
//                BigDecimal projPlanAmount = repaymentSettleMoneyDto.getProjPlanAmount(); //应还金额
//                logger.info("=========projPlanAmount=" + projPlanAmount);
//
//                Integer sumShareProfitIndex = repaymentSettleMoneyDto.getShareProfitIndex();
//                if (sumShareProfitIndex.intValue() >= 1200) {  //线下部分
//                    if (CollectionUtils.isNotEmpty(beforSettleMoneyDto)) { //如果之前期不为空 线下部分 前面已过滤
//                        BigDecimal sumXxMoney = BigDecimal.ZERO;
//                        for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoBefore : beforSettleMoneyDto) {
//                            if (sumProjPlanId.equals(repaymentSettleMoneyDtoBefore.getProjPlanId())) { //标ID相同
//                                if (sumPlanItemType.equals(repaymentSettleMoneyDtoBefore.getPlanItemType())) { //并且里面的项相同则累加
//                                    sumXxMoney = sumXxMoney.add(repaymentSettleMoneyDtoBefore.getMoney());
//                                }
//                            }
//                        }
//                        repaymentSettleMoneyDto.setProjPlanAmount(projPlanAmount.add(sumXxMoney));
//                    }
//                }
//
//                if (sumPlanItemType.equals(RepayPlanFeeTypeEnum.PRINCIPAL.getValue())) { //如果为本金明细项
//                    if (CollectionUtils.isNotEmpty(afterSettleMoneyDto)) { //如果之前期不为空 本金部分 前面已过滤
//                        BigDecimal sumBjMoney = BigDecimal.ZERO;
//                        for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoAfter : afterSettleMoneyDto) {
//                            if (sumProjPlanId.equals(repaymentSettleMoneyDtoAfter.getProjPlanId())) { //标ID相同
//                                logger.info("=========repaymentSettleMoneyDtoAfter.getProjPlanId()=" + repaymentSettleMoneyDtoAfter.getProjPlanId());
//                                if (sumPlanItemType.equals(repaymentSettleMoneyDtoAfter.getPlanItemType())) { //并且里面的项相同则累加
//                                    logger.info("=========repaymentSettleMoneyDtoAfter.getMoney()=" + repaymentSettleMoneyDtoAfter.getMoney());
//                                    sumBjMoney = sumBjMoney.add(repaymentSettleMoneyDtoAfter.getMoney());
//                                    logger.info("=========sumBjMoney=" + projPlanAmount);
//
//                                }
//                            }
//                        }
//                        repaymentSettleMoneyDto.setProjPlanAmount(projPlanAmount.add(sumBjMoney));
//                    }
//                }
//                logger.info("=========repaymentSettleMoneyDto.getProjPlanAmount=" + repaymentSettleMoneyDto.getProjPlanAmount());
//
//            }
//        }
    }


    //通过标排序 进行标的明细项排序
    public List<RepaymentSettleMoneyDto> sortProjIdMoneyDto(List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtoOder, List<RepaymentSettleMoneyDto> moneyDto) {
        List<RepaymentSettleMoneyDto> sortMoneyDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtoOder)) { //标的排序

            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtoOder) {
                String projPlanId = repaymentSettleMoneyDto.getProjPlanId();

                if (CollectionUtils.isNotEmpty(moneyDto)) {
                    for (RepaymentSettleMoneyDto beforRepaymentSettleMoneyDto : moneyDto) {
                        String projPlanIdBefor = beforRepaymentSettleMoneyDto.getProjPlanId();
                        if (projPlanIdBefor.equals(projPlanId)) { //标的id相等
                            sortMoneyDto.add(beforRepaymentSettleMoneyDto);
                        }
                    }

                }
            }
        }
        return sortMoneyDto;
    }


    public Boolean paySettleMoney(FinanceSettleBaseDto financeSettleBaseDto, RepaymentSettleMoneyDto moneyDto, BigDecimal surplusMoney) {
        Integer rIdex = financeSettleBaseDto.getResourceIndex();
        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();

        if (rIdex.intValue() == 0 && financeSettleBaseDto.getCuralDivideAmount().compareTo(BigDecimal.ZERO) == 0) {
            RepaymentResource repaymentResource = repaymentResources.get(rIdex);
            financeSettleBaseDto.setCuralResource(repaymentResource);
            financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
            logger.info("@@第" + rIdex.intValue() + 1 + "笔流水,总额=" + repaymentResource.getRepayAmount());
        } else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(BigDecimal.ZERO) == 0) {
            if (rIdex > repaymentResources.size() - 1) {
                return false;
            } else {
                RepaymentResource repaymentResource = repaymentResources.get(rIdex);
                financeSettleBaseDto.setCuralResource(repaymentResource);
                financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
                logger.info("@@第" + rIdex.intValue() + 1 + "笔流水,总额=" + repaymentResource.getRepayAmount());
            }
        }


        // 本次调用此方法实还金额总和
        BigDecimal realPayed = BigDecimal.ZERO;


        BigDecimal unpaid = moneyDto.getMoney();
        //上次还完剩余金额不为空 上次还款不足
        if (surplusMoney != null) {
            //为避免重复扣款  不可能存在 剩余金额大于应还金额
            unpaid = surplusMoney;
        }
        // 实还金额
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal curalDivideAmount = financeSettleBaseDto.getCuralDivideAmount(); //剩余流水

        RepaymentResource repaymentResource = financeSettleBaseDto.getCuralResource(); //当前的流水对象

        int c = curalDivideAmount.compareTo(unpaid);
        if (c > 0) {//剩余流水大于应还
            logger.info("curalDivideAmount 大于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, moneyDto.getPlanItemName());
            money = unpaid;
            financeSettleBaseDto.setCuralDivideAmount(curalDivideAmount.subtract(unpaid));
            logger.info("curalDivideAmount变为{}", financeSettleBaseDto.getCuralDivideAmount());
            createSettleLogDetail(money, moneyDto, repaymentResource, financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            return true;
        } else if (c == 0) { //剩余流水等于应还
            logger.info("curalDivideAmount等于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, moneyDto.getPlanItemName());
            money = unpaid;
            logger.info("curalDivideAmount变为0", moneyDto.getPlanItemName());
            // 创建实还流水
            createSettleLogDetail(money, moneyDto, repaymentResource, financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            // 上一条还款来源的可用金额已用完，找下一条还款来源来用
            financeSettleBaseDto.setCuralDivideAmount(BigDecimal.ZERO);
            financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex() + 1);
            return true;
        } else { //剩余流水小于应还
            logger.info("curalDivideAmount少于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{},还需还款{}", curalDivideAmount, curalDivideAmount,
                    moneyDto.getPlanItemName(), unpaid.subtract(money));
            money = financeSettleBaseDto.getCuralDivideAmount();

            createSettleLogDetail(money, moneyDto, repaymentResource, financeSettleBaseDto);
            logger.info("@@第" + rIdex + "笔流水已分完,流水ID=" + repaymentResource.getRepaySourceRefId());
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed); //本次已还金额

            //还完还欠多少
            unpaid = unpaid.subtract(money);
            financeSettleBaseDto.setCuralDivideAmount(BigDecimal.ZERO);
            financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex() + 1);
            // 如果成功取到下一条还款流水 剩余未还完的继续还
            boolean pRet = paySettleMoney(financeSettleBaseDto, moneyDto, unpaid);
            if (pRet && financeSettleBaseDto.getRealPayedAmount() != null) {
                realPayed = realPayed.add(financeSettleBaseDto.getRealPayedAmount()); //递归累加实还金额
            }
            financeSettleBaseDto.setRealPayedAmount(realPayed);
            return pRet;
        }
    }

    public void createSettleLogDetail(BigDecimal amount, RepaymentSettleMoneyDto moneyDto, RepaymentResource repaymentResource, FinanceSettleBaseDto financeSettleBaseDto) {

        String projPlanDetailId = moneyDto.getProjPlanDetailId();
        BigDecimal projFactAmount = moneyDto.getProjFactAmount() == null ? BigDecimal.ZERO : moneyDto.getProjFactAmount();


        RepaymentSettleLogDetail repaymentSettleLogDetail = new RepaymentSettleLogDetail();
        repaymentSettleLogDetail.setFactAmount(amount);
        repaymentSettleLogDetail.setPlanItemType(moneyDto.getPlanItemType());
        repaymentSettleLogDetail.setSettleLogId(financeSettleBaseDto.getUuid());
        repaymentSettleLogDetail.setProjPlanDetailId(projPlanDetailId);
        repaymentSettleLogDetail.setSettleDetailLogId(UUID.randomUUID().toString());


        //累加标的实还
        String projPlanId = moneyDto.getProjPlanId();
        Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
        CurrPeriodProjDetailVO vo = webFactRepays.get(projPlanId);

        if (vo == null) {
            vo = new CurrPeriodProjDetailVO();
        }
        switch (moneyDto.getPlanItemType()) {
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
                if (moneyDto.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                    vo.setOnlineOverDue(vo.getOnlineOverDue().add(amount));
                }
                if (moneyDto.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                    vo.setOfflineOverDue(vo.getOfflineOverDue().add(amount));
                }
                break;
            default:
                logger.info("未定义的类型!!!{}||{}||{}", moneyDto.getPlanItemName(), moneyDto.getPlanItemType(), amount);
                break;
        }
        Boolean preview = financeSettleBaseDto.getPreview();

        if (preview) { //保存操作
            repaymentSettleLogDetailMapper.insert(repaymentSettleLogDetail);

            RepaymentProjPlanListDetail repaymentProjPlanListDetail = new RepaymentProjPlanListDetail();
            repaymentProjPlanListDetail.setProjPlanDetailId(projPlanDetailId);
            repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(amount));
            repaymentProjPlanListDetail.setFactRepayDate(new Date());
            repaymentProjPlanListDetailMapper.updateById(repaymentProjPlanListDetail);

        }

        webFactRepays.put(projPlanId, vo);
    }

    public void makeMonePoolRepayment(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();
        BigDecimal surplus = financeSettleReq.getSurplusFund();
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

                /*log.setFactAmount(log.getFactAmount().add(resource.getRepayAmount()));
                log.setRepayDate(dto.getRepaymentResources().get(dto.getRepaymentResources().size()-1).getRepayDate());
    			log.setRepaySource(Integer.valueOf(resource.getRepaySource()));
    			*/
                repaymentResources.add(repaymentResource);
//                financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
//                financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
//                financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//                financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
//                if (!financeSettleReq.getPreview()) {
//                    repaymentResource.insert();
//                }
            }
        }
//        if (surplus != null && surplus.compareTo(new BigDecimal("0")) > 0) {
//            BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(financeSettleReq.getBusinessId(), null);
//            if (surplus.compareTo(canUseSurplus) > 0) {
//                throw new ServiceRuntimeException("往期结余金额不足");
//            }
//
//            AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
//            accountantOverRepayLog.setBusinessAfterId(financeSettleReq.getAfterId());
//            accountantOverRepayLog.setBusinessId(financeSettleReq.getBusinessId());
//            accountantOverRepayLog.setCreateTime(new Date());
//            accountantOverRepayLog.setCreateUser(financeSettleBaseDto.getUserId());
//            accountantOverRepayLog.setFreezeStatus(0);
//            accountantOverRepayLog.setIsRefund(0);
//            accountantOverRepayLog.setIsTemporary(0);
//            accountantOverRepayLog.setMoneyType(0);
//            accountantOverRepayLog.setOverRepayMoney(financeSettleReq.getSurplusFund());
//            accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务結清", financeSettleReq.getBusinessId(), financeSettleReq.getAfterId()));
//
//
//            RepaymentResource repaymentResource = new RepaymentResource();
//            repaymentResource.setAfterId(financeSettleReq.getAfterId());
//            repaymentResource.setBusinessId(financeSettleReq.getBusinessId());
//            repaymentResource.setOrgBusinessId(financeSettleReq.getBusinessId());
//            repaymentResource.setCreateDate(new Date());
//            repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
//            repaymentResource.setIsCancelled(0);
//            repaymentResource.setRepayAmount(financeSettleReq.getSurplusFund());
//            repaymentResource.setRepayDate(new Date());
//            repaymentResource.setSettleLogId(financeSettleBaseDto.getRepaymentSettleLog().getSettleLogId());
//            //11:用往期结余还款',
//            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.SURPLUS_REPAY.getValue().toString());
//
//            if (!financeSettleReq.getPreview()) {
//                accountantOverRepayLog.insert();
//                financeSettleBaseDto.getRepaymentSettleLog().setSurplusUseRefId(accountantOverRepayLog.getId().toString());
//
//                repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
//                repaymentResource.insert();
//                if (mprIds.size() == 0) {
//                    financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
//                }
//
//            }
//
//            repaymentResources.add(repaymentResource);
//            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//            financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
//            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//            financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
//        }
//        if (surplus != null && surplus.compareTo(new BigDecimal("0")) > 0) {
//            BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(financeSettleReq.getBusinessId(), null);
//            if (surplus.compareTo(canUseSurplus) > 0) {
//                throw new ServiceRuntimeException("往期结余金额不足");
//            }
//
//            AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
//            accountantOverRepayLog.setBusinessAfterId(financeSettleReq.getAfterId());
//            accountantOverRepayLog.setBusinessId(financeSettleReq.getBusinessId());
//            accountantOverRepayLog.setCreateTime(new Date());
//            accountantOverRepayLog.setCreateUser(financeSettleBaseDto.getUserId());
//            accountantOverRepayLog.setFreezeStatus(0);
//            accountantOverRepayLog.setIsRefund(0);
//            accountantOverRepayLog.setIsTemporary(0);
//            accountantOverRepayLog.setMoneyType(0);
//            accountantOverRepayLog.setOverRepayMoney(financeSettleReq.getSurplusFund());
//            accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务結清", financeSettleReq.getBusinessId(), financeSettleReq.getAfterId()));
//
//
//            RepaymentResource repaymentResource = new RepaymentResource();
//            repaymentResource.setAfterId(financeSettleReq.getAfterId());
//            repaymentResource.setBusinessId(financeSettleReq.getBusinessId());
//            repaymentResource.setOrgBusinessId(financeSettleReq.getBusinessId());
//            repaymentResource.setCreateDate(new Date());
//            repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
//            repaymentResource.setIsCancelled(0);
//            repaymentResource.setRepayAmount(financeSettleReq.getSurplusFund());
//            repaymentResource.setRepayDate(new Date());
//            repaymentResource.setSettleLogId(financeSettleBaseDto.getRepaymentSettleLog().getSettleLogId());
//            //11:用往期结余还款',
//            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.SURPLUS_REPAY.getValue().toString());
//
//            if (!financeSettleReq.getPreview()) {
//                accountantOverRepayLog.insert();
//                financeSettleBaseDto.getRepaymentSettleLog().setSurplusUseRefId(accountantOverRepayLog.getId().toString());
//
//                repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
//                repaymentResource.insert();
//                if (mprIds.size() == 0) {
//                    financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
//                }
//
//            }
//
//            repaymentResources.add(repaymentResource);
//            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//            financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
//            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//            financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
//        }
//        if (!CollectionUtils.isEmpty(mprIds)) {
//        	List<MoneyPoolRepayment> moneyPoolRepaymentList = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().in("id", mprIds));
//            if (!CollectionUtils.isEmpty(moneyPoolRepaymentList)) {
//                for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepaymentList) {
//                    RepaymentResource repaymentResource = new RepaymentResource();
//
//
//                    if (moneyPoolRepayment.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
//                        throw new ServiceRuntimeException("已还款的银行流水不能再还款,请重新检查");
//                    }
//                    UUID uuid = UUID.randomUUID();
//                    repaymentResource.setResourceId(String.valueOf(uuid));
//
//
//                    repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
//                    repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
//                    repaymentResource.setOrgBusinessId(moneyPoolRepayment.getOriginalBusinessId());
//                    repaymentResource.setCreateDate(new Date());
//                    repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
//                    repaymentResource.setIsCancelled(0);
//                    repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
//                    repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
//                    repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.OFFLINE_TRANSFER.getValue().toString());
//                    repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());
//
//                    repaymentResource.setSettleLogId(financeSettleBaseDto.getUuid());
//
//                    /*log.setFactAmount(log.getFactAmount().add(resource.getRepayAmount()));
//        			log.setRepayDate(dto.getRepaymentResources().get(dto.getRepaymentResources().size()-1).getRepayDate());
//        			log.setRepaySource(Integer.valueOf(resource.getRepaySource()));
//        			*/
//                    repaymentResources.add(repaymentResource);
//                    financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
//                    financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
//                    financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//                    financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
//                    if (!financeSettleReq.getPreview()) {
//                    	repaymentResource.insert();
//                    }
//                }
//            }
//		}


        financeSettleBaseDto.setRepaymentResources(repaymentResources);
    }


    //组装标的还款计划信息
    public void makeRepaymentProjPlan(RepaymentBizPlanDto repaymentBizPlanDto, FinanceSettleBaseDto baseDto, List<RepaymentSettleMoneyDto> beforeSettleMoney, List<RepaymentSettleMoneyDto> afterSettleMoney) {
        String businessId = baseDto.getBusinessId();
        String planId = baseDto.getPlanId();
        String afterId = baseDto.getAfterId();

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


                //标的当前期
                List<RepaymentProjPlanList> repaymentProjPlanListList = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlanId).eq("after_id", afterId));
                if (CollectionUtils.isNotEmpty(repaymentProjPlanListList)) {
                    List<RepaymentProjPlanListBak> repaymentProjPlanListBaks = baseDto.getRepaymentProjPlanListBaks();
                    List<RepaymentProjPlanListDto> projPlanListDtos = new ArrayList<>();
                    for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanListList) {


                        //标Id
//                        String projPlanId = repaymentProjPlanList.getProjPlanId();
                        BigDecimal money = BigDecimal.ZERO; //本次标的以后期数本金金额
                        if (CollectionUtils.isNotEmpty(afterSettleMoney)) {
                            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : afterSettleMoney) {
                                //同个标的本金累加  同个标下一个期次下面 只会有一个本金
                                if (repaymentSettleMoneyDto.getProjPlanId().equals(projPlanId)) {
                                    money = repaymentSettleMoneyDto.getMoney();
                                }

                            }
                        }


                        //标的应还项目明细ID
                        String projPlanListId = repaymentProjPlanList.getProjPlanListId();

                        RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
                        BigDecimal unpaid = repaymentProjPlanListDto.getUnpaid();

                        //备份标的还款计划列表
                        RepaymentProjPlanListBak repaymentProjPlanListBak = new RepaymentProjPlanListBak();
                        BeanUtils.copyProperties(repaymentProjPlanList, repaymentProjPlanListBak);
                        repaymentProjPlanListBak.setSettleLogId(uuid);
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


                                //本金
                                if (repaymentProjPlanListDetail.getPlanItemType().intValue() == RepayPlanFeeTypeEnum.PRINCIPAL.getValue().intValue() && money.compareTo(BigDecimal.ZERO) > 0) {
                                    //累加金额
                                    repaymentProjPlanListDetail.setProjPlanAmount(repaymentProjPlanListDetail.getProjPlanAmount().add(money));
                                }

                                BigDecimal projPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
                                //应还
                                //减免
                                BigDecimal derateAmount = repaymentProjPlanListDetail.getDerateAmount();
                                //应实还
                                BigDecimal mayAmount = projPlanAmount.subtract(derateAmount);
                                //实还
                                BigDecimal projFactAmount = repaymentProjPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : repaymentProjPlanListDetail.getProjFactAmount();

                                //标的应还
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
        RepaymentBizPlanDto planDto = financeSettleBaseDto.getPlanDto();
        //整个业务还款计划DTO
//        List<RepaymentBizPlanDto> planDtoList = financeSettleBaseDto.getPlanDtoList();
        String lastProjectId = null;
//        if (CollectionUtils.isNotEmpty(planDtoList)) {

        // 上一次还款计划是否成功的标志位
        boolean lastPaySuc = true;
        List<CurrPeriodProjDetailVO> currPeriodProjDetailVOList = financeSettleBaseDto.getCurrPeriodProjDetailVOList();
//            for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
        //单个还款计划标的还款计划
        List<RepaymentProjPlanDto> projPlanDtos = planDto.getProjPlanDtos();
        if (CollectionUtils.isNotEmpty(projPlanDtos)) {

            //扣款排序
            settleSort(projPlanDtos);

            // 按核销顺序还金额（先还核销顺序小于1200的费用）
            for (int i = 0; i < projPlanDtos.size(); i++) {
                if (!lastPaySuc)
                    break;
                RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
                //填充planId
                RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
                financeSettleBaseDto.setPlanId(repaymentProjPlan.getPlanId());
                financeSettleBaseDto.setBusinessId(repaymentProjPlan.getBusinessId());
                financeSettleBaseDto.setOrgBusinessId(repaymentProjPlan.getOriginalBusinessId());
                financeSettleBaseDto.setProjPlanId(repaymentProjPlan.getProjPlanId());


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
                if (!lastPaySuc)
                    break;
                RepaymentProjPlanDto repaymentProjPlanDto = projPlanDtos.get(i);
                RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
                String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
                financeSettleBaseDto.setPlanId(repaymentProjPlan.getPlanId());
                financeSettleBaseDto.setBusinessId(repaymentProjPlan.getBusinessId());
                financeSettleBaseDto.setOrgBusinessId(repaymentProjPlan.getOriginalBusinessId());
                financeSettleBaseDto.setProjPlanId(repaymentProjPlan.getProjPlanId());
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

            for (RepaymentProjPlanDto repaymentProjPlanDto : projPlanDtos) {
                RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
                String projPlanId = repaymentProjPlan.getProjPlanId();
                TuandaiProjectInfo tuandaiProjectInfo = repaymentProjPlanDto.getTuandaiProjectInfo();
                String realName = tuandaiProjectInfo.getRealName();

                Boolean flag = false;
                String masterIssueId = tuandaiProjectInfo.getMasterIssueId();
                String projectId = tuandaiProjectInfo.getProjectId();
                if (masterIssueId.equals(projectId)) {
                    flag = true;
                }
                Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
                if (MapUtils.isNotEmpty(webFactRepays)) {
                    CurrPeriodProjDetailVO currPeriodProjDetailVO = webFactRepays.get(projPlanId);
                    if (currPeriodProjDetailVO != null) {
                        BigDecimal item10 = currPeriodProjDetailVO.getItem10();
                        BigDecimal item20 = currPeriodProjDetailVO.getItem20();
                        BigDecimal item30 = currPeriodProjDetailVO.getItem30();
                        BigDecimal item50 = currPeriodProjDetailVO.getItem50();
                        BigDecimal online = currPeriodProjDetailVO.getOnlineOverDue();
                        BigDecimal offline = currPeriodProjDetailVO.getOfflineOverDue();
                        currPeriodProjDetailVO.setTotal(item10.add(item20).add(item30).add(item50).add(online).add(offline));
                        currPeriodProjDetailVO.setUserName(realName);
                        currPeriodProjDetailVO.setMaster(flag);
                        currPeriodProjDetailVOList.add(currPeriodProjDetailVO);
                    }
                }
            }


            financeSettleBaseDto.setCurrPeriodProjDetailVOList(currPeriodProjDetailVOList);
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

//            SettleInfoVO settleInfoVO = settleService.settleInfoVO(businessId, afterId, planId);
//            if (settleInfoVO.getPenalty() != null) {
//                BigDecimal penalty = settleInfoVO.getPenalty();
//                //结余大于提前违约金
//            }
        }
        //整个还款计划 或者业务还完

    }


    /**
     * 填充biz业务还款计划
     */
 /*   public void shareBizSettleMoney(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq req) {
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
//                            planListDetail.updateById();
                        }


                    }
                }
            }

        }
    }
    */


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

        if (rIdex.intValue() == 0 && financeSettleBaseDto.getCuralDivideAmount().compareTo(BigDecimal.ZERO) == 0) {
            RepaymentResource repaymentResource = repaymentResources.get(rIdex);
            financeSettleBaseDto.setCuralResource(repaymentResource);
            financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
            logger.info("@@第" + rIdex.intValue() + 1 + "笔流水,总额=" + repaymentResource.getRepayAmount());
        } else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(BigDecimal.ZERO) == 0) {
            if (rIdex > repaymentResources.size() - 1) {
                return false;
            } else {
                RepaymentResource repaymentResource = repaymentResources.get(rIdex);
                financeSettleBaseDto.setCuralResource(repaymentResource);
                financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
                logger.info("@@第" + rIdex.intValue() + 1 + "笔流水,总额=" + repaymentResource.getRepayAmount());
            }
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

        RepaymentResource repaymentResource = financeSettleBaseDto.getCuralResource(); //当前的流水对象

        int c = curalDivideAmount.compareTo(unpaid);
        if (c > 0) {//剩余流水大于应还
            logger.info("curalDivideAmount 大于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            financeSettleBaseDto.setCuralDivideAmount(curalDivideAmount.subtract(unpaid));
            logger.info("curalDivideAmount变为{}", financeSettleBaseDto.getCuralDivideAmount());
            createProjFactRepay(money, detail, repaymentResource, financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            return true;
        } else if (c == 0) { //剩余流水等于应还
            logger.info("curalDivideAmount等于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{}", curalDivideAmount, unpaid, detail.getPlanItemName());
            money = unpaid;
            logger.info("curalDivideAmount变为0", detail.getPlanItemName());
            // 创建实还流水
            createProjFactRepay(money, detail, repaymentResource, financeSettleBaseDto);
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed);

            // 上一条还款来源的可用金额已用完，找下一条还款来源来用
            financeSettleBaseDto.setCuralDivideAmount(BigDecimal.ZERO);
            financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex() + 1);
            return true;
        } else { //剩余流水小于应还
            logger.info("curalDivideAmount少于unpaid");
            logger.info("@@从curalDivideAmount={}分unpaid={}到{},还需还款{}", curalDivideAmount, curalDivideAmount,
                    detail.getPlanItemName(), unpaid.subtract(money));
            money = financeSettleBaseDto.getCuralDivideAmount();

            createProjFactRepay(money, detail, repaymentResource, financeSettleBaseDto);
            logger.info("@@第" + rIdex + "笔流水已分完,流水ID=" + repaymentResource.getRepaySourceRefId());
            realPayed = money;
            financeSettleBaseDto.setRealPayedAmount(realPayed); //本次已还金额

            //还完还欠多少
            unpaid = unpaid.subtract(money);
            financeSettleBaseDto.setCuralDivideAmount(BigDecimal.ZERO);
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
//        detail.updateById();
        //累加标的实还信息
        rendCurrPeriodProjDetailVO(divideAmount, detail, financeSettleBaseDto);
        return fact;
    }

    /**
     * 将填充到实还的资金拷贝一份填充到CurrPeriodProjDetailVO
     *
     * @param amount
     * @param detail
     * @param financeSettleBaseDto
     * @author 王继光 2018年5月24日 下午11:44:50
     */
    private void rendCurrPeriodProjDetailVO(BigDecimal amount, RepaymentProjPlanListDetail detail, FinanceSettleBaseDto financeSettleBaseDto) {

        String projPlanId = financeSettleBaseDto.getProjPlanId();
        Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();

        CurrPeriodProjDetailVO vo = webFactRepays.get(projPlanId);
        if (vo == null) {
            vo = new CurrPeriodProjDetailVO();
        }
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

        webFactRepays.put(projPlanId, vo);
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
        if (MapUtils.isEmpty(stringListMap)) {
            stringListMap = new HashMap<>();
        }
        List<RepaymentProjFactRepay> list = stringListMap.get(bizPlanListDetailId);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(fact);
        stringListMap.put(bizPlanListDetailId, list);

        financeSettleBaseDto.getProjFactRepays().put(planId, stringListMap);


//        financeBaseDto.setResourceIndex(reIndex);
////        resourceIndex.set(reIndex);
//        RepaymentResource resource = rResources.get(reIndex);
//        financeBaseDto.setCuralResource(resource);
////        curalResource.set(resource);
//        financeBaseDto.setCuralDivideAmount(resource.getRepayAmount());
////        curalDivideAmount.set(resource.getRepayAmount());
//
//        return true;
    }

    /**
     * 更新还款计划状态并持久化
     *
     * @param dto
     * @author 王继光
     * 2018年7月16日 上午10:43:30
     */
//    private void updateStatus(FinanceSettleBaseDto dto, FinanceSettleReq req) {
//        if (req.getPreview()) {
//            return;
//        }
//        bak(dto);
//        List<RepaymentBizPlanDto> planDtoList = dto.getPlanDtoList();
//        for (RepaymentBizPlanDto repaymentBizPlanDto : planDtoList) {
//            //先更新标的
//            updateProjPlanList(repaymentBizPlanDto.getProjPlanDtos(), dto);
//            //再更新业务
//            updateBizPlanList(repaymentBizPlanDto.getBizPlanListDtos(), dto, repaymentBizPlanDto);
//        }
//
//        for (Map.Entry<String, Map<String, List<RepaymentProjFactRepay>>> mpe : dto.getProjFactRepays().entrySet()) {
//            for (Map.Entry<String, List<RepaymentProjFactRepay>> mpe1 : mpe.getValue().entrySet()) {
//                for (RepaymentProjFactRepay factRepay : mpe1.getValue()) {
//                    factRepay.setSettleLogId(dto.getRepaymentSettleLog().getSettleLogId());
//                    factRepay.updateAllColumnById();
//                }
//            }
//        }
//
//
//        dto.getRepaymentSettleLog().insert();
//    }
    private void updateProjPlanList(List<RepaymentProjPlanDto> projPlanDtos, FinanceSettleBaseDto dto) {
        for (RepaymentProjPlanDto repaymentProjPlanDto : projPlanDtos) {
            RepaymentProjPlan repaymentProjPlan = repaymentProjPlanDto.getRepaymentProjPlan();
            /* 标的亏损结清标志位,本金没还够*/
            boolean deficitSettle = false;
            /*标的坏账结清标志,线上除滞纳金部分没还够*/
            boolean badSettle = false;
            List<RepaymentProjPlanListDto> projPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
                RepaymentProjPlanList projPlanList = projPlanListDto.getRepaymentProjPlanList();
                BigDecimal projPlanAmount = projPlanList.getTotalBorrowAmount();
                BigDecimal projFactAmount = BigDecimal.ZERO;
                BigDecimal projOverAmount = projPlanList.getOverdueAmount() != null ? projPlanList.getOverdueAmount() : BigDecimal.ZERO;
                BigDecimal projDerateAmout = projPlanList.getDerateAmount() != null ? projPlanList.getDerateAmount() : BigDecimal.ZERO;
                BigDecimal projOnlinePlanAmount = BigDecimal.ZERO;
                BigDecimal projOnlineFactAmount = BigDecimal.ZERO;

                List<RepaymentProjPlanListDetail> projPlanListDetails = projPlanListDto.getProjPlanListDetails();
                for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDetails) {
                    projFactAmount = projFactAmount.add(projPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : projPlanListDetail.getProjFactAmount());
                    /*统计线上部分除滞纳金以外的实还和应还*/
                    if (projPlanListDetail.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY && projPlanListDetail.getPlanItemType() != 60) {
                        projOnlinePlanAmount = projOnlinePlanAmount.add(projPlanListDetail.getProjPlanAmount());
                        projOnlineFactAmount = projFactAmount.add(projPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : projPlanListDetail.getProjFactAmount());
                    }

					/*判断是否本金没还够*/
                    if (Integer.valueOf(10).equals(projPlanListDetail.getPlanItemType())) {
                        BigDecimal item10planAmount = projPlanListDetail.getProjPlanAmount();
                        BigDecimal item10factAmount = projPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : projPlanListDetail.getProjFactAmount();
                        if (item10factAmount.compareTo(item10planAmount) < 0) {
                            deficitSettle = true;
                        }
                    }

                    if (!dto.getPreview()) {
                        projPlanListDetail.updateAllColumnById();
                    }
                }

                if (projOnlineFactAmount.compareTo(projOnlinePlanAmount) < 0) {
                    badSettle = true;
                }

                Collections.sort(projPlanListDetails, new Comparator<RepaymentProjPlanListDetail>() {
                    @Override
                    public int compare(RepaymentProjPlanListDetail arg0, RepaymentProjPlanListDetail arg1) {
                        if (arg0.getFactRepayDate().before(arg1.getFactRepayDate())) {
                            return 1;
                        }
                        if (arg0.getFactRepayDate().after(arg1.getFactRepayDate())) {
                            return -1;
                        }
                        return 0;
                    }
                });
                projPlanList.setFactRepayDate(projPlanListDetails.get(0).getFactRepayDate());
                if (projPlanAmount.add(projOverAmount).compareTo(projDerateAmout.add(projFactAmount)) <= 0) {
                    projPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
                    projPlanList.setCurrentSubStatus(RepayCurrentStatusEnums.已还款.toString());
                    projPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
                    projPlanList.setRepayFlag(RepayPlanPayedTypeEnum.OFFLINE_CHECK_SETTLE.getValue());
                } else {

                }

                if (!dto.getPreview()) {
                    projPlanList.updateAllColumnById();
                }
            }

            if (dto.getPreSettle()) {
                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_EARLY.getValue());
            }
            if (deficitSettle) {
                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_LOSS.getValue());
            }
            if (badSettle) {
                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_BAD.getValue());
            }

            if (!dto.getPreview()) {
                repaymentProjPlan.updateAllColumnById();
            }
        }
    }

    private void updateBizPlanList(List<RepaymentBizPlanListDto> bizPlanListDtos, FinanceSettleBaseDto dto, RepaymentBizPlanDto repaymentBizPlanDto) {
        for (RepaymentBizPlanListDto repaymentBizPlanListDto : bizPlanListDtos) {
            /* 标的亏损结清标志位,本金没还够*/
            boolean deficitSettle = false;
            /*标的坏账结清标志,线上除滞纳金部分没还够*/
            boolean badSettle = false;
            List<RepaymentBizPlanListDetail> bizPlanListDetails = repaymentBizPlanListDto.getBizPlanListDetails();
            RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListDto.getRepaymentBizPlanList();
            BigDecimal planAmount = repaymentBizPlanList.getTotalBorrowAmount();
            BigDecimal planFactAmount = BigDecimal.ZERO;
            BigDecimal planOverAmount = repaymentBizPlanList.getOverdueAmount() != null ? repaymentBizPlanList.getOverdueAmount() : BigDecimal.ZERO;
            BigDecimal planDerateAmout = repaymentBizPlanList.getDerateAmount() != null ? repaymentBizPlanList.getDerateAmount() : BigDecimal.ZERO;
            BigDecimal onlinePlanAmount = BigDecimal.ZERO;
            BigDecimal onlineFactAmount = BigDecimal.ZERO;

            for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : bizPlanListDetails) {
                planFactAmount = planFactAmount.add(repaymentBizPlanListDetail.getFactAmount() == null ? BigDecimal.ZERO : repaymentBizPlanListDetail.getFactAmount());
                /*统计线上部分除滞纳金以外的实还和应还*/
                if (repaymentBizPlanListDetail.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY && repaymentBizPlanListDetail.getPlanItemType() != 60) {
                    onlinePlanAmount = onlinePlanAmount.add(repaymentBizPlanListDetail.getPlanAmount());
                    onlineFactAmount = onlineFactAmount.add(repaymentBizPlanListDetail.getFactAmount() == null ? BigDecimal.ZERO : repaymentBizPlanListDetail.getFactAmount());
                }

				/*判断是否本金没还够*/
                if (Integer.valueOf(10).equals(repaymentBizPlanListDetail.getPlanItemType())) {
                    BigDecimal item10planAmount = repaymentBizPlanListDetail.getPlanAmount();
                    BigDecimal item10factAmount = repaymentBizPlanListDetail.getFactAmount() == null ? BigDecimal.ZERO : repaymentBizPlanListDetail.getFactAmount();
                    if (item10factAmount.compareTo(item10planAmount) < 0) {
                        deficitSettle = true;
                    }
                }

                if (!dto.getPreview()) {
                    repaymentBizPlanListDetail.updateAllColumnById();
                }
            }

            if (onlineFactAmount.compareTo(onlinePlanAmount) < 0) {
                badSettle = true;
            }

            Collections.sort(bizPlanListDetails, new Comparator<RepaymentBizPlanListDetail>() {
                @Override
                public int compare(RepaymentBizPlanListDetail arg0, RepaymentBizPlanListDetail arg1) {
                    if (arg0.getFactRepayDate().before(arg1.getFactRepayDate())) {
                        return 1;
                    }
                    if (arg0.getFactRepayDate().after(arg1.getFactRepayDate())) {
                        return -1;
                    }
                    return 0;
                }
            });
            repaymentBizPlanList.setFactRepayDate(bizPlanListDetails.get(0).getFactRepayDate());
            if (planAmount.add(planOverAmount).compareTo(planDerateAmout.add(planFactAmount)) <= 0) {
                repaymentBizPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
                repaymentBizPlanList.setCurrentSubStatus(RepayCurrentStatusEnums.已还款.toString());
                repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
                repaymentBizPlanList.setRepayFlag(RepayPlanPayedTypeEnum.OFFLINE_CHECK_SETTLE.getValue());
            } else {
            }

            if (dto.getPreSettle()) {
                repaymentBizPlanDto.getRepaymentBizPlan().setPlanStatus(RepayPlanSettleStatusEnum.PAYED_EARLY.getValue());
            }
            if (deficitSettle) {
                repaymentBizPlanDto.getRepaymentBizPlan().setPlanStatus(RepayPlanSettleStatusEnum.PAYED_LOSS.getValue());
            }
            if (badSettle) {
                repaymentBizPlanDto.getRepaymentBizPlan().setPlanStatus(RepayPlanSettleStatusEnum.PAYED_BAD.getValue());
            }

            if (!dto.getPreview()) {
                repaymentBizPlanList.updateAllColumnById();
                repaymentBizPlanDto.getRepaymentBizPlan().updateAllColumnById();
            }
        }
    }

    /**
     * 备份以前的记录
     *
     * @param dto
     * @author 王继光
     * 2018年7月17日 上午10:33:52
     */
    private void bak(FinanceSettleBaseDto dto) {
        dto.getRepaymentBizPlanBaks();
        dto.getRepaymentBizPlanListBaks();
        dto.getRepaymentBizPlanListDetailBaks();
        dto.getRepaymentProjPlanBaks();
        dto.getRepaymentProjPlanListBaks();
        dto.getRepaymentProjPlanListDetailBaks();

        for (RepaymentBizPlanBak bizPlanBak : dto.getRepaymentBizPlanBaks()) {
            if (StringUtil.isEmpty(bizPlanBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            bizPlanBak.insert();
        }

        for (RepaymentBizPlanListBak bizPlanListBak : dto.getRepaymentBizPlanListBaks()) {
            if (StringUtil.isEmpty(bizPlanListBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            bizPlanListBak.insert();
        }

        for (RepaymentBizPlanListDetailBak bizPlanListDetailBak : dto.getRepaymentBizPlanListDetailBaks()) {
            if (StringUtil.isEmpty(bizPlanListDetailBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            bizPlanListDetailBak.insert();
        }

        for (RepaymentProjPlanBak projPlanBak : dto.getRepaymentProjPlanBaks()) {
            if (StringUtil.isEmpty(projPlanBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            projPlanBak.insert();
        }

        for (RepaymentProjPlanListBak projPlanListBak : dto.getRepaymentProjPlanListBaks()) {
            if (StringUtil.isEmpty(projPlanListBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            projPlanListBak.insert();
        }

        for (RepaymentProjPlanListDetailBak projPlanListDetailBak : dto.getRepaymentProjPlanListDetailBaks()) {
            if (StringUtil.isEmpty(projPlanListDetailBak.getSettleLogId())) {
                throw new ServiceRuntimeException("bak的settle_log_id不能为空");
            }
            projPlanListDetailBak.insert();
        }
    }
    
    @Override
	public SettleInfoVO settleInfoVO(FinanceSettleReq req) {
		RepaymentBizPlanList cur = new RepaymentBizPlanList() ;
		cur.setOrigBusinessId(req.getBusinessId());cur.setAfterId(req.getAfterId());
		cur = repaymentBizPlanListMapper.selectOne(cur);
		if (cur==null) {
			throw new ServiceRuntimeException("找不到当前期还款计划");
		}
		
		List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().eq("plan_list_id", cur.getPlanListId()).eq("is_finance_match", 1).orderBy("trade_date",false));
		SettleInfoVO infoVO = new SettleInfoVO() ;
		Date settleDate = null ;
		if (CollectionUtils.isEmpty(moneyPoolRepayments)) {
			settleDate = new Date();
		}else {
			settleDate = moneyPoolRepayments.get(0).getTradeDate() ;
		}

		int diff = DateUtil.getDiffDays(cur.getDueDate(), settleDate);
		if (diff>0&&cur.getCurrentStatus().equals(RepayPlanStatus.OVERDUE.getName())) {
			infoVO.setOverDueDays(diff);
		}
		infoVO.setItem10(repaymentProjPlanListDetailMapper.calcUnpaidPrincipal(req.getBusinessId(), req.getPlanId()));
		calcCurPeriod(cur,infoVO,settleDate);



		infoVO.setRepayPlanDate(cur.getDueDate());


		infoVO.setDerates(repaymentBizPlanListDetailMapper.selectLastPlanListDerateFees(req.getBusinessId(),cur.getDueDate(), req.getPlanId()));
		infoVO.setLackFees(repaymentBizPlanListDetailMapper.selectLastPlanListLackFees(req.getBusinessId(),cur.getDueDate(), req.getPlanId()));

		infoVO.setPenalty(calcPenalty(cur, req.getPlanId()));
		
		infoVO.setSubtotal(infoVO.getSubtotal().add(infoVO.getItem10()).add(infoVO.getItem20()).add(infoVO.getItem30()).add(infoVO.getItem50()));
		infoVO.setTotal(infoVO.getTotal().add(infoVO.getSubtotal()).add(infoVO.getOfflineOverDue()).add(infoVO.getOnlineOverDue()).add(infoVO.getDerate()).add(infoVO.getPlanRepayBalance()));
		
		return infoVO;
	}
	
	/**
	 * 计算提前还款违约金
	 * @author 王继光
	 * 2018年7月11日 下午10:06:09
	 * @param bizPlanList
	 * @param planId
	 * @return
	 */
	private BigDecimal calcPenalty(RepaymentBizPlanList bizPlanList,String planId) {
		List<ProjExtRate> extRates = projExtRateMapper
				.selectList(new EntityWrapper<ProjExtRate>().eq("business_id", bizPlanList.getOrigBusinessId())
						.le("begin_peroid", bizPlanList.getPeriod()).ge("end_peroid", bizPlanList.getPeriod()));
		BigDecimal penalty = BigDecimal.ZERO;
		for (ProjExtRate projExtRate : extRates) {
			switch (projExtRate.getCalcWay()) {
			//根据计算方式不同分别计算
			case 1:
				//1.借款金额*费率值
				RepaymentProjPlan projPlan = repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("project_id", projExtRate.getProjectId()));
				penalty = penalty.add(projPlan.getBorrowMoney().multiply(projExtRate.getRateValue())) ;
				break;
			case 2:
				//2剩余本金*费率值
				BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
				penalty = penalty.add(upaid.multiply(projExtRate.getRateValue())) ;
				break;
			case 3:
				//3.1*费率值'
				penalty = penalty.add(projExtRate.getRateValue());
				break;
			default:
				break;
			}
		}
		return penalty;
	}
	
	/**
	 * 计算当前期未还金额
	 * @author 王继光
	 * 2018年7月7日 下午4:33:49
	 * @param repaymentBizPlanList
	 * @param infoVO
	 */
	private void calcCurPeriod(RepaymentBizPlanList repaymentBizPlanList,SettleInfoVO infoVO,Date factRepayDate) {
		BigDecimal item20 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "20", null);
		BigDecimal item30 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "30", null);
		BigDecimal item50 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "50", null);
		
		infoVO.setItem20(item20);
		infoVO.setItem30(item30);
		infoVO.setItem50(item50);
		
		repaymentBizPlanList = repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanList);
		BigDecimal item60offline = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
		BigDecimal item60online = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
		//TODO 要调用滞纳金计算
		infoVO.setOfflineOverDue(infoVO.getOfflineOverDue().add(item60offline));
		infoVO.setOnlineOverDue(infoVO.getOnlineOverDue().add(item60online));
	}
	
	/**
	 * 查找往期还款计划
	 * @author 王继光
	 * 2018年7月5日 下午6:23:48
	 * @param cur
	 * @param planId
	 * @return
	 */
	private List<RepaymentBizPlanList> selectLastPlanLists(RepaymentBizPlanList cur, String planId){
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", cur.getOrigBusinessId()).eq("after_id", cur.getAfterId());
		planListEW.lt("due_date", cur.getDueDate());
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		return repaymentBizPlanListMapper.selectList(planListEW);
	}
	
	/**
	 * 查询往后的还款计划
	 * @author 王继光
	 * 2018年7月5日 下午6:25:18
	 * @param cur
	 * @param planId
	 * @return
	 */
	private List<RepaymentBizPlanList> selectNextPlanLists(RepaymentBizPlanList cur,String planId){
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", cur.getOrigBusinessId());
		planListEW.gt("due_date", cur.getDueDate());
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		return repaymentBizPlanListMapper.selectList(planListEW);
	}

}
