/**
 * 
 */
package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.entity.BasicBusiness;
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
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
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
import com.hongte.alms.base.process.entity.Process;

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
			t.put("projs", proj);
		}
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
			List<ApplyDerateType> applyDerateTypes = applyDerateTypeMapper.selectList(
					new EntityWrapper<ApplyDerateType>().in("apply_derate_process_id", applyDerateProcessIds));
			BigDecimal t1 = new BigDecimal(0);
			for (ApplyDerateType applyDerateType : applyDerateTypes) {
				t1 = t1.add(applyDerateType.getDerateMoney());
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
		boolean enough = isSurplusFundEnough(req);
		if (!enough) {
			return Result.error("500", "结余金额不够");
		}
		List<Map<String,Object>> projs = repaymentProjPlanMapper.selectProjPlanProjectInfo(req.getBusinessId()) ;
		if (projs==null||projs.size()==0) {
			return Result.error("500", "找不到对应的标的信息");
		}
		/*查到的本次还款金额*/
		BigDecimal dtotal = moneyPoolRepaymentMapper.sumMoneyPoolRepaymentAmountByMprIds(req.getMprIds());
		if (dtotal==null||dtotal.equals(new BigDecimal(0))) {
			return Result.error("500", "找不到本次还款信息或本次还款总金额为0");
		}
		/*查到的本次还款金额,用于减法运算*/
		BigDecimal dtotalCopy = dtotal ;
		/*上标总金额*/
		BigDecimal projCount = new BigDecimal(0);
		for (Map<String, Object> map : projs) {
			BigDecimal projAmount = (BigDecimal)map.get("amount");
			projCount = projCount.add(projAmount) ;
		}
		
		for (int i = 0; i < projs.size(); i++) {
			String projectId = (String)projs.get(i).get("projectId");
			String realName = (String)projs.get(i).get("realName");
			BigDecimal projAmount = (BigDecimal)projs.get(i).get("amount");
			/*标的与上标总金额占比*/
			BigDecimal proportion = calProportion(projCount, projAmount);
			/*标的分配到的金额*/
			BigDecimal distributive = new BigDecimal(0);
			if (i==projs.size()-1) {
				distributive = dtotalCopy ;
			}else {
				distributive = dtotal.multiply(proportion);
				dtotalCopy = dtotalCopy.subtract(distributive);
			}
			
			boolean isMaster = (boolean)projs.get(i).get("isMaster") ;
			CurrPeriodProjDetailVO currPeriodProjDetailVO = new CurrPeriodProjDetailVO() ;
			currPeriodProjDetailVO.setMaster(isMaster);
			currPeriodProjDetailVO.setUserName(realName);
			currPeriodProjDetailVO.setProjAmount(projAmount);
			
			RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListMapper.selectByProjectIDAndAfterId(projectId, req.getAfterId());
		}
		
		/*for (Map<String, Object> map : projs) {
			String projectId = (String)map.get("projectId");
			String realName = (String)map.get("realName");
			BigDecimal projAmount = (BigDecimal)map.get("amount");
			
			BigDecimal proportion = projAmount.divide(projCount).setScale(10, BigDecimal.ROUND_HALF_UP);
			
			boolean isMaster = (boolean)map.get("isMaster") ;
			CurrPeriodProjDetailVO currPeriodProjDetailVO = new CurrPeriodProjDetailVO() ;
			currPeriodProjDetailVO.setMaster(isMaster);
			currPeriodProjDetailVO.setUserName(realName);
			currPeriodProjDetailVO.setProjAmount(projAmount);
			
			RepaymentProjPlanList repaymentProjPlanList = repaymentProjPlanListMapper.selectByProjectIDAndAfterId(projectId, req.getAfterId());
			
		}*/
		
		return null;
	}

	/**
	 * 计算占比
	 * @author 王继光
	 * 2018年5月17日 上午11:17:10
	 * @param total
	 * @param sub
	 * @return
	 */
	private BigDecimal calProportion(BigDecimal total,BigDecimal sub) {
		return sub.divide(total).setScale(10, BigDecimal.ROUND_HALF_UP);
	}
	
	@Override
	public Result confirmRepayment(ConfirmRepaymentReq req) {
		isSurplusFundEnough(req);
		return null;
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

}
