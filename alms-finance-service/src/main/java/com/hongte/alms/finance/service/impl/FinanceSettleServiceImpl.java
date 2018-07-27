package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.baseException.SettleRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.exception.ExceptionCodeEnum;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executor;

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
    Executor executor;

    @Autowired
    RepaymentBizPlanListSynchMapper repaymentBizPlanListSynchMapper;
    @Autowired
    RepaymentBizPlanBakMapper repaymentBizPlanBakMapper;

    @Autowired
    RepaymentBizPlanListBakMapper repaymentBizPlanListBakMapper;

    @Autowired
    RepaymentBizPlanListDetailBakMapper repaymentBizPlanListDetailBakMapper;
    @Autowired
    RepaymentConfirmLogMapper repaymentConfirmLogMapper;

    @Autowired
    @Qualifier("RepaymentConfirmPlatRepayLogService")
    RepaymentConfirmPlatRepayLogService repaymentConfirmPlatRepayLogService;

    @Autowired
    RepaymentBizPlanListSynchService repaymentBizPlanListSynchService;

    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    RepaymentConfirmLogService confirmLogService;

    @Autowired
    @Qualifier("ShareProfitService")
    ShareProfitService shareProfitService;

    @Autowired
    @Qualifier("MoneyPoolService")
    MoneyPoolService moneyPoolService;

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
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService bizPlanListService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    private RepaymentBizPlanService bizPlanService;

    @Autowired
    @Qualifier("AccountantOverRepayLogService")
    private AccountantOverRepayLogService accountantOverRepayLogService;

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
    public List<CurrPeriodProjDetailVO> financeSettle(FinanceSettleReq financeSettleReq) {
        FinanceSettleBaseDto financeSettleBaseDto = new FinanceSettleBaseDto();
        financeSettleBaseDto.setPreview(financeSettleReq.getPreview());
        //结算流水ID
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);
        financeSettleBaseDto.setPreview(financeSettleReq.getPreview());
        LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
        if (loginInfo != null) {
            financeSettleBaseDto.setUserId(loginInfo.getUserId());
            financeSettleBaseDto.setUserName(loginInfo.getUserName());
        }
        financeSettleBaseDto.setBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setAfterId(financeSettleReq.getAfterId());
        financeSettleBaseDto.setOrgBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setPlanId(financeSettleReq.getPlanId());

        makeMonePoolRepayment(financeSettleBaseDto, financeSettleReq);
        /*创建结清记录*/
//        createSettleLog(financeSettleBaseDto, financeSettleReq);
        /*计算结余金额*/
        calcSurplus(financeSettleBaseDto, financeSettleReq);
        //生成流水消费记录

        //数据填充及bak
        makeRepaymentPlanAllPlan(financeSettleBaseDto, financeSettleReq);

        //开始标的结清
//        shareProjSettleMoney(financeSettleBaseDto, financeSettleReq);

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


        //查出当前还款计划的当前期
        RepaymentBizPlanList repaymentBizPlanList = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));

//        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).orderBy("due_date", true));


        if (repaymentBizPlanList != null) {
            Date dueDate = repaymentBizPlanList.getDueDate();

            if (StringUtil.isEmpty(planId)) {// 整个业务结清


                List<RepaymentBizPlanSettleDto> bizPlanSettleDtos = getCurrentPeriod(financeSettleReq);

                Map<String, RepaymentProjPlanSettleDto> projPlanSettleDtoMap = new HashMap<>();

                for (RepaymentBizPlanSettleDto repaymentBizPlanSettleDto : bizPlanSettleDtos) {
                    List<RepaymentProjPlanSettleDto> repaymentProjPlanSettleDtos = repaymentBizPlanSettleDto.getProjPlanStteleDtos();

                    for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : repaymentProjPlanSettleDtos) {
                        RepaymentProjPlanSettleDto projPlanSettleDto = projPlanSettleDtoMap.get(repaymentProjPlanSettleDto.getTuandaiProjectInfo().getProjectId());
                        if (projPlanSettleDto != null) {
                            logger.error("此业务有两个以上相同的标的还款计划 projPlanSettleDto" + JSON.toJSONString(projPlanSettleDto));
                            throw new SettleRepaymentExcepiton("此业务有两个以上相同的标的还款计划", ExceptionCodeEnum.TOW_PROJ_PLAN.getValue().toString());
                        }
                        projPlanSettleDtoMap.put(repaymentProjPlanSettleDto.getTuandaiProjectInfo().getProjectId(), repaymentProjPlanSettleDto);
                    }
                }


//                ProjPlanDtoUtil.sort();


//                //根据业务id找出这个业务的所有还款计划列表
//                List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));
//
//                if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {
//
//                    for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) { //多个还款计划
//                        String planIdInner = repaymentBizPlan.getPlanId();
//
//                        //查出此业务还款计划的每一期 按应还日期进行升序排序
//                        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planIdInner).orderBy("due_date", true));
//                        if (CollectionUtils.isNotEmpty(repaymentBizPlanLists)) {
//                            Boolean flag = false;
//                            Set<Integer> repayStatusSet = new HashSet<>();
//                            List<RepaymentBizPlanList> list = new ArrayList<>();//保存本次还款计划的当前期
//                            for (RepaymentBizPlanList repaymentBizPlanListInner : repaymentBizPlanLists) {
//                                Date dueDateInner = repaymentBizPlanListInner.getDueDate();
//                                if (!flag && !afterId.equals(repaymentBizPlanListInner.getAfterId())) { //当前期之前 并且不为当前期
//                                    repayStatusSet.add(repaymentBizPlanListInner.getRepayStatus() == null ? 0 : repaymentBizPlanListInner.getRepayStatus());
//                                }
//
//                                if (dueDateInner.getTime() - dueDate.getTime() >= 0) { //业务期数 应还日期大于当期日期
//                                    if (CollectionUtils.isEmpty(list)) {
//                                        list.add(repaymentBizPlanListInner);
//                                    }
//                                    flag = true;
//                                }
//                                if (flag && (repayStatusSet.contains(0) || repayStatusSet.contains(1))) { // 找到当前期以后开始检查 0代表未还款 1代表部分还款
//                                    throw new ServiceRuntimeException("500", "当前期数不能进行结清");
//                                }
//                            }
//
//                            //通过当期期数查出当前期前面 及后面部分
//                            if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
//                                RepaymentBizPlanList repaymentBizPlanListNow = list.get(0);
//
//                                makeRepaymentPlanOnePlan(financeSettleBaseDto, repaymentBizPlanListNow, financeSettleReq);
//                            }
//                        }else{
//
//                            logger.error("找业务还款计划的期数信息 RepaymentBizPlanList  businessId:"+businessId+"     planId:"+planIdInner);
//                            throw new SettleRepaymentExcepiton("找业务还款计划的期数信息", ExceptionCodeEnum.NO_BIZ_PLAN.getValue().toString());
//                        }
//
//
//                    }
//                }else{
//                    logger.error("找不到业务的还款计划 repaymentBizPlan  businessId:"+businessId);
//                    throw new SettleRepaymentExcepiton("找不到业务的还款计划", ExceptionCodeEnum.NO_BIZ_PLAN.getValue().toString());
//                }

            } else {
                //单个还款计划结清
//                RepaymentBizPlan repaymentBizPlan = bizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planId));

                makeRepaymentPlanOnePlan(financeSettleBaseDto, repaymentBizPlanList, financeSettleReq);
            }

        } else {
            logger.error("找不到对应的还款计划列表repaymentBizPlanList  businessId:" + businessId + "     after_id:" + afterId);
            throw new SettleRepaymentExcepiton("找不到对应的还款计划列表", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
        }

    }

    public void makeRepaymentPlanMultPlan(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

    }

    public void makeRepaymentPlanOnePlan(FinanceSettleBaseDto financeSettleBaseDto, RepaymentBizPlanList repaymentBizPlanListNow, FinanceSettleReq financeSettleReq) {

        String businessId = financeSettleReq.getBusinessId();
//        String planIdNow = financeSettleReq.getPlanId();
        String planIdNow = repaymentBizPlanListNow.getPlanId();
        String afterIdNow = repaymentBizPlanListNow.getAfterId();

        List<RepaymentBizPlanList> repaymentBizPlanListNowList = new ArrayList<>();

        //通过当期的业务list 调用滞纳金生成接口 单独开启事务  数据库字段滞纳金已更新

        RepaymentBizPlanList repaymentBizPlanList = repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanListNow, 1);


        //先通过应还时间排序  先还后还的分好类
        //再通过标的还款顺序进行分类

        //查出所有的标的明细项 并按还款时间进行排序 由低到高
        List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtos = repaymentProjPlanMapper.selectProjPlanMoney(businessId, planIdNow);

        List<RepaymentSettleMoneyDto> beforSettleMoneyDto = new ArrayList<>(); //先结算
        List<RepaymentSettleMoneyDto> afterSettleMoneyDto = new ArrayList<>(); //后结算
        List<RepaymentSettleMoneyDto> nowSettleMoneyDto = new ArrayList<>(); //当前期


        Boolean flagNow = false;
        if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtos)) {
            Set<Integer> repayStatusSet = new HashSet<>();
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtos) {
                Integer repayStatus = repaymentSettleMoneyDto.getRepayStatus(); //部分还款状态子状态,null:未还款,1:部分还款,2:线上已还款,3:全部已还款
                BigDecimal money = repaymentSettleMoneyDto.getMoney();
                if (!flagNow && !afterIdNow.equals(repaymentSettleMoneyDto.getAfterId()) && !repayStatus.equals(SectionRepayStatusEnum.ALL_REPAID.getKey())) { //添加当前期之前的 不为已还款
                    repayStatusSet.add(repaymentSettleMoneyDto.getRepayStatus() == null ? 0 : repaymentSettleMoneyDto.getRepayStatus());
                    if (repaymentSettleMoneyDto.getShareProfitIndex().intValue() >= Constant.ONLINE_OFFLINE_FEE_BOUNDARY.intValue() && money.compareTo(BigDecimal.ZERO) > 0) {
                        afterSettleMoneyDto.add(repaymentSettleMoneyDto);
                    }
                } else if (afterIdNow.equals(repaymentSettleMoneyDto.getAfterId())) { //如果期数 等于当前期数
                    nowSettleMoneyDto.add(repaymentSettleMoneyDto);
                    //线下部分 添加到后结算 即为差额
//                    if (repaymentSettleMoneyDto.getShareProfitIndex().intValue() >= 1200 && money.compareTo(BigDecimal.ZERO) > 0) {
//                        afterSettleMoneyDto.add(repaymentSettleMoneyDto);  //线下部分 添加到后结算
//                    } else { //小于1200的添加到 前结算
//                        beforSettleMoneyDto.add(repaymentSettleMoneyDto); //本期  本金添加到前结算
//                    }
                    flagNow = true; //找到当前期
                } else if (flagNow && !afterIdNow.equals(repaymentSettleMoneyDto.getAfterId()) && !repayStatus.equals(SectionRepayStatusEnum.ALL_REPAID.getKey())) { //添加当前期之后的
                    if (repaymentSettleMoneyDto.getPlanItemType().equals(RepayPlanFeeTypeEnum.PRINCIPAL.getValue()) && money.compareTo(BigDecimal.ZERO) > 0) {
                        beforSettleMoneyDto.add(repaymentSettleMoneyDto);
                    }
                }
                if (flagNow && (repayStatusSet.contains(0) || repayStatusSet.contains(1))) { // 找到当前期以后开始检查 0代表未还款 1代表部分还款
                    throw new ServiceRuntimeException("500", "当前期数不能进行结清");
                }

            }
        }


        //标的还款顺序分类
        List<RepaymentSettleProjDto> repaymentSettleProjDtos = repaymentProjPlanMapper.orderSettleProj(businessId, planIdNow);
        RepaymentSettleMoneyDto repaymentSettleMoneyDtoNow = null;
        if (nowSettleMoneyDto.size() > 0) {
            repaymentSettleMoneyDtoNow = nowSettleMoneyDto.get(0);
        }
        //扣减提前违约金额 planIdNow页面传入
        List<RepaymentSettleMoneyDto> settleMoneyList = settleFeesCreateMoney(repaymentBizPlanListNow, planIdNow, repaymentSettleMoneyDtoNow);

        //其它费用项
        List<RepaymentSettleMoneyDto> otherFeesList = otherFeesCreateMoney(financeSettleReq);

        //所有标的应还
        Map<String, CurrPeriodProjDetailVO> projMoneyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(repaymentSettleProjDtos)) {
            for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
                String projPlanId = repaymentSettleProjDto.getProjPlanId();
                CurrPeriodProjDetailVO vo = new CurrPeriodProjDetailVO();


                if (CollectionUtils.isNotEmpty(beforSettleMoneyDto)) {
                    for (RepaymentSettleMoneyDto moneyDto : beforSettleMoneyDto) {
                        String moneyDtoPlanId = moneyDto.getProjPlanId();
                        if (moneyDtoPlanId.equals(projPlanId)) {
                            sumProjMoney(vo, moneyDto);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(nowSettleMoneyDto)) {
                    for (RepaymentSettleMoneyDto moneyDto : nowSettleMoneyDto) {
                        String moneyDtoPlanId = moneyDto.getProjPlanId();
                        if (moneyDtoPlanId.equals(projPlanId)) {
                            sumProjMoney(vo, moneyDto);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(afterSettleMoneyDto)) {
                    for (RepaymentSettleMoneyDto moneyDto : afterSettleMoneyDto) {
                        String moneyDtoPlanId = moneyDto.getProjPlanId();
                        if (moneyDtoPlanId.equals(projPlanId)) {
                            sumProjMoney(vo, moneyDto);
                        }
                    }
                }

                projMoneyMap.put(projPlanId, vo);
            }
        }


        //拆分还款的规则应优先还共借标的，在还主借标的，若有多个共借标，则有优先还上标金额较小的标的，若共借标中的金额先等，则优先还满标时间较早的标的
        Boolean flag = true;


        if (CollectionUtils.isNotEmpty(repaymentSettleProjDtos)) {

            if (CollectionUtils.isNotEmpty(beforSettleMoneyDto)) {
                //单个标线上
                flag = canceBeforMoney(beforSettleMoneyDto, nowSettleMoneyDto, repaymentSettleProjDtos, financeSettleBaseDto);
            }
            if (flag && CollectionUtils.isNotEmpty(settleMoneyList)) {
                //提前违约金
                flag = canceSettleMoney(repaymentSettleProjDtos, settleMoneyList, financeSettleBaseDto);
            }

            if (flag) {
                //单个线下
                flag = canceAfterMoney(nowSettleMoneyDto, afterSettleMoneyDto, repaymentSettleProjDtos, financeSettleBaseDto);
            }

            if (flag) {
                //其它费用
                flag = otherFeesList(otherFeesList, financeSettleBaseDto);
            }
        }


        Boolean preview = financeSettleReq.getPreview() == null ? true : financeSettleReq.getPreview();
        //状态更改
        if (!preview) { //保存操作
            if (MapUtils.isNotEmpty(projMoneyMap)) {


                //更新标的状态
                BigDecimal allMoney = updateProjPlan(projMoneyMap, financeSettleBaseDto, afterIdNow);
                //更新业务的状态 及保存结清操作记录 更新流水状态
                updateBizPlan(projMoneyMap, financeSettleBaseDto, allMoney, planIdNow, afterIdNow);

//                updateMoneyPoolRepayment(financeSettleReq);

                //调用标的合规和还款接口 标的list
//                repayLog(financeSettleBaseDto);

            }
        }

        //标的实还
        List<CurrPeriodProjDetailVO> currPeriodProjDetailVOList = financeSettleBaseDto.getCurrPeriodProjDetailVOList();
        for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
            String projPlanId = repaymentSettleProjDto.getProjPlanId();
            int isMast = repaymentSettleProjDto.getIsMast();
            Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
            if (MapUtils.isNotEmpty(webFactRepays)) {
                CurrPeriodProjDetailVO currPeriodProjDetailVO = webFactRepays.get(projPlanId);
                if (currPeriodProjDetailVO != null) {
                    currPeriodProjDetailVO.setMaster(isMast == 1 ? true : false);
                    currPeriodProjDetailVO.setUserName(repaymentSettleProjDto.getUserName());
                    currPeriodProjDetailVO.setProjAmount(repaymentSettleProjDto.getProjMoney());
                    currPeriodProjDetailVOList.add(currPeriodProjDetailVO);
                }

            }
        }
        if (CollectionUtils.isNotEmpty(currPeriodProjDetailVOList)) {
            int index = currPeriodProjDetailVOList.size() - 1;
            currPeriodProjDetailVOList.get(index).setSurplus(financeSettleBaseDto.getCuralDivideAmount());
        }


//        if(!financeSettleBaseDto.getPreview()){
//            throw new ServiceRuntimeException("测试用");
//        }
    }

    public void updateMoneyPoolRepayment(FinanceSettleReq financeSettleReq) {
        //保存还款来源信息
        List<String> mprIds = financeSettleReq.getMprIds();
        if (CollectionUtils.isNotEmpty(mprIds)) {
            for (String id : mprIds) {
                MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment();
                moneyPoolRepayment.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
                moneyPoolRepayment.setId(Integer.parseInt(id));
                moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
            }
        }
    }

    public Boolean otherFeesList(List<RepaymentSettleMoneyDto> otherFeesList, FinanceSettleBaseDto financeSettleBaseDto) {
        //开始进行结算
        Boolean flag = true;
        if (CollectionUtils.isNotEmpty(otherFeesList)) {
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : otherFeesList) {
                flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                if (!flag) {
                    break;
                }
            }
        }
        return flag;
    }

    public List<RepaymentSettleMoneyDto> otherFeesCreateMoney(FinanceSettleReq financeSettleReq) {
        List<SettleFeesVO> otherFees = financeSettleReq.getOtherFees();
        List<RepaymentSettleMoneyDto> settleFeesMoneyLists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(otherFees)) {

            for (SettleFeesVO settleFeesVO : otherFees) {
                String feeId = settleFeesVO.getFeeId();
                String feeName = settleFeesVO.getFeeName();
                RepaymentSettleMoneyDto repaymentSettleMoneyDto = new RepaymentSettleMoneyDto();
                repaymentSettleMoneyDto.setFeeId(feeId);


                settleFeesMoneyLists.add(repaymentSettleMoneyDto);
            }
        }

        return settleFeesMoneyLists;
    }

    public Boolean canceSettleMoney(List<RepaymentSettleProjDto> repaymentSettleProjDtos, List<RepaymentSettleMoneyDto> settleMoneyList, FinanceSettleBaseDto financeSettleBaseDto) {
        //开始进行结算
        Boolean flag = true;
        for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
            String projPlanId = repaymentSettleProjDto.getProjPlanId();
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : settleMoneyList) {
                String projPlanIdInner = repaymentSettleMoneyDto.getProjPlanId();
                if (projPlanId.equals(projPlanIdInner)) {
                    flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                }
                if (!flag) {
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    //组装提前违约金
    public List<RepaymentSettleMoneyDto> settleFeesCreateMoney(RepaymentBizPlanList repaymentBizPlanListNow, String planIdNow, RepaymentSettleMoneyDto repaymentSettleMoneyDtoNow) {
        List<SettleFeesVO> settleFeesList = calcPenalty(repaymentBizPlanListNow, planIdNow);


        List<RepaymentSettleMoneyDto> settleFeesMoneyLists = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(settleFeesList) && repaymentSettleMoneyDtoNow != null) {


            for (SettleFeesVO settleFeesVO : settleFeesList) {
                String projectId = settleFeesVO.getProjectId();
                BigDecimal amount = settleFeesVO.getAmount();
                String planItemType = settleFeesVO.getPlanItemType();
                String feeId = settleFeesVO.getFeeId();
                String feeName = settleFeesVO.getFeeName();
                RepaymentSettleMoneyDto repaymentSettleMoneyDto = new RepaymentSettleMoneyDto();
                RepaymentProjPlan repaymentProjPlan = repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("project_id", projectId));
                repaymentSettleMoneyDto.setMoney(amount);
                repaymentSettleMoneyDto.setAfterId(repaymentSettleMoneyDtoNow.getAfterId());
                repaymentSettleMoneyDto.setPlanItemType(Integer.parseInt(planItemType));
                repaymentSettleMoneyDto.setPlanItemName(settleFeesVO.getPlanItemName());
                repaymentSettleMoneyDto.setFeeId(feeId);
                repaymentSettleMoneyDto.setPeriod(repaymentSettleMoneyDtoNow.getPeriod());
                repaymentSettleMoneyDto.setProjPlanId(repaymentProjPlan.getProjPlanId());
                repaymentSettleMoneyDto.setProjectId(settleFeesVO.getProjectId());

                if (repaymentProjPlan != null) {
                    repaymentSettleMoneyDto.setBusinessId(repaymentProjPlan.getBusinessId());
                    repaymentSettleMoneyDto.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());
                }

                settleFeesMoneyLists.add(repaymentSettleMoneyDto);
            }
        }

        return settleFeesMoneyLists;
    }

    /**
     * 更新业务的状态
     *
     * @param projMoneyMap
     * @param financeSettleBaseDto
     * @param allMoney
     * @param planIdNow
     * @param afterIdNow
     */
    public void updateBizPlan(Map<String, CurrPeriodProjDetailVO> projMoneyMap, FinanceSettleBaseDto financeSettleBaseDto, BigDecimal allMoney, String planIdNow, String afterIdNow) {
        //所有的金额 更新业务还款计划状态
        BigDecimal allFactMoney = BigDecimal.ZERO;
        if (allMoney.compareTo(BigDecimal.ZERO) > 0) {//
            Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
            if (MapUtils.isNotEmpty(webFactRepays)) {
                CurrPeriodProjDetailVO newVo = new CurrPeriodProjDetailVO();

                //累加所有标的实还
                for (String proj : webFactRepays.keySet()) {
                    CurrPeriodProjDetailVO vo = webFactRepays.get(proj);
                    newVo.setItem10(newVo.getItem10().add(vo.getItem10()));
                    newVo.setItem20(newVo.getItem20().add(vo.getItem20()));
                    newVo.setItem30(newVo.getItem30().add(vo.getItem30()));
                    newVo.setItem50(newVo.getItem50().add(vo.getItem50()));
                    newVo.setItem70(newVo.getItem70().add(vo.getItem70()));
                    newVo.setOtherMoney(newVo.getOtherMoney().add(vo.getOtherMoney()));

                    newVo.setOfflineOverDue(newVo.getOfflineOverDue().add(vo.getOfflineOverDue()));
                    newVo.setOnlineOverDue(newVo.getOnlineOverDue().add(vo.getOnlineOverDue()));
                }
                newVo.setTotal(newVo.getItem10().add(newVo.getItem20()).add(newVo.getItem30()).add(newVo.getItem50())
                        .add(newVo.getOfflineOverDue()).add(newVo.getOnlineOverDue()));

                allFactMoney = newVo.getItem10().add(newVo.getItem20()).add(newVo.getItem30()).add(newVo.getItem50())
                        .add(newVo.getOfflineOverDue()).add(newVo.getOnlineOverDue()).add(newVo.getItem70()).add(newVo.getOtherMoney());
                //累加所有应还
                CurrPeriodProjDetailVO sourceVo = new CurrPeriodProjDetailVO();

                for (String proj : projMoneyMap.keySet()) {
                    CurrPeriodProjDetailVO vo = projMoneyMap.get(proj);
                    sourceVo.setItem10(sourceVo.getItem10().add(vo.getItem10()));
                    sourceVo.setItem20(sourceVo.getItem20().add(vo.getItem20()));
                    sourceVo.setItem30(sourceVo.getItem30().add(vo.getItem30()));
                    sourceVo.setItem50(sourceVo.getItem50().add(vo.getItem50()));
                    sourceVo.setItem70(sourceVo.getItem70().add(vo.getItem70()));
                    sourceVo.setOtherMoney(sourceVo.getOtherMoney().add(vo.getOtherMoney()));
                    sourceVo.setOfflineOverDue(sourceVo.getOfflineOverDue().add(vo.getOfflineOverDue()));
                    sourceVo.setOnlineOverDue(sourceVo.getOnlineOverDue().add(vo.getOnlineOverDue()));
                }
                sourceVo.setTotal(sourceVo.getItem10().add(sourceVo.getItem20()).add(sourceVo.getItem30()).add(sourceVo.getItem50())
                        .add(sourceVo.getOfflineOverDue()).add(sourceVo.getOnlineOverDue()));


                if (newVo != null) {
                    RepaymentBizPlanList repaymentBizPlanList = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planIdNow).eq("after_id", afterIdNow));
                    String planListIdNow = repaymentBizPlanList.getPlanListId();//当期


                    //整个还款计划其它期 都为已还款
                    List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planIdNow));
                    if (CollectionUtils.isNotEmpty(repaymentBizPlanLists)) {
                        for (RepaymentBizPlanList repaymentBizPlanListN : repaymentBizPlanLists) {
                            RepaymentBizPlanListBak repaymentBizPlanListBak = new RepaymentBizPlanListBak();
                            repaymentBizPlanListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                            BeanUtils.copyProperties(repaymentBizPlanListN, repaymentBizPlanListBak);
                            repaymentBizPlanListBakMapper.insert(repaymentBizPlanListBak);


                            repaymentBizPlanListN.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
                            repaymentBizPlanListN.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
                            repaymentBizPlanListN.setRepayFlag(PepayPlanRepayFlagStatusEnum.UNDERLINE_ALL_SETTLE.getValue());
                            repaymentBizPlanListN.setFactRepayDate(new Date());
                            repaymentBizPlanListN.setFinanceComfirmDate(new Date());
                            repaymentBizPlanListN.setFinanceConfirmUser(financeSettleBaseDto.getUserId());
                            repaymentBizPlanListN.setFinanceConfirmUserName(financeSettleBaseDto.getUserName());
                            repaymentBizPlanListMapper.updateById(repaymentBizPlanListN);

                            RepaymentBizPlanListSynch synch = new RepaymentBizPlanListSynch();
                            synch.setPlanListId(repaymentBizPlanListN.getPlanListId());
                            synch = repaymentBizPlanListSynchMapper.selectOne(synch);
                            synch.setCurrentStatus(repaymentBizPlanListN.getCurrentStatus());
                            synch.setCurrentSubStatus(repaymentBizPlanListN.getCurrentSubStatus());
                            synch.setRepayStatus(repaymentBizPlanListN.getRepayStatus());
                            synch.setRepayFlag(repaymentBizPlanListN.getRepayFlag());
                            synch.setFinanceConfirmUser(repaymentBizPlanListN.getFinanceConfirmUser());
                            synch.setFinanceConfirmUserName(repaymentBizPlanListN.getFinanceConfirmUserName());

                            if (planListIdNow.equals(repaymentBizPlanListN.getPlanListId())) {
                                synch.setFactAmountExt(allFactMoney);
                            }
                            repaymentBizPlanListSynchMapper.updateAllColumnById(synch);

                        }


                    }

                    RepaymentBizPlan repaymentBizPlan = bizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", planIdNow));

                    RepaymentBizPlanBak repaymentBizPlanBak = new RepaymentBizPlanBak();
                    repaymentBizPlanBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                    BeanUtils.copyProperties(repaymentBizPlan, repaymentBizPlanBak);
                    repaymentBizPlanBakMapper.insert(repaymentBizPlanBak);


                    if (repaymentBizPlanList != null) {
                        String planListId = repaymentBizPlanList.getPlanListId();
                        List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planListId));
                        if (CollectionUtils.isNotEmpty(repaymentBizPlanListDetails)) {
                            for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {

                                RepaymentBizPlanListDetailBak repaymentBizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
                                repaymentBizPlanListDetailBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                                BeanUtils.copyProperties(repaymentBizPlanListDetail, repaymentBizPlanListDetailBak);
                                repaymentBizPlanListDetailBakMapper.insert(repaymentBizPlanListDetailBak);


                                Integer planItemType = repaymentBizPlanListDetail.getPlanItemType();
                                BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount() == null ? BigDecimal.ZERO : repaymentBizPlanListDetail.getFactAmount();
                                switch (planItemType) {
                                    case 10:
                                        repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getItem10()));
                                        break;
                                    case 20:
                                        repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getItem20()));
                                        break;
                                    case 30:
                                        repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getItem30()));
                                        break;
                                    case 50:
                                        repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getItem50()));
                                        break;
                                    case 60:
                                        if (repaymentBizPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                                            repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getOnlineOverDue()));
                                        }
                                        if (repaymentBizPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                                            repaymentBizPlanListDetail.setFactAmount(factAmount.add(newVo.getOfflineOverDue()));
                                        }
                                        break;
                                    default:
                                        logger.info("未定义的类型!!!{}||{}||{}", repaymentBizPlanListDetail.getPlanItemName(), repaymentBizPlanListDetail.getPlanItemType());
                                        break;
                                }
                                repaymentBizPlanListDetail.setFactRepayDate(new Date());
                                logger.info("=====>>>repaymentBizPlanListDetail{}", repaymentBizPlanListDetail);
                                repaymentBizPlanListDetailMapper.updateById(repaymentBizPlanListDetail);
                            }

                            //保存结余
                            BigDecimal endMoney = financeSettleBaseDto.getCuralDivideAmount();
                            String overRepayLogId = null;
                            if (endMoney.compareTo(BigDecimal.ZERO) > 0) {
                                AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
                                accountantOverRepayLog.setBusinessAfterId(repaymentBizPlanList.getAfterId());
                                accountantOverRepayLog.setBusinessId(repaymentBizPlanList.getBusinessId());
                                accountantOverRepayLog.setCreateTime(new Date());
                                accountantOverRepayLog.setCreateUser(financeSettleBaseDto.getUserId());
                                accountantOverRepayLog.setFreezeStatus(0);
                                accountantOverRepayLog.setIsRefund(0);
                                accountantOverRepayLog.setIsTemporary(0);
                                accountantOverRepayLog.setMoneyType(1);
                                accountantOverRepayLog.setOverRepayMoney(endMoney);
                                accountantOverRepayLog
                                        .setRemark(String.format("收入于%s的%s期线下财务结清", repaymentBizPlanList.getBusinessId(), repaymentBizPlanList.getAfterId()));


                                accountantOverRepayLog.insert();
                                overRepayLogId = accountantOverRepayLog.getId().toString();

                            }
                            //保存结清操作记录
                            BigDecimal factMoney = sourceVo.getTotal().add(sourceVo.getItem70()).add(sourceVo.getOtherMoney());
                            if (factMoney.compareTo(BigDecimal.ZERO) > 0) {
                                RepaymentConfirmLog repaymentConfirmLog = new RepaymentConfirmLog();
                                repaymentConfirmLog.setConfirmLogId(financeSettleBaseDto.getUuid());
                                repaymentConfirmLog.setRepayDate(new Date());
                                repaymentConfirmLog.setBusinessId(repaymentBizPlanList.getBusinessId());
                                repaymentConfirmLog.setOrgBusinessId(repaymentBizPlanList.getOrigBusinessId());
                                repaymentConfirmLog.setFactAmount(factMoney);
                                repaymentConfirmLog.setProjPlanJson(JSON.toJSONString(sourceVo));
                                repaymentConfirmLog.setSurplusUseRefId(overRepayLogId);
                                repaymentConfirmLog.setSurplusAmount(endMoney);
                                repaymentConfirmLog.setAfterId(repaymentBizPlanList.getAfterId());
                                repaymentConfirmLog.setPeriod(repaymentBizPlanList.getPeriod());
                                repaymentConfirmLog.setCreateTime(new Date());
                                repaymentConfirmLog.setRepaySource(10);

                                repaymentConfirmLogMapper.insert(repaymentConfirmLog);
                            }


                            //更新流水状态

                        }


                        BigDecimal moneySource = sourceVo.getItem10().add(sourceVo.getItem20()).add(sourceVo.getItem30()).add(sourceVo.getItem50()).add(sourceVo.getOnlineOverDue()).add(sourceVo.getOfflineOverDue());
                        BigDecimal moneyNew = newVo.getItem10().add(newVo.getItem20()).add(newVo.getItem30()).add(newVo.getItem50()).add(newVo.getOnlineOverDue()).add(newVo.getOfflineOverDue());

                        if (moneySource.compareTo(moneyNew) == 0) { //实还和应还相等 已还完
                            repaymentBizPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED.getValue());


                            repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
                            repaymentBizPlanList.setFactRepayDate(new Date());
                            repaymentBizPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());

//
                        } else if (moneySource.compareTo(moneyNew) > 0) { //实还小于 应还

                            if (sourceVo.getItem10().compareTo(newVo.getItem10()) > 0) { //应还本金大于实还本金 //走到这一步 亏损结清
                                repaymentBizPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_LOSS.getValue());

                                repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
                            } else if (sourceVo.getItem20().compareTo(newVo.getItem20()) > 0
                                    || sourceVo.getItem30().compareTo(newVo.getItem30()) > 0
                                    || sourceVo.getItem50().compareTo(newVo.getItem50()) > 0
                                    || sourceVo.getOnlineOverDue().compareTo(newVo.getOnlineOverDue()) > 0) { //走到这一步 坏账结清
                                repaymentBizPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_BAD.getValue());

                                repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
                            } else if (sourceVo.getOfflineOverDue().compareTo(newVo.getOfflineOverDue()) > 0) { //走到这一步 线上部分已还完
                                repaymentBizPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED.getValue());

                                repaymentBizPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
                            }
                        }
//                        logger.info("=====>>>repaymentBizPlanList{}", repaymentBizPlanList);
//                        repaymentBizPlanListMapper.updateById(repaymentBizPlanList);
                        logger.info("=====>>>repaymentBizPlan{}", repaymentBizPlan);
                        //更新标的状态
                        repaymentBizPlanMapper.updateById(repaymentBizPlan);
                    }
                }


            }
        }
    }

    /**
     * 更新标的状态
     *
     * @param projMoneyMap
     * @param financeSettleBaseDto
     * @param afterIdNow
     */
    public BigDecimal updateProjPlan(Map<String, CurrPeriodProjDetailVO> projMoneyMap, FinanceSettleBaseDto financeSettleBaseDto, String afterIdNow) {

        List<RepaymentProjPlanList> ptojPlanList = financeSettleBaseDto.getPtojPlanList();
        BigDecimal allMoney = BigDecimal.ZERO;
        for (String projId : projMoneyMap.keySet()) {
            CurrPeriodProjDetailVO moneyVo = projMoneyMap.get(projId);
            allMoney = allMoney.add(moneyVo.getTotal());
            Map<String, CurrPeriodProjDetailVO> webFactRepays = financeSettleBaseDto.getWebFactRepays();
            if (MapUtils.isNotEmpty(webFactRepays)) {
                CurrPeriodProjDetailVO vo = webFactRepays.get(projId);
                if (vo != null) {
                    BigDecimal total = vo.getTotal();
                    RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListService.selectOne(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projId).eq("after_id", afterIdNow));

                    RepaymentProjPlanListBak repaymentProjPlanListBak = new RepaymentProjPlanListBak();
                    repaymentProjPlanListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                    BeanUtils.copyProperties(repaymentProjPlanList, repaymentProjPlanListBak);
                    repaymentProjPlanListBakMapper.insert(repaymentProjPlanListBak);


                    if (repaymentProjPlanList != null) {

                        RepaymentProjPlan repaymentProjPlan = repaymentProjPlanService.selectById(projId);

                        RepaymentProjPlanBak repaymentProjPlanBak = new RepaymentProjPlanBak();
                        repaymentProjPlanBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                        BeanUtils.copyProperties(repaymentProjPlan, repaymentProjPlanBak);
                        repaymentProjPlanBakMapper.insert(repaymentProjPlanBak);

                        String projPlanListId = repaymentProjPlanList.getProjPlanListId();
                        List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId));
                        if (CollectionUtils.isNotEmpty(repaymentProjPlanListDetails)) {
                            for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {

                                RepaymentProjPlanListDetailBak repaymentProjPlanListDetailBak = new RepaymentProjPlanListDetailBak();
                                repaymentProjPlanListDetailBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                                BeanUtils.copyProperties(repaymentProjPlanListDetail, repaymentProjPlanListDetailBak);
                                repaymentProjPlanListDetailBakMapper.insert(repaymentProjPlanListDetailBak);


                                Integer planItemType = repaymentProjPlanListDetail.getPlanItemType();
                                BigDecimal projFactAmount = repaymentProjPlanListDetail.getProjFactAmount() == null ? BigDecimal.ZERO : repaymentProjPlanListDetail.getProjFactAmount();
                                switch (planItemType) {
                                    case 10:
                                        repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getItem10()));
                                        break;
                                    case 20:
                                        repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getItem20()));
                                        break;
                                    case 30:
                                        repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getItem30()));
                                        break;
                                    case 50:
                                        repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getItem50()));
                                        break;
                                    case 60:
                                        if (repaymentProjPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                                            repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getOnlineOverDue()));
                                        }
                                        if (repaymentProjPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                                            repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(vo.getOfflineOverDue()));
                                        }
                                        break;
                                    default:
                                        logger.info("未定义的类型!!!{}||{}||{}", repaymentProjPlanListDetail.getPlanItemName(), repaymentProjPlanListDetail.getPlanItemType());
                                        break;
                                }
                                repaymentProjPlanListDetail.setFactRepayDate(new Date());
                                logger.info("=====>>>repaymentProjPlanListDetail{}", repaymentProjPlanListDetail);
                                repaymentProjPlanListDetailMapper.updateById(repaymentProjPlanListDetail);
                            }

                        }

                        BigDecimal moneySource = moneyVo.getItem10().add(moneyVo.getItem20()).add(moneyVo.getItem30()).add(moneyVo.getItem50()).add(moneyVo.getOnlineOverDue()).add(moneyVo.getOfflineOverDue());
                        BigDecimal moneyFacte = vo.getItem10().add(vo.getItem20()).add(vo.getItem30()).add(vo.getItem50()).add(vo.getOnlineOverDue()).add(vo.getOfflineOverDue());

                        if (moneySource.compareTo(moneyFacte) == 0) { //实还和应还相等 已还完
                            repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED.getValue());


                            repaymentProjPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
                            repaymentProjPlanList.setFactRepayDate(new Date());
                            repaymentProjPlanList.setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());

//
                        } else if (moneySource.compareTo(moneyFacte) > 0) { //实还小于 应还

                            if (moneyVo.getItem10().compareTo(vo.getItem10()) > 0) { //应还本金大于实还本金 //走到这一步 亏损结清
                                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_LOSS.getValue());
                                repaymentProjPlanList.setRepayStatus(1);
                            } else if (moneyVo.getItem20().compareTo(vo.getItem20()) > 0
                                    || moneyVo.getItem30().compareTo(vo.getItem30()) > 0
                                    || moneyVo.getItem50().compareTo(vo.getItem50()) > 0
                                    || moneyVo.getOnlineOverDue().compareTo(vo.getOnlineOverDue()) > 0) { //走到这一步 坏账结清
                                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED_BAD.getValue());

                                repaymentProjPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
                            } else if (moneyVo.getOfflineOverDue().compareTo(vo.getOfflineOverDue()) > 0) { //走到这一步 线上部分已还完
                                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.PAYED.getValue());

                                repaymentProjPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
                            }
                        }
                        logger.info("=====>>>repaymentProjPlanList{}", repaymentProjPlanList);
                        //添加合规化list
                        ptojPlanList.add(repaymentProjPlanList);
                        repaymentProjPlanListMapper.updateById(repaymentProjPlanList);
                        logger.info("=====>>>repaymentProjPlan{}", repaymentProjPlan);
                        //更新标的状态
                        repaymentProjPlanMapper.updateById(repaymentProjPlan);

                    }
                }

            }
        }

        return allMoney;
    }

    public void sumSettleFees(CurrPeriodProjDetailVO vo, SettleFeesVO settleFeesVO) {
        Integer planItemType = Integer.parseInt(settleFeesVO.getPlanItemType());
        BigDecimal amount = settleFeesVO.getAmount();
        switch (planItemType.intValue()) {
            case 70:
                vo.setItem70(vo.getItem70().add(amount));
                break;

            default:
                logger.info("未定义的类型!!!{}||{}||{}", settleFeesVO.getPlanItemName(), settleFeesVO.getPlanItemType(), amount);
                break;
        }

        vo.setTotal(vo.getTotal().add(amount));


    }

    //累加所有标的明细项
    public void sumProjMoney(CurrPeriodProjDetailVO vo, RepaymentSettleMoneyDto moneyDto) {
        String projPlanIdDetail = moneyDto.getProjPlanId();
        Integer planItemType = moneyDto.getPlanItemType();
        BigDecimal amount = moneyDto.getMoney();
        switch (planItemType.intValue()) {
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
            case 70:
                vo.setItem70(vo.getItem70().add(amount));
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

        vo.setTotal(vo.getItem10().add(vo.getItem20()).add(vo.getItem30()).add(vo.getItem50()).add(vo.getOfflineOverDue()).add(vo.getOnlineOverDue()));

    }

    public void repayLog(FinanceSettleBaseDto financeSettleBaseDto) {
        List<RepaymentProjPlanList> ptojPlanList = financeSettleBaseDto.getPtojPlanList();
        if (CollectionUtils.isNotEmpty(ptojPlanList)) {
            //将本次推送的标还款计划listId记录下来
            for (RepaymentProjPlanList projPlanList : ptojPlanList) {
                RepaymentConfirmPlatRepayLog log = new RepaymentConfirmPlatRepayLog();
                log.setConfirmLogId(financeSettleBaseDto.getUuid());
                log.setProjPlanListId(projPlanList.getProjPlanListId());
                log.setCreateTime(new Date());
                log.setCreateUser(loginUserInfoHelper.getUserId() == null ? Constant.ADMIN_ID : loginUserInfoHelper.getUserId());
                repaymentConfirmPlatRepayLogService.insert(log);
            }
            shareProfitService.tdrepayRechargeThread(ptojPlanList, financeSettleBaseDto.getUuid());
        }

    }


    public Boolean canceBeforMoney(List<RepaymentSettleMoneyDto> beforSettleMoneyDto, List<RepaymentSettleMoneyDto> nowSettleMoneyDto, List<RepaymentSettleProjDto> repaymentSettleProjDtos, FinanceSettleBaseDto financeSettleBaseDto) {
        Map<String, List<RepaymentSettleMoneyDto>> mapBefor = new HashMap<>();
        for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
            List<RepaymentSettleMoneyDto> beforeList = new ArrayList<>();
            //单个标的线上部分
            String projPlanId = repaymentSettleProjDto.getProjPlanId();
            //所有的往后期的线上累加
            BigDecimal beforMoney = BigDecimal.ZERO;
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoBefor : beforSettleMoneyDto) {
                String projPlanIdBefor = repaymentSettleMoneyDtoBefor.getProjPlanId();
                if (projPlanIdBefor.equals(projPlanId)) {
                    beforMoney = beforMoney.add(repaymentSettleMoneyDtoBefor.getMoney());
                }

            }
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoNow : nowSettleMoneyDto) {
                String projPlanIdNow = repaymentSettleMoneyDtoNow.getProjPlanId();
                Integer planItemType = repaymentSettleMoneyDtoNow.getPlanItemType();
                BigDecimal money = repaymentSettleMoneyDtoNow.getMoney();
                if (projPlanIdNow.equals(projPlanId)) {
                    if (RepayPlanFeeTypeEnum.PRINCIPAL.getValue().equals(planItemType) && beforMoney.compareTo(BigDecimal.ZERO) > 0) {
                        beforMoney = beforMoney.add(money);
                        repaymentSettleMoneyDtoNow.setMoney(beforMoney);
                        beforeList.add(repaymentSettleMoneyDtoNow);
                    } else if (repaymentSettleMoneyDtoNow.getShareProfitIndex().intValue() < 1200) { //线上部分的其它项
                        beforeList.add(repaymentSettleMoneyDtoNow);
                    }

                }
            }
            mapBefor.put(projPlanId, beforeList);
        }


        //开始进行结算
        Boolean flag = true;
        if (MapUtils.isNotEmpty(mapBefor)) {
            for (String projPlanIdInner : mapBefor.keySet()) {
                List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtosBefors = mapBefor.get(projPlanIdInner);
                if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtosBefors)) {
                    for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtosBefors) {
                        flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                        if (!flag) {
                            break;
                        }
                    }

                }
                if (!flag) {
                    break;
                }
            }
        }
        return flag;
    }


    //其它期的本金

    public Boolean canceAfterMoney(List<RepaymentSettleMoneyDto> nowSettleMoneyDto, List<RepaymentSettleMoneyDto> afterSettleMoneyDto, List<RepaymentSettleProjDto> repaymentSettleProjDtos, FinanceSettleBaseDto financeSettleBaseDto) {
        //标的线下部分
        Map<String, List<RepaymentSettleMoneyDto>> mapAfter = new HashMap<>();

        for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
            List<RepaymentSettleMoneyDto> afterList = new ArrayList<>();
            String projPlanId = repaymentSettleProjDto.getProjPlanId();
            //所有的往后期的线下累加
            BigDecimal afterMoney = BigDecimal.ZERO;
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoAfter : afterSettleMoneyDto) {
                String projPlanIdBefor = repaymentSettleMoneyDtoAfter.getProjPlanId();
                if (projPlanIdBefor.equals(projPlanId)) {
                    afterMoney = afterMoney.add(repaymentSettleMoneyDtoAfter.getMoney());
                }

            }
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoNow : nowSettleMoneyDto) {
                String projPlanIdNow = repaymentSettleMoneyDtoNow.getProjPlanId();
                Integer planItemType = repaymentSettleMoneyDtoNow.getPlanItemType();
                BigDecimal money = repaymentSettleMoneyDtoNow.getMoney();
                if (projPlanIdNow.equals(projPlanId)) {
                    if (repaymentSettleMoneyDtoNow.getShareProfitIndex().intValue() >= 1200) { //线上部分的其它项
                        afterMoney = afterMoney.add(money);
                        repaymentSettleMoneyDtoNow.setMoney(afterMoney);
                        afterList.add(repaymentSettleMoneyDtoNow);
                    }

                }
            }
            mapAfter.put(projPlanId, afterList);
        }


        //开始进行结算
        Boolean flag = true;
        if (MapUtils.isNotEmpty(mapAfter)) {
            for (String projPlanIdInner : mapAfter.keySet()) {
                List<RepaymentSettleMoneyDto> repaymentSettleMoneyDtosBefors = mapAfter.get(projPlanIdInner);
                if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtosBefors)) {
                    for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : repaymentSettleMoneyDtosBefors) {
                        flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                        if (!flag) {
                            break;
                        }
                    }

                }
                if (!flag) {
                    break;
                }
            }
        }
        return flag;

    }

    //其它期的线下部分
    public Boolean canceAfterSettleMoney(List<RepaymentSettleMoneyDto> nowSettleMoneyDto, List<RepaymentSettleMoneyDto> afterSettleMoneyDto, List<RepaymentSettleProjDto> repaymentSettleProjDtos, FinanceSettleBaseDto financeSettleBaseDto) {
        List<RepaymentSettleMoneyDto> sortAfterMoneyDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nowSettleMoneyDto) && CollectionUtils.isNotEmpty(repaymentSettleProjDtos)) { //当前期

            for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleProjDtos) {
                String projPlanId = repaymentSettleProjDto.getProjPlanId();
                for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : nowSettleMoneyDto) {
                    String sumProjPlanId = repaymentSettleMoneyDto.getProjPlanId();
                    logger.info("=========sumProjPlanId=" + sumProjPlanId);

                    if (projPlanId.equals(sumProjPlanId)) {  //通过数据库查出排序的标ID 再对当前期排序
                        Integer sumPlanItemType = repaymentSettleMoneyDto.getPlanItemType();
                        BigDecimal projPlanAmount = repaymentSettleMoneyDto.getMoney(); //应还金额
                        logger.info("=========projPlanAmount=" + projPlanAmount);
                        Integer sumShareProfitIndex = repaymentSettleMoneyDto.getShareProfitIndex();
                        if (sumShareProfitIndex.intValue() >= 1200) {  //如果为本金明细项
                            if (CollectionUtils.isNotEmpty(afterSettleMoneyDto)) { //如果之前期不为空 本金部分 前面已过滤
                                BigDecimal sumBjMoney = BigDecimal.ZERO;
                                for (RepaymentSettleMoneyDto repaymentSettleMoneyDtoBefor : afterSettleMoneyDto) {
                                    if (sumProjPlanId.equals(repaymentSettleMoneyDtoBefor.getProjPlanId())) { //标ID相同
                                        logger.info("=========repaymentSettleMoneyDtoAfter.getProjPlanId()=" + repaymentSettleMoneyDtoBefor.getProjPlanId());
                                        if (sumPlanItemType.equals(repaymentSettleMoneyDtoBefor.getPlanItemType())) { //并且里面的项相同则累加
                                            logger.info("=========repaymentSettleMoneyDtoAfter.getMoney()=" + repaymentSettleMoneyDtoBefor.getMoney());
                                            sumBjMoney = sumBjMoney.add(repaymentSettleMoneyDtoBefor.getMoney());
                                            logger.info("=========sumBjMoney=" + projPlanAmount);

                                        }
                                    }
                                }
                                repaymentSettleMoneyDto.setMoney(sumBjMoney);
                                sortAfterMoneyDto.add(repaymentSettleMoneyDto);
                            }
                        }
                        logger.info("=========repaymentSettleMoneyDto.getProjPlanAmount=" + repaymentSettleMoneyDto.getProjPlanAmount());
                    }
                }
            }

        }
        //开始进行结算
        Boolean flag = true;
        if (CollectionUtils.isNotEmpty(sortAfterMoneyDto)) {
            for (RepaymentSettleMoneyDto repaymentSettleMoneyDto : sortAfterMoneyDto) {
                flag = paySettleMoney(financeSettleBaseDto, repaymentSettleMoneyDto, null);
                if (!flag) {
                    break;
                }
            }
        }

        return flag;
    }


    //通过标排序 进行标的明细项排序
    public List<RepaymentSettleMoneyDto> sortProjIdMoneyDto(List<RepaymentSettleProjDto> repaymentSettleMoneyDtoOder, List<RepaymentSettleMoneyDto> moneyDto) {
        List<RepaymentSettleMoneyDto> sortMoneyDto = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(repaymentSettleMoneyDtoOder)) { //标的排序

            for (RepaymentSettleProjDto repaymentSettleProjDto : repaymentSettleMoneyDtoOder) {
                String projPlanId = repaymentSettleProjDto.getProjPlanId();

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
            logger.info("@@第" + (rIdex.intValue() + 1) + "笔流水,总额=" + repaymentResource.getRepayAmount());
        } else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(BigDecimal.ZERO) == 0) {
            if (rIdex > repaymentResources.size() - 1) {
                return false;
            } else {
                RepaymentResource repaymentResource = repaymentResources.get(rIdex);
                financeSettleBaseDto.setCuralResource(repaymentResource);
                financeSettleBaseDto.setCuralDivideAmount(repaymentResource.getRepayAmount());
                logger.info("@@第" + (rIdex.intValue() + 1) + "笔流水,总额=" + repaymentResource.getRepayAmount());
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
            money = financeSettleBaseDto.getCuralDivideAmount();
            logger.info("@@从curalDivideAmount={}分unpaid={}到{},还需还款{}", curalDivideAmount, curalDivideAmount,
                    moneyDto.getPlanItemName(), unpaid.subtract(money));

            createSettleLogDetail(money, moneyDto, repaymentResource, financeSettleBaseDto);
            logger.info("@@第" + (rIdex.intValue() + 1) + "笔流水已分完,流水ID=" + repaymentResource.getRepaySourceRefId());
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

    public void createSettleLogDetail(BigDecimal amount, RepaymentSettleMoneyDto moneyDto, RepaymentResource resource, FinanceSettleBaseDto financeSettleBaseDto) {

        String projPlanDetailId = moneyDto.getProjPlanDetailId();
        BigDecimal projFactAmount = moneyDto.getProjFactAmount() == null ? BigDecimal.ZERO : moneyDto.getProjFactAmount();


        RepaymentProjFactRepay fact = new RepaymentProjFactRepay();
        fact.setAfterId(moneyDto.getAfterId());
        fact.setBusinessId(moneyDto.getBusinessId());
        fact.setCreateDate(new Date());
        fact.setCreateUser(financeSettleBaseDto.getUserId());
        fact.setOrigBusinessId(moneyDto.getOrigBusinessId());
        fact.setProjectId(moneyDto.getProjectId());
        fact.setPeriod(moneyDto.getPeriod());
        fact.setPlanItemName(moneyDto.getPlanItemName());
        fact.setPlanItemType(moneyDto.getPlanItemType());
        fact.setFeeId(moneyDto.getFeeId());
        fact.setPlanListId(moneyDto.getPlanListId());
        fact.setProjPlanDetailId(moneyDto.getProjPlanDetailId());
        fact.setProjPlanListId(moneyDto.getProjPlanListId());
        fact.setFactRepayDate(resource.getRepayDate());// 还款来源日期
        fact.setRepayRefId(resource.getRepaySourceRefId());// 还款来源id
        fact.setRepaySourceId(resource.getResourceId());
        fact.setRepaySource(Integer.valueOf(resource.getRepaySource()));// 还款来源类别

        fact.setProjPlanDetailRepayId(UUID.randomUUID().toString());
        fact.setConfirmLogId(financeSettleBaseDto.getUuid());
        fact.setFactAmount(amount);


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
            case 70:
                vo.setItem70(vo.getItem70().add(amount));
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
                vo.setOtherMoney(vo.getOtherMoney().add(amount));
                logger.info("未定义的类型!!!{}||{}||{}", moneyDto.getPlanItemName(), moneyDto.getPlanItemType(), amount);
                break;
        }

        //用户扣款详情回填dto
//        List<RepaymentSettleLogDetail> repaymentSettleLogDetailLists = financeSettleBaseDto.getRepaymentSettleLogDetailList();
//        repaymentSettleLogDetailLists.add(repaymentSettleLogDetail);


        Boolean preview = financeSettleBaseDto.getPreview() == null ? true : financeSettleBaseDto.getPreview();
        if (!preview) { //保存操作 true 为预览 false为保存
            repaymentProjFactRepayMapper.insert(fact);

//            RepaymentProjPlanListDetail repaymentProjPlanListDetail = new RepaymentProjPlanListDetail();
//            repaymentProjPlanListDetail.setProjPlanDetailId(projPlanDetailId);
//            repaymentProjPlanListDetail.setProjFactAmount(projFactAmount.add(amount));
//            repaymentProjPlanListDetail.setFactRepayDate(new Date());
//            repaymentProjPlanListDetailMapper.updateById(repaymentProjPlanListDetail);

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

                repaymentResource.setConfirmLogId(financeSettleBaseDto.getUuid());

                /*log.setFactAmount(log.getFactAmount().add(resource.getRepayAmount()));
                log.setRepayDate(dto.getRepaymentResources().get(dto.getRepaymentResources().size()-1).getRepayDate());
    			log.setRepaySource(Integer.valueOf(resource.getRepaySource()));
    			*/
                repaymentResources.add(repaymentResource);
//                financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
//                financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
//                financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
//                financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
                if (!financeSettleReq.getPreview()) {
                    repaymentResource.insert();
                }
            }
        }
        if (surplus != null && surplus.compareTo(new BigDecimal("0")) > 0) {
            BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(financeSettleReq.getBusinessId(), null);
            if (surplus.compareTo(canUseSurplus) > 0) {
                throw new ServiceRuntimeException("往期结余金额不足");
            }

            AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
            accountantOverRepayLog.setBusinessAfterId(financeSettleReq.getAfterId());
            accountantOverRepayLog.setBusinessId(financeSettleReq.getBusinessId());
            accountantOverRepayLog.setCreateTime(new Date());
            accountantOverRepayLog.setCreateUser(financeSettleBaseDto.getUserId());
            accountantOverRepayLog.setFreezeStatus(0);
            accountantOverRepayLog.setIsRefund(0);
            accountantOverRepayLog.setIsTemporary(0);
            accountantOverRepayLog.setMoneyType(0);
            accountantOverRepayLog.setOverRepayMoney(financeSettleReq.getSurplusFund());
            accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务結清", financeSettleReq.getBusinessId(), financeSettleReq.getAfterId()));


            RepaymentResource repaymentResource = new RepaymentResource();
            repaymentResource.setAfterId(financeSettleReq.getAfterId());
            repaymentResource.setBusinessId(financeSettleReq.getBusinessId());
            repaymentResource.setOrgBusinessId(financeSettleReq.getBusinessId());
            repaymentResource.setCreateDate(new Date());
            repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
            repaymentResource.setIsCancelled(0);
            repaymentResource.setRepayAmount(financeSettleReq.getSurplusFund());
            repaymentResource.setRepayDate(new Date());
//            repaymentResource.setSettleLogId(financeSettleBaseDto.getRepaymentSettleLog().getSettleLogId());
            repaymentResource.setConfirmLogId(financeSettleBaseDto.getUuid());
            //11:用往期结余还款',
            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.SURPLUS_REPAY.getValue().toString());

            if (!financeSettleReq.getPreview()) {
                accountantOverRepayLog.insert();
                financeSettleBaseDto.getRepaymentSettleLog().setSurplusUseRefId(accountantOverRepayLog.getId().toString());

                repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
                repaymentResource.insert();
                if (mprIds.size() == 0) {
                    financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
                }

            }

            repaymentResources.add(repaymentResource);
            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
            financeSettleBaseDto.getRepaymentSettleLog().setRepaySource(Integer.valueOf(repaymentResource.getRepaySource()));
            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(financeSettleReq.getSurplusFund()));
            financeSettleBaseDto.getRepaymentSettleLog().setFactAmount(financeSettleBaseDto.getRepayFactAmount());
        }
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
                                repaymentProjPlanListDetailBakMapper.insert(repaymentProjPlanListDetailBak);

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
        RepaymentBizPlanList cur = new RepaymentBizPlanList();
        cur.setOrigBusinessId(req.getBusinessId());
        cur.setAfterId(req.getAfterId());
        cur = repaymentBizPlanListMapper.selectOne(cur);
        if (cur == null) {
            throw new ServiceRuntimeException("找不到当前期还款计划");
        }

        List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().eq("plan_list_id", cur.getPlanListId()).eq("is_finance_match", 1).orderBy("trade_date", false));
        SettleInfoVO infoVO = new SettleInfoVO();
        Date settleDate = null;
        if (CollectionUtils.isEmpty(moneyPoolRepayments)) {
            settleDate = new Date();
        } else {
            settleDate = moneyPoolRepayments.get(0).getTradeDate();
        }

        int diff = DateUtil.getDiffDays(cur.getDueDate(), settleDate);
        if (diff > 0 && cur.getCurrentStatus().equals(RepayPlanStatus.OVERDUE.getName())) {
            infoVO.setOverDueDays(diff);
        }
        infoVO.setItem10(repaymentProjPlanListDetailMapper.calcUnpaidPrincipal(req.getBusinessId(), req.getPlanId()));
        calcCurPeriod(cur, infoVO, settleDate, req.getPlanId());


        infoVO.setRepayPlanDate(cur.getDueDate());


        infoVO.setDerates(repaymentBizPlanListDetailMapper.selectLastPlanListDerateFees(req.getBusinessId(), cur.getDueDate(), req.getPlanId()));
        infoVO.setLackFees(repaymentBizPlanListDetailMapper.selectLastPlanListLackFees(req.getBusinessId(), cur.getDueDate(), req.getPlanId()));

        infoVO.setPenaltyFees(calcPenalty(cur, req.getPlanId()));

        if (req.getOtherFees() == null) {
            infoVO.setOtherFees(new ArrayList<>());
        } else {
            infoVO.setOtherFees(req.getOtherFees());
        }
        infoVO.setSubtotal(infoVO.getSubtotal().add(infoVO.getItem10()).add(infoVO.getItem20()).add(infoVO.getItem30()).add(infoVO.getItem50()));
        infoVO.setTotal(infoVO.getTotal().add(infoVO.getSubtotal()).add(infoVO.getOfflineOverDue()).add(infoVO.getOnlineOverDue()).subtract(infoVO.getDerate()).add(infoVO.getPlanRepayBalance()).add(infoVO.getPenalty()).add(infoVO.getOther()));

        return infoVO;
    }

    /**
     * 计算提前还款违约金
     *
     * @param bizPlanList
     * @param planId
     * @return
     * @author 王继光
     * 2018年7月11日 下午10:06:09
     */
    private List<SettleFeesVO> calcPenalty(RepaymentBizPlanList bizPlanList, final String planId) {
        EntityWrapper<RepaymentProjPlanList> eWrapper = new EntityWrapper<RepaymentProjPlanList>();
        eWrapper.eq("plan_list_id", bizPlanList.getPlanListId());
        if (!StringUtil.isEmpty(planId)) {
            eWrapper.eq("plan_id", planId);
        }
        List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListMapper.selectList(eWrapper);
        Set<String> projPlanStrs = new HashSet<>();
        for (RepaymentProjPlanList projPlanList : projPlanLists) {
            projPlanStrs.add(projPlanList.getProjPlanId());
        }
        List<RepaymentProjPlan> projPlans = repaymentProjPlanMapper.selectList(new EntityWrapper<RepaymentProjPlan>().in("proj_plan_id", projPlanStrs));
        Set<String> projectIds = new HashSet<>();
        for (RepaymentProjPlan projPlan : projPlans) {
            projectIds.add(projPlan.getProjectId());
        }
        List<ProjExtRate> extRates = projExtRateMapper
                .selectList(new EntityWrapper<ProjExtRate>()
                        .eq("business_id", bizPlanList.getOrigBusinessId())
                        .in("project_id", projectIds)
                        .eq("rate_type", RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())
                        .le("begin_peroid", bizPlanList.getPeriod())
                        .ge("end_peroid", bizPlanList.getPeriod()));

        List<SettleFeesVO> fees = new ArrayList<>();
        for (ProjExtRate projExtRate : extRates) {
            BigDecimal penalty = BigDecimal.ZERO;
            SettleFeesVO fee = new SettleFeesVO();
            if (PepayPlanProjExtRatCalEnum.BY_BORROW_MONEY.getValue() == projExtRate.getCalcWay()) {
                //1.借款金额*费率值
                TuandaiProjectInfo projectInfo = tuandaiProjectInfoMapper.selectById(projExtRate.getProjectId());
                penalty = projectInfo.getBorrowAmount().multiply(projExtRate.getRateValue());

            } else if (PepayPlanProjExtRatCalEnum.BY_REMIND_MONEY.getValue() == projExtRate.getCalcWay()) {
                //2剩余本金*费率值
                BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
                penalty = penalty.add(upaid.multiply(projExtRate.getRateValue()));
            } else if (PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue() == projExtRate.getCalcWay()) {
                //3.1*费率值'
                penalty = penalty.add(projExtRate.getRateValue());
            } else if (PepayPlanProjExtRatCalEnum.REMIND_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                //4 剩余的平台服务费合计
                penalty = penalty.add(projExtRate.getRateValue());
            } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_COM_FEE.getValue() == projExtRate.getCalcWay()) {
                //5 费率值*月收分公司服务费
                BigDecimal serviceFee = repaymentProjPlanListDetailMapper.calcProjectPlanAmount(
                        projExtRate.getProjectId(), planId, RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue().toString(), null);
                penalty = penalty.add(projExtRate.getRateValue().multiply(serviceFee));
            } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                //6 费率值*月收平台服务费
                BigDecimal platformFee = repaymentProjPlanListDetailMapper.calcProjectPlanAmount(
                        projExtRate.getProjectId(), planId, RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue().toString(), null);
                penalty = penalty.add(projExtRate.getRateValue().multiply(platformFee));
            } else if (PepayPlanProjExtRatCalEnum.BY_REM_MONEY_AND_FEE.getValue() == projExtRate.getCalcWay()) {
                //(剩余本金*费率值) - 分公司服务费违约金 - 平台服务费违约金

                BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
                BigDecimal servicePenalty = projExtRateMapper.calcProjextRate(
                        projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid());

                BigDecimal platformPenalty = projExtRateMapper.calcProjextRate(
                        projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid());
                penalty = (upaid.multiply(projExtRate.getRateValue())).subtract(servicePenalty).subtract(platformPenalty);

            } else {
                logger.error("错误： projExtRate.CalcWay[{}]尚未有对应算法", projExtRate.getCalcWay());
                throw new ServiceRuntimeException("错误： projExtRate.CalcWay[" + projExtRate.getCalcWay() + "]尚未有对应算法");
            }

            fee.setAmount(penalty);
            fee.setFeeId(projExtRate.getFeeId());
            fee.setFeeName(projExtRate.getFeeName());
            fee.setPlanItemName(projExtRate.getRateName());
            fee.setPlanItemType(projExtRate.getRateType().toString());
            fee.setProjectId(projExtRate.getProjectId());
            fees.add(fee);
        }
        return fees;
    }

    /**
     * 计算当前期未还金额
     *
     * @param repaymentBizPlanList
     * @param infoVO
     * @author 王继光
     * 2018年7月7日 下午4:33:49
     */
    private void calcCurPeriod(RepaymentBizPlanList repaymentBizPlanList, SettleInfoVO infoVO, Date factRepayDate, String planId) {

        if (StringUtil.isEmpty(planId)) {
            List<String> planLists = curPeriod(repaymentBizPlanList);
            List<RepaymentBizPlanList> list = repaymentBizPlanListMapper.selectBatchIds(planLists);

            for (RepaymentBizPlanList repaymentBizPlanList2 : list) {
                BigDecimal item20 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList2.getPlanListId(), "20", null);
                BigDecimal item30 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList2.getPlanListId(), "30", null);
                BigDecimal item50 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList2.getPlanListId(), "50", null);

                infoVO.setItem20(infoVO.getItem20().add(item20));
                infoVO.setItem30(infoVO.getItem30().add(item30));
                infoVO.setItem50(infoVO.getItem50().add(item50));

                repaymentBizPlanList2.setFactRepayDate(factRepayDate);
                repaymentBizPlanList = repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanList2, 1);
                BigDecimal item60offline = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList2.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
                BigDecimal item60online = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList2.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
                //TODO 要调用滞纳金计算
                infoVO.setOfflineOverDue(infoVO.getOfflineOverDue().add(item60offline));
                infoVO.setOnlineOverDue(infoVO.getOnlineOverDue().add(item60online));
            }
        } else {
            BigDecimal item20 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "20", null);
            BigDecimal item30 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "30", null);
            BigDecimal item50 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "50", null);

            infoVO.setItem20(item20);
            infoVO.setItem30(item30);
            infoVO.setItem50(item50);

            repaymentBizPlanList.setFactRepayDate(factRepayDate);
            repaymentBizPlanList = repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanList, 1);
            BigDecimal item60offline = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
            BigDecimal item60online = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "60", RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
            //TODO 要调用滞纳金计算
            infoVO.setOfflineOverDue(infoVO.getOfflineOverDue().add(item60offline));
            infoVO.setOnlineOverDue(infoVO.getOnlineOverDue().add(item60online));
        }


    }

    /**
     * 查找往期还款计划
     *
     * @param cur
     * @param planId
     * @return
     * @author 王继光
     * 2018年7月5日 下午6:23:48
     */
    private List<RepaymentBizPlanList> selectLastPlanLists(RepaymentBizPlanList cur, String planId) {
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
     *
     * @param cur
     * @param planId
     * @return
     * @author 王继光
     * 2018年7月5日 下午6:25:18
     */
    private List<RepaymentBizPlanList> selectNextPlanLists(RepaymentBizPlanList cur, String planId) {
        EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
        planListEW.eq("orig_business_id", cur.getOrigBusinessId());
        planListEW.gt("due_date", cur.getDueDate());
        if (!StringUtil.isEmpty(planId)) {
            planListEW.eq("plan_id", planId);
        }
        planListEW.orderBy("due_date");
        return repaymentBizPlanListMapper.selectList(planListEW);
    }


    /**
     * 全部业务结清的场景下,查找当前期的planListId
     *
     * @param now
     * @return
     * @author 王继光
     * 2018年7月25日 下午5:19:58
     */
    @Override
    public List<String> curPeriod(RepaymentBizPlanList now) {
        List<RepaymentBizPlan> plan = repaymentBizPlanMapper.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", now.getBusinessId()));
        List<String> res = new ArrayList<>();
        for (RepaymentBizPlan repaymentBizPlan : plan) {
            List<RepaymentBizPlanList> selectList = repaymentBizPlanListMapper.selectList(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", now.getBusinessId())
                            .eq("plan_id", repaymentBizPlan.getPlanId())
                            .gt("due_date", new Date())
                            .eq("current_status", RepayCurrentStatusEnums.还款中.toString())
                            .orderBy("due_date"));
            if (CollectionUtils.isEmpty(selectList)) {
                continue;
            }
            res.add(selectList.get(0).getPlanListId());
        }
        return res;
    }


	/* (non-Javadoc)
     * @see com.hongte.alms.finance.service.FinanceSettleService#getCurrentPeriod(com.hongte.alms.finance.req.FinanceSettleReq)
	 */

    /**
     * 取还款计划的当前期
     *
     * @param req
     * @return
     */
    @Override
    public List<RepaymentBizPlanSettleDto> getCurrentPeriod(FinanceSettleReq req) {
        EntityWrapper<RepaymentBizPlan> ew = new EntityWrapper<RepaymentBizPlan>();
        ew.eq("business_id", req.getBusinessId());
        //如果传了还款计划Id，则使用还款计划Id来查业务的还款计划
        if (!StringUtil.isEmpty(req.getPlanId())) {
            ew.eq("plan_id", req.getPlanId());
        }

        List<RepaymentBizPlan> plan = repaymentBizPlanMapper.selectList(ew);
        List<RepaymentBizPlanSettleDto> res = new ArrayList<>();
        //结清日期
        Date settleDate = new Date();
        //根据查出的还款计划列表找业务还款计划的当前期
        for (RepaymentBizPlan repaymentBizPlan : plan) {
            //找出这个还款计划的期数列表
            List<RepaymentBizPlanList> BizPlanLists = repaymentBizPlanListMapper.selectList(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", req.getBusinessId())
                            .eq("plan_id", repaymentBizPlan.getPlanId())
                            .orderBy("due_date", false));

            //找应还日期离当前日期最近且为还款中的期数作为当前期
            List<RepaymentBizPlanList> selectList = repaymentBizPlanListMapper.selectList(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", req.getBusinessId())
                            .eq("plan_id", repaymentBizPlan.getPlanId())
                            .gt("due_date", settleDate)
                            .eq("current_status", RepayCurrentStatusEnums.还款中.toString())
                            .orderBy("due_date"));
            //判断当前期列表是否为空
            if (CollectionUtils.isEmpty(selectList)) {
                //找不到还款中的当前期则判断结清日期是否大过还款计划最后一次还款的期限

                RepaymentBizPlanList lastBizPlanList = BizPlanLists.get(0);
                //如果最后一期的应还时间小于当前的结清时间
                if (DateUtil.getDiff(lastBizPlanList.getDueDate(), settleDate) <= 0) {
                    //且为未还款
                    if (!lastBizPlanList.getCurrentStatus().equals(RepayCurrentStatusEnums.已还款.toString())) {
                        selectList = new LinkedList<RepaymentBizPlanList>();
                        selectList.add(lastBizPlanList);
                    }
                }
            }
            //再次判断当前期列表是否为空
            if (CollectionUtils.isEmpty(selectList)) {
                if (!StringUtil.isEmpty(req.getPlanId())) {
                    logger.error("找此业务还款计划的当前期 RepaymentBizPlanList  businessId:" + req.getBusinessId() + "     planId:" + repaymentBizPlan.getPlanId());
                    throw new SettleRepaymentExcepiton("找此业务还款计划的当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                } else {
                    continue;
                }
            }

            //业务还款计划当前期列表
            List<RepaymentBizPlanList> cuRepaymentBizPlanLists = new ArrayList<>();
            cuRepaymentBizPlanLists.add(selectList.get(0));

            //业务还款计划Dto
            RepaymentBizPlanSettleDto planDto = new RepaymentBizPlanSettleDto();
            planDto.setRepaymentBizPlan(repaymentBizPlan);


            for (RepaymentBizPlanList curRepaymentBizPlanList : cuRepaymentBizPlanLists) {
                RepaymentBizPlanListDto planListDto = new RepaymentBizPlanListDto();
                planListDto.setRepaymentBizPlanList(curRepaymentBizPlanList);
                List<RepaymentBizPlanListDetail> planListDetails = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", curRepaymentBizPlanList.getPlanListId()));

                planListDto.setBizPlanListDetails(planListDetails);

                //业务还款计划，当前期设置业务还款计划列表（应该有一个字段专门存储业务还款计划的当前期）
                planDto.setCurrBizPlanListDto(planListDto);


                List<RepaymentBizPlanListDto> planListDtos = new LinkedList<>();

                List<RepaymentBizPlanListDto> beforePlanListDtos = new LinkedList<>();

                List<RepaymentBizPlanListDto> afterPlanListDtos = new LinkedList<>();


                for (RepaymentBizPlanList bizPlanList : BizPlanLists) {


                    RepaymentBizPlanListDto dto = new RepaymentBizPlanListDto();
                    dto.setRepaymentBizPlanList(curRepaymentBizPlanList);
                    List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", curRepaymentBizPlanList.getPlanListId()));

                    dto.setBizPlanListDetails(details);

                    //如果是当前期，则不存储
                    if (bizPlanList.getPeriod() == curRepaymentBizPlanList.getPeriod()) {
                        continue;
                    }
                    if (bizPlanList.getPeriod() < curRepaymentBizPlanList.getPeriod()) {
                        beforePlanListDtos.add(dto);
                    } else if (bizPlanList.getPeriod() > curRepaymentBizPlanList.getPeriod()) {
                        afterPlanListDtos.add(dto);
                    }
                }

                //当前期之前的还款计划列表
                planDto.setBeforeBizPlanListDtos(beforePlanListDtos);

                //当前期之后的还款计划列表
                planDto.setAfterBizPlanListDtos(afterPlanListDtos);
            }


            //根据还款计划id，查找出标的还款计划列表
            List<RepaymentProjPlan> projPlans = repaymentProjPlanMapper.selectList(new EntityWrapper<RepaymentProjPlan>().eq("plan_id", repaymentBizPlan.getPlanId()));
            for (RepaymentProjPlan repaymentProjPlan : projPlans) {
                RepaymentProjPlanSettleDto projPlanDto = new RepaymentProjPlanSettleDto();
                projPlanDto.setRepaymentProjPlan(repaymentProjPlan);
                TuandaiProjectInfo projectInfo = tuandaiProjectInfoMapper.selectById(repaymentProjPlan.getProjectId());
                projPlanDto.setTuandaiProjectInfo(projectInfo);

                //取标的还款计划当前期
                List<RepaymentProjPlanList> curprojPlanLists = repaymentProjPlanListMapper.selectList(
                        new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId()).eq("plan_list_id", cuRepaymentBizPlanLists.get(0).getPlanListId()));

                //如果找不到这个标的还款计划当前期
                if (CollectionUtils.isEmpty(selectList)) {
                    logger.error("找不到这个标的还款计划当前期 RepaymentProjPlanList  proj_plan_id:" + repaymentProjPlan.getProjPlanId() + "     plan_list_id:" + cuRepaymentBizPlanLists.get(0).getPlanListId());
                    throw new SettleRepaymentExcepiton("找不到这个标的还款计划当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                } else if (selectList.size() > 1) {
                    logger.error("这个标有两个以上当前期 RepaymentProjPlanList  proj_plan_id:" + repaymentProjPlan.getProjPlanId() + "     plan_list_id:" + cuRepaymentBizPlanLists.get(0).getPlanListId());
                    throw new SettleRepaymentExcepiton("这个标有两个以上当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                }

                RepaymentProjPlanList curRepayProjPlanList = curprojPlanLists.get(0);
                RepaymentProjPlanListDto curProjPlanListDto = creatProjPlanListDto(curRepayProjPlanList);
                projPlanDto.setCurrProjPlanListDto(curProjPlanListDto);

                //取标的还款计划所有期
                List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListMapper.selectList(
                        new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
                                .orderBy("due_date"));


                List<RepaymentProjPlanListDto> beforeProjPlanListDtos = new LinkedList<>();
                List<RepaymentProjPlanListDto> afterProjPlanListDtos = new LinkedList<>();

                for (RepaymentProjPlanList repaymentProjPlanList : projPlanLists) {
                    RepaymentProjPlanListDto projPlanListDto = creatProjPlanListDto(repaymentProjPlanList);
                    projPlanDto.setProjPlanListDtos(projPlanListDto);
                    if (repaymentProjPlanList.getPeriod() > curRepayProjPlanList.getPeriod()) {
                        afterProjPlanListDtos.add(projPlanListDto);
                    } else if (repaymentProjPlanList.getPeriod() < curRepayProjPlanList.getPeriod()) {
                        beforeProjPlanListDtos.add(projPlanListDto);
                    }

                }

                projPlanDto.setBeforeProjPlanListDtos(beforeProjPlanListDtos);
                projPlanDto.setAfterProjPlanListDtos(afterProjPlanListDtos);


                //planDto.setProjPlanDtos(projPlanDto);
                planDto.setProjPlanStteleDtos(projPlanDto);
            }


            res.add(planDto);
        }

        //将当前期之前的和当前期之后的费用项累加到当前期
        for (RepaymentBizPlanSettleDto bizPlanDto : res) {

            List<RepaymentProjPlanSettleDto> projPlanDtos = bizPlanDto.getProjPlanStteleDtos();

            for (RepaymentProjPlanSettleDto repaymentProjPlanDto : projPlanDtos) {

                //当前期之前期数费用的统计
                Map<String, PlanListDetailShowPayDto> beforeFeels = calcShowPayFeels(repaymentProjPlanDto.getBeforeProjPlanListDtos(), null);
                repaymentProjPlanDto.setBeforeFeels(beforeFeels);

                //当前期之后期数费用的统计(只累加本金)
                Map<String, PlanListDetailShowPayDto> afterFeels = calcShowPayFeels(repaymentProjPlanDto.getAfterProjPlanListDtos(), RepayPlanFeeTypeEnum.PRINCIPAL.getUuid());
                repaymentProjPlanDto.setAfterFeels(afterFeels);


//                repaymentProjPlanDto.getCurrProjPlanListDto()

            }
        }


        return res;
    }


    private Map<String, PlanListDetailShowPayDto> calcShowPayFeels(List<RepaymentProjPlanListDto> projPlanLists, String feelId) {
        //当前期之前期数费用的统计
        Map<String, PlanListDetailShowPayDto> beforeFeels = new HashMap<>();
        for (RepaymentProjPlanListDto bforePlanDto : projPlanLists) {
            List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = bforePlanDto.getRepaymentProjPlanListDetailDtos();
            for (RepaymentProjPlanListDetailDto projPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                RepaymentProjPlanListDetail detail1 = projPlanListDetailDto.getRepaymentProjPlanListDetail();
                //如果指定了需累加的费用项，则其他费用项都跳过
                if (feelId != null && !feelId.equals(detail1.getFeeId())) {
                    continue;
                }

                List<RepaymentProjFactRepay> factRepayList = projPlanListDetailDto.getRepaymentProjFactRepays();
                if (!CollectionUtils.isEmpty(factRepayList)) {
                    BigDecimal payedMoney = new BigDecimal("0");
                    for (RepaymentProjFactRepay repaymentProjFactRepay : factRepayList) {
                        payedMoney = payedMoney.add(repaymentProjFactRepay.getFactAmount());
                    }

                    BigDecimal showPayMoney = detail1.getProjPlanAmount().subtract(payedMoney);
                    if (showPayMoney.compareTo(new BigDecimal("0")) > 0) {
                        PlanListDetailShowPayDto planListDetailShowPayDto = beforeFeels.get(detail1.getFeeId());
                        if (planListDetailShowPayDto == null) {
                            planListDetailShowPayDto = new PlanListDetailShowPayDto();
                            planListDetailShowPayDto.setFeelId(detail1.getFeeId());
                            planListDetailShowPayDto.setShareProfitIndex(detail1.getShareProfitIndex());
                            planListDetailShowPayDto.setShowPayMoney(new BigDecimal("0"));
                            beforeFeels.put(detail1.getFeeId(), planListDetailShowPayDto);
                        }
                        planListDetailShowPayDto.setShowPayMoney(planListDetailShowPayDto.getShowPayMoney().add(showPayMoney));
                    }
                }

            }
        }


        return beforeFeels;
    }

    //showPayFeels =


    private RepaymentProjPlanListDto creatProjPlanListDto(RepaymentProjPlanList repaymentProjPlanList) {
        RepaymentProjPlanListDto projPlanListDto = new RepaymentProjPlanListDto();
        projPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);

        List<RepaymentProjPlanListDetail> projPlanListDetails = repaymentProjPlanListDetailMapper.selectList(
                new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId()));

        projPlanListDto.setProjPlanListDetails(projPlanListDetails);


        List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = new LinkedList<>();
        for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDetails) {
            RepaymentProjPlanListDetailDto projPlanListDetailDto = new RepaymentProjPlanListDetailDto();
            repaymentProjPlanListDetailDtos.add(projPlanListDetailDto);
            projPlanListDetailDto.setRepaymentProjPlanListDetail(projPlanListDetail);

            List<RepaymentProjFactRepay> factRepayList = repaymentProjFactRepayMapper.selectList(
                    new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_detail_id", projPlanListDetail.getProjPlanDetailId())
            );
            projPlanListDetailDto.setRepaymentProjFactRepays(factRepayList);
        }
        projPlanListDto.setRepaymentProjPlanListDetailDtos(repaymentProjPlanListDetailDtos);

        return projPlanListDto;
    }


    @Override
    public void financeSettleRecall(String businessId, String afterId) {

        List<RepaymentConfirmLog> logs = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId).eq("after_id", afterId).orderBy("create_time", false));
        RepaymentConfirmLog log = logs.get(0);
        if (log.getSurplusUseRefId() != null) {
            AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogMapper
                    .selectById(log.getSurplusUseRefId());
            if (accountantOverRepayLog != null) {
                accountantOverRepayLog.deleteById();
            }
        }

        /*找实还明细记录*/
        List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper
                .selectList(new EntityWrapper<RepaymentProjFactRepay>().eq("confirm_log_id", log.getConfirmLogId()));

        for (RepaymentProjFactRepay factRepay : factRepays) {
            RepaymentResource resource = new RepaymentResource();
            resource.setResourceId(factRepay.getRepaySourceId());
            /*找还款来源记录*/
            resource = repaymentResourceMapper.selectOne(resource);
            /*撤销银行流水与财务登记关联*/
            moneyPoolService.revokeConfirmRepaidUpdateMoneyPool(resource);

			/*删除实还明细记录*/
//			factRepay.deleteById();
        }
        for (RepaymentProjFactRepay factRepay : factRepays) {
            RepaymentResource resource = new RepaymentResource();
            resource.setResourceId(factRepay.getRepaySourceId());
            /*找还款来源记录*/
            resource = repaymentResourceMapper.selectOne(resource);
            if (resource != null) {
                /*删除还款来源记录*/
                resource.deleteById();
            }
        }
        repaymentProjFactRepayMapper.delete(new EntityWrapper<RepaymentProjFactRepay>().eq("confirm_log_id", log.getConfirmLogId()));

		/*根据 confirm_log_id 找还款计划6张表相关的备份记录*/
        List<RepaymentBizPlanBak> selectList = repaymentBizPlanBakMapper.selectList(new EntityWrapper<RepaymentBizPlanBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentBizPlanListBak> selectList2 = repaymentBizPlanListBakMapper.selectList(new EntityWrapper<RepaymentBizPlanListBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentBizPlanListDetailBak> selectList3 = repaymentBizPlanListDetailBakMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetailBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanBak> selectList4 = repaymentProjPlanBakMapper.selectList(new EntityWrapper<RepaymentProjPlanBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanListBak> selectList5 = repaymentProjPlanListBakMapper.selectList(new EntityWrapper<RepaymentProjPlanListBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanListDetailBak> selectList6 = repaymentProjPlanListDetailBakMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetailBak>().eq("confirm_log_id", log.getConfirmLogId()));

		/*根据bak的主键找现在的记录,先删除,再新增*/
        for (RepaymentProjPlanListDetailBak repaymentProjPlanListDetailBak : selectList6) {
            RepaymentProjPlanListDetail detail = new RepaymentProjPlanListDetail(repaymentProjPlanListDetailBak);
            repaymentProjPlanListDetailMapper.deleteById(detail.getProjPlanDetailId());
            detail.insert();
            repaymentProjPlanListDetailBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_detail_id", repaymentProjPlanListDetailBak.getProjPlanDetailId())
                    .eq("confirm_log_id", repaymentProjPlanListDetailBak.getConfirmLogId()));
        }

        for (RepaymentProjPlanListBak repaymentProjPlanListBak : selectList5) {
            RepaymentProjPlanList list = new RepaymentProjPlanList(repaymentProjPlanListBak);
            repaymentProjPlanListMapper.deleteById(list.getProjPlanListId());
            list.insert();
            repaymentProjPlanListBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_list_id", repaymentProjPlanListBak.getProjPlanListId())
                    .eq("confirm_log_id", repaymentProjPlanListBak.getConfirmLogId()));
        }

        for (RepaymentProjPlanBak repaymentProjPlanBak : selectList4) {
            RepaymentProjPlan plan = new RepaymentProjPlan(repaymentProjPlanBak);
            repaymentProjPlanMapper.deleteById(plan.getProjPlanId());
            plan.insert();
            repaymentProjPlanBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_id", repaymentProjPlanBak.getProjPlanId())
                    .eq("confirm_log_id", repaymentProjPlanBak.getConfirmLogId()));
        }
        BigDecimal bplFactAmount = BigDecimal.ZERO;
        for (RepaymentBizPlanListDetailBak repaymentBizPlanListDetailBak : selectList3) {
            RepaymentBizPlanListDetail detail = new RepaymentBizPlanListDetail(repaymentBizPlanListDetailBak);
            repaymentBizPlanListDetailMapper.deleteById(detail.getPlanDetailId());
            detail.insert();
            bplFactAmount = bplFactAmount.add(detail.getFactAmount() == null ? BigDecimal.ZERO : detail.getFactAmount());
            repaymentBizPlanListDetailBak.delete(new EntityWrapper<>()
                    .eq("plan_detail_id", repaymentBizPlanListDetailBak.getPlanDetailId())
                    .eq("confirm_log_id", repaymentBizPlanListDetailBak.getConfirmLogId()));
        }

        for (RepaymentBizPlanListBak repaymentBizPlanListBak : selectList2) {
            RepaymentBizPlanList list = new RepaymentBizPlanList(repaymentBizPlanListBak);
            repaymentBizPlanListMapper.deleteById(list.getPlanListId());
            list.insert();

            RepaymentBizPlanListSynch synch = new RepaymentBizPlanListSynch();
            synch.setPlanListId(list.getPlanListId());
            synch = repaymentBizPlanListSynchMapper.selectOne(synch);
            String afterIdSource = synch.getAfterId();
            synch.setCurrentStatus(list.getCurrentStatus());
            synch.setCurrentSubStatus(list.getCurrentSubStatus());
            synch.setRepayStatus(list.getRepayStatus());
            synch.setRepayFlag(list.getRepayFlag());
            synch.setFinanceConfirmUser(list.getFinanceConfirmUser());
            synch.setFinanceConfirmUserName(list.getFinanceConfirmUserName());
            if (afterIdSource.equals(afterId)) {
                synch.setFactAmountExt(bplFactAmount);
            }
            repaymentBizPlanListSynchMapper.updateAllColumnById(synch);

            repaymentBizPlanListBak.delete(new EntityWrapper<>()
                    .eq("plan_list_id", repaymentBizPlanListBak.getPlanListId())
                    .eq("confirm_log_id", repaymentBizPlanListBak.getConfirmLogId()));
        }

        for (RepaymentBizPlanBak repaymentBizPlanBak : selectList) {
            RepaymentBizPlan plan = new RepaymentBizPlan(repaymentBizPlanBak);
            repaymentBizPlanMapper.deleteById(plan.getPlanId());
            plan.insert();
            repaymentBizPlanBak.delete(new EntityWrapper<>()
                    .eq("plan_id", repaymentBizPlanBak.getPlanId())
                    .eq("confirm_log_id", repaymentBizPlanBak.getConfirmLogId()));
        }


    }


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void bak(FinanceSettleBaseDto base, List<RepaymentBizPlanSettleDto> dtos) {
		if (base.getPreview()) {
			return;
		}
		String confirmLogId = base.getUuid();
		for (RepaymentBizPlanSettleDto repaymentBizPlanSettleDto : dtos) {
			/*备份bizPlan*/
			RepaymentBizPlanBak repaymentBizPlanBak = new RepaymentBizPlanBak();
			BeanUtils.copyProperties(repaymentBizPlanSettleDto.getRepaymentBizPlan(), repaymentBizPlanBak);
			repaymentBizPlanBak.setConfirmLogId(confirmLogId);
			repaymentBizPlanBak.insert();
			
			List<RepaymentBizPlanListDto> afterBizPlanListDtos = repaymentBizPlanSettleDto.getAfterBizPlanListDtos();
			List<RepaymentBizPlanListDto> beforeBizPlanListDtos = repaymentBizPlanSettleDto.getBeforeBizPlanListDtos();
			RepaymentBizPlanListDto currBizPlanListDto = repaymentBizPlanSettleDto.getCurrBizPlanListDto();
			List<RepaymentProjPlanSettleDto> projPlanStteleDtos = repaymentBizPlanSettleDto.getProjPlanStteleDtos();

			/*备份当前期以后的*/
			for (RepaymentBizPlanListDto afterBizPlanListDto : afterBizPlanListDtos) {
				/*备份当前期以后的bizPlanList*/
				RepaymentBizPlanListBak repaymentBizPlanListBak = new RepaymentBizPlanListBak();
				BeanUtils.copyProperties(afterBizPlanListDto.getRepaymentBizPlanList(), repaymentBizPlanListBak);
				repaymentBizPlanListBak.setConfirmLogId(confirmLogId);
				repaymentBizPlanListBak.insert();

				/*备份当前期以后的bizPlanListDetail*/
				for (RepaymentBizPlanListDetail bizPlanListDetail : afterBizPlanListDto.getBizPlanListDetails()) {
					RepaymentBizPlanListDetailBak bizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
					BeanUtils.copyProperties(bizPlanListDetail, bizPlanListDetailBak);
					bizPlanListDetailBak.setConfirmLogId(confirmLogId);
					bizPlanListDetailBak.insert();
				}
			}
			/*备份当前期以前的*/
			for (RepaymentBizPlanListDto beforeBizPlanListDto : beforeBizPlanListDtos) {
				/*备份当前期以前的bizPlanList*/
				RepaymentBizPlanListBak repaymentBizPlanListBak = new RepaymentBizPlanListBak();
				BeanUtils.copyProperties(beforeBizPlanListDto.getRepaymentBizPlanList(), repaymentBizPlanListBak);
				repaymentBizPlanListBak.setConfirmLogId(confirmLogId);
				repaymentBizPlanListBak.insert();

				/*备份当前期以前的bizPlanListDetail*/
				for (RepaymentBizPlanListDetail bizPlanListDetail : beforeBizPlanListDto.getBizPlanListDetails()) {
					RepaymentBizPlanListDetailBak bizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
					BeanUtils.copyProperties(bizPlanListDetail, bizPlanListDetailBak);
					bizPlanListDetailBak.setConfirmLogId(confirmLogId);
					bizPlanListDetailBak.insert();
				}
			}

			/*备份当前期当前期的BizPlanLis*/
			RepaymentBizPlanListBak currBizPlanListBak = new RepaymentBizPlanListBak();
			BeanUtils.copyProperties(currBizPlanListDto.getRepaymentBizPlanList(), currBizPlanListBak);
			currBizPlanListBak.setConfirmLogId(confirmLogId);
			currBizPlanListBak.insert();
			/*备份当前期当前期的BizPlanListDetail*/
			for (RepaymentBizPlanListDetail cuRepaymentBizPlanListDetail : currBizPlanListDto.getBizPlanListDetails()) {
				RepaymentBizPlanListDetailBak bizPlanListDetailBak = new RepaymentBizPlanListDetailBak();
				BeanUtils.copyProperties(cuRepaymentBizPlanListDetail, bizPlanListDetailBak);
				bizPlanListDetailBak.setConfirmLogId(confirmLogId);
				bizPlanListDetailBak.insert();
			}

			/*备份标的*/
			for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : projPlanStteleDtos) {
				/*备份projPlan*/
				RepaymentProjPlanBak projPlanBak = new RepaymentProjPlanBak();
				BeanUtils.copyProperties(repaymentProjPlanSettleDto.getRepaymentProjPlan(), projPlanBak);
				projPlanBak.setConfirmLogId(confirmLogId);
				projPlanBak.insert();

				List<RepaymentProjPlanListDto> afterProjPlanListDtos = repaymentProjPlanSettleDto
						.getAfterProjPlanListDtos();
				List<RepaymentProjPlanListDto> beforeProjPlanListDtos = repaymentProjPlanSettleDto
						.getBeforeProjPlanListDtos();
				RepaymentProjPlanListDto currProjPlanListDto = repaymentProjPlanSettleDto.getCurrProjPlanListDto();

				/*备份当前期之后的*/
				for (RepaymentProjPlanListDto afterProjPlanListDto : afterProjPlanListDtos) {
					
					RepaymentProjPlanList repaymentProjPlanList = afterProjPlanListDto.getRepaymentProjPlanList();
					List<RepaymentProjPlanListDetail> projPlanListDetails = afterProjPlanListDto
							.getProjPlanListDetails();

					/*备份当前期之后的projPlanList*/
					RepaymentProjPlanListBak projPlanListBak = new RepaymentProjPlanListBak();
					BeanUtils.copyProperties(repaymentProjPlanList, projPlanListBak);
					projPlanListBak.setConfirmLogId(confirmLogId);
					projPlanListBak.insert();

					/*备份当前期之后的projPlanListDetail*/
					for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {
						RepaymentProjPlanListDetailBak projPlanListDetailBak = new RepaymentProjPlanListDetailBak();
						BeanUtils.copyProperties(repaymentProjPlanListDetail, projPlanListDetailBak);
						projPlanListDetailBak.setConfirmLogId(confirmLogId);
						projPlanListDetailBak.insert();
					}

				}

				/*备份当前期之前的*/
				for (RepaymentProjPlanListDto beforeProjPlanListDto : beforeProjPlanListDtos) {
					RepaymentProjPlanList repaymentProjPlanList = beforeProjPlanListDto.getRepaymentProjPlanList();
					List<RepaymentProjPlanListDetail> projPlanListDetails = beforeProjPlanListDto
							.getProjPlanListDetails();

					/*备份当前期之前的projPlanList*/
					RepaymentProjPlanListBak projPlanListBak = new RepaymentProjPlanListBak();
					BeanUtils.copyProperties(repaymentProjPlanList, projPlanListBak);
					projPlanListBak.setConfirmLogId(confirmLogId);
					projPlanListBak.insert();

					/*备份当前期之前的projPlanListDetail*/
					for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {
						RepaymentProjPlanListDetailBak projPlanListDetailBak = new RepaymentProjPlanListDetailBak();
						BeanUtils.copyProperties(repaymentProjPlanListDetail, projPlanListDetailBak);
						projPlanListDetailBak.setConfirmLogId(confirmLogId);
						projPlanListDetailBak.insert();
					}

				}

				RepaymentProjPlanList repaymentProjPlanList = currProjPlanListDto.getRepaymentProjPlanList();
				List<RepaymentProjPlanListDetail> projPlanListDetails = currProjPlanListDto.getProjPlanListDetails();

				/*备份当前期的ProjPlanList*/
				RepaymentProjPlanListBak projPlanListBak = new RepaymentProjPlanListBak();
				BeanUtils.copyProperties(repaymentProjPlanList, projPlanListBak);
				projPlanListBak.setConfirmLogId(confirmLogId);
				projPlanListBak.insert();

				/*备份当前期的projPlanListDetail*/
				for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {
					RepaymentProjPlanListDetailBak projPlanListDetailBak = new RepaymentProjPlanListDetailBak();
					BeanUtils.copyProperties(repaymentProjPlanListDetail, projPlanListDetailBak);
					projPlanListDetailBak.setConfirmLogId(confirmLogId);
					projPlanListDetailBak.insert();
				}
			}
		}

	}


}
