package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanListDetailDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanListDto;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanBak;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListBak;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetailBak;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanBak;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListBak;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetailBak;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.RepaymentResourceService;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.service.ShareProfitService;
import com.ht.ussp.bean.LoginUserInfoHelper;

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

	private ThreadLocal<String> businessId = new ThreadLocal<String>();
	private ThreadLocal<String> orgBusinessId = new ThreadLocal<String>();
	private ThreadLocal<String> afterId = new ThreadLocal<String>();
	private ThreadLocal<List<CurrPeriodProjDetailVO>> projListDetails = new ThreadLocal<List<CurrPeriodProjDetailVO>>();
	private ThreadLocal<List<RepaymentResource>> repaymentResources = new ThreadLocal<List<RepaymentResource>>();
	// 业务还款计划dto
	private ThreadLocal<RepaymentBizPlanDto> planDto = new ThreadLocal<RepaymentBizPlanDto>();

	/**
	 * 总应还金额（减去实还后的金额）
	 */
	private ThreadLocal<BigDecimal> repayPlanAmount = new ThreadLocal<BigDecimal>();
	/**
	 * 总实还金额
	 */
	private ThreadLocal<BigDecimal> repayFactAmount = new ThreadLocal<BigDecimal>();
	/**
	 * 总银行流水金额
	 */
	private ThreadLocal<BigDecimal> moneyPoolAmount = new ThreadLocal<BigDecimal>();
	/**
	 * 本次还款后,结余金额
	 */
	private ThreadLocal<BigDecimal> surplusAmount = new ThreadLocal<BigDecimal>();
	/**
	 * 本次还款后,缺多少金额
	 */
	private ThreadLocal<BigDecimal> lackAmount = new ThreadLocal<BigDecimal>();
	private ThreadLocal<String> remark = new ThreadLocal<String>();
	private ThreadLocal<Boolean> save = new ThreadLocal<Boolean>();
	private ThreadLocal<RepaymentConfirmLog> confirmLog = new ThreadLocal<RepaymentConfirmLog>();
	private ThreadLocal<RepaymentBizPlanBak> repaymentBizPlanBak = new ThreadLocal<RepaymentBizPlanBak>();
	private ThreadLocal<RepaymentBizPlanListBak> repaymentBizPlanListBak = new ThreadLocal<RepaymentBizPlanListBak>();
	private ThreadLocal<List<RepaymentBizPlanListDetailBak>> repaymentBizPlanListDetailBaks = new ThreadLocal<List<RepaymentBizPlanListDetailBak>>();
	private ThreadLocal<List<RepaymentProjPlanBak>> repaymentProjPlanBaks = new ThreadLocal<List<RepaymentProjPlanBak>>();
	private ThreadLocal<List<RepaymentProjPlanListBak>> repaymentProjPlanListBaks = new ThreadLocal<List<RepaymentProjPlanListBak>>();
	private ThreadLocal<List<RepaymentProjPlanListDetailBak>> repaymentProjPlanListDetailBaks = new ThreadLocal<List<RepaymentProjPlanListDetailBak>>();

	private ThreadLocal<List<Integer>> repaySource = new ThreadLocal<List<Integer>>();
	private ThreadLocal<List<RepaymentProjPlanListDetail>> updatedProjPlanDetails = new ThreadLocal<List<RepaymentProjPlanListDetail>>();

	private void initVariable(ConfirmRepaymentReq req) {
		businessId.set(req.getBusinessId());
		afterId.set(req.getAfterId());
		remark.set(req.getRemark());
		projListDetails.set(new ArrayList<>());
		repaymentResources.set(new ArrayList<>());
		repayPlanAmount.set(new BigDecimal("0"));
		repayFactAmount.set(new BigDecimal("0"));
		moneyPoolAmount.set(new BigDecimal("0"));
		surplusAmount.set(new BigDecimal("0"));
		lackAmount.set(new BigDecimal("0"));
		repaySource.set(req.getRepaySource());
		updatedProjPlanDetails.set(new ArrayList<>());
		confirmLog.set(createConfirmLog());
		repaymentBizPlanListDetailBaks.set(new ArrayList<>());
		repaymentProjPlanBaks.set(new ArrayList<>());
		repaymentProjPlanListBaks.set(new ArrayList<>());
		repaymentProjPlanListDetailBaks.set(new ArrayList<>());

	}

	@Override
	@Transactional(rollbackFor = { ServiceRuntimeException.class, Exception.class })
	public List<CurrPeriodProjDetailVO> execute(ConfirmRepaymentReq req, boolean save) {
		this.save.set(save);
		if (req == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq 不能为空");
		}
		if (req.getBusinessId() == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq.businessId 不能为空");
		}
		if (req.getAfterId() == null) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq.afterId 不能为空");
		}
		if ((req.getMprIds() == null || req.getMprIds().isEmpty())
				&& (req.getSurplusFund() == null || req.getSurplusFund().equals(new BigDecimal("0")))
				&& (req.getLogIds() == null || req.getLogIds().isEmpty())) {
			throw new ServiceRuntimeException("ConfirmRepaymentReq至少要有一个还款来源");
		}

		initVariable(req);
		BigDecimal unpaid = repaymentProjFactRepayService.caluUnpaid(businessId.get(), afterId.get());
		if (unpaid.compareTo(new BigDecimal("0")) <= 0) {
			throw new ServiceRuntimeException("不存在未还款项目");
		}
		// 设置应还金额
		repayPlanAmount.set(unpaid);
		// 设置业务还款计划信息
		planDto.set(initRepaymentBizPlanDto(req));
		sortRepaymentResource(req);
		if (repayFactAmount.get().compareTo(repayPlanAmount.get()) >= 0) {
			surplusAmount.set(repayFactAmount.get().subtract(repayPlanAmount.get()));
			logger.info("surplusAmount={}", surplusAmount.get());
			if (surplusAmount.get().compareTo(new BigDecimal("0")) > 0 && this.save.get()) {
				AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
				accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
				accountantOverRepayLog.setBusinessId(req.getBusinessId());
				accountantOverRepayLog.setCreateTime(new Date());
				accountantOverRepayLog.setCreateUser(loginUserInfoHelper.getUserId());
				accountantOverRepayLog.setFreezeStatus(0);
				accountantOverRepayLog.setIsRefund(0);
				accountantOverRepayLog.setIsTemporary(0);
				accountantOverRepayLog.setMoneyType(1);
				accountantOverRepayLog.setOverRepayMoney(surplusAmount.get());
				accountantOverRepayLog
						.setRemark(String.format("收入于%s的%s期线下财务确认", req.getBusinessId(), req.getAfterId()));
				accountantOverRepayLog.insert();

				confirmLog.get().setSurplusAmount(surplusAmount.get());
				confirmLog.get().setSurplusRefId(accountantOverRepayLog.getId().toString());
			}
		} else {
			lackAmount.set(repayPlanAmount.get().subtract(repayFactAmount.get()));
			logger.info("lackAmount={}", lackAmount.get());
		}
		caluProportion(planDto.get());
		divideOveryDueMoney(req.getOfflineOverDue(), planDto.get(), false);
		divideOveryDueMoney(req.getOnlineOverDue(), planDto.get(), true);
		fill();
		if (save) {
			updateStatus();
		}
		System.out.println(JSON.toJSONString(projListDetails));
		return projListDetails.get();
	}

	/**
	 * 根据参数创建还款来源并排序,银行流水,银行代扣,线下代扣3个优先级最高,结余优先级最低
	 * 
	 * @author 王继光 2018年5月24日 下午3:45:07
	 * @param req
	 * @return
	 */
	private void sortRepaymentResource(ConfirmRepaymentReq req) {
		// 线下代扣流水ID列表
		List<String> mprids = req.getMprIds();
		// 结余金额
		BigDecimal surplus = req.getSurplusFund();
		// 代扣银行流水ID
		List<Integer> logIds = req.getLogIds();

		// 处理线下转账
		if (mprids != null && mprids.size() > 0) {
			List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectBatchIds(mprids);
			for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepayments) {
				// 增加总实还金额
				repayFactAmount.set(repayFactAmount.get().add(moneyPoolRepayment.getAccountMoney()));
				// 增加银行流水还的金额
				moneyPoolAmount.set(moneyPoolAmount.get().add(moneyPoolRepayment.getAccountMoney()));
				RepaymentResource repaymentResource = new RepaymentResource();
				repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
				repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
				repaymentResource.setOrgBusinessId(moneyPoolRepayment.getOriginalBusinessId());
				repaymentResource.setCreateDate(new Date());
				repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
				repaymentResource.setIsCancelled(0);
				repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
				repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
				repaymentResource.setRepaySource(RepayPlanRepaySrcEnum.OFFLINE_TRANSFER.getValue().toString());
				repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());
				if (save.get()) {
					confirmLog.get().setRepayDate(repaymentResource.getRepayDate());
					repaymentResource.insert();
				}
				repaymentResources.get().add(repaymentResource);
			}
		}

		// repaySource {20：自动线下代扣已还款，21，人工线下代扣已还款，30：自动银行代扣已还款，31：人工银行代扣已还款}
		if (logIds != null && logIds.size() > 0) {
			WithholdingRepaymentLog log = withholdingRepaymentLogService.selectById(logIds.get(0));
			String repaySource = "30";
			if (log.getBindPlatformId() == PlatformEnum.YH_FORM.getValue() && log.getCreateUser().equals("auto_run")) {// 自动银行代扣已还款
				repaySource = "30";
			} else if (log.getBindPlatformId() == PlatformEnum.YH_FORM.getValue()
					&& (!log.getCreateUser().equals("auto_run"))) {// 人工银行代扣已还款
				repaySource = "31";
			} else if (log.getBindPlatformId() != PlatformEnum.YH_FORM.getValue()
					&& log.getCreateUser().equals("auto_run")) {// 20：自动线下代扣已还款
				repaySource = "20";
			} else if (log.getBindPlatformId() != PlatformEnum.YH_FORM.getValue()
					&& (!log.getCreateUser().equals("auto_run"))) {// 21，人工线下代扣已还款
				repaySource = "21";
			}
			RepaymentResource temp = repaymentResourceService.selectOne(new EntityWrapper<RepaymentResource>()
					.eq("business_id", log.getOriginalBusinessId()).eq("after_id", log.getAfterId())
					.eq("repay_source_ref_id", log.getLogId()).eq("repay_source", repaySource));
			if (temp != null) {// 已经核销过
				return;
			}

			RepaymentResource repaymentResource = new RepaymentResource();
			repaymentResource.setAfterId(log.getAfterId());
			List<BasicBusiness> basicBusiness = basicBusinessMapper.selectList(
					new EntityWrapper<BasicBusiness>().eq("source_business_id", log.getOriginalBusinessId()));
			List<RepaymentBizPlanList> ll = repaymentBizPlanListMapper
					.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("after_id", log.getAfterId())
							.eq("orig_business_id", log.getOriginalBusinessId()));
			if (ll == null || ll.size() == 0) {

			} else if (ll.size() > 1) {

			}
			repaymentResource.setBusinessId(log.getOriginalBusinessId());
			repaymentResource.setOrgBusinessId(log.getOriginalBusinessId());
			repaymentResource.setCreateDate(new Date());
			if (loginUserInfoHelper != null && loginUserInfoHelper.getUserId() != null) {
				repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
			} else {
				repaymentResource.setCreateUser("admin");
			}

			repaymentResource.setIsCancelled(0);
			repaymentResource.setRepayAmount(log.getCurrentAmount());
			repaymentResource.setRepayDate(log.getCreateTime());
			repaymentResource.setRepaySource(repaySource);
			if (save.get()) {
				confirmLog.get().setRepayDate(repaymentResource.getRepayDate());
				repaymentResource.setRepaySourceRefId(log.getLogId().toString());
				repaymentResource.insert();
			}
			repayFactAmount.set(repayFactAmount.get().add(log.getCurrentAmount()));
			repaymentResources.get().add(repaymentResource);

		}

		if (surplus != null && surplus.compareTo(new BigDecimal("0")) > 0) {
			BigDecimal canUseSurplus = accountantOverRepayLogService.caluCanUse(businessId.get(), null);
			if (surplus.compareTo(canUseSurplus) > 0) {
				throw new ServiceRuntimeException("往期结余金额不足");
			}

			AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog();
			accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
			accountantOverRepayLog.setBusinessId(req.getBusinessId());
			accountantOverRepayLog.setCreateTime(new Date());
			accountantOverRepayLog.setCreateUser(loginUserInfoHelper.getUserId());
			accountantOverRepayLog.setFreezeStatus(0);
			accountantOverRepayLog.setIsRefund(0);
			accountantOverRepayLog.setIsTemporary(0);
			accountantOverRepayLog.setMoneyType(0);
			accountantOverRepayLog.setOverRepayMoney(req.getSurplusFund());
			accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务确认", req.getBusinessId(), req.getAfterId()));
			if (save.get()) {
				accountantOverRepayLog.insert();
				confirmLog.get().setSurplusUseRefId(accountantOverRepayLog.getId().toString());

			}

			RepaymentResource repaymentResource = new RepaymentResource();
			repaymentResource.setAfterId(req.getAfterId());
			repaymentResource.setBusinessId(req.getBusinessId());
			repaymentResource.setOrgBusinessId(req.getBusinessId());
			repaymentResource.setCreateDate(new Date());
			repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
			repaymentResource.setIsCancelled(0);
			repaymentResource.setRepayAmount(req.getSurplusFund());
			repaymentResource.setRepayDate(new Date());
			repaymentResource.setRepaySource("11");
			if (save.get()) {
				repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
				repaymentResource.insert();
				if (mprids.size() == 0) {
					confirmLog.get().setRepayDate(repaymentResource.getRepayDate());
				}
			}
			repayFactAmount.set(repayFactAmount.get().add(req.getSurplusFund()));
			repaymentResources.get().add(repaymentResource);
		}

	}

	/**
	 * 查找并关联业务有关的还款计划
	 * 
	 * @author 王继光 2018年5月17日 下午9:37:57
	 * @param req
	 * @return
	 */
	private RepaymentBizPlanDto initRepaymentBizPlanDto(ConfirmRepaymentReq req) {
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

		repaymentBizPlanBak.set(new RepaymentBizPlanBak(repaymentBizPlan));
		orgBusinessId.set(repaymentBizPlan.getOriginalBusinessId());
		repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);

		List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = new ArrayList<>();
		int i = 1;
		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			logger.info("这条LOG应该只出现一次(第{}次),planlistId={},planId={}", i, repaymentBizPlanList.getPlanListId(),
					repaymentBizPlan.getPlanId());
			i++;
			repaymentBizPlanListBak.set(new RepaymentBizPlanListBak(repaymentBizPlanList));

			RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
			List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
					.selectList(new EntityWrapper<RepaymentBizPlanListDetail>()
							.eq("plan_list_id", repaymentBizPlanList.getPlanListId()).orderBy("share_profit_index")
							.orderBy("plan_item_type").orderBy("fee_id"));

			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
				repaymentBizPlanListDetailBaks.get().add(new RepaymentBizPlanListDetailBak(repaymentBizPlanListDetail));
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
			repaymentProjPlanBaks.get().add(new RepaymentProjPlanBak(repaymentProjPlan));

			RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto();
			repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
			List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(
					new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
							.eq("plan_list_id", repaymentBizPlanLists.get(0).getPlanListId())
							.orderBy("total_borrow_amount", false));

			List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = new ArrayList<>();
			for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
				repaymentProjPlanListBaks.get().add(new RepaymentProjPlanListBak(repaymentProjPlanList));

				RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
				repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
				List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
						.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
								.eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId())
								.orderBy("share_profit_index").orderBy("plan_item_type").orderBy("fee_id"));

				List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = new ArrayList<>();
				BigDecimal unpaid = new BigDecimal("0");
				for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
					repaymentProjPlanListDetailBaks.get()
							.add(new RepaymentProjPlanListDetailBak(repaymentProjPlanListDetail));

					if (repaymentProjPlanListDetail.getProjFactAmount() == null) {
						repaymentProjPlanListDetail.setProjFactAmount(new BigDecimal("0"));
					}
					unpaid = unpaid.add(repaymentProjPlanListDetail.getProjPlanAmount()
							.subtract(repaymentProjPlanListDetail.getProjFactAmount()));

					RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto = new RepaymentProjPlanListDetailDto();
					repaymentProjPlanListDetailDto.setRepaymentProjPlanListDetail(repaymentProjPlanListDetail);
					List<RepaymentProjFactRepay> repaymentProjFactRepays = repaymentProjFactRepayMapper
							.selectList(new EntityWrapper<RepaymentProjFactRepay>()
									.eq("proj_plan_detail_id", repaymentProjPlanListDetail.getProjPlanDetailId())
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
			if (projListDetails.get() == null) {
				projListDetails.set(new ArrayList<>());
			}
			projListDetails.get().add(currPeriodProjDetailVO);
			repaymentProjPlanDto.setTuandaiProjectInfo(tuandaiProjectInfo);
			repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
			repaymentProjPlanDto.setProjPlanListDtos(repaymentProjPlanListDtos);
			repaymentProjPlanDtos.add(repaymentProjPlanDto);
		}

		Collections.sort(repaymentProjPlanDtos, new Comparator<RepaymentProjPlanDto>() {
			// 排序规则说明 需补充
			@Override
			public int compare(RepaymentProjPlanDto arg0, RepaymentProjPlanDto arg1) {
				if (arg0.getTuandaiProjectInfo().getMasterIssueId()
						.equals(arg0.getTuandaiProjectInfo().getProjectId())) {
					return 1;
				}
				if (arg0.getRepaymentProjPlan().getBorrowMoney()
						.compareTo(arg1.getRepaymentProjPlan().getBorrowMoney()) < 0) {
					return -1;
				}
				if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
						.before(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
					return -1;
				}

				return 0;
			}

		});

		for (RepaymentProjPlanDto repaymentProjPlanDto2 : repaymentProjPlanDtos) {
			logger.info("满标时间{}"
					+ DateUtil.formatDate(repaymentProjPlanDto2.getTuandaiProjectInfo().getQueryFullSuccessDate()));
			logger.info("借款金额{}" + repaymentProjPlanDto2.getRepaymentProjPlan().getBorrowMoney());
			logger.info("是否主借标{}" + repaymentProjPlanDto2.getTuandaiProjectInfo().getMasterIssueId()
					.equals(repaymentProjPlanDto2.getTuandaiProjectInfo().getProjectId()));

		}
		repaymentBizPlanDto.setProjPlanDtos(repaymentProjPlanDtos);
		return repaymentBizPlanDto;

	}

	/**
	 * 分配逾期滞纳金到某还款来源中
	 * 
	 * @author 王继光 2018年5月24日 下午9:10:04
	 * @param req
	 */
	@Deprecated
	private void divideOverDueToRepayResource(ConfirmRepaymentReq req) {
		BigDecimal offLineOverDue = req.getOfflineOverDue();
		BigDecimal onLineOverDue = req.getOnlineOverDue();
		for (RepaymentResource resource : repaymentResources.get()) {
			if (offLineOverDue == null && onLineOverDue == null) {
				break;
			}
			BigDecimal surplus = resource.getRepayAmount();
			if (resource.getdOfflineOverDue() == null) {
				if (offLineOverDue != null) {
					if (surplus.compareTo(offLineOverDue) >= 0) {
						resource.setdOfflineOverDue(offLineOverDue);
						surplus = surplus.subtract(offLineOverDue);
						offLineOverDue = null;
					} else {
						offLineOverDue = offLineOverDue.subtract(surplus);
						surplus = new BigDecimal("0");
					}
				}
			}
			if (resource.getdOnlineOverDue() == null) {
				if (onLineOverDue != null) {
					if (surplus.compareTo(offLineOverDue) >= 0) {
						resource.setdOfflineOverDue(offLineOverDue);
						surplus = surplus.subtract(offLineOverDue);
						onLineOverDue = null;
					} else {
						onLineOverDue = onLineOverDue.subtract(surplus);
					}
				}
			}
		}

		if (offLineOverDue != null || onLineOverDue != null) {
			logger.info("本次所有的还款来源都不够钱还线上滞纳金 或 线下滞纳金");
			// TODO 前期开发假设所有的还款来源足够填充,后期需要优化此段逻辑
		}

	}

	/**
	 * 计算每个标的占比
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
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
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
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
	 * 计算每个标的分配下来的滞纳金金额
	 * 
	 * @author 王继光 2018年5月24日 下午9:27:27
	 * @param money
	 * @param dto
	 * @param online
	 *            true=分配线上滞纳金到标的,false=分配线下滞纳金到标的
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

	private CurrPeriodProjDetailVO getCurrPeriodProjDetailVO(String projectId) {
		for (CurrPeriodProjDetailVO currPeriodProjDetailVO : projListDetails.get()) {
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
	private void fill() {
		for (RepaymentResource resource : repaymentResources.get()) {
			BigDecimal repayAmount = resource.getRepayAmount();
			logger.info("还款来源{}金额={}", resource.getRepayAmount());
			/* 每笔还款来源按比例分配金额到标的 */
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

	/**
	 * 将填充到实还的资金拷贝一份填充到CurrPeriodProjDetailVO
	 * 
	 * @author 王继光 2018年5月24日 下午11:44:50
	 * @param amount
	 * @param detail
	 * @param vo
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
	 * @author 王继光 2018年5月24日 下午11:45:26
	 * @param divideAmount
	 * @param detail
	 * @param vo
	 * @return
	 */
	private RepaymentProjFactRepay createProjFactRepay(BigDecimal divideAmount, RepaymentProjPlanListDetail detail,
			CurrPeriodProjDetailVO vo, RepaymentResource resource) {
		RepaymentProjFactRepay fact = new RepaymentProjFactRepay();
		fact.setAfterId(afterId.get());
		fact.setBusinessId(businessId.get());
		fact.setCreateDate(new Date());
		fact.setCreateUser(loginUserInfoHelper.getUserId());
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
		fact.setConfirmLogId(confirmLog.get().getConfirmLogId());
		fact.setFactAmount(divideAmount);
		detail.setProjFactAmount(detail.getProjFactAmount().add(divideAmount));
		rendCurrPeriodProjDetailVO(divideAmount, detail, vo);
		if (save.get()) {
			fact.setProjPlanDetailRepayId(UUID.randomUUID().toString());
			fact.insert();
			detail.setRepaySource(Integer.valueOf(resource.getRepaySource()));
			detail.setFactRepayDate(resource.getRepayDate());
			detail.setUpdateDate(new Date());
			detail.setUpdateUser(loginUserInfoHelper.getUserId());
			detail.updateById();
			updatedProjPlanDetails.get().add(detail);
			// update(detail);
		}

		return fact;
	}

	/**
	 * 在内存中更新数据
	 * 
	 * @author 王继光 2018年6月15日 下午8:26:42
	 */
	public void updateInMem() {
		for (RepaymentProjPlanListDetail detail : updatedProjPlanDetails.get()) {
			RepaymentBizPlanListDetail planListDetail = findRepaymentBizPlanListDetail(detail.getPlanDetailId());
			if (planListDetail == null) {
				throw new ServiceRuntimeException("找不到对应的planListDetail");
			}
			planListDetail.setFactAmount(detail.getProjFactAmount().add(
					planListDetail.getFactAmount() == null ? new BigDecimal("0") : planListDetail.getFactAmount()));
			planListDetail.setFactRepayDate(detail.getFactRepayDate());
			planListDetail.setRepaySource(detail.getRepaySource());
			planListDetail.setUpdateDate(new Date());
			planListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
			planListDetail.updateById();
			updateRepaymentBizPlanListDetailInMem(planListDetail);

			RepaymentProjPlanList projPlanList = findRepaymentProjPlanList(detail.getProjPlanListId());

			if (projPlanList == null) {
				throw new ServiceRuntimeException("找不到对应的projPlanList");
			}
			projPlanList.setFactRepayDate(confirmLog.get().getRepayDate());
			projPlanList.setRemark(remark.get());
			BigDecimal pjlFactAmount = sumProjPlanListFactAmountInMem(projPlanList.getProjPlanListId());

			/* 如果实还大于应还+逾期 */
			if (pjlFactAmount.compareTo(projPlanList.getTotalBorrowAmount()
					.add(projPlanList.getOverdueAmount() == null ? new BigDecimal("0")
							: projPlanList.getOverdueAmount())) >= 0) {
				projPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
				projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
				setRepayConfirmedFlag(projPlanList);
			} else {
				projPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
				projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
				projPlanList.setRepayFlag(null);
			}
			projPlanList.setUpdateTime(new Date());
			projPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
			projPlanList.updateAllColumnById();
			updateRepaymentProjPlanListInMem(projPlanList);

			RepaymentBizPlanList bizPlanList = findRepaymentbizplanlist(projPlanList.getPlanListId());
			bizPlanList.setFactRepayDate(confirmLog.get().getRepayDate());
			bizPlanList.setRemark(remark.get());

			BigDecimal bplFactAmount = sumBizPlanListFactAmount(bizPlanList.getPlanListId());
			;
			if (bplFactAmount.compareTo(
					bizPlanList.getTotalBorrowAmount().add(bizPlanList.getOverdueAmount() == null ? new BigDecimal("0")
							: bizPlanList.getOverdueAmount())) >= 0) {
				bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
				bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
				bizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
				setRepayConfirmedFlag(bizPlanList);
			} else {
				bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
				bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
				projPlanList.setRepayFlag(null);
				if (bplFactAmount.compareTo(bizPlanList.getTotalBorrowAmount()) >= 0) {
					bizPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
				} else {
					bizPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
				}
			}
			bizPlanList.updateAllColumnById();
			updateRepaymentBizPlanList(bizPlanList);
		}
	}

	public void update(RepaymentProjPlanListDetail detail) {
		RepaymentBizPlanListDetail planListDetail = repaymentBizPlanListDetailMapper
				.selectList(
						new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_detail_id", detail.getPlanDetailId()))
				.get(0);

		planListDetail.setFactAmount(detail.getProjFactAmount()
				.add(planListDetail.getFactAmount() == null ? new BigDecimal("0") : planListDetail.getFactAmount()));
		planListDetail.setFactRepayDate(detail.getFactRepayDate());
		planListDetail.setRepaySource(detail.getRepaySource());
		planListDetail.setUpdateDate(new Date());
		planListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
		planListDetail.updateById();

		RepaymentProjPlanList projPlanList = repaymentProjPlanListMapper
				.selectList(
						new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_list_id", detail.getProjPlanListId()))
				.get(0);
		projPlanList.setFactRepayDate(confirmLog.get().getRepayDate());
		projPlanList.setRemark(remark.get());
		BigDecimal pjlFactAmount = repaymentProjPlanListMapper
				.caluProjPlanListFactAmount(projPlanList.getProjPlanListId());

		/* 如果实还大于应还+逾期 */
		if (pjlFactAmount.compareTo(
				projPlanList.getTotalBorrowAmount().add(projPlanList.getOverdueAmount() == null ? new BigDecimal("0")
						: projPlanList.getOverdueAmount())) >= 0) {
			projPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
			projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
			setRepayConfirmedFlag(projPlanList);
		} else {
			projPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
			projPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
			projPlanList.setRepayFlag(null);
		}
		projPlanList.setUpdateTime(new Date());
		projPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
		projPlanList.updateAllColumnById();

		RepaymentBizPlanList bizPlanList = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_list_id", projPlanList.getPlanListId()))
				.get(0);
		bizPlanList.setFactRepayDate(confirmLog.get().getRepayDate());
		bizPlanList.setRemark(remark.get());

		BigDecimal bplFactAmount = repaymentBizPlanListMapper.caluBizPlanListFactAmount(bizPlanList.getPlanListId());
		if (bplFactAmount.compareTo(bizPlanList.getTotalBorrowAmount().add(
				bizPlanList.getOverdueAmount() == null ? new BigDecimal("0") : bizPlanList.getOverdueAmount())) >= 0) {
			bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
			bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
			bizPlanList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
			setRepayConfirmedFlag(bizPlanList);
		} else {
			bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());
			bizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYING.getName());
			projPlanList.setRepayFlag(null);
			if (bplFactAmount.compareTo(bizPlanList.getTotalBorrowAmount()) >= 0) {
				bizPlanList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
			} else {
				bizPlanList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
			}
		}
	}

	/**
	 * 设置还款确认状态
	 * 
	 * @author 王继光 2018年6月15日 上午11:29:49
	 * @param projPlanList
	 */
	private void setRepayConfirmedFlag(RepaymentProjPlanList projPlanList) {
		RepaymentResource repaymentResource = repaymentResources.get().get(repaymentResources.get().size() - 1);
		if (repaymentResource.getRepaySource().equals(10)) {
			projPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(11)) {
			projPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(20)) {
			projPlanList.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(30)) {
			projPlanList.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
		}
	}

	/**
	 * 设置还款确认状态
	 * 
	 * @author 王继光 2018年6月15日 上午11:29:49
	 * @param projPlanList
	 */
	private void setRepayConfirmedFlag(RepaymentBizPlanList bizjPlanList) {
		RepaymentResource repaymentResource = repaymentResources.get().get(repaymentResources.get().size() - 1);
		if (repaymentResource.getRepaySource().equals(10)) {
			bizjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(11)) {
			bizjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(20)) {
			bizjPlanList.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
		}
		if (repaymentResource.getRepaySource().equals(30)) {
			bizjPlanList.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
		}
	}

	private RepaymentConfirmLog createConfirmLog() {
		RepaymentConfirmLog repaymentConfirmLog = new RepaymentConfirmLog();
		repaymentConfirmLog.setAfterId(afterId.get());
		repaymentConfirmLog.setBusinessId(businessId.get());
		repaymentConfirmLog.setCanRevoke(1);
		repaymentConfirmLog.setConfirmLogId(UUID.randomUUID().toString());
		repaymentConfirmLog.setCreateTime(new Date());
		repaymentConfirmLog.setCreateUser(loginUserInfoHelper.getUserId());
		List<RepaymentConfirmLog> repaymentConfirmLogs = repaymentConfirmLog.selectList(new EntityWrapper<>()
				.eq("business_id", businessId.get()).eq("after_id", afterId.get()).orderBy("`idx`", false));
		if (repaymentConfirmLogs == null) {
			repaymentConfirmLog.setIdx(1);
		} else {
			if (repaymentConfirmLogs.size() == 0) {
				repaymentConfirmLog.setIdx(1);
			} else {
				repaymentConfirmLog.setIdx(repaymentConfirmLogs.get(0).getIdx() + 1);
			}
		}
		repaymentConfirmLog.setOrgBusinessId(orgBusinessId.get());
		repaymentConfirmLog.setProjPlanJson(JSON.toJSONString(projListDetails.get()));
		return repaymentConfirmLog;
	}

	private List<RepaymentProjPlanListDetail> findProjPlanListDetailByPlanDetailId(String planDetailId) {
		List<RepaymentProjPlanListDetail> details = new ArrayList<>();
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
			List<RepaymentProjPlanListDto> projPlanListDtos = projPlanDto.getProjPlanListDtos();
			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				for (RepaymentProjPlanListDetail detail : projPlanListDto.getProjPlanListDetails()) {
					if (detail.getPlanDetailId().equals(planDetailId)) {
						details.add(detail);
					}
				}
			}
		}
		return details;
	}

	/**
	 * 在planDto中找出RepaymentBizPlanListDetail
	 * 
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private RepaymentBizPlanListDetail findRepaymentBizPlanListDetail(String bizPlanListDetail) {
		for (RepaymentBizPlanListDto planListDto : planDto.get().getBizPlanListDtos()) {
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
	 * @author 王继光 2018年6月15日 下午7:40:55
	 * @param projPlanListId
	 * @return
	 */
	private BigDecimal sumProjPlanListFactAmountInMem(String projPlanListId) {
		BigDecimal res = new BigDecimal("0");
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
			for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
				for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDto.getProjPlanListDetails()) {
					res = res.add(projPlanListDetail.getProjFactAmount() == null ? new BigDecimal("0")
							: projPlanListDetail.getProjFactAmount());
				}
			}
		}
		return res;
	}

	/**
	 * 在planDto中更新RepaymentBizPlanListDetail
	 * 
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private void updateRepaymentBizPlanListDetailInMem(RepaymentBizPlanListDetail bizPlanListDetail) {
		for (RepaymentBizPlanListDto planListDto : planDto.get().getBizPlanListDtos()) {
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
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private RepaymentProjPlanList findRepaymentProjPlanList(String projPlanListId) {
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
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
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private void updateRepaymentProjPlanListInMem(RepaymentProjPlanList projPlanList) {
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
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
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private RepaymentBizPlanList findRepaymentbizplanlist(String bizPlanListId) {
		for (RepaymentBizPlanListDto bizPlanListDto : planDto.get().getBizPlanListDtos()) {
			if (bizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(bizPlanListId)) {
				return bizPlanListDto.getRepaymentBizPlanList();
			}
		}
		return null;
	}

	/**
	 * 计算bizPlanList实还金额
	 * 
	 * @author 王继光 2018年6月15日 下午7:45:42
	 * @param bizPlanListId
	 * @return
	 */
	private BigDecimal sumBizPlanListFactAmount(String bizPlanListId) {
		BigDecimal res = new BigDecimal("0");
		for (RepaymentBizPlanListDto bizPlanListDto : planDto.get().getBizPlanListDtos()) {
			for (RepaymentBizPlanListDetail bizPlanListDetail : bizPlanListDto.getBizPlanListDetails()) {
				if (bizPlanListDetail.getPlanListId().equals(bizPlanListId)) {
					res = res.add(bizPlanListDetail.getFactAmount() == null ? new BigDecimal("0")
							: bizPlanListDetail.getFactAmount());
				}
			}
		}
		return res;
	}

	/**
	 * 在planDto中更新Repaymentbizplanlist
	 * 
	 * @author 王继光 2018年6月15日 下午7:22:09
	 * @param bizPlanListDetail
	 * @return
	 */
	private void updateRepaymentBizPlanList(RepaymentBizPlanList bizPlanList) {
		for (RepaymentBizPlanListDto bizPlanListDto : planDto.get().getBizPlanListDtos()) {
			if (bizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(bizPlanList.getPlanListId())) {
				bizPlanListDto.setRepaymentBizPlanList(bizPlanList);
			}
		}
	}

	private List<RepaymentProjPlanListDetail> findProjPlanListDetailByProjPlanListId(String projPlanListId) {
		List<RepaymentProjPlanListDetail> details = new ArrayList<>();
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
			List<RepaymentProjPlanListDto> projPlanListDtos = projPlanDto.getProjPlanListDtos();
			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				if (projPlanListDto.getRepaymentProjPlanList().getProjPlanListId().equals(projPlanListId)) {
					details.addAll(projPlanListDto.getProjPlanListDetails());
				}
			}
		}
		return details;
	}

	private List<RepaymentProjPlanListDetail> findProjPlanListDetailByPlanList(String planListId) {
		List<RepaymentProjPlanListDetail> details = new ArrayList<>();
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
			List<RepaymentProjPlanListDto> projPlanListDtos = projPlanDto.getProjPlanListDtos();
			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				if (projPlanListDto.getRepaymentProjPlanList().getPlanListId().equals(planListId)) {
					details.addAll(projPlanListDto.getProjPlanListDetails());
				}
			}
		}
		return details;
	}

	private void updateProjPlanStatus() {
		for (RepaymentProjPlanDto projPlanDto : planDto.get().getProjPlanDtos()) {
			for (RepaymentProjPlanListDto projPlanListDto : projPlanDto.getProjPlanListDtos()) {
				BigDecimal factAmount = new BigDecimal("0");
				projPlanListDto.getRepaymentProjPlanList().setFactRepayDate(confirmLog.get().getRepayDate());
				for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDto.getProjPlanListDetails()) {
					factAmount = factAmount
							.add(projPlanListDetail.getProjFactAmount() != null ? projPlanListDetail.getProjFactAmount()
									: new BigDecimal("0"));
				}
				if (factAmount.compareTo(projPlanListDto.getRepaymentProjPlanList().getTotalBorrowAmount()
						.add(projPlanListDto.getRepaymentProjPlanList().getOverdueAmount() == null ? new BigDecimal("0")
								: projPlanListDto.getRepaymentProjPlanList().getOverdueAmount())) == 0) {
					projPlanListDto.getRepaymentProjPlanList().setCurrentStatus(RepayCurrentStatusEnums.已还款.toString());
					projPlanListDto.getRepaymentProjPlanList()
							.setCurrentSubStatus(RepayCurrentStatusEnums.已还款.toString());
					RepaymentResource repaymentResource = repaymentResources.get()
							.get(repaymentResources.get().size() - 1);
					if (repaymentResource.getRepaySource().equals(10)) {
						projPlanListDto.getRepaymentProjPlanList()
								.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
					}
					if (repaymentResource.getRepaySource().equals(11)) {
						projPlanListDto.getRepaymentProjPlanList()
								.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
					}
					if (repaymentResource.getRepaySource().equals(20)) {
						projPlanListDto.getRepaymentProjPlanList()
								.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
					}
					if (repaymentResource.getRepaySource().equals(30)) {
						projPlanListDto.getRepaymentProjPlanList()
								.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
					}
					projPlanListDto.getRepaymentProjPlanList().updateAllColumnById();
				}
			}
		}

	}

	private void updateStatus() {
		// RepaymentBizPlanListDto planListDto =
		// planDto.get().getBizPlanListDtos().get(0);
		// RepaymentBizPlanList planList = planListDto.getRepaymentBizPlanList();
		// List<RepaymentBizPlanListDetail> planListDetails =
		// planListDto.getBizPlanListDetails();
		// boolean item10Repaid = false ;
		// boolean item20Repaid = false ;
		// boolean item30Repaid = false ;
		// boolean item50Repaid = false ;
		// boolean onlineOverDueRepaid = false ;
		// BigDecimal planAmount = planList.getTotalBorrowAmount();
		// BigDecimal derateAmount = planList.getDerateAmount()==null?new
		// BigDecimal("0"):planList.getDerateAmount();
		// BigDecimal lateFee = planList.getOverdueAmount()==null?new
		// BigDecimal("0"):planList.getOverdueAmount();
		// BigDecimal factAmount = new BigDecimal("0");
		//
		// for (RepaymentBizPlanListDetail planListDetail : planListDetails) {
		// BigDecimal detailtPlanAmount = planListDetail.getPlanAmount();
		// BigDecimal detailDerateAmount = planListDetail.getDerateAmount() == null ?
		// new BigDecimal("0"):planListDetail.getDerateAmount();
		// BigDecimal detailFactAmount = new BigDecimal("0");
		// List<RepaymentProjPlanListDetail> projPlanListDetails =
		// findProjPlanListDetailByPlanDetailId(planListDetail.getPlanDetailId());
		// Integer repaySource = null ;
		// Date repayDate = null ;
		//
		// for (RepaymentProjPlanListDetail projPlanListDetail : projPlanListDetails) {
		// repaySource = projPlanListDetail.getRepaySource();
		// repayDate = projPlanListDetail.getFactRepayDate();
		// detailFactAmount =
		// detailFactAmount.add(projPlanListDetail.getProjFactAmount());
		// }
		//
		// if
		// (detailFactAmount.compareTo(detailtPlanAmount.subtract(detailDerateAmount))==0)
		// {
		// //某项还完
		// switch (planListDetail.getPlanItemType()) {
		// case 10:
		// item10Repaid = true;break;
		// case 20:
		// item20Repaid = true;break;
		// case 30:
		// item30Repaid = true;
		// break;
		// case 50:
		// item50Repaid = true;
		// break;
		// case 60:
		// if
		// (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid()))
		// {
		// onlineOverDueRepaid = true;
		// }
		// break;
		// default:
		// break;
		// }
		// }
		//
		// planListDetail.setFactAmount(detailFactAmount);
		// planListDetail.setRepaySource(repaySource);
		// planListDetail.setFactRepayDate(repayDate);
		// factAmount = factAmount.add(detailFactAmount);
		// planListDetail.setFactAmount(detailFactAmount);
		// planListDetail.updateById();
		// }
		//
		// int compare =
		// factAmount.compareTo(planAmount.add(lateFee).subtract(derateAmount));
		// if (compare>=0) {
		// planList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
		// planList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
		// planList.setRepayStatus(SectionRepayStatusEnum.ALL_REPAID.getKey());
		// RepaymentResource repaymentResource =
		// repaymentResources.get().get(repaymentResources.get().size()-1);
		// if (repaymentResource.getRepaySource().equals(10)) {
		// planList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		// }
		// if (repaymentResource.getRepaySource().equals(11)) {
		// planList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
		// }
		// if (repaymentResource.getRepaySource().equals(20)) {
		// planList.setRepayFlag(RepayedFlag.AUTO_WITHHOLD_OFFLINE_REPAYED.getKey());
		// }
		// if (repaymentResource.getRepaySource().equals(30)) {
		// planList.setRepayFlag(RepayedFlag.AUTO_BANK_WITHHOLD_REPAYED.getKey());
		// }
		// }else {
		// if
		// (onlineOverDueRepaid&&item10Repaid&&item20Repaid&&item30Repaid&&item50Repaid)
		// {
		// planList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
		// }else {
		// planList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
		// }
		// }
		// planList.setFactRepayDate(confirmLog.get().getRepayDate());
		// planList.setFinanceConfirmUser(loginUserInfoHelper.getUserId());
		// planList.setFinanceComfirmDate(new Date());
		// planList.setFinanceConfirmUserName(loginUserInfoHelper.getLoginInfo()==null?"手动代扣":loginUserInfoHelper.getLoginInfo().getUserName());
		// planList.updateById();
		// updateProjPlanStatus();
		updateInMem();
		confirmLog.get().setFactAmount(repayFactAmount.get());
		// confirmLog.get().setRepayDate(planList.getFactRepayDate());
		confirmLog.get().setProjPlanJson(JSON.toJSONString(projListDetails.get()));
		confirmLog.get().setPeriod(planDto.get().getBizPlanListDtos().get(0).getRepaymentBizPlanList().getPeriod());
		confirmLog.get().insert();

		String confirmLogId = confirmLog.get().getConfirmLogId();
		repaymentBizPlanBak.get().setConfirmLogId(confirmLogId);
		repaymentBizPlanBak.get().insert();
		repaymentBizPlanListBak.get().setConfirmLogId(confirmLogId);
		repaymentBizPlanListBak.get().insert();

		for (RepaymentBizPlanListDetailBak planListDetailBak : repaymentBizPlanListDetailBaks.get()) {
			planListDetailBak.setConfirmLogId(confirmLogId);
			planListDetailBak.insert();
		}

		for (RepaymentProjPlanBak projPlanBak : repaymentProjPlanBaks.get()) {
			projPlanBak.setConfirmLogId(confirmLogId);
			projPlanBak.insert();
		}

		for (RepaymentProjPlanListBak projPlanListBak : repaymentProjPlanListBaks.get()) {
			projPlanListBak.setConfirmLogId(confirmLogId);
			projPlanListBak.insert();
		}

		for (RepaymentProjPlanListDetailBak projPlanListDetailBak : repaymentProjPlanListDetailBaks.get()) {
			projPlanListDetailBak.setConfirmLogId(confirmLogId);
			projPlanListDetailBak.insert();
		}
		
		logger.info("调用平台合规化还款接口开始，confirmLogId：{}", confirmLogId);
		tdrepayRecharge(confirmLogId);
		logger.info("调用平台合规化还款接口结束");
	}

	private void tdrepayRecharge(String confirmLogId) {
		
		SysApiCallFailureRecord record = new SysApiCallFailureRecord();
		try {
			record.setModuleName(AlmsServiceNameEnums.FINANCE.getName());
			record.setApiCode(Constant.INTERFACE_CODE_PLATREPAY_REPAYMENT);
			record.setApiName(Constant.INTERFACE_NAME_PLATREPAY_REPAYMENT);
			record.setRefId(confirmLogId);
			record.setCreateUser(loginUserInfoHelper.getUserId());
			record.setCraeteTime(new Date());
			
			Result result = null;
			String busId = this.businessId.get();
			
			RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListService
					.selectOne(new EntityWrapper<RepaymentProjPlanList>().eq("orig_business_id", busId)
							.eq("after_id", this.afterId.get()));
			
			RepaymentProjPlan plan = null;
			
			if (repaymentProjPlanList != null) {
				plan = repaymentProjPlanService.selectById(repaymentProjPlanList.getProjPlanId());
			}
			
			if (plan != null) {
				
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("confirmLogId", confirmLogId);
				paramMap.put("businessId", busId);
				paramMap.put("projectId", plan.getProjectId());
				
				record.setApiParamCiphertext(JSONObject.toJSONString(paramMap));
				
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
		sysApiCallFailureRecordService.insert(record);
	}
}
