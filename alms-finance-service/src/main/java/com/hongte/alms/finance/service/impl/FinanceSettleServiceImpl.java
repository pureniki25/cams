package com.hongte.alms.finance.service.impl;

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
						bak.insert();
					}
					for (RepaymentBizPlanListBak bak : financeSettleBaseDto.getRepaymentBizPlanListBaks()) {
						bak.insert();
					}
					for (RepaymentBizPlanListDetailBak bak : financeSettleBaseDto.getRepaymentBizPlanListDetailBaks()) {
						bak.insert();
					}
					for (RepaymentProjPlanBak bak : financeSettleBaseDto.getRepaymentProjPlanBaks()) {
						bak.insert();
					}
					for (RepaymentProjPlanListBak bak : financeSettleBaseDto.getRepaymentProjPlanListBaks()) {
						bak.insert();
					}
					for (RepaymentProjPlanListDetailBak bak : financeSettleBaseDto.getRepaymentProjPlanListDetailBaks()) {
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
									if (bizPlanListDetail.getFeeId().equals(entry.getKey())) {
										if (bizPlanListDetail.getFactAmount()==null) {
											bizPlanListDetail.setFactAmount(BigDecimal.ZERO);
										}
										bizPlanListDetail.setFactAmount(bizPlanListDetail.getFactAmount().add(entry.getValue()));
									}
								}
							}
							
							
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
								bizPlanListDetail.updateAllColumnById();
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

//            } 
            
//            else {
//                List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planId).orderBy("period", true));
//
//                Integer period = repaymentBizPlanList.getPeriod();
//                String currentStatus = repaymentBizPlanList.getCurrentStatus();
//                if( RepayCurrentStatusEnums.逾期.toString().equals(currentStatus) && period.intValue() !=repaymentBizPlanLists.size()-1){
//                    throw new ServiceRuntimeException("当前逾期不能进行还款计划结清!");
//                }
//                //单个还款计划结清
////                RepaymentBizPlan repaymentBizPlan = bizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId).eq("plan_id", planId));
//
//                makeRepaymentPlanOnePlan(financeSettleBaseDto, repaymentBizPlanList, financeSettleReq);
//            }

        } else {
            logger.error("找不到对应的还款计划列表repaymentBizPlanList  businessId:" + businessId + "     after_id:" + afterId);
            throw new SettleRepaymentExcepiton("找不到对应的还款计划列表", ExceptionCodeEnum.NO_BIZ_PLAN_LIST.getValue().toString());
        }

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
		} else if (pfact.compareTo(pOnlinePlanAmount) >= 0) {
			repaymentProjPlanSettleDto.getCurrProjPlanListDto().getRepaymentProjPlanList()
					.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
		} else if (pfact.compareTo(pAllPlanAmount) >= 0) {
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
                //合并报错，临时注释
//                updateBizPlan(projMoneyMap, financeSettleBaseDto, allMoney, planIdNow, afterIdNow);

                updateMoneyPoolRepayment(financeSettleReq);

                //调用标的合规和还款接口 标的list
                repayLog(financeSettleBaseDto);

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
                MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(id);

                String moneyPoolId = moneyPoolRepayment.getMoneyPoolId();

                moneyPoolRepayment.setLastState(moneyPoolRepayment.getState());
                moneyPoolRepayment.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
                moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);

                MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolId);
                moneyPool.setLastFinanceStatus(moneyPool.getFinanceStatus());
                moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.财务确认已还款.toString());
                moneyPoolMapper.updateById(moneyPool);

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
                repaymentSettleMoneyDto.setFeeName(feeName);

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
                repaymentSettleMoneyDto.setFeeName(feeName);
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
            			mpr.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
            			
            			MoneyPool moneyPool = moneyPoolMapper.selectById(mpr.getMoneyPoolId());
            			moneyPool.setLastStatus(moneyPool.getStatus());
            			moneyPool.setLastFinanceStatus(moneyPool.getFinanceStatus());
            			moneyPool.setStatus(RepayRegisterState.完成.toString());
            			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.财务确认已还款.toString());
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
        	if (detail.getProjPlanDetailId()!=null) {
        		detail.updateAllColumnById();
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
    private List<SettleFeesVO> calcPenalty(RepaymentBizPlanList bizPlanList, final String planId) {
        EntityWrapper<RepaymentProjPlanList> eWrapper = new EntityWrapper<RepaymentProjPlanList>();
        eWrapper.eq("period", bizPlanList.getPeriod());
        eWrapper.eq("business_id", bizPlanList.getBusinessId());
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
                penalty = projectInfo.getFullBorrowMoney().multiply(projExtRate.getRateValue());
                
            } else if (PepayPlanProjExtRatCalEnum.BY_REMIND_MONEY.getValue() == projExtRate.getCalcWay()) {
                //2剩余本金*费率值
                BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
                penalty = penalty.add(upaid.multiply(projExtRate.getRateValue()));
            } else if (PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue() == projExtRate.getCalcWay()) {
                //3.1*费率值'
                penalty = penalty.add(projExtRate.getRateValue());
            } else if (PepayPlanProjExtRatCalEnum.REMIND_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                //4 剩余的平台服务费合计
                penalty = penalty.add(repaymentProjPlanListDetailMapper.calcSurplusPlatformFees(bizPlanList.getBusinessId(), projExtRate.getProjectId(), planId, bizPlanList.getPeriod()));
            } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_COM_FEE.getValue() == projExtRate.getCalcWay()) {
                //5 费率值*月收分公司服务费
                BigDecimal serviceFee = repaymentProjPlanListDetailMapper.calcService(bizPlanList.getBusinessId(), projExtRate.getProjectId(), planId, bizPlanList.getPeriod());
                penalty = penalty.add(projExtRate.getRateValue().multiply(serviceFee));
            } else if (PepayPlanProjExtRatCalEnum.BY_MONTH_PLAT_FEE.getValue() == projExtRate.getCalcWay()) {
                //6 费率值*月收平台服务费
                BigDecimal platformFee = repaymentProjPlanListDetailMapper.calcPlatFee(bizPlanList.getBusinessId(), projExtRate.getProjectId(), planId, bizPlanList.getPeriod());
                penalty = penalty.add(projExtRate.getRateValue().multiply(platformFee));
            } else if (PepayPlanProjExtRatCalEnum.BY_REM_MONEY_AND_FEE.getValue() == projExtRate.getCalcWay()) {
                //(剩余本金*费率值*剩余期数) - 分公司服务费违约金 - 平台服务费违约金
            	//剩余本金*费率值*剩余借款期数   得出的结果需与  剩余本金*6%  比较  取小值
            	
            	BasicBusiness business = basicBusinessMapper.selectById(bizPlanList.getBusinessId());
            	int surplusPeriod = business.getBorrowLimit() - bizPlanList.getPeriod() ;
            	
                BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
                BigDecimal p6 = upaid.multiply(new BigDecimal("0.06")) ;
                
                BigDecimal servicePenalty = projExtRateMapper.calcProjextRate(
                        projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid(),bizPlanList.getPeriod().toString());
                BigDecimal serviceFee = repaymentProjPlanListDetailMapper.calcService(bizPlanList.getBusinessId(), projExtRate.getProjectId(), planId, bizPlanList.getPeriod());
                BigDecimal service = servicePenalty.multiply(serviceFee);
                
                
                BigDecimal platformPenalty = projExtRateMapper.calcProjextRate(
                        projExtRate.getProjectId(), RepayPlanFeeTypeEnum.PENALTY_AMONT.getValue().toString(), RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid(),bizPlanList.getPeriod().toString());
                BigDecimal platformFee = repaymentProjPlanListDetailMapper.calcPlatFee(bizPlanList.getBusinessId(), projExtRate.getProjectId(), planId, bizPlanList.getPeriod());
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
            /*将违约金设为线上费用*/
            fee.setShareProfitIndex(Constant.ONLINE_OFFLINE_FEE_BOUNDARY-1);
            /*将违约金设为线上费用*/
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
					.gt("due_date", settleDate)
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
                    if(bizPlanList.getPeriod()<curRepaymentBizPlanList.getPeriod()){
                        beforePlanListDtos.add(dto);
                    }else if(bizPlanList.getPeriod()>curRepaymentBizPlanList.getPeriod()){
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
                    if(repaymentProjPlanList.getPeriod()>curRepayProjPlanList.getPeriod()){
                        afterProjPlanListDtos.add(projPlanListDto);
                        
                        /*备份结清期往后的标的期数*/
                        RepaymentProjPlanListBak afterProjPlanListBak = new RepaymentProjPlanListBak() ;
        				BeanUtils.copyProperties(projPlanListDto.getRepaymentProjPlanList(), afterProjPlanListBak);
        				afterProjPlanListBak.setConfirmLogId(financeSettleBaseDto.getUuid());
                        financeSettleBaseDto.getRepaymentProjPlanListBaks().add(afterProjPlanListBak);
                        
                    }else if(repaymentProjPlanList.getPeriod()<curRepayProjPlanList.getPeriod()){
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
    

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
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

        log.deleteById();
    }
}
