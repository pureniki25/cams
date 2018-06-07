package com.hongte.alms.platRepay.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.TdrepayRechargeDetailService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platRepay.service.TdrepayRechargeService;
import com.hongte.alms.platRepay.vo.TdrepayRechargeInfoVO;
import com.ht.ussp.bean.LoginUserInfoHelper;

@Service("TdrepayRechargeService")
public class TdrepayRechargeServiceImpl implements TdrepayRechargeService {

	private static final Logger LOG = LoggerFactory.getLogger(TdrepayRechargeServiceImpl.class);

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("IssueSendOutsideLogService")
	private IssueSendOutsideLogService issueSendOutsideLogService;

	@Autowired
	@Qualifier("TdrepayRechargeLogService")
	private TdrepayRechargeLogService tdrepayRechargeLogService;

	@Autowired
	@Qualifier("TdrepayRechargeDetailService")
	private TdrepayRechargeDetailService tdrepayRechargeDetailService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTdrepayRechargeInfo(TdrepayRechargeInfoVO vo) {
		try {
			TdrepayRechargeLog rechargeLog = handleTdrepayRechargeLog(vo);

			tdrepayRechargeDetailService.insertBatch(vo.getDetailList());

			tdrepayRechargeLogService.insert(rechargeLog);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	private TdrepayRechargeLog handleTdrepayRechargeLog(TdrepayRechargeInfoVO vo) {

		TdrepayRechargeLog rechargeLog = new TdrepayRechargeLog();
		rechargeLog.setProjectId(vo.getProjectId());
		rechargeLog.setAssetType(vo.getAssetType());
		rechargeLog.setOrigBusinessId(vo.getOrigBusinessId());
		rechargeLog.setBusinessType(vo.getBusinessType());
		rechargeLog.setRepaymentType(vo.getRepaymentType());
		rechargeLog.setFactRepayDate(vo.getFactRepayDate());
		rechargeLog.setCustomerName(vo.getCustomerName());
		rechargeLog.setCompanyName(vo.getCompanyName());
		rechargeLog.setRepaySource(vo.getRepaySource());
		rechargeLog.setAfterId(vo.getAfterId());
		rechargeLog.setPeriod(vo.getPeriod());
		rechargeLog.setSettleType(vo.getSettleType());
		rechargeLog.setResourceAmount(vo.getResourceAmount());
		rechargeLog.setFactRepayAmount(vo.getFactRepayAmount());
		rechargeLog.setRechargeAmount(vo.getRechargeAmount());
		rechargeLog.setAdvanceType(vo.getAdvanceType());
		rechargeLog.setIsComplete(vo.getIsComplete());

		if (StringUtil.notEmpty(vo.getProjPlanListId())) {
			rechargeLog.setProjPlanListId(vo.getProjPlanListId());
		}

		rechargeLog.setProcessStatus(0); // 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
		rechargeLog.setCreateTime(new Date());
		rechargeLog.setCreateUser(loginUserInfoHelper.getUserId());

		return rechargeLog;
	}

}
