package com.hongte.alms.platrepay.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.google.common.collect.Lists;
import com.hongte.alms.base.dto.compliance.DistributeFundDTO;
import com.hongte.alms.base.dto.compliance.DistributeFundDetailDTO;
import com.hongte.alms.base.dto.compliance.TdAdvanceShareProfitDTO;
import com.hongte.alms.base.dto.compliance.TdDepaymentEarlierDTO;
import com.hongte.alms.base.dto.compliance.TdPlatformPlanRepaymentDTO;
import com.hongte.alms.base.dto.compliance.TdProjectPaymentInfoResult;
import com.hongte.alms.base.dto.compliance.TdRefundMonthInfoDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.TdrepayAdvanceLog;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.entity.TdrepayRechargeRecord;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.AgencyRechargeLogMapper;
import com.hongte.alms.base.mapper.TdrepayRechargeLogMapper;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.TdrepayAdvanceLogService;
import com.hongte.alms.base.service.TdrepayRechargeDetailService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
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
	@Qualifier("TuandaiProjectInfoService")
	private TuandaiProjectInfoService tuandaiProjectInfoService;

	@Autowired
	private TdrepayRechargeLogMapper tdrepayRechargeLogMapper;

	@Autowired
	private AgencyRechargeLogMapper agencyRechargeLogMapper;

	@Autowired
	@Qualifier("SysParameterService")
	private SysParameterService sysParameterService;

	@Autowired
	private EipRemote eipRemote;

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTdrepayRechargeInfo(TdrepayRechargeInfoVO vo) {
		try {
			TdrepayRechargeLog rechargeLog = handleTdrepayRechargeLog(vo);

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", vo.getProjectId());

			LOG.info("标的还款信息查询接口/eip/td/assetside/getProjectPayment参数信息，{}", paramMap);
			Result result = eipRemote.getProjectPayment(paramMap);
			LOG.info("标的还款信息查询接口/eip/td/assetside/getProjectPayment返回信息，{}", result);

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
							break;
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

		// 若还款来源为银行代扣、或者网关充值、或者充值金额等于0，不需要手动资金分发，直接赋值成功
		if (vo.getRepaySource().intValue() == 3 || vo.getRepaySource().intValue() == 4
				|| BigDecimal.ZERO.compareTo(vo.getRechargeAmount()) > -1) {
			rechargeLog.setProcessStatus(2);
		} else {
			rechargeLog.setProcessStatus(0); // 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
		}
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
						handleSendDistributeFundResult(rechargeInfoVOs, userId, result);
						vos.removeAll(rechargeInfoVOs);
					}

				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			/*
			 * 将分发状态更新为处理中 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
			 */
			updateTdrepayRechargeLogProcessStatus(vos, 3, userId);

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
		if (result == null || !Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
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
			tdrepayRechargeLog.setProcessTime(new Date());
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
		switch (businessType) {
		case 31:
			businessType = 9;
			break;
		case 32:
			businessType = 11;
			break;
		case 33:
			businessType = 20;
			break;

		default:
			break;
		}
		dto.setOrgType(BusinessTypeEnum.getOrgTypeByValue(businessType));
		String batchId = UUID.randomUUID().toString();
		dto.setBatchId(batchId);
		String rechargeAccountType = BusinessTypeEnum.getRechargeAccountName(businessType);

		SysParameter sysParameter = this.queryRechargeAccountSysParams(rechargeAccountType);
		if (sysParameter == null) {
			return Result.buildFail();
		}

		String oIdPartner = sysParameter.getParamValue2();
		dto.setOidPartner(oIdPartner);
		String clientIp = CommonUtil.getClientIp();
		dto.setUserIP(clientIp);

		String logUserId = sysParameter.getParamValue();
		dto.setUserId(logUserId);

		/*
		 * 将rechargeInfoVOs中的数据装入一个批次中
		 */
		List<DistributeFundDetailDTO> detailList = new LinkedList<>();

		BigDecimal totalAmount = BigDecimal.ZERO;

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

			BigDecimal amount = vo.getRechargeAmount() == null ? BigDecimal.ZERO : vo.getRechargeAmount();

			totalAmount = totalAmount.add(amount);

			DistributeFundDetailDTO detailDTO = new DistributeFundDetailDTO();
			detailDTO.setAmount(amount.doubleValue());
			detailDTO.setRequestNo(requestNo);
			detailDTO.setUserId(tdUserId);
			detailList.add(detailDTO);

			TdrepayRechargeLog tdrepayRechargeLog = new TdrepayRechargeLog();
			tdrepayRechargeLog.setLogId(vo.getLogId());
			tdrepayRechargeLog.setBatchId(batchId);
			tdrepayRechargeLog.setRequestNo(requestNo);
			tdrepayRechargeLog.setTdUserId(tdUserId);
			tdrepayRechargeLog.setUserId(logUserId);
			tdrepayRechargeLog.setUserIp(clientIp);
			tdrepayRechargeLog.setOidPartner(oIdPartner);
			tdrepayRechargeLog.setRechargeAmount(amount);
			tdrepayRechargeLog.setUpdateTime(new Date());
			tdrepayRechargeLog.setUpdateUser(userId);
			tdrepayRechargeLogs.add(tdrepayRechargeLog);
		}

		if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				tdrepayRechargeLog.setTotalAmount(totalAmount);
			}
		}

		dto.setTotalAmount(totalAmount.doubleValue());
		dto.setDetailList(detailList);

		outsideLog.setSendJson(JSONObject.toJSONString(dto));
		outsideLog.setInterfacecode(Constant.INTERFACE_CODE_SEND_DISTRIBUTE_FUND);
		outsideLog.setInterfacename(Constant.INTERFACE_NAME_SEND_DISTRIBUTE_FUND);
		outsideLog.setCreateTime(new Date());
		outsideLog.setCreateUserId(userId);

		tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
		Result result = null;
		try {
			// 调用 eip 平台资金分发接口
			LOG.info("资金分发接口/eip/td/assetside/userDistributeFund参数信息，{}", dto);
			result = eipRemote.userDistributeFund(dto);
			LOG.info("资金分发接口/eip/td/assetside/userDistributeFund返回信息，{}", result);
		} catch (Exception e) {
			LOG.error("批次号:" + batchId + "，调用eip平台资金分发接口失败！DTO 数据：" + dto.toString(), e);
			outsideLog.setReturnJson(e.getMessage());
		}

		outsideLog.setReturnJson(JSONObject.toJSONString(result));
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
				infoVOs = new ArrayList<>();
				infoVOs.add(vo);
				dtoMap.put(businessType, infoVOs);
			} else {
				infoVOs.add(vo);
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

	@Override
	public SysParameter queryRechargeAccountSysParams(String rechargeAccountType) {
		if (StringUtil.isEmpty(rechargeAccountType)) {
			return null;
		}
		return sysParameterService.queryRechargeAccountSysParams(rechargeAccountType);
	}

	@Override
	public void repayComplianceWithRequirements() {
		/*
		 * 读取待处理和处理失败的，且资金分发成功的数据
		 */
		List<TdrepayRechargeLog> tdrepayRechargeLogs = null;

		try {
			tdrepayRechargeLogs = queryRechargeSuccessData();
			if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
				return;
			}
			// 按照projectId分组数据
			// 结构：Map<projectId, Map<period, List<period的所有数据>>>
			Map<String, Map<Integer, List<TdrepayRechargeLog>>> mapGroupByProjectId = mapGroupByProjectId(
					tdrepayRechargeLogs);

			for (Entry<String, Map<Integer, List<TdrepayRechargeLog>>> entry : mapGroupByProjectId.entrySet()) {
				// periodDataMap是同projectId， 按 period 分组，且按 period 从小到大排序后的数据
				Map<Integer, List<TdrepayRechargeLog>> periodDataMap = entry.getValue();

				for (Entry<Integer, List<TdrepayRechargeLog>> periodDataEntry : periodDataMap.entrySet()) {

					// 同 period 数据集合
					List<TdrepayRechargeLog> rechargeLogs = periodDataEntry.getValue();

					// 按ConfirmTime从小到大排序，优先处理最早传入的数据
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
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					tdrepayRechargeLog.setStatus(0);
					tdrepayRechargeLog.setUpdateTime(new Date());
					tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
				}
				tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
			}
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

			return (settleType != null && (settleType.intValue() == 10 || settleType.intValue() == 11
					|| settleType.intValue() == 20 || settleType.intValue() == 30));
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
			}
		}

		Map<String, Map<Integer, List<TdrepayRechargeLog>>> projectIdMap = new HashMap<>();

		for (Entry<String, List<TdrepayRechargeLog>> entry : mapGroupByProjectId.entrySet()) {

			// projectId 分组后的数据， 每一个list都是同一个projectId的集合
			List<TdrepayRechargeLog> rechargeLogs = entry.getValue();

			if (CollectionUtils.isNotEmpty(rechargeLogs)) {
				// list按照期次 Period 分组，并按照Period从小到大排序
				Map<Integer, List<TdrepayRechargeLog>> map = repayChargeLogGroupByPeriod(rechargeLogs);
				projectIdMap.put(entry.getKey(), map);
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
				Boolean currPeriodAdvance = isCurrPeriodAdvance(map.get("queryProjectPaymentResult"),
						map.get("advanceShareProfitResult"), period);

				if (currPeriodAdvance != null && currPeriodAdvance) {

					List<TdrepayRechargeLog> sucLst = new ArrayList<>();

					for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {

						// 根据logId 获取对应的费用明细列表
						List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
								.selectList(new EntityWrapper<TdrepayRechargeDetail>()
										.eq("log_id", tdrepayRechargeLog.getLogId()).orderBy("fee_type"));

						if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {

							// 调用 偿还垫付接口 ， 按期数财务确认时间从小到大顺序调用，若本期次某一数据执行失败，则本期次未执行的数据不再继续执行
							Result result = remoteAdvanceShareProfit(tdrepayRechargeLog, tdrepayRechargeDetails);

							if (result != null) {
								tdrepayRechargeLog.setRemark(result.getCodeDesc());
							} else {
								tdrepayRechargeLog.setRemark("eip接口调用异常");
							}

							if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

								sucLst.add(tdrepayRechargeLog);

							} else {
								break;
							}
						}
					}

					if (!sucLst.isEmpty()) {
						for (TdrepayRechargeLog tdrepayRechargeLog : sucLst) {
							tdrepayRechargeLog.setStatus(2);
							tdrepayRechargeLog.setUpdateTime(new Date());
							tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
						}
						tdrepayRechargeLogService.updateBatchById(sucLst);
						// 移除偿还垫付成功的数据
						repayChargeLogs.removeAll(sucLst);
					}

					if (!repayChargeLogs.isEmpty()) {

						// 标记处理失败,待定时任务重试
						for (TdrepayRechargeLog rechargeLog : repayChargeLogs) {
							rechargeLog.setStatus(3);
							rechargeLog.setUpdateTime(new Date());
							rechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
						}
						tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
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

		// repayChargeLogs 均为结清期的数据, 同projectId,同period,且已经根据ConfirmTime从小到大排序
		if (CollectionUtils.isNotEmpty(repayChargeLogs)) {

			/*
			 * 一、 处理还垫付
			 */
			boolean isSuccess = handleSettleAdvanceData(repayChargeLogs);

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
					Map<String, BigDecimal> map = totalRepaymentEarlierFinances(repayChargeLogs);

					// 计算提前结清应还分润
					BigDecimal assetsCharge = map.get("assetsCharge"); // 资产端服务费
					BigDecimal guaranteeCharge = map.get("guaranteeCharge"); // 担保公司服务费
					BigDecimal agencyCharge = map.get("agencyCharge"); // 中介公司服务费

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
				} else {
					for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
						// 非提前结清，标记为成功
						tdrepayRechargeLog.setStatus(2);
						tdrepayRechargeLog.setUpdateTime(new Date());
						tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
					}
					tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
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
	private Map<String, BigDecimal> totalRepaymentEarlierFinances(List<TdrepayRechargeLog> repayChargeLogs) {

		Map<String, BigDecimal> resultMap = new HashMap<>();

		BigDecimal assetsCharge = BigDecimal.ZERO; // 资产端服务费
		BigDecimal guaranteeCharge = BigDecimal.ZERO; // 担保公司服务费
		BigDecimal agencyCharge = BigDecimal.ZERO; // 中介公司服务费

		List<String> logIds = new LinkedList<>();
		for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {
			logIds.add(tdrepayRechargeLog.getLogId());
		}

		List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
				.selectList(new EntityWrapper<TdrepayRechargeDetail>().in("log_id", logIds));

		// 计算提前结清应还分润
		for (TdrepayRechargeDetail detail : tdrepayRechargeDetails) {
			Integer feeType = detail.getFeeType();
			BigDecimal feeValue = detail.getFeeValue() == null ? BigDecimal.ZERO : detail.getFeeValue();

			if (feeType == null || feeValue == null || feeValue.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			switch (feeType) {
			case 40:
				assetsCharge = assetsCharge.add(feeValue);
				break;
			case 50:
				guaranteeCharge = guaranteeCharge.add(feeValue);
				break;
			case 80:
				agencyCharge = agencyCharge.add(feeValue);
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
	 * 计算提前结清应还分润
	 * 
	 * @param repayChargeLogs
	 * @return
	 */
	private Map<String, BigDecimal> totalRepaymentEarlierFinances(TdrepayRechargeLog tdrepayRechargeLog) {

		Map<String, BigDecimal> resultMap = new HashMap<>();

		BigDecimal assetsCharge = BigDecimal.ZERO; // 资产端服务费
		BigDecimal guaranteeCharge = BigDecimal.ZERO; // 担保公司服务费
		BigDecimal agencyCharge = BigDecimal.ZERO; // 中介公司服务费

		List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
				.selectList(new EntityWrapper<TdrepayRechargeDetail>().eq("log_id", tdrepayRechargeLog.getLogId()));

		// 计算提前结清应还分润
		for (TdrepayRechargeDetail detail : tdrepayRechargeDetails) {
			Integer feeType = detail.getFeeType();
			BigDecimal feeValue = detail.getFeeValue() == null ? BigDecimal.ZERO : detail.getFeeValue();

			if (feeType == null || feeValue == null || feeValue.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			switch (feeType) {
			case 40:
				assetsCharge = assetsCharge.add(feeValue);
				break;
			case 50:
				guaranteeCharge = guaranteeCharge.add(feeValue);
				break;
			case 80:
				agencyCharge = agencyCharge.add(feeValue);
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
	private Result remoteRepaymentEarlier(List<TdrepayRechargeLog> repayChargeLogs, BigDecimal assetsCharge,
			BigDecimal guaranteeCharge, BigDecimal agencyCharge) {
		TdrepayRechargeLog tdrepayRechargeLog = repayChargeLogs.get(repayChargeLogs.size() - 1);

		// 提前结清接口参数DTO
		TdDepaymentEarlierDTO tdDepaymentEarlierDTO = new TdDepaymentEarlierDTO();
		tdDepaymentEarlierDTO.setAgencyCharge(agencyCharge);
		tdDepaymentEarlierDTO.setAssetsCharge(assetsCharge);
		tdDepaymentEarlierDTO.setGuaranteeCharge(guaranteeCharge);
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
		LOG.info("提前结清接口/eip/td/repayment/repaymentEarlier参数信息，{}", tdDepaymentEarlierDTO);
		try {
			// 调用提前结清接口
			repaymentEarlierResult = eipRemote.repaymentEarlier(tdDepaymentEarlierDTO);
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(repaymentEarlierResult));
			LOG.info("提前结清接口/eip/td/repayment/repaymentEarlier返回信息，{}", repaymentEarlierResult);
		} catch (Exception e) {
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		if (repaymentEarlierResult != null) {
			tdrepayRechargeLog.setRemark(repaymentEarlierResult.getCodeDesc());
		} else {
			tdrepayRechargeLog.setRemark("eip接口调用异常");
		}

		issueSendOutsideLogService.insert(issueSendOutsideLog);

		return repaymentEarlierResult;
	}

	@SuppressWarnings("rawtypes")
	private Result remoteRepaymentEarlier(TdrepayRechargeLog tdrepayRechargeLog, BigDecimal assetsCharge,
			BigDecimal guaranteeCharge, BigDecimal agencyCharge) {

		// 提前结清接口参数DTO
		TdDepaymentEarlierDTO tdDepaymentEarlierDTO = new TdDepaymentEarlierDTO();
		tdDepaymentEarlierDTO.setAgencyCharge(agencyCharge);
		tdDepaymentEarlierDTO.setAssetsCharge(assetsCharge);
		tdDepaymentEarlierDTO.setGuaranteeCharge(guaranteeCharge);
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
		LOG.info("提前结清接口/eip/td/repayment/repaymentEarlier参数信息，{}", tdDepaymentEarlierDTO);
		try {
			// 调用提前结清接口
			repaymentEarlierResult = eipRemote.repaymentEarlier(tdDepaymentEarlierDTO);
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(repaymentEarlierResult));
			LOG.info("提前结清接口/eip/td/repayment/repaymentEarlier返回信息，{}", repaymentEarlierResult);
		} catch (Exception e) {
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		if (repaymentEarlierResult != null) {
			tdrepayRechargeLog.setRemark(repaymentEarlierResult.getCodeDesc());
		} else {
			tdrepayRechargeLog.setRemark("eip接口调用异常");
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

		// repayChargeLogs 均为结清期的数据, 同projectId,同period,且已经根据ConfirmTime从大到小排序

		// 从平台获取标的还款信息、还垫付信息 （标的维度）
		String projectId = repayChargeLogs.get(0).getProjectId();
		Integer period = repayChargeLogs.get(0).getPeriod();

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

		Result queryProjectPaymentResult = map.get("queryProjectPaymentResult");
		Result advanceShareProfitResult = map.get("advanceShareProfitResult");

		// 2、判断当前期是否有垫付
		Boolean currPeriodAdvanceFlag = isCurrPeriodAdvance(queryProjectPaymentResult, advanceShareProfitResult,
				period);
		if (currPeriodAdvanceFlag != null && currPeriodAdvanceFlag) {

			List<TdrepayRechargeLog> sucLst = new ArrayList<>();

			for (TdrepayRechargeLog tdrepayRechargeLog : repayChargeLogs) {

				// 根据logId 获取对应的费用明细列表
				List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
						.selectList(new EntityWrapper<TdrepayRechargeDetail>()
								.eq("log_id", tdrepayRechargeLog.getLogId()).orderBy("fee_type"));

				if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {

					// 调用 偿还垫付接口 ， 按期数财务确认时间从小到大顺序调用，若本期次某一数据执行失败，则本期次未执行的数据不再继续执行
					Result result = remoteAdvanceShareProfit(tdrepayRechargeLog, tdrepayRechargeDetails);

					if (result != null) {
						tdrepayRechargeLog.setRemark(result.getCodeDesc());
					} else {
						tdrepayRechargeLog.setRemark("eip接口调用异常");
					}

					if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

						sucLst.add(tdrepayRechargeLog);

					} else {
						flag = false;
						break;
					}
				}
			}

			if (!sucLst.isEmpty()) {
				for (TdrepayRechargeLog tdrepayRechargeLog : sucLst) {
					tdrepayRechargeLog.setStatus(2);
					tdrepayRechargeLog.setUpdateTime(new Date());
					tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
				}
				tdrepayRechargeLogService.updateBatchById(sucLst);
				// 移除偿还垫付成功的数据
				repayChargeLogs.removeAll(sucLst);
			}

			if (!repayChargeLogs.isEmpty()) {

				// 标记处理失败,待定时任务重试
				for (TdrepayRechargeLog rechargeLog : repayChargeLogs) {
					rechargeLog.setStatus(3);
					rechargeLog.setUpdateTime(new Date());
					rechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
				}
				tdrepayRechargeLogService.updateBatchById(repayChargeLogs);
			}

		}

		return flag;
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
					break;
				}
			}

		}
		return projectPaymentDTO;
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

			Date confirmTime = repayChargeLogs.get(repayChargeLogs.size() - 1).getConfirmTime();

			if (periodsList.size() > 1) {
				Collections.sort(periodsList, new Comparator<TdRefundMonthInfoDTO>() {

					@Override
					public int compare(TdRefundMonthInfoDTO o1, TdRefundMonthInfoDTO o2) {
						return o1.getPeriods() - o2.getPeriods();
					}
				});
			}
			String cycDate = periodsList.get(periodsList.size() - 1).getCycDate();

			if (confirmTime != null && StringUtil.notEmpty(cycDate)) {
				cycDate += " 00:00:00";
				return confirmTime.before(DateUtil.getDate(cycDate));
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
		// paramMap.put("orgType", 1); // 机构类型 传输任意值
		paramMap.put("projectId", projectId);

		IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLog(loginUserInfoHelper.getUserId(), paramMap,
				Constant.INTERFACE_CODE_GET_PROJECT_PAYMENT, Constant.INTERFACE_NAME_GET_PROJECT_PAYMENT,
				Constant.SYSTEM_CODE_EIP, projectId);

		LOG.info("标的还款信息查询接口/eip/td/assetside/getProjectPayment参数信息，{}", paramMap);
		Result result = null;
		try {
			result = eipRemote.getProjectPayment(paramMap);
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
			LOG.info("标的还款信息查询接口/eip/td/assetside/getProjectPayment返回信息，{}", result);
		} catch (Exception e) {
			issueSendOutsideLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
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
			}
		}
		return map;
	}

	/**
	 * list 按照confirmTime进行排序
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

		paramDTO.setOrgType(BusinessTypeEnum.getOrgTypeByValue((tdrepayRechargeLog.getBusinessType())));

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

		BigDecimal totalAmount = BigDecimal.ZERO;

		for (TdrepayRechargeDetail tdrepayRechargeDetail : tdrepayRechargeDetails) {
			// 费用类型
			Integer feeType = tdrepayRechargeDetail.getFeeType();
			// 费用值
			BigDecimal feeValue = tdrepayRechargeDetail.getFeeValue();

			if (feeType == null || feeValue == null || feeValue.compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}

			totalAmount = totalAmount.add(feeValue);
			// 本金 + 利息
			BigDecimal principalAndInterest = BigDecimal.ZERO;
			if (feeType.intValue() == 10 || feeType.intValue() == 20) {
				principalAndInterest = paramDTO.getPrincipalAndInterest() == null ? principalAndInterest
						: paramDTO.getPrincipalAndInterest();
			}

			switch (feeType) {
			case 10: // 本金
				BigDecimal principalInterest1 = principalAndInterest.add(feeValue);
				paramDTO.setPrincipalAndInterest(principalInterest1);
				tdrepayAdvanceLog.setPrincipalAndInterest(principalInterest1);
				break;
			case 20: // 利息
				BigDecimal principalInterest2 = principalAndInterest.add(feeValue);
				paramDTO.setPrincipalAndInterest(principalInterest2);
				tdrepayAdvanceLog.setPrincipalAndInterest(principalInterest2);
				break;
			case 30: // 平台服务费
				BigDecimal tuandaiAmount = paramDTO.getTuandaiAmount() == null ? feeValue
						: paramDTO.getTuandaiAmount().add(feeValue);
				paramDTO.setTuandaiAmount(tuandaiAmount);
				tdrepayAdvanceLog.setTuandaiAmount(tuandaiAmount);
				break;
			case 40: // 资产端服务费
				BigDecimal orgAmount = paramDTO.getOrgAmount() == null ? feeValue
						: paramDTO.getOrgAmount().add(feeValue);
				paramDTO.setOrgAmount(orgAmount);
				tdrepayAdvanceLog.setOrgAmount(orgAmount);
				break;
			case 50: // 担保公司服务费
				BigDecimal guaranteeAmount = paramDTO.getGuaranteeAmount() == null ? feeValue
						: paramDTO.getGuaranteeAmount().add(feeValue);
				paramDTO.setGuaranteeAmount(guaranteeAmount);
				tdrepayAdvanceLog.setGuaranteeAmount(guaranteeAmount);
				break;
			case 60: // 仲裁服务费
				BigDecimal arbitrationAmount = paramDTO.getArbitrationAmount() == null ? feeValue
						: paramDTO.getArbitrationAmount().add(feeValue);
				paramDTO.setArbitrationAmount(arbitrationAmount);
				tdrepayAdvanceLog.setArbitrationAmount(arbitrationAmount);
				break;
			case 70: // 逾期费用（罚息）
				BigDecimal overDueAmount = paramDTO.getOverDueAmount() == null ? feeValue
						: paramDTO.getOverDueAmount().add(feeValue);
				paramDTO.setOverDueAmount(overDueAmount);
				tdrepayAdvanceLog.setOverDueAmount(overDueAmount);
				break;

			default:
				break;
			}
		}

		paramDTO.setTotalAmount(totalAmount);

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
		LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit参数信息，{}", paramDTO);
		try {
			// 调用偿还垫付接口
			result = eipRemote.advanceShareProfit(paramDTO);
			issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));
			LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit返回信息，{}", result);
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
				.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("process_status", 2).eq("is_valid", 1)// 分发成功
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

	@Override
	public void repaymentAdvance() {
		List<Integer> lstStatus = new ArrayList<>();
		lstStatus.add(0); // 未处理的数据
		lstStatus.add(3); // 还垫付失败的数据
		lstStatus.add(4); // 部分还垫付的数据

		List<TdrepayRechargeLog> tdrepayRechargeLogs = queryToDoData(lstStatus, false);

		if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
			return;
		}

		for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
			try {
				advanceShareProfit(tdrepayRechargeLog);
			} catch (Exception e) {
				LOG.error("标的ID：{}，平台期数：{}，系统异常：{}", tdrepayRechargeLog.getProjectId(), tdrepayRechargeLog.getPeriod(),
						e);
			}
		}
	}

	/**
	 * 获取需处理的数据
	 * 
	 * @param lstStatus
	 *            处理状态lst
	 * @param isSettle
	 *            是否结清
	 * @return
	 */
	private List<TdrepayRechargeLog> queryToDoData(List<Integer> lstStatus, boolean isSettle) {
		if (CollectionUtils.isEmpty(lstStatus)) {
			return Collections.emptyList();
		}

		List<TdrepayRechargeLog> rechargeLogs = null;

		if (isSettle) {
			rechargeLogs = tdrepayRechargeLogService.selectList(new EntityWrapper<TdrepayRechargeLog>()
					.eq("is_valid", 1).in("status", lstStatus).eq("process_status", 2).ne("settle_type", 0).where("order by project_id, period"));
		} else {
			rechargeLogs = tdrepayRechargeLogService.selectList(new EntityWrapper<TdrepayRechargeLog>()
					.eq("is_valid", 1).in("status", lstStatus).eq("process_status", 2).where("order by project_id, period"));
		}

		if (CollectionUtils.isEmpty(rechargeLogs)) {
			return Collections.emptyList();
		}

		for (TdrepayRechargeLog tdrepayRechargeLog : rechargeLogs) {
			tdrepayRechargeLog.setStatus(1); // 标记为处理中，防止重复处理
		}
		tdrepayRechargeLogService.updateBatchById(rechargeLogs);
		return rechargeLogs;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void advanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog) {
		try {
			// 3、根据 project_id 查询担保公司垫付记录、还垫付记录
			String projectId = tdrepayRechargeLog.getProjectId();
			Map<String, Result> resultMap = getAdvanceShareProfitAndProjectPayment(projectId);
			Result queryProjectPaymentResult = resultMap.get("queryProjectPaymentResult");
			Result advanceShareProfitResult = resultMap.get("advanceShareProfitResult");

			Map<String, Object> map = handlePaymentAdvanceResult(queryProjectPaymentResult, advanceShareProfitResult);
			// 标的还款信息
			List<TdProjectPaymentDTO> tdProjectPaymentDTOs = (List<TdProjectPaymentDTO>) map
					.get("tdProjectPaymentDTOs");
			// 还垫付信息
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = (TdReturnAdvanceShareProfitResult) map
					.get("returnAdvanceShareProfitResult");

			// 担保公司垫付信息
			BigDecimal principalAndInterest = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount = BigDecimal.ZERO; // 平台服务费
			BigDecimal orgAmount = BigDecimal.ZERO; // 资产端服务费
			BigDecimal guaranteeAmount = BigDecimal.ZERO; // 担保公司服务费
			BigDecimal arbitrationAmount = BigDecimal.ZERO; // 仲裁服务费
			BigDecimal penaltyAmount = BigDecimal.ZERO; // 滞纳金

			// 还垫付信息
			BigDecimal principalAndInterest2 = BigDecimal.ZERO; // 本金利息
			BigDecimal tuandaiAmount2 = BigDecimal.ZERO; // 平台服务费
			BigDecimal orgAmount2 = BigDecimal.ZERO; // 资产端服务费
			BigDecimal guaranteeAmount2 = BigDecimal.ZERO; // 担保公司服务费
			BigDecimal arbitrationAmount2 = BigDecimal.ZERO; // 仲裁服务费

			// 当期剩余未还担保公司垫付金额
			BigDecimal principalAndInterest3 = null; // 本金利息
			BigDecimal tuandaiAmount3 = null; // 平台服务费
			BigDecimal orgAmount3 = null; // 资产端服务费
			BigDecimal guaranteeAmount3 = null; // 担保公司服务费
			BigDecimal arbitrationAmount3 = null; // 仲裁服务费
			BigDecimal totalAmount = BigDecimal.ZERO; // 费用合计

			Integer period = tdrepayRechargeLog.getPeriod();
			if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
				for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
					if (tdProjectPaymentDTO.getPeriod() == period.intValue()) {
						TdGuaranteePaymentDTO guaranteePayment = tdProjectPaymentDTO.getGuaranteePayment();
						if (guaranteePayment != null) {
							principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null
									? principalAndInterest
									: guaranteePayment.getPrincipalAndInterest();
							penaltyAmount = guaranteePayment.getPenaltyAmount() == null ? penaltyAmount
									: guaranteePayment.getPenaltyAmount();
							// 若担保公司垫付了滞纳金，则需要将滞纳金计算到本息上
							if (BigDecimal.ZERO.compareTo(penaltyAmount) < 0) {
								principalAndInterest = principalAndInterest.add(penaltyAmount);
							}
							tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? tuandaiAmount
									: guaranteePayment.getTuandaiAmount();
							orgAmount = guaranteePayment.getOrgAmount() == null ? orgAmount
									: guaranteePayment.getOrgAmount();
							guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? guaranteeAmount
									: guaranteePayment.getGuaranteeAmount();
							arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? arbitrationAmount
									: guaranteePayment.getArbitrationAmount();
						}
						break;
					}
				}
			}

			if (returnAdvanceShareProfitResult != null
					&& CollectionUtils.isNotEmpty(returnAdvanceShareProfitResult.getReturnAdvanceShareProfits())) {
				List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = returnAdvanceShareProfitResult
						.getReturnAdvanceShareProfits();

				if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
					for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
						if (tdReturnAdvanceShareProfitDTO.getPeriod() == period.intValue()) {
							principalAndInterest2 = tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest() == null
									? principalAndInterest2
									: tdReturnAdvanceShareProfitDTO.getPrincipalAndInterest();
							tuandaiAmount2 = tdReturnAdvanceShareProfitDTO.getTuandaiAmount() == null ? tuandaiAmount2
									: tdReturnAdvanceShareProfitDTO.getTuandaiAmount();
							orgAmount2 = tdReturnAdvanceShareProfitDTO.getOrgAmount() == null ? orgAmount2
									: tdReturnAdvanceShareProfitDTO.getOrgAmount();
							guaranteeAmount2 = tdReturnAdvanceShareProfitDTO.getGuaranteeAmount() == null
									? guaranteeAmount2
									: tdReturnAdvanceShareProfitDTO.getGuaranteeAmount();
							arbitrationAmount2 = tdReturnAdvanceShareProfitDTO.getArbitrationAmount() == null
									? arbitrationAmount2
									: tdReturnAdvanceShareProfitDTO.getArbitrationAmount();
							break;
						}
					}
				}
			}

			// 5、计算剩余应偿还担保公司垫付记录
			principalAndInterest3 = principalAndInterest.subtract(principalAndInterest2);
			totalAmount = totalAmount.add(principalAndInterest3);
			tuandaiAmount3 = tuandaiAmount.subtract(tuandaiAmount2);
			totalAmount = totalAmount.add(tuandaiAmount3);
			orgAmount3 = orgAmount.subtract(orgAmount2);
			totalAmount = totalAmount.add(orgAmount3);
			guaranteeAmount3 = guaranteeAmount.subtract(guaranteeAmount2);
			totalAmount = totalAmount.add(guaranteeAmount3);
			arbitrationAmount3 = arbitrationAmount.subtract(arbitrationAmount2);
			totalAmount = totalAmount.add(arbitrationAmount3);

			// 若没有垫付未还记录
			if (totalAmount.compareTo(BigDecimal.ZERO) < 1) {
				if (tdrepayRechargeLog.getSettleType().intValue() == 0) {
					// 非结清，则设置status = 2；
					tdrepayRechargeLog.setStatus(2);
					tdrepayRechargeLog.setRemark("执行成功");
				} else {
					// 结清，判断是否是提前结清
					List<TdPlatformPlanRepaymentDTO> tdPlatformPlanRepaymentDTOs = remotePlatformRepaymentPlan(
							tdrepayRechargeLog.getProjectId());
					if (CollectionUtils.isNotEmpty(tdPlatformPlanRepaymentDTOs)) {
						for (TdPlatformPlanRepaymentDTO dto : tdPlatformPlanRepaymentDTOs) {
							if (dto.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()) {
								if (new Date().before(DateUtil.getDate(dto.getCycDate()))) {
									// 若是提前结清，则处理状态设置为处理失败，待执行提前结清任务
									tdrepayRechargeLog.setStatus(3);
									tdrepayRechargeLog.setRemark("没有垫付未还记录，待执行提前结清任务");
								} else {
									// 若不是提前结清，则处理状态设置为成功，流程结束
									tdrepayRechargeLog.setStatus(2);
									tdrepayRechargeLog.setRemark("执行成功");
								}
								break;
							}
							tdrepayRechargeLog.setStatus(3);
							tdrepayRechargeLog.setRemark("在平台还款计划没有匹配到对应的期数");
						}
					}else {
						tdrepayRechargeLog.setStatus(3);
						tdrepayRechargeLog.setRemark("没有查询到平台还款计划");
					}
				}
				tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
				return;
			}

			/*
			 * 6、根据 tdUserId 查询存管账户余额, 比较应还垫付总额与客户存管账户余额记录 a.若是主借标，则只查询主借款人账户余额； b.若是共借标：
			 * 若共借人存管账户余额若大于或等于应还垫付总额，则传结清状态给平台，调用偿还垫付接口 若共借人存管账户余额若小于应还垫付总额，则还需查询主借人存管账户余额：
			 * 若主、共借人存管账户余额之和大于或等于应还垫付总额，则传结清状态给平台，调用偿还垫付接口
			 * 若主、共借人存管账户余额之和小于应还垫付总额，则传结非清状态给平台，调用偿还垫付接口
			 */

			advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
					guaranteeAmount3, arbitrationAmount3, totalAmount, period);
		} catch (Exception e) {
			tdrepayRechargeLog.setStatus(3);
			tdrepayRechargeLog.setRemark(e.getMessage());
			tdrepayRechargeLog.setUpdateTime(new Date());
			tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> handlePaymentAdvanceResult(Result queryProjectPaymentResult,
			Result advanceShareProfitResult) {
		Map<String, Object> resultMap = new HashMap<>();
		List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;
		if (queryProjectPaymentResult.getData() != null) {
			JSONObject parseObject = (JSONObject) JSONObject.toJSON(queryProjectPaymentResult.getData());
			if (parseObject.get("projectPayments") != null) {
				tdProjectPaymentDTOs = JSONObject.parseArray(
						JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
			}
		}

		TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = null;
		if (advanceShareProfitResult.getData() != null) {
			returnAdvanceShareProfitResult = JSONObject.parseObject(
					JSONObject.toJSONString(advanceShareProfitResult.getData()),
					TdReturnAdvanceShareProfitResult.class);
		}

		resultMap.put("tdProjectPaymentDTOs", tdProjectPaymentDTOs);
		resultMap.put("returnAdvanceShareProfitResult", returnAdvanceShareProfitResult);

		return resultMap;
	}

	private void advanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog, String projectId,
			BigDecimal principalAndInterest3, BigDecimal tuandaiAmount3, BigDecimal orgAmount3,
			BigDecimal guaranteeAmount3, BigDecimal arbitrationAmount3, BigDecimal totalAmount, Integer period) {

		// 查询客户存管账户余额
		BigDecimal aviMoney = queryUserAviMoney(tdrepayRechargeLog.getTdUserId());

		Set<Integer> businessTypes = new HashSet<>();
		businessTypes.add(28); // 商贸贷共借
		businessTypes.add(29); // 业主贷共借
		businessTypes.add(31); // 车贷共借
		businessTypes.add(32); // 房贷共借
		businessTypes.add(33); // 一点车贷共借

		// 若是共借标，则需要查找主借标的 tdUserId
		if (businessTypes.contains(tdrepayRechargeLog.getBusinessType())) {
			if (aviMoney.compareTo(totalAmount) < 0) {
				TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService
						.selectOne(new EntityWrapper<TuandaiProjectInfo>()
								.eq("business_id", tdrepayRechargeLog.getOrigBusinessId())
								.where("project_id = master_issue_id"));

				if (tuandaiProjectInfo != null) {
					aviMoney = aviMoney.add(queryUserAviMoney(tuandaiProjectInfo.getTdUserId()));
				}

				if (aviMoney.compareTo(totalAmount) >= 0) {
					advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
							guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
				} else {
					totalAmount = aviMoney;
					if (aviMoney.compareTo(principalAndInterest3) > 0) {
						aviMoney = aviMoney.subtract(principalAndInterest3);
						if (aviMoney.compareTo(tuandaiAmount3) > 0) {
							aviMoney = aviMoney.subtract(tuandaiAmount3);
							if (aviMoney.compareTo(guaranteeAmount3) > 0) {
								orgAmount3 = aviMoney.subtract(guaranteeAmount3);
							} else {
								guaranteeAmount3 = aviMoney;
								orgAmount3 = BigDecimal.ZERO;
							}
						} else {
							tuandaiAmount3 = aviMoney;
							guaranteeAmount3 = BigDecimal.ZERO;
							orgAmount3 = BigDecimal.ZERO;
						}
					} else {
						principalAndInterest3 = aviMoney;
						tuandaiAmount3 = BigDecimal.ZERO;
						guaranteeAmount3 = BigDecimal.ZERO;
						orgAmount3 = BigDecimal.ZERO;
					}
					advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
							guaranteeAmount3, arbitrationAmount3, totalAmount, period, 0);
				}
			} else {
				advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
						guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
			}
		} else {
			if (aviMoney.compareTo(totalAmount) >= 0) {
				advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
						guaranteeAmount3, arbitrationAmount3, totalAmount, period, 1);
			} else {
				totalAmount = aviMoney;
				if (aviMoney.compareTo(principalAndInterest3) > 0) {
					aviMoney = aviMoney.subtract(principalAndInterest3);
					if (aviMoney.compareTo(tuandaiAmount3) > 0) {
						aviMoney = aviMoney.subtract(tuandaiAmount3);
						if (aviMoney.compareTo(guaranteeAmount3) > 0) {
							orgAmount3 = aviMoney.subtract(guaranteeAmount3);
						} else {
							guaranteeAmount3 = aviMoney;
							orgAmount3 = BigDecimal.ZERO;
						}
					} else {
						tuandaiAmount3 = aviMoney;
						guaranteeAmount3 = BigDecimal.ZERO;
						orgAmount3 = BigDecimal.ZERO;
					}
				} else {
					principalAndInterest3 = aviMoney;
					tuandaiAmount3 = BigDecimal.ZERO;
					guaranteeAmount3 = BigDecimal.ZERO;
					orgAmount3 = BigDecimal.ZERO;
				}
				advanceShareProfit(tdrepayRechargeLog, projectId, principalAndInterest3, tuandaiAmount3, orgAmount3,
						guaranteeAmount3, arbitrationAmount3, totalAmount, period, 0);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal queryUserAviMoney(String tdUserId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", tdUserId);
		LOG.info("查询代充值账户余额/eip/xiaodai/queryUserAviMoney参数信息，{}", paramMap);
		Result result = eipRemote.queryUserAviMoney(paramMap);
		LOG.info("查询代充值账户余额/eip/xiaodai/queryUserAviMoney返回信息，{}", result);

		if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
				&& result.getData() != null) {
			Map map = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), Map.class);
			if (map != null) {
				return map.get("aviMoney") == null ? BigDecimal.ZERO
						: BigDecimal.valueOf(Double.valueOf((String) map.get("aviMoney")));
			}
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 偿还垫付
	 * 
	 * @param tdrepayRechargeLog
	 * @param projectId
	 * @param principalAndInterest3
	 * @param tuandaiAmount3
	 * @param orgAmount3
	 * @param guaranteeAmount3
	 * @param arbitrationAmount3
	 * @param totalAmount
	 * @param period
	 */
	@SuppressWarnings("rawtypes")
	private void advanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog, String projectId,
			BigDecimal principalAndInterest3, BigDecimal tuandaiAmount3, BigDecimal orgAmount3,
			BigDecimal guaranteeAmount3, BigDecimal arbitrationAmount3, BigDecimal totalAmount, Integer period,
			Integer status) {
		if (BigDecimal.ZERO.compareTo(totalAmount) >= 0) {
			tdrepayRechargeLog.setStatus(3);
			tdrepayRechargeLog.setRemark("借款人存管余额或偿还垫付金额为0，偿还垫付失败！");
			tdrepayRechargeLog.setUpdateTime(new Date());
			tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
			return;
		}

		// 查询资金分发明细（取罚息金额）
		List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
				.selectList(new EntityWrapper<TdrepayRechargeDetail>().eq("log_id", tdrepayRechargeLog.getLogId())
						.eq("fee_type", 70));
		BigDecimal overDueAmount = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {
			for (TdrepayRechargeDetail tdrepayRechargeDetail : tdrepayRechargeDetails) {
				if (overDueAmount.compareTo(tdrepayRechargeDetail.getFeeValue()) < 0) {
					overDueAmount = overDueAmount.add(tdrepayRechargeDetail.getFeeValue());
					totalAmount = totalAmount.add(overDueAmount);
				}
			}
		}

		// 参数DTO
		TdAdvanceShareProfitDTO paramDTO = new TdAdvanceShareProfitDTO();
		paramDTO.setProjectId(projectId);
		paramDTO.setPeriod(period);
		paramDTO.setTotalAmount(totalAmount);
		paramDTO.setPrincipalAndInterest(principalAndInterest3);
		paramDTO.setTuandaiAmount(tuandaiAmount3);
		paramDTO.setOrgAmount(orgAmount3);
		paramDTO.setGuaranteeAmount(guaranteeAmount3);
		paramDTO.setArbitrationAmount(arbitrationAmount3);
		paramDTO.setOverDueAmount(overDueAmount);
		paramDTO.setOrgType(BusinessTypeEnum.getOrgTypeByValue(tdrepayRechargeLog.getBusinessType()));
		paramDTO.setStatus(status);

		// 第三方接口调用日志
		IssueSendOutsideLog issueSendOutsideLog = new IssueSendOutsideLog();
		issueSendOutsideLog.setCreateTime(new Date());
		issueSendOutsideLog.setCreateUserId(loginUserInfoHelper.getUserId());
		issueSendOutsideLog.setSendJson(JSONObject.toJSONString(paramDTO));
		issueSendOutsideLog.setInterfacecode(Constant.INTERFACE_CODE_ADVANCE_SHARE_PROFIT);
		issueSendOutsideLog.setInterfacename(Constant.INTERFACE_NAME_ADVANCE_SHARE_PROFIT);
		issueSendOutsideLog.setSystem(Constant.SYSTEM_CODE_EIP);
		issueSendOutsideLog.setSendKey(projectId);

		// 偿还垫付记录
		TdrepayAdvanceLog tdrepayAdvanceLog = new TdrepayAdvanceLog();
		tdrepayAdvanceLog.setProjectId(projectId);
		tdrepayAdvanceLog.setPeriod(period);
		tdrepayAdvanceLog.setStatus(status);
		tdrepayAdvanceLog.setTotalAmount(totalAmount);
		tdrepayAdvanceLog.setPrincipalAndInterest(principalAndInterest3);
		tdrepayAdvanceLog.setTuandaiAmount(tuandaiAmount3);
		tdrepayAdvanceLog.setOrgAmount(orgAmount3);
		tdrepayAdvanceLog.setGuaranteeAmount(guaranteeAmount3);
		tdrepayAdvanceLog.setArbitrationAmount(arbitrationAmount3);
		tdrepayAdvanceLog.setCreateTime(new Date());
		tdrepayAdvanceLog.setCreateUser(loginUserInfoHelper.getUserId());

		// 调用偿还垫付接口
		LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit参数信息，{}", paramDTO);
		Result result = eipRemote.advanceShareProfit(paramDTO);
		LOG.info("偿还垫付接口/eip/td/repayment/advanceShareProfit返回信息，{}", result);

		issueSendOutsideLog.setReturnJson(JSONObject.toJSONString(result));

		if (result != null) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", projectId);
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
			Result advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);

			int logStatus = 4;

			if (advanceShareProfitResult != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(advanceShareProfitResult.getReturnCode())
					&& advanceShareProfitResult.getData() != null) {
				JSONObject parseObject = (JSONObject) JSONObject.toJSON(advanceShareProfitResult.getData());
				if (parseObject.get("returnAdvanceShareProfits") != null) {
					List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits = JSONObject.parseArray(
							JSONObject.toJSONString(parseObject.get("returnAdvanceShareProfits")),
							TdReturnAdvanceShareProfitDTO.class);
					if (CollectionUtils.isNotEmpty(returnAdvanceShareProfits)) {
						for (TdReturnAdvanceShareProfitDTO tdReturnAdvanceShareProfitDTO : returnAdvanceShareProfits) {
							if (tdReturnAdvanceShareProfitDTO.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()
									&& tdReturnAdvanceShareProfitDTO.getStatus() == 1) {
								logStatus = 2;
								break;
							}
						}
					} else {
						logStatus = 4;
					}
				}
			}
			if (Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {
				tdrepayAdvanceLog.setAdvanceStatus(1);
			} else {
				tdrepayAdvanceLog.setAdvanceStatus(2);
			}
			tdrepayRechargeLog.setStatus(logStatus);
			tdrepayRechargeLog.setRemark(result.getCodeDesc());
		} else {
			tdrepayRechargeLog.setStatus(3);
			tdrepayRechargeLog.setRemark("eip偿还垫付接口调用异常");
		}
		tdrepayRechargeLog.setUpdateTime(new Date());
		tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
		tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
		issueSendOutsideLogService.insert(issueSendOutsideLog);
		tdrepayAdvanceLogService.insert(tdrepayAdvanceLog);
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

		// String userId = loginUserInfoHelper.getUserId();

		// IssueSendOutsideLog queryProjectPaymentLog = issueSendOutsideLog(userId,
		// paramMap,
		// Constant.INTERFACE_CODE_QUERY_PROJECT_PAYMENT,
		// Constant.INTERFACE_NAME_QUERY_PROJECT_PAYMENT,
		// Constant.SYSTEM_CODE_EIP, projectId);
		//
		// IssueSendOutsideLog advanceShareProfitLog = issueSendOutsideLog(userId,
		// paramMap,
		// Constant.INTERFACE_CODE_RETURN_ADVANCE_SHARE_PROFIT,
		// Constant.INTERFACE_NAME_RETURN_ADVANCE_SHARE_PROFIT,
		// Constant.SYSTEM_CODE_EIP, projectId);

		Result queryProjectPaymentResult = null;
		Result advanceShareProfitResult = null;
		try {
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", paramMap);
			queryProjectPaymentResult = eipRemote.queryProjectPayment(paramMap); // 标的还款信息
			// queryProjectPaymentLog.setReturnJson(JSONObject.toJSONString(queryProjectPaymentResult));
			LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", queryProjectPaymentResult);

			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
			advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
			// advanceShareProfitLog.setReturnJson(JSONObject.toJSONString(advanceShareProfitResult));
			LOG.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);
		} catch (Exception e) {
			// queryProjectPaymentLog.setReturnJson(e.getMessage());
			// advanceShareProfitLog.setReturnJson(e.getMessage());
			LOG.error(e.getMessage(), e);
		}

		// issueSendOutsideLogService.insert(advanceShareProfitLog);
		// issueSendOutsideLogService.insert(queryProjectPaymentLog);

		resultMap.put("advanceShareProfitResult", advanceShareProfitResult);
		resultMap.put("queryProjectPaymentResult", queryProjectPaymentResult);

		return resultMap;
	}

	@Override
	public void repaymentEarlier() {
		List<Integer> lstStatus = new ArrayList<>();
		lstStatus.add(0); // 未处理的数据
		lstStatus.add(3); // 还垫付失败的数据
		lstStatus.add(4); // 部分还垫付的数据

		List<TdrepayRechargeLog> tdrepayRechargeLogs = queryToDoData(lstStatus, true);

		// 判断是否提前结清：当前时间 与平台当期应还日期比较
		List<TdrepayRechargeLog> isSettleData = getSettleData(tdrepayRechargeLogs);

		if (CollectionUtils.isEmpty(isSettleData)) {
			return;
		}

		for (TdrepayRechargeLog tdrepayRechargeLog : isSettleData) {
			repaymentEarlier(tdrepayRechargeLog);
		}
	}

	/**
	 * 获取没有垫付未还记录的，且是提前结清的数据
	 * 
	 * @param lstStatus
	 * @param tdrepayRechargeLogs
	 * @return
	 */
	private List<TdrepayRechargeLog> getSettleData(List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		List<TdrepayRechargeLog> isSettleData = null;
		try {
			if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
				return isSettleData;
			}

			isSettleData = handleMoveUpSettle(tdrepayRechargeLogs);

			// 移除提前结清的数据
			if (CollectionUtils.isNotEmpty(isSettleData)) {
				tdrepayRechargeLogs.removeAll(isSettleData);
			}

			// 非提前结清的数据更新处理状态更新为失败，若有垫付未还记录，待偿还垫付定时任务执行
			if (!tdrepayRechargeLogs.isEmpty()) {
				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					tdrepayRechargeLog.setStatus(3);
				}
				tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
			}

			if (CollectionUtils.isEmpty(isSettleData)) {
				return isSettleData;
			}

			// 查询是否有垫付记录未还
			List<TdrepayRechargeLog> rechargeLogs = handleDdvancePaymentRecordData(isSettleData);

			// 移除有垫付未还的数据
			isSettleData.removeAll(rechargeLogs);

		} catch (Exception e) {
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
				Set<TdrepayRechargeLog> set = new HashSet<>(tdrepayRechargeLogs);
				if (CollectionUtils.isNotEmpty(isSettleData)) {
					set.addAll(isSettleData);
				}
				for (TdrepayRechargeLog tdrepayRechargeLog : set) {
					tdrepayRechargeLog.setRemark(e.getMessage());
					tdrepayRechargeLog.setStatus(3);
				}
				tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
			}
			LOG.error(e.getMessage(), e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
		return isSettleData;
	}

	/**
	 * 计算提前结清应还分润、调用提前结清接口
	 * 
	 * @param tdrepayRechargeLog
	 */
	@SuppressWarnings("rawtypes")
	private void repaymentEarlier(TdrepayRechargeLog tdrepayRechargeLog) {
		try {
			// 计算当期提前结清应还分润
			Map<String, BigDecimal> map = totalRepaymentEarlierFinances(tdrepayRechargeLog);
			// 计算提前结清应还分润
			BigDecimal assetsCharge = map.get("assetsCharge"); // 资产端服务费
			BigDecimal guaranteeCharge = map.get("guaranteeCharge"); // 担保公司服务费
			BigDecimal agencyCharge = map.get("agencyCharge"); // 中介公司服务费

			Result remoteRepaymentEarlierResult = remoteRepaymentEarlier(tdrepayRechargeLog, assetsCharge,
					guaranteeCharge, agencyCharge);

			if (remoteRepaymentEarlierResult != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(remoteRepaymentEarlierResult.getReturnCode())) {
				// 标记为处理成功，流程结束
				tdrepayRechargeLog.setStatus(2);
				tdrepayRechargeLog.setRemark("提前结清成功");
				tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
			} else {
				// 标记为处理失败，待下次定时任务重试
				tdrepayRechargeLog.setStatus(3);
				if (remoteRepaymentEarlierResult != null) {
					tdrepayRechargeLog.setRemark(remoteRepaymentEarlierResult.getCodeDesc());
				} else {
					tdrepayRechargeLog.setRemark("提前结清失败");
				}
				tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
			}
		} catch (Exception e) {
			tdrepayRechargeLog.setStatus(3);
			tdrepayRechargeLog.setRemark(e.getMessage());
			tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 处理有垫付未还的数据
	 * 
	 * @param tdrepayRechargeLogs
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<TdrepayRechargeLog> handleDdvancePaymentRecordData(List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		List<TdrepayRechargeLog> advancePaymentRecordList = new ArrayList<>();
		for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
			try {
				Map<String, Result> resultMap = getAdvanceShareProfitAndProjectPayment(
						tdrepayRechargeLog.getProjectId());
				if (resultMap == null || resultMap.isEmpty()) {
					tdrepayRechargeLog.setStatus(3);
					tdrepayRechargeLog.setRemark("获取平台实还信息或还垫付信息失败");
					tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
					continue;
				}

				Result queryProjectPaymentResult = resultMap.get("queryProjectPaymentResult");
				Result advanceShareProfitResult = resultMap.get("advanceShareProfitResult");

				Map<String, Object> map = handlePaymentAdvanceResult(queryProjectPaymentResult,
						advanceShareProfitResult);
				// 标的还款信息
				List<TdProjectPaymentDTO> tdProjectPaymentDTOs = (List<TdProjectPaymentDTO>) map
						.get("tdProjectPaymentDTOs");
				// 还垫付信息
				TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = (TdReturnAdvanceShareProfitResult) map
						.get("returnAdvanceShareProfitResult");

				List<TdReturnAdvanceShareProfitDTO> tdReturnAdvanceShareProfitDTOs = null;
				if (returnAdvanceShareProfitResult != null) {
					tdReturnAdvanceShareProfitDTOs = returnAdvanceShareProfitResult.getReturnAdvanceShareProfits();
				}

				// 担保公司垫付所有期数总金额
				BigDecimal totalGuaranteePayment = BigDecimal.ZERO;
				if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {
					for (TdProjectPaymentDTO dto : tdProjectPaymentDTOs) {
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getPrincipalAndInterest() == null ? BigDecimal.ZERO
										: dto.getPrincipalAndInterest());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getPenaltyAmount() == null ? BigDecimal.ZERO : dto.getPenaltyAmount());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getTuandaiAmount() == null ? BigDecimal.ZERO : dto.getTuandaiAmount());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getOrgAmount() == null ? BigDecimal.ZERO : dto.getOrgAmount());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getGuaranteeAmount() == null ? BigDecimal.ZERO : dto.getGuaranteeAmount());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getArbitrationAmount() == null ? BigDecimal.ZERO : dto.getArbitrationAmount());
						totalGuaranteePayment = totalGuaranteePayment
								.add(dto.getAgencyAmount() == null ? BigDecimal.ZERO : dto.getAgencyAmount());
					}
				}

				// 已还垫付所有期数总金额
				BigDecimal totalReturnAdvance = BigDecimal.ZERO;
				if (CollectionUtils.isNotEmpty(tdReturnAdvanceShareProfitDTOs)) {
					for (TdReturnAdvanceShareProfitDTO dto : tdReturnAdvanceShareProfitDTOs) {
						totalReturnAdvance = totalReturnAdvance
								.add(dto.getTotalAmount() == null ? BigDecimal.ZERO : dto.getTotalAmount());
					}
				}

				// 剩余未还垫付金额
				BigDecimal totalSurplusAdvance = totalGuaranteePayment.subtract(totalReturnAdvance);

				if (BigDecimal.ZERO.compareTo(totalSurplusAdvance) < 0) {
					tdrepayRechargeLog.setStatus(3);
					tdrepayRechargeLog.setRemark("剩余：" + totalSurplusAdvance + " 垫付未还！将尝试偿还垫付再提前结清。");
					advancePaymentRecordList.add(tdrepayRechargeLog);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				tdrepayRechargeLog.setStatus(3);
				tdrepayRechargeLog.setRemark(e.getMessage());
				advancePaymentRecordList.add(tdrepayRechargeLog);
			}
		}

		if (!advancePaymentRecordList.isEmpty()) {
			tdrepayRechargeLogService.updateBatchById(advancePaymentRecordList);
		}
		return advancePaymentRecordList;
	}

	/**
	 * 获取提前结清的数据
	 * 
	 * @param tdrepayRechargeLogs
	 * @return
	 */
	private List<TdrepayRechargeLog> handleMoveUpSettle(List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		List<TdrepayRechargeLog> isSettleData = new ArrayList<>();
		List<TdPlatformPlanRepaymentDTO> tdPlatformPlanRepaymentDTOs = null;
		for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
			try {
				tdPlatformPlanRepaymentDTOs = remotePlatformRepaymentPlan(tdrepayRechargeLog.getProjectId());
				if (CollectionUtils.isNotEmpty(tdPlatformPlanRepaymentDTOs)) {
					for (TdPlatformPlanRepaymentDTO dto : tdPlatformPlanRepaymentDTOs) {
						if (dto.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()) {
							if (new Date().before(DateUtil.getDate(dto.getCycDate()))) {
								isSettleData.add(tdrepayRechargeLog);
							} else {
								break;
							}
						}
					}
				}else {
					tdrepayRechargeLog.setRemark("没有查询到平台还款计划");
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return isSettleData;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TdPlatformPlanRepaymentDTO> remotePlatformRepaymentPlan(String projectId) {
		/*
		 * 通过外联平台eip调用团贷查询标的还款计划信息
		 */
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projectId);

		Result ret = eipRemote.queryRepaymentSchedule(paramMap);
		LOG.info("查询平台标的还款计划，标id：{}；接口返回数据：{}", projectId, ret);

		if (ret != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(ret.getReturnCode()) && ret.getData() != null) {

			Map map = JSONObject.parseObject(JSONObject.toJSONString(ret.getData()), Map.class);

			if (map != null && map.get("repaymentScheduleList") != null) {
				return JSONObject.parseArray(JSONObject.toJSONString(map.get("repaymentScheduleList")),
						TdPlatformPlanRepaymentDTO.class);

			}
		}

		return Lists.newArrayList();

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

		BigDecimal zero = BigDecimal.ZERO;

		// 担保公司垫付信息
		BigDecimal principalAndInterest = zero; // 本金利息
		BigDecimal tuandaiAmount = zero; // 实还平台服务费
		BigDecimal orgAmount = zero; // 实还资产端服务费
		BigDecimal guaranteeAmount = zero; // 实还担保公司服务费
		BigDecimal arbitrationAmount = zero; // 实还仲裁服务费

		// 还垫付信息
		BigDecimal principalAndInterest2 = zero; // 本金利息
		BigDecimal tuandaiAmount2 = zero; // 实还平台服务费
		BigDecimal orgAmount2 = zero; // 实还资产端服务费
		BigDecimal guaranteeAmount2 = zero; // 实还担保公司服务费
		BigDecimal arbitrationAmount2 = zero; // 实还仲裁服务费

		// 得到当期的标的还款信息
		TdProjectPaymentDTO projectPaymentDTO = handleTdProjectPaymentDTO(period, tdProjectPaymentDTOs);
		if (projectPaymentDTO != null && projectPaymentDTO.getGuaranteePayment() != null) {

			TdGuaranteePaymentDTO guaranteePayment = projectPaymentDTO.getGuaranteePayment();

			principalAndInterest = guaranteePayment.getPrincipalAndInterest() == null ? zero
					: guaranteePayment.getPrincipalAndInterest();
			tuandaiAmount = guaranteePayment.getTuandaiAmount() == null ? zero : guaranteePayment.getTuandaiAmount();
			orgAmount = guaranteePayment.getOrgAmount() == null ? zero : guaranteePayment.getOrgAmount();
			guaranteeAmount = guaranteePayment.getGuaranteeAmount() == null ? zero
					: guaranteePayment.getGuaranteeAmount();
			arbitrationAmount = guaranteePayment.getArbitrationAmount() == null ? zero
					: guaranteePayment.getArbitrationAmount();
		}

		// 得到当期的标的还垫付信息
		TdReturnAdvanceShareProfitDTO returnAdvanceShareProfitDTO = handleTdReturnAdvanceShareProfitDTO(
				returnAdvanceShareProfits, period);

		if (returnAdvanceShareProfitDTO != null) {
			principalAndInterest2 = returnAdvanceShareProfitDTO.getPrincipalAndInterest() == null ? zero
					: returnAdvanceShareProfitDTO.getPrincipalAndInterest();
			tuandaiAmount2 = returnAdvanceShareProfitDTO.getTuandaiAmount() == null ? zero
					: returnAdvanceShareProfitDTO.getTuandaiAmount();
			orgAmount2 = returnAdvanceShareProfitDTO.getOrgAmount() == null ? zero
					: returnAdvanceShareProfitDTO.getOrgAmount();
			guaranteeAmount2 = returnAdvanceShareProfitDTO.getGuaranteeAmount() == null ? zero
					: returnAdvanceShareProfitDTO.getGuaranteeAmount();
			arbitrationAmount2 = returnAdvanceShareProfitDTO.getArbitrationAmount() == null ? zero
					: returnAdvanceShareProfitDTO.getArbitrationAmount();
		}

		return principalAndInterest.subtract(principalAndInterest2).compareTo(zero) > 0
				|| tuandaiAmount.subtract(tuandaiAmount2).compareTo(zero) > 0
				|| orgAmount.subtract(orgAmount2).compareTo(zero) > 0
				|| guaranteeAmount.subtract(guaranteeAmount2).compareTo(zero) > 0
				|| arbitrationAmount.subtract(arbitrationAmount2).compareTo(zero) > 0;
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
					break;
				}
			}
		}

		return returnAdvanceShareProfitDTO;
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
					.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).eq("is_valid", 1)
							.orderBy("after_id", false));
			if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {

				Map<String, IssueSendOutsideLog> batchIdMap = new HashMap<>();

				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					String batchId = tdrepayRechargeLog.getBatchId();
					if (StringUtil.isEmpty(batchId)) {
						continue;
					}
					IssueSendOutsideLog issueSendOutsideLog = batchIdMap.get(batchId);
					if (issueSendOutsideLog == null) {
						issueSendOutsideLog = issueSendOutsideLogService
								.selectOne(new EntityWrapper<IssueSendOutsideLog>()
										.eq("Interfacecode", Constant.INTERFACE_CODE_SEND_DISTRIBUTE_FUND)
										.eq("send_key", batchId));
						if (issueSendOutsideLog != null) {
							batchIdMap.put(batchId, issueSendOutsideLog);
						} else {
							batchIdMap.put(batchId, new IssueSendOutsideLog());
						}
					}

				}

				for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
					DistributeFundRecordVO vo = BeanUtils.deepCopy(tdrepayRechargeLog, DistributeFundRecordVO.class);
					TuandaiProjectInfo info = tuandaiProjectInfoService
							.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("td_user_id", vo.getTdUserId()));
					if (info != null) {
						vo.setPerson(info.getRealName());
					}
					vo.setProcessStatusStr(ProcessStatusTypeEnum.getName(tdrepayRechargeLog.getProcessStatus()));
					vo.setCreateTimeStr(DateUtil.formatDate(vo.getCreateTime()));
					vo.setFactRepayDateStr(DateUtil.formatDate(vo.getFactRepayDate()));
					IssueSendOutsideLog issueSendOutsideLog = batchIdMap.get(tdrepayRechargeLog.getBatchId());
					if (issueSendOutsideLog != null) {
						JSONObject parseObject = JSONObject.parseObject(issueSendOutsideLog.getReturnJson());
						if (parseObject != null) {
							vo.setRemark(parseObject.getString("codeDesc"));
						}
					}
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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void revokeTdrepayRecharge(List<TdrepayRechargeLog> tdrepayRechargeLogs) {
		if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
			List<TdrepayRechargeRecord> records = new ArrayList<>();

			for (TdrepayRechargeLog rechargeLog : tdrepayRechargeLogs) {
				rechargeLog.setIsValid(2);
				TdrepayRechargeRecord record = BeanUtils.deepCopy(rechargeLog, TdrepayRechargeRecord.class);
				records.add(record);
			}

			tdrepayRechargeLogService.updateBatchById(tdrepayRechargeLogs);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void handleRunningData() {
		/*
		 * 查询合规化还款处理中的数据
		 */
		List<TdrepayRechargeLog> tdrepayRechargeLogs = tdrepayRechargeLogService
				.selectList(new EntityWrapper<TdrepayRechargeLog>().eq("status", 1).eq("is_valid", 1));
		if (CollectionUtils.isNotEmpty(tdrepayRechargeLogs)) {
			Map<String, Object> paramMap = new HashMap<>();
			for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
				paramMap.put("projectId", tdrepayRechargeLog.getProjectId());
				List<TdProjectPaymentDTO> tdProjectPaymentDTOs = null;

				String sendJson = JSONObject.toJSONString(paramMap);
				IssueSendOutsideLog issueSendOutsideLog = issueSendOutsideLogService.createIssueSendOutsideLog(
						loginUserInfoHelper.getUserId(), sendJson, Constant.INTERFACE_CODE_QUERY_PROJECT_PAYMENT,
						Constant.INTERFACE_NAME_QUERY_PROJECT_PAYMENT, Constant.SYSTEM_CODE_EIP);
				Result result = null;
				try {
					LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", sendJson);
					result = eipRemote.queryProjectPayment(paramMap);
					String resultJson = JSONObject.toJSONString(result);
					LOG.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", resultJson);
					issueSendOutsideLog.setReturnJson(resultJson);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					issueSendOutsideLog.setReturnJson(e.getMessage());
				}

				if (result != null && Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())
						&& result.getData() != null) {
					JSONObject parseObject = (JSONObject) JSONObject.toJSON(result.getData());
					if (parseObject.get("projectPayments") != null) {
						tdProjectPaymentDTOs = JSONObject.parseArray(
								JSONObject.toJSONString(parseObject.get("projectPayments")), TdProjectPaymentDTO.class);
						if (CollectionUtils.isNotEmpty(tdProjectPaymentDTOs)) {

							boolean paymentFlag = false; // 是否有实还记录

							for (TdProjectPaymentDTO tdProjectPaymentDTO : tdProjectPaymentDTOs) {
								/*
								 * 匹配当期，且平台为结清状态，则更新贷后合规化表状态为处理成功
								 */
								if (tdProjectPaymentDTO.getPeriod() == tdrepayRechargeLog.getPeriod().intValue()) {
									if (tdProjectPaymentDTO.getStatus() == 1) {
										tdrepayRechargeLog.setStatus(2);
									} else {
										tdrepayRechargeLog.setStatus(3);
									}
									tdrepayRechargeLog.setUpdateTime(new Date());
									tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
									tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
									issueSendOutsideLogService.insert(issueSendOutsideLog);
									paymentFlag = true;
									break;
								}
							}

							if (!paymentFlag) {
								tdrepayRechargeLog.setStatus(0);
								tdrepayRechargeLog.setUpdateTime(new Date());
								tdrepayRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
								tdrepayRechargeLogService.updateById(tdrepayRechargeLog);
								issueSendOutsideLogService.insert(issueSendOutsideLog);
								break;
							}
						}
					}
				}

			}
		}
	}

	public static void main(String[] args) {
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
		System.out.println(sdf.format(d1));
	}
}
