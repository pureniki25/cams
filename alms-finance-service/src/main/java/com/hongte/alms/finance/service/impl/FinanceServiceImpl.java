/**
 * 
 */
package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.ConfirmRepaymentPreviewDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanListDto;
import com.hongte.alms.base.dto.ActualPaymentLogDTO;
import com.hongte.alms.base.dto.ActualPaymentSingleLogDTO;
import com.hongte.alms.base.dto.RepaymentPlanInfoDTO;
import com.hongte.alms.base.dto.RepaymentProjInfoDTO;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanPayedTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.vo.finance.CurrPeriodDerateInfoVO;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.service.FinanceService;
import com.hongte.alms.finance.util.kafka.KafkaUtils;
import com.ht.ussp.bean.LoginUserInfoHelper;

/**
 * @author 王继光 2018年5月7日 下午6:03:41
 */
@Service("FinanceService")
public class FinanceServiceImpl implements FinanceService {
	private static Logger logger = LoggerFactory.getLogger(FinanceServiceImpl.class);
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
	@Qualifier("RepaymentProjPlanListService")
	private RepaymentProjPlanListService repaymentProjPlanListService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService ;
	
	@Autowired
	private TransferOfLitigationMapper transferOfLitigationMapper;

	private static final String SEND_DATA_PLATFORM_SPLIT_SYMBOL = "\\t";

	@Override
	@Transactional(rollbackFor = ServiceRuntimeException.class)
	public Result appointBankStatement(RepaymentRegisterInfoDTO dto) {
		logger.info("@appointBankStatement--开始[{}]", dto);

		if (!StringUtil.isEmpty(dto.getMprid())) {
			update(dto);
			return Result.success();
		}

		RepaymentBizPlanList repaymentBizPlanList = new RepaymentBizPlanList();
		repaymentBizPlanList.setOrigBusinessId(dto.getBusinessId());
		repaymentBizPlanList.setAfterId(dto.getAfterId());
		repaymentBizPlanList = repaymentBizPlanListMapper.selectOne(repaymentBizPlanList);
		if (repaymentBizPlanList == null) {
			return Result.error("500", "找不到对应的还款计划");
		}
		Date now = new Date();
		MoneyPool moneyPool = initBy(dto);
		moneyPool.setCreateTime(now);
		moneyPool.setCreateUser(loginUserInfoHelper.getUserId());
		moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.财务指定银行流水.toString());
		moneyPool.setGainerName(loginUserInfoHelper.getUserId());
		moneyPool.setIncomeType(1);
		moneyPool.setIsManualCreate(1);
		moneyPool.setIsTemporary(0);
		moneyPool.setStatus(RepayRegisterState.已领取.toString());
		boolean mpSaveResult = moneyPool.insert();
		if (!mpSaveResult) {
			return Result.error("500", "保存银行流水mp失败");
		}
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment(dto);
		moneyPoolRepayment.setPlanListId(repaymentBizPlanList.getPlanListId());
		moneyPoolRepayment.setCreateTime(now);
		moneyPoolRepayment.setCreateUser(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setCreateUserRole(null);
		moneyPoolRepayment.setIsFinanceMatch(1);
		moneyPoolRepayment.setState(moneyPool.getFinanceStatus());
		moneyPoolRepayment.setOriginalBusinessId(dto.getBusinessId());
		moneyPoolRepayment.setAfterId(dto.getAfterId());
		moneyPoolRepayment.setOperateId(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setClaimDate(now);
		moneyPoolRepayment.setOperateName(loginUserInfoHelper.getLoginInfo().getUserName());
		moneyPoolRepayment.setOperateId(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setIncomeType(moneyPool.getIncomeType());
		moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		boolean mrpSaveResult = moneyPoolRepayment.insert();
		if (!mrpSaveResult) {
			throw new ServiceRuntimeException("保存银行流水mpr失败");
		}
		return Result.success();
	}

	private boolean update(RepaymentRegisterInfoDTO dto) {
		String mprid = dto.getMprid();
		Date now = new Date();
		if (StringUtil.isEmpty(mprid)) {
			throw new ServiceRuntimeException("mprid不能为空");
		}
		MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(mprid);
		if (StringUtil.isEmpty(moneyPoolRepayment.getMoneyPoolId())) {
			throw new ServiceRuntimeException("此还款登记没关联银行流水");
		}
		MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolRepayment.getMoneyPoolId());
		moneyPool.setAcceptBank(dto.getAcceptBank());
		moneyPool.setAccountMoney(new BigDecimal(dto.getRepaymentMoney()));
		moneyPool.setTradeType(dto.getTradeType());
		moneyPool.setTradeDate(DateUtil.getDate(dto.getRepaymentDate()));
		moneyPool.setTradeRemark(dto.getRemark() != null ? dto.getRemark() : null);
		moneyPool.setTradePlace(dto.getTradePlace() != null ? dto.getTradePlace() : null);
		moneyPool.setRemitBank(dto.getFactRepaymentUser());
		moneyPool.setUpdateTime(now);

		moneyPool.setUpdateUser(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setState(moneyPool.getFinanceStatus());
		moneyPoolRepayment.setOriginalBusinessId(dto.getBusinessId());
		moneyPoolRepayment.setAfterId(dto.getAfterId());
		moneyPoolRepayment.setOperateId(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setOperateName(loginUserInfoHelper.getLoginInfo().getUserName());
		moneyPoolRepayment.setIncomeType(moneyPool.getIncomeType());
		moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		moneyPoolRepayment.setUpdateTime(now);
		moneyPoolRepayment.setUpdateUser(loginUserInfoHelper.getUserId());

		moneyPoolRepayment.setCertificatePictureUrl(dto.getCert());
		moneyPoolRepayment.setBankAccount(dto.getAcceptBank());
		moneyPoolRepayment.setAccountMoney(new BigDecimal(dto.getRepaymentMoney()));
		moneyPoolRepayment.setTradeType(dto.getTradeType());
		moneyPoolRepayment.setTradeDate(moneyPool.getTradeDate());
		moneyPoolRepayment.setRemark(dto.getRemark());
		moneyPoolRepayment.setTradePlace(dto.getTradePlace());
		moneyPoolRepayment.setFactTransferName(dto.getFactRepaymentUser());

		boolean updateMP = moneyPool.updateAllColumnById();
		boolean updateMPR = moneyPoolRepayment.updateAllColumnById();
		if (updateMP && updateMPR) {
			return true;
		} else {
			throw new ServiceRuntimeException("更新失败");
		}
	}

	private MoneyPool initBy(RepaymentRegisterInfoDTO dto) {
		MoneyPool moneyPool = new MoneyPool();
		moneyPool.setAcceptBank(dto.getAcceptBank());
		moneyPool.setAccountMoney(new BigDecimal(dto.getRepaymentMoney()));
		moneyPool.setTradeType(dto.getTradeType());
		moneyPool.setTradeDate(DateUtil.getDate(dto.getRepaymentDate()));
		moneyPool.setTradeRemark(dto.getRemark() != null ? dto.getRemark() : null);
		moneyPool.setTradePlace(dto.getTradePlace() != null ? dto.getRemark() : null);
		moneyPool.setRemitBank(dto.getFactRepaymentUser());
		return moneyPool;
	}

	@Override
	@Transactional(rollbackFor = ServiceRuntimeException.class)
	public Result matchBankStatement(List<String> moneyPoolIds, String businessId, String afterId, String mprid) {
		if (mprid != null) {
			MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolIds.get(0));
			MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(mprid);
			moneyPoolRepayment.setAccountMoney(moneyPool.getAccountMoney());
			moneyPoolRepayment.setBankAccount(moneyPool.getAcceptBank());
			moneyPoolRepayment.setCreateUser(loginUserInfoHelper.getUserId());
			moneyPoolRepayment.setFactTransferName(moneyPool.getRemitBank());
			moneyPoolRepayment.setIncomeType(1);
			moneyPoolRepayment.setRemark(moneyPool.getTradeRemark());
			moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
			moneyPoolRepayment.setTradeDate(moneyPool.getTradeDate());
			moneyPoolRepayment.setTradePlace(moneyPool.getTradePlace());
			moneyPoolRepayment.setTradeType(moneyPool.getTradeType());

			moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
			moneyPoolRepayment.setState(RepayRegisterFinanceStatus.财务指定银行流水.toString());
			moneyPoolRepayment.setIsFinanceMatch(1);
			moneyPoolRepayment.updateById();
			return Result.success();
		}

		List<MoneyPoolRepayment> list = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>()
				.eq("original_business_id", businessId).eq("after_id", afterId).in("money_pool_id", moneyPoolIds));

		if (list != null && list.size() > 0) {
			return Result.error("500", "已存在匹配的银行流水,不可重复匹配");
		}
		Date now = new Date();
		/*
		 * if (mprid != null) { MoneyPool moneyPool =
		 * moneyPoolMapper.selectById(moneyPoolIds.get(0)); MoneyPoolRepayment
		 * moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(mprid);
		 * moneyPoolRepayment.setAccountMoney(moneyPool.getAccountMoney());
		 * moneyPoolRepayment.setBankAccount(moneyPool.getAcceptBank());
		 * moneyPoolRepayment.setCreateUser(loginUserInfoHelper.getUserId());
		 * moneyPoolRepayment.setFactTransferName(moneyPool.getRemitBank());
		 * moneyPoolRepayment.setIncomeType(1);
		 * moneyPoolRepayment.setRemark(moneyPool.getTradeRemark());
		 * moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		 * moneyPoolRepayment.setTradeDate(moneyPool.getTradeDate());
		 * moneyPoolRepayment.setTradePlace(moneyPool.getTradePlace());
		 * moneyPoolRepayment.setTradeType(moneyPool.getTradeType());
		 * 
		 * moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		 * moneyPoolRepayment.setState(RepayRegisterFinanceStatus.财务指定银行流水.toString());
		 * moneyPoolRepayment.setIsFinanceMatch(1); moneyPoolRepayment.updateById();
		 * return Result.success(); } else {
		 */
		List<MoneyPool> moneyPools = moneyPoolMapper.selectBatchIds(moneyPoolIds);
		RepaymentBizPlanList repaymentBizPlanList = new RepaymentBizPlanList();
		repaymentBizPlanList.setOrigBusinessId(businessId);
		repaymentBizPlanList.setAfterId(afterId);
		repaymentBizPlanList = repaymentBizPlanListMapper.selectOne(repaymentBizPlanList);
		for (MoneyPool moneyPool : moneyPools) {
			if (moneyPool.getStatus().equals(RepayRegisterState.完成.toString())) {
				throw new ServiceRuntimeException("已完成的银行流水不能被匹配,请刷新检查");
			}
			MoneyPoolRepayment moneyPoolRepayment = copy(moneyPool);
			moneyPoolRepayment.setOriginalBusinessId(businessId);
			moneyPoolRepayment.setAfterId(afterId);
			moneyPoolRepayment.setPlanListId(repaymentBizPlanList.getPlanListId());
			moneyPoolRepayment.setCreateTime(now);
			moneyPoolRepayment.setCreateUser(loginUserInfoHelper.getUserId());
			moneyPoolRepayment.setState(RepayRegisterFinanceStatus.财务指定银行流水.toString());
			moneyPoolRepayment.setIsFinanceMatch(1);
			boolean r = moneyPoolRepayment.insert();
		}
		return Result.success();
		// }

	}

	private MoneyPoolRepayment copy(MoneyPool moneyPool) {
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment();
		moneyPoolRepayment.setAccountMoney(moneyPool.getAccountMoney());
		moneyPoolRepayment.setBankAccount(moneyPool.getAcceptBank());
		moneyPoolRepayment.setCertificatePictureUrl(null);
		moneyPoolRepayment.setCreateUser(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setFactTransferName(moneyPool.getRemitBank());
		moneyPoolRepayment.setIncomeType(1);
		moneyPoolRepayment.setRemark(moneyPool.getTradeRemark());
		moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		moneyPoolRepayment.setTradeDate(moneyPool.getTradeDate());
		moneyPoolRepayment.setTradePlace(moneyPool.getTradePlace());
		moneyPoolRepayment.setTradeType(moneyPool.getTradeType());
		return moneyPoolRepayment;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result disMatchedBankStatement(MoneyPool moneyPool, MoneyPoolRepayment moneyPoolRepayment) {
		try {
			Date now = new Date();
			moneyPoolRepayment.setIsFinanceMatch(0);
			moneyPoolRepayment.setState(RepayRegisterFinanceStatus.未关联银行流水.toString());
			moneyPoolRepayment.setClaimDate(null);
			moneyPoolRepayment.setMoneyPoolId(null);
			moneyPoolRepayment.setUpdateTime(now);
			moneyPoolRepayment.setUpdateUser(loginUserInfoHelper.getUserId());
			boolean mprUpdateRes = moneyPoolRepayment.updateAllColumnById();
			if (!mprUpdateRes) {
				return Result.error("500", "更新还款登记数据失败");
			}
			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.未关联银行流水.toString());
			moneyPool.setStatus(RepayRegisterState.待领取.toString());
			boolean mpUpdateRes = moneyPool.updateAllColumnById();
			if (!mpUpdateRes) {
				return Result.error("500", "更新银行流水数据失败");
			}
			return Result.success();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("500", "处理发生异常");
		}
	}

	@Override
	public Result thisPeroidRepayment(String businessId, String afterId) {
		RepaymentBizPlanList rpl = new RepaymentBizPlanList();
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl);
		List<JSONObject> list = confirmLogService.selectCurrentPeriodConfirmedProjInfo(businessId, afterId);
		List<JSONObject> infos = new ArrayList<>();
		infos.add(thisPeriodPlanRepayment(rpl));
		infos.addAll(list);
		return Result.success(infos);
	}

	private JSONObject thisPeriodPlanRepayment(RepaymentBizPlanList rpl) {
		JSONObject t = initThisPeriodPlanRepaymentBase();
		t.put("type", "本期应还");
		t.put("repayDate", DateUtil.toDateString(rpl.getDueDate(), DateUtil.DEFAULT_FORMAT_DATE));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", rpl.getPlanListId()));
		BigDecimal derate = new BigDecimal("0");
		BigDecimal subtotal = new BigDecimal("0");
		BigDecimal total = new BigDecimal("0");
		t.put("item30", new BigDecimal("0"));
		t.put("item50", new BigDecimal("0"));
		t.put("onlineOverDue", new BigDecimal("0"));
		t.put("offlineOverDue", new BigDecimal("0"));
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
			if (repaymentBizPlanListDetail.getDerateAmount() != null) {
				derate = derate.add(repaymentBizPlanListDetail.getDerateAmount());
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(10)) {
				t.put("item10", repaymentBizPlanListDetail.getPlanAmount());
				subtotal = subtotal.add(repaymentBizPlanListDetail.getPlanAmount());
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(20)) {
				t.put("item20", repaymentBizPlanListDetail.getPlanAmount());
				subtotal = subtotal.add(repaymentBizPlanListDetail.getPlanAmount());
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(30)) {
				t.put("item30", repaymentBizPlanListDetail.getPlanAmount().add(t.getBigDecimal("item30")));
				subtotal = subtotal.add(repaymentBizPlanListDetail.getPlanAmount());
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(50)) {
				t.put("item50", repaymentBizPlanListDetail.getPlanAmount().add(t.getBigDecimal("item50")));
				subtotal = subtotal.add(repaymentBizPlanListDetail.getPlanAmount());
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId()
					.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
				t.put("onlineOverDue", repaymentBizPlanListDetail.getPlanAmount().add(t.getBigDecimal("onlineOverDue")));
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId()
					.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
				t.put("offlineOverDue", repaymentBizPlanListDetail.getPlanAmount().add(t.getBigDecimal("offlineOverDue")));
				total = total.add(repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
		}

		t.put("derate", derate);
		total = total.subtract(derate);
		t.put("subTotal", subtotal);
		t.put("total", total);

		List<JSONObject> projs = new ArrayList<>();
		List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListMapper
				.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("plan_list_id", rpl.getPlanListId()));
		for (RepaymentProjPlanList projPlanList : projPlanLists) {
			List<RepaymentProjPlanListDetail> projPlanListDetails = repaymentProjPlanListDetailMapper
					.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
							projPlanList.getProjPlanListId()));
			JSONObject proj = new JSONObject();
			TuandaiProjectInfo projectInfo = tuandaiProjectInfoMapper
					.selectProjectInfoByProjPlanId(projPlanList.getProjPlanId());
			proj.put("userName", projectInfo.getRealName());
			proj.put("projAmount", projectInfo.getAmount());
			proj.put("item10", BigDecimal.ZERO);
			proj.put("item20", BigDecimal.ZERO);
			proj.put("item30", BigDecimal.ZERO);
			proj.put("item50", BigDecimal.ZERO);
			proj.put("onlineOverDue", BigDecimal.ZERO);
			proj.put("offlineOverDue", BigDecimal.ZERO);
			proj.put("subTotal", BigDecimal.ZERO);
			proj.put("total", BigDecimal.ZERO);
			for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : projPlanListDetails) {
				if (repaymentProjPlanListDetail.getPlanItemType().equals(10)) {
					proj.put("item10", repaymentProjPlanListDetail.getProjPlanAmount());
					proj.put("subTotal", proj.getBigDecimal("subTotal").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(20)) {
					proj.put("item20", repaymentProjPlanListDetail.getProjPlanAmount());
					proj.put("subTotal", proj.getBigDecimal("subTotal").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(30)) {
					proj.put("item30", repaymentProjPlanListDetail.getProjPlanAmount().add(proj.getBigDecimal("item30")));
					proj.put("subTotal", proj.getBigDecimal("subTotal").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(50)) {
					proj.put("item50", repaymentProjPlanListDetail.getProjPlanAmount().add(proj.getBigDecimal("item50")));
					proj.put("subTotal", proj.getBigDecimal("subTotal").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
					proj.put("onlineOverDue", repaymentProjPlanListDetail.getProjPlanAmount().add(proj.getBigDecimal("onlineOverDue")));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
					proj.put("offlineOverDue", repaymentProjPlanListDetail.getProjPlanAmount().add(proj.getBigDecimal("offlineOverDue")));
					proj.put("total", proj.getBigDecimal("total").add(repaymentProjPlanListDetail.getProjPlanAmount()));
					continue;
				}
			}
			projs.add(proj);
		}
		t.put("list", projs);
		return t;
	}

	private JSONObject initThisPeriodPlanRepaymentBase() {
		JSONObject t = new JSONObject();
		t.put("item10", 0);
		t.put("item20", 0);
		t.put("item30", 0);
		t.put("item50", 0);
		t.put("subtotal", 0);
		t.put("offlineOverDue", 0);
		t.put("onlineOverDue", 0);
		t.put("derate", 0);
		t.put("total", 0);
		return t;
	}

	@Override
	public CurrPeriodRepaymentInfoVO getCurrPeriodRepaymentInfoVO(String businessId, String afterId , String repayDate) {
		CurrPeriodRepaymentInfoVO c = new CurrPeriodRepaymentInfoVO();
		c.setMoneyPoolRepayDates(new LinkedHashSet<>());
		
		RepaymentBizPlanList rpl = new RepaymentBizPlanList();
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl);
		
		/*根据最后一条实还流水的时间重新计算滞纳金*/
		List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectList(
				new EntityWrapper<MoneyPoolRepayment>().eq("plan_list_id", rpl.getPlanListId()).isNotNull("money_pool_id").andNew("state",RepayRegisterFinanceStatus.未关联银行流水.toString()).or().eq("state", RepayRegisterFinanceStatus.财务指定银行流水.toString()).orderBy("trade_date",false));
		/*将流水时间收集,返回前端*/
		for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepayments) {
			c.getMoneyPoolRepayDates().add(DateUtil.formatDate(moneyPoolRepayment.getTradeDate()));
		}
		/*将流水时间收集,返回前端*/
		/*别少当天*/
		c.getMoneyPoolRepayDates().add(DateUtil.formatDate(new Date()));
		/*别少当天*/
		
		
		Date factRepayDate = new Date() ;
		if (!CollectionUtils.isEmpty(moneyPoolRepayments)) {
			factRepayDate = moneyPoolRepayments.get(0).getTradeDate() ;
		}
		if (!StringUtil.isEmpty(repayDate)) {
			factRepayDate = DateUtil.getDate(repayDate);
		}
		rpl.setFactRepayDate(factRepayDate);
		rpl = repaymentProjPlanListService.calLateFeeForPerPList(rpl, 1);
		/*根据最后一条实还流水的时间重新计算滞纳金*/
		
		c.setRepayDate(rpl.getDueDate());
		if (rpl.getOverdueDays() != null) {
			c.setOverDays(rpl.getOverdueDays().intValue());
		}
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", rpl.getPlanListId()));
		for (RepaymentBizPlanListDetail rd : details) {
			if (rd.getPlanItemType().equals(10)) {
				c.setItem10(rd.getPlanAmount().subtract(calFactRepay(10, null, businessId, afterId)));
				continue;
			}
			if (rd.getPlanItemType().equals(20)) {
				c.setItem20(rd.getPlanAmount().subtract(calFactRepay(20, null, businessId, afterId)));
				continue;
			}
			if (rd.getPlanItemType().equals(30)) {
				c.setItem30(rd.getPlanAmount().subtract(calFactRepay(30, rd.getFeeId(), businessId, afterId)).add(c.getItem30()));
				continue;
			}
			if (rd.getPlanItemType().equals(50)) {
				c.setItem50(rd.getPlanAmount().subtract(calFactRepay(50, rd.getFeeId(), businessId, afterId)).add(c.getItem50()));
				continue;
			}
			if (rd.getPlanItemType().equals(60)
					&& rd.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
				c.setOnlineOverDue(rd.getPlanAmount().subtract(
						calFactRepay(60, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), businessId, afterId)));
				continue;
			}
			if (rd.getPlanItemType().equals(60)
					&& rd.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
				c.setOfflineOverDue(rd.getPlanAmount().subtract(calFactRepay(60,
						RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid(), businessId, afterId)));
				continue;
			}
		}

		List<ApplyDerateProcess> applyDerateProcesses = applyDerateProcessMapper.selectList(
				new EntityWrapper<ApplyDerateProcess>().eq("crp_id", rpl.getPlanListId()).eq("is_settle", 0));
		List<String> applyDerateProcessIds = new ArrayList<>();
		for (ApplyDerateProcess applyDerateProcess : applyDerateProcesses) {
			Process process = new Process();
			process.setProcessId(applyDerateProcess.getProcessId());
			process = processMapper.selectOne(process);
			if (process.getProcessResult() == null || process.getProcessResult().equals(2)) {
				continue;
			}
			applyDerateProcessIds.add(applyDerateProcess.getApplyDerateProcessId());
		}

		if (applyDerateProcessIds.size() > 0) {
			List<JSONObject> derateDetails = new ArrayList<>();
			List<ApplyDerateType> applyDerateTypes = applyDerateTypeMapper.selectList(
					new EntityWrapper<ApplyDerateType>().in("apply_derate_process_id", applyDerateProcessIds));
			BigDecimal t1 = new BigDecimal(0);
			for (ApplyDerateType applyDerateType : applyDerateTypes) {
				t1 = t1.add(applyDerateType.getDerateMoney());
				JSONObject derateDetail = new JSONObject();
				derateDetail.put("name", applyDerateType.getDerateTypeName());
				derateDetail.put("amount", applyDerateType.getDerateMoney());
				derateDetails.add(derateDetail);
			}
			c.setDerate(t1);
			c.setDerateDetails(derateDetails);
		}
		return c;
	}

	@Override
	public CurrPeriodDerateInfoVO getCurrPeriodDerate(String businessId, String afterId) {
		CurrPeriodDerateInfoVO currPeriodDerateInfoVO = new CurrPeriodDerateInfoVO();
		RepaymentBizPlanList rpl = new RepaymentBizPlanList();
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl);
		List<ApplyDerateProcess> applyDerateProcesses = applyDerateProcessMapper.selectList(
				new EntityWrapper<ApplyDerateProcess>().eq("crp_id", rpl.getPlanListId()).eq("is_settle", 0));
		List<String> applyDerateProcessIds = new ArrayList<>();
		for (ApplyDerateProcess applyDerateProcess : applyDerateProcesses) {
			Process process = new Process();
			process.setProcessId(applyDerateProcess.getProcessId());
			process = processMapper.selectOne(process);
			if (process.getProcessResult() == null || process.getProcessResult().equals(2)) {
				continue;
			}
			applyDerateProcessIds.add(applyDerateProcess.getApplyDerateProcessId());
		}

		if (applyDerateProcessIds.size() > 0) {
			List<ApplyDerateType> applyDerateTypes = applyDerateTypeMapper.selectList(
					new EntityWrapper<ApplyDerateType>().in("apply_derate_process_id", applyDerateProcessIds));

			currPeriodDerateInfoVO.setList(applyDerateTypes);
		}
		return currPeriodDerateInfoVO;
	}

	@Override
	@Deprecated
	public BigDecimal getSurplusFund(String businessId, String afterId) {
		BigDecimal r = new BigDecimal(0);
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(
				new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).orderBy("due_date"));
		List<RepaymentBizPlanList> exs = new ArrayList<>();
		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			if (repaymentBizPlanList.getAfterId().equals(afterId)) {
				break;
			}
			exs.add(repaymentBizPlanList);
		}
		for (RepaymentBizPlanList repaymentBizPlanList : exs) {
			List<RepaymentProjFactRepay> repaymentProjFactRepays = repaymentProjFactRepayMapper.selectList(
					new EntityWrapper<RepaymentProjFactRepay>().eq("business_id", repaymentBizPlanList.getBusinessId()).eq("is_cancelled", 0)
							.eq("after_id", repaymentBizPlanList.getAfterId()));
			BigDecimal f = new BigDecimal(0);
			for (RepaymentProjFactRepay repaymentProjFactRepay : repaymentProjFactRepays) {
				if (repaymentProjFactRepay.getFactAmount() != null) {
					f = f.add(repaymentProjFactRepay.getFactAmount());
				}
			}
			if (f.subtract(repaymentBizPlanList.getTotalBorrowAmount()).compareTo(new BigDecimal(0)) > 0) {
				r = r.add(f.subtract(repaymentBizPlanList.getTotalBorrowAmount()));
			}
		}
		return r;
	}

	@Override
	public List<CurrPeriodProjDetailVO> getCurrPeriodProjDetailVOs(String businessId, String afterId) {
		RepaymentBizPlanList rpl = new RepaymentBizPlanList();
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl);
		List<TuandaiProjectInfo> tuandaiProjectInfos = tuandaiProjectInfoMapper
				.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", rpl.getOrigBusinessId()));
		List<CurrPeriodProjDetailVO> projs = new ArrayList<>();
		for (TuandaiProjectInfo tuandaiProjectInfo : tuandaiProjectInfos) {
			RepaymentProjPlan projPlan = new RepaymentProjPlan();
			projPlan.setProjectId(tuandaiProjectInfo.getProjectId());
			projPlan.setBusinessId(tuandaiProjectInfo.getBusinessId());
			projPlan = repaymentProjPlanMapper.selectOne(projPlan);
			if (projPlan == null) {
				continue;
			}
			RepaymentProjPlanList repaymentProjPlanList = new RepaymentProjPlanList();
			repaymentProjPlanList.setProjPlanId(projPlan.getPlanId());
			repaymentProjPlanList.setPlanListId(rpl.getPlanListId());
			repaymentProjPlanList = repaymentProjPlanListMapper.selectOne(repaymentProjPlanList);
			if (repaymentProjPlanList == null) {
				continue;
			}
			CurrPeriodProjDetailVO currPeriodProjDetailVO = new CurrPeriodProjDetailVO();
			List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
					.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
							repaymentProjPlanList.getProjPlanListId()));
			currPeriodProjDetailVO.setUserName(tuandaiProjectInfo.getRealName());
			currPeriodProjDetailVO.setProjAmount(tuandaiProjectInfo.getAmount());
			if (tuandaiProjectInfo.getMasterIssueId().equals(tuandaiProjectInfo.getProjectId())) {
				currPeriodProjDetailVO.setMaster(true);
			} else {
				currPeriodProjDetailVO.setMaster(false);
			}
			for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
				if (repaymentProjPlanListDetail.getPlanItemType().equals(10)) {
					currPeriodProjDetailVO.setItem10(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(20)) {
					currPeriodProjDetailVO.setItem20(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(30)) {
					currPeriodProjDetailVO.setItem30(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(50)) {
					currPeriodProjDetailVO.setItem50(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
					currPeriodProjDetailVO.setOnlineOverDue(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
					currPeriodProjDetailVO.setOfflineOverDue(repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
			}
			projs.add(currPeriodProjDetailVO);
		}
		return null;
	}

	@Override
	public BigDecimal calFactRepay(Integer itemType, String feeId, String businessId, String afterId) {
		return repaymentProjFactRepayMapper.calFactRepay(itemType, feeId, businessId, afterId);
	}

	@Override
	public List<MatchedMoneyPoolVO> selectConfirmedBankStatement(String businessId, String afterId) {
		List<MatchedMoneyPoolVO> list = new ArrayList<>();
		List<RepaymentResource> repaymentResources = repaymentResourceMapper
				.selectList(new EntityWrapper<RepaymentResource>().eq("business_id", businessId).eq("after_id", afterId)
						.eq("is_cancelled", 0).orderBy("repay_date"));
		for (RepaymentResource repaymentResource : repaymentResources) {
			MatchedMoneyPoolVO matchedMoneyPoolVO = null;
			String resource = repaymentResource.getRepaySource();
			switch (resource) {
			case "10":
				// 线下转账
				MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper
						.selectById(repaymentResource.getRepaySourceRefId());
				if (moneyPoolRepayment == null || moneyPoolRepayment.getMoneyPoolId() == null) {
					matchedMoneyPoolVO = new MatchedMoneyPoolVO();
					matchedMoneyPoolVO.setAccountMoney(repaymentResource.getRepayAmount());
					matchedMoneyPoolVO.setTradeDate(repaymentResource.getRepayDate());
					matchedMoneyPoolVO.setRemark("找不到还款登记信息");
					break;
				}
				MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolRepayment.getMoneyPoolId());
				if (moneyPool == null) {
					matchedMoneyPoolVO = new MatchedMoneyPoolVO();
					matchedMoneyPoolVO.setAccountMoney(repaymentResource.getRepayAmount());
					matchedMoneyPoolVO.setTradeDate(repaymentResource.getRepayDate());
					matchedMoneyPoolVO.setRemark("找不到银行流水信息");
					break;
				}
				matchedMoneyPoolVO = new MatchedMoneyPoolVO();
				matchedMoneyPoolVO.setAccountMoney(moneyPool.getAccountMoney());
				matchedMoneyPoolVO.setBankAccount(moneyPool.getAcceptBank());
				matchedMoneyPoolVO.setMoneyPoolId(moneyPoolRepayment.getMoneyPoolId());
				matchedMoneyPoolVO.setRemark(moneyPoolRepayment.getRemark());
				matchedMoneyPoolVO.setRepaymentCode(moneyPool.getPayCode());
				matchedMoneyPoolVO.setTradeDate(moneyPool.getTradeDate());
				matchedMoneyPoolVO.setTradePlace(moneyPool.getTradePlace());
				matchedMoneyPoolVO.setSummary(moneyPool.getSummary());
				matchedMoneyPoolVO.setTradeType(moneyPool.getTradeType());
				matchedMoneyPoolVO.setStatus(moneyPoolRepayment.getState());

				break;
			case "20":
				// 线下代扣
				// TODO
			case "30":
				// 银行代扣
				// TODO
			default:
				matchedMoneyPoolVO = new MatchedMoneyPoolVO();
				matchedMoneyPoolVO.setAccountMoney(repaymentResource.getRepayAmount());
				matchedMoneyPoolVO.setTradeDate(repaymentResource.getRepayDate());
				
				matchedMoneyPoolVO.setRemark("还款来源:" + RepayPlanRepaySrcEnum.getByValue(Integer.valueOf(repaymentResource.getRepaySource())).getDesc());
				break;
			}
			list.add(matchedMoneyPoolVO);
		}
		return list;
	}

	/**
	 * 计算每个标的占比
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private void caluProportion(RepaymentBizPlanDto dto) {
		BigDecimal count = new BigDecimal(0);
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			count = count.add(projPlanDto.getRepaymentProjPlan().getBorrowMoney());
		}
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			BigDecimal proportion = projPlanDto.getRepaymentProjPlan().getBorrowMoney().divide(count).setScale(10,
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
	private void distributiveMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				// repaymentProjPlanDto.setDistributiveMoney(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				// repaymentProjPlanDto.setDistributiveMoney(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	/**
	 * 计算每个标的分配下来的线上滞纳金金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private void distributiveOnlineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setOnlineOverDue(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	/**
	 * 计算每个标的分配下来的线下滞纳金金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private void distributiveOfflineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setOfflineOverDue(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	/**
	 * 填充每一项
	 * 
	 * @author 王继光 2018年5月19日 上午10:36:33
	 * @param money
	 *            还款来源的总金额
	 * @param onlineOverDue
	 *            线上滞纳金,此部分资金将在money扣除
	 * @param offlineOverDue
	 *            线下滞纳金,此部分资金将在money扣除
	 * @param dto
	 *            还款计划总体信息
	 * @param preview
	 *            是否预览,true-预览,不执行插入更新操作;false-执行插入更新操作
	 * @param factRepayDate
	 *            实还日期
	 * @param repaySource
	 *            还款来源id,10：线下转账，20：线下代扣，30：银行代扣
	 * @param repayRefId
	 *            还款来源关联的moneyPoolRepaymentId/线下代扣id/银行代扣id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	private ConfirmRepaymentPreviewDto fillItem(BigDecimal money, BigDecimal onlineOverDue, BigDecimal offlineOverDue,
			RepaymentBizPlanDto dto, Date factRepayDate, Integer repaySource, String repayRefId, boolean preview) {
		/* 有线上滞纳金先从还款金额扣除滞纳金 */
		if (onlineOverDue != null) {
			money = money.subtract(onlineOverDue);
			/* 给每个标均分滞纳金 */
			distributiveOnlineOveryDueMoney(onlineOverDue, dto);
		}
		/* 有线下滞纳金先从还款金额扣除滞纳金 */
		if (offlineOverDue != null) {
			money = money.subtract(offlineOverDue);
			/* 给每个标均分滞纳金 */
			distributiveOfflineOveryDueMoney(offlineOverDue, dto);
		}
		/* 计算占比 */
		caluProportion(dto);
		/* 分配资金 */
		distributiveMoney(money, dto);

		List<RepaymentProjPlanDto> list = dto.getProjPlanDtos();
		List<CurrPeriodProjDetailVO> currPeriodProjDetailVOs = new ArrayList<>();
		for (RepaymentProjPlanDto repaymentProjPlanDto : list) {
			RepaymentProjPlan projPlan = repaymentProjPlanDto.getRepaymentProjPlan();
			BigDecimal distributiveMoney = repaymentProjPlanDto.getDivideAmount();
			List<RepaymentProjPlanListDto> projPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();

			/* 开始渲染每个标的还款信息 */
			CurrPeriodProjDetailVO detailVO = new CurrPeriodProjDetailVO();
			TuandaiProjectInfo projectInfo = repaymentProjPlanDto.getTuandaiProjectInfo();
			String userName = projectInfo.getRealName();
			boolean isMaster = projectInfo.getMasterIssueId().equals(projectInfo.getProjectId());
			detailVO.setMaster(isMaster);
			detailVO.setUserName(userName);
			detailVO.setProjAmount(projectInfo.getAmount());
			detailVO.setProject(projectInfo.getProjectId());
			BigDecimal surplusFund = new BigDecimal(0);

			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				RepaymentProjPlanList repaymentProjPlanList = projPlanListDto.getRepaymentProjPlanList();
				/* 计算总还金额 */
				BigDecimal repayAmount = caluProjPlanListSurplusFund(projPlanListDto);
				BigDecimal overDueOnline = caluProjPlanListSurplusFund(projPlanListDto, 60,
						RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
				BigDecimal overDueOffline = caluProjPlanListSurplusFund(projPlanListDto, 60,
						RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());

				repayAmount = repayAmount.subtract(overDueOnline).subtract(overDueOffline);

				int c = repayAmount.compareTo(distributiveMoney);
				RepaymentBizPlanListDto repaymentBizPlanListDto = findRepaymentBizPlanListDto(
						projPlanListDto.getRepaymentProjPlanList().getPlanListId(), dto);
				/* 比较总还金额与分配金额 */
				if (c == -1) {
					/* 总还金额<分配金额,有余额,且每一项都填满 */
					surplusFund = distributiveMoney.subtract(repayAmount);
					// BigDecimal surplusFundAdd = repaymentProjPlanDto.get().add(surplusFund);
					// repaymentProjPlanDto.setSurplusMoney(surplusFundAdd);
					detailVO.setSurplus(surplusFund);
				} else {
					/* 总还金额==分配金额,没有余额,且每一项都填满 */
					/* 总还金额>分配金额,没有余额,有没填满的项 */
				}

				List<RepaymentProjPlanListDetail> details = projPlanListDto.getProjPlanListDetails();
				List<RepaymentProjFactRepay> factRepays = new ArrayList<>();
				for (int i = 0; i < details.size(); i++) {
					RepaymentProjPlanListDetail repaymentProjPlanListDetail = details.get(i);
					Integer itemType = repaymentProjPlanListDetail.getPlanItemType();
					String feeId = repaymentProjPlanListDetail.getFeeId();
					/* 子项应还金额 */
					BigDecimal itemPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
					/* 子项已还金额 */
					BigDecimal factPlanAmount = repaymentProjPlanListDetail.getProjFactAmount();
					if (factPlanAmount == null) {
						factPlanAmount = new BigDecimal(0);
					}
					if (itemType.equals(new Integer(60))) {
						boolean online = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId);
						boolean offline = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId);
						if (offline) {
							/* cr==0,itemPlanAmount==repaymentProjPlanDto.getOfflineOverDue(),刚好足够 */
							/* cr==1,itemPlanAmount>repaymentProjPlanDto.getOfflineOverDue(),不足 */
							/* cr==-1,itemPlanAmount<repaymentProjPlanDto.getOfflineOverDue(),填满有余 */
							// int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOfflineOverDue());
							// if (cr>=0) {
							// repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
							// detailVO.setOfflineOverDue(repaymentProjPlanDto.getOfflineOverDue());
							// }else {
							// repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
							// detailVO.setOfflineOverDue(itemPlanAmount);
							// surplusFund =
							// surplusFund.add(repaymentProjPlanDto.getOfflineOverDue().subtract(itemPlanAmount));
							// }

							repaymentProjPlanListDetail
									.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
							repaymentProjPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentProjPlanListDetail.setUpdateDate(new Date());
							repaymentProjPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							detailVO.setOfflineOverDue(repaymentProjPlanDto.getOfflineOverDue());
							if (preview) {
								continue;
							}
							RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList,
									factRepayDate, repaymentProjPlanListDetail,
									repaymentProjPlanDto.getOfflineOverDue(), projPlan, repayRefId, repaySource);
							factRepays.add(repaymentProjFactRepay);
							repaymentProjPlanListDetail.updateById();

							RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto,
									repaymentProjPlanListDetail.getPlanDetailId());
							BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
							if (factAmount == null) {
								factAmount = new BigDecimal(0);
							}
							repaymentBizPlanListDetail
									.setFactAmount(factAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
							repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentBizPlanListDetail.setUpdateDate(new Date());
							repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							repaymentBizPlanListDetail.updateById();

						}

						if (online) {
							/* cr==0,itemPlanAmount==repaymentProjPlanDto.getOnlineOverDue(),刚好足够 */
							/* cr==1,itemPlanAmount>repaymentProjPlanDto.getOnlineOverDue(),不足 */
							/* cr==-1,itemPlanAmount<repaymentProjPlanDto.getOnlineOverDue(),填满有余 */
							// int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOnlineOverDue());
							// if (cr>=0) {
							// repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
							// detailVO.setOfflineOverDue(repaymentProjPlanDto.getOnlineOverDue());
							// }else {
							// repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
							// detailVO.setOfflineOverDue(itemPlanAmount);
							// surplusFund =
							// surplusFund.add(repaymentProjPlanDto.getOnlineOverDue().subtract(itemPlanAmount));
							// }

							repaymentProjPlanListDetail
									.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
							repaymentProjPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentProjPlanListDetail.setUpdateDate(new Date());
							repaymentProjPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							detailVO.setOfflineOverDue(repaymentProjPlanDto.getOnlineOverDue());
							if (preview) {
								continue;
							}
							RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList,
									factRepayDate, repaymentProjPlanListDetail, repaymentProjPlanDto.getOnlineOverDue(),
									projPlan, repayRefId, repaySource);
							factRepays.add(repaymentProjFactRepay);
							repaymentProjPlanListDetail.updateById();

							RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto,
									repaymentProjPlanListDetail.getPlanDetailId());
							BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
							if (factAmount == null) {
								factAmount = new BigDecimal(0);
							}
							repaymentBizPlanListDetail
									.setFactAmount(factAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
							repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentBizPlanListDetail.setUpdateDate(new Date());
							repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							repaymentBizPlanListDetail.updateById();
						}

					} else {
						int cr = itemPlanAmount.compareTo(distributiveMoney);

						/* 比较子项金额与分配金额 */
						if (cr >= 0) {
							/* 子项金额>分配金额,子项实还填入剩余的分配金额,退出循环 */
							/* 子项金额==分配金额,子项实还填入剩余的分配金额,退出循环 */
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(distributiveMoney));
							repaymentProjPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentProjPlanListDetail.setUpdateDate(new Date());
							repaymentProjPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());

							/* 将分润的信息填写到CurrPeriodProjDetailVO中 */
							renderCurrPeriodProjDetailVO(repaymentProjPlanListDetail, detailVO, distributiveMoney);
							if (!preview) {
								RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList,
										factRepayDate, repaymentProjPlanListDetail, distributiveMoney, projPlan,
										repayRefId, repaySource);
								factRepays.add(repaymentProjFactRepay);
								repaymentProjPlanListDetail.updateById();

								RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(
										dto, repaymentProjPlanListDetail.getPlanDetailId());
								BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
								if (factAmount == null) {
									factAmount = new BigDecimal(0);
								}
								repaymentBizPlanListDetail.setFactAmount(factAmount.add(distributiveMoney));
								repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
								repaymentBizPlanListDetail.setUpdateDate(new Date());
								repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
								repaymentBizPlanListDetail.updateById();
							}

							distributiveMoney = new BigDecimal(0);

							break;
						} else {
							/* 子项金额<分配金额,子项实还填入子项应还 */
							distributiveMoney = distributiveMoney.subtract(itemPlanAmount);
							/* 将分润的信息填写到CurrPeriodProjDetailVO中 */
							renderCurrPeriodProjDetailVO(repaymentProjPlanListDetail, detailVO, itemPlanAmount);
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
							repaymentProjPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentProjPlanListDetail.setUpdateDate(new Date());
							repaymentProjPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							if (!preview) {
								RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList,
										factRepayDate, repaymentProjPlanListDetail, itemPlanAmount, projPlan,
										repayRefId, repaySource);
								factRepays.add(repaymentProjFactRepay);
								repaymentProjPlanListDetail.updateById();

								RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(
										dto, repaymentProjPlanListDetail.getPlanDetailId());
								BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
								if (factAmount == null) {
									factAmount = new BigDecimal(0);
								}
								repaymentBizPlanListDetail.setFactAmount(factAmount.add(itemPlanAmount));
								repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
								repaymentBizPlanListDetail.setUpdateDate(new Date());
								repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
								repaymentBizPlanListDetail.updateById();
							}
						}
					}

				}

				if (!preview) {
					BigDecimal total = repaymentProjPlanList.getTotalBorrowAmount();
					BigDecimal overDue = repaymentProjPlanList.getOverdueAmount() == null ? new BigDecimal(0)
							: repaymentProjPlanList.getOverdueAmount();
					total = total.add(overDue);

					List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
									repaymentProjPlanList.getProjPlanListId()));
					BigDecimal repay = new BigDecimal(0);
					for (RepaymentProjPlanListDetail repaymentProjPlanListDetail2 : repaymentProjPlanListDetails) {
						repay = repay.add(repaymentProjPlanListDetail2.getProjFactAmount());
					}

					int compare = repay.compareTo(total);
					if (compare >= 0) {
						repaymentProjPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
						repaymentProjPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
						if (repaySource.equals(10)) {
							repaymentProjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
						}
					} else {
						repaymentProjPlanList.setCurrentStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentProjPlanList.setCurrentSubStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentProjPlanList.setRepayFlag(RepayedFlag.REPAYING.getKey());
					}
					repaymentProjPlanList.setFactRepayDate(factRepayDate);
					repaymentProjPlanList.setUpdateTime(new Date());
					repaymentProjPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
					repaymentProjPlanList.updateById();

					RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListDto.getRepaymentBizPlanList();
					List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper
							.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("plan_list_id",
									repaymentBizPlanList.getPlanListId()));

					boolean allRepayed = true;
					for (RepaymentProjPlanList repaymentProjPlanList2 : repaymentProjPlanLists) {
						boolean repayStatus = repaymentProjPlanList2.getCurrentStatus()
								.equals(RepayPlanStatus.REPAYED.getName());
						if (!repayStatus) {
							allRepayed = false;
						}
					}

					if (allRepayed) {
						repaymentBizPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
						repaymentBizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
						if (repaySource.equals(10)) {
							repaymentBizPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
						}
					} else {
						repaymentBizPlanList.setCurrentStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentBizPlanList.setCurrentSubStatus(RepayPlanStatus.PARTAIL.getName());
					}

					repaymentBizPlanList.setFactRepayDate(factRepayDate);
					repaymentBizPlanList.setUpdateTime(new Date());
					repaymentBizPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
					if (repaySource.equals(10)) {
						repaymentBizPlanList.setFinanceComfirmDate(new Date());
						repaymentBizPlanList.setFinanceConfirmUser(loginUserInfoHelper.getUserId());
						repaymentBizPlanList
								.setFinanceConfirmUserName(loginUserInfoHelper.getLoginInfo().getUserName());
					}

					// TODO 自动代扣
					if (repaySource.equals("")) {
						repaymentBizPlanList.setAutoWithholdingConfirmedDate(new Date());
						repaymentBizPlanList.setAutoWithholdingConfirmedUser(loginUserInfoHelper.getUserId());
						repaymentBizPlanList
								.setAutoWithholdingConfirmedUserName(loginUserInfoHelper.getLoginInfo().getUserName());
					}

					repaymentBizPlanList.updateById();
				}

				if (distributiveMoney.equals(new BigDecimal(0))) {
					break;
				}

			}

			currPeriodProjDetailVOs.add(detailVO);
			// repaymentProjPlanDto.setSurplusMoney(surplusFund);
		}
		ConfirmRepaymentPreviewDto confirmRepaymentPreviewDto = new ConfirmRepaymentPreviewDto();
		confirmRepaymentPreviewDto.setBizPlanDto(dto);
		confirmRepaymentPreviewDto.setList(currPeriodProjDetailVOs);
		return confirmRepaymentPreviewDto;
	}

	/**
	 * 根据repaymentProjPlanListDetail创建factRepayDetail
	 * 
	 * @author 王继光 2018年5月19日 下午1:57:05
	 * @param repaymentProjPlanList
	 * @param factRepayDate
	 * @param repaymentProjPlanListDetail
	 * @param money
	 *            金额
	 * @param projPlan
	 * @param repayRefId
	 * @param repaySource
	 * @return
	 */
	private RepaymentProjFactRepay saveFactRepay(RepaymentProjPlanList repaymentProjPlanList, Date factRepayDate,
			RepaymentProjPlanListDetail repaymentProjPlanListDetail, BigDecimal money, RepaymentProjPlan projPlan,
			String repayRefId, Integer repaySource) {
		RepaymentProjFactRepay repaymentProjFactRepay = new RepaymentProjFactRepay();
		repaymentProjFactRepay.setIsCancelled(0);
		repaymentProjFactRepay.setAfterId(repaymentProjPlanList.getAfterId());
		repaymentProjFactRepay.setBusinessId(repaymentProjPlanList.getBusinessId());
		repaymentProjFactRepay.setCreateDate(new Date());
		repaymentProjFactRepay.setCreateUser(loginUserInfoHelper.getUserId());
		repaymentProjFactRepay.setFactAmount(money);
		repaymentProjFactRepay.setFactRepayDate(factRepayDate);
		repaymentProjFactRepay.setFeeId(repaymentProjPlanListDetail.getFeeId());
		repaymentProjFactRepay.setOrigBusinessId(repaymentProjPlanList.getOrigBusinessId());
		repaymentProjFactRepay.setPeriod(repaymentProjPlanList.getPeriod());
		repaymentProjFactRepay.setPlanItemName(repaymentProjPlanListDetail.getPlanItemName());
		repaymentProjFactRepay.setPlanItemType(repaymentProjPlanListDetail.getPlanItemType());
		repaymentProjFactRepay.setProjectId(projPlan.getProjectId());
		repaymentProjFactRepay.setProjPlanDetailId(repaymentProjPlanListDetail.getProjPlanDetailId());
		repaymentProjFactRepay.setRepayRefId(repayRefId);
		repaymentProjFactRepay.setRepaySource(repaySource);
		repaymentProjFactRepay.setProjPlanDetailRepayId(UUID.randomUUID().toString());
		repaymentProjFactRepay.insert();
		return repaymentProjFactRepay;
	}

	/**
	 * 根据planListDetailId在RepaymentBizPlanDto查RepaymentBizPlanListDetail
	 * 
	 * @author 王继光 2018年5月19日 下午2:47:13
	 * @param dto
	 * @param planListDetailId
	 * @return
	 */
	private RepaymentBizPlanListDetail findRepaymentBizPlanListDetail(RepaymentBizPlanDto dto,
			String planListDetailId) {
		for (RepaymentBizPlanListDto planListDto : dto.getBizPlanListDtos()) {
			for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
				if (planListDetail.getPlanDetailId().equals(planListDetail)) {
					return planListDetail;
				}
			}
		}
		return null;
	}

	/**
	 * 计算标的还款计划列表总共需要还款的金额
	 * 
	 * @author 王继光 2018年5月18日 上午11:05:05
	 * @param projPlanListDto
	 * @return
	 */
	private BigDecimal caluProjPlanListSurplusFund(RepaymentProjPlanListDto projPlanListDto) {
		BigDecimal res = new BigDecimal(0);
		List<RepaymentProjPlanListDetail> list = projPlanListDto.getProjPlanListDetails();
		for (RepaymentProjPlanListDetail detail : list) {
			BigDecimal planAmount = detail.getProjPlanAmount();
			BigDecimal factAmount = detail.getProjFactAmount();
			if (factAmount == null) {
				res = res.add(planAmount);
			} else {
				res = res.add(planAmount.subtract(factAmount));
			}
		}
		return res;
	}

	/**
	 * 计算标的还款计划列表某项费用总共需要还款的金额
	 * 
	 * @author 王继光 2018年5月18日 上午11:05:05
	 * @param projPlanListDto
	 * @return
	 */
	private BigDecimal caluProjPlanListSurplusFund(RepaymentProjPlanListDto projPlanListDto, Integer itemType,
			String feeId) {
		BigDecimal res = new BigDecimal(0);
		List<RepaymentProjPlanListDetail> list = projPlanListDto.getProjPlanListDetails();
		for (RepaymentProjPlanListDetail detail : list) {
			if (feeId == null && detail.getPlanItemType().equals(itemType)) {
				BigDecimal planAmount = detail.getProjPlanAmount();
				BigDecimal factAmount = detail.getProjFactAmount();
				if (factAmount == null) {
					res = res.add(planAmount);
				} else {
					res = res.add(planAmount.subtract(factAmount));
				}
			} else if (detail.getPlanItemType().equals(itemType) && detail.getFeeId().equals(feeId)) {
				BigDecimal planAmount = detail.getProjPlanAmount();
				BigDecimal factAmount = detail.getProjFactAmount();
				if (factAmount == null) {
					res = res.add(planAmount);
				} else {
					res = res.add(planAmount.subtract(factAmount));
				}
			}

		}
		return res;
	}

	/**
	 * 将分润的信息填写到CurrPeriodProjDetailVO中
	 * 
	 * @author 王继光 2018年5月18日 下午2:35:53
	 * @param detail
	 * @param vo
	 * @param money
	 */
	private void renderCurrPeriodProjDetailVO(RepaymentProjPlanListDetail detail, CurrPeriodProjDetailVO vo,
			BigDecimal money) {
		switch (detail.getPlanItemType()) {
		case 10:
			vo.setItem10(money);
			break;
		case 20:
			vo.setItem20(money);
			break;
		case 30:
			vo.setItem30(money);
			break;
		case 50:
			vo.setItem50(money);
			break;
		/*
		 * case 60: String feeId = detail.getFeeId(); boolean onlineOverDue =
		 * RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId); boolean
		 * offlineOvueDue =
		 * RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId); if
		 * (offlineOvueDue) { vo.setOfflineOverDue(money); } if (onlineOverDue) {
		 * vo.setOnlineOverDue(money); } break;
		 */
		default:
			break;
		}
	}

	private RepaymentBizPlanListDto findRepaymentBizPlanListDto(String planListId, RepaymentBizPlanDto dto) {
		List<RepaymentBizPlanListDto> list = dto.getBizPlanListDtos();
		for (RepaymentBizPlanListDto repaymentBizPlanListDto : list) {
			if (repaymentBizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(planListId)) {
				return repaymentBizPlanListDto;
			}
		}
		return null;

	}

	@Override
	public Map<String, Object> queryRepaymentPlanInfoByBusinessId(String businessId) {
		try {
			RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService
					.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId));
			String origBusinessId = repaymentBizPlanList.getOrigBusinessId();
			
			Map<String, Object> resultMap = new HashMap<>();

			List<RepaymentPlanInfoDTO> repaymentPlanInfoDTOs = repaymentBizPlanListMapper
					.queryRepaymentPlanInfoByBusinessId(origBusinessId);

			if (CollectionUtils.isNotEmpty(repaymentPlanInfoDTOs)) {

				Map<String, List<RepaymentPlanInfoDTO>> map = new LinkedHashMap<>();

				for (RepaymentPlanInfoDTO repaymentPlanInfoDTO : repaymentPlanInfoDTOs) {
					
					double accrual = repaymentPlanInfoDTO.getAccrual();
					double principal = repaymentPlanInfoDTO.getPrincipal();
					double serviceCharge = repaymentPlanInfoDTO.getServiceCharge();
					double platformCharge = repaymentPlanInfoDTO.getPlatformCharge();
					double offlineLateFee = repaymentPlanInfoDTO.getOfflineLateFee()
							- repaymentPlanInfoDTO.getOfflineDerateAmount();
					double onlineLateFee = repaymentPlanInfoDTO.getOnlineLateFee()
							- repaymentPlanInfoDTO.getOnlineDerateAmount();
					double otherFee = repaymentPlanInfoDTO.getOtherFee();
					double surplus = repaymentPlanInfoDTO.getSurplus(); // 结余

					// 小计：本金+利息+月收分公司服务费+月收平台费 + + otherFee
					repaymentPlanInfoDTO
							.setSubtotal(BigDecimal.valueOf(accrual + principal + serviceCharge + platformCharge + otherFee)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

					// 还款合计（含滞纳金）：小计+线上滞纳金+线下滞纳金+结余
					repaymentPlanInfoDTO.setTotal(
							BigDecimal.valueOf(onlineLateFee + offlineLateFee + repaymentPlanInfoDTO.getSubtotal() + surplus)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

					String afterId = repaymentPlanInfoDTO.getAfterId();

					// 根据afterId 将 应还与实还分组
					List<RepaymentPlanInfoDTO> dtos = map.get(afterId);

					if (dtos == null) {
						dtos = new ArrayList<>();
						dtos.add(repaymentPlanInfoDTO);
						map.put(afterId, dtos);
					} else {
						dtos.add(repaymentPlanInfoDTO);
						map.put(afterId, dtos);
					}

				}

				if (!map.isEmpty()) {

					List<RepaymentPlanInfoDTO> resultList = new ArrayList<>();
					
					Double businessSurplus = transferOfLitigationMapper.queryOverRepayMoneyByBusinessId(origBusinessId);

					for (Entry<String, List<RepaymentPlanInfoDTO>> entry : map.entrySet()) {

						List<RepaymentPlanInfoDTO> infoDTOs = entry.getValue();

						RepaymentPlanInfoDTO planInfoDTO = new RepaymentPlanInfoDTO(); // 计划还款
						RepaymentPlanInfoDTO factInfoDTO = new RepaymentPlanInfoDTO(); // 实际还款

						for (RepaymentPlanInfoDTO infoDTO : infoDTOs) {
							String repayment = infoDTO.getRepayment();
							if ("计划还款".equals(repayment)) {
								planInfoDTO = infoDTO; // infoDTOs.get(0) 根据sql取数规则，必定为计划还款
								int confirmFlag = planInfoDTO.getConfirmFlag();
								switch (confirmFlag) {
								case -1:
									planInfoDTO.setConfirmFlagStr("逾期");
									break;
								case 1:
									planInfoDTO.setConfirmFlagStr(SectionRepayStatusEnum.getName(confirmFlag));
									break;
								case 2:
									planInfoDTO.setConfirmFlagStr(SectionRepayStatusEnum.getName(confirmFlag));
									break;

								default:
									planInfoDTO.setConfirmFlagStr(RepayPlanPayedTypeEnum.descOf(confirmFlag));
									break;
								}
							} else if ("实际还款".equals(repayment)) {
								factInfoDTO = infoDTO;
							}
						}

						// 还款合计（含滞纳金）

						RepaymentPlanInfoDTO balanceRepayment = new RepaymentPlanInfoDTO(); // 差额
						balanceRepayment.setRepayment("差额");
						balanceRepayment.setAfterId(planInfoDTO.getAfterId());
						balanceRepayment.setPlanListId(planInfoDTO.getPlanListId());
						if (factInfoDTO.getRepaymentDate() != null) {
							balanceRepayment
									.setAccrual(BigDecimal.valueOf(planInfoDTO.getAccrual() - factInfoDTO.getAccrual())
											.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setOfflineLateFee(BigDecimal
									.valueOf(planInfoDTO.getOfflineLateFee() - factInfoDTO.getOfflineLateFee())
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setOnlineLateFee(
									BigDecimal.valueOf(planInfoDTO.getOnlineLateFee() - factInfoDTO.getOnlineLateFee())
											.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setPlatformCharge(BigDecimal
									.valueOf(planInfoDTO.getPlatformCharge() - factInfoDTO.getPlatformCharge())
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setPrincipal(
									BigDecimal.valueOf(planInfoDTO.getPrincipal() - factInfoDTO.getPrincipal())
											.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setSubtotal(
									BigDecimal.valueOf(planInfoDTO.getSubtotal() - factInfoDTO.getSubtotal())
											.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment
									.setTotal(BigDecimal.valueOf((planInfoDTO.getTotal() - factInfoDTO.getTotal()) > 0
											? planInfoDTO.getTotal() - factInfoDTO.getTotal()
											: 0).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
							balanceRepayment.setServiceCharge(
									BigDecimal.valueOf(planInfoDTO.getServiceCharge() - factInfoDTO.getServiceCharge())
											.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

						}
						
						infoDTOs.add(balanceRepayment);
						resultList.addAll(infoDTOs);
						resultMap.put("resultList", resultList);
						resultMap.put("businessSurplus", businessSurplus);
					}
					return resultMap;
				}
			}

			return resultMap;
		} catch (Exception e) {
			logger.error("根据业务编号获取还款计划信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> queryPlanRepaymentProjInfoByPlanListId(String planListId) {
		try {
			Map<String, Object> resultMap = new HashMap<>();

			List<RepaymentProjInfoDTO> repaymentProjInfoDTOs = repaymentBizPlanListMapper
					.queryPlanRepaymentProjInfoByPlanListId(planListId);

			if (CollectionUtils.isNotEmpty(repaymentProjInfoDTOs)) {

				for (RepaymentProjInfoDTO repaymentProjInfoDTO : repaymentProjInfoDTOs) {
					
					int confirmFlag = repaymentProjInfoDTO.getConfirmFlag();
					switch (confirmFlag) {
					case -1:
						repaymentProjInfoDTO.setConfirmFlagStr("逾期");
						break;
					case 1:
						repaymentProjInfoDTO.setConfirmFlagStr(SectionRepayStatusEnum.getName(confirmFlag));
						break;
					case 2:
						repaymentProjInfoDTO.setConfirmFlagStr(SectionRepayStatusEnum.getName(confirmFlag));
						break;

					default:
						repaymentProjInfoDTO.setConfirmFlagStr(RepayPlanPayedTypeEnum.descOf(confirmFlag));
						break;
					}

					double accrual = repaymentProjInfoDTO.getAccrual();
					double principal = repaymentProjInfoDTO.getPrincipal();
					double serviceCharge = repaymentProjInfoDTO.getServiceCharge();
					double platformCharge = repaymentProjInfoDTO.getPlatformCharge();
					double offlineLateFee = repaymentProjInfoDTO.getOfflineLateFee()
							- repaymentProjInfoDTO.getOfflineDerateAmount();
					double onlineLateFee = repaymentProjInfoDTO.getOnlineLateFee()
							- repaymentProjInfoDTO.getOnlineDerateAmount();

					repaymentProjInfoDTO
							.setSubtotal(BigDecimal.valueOf(accrual + principal + serviceCharge + platformCharge)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
					repaymentProjInfoDTO.setTotal(
							BigDecimal.valueOf(onlineLateFee + offlineLateFee + repaymentProjInfoDTO.getSubtotal())
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
				}
			}

			resultMap.put("resultProjInfo", repaymentProjInfoDTOs);

			return resultMap;
		} catch (Exception e) {
			logger.error("根据业务还款计划列表ID获取所有对应的标的应还还款计划信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> queryActualRepaymentProjInfoByPlanListId(String planListId) {
		try {
			Map<String, Object> resultMap = new HashMap<>();

			List<RepaymentProjInfoDTO> repaymentProjInfoDTOs = repaymentBizPlanListMapper
					.queryActualRepaymentProjInfoByPlanListId(planListId);

			if (CollectionUtils.isNotEmpty(repaymentProjInfoDTOs)) {

				for (RepaymentProjInfoDTO repaymentProjInfoDTO : repaymentProjInfoDTOs) {

					double accrual = repaymentProjInfoDTO.getAccrual();
					double principal = repaymentProjInfoDTO.getPrincipal();
					double serviceCharge = repaymentProjInfoDTO.getServiceCharge();
					double platformCharge = repaymentProjInfoDTO.getPlatformCharge();
					double offlineLateFee = repaymentProjInfoDTO.getOfflineLateFee();
					double onlineLateFee = repaymentProjInfoDTO.getOnlineLateFee();

					repaymentProjInfoDTO
							.setSubtotal(BigDecimal.valueOf(accrual + principal + serviceCharge + platformCharge)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
					repaymentProjInfoDTO.setTotal(BigDecimal
							.valueOf(onlineLateFee + offlineLateFee + repaymentProjInfoDTO.getSubtotal())
							.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

				}
			}

			resultMap.put("resultProjInfo", repaymentProjInfoDTOs);

			return resultMap;
		} catch (Exception e) {
			logger.error("根据业务还款计划列表ID获取所有对应的标的实还还款计划信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> queryDifferenceRepaymentProjInfo(String planListId) {
		try {
			Map<String, Object> resultMap = new HashMap<>();

			List<RepaymentProjInfoDTO> planDTOs = repaymentBizPlanListMapper
					.queryPlanRepaymentProjInfoByPlanListId(planListId);

			List<RepaymentProjInfoDTO> actualDTOs = repaymentBizPlanListMapper
					.queryActualRepaymentProjInfoByPlanListId(planListId);

			List<RepaymentProjInfoDTO> repaymentProjInfoDTOs = new ArrayList<>();

			if (CollectionUtils.isNotEmpty(planDTOs) && CollectionUtils.isNotEmpty(actualDTOs)) {

				Map<String, List<RepaymentProjInfoDTO>> map = new HashMap<>();

				for (RepaymentProjInfoDTO repaymentProjInfoDTO : planDTOs) {

					String projPlanListId = repaymentProjInfoDTO.getProjPlanListId();

					List<RepaymentProjInfoDTO> list = map.get(projPlanListId);

					if (list == null) {
						List<RepaymentProjInfoDTO> infoDTOs = new ArrayList<>();
						infoDTOs.add(repaymentProjInfoDTO);
						map.put(projPlanListId, infoDTOs);
					}
				}

				for (RepaymentProjInfoDTO repaymentProjInfoDTO : actualDTOs) {

					String projPlanListId = repaymentProjInfoDTO.getProjPlanListId();

					List<RepaymentProjInfoDTO> list = map.get(projPlanListId);

					if (list == null) {
						continue;
					} else {
						list.add(repaymentProjInfoDTO);
						map.put(projPlanListId, list);
					}
				}

				for (Entry<String, List<RepaymentProjInfoDTO>> entry : map.entrySet()) {

					List<RepaymentProjInfoDTO> dtos = entry.getValue();
					RepaymentProjInfoDTO dtoPlan = dtos.get(0);
					RepaymentProjInfoDTO dtoAccrual = dtos.get(1);
					dtoPlan.setSubtotal(dtoPlan.getPrincipal() + dtoPlan.getAccrual() + dtoPlan.getServiceCharge()
							+ dtoPlan.getPlatformCharge());
					dtoPlan.setTotal(dtoPlan.getSubtotal() + dtoPlan.getOnlineLateFee() + dtoPlan.getOfflineLateFee());
					
					dtoAccrual.setSubtotal(dtoAccrual.getPrincipal() + dtoAccrual.getAccrual() + dtoAccrual.getServiceCharge()
					+ dtoAccrual.getPlatformCharge());
					dtoAccrual.setTotal(dtoAccrual.getSubtotal() + dtoAccrual.getOnlineLateFee() + dtoAccrual.getOfflineLateFee());

					Date factRepayDate = dtoAccrual.getRepaymentDate(); // 实还日期

					RepaymentProjInfoDTO dtoDifference = new RepaymentProjInfoDTO();
					dtoDifference.setRealName(dtoPlan.getRealName());
					dtoDifference.setRepayment("差额");
					if (factRepayDate != null) {
						dtoDifference.setPrincipal(BigDecimal.valueOf(dtoPlan.getPrincipal() - dtoAccrual.getPrincipal())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setAccrual(BigDecimal.valueOf(dtoPlan.getAccrual() - dtoAccrual.getAccrual())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setServiceCharge(BigDecimal.valueOf(dtoPlan.getServiceCharge() - dtoAccrual.getServiceCharge())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setPlatformCharge(BigDecimal.valueOf(dtoPlan.getPlatformCharge() - dtoAccrual.getPlatformCharge())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setSubtotal(BigDecimal.valueOf(dtoPlan.getSubtotal() - dtoAccrual.getSubtotal())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setOnlineLateFee(BigDecimal.valueOf(dtoPlan.getOnlineLateFee() - dtoAccrual.getOnlineLateFee())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setOfflineLateFee(BigDecimal.valueOf(dtoPlan.getOfflineLateFee() - dtoAccrual.getOfflineLateFee())
								.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
						dtoDifference.setSurplus(0);
						dtoDifference.setTotal(
								BigDecimal.valueOf((dtoPlan.getTotal() - dtoAccrual.getTotal()) > 0 ? dtoPlan.getTotal() - dtoAccrual.getTotal() : 0)
										.setScale(2, RoundingMode.HALF_EVEN).doubleValue());

					}
					repaymentProjInfoDTOs.add(dtoDifference);
				}

				resultMap.put("resultProjInfo", repaymentProjInfoDTOs);
			}

			return resultMap;
		} catch (Exception e) {
			logger.error("根据业务还款计划列表ID获取所有对应的标的实还还款计划信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, List<String>> queryBizOtherFee(String planListId) {
		try {
			Map<String, List<String>> resultMap = new HashMap<>();
			List<String> factAmountResultList = new ArrayList<>();
			List<String> planAmountResultList = new ArrayList<>();
			List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService
					.selectList(new EntityWrapper<RepaymentBizPlanListDetail>()
							.notIn("plan_item_type", 10, 20, 30, 50, 60).eq("plan_list_id", planListId));
			if (CollectionUtils.isNotEmpty(details)) {
				for (RepaymentBizPlanListDetail detail : details) {
					BigDecimal factAmount = detail.getFactAmount();	// 实还
					BigDecimal planAmount = detail.getPlanAmount(); // 应还
					factAmountResultList.add(
							detail.getPlanItemName() + ": " + (factAmount == null ? BigDecimal.valueOf(0) : factAmount)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
					planAmountResultList.add(
							detail.getPlanItemName() + ": " + (planAmount == null ? BigDecimal.valueOf(0) : planAmount)
							.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
				}
				resultMap.put("factAmountResultList", factAmountResultList);
				resultMap.put("planAmountResultList", planAmountResultList);
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("查询业务还款计划其他费用异常", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, List<String>> queryProjOtherFee(String projPlanListId) {
		try {
			Map<String, List<String>> resultMap = new HashMap<>();
			List<String> factAmountResultList = new ArrayList<>();
			List<String> planAmountResultList = new ArrayList<>();
			List<RepaymentProjPlanListDetail> details = repaymentProjPlanListDetailService
					.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", projPlanListId)
							.notIn("plan_item_type", 10, 20, 30, 50, 60));
			if (CollectionUtils.isNotEmpty(details)) {
				for (RepaymentProjPlanListDetail detail : details) {
					BigDecimal projFactAmount = detail.getProjFactAmount();	// 实还
					BigDecimal projPlanAmount = detail.getProjPlanAmount(); // 应还
					factAmountResultList.add(detail.getPlanItemName() + ": "
							+ (projFactAmount == null ? BigDecimal.valueOf(0) : projFactAmount)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
					planAmountResultList.add(detail.getPlanItemName() + ": "
							+ (projPlanAmount == null ? BigDecimal.valueOf(0) : projPlanAmount)
							.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
				}
				resultMap.put("factAmountResultList", factAmountResultList);
				resultMap.put("planAmountResultList", planAmountResultList);
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("查询标还款计划其他费用异常", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void sendLoanBalanceToDataPlatform(String businessId) {
		try {
			if (StringUtil.isEmpty(businessId)) {
				throw new ServiceRuntimeException("业务编号不能为空！");
			}
			BigDecimal bigDecimal = repaymentBizPlanMapper.queryLoanBalanceByBusinessId(businessId);
			double loanBalance = (bigDecimal == null ? 0 : bigDecimal.doubleValue());
			/*
			 * 推送到kafka
			 */
			logger.info("发送数据平台贷款余额（撮合业务余额），业务编号：{}，金额：{}", businessId, loanBalance);
			KafkaUtils.sendMessage(Constant.TOTAL_BUSINESS_BALANCE_TOPIC,
					businessId + SEND_DATA_PLATFORM_SPLIT_SYMBOL + loanBalance);
		} catch (Exception e) {
			logger.error("获取业务维度贷款余额失败，业务编号：{}；抛出异常{}", businessId, e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> queryActualPaymentByBusinessId(String businessId) {
		try {
			if (StringUtil.isEmpty(businessId)) {
				throw new ServiceRuntimeException("业务编号不能为空！");
			}
			Map<String, Object> resultMap = new HashMap<>();

			List<ActualPaymentSingleLogDTO> actualPaymentSingleLogDTOs = moneyPoolRepaymentMapper
					.queryActualPaymentByBusinessId(businessId);

			if (CollectionUtils.isNotEmpty(actualPaymentSingleLogDTOs)) {

				List<ActualPaymentSingleLogDTO> singleLogDTOs = new ArrayList<>(); // afterId 为空的实还流水

				Map<String, List<ActualPaymentSingleLogDTO>> map = new TreeMap<>();

				for (ActualPaymentSingleLogDTO actualPaymentSingleLogDTO : actualPaymentSingleLogDTOs) {

					String afterId = actualPaymentSingleLogDTO.getAfterId();

					if (StringUtil.isEmpty(afterId)) {
						if (actualPaymentSingleLogDTO.getTradeTime() != null) {
							singleLogDTOs.add(actualPaymentSingleLogDTO);
						}
						continue;
					}

					// 根据afterId分组
					List<ActualPaymentSingleLogDTO> list = map.get(afterId);

					if (list == null) {
						List<ActualPaymentSingleLogDTO> dtoList = new LinkedList<>();
						dtoList.add(actualPaymentSingleLogDTO);
						map.put(afterId, dtoList);
					} else {
						list.add(actualPaymentSingleLogDTO);
						map.put(afterId, list);
					}

				}

				// 分别求每一期的实收合计
				if (!map.isEmpty()) {

					List<ActualPaymentLogDTO> actualPaymentLogDTOs = new LinkedList<>();

					for (Entry<String, List<ActualPaymentSingleLogDTO>> entry : map.entrySet()) {
						// 一个list相当于一期
						List<ActualPaymentSingleLogDTO> list = entry.getValue();

						// 实收合计
						double receivedTotal = 0;

						for (ActualPaymentSingleLogDTO dto : list) {
							receivedTotal += dto.getCurrentAmount();
						}

						ActualPaymentLogDTO logDTO = new ActualPaymentLogDTO();
						logDTO.setActualPaymentSingleLogDTOs(list);
						logDTO.setAfterId(entry.getKey());
						logDTO.setReceivedTotal(receivedTotal);
						actualPaymentLogDTOs.add(logDTO);
					}
					resultMap.put("actualPaymentLogDTOs", actualPaymentLogDTOs);
				}

				if (singleLogDTOs.size() > 1) {
					Collections.sort(singleLogDTOs, new Comparator<ActualPaymentSingleLogDTO>() {
						@Override
						public int compare(ActualPaymentSingleLogDTO o1, ActualPaymentSingleLogDTO o2) {
							return o1.getTradeTime().compareTo(o2.getTradeTime());
						}
					});
				}

				resultMap.put("singleLogDTOs", singleLogDTOs);

			}
			return resultMap;
		} catch (Exception e) {
			logger.error("根据业务编号查找实还流水失败，业务编号：{}；抛出异常{}", businessId, e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

}
