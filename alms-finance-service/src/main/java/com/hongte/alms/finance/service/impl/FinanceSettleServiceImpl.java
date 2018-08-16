package com.hongte.alms.finance.service.impl;

import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr.Function;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.baseException.SettleRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.util.ProjPlanDtoUtil;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.exception.ExceptionCodeEnum;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.FinanceBaseDto;
import com.hongte.alms.finance.req.FinanceSettleBaseDto;
import com.hongte.alms.finance.req.FinanceSettleReq;
import com.hongte.alms.finance.service.FinanceSettleService;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scala.Unit;

import javax.enterprise.inject.New;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
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

    @Autowired
    private PlatformRepaymentFeignClient platformRepaymentFeignClient;
    
    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;
    
    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    private WithholdingRepaymentLogService withholdingRepaymentLogService ;
    
    @Autowired
	@Qualifier("RepaymentBizPlanListSynchService")
	RepaymentBizPlanListSynchService repaymentBizPlanListSynchService;
    
	/**
	 * 线上部分费用feeid集合
	 */
	 final List<String> ONLINE_FEES = Arrays.asList(RepayPlanFeeTypeEnum.PRINCIPAL.getUuid(),
			RepayPlanFeeTypeEnum.INTEREST.getUuid(), RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getUuid(),
			RepayPlanFeeTypeEnum.PLAT_CHARGE.getUuid(), RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
    public List<CurrPeriodProjDetailVO> financeSettle(FinanceSettleReq financeSettleReq) {
        FinanceSettleBaseDto financeSettleBaseDto = new FinanceSettleBaseDto();
        //结算流水ID
        String uuid = UUID.randomUUID().toString();
        financeSettleBaseDto.setUuid(uuid);
        financeSettleBaseDto.setPreview(financeSettleReq.getPreview());
        LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
        if (loginInfo != null) {
            financeSettleBaseDto.setUserId(loginInfo.getUserId());
            financeSettleBaseDto.setUserName(loginInfo.getUserName());
        }
        financeSettleBaseDto.setRemark(financeSettleReq.getRemark());
        financeSettleBaseDto.setBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setAfterId(financeSettleReq.getAfterId());
        financeSettleBaseDto.setOrgBusinessId(financeSettleReq.getBusinessId());
        financeSettleBaseDto.setPlanId(financeSettleReq.getPlanId());
        financeSettleBaseDto.setRepayFactAmount(BigDecimal.ZERO);
        
        SettleInfoVO settleInfoVO = settleInfoVO(financeSettleReq);
        financeSettleBaseDto.setSettleInfoVO(settleInfoVO);
        financeSettleBaseDto.setDerates(settleInfoVO.getDerates());
        
        List<RepaymentBizPlanSettleDto> currentPeriods = getCurrentPeriod(financeSettleReq, financeSettleBaseDto);
        financeSettleBaseDto.setCurrentPeriods(currentPeriods);
        for (RepaymentBizPlanSettleDto repaymentBizPlanSettleDto : currentPeriods) {
			repaymentBizPlanSettleDto.getBeforeBizPlanListDtos();
			for (RepaymentBizPlanListDto beforePlanList : repaymentBizPlanSettleDto.getBeforeBizPlanListDtos()) {
				if (beforePlanList.getRepaymentBizPlanList().getRepayStatus()!=null
						&&!beforePlanList.getRepaymentBizPlanList().getRepayStatus().equals(SectionRepayStatusEnum.ONLINE_REPAID.getKey())
						&& !beforePlanList.getRepaymentBizPlanList().getRepayStatus().equals(SectionRepayStatusEnum.ALL_REPAID.getKey())) {
					throw new ServiceRuntimeException("往期存在未还清线上部分的期数");
				}
			}
			
			Integer planStatus = repaymentBizPlanSettleDto.getRepaymentBizPlan().getPlanStatus();
			if (RepayPlanSettleStatusEnum.PAYED.getValue().equals(planStatus)) {
				throw new ServiceRuntimeException("此次存在已结清还款计划");
			}
			if (RepayPlanSettleStatusEnum.PAYED_BAD.getValue().equals(planStatus)) {
				throw new ServiceRuntimeException("此次存在已坏账结清还款计划");
			}
			if (RepayPlanSettleStatusEnum.PAYED_LOSS.getValue().equals(planStatus)) {
				throw new ServiceRuntimeException("此次存在已亏损结清还款计划");
			}
			if (RepayPlanSettleStatusEnum.PAYED_EARLY.getValue().equals(planStatus)) {
				throw new ServiceRuntimeException("此次存在已提前结清还款计划");
			}
		}
        
        /*创建还款日志*/
        createConfirmLog(financeSettleBaseDto);
        //处理还款来源,整合成RepaymentResource
        handleRepaymentResource(financeSettleBaseDto, financeSettleReq);
        //数据填充及bak及入库
        makeRepaymentPlanAllPlan(financeSettleBaseDto, financeSettleReq);
        /*计算结余金额*/
        calcSurplus(financeSettleBaseDto);

        return financeSettleBaseDto.getCurrPeriodProjDetailVOList();


    }


    /**
     * 计算结余金额
     *
     * @param dto
     * @author 王继光
     * 2018年7月17日 下午3:10:03
     */
    private void calcSurplus(FinanceSettleBaseDto dto) {
    	BigDecimal fact = dto.getRepayFactAmount();
    	BigDecimal plan = dto.getSettleInfoVO().getTotal();
    	if (fact.compareTo(plan) > 0) {
			dto.setSurplusAmount(fact.subtract(plan));
			dto.getCurrPeriodProjDetailVOList().get(0).setSurplus(dto.getSurplusAmount());
		}
    	
    }
    
    
    /**
     * 创建还款日志
     * @author 王继光
     * 2018年8月3日 下午3:15:04
     * @param financeSettleBaseDto
     */
    private void createConfirmLog(FinanceSettleBaseDto financeSettleBaseDto ) {
    	RepaymentConfirmLog repaymentConfirmLog = new RepaymentConfirmLog();
        repaymentConfirmLog.setConfirmLogId(financeSettleBaseDto.getUuid());
        repaymentConfirmLog.setRepayDate(new Date());
        repaymentConfirmLog.setBusinessId(financeSettleBaseDto.getBusinessId());
        repaymentConfirmLog.setOrgBusinessId(financeSettleBaseDto.getBusinessId());
        repaymentConfirmLog.setFactAmount(financeSettleBaseDto.getRepayFactAmount());
        repaymentConfirmLog.setProjPlanJson(JSON.toJSONString(financeSettleBaseDto.getCurrPeriodProjDetailVOList()));
        
        if (financeSettleBaseDto.getSurplusAmount()!=null && financeSettleBaseDto.getSurplusAmount().compareTo(BigDecimal.ZERO) > 0 ) {
        	
        	AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
            accountantOverRepayLog.setBusinessAfterId(financeSettleBaseDto.getAfterId());
            accountantOverRepayLog.setBusinessId(financeSettleBaseDto.getBusinessId());
            accountantOverRepayLog.setCreateTime(new Date());
            accountantOverRepayLog.setCreateUser(financeSettleBaseDto.getUserId());
            accountantOverRepayLog.setFreezeStatus(0);
            accountantOverRepayLog.setIsRefund(0);
            accountantOverRepayLog.setIsTemporary(0);
            accountantOverRepayLog.setMoneyType(1);
            accountantOverRepayLog.setOverRepayMoney(financeSettleBaseDto.getSurplusAmount());
            accountantOverRepayLog
                    .setRemark(String.format("收入于%s的%s期线下财务确认", financeSettleBaseDto.getBusinessId(), financeSettleBaseDto.getAfterId()));
            
            repaymentConfirmLog.setSurplusAmount(financeSettleBaseDto.getSurplusAmount());
            
            if (!financeSettleBaseDto.getPreview()) {
            	accountantOverRepayLog.insert();
            	repaymentConfirmLog.setSurplusRefId(accountantOverRepayLog.getId().toString());
    		}
		}
        
        
        repaymentConfirmLog.setAfterId(financeSettleBaseDto.getAfterId());
        repaymentConfirmLog.setPeriod(financeSettleBaseDto.getCurrentPeriods().get(0).getCurrBizPlanListDto().getRepaymentBizPlanList().getPeriod());
        repaymentConfirmLog.setCreateTime(new Date());
        repaymentConfirmLog.setRepaySource(10);
        repaymentConfirmLog.setType(2);//还款日志类型，1=还款日志，2=结清日志
        repaymentConfirmLog.setPlanId(financeSettleBaseDto.getPlanId());
        financeSettleBaseDto.setRepaymentConfirmLog(repaymentConfirmLog);
        
        /*if (!financeSettleBaseDto.getPreview()) {
        	repaymentConfirmLog.insert();
		}*/
        
    }


    @Override
    public void makeRepaymentPlanAllPlan(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

        String businessId = financeSettleReq.getBusinessId();
        String afterId = financeSettleReq.getAfterId();


        //查出当前还款计划的当前期
        RepaymentBizPlanList repaymentBizPlanList = bizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));



        if (repaymentBizPlanList != null) {
           
//            if (StringUtil.isEmpty(planId)) {// 整个业务结清
                List<RepaymentBizPlanSettleDto> curPeriods = financeSettleBaseDto.getCurrentPeriods();
                Map<String,RepaymentProjPlanSettleDto>  projPlanSettleDtoMap = new HashMap<>();

                //业务结清,整个业务的标的还款计划信息
                List<RepaymentProjPlanSettleDto> projPlanSettleDtoList = new LinkedList<>();

                for(RepaymentBizPlanSettleDto repaymentBizPlanSettleDto:curPeriods){
                    List<RepaymentProjPlanSettleDto> repaymentProjPlanSettleDtos = repaymentBizPlanSettleDto.getProjPlanStteleDtos();

                    for(RepaymentProjPlanSettleDto repaymentProjPlanSettleDto:repaymentProjPlanSettleDtos){
                        RepaymentProjPlanSettleDto projPlanSettleDto = projPlanSettleDtoMap.get(repaymentProjPlanSettleDto.getTuandaiProjectInfo().getProjectId());
                        if(projPlanSettleDto!=null){
                            logger.error("此业务有两个以上相同的标的还款计划 projPlanSettleDto"+ JSON.toJSONString(projPlanSettleDto));
                            throw new SettleRepaymentExcepiton("此业务有两个以上相同的标的还款计划", ExceptionCodeEnum.TOW_PROJ_PLAN.getValue().toString());
                        }
                        projPlanSettleDtoMap.put(repaymentProjPlanSettleDto.getTuandaiProjectInfo().getProjectId(),repaymentProjPlanSettleDto);
                        projPlanSettleDtoList.add(repaymentProjPlanSettleDto);
                    }
                }

                /*标的排序*/
                ProjPlanDtoUtil.sortSettleDtos(projPlanSettleDtoList);

                if (financeSettleBaseDto.getCuralResource()==null) {
					if (CollectionUtils.isEmpty(financeSettleBaseDto.getRepaymentResources())) {
						throw new ServiceRuntimeException("找不到还款来源");
					}else {
						financeSettleBaseDto.setCuralResource(financeSettleBaseDto.getRepaymentResources().get(0));
						financeSettleBaseDto.setResourceIndex(0);
						financeSettleBaseDto.setNoMoney(false);
						financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralResource().getRepayAmount());
					}
				}
                
                /*如果有减免,先处理减免*/
                for (SettleFeesVO derateFee : financeSettleBaseDto.getDerates()) {
					for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : projPlanSettleDtoList) {
						List<PlanListDetailShowPayDto> planListDetailShowPayDtos = sortFeeByShareProfitIndex(repaymentProjPlanSettleDto);
						for (PlanListDetailShowPayDto planListDetailShowPayDto : planListDetailShowPayDtos) {
							if (derateFee.getFeeId().equals(planListDetailShowPayDto.getFeelId()) 
									&& derateFee.getAmount().compareTo(BigDecimal.ZERO) >0 ) {
								BigDecimal showPayMoney = planListDetailShowPayDto.getShowPayMoney();
								BigDecimal derate = derateFee.getAmount();
								
								if (derate.compareTo(showPayMoney)<0) {
									showPayMoney = showPayMoney.subtract(derate);
									derate = BigDecimal.ZERO;
								}else if(derate.compareTo(showPayMoney)==0) {
									showPayMoney = BigDecimal.ZERO;
									derate = BigDecimal.ZERO;
								}else {
									showPayMoney = BigDecimal.ZERO;
									derate = derate.subtract(showPayMoney);
								}
								
								planListDetailShowPayDto.setShowPayMoney(showPayMoney);
								derateFee.setAmount(derate);
								if (derate.compareTo(BigDecimal.ZERO)==0) {
									financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex()+1);
									financeSettleBaseDto.setCuralResource(financeSettleBaseDto.getRepaymentResources().get(financeSettleBaseDto.getResourceIndex()));
								}
							}
						}
					}
				}
                /*如果有减免,先处理减免*/
                
                for(RepaymentProjPlanSettleDto repaymentProjPlanSettleDto:projPlanSettleDtoList){
                	//先还线上部分
                	financeSettleBaseDto.setProjPlanId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjPlanId());
                	financeSettleBaseDto.setProjectId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId());
                	List<PlanListDetailShowPayDto> planListDetailShowPayDtos = sortFeeByShareProfitIndex(repaymentProjPlanSettleDto);
                	
                	for (PlanListDetailShowPayDto planListDetailShowPayDto : planListDetailShowPayDtos) {
                		RepaymentProjPlanListDetail repaymentProjPlanListDetail = repaymentProjPlanSettleDto.getCurProjListDetailMap().get(planListDetailShowPayDto.getFeelId());
						if (planListDetailShowPayDto.getShareProfitIndex() >= Constant.ONLINE_OFFLINE_FEE_BOUNDARY || repaymentProjPlanListDetail == null ) {
							continue;
						}
						
						
						if (financeSettleBaseDto.isNoMoney()) {
							if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
								financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
							}
							
							financeSettleBaseDto.getUnderfillFees().add(planListDetailShowPayDto);
							
							continue;
						}
						
						if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())>0) {
							/*足够还掉某一个费用项*/
							financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(planListDetailShowPayDto.getShowPayMoney()));
							createProjFactRepay(planListDetailShowPayDto.getShowPayMoney(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())==0) {
							financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(planListDetailShowPayDto.getShowPayMoney()));
							createProjFactRepay(planListDetailShowPayDto.getShowPayMoney(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())<0) {
							createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
							changeRepaymentResources(planListDetailShowPayDto,repaymentProjPlanListDetail, financeSettleBaseDto);
						}
					}
                } 
                
                
                
				List<SettleFeesVO> penaltyFees = financeSettleBaseDto.getSettleInfoVO().getPenaltyFees();
				for (SettleFeesVO settleFeesVO : penaltyFees) {
					
					if (settleFeesVO.getShareProfitIndex() >= Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
						/*本金违约金先不核销*/
						continue;
					}
					
					for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : projPlanSettleDtoList) {
	                	financeSettleBaseDto.setProjPlanId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjPlanId());
	                	financeSettleBaseDto.setProjectId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId());
						// 再还提前违约金
						if (settleFeesVO.getProjectId().equals(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId())) {
							
							PlanListDetailShowPayDto planListDetailShowPayDto = new PlanListDetailShowPayDto() ;
							planListDetailShowPayDto.setFeelId(settleFeesVO.getFeeId());
							planListDetailShowPayDto.setFeelName(settleFeesVO.getFeeName());
							planListDetailShowPayDto.setShareProfitIndex(settleFeesVO.getShareProfitIndex());
							planListDetailShowPayDto.setShowPayMoney(settleFeesVO.getAmount());
							
							repaymentProjPlanSettleDto.getCurShowPayFeels().put(planListDetailShowPayDto.getFeelId(), planListDetailShowPayDto);
							
							if (financeSettleBaseDto.isNoMoney()) {
								if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
									financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
								}
								
								financeSettleBaseDto.getUnderfillFees().add(planListDetailShowPayDto);
								
								continue;
							}
							RepaymentProjPlanListDetail repaymentProjPlanListDetail = repaymentProjPlanSettleDto.getCurrProjPlanListDto().getProjPlanListDetails().get(0);
							RepaymentProjPlanListDetail projPlanListDetail = new RepaymentProjPlanListDetail() ;
							BeanUtils.copyProperties(repaymentProjPlanListDetail, projPlanListDetail);
							
							projPlanListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
							projPlanListDetail.setPlanDetailId(settleFeesVO.getPlanListDetailId());
							
							projPlanListDetail.setShareProfitIndex(settleFeesVO.getShareProfitIndex());
							projPlanListDetail.setProjPlanAmount(settleFeesVO.getAmount());
							projPlanListDetail.setProjFactAmount(BigDecimal.ZERO);
							projPlanListDetail.setFeeId(settleFeesVO.getFeeId());
							projPlanListDetail.setPlanItemName(RepayPlanFeeTypeEnum.feeIdOf(settleFeesVO.getFeeId()).getDesc());
							projPlanListDetail.setPlanItemType(Integer.parseInt(settleFeesVO.getPlanItemType()));
							projPlanListDetail.setCreateDate(new Date());
							projPlanListDetail.setCreateUser(financeSettleBaseDto.getUserId());
							/*由于提前违约金没有应还日期,所以duedate为null*/
//							projPlanListDetail.setPeriod(repaymentBizPlanList.getPeriod());
//							projPlanListDetail.setPlanListId(repaymentBizPlanList.getPlanListId());
//							projPlanListDetail.setProjPlanDetailId(null);
//							projPlanListDetail.setProjPlanListId(null);
					        
					        
							
							if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())>0) {
								createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
							}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())==0) {
								createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
							}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())<0) {
								createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(financeSettleBaseDto.getCuralDivideAmount()));
								changeRepaymentResources(planListDetailShowPayDto,projPlanListDetail, financeSettleBaseDto);
							}
							
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getProjPlanListDetails().add(projPlanListDetail);
						}
					}
				}
				/*核销本金违约金*/
				for (SettleFeesVO settleFeesVO : penaltyFees) {
	
					if (settleFeesVO.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
						continue;
					}
	
					for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : projPlanSettleDtoList) {
						financeSettleBaseDto
								.setProjPlanId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjPlanId());
						financeSettleBaseDto.setProjectId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId());
						// 再还提前违约金
						if (settleFeesVO.getProjectId()
								.equals(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId())) {
	
							PlanListDetailShowPayDto planListDetailShowPayDto = new PlanListDetailShowPayDto();
							planListDetailShowPayDto.setFeelId(settleFeesVO.getFeeId());
							planListDetailShowPayDto.setFeelName(settleFeesVO.getFeeName());
							planListDetailShowPayDto.setShareProfitIndex(settleFeesVO.getShareProfitIndex());
							planListDetailShowPayDto.setShowPayMoney(settleFeesVO.getAmount());
							/*本金违约金不算资金分发,所以要注释这里*/
//							repaymentProjPlanSettleDto.getCurShowPayFeels().put(planListDetailShowPayDto.getFeelId(),
//									planListDetailShowPayDto);
							/*本金违约金不算资金分发,所以要注释这里*/
							
							if (financeSettleBaseDto.isNoMoney()) {
								if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
									financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
								}
	
								financeSettleBaseDto.getUnderfillFees().add(planListDetailShowPayDto);
	
								continue;
							}
	
//							RepaymentProjPlanListDetail projPlanListDetail = new RepaymentProjPlanListDetail();
//							projPlanListDetail.setProjPlanAmount(settleFeesVO.getAmount());
//							projPlanListDetail.setFeeId(settleFeesVO.getFeeId());
//							projPlanListDetail.setPlanItemName(settleFeesVO.getPlanItemName());
//							projPlanListDetail.setPlanItemType(Integer.parseInt(settleFeesVO.getPlanItemType()));
//							projPlanListDetail.setPeriod(repaymentBizPlanList.getPeriod());
//							projPlanListDetail.setPlanListId(repaymentBizPlanList.getPlanListId());
//							projPlanListDetail.setProjPlanDetailId(null);
//							projPlanListDetail.setProjPlanListId(null);
	
							RepaymentProjPlanListDetail repaymentProjPlanListDetail = repaymentProjPlanSettleDto.getCurrProjPlanListDto().getProjPlanListDetails().get(0);
							RepaymentProjPlanListDetail projPlanListDetail = new RepaymentProjPlanListDetail() ;
							BeanUtils.copyProperties(repaymentProjPlanListDetail, projPlanListDetail);
							
							projPlanListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
							projPlanListDetail.setPlanDetailId(settleFeesVO.getPlanListDetailId());
							
							projPlanListDetail.setProjPlanAmount(settleFeesVO.getAmount());
							projPlanListDetail.setProjFactAmount(BigDecimal.ZERO);
							projPlanListDetail.setFeeId(settleFeesVO.getFeeId());
							projPlanListDetail.setPlanItemName(RepayPlanFeeTypeEnum.feeIdOf(settleFeesVO.getFeeId()).getDesc());
							projPlanListDetail.setPlanItemType(Integer.parseInt(settleFeesVO.getPlanItemType()));
							projPlanListDetail.setCreateDate(new Date());
							projPlanListDetail.setCreateUser(financeSettleBaseDto.getUserId());
							/*由于提前违约金没有应还日期,所以duedate为null*/
//							projPlanListDetail.setPeriod(repaymentBizPlanList.getPeriod());
//							projPlanListDetail.setPlanListId(repaymentBizPlanList.getPlanListId());
//							projPlanListDetail.setProjPlanDetailId(null);
//							projPlanListDetail.setProjPlanListId(null);
							
							if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount()) > 0) {
								createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail,
										financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(
										financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
							} else if (financeSettleBaseDto.getCuralDivideAmount()
									.compareTo(settleFeesVO.getAmount()) == 0) {
								createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail,
										financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(
										financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
							} else if (financeSettleBaseDto.getCuralDivideAmount()
									.compareTo(settleFeesVO.getAmount()) < 0) {
								createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), projPlanListDetail,
										financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
								financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount()
										.subtract(financeSettleBaseDto.getCuralDivideAmount()));
								changeRepaymentResources(planListDetailShowPayDto, projPlanListDetail,
										financeSettleBaseDto);
							}
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getProjPlanListDetails().add(projPlanListDetail);
						}
					}
				}

				for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : projPlanSettleDtoList) {
                	financeSettleBaseDto.setProjPlanId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjPlanId());
                	financeSettleBaseDto.setProjectId(repaymentProjPlanSettleDto.getRepaymentProjPlan().getProjectId());
					// 再还线下部分

					List<PlanListDetailShowPayDto> planListDetailShowPayDtos = sortFeeByShareProfitIndex(repaymentProjPlanSettleDto);
                	
                	for (PlanListDetailShowPayDto planListDetailShowPayDto : planListDetailShowPayDtos) {
                		RepaymentProjPlanListDetail repaymentProjPlanListDetail = repaymentProjPlanSettleDto.getCurProjListDetailMap().get(planListDetailShowPayDto.getFeelId());
						if (planListDetailShowPayDto.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY ) {
							continue;
						}
						
						if (repaymentProjPlanListDetail == null ) {
							repaymentProjPlanListDetail = new RepaymentProjPlanListDetail() ;
							repaymentProjPlanListDetail.setProjPlanAmount(planListDetailShowPayDto.getShowPayMoney());
							repaymentProjPlanListDetail.setFeeId(planListDetailShowPayDto.getFeelId());
							repaymentProjPlanListDetail.setPlanItemName(planListDetailShowPayDto.getFeelName());
							repaymentProjPlanListDetail.setPlanItemType(RepayPlanFeeTypeEnum.OVER_DUE_AMONT.getValue());
							repaymentProjPlanListDetail.setPeriod(repaymentBizPlanList.getPeriod());
							repaymentProjPlanListDetail.setPlanListId(repaymentBizPlanList.getPlanListId());
							repaymentProjPlanListDetail.setProjPlanDetailId(null);
							repaymentProjPlanListDetail.setProjPlanListId(null);
						}
						
						if (financeSettleBaseDto.isNoMoney()) {
							if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
								financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
							}
							financeSettleBaseDto.getUnderfillFees().add(planListDetailShowPayDto);
							continue;
						}
						
						if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())>0) {
							/*足够还掉某一个费用项*/
							financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(planListDetailShowPayDto.getShowPayMoney()));
							createProjFactRepay(planListDetailShowPayDto.getShowPayMoney(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())==0) {
							financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(planListDetailShowPayDto.getShowPayMoney()));
							createProjFactRepay(planListDetailShowPayDto.getShowPayMoney(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(planListDetailShowPayDto.getShowPayMoney())<0) {
							createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), repaymentProjPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
							changeRepaymentResources(planListDetailShowPayDto,repaymentProjPlanListDetail, financeSettleBaseDto);
						}
					}
                	
				}
				
					// 再还其他费用
				for (SettleFeesVO settleFeesVO : financeSettleBaseDto.getSettleInfoVO().getOtherFees()) {
					if (settleFeesVO.getAmount()==null) {
						continue;
					}
					
					financeSettleBaseDto.setProjectId(curPeriods.get(0).getProjPlanStteleDtos().get(0).getRepaymentProjPlan().getProjectId());
					financeSettleBaseDto.setProjPlanId(curPeriods.get(0).getProjPlanStteleDtos().get(0).getRepaymentProjPlan().getProjPlanId());
					
					PlanListDetailShowPayDto planListDetailShowPayDto = new PlanListDetailShowPayDto() ;
					planListDetailShowPayDto.setFeelId(settleFeesVO.getFeeId());
					planListDetailShowPayDto.setFeelName(settleFeesVO.getFeeName());
					planListDetailShowPayDto.setShareProfitIndex(settleFeesVO.getShareProfitIndex());
					planListDetailShowPayDto.setShowPayMoney(settleFeesVO.getAmount());
					
					
					if (financeSettleBaseDto.isNoMoney()) {
						if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
							financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
						}
						financeSettleBaseDto.getUnderfillFees().add(planListDetailShowPayDto);
						continue;
					}
					
					RepaymentProjPlanListDetail projPlanListDetail = new RepaymentProjPlanListDetail() ;
					projPlanListDetail.setProjPlanAmount(settleFeesVO.getAmount());
					projPlanListDetail.setFeeId(settleFeesVO.getFeeId());
					projPlanListDetail.setPlanItemName(settleFeesVO.getPlanItemName());
					projPlanListDetail.setPlanItemType(Integer.parseInt(settleFeesVO.getPlanItemType()));
					projPlanListDetail.setPeriod(repaymentBizPlanList.getPeriod());
					projPlanListDetail.setPlanListId(repaymentBizPlanList.getPlanListId());
					projPlanListDetail.setProjPlanDetailId(null);
					projPlanListDetail.setProjPlanListId(null);
					
					if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())>0) {
						createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
					}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())==0) {
						createProjFactRepay(settleFeesVO.getAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(settleFeesVO.getAmount()));
					}else if (financeSettleBaseDto.getCuralDivideAmount().compareTo(settleFeesVO.getAmount())<0) {
						createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), projPlanListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
						financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(financeSettleBaseDto.getCuralDivideAmount()));
						changeRepaymentResources(planListDetailShowPayDto,projPlanListDetail, financeSettleBaseDto);
					}
					
					
				}
                
				/*将数据入库*/

				if (!financeSettleBaseDto.getPreview()) {
					for (RepaymentBizPlanBak bak : financeSettleBaseDto.getRepaymentBizPlanBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					for (RepaymentBizPlanListBak bak : financeSettleBaseDto.getRepaymentBizPlanListBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					for (RepaymentBizPlanListDetailBak bak : financeSettleBaseDto.getRepaymentBizPlanListDetailBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					for (RepaymentProjPlanBak bak : financeSettleBaseDto.getRepaymentProjPlanBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					for (RepaymentProjPlanListBak bak : financeSettleBaseDto.getRepaymentProjPlanListBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					for (RepaymentProjPlanListDetailBak bak : financeSettleBaseDto.getRepaymentProjPlanListDetailBaks()) {
						bak.setConfirmLogId(financeSettleBaseDto.getRepaymentConfirmLog().getConfirmLogId());
						bak.insert();
					}
					
					boolean saveConfirmLog = financeSettleBaseDto.getRepaymentConfirmLog().insert();
	                if (!saveConfirmLog) {
						throw new ServiceRuntimeException("结清记录保存异常");
					}
					
	                RepayPlanSettleStatusEnum e = RepayPlanSettleStatusEnum.PAYED ;
	                if (financeSettleBaseDto.getPreSettle()) {
						e = RepayPlanSettleStatusEnum.PAYED_EARLY;
					}
	                
	                if (!CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
	                	//判断是否有未还满的费用项
	                	for (PlanListDetailShowPayDto planListDetailShowPayDto : financeSettleBaseDto.getUnderfillFees()) {
	                		if (planListDetailShowPayDto.getShareProfitIndex()!=null && planListDetailShowPayDto.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
								financeSettleBaseDto.setBadSettle(true);
								e = RepayPlanSettleStatusEnum.PAYED_LOSS ;
							}
	                		
							if (planListDetailShowPayDto.getFeelId().equals(RepayPlanFeeTypeEnum.PRINCIPAL.getUuid())) {
								financeSettleBaseDto.setLossSettle(true);
								e = RepayPlanSettleStatusEnum.PAYED_BAD ;
							}
						}
					}
	                
	                List<RepaymentProjPlanList> projPlanList = new ArrayList<>() ;
	                
	                for (RepaymentBizPlanSettleDto bizPlanSettleDto : curPeriods) {
	                	/*更新业务plan*/
	                	bizPlanSettleDto.getRepaymentBizPlan().setPlanStatus(e.getValue());
	                	bizPlanSettleDto.getRepaymentBizPlan().updateAllColumnById();
	                	
	                	for (RepaymentProjPlanSettleDto repaymentProjPlanSettleDto : bizPlanSettleDto.getProjPlanStteleDtos()) {
							
	                		RepayPlanSettleStatusEnum pe = setProjPlanList(financeSettleBaseDto,repaymentProjPlanSettleDto);
							/*更新标PLAN*/
	                		repaymentProjPlanSettleDto.getRepaymentProjPlan().setPlanStatus(pe.getValue());
							repaymentProjPlanSettleDto.getRepaymentProjPlan().updateAllColumnById();
							
							/*将标的细项回填业务细项*/
							for (Entry<String, BigDecimal> entry : repaymentProjPlanSettleDto.getCurFactRepayAmount().entrySet()) {
								for (RepaymentBizPlanListDetail bizPlanListDetail : bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails()) {
									if (bizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())) {
										continue;
									}
									
									if (bizPlanListDetail.getFeeId().equals(entry.getKey())) {
										if (bizPlanListDetail.getFactAmount()==null) {
											bizPlanListDetail.setFactAmount(BigDecimal.ZERO);
										}
										bizPlanListDetail.setFactAmount(bizPlanListDetail.getFactAmount().add(entry.getValue()));
									}
								}
							}
							/*将标的细项回填业务细项*/
							
							/*新增提前违约金细项*/
							for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanSettleDto.getCurrProjPlanListDto().getProjPlanListDetails()) {
								if (repaymentProjPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())) {
									RepaymentBizPlanListDetail bizPlanDetail = existBizPlanDetail(bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails(), repaymentProjPlanListDetail.getPlanDetailId());
									if (bizPlanDetail==null) {
										bizPlanDetail = new RepaymentBizPlanListDetail() ;
										RepaymentBizPlanListDetail bizPlanListDetail = bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails().get(0);
										BeanUtils.copyProperties(bizPlanListDetail, bizPlanDetail);
										
										bizPlanDetail.setPlanDetailId(repaymentProjPlanListDetail.getPlanDetailId());
										bizPlanDetail.setPlanAmount(BigDecimal.ZERO);
										bizPlanDetail.setFactAmount(BigDecimal.ZERO);
										bizPlanDetail.setFactRepayDate(null);
										bizPlanDetail.setShareProfitIndex(repaymentProjPlanListDetail.getShareProfitIndex());
										bizPlanDetail.setFeeId(repaymentProjPlanListDetail.getFeeId());
										bizPlanDetail.setPlanItemName(repaymentProjPlanListDetail.getPlanItemName());
										bizPlanDetail.setPlanItemType(repaymentProjPlanListDetail.getPlanItemType());
										bizPlanDetail.setCreateDate(new Date());
										bizPlanDetail.setCreateUser(financeSettleBaseDto.getUserId());
									}
									
									bizPlanDetail.setPlanAmount(bizPlanDetail.getPlanAmount().add(repaymentProjPlanListDetail.getProjPlanAmount()));
									bizPlanDetail.setFactAmount(bizPlanDetail.getFactAmount().add(repaymentProjPlanListDetail.getProjFactAmount()));
									
									bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails().add(bizPlanDetail);
								}
							}
							/*新增提前违约金细项*/
							
							/*更新标PLANList*/
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList().setFactRepayDate(financeSettleBaseDto.getRepaymentResources().get(financeSettleBaseDto.getRepaymentResources().size()-1).getRepayDate());
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList().setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList().setRepayFlag(PepayPlanRepayFlagStatusEnum.UNDERLINE_ALL_SETTLE.getValue());
							repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList().updateAllColumnById();
							
							/*更新往后期的标的期数*/
		                	for (RepaymentProjPlanListDto afterProjPlanListDto : repaymentProjPlanSettleDto.getAfterProjPlanListDtos()) {
		                		afterProjPlanListDto.getRepaymentProjPlanList().setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
		                		afterProjPlanListDto.getRepaymentProjPlanList().setRepayFlag(PepayPlanRepayFlagStatusEnum.UNDERLINE_ALL_SETTLE.getValue());
		                		afterProjPlanListDto.getRepaymentProjPlanList().updateAllColumnById();
							}
		                	
							/*将本期调用合规化还款接口的projPlanList存入内存*/
							projPlanList.add(repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList());
						}
	                	
	                	BigDecimal factReapy = BigDecimal.ZERO;
	                	BigDecimal onLineFee = BigDecimal.ZERO;
	                	BigDecimal planAmount = BigDecimal.ZERO;
	                	
	                	for (RepaymentBizPlanListDetail bizPlanListDetail : bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails()) {
							
	                		planAmount = planAmount.add(bizPlanListDetail.getPlanAmount());
	                		if (bizPlanListDetail.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
								onLineFee = onLineFee.add(bizPlanListDetail.getPlanAmount());
							}
	                		
//	                		List<RepaymentProjFactRepay> bizPlanFactRepay = calcBizPlanFactRepay(bizPlanSettleDto.getRepaymentBizPlan().getPlanId(), bizPlanListDetail.getPlanDetailId(), financeSettleBaseDto);
//							if (CollectionUtils.isEmpty(bizPlanFactRepay)) {
//								continue;
//							}
							
							/*更新业务detail*/
							/*for (RepaymentProjFactRepay repaymentProjFactRepay : bizPlanFactRepay) {
								bizPlanListDetail.setFactAmount((bizPlanListDetail.getFactAmount()==null?BigDecimal.ZERO:bizPlanListDetail.getFactAmount())
										.add(repaymentProjFactRepay.getFactAmount()));*/
								bizPlanListDetail.setFactRepayDate(financeSettleBaseDto.getRepaymentResources().get(financeSettleBaseDto.getRepaymentResources().size()-1).getRepayDate());
								if (bizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())) {
									bizPlanListDetail.insert();
								}else {
									bizPlanListDetail.updateAllColumnById();
								}
//							}
							
							factReapy = factReapy.add(bizPlanListDetail.getFactAmount());
						}
	                	if (factReapy.compareTo(BigDecimal.ZERO)>0) {
	                		bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
	                	}else if (factReapy.compareTo(onLineFee)>=0) {
	                		bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
						}else if (factReapy.compareTo(planAmount)>=0) {
							bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
						}
	                	
	                	
	                	/*更新业务planList*/
			            
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setFactRepayDate(financeSettleBaseDto.getRepaymentResources().get(financeSettleBaseDto.getRepaymentResources().size()-1).getRepayDate());
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setRepayFlag(PepayPlanRepayFlagStatusEnum.UNDERLINE_ALL_SETTLE.getValue());
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setFinanceComfirmDate(new Date());
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setFinanceConfirmUser(financeSettleBaseDto.getUserId());;
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().setFinanceConfirmUserName(financeSettleBaseDto.getUserName());;
	                	
	                	
	                	
	                	bizPlanSettleDto.getCurrBizPlanListDto().getRepaymentBizPlanList().updateAllColumnById();
	                	
	                	/*更新结清期往后的期数*/
	                	for (RepaymentBizPlanListDto bizPlanListDto : bizPlanSettleDto.getAfterBizPlanListDtos()) {
	                		bizPlanListDto.getRepaymentBizPlanList().setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
	                		bizPlanListDto.getRepaymentBizPlanList().setRepayFlag(PepayPlanRepayFlagStatusEnum.UNDERLINE_ALL_SETTLE.getValue());
	                		bizPlanListDto.getRepaymentBizPlanList().updateAllColumnById();
						}
					}
	                
	                //更新结清期备注
	                updateRemark(financeSettleBaseDto);
	                
	                /*将调用合规化还款的projPlanList保存到数据库*/
	                for(RepaymentProjPlanList repaymentProjPlanList: projPlanList){
	                    RepaymentConfirmPlatRepayLog log = new RepaymentConfirmPlatRepayLog();
	                    log.setConfirmLogId(financeSettleBaseDto.getUuid());
	                    log.setProjPlanListId(repaymentProjPlanList.getProjPlanListId());
	                    log.setCreateTime(new Date());
	                    log.setCreateUser(loginUserInfoHelper.getUserId()==null?Constant.ADMIN_ID:loginUserInfoHelper.getUserId());
	                    repaymentConfirmPlatRepayLogService.insert(log);
	                }
	                
	                executor.execute(new Runnable() {
	                    @Override
	                    public void run() {
	                        logger.info("调用平台合规化还款接口开始，confirmLogId：{}", financeSettleBaseDto.getUuid());
	                        try {
	                            //睡一下，让还款的信息先存完。
	                            try{
	                                Thread.sleep(3000);
	                            }catch (InterruptedException e){
	                                logger.error(e.getMessage(), e);
	                            }
	                            tdrepayRecharge(projPlanList);
	                            /*更新财务管理列表*/
	                            repaymentBizPlanListSynchService.updateRepaymentBizPlan();
	                            repaymentBizPlanListSynchService.updateRepaymentBizPlanList();
	                            repaymentBizPlanListSynchService.updateRepaymentBizPlanListDetail();
	                            shareProfitService.updateRepayPlanToLMS(businessId);
	                        } catch (Exception e) {
	                            logger.error(e.getMessage(), e);
	                            Thread.currentThread().interrupt();
	                        }
	                        logger.info("调用平台合规化还款接口结束");
	                    }
	                });
	                
	                
				}
				
                logger.info(JSON.toJSONString(financeSettleBaseDto.getUnderfillFees()));


        } else {
            logger.error("找不到对应的还款计划列表repaymentBizPlanList  businessId:" + businessId + "     after_id:" + afterId);
            throw new SettleRepaymentExcepiton("找不到对应的还款计划列表", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
        }

    }

    /**
     * 根据planListDetail 查找
     * @author 王继光
     * 2018年8月14日 下午9:40:58
     * @param details
     * @param planListDetailId
     * @return
     */
    private RepaymentBizPlanListDetail existBizPlanDetail(List<RepaymentBizPlanListDetail> details,String planListDetailId) {
    	for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
    		if (planListDetailId.equals(repaymentBizPlanListDetail.getPlanDetailId())) {
				return repaymentBizPlanListDetail ;
			}
		}
    	return null ;
    }
	/**
	 * 根据实还 设置 标的状态
	 * @author 王继光
	 * 2018年8月10日 下午10:43:26
	 * @param financeSettleBaseDto
	 * @param repaymentProjPlanSettleDto
	 * @return
	 */
	private RepayPlanSettleStatusEnum setProjPlanList(FinanceSettleBaseDto financeSettleBaseDto,
			RepaymentProjPlanSettleDto repaymentProjPlanSettleDto) {
		RepayPlanSettleStatusEnum pe = RepayPlanSettleStatusEnum.PAYED;
		if (financeSettleBaseDto.getPreSettle()) {
			pe = RepayPlanSettleStatusEnum.PAYED_EARLY;
		}

		/* 比较各费用实还金额,以确认是哪种结清 */
		boolean bad = false;
		boolean loss = false;
		BigDecimal pfact = BigDecimal.ZERO;
		BigDecimal pOnlinePlanAmount = BigDecimal.ZERO;
		BigDecimal pAllPlanAmount = BigDecimal.ZERO;
		for (Entry<String, PlanListDetailShowPayDto> et : repaymentProjPlanSettleDto.getCurShowPayFeels().entrySet()) {
			String feeId = et.getKey();
			PlanListDetailShowPayDto dto = et.getValue();

			pAllPlanAmount = pAllPlanAmount.add(dto.getShowPayMoney());

			BigDecimal fact = BigDecimal.ZERO;
			if (repaymentProjPlanSettleDto.getCurFactRepayAmount().containsKey(feeId)) {
				fact = repaymentProjPlanSettleDto.getCurFactRepayAmount().get(feeId);
			}
			pfact = pfact.add(fact);

			if (dto.getShareProfitIndex() > Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
				continue;
			}

			if (ONLINE_FEES.contains(feeId)) {
				pOnlinePlanAmount = pOnlinePlanAmount.add(dto.getShowPayMoney());
			}

			if (fact.compareTo(dto.getShowPayMoney()) < 0) {
				if (dto.getFeelId().equals(RepayPlanFeeTypeEnum.PRINCIPAL.getUuid())) {
					bad = true;
				}
				if (dto.getFeelId().equals(RepayPlanFeeTypeEnum.INTEREST.getUuid())
						|| dto.getFeelId().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getUuid())
						|| dto.getFeelId().equals(RepayPlanFeeTypeEnum.PLAT_CHARGE.getUuid())
						|| dto.getFeelId().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid())
						|| dto.getFeelId().equals(RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid())) {
					loss = true;
				}
			}

		}
		/* 比较各费用实还金额,以确认是哪种结清 */

		if (bad) {
			pe = RepayPlanSettleStatusEnum.PAYED_BAD;
		}
		if (loss) {
			pe = RepayPlanSettleStatusEnum.PAYED_LOSS;
		}

		if (pfact.compareTo(BigDecimal.ZERO) > 0) {
			repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList()
					.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
		}  
		if (pfact.compareTo(pOnlinePlanAmount) >= 0) {
			repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList()
					.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
		}  
		if (pfact.compareTo(pAllPlanAmount) >= 0) {
			repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList()
					.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
		}
		return pe ;
	}
    
    private void tdrepayRecharge(List<RepaymentProjPlanList> projPlanLists) {

        if(projPlanLists==null||projPlanLists.size()==0){
        	logger.info("开始调用平台合规化还款接口，参数：{}", projPlanLists);
            return;
        }
//        //睡一下，让还款的信息先存完。
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for(RepaymentProjPlanList repaymentProjPlanList : projPlanLists){
            SysApiCallFailureRecord record = new SysApiCallFailureRecord();
            Result result = null;
            try {
                record.setModuleName(AlmsServiceNameEnums.FINANCE.getName());
                record.setApiCode(Constant.INTERFACE_CODE_PLATREPAY_REPAYMENT);
                record.setApiName(Constant.INTERFACE_NAME_PLATREPAY_REPAYMENT);
                record.setRefId(repaymentProjPlanList.getProjPlanListId());
                record.setCreateUser(
                        StringUtil.isEmpty(loginUserInfoHelper.getUserId()) ? "null" : loginUserInfoHelper.getUserId());
                record.setCraeteTime(new Date());
                record.setTargetUrl(Constant.INTERFACE_CODE_PLATREPAY_REPAYMENT);

                RepaymentProjPlan plan = null;

                if (repaymentProjPlanList != null) {
                    plan = repaymentProjPlanService.selectById(repaymentProjPlanList.getProjPlanId());
                }

                if (plan != null) {

                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("projPlanListId",repaymentProjPlanList.getProjPlanListId());


                    record.setApiParamPlaintext(JSONObject.toJSONString(paramMap));
//                    sysApiCallFailureRecordService.insert(record);

                    // 平台合规化还款接口
                    result = platformRepaymentFeignClient.repayment(paramMap);
                    if (result != null) {
                        record.setApiReturnInfo(JSONObject.toJSONString(result));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                record.setApiReturnInfo(e.getMessage());
            }
            logger.info("平台合规化还款接口返回结果：{}", JSONObject.toJSONString(result));
            if (result == null || !"1".equals(result.getCode())) {
                sysApiCallFailureRecordService.insert(record);
            }
        }

    }
    
    /**
     * 计算业务层面的实还
     * @author 王继光
     * 2018年7月30日 下午8:19:24
     * @param planId
     * @param planListDetailId
     * @param dto
     * @return
     */
    private List<RepaymentProjFactRepay> calcBizPlanFactRepay(String planId,String planListDetailId,FinanceSettleBaseDto dto) {
    	if (dto.getProjFactRepays().containsKey(planId)) {
			if (dto.getProjFactRepays().get(planId).containsKey(planListDetailId)) {
				List<RepaymentProjFactRepay> list = dto.getProjFactRepays().get(planId).get(planListDetailId);
				return list ;
			}
		}
    	return null ;
    }
    /**
     * 取下个还款来源
     * @author 王继光
     * 2018年7月30日 上午11:53:32
     * @param financeSettleBaseDto
     * @param noMoney
     * @return
     */
    private boolean changeRepaymentResources(FinanceSettleBaseDto financeSettleBaseDto) {
			/*改还款来源继续往下走*/
			try {
				financeSettleBaseDto.setCuralResource(financeSettleBaseDto.getRepaymentResources().get(financeSettleBaseDto.getResourceIndex()+1));
				financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralResource().getRepayAmount());
				financeSettleBaseDto.setResourceIndex(financeSettleBaseDto.getResourceIndex()+1);
				financeSettleBaseDto.setNoMoney(false);
				return true ;
			} catch (Exception e) {
				/*也没下一个了还款来源*/
				financeSettleBaseDto.setResourceIndex(null);
				financeSettleBaseDto.setCuralResource(null);
				financeSettleBaseDto.setCuralDivideAmount(BigDecimal.ZERO);
				financeSettleBaseDto.setNoMoney(true);
				return false ;
			}
    }
    
    /**
     * 递归还款来源以便还尽某条RepaymentProjPlanListDetail
     * @author 王继光
     * 2018年7月30日 下午3:39:47
     * @param projListDetail
     * @param financeSettleBaseDto
     * @param noMoney
     */
    private void changeRepaymentResources(PlanListDetailShowPayDto fee,RepaymentProjPlanListDetail projListDetail,FinanceSettleBaseDto financeSettleBaseDto) {
		BigDecimal unpaid = projListDetail.getProjPlanAmount().subtract(projListDetail.getProjFactAmount());
		if (unpaid.compareTo(BigDecimal.ZERO) < 0) {
			unpaid = fee.getShowPayMoney().subtract(projListDetail.getProjFactAmount());
		}
		if (changeRepaymentResources(financeSettleBaseDto)) {
			if (financeSettleBaseDto.getCuralDivideAmount().compareTo(unpaid)>=0) {
				projListDetail.setProjFactAmount(financeSettleBaseDto.getCuralDivideAmount());
				projListDetail.setFactRepayDate(financeSettleBaseDto.getCuralResource().getRepayDate());
				createProjFactRepay(unpaid, projListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
				
				financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(unpaid));
				return ;
			}else {
				projListDetail.setProjFactAmount(projListDetail.getProjFactAmount().add(financeSettleBaseDto.getCuralDivideAmount()));
				projListDetail.setFactRepayDate(financeSettleBaseDto.getCuralResource().getRepayDate());
				createProjFactRepay(financeSettleBaseDto.getCuralDivideAmount(), projListDetail, financeSettleBaseDto.getCuralResource(), financeSettleBaseDto);
				financeSettleBaseDto.setCuralDivideAmount(financeSettleBaseDto.getCuralDivideAmount().subtract(financeSettleBaseDto.getCuralDivideAmount()));
				
				changeRepaymentResources(fee,projListDetail, financeSettleBaseDto);
			}
		}else {
			/*没钱*/
			if (CollectionUtils.isEmpty(financeSettleBaseDto.getUnderfillFees())) {
				financeSettleBaseDto.setUnderfillFees(new ArrayList<>());
			}
			financeSettleBaseDto.getUnderfillFees().add(fee);
		}
    	
    }
    
    /**
     * 从RepaymentProjPlanSettleDto取出PlanListDetailShowPayDto并排序
     * @author 王继光
     * 2018年7月30日 下午12:01:49
     * @param repaymentProjPlanSettleDto
     * @return
     */
    private List<PlanListDetailShowPayDto> sortFeeByShareProfitIndex(RepaymentProjPlanSettleDto repaymentProjPlanSettleDto) {
    	List<PlanListDetailShowPayDto> planListDetailShowPayDtos = new ArrayList<>() ;
    	for (Entry<String, PlanListDetailShowPayDto> entry : repaymentProjPlanSettleDto.getCurShowPayFeels().entrySet()) {
    		planListDetailShowPayDtos.add(entry.getValue());
		}
    	ProjPlanDtoUtil.sortFeeByShareProfitIndex(planListDetailShowPayDtos);
    	return planListDetailShowPayDtos ;
    }






    //累加所有标的明细项
    public void sumProjMoney(CurrPeriodProjDetailVO vo, RepaymentSettleMoneyDto moneyDto) {
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









    public void createSettleLogDetail(BigDecimal amount, RepaymentSettleMoneyDto moneyDto, RepaymentResource resource, FinanceSettleBaseDto financeSettleBaseDto) {

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
        if (projPlanId != null) {
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

            webFactRepays.put(projPlanId, vo);
        }else {
            List<SettleFeesVO> otherFees = financeSettleBaseDto.getOtherFees();
            SettleFeesVO settleFeesVO=new SettleFeesVO();
            settleFeesVO.setFeeId(moneyDto.getFeeId());
            settleFeesVO.setFeeName(moneyDto.getFeeName());
            settleFeesVO.setAmount(amount);
            otherFees.add(settleFeesVO);
        }
        Boolean preview = financeSettleBaseDto.getPreview() == null ? true : financeSettleBaseDto.getPreview();
        if (!preview) { //保存操作 true 为预览 false为保存
            repaymentProjFactRepayMapper.insert(fact);
        }


    }

    
    /**
     * 处理还款来源
     * @author 王继光
     * 2018年8月3日 下午2:40:26
     * @param financeSettleBaseDto
     * @param financeSettleReq
     */
    public void handleRepaymentResource(FinanceSettleBaseDto financeSettleBaseDto, FinanceSettleReq financeSettleReq) {

        List<RepaymentResource> repaymentResources = financeSettleBaseDto.getRepaymentResources();
        BigDecimal surplus = financeSettleReq.getSurplusFund();
        List<String> mprIds = financeSettleReq.getMprIds();

        /*将减免的resource查出添加进来*/
		for (SettleFeesVO settleFeesVO : financeSettleBaseDto.getDerates()) {
			RepaymentResource repaymentResource = new RepaymentResource();
			UUID uuid = UUID.randomUUID();
			repaymentResource.setResourceId(String.valueOf(uuid));
			repaymentResource.setAfterId(financeSettleBaseDto.getAfterId());
			repaymentResource.setBusinessId(financeSettleBaseDto.getBusinessId());
			repaymentResource.setOrgBusinessId(financeSettleBaseDto.getOrgBusinessId());
			repaymentResource.setCreateDate(new Date());
			repaymentResource.setCreateUser(financeSettleBaseDto.getUserId());
			repaymentResource.setIsCancelled(0);
			repaymentResource.setRepayAmount(settleFeesVO.getAmount());
			repaymentResource.setRepayDate(new Date());
			repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.DERATE.getValue().toString());
			repaymentResource.setRepaySourceRefId(settleFeesVO.getPlanItemName());
			repaymentResource.setConfirmLogId(financeSettleBaseDto.getUuid());
			repaymentResources.add(repaymentResource);
			/*减免金额不加入实还金额里面*/
		}
		/*将减免的resource查出添加进来*/
        
        if (!CollectionUtils.isEmpty(mprIds)) {
        	List<MoneyPoolRepayment> moneyPoolRepaymentList = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().in("id", mprIds));
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
                repaymentResources.add(repaymentResource);
                financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(repaymentResource.getRepayAmount()));
                
                if (!financeSettleReq.getPreview()) {
                    repaymentResource.insert();
                    
                    List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectBatchIds(financeSettleReq.getMprIds());
            		for (MoneyPoolRepayment mpr : moneyPoolRepayments) {
            			mpr.setLastState(mpr.getState());
            			mpr.setUpdateTime(new Date());
            			mpr.setUpdateUser(loginUserInfoHelper.getUserId());
            			
            			mpr.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
            			
            			MoneyPool moneyPool = moneyPoolMapper.selectById(mpr.getMoneyPoolId());
            			moneyPool.setLastStatus(moneyPool.getStatus());
            			moneyPool.setLastFinanceStatus(moneyPool.getFinanceStatus());
            			moneyPool.setStatus(RepayRegisterState.完成.toString());
            			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.财务确认已还款.toString());
            			moneyPool.setUpdateTime(new Date());
            			moneyPool.setUpdateUser(loginUserInfoHelper.getUserId());
            			
            			mpr.updateById();
            			moneyPool.updateById();
            		}
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
            repaymentResource.setConfirmLogId(financeSettleBaseDto.getUuid());
            //11:用往期结余还款',
            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.SURPLUS_REPAY.getValue().toString());

            if (!financeSettleReq.getPreview()) {
                accountantOverRepayLog.insert();
                financeSettleBaseDto.getRepaymentConfirmLog().setSurplusUseRefId(accountantOverRepayLog.getId().toString());
                repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
                repaymentResource.insert();
                if (mprIds.size() == 0) {
                    financeSettleBaseDto.getRepaymentSettleLog().setRepayDate(repaymentResource.getRepayDate());
                }

            }

            repaymentResources.add(repaymentResource);
            financeSettleBaseDto.setRepayFactAmount(financeSettleBaseDto.getRepayFactAmount().add(repaymentResource.getRepayAmount()));
        }
        financeSettleBaseDto.setRepaymentResources(repaymentResources);
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
        fact.setOrigBusinessId(financeSettleBaseDto.getOrgBusinessId());
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
        fact.setConfirmLogId(financeSettleBaseDto.getUuid());
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
        
        if (!financeSettleBaseDto.getPreview()) {
        	fact.insert();
        	if (detail.getProjPlanDetailId()!=null && !detail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())) {
        		detail.updateAllColumnById();
			}
        	if (detail.getProjPlanDetailId()!=null && detail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue())) {
        		detail.setProjPlanDetailId(UUID.randomUUID().toString());
        		detail.insert();
			}
		}
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
            logger.info("进这里就麻烦大了!!");
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
            case 70:
				vo.setItem70(vo.getItem70().add(amount));
				if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.PRINCIPAL_PENALTY.getUuid())) {
					vo.setItem70principal(vo.getItem70principal().add(amount));
				}
				if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid())) {
					vo.setItem70service(vo.getItem70service().add(amount));
				}
				if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid())) {
					vo.setItem70platform(vo.getItem70platform().add(amount));
				}
            	break;
            default:
            	vo.setOtherMoney(vo.getOtherMoney().add(amount));
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
        setCurFactRepayAmount(financeSettleBaseDto,fact);

    }
    
    /**
     * 将实还累加并保存进标的
     * @author 王继光
     * 2018年8月10日 下午8:33:07
     * @param financeSettleBaseDto
     * @param fact
     */
    private void setCurFactRepayAmount(FinanceSettleBaseDto financeSettleBaseDto,RepaymentProjFactRepay fact) {
    	for (RepaymentBizPlanSettleDto bizPlanSettleDto : financeSettleBaseDto.getCurrentPeriods()) {
			for (RepaymentProjPlanSettleDto projPlanSettleDto : bizPlanSettleDto.getProjPlanStteleDtos()) {
				if (fact.getProjectId().equals(projPlanSettleDto.getRepaymentProjPlan().getProjectId())) {
					if (projPlanSettleDto.getCurFactRepayAmount()==null) {
						projPlanSettleDto.setCurFactRepayAmount(new HashMap<>());
					}
					if (projPlanSettleDto.getCurFactRepayAmount().containsKey(fact.getFeeId())) {
						BigDecimal factAmount = projPlanSettleDto.getCurFactRepayAmount().get(fact.getFeeId());
						factAmount = factAmount.add(fact.getFactAmount());
						projPlanSettleDto.getCurFactRepayAmount().put(fact.getFeeId(), factAmount);
					}else {
						projPlanSettleDto.getCurFactRepayAmount().put(fact.getFeeId(), fact.getFactAmount());
					}
				}
			}
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
        if (diff > 0 ) {
        	//&& cur.getCurrentStatus().equals(RepayPlanStatus.OVERDUE.getName())
            infoVO.setOverDueDays(diff);
        }
        infoVO.setItem10(repaymentProjPlanListDetailMapper.calcUnpaidPrincipal(req.getBusinessId(), req.getPlanId()));
        calcCurPeriod(cur, infoVO, settleDate, req.getPlanId());


        infoVO.setRepayPlanDate(cur.getDueDate());

        /*repaymentBizPlanListDetailMapper.selectLastPlanListDerateFees(req.getBusinessId(), cur.getDueDate(), req.getPlanId())*/
        infoVO.setDerates(listDerate(cur.getPlanListId()));
        infoVO.setLackFees(repaymentBizPlanListDetailMapper.selectLastPlanListLackFees(req.getBusinessId(), cur.getDueDate(), req.getPlanId()));

        calcPenalty(req,infoVO);
        
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
     * 查询减免项,转化为SettleFeesVO
     * @author 王继光
     * 2018年8月7日 上午10:50:23
     * @param planListId
     * @return
     */
    private List<SettleFeesVO> listDerate(String planListId) {
		List<ApplyDerateType> listDerate = applyDerateTypeMapper.listDerate(planListId,1);
		List<SettleFeesVO> derate = new ArrayList<>();
		for (ApplyDerateType d : listDerate) {
			SettleFeesVO settleFeesVO = new SettleFeesVO();
			settleFeesVO.setAmount(d.getDerateMoney());
			settleFeesVO.setFeeName(d.getDerateTypeName());
			
			if (d.getDerateType().equals("60")) {
				if (d.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
					settleFeesVO.setFeeName(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getDesc());
				}
				if (d.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
					settleFeesVO.setFeeName(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getDesc());
				}
			}
			/*注意,此处特意将ApplyDerateType.ApplyDerateTypeId放进planItemName*/
			settleFeesVO.setPlanItemName(d.getApplyDerateTypeId());
			/*注意,此处特意将ApplyDerateType.ApplyDerateTypeId放进planItemName*/
			settleFeesVO.setFeeId(d.getFeeId());
			settleFeesVO.setPlanItemType(d.getDerateType());
			settleFeesVO.setShareProfitIndex(1);
			derate.add(settleFeesVO);
		}
		//TODO 还有减免 其他费用项 还没做好,但是当前2018/8/10并不需要
		return derate ;
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
    private void calcPenalty(final FinanceSettleReq req,SettleInfoVO infoVO) {
    	List<SettleFeesVO> fees = new ArrayList<>();
    	infoVO.setPenaltyFees(fees);
    	for (RepaymentBizPlanList bizPlanList : getCurrenPeroids(req)) {
    		
    		infoVO.getPenaltyBiz().put(bizPlanList.getPlanListId(), new ArrayList<>());
    		
    		EntityWrapper<RepaymentProjPlanList> eWrapper = new EntityWrapper<RepaymentProjPlanList>();
            eWrapper.eq("period", bizPlanList.getPeriod());
            eWrapper.eq("business_id", bizPlanList.getBusinessId());
            if (!StringUtil.isEmpty(req.getPlanId())) {
                eWrapper.eq("plan_id", req.getPlanId());
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

            
            for (ProjExtRate projExtRate : extRates) {
                BigDecimal penalty = BigDecimal.ZERO;
                SettleFeesVO fee = new SettleFeesVO();
                if (PepayPlanProjExtRatCalEnum.BY_BORROW_MONEY.getValue() == projExtRate.getCalcWay()) {
                    //1.借款金额*费率值
                    TuandaiProjectInfo projectInfo = tuandaiProjectInfoMapper.selectById(projExtRate.getProjectId());
                    penalty = projectInfo.getFullBorrowMoney().multiply(projExtRate.getRateValue());
                    
                } else if (PepayPlanProjExtRatCalEnum.BY_REMIND_MONEY.getValue() == projExtRate.getCalcWay()) {
                    //2剩余本金*费率值
                    BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), bizPlanList.getPlanId());
                    penalty = penalty.add(upaid.multiply(projExtRate.getRateValue()));
                } else if (PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue() == projExtRate.getCalcWay()) {
                    //3.1*费率值'
                    penalty = penalty.add(projExtRate.getRateValue());
                } else if (PepayPlanProjExtRatCalEnum.REMIND_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                    //4 剩余的平台服务费合计
                    penalty = penalty.add(repaymentProjPlanListDetailMapper.calcSurplusPlatformFees(bizPlanList.getBusinessId(), projExtRate.getProjectId(), bizPlanList.getPlanId(), bizPlanList.getPeriod()));
                } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_COM_FEE.getValue() == projExtRate.getCalcWay()) {
                    //5 费率值*月收分公司服务费
                    BigDecimal serviceFee = repaymentProjPlanListDetailMapper.calcService(bizPlanList.getBusinessId(), projExtRate.getProjectId(), bizPlanList.getPlanId(), bizPlanList.getPeriod());
                    penalty = penalty.add(projExtRate.getRateValue().multiply(serviceFee));
                } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                    //6 费率值*月收平台服务费
                    BigDecimal platformFee = repaymentProjPlanListDetailMapper.calcPlatFee(bizPlanList.getBusinessId(), projExtRate.getProjectId(), bizPlanList.getPlanId(), bizPlanList.getPeriod());
                    penalty = penalty.add(projExtRate.getRateValue().multiply(platformFee));
                } else if (PepayPlanProjExtRatCalEnum.BY_REM_MONEY_AND_FEE.getValue() == projExtRate.getCalcWay()) {
                    //(剩余本金*费率值*剩余期数) - 分公司服务费违约金 - 平台服务费违约金
                	//剩余本金*费率值*剩余借款期数   得出的结果需与  剩余本金*6%  比较  取小值
                	
                	BasicBusiness business = basicBusinessMapper.selectById(bizPlanList.getBusinessId());
                	int surplusPeriod = business.getBorrowLimit() - bizPlanList.getPeriod() ;
                	
                    BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), bizPlanList.getPlanId());
                    BigDecimal p6 = upaid.multiply(new BigDecimal("0.06")) ;
                    
                    BigDecimal servicePenalty = projExtRateMapper.calcProjextRate(
                            projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid(),bizPlanList.getPeriod().toString());
                    BigDecimal serviceFee = repaymentProjPlanListDetailMapper.calcService(bizPlanList.getBusinessId(), projExtRate.getProjectId(), bizPlanList.getPlanId(), bizPlanList.getPeriod());
                    BigDecimal service = servicePenalty.multiply(serviceFee);
                    
                    
                    BigDecimal platformPenalty = projExtRateMapper.calcProjextRate(
                            projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid(),bizPlanList.getPeriod().toString());
                    BigDecimal platformFee = repaymentProjPlanListDetailMapper.calcPlatFee(bizPlanList.getBusinessId(), projExtRate.getProjectId(), bizPlanList.getPlanId(), bizPlanList.getPeriod());
                    BigDecimal platform = platformPenalty.multiply(platformFee);
                    
                    penalty = (upaid.multiply(projExtRate.getRateValue()).multiply(new BigDecimal(surplusPeriod))) ;
                    		
                    if (penalty.compareTo(p6)>0) {
    					penalty = p6 ;
    				}
                    
                    penalty = (penalty).subtract(service).subtract(platform);
                    
                    if (penalty.compareTo(BigDecimal.ZERO) < 0) {
    					penalty = BigDecimal.ZERO ;
    				}
                    
                    

                } else {
                    logger.error("错误： projExtRate.CalcWay[{}]尚未有对应算法", projExtRate.getCalcWay());
                    throw new ServiceRuntimeException("错误： projExtRate.CalcWay[" + projExtRate.getCalcWay() + "]尚未有对应算法");
                }

                fee.setAmount(penalty.setScale(2, RoundingMode.HALF_UP));
                fee.setFeeId(projExtRate.getFeeId());
                fee.setFeeName(projExtRate.getFeeName());
                fee.setPlanItemName(projExtRate.getRateName());
                fee.setPlanItemType(projExtRate.getRateType().toString());
                fee.setProjectId(projExtRate.getProjectId());
                fee.setPlanListId(bizPlanList.getPlanListId());
                fee.setPlanListDetailId(UUID.randomUUID().toString());
                
                /*将违约金设为线上费用*/
                if (RepayPlanFeeTypeEnum.PRINCIPAL_PENALTY.getUuid().equals(projExtRate.getFeeId())) {
                	/*本金违约金算线下不参与分发*/
                	fee.setShareProfitIndex(Constant.ONLINE_OFFLINE_FEE_BOUNDARY+1);
    			}else if (RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid().equals(projExtRate.getFeeId())) {
    				fee.setShareProfitIndex(Constant.ONLINE_OFFLINE_FEE_BOUNDARY-1);
    			}else if (RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid().equals(projExtRate.getFeeId())) {
    				fee.setShareProfitIndex(Constant.ONLINE_OFFLINE_FEE_BOUNDARY-1);
    			}
                /*将违约金设为线上费用*/
                fees.add(fee);
                infoVO.getPenaltyBiz().get(bizPlanList.getPlanListId()).add(fee);
            }
		}
    	infoVO.setPenaltyFees(fees);
        
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
            if (CollectionUtils.isEmpty(planLists)) {
				throw new ServiceRuntimeException("找不到当前期");
			}
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
     * 更新备注
     * @author 王继光
     * 2018年7月10日 下午3:18:25
     * @param financeBaseDto
     */
    private void updateRemark (FinanceSettleBaseDto financeBaseDto) {
    	RepaymentBizPlanList  bizPlanList = financeBaseDto.getCurrentPeriods().get(0).getCurrBizPlanListDto().getRepaymentBizPlanList();
    	if (!StringUtil.isEmpty(financeBaseDto.getRemark())) {
        	if (StringUtil.isEmpty(bizPlanList.getRemark())) {
        		bizPlanList.setRemark(financeBaseDto.getRemark());
			}else {
				bizPlanList.setRemark(bizPlanList.getRemark().concat("\r\n").concat(financeBaseDto.getRemark()));
			}
		}else {
			
			StringBuffer feeDetails = new StringBuffer();
			BigDecimal factTotalAmount = BigDecimal.ZERO ;
			RepaymentResource lastOne = financeBaseDto.getRepaymentResources().get(financeBaseDto.getRepaymentResources().size()-1);
			StringBuffer repayWay = new StringBuffer();
			String repayDate = DateUtil.formatDate(lastOne.getRepayDate());
            
			switch (lastOne.getRepaySource()) {
			case "10":
			case "11":
				repayWay.append("线下还款");
				break;
			case "20":
				repayWay.append("自动线下代扣");
				WithholdingRepaymentLog log = withholdingRepaymentLogService.selectById(lastOne.getRepaySourceRefId());
				if (log.getBindPlatformId().equals(0)) {
					repayWay.append("(易宝代扣)");
				}
				if (log.getBindPlatformId().equals(0)) {
					repayWay.append("(宝付代扣)");
				}
				break;
			case "21":
				repayWay.append("人工线下代扣");
				WithholdingRepaymentLog log1 = withholdingRepaymentLogService.selectById(lastOne.getRepaySourceRefId());
				if (log1.getBindPlatformId().equals(0)) {
					repayWay.append("(易宝代扣)");
				}
				if (log1.getBindPlatformId().equals(0)) {
					repayWay.append("(宝付代扣)");
				}
				break;
			case "30":
				repayWay.append("自动银行代扣");
				break;
			case "31":
				repayWay.append("人工银行代扣");
				break;
			default:
				break;
			}

			
			for (RepaymentBizPlanSettleDto bizPlanSettleDto : financeBaseDto.getCurrentPeriods()) {
				String planId = bizPlanSettleDto.getRepaymentBizPlan().getPlanId();
				for (RepaymentBizPlanListDetail planListDetail : bizPlanSettleDto.getCurrBizPlanListDto().getBizPlanListDetails() ) {
					if (financeBaseDto.getProjFactRepays().containsKey(planId)) {
						if (financeBaseDto.getProjFactRepays().get(planId).containsKey(planListDetail.getPlanDetailId())) {
							List<RepaymentProjFactRepay> list = financeBaseDto.getProjFactRepays().get(planId).get(planListDetail.getPlanDetailId());
							if (list != null && !list.isEmpty()) {
								/*优化同一个费用项在备注里出现2次*/
								List<RepaymentProjFactRepay> newList = new ArrayList<>() ;
								for (RepaymentProjFactRepay repaymentProjFactRepay : list) {
									boolean existSameFee = false ;
									for (RepaymentProjFactRepay repaymentProjFactRepay2 : newList) {
										if (repaymentProjFactRepay2.getFeeId().equals(repaymentProjFactRepay.getFeeId())) {
											existSameFee = true ;
											repaymentProjFactRepay2.setFactAmount(repaymentProjFactRepay2.getFactAmount().add(repaymentProjFactRepay.getFactAmount()));
										}
									}
									if (!existSameFee) {
										newList.add(repaymentProjFactRepay);
									}
								}
								/*优化同一个费用项在备注里出现2次*/
								
								BigDecimal factAmount = BigDecimal.ZERO;
								for (RepaymentProjFactRepay repaymentProjFactRepay : newList) {
									factAmount = repaymentProjFactRepay.getFactAmount().add(factAmount);
								}
								feeDetails.append(factAmount.setScale(2, RoundingMode.HALF_UP));
								if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
									feeDetails.append("线上滞纳金").append(" ");
								}else if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
									feeDetails.append("线下滞纳金").append(" ");
								}else {
									feeDetails.append(planListDetail.getPlanItemName()).append(" ");
								}
								factTotalAmount = factTotalAmount.add(factAmount);
								continue;
							}
							
						}
					}
					
					feeDetails.append(BigDecimal.ZERO.setScale(2));
					if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
						feeDetails.append("线上滞纳金").append(" ");
					}else if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
						feeDetails.append("线下滞纳金").append(" ");
					}else {
						feeDetails.append(planListDetail.getPlanItemName()).append(" ");
					}
				}
			}
			
			//{0}=日期,{1}=扣款方式,{2}=扣款总额,{3}=明细
			String remark = MessageFormat.format("{0}{1}{2}元,费用明细:{3}",repayDate,repayWay.toString(),factTotalAmount.setScale(2, RoundingMode.HALF_UP),feeDetails.toString());
			if (StringUtil.isEmpty(bizPlanList.getRemark())) {
        		bizPlanList.setRemark(remark);
			}else {
				bizPlanList.setRemark(bizPlanList.getRemark().concat("\r\n").concat(remark));
			}
		}
    	
    	bizPlanList.updateAllColumnById();
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
//                            .ge("due_date", new Date())
                            .and(" DATE(due_date) >= DATE({0}) ", new Date())
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

    private List<RepaymentBizPlanList> getCurrenPeroids(FinanceSettleReq req) {
    	List<RepaymentBizPlanList> cuRepaymentBizPlanLists = new ArrayList<>();
    	EntityWrapper<RepaymentBizPlan> ew = new EntityWrapper<RepaymentBizPlan>();
		ew.eq("business_id", req.getBusinessId());
		//如果传了还款计划Id，则使用还款计划Id来查业务的还款计划
		if (!StringUtil.isEmpty(req.getPlanId())) {
			ew.eq("plan_id", req.getPlanId());
		}

		List<RepaymentBizPlan> plan = repaymentBizPlanMapper.selectList(ew);
		List<RepaymentBizPlanSettleDto> res = new ArrayList<>() ;
		
		//结清日期
        Date  settleDate = new Date();
        
        EntityWrapper<RepaymentBizPlanList> ew1 = new EntityWrapper<>();
		ew1.eq("business_id", req.getBusinessId());
		if (!StringUtil.isEmpty(req.getPlanId())) {
			ew1.eq("plan_id",req.getPlanId());
		}
		ew1.orderBy("due_date",false);
		/*找最后一期*/
		RepaymentBizPlanList finalPeriod = repaymentBizPlanListMapper.selectList(ew1).get(0);
		
		//根据查出的还款计划列表找业务还款计划的当前期
		for (RepaymentBizPlan repaymentBizPlan : plan) {
			// 找出这个还款计划的期数列表
			List<RepaymentBizPlanList> BizPlanLists = repaymentBizPlanListMapper
					.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", req.getBusinessId())
							.eq("plan_id", repaymentBizPlan.getPlanId()).orderBy("due_date", false));

			// 找应还日期离当前日期最近且为还款中的期数作为当前期
			List<RepaymentBizPlanList> selectList = repaymentBizPlanListMapper
					.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", req.getBusinessId())
							.eq("plan_id", repaymentBizPlan.getPlanId()).and(" DATE(due_date) >= DATE({0}) ", settleDate)
							.eq("current_status", RepayCurrentStatusEnums.还款中.toString()).orderBy("due_date",false));
			// 判断当前期列表是否为空
			if (CollectionUtils.isEmpty(selectList)) {
				// 找不到还款中的当前期则判断结清日期是否大过还款计划最后一次还款的期限

				RepaymentBizPlanList lastBizPlanList = BizPlanLists.get(0);
				// 如果最后一期的应还时间小于当前的结清时间
				if (DateUtil.getDiff(lastBizPlanList.getDueDate(), settleDate) <= 0) {
					// 且为未还款
					if (!lastBizPlanList.getCurrentStatus().equals(RepayCurrentStatusEnums.已还款.toString())) {
						selectList = new LinkedList<RepaymentBizPlanList>();
						selectList.add(lastBizPlanList);
					}
				}
			}
			// 再次判断当前期列表是否为空
			if (CollectionUtils.isEmpty(selectList)) {
				if (!StringUtil.isEmpty(req.getPlanId())) {
					logger.error("找不到此业务还款计划的当前期 RepaymentBizPlanList  businessId:" + req.getBusinessId() + "     planId:"
							+ repaymentBizPlan.getPlanId());
					throw new SettleRepaymentExcepiton("找不到此业务还款计划的当前期",
							ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
				} else {
					continue;
				}
			}

			// 业务还款计划当前期列表
			
			cuRepaymentBizPlanLists.add(selectList.get(0));
		}
		return cuRepaymentBizPlanLists;
    }
    
    /**
     * 取还款计划的当前期
     * @param req
     * @return
     */
	@Override
	public List<RepaymentBizPlanSettleDto> getCurrentPeriod(FinanceSettleReq req,FinanceSettleBaseDto financeSettleBaseDto) {
		EntityWrapper<RepaymentBizPlan> ew = new EntityWrapper<RepaymentBizPlan>();
		ew.eq("business_id", req.getBusinessId());
		//如果传了还款计划Id，则使用还款计划Id来查业务的还款计划
		if (!StringUtil.isEmpty(req.getPlanId())) {
			ew.eq("plan_id", req.getPlanId());
		}

		List<RepaymentBizPlan> plan = repaymentBizPlanMapper.selectList(ew);
		List<RepaymentBizPlanSettleDto> res = new ArrayList<>() ;
		
		//结清日期
        Date  settleDate = new Date();
        
        EntityWrapper<RepaymentBizPlanList> ew1 = new EntityWrapper<>();
		ew1.eq("business_id", req.getBusinessId());
		if (!StringUtil.isEmpty(req.getPlanId())) {
			ew1.eq("plan_id",req.getPlanId());
		}
		ew1.orderBy("due_date",false);
		/*找最后一期*/
		RepaymentBizPlanList finalPeriod = repaymentBizPlanListMapper.selectList(ew1).get(0);
		/*判断是否提前结清*/
		if (DateUtil.getDiffDays(settleDate, finalPeriod.getDueDate())>0) {
			financeSettleBaseDto.setPreSettle(true);
		}
		
		//根据查出的还款计划列表找业务还款计划的当前期
    	for (RepaymentBizPlan repaymentBizPlan : plan) {
            //找出这个还款计划的期数列表
            List<RepaymentBizPlanList> BizPlanLists = repaymentBizPlanListMapper.selectList(
                    new EntityWrapper<RepaymentBizPlanList>()
                            .eq("business_id", req.getBusinessId())
                            .eq("plan_id", repaymentBizPlan.getPlanId())
                            .orderBy("due_date",false));

    	    //找应还日期离当前日期最近且为还款中的期数作为当前期
    		List<RepaymentBizPlanList> selectList = repaymentBizPlanListMapper.selectList(
					new EntityWrapper<RepaymentBizPlanList>()
					.eq("business_id", req.getBusinessId())
					.eq("plan_id", repaymentBizPlan.getPlanId())
//					.ge("due_date", settleDate)
					.and(" DATE(due_date) >= DATE({0}) ",settleDate)
					.eq("current_status", RepayCurrentStatusEnums.还款中.toString())
					.orderBy("due_date"));
    		//判断当前期列表是否为空
			if (CollectionUtils.isEmpty(selectList)) {
			    //找不到还款中的当前期则判断结清日期是否大过还款计划最后一次还款的期限

                RepaymentBizPlanList  lastBizPlanList =  BizPlanLists.get(0);
                //如果最后一期的应还时间小于当前的结清时间
                if(DateUtil.getDiff(lastBizPlanList.getDueDate(),settleDate) <=0){
                    //且为未还款
                    if(!lastBizPlanList.getCurrentStatus().equals(RepayCurrentStatusEnums.已还款.toString())){
                        selectList = new LinkedList<RepaymentBizPlanList>();
                        selectList.add(lastBizPlanList);
                    }
                }
			}
			//再次判断当前期列表是否为空
            if (CollectionUtils.isEmpty(selectList)) {
                if (!StringUtil.isEmpty(req.getPlanId())) {
                    logger.error("找此业务还款计划的当前期 RepaymentBizPlanList  businessId:"+req.getBusinessId()+"     planId:"+repaymentBizPlan.getPlanId());
                    throw new SettleRepaymentExcepiton("找此业务还款计划的当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                }else{
                    continue;
                }
            }

            /*备份RepaymentBizPlanBak*/
            RepaymentBizPlanBak planBak = new RepaymentBizPlanBak();
            BeanUtils.copyProperties(repaymentBizPlan, planBak);
            planBak.setConfirmLogId(financeSettleBaseDto.getUuid());
            financeSettleBaseDto.getRepaymentBizPlanBaks().add(planBak);
            
			//业务还款计划当前期列表
			List<RepaymentBizPlanList> cuRepaymentBizPlanLists = new ArrayList<>() ;
			cuRepaymentBizPlanLists.add(selectList.get(0)) ;

			//业务还款计划Dto
    		RepaymentBizPlanSettleDto planDto = new RepaymentBizPlanSettleDto() ;
			planDto.setRepaymentBizPlan(repaymentBizPlan);




			for (RepaymentBizPlanList curRepaymentBizPlanList : cuRepaymentBizPlanLists) {
				
				/*备份RepaymentBizPlanListBak*/
				RepaymentBizPlanListBak planListBak = new RepaymentBizPlanListBak() ;
				BeanUtils.copyProperties(curRepaymentBizPlanList, planListBak);
				planListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
				financeSettleBaseDto.getRepaymentBizPlanListBaks().add(planListBak);
				
				RepaymentBizPlanListDto planListDto = new RepaymentBizPlanListDto() ;
				planListDto.setRepaymentBizPlanList(curRepaymentBizPlanList);
				List<RepaymentBizPlanListDetail> planListDetails = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", curRepaymentBizPlanList.getPlanListId())) ;

				for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : planListDetails) {
					/*备份RepaymentBizPlanListDetail*/
					RepaymentBizPlanListDetailBak planListDetailBak = new RepaymentBizPlanListDetailBak();
					BeanUtils.copyProperties(repaymentBizPlanListDetail, planListDetailBak);
					planListDetailBak.setConfirmLogId(financeSettleBaseDto.getUuid());
					financeSettleBaseDto.getRepaymentBizPlanListDetailBaks().add(planListDetailBak);
				}
				
				planListDto.setBizPlanListDetails(planListDetails);

				//业务还款计划，当前期设置业务还款计划列表（应该有一个字段专门存储业务还款计划的当前期）
                planDto.setCurrBizPlanListDto(planListDto);


                List<RepaymentBizPlanListDto> beforePlanListDtos = new LinkedList<>();

                List<RepaymentBizPlanListDto> afterPlanListDtos = new LinkedList<>();


                for(RepaymentBizPlanList  bizPlanList:BizPlanLists){


                    RepaymentBizPlanListDto dto = new RepaymentBizPlanListDto() ;
                    dto.setRepaymentBizPlanList(bizPlanList);
                    List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", bizPlanList.getPlanListId())) ;

                    dto.setBizPlanListDetails(details);

                    //如果是当前期，则不存储
                    if(bizPlanList.getPeriod() == curRepaymentBizPlanList.getPeriod()){
                        continue;
                    }
                    if(bizPlanList.getPeriod()<curRepaymentBizPlanList.getPeriod() && bizPlanList.getPlanId().equals(curRepaymentBizPlanList.getPlanId())){
                        beforePlanListDtos.add(dto);
                    }else if(bizPlanList.getPeriod()>curRepaymentBizPlanList.getPeriod() && bizPlanList.getPlanId().equals(curRepaymentBizPlanList.getPlanId())){
                        afterPlanListDtos.add(dto);
                        
                        /*顺便备份结清期往后的期数*/
                        RepaymentBizPlanListBak afterPlanListBak = new RepaymentBizPlanListBak() ;
        				BeanUtils.copyProperties(dto.getRepaymentBizPlanList(), afterPlanListBak);
        				planListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
        				financeSettleBaseDto.getRepaymentBizPlanListBaks().add(afterPlanListBak);
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
				/*备份RepaymentProjPlan*/
				RepaymentProjPlanBak projPlanBak = new RepaymentProjPlanBak();
				BeanUtils.copyProperties(repaymentProjPlan, projPlanBak);
				projPlanBak.setConfirmLogId(financeSettleBaseDto.getUuid());
				financeSettleBaseDto.getRepaymentProjPlanBaks().add(projPlanBak);
				
				
				RepaymentProjPlanSettleDto projPlanDto = new RepaymentProjPlanSettleDto() ;
				projPlanDto.setRepaymentProjPlan(repaymentProjPlan);
				TuandaiProjectInfo projectInfo = tuandaiProjectInfoMapper.selectById(repaymentProjPlan.getProjectId());
				projPlanDto.setTuandaiProjectInfo(projectInfo);
				
				CurrPeriodProjDetailVO periodProjDetailVO = new CurrPeriodProjDetailVO() ;
				periodProjDetailVO.setProject(projectInfo.getProjectId());
				periodProjDetailVO.setMaster(projectInfo.getProjectId().equals(projectInfo.getMasterIssueId()));
				periodProjDetailVO.setUserName(projectInfo.getRealName());
				periodProjDetailVO.setQueryFullSuccessDate(projectInfo.getQueryFullSuccessDate());
				periodProjDetailVO.setProjAmount(projectInfo.getFullBorrowMoney());
				
				if (CollectionUtils.isEmpty(financeSettleBaseDto.getCurrPeriodProjDetailVOList())) {
					financeSettleBaseDto.setCurrPeriodProjDetailVOList(new ArrayList<>());
				}
				financeSettleBaseDto.getCurrPeriodProjDetailVOList().add(periodProjDetailVO);
				if (financeSettleBaseDto.getWebFactRepays()==null) {
					financeSettleBaseDto.setWebFactRepays(new HashMap<>());
				}
				if (!financeSettleBaseDto.getWebFactRepays().containsKey(projectInfo.getProjectId())) {
					financeSettleBaseDto.getWebFactRepays().put(repaymentProjPlan.getProjPlanId(), periodProjDetailVO);
				}
				
				
				//取标的还款计划当前期
				List<RepaymentProjPlanList> curprojPlanLists = repaymentProjPlanListMapper.selectList(
						new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId()).eq("plan_list_id", cuRepaymentBizPlanLists.get(0).getPlanListId()));

				//如果找不到这个标的还款计划当前期
				if(CollectionUtils.isEmpty(selectList)){
                    logger.error("找不到这个标的还款计划当前期 RepaymentProjPlanList  proj_plan_id:"+repaymentProjPlan.getProjPlanId()+"     plan_list_id:"+cuRepaymentBizPlanLists.get(0).getPlanListId());
                    throw new SettleRepaymentExcepiton("找不到这个标的还款计划当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                }else if(cuRepaymentBizPlanLists.size()>1){
                    logger.error("这个标有两个以上当前期 RepaymentProjPlanList  proj_plan_id:"+repaymentProjPlan.getProjPlanId()+"     plan_list_id:"+cuRepaymentBizPlanLists.get(0).getPlanListId());
                    throw new SettleRepaymentExcepiton("这个标有两个以上当前期", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
                }

                RepaymentProjPlanList curRepayProjPlanList = curprojPlanLists.get(0);
                RepaymentProjPlanListDto curProjPlanListDto = creatProjPlanListDto(curRepayProjPlanList,financeSettleBaseDto);
                projPlanDto.setCurrProjPlanListDto(curProjPlanListDto);

                //取标的还款计划所有期
                List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListMapper.selectList(
                        new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
                        .orderBy("due_date"));


                List<RepaymentProjPlanListDto>  beforeProjPlanListDtos = new LinkedList<>();
                List<RepaymentProjPlanListDto> afterProjPlanListDtos = new LinkedList<>();

				for (RepaymentProjPlanList repaymentProjPlanList : projPlanLists) {
					
					/*备份RepaymentProjPlanList*/
					RepaymentProjPlanListBak projPlanListBak = new RepaymentProjPlanListBak();
					BeanUtils.copyProperties(repaymentProjPlanList, projPlanListBak);
					projPlanListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
					financeSettleBaseDto.getRepaymentProjPlanListBaks().add(projPlanListBak);
					
                    RepaymentProjPlanListDto projPlanListDto = creatProjPlanListDto(repaymentProjPlanList,financeSettleBaseDto);
                    projPlanDto.setProjPlanListDtos(projPlanListDto);
                    if(repaymentProjPlanList.getPeriod()>curRepayProjPlanList.getPeriod() && repaymentProjPlanList.getProjPlanId().equals(curRepayProjPlanList.getProjPlanId())){
                        afterProjPlanListDtos.add(projPlanListDto);
                        
                        /*备份结清期往后的标的期数*/
                        RepaymentProjPlanListBak afterProjPlanListBak = new RepaymentProjPlanListBak() ;
        				BeanUtils.copyProperties(projPlanListDto.getRepaymentProjPlanList(), afterProjPlanListBak);
        				afterProjPlanListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                        financeSettleBaseDto.getRepaymentProjPlanListBaks().add(afterProjPlanListBak);
                        
                    }else if(repaymentProjPlanList.getPeriod()<curRepayProjPlanList.getPeriod() && repaymentProjPlanList.getProjPlanId().equals(curRepayProjPlanList.getProjPlanId())){
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
        for(RepaymentBizPlanSettleDto  bizPlanDto:res){

            List<RepaymentProjPlanSettleDto> projPlanDtos = bizPlanDto.getProjPlanStteleDtos();

            for(RepaymentProjPlanSettleDto repaymentProjPlanDto:projPlanDtos){

                RepaymentProjPlanListDto repaymentProjPlanListDto = repaymentProjPlanDto.getCurrProjPlanListDto();
                List<RepaymentProjPlanListDto> curRepaymentProjPlanListDtos = new LinkedList<>();
                curRepaymentProjPlanListDtos.add(repaymentProjPlanListDto);
                //当期结清，应支付的费用明细
                Map<String,PlanListDetailShowPayDto> curShowPayMoney = calcShowPayFeels(curRepaymentProjPlanListDtos,null);
                repaymentProjPlanDto.setCurShowPayFeels(curShowPayMoney);

                for (RepaymentProjPlanListDetail detail : repaymentProjPlanDto.getCurrProjPlanListDto().getProjPlanListDetails()) {
                	if (repaymentProjPlanDto.getCurProjListDetailMap()==null) {
						repaymentProjPlanDto.setCurProjListDetailMap(new HashMap<>());
					}
                	if (!repaymentProjPlanDto.getCurProjListDetailMap().containsKey(detail.getFeeId())) {
                		repaymentProjPlanDto.getCurProjListDetailMap().put(detail.getFeeId(), detail);
					}
				}

                //当前期之前期数费用的统计
                 Map<String,PlanListDetailShowPayDto> beforeFeels = calcShowPayFeels(repaymentProjPlanDto.getBeforeProjPlanListDtos(),RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
                repaymentProjPlanDto.setBeforeFeels(beforeFeels);

                //当前期之后期数费用的统计(只累加本金)
                Map<String,PlanListDetailShowPayDto> afterFeels =  calcShowPayFeels(repaymentProjPlanDto.getAfterProjPlanListDtos(),RepayPlanFeeTypeEnum.PRINCIPAL.getUuid());
                repaymentProjPlanDto.setAfterFeels(afterFeels);

                //之前期应还费用累加
                for(PlanListDetailShowPayDto planListDetailShowPayDto:beforeFeels.values()){
                    PlanListDetailShowPayDto showPayDto =  curShowPayMoney.get(planListDetailShowPayDto.getFeelId());
                    addToCurShowPay(showPayDto,planListDetailShowPayDto,curShowPayMoney);
                }

                //之后期应还费用累加
                for(PlanListDetailShowPayDto planListDetailShowPayDto:afterFeels.values()){
                    PlanListDetailShowPayDto showPayDto =  curShowPayMoney.get(planListDetailShowPayDto.getFeelId());
                    addToCurShowPay(showPayDto,planListDetailShowPayDto,curShowPayMoney);
                }


            }
        }


		return res;
	}


	private  void  addToCurShowPay(PlanListDetailShowPayDto showPayDto,PlanListDetailShowPayDto planListDetailShowPayDto,Map<String,PlanListDetailShowPayDto> curShowPayMoney){
        if(showPayDto == null){
            showPayDto = new PlanListDetailShowPayDto();
            BeanUtils.copyProperties(planListDetailShowPayDto,showPayDto);
            curShowPayMoney.put(showPayDto.getFeelId(),showPayDto);
        }else{
            showPayDto.setShowPayMoney(showPayDto.getShowPayMoney().add(planListDetailShowPayDto.getShowPayMoney()));
        }
    }




	private  Map<String,PlanListDetailShowPayDto>  calcShowPayFeels(List<RepaymentProjPlanListDto> projPlanLists,String feelId){
        //当前期之前期数费用的统计
        Map<String,PlanListDetailShowPayDto> beforeFeels = new HashMap<>();
        for(RepaymentProjPlanListDto bforePlanDto: projPlanLists){
             calcShowPayFeel(bforePlanDto,feelId,beforeFeels);
        }


	    return beforeFeels;
    }

    private void calcShowPayFeel(RepaymentProjPlanListDto bforePlanDto,String feelId,Map<String,PlanListDetailShowPayDto> beforeFeels){
        List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos =bforePlanDto.getRepaymentProjPlanListDetailDtos();
        for(RepaymentProjPlanListDetailDto projPlanListDetailDto: repaymentProjPlanListDetailDtos){
            RepaymentProjPlanListDetail  detail1 =projPlanListDetailDto.getRepaymentProjPlanListDetail();
            //如果指定了需累加的费用项，则其他费用项都跳过
            if(feelId!=null && !feelId.equals(detail1.getFeeId())){
                continue;
            }

            List<RepaymentProjFactRepay> factRepayList = projPlanListDetailDto.getRepaymentProjFactRepays();
            if(!CollectionUtils.isEmpty(factRepayList)){
                BigDecimal  payedMoney = new BigDecimal("0");
                for(RepaymentProjFactRepay repaymentProjFactRepay: factRepayList){
                    payedMoney =payedMoney.add(repaymentProjFactRepay.getFactAmount());
                }

                BigDecimal  showPayMoney = detail1.getProjPlanAmount().subtract(payedMoney);
                if(showPayMoney.compareTo(new BigDecimal("0"))>0){
                    PlanListDetailShowPayDto  planListDetailShowPayDto = beforeFeels.get(detail1.getFeeId());
                    if(planListDetailShowPayDto == null){
                        planListDetailShowPayDto = new PlanListDetailShowPayDto();
                        planListDetailShowPayDto.setFeelId(detail1.getFeeId());
                        planListDetailShowPayDto.setFeelName(detail1.getPlanItemName());
                        planListDetailShowPayDto.setShareProfitIndex(detail1.getShareProfitIndex());
                        planListDetailShowPayDto.setShowPayMoney(new BigDecimal("0"));
                        beforeFeels.put(detail1.getFeeId(),planListDetailShowPayDto);
                    }
                    planListDetailShowPayDto.setShowPayMoney(planListDetailShowPayDto.getShowPayMoney().add(showPayMoney));
                }
            }else {
            	PlanListDetailShowPayDto  planListDetailShowPayDto = beforeFeels.get(detail1.getFeeId());
                if(planListDetailShowPayDto == null){
                    planListDetailShowPayDto = new PlanListDetailShowPayDto();
                    planListDetailShowPayDto.setFeelId(detail1.getFeeId());
                    planListDetailShowPayDto.setFeelName(detail1.getPlanItemName());
                    planListDetailShowPayDto.setShareProfitIndex(detail1.getShareProfitIndex());
                    planListDetailShowPayDto.setShowPayMoney(new BigDecimal("0"));
                    beforeFeels.put(detail1.getFeeId(),planListDetailShowPayDto);
                }
                planListDetailShowPayDto.setShowPayMoney(planListDetailShowPayDto.getShowPayMoney().add(detail1.getProjPlanAmount()));
			}
        }
    }

	//showPayFeels =



    private  RepaymentProjPlanListDto   creatProjPlanListDto(RepaymentProjPlanList repaymentProjPlanList,FinanceSettleBaseDto financeSettleBaseDto){
        RepaymentProjPlanListDto projPlanListDto = new RepaymentProjPlanListDto() ;
        projPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);

        List<RepaymentProjPlanListDetail> projPlanListDetails = repaymentProjPlanListDetailMapper.selectList(
                new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId()));

        projPlanListDto.setProjPlanListDetails(projPlanListDetails);


        List<RepaymentProjPlanListDetailDto>  repaymentProjPlanListDetailDtos = new LinkedList<>();
        for(RepaymentProjPlanListDetail projPlanListDetail:  projPlanListDetails ){
        	/*备份RepaymentProjPlanListDetail*/
        	RepaymentProjPlanListDetailBak projPlanListDetailBak = new RepaymentProjPlanListDetailBak() ;
        	BeanUtils.copyProperties(projPlanListDetail, projPlanListDetailBak);
        	projPlanListDetailBak.setConfirmLogId(financeSettleBaseDto.getUuid());
        	financeSettleBaseDto.getRepaymentProjPlanListDetailBaks().add(projPlanListDetailBak);
        	
            RepaymentProjPlanListDetailDto  projPlanListDetailDto = new RepaymentProjPlanListDetailDto();
            repaymentProjPlanListDetailDtos.add(projPlanListDetailDto);
            projPlanListDetailDto.setRepaymentProjPlanListDetail(projPlanListDetail);

            List<RepaymentProjFactRepay>  factRepayList = repaymentProjFactRepayMapper.selectList(
                    new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_detail_id",projPlanListDetail.getProjPlanDetailId())
            );
            projPlanListDetailDto.setRepaymentProjFactRepays(factRepayList);
        }
        projPlanListDto.setRepaymentProjPlanListDetailDtos(repaymentProjPlanListDetailDtos);

        return projPlanListDto;
    }
    

}
