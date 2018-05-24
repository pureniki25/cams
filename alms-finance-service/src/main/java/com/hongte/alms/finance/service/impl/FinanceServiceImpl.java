/**
 * 
 */
package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import com.hongte.alms.base.RepayPlan.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.RepaymentPlanInfoDTO;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlan;
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
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
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
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.mapper.ProcessMapper;
import com.hongte.alms.base.vo.finance.CurrPeriodDerateInfoVO;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.base.vo.finance.CurrPeriodRepaymentInfoVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.service.FinanceService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.BoaInRoleInfoDto;

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
	RepaymentBizPlanMapper repaymentBizPlanMapper ;
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

	@Override
	@Transactional(rollbackFor = ServiceRuntimeException.class)
	public Result appointBankStatement(RepaymentRegisterInfoDTO dto) {
		logger.info("@appointBankStatement--开始[{}]", dto);
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
		for (BoaInRoleInfoDto i : loginUserInfoHelper.getUserRole()) {
			logger.info(i.getRoleName());
		}
		moneyPoolRepayment.setCreateUserRole(null);
		moneyPoolRepayment.setIsFinanceMatch(1);
		moneyPoolRepayment.setState(moneyPool.getFinanceStatus());
		moneyPoolRepayment.setOriginalBusinessId(dto.getBusinessId());
		moneyPoolRepayment.setAfterId(dto.getAfterId());
		moneyPoolRepayment.setOperateId(loginUserInfoHelper.getUserId());
		moneyPoolRepayment.setClaimDate(now);
		moneyPoolRepayment.setOperateName(loginUserInfoHelper.getLoginInfo().getUserName());
		moneyPoolRepayment.setIncomeType(moneyPool.getIncomeType());
		moneyPoolRepayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		boolean mrpSaveResult = moneyPoolRepayment.insert();
		if (!mrpSaveResult) {
			throw new ServiceRuntimeException("保存银行流水mpr失败");
		}
		return Result.success();
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
		List<MoneyPoolRepayment> list = moneyPoolRepaymentMapper
				.selectList(new EntityWrapper<MoneyPoolRepayment>().eq("original_business_id", businessId)
						.eq("after_id", afterId).in("money_pool_id", moneyPoolIds).eq("is_finance_match", 1));

		if (list != null && list.size() > 0) {
			return Result.error("500", "已存在匹配的银行流水,不可重复匹配");
		}
		Date now = new Date();
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
		} else {
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
		}

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
			boolean mprUpdateRes = moneyPoolRepayment.updateById();
			if (!mprUpdateRes) {
				return Result.error("500", "更新还款登记数据失败");
			}
			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.未关联银行流水.toString());
			moneyPool.setStatus(RepayRegisterState.待领取.toString());
			boolean mpUpdateRes = moneyPool.updateById();
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
		if (rpl == null) {
			return Result.error("500", "找不到对应的还款计划");
		}
		List<RepaymentResource> resources = repaymentResourceMapper.selectList(new EntityWrapper<RepaymentResource>()
				.eq("business_id", businessId).eq("after_id", afterId).eq("is_cancelled", 0));
		List<String> ids = new ArrayList<>();
		for (RepaymentResource o : resources) {
			if (o != null && o.getResourceId() != null) {
				ids.add(o.getResourceId());
			}
		}

		List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper
				.selectList(new EntityWrapper<RepaymentProjFactRepay>().in("repay_ref_id", ids).orderBy("create_date")
						.orderBy("repay_ref_id"));
		List<JSONObject> facts = new ArrayList<>();
		for (RepaymentResource o : resources) {
			if (o != null && o.getResourceId() != null) {
				JSONObject fact = new JSONObject();
				fact.put("type", "实际还款日期");
				fact.put("repayDate", DateUtil.toDateString(o.getRepayDate(), DateUtil.DEFAULT_FORMAT_DATE));
				for (RepaymentProjFactRepay repaymentProjFactRepay : factRepays) {
					if (repaymentProjFactRepay.getRepayRefId().equals(o.getResourceId())) {
						if (repaymentProjFactRepay.getPlanItemType().equals(10)) {
							fact.put("item10", repaymentProjFactRepay.getFactAmount());
							continue;
						}
						if (repaymentProjFactRepay.getPlanItemType().equals(20)) {
							fact.put("item20", repaymentProjFactRepay.getFactAmount());
							continue;
						}
						if (repaymentProjFactRepay.getPlanItemType().equals(30)) {
							fact.put("item30", repaymentProjFactRepay.getFactAmount());
							continue;
						}
						if (repaymentProjFactRepay.getPlanItemType().equals(50)) {
							fact.put("item50", repaymentProjFactRepay.getFactAmount());
							continue;
						}
						if (repaymentProjFactRepay.getPlanItemType().equals(60) && repaymentProjFactRepay.getFeeId()
								.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
							fact.put("onlineOverDue", repaymentProjFactRepay.getFactAmount());
							continue;
						}
						if (repaymentProjFactRepay.getPlanItemType().equals(60) && repaymentProjFactRepay.getFeeId()
								.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
							fact.put("offlineOverDue", repaymentProjFactRepay.getFactAmount());
							continue;
						}
					}
				}
				facts.add(fact);
			}
		}
		JSONObject fact = new JSONObject();
		fact.put("facts", facts);
		List<JSONObject> infos = new ArrayList<>();
		infos.add(thisPeriodPlanRepayment(rpl));
		infos.add(fact);
		return Result.success(infos);
	}

	private JSONObject thisPeriodPlanRepayment(RepaymentBizPlanList rpl) {
		JSONObject t = initThisPeriodPlanRepaymentBase();
		t.put("type", "本期应还日期");
		t.put("repayDate", DateUtil.toDateString(rpl.getDueDate(), DateUtil.DEFAULT_FORMAT_DATE));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", rpl.getPlanListId()));
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
			if (repaymentBizPlanListDetail.getPlanItemType().equals(10)) {
				t.put("item10", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(20)) {
				t.put("item20", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(30)) {
				t.put("item30", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(50)) {
				t.put("item50", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId()
					.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
				t.put("onlineOverDue", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId()
					.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
				t.put("offlineOverDue", repaymentBizPlanListDetail.getPlanAmount());
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
			List<ApplyDerateType> applyDerateTypes = applyDerateTypeMapper.selectList(
					new EntityWrapper<ApplyDerateType>().in("apply_derate_process_id", applyDerateProcessIds));
			BigDecimal t1 = new BigDecimal(0);
			for (ApplyDerateType applyDerateType : applyDerateTypes) {
				t1 = t1.add(applyDerateType.getDerateMoney());
			}
			t.put("derate", t1);
		}

		List<TuandaiProjectInfo> tuandaiProjectInfos = tuandaiProjectInfoMapper
				.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("business_id", rpl.getOrigBusinessId()));
		List<JSONObject> projs = new ArrayList<>();
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
			JSONObject proj = new JSONObject();
			List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper
					.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",
							repaymentProjPlanList.getProjPlanListId()));
			proj.put("realName", tuandaiProjectInfo.getRealName());
			proj.put("amount", tuandaiProjectInfo.getAmount());
			for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
				if (repaymentProjPlanListDetail.getPlanItemType().equals(10)) {
					proj.put("item10", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(20)) {
					proj.put("item20", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(30)) {
					proj.put("item30", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(50)) {
					proj.put("item50", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
					proj.put("onlineOverDue", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
				if (repaymentProjPlanListDetail.getPlanItemType().equals(60) && repaymentProjPlanListDetail.getFeeId()
						.equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
					proj.put("offlineOverDue", repaymentProjPlanListDetail.getProjPlanAmount());
					continue;
				}
			}
			projs.add(proj);
		}
		t.put("projs", projs);
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
	public CurrPeriodRepaymentInfoVO getCurrPeriodRepaymentInfoVO(String businessId, String afterId) {
		CurrPeriodRepaymentInfoVO c = new CurrPeriodRepaymentInfoVO();
		RepaymentBizPlanList rpl = new RepaymentBizPlanList();
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl);
		c.setRepayDate(rpl.getDueDate());
		if (rpl.getOverdueDays()!=null) {
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
				c.setItem30(rd.getPlanAmount().subtract(calFactRepay(30, null, businessId, afterId)));
				continue;
			}
			if (rd.getPlanItemType().equals(50)) {
				c.setItem50(rd.getPlanAmount().subtract(calFactRepay(50, null, businessId, afterId)));
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
			List<JSONObject> derateDetails = new ArrayList<>() ;
			List<ApplyDerateType> applyDerateTypes = applyDerateTypeMapper.selectList(
					new EntityWrapper<ApplyDerateType>().in("apply_derate_process_id", applyDerateProcessIds));
			BigDecimal t1 = new BigDecimal(0);
			for (ApplyDerateType applyDerateType : applyDerateTypes) {
				t1 = t1.add(applyDerateType.getDerateMoney());
				JSONObject derateDetail = new JSONObject() ;
				derateDetail.put("name", applyDerateType.getDerateTypeName());
				derateDetail.put("amount", applyDerateType.getDerateMoney());
				derateDetails.add(derateDetail);
			}
			c.setDerate(t1);
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
					new EntityWrapper<RepaymentProjFactRepay>().eq("business_id", repaymentBizPlanList.getBusinessId())
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
				break;
			case "30":
				// 银行代扣
				// TODO
				break;
			default:
				matchedMoneyPoolVO = new MatchedMoneyPoolVO();
				matchedMoneyPoolVO.setAccountMoney(repaymentResource.getRepayAmount());
				matchedMoneyPoolVO.setTradeDate(repaymentResource.getRepayDate());
				matchedMoneyPoolVO.setRemark("还款来源:" + repaymentResource.getRepaySource());
				break;
			}
			list.add(matchedMoneyPoolVO);
		}
		return list;
	}

	@Override
	public Result previewConfirmRepayment(ConfirmRepaymentReq req) {
		boolean isSurplusFundEnough = isSurplusFundEnough(req);
		if (!isSurplusFundEnough) {
			return Result.error("500", "结余金额不足");
		}
		RepaymentBizPlanDto repaymentBizPlanDto = initRepaymentBizPlanDto(req);
		List<MoneyPoolRepayment> list = moneyPoolRepaymentMapper.selectBatchIds(req.getMprIds());
		BigDecimal repayMoney = new BigDecimal(0);
		ConfirmRepaymentPreviewDto confirmRepaymentPreviewDto= new ConfirmRepaymentPreviewDto();
		for (MoneyPoolRepayment moneyPoolRepayment : list) {
			repayMoney = moneyPoolRepayment.getAccountMoney().add(repayMoney);
			confirmRepaymentPreviewDto = fillItem(
					repayMoney, 
					req.getOnlineOverDue(), 
					req.getOfflineOverDue(), 
					repaymentBizPlanDto, 
					moneyPoolRepayment.getTradeDate(), 
					10, 
					moneyPoolRepayment.getId().toString(), 
					true);
		}
		logger.info(JSON.toJSONString(confirmRepaymentPreviewDto.getList()));
		return Result.success(confirmRepaymentPreviewDto.getList());
	}

	/**
	 * 用moneyPoolRepayment实例一个RepaymentResource
	 * @author 王继光
	 * 2018年5月17日 下午3:40:11
	 * @param moneyPoolRepayment
	 * @return
	 */
	private void saveByMoneyPoolRepayment(MoneyPoolRepayment moneyPoolRepayment) {
		RepaymentResource repaymentResource = new RepaymentResource() ;
		repaymentResource.setAfterId(moneyPoolRepayment.getAfterId());
		repaymentResource.setBusinessId(moneyPoolRepayment.getOriginalBusinessId());
		repaymentResource.setCreateDate(new Date());
		repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
		repaymentResource.setIsCancelled(0);
		repaymentResource.setRepayAmount(moneyPoolRepayment.getAccountMoney());
		repaymentResource.setRepayDate(moneyPoolRepayment.getTradeDate());
		repaymentResource.setRepaySource("10");
		repaymentResource.setRepaySourceRefId(moneyPoolRepayment.getId().toString());
		repaymentResource.insert();
	}
	
	private AccountantOverRepayLog saveByAccountOverRepay(ConfirmRepaymentReq req) {
		AccountantOverRepayLog accountantOverRepayLog = new AccountantOverRepayLog() ;
		accountantOverRepayLog.setBusinessAfterId(req.getAfterId());
		accountantOverRepayLog.setBusinessId(req.getBusinessId());
		accountantOverRepayLog.setCreateTime(new Date());
		accountantOverRepayLog.setCreateUser(loginUserInfoHelper.getUserId());
		accountantOverRepayLog.setFreezeStatus(0);
		accountantOverRepayLog.setIsRefund(0);
		accountantOverRepayLog.setIsTemporary(0);
		accountantOverRepayLog.setMoneyType(0);
		accountantOverRepayLog.setOverRepayMoney(req.getSurplusFund());
		accountantOverRepayLog.setRemark(String.format("支出于%s的%s期线下财务确认", req.getBusinessId(),req.getAfterId()));
		accountantOverRepayLog.insert();
		
		RepaymentResource repaymentResource = new RepaymentResource() ;
		repaymentResource.setAfterId(req.getAfterId());
		repaymentResource.setBusinessId(req.getBusinessId());
		repaymentResource.setCreateDate(new Date());
		repaymentResource.setCreateUser(loginUserInfoHelper.getUserId());
		repaymentResource.setIsCancelled(0);
		repaymentResource.setRepayAmount(req.getSurplusFund());
		repaymentResource.setRepayDate(new Date());
		repaymentResource.setRepaySource("11");
		repaymentResource.setRepaySourceRefId(accountantOverRepayLog.getId().toString());
		repaymentResource.insert();
		return accountantOverRepayLog;
	}
	
	/**
	 * 计算本次总还款金额
	 * @author 王继光
	 * 2018年5月17日 下午4:49:27
	 * @param moneyPoolRepayments
	 * @return
	 */
	private BigDecimal calRepaymentCount(List<MoneyPoolRepayment> moneyPoolRepayments) {
		BigDecimal count = new BigDecimal(0);
		for (MoneyPoolRepayment moneyPoolRepayment : moneyPoolRepayments) {
			count = count.add(moneyPoolRepayment.getAccountMoney());
		}
		return count;
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Result confirmRepayment(ConfirmRepaymentReq req) {
		boolean isSurplusFundEnough = isSurplusFundEnough(req);
		if (!isSurplusFundEnough) {
			return Result.error("500", "结余金额不足");
		}
		RepaymentBizPlanDto repaymentBizPlanDto = initRepaymentBizPlanDto(req);
		ConfirmRepaymentPreviewDto confirmRepaymentPreviewDto= new ConfirmRepaymentPreviewDto();
		
		if (req.getSurplusFund()!=null||req.getSurplusFund().compareTo(new BigDecimal(0))>0) {
			/*用结余*/
			AccountantOverRepayLog accountantOverRepayLog = saveByAccountOverRepay(req);
			confirmRepaymentPreviewDto = fillItem(
					req.getSurplusFund(),
					new BigDecimal(0),
					new BigDecimal(0),
					repaymentBizPlanDto,
					new Date(),
					11,
					accountantOverRepayLog.getId().toString(),
					false);
		}
		List<MoneyPoolRepayment> list = moneyPoolRepaymentMapper.selectBatchIds(req.getMprIds());
		BigDecimal repayMoney = new BigDecimal(0);
		for (MoneyPoolRepayment moneyPoolRepayment : list) {
			repayMoney = moneyPoolRepayment.getAccountMoney().add(repayMoney);
			confirmRepaymentPreviewDto = fillItem(
					repayMoney, 
					req.getOnlineOverDue(), 
					req.getOfflineOverDue(), 
					repaymentBizPlanDto, 
					moneyPoolRepayment.getTradeDate(), 
					10, 
					moneyPoolRepayment.getId().toString(), 
					false);
			saveByMoneyPoolRepayment(moneyPoolRepayment);
		}
		logger.info(JSON.toJSONString(confirmRepaymentPreviewDto.getList()));
		return Result.success(confirmRepaymentPreviewDto);
	}
	

	/**
	 * 查找并关联业务有关的还款计划
	 * @author 王继光
	 * 2018年5月17日 下午9:37:57
	 * @param req
	 * @return
	 */
	private RepaymentBizPlanDto initRepaymentBizPlanDto(ConfirmRepaymentReq req) {
		
		RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto() ;
		RepaymentBizPlan repaymentBizPlan = new RepaymentBizPlan() ;
		repaymentBizPlan.setBusinessId(req.getBusinessId());
		repaymentBizPlan = repaymentBizPlanMapper.selectOne(repaymentBizPlan);
		repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);
		
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(
				new EntityWrapper<RepaymentBizPlanList>()
				.eq("plan_id", repaymentBizPlan.getPlanId())
				.eq("business_id", req.getBusinessId())
				.eq("after_id", req.getAfterId())
				.orderBy("after_id"));
		
		List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = new ArrayList<>() ;
		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto() ;
			List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper.selectList(
					new EntityWrapper<RepaymentBizPlanListDetail>()
					.eq("plan_list_id", repaymentBizPlanList.getPlanListId())
					.orderBy("share_profit_index")
					.orderBy("plan_item_type")
					.orderBy("fee_id"));
			repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetails);
			repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);
			repaymentBizPlanListDtos.add(repaymentBizPlanListDto);
			
		}
		repaymentBizPlanDto.setBizPlanListDtos(repaymentBizPlanListDtos);

		

		
		List<RepaymentProjPlanDto> repaymentProjPlanDtos = new ArrayList<>() ;
		List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanMapper
				.selectList(new EntityWrapper<RepaymentProjPlan>().eq("business_id", req.getBusinessId()));
		for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
			RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto() ;
			List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(
					new EntityWrapper<RepaymentProjPlanList>()
					.eq("proj_plan_id", repaymentProjPlan.getProjPlanId())
					.eq("business_id", req.getBusinessId())
					.eq("after_id", req.getAfterId())
					.orderBy("total_borrow_amount",false));
			List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = new ArrayList<>() ;
			for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
				RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto() ;
				repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
				List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper.selectList(
						new EntityWrapper<RepaymentProjPlanListDetail>()
						.eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId())
						.orderBy("share_profit_index")
						.orderBy("plan_item_type")
						.orderBy("fee_id"));
				
				List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos = new ArrayList<>() ;
				for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
					RepaymentProjPlanListDetailDto repaymentProjPlanListDetailDto = new RepaymentProjPlanListDetailDto() ;
					repaymentProjPlanListDetailDto.setRepaymentProjPlanListDetail(repaymentProjPlanListDetail);
					List<RepaymentProjFactRepay> repaymentProjFactRepays =repaymentProjFactRepayMapper.selectList(new EntityWrapper<RepaymentProjFactRepay>()
							.eq("proj_plan_detail_id", repaymentProjPlanListDetail.getProjPlanDetailId())
							.orderBy("plan_item_type")
							.orderBy("fee_id")) ;
					repaymentProjPlanListDetailDto.setRepaymentProjFactRepays(repaymentProjFactRepays);
					repaymentProjPlanListDetailDtos.add(repaymentProjPlanListDetailDto);
					
				}
				repaymentProjPlanListDto.setRepaymentProjPlanListDetailDtos(repaymentProjPlanListDetailDtos);
				repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetails);
				repaymentProjPlanListDtos.add(repaymentProjPlanListDto);
			}
			TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoMapper.selectById(repaymentProjPlan.getProjectId());
			repaymentProjPlanDto.setTuandaiProjectInfo(tuandaiProjectInfo);
			repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
			repaymentProjPlanDto.setProjPlanListDtos(repaymentProjPlanListDtos);
			repaymentProjPlanDtos.add(repaymentProjPlanDto);
		}
		
		repaymentBizPlanDto.setProjPlanDtos(repaymentProjPlanDtos);
		
		
		
		return repaymentBizPlanDto;
		
	}
	/**
	 * 检查结余金额是否足够
	 * 
	 * @author 王继光 2018年5月16日 下午3:12:46
	 * @return
	 */
	private boolean isSurplusFundEnough(ConfirmRepaymentReq req) {
		if (req.getSurplusFund() == null || req.getSurplusFund().equals(new BigDecimal(0))) {
			return true;
		}
		BigDecimal surplusFund = getSurplusFund(req.getBusinessId(), req.getAfterId());
		int compareResult = req.getSurplusFund().compareTo(surplusFund);
		if (compareResult > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * 计算每个标的占比
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private  void caluProportion(RepaymentBizPlanDto dto) {
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
	private  void distributiveMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setDistributiveMoney(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setDistributiveMoney(dmoney);
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
	private  void distributiveOnlineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
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
	private  void distributiveOfflineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
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
	 * @author 王继光
	 * 2018年5月19日 上午10:36:33
	 * @param money 还款来源的总金额
	 * @param onlineOverDue 线上滞纳金,此部分资金将在money扣除
	 * @param offlineOverDue 线下滞纳金,此部分资金将在money扣除
	 * @param dto 还款计划总体信息
	 * @param preview 是否预览,true-预览,不执行插入更新操作;false-执行插入更新操作
	 * @param factRepayDate 实还日期
	 * @param repaySource 还款来源id,10：线下转账，20：线下代扣，30：银行代扣
	 * @param repayRefId 还款来源关联的moneyPoolRepaymentId/线下代扣id/银行代扣id
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	private  ConfirmRepaymentPreviewDto fillItem( 
			BigDecimal money,BigDecimal onlineOverDue,BigDecimal offlineOverDue, RepaymentBizPlanDto dto,Date factRepayDate,Integer repaySource,String repayRefId,boolean preview) {
		/*有线上滞纳金先从还款金额扣除滞纳金*/
		if (onlineOverDue!=null) {
			money = money.subtract(onlineOverDue);
			/*给每个标均分滞纳金*/
			distributiveOnlineOveryDueMoney(onlineOverDue, dto);
		}
		/*有线下滞纳金先从还款金额扣除滞纳金*/
		if (offlineOverDue!=null) {
			money = money.subtract(offlineOverDue);
			/*给每个标均分滞纳金*/
			distributiveOfflineOveryDueMoney(offlineOverDue, dto);
		}
		/* 计算占比 */
		caluProportion(dto);
		/* 分配资金 */
		distributiveMoney(money, dto);
		
		
		
		List<RepaymentProjPlanDto> list = dto.getProjPlanDtos();
		List<CurrPeriodProjDetailVO> currPeriodProjDetailVOs = new ArrayList<>();
		for (RepaymentProjPlanDto repaymentProjPlanDto : list) {
			RepaymentProjPlan projPlan = repaymentProjPlanDto.getRepaymentProjPlan() ;
			BigDecimal distributiveMoney = repaymentProjPlanDto.getDistributiveMoney();
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
				RepaymentProjPlanList repaymentProjPlanList = projPlanListDto.getRepaymentProjPlanList() ;
				/* 计算总还金额 */
				BigDecimal repayAmount = caluProjPlanListSurplusFund(projPlanListDto);
				BigDecimal overDueOnline = caluProjPlanListSurplusFund(projPlanListDto,60,RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid());
				BigDecimal overDueOffline = caluProjPlanListSurplusFund(projPlanListDto,60,RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
				
				repayAmount = repayAmount.subtract(overDueOnline).subtract(overDueOffline);
				
				int c = repayAmount.compareTo(distributiveMoney);
				RepaymentBizPlanListDto repaymentBizPlanListDto = findRepaymentBizPlanListDto(projPlanListDto.getRepaymentProjPlanList().getPlanListId(),dto);
				/* 比较总还金额与分配金额 */
				if (c == -1) {
					/* 总还金额<分配金额,有余额,且每一项都填满 */
					surplusFund = distributiveMoney.subtract(repayAmount);
					BigDecimal surplusFundAdd = repaymentProjPlanDto.getSurplusMoney().add(surplusFund);
					repaymentProjPlanDto.setSurplusMoney(surplusFundAdd);
					detailVO.setSurplus(surplusFund);
				} else {
					/* 总还金额==分配金额,没有余额,且每一项都填满 */
					/* 总还金额>分配金额,没有余额,有没填满的项 */
				}

				List<RepaymentProjPlanListDetail> details = projPlanListDto.getProjPlanListDetails();
				List<RepaymentProjFactRepay> factRepays = new ArrayList<>() ;
				for (int i = 0; i < details.size(); i++) {
					RepaymentProjPlanListDetail repaymentProjPlanListDetail = details.get(i);
					Integer itemType = repaymentProjPlanListDetail.getPlanItemType();
					String feeId = repaymentProjPlanListDetail.getFeeId();
					/* 子项应还金额 */
					BigDecimal itemPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
					/* 子项已还金额 */
					BigDecimal factPlanAmount = repaymentProjPlanListDetail.getProjFactAmount();
					if (factPlanAmount==null) {
						factPlanAmount = new BigDecimal(0);
					}
					if (itemType.equals(new Integer(60))) {
						boolean online = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId);
						boolean offline = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId);
						if (offline) {
							/*cr==0,itemPlanAmount==repaymentProjPlanDto.getOfflineOverDue(),刚好足够*/
							/*cr==1,itemPlanAmount>repaymentProjPlanDto.getOfflineOverDue(),不足*/
							/*cr==-1,itemPlanAmount<repaymentProjPlanDto.getOfflineOverDue(),填满有余*/
//							int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOfflineOverDue());
//							if (cr>=0) {
//								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
//								detailVO.setOfflineOverDue(repaymentProjPlanDto.getOfflineOverDue());
//							}else {
//								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
//								detailVO.setOfflineOverDue(itemPlanAmount);
//								surplusFund = surplusFund.add(repaymentProjPlanDto.getOfflineOverDue().subtract(itemPlanAmount));
//							}
							
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
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
							
							RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto, repaymentProjPlanListDetail.getPlanDetailId());
							BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
							if (factAmount==null) {
								factAmount = new BigDecimal(0);
							}
							repaymentBizPlanListDetail.setFactAmount(factAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
							repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentBizPlanListDetail.setUpdateDate(new Date());
							repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							repaymentBizPlanListDetail.updateById();
							
						}
						
						if (online) {
							/*cr==0,itemPlanAmount==repaymentProjPlanDto.getOnlineOverDue(),刚好足够*/
							/*cr==1,itemPlanAmount>repaymentProjPlanDto.getOnlineOverDue(),不足*/
							/*cr==-1,itemPlanAmount<repaymentProjPlanDto.getOnlineOverDue(),填满有余*/
//							int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOnlineOverDue());
//							if (cr>=0) {
//								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
//								detailVO.setOfflineOverDue(repaymentProjPlanDto.getOnlineOverDue());
//							}else {
//								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
//								detailVO.setOfflineOverDue(itemPlanAmount);
//								surplusFund = surplusFund.add(repaymentProjPlanDto.getOnlineOverDue().subtract(itemPlanAmount));
//							}
							
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
							repaymentProjPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentProjPlanListDetail.setUpdateDate(new Date());
							repaymentProjPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							detailVO.setOfflineOverDue(repaymentProjPlanDto.getOnlineOverDue());
							if (preview) {
								continue;
							}
							RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList,
									factRepayDate, repaymentProjPlanListDetail,
									repaymentProjPlanDto.getOnlineOverDue(), projPlan, repayRefId, repaySource);
							factRepays.add(repaymentProjFactRepay);
							repaymentProjPlanListDetail.updateById();
							
							RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto, repaymentProjPlanListDetail.getPlanDetailId());
							BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
							if (factAmount==null) {
								factAmount = new BigDecimal(0);
							}
							repaymentBizPlanListDetail.setFactAmount(factAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
							repaymentBizPlanListDetail.setFactRepayDate(factRepayDate);
							repaymentBizPlanListDetail.setUpdateDate(new Date());
							repaymentBizPlanListDetail.setUpdateUser(loginUserInfoHelper.getUserId());
							repaymentBizPlanListDetail.updateById();
						}
						
					}else {
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
								RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList, factRepayDate, repaymentProjPlanListDetail,
										distributiveMoney, projPlan, repayRefId, repaySource);
								factRepays.add(repaymentProjFactRepay);
								repaymentProjPlanListDetail.updateById();
								
								RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto, repaymentProjPlanListDetail.getPlanDetailId());
								BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
								if (factAmount==null) {
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
								RepaymentProjFactRepay repaymentProjFactRepay = saveFactRepay(repaymentProjPlanList, factRepayDate, repaymentProjPlanListDetail,
										itemPlanAmount, projPlan, repayRefId, repaySource);
								factRepays.add(repaymentProjFactRepay);
								repaymentProjPlanListDetail.updateById();
								
								RepaymentBizPlanListDetail repaymentBizPlanListDetail = findRepaymentBizPlanListDetail(dto, repaymentProjPlanListDetail.getPlanDetailId());
								BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
								if (factAmount==null) {
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
					BigDecimal overDue = repaymentProjPlanList.getOverdueAmount()==null?new BigDecimal(0):repaymentProjPlanList.getOverdueAmount();
					total = total.add(overDue);

					List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id", repaymentProjPlanList.getProjPlanListId()));
					BigDecimal repay = new BigDecimal(0);
					for (RepaymentProjPlanListDetail repaymentProjPlanListDetail2 : repaymentProjPlanListDetails) {
						repay = repay.add(repaymentProjPlanListDetail2.getProjFactAmount());
					}
					
					int compare = repay.compareTo(total);
					if (compare>=0) {
						repaymentProjPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
						repaymentProjPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
						if (repaySource.equals(10)) {
							repaymentProjPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
						}
					}else {
						repaymentProjPlanList.setCurrentStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentProjPlanList.setCurrentSubStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentProjPlanList.setRepayFlag(RepayedFlag.REPAYING.getKey());
					}
					repaymentProjPlanList.setFactRepayDate(factRepayDate);
					repaymentProjPlanList.setUpdateTime(new Date());
					repaymentProjPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
					repaymentProjPlanList.updateById();
					
					RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListDto.getRepaymentBizPlanList() ;
					List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListMapper.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("plan_list_id", repaymentBizPlanList.getPlanListId()));
					
					boolean allRepayed = true ;
					for (RepaymentProjPlanList repaymentProjPlanList2 : repaymentProjPlanLists) {
						boolean repayStatus = repaymentProjPlanList2.getCurrentStatus().equals(RepayPlanStatus.REPAYED.getName());
						if (!repayStatus) {
							allRepayed = false ;
						}
					}
					
					if (allRepayed) {
						repaymentBizPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName());
						repaymentBizPlanList.setCurrentSubStatus(RepayPlanStatus.REPAYED.getName());
						if (repaySource.equals(10)) {
							repaymentBizPlanList.setRepayFlag(RepayedFlag.CONFIRM_OFFLINE_REPAYED.getKey());
						}
					}else {
						repaymentBizPlanList.setCurrentStatus(RepayPlanStatus.PARTAIL.getName());
						repaymentBizPlanList.setCurrentSubStatus(RepayPlanStatus.PARTAIL.getName());
					}
					
					repaymentBizPlanList.setFactRepayDate(factRepayDate);
					repaymentBizPlanList.setUpdateTime(new Date());
					repaymentBizPlanList.setUpdateUser(loginUserInfoHelper.getUserId());
					if (repaySource.equals(10)) {
						repaymentBizPlanList.setFinanceComfirmDate(new Date());
						repaymentBizPlanList.setFinanceConfirmUser(loginUserInfoHelper.getUserId());
						repaymentBizPlanList.setFinanceConfirmUserName(loginUserInfoHelper.getLoginInfo().getUserName());
					}
					
					//TODO 自动代扣
					if (repaySource.equals("")) {
						repaymentBizPlanList.setAutoWithholdingConfirmedDate(new Date());
						repaymentBizPlanList.setAutoWithholdingConfirmedUser(loginUserInfoHelper.getUserId());
						repaymentBizPlanList.setAutoWithholdingConfirmedUserName(loginUserInfoHelper.getLoginInfo().getUserName());
					}
					
						
					repaymentBizPlanList.updateById();
				}
				
				if (distributiveMoney.equals(new BigDecimal(0))) {
					break;
				}

			}
			
			
			currPeriodProjDetailVOs.add(detailVO);
			repaymentProjPlanDto.setSurplusMoney(surplusFund);
		}
		ConfirmRepaymentPreviewDto confirmRepaymentPreviewDto = new ConfirmRepaymentPreviewDto();
		confirmRepaymentPreviewDto.setBizPlanDto(dto);
		confirmRepaymentPreviewDto.setList(currPeriodProjDetailVOs);
		return confirmRepaymentPreviewDto;
	}

	/**
	 * 根据repaymentProjPlanListDetail创建factRepayDetail
	 * @author 王继光
	 * 2018年5月19日 下午1:57:05
	 * @param repaymentProjPlanList
	 * @param factRepayDate
	 * @param repaymentProjPlanListDetail
	 * @param money 金额
	 * @param projPlan
	 * @param repayRefId
	 * @param repaySource
	 * @return
	 */
	private RepaymentProjFactRepay saveFactRepay(
			RepaymentProjPlanList repaymentProjPlanList,  
			Date factRepayDate, 
			RepaymentProjPlanListDetail repaymentProjPlanListDetail, 
			BigDecimal money, 
			RepaymentProjPlan projPlan, 
			String repayRefId, 
			Integer repaySource) {
		RepaymentProjFactRepay repaymentProjFactRepay = new RepaymentProjFactRepay() ;
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
		return repaymentProjFactRepay ;
	}
	
	/**
	 * 根据planListDetailId在RepaymentBizPlanDto查RepaymentBizPlanListDetail
	 * @author 王继光
	 * 2018年5月19日 下午2:47:13
	 * @param dto
	 * @param planListDetailId
	 * @return
	 */
	private RepaymentBizPlanListDetail findRepaymentBizPlanListDetail(RepaymentBizPlanDto dto,String planListDetailId) {
		for (RepaymentBizPlanListDto planListDto : dto.getBizPlanListDtos()) {
			for (RepaymentBizPlanListDetail planListDetail : planListDto.getBizPlanListDetails()) {
				if (planListDetail.getPlanDetailId().equals(planListDetail)) {
					return planListDetail;
				}
			}
		}
		return null ;
	}
	
	private BigDecimal caluFactRepayAmount(List<RepaymentProjFactRepay> factRepays) {
		BigDecimal res = new BigDecimal(0);
		for (RepaymentProjFactRepay repaymentProjFactRepay : factRepays) {
			res = repaymentProjFactRepay.getFactAmount().add(res);
		}
		return res ;
	}
	
	/**
	 * 计算标的还款计划列表总共需要还款的金额
	 * 
	 * @author 王继光 2018年5月18日 上午11:05:05
	 * @param projPlanListDto
	 * @return
	 */
	private  BigDecimal caluProjPlanListSurplusFund(RepaymentProjPlanListDto projPlanListDto) {
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
	private  BigDecimal caluProjPlanListSurplusFund(RepaymentProjPlanListDto projPlanListDto,Integer itemType,String feeId) {
		BigDecimal res = new BigDecimal(0);
		List<RepaymentProjPlanListDetail> list = projPlanListDto.getProjPlanListDetails();
		for (RepaymentProjPlanListDetail detail : list) {
			if (feeId==null&&detail.getPlanItemType().equals(itemType)) {
				BigDecimal planAmount = detail.getProjPlanAmount();
				BigDecimal factAmount = detail.getProjFactAmount();
				if (factAmount == null) {
					res = res.add(planAmount);
				} else {
					res = res.add(planAmount.subtract(factAmount));
				}
			}else if (detail.getPlanItemType().equals(itemType)&&detail.getFeeId().equals(feeId)) {
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
	private  void renderCurrPeriodProjDetailVO(RepaymentProjPlanListDetail detail, CurrPeriodProjDetailVO vo,
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
		/*case 60:
			String feeId = detail.getFeeId();
			boolean onlineOverDue = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId);
			boolean offlineOvueDue = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId);
			if (offlineOvueDue) {
				vo.setOfflineOverDue(money);
			}
			if (onlineOverDue) {
				vo.setOnlineOverDue(money);
			}
			break;*/
		default:
			break;
		}
	}
	
	
	private RepaymentBizPlanListDto findRepaymentBizPlanListDto(String planListId, RepaymentBizPlanDto dto) {
		List<RepaymentBizPlanListDto> list =  dto.getBizPlanListDtos();
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
			Map<String, Object> resultMap = new HashMap<>();

			List<RepaymentPlanInfoDTO> repaymentPlanInfoDTOs = repaymentBizPlanListMapper
					.queryRepaymentPlanInfoByBusinessId(businessId);

			if (CollectionUtils.isNotEmpty(repaymentPlanInfoDTOs)) {

				Map<String, List<RepaymentPlanInfoDTO>> map = new TreeMap<>();

				for (RepaymentPlanInfoDTO repaymentPlanInfoDTO : repaymentPlanInfoDTOs) {

					double accrual = repaymentPlanInfoDTO.getAccrual();
					double principal = repaymentPlanInfoDTO.getPrincipal();
					double serviceCharge = repaymentPlanInfoDTO.getServiceCharge();
					double platformCharge = repaymentPlanInfoDTO.getPlatformCharge();
					double offlineLateFee = repaymentPlanInfoDTO.getOfflineLateFee();
					double onlineLateFee = repaymentPlanInfoDTO.getOnlineLateFee();
					repaymentPlanInfoDTO
							.setSubtotal(BigDecimal.valueOf(accrual + principal + serviceCharge + platformCharge)
									.setScale(2, RoundingMode.HALF_EVEN).doubleValue());
					repaymentPlanInfoDTO.setTotal(
							BigDecimal.valueOf(onlineLateFee + offlineLateFee + repaymentPlanInfoDTO.getSubtotal())
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

					for (Entry<String, List<RepaymentPlanInfoDTO>> entry : map.entrySet()) {

						// 每一个 infoDTOs 只可能有一个应还数据，可能有多个实还数据
						List<RepaymentPlanInfoDTO> infoDTOs = entry.getValue();

						RepaymentPlanInfoDTO planInfoDTO = new RepaymentPlanInfoDTO(); // 计划还款

						double accrual = 0; // 利息
						double offlineLateFee = 0; // 线下滞纳金
						double principal = 0; // 本金
						double serviceCharge = 0; // 月收分公司服务费
						double platformCharge = 0; // 月收平台费
						double subtotal = 0; // 小计
						double onlineLateFee = 0; // 线上滞纳金
						double total = 0; // 还款合计（含滞纳金）

						for (RepaymentPlanInfoDTO infoDTO : infoDTOs) {
							String repayment = infoDTO.getRepayment();
							if ("计划还款".equals(repayment)) {
								planInfoDTO = infoDTO; // infoDTOs.get(0) 根据sql取数规则，必定为计划还款
							} else if ("实际还款".equals(repayment)) {
								accrual += infoDTO.getAccrual();
								offlineLateFee += infoDTO.getOfflineLateFee();
								principal += infoDTO.getPrincipal();
								serviceCharge += infoDTO.getServiceCharge();
								platformCharge += infoDTO.getPlatformCharge();
								subtotal += infoDTO.getSubtotal();
								onlineLateFee += infoDTO.getOnlineLateFee();
								total += infoDTO.getTotal();
								infoDTO.setSurplus((infoDTO.getAmount() - planInfoDTO.getAmount()) < 0 ? 0
										: (infoDTO.getAmount() - planInfoDTO.getAmount()));
							}
						}

						RepaymentPlanInfoDTO balanceRepayment = new RepaymentPlanInfoDTO(); // 差额
						balanceRepayment.setAccrual(planInfoDTO.getAccrual() - accrual);
						balanceRepayment.setAfterId(planInfoDTO.getAfterId());
						balanceRepayment.setConfirmFlag(planInfoDTO.getConfirmFlag());
						balanceRepayment.setOfflineLateFee(planInfoDTO.getOfflineLateFee() - offlineLateFee);
						balanceRepayment.setOnlineLateFee(planInfoDTO.getOnlineLateFee() - onlineLateFee);
						balanceRepayment.setOverdueDays(planInfoDTO.getOverdueDays());
						balanceRepayment.setPlanListId(planInfoDTO.getPlanListId());
						balanceRepayment.setPlatformCharge(planInfoDTO.getPlatformCharge() - platformCharge);
						balanceRepayment.setPrincipal(planInfoDTO.getPrincipal() - principal);
						balanceRepayment.setSubtotal(planInfoDTO.getSubtotal() - subtotal);
						balanceRepayment.setTotal(planInfoDTO.getTotal() - total);
						balanceRepayment.setRepayment("差额");
						balanceRepayment.setServiceCharge(planInfoDTO.getServiceCharge() - serviceCharge);

						infoDTOs.add(balanceRepayment);

						resultList.addAll(infoDTOs);

						resultMap.put("resultList", resultList);

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
	public Map<String, Object> queryRepaymentProjInfoByPlanListId(String planListId) {
		// TODO Auto-generated method stub
		return null;
	}

}
