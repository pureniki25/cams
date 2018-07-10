package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanListDto;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.finance.req.FinanceBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        //通过业务ID查询还款计划 查询出所有需要还的金额

        FinanceBaseDto financeBaseDto = new FinanceBaseDto();
        BigDecimal shouldReturnAmount = BigDecimal.ZERO; //总应还金额
        BigDecimal shouldProjReturnAmount = BigDecimal.ZERO; //标总应还金额


        BigDecimal factReturnAmount = BigDecimal.ZERO; //总实还金额

        BigDecimal shouldFactReturnAmount = BigDecimal.ZERO; //总实应还金额


        BigDecimal flowAmount = BigDecimal.ZERO; //总流水金额
        String businessId = financeSettleReq.getBusinessId();
        String planId = financeSettleReq.getPlanId();

        List<RepaymentBizPlanListDto> planDtoListList = new ArrayList<>();
        List<RepaymentProjPlanListDto> projDtoListList = new ArrayList<>();

        //查询本次业务下的所有还款计划 计算应还项
        List<RepaymentBizPlanList> repaymentBizPlanListList = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planId));
        if (!CollectionUtils.isEmpty(repaymentBizPlanListList)) {
            for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanListList) {

                BigDecimal totalBorrowAmount = repaymentBizPlanList.getTotalBorrowAmount();
                BigDecimal overdueAmount = repaymentBizPlanList.getOverdueAmount();
                BigDecimal derateAmount = repaymentBizPlanList.getDerateAmount();//减免

                shouldReturnAmount = shouldReturnAmount.add(totalBorrowAmount).add(overdueAmount).subtract(derateAmount);  //计算所有的应还

                //组装还款计划对象
                RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
                repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);
                String planListId = repaymentBizPlanList.getPlanListId();
                //查询还款计划详情
                List<RepaymentBizPlanListDetail> repaymentBizPlanListDetailList = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planListId));
                repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetailList);
                planDtoListList.add(repaymentBizPlanListDto);
            }

        }

        //查询本次业务下的所有还款计划 计算应还项
        List<RepaymentProjPlanList> repaymentProjPlanListList = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("business_id", businessId).eq("plan_id", planId));
        if (!CollectionUtils.isEmpty(repaymentProjPlanListList)) {
            for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanListList) {
                BigDecimal totalBorrowAmount = repaymentProjPlanList.getTotalBorrowAmount();
                BigDecimal overdueAmount = repaymentProjPlanList.getOverdueAmount();
                BigDecimal derateAmount = repaymentProjPlanList.getDerateAmount();//减免金额
                shouldProjReturnAmount = shouldProjReturnAmount.add(totalBorrowAmount).add(overdueAmount).subtract(derateAmount); //所有应还

                //标的还款计划
                RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
                repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
                String projPlanListId = repaymentProjPlanList.getProjPlanListId();

                List<RepaymentProjPlanListDetail> repaymentProjPlanListDetailList = repaymentProjPlanListDetailMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId));
                repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetailList);

                projDtoListList.add(repaymentProjPlanListDto);
            }
        }


        if (shouldReturnAmount.compareTo(shouldProjReturnAmount) != 0) {
            throw new ServiceRuntimeException("标的应还金额与还款计划的金额对应不上");
        }

        //查询本次业务的实还记录
        List<RepaymentConfirmLog> repaymentConfirmLogList = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId));
        if (!CollectionUtils.isEmpty(repaymentConfirmLogList)) {
            for (RepaymentConfirmLog repaymentConfirmLog : repaymentConfirmLogList) {
                BigDecimal factAmount = repaymentConfirmLog.getFactAmount();
                factReturnAmount = factReturnAmount.add(factAmount);
            }

        }


        //查询本次业务匹配的流水
        List<String> mprIds = financeSettleReq.getMprIds();
        List<MoneyPoolRepayment> moneyPoolRepaymentList = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().in("id", mprIds));
        if (!CollectionUtils.isEmpty(moneyPoolRepaymentList)) {
            for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepaymentList) {
                BigDecimal accountMoney = moneyPoolRepayment.getAccountMoney();
                //累加匹配流水
                flowAmount = flowAmount.add(accountMoney);
            }
        }

        shouldFactReturnAmount = shouldProjReturnAmount.subtract(factReturnAmount);//总应还减去总实还
        if (shouldFactReturnAmount.compareTo(flowAmount) > 0) { //总实应还大于流水金额
            throw new ServiceRuntimeException("总实应还应该小于或等于流水金额才能进行结清");
        } else {
            //======================================开始进行结清操作

            RepaymentConfirmLog repaymentConfirmLog = new RepaymentConfirmLog();


            if (CollectionUtils.isNotEmpty(projDtoListList)) {
                for (RepaymentProjPlanListDto repaymentProjPlanListDto : projDtoListList) {

                    RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListDto.getRepaymentProjPlanList();
                    String currentStatus = repaymentProjPlanList.getCurrentStatus();
                    if (RepayCurrentStatusEnums.还款中.equals(currentStatus) || RepayCurrentStatusEnums.逾期.equals(currentStatus)) {
                        List<RepaymentProjPlanListDetail> projPlanListDetails = repaymentProjPlanListDto.getProjPlanListDetails();
//                    RepaymentProjFactRepay repaymentProjFactRepay=new RepaymentProjFactRepay();
//                    repaymentProjFactRepay.setAfterId();
                        if (CollectionUtils.isNotEmpty(projPlanListDetails)) {

                            for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {

                            }
                        }

                    }


                }


            }


        }
    }
}
