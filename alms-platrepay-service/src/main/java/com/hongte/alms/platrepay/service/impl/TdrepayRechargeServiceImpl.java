package com.hongte.alms.platrepay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.compliance.DistributeFundDTO;
import com.hongte.alms.base.dto.compliance.DistributeFundDetailDTO;
import com.hongte.alms.base.dto.compliance.TdAdvanceShareProfitDTO;
import com.hongte.alms.base.dto.compliance.TdDepaymentEarlierDTO;
import com.hongte.alms.base.dto.compliance.TdProjectPaymentInfoResult;
import com.hongte.alms.base.dto.compliance.TdRefundMonthInfoDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.entity.TdrepayAdvanceLog;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.AgencyRechargeLogMapper;
import com.hongte.alms.base.mapper.TdrepayRechargeLogMapper;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.TdrepayAdvanceLogService;
import com.hongte.alms.base.service.TdrepayRechargeDetailService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.base.vo.compliance.RechargeRecordReq;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.dto.TdGuaranteePaymentDTO;
import com.hongte.alms.platrepay.dto.TdProjectPaymentDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitDTO;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitResult;
import com.hongte.alms.platrepay.dto.TdrepayProjectInfoDTO;
import com.hongte.alms.platrepay.dto.TdrepayProjectPeriodInfoDTO;
import com.hongte.alms.platrepay.enums.ProcessStatusTypeEnum;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.core.Result;
import com.ht.ussp.util.BeanUtils;

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

	@Autowired
	@Qualifier("TdrepayAdvanceLogService")
	private TdrepayAdvanceLogService tdrepayAdvanceLogService;

	@Autowired
	private TdrepayRechargeLogMapper tdrepayRechargeLogMapper;
	
	@Autowired
	private AgencyRechargeLogMapper agencyRechargeLogMapper;

	@Autowired
	private EipRemote eipRemote;

	@Value("${recharge.account.car.loan}")
	private String carLoanUserId;
	@Value("${recharge.account.house.loan}")
	private String houseLoanUserId;
	@Value("${recharge.account.relief.loan}")
	private String reliefLoanUserId;
	@Value("${recharge.account.quick.loan}")
	private String quickLoanUserId;
	@Value("${recharge.account.car.business}")
	private String carBusinessUserId;
	@Value("${recharge.account.second.hand.car.loan}")
	private String secondHandCarLoanUserId;
	@Value("${recharge.account.yi.dian.car.loan}")
	private String yiDianCarLoanUserId;
	@Value("${recharge.account.credit.loan}")
	private String creditLoanUserId;

	@Value("${recharge.account.car.loan.oid.partner}")
	private String carLoanOidPartner;
	@Value("${recharge.account.house.loan.oid.partner}")
	private String houseLoanOidPartner;
	@Value("${recharge.account.relief.loan.oid.partner}")
	private String reliefLoanOidPartner;
	@Value("${recharge.account.quick.loan.oid.partner}")
	private String quickLoanOidPartner;
	@Value("${recharge.account.car.business.loan.oid.partner}")
	private String carBusinessOidPartner;
	@Value("${recharge.account.second.hand.car.loan.oid.partner}")
	private String secondHandCarLoanOidPartner;
	@Value("${recharge.account.yi.dian.car.loan.oid.partner}")
	private String yiDianCarLoanOidPartner;
	@Value("${recharge.account.credit.loan.oid.partner}")
	private String creditLoanOidPartner;

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTdrepayRechargeInfo(TdrepayRechargeInfoVO vo) {
		try {
			TdrepayRechargeLog rechargeLog = handleTdrepayRechargeLog(vo);

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("orgType", 1); // 机构类型 传输任意值
			paramMap.put("projectId", vo.getProjectId());

			Result result = eipRemote.getProjectPayment(paramMap);

			if (result != null && result.getData() != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
				TdrepayProjectInfoDTO tdrepayProjectInfoDTO = JSONObject
						.parseObject(JSONObject.toJSONString(result.getData()), TdrepayProjectInfoDTO.class);
				List<TdrepayProjectPeriodInfoDTO> periodsList = tdrepayProjectInfoDTO.getPeriodsList();
				if (CollectionUtils.isNotEmpty(periodsList)) {

					for (TdrepayProjectPeriodInfoDTO tdrepayProjectPeriodInfoDTO : periodsList) {
						int periods = tdrepayProjectPeriodInfoDTO.getPeriods();
						int peroidVO = vo.getPeriod().intValue();
						if (peroidVO == periods) {
							rechargeLog.setPlatStatus(String.valueOf(tdrepayProjectPeriodInfoDTO.getStatus()));
						}
					}
				}
			}

			List<TdrepayRechargeDetail> detailList = vo.getDetailList();
			if (CollectionUtils.isNotEmpty(detailList)) {

				String logId = rechargeLog.getLogId();

				for (TdrepayRechargeDetail tdrepayRechargeDetail : detailList) {
					tdrepayRechargeDetail.setLogId(logId);
					tdrepayRechargeDetail.setCreateTime(new Date());
					tdrepayRechargeDetail.setCreateUser(loginUserInfoHelper.getUserId());
					tdrepayRechargeDetailService.insert(tdrepayRechargeDetail);
				}
			}
			tdrepayRechargeLogService.insert(rechargeLog);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	private TdrepayRechargeLog handleTdrepayRechargeLog(TdrepayRechargeInfoVO vo) {

		TdrepayRechargeLog rechargeLog = new TdrepayRechargeLog();
		rechargeLog.setLogId(UUID.randomUUID().toString());
		rechargeLog.setProjectId(vo.getProjectId());
		rechargeLog.setAssetType(vo.getAssetType());
		rechargeLog.setOrigBusinessId(vo.getOrigBusinessId());
		rechargeLog.setBusinessType(vo.getBusinessType());
		rechargeLog.setFactRepayDate(vo.getFactRepayDate());
		rechargeLog.setCustomerName(vo.getCustomerName());
		rechargeLog.setCompanyName(vo.getCompanyName());
		rechargeLog.setRepaySource(vo.getRepaySource());
		rechargeLog.setAfterId(vo.getAfterId());
		rechargeLog.setPeriod(vo.getPeriod());
		rechargeLog.setSettleType(vo.getSettleType());
		rechargeLog.setConfirmLogId(vo.getConfirmLogId());
		rechargeLog.setConfirmTime(vo.getConfirmTime());
		rechargeLog.setResourceAmount(vo.getResourceAmount());
		rechargeLog.setFactRepayAmount(vo.getFactRepayAmount());
		rechargeLog.setRechargeAmount(vo.getRechargeAmount());
		rechargeLog.setIsComplete(vo.getIsComplete());
		rechargeLog.setTdUserId(vo.getTdUserId());

		if (StringUtil.notEmpty(vo.getProjPlanListId())) {
			rechargeLog.setProjPlanListId(vo.getProjPlanListId());
		}

		rechargeLog.setProcessStatus(0); // 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
		rechargeLog.setCreateTime(new Date());
		rechargeLog.setCreateUser(loginUserInfoHelper.getUserId());

		return rechargeLog;
	}

	@Override
	public List<TdrepayRechargeLog> queryComplianceRepaymentData(ComplianceRepaymentVO vo) {
		return tdrepayRechargeLogMapper.queryComplianceRepaymentData(vo);
	}

	@Override
	public int countComplianceRepaymentData(ComplianceRepaymentVO vo) {
		return tdrepayRechargeLogMapper.countComplianceRepaymentData(vo);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void tdrepayRecharge(List<TdrepayRechargeInfoVO> vos) {

		/*
		 * 只有还款来源是线下还款和第三方代扣的才需使用代充值账户进行资金分发
		 * 
		 * 还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
		 */
		mismatchConditionFilter(vos);

		String userId = loginUserInfoHelper.getUserId();

		try {

			/*
			 * 将分发状态更新为处理中 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
			 */
			updateTdrepayRechargeLogProcessStatus(vos, 1, userId);

			/*
			 * 由于每种业务类型对应一个资产端账户唯一编号，根据业务类型进行分批
			 */
			Map<Integer, List<List<TdrepayRechargeInfoVO>>> dtoMap = groupTdrepayRechargeInfoVOByBusinessType(vos);

			if (!dtoMap.isEmpty()) {
				for (Entry<Integer, List<List<TdrepayRechargeInfoVO>>> entry : dtoMap.entrySet()) {
					List<List<TdrepayRechargeInfoVO>> infoVOs = entry.getValue();
					for (List<TdrepayRechargeInfoVO> rechargeInfoVOs : infoVOs) {
						// 调用 eip 平台资金分发接口
						Result result = sendDistributeFund(rechargeInfoVOs, entry.getKey(), userId);
						handleSendDistributeFundResult(vos, userId, result);
					}

				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			/*
			 * 将分发状态更新为处理中 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
			 */
			updateTdrepayRechargeLogProcessStatus(vos, 0, userId);

			throw new ServiceRuntimeException("资金分发执行失败", e);
		}

	}

	/**
	 * 过滤不符合资金分发条件的数据
	 * 
	 * @param vos
	 */
	private void mismatchConditionFilter(List<TdrepayRechargeInfoVO> vos) {
		if (CollectionUtils.isEmpty(vos)) {
			return;
		}

		List<TdrepayRechargeInfoVO> lstVO = new LinkedList<>();
		for (TdrepayRechargeInfoVO vo : vos) {
			Integer repaySource = vo.getRepaySource();
			Integer processStatus = vo.getProcessStatus();
			if ((repaySource != null && repaySource.intValue() != 1 && repaySource.intValue() != 2)
					|| (processStatus != null && (processStatus.intValue() == 1 || processStatus.intValue() == 2))) {
				lstVO.add(vo);
			}
		}

		vos.removeAll(lstVO);

		if (CollectionUtils.isEmpty(vos)) {
			throw new ServiceRuntimeException("请重新选择符合资金分发条件的数据");
		}
	}

	/**
	 * 更新资金分发失败
	 * 
	 * @param vos
	 * @param userId
	 * @param result
	 */
	@SuppressWarnings("rawtypes")
	private void handleSendDistributeFundResult(List<TdrepayRechargeInfoVO> vos, String userId, Result result) {
		if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
			updateTdrepayRechargeLogProcessStatus(vos, 2, userId);
		} else {
			updateTdrepayRechargeLogProcessStatus(vos, 3, userId);
		}
	}

	/**
	 * 更新资金分发状态
	 * 
	 * @param vos
	 * @param processStatus
	 * @param userId
	 */
	private List<TdrepayRechargeLog> updateTdrepayRechargeLogProcessStatus(List<TdrepayRechargeInfoVO> vos,
			Integer processStatus, String userId) {
		List<TdrepayRechargeLog> rechargeLogs = new LinkedList<>();
		for (TdrepayRechargeInfoVO vo : vos) {
			TdrepayRechargeLog tdrepayRechargeLog = new TdrepayRechargeLog();
			tdrepayRechargeLog.setLogId(vo.getLogId());
			tdrepayRechargeLog.setProcessStatus(processStatus); // 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
			tdrepayRechargeLog.setUpdateTime(new Date());
			tdrepayRechargeLog.setUpdateUser(userId);
			rechargeLogs.add(tdrepayRechargeLog);
		}
		tdrepayRechargeLogService.updateBatchById(rechargeLogs);

		return rechargeLogs;
	}

	/**
	 * 调用 eip 平台资金分发接口
	 * 
	 * @param rechargeInfoVOs
	 * @param businessType
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Result sendDistributeFund(List<TdrepayRechargeInfoVO> rechargeInfoVOs, Integer businessType,
			String userId) {
		DistributeFundDTO dto = new DistributeFundDTO();
		String batchId = UUID.randomUUID().toString();
		dto.setBatchId(batchId);
		String rechargeAccountType = getRechargeAccountTypeByBusinessType(businessType);
		String oIdPartner = handleOIdPartner(rechargeAccountType);
		dto.setOidPartner(oIdPartner);
		String clientIp = CommonUtil.getClientIp();
		dto.setUserIP(clientIp);
		String logUserId = handleAccountType(rechargeAccountType);
		dto.setUserId(logUserId);

		/*
		 * 将rechargeInfoVOs中的数据装入一个批次中
		 */
		List<DistributeFundDetailDTO> detailList = new LinkedList<>();

		double totalAmount = 0;

		/*
		 * 记录调用第三方接口日志
		 */
		IssueSendOutsideLog outsideLog = new IssueSendOutsideLog();
		outsideLog.setSystem(Constant.SYSTEM_CODE_EIP);
		outsideLog.setSendKey(batchId);

		List<TdrepayRechargeLog> tdrepayRechargeLogs = new LinkedList<>();
		for (TdrepayRechargeInfoVO vo : rechargeInfoVOs) {
			// 团贷网用户唯一编号 必须为guid转化成的字符串
			String requestNo = StringUtil.isEmpty(vo.getRequestNo()) ? UUID.randomUUID().toString() : vo.getRequestNo();

			String tdUserId = vo.getTdUserId();
			vo.setRequestNo(requestNo);
			// 设置统一 batchId
			vo.setBatchId(batchId);

			double amount = vo.getRechargeAmount() == null ? 0 : vo.getRechargeAmount().doubleValue();

			totalAmount += amount;

			DistributeFundDetailDTO detailDTO = new DistributeFundDetailDTO();
			detailDTO.setAmount(amount);
			detailDTO.setRequestNo(requestNo);
			detailDTO.setUserId(tdUserId);
			detailList.add(detailDTO);

			TdrepayRechargeLog tdrepayRechargeLog = new TdrepayRechargeLog();
			tdrepayRechargeLog.setLogId(vo.getLogId());
			tdrepayRechargeLog.setBatchId(batchId);
			tdrepayRechargeLog.setRequestNo(requestNo);
			tdrepayRechargeLog.setTotalAmount(BigDecimal.valueOf(totalAmount));
			tdrepayRechargeLog.setTdUserId(tdUserId);
			tdrepayRechargeLog.setUserId(logUserId);
			tdrepayRechargeLog.setUserIp(clientIp);
			tdrepayRechargeLog.setOidPartner(oIdPartner);
			tdrepayRechargeLog.setRechargeAmount(BigDecimal.valueOf(amount));
			tdrepayRechargeLog.setUpdateTime(new Date());
			tdrepayRechargeLog.setUpdateUser(userId);
			tdrepayRechargeLogs.add(tdrepayRechargeLog);
		}
		tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);

		dto.setTotalAmount(totalAmount);
		dto.setDetailList(detailList);

		outsideLog.setSendJson(JSONObject.toJSONString(dto));
		outsideLog.setInterfacecode(Constant.INTERFACE_CODE_SEND_DISTRIBUTE_FUND);
		outsideLog.setInterfacename(Constant.INTERFACE_NAME_SEND_DISTRIBUTE_FUND);
		outsideLog.setCreateTime(new Date());
		outsideLog.setCreateUserId(userId);

		Result result = null;

		try {
			// 调用 eip 平台资金分发接口
			result = eipRemote.userDistributeFund(dto);
		} catch (Exception e) {
			LOG.error("批次号:" + batchId + "，调用eip平台资金分发接口失败！DTO 数据：" + dto.toString(), e);
			outsideLog.setReturnJson(e.getMessage());
		}

		if (result != null) {
			outsideLog.setReturnJson(JSONObject.toJSONString(result));
		}
		issueSendOutsideLogService.insert(outsideLog);
		return result;

	}

	private Map<Integer, List<List<TdrepayRechargeInfoVO>>> groupTdrepayRechargeInfoVOByBusinessType(
			List<TdrepayRechargeInfoVO> vos) {
		Map<Integer, List<TdrepayRechargeInfoVO>> dtoMap = new HashMap<>();

		for (TdrepayRechargeInfoVO vo : vos) {

			Integer businessType = vo.getBusinessType();

			List<TdrepayRechargeInfoVO> infoVOs = dtoMap.get(businessType);

			if (infoVOs == null) {
				List<TdrepayRechargeInfoVO> list = new ArrayList<>();
				list.add(vo);
				dtoMap.put(businessType, list);
			} else {
				infoVOs.add(vo);
				dtoMap.put(businessType, infoVOs);
			}
		}

		Map<Integer, List<List<TdrepayRechargeInfoVO>>> map = new HashMap<>();

		/*
		 * 每个批次不能大于 50 条，超过的批次重新分组
		 */
		for (Entry<Integer, List<TdrepayRechargeInfoVO>> entry : dtoMap.entrySet()) {
			List<TdrepayRechargeInfoVO> list = entry.getValue();
			if (list.size() > 50) {
				List<List<TdrepayRechargeInfoVO>> wrapList = new ArrayList<>();
				int count = 0;
				while (count < list.size()) {
					wrapList.add(new ArrayList<TdrepayRechargeInfoVO>(
							list.subList(count, (count + 50) > list.size() ? list.size() : (count + 50))));
					count += 50;
				}
				map.put(entry.getKey(), wrapList);
			} else {
				List<List<TdrepayRechargeInfoVO>> arrayList = new ArrayList<>();
				arrayList.add(list);
				map.put(entry.getKey(), arrayList);
			}
		}

		return map;
	}

	/**
	 * 根据业务类型获取代充值账户类型
	 * 
	 * @param businessType
	 *            业务类型
	 * @return
	 */
	private String getRechargeAccountTypeByBusinessType(Integer businessType) {

		String rechargeAccountType = "";

		if (businessType == null) {
			return rechargeAccountType;
		}

		switch (businessType) {
		case 9:
			rechargeAccountType = "车贷代充值";
			break;
		case 11:
			rechargeAccountType = "房贷代充值";
			break;
		case 20:
			rechargeAccountType = "一点车贷代充值";
			break;
		case 25:
			rechargeAccountType = "信用贷代充值";
			break;
		case 26:
			rechargeAccountType = "信用贷代充值";
			break;
		case 27:
			rechargeAccountType = "信用贷代充值";
			break;
		case 28:
			rechargeAccountType = "信用贷代充值";
			break;
		case 29:
			rechargeAccountType = "信用贷代充值";
			break;
		default:
			rechargeAccountType = "";
			break;
		}
		return rechargeAccountType;

	}

	@Override
	public String handleAccountType(String rechargeAccountType) {

		String userId = null;

		if (StringUtil.isEmpty(rechargeAccountType)) {
			return userId;
		}

		switch (rechargeAccountType) {
		case "车贷代充值":
			userId = carLoanUserId;
			break;
		case "房贷代充值":
			userId = houseLoanUserId;
			break;
		case "扶贫贷代充值":
			userId = reliefLoanUserId;
			break;
		case "闪贷业务代充值":
			userId = quickLoanUserId;
			break;
		case "车全业务代充值":
			userId = carBusinessUserId;
			break;
		case "二手车业务代充值":
			userId = secondHandCarLoanUserId;
			break;
		case "一点车贷代充值":
			userId = yiDianCarLoanUserId;
			break;
		case "信用贷代充值":
			userId = creditLoanUserId;
			break;
		default:
			userId = "";
			break;
		}
		return userId;
	}

	@Override
	public String handleOIdPartner(String rechargeAccountType) {

		String oIdPartner = null;

		if (StringUtil.isEmpty(rechargeAccountType)) {
			return oIdPartner;
		}

		switch (rechargeAccountType) {
		case "车贷代充值":
			oIdPartner = carLoanOidPartner;
			break;
		case "房贷代充值":
			oIdPartner = houseLoanOidPartner;
			break;
		case "扶贫贷代充值":
			oIdPartner = reliefLoanOidPartner;
			break;
		case "闪贷业务代充值":
			oIdPartner = quickLoanOidPartner;
			break;
		case "车全业务代充值":
			oIdPartner = carBusinessOidPartner;
			break;
		case "二手车业务代充值":
			oIdPartner = secondHandCarLoanOidPartner;
			break;
		case "一点车贷代充值":
			oIdPartner = yiDianCarLoanOidPartner;
			break;
		case "信用贷代充值":
			oIdPartner = creditLoanOidPartner;
			break;
		default:
			oIdPartner = "";
			break;
		}
		return oIdPartner;
	}

	@Override
	public int handleTdUserName(int businessType) {
		int orgType = -1;

		switch (businessType) {
		case 25:
			orgType = 0;
			break;
		case 26:
			orgType = 1;
			break;
		case 27:
			orgType = 2;
			break;
		case 28:
			orgType = 3;
			break;
		case 29:
			orgType = 4;
			break;
		default:
			break;
		}
		return orgType;
	}

	@Override
	public void repayComplianceWithRequirements() {
		/*
		 * 读取待处理和处理失败的，且资金分发成功的数据
		 */
		List<TdrepayRechargeLog> tdrepayRechargeLogs = queryRechargeSuccessData();
		if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
			return;
		}

		try {
			// 按照projectId分组数据
			// 结构：Map<projectId, Map<period, List<所有期数的数据>>>
			Map<String, Map<Integer, List<TdrepayRechargeLog>>> mapGroupByProjectId = mapGroupByProjectId(
					tdrepayRechargeLogs);

			for (Entry<String, Map<Integer, List<TdrepayRechargeLog>>> entry : mapGroupByProjectId.entrySet()) {
				// periodDataMap是同projectId， 按 period 分组，且按 period 从大到小排序后的数据
				Map<Integer, List<TdrepayRechargeLog>> periodDataMap = entry.getValue();

				for (Entry<Integer, List<TdrepayRechargeLog>> periodDataEntry : periodDataMap.entrySet()) {

					// 同 period 数据集合，已按照period从小到大排序
					List<TdrepayRechargeLog> rechargeLogs = periodDataEntry.getValue();

					// 按照createTime从小到大排序，优先处理最早传入的数据
					sortChargeLogListByConfirmTime(rechargeLogs);

					if (differentiateSettleData(rechargeLogs)) {

						// 处理结清数据
						handleSettleData(rechargeLogs, tdrepayRechargeLogs);

					} else {
						// 处理未结清数据
						handleNotSettleData(rechargeLogs, tdrepayRechargeLogs);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				tdrepayRechargeLog.setStatus(0);
				tdrepayRechargeLog.setUpdateTime(new Date());
				tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
			}
			tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
		}
	}

	/**
	 * 判断是否结清与未结清
	 * 
	 * @param map
	 */
	private boolean differentiateSettleData(List<TdrepayRechargeLog> tdrepayRechargeLogs) {

		// tdrepayRechargeLogs为 一个 projectId 某一期的所有数据
		if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
			return false;
		} else {
			// 取最后一次传入的结清状态
			Integer settleType = tdrepayRechargeLogs.get(tdrepayRechargeLogs.size() - 1).getSettleType();

			// 如果是结清数据
			if (settleType != null && (settleType.intValue() == 10 || settleType.intValue() == 11
					|| settleType.intValue() == 20 || settleType.intValue() == 30)) {
				return true;
			} else if (settleType != null && (settleType.intValue() == 0)) { // 如果是未结清数据
				return false;
			}
			return false;
		}
	}

	/**
	 * 根据projectId分组
	 * 
	 * @param tdrepayRechargeLogs
	 * @return
	 */
	private Map<String, Map<Integer, List<TdrepayRechargeLog>>> mapGroupByProjectId(
			List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		Map<String, List<TdrepayRechargeLog>> mapGroupByProjectId = new HashMap<>();
		for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
			String projectId = tdrepayRechargeLog.getProjectId();
			List<TdrepayRechargeLog> list = mapGroupByProjectId.get(projectId);
			if (list == null) {
				list = new ArrayList<>();
				list.add(tdrepayRechargeLog);
				mapGroupByProjectId.put(projectId, list);
			} else {
				list.add(tdrepayRechargeLog);
				mapGroupByProjectId.put(projectId, list);
			}
		}

		Map<String, Map<Integer, List<TdrepayRechargeLog>>> projectIdMap = new HashMap<>();

		for (Entry<String, List<TdrepayRechargeLog>> entry : mapGroupByProjectId.entrySet()) {

			// projectId 分组后的数据， 每一个list都是同一个projectId的集合
			List<TdrepayRechargeLog> rechargeLogs = entry.getValue();

			if (CollectionUtils.isNotEmpty(rechargeLogs)) {
				// list按照期次 Period 分组，并按照Period从小到大排序
				Map<Integer, List<TdrepayRechargeLog>> map = repayChargeLogGroupByPeriod(rechargeLogs);
				projectIdMap.put(rechargeLogs.get(0).getProjectId(), map);
			}
		}

		return projectIdMap;
	}

	/**
	 * 处理未结清数据
	 * 
	 * @param repayChargeLogs
	 */
	@SuppressWarnings("rawtypes")
	private void handleNotSettleData(List<TdrepayRechargeLog> repayChargeLogs,
			List<TdrepayRechargeLog> tdrepayRechargeLogs) {

		if (CollectionUtils.isNotEmpty(repayChargeLogs)) {

			String projectId = repayChargeLogs.get(0).getProjectId();
			Integer period = repayChargeLogs.get(0).getPeriod();

			Map<String, Result> map = this.getAdvanceShareProfitAndProjectPayment(projectId);

			Result queryProjectPaymentResult = map.get("queryProjectPaymentResult");
			Result advanceShareProfitResult = map.get("advanceShareProfitResult");

			if (queryProjectPaymentResult != null && advanceShareProfitResult != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())) {

				// 判断当前期是否存在垫付未还记录
				// TODO 待提供判断是否存在垫付未还记录查询接口
				if (isCurrPeriodAdvance(map.get("queryProjectPaymentResult"), map.get("advanceShareProfitResult"),
						period)) {

					// repayChargeLogs 是同projectId，同period 按照createTime从大到小排好序的数据
					for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {

						// 根据logId 获取对应的费用明细列表

						List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService.selectList(
								new EntityWrapper<TdrepayRechargeDetail>().eq("log_id", tdrepayRechargeLog.getLogId()));

						if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {

							// 调用 偿还垫付接口 ， 按期数从小到大顺序调用，若某一期执行失败，则后面期数本次不再继续执行

							Result result = remoteAdvanceShareProfit(tdrepayRechargeLog, tdrepayRechargeDetails);
							if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

								// 标记处理成功,流程结束
								tdrepayRechargeLog.setStatus(2);

								tdrepayRechargeLogService.updateById(tdrepayRechargeLog);

							} else {

								// 标记处理失败,待定时任务重试
								tdrepayRechargeLog.setStatus(3);

								tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
								// 处理每一期的一组数据时，如果某条处理失败，则当期后面的数据本次流程不再处理
								break;
							}
						}
					}
				} else {
					for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
						tdrepayRechargeLog.setStatus(2);
						tdrepayRechargeLog.setUpdateTime(new Date());
						tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
					}
					tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
				}
			} else {
				for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
					tdrepayRechargeLog.setStatus(3);
					tdrepayRechargeLog.setUpdateTime(new Date());
					tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
				}
				tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
			}

			tdrepayRechargeLogs.removeAll(repayChargeLogs);
		}
	}

	/**
	 * 处理结清数据
	 */
	private void handleSettleData(List<TdrepayRechargeLog> repayChargeLogs,
			List<TdrepayRechargeLog> tdrepayRechargeLogs) {

		// repayChargeLogs 均为结清期的数据, 同projectId,同period,且已经根据createTime从大到小排序
		if (CollectionUtils.isNotEmpty(repayChargeLogs)) {

			boolean isSuccess = handleSettleAdvanceData(repayChargeLogs);

			/*
			 * 一、 处理还垫付
			 */
			if (isSuccess) {

				/*
				 * 二、若还垫付处理成功，则继续处理是否提前结清
				 */
				handleRepaymentEarlierData(repayChargeLogs);
			}

			tdrepayRechargeLogs.removeAll(repayChargeLogs);
		}
	}

	/**
	 * 处理提前结清数据
	 * 
	 * @param repayChargeLogs
	 */
	@SuppressWarnings("rawtypes")
	private void handleRepaymentEarlierData(List<TdrepayRechargeLog> repayChargeLogs) {

		// 调用标的还款信息查询接口 /assetside/getProjectPayment

		Result remoteGetProjectPaymentResult = null;
		try {
			remoteGetProjectPaymentResult = this.remoteGetProjectPayment(repayChargeLogs.get(0).getProjectId());
		} catch (Exception e) {
			for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
				// 标记为处理失败，待下次定时任务重试
				tdrepayRechargeLog.setStatus(3);
				tdrepayRechargeLog.setUpdateTime(new Date());
				tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
			}
			tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
			return;
		}

		if (remoteGetProjectPaymentResult != null
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(remoteGetProjectPaymentResult.getReturnCode())) {

			TdProjectPaymentInfoResult tdProjectPaymentInfoResult = JSONObject.parseObject(
					JSONObject.toJSONString(remoteGetProjectPaymentResult.getData()), TdProjectPaymentInfoResult.class);

			if (tdProjectPaymentInfoResult != null) {

				List<TdRefundMonthInfoDTO> periodsList = tdProjectPaymentInfoResult.getPeriodsList();

				// 若是提前结清
				if (isAdvanceSettle(repayChargeLogs, periodsList)) {

					// 计算当期提前结清应还分润
					Map<String, Double> map = totalRepaymentEarlierFinances(repayChargeLogs);

					// 计算提前结清应还分润
					Double assetsCharge = map.get("assetsCharge"); // 资产端服务费
					Double guaranteeCharge = map.get("guaranteeCharge"); // 担保公司服务费
					Double agencyCharge = map.get("agencyCharge"); // 中介公司服务费

					Result remoteRepaymentEarlierResult = remoteRepaymentEarlier(repayChargeLogs, assetsCharge,
							guaranteeCharge, agencyCharge);
					if (remoteRepaymentEarlierResult != null
							&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(remoteRepaymentEarlierResult.getReturnCode())) {
						for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
							// 标记为处理成功，流程结束
							tdrepayRechargeLog.setStatus(2);
							tdrepayRechargeLog.setUpdateTime(new Date());
							tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
						}
						tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
					} else {
						for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
							// 标记为处理失败，待下次定时任务重试
							tdrepayRechargeLog.setStatus(3);
							tdrepayRechargeLog.setUpdateTime(new Date());
							tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
						}
						tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
					}
				}
			}
		}
	}

	/**
	 * 计算提前结清应还分润
	 * 
	 * @param repayChargeLogs
	 * @return
	 */
	private Map<String, Double> totalRepaymentEarlierFinances(List<TdrepayRechargeLog> repayChargeLogs) {

		Map<String, Double> resultMap = new HashMap<>();

		Double assetsCharge = 0.0; // 资产端服务费
		Double guaranteeCharge = 0.0; // 担保公司服务费
		Double agencyCharge = 0.0; // 中介公司服务费

		List<String> logIds = new LinkedList<>();
		for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
			logIds.add(tdrepayRechargeLog.getLogId());
		}

		List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
				.selectList(new EntityWrapper<TdrepayRechargeDetail>().in("log_id", logIds));

		// 计算提前结清应还分润
		for (TdrepayRechargeDetail detail : tdrepayRechargeDetails) {
			Integer feeType = detail.getFeeType();
			BigDecimal feeValue = detail.getFeeValue();
			switch (feeType) {
			case 40:
				assetsCharge += (feeValue == null ? 0 : feeValue.doubleValue());
				break;
			case 50:
				guaranteeCharge += (feeValue == null ? 0 : feeValue.doubleValue());
				break;
			case 80:
				agencyCharge += (feeValue == null ? 0 : feeValue.doubleValue());
				break;

			default:
				break;
			}
		}

		resultMap.put("assetsCharge", assetsCharge);
		resultMap.put("guaranteeCharge", guaranteeCharge);
		resultMap.put("agencyCharge", agencyCharge);
		return resultMap;
	}

	/**
	 * 远程调用提前结清接口
	 * 
	 * @param repayChargeLogs
	 * @param assetsCharge
	 * @param guaranteeCharge
	 * @param agencyCharge
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Result remoteRepaymentEarlier(List<TdrepayRechargeLog> repayChargeLogs, Double assetsCharge,
			Double guaranteeCharge, Double agencyCharge) {
		TdrepayRechargeLog tdrepayRechargeLog = repayChargeLogs.get(repayChargeLogs.size() - 1);

		// 提前结清接口参数DTO
		TdDepaymentEarlierDTO tdDepaymentEarlierDTO = new TdDepaymentEarlierDTO();
		tdDepaymentEarlierDTO.setAgencyCharge(BigDecimal.valueOf(agencyCharge == null ? 0 : agencyCharge));
		tdDepaymentEarlierDTO.setAssetsCharge(BigDecimal.valueOf(assetsCharge == null ? 0 : assetsCharge));
		tdDepaymentEarlierDTO.setGuaranteeCharge(BigDecimal.valueOf(guaranteeCharge == null ? 0 : guaranteeCharge));
		tdDepaymentEarlierDTO.setProjectId(tdrepayRechargeLog.getProjectId());

		// 判断是否坏账结清
		int settleType = tdrepayRechargeLog.getSettleType().intValue();
		if (settleType == 30) {
			tdDepaymentEarlierDTO.setType(Constant.REPAYMENT_EARLIER_BAD);
		} else if (settleType == 10 || settleType == 11 || settleType == 20) {
			tdDepaymentEarlierDTO.setType(Constant.REPAYMENT_EARLIER_NORMAL);
		}

		IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(loginUserInfoHelper.getUserId(),
				tdDepaymentEarlierDTO, Constant.INTERFACE_CODE_REPAYMENT_EARLIER,
				Constant.INTERFACE_NAME_REPAYMENT_EARLIER, Constant.SYSTEM_CODE_EIP, tdrepayRechargeLog.getProjectId());

		Result repaymentEarlierResult = null;

		try {
			// 调用提前结清接口
			repaymentEarlierResult = eipRemote.repaymentEarlier(tdDepaymentEarlierDTO);
		} catch (Exception e) {
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		if (repaymentEarlierResult != null) {
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(repaymentEarlierResult));
		}
		issueSendOutsideLogService.insert(issueSendOutsideLog);

		return repaymentEarlierResult;
	}

	/**
	 * 处理结清数据还垫付
	 * 
	 * @param repayChargeLogs
	 * @param tdrepayRechargeDetails
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean handleSettleAdvanceData(List<TdrepayRechargeLog> repayChargeLogs) {

		// repayChargeLogs 均为结清期的数据, 同projectId,同period,且已经根据createTime从大到小排序

		// 从平台获取标的还款信息、还垫付信息 （标的维度）
		String projectId = repayChargeLogs.get(0).getProjectId();
		Integer period = repayChargeLogs.get(0).getPeriod();
		Integer businessType = repayChargeLogs.get(0).getBusinessType();

		Map<String, Result> map = new HashMap<>();
		try {
			map = this.getAdvanceShareProfitAndProjectPayment(projectId);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
				tdrepayRechargeLog.setStatus(3);
				tdrepayRechargeLog.setUpdateTime(new Date());
				tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
			}
			tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
			return false;
		}

		boolean flag = true; // 标记，是否还垫付成功

		Result queryProjectPaymentResult = null;
		Result advanceShareProfitResult = null;

		if (!map.isEmpty()) {
			queryProjectPaymentResult = map.get("queryProjectPaymentResult");
			advanceShareProfitResult = map.get("advanceShareProfitResult");
		}

		// 1、判断往期是否存在未还垫付
		// TODO 待提供判断是否存在垫付未还记录查询接口

		/*
		 * 去掉对往期的判断，在前端调用平台还款接口时做验证，如果往期有未结清期，则数据不能通过。
		 */
		/*
		 * boolean pastPeriodAdvanceFlag = isPastPeriodAdvance(projectId, period,
		 * businessType, queryProjectPaymentResult, advanceShareProfitResult);
		 * 
		 * if (!pastPeriodAdvanceFlag) {
		 * 
		 * for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) { //
		 * 标记处理失败,待定时任务重试 tdrepayRechargeLog.setStatus(3);
		 * tdrepayRechargeLog.setUpdateTime(new Date());
		 * tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId()); }
		 * tdrepayRechargeLogService.updateBatchById(repayChargeLogs); return false; }
		 */

		// 2、判断当前期是否有垫付
		// TODO 待提供判断是否存在垫付未还记录查询接口
		Boolean currPeriodAdvanceFlag = isCurrPeriodAdvance(queryProjectPaymentResult, advanceShareProfitResult,
				period);
		if (currPeriodAdvanceFlag != null && currPeriodAdvanceFlag) {

			for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {

				// 根据logId 获取对应的费用明细列表
				List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
						.selectList(new EntityWrapper<TdrepayRechargeDetail>()
								.eq("log_id", tdrepayRechargeLog.getLogId()).orderBy("fee_type"));

				if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {

					// 调用 偿还垫付接口 ， 按期数从小到大顺序调用，若某一期执行失败，则后面期数本次不再继续执行
					Result result = remoteAdvanceShareProfit(tdrepayRechargeLog, tdrepayRechargeDetails);

					if (result == null || !Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

						flag = false;

						// 标记处理失败,待定时任务重试
						tdrepayRechargeLog.setStatus(3);

						tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
						break;
					}
				}
			}
		}

		return flag;
	}

	/**
	 * 处理往期是否有垫付未还记录
	 * 
	 * @param projectId
	 * @param period
	 * @param queryProjectPaymentResult
	 * @param advanceShareProfitResult
	 */
	@SuppressWarnings("rawtypes")
	private boolean isPastPeriodAdvance(String projectId, Integer period, Integer businessType,
			Result queryProjectPaymentResult, Result advanceShareProfitResult) {
		if (queryProjectPaymentResult != null && advanceShareProfitResult != null
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())) {

			// 标的还款信息
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;
			if (queryProjectPaymentResult.getData() != null) {
				JSONObject parseObject = (JSONObject) JSONObject.toJSON(queryProjectPaymentResult.getData());
				if (parseObject.get("projectPayments") != null) {
					tdProjectPaymentDTOs = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
				}
			}

			// 还垫付信息
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = null;
			if (advanceShareProfitResult.getData() != null) {
				String json2 = JSONObject.toJSONString(advanceShareProfitResult.getData());
				returnAdvanceShareProfitResult = JSONObject.parseObject(json2, TdReturnAdvanceShareProfitResult.class);
			}

			// 担保公司垫付信息
			double principalAndInterest = 0; // 本金利息
			double tuandaiAmount = 0; // 实还平台服务费
			double orgAmount = 0; // 实还资产端服务费
			double guaranteeAmount = 0; // 实还担保公司服务费
			double arbitrationAmount = 0; // 实还仲裁服务费

			// 还垫付信息
			double principalAndInterest2 = 0; // 本金利息
			double tuandaiAmount2 = 0; // 实还平台服务费
			double orgAmount2 = 0; // 实还资产端服务费
			double guaranteeAmount2 = 0; // 实还担保公司服务费
			double arbitrationAmount2 = 0; // 实还仲裁服务费

			// 得到往期的标的还款信息
			List<TdProjectPaymentDTO> pastPeriodDTOs = handlePastPeriodTdProjectPaymentDTO(period,
					tdProjectPaymentDTOs);

			// 得到往期的标的还垫付信息
			List<TdReturnAdvanceShareProfitDTO> tdReturnAdvanceShareProfitDTOs = handlePastPeriodTdReturnAdvanceShareProfitDTO(
					returnAdvanceShareProfitResult, period);

			if (CollectionUtils.isEmpty(pastPeriodDTOs)) {
				return false;
			}

			// 判断往期是否有未还垫付记录
			for (TdProjectPaymentDTO tdProjectPaymentDTO : pastPeriodDTOs) {

				TdGuaranteePaymentDTO guaranteePayment = tdProjectPaymentDTO.getGuaranteePayment();

				if (guaranteePayment != null) {

					int tdPeriod = tdProjectPaymentDTO.getPeriod();

					principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? 0
							: guaranteePayment.getPrincipalAndInterest().doubleValue();
					tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? 0
							: guaranteePayment.getTuandaiAmount().doubleValue();
					orgAmount = guaranteePayment.getOrgAmount() == null ? 0
							: guaranteePayment.getOrgAmount().doubleValue();
					guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? 0
							: guaranteePayment.getGuaranteeAmount().doubleValue();
					arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? 0
							: guaranteePayment.getArbitrationAmount().doubleValue();

					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : tdReturnAdvanceShareProfitDTOs) {
						if (tdReturnAdvanceShareProfitDTO.getPeriod() == tdPeriod) {
							principalAndInterest2 = tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest().doubleValue();
							tuandaiAmount2 = tdReturnAdvanceShareProfitDTO.getTuandaiAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getTuandaiAmount().doubleValue();
							orgAmount2 = tdReturnAdvanceShareProfitDTO.getOrgAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getOrgAmount().doubleValue();
							guaranteeAmount2 = tdReturnAdvanceShareProfitDTO.getGuaranteeAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getGuaranteeAmount().doubleValue();
							arbitrationAmount2 = tdReturnAdvanceShareProfitDTO.getArbitrationAmount() == null ? 0
									: tdReturnAdvanceShareProfitDTO.getArbitrationAmount().doubleValue();
						}
					}

					// 往期剩余多少没有还
					double principalAndInterest3 = BigDecimal.valueOf(principalAndInterest - principalAndInterest2)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double tuandaiAmount3 = BigDecimal.valueOf(tuandaiAmount - tuandaiAmount2)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double orgAmount3 = BigDecimal.valueOf(orgAmount - orgAmount2).setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					double guaranteeAmount3 = BigDecimal.valueOf(guaranteeAmount - guaranteeAmount2)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double arbitrationAmount3 = BigDecimal.valueOf(arbitrationAmount - arbitrationAmount2)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					double totalAmount = BigDecimal.valueOf(
							principalAndInterest3 + tuandaiAmount3 + orgAmount3 + guaranteeAmount3 + arbitrationAmount3)
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

					if (totalAmount > 0) {

						TdAdvanceShareProfitDTO dto = new TdAdvanceShareProfitDTO();
						TdrepayAdvanceLog tdrepayAdvanceLog = new TdrepayAdvanceLog();
						tdrepayAdvanceLog.setArbitrationAmount(BigDecimal.valueOf(arbitrationAmount3));
						tdrepayAdvanceLog.setTuandaiAmount(BigDecimal.valueOf(tuandaiAmount3));
						tdrepayAdvanceLog.setOrgAmount(BigDecimal.valueOf(orgAmount3));
						tdrepayAdvanceLog.setGuaranteeAmount(BigDecimal.valueOf(guaranteeAmount3));
						tdrepayAdvanceLog.setPrincipalAndInterest(BigDecimal.valueOf(principalAndInterest3));
						tdrepayAdvanceLog.setTotalAmount(BigDecimal.valueOf(totalAmount));
						tdrepayAdvanceLog.setStatus(1);
						tdrepayAdvanceLog.setCreateTime(new Date());
						String userId = loginUserInfoHelper.getUserId();
						tdrepayAdvanceLog.setCreateUser(userId);

						dto.setPeriod(tdPeriod); // 往期期次
						dto.setProjectId(projectId);
						dto.setPrincipalAndInterest(BigDecimal.valueOf(principalAndInterest3));
						dto.setStatus(1);
						dto.setTuandaiAmount(BigDecimal.valueOf(tuandaiAmount3));
						dto.setOrgType(handleTdUserName(businessType));
						dto.setOrgAmount(BigDecimal.valueOf(orgAmount3));
						dto.setGuaranteeAmount(BigDecimal.valueOf(guaranteeAmount3));
						dto.setArbitrationAmount(BigDecimal.valueOf(arbitrationAmount3));
						dto.setOverDueAmount(BigDecimal.valueOf(0));
						dto.setTotalAmount(BigDecimal.valueOf(totalAmount));

						IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(userId, dto,
								Constant.INTERFACE_CODE_ADVANCE_SHARE_PROFIT,
								Constant.INTERFACE_NAME_ADVANCE_SHARE_PROFIT, Constant.SYSTEM_CODE_EIP, projectId);

						Result result = null;

						try {
							// 调用偿还垫付接口
							result = eipRemote.advanceShareProfit(dto);
						} catch (Exception e) {
							issueSendOutsideLog.setReturnJson(e.getMessage());
							LOG.error(e.getMessage(), e);
						}

						if (result != null) {
							issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
						}

						if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
							// 还垫付记录表标记为处理成功
							tdrepayAdvanceLog.setAdvanceStatus(1);
						} else {
							// 还垫付记录表标记为处理失败
							tdrepayAdvanceLog.setAdvanceStatus(2);
						}

						tdrepayAdvanceLogService.insert(tdrepayAdvanceLog);
						issueSendOutsideLogService.insert(issueSendOutsideLog);

						if (result == null || !Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
							return false;
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到当期的标的还款信息
	 * 
	 * @param period
	 * @param projectPaymentResult
	 * @return
	 */
	private TdProjectPaymentDTO handleTdProjectPaymentDTO(Integer period,
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs) {

		TdProjectPaymentDTO projectPaymentDTO = null;

		if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
			// 得到当期的标的还款信息

			for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
				int tdPeriod = tdProjectPaymentDTO.getPeriod();
				if (period != null && tdPeriod == period.intValue()) {
					projectPaymentDTO = tdProjectPaymentDTO;
				}
			}

		}
		return projectPaymentDTO;
	}

	/**
	 * 得到往期的标的还款信息
	 * 
	 * @param period
	 * @param projectPaymentResult
	 * @return
	 */
	private List<TdProjectPaymentDTO> handlePastPeriodTdProjectPaymentDTO(Integer period,
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs) {

		List<TdProjectPaymentDTO> pastPeriodDTOs = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
			// 得到当期的标的还款信息
			for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
				if (period != null && tdProjectPaymentDTO.getPeriod() < period.intValue()) {
					pastPeriodDTOs.add(tdProjectPaymentDTO);
				}
			}
		}

		return pastPeriodDTOs;
	}

	/**
	 * 判断是否提前结清
	 * 
	 * @param repayChargeLogs
	 * @param periodsList
	 * @return
	 */
	private boolean isAdvanceSettle(List<TdrepayRechargeLog> repayChargeLogs, List<TdRefundMonthInfoDTO> periodsList) {
		if (CollectionUtils.isNotEmpty(periodsList)) {

			Date factRepayDate = repayChargeLogs.get(repayChargeLogs.size() - 1).getFactRepayDate();

			if (periodsList.size() > 1) {
				Collections.sort(periodsList, new Comparator<TdRefundMonthInfoDTO>() {

					@Override
					public int compare(TdRefundMonthInfoDTO o1, TdRefundMonthInfoDTO o2) {
						return o1.getPeriods() - o2.getPeriods();
					}
				});
			}
			String cycDate = periodsList.get(periodsList.size() - 1).getCycDate();

			if (factRepayDate != null && StringUtil.notEmpty(cycDate)) {
				cycDate += " 00:00:00";
				return factRepayDate.before(DateUtil.getDate(cycDate));
			}
		}
		return false;
	}

	/**
	 * 远程调用标的还款信息查询接口
	 * 
	 * @param repayChargeLogs
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Result remoteGetProjectPayment(String projectId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("orgType", 1); // 机构类型 传输任意值
		paramMap.put("projectId", projectId);

		IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(loginUserInfoHelper.getUserId(), paramMap,
				Constant.INTERFACE_CODE_GET_PROJECT_PAYMENT, Constant.INTERFACE_NAME_GET_PROJECT_PAYMENT,
				Constant.SYSTEM_CODE_EIP, projectId);

		Result result = null;
		try {
			result = eipRemote.getProjectPayment(paramMap);
		} catch (Exception e) {
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}
		if (result != null) {
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
		}
		issueSendOutsideLogService.insert(issueSendOutsideLog);

		if (result == null || !Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
			throw new ServiceRuntimeException("远程调用标的还款信息查询接口异常，地址：http://eip-out/eip/td/assetside/getProjectPayment");
		}

		return result;
	}

	/**
	 * list按照期次 Period 分组，并按照Period排序
	 * 
	 * @param rechargeLogs
	 */
	private Map<Integer, List<TdrepayRechargeLog>> repayChargeLogGroupByPeriod(List<TdrepayRechargeLog> rechargeLogs) {

		Map<Integer, List<TdrepayRechargeLog>> map = new TreeMap<>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		// rechargeLogs 是同一个projectId的所有期数的集合
		for (TdrepayRechargeLog tdrepayRechargeLog : rechargeLogs) {
			Integer period = tdrepayRechargeLog.getPeriod();
			List<TdrepayRechargeLog> list = map.get(period);
			if (list == null) {
				list = new ArrayList<>();
				list.add(tdrepayRechargeLog);
				map.put(period, list);
			} else {
				list.add(tdrepayRechargeLog);
				map.put(period, list);
			}
		}
		return map;
	}

	/**
	 * list 按照createTime进行排序
	 * 
	 * @param rechargeLogs
	 */
	private void sortChargeLogListByConfirmTime(List<TdrepayRechargeLog> rechargeLogs) {
		if (rechargeLogs != null && rechargeLogs.size() > 1) {
			Collections.sort(rechargeLogs, new Comparator<TdrepayRechargeLog>() {

				@Override
				public int compare(TdrepayRechargeLog o1, TdrepayRechargeLog o2) {
					if (o1.getConfirmTime().before(o2.getConfirmTime())) {
						return -1;
					} else if (o1.getConfirmTime().after(o2.getConfirmTime())) {
						return 1;
					} else {
						return 0;
					}
				}
			});
		}
	}

	/**
	 * 远程调用偿还垫付接口
	 * 
	 * @param tdrepayRechargeLog
	 * @param tdrepayRechargeDetails
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Result remoteAdvanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog,
			List<TdrepayRechargeDetail> tdrepayRechargeDetails) {

		/*
		 * 1、处理调用偿还垫付接口必须的参数，和记录日志
		 */
		// 参数DTO
		TdAdvanceShareProfitDTO paramDTO = new TdAdvanceShareProfitDTO();

		// 还垫付日志记录
		TdrepayAdvanceLog tdrepayAdvanceLog = new TdrepayAdvanceLog();

		paramDTO.setOrgType(handleTdUserName(tdrepayRechargeLog.getBusinessType()));

		// 期次
		Integer period = tdrepayRechargeLog.getPeriod();
		tdrepayAdvanceLog.setPeriod(period);
		paramDTO.setPeriod(period);

		// 标的ID
		String projectId = tdrepayRechargeLog.getProjectId();
		tdrepayAdvanceLog.setProjectId(projectId);
		paramDTO.setProjectId(projectId);

		// 当期结清状态
		Integer isComplete = tdrepayRechargeLog.getIsComplete();
		tdrepayAdvanceLog.setStatus(isComplete);
		paramDTO.setStatus(isComplete);

		double totalAmount = 0;

		for (TdrepayRechargeDetail tdrepayRechargeDetail : tdrepayRechargeDetails) {
			// 费用类型
			Integer feeType = tdrepayRechargeDetail.getFeeType();

			if (feeType == null) {
				continue;
			}
			// 费用值
			BigDecimal feeValue = tdrepayRechargeDetail.getFeeValue();

			double doubleFeeValue = feeValue == null ? 0 : feeValue.doubleValue();

			totalAmount += doubleFeeValue;
			// 本金 + 利息
			BigDecimal principalAndInterest = paramDTO.getPrincipalAndInterest();
			double principalAndInterestDouble = principalAndInterest == null ? 0 : principalAndInterest.doubleValue();

			switch (feeType) {
			case 10: // 本金
				paramDTO.setPrincipalAndInterest(feeValue);
				tdrepayAdvanceLog.setPrincipalAndInterest(feeValue);
				break;
			case 20: // 利息
				BigDecimal principalInterest = BigDecimal
						.valueOf(principalAndInterestDouble + (feeValue == null ? 0 : doubleFeeValue));
				paramDTO.setPrincipalAndInterest(principalInterest);
				tdrepayAdvanceLog.setPrincipalAndInterest(principalInterest);
				break;
			case 30: // 平台服务费
				paramDTO.setTuandaiAmount(feeValue);
				tdrepayAdvanceLog.setTuandaiAmount(feeValue);
				break;
			case 40: // 资产端服务费
				paramDTO.setOrgAmount(feeValue);
				tdrepayAdvanceLog.setOrgAmount(feeValue);
				break;
			case 50: // 担保公司服务费
				paramDTO.setGuaranteeAmount(feeValue);
				tdrepayAdvanceLog.setGuaranteeAmount(feeValue);
				break;
			case 60: // 仲裁服务费
				paramDTO.setArbitrationAmount(feeValue);
				tdrepayAdvanceLog.setArbitrationAmount(feeValue);
				break;
			case 70: // 逾期费用（罚息）
				paramDTO.setOverDueAmount(feeValue);
				tdrepayAdvanceLog.setOverDueAmount(feeValue);
				break;

			default:
				break;
			}
		}

		paramDTO.setTotalAmount(BigDecimal.valueOf(totalAmount));

		/*
		 * 2、开始调用偿还垫付接口
		 */
		Result result = null;
		// 记录第三方接口日志
		String userId = loginUserInfoHelper.getUserId();
		IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(userId, paramDTO,
				Constant.INTERFACE_CODE_ADVANCE_SHARE_PROFIT, Constant.INTERFACE_NAME_ADVANCE_SHARE_PROFIT,
				Constant.SYSTEM_CODE_EIP, projectId);

		tdrepayAdvanceLog.setCreateTime(new Date());
		tdrepayAdvanceLog.setCreateUser(userId);

		try {
			// 调用偿还垫付接口
			result = eipRemote.advanceShareProfit(paramDTO);
		} catch (Exception e) {
			// 还垫付记录表标记为处理失败
			tdrepayAdvanceLog.setAdvanceStatus(2);
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
			// 还垫付记录表标记为处理成功
			tdrepayAdvanceLog.setAdvanceStatus(1);
		} else {
			// 还垫付记录表标记为处理失败
			tdrepayAdvanceLog.setAdvanceStatus(2);
		}

		if (result != null) {
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
		}
		tdrepayAdvanceLogService.insert(tdrepayAdvanceLog);
		issueSendOutsideLogService.insert(issueSendOutsideLog);

		return result;
	}

	/**
	 * 读取待处理和处理失败的且资金分发成功的数据
	 * 
	 * @return
	 */
	private List<TdrepayRechargeLog> queryRechargeSuccessData() {
		List<Integer> lstStatus = new ArrayList<>();
		lstStatus.add(0); // 未处理
		lstStatus.add(3); // 处理失败
		List<TdrepayRechargeLog> rechargeLogs = tdrepayRechargeLogService
				.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("process_status", 2)// 分发成功
						.in("status", lstStatus));

		if (CollectionUtils.isEmpty(rechargeLogs)) {
			return Collections.emptyList();
		}

		for (TdrepayRechargeLog tdrepayRechargeLog : rechargeLogs) {
			tdrepayRechargeLog.setStatus(1); // 标记为处理中，防止重复处理
		}
		tdrepayRechargeLogService.updateBatchById(rechargeLogs);
		return rechargeLogs;
	}

	/**
	 * 从平台获取标的还款信息、还垫付信息
	 * 
	 * @param projectId
	 * @param period
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Result> getAdvanceShareProfitAndProjectPayment(String projectId) {

		Map<String, Result> resultMap = new HashMap<>();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projectId);

		String userId = loginUserInfoHelper.getUserId();

		IssueSendOutsideLog queryProjectPaymentLog = issueSendOutsideLog(userId, paramMap,
				Constant.INTERFACE_CODE_QUERY_PROJECT_PAYMENT, Constant.INTERFACE_NAME_QUERY_PROJECT_PAYMENT,
				Constant.SYSTEM_CODE_EIP, projectId);

		IssueSendOutsideLog advanceShareProfitLog = issueSendOutsideLog(userId, paramMap,
				Constant.INTERFACE_CODE_RETURN_ADVANCE_SHARE_PROFIT,
				Constant.INTERFACE_NAME_RETURN_ADVANCE_SHARE_PROFIT, Constant.SYSTEM_CODE_EIP, projectId);

		Result queryProjectPaymentResult = null;
		Result advanceShareProfitResult = null;
		try {
			queryProjectPaymentResult = eipRemote.queryProjectPayment(paramMap); // 标的还款信息
			advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
		} catch (Exception e) {
			queryProjectPaymentLog.setReturnJson(e.getMessage());
			advanceShareProfitLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		if (queryProjectPaymentResult != null) {
			queryProjectPaymentLog.setReturnJson(JSONObject.toJSONString(queryProjectPaymentResult));
		}
		if (advanceShareProfitResult != null) {
			advanceShareProfitLog.setReturnJson(JSONObject.toJSONString(advanceShareProfitResult));
		}

		issueSendOutsideLogService.insert(advanceShareProfitLog);
		issueSendOutsideLogService.insert(queryProjectPaymentLog);

		if (queryProjectPaymentResult == null
				|| !Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())
				|| advanceShareProfitResult == null
				|| !Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())) {
			throw new ServiceRuntimeException("从平台获取标的还款信息或还垫付信息异常");
		}

		resultMap.put("advanceShareProfitResult", advanceShareProfitResult);
		resultMap.put("queryProjectPaymentResult", queryProjectPaymentResult);

		return resultMap;
	}

	/**
	 * 判断当前期是否存在未还记录
	 * 
	 * @param queryProjectPaymentResult
	 * @param advanceShareProfitResult
	 * @param period
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private Boolean isCurrPeriodAdvance(Result queryProjectPaymentResult, Result advanceShareProfitResult,
			Integer period) {
		if (queryProjectPaymentResult != null && advanceShareProfitResult != null
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
				&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(queryProjectPaymentResult.getReturnCode())) {
			// 标的还款信息
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;
			if (queryProjectPaymentResult.getData() != null) {

				JSONObject parseObject = (JSONObject) JSONObject.toJSON(queryProjectPaymentResult.getData());
				if (parseObject.get("projectPayments") != null) {
					tdProjectPaymentDTOs = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
				}
			}

			// 还垫付信息
			List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = null;
			if (advanceShareProfitResult.getData() != null) {

				JSONObject parseObject = (JSONObject) JSONObject.toJSON(advanceShareProfitResult.getData());
				if (parseObject.get("returnAdvanceShareProfits") != null) {
					returnAdvanceShareProfits = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("returnAdvanceShareProfits")),
							TdReturnAdvanceShareProfitDTO.class);
				}
			}

			// 根据 标的还款信息 和 还垫付信息 判断是否有未还垫付记录
			return isCurrPeriodAdvance(tdProjectPaymentDTOs, returnAdvanceShareProfits, period);
		}
		return null;
	}

	/**
	 * 判断当前期是否存在未还记录
	 * 
	 * @param projectPaymentResult
	 * @param returnAdvanceShareProfitResult
	 * @param period
	 * @return
	 */
	private boolean isCurrPeriodAdvance(List<TdProjectPaymentDTO> tdProjectPaymentDTOs,
			List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits, Integer period) {

		// 判断 标的当前期是否存在垫付未还记录 需另外创建还垫付记录表 根据 标的还款信息查询接口 和 还垫付信息查询接口 取得数据对比

		// 担保公司垫付信息
		double principalAndInterest = 0; // 本金利息
		double tuandaiAmount = 0; // 实还平台服务费
		double orgAmount = 0; // 实还资产端服务费
		double guaranteeAmount = 0; // 实还担保公司服务费
		double arbitrationAmount = 0; // 实还仲裁服务费

		// 还垫付信息
		double principalAndInterest2 = 0; // 本金利息
		double tuandaiAmount2 = 0; // 实还平台服务费
		double orgAmount2 = 0; // 实还资产端服务费
		double guaranteeAmount2 = 0; // 实还担保公司服务费
		double arbitrationAmount2 = 0; // 实还仲裁服务费

		// 得到当期的标的还款信息
		TdProjectPaymentDTO projectPaymentDTO = handleTdProjectPaymentDTO(period, tdProjectPaymentDTOs);

		if (projectPaymentDTO != null && projectPaymentDTO.getGuaranteePayment() != null) {

			TdGuaranteePaymentDTO guaranteePayment = projectPaymentDTO.getGuaranteePayment();

			principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? 0
					: guaranteePayment.getPrincipalAndInterest().doubleValue();
			tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? 0
					: guaranteePayment.getTuandaiAmount().doubleValue();
			orgAmount = guaranteePayment.getOrgAmount() == null ? 0 : guaranteePayment.getOrgAmount().doubleValue();
			guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? 0
					: guaranteePayment.getGuaranteeAmount().doubleValue();
			arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? 0
					: guaranteePayment.getArbitrationAmount().doubleValue();
		}

		// 得到当期的标的还垫付信息
		TdReturnAdvanceShareProfitDTO returnAdvanceShareProfitDTO = handleTdReturnAdvanceShareProfitDTO(
				returnAdvanceShareProfits, period);

		if (returnAdvanceShareProfitDTO != null) {
			principalAndInterest2 = returnAdvanceShareProfitDTO.getPrincipalAndInterest() == null ? 0
					: returnAdvanceShareProfitDTO.getPrincipalAndInterest().doubleValue();
			tuandaiAmount2 = returnAdvanceShareProfitDTO.getTuandaiAmount() == null ? 0
					: returnAdvanceShareProfitDTO.getTuandaiAmount().doubleValue();
			orgAmount2 = returnAdvanceShareProfitDTO.getOrgAmount() == null ? 0
					: returnAdvanceShareProfitDTO.getOrgAmount().doubleValue();
			guaranteeAmount2 = returnAdvanceShareProfitDTO.getGuaranteeAmount() == null ? 0
					: returnAdvanceShareProfitDTO.getGuaranteeAmount().doubleValue();
			arbitrationAmount2 = returnAdvanceShareProfitDTO.getArbitrationAmount() == null ? 0
					: returnAdvanceShareProfitDTO.getArbitrationAmount().doubleValue();
		}

		return (principalAndInterest - principalAndInterest2) > 0 || (tuandaiAmount - tuandaiAmount2) > 0
				|| (orgAmount - orgAmount2) > 0 || (guaranteeAmount - guaranteeAmount2) > 0
				|| (arbitrationAmount - arbitrationAmount2) > 0;
	}

	/**
	 * 得到当期的标的还垫付信息
	 * 
	 * @param returnAdvanceShareProfitResult
	 * @param period
	 * @return
	 */
	private TdReturnAdvanceShareProfitDTO handleTdReturnAdvanceShareProfitDTO(
			List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits, Integer period) {
		TdReturnAdvanceShareProfitDTO returnAdvanceShareProfitDTO = null;

		if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
			// 得到当期的标的还垫付信息
			for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
				if (period != null && tdReturnAdvanceShareProfitDTO.getPeriod() == period.intValue()) {
					returnAdvanceShareProfitDTO = tdReturnAdvanceShareProfitDTO;
				}
			}
		}

		return returnAdvanceShareProfitDTO;
	}

	/**
	 * 得到往期的标的还垫付信息
	 * 
	 * @param returnAdvanceShareProfitResult
	 * @param period
	 * @return
	 */
	private List<TdReturnAdvanceShareProfitDTO> handlePastPeriodTdReturnAdvanceShareProfitDTO(
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult, Integer period) {
		List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfitDTOs = new ArrayList<>();

		if (returnAdvanceShareProfitResult != null
				&& CollectionUtils.isNotEmpty(returnAdvanceShareProfitResult.getReturnAdvanceShareProfits())) {

			List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
					.getReturnAdvanceShareProfits();

			// 得到往期的标的还垫付信息
			for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
				if (period != null && tdReturnAdvanceShareProfitDTO.getPeriod() < period.intValue()) {
					returnAdvanceShareProfitDTOs.add(tdReturnAdvanceShareProfitDTO);
				}
			}
		}
		return returnAdvanceShareProfitDTOs;
	}

	/**
	 * 记录第三方日志
	 * 
	 * @param userId
	 * @param sendObject
	 * @param interfaceCode
	 * @param interfaceName
	 * @param systemCode
	 * @param sendKey
	 * @return
	 */
	private IssueSendOutsideLog issueSendOutsideLog(String userId, Object sendObject, String interfaceCode,
			String interfaceName, String systemCode, String sendKey) {
		IssueSendOutsideLog issueSendOutsideLog = new IssueSendOutsideLog();
		issueSendOutsideLog.setCreateTime(new Date());
		issueSendOutsideLog.setCreateUserId(userId);
		issueSendOutsideLog.setSendJson(JSONObject.toJSONString(sendObject));
		issueSendOutsideLog.setInterfacecode(interfaceCode);
		issueSendOutsideLog.setInterfacename(interfaceName);
		issueSendOutsideLog.setSystem(systemCode);
		issueSendOutsideLog.setSendKey(sendKey);

		return issueSendOutsideLog;
	}

	@Override
	public List<DistributeFundRecordVO> queryDistributeFundRecord(String projectId) {
		try {
			List<DistributeFundRecordVO> distributeFundRecordVOs = new ArrayList<>();
			List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
					.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).orderBy("period"));
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					DistributeFundRecordVO vo = BeanUtils.deepCopy(tdrepayRechargeLog, DistributeFundRecordVO.class);
					vo.setProcessStatusStr(ProcessStatusTypeEnum.getName(tdrepayRechargeLog.getProcessStatus()));
					vo.setCreateTimeStr(DateUtil.formatDate(vo.getCreateTime()));
					vo.setFactRepayDateStr(DateUtil.formatDate(vo.getFactRepayDate()));
					vo.setDetails(tdrepayRechargeDetailService.selectList(
							new EntityWrapper<TdrepayRechargeDetail>().eq("log_id", tdrepayRechargeLog.getLogId())));
					distributeFundRecordVOs.add(vo);
				}
			}
			return distributeFundRecordVOs;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException("系统异常，查询资金分发记录失败");
		}
	}

	@Override
	public List<AgencyRechargeLog> queryRechargeRecord(RechargeRecordReq req) {
		return agencyRechargeLogMapper.queryRechargeRecord(req);
	}
	
	@Override
	public int countRechargeRecord(RechargeRecordReq req) {
		return agencyRechargeLogMapper.countRechargeRecord(req);
	}
}
