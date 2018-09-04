package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.AlmsOpenServiceFeignClient;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.mapper.*;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.util.ProjPlanDtoUtil;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.FinanceBaseDto;
import com.hongte.alms.finance.service.FinanceService;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author 王继光 2018年5月24日 下午2:46:52
 */
@Service("ShareProfitService")
public class ShareProfitServiceImpl implements ShareProfitService {
    private static Logger logger = LoggerFactory.getLogger(ShareProfitServiceImpl.class);
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
    MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;
    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;


    @Autowired
    @Qualifier("RepaymentConfirmPlatRepayLogService")
    RepaymentConfirmPlatRepayLogService repaymentConfirmPlatRepayLogService;

    @Autowired
    Executor executor;

    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    RepaymentConfirmLogService repaymentConfirmLogService;

    @Autowired
    @Qualifier("AccountantOverRepayLogService")
    AccountantOverRepayLogService accountantOverRepayLogService;
    @Autowired
    @Qualifier("RepaymentProjFactRepayService")
    RepaymentProjFactRepayService repaymentProjFactRepayService;
    @Autowired
    @Qualifier("RepaymentResourceService")
    RepaymentResourceService repaymentResourceService;

    @Autowired
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;

    @Autowired
    private PlatformRepaymentFeignClient platformRepaymentFeignClient;

    @Autowired
    private AlmsOpenServiceFeignClient almsOpenServiceFeignClient;

    @Autowired
    @Qualifier("RepaymentProjPlanListService")
    private RepaymentProjPlanListService repaymentProjPlanListService;

    @Autowired
    @Qualifier("RepaymentProjPlanService")
    private RepaymentProjPlanService repaymentProjPlanService;

    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;

    @Autowired
    @Qualifier("TuandaiProjectInfoService")
    private TuandaiProjectInfoService tuandaiProjectInfoService;

    @Autowired
    @Qualifier("SysApiCallFailureRecordService")
    private SysApiCallFailureRecordService sysApiCallFailureRecordService;
    
    @Autowired
    @Qualifier("IssueSendOutsideLogService")
    private IssueSendOutsideLogService issueSendOutsideLogService ;
    @Autowired
    @Qualifier("AgencyRechargeLogService")
    private AgencyRechargeLogService agencyRechargeLogService;

    @Autowired
	private RepaymentBizPlanListSynchMapper repaymentBizPlanListSynchMapper ;
    
    @Autowired
    @Qualifier("MoneyPoolService")
	MoneyPoolService moneyPoolService ;
    
    @Autowired
    @Qualifier("FinanceService")
	FinanceService financeService;

    private FinanceBaseDto initFinanceBase(ConfirmRepaymentReq req) {
        FinanceBaseDto financeBaseDto = new FinanceBaseDto();
        financeBaseDto.setBusinessId(req.getBusinessId());
        financeBaseDto.setOrgBusinessId(selectOrgBusinessId(req));
        financeBaseDto.setAfterId(req.getAfterId());
        financeBaseDto.setRemark(req.getRemark());
        financeBaseDto.setCallFlage(req.getCallFlage());


        financeBaseDto.setProjListDetails(new ArrayList<>());
        financeBaseDto.setRepaymentResources(new ArrayList<>());
//        financeBaseDto.setUpdatedProjPlanDetails(new HashSet<>());
        financeBaseDto.setProjFactRepays(new HashMap<>());
        financeBaseDto.setRepaymentBizPlanListDetailBaks(new ArrayList<>());
        financeBaseDto.setRepaymentProjPlanBaks(new ArrayList<>());
        financeBaseDto.setRepaymentProjPlanListBaks(new ArrayList<>());
        financeBaseDto.setRepaymentProjPlanListDetailBaks(new ArrayList<>());
        financeBaseDto.setCurTimeRepaidProjPlanList(new ArrayList<>());
        LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
        if (loginInfo != null) {
            financeBaseDto.setUserId(loginInfo.getUserId());
            financeBaseDto.setUserName(loginInfo.getUserName());
        }
        financeBaseDto.setConfirmLog(createConfirmLog(financeBaseDto));
        return financeBaseDto;
    }


    public String selectOrgBusinessId(ConfirmRepaymentReq req) {
        String orgBusinessId = null;
        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper
                .selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", req.getBusinessId())
                        .eq("after_id", req.getAfterId()).orderBy("after_id"));
        if (!CollectionUtils.isEmpty(repaymentBizPlanLists)) {
            orgBusinessId = repaymentBizPlanLists.get(0).getOrigBusinessId();
        }
        return orgBusinessId;
    }

    @Override
    @Transactional(rollbackFor = {ServiceRuntimeException.class, Exception.class})
    public List<CurrPeriodProjDetailVO> execute(ConfirmRepaymentReq req, boolean save) {
    	logger.info("请求内容={}",JSONObject.toJSONString(req));
    	
//        this.save.set(save);
        if (req == null) {
            throw new ServiceRuntimeException("ConfirmRepaymentReq 不能为空");
        }
        if (req.getBusinessId() == null) {
            throw new ServiceRuntimeException("ConfirmRepaymentReq.businessId 不能为空");
        }
        if (req.getAfterId() == null) {
            throw new ServiceRuntimeException("ConfirmRepaymentReq.afterId 不能为空");
        }
        if((req.getMprIds() == null || req.getMprIds().isEmpty())
                && (req.getSurplusFund() == null || req.getSurplusFund().equals(new BigDecimal("0")))
                && (req.getLogIds() == null || req.getLogIds().isEmpty())) {
            throw new ServiceRuntimeException("NO_RESOURCE","ConfirmRepaymentReq至少要有一个还款来源");
        }
        if (req.getCallFlage() == null) {
            throw new ServiceRuntimeException("ConfirmRepaymentReq.callFlage 不能为空");
        }
        //==========================
        //构建初始化对象
        //==========================
        FinanceBaseDto financeBaseDto = initFinanceBase(req);
        financeBaseDto.setSave(save);

        String businessId = financeBaseDto.getBusinessId();
        String afterId = financeBaseDto.getAfterId();
//        financeService.getCurrPeriodRepaymentInfoVO(businessId, afterId);

        BigDecimal unpaid = repaymentProjFactRepayService.caluUnpaid(businessId, afterId);
        if (unpaid.compareTo(new BigDecimal("0")) <= 0) {
            throw new ServiceRuntimeException("不存在未还款项目");
        }
        // 设置应还金额
        financeBaseDto.setRepayPlanAmount(unpaid);
        // 设置业务还款计划信息
        financeBaseDto.setPlanDto(initRepaymentBizPlanDto(req, financeBaseDto));
        sortRepaymentResource(req, financeBaseDto);

//        financeBaseDto.setResourceIndex(0);
//        financeBaseDto.setCuralResource(financeBaseDto.getRepaymentResources().get(0));
        /*List<ApplyDerateType> listBizPlanListUnusedDerates = applyDerateTypeMapper.listBizPlanListUnusedDerate(financeBaseDto.getPlanDto().getBizPlanListDtos().get(0).getRepaymentBizPlanList().getPlanListId());
        if (!CollectionUtils.isEmpty(listBizPlanListUnusedDerates)) {
        	List<DerateUseLog> list = new ArrayList<>() ;
			for (ApplyDerateType applyDerateType : listBizPlanListUnusedDerates) {
				
			}
		}*/
        
        BigDecimal repayFactAmount = financeBaseDto.getRepayFactAmount();
        BigDecimal repayPlanAmount = financeBaseDto.getRepayPlanAmount();
        if (repayFactAmount.compareTo(repayPlanAmount) >= 0) {
            financeBaseDto.setSurplusAmount(repayFactAmount.subtract(repayPlanAmount));
//            surplusAmount.set(repayFactAmount.subtract(repayPlanAmount));
            logger.info("surplusAmount={}", financeBaseDto.getSurplusAmount());
            if (financeBaseDto.getSurplusAmount().compareTo(new BigDecimal("0")) > 0 && financeBaseDto.getSave()) {
                AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
                accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
                accountantOverRepayLog.setBusinessId(req.getBusinessId());
                accountantOverRepayLog.setCreateTime(new Date());
                accountantOverRepayLog.setCreateUser(financeBaseDto.getUserId());
                accountantOverRepayLog.setFreezeStatus(0);
                accountantOverRepayLog.setIsRefund(0);
                accountantOverRepayLog.setIsTemporary(0);
                accountantOverRepayLog.setMoneyType(1);
                accountantOverRepayLog.setOverRepayMoney(financeBaseDto.getSurplusAmount());
                accountantOverRepayLog
                        .setRemark(String.format("收入于%s的%s期线下财务确认", req.getBusinessId(), req.getAfterId()));
                accountantOverRepayLog.insert();

                BigDecimal surplusAmount = financeBaseDto.getSurplusAmount();
                RepaymentConfirmLog confirmLog = financeBaseDto.getConfirmLog();
                confirmLog.setSurplusAmount(surplusAmount);
                confirmLog.setSurplusRefId(accountantOverRepayLog.getId().toString());
//                this.confirmLog.get().setSurplusAmount(surplusAmount.get());
//                this.confirmLog.get().setSurplusRefId(accountantOverRepayLog.getId().toString());
            }
        } else {
            financeBaseDto.setLackAmount(repayPlanAmount.subtract(repayFactAmount));
//            lackAmount.set(repayPlanAmount.subtract(repayFactAmount));
            logger.info("lackAmount={}", financeBaseDto.getLackAmount());
        }

        RepaymentBizPlanDto planDto = financeBaseDto.getPlanDto();
        // 分配线上输入的滞纳金
        divideOveryDueMoneyNew(req.getOfflineOverDue(), planDto, false);
        divideOveryDueMoneyNew(req.getOnlineOverDue(), planDto, true);

        // 填充信息 新的分润方法，按标从小到大，到主借标的顺序分
        fillNew(financeBaseDto);

        /////// 旧的分润方法 均分 开始 ==========
        // 计算标在业务中的占比
        // caluProportion(planDto.get());
        // 分配线上输入的滞纳金
        // divideOveryDueMoney(req.getOfflineOverDue(), planDto.get(), false);
        // divideOveryDueMoney(req.getOnlineOverDue(), planDto.get(), true);
        // 填充信息
        //fill();
        /////// 旧的分润方法 均分 结束 ==========

        if (save) {
            updateStatus(req, financeBaseDto);
        }
        logger.info("===============返回的对象数据为{}", financeBaseDto.getProjListDetails());
        return financeBaseDto.getProjListDetails();
    }

    /**
     * 根据参数创建还款来源并排序,银行流水,银行代扣,线下代扣3个优先级最高,结余优先级最低
     *
     * @param req
     * @return
     * @author 王继光 2018年5月24日 下午3:45:07
     */
    private void sortRepaymentResource(ConfirmRepaymentReq req, FinanceBaseDto financeBaseDto) {
        // 线下代扣流水ID列表
        List<String> mprids = req.getMprIds();
        // 结余金额
        BigDecimal surplus = req.getSurplusFund();
        // 代扣银行流水ID
        List<Integer> logIds = req.getLogIds();
        // 网关充值快捷充值ids
        List<String> rechargeIds = req.getRechargeIds();
        // 处理线下转账
        if (mprids != null && mprids.size() > 0) {
            List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectBatchIds(mprids);
            for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepayments) {
            	
            	if (moneyPoolRepayment.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
					throw new ServiceRuntimeException("已还款的银行流水不能再还款,请重新检查");
				}
            	
                // 增加总实还金额
                BigDecimal repayFactAmount = financeBaseDto.getRepayFactAmount().add(moneyPoolRepayment.getAccountMoney());
                financeBaseDto.setRepayFactAmount(repayFactAmount);
//                repayFactAmount.set();
                // 增加银行流水还的金额
                BigDecimal moneyPoolAmount = financeBaseDto.getMoneyPoolAmount().add(moneyPoolRepayment.getAccountMoney());
                financeBaseDto.setMoneyPoolAmount(moneyPoolAmount);
//                moneyPoolAmount.set();
                RepaymentResource repaymentResource = new RepaymentResource();
                repaymentResource.setIsCancelled(0);
                repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
                repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
                repaymentResource.setOrgBusinessId(moneyPoolRepayment.getOriginalBusinessId());
                repaymentResource.setCreateDate(new Date());
                repaymentResource.setCreateUser(financeBaseDto.getUserId());
                repaymentResource.setIsCancelled(0);
                repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
                repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
                repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.OFFLINE_TRANSFER.getValue().toString());
                repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());
                repaymentResource.setConfirmLogId(financeBaseDto.getConfirmLog().getConfirmLogId());
                if (financeBaseDto.getSave()) {
                    financeBaseDto.getConfirmLog().setRepayDate(repaymentResource.getRepayDate());
//                    confirmLog.get().setRepayDate(repaymentResource.getRepayDate());
                    repaymentResource.insert();
                }
                financeBaseDto.getRepaymentResources().add(repaymentResource);
                
//                repaymentResources.get().add(repaymentResource);
            }
            
            if (financeBaseDto.getSave()) {
            	moneyPoolService.confirmRepaidUpdateMoneyPool(req);
			}
        }

        // repaySource {20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款}
        if (logIds != null && logIds.size() > 0) {
            WithholdingRepaymentLog log = withholdingRepaymentLogService.selectById(logIds.get(0));
            String repaySource = RepayPlanRepaySrcEnum.BNAK_WITHHOLD.getValue().toString();
            if (log.getBindPlatformId() == PlatformEnum.YH_FORM.getValue() && log.getCreateUser().equals("auto_run")) {// 自动银行代扣已还款
                repaySource = RepayPlanRepaySrcEnum.BNAK_WITHHOLD.getValue().toString();
            } else if (log.getBindPlatformId() == PlatformEnum.YH_FORM.getValue()
                    && (!log.getCreateUser().equals("auto_run"))) {// 人工银行代扣已还款
                repaySource = RepayPlanRepaySrcEnum.BNAK_WITHHOLD_MAN.getValue().toString();
            } else if (log.getBindPlatformId() != PlatformEnum.YH_FORM.getValue()
                    && log.getCreateUser().equals("auto_run")) {// 20：自动线下代扣已还款
                repaySource = RepayPlanRepaySrcEnum.OFFLINE_WITHHOLD.getValue().toString();
            } else if (log.getBindPlatformId() != PlatformEnum.YH_FORM.getValue()
                    && (!log.getCreateUser().equals("auto_run"))) {// 21，人工线下代扣已还款
                repaySource = RepayPlanRepaySrcEnum.OFFLINE_WITHHOLD_MAN.getValue().toString();
            }
            RepaymentResource temp = repaymentResourceService.selectOne(new EntityWrapper<RepaymentResource>()
                    .eq("business_id", log.getOriginalBusinessId()).eq("after_id", log.getAfterId()).eq("is_cancelled", 0)
                    .eq("repay_source_ref_id", log.getLogId()).eq("repay_source", repaySource));
            if (temp != null) {// 已经核销过
                return;
            }

            RepaymentResource repaymentResource = new RepaymentResource();
            repaymentResource.setIsCancelled(0);
            repaymentResource.setAfterId(log.getAfterId());
            repaymentResource.setBusinessId(log.getOriginalBusinessId());
            repaymentResource.setOrgBusinessId(log.getOriginalBusinessId());
            repaymentResource.setCreateDate(new Date());
            if (financeBaseDto.getUserId() != null) {
                repaymentResource.setCreateUser(financeBaseDto.getUserId());
            } else {
                repaymentResource.setCreateUser("admin");
            }

            repaymentResource.setIsCancelled(0);
            repaymentResource.setRepayAmount(log.getCurrentAmount());
            repaymentResource.setRepayDate(log.getCreateTime());
            repaymentResource.setRepaySource(repaySource);
            if (financeBaseDto.getSave()) {
                RepaymentConfirmLog repaymentConfirmLog = financeBaseDto.getConfirmLog();
                repaymentConfirmLog.setRepayDate(repaymentResource.getRepayDate());
                repaymentResource.setRepaySourceRefId(log.getLogId().toString());
                repaymentResource.setConfirmLogId(repaymentConfirmLog.getConfirmLogId());
                repaymentResource.insert();
            }
            BigDecimal amount = financeBaseDto.getRepayFactAmount().add(log.getCurrentAmount());
            financeBaseDto.setRepayFactAmount(amount);
            financeBaseDto.getRepaymentResources().add(repaymentResource);
        }

        if (surplus != null && surplus.compareTo(new BigDecimal("0")) > 0) {
            BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(financeBaseDto.getBusinessId(), null);
            if (surplus.compareTo(canUseSurplus) > 0) {
                throw new ServiceRuntimeException("往期结余金额不足");
            }

            AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
            accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
            accountantOverRepayLog.setBusinessId(req.getBusinessId());
            accountantOverRepayLog.setCreateTime(new Date());
            accountantOverRepayLog.setCreateUser(financeBaseDto.getUserId());
            accountantOverRepayLog.setFreezeStatus(0);
            accountantOverRepayLog.setIsRefund(0);
            accountantOverRepayLog.setIsTemporary(0);
            accountantOverRepayLog.setMoneyType(0);
            accountantOverRepayLog.setOverRepayMoney(req.getSurplusFund());
            accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务确认", req.getBusinessId(), req.getAfterId()));
            if (financeBaseDto.getSave()) {
                accountantOverRepayLog.insert();
                financeBaseDto.getConfirmLog().setSurplusUseRefId(accountantOverRepayLog.getId().toString());
//                confirmLog.get().setSurplusUseRefId(accountantOverRepayLog.getId().toString());

            }

            RepaymentResource repaymentResource = new RepaymentResource();
            repaymentResource.setIsCancelled(0);
            repaymentResource.setAfterId(req.getAfterId());
            repaymentResource.setBusinessId(req.getBusinessId());
            repaymentResource.setOrgBusinessId(req.getBusinessId());
            repaymentResource.setCreateDate(new Date());
            repaymentResource.setCreateUser(financeBaseDto.getUserId());
            repaymentResource.setIsCancelled(0);
            repaymentResource.setRepayAmount(req.getSurplusFund());
            repaymentResource.setRepayDate(new Date());
            repaymentResource.setConfirmLogId(financeBaseDto.getConfirmLog().getConfirmLogId());
            //11:用往期结余还款',
            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.SURPLUS_REPAY.getValue().toString());
            if (financeBaseDto.getSave()) {
                repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
                repaymentResource.insert();
                if (mprids.size() == 0) {
                    financeBaseDto.getConfirmLog().setRepayDate(repaymentResource.getRepayDate());
//                    confirmLog.get().setRepayDate(repaymentResource.getRepayDate());
                }
            }
            BigDecimal reamount = financeBaseDto.getRepayFactAmount().add(req.getSurplusFund());
            financeBaseDto.setRepayFactAmount(reamount);
            financeBaseDto.getRepaymentResources().add(repaymentResource);
//            repayFactAmount.set(repayFactAmount.get().add(req.getSurplusFund()));
//            repaymentResources.get().add(repaymentResource);
        }
        
        
        // 充值记录转 RepaymentResource
        //RepayPlanRepaySrcEnum.PC_GATEWAY.getValue().equals(financeBaseDto.getCallFlage())
        if (RepayPlanRepaySrcEnum.APP_FAST_CHARGE.getValue().equals(financeBaseDto.getCallFlage())) {
        	handleAgencyRechargeLog(financeBaseDto);
		}
        
    }

    /**
     *  充值记录转 RepaymentResource
     * @param financeBaseDto
     */
	private void handleAgencyRechargeLog(FinanceBaseDto financeBaseDto) {
		List<AgencyRechargeLog> agencyRechargeLogs = agencyRechargeLogService
				.selectList(new EntityWrapper<AgencyRechargeLog>().eq("handle_status", "2").eq("charge_type", "3"));
		if (CollectionUtils.isEmpty(agencyRechargeLogs)) {
			return;
		}
		
		for (AgencyRechargeLog agencyRechargeLog : agencyRechargeLogs) {
			RepaymentResource repaymentResource = new RepaymentResource();
			repaymentResource.setIsCancelled(0);
            repaymentResource.setAfterId(agencyRechargeLog.getAfterId());
            repaymentResource.setBusinessId(agencyRechargeLog.getOrigBusinessId());
            repaymentResource.setOrgBusinessId(agencyRechargeLog.getOrigBusinessId());
            repaymentResource.setCreateDate(new Date());
            repaymentResource.setCreateUser(financeBaseDto.getUserId());
            repaymentResource.setRepayAmount(agencyRechargeLog.getRechargeAmount());
            repaymentResource.setRepayDate(agencyRechargeLog.getCreateTime());
            repaymentResource.setConfirmLogId(financeBaseDto.getConfirmLog().getConfirmLogId());
            //50:APP快捷充值
            repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.APP_FAST_CHARGE.getValue().toString());
            if (financeBaseDto.getSave()) {
                repaymentResource.setRepaySourceRefId(agencyRechargeLog.getCmOrderNo());
                repaymentResource.insert();
            }
//            BigDecimal reamount = financeBaseDto.getRepayFactAmount();
            financeBaseDto.setRepayFactAmount(financeBaseDto.getRepayFactAmount().add(agencyRechargeLog.getRechargeAmount()));
            financeBaseDto.getRepaymentResources().add(repaymentResource);
		}
	}

    /**
     * 查找并关联业务有关的还款计划
     *
     * @param req
     * @return
     * @author 王继光 2018年5月17日 下午9:37:57
     */
    private RepaymentBizPlanDto initRepaymentBizPlanDto(ConfirmRepaymentReq req, FinanceBaseDto financeBaseDto) {
        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper
                .selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", req.getBusinessId())
                        .eq("after_id", req.getAfterId()).orderBy("after_id"));

        // 判断是否找到还款计划列表
        if (repaymentBizPlanLists == null || repaymentBizPlanLists.size() == 0) {
            String ss = "查找并关联业务有关的还款计划 未找到业务还款计划列表：business_id:" + req.getBusinessId() + "    after_id:"
                    + req.getAfterId();
            logger.error(ss);
            throw new ServiceRuntimeException(ss);
        } else if (repaymentBizPlanLists.size() > 1) {
            String ss = "查找并关联业务有关的还款计划 找到两条以上业务还款计划列表：business_id:" + req.getBusinessId() + "    after_id:"
                    + req.getAfterId();
            logger.error(ss);
            throw new ServiceRuntimeException(ss);
        }

//        repaymentProjPlanListService.calLateFeeForPerPList(repaymentBizPlanLists.get(0), 1) ;
        
        
        RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto();
        RepaymentBizPlan repaymentBizPlan = new RepaymentBizPlan();

        repaymentBizPlan = repaymentBizPlanMapper.selectById(repaymentBizPlanLists.get(0).getPlanId());
        // 判断是否找到还款计划
        if (repaymentBizPlan == null) {
            String ss = "判断是否找到还款计划（repaymentBizPlan） 未找到业务还款计划：business_id:" + req.getBusinessId() + "    after_id:"
                    + req.getAfterId();
            logger.error(ss);
            throw new ServiceRuntimeException(ss);
        }

        financeBaseDto.setRepaymentBizPlanBak(new RepaymentBizPlanBak(repaymentBizPlan));
//        repaymentBizPlanBak.set();
        financeBaseDto.setOrgBusinessId(repaymentBizPlan.getOriginalBusinessId());
//        orgBusinessId.set(repaymentBizPlan.getOriginalBusinessId());
        repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);

        List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = new ArrayList<>();
        int i = 1;
        for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
            logger.info("这条LOG应该只出现一次(第{}次),planlistId={},planId={}", i, repaymentBizPlanList.getPlanListId(),
                    repaymentBizPlan.getPlanId());
            i++;
            financeBaseDto.setRepaymentBizPlanListBak(new RepaymentBizPlanListBak(repaymentBizPlanList));
//            repaymentBizPlanListBak.set(new RepaymentBizPlanListBak(repaymentBizPlanList));

            RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
            List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
                    .selectList(new EntityWrapper<RepaymentBizPlanListDetail>()
                            .eq("plan_list_id", repaymentBizPlanList.getPlanListId()).orderBy("share_profit_index")
                            .orderBy("plan_item_type").orderBy("fee_id"));

            for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
                List<RepaymentBizPlanListDetailBak> repaymentBizPlanListDetailBaks = financeBaseDto.getRepaymentBizPlanListDetailBaks();
                repaymentBizPlanListDetailBaks.add(new RepaymentBizPlanListDetailBak(repaymentBizPlanListDetail));
//                repaymentBizPlanListDetailBaks.get().add(new RepaymentBizPlanListDetailBak(repaymentBizPlanListDetail));
            }
            repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetails);
            repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);
            repaymentBizPlanListDtos.add(repaymentBizPlanListDto);

        }
        repaymentBizPlanDto.setBizPlanListDtos(repaymentBizPlanListDtos);

        List<RepaymentProjPlanDto> repaymentProjPlanDtos = new ArrayList<>();
        List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanMapper
                .selectList(new EntityWrapper<RepaymentProjPlan>().eq("plan_id", repaymentBizPlan.getPlanId()));
        for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
            List<RepaymentProjPlanBak> repaymentProjPlanBaks = financeBaseDto.getRepaymentProjPlanBaks();
            repaymentProjPlanBaks.add(new RepaymentProjPlanBak(repaymentProjPlan));
//            repaymentProjPlanBaks.get().add(new RepaymentProjPlanBak(repaymentProjPlan));


            RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto();
            repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
            List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(
                    new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
                            .eq("plan_list_id", repaymentBizPlanLists.get(0).getPlanListId())
                            .orderBy("total_borrow_amount", false));

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = new ArrayList<>();
            for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
                List<RepaymentProjPlanListBak> repaymentProjPlanListBaks = financeBaseDto.getRepaymentProjPlanListBaks();
                repaymentProjPlanListBaks.add(new RepaymentProjPlanListBak(repaymentProjPlanList));
//                this.repaymentProjPlanListBaks.get().add(new RepaymentProjPlanListBak(repaymentProjPlanList));

                RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
                repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
                List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
                        .selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
                                .eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId())
                                .orderBy("share_profit_index").orderBy("plan_item_type").orderBy("fee_id"));

                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = new ArrayList<>();
                BigDecimal unpaid = new BigDecimal("0");
                for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
                    List<RepaymentProjPlanListDetailBak> repaymentProjPlanListDetailBaks = financeBaseDto.getRepaymentProjPlanListDetailBaks();
                    repaymentProjPlanListDetailBaks.add(new RepaymentProjPlanListDetailBak(repaymentProjPlanListDetail));
//                    this.repaymentProjPlanListDetailBaks.get()
//                            .add(new RepaymentProjPlanListDetailBak(repaymentProjPlanListDetail));

                    if (repaymentProjPlanListDetail.getProjFactAmount() == null) {
                        repaymentProjPlanListDetail.setProjFactAmount(new BigDecimal("0"));
                    }
                    unpaid = unpaid.add(repaymentProjPlanListDetail.getProjPlanAmount()
                            .subtract(repaymentProjPlanListDetail.getProjFactAmount()));

                    RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto = new RepaymentProjPlanListDetailDto();
                    repaymentProjPlanListDetailDto.setRepaymentProjPlanListDetail(repaymentProjPlanListDetail);
                    List<RepaymentProjFactRepay> repaymentProjFactRepays = repaymentProjFactRepayMapper
                            .selectList(new EntityWrapper<RepaymentProjFactRepay>()
                                    .eq("proj_plan_detail_id", repaymentProjPlanListDetail.getProjPlanDetailId()).eq("is_cancelled", 0)
                                    .orderBy("plan_item_type").orderBy("fee_id"));
                    repaymentProjPlanListDetailDto.setRepaymentProjFactRepays(repaymentProjFactRepays);
                    repaymentProjPlanListDetailDtos.add(repaymentProjPlanListDetailDto);

                }
                repaymentProjPlanListDto.setUnpaid(unpaid);
                repaymentProjPlanListDto.setRepaymentProjPlanListDetailDtos(repaymentProjPlanListDetailDtos);
                repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetails);
                repaymentProjPlanListDtos.add(repaymentProjPlanListDto);
            }
            TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoMapper
                    .selectById(repaymentProjPlan.getProjectId());
            CurrPeriodProjDetailVO currPeriodProjDetailVO = new CurrPeriodProjDetailVO();
            currPeriodProjDetailVO
                    .setMaster(tuandaiProjectInfo.getMasterIssueId().equals(tuandaiProjectInfo.getProjectId()));
            currPeriodProjDetailVO.setProjAmount(repaymentProjPlanDto.getRepaymentProjPlan().getBorrowMoney());
            currPeriodProjDetailVO.setProject(tuandaiProjectInfo.getProjectId());
            currPeriodProjDetailVO.setUserName(tuandaiProjectInfo.getRealName());
            currPeriodProjDetailVO.setMaster(tuandaiProjectInfo.getMasterIssueId().equals(tuandaiProjectInfo.getProjectId()));
            currPeriodProjDetailVO.setQueryFullSuccessDate(tuandaiProjectInfo.getQueryFullSuccessDate());
            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
            projListDetails.add(currPeriodProjDetailVO);

//            if (projListDetails.get() == null) {
//                projListDetails.set(new ArrayList<>());
//            }
//            this.projListDetails.get().add(currPeriodProjDetailVO);
            repaymentProjPlanDto.setTuandaiProjectInfo(tuandaiProjectInfo);
            repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
            repaymentProjPlanDto.setProjPlanListDtos(repaymentProjPlanListDtos);
            repaymentProjPlanDtos.add(repaymentProjPlanDto);
        }

        ProjPlanDtoUtil.sort(repaymentProjPlanDtos);
        
        CurrPeriodProjDetailVO.sort(financeBaseDto.getProjListDetails());
        
        for (RepaymentProjPlanDto repaymentProjPlanDto2 : repaymentProjPlanDtos) {
            logger.info("满标时间{}"
                    + DateUtil.formatDate("yyyy-MM-dd HH:mm:ss",repaymentProjPlanDto2.getTuandaiProjectInfo().getQueryFullSuccessDate()));
            logger.info("借款金额{}" + repaymentProjPlanDto2.getRepaymentProjPlan().getBorrowMoney());
            logger.info("是否主借标{}" + repaymentProjPlanDto2.getTuandaiProjectInfo().getMasterIssueId()
                    .equals(repaymentProjPlanDto2.getTuandaiProjectInfo().getProjectId()));
            logger.info("借款人{}",repaymentProjPlanDto2.getTuandaiProjectInfo().getRealName());

        }
        repaymentBizPlanDto.setProjPlanDtos(repaymentProjPlanDtos);
        return repaymentBizPlanDto;

    }


    /**
     * 计算每个标的占比
     *
     * @param dto
     * @return
     * @author 王继光 2018年5月18日 上午10:43:18
     */
    private void caluProportion(RepaymentBizPlanDto dto) {
        BigDecimal count = new BigDecimal("0");
        for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
            count = count.add(projPlanDto.getRepaymentProjPlan().getBorrowMoney());
        }
        for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
            BigDecimal proportion = projPlanDto.getRepaymentProjPlan().getBorrowMoney().divide(count, 10,
                    BigDecimal.ROUND_HALF_UP);
            projPlanDto.setProportion(proportion);
        }
    }

    /**
     * 计算每个标的分配下来的金额
     *
     * @param dto
     * @return
     * @author 王继光 2018年5月18日 上午10:43:18
     */
    private void divideMoney(BigDecimal money, RepaymentBizPlanDto dto) {
        BigDecimal moneyCopy = money;
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            if (i == dto.getProjPlanDtos().size() - 1) {
                repaymentProjPlanDto.setDivideAmount(moneyCopy);
            } else {
                BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion()).setScale(2,
                        RoundingMode.HALF_UP);
                repaymentProjPlanDto.setDivideAmount(dmoney);
                moneyCopy = moneyCopy.subtract(dmoney);
            }
        }
    }

    /**
     * 计算每个标的分配的金额 按照先小标，后大标，最后主借标的规则分配
     *
     * @param money
     * @param dto
     */
    private void divideMoneyNew(BigDecimal money, RepaymentBizPlanDto dto) {
        BigDecimal moneyCopy = money;

        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            if (i == dto.getProjPlanDtos().size() - 1) {
                repaymentProjPlanDto.setDivideAmount(moneyCopy);
            } else {
                // 1.计算应还金额

                // 2.计算实还金额

                // 3.计算本次还款应还金额

            }

        }

    }

    /**
     * 计算每个标的分配下来的滞纳金金额
     *
     * @param money
     * @param dto
     * @param online true=分配线上滞纳金到标的,false=分配线下滞纳金到标的
     * @author 王继光 2018年5月24日 下午9:27:27
     */
    private void divideOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto, boolean online) {
        if (money == null) {
            return;
        }
        BigDecimal moneyCopy = money;
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            if (i == dto.getProjPlanDtos().size() - 1) {
                if (online) {
                    repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
                } else {
                    repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
                }
            } else {
                BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion()).setScale(2,
                        RoundingMode.HALF_UP);
                if (online) {
                    repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
                } else {
                    repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
                }
                moneyCopy = moneyCopy.subtract(dmoney);
            }
        }
    }

    /**
     * 计算每个标分配下来的滞纳金金额 按照先小标，后大标，最后主借标的顺序进行分配
     *
     * @param money
     * @param dto
     * @param online
     */
    private void divideOveryDueMoneyNew(BigDecimal money, RepaymentBizPlanDto dto, boolean online) {
        if (money == null) {
            return;
        }
        // 待分配的金额
        BigDecimal moneyCopy = money;
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);

            // 如果还有待分配的滞纳金，则继续分配
            if (moneyCopy.compareTo(new BigDecimal("0")) > 0) {
                // 如果是最后一期，则将剩余的所有滞纳金分配到最后一期
                if (i == dto.getProjPlanDtos().size() - 1) {
                    if (online) {
                        repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
                    } else {
                        repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
                    }
                } else {
                    List<RepaymentProjPlanListDetailDto> detailDtos = repaymentProjPlanDto.getProjPlanListDtos().get(0)
                            .getRepaymentProjPlanListDetailDtos();
                    // 计划中应还的滞纳金
                    BigDecimal planOverDue = new BigDecimal("0");
                    // 实际已还的滞纳金
                    BigDecimal payedOverDue = new BigDecimal("0");
                    // 本次应还的滞纳金
                    BigDecimal showPayOverDue = new BigDecimal("0");
                    // 这次分配到的滞纳金
                    BigDecimal dmoney = null;
                    // 计算这个标应还的滞纳金
                    if (online) {
                        for (RepaymentProjPlanListDetailDto detailDto : detailDtos) {
                            if (detailDto.getRepaymentProjPlanListDetail().getFeeId()
                                    .equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                                planOverDue.add(detailDto.getRepaymentProjPlanListDetail().getProjPlanAmount());
                                for (RepaymentProjFactRepay repaymentProjFactRepay : detailDto
                                        .getRepaymentProjFactRepays()) {
                                    payedOverDue.add(repaymentProjFactRepay.getFactAmount());
                                }
                            }
                        }
                        showPayOverDue = planOverDue.subtract(payedOverDue);
                        // 如果应还的滞纳金比剩余的少
                        if (showPayOverDue.compareTo(moneyCopy) < 0) {
                            dmoney = showPayOverDue;
                        } else {
                            dmoney = moneyCopy;
                        }
                        repaymentProjPlanDto.setOnlineOverDue(dmoney);
                        moneyCopy = moneyCopy.add(dmoney);

                    } else {
                        for (RepaymentProjPlanListDetailDto detailDto : detailDtos) {
                            if (detailDto.getRepaymentProjPlanListDetail().getFeeId()
                                    .equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                                planOverDue.add(detailDto.getRepaymentProjPlanListDetail().getProjPlanAmount());
                                for (RepaymentProjFactRepay repaymentProjFactRepay : detailDto
                                        .getRepaymentProjFactRepays()) {
                                    payedOverDue.add(repaymentProjFactRepay.getFactAmount());
                                }
                            }
                        }
                        showPayOverDue = planOverDue.subtract(payedOverDue);
                        // 如果应还的滞纳金比剩余的少
                        if (showPayOverDue.compareTo(moneyCopy) < 0) {
                            dmoney = showPayOverDue;
                        } else {
                            dmoney = moneyCopy;
                        }
                        repaymentProjPlanDto.setOfflineOverDue(dmoney);
                        moneyCopy = moneyCopy.add(dmoney);
                    }
                }
            } else {
                break;
            }
        }
    }

    private CurrPeriodProjDetailVO getCurrPeriodProjDetailVO(String projectId, List<CurrPeriodProjDetailVO> projListDetails) {
        for (CurrPeriodProjDetailVO currPeriodProjDetailVO : projListDetails) {
            if (currPeriodProjDetailVO.getProject().equals(projectId)) {
                return currPeriodProjDetailVO;
            }
        }
        return null;
    }

    /**
     * 填充detail
     *
     * @author 王继光 2018年6月15日 下午5:53:55
     */
    /*
    private void fill() {
        for (RepaymentResource resource : repaymentResources.get()) {
            BigDecimal repayAmount = resource.getRepayAmount();
            logger.info("还款来源{}金额={}", resource.getRepayAmount());
             每笔还款来源按比例分配金额到标的
            divideMoney(repayAmount, planDto.get());
            // planDto.get().getBizPlanListDtos().get(0).getRepaymentBizPlanList().setFactRepayDate(resource.getRepayDate());
            confirmLog.get().setRepaySource(Integer.parseInt(resource.getRepaySource()));

            if (Integer.parseInt(resource.getRepaySource()) == 11) {
                // 注意:当还款来源是用结余还钱,repayment_resource还是11,但是confirmLog却是10!!
                confirmLog.get().setRepaySource(10);
            }
            BigDecimal surplus = new BigDecimal("0");
            for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
                BigDecimal divideAmount = projPlanDto.getDivideAmount().add(surplus);
                logger.info("此次还款来源分配到的金额={}", projPlanDto.getDivideAmount());

                BigDecimal offLineOverDue = projPlanDto.getOfflineOverDue() == null ? new BigDecimal("0")
                        : projPlanDto.getOfflineOverDue();

                logger.info("此次还款来源分配到offline滞纳的金额={}", offLineOverDue);

                BigDecimal onLineOverDue = projPlanDto.getOnlineOverDue() == null ? new BigDecimal("0")
                        : projPlanDto.getOnlineOverDue();
                logger.info("此次还款来源分配到online滞纳的金额={}", onLineOverDue);

                divideAmount = divideAmount.subtract(offLineOverDue).subtract(onLineOverDue);

                String projectId = projPlanDto.getTuandaiProjectInfo().getProjectId();
                CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId);

                logger.info("====================开始遍历标的{}还款计划=======================", projectId);

                for (RepaymentProjPlanListDto repaymentProjPlanListDto : projPlanDto.getProjPlanListDtos()) {
                    if (divideAmount == null && offLineOverDue == null && onLineOverDue == null) {
                        logger.info("@@没有钱可以分配到细项,跳出标的还款计划循环");
                        break;
                    }
                    logger.info("====================开始遍历{}的细项=======================",
                            repaymentProjPlanListDto.getRepaymentProjPlanList().getProjPlanListId());
                    for (RepaymentProjPlanListDetail detail : repaymentProjPlanListDto.getProjPlanListDetails()) {
                        if (detail.getProjPlanAmount().compareTo(detail.getProjFactAmount()) == 0) {
                            logger.info("{}此项实还等于应还,已还满", detail.getPlanItemName());
                            continue;
                        }
                        if (divideAmount == null) {
                            logger.info("没有钱可以分配到细项,跳出标的细项循环");
                            break;
                        }
                        BigDecimal unpaid = detail.getProjPlanAmount().subtract(
                                detail.getDerateAmount() == null ? new BigDecimal("0") : detail.getDerateAmount())
                                .subtract(detail.getProjFactAmount());
                        logger.info("{}-{}未还金额{}", detail.getProjPlanDetailId(), detail.getPlanItemName(), unpaid);
                        BigDecimal money = new BigDecimal("0");
                        int c = divideAmount.compareTo(unpaid);
                        if (c > 0) {
                            logger.info("divideAmount大于unpaid");
                            logger.info("@@从divideAmount={}分unpaid={}到{}", divideAmount, unpaid,
                                    detail.getPlanItemName());
                            money = unpaid;
                            divideAmount = divideAmount.subtract(unpaid);
                            logger.info("divideAmount变为{}", divideAmount);
                        } else if (c == 0) {
                            logger.info("divideAmount等于unpaid");
                            logger.info("@@从divideAmount={}分unpaid={}到{}", divideAmount, unpaid,
                                    detail.getPlanItemName());
                            money = unpaid;
                            logger.info("divideAmount变为null", detail.getPlanItemName());
                            divideAmount = null;
                        } else {
                            logger.info("divideAmount少于unpaid");
                            logger.info("@@从divideAmount={}分unpaid={}到{}", divideAmount, divideAmount,
                                    detail.getPlanItemName());
                            money = divideAmount;
                            divideAmount = null;
                            logger.info("divideAmount变为null", detail.getPlanItemName());
                        }

                        switch (detail.getPlanItemType()) {
                            case 10:
                            case 20:
                            case 30:
                            case 50:
                                createProjFactRepay(money, detail, currPeriodProjDetailVO, resource);
                                break;
                            case 60:
                                if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
                                    createProjFactRepay(money, detail, currPeriodProjDetailVO, resource);
                                }
                                if (detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
                                    createProjFactRepay(money, detail, currPeriodProjDetailVO, resource);
                                }
                                break;
                            default:
                                logger.info("又或者难道是这里!!!");
                                createProjFactRepay(money, detail, currPeriodProjDetailVO, resource);
                                break;
                        }
                    }
                }
                if (divideAmount != null) {
                    logger.info("居然多出{}", divideAmount);
                    logger.info("原剩余{},现加上{},变为{}", surplus, divideAmount, surplus.add(divideAmount));
                    surplus = surplus.add(divideAmount);
                }
            }

        }

        if (surplusAmount.get().compareTo(new BigDecimal("0")) > 0) {
            logger.info("============================有结余{}", surplusAmount.get());
            projListDetails.get().get(0).setSurplus(surplusAmount.get());
        }

    }
*/

    /**
     * 分配规则变更，重新写
     */
    private void fillNew(FinanceBaseDto financeBaseDto) {
        RepaymentBizPlanDto dto = financeBaseDto.getPlanDto();

        // 上一次还款是否成功的标志位
        boolean lastPaySuc = true;


        // 3.最后按核销顺序还金额（先还核销顺序小于1200的费用）
        String lastProjectId = null;
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            if (lastPaySuc == false)
                return;
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
            lastProjectId = projectId;
            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
            CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId, projListDetails);

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            // 遍历标的还款计划
            for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto
                        .getRepaymentProjPlanListDetailDtos();
                // //遍历这个标的每一期还款计划，费用细项
                for (RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                    RepaymentProjPlanListDetail detail = repaymentProjPlanListDetailDto
                            .getRepaymentProjPlanListDetail();
                    /*线上滞纳金的核销优先级调整为先核销其他标的的本金利息服务费后在核销线上滞纳金 2018-08-31 update 贷后二期优化问题 肖莹环*/
                    if(detail.getShareProfitIndex().compareTo(1200)>=0 || detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())){
                        continue;
                    }
                    /*线上滞纳金的核销优先级调整为先核销其他标的的本金利息服务费后在核销线上滞纳金 2018-08-31 update 贷后二期优化问题 肖莹环*/
                    boolean bl = payOneFeeDetail(detail, currPeriodProjDetailVO, null, financeBaseDto);
                    if (!bl && financeBaseDto.getRealPayedAmount() != null) {
                        lastPaySuc = false;
                        break;
                    }
                }
            }
        }
        
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            if (lastPaySuc == false)
                return;
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
            lastProjectId = projectId;
            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
            CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId, projListDetails);

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            // 遍历标的还款计划
            for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto
                        .getRepaymentProjPlanListDetailDtos();
                // //遍历这个标的每一期还款计划，费用细项
                for (RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                    RepaymentProjPlanListDetail detail = repaymentProjPlanListDetailDto
                            .getRepaymentProjPlanListDetail();
//                    if(detail.getShareProfitIndex().compareTo(1200)>=0 && !detail){
//                        continue;
//                    }
                    /*线上滞纳金的核销优先级调整为先核销其他标的的本金利息服务费后在核销线上滞纳金 2018-08-31 update 贷后二期优化问题 肖莹环*/
                    if (!detail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
						continue;
					}
                    /*线上滞纳金的核销优先级调整为先核销其他标的的本金利息服务费后在核销线上滞纳金 2018-08-31 update 贷后二期优化问题 肖莹环*/
                    boolean bl = payOneFeeDetail(detail, currPeriodProjDetailVO, null, financeBaseDto);
                    if (!bl && financeBaseDto.getRealPayedAmount() != null) {
                        lastPaySuc = false;
                        break;
                    }
                }
            }
        }

        //再还核销顺序大于等于1200的费用项
        for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
            if (lastPaySuc == false)
                return;
            RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
            String projectId = repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId();
            lastProjectId = projectId;
            List<CurrPeriodProjDetailVO> projListDetails = financeBaseDto.getProjListDetails();
            CurrPeriodProjDetailVO currPeriodProjDetailVO = getCurrPeriodProjDetailVO(projectId, projListDetails);

            List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();
            // 遍历标的还款计划
            for (RepaymentProjPlanListDto repaymentProjPlanListDto : repaymentProjPlanListDtos) {
                List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = repaymentProjPlanListDto
                        .getRepaymentProjPlanListDetailDtos();
                // //遍历这个标的每一期还款计划，费用细项
                for (RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto : repaymentProjPlanListDetailDtos) {
                    RepaymentProjPlanListDetail detail = repaymentProjPlanListDetailDto
                            .getRepaymentProjPlanListDetail();
                    if(detail.getShareProfitIndex().compareTo(1200)<0){
                        continue;
                    }
                    boolean bl = payOneFeeDetail(detail, currPeriodProjDetailVO, null, financeBaseDto);
                    if (!bl && financeBaseDto.getRealPayedAmount() != null) {
                        lastPaySuc = false;
                        break;
                    }
                }
            }
        }

        // 结余
        BigDecimal surplusFund = new BigDecimal("0");
        // 如果最后一次还款都还足了，就计算结余
        if (lastPaySuc == true) {
            surplusFund = surplusFund.add(financeBaseDto.getCuralDivideAmount());
            boolean setBl = true;
            while (setBl == true) {
            	if (financeBaseDto.getResourceIndex()==null) {
            		setBl = setNewRepaymentResource(0, financeBaseDto);
				}else {
					setBl = setNewRepaymentResource(financeBaseDto.getResourceIndex() + 1, financeBaseDto);
				}
                if (setBl) {
                    surplusFund = surplusFund.add(financeBaseDto.getCuralDivideAmount());
                }
            }
        }
        // 将结余设置到最后一个标
        if (lastProjectId != null) {
            CurrPeriodProjDetailVO lastPeriodProjDetailVO = getCurrPeriodProjDetailVO(lastProjectId, financeBaseDto.getProjListDetails());
            lastPeriodProjDetailVO.setSurplus(surplusFund);
        }

    }

    /**
     * 还一个费用详情的费用
     *
     * @param detail                 本次还的费用项
     * @param currPeriodProjDetailVO 当前还的标的详情
     * @param maxPayMoney            本次调用可还的最大金额
     * @return boolean
     */
    private boolean payOneFeeDetail(RepaymentProjPlanListDetail detail, CurrPeriodProjDetailVO currPeriodProjDetailVO,
                                    BigDecimal maxPayMoney, FinanceBaseDto financeBaseDto) {

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
        fact.setIsCancelled(0);
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
            fact.setIsCancelled(0);
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
    	financeBaseDto.getProjFactRepayArray().add(fact);
    }
    /**
     * 更新备注
     * @author 王继光
     * 2018年7月10日 下午3:18:25
     * @param financeBaseDto
     */
    private void updateRemark (FinanceBaseDto financeBaseDto) {
    	RepaymentBizPlanList  bizPlanList = financeBaseDto.getPlanDto().getBizPlanListDtos().get(0).getRepaymentBizPlanList();
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
				if (log.getBindPlatformId().equals(PlatformEnum.YB_FORM.getValue())) {
					repayWay.append("(易宝代扣)");
				}
				if (log.getBindPlatformId().equals(PlatformEnum.BF_FORM.getValue())) {
					repayWay.append("(宝付代扣)");
				}
				break;
			case "21":
				repayWay.append("人工线下代扣");
				WithholdingRepaymentLog log1 = withholdingRepaymentLogService.selectById(lastOne.getRepaySourceRefId());
				if (log1.getBindPlatformId().equals(PlatformEnum.YB_FORM.getValue())) {
					repayWay.append("(易宝代扣)");
				}
				if (log1.getBindPlatformId().equals(PlatformEnum.BF_FORM.getValue())) {
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

			for (RepaymentBizPlanListDto planListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
				for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
					List<RepaymentProjFactRepay> list = financeBaseDto.getProjFactRepays().get(planListDetail.getPlanDetailId());
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
							feeDetails.append("线上滞纳金").append(",");
						}else if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
							feeDetails.append("线下滞纳金").append(",");
						}else {
							feeDetails.append(planListDetail.getPlanItemName()).append(",");
						}
						factTotalAmount = factTotalAmount.add(factAmount);
					}else {
						feeDetails.append(BigDecimal.ZERO.setScale(2));
						if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
							feeDetails.append("线上滞纳金").append(",");
						}else if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
							feeDetails.append("线下滞纳金").append(",");
						}else {
							feeDetails.append(planListDetail.getPlanItemName()).append(",");
						}
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
     * 新的updateRemark
     * @author 王继光
     * 2018年9月3日 下午2:46:12
     * @param financeBaseDto
     */
    private void updateRemarkNew(FinanceBaseDto financeBaseDto) {
    	RepaymentBizPlanList  bizPlanList = financeBaseDto.getPlanDto().getBizPlanListDtos().get(0).getRepaymentBizPlanList();
    	List<RepaymentConfirmLog> repayLogs = repaymentConfirmLogService.selectList(
    			new EntityWrapper<RepaymentConfirmLog>()
    			.eq("business_id", bizPlanList.getBusinessId())
    			.eq("after_id", bizPlanList.getAfterId()).eq("is_cancelled",0).eq("type", 1).orderBy("create_time")) ;
    	BigDecimal count = BigDecimal.ZERO;
    	List<RepaymentResource> allResource = new ArrayList<>() ;
    	List<RepaymentProjFactRepay> allFactRepay = new ArrayList<>() ;
    	for (RepaymentConfirmLog repaymentConfirmLog : repayLogs) {
			count = repaymentConfirmLog.getFactAmount().add(count);
			
			List<RepaymentResource> repaymentResources = repaymentResourceService.selectList(
					new EntityWrapper<RepaymentResource>()
					.eq("confirm_log_id", repaymentConfirmLog.getConfirmLogId())
					.eq("is_cancelled", 0)
					.orderBy("repay_date"));
			
			if (!CollectionUtils.isEmpty(repaymentResources)) {
				allResource.addAll(repaymentResources);
			}
			
			List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayService.selectList(new EntityWrapper<RepaymentProjFactRepay>()
					.eq("confirm_log_id", repaymentConfirmLog.getConfirmLogId())
					.eq("is_cancelled", 0));
			
			if (!CollectionUtils.isEmpty(factRepays)) {
				allFactRepay.addAll(factRepays);
			}
		}
//    	allResource.addAll(financeBaseDto.getRepaymentResources());
//    	allFactRepay.addAll(financeBaseDto.getProjFactRepayArray());
    	
    	StringBuffer remark = new StringBuffer() ;
    	remark.append("备注:\r\n");
    	for (RepaymentResource repaymentResource : allResource) {
			remark.append(DateUtil.formatDate(repaymentResource.getRepayDate()));
			remark.append("  ");
			remark.append(RepayPlanRepaySrcEnum.descOf(Integer.valueOf(repaymentResource.getRepaySource()))) ;
			remark.append("收到  ").append(repaymentResource.getRepayAmount().setScale(2, RoundingMode.HALF_UP)).append("元").append("\r\n") ;
		}
    	remark.append("合计:  ").append(count.setScale(2, RoundingMode.HALF_UP)).append("元").append("\r\n");
    	remark.append("明细:\r\n");
    	
    	List<SettleFeesVO> feesVOs = new ArrayList<>() ;
    	for (RepaymentProjFactRepay factRepay : allFactRepay) {
    		if (factRepay.getFactAmount().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
    		boolean contain = false ;
    		for (SettleFeesVO settleFeesVO : feesVOs) {
				if (settleFeesVO.getFeeId().equals(factRepay.getFeeId())) {
					contain = true ;
					settleFeesVO.setAmount(settleFeesVO.getAmount().add(factRepay.getFactAmount()));
					break ;
				}
			}
    		if (!contain) {
				SettleFeesVO feesVO = new SettleFeesVO() ;
				feesVO.setFeeId(factRepay.getFeeId());
				feesVO.setAmount(factRepay.getFactAmount());
				feesVO.setPlanItemName(factRepay.getPlanItemName());
				feesVOs.add(feesVO);
			}
		}
    	
    	for (SettleFeesVO settleFeesVO : feesVOs) {
			remark.append(settleFeesVO.getAmount().setScale(2, RoundingMode.HALF_UP)).append("元").append(settleFeesVO.getPlanItemName()).append(",") ;
		}
    	
    	BigDecimal balance = accountantOverRepayLogService.caluCanUse(bizPlanList.getBusinessId(), bizPlanList.getAfterId());
    	if (financeBaseDto.getConfirmLog().getSurplusAmount()!=null) {
			balance = balance.add(financeBaseDto.getConfirmLog().getSurplusAmount());
		}
    	if (balance.compareTo(BigDecimal.ZERO)>0) {
			remark.append(balance).append("元").append("结余");
		}
    	remark.append(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", financeBaseDto.getConfirmLog().getCreateTime())) ;
    	
    	bizPlanList.setRemark(remark.toString());
    	bizPlanList.updateAllColumnById();
    }
    /**
     * 在内存中更新数据
     *
     * @author 王继光 2018年6月15日 下午8:26:42
     */
    public void updateInMem(ConfirmRepaymentReq req, FinanceBaseDto financeBaseDto) {
//        Set<RepaymentProjPlanListDetail> updatedProjPlanDetails = financeBaseDto.getUpdatedProjPlanDetails();

        for (RepaymentBizPlanListDto planListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
			for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
				List<RepaymentProjFactRepay> list = financeBaseDto.getProjFactRepays().get(planListDetail.getPlanDetailId());
				if (list!=null&&!list.isEmpty()) {
					for (RepaymentProjFactRepay repaymentProjFactRepay : list) {
						planListDetail.setFactAmount(repaymentProjFactRepay.getFactAmount().add(planListDetail.getFactAmount()==null?BigDecimal.ZERO:planListDetail.getFactAmount()));
//			            planListDetail.setFactAmount(detail.getProjFactAmount());
			            planListDetail.setFactRepayDate(repaymentProjFactRepay.getFactRepayDate());
			            planListDetail.setRepaySource(repaymentProjFactRepay.getRepaySource());
			            planListDetail.setUpdateDate(new Date());
			            planListDetail.setUpdateUser(financeBaseDto.getUserId());
			            planListDetail.updateById();
			            updateRepaymentBizPlanListDetailInMem(planListDetail, financeBaseDto);

			            RepaymentProjPlanList projPlanList = findRepaymentProjPlanList(repaymentProjFactRepay.getProjPlanListId(), financeBaseDto);

			            if (projPlanList == null) {
			                throw new ServiceRuntimeException("找不到对应的projPlanList");
			            }
			            projPlanList.setFactRepayDate(financeBaseDto.getConfirmLog().getRepayDate());
			            
			            BigDecimal pjlFactAmount = sumProjPlanListFactAmountInMem(projPlanList.getProjPlanListId(), financeBaseDto);
			            BigDecimal pjlOnlineAmount = sumProjPlanListOnlinePartAmountInMem(projPlanList.getProjPlanListId(),financeBaseDto);
						/* 如果实还大于应还+逾期 */
			            if (pjlFactAmount.compareTo(projPlanList.getTotalBorrowAmount()
			                    .add(projPlanList.getOverdueAmount() == null ? BigDecimal.ZERO
			                            : projPlanList.getOverdueAmount())) >= 0) {
			                projPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
			                projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
			                projPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
			                financeBaseDto.getCurTimeRepaidProjPlanList().add(projPlanList);
			                setRepayConfirmedFlagPro(projPlanList, financeBaseDto);
			            } else {
			                projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
			                projPlanList.setRepayFlag(null);
			                
			                if (pjlFactAmount.compareTo(pjlOnlineAmount) >= 0) {
				                projPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
			                	projPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
                                financeBaseDto.getCurTimeRepaidProjPlanList().add(projPlanList);
			                } else {
			                	projPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
			                }
			                
			            }
			            projPlanList.setUpdateTime(new Date());
			            projPlanList.setUpdateUser(financeBaseDto.getUserId());
			            
			            
			            
			            projPlanList.updateAllColumnById();
			            updateRepaymentProjPlanListInMem(projPlanList, financeBaseDto);

			            RepaymentBizPlanList bizPlanList = findRepaymentbizplanlist(projPlanList.getPlanListId(), financeBaseDto);
			            bizPlanList.setFactRepayDate(financeBaseDto.getConfirmLog().getRepayDate());
			            bizPlanList.setFinanceComfirmDate(new Date());

			            BigDecimal bplFactAmount = sumBizPlanListFactAmount(bizPlanList.getPlanListId(), financeBaseDto);
			            BigDecimal bplOnlineAmount = sumBizPlanListOnlinePartAmount(bizPlanList.getPlanListId(),financeBaseDto);
			            if (bplFactAmount.compareTo(
			                    bizPlanList.getTotalBorrowAmount().add(bizPlanList.getOverdueAmount() == null ? new BigDecimal("0")
			                            : bizPlanList.getOverdueAmount())) >= 0) {
			                bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
			                bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
			                bizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
			                setRepayConfirmedFlagBiz(bizPlanList, financeBaseDto);
			                LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
			                if (loginInfo != null) {
			                    bizPlanList.setFinanceConfirmUser(loginInfo.getUserId());
			                    bizPlanList.setFinanceConfirmUserName(loginInfo.getUserName());
			                } else {
			                    bizPlanList.setFinanceConfirmUser(Constant.ADMIN_ID);
			                    bizPlanList.setFinanceConfirmUserName("admin");
			                }
			                
			            } else {

							bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
							bizPlanList.setRepayFlag(null);
			                if (bplFactAmount.compareTo(bplOnlineAmount) >= 0) {
								bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
			                    bizPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
			                } else {
			                    bizPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
			                }
			            }
			            bizPlanList.updateAllColumnById();
			            updateRepaymentBizPlanList(bizPlanList, financeBaseDto);
			            
			            RepaymentBizPlanListSynch synch = new RepaymentBizPlanListSynch() ;
		                synch.setPlanListId(bizPlanList.getPlanListId());
		                synch = repaymentBizPlanListSynchMapper.selectOne(synch) ;
		                synch.setCurrentStatus(bizPlanList.getCurrentStatus());
		                synch.setCurrentSubStatus(bizPlanList.getCurrentSubStatus());
		                synch.setRepayStatus(bizPlanList.getRepayStatus());
		                synch.setRepayFlag(bizPlanList.getRepayFlag());
		                synch.setFactRepayDate(bizPlanList.getFactRepayDate());
		                synch.setFinanceConfirmUser(bizPlanList.getFinanceConfirmUser());
		                synch.setFinanceConfirmUserName(bizPlanList.getFinanceConfirmUserName());
		                synch.setFactAmountExt(bplFactAmount);
		                repaymentBizPlanListSynchMapper.updateAllColumnById(synch);
					}
				}
			}
		}
    }


    
    /**
     * 设置还款确认状态
     *
     * @param projPlanList
     * @author 王继光 2018年6月15日 上午11:29:49
     */
    private void setRepayConfirmedFlagPro(RepaymentProjPlanList projPlanList, FinanceBaseDto financeBaseDto) {
        List<RepaymentResource> repaymentResources = financeBaseDto.getRepaymentResources();
        RepaymentResource repaymentResource = repaymentResources.get(repaymentResources.size() - 1);
        if (repaymentResource.getRepaySource().equals("10")) {
            projPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("11")) {
            projPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("20")) {
            projPlanList.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("30")) {
            projPlanList.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("31")) {
            projPlanList.setRepayFlag(RepayedFlag.MANUA_BANKL_WITHHOLD_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("21")) {
            projPlanList.setRepayFlag(RepayedFlag.MANUAL_WITHHOLD_OFFLINE_REPAYED.getKey());
        }
    }

    /**
     * 设置还款确认状态
     *
     * @author 王继光 2018年6月15日 上午11:29:49
     */
    private void setRepayConfirmedFlagBiz(RepaymentBizPlanList bizjPlanList, FinanceBaseDto financeBaseDto) {
        List<RepaymentResource> repaymentResources = financeBaseDto.getRepaymentResources();
        RepaymentResource repaymentResource = repaymentResources.get(repaymentResources.size() - 1);
//        RepaymentResource repaymentResource = repaymentResources.get().get(repaymentResources.get().size() - 1);
        if (repaymentResource.getRepaySource().equals("10")) {
            bizjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("11")) {
            bizjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("20")) {
            bizjPlanList.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("30")) {
            bizjPlanList.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("31")) {
        	bizjPlanList.setRepayFlag(RepayedFlag.MANUA_BANKL_WITHHOLD_REPAYED.getKey());
        }
        if (repaymentResource.getRepaySource().equals("21")) {
        	bizjPlanList.setRepayFlag(RepayedFlag.MANUAL_WITHHOLD_OFFLINE_REPAYED.getKey());
        }
    }

    private RepaymentConfirmLog createConfirmLog(FinanceBaseDto financeBaseDto) {
        String afterId = financeBaseDto.getAfterId();
        String businessId = financeBaseDto.getBusinessId();
        String orgBusinessId = financeBaseDto.getOrgBusinessId();

        RepaymentConfirmLog repaymentConfirmLog = new RepaymentConfirmLog();
        repaymentConfirmLog.setIsCancelled(0);
        repaymentConfirmLog.setAfterId(afterId);
        repaymentConfirmLog.setBusinessId(businessId);
        repaymentConfirmLog.setCanRevoke(1);
        repaymentConfirmLog.setConfirmLogId(UUID.randomUUID().toString());
        /*增加还款类型区分,1=还款日志,2=结清日志*/
        repaymentConfirmLog.setType(1);
        repaymentConfirmLog.setCreateTime(new Date());
        repaymentConfirmLog.setCreateUser(loginUserInfoHelper.getUserId());
        List<RepaymentConfirmLog> repaymentConfirmLogs = repaymentConfirmLog.selectList(new EntityWrapper<>()
                .eq("business_id", businessId).eq("after_id", afterId).eq("type", 1).orderBy("`idx`", false));
        if (repaymentConfirmLogs == null) {
            repaymentConfirmLog.setIdx(1);
        } else {
            if (repaymentConfirmLogs.size() == 0) {
                repaymentConfirmLog.setIdx(1);
            } else {
                repaymentConfirmLog.setIdx(repaymentConfirmLogs.get(0).getIdx() + 1);
            }
        }
        repaymentConfirmLog.setOrgBusinessId(orgBusinessId);
//		repaymentConfirmLog.setProjPlanJson(JSON.toJSONString(projListDetails.get()));
        repaymentConfirmLog.setRepaySource(financeBaseDto.getCallFlage());

        return repaymentConfirmLog;
    }


    /**
     * 在planDto中找出RepaymentBizPlanListDetail
     *
     * @param bizPlanListDetail
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private RepaymentBizPlanListDetail findRepaymentBizPlanListDetail(String bizPlanListDetail, FinanceBaseDto financeBaseDto) {
        for (RepaymentBizPlanListDto planListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
                if (planListDetail.getPlanDetailId().equals(bizPlanListDetail)) {
                    return planListDetail;
                }
            }
        }
        return null;
    }

    /**
     * 计算projPlanList实还金额
     *
     * @param projPlanListId
     * @return
     * @author 王继光 2018年6月15日 下午7:40:55
     */
    private BigDecimal sumProjPlanListFactAmountInMem(String projPlanListId, FinanceBaseDto financeBaseDto) {
        BigDecimal res = new BigDecimal("0");
        for (RepaymentProjPlanDto projPlanDto : financeBaseDto.getPlanDto().getProjPlanDtos()) {
        	for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
        		String projPlanListIdc = projPlanListDto.getRepaymentProjPlanList().getProjPlanListId();
        		if (projPlanListId.equals(projPlanListIdc)) {
        			for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDto.getProjPlanListDetails()) {
                        res = res.add(projPlanListDetail.getProjFactAmount() == null ? new BigDecimal("0")
                                : projPlanListDetail.getProjFactAmount());
                    }
				}
            }
        }
        return res;
    }
    
    /**
     * 计算projPlanList应还线上滞纳金
     * @author 王继光
     * 2018年7月10日 下午4:31:52
     * @param projPlanListId
     * @param financeBaseDto
     * @return
     */
    private BigDecimal sumProjPlanListOnlinePartAmountInMem(String projPlanListId, FinanceBaseDto financeBaseDto) {
        BigDecimal res = new BigDecimal("0");
        for (RepaymentProjPlanDto projPlanDto : financeBaseDto.getPlanDto().getProjPlanDtos()) {
        	for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
        		String projPlanListIdc = projPlanListDto.getRepaymentProjPlanList().getProjPlanListId();
        		if (projPlanListId.equals(projPlanListIdc)) {
        			for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDto.getProjPlanListDetails()) {
        				if (projPlanListDetail.getShareProfitIndex()<Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
        					res = res.add(projPlanListDetail.getProjPlanAmount() == null ? new BigDecimal("0")
        							: projPlanListDetail.getProjPlanAmount());
						}
                    }
				}
            }
        }
        return res;
    }

    /**
     * 在planDto中更新RepaymentBizPlanListDetail
     *
     * @param bizPlanListDetail
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private void updateRepaymentBizPlanListDetailInMem(RepaymentBizPlanListDetail bizPlanListDetail, FinanceBaseDto financeBaseDto) {
        for (RepaymentBizPlanListDto planListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
                if (planListDetail.getPlanDetailId().equals(bizPlanListDetail.getPlanDetailId())) {

                    planListDetail = bizPlanListDetail;

                }
            }
        }
    }

    /**
     * 在planDto中找出RepaymentProjPlanList
     *
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private RepaymentProjPlanList findRepaymentProjPlanList(String projPlanListId, FinanceBaseDto financeBaseDto) {
        for (RepaymentProjPlanDto projPlanDto : financeBaseDto.getPlanDto().getProjPlanDtos()) {
            for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
                if (projPlanListDto.getRepaymentProjPlanList().getProjPlanListId().equals(projPlanListId)) {
                    return projPlanListDto.getRepaymentProjPlanList();
                }
            }
        }
        return null;
    }

    /**
     * 在planDto中更新RepaymentProjPlanList
     *
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private void updateRepaymentProjPlanListInMem(RepaymentProjPlanList projPlanList, FinanceBaseDto financeBaseDto) {
        for (RepaymentProjPlanDto projPlanDto : financeBaseDto.getPlanDto().getProjPlanDtos()) {
            for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
                if (projPlanListDto.getRepaymentProjPlanList().getProjPlanListId()
                        .equals(projPlanList.getProjPlanListId())) {
                    projPlanListDto.setRepaymentProjPlanList(projPlanList);
                }
            }
        }
    }

    /**
     * 在planDto中找出Repaymentbizplanlist
     *
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private RepaymentBizPlanList findRepaymentbizplanlist(String bizPlanListId, FinanceBaseDto financeBaseDto) {
        for (RepaymentBizPlanListDto bizPlanListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            if (bizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(bizPlanListId)) {
                return bizPlanListDto.getRepaymentBizPlanList();
            }
        }
        return null;
    }

    /**
     * 计算bizPlanList实还金额
     *
     * @param bizPlanListId
     * @return
     * @author 王继光 2018年6月15日 下午7:45:42
     */
    private BigDecimal sumBizPlanListFactAmount(String bizPlanListId, FinanceBaseDto financeBaseDto) {
        BigDecimal res = new BigDecimal("0");
        
        for (RepaymentProjPlanDto projPlanDto : financeBaseDto.getPlanDto().getProjPlanDtos()) {
			for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
				if (projPlanListDto.getRepaymentProjPlanList().getPlanListId().equals(bizPlanListId)) {
					for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDto.getProjPlanListDetails()) {
						res = res.add(projPlanListDetail.getProjFactAmount() == null ? new BigDecimal("0")
	                            : projPlanListDetail.getProjFactAmount());
					}
				}
			}
		}
        
        /*for (RepaymentBizPlanListDto bizPlanListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            for (RepaymentBizPlanListDetail bizPlanListDetail : bizPlanListDto.getBizPlanListDetails()) {
                if (bizPlanListDetail.getPlanListId().equals(bizPlanListId)) {
                    res = res.add(bizPlanListDetail.getFactAmount() == null ? new BigDecimal("0")
                            : bizPlanListDetail.getFactAmount());
                }
            }
        }*/
        return res;
    }
    
    /**
     * 计算bizPlanList线上部分总金额
     * @author 王继光
     * 2018年7月10日 下午4:35:32
     * @param bizPlanListId
     * @param financeBaseDto
     * @return
     */
    private BigDecimal sumBizPlanListOnlinePartAmount(String bizPlanListId, FinanceBaseDto financeBaseDto) {
        BigDecimal res = new BigDecimal("0");
        for (RepaymentBizPlanListDto bizPlanListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            for (RepaymentBizPlanListDetail bizPlanListDetail : bizPlanListDto.getBizPlanListDetails()) {
                if (bizPlanListDetail.getShareProfitIndex() < Constant.ONLINE_OFFLINE_FEE_BOUNDARY) {
                		 res = res.add(bizPlanListDetail.getPlanAmount() == null ? new BigDecimal("0")
                                 : bizPlanListDetail.getPlanAmount());
                }
            }
        }
        return res;
    }

    /**
     * 在planDto中更新Repaymentbizplanlist
     *
     * @return
     * @author 王继光 2018年6月15日 下午7:22:09
     */
    private void updateRepaymentBizPlanList(RepaymentBizPlanList bizPlanList, FinanceBaseDto financeBaseDto) {
        for (RepaymentBizPlanListDto bizPlanListDto : financeBaseDto.getPlanDto().getBizPlanListDtos()) {
            if (bizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(bizPlanList.getPlanListId())) {
                bizPlanListDto.setRepaymentBizPlanList(bizPlanList);
            }
        }
    }



    private void updateStatus(ConfirmRepaymentReq req, FinanceBaseDto financeBaseDto) {
        updateInMem(req, financeBaseDto);
        RepaymentConfirmLog confirmLog = financeBaseDto.getConfirmLog();
        confirmLog.setFactAmount(financeBaseDto.getRepayFactAmount());
        // confirmLog.get().setRepayDate(planList.getFactRepayDate());
        confirmLog.setProjPlanJson(JSON.toJSONString(financeBaseDto.getProjListDetails()));
        confirmLog.setPeriod(financeBaseDto.getPlanDto().getBizPlanListDtos().get(0).getRepaymentBizPlanList().getPeriod());
        confirmLog.setIsCancelled(0);
        confirmLog.insert();
        
//        updateRemark(financeBaseDto);
        updateRemarkNew(financeBaseDto);
        String confirmLogId = confirmLog.getConfirmLogId();
        RepaymentBizPlanBak repaymentBizPlanBak = financeBaseDto.getRepaymentBizPlanBak();
        RepaymentBizPlanListBak repaymentBizPlanListBak = financeBaseDto.getRepaymentBizPlanListBak();
        repaymentBizPlanBak.setConfirmLogId(confirmLogId);
        repaymentBizPlanBak.insert();
        repaymentBizPlanListBak.setConfirmLogId(confirmLogId);
        repaymentBizPlanListBak.insert();

        for (RepaymentBizPlanListDetailBak planListDetailBak : financeBaseDto.getRepaymentBizPlanListDetailBaks()) {
            planListDetailBak.setConfirmLogId(confirmLogId);
            planListDetailBak.insert();
        }

        for (RepaymentProjPlanBak projPlanBak : financeBaseDto.getRepaymentProjPlanBaks()) {
            projPlanBak.setConfirmLogId(confirmLogId);
            projPlanBak.insert();
        }

        for (RepaymentProjPlanListBak projPlanListBak : financeBaseDto.getRepaymentProjPlanListBaks()) {
            projPlanListBak.setConfirmLogId(confirmLogId);
            projPlanListBak.insert();
        }

        for (RepaymentProjPlanListDetailBak projPlanListDetailBak : financeBaseDto.getRepaymentProjPlanListDetailBaks()) {
            projPlanListDetailBak.setConfirmLogId(confirmLogId);
            projPlanListDetailBak.insert();
        }
        List<RepaymentProjPlanList>  ptojPlanList = removeDuplicateProjPlist(financeBaseDto.getCurTimeRepaidProjPlanList());
//        String afterId = financeBaseDto.getAfterId();
        //排除掉已经推送过数据给合规化还款模块的标的还款计划list
//        for(RepaymentProjPlanList projPlanList: ptojPlanList){
//            List<RepaymentConfirmPlatRepayLog>  ll= repaymentConfirmPlatRepayLogService.selectList
//                    (new EntityWrapper<RepaymentConfirmPlatRepayLog>().
//                            eq("proj_plan_list_id",projPlanList.getProjPlanListId()));
//            if(ll!=null && ll.size()>0){
//                ptojPlanList.remove(projPlanList);
//            }
//        }
        Iterator<RepaymentProjPlanList> iter = ptojPlanList.iterator();
        while(iter.hasNext()){
            RepaymentProjPlanList  projPlanList = iter.next();
            List<RepaymentConfirmPlatRepayLog>  ll= repaymentConfirmPlatRepayLogService.selectList
                    (new EntityWrapper<RepaymentConfirmPlatRepayLog>().
                            eq("proj_plan_list_id",projPlanList.getProjPlanListId()));
            if(ll!=null && ll.size()>0){
                iter.remove();
            }
        }


        //将本次推送的标还款计划listId记录下来
        for(RepaymentProjPlanList projPlanList: ptojPlanList){
            RepaymentConfirmPlatRepayLog log = new RepaymentConfirmPlatRepayLog();
            log.setConfirmLogId(confirmLogId);
            log.setProjPlanListId(projPlanList.getProjPlanListId());
            log.setCreateTime(new Date());
            log.setCreateUser(loginUserInfoHelper.getUserId()==null?Constant.ADMIN_ID:loginUserInfoHelper.getUserId());
            repaymentConfirmPlatRepayLogService.insert(log);
        }


        String businessId = financeBaseDto.getBusinessId();

        tdrepayRechargeThread( ptojPlanList, confirmLogId);

        //下面要触发往信贷更新还未计划数据，直接调用open中的接口方法。 张贵宏 2018.06.28
        executor.execute(() -> {
            logger.info("触发往信贷更新还未计划数据开始，businessId:[{}]", businessId);
            //睡一下，让还款的信息先存完。
            try{
                Thread.sleep(1000*60);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
            updateRepayPlanToLMS(businessId);
            logger.info("触发往信贷更新还未计划数据结束，businessId:[{}]", businessId);
        });
    }

    public void tdrepayRechargeThread(List<RepaymentProjPlanList> ptojPlanList,String confirmLogId){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("调用平台合规化还款接口开始，confirmLogId：{}", confirmLogId);
                try {
                    //睡一下，让还款的信息先存完。
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        logger.error(e.getMessage(), e);
                    }
                    tdrepayRecharge(ptojPlanList);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
                logger.info("调用平台合规化还款接口结束");
            }
        });
    }

    private static ArrayList<RepaymentProjPlanList> removeDuplicateProjPlist(List<RepaymentProjPlanList> projPlanLists) {
        Set<RepaymentProjPlanList> set = new TreeSet<RepaymentProjPlanList>(new Comparator<RepaymentProjPlanList>() {
            @Override
            public int compare(RepaymentProjPlanList o1, RepaymentProjPlanList o2) {
                //字符串,则按照asicc码升序排列
                return o1.getProjPlanId().compareTo(o2.getProjPlanId());
            }
        });
        set.addAll(projPlanLists);
        return new ArrayList<RepaymentProjPlanList>(set);
    }


    /*
     *  还款计划相关数据有变更后需要向信贷系统推送最新数据
     *
     * @param businessId 业务id
     * @return void
     * @author 张贵宏
     * @date 2018/6/28 17:32
     */
    @Override
    public void updateRepayPlanToLMS(String businessId) {
        Result result = null;
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("businessId", businessId);
        
        IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLogService.createIssueSendOutsideLog(
    			loginUserInfoHelper.getUserId(), 
    			JSON.toJSONString(paramMap), 
    			Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS, 
    			Constant.INTERFACE_NAME_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS, 
    			Constant.SYSTEM_CODE_EIP) ;
        issueSendOutsideLog.setBusinessId(businessId);
        try {
        	
            result = almsOpenServiceFeignClient.updateRepayPlanToLMS(paramMap);
            if (result == null || !"1".equals(result.getCode())) {

            	issueSendOutsideLog.setReturnJson(JSON.toJSONString(result));
            	issueSendOutsideLogService.save(issueSendOutsideLog);
                sysApiCallFailureRecordService.save(
                        AlmsServiceNameEnums.FINANCE,
                        Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS,
                        Constant.INTERFACE_NAME_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS,
                        businessId, JSON.toJSONString(paramMap), null, JSON.toJSONString(result), null, loginUserInfoHelper.getUserId() == null ? "null" : loginUserInfoHelper.getUserId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            issueSendOutsideLog.setReturnJson(null);
        	issueSendOutsideLogService.save(issueSendOutsideLog);
            sysApiCallFailureRecordService.save(
                    AlmsServiceNameEnums.FINANCE,
                    Constant.INTERFACE_CODE_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS,
                    Constant.INTERFACE_NAME_OPEN_REPAYPLAN_UPDATEREPAYPLANTOLMS,
                    businessId, JSON.toJSONString(paramMap), null, e.getMessage(), null, loginUserInfoHelper.getUserId() == null ? "null" : loginUserInfoHelper.getUserId());
        }
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

    public static void main(String[] args) {

        List<RepaymentProjPlanDto> repaymentProjPlanDtos = new LinkedList<>();

        RepaymentProjPlanDto repaymentProjPlanDto1 = new RepaymentProjPlanDto();
        TuandaiProjectInfo tuandaiProjectInfo1 = new TuandaiProjectInfo();
        repaymentProjPlanDto1.setTuandaiProjectInfo(tuandaiProjectInfo1);
        tuandaiProjectInfo1.setMasterIssueId("1111111");
        tuandaiProjectInfo1.setProjectId("111112");
        tuandaiProjectInfo1.setQueryFullSuccessDate(new Date());
        RepaymentProjPlan repaymentProjPlan1 = new RepaymentProjPlan();
        repaymentProjPlan1.setBorrowMoney(new BigDecimal("100"));
        repaymentProjPlanDto1.setRepaymentProjPlan(repaymentProjPlan1);
        repaymentProjPlanDtos.add(repaymentProjPlanDto1);

        RepaymentProjPlanDto repaymentProjPlanDto3 = new RepaymentProjPlanDto();
        TuandaiProjectInfo tuandaiProjectInfo3 = new TuandaiProjectInfo();
        repaymentProjPlanDto3.setTuandaiProjectInfo(tuandaiProjectInfo3);
        tuandaiProjectInfo3.setMasterIssueId("1111111");
        tuandaiProjectInfo3.setProjectId("1111113");
        tuandaiProjectInfo3.setQueryFullSuccessDate(new Date());
        RepaymentProjPlan repaymentProjPlan3 = new RepaymentProjPlan();
        repaymentProjPlan3.setBorrowMoney(new BigDecimal("20"));
        repaymentProjPlanDto3.setRepaymentProjPlan(repaymentProjPlan3);
        repaymentProjPlanDtos.add(repaymentProjPlanDto3);

        RepaymentProjPlanDto repaymentProjPlanDto2 = new RepaymentProjPlanDto();
        TuandaiProjectInfo tuandaiProjectInfo2 = new TuandaiProjectInfo();
        repaymentProjPlanDto2.setTuandaiProjectInfo(tuandaiProjectInfo2);
        tuandaiProjectInfo2.setMasterIssueId("1111111");
        tuandaiProjectInfo2.setProjectId("1111111");
        tuandaiProjectInfo2.setQueryFullSuccessDate(new Date());
        RepaymentProjPlan repaymentProjPlan2 = new RepaymentProjPlan();
        repaymentProjPlan2.setBorrowMoney(new BigDecimal("10"));
        repaymentProjPlanDto2.setRepaymentProjPlan(repaymentProjPlan2);
        repaymentProjPlanDtos.add(repaymentProjPlanDto2);

        ProjPlanDtoUtil.sort(repaymentProjPlanDtos);
        
        logger.info(JSON.toJSONString(repaymentProjPlanDtos));
        for (RepaymentProjPlanDto repaymentProjPlanDto : repaymentProjPlanDtos) {

            logger.info("满标时间{}"
                    + DateUtil.formatDate(repaymentProjPlanDto.getTuandaiProjectInfo().getQueryFullSuccessDate()));
            logger.info("借款金额{}" + repaymentProjPlanDto.getRepaymentProjPlan().getBorrowMoney());
            logger.info("是否主借标{}" + repaymentProjPlanDto.getTuandaiProjectInfo().getMasterIssueId()
                    .equals(repaymentProjPlanDto.getTuandaiProjectInfo().getProjectId()));

        }

    }
    // public static
}
