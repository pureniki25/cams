/**
 * 
 */
package com.hongte.alms.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.process.mapper.ProcessMapper;
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
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentResourceMapper repaymentResourceMapper ;
	@Autowired
	RepaymentProjFactRepayMapper repaymentProjFactRepayMapper ;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper ;
	@Autowired
	MoneyPoolMapper moneyPoolMapper;
	@Autowired
	ApplyDerateProcessMapper applyDerateProcessMapper ;
	@Autowired
	ProcessMapper processMapper ;

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
		List<MoneyPoolRepayment> list = moneyPoolRepaymentMapper.selectList(
				new EntityWrapper<MoneyPoolRepayment>()
				.eq("original_business_id", businessId)
				.eq("after_id",afterId)
				.in("money_pool_id", moneyPoolIds)
				.eq("is_finance_match", 1));
		
		if (list!=null&&list.size()>0) {
			return Result.error("500","已存在匹配的银行流水,不可重复匹配");
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
	@Transactional(rollbackFor=Exception.class)
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
			if(!mpUpdateRes) {
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
		RepaymentBizPlanList rpl = new RepaymentBizPlanList() ;
		rpl.setBusinessId(businessId);
		rpl.setAfterId(afterId);
		rpl = repaymentBizPlanListMapper.selectOne(rpl) ;
		if (rpl==null) {
			return Result.error("500", "找不到对应的还款计划");
		}
		List<RepaymentResource> resources = repaymentResourceMapper.selectList(new EntityWrapper<RepaymentResource>().eq("business_id", businessId).eq("after_id", afterId).eq("is_cancelled",0)) ;
		List<String> ids = new ArrayList<>();
		for (RepaymentResource o : resources) {
			if (o!=null&&o.getResourceId()!=null) {
				ids.add(o.getResourceId());
			}
		}
		
		List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper.selectList(new EntityWrapper<RepaymentProjFactRepay>().in("repay_ref_id", ids).orderBy("repay_ref_id"));
		
		List<JSONObject> infos = new ArrayList<>() ;
		
		
		return null;
	}
	
	private JSONObject thisPeriodPlanRepayment(RepaymentBizPlanList rpl) {
		JSONObject t = initThisPeriodPlanRepaymentBase() ;
		t.put("repayDate", DateUtil.toDateString(rpl.getDueDate(), DateUtil.DEFAULT_FORMAT_DATE)) ;
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", rpl.getPlanListId()));
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
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
				t.put("onlineOverDue", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
			if (repaymentBizPlanListDetail.getPlanItemType().equals(60) && repaymentBizPlanListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
				t.put("offlineOverDue", repaymentBizPlanListDetail.getPlanAmount());
				continue;
			}
		}
		
		List<ApplyDerateProcess> applyDerateProcesses = applyDerateProcessMapper.selectList(new EntityWrapper<ApplyDerateProcess>().eq("crp_id", rpl.getPlanListId()));
		for (ApplyDerateProcess applyDerateProcess : applyDerateProcesses) {
			Process process = new Process() ;
			process.setProcessId(applyDerateProcess.getProcessId());
			process = processMapper.selectOne(process) ;
		}
		
		
		return t ;
	}
	
	private JSONObject initThisPeriodPlanRepaymentBase() {
		JSONObject t = new JSONObject() ;
		t.put("item10", 0);
		t.put("item20", 0);
		t.put("item30", 0);
		t.put("item50", 0);
		t.put("subtotal", 0);
		t.put("offlineOverDue", 0);
		t.put("onlineOverDue", 0);
		t.put("derate", 0);
		t.put("total", 0);
		return t ;
	}

}
