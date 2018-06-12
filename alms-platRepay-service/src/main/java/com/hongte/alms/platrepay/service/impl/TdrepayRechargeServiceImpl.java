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
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.TdrepayRechargeLogMapper;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.base.service.TdrepayRechargeDetailService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.dto.TdProjectPaymentDTO;
import com.hongte.alms.platrepay.dto.TdProjectPaymentResult;
import com.hongte.alms.platrepay.dto.TdReturnAdvanceShareProfitResult;
import com.hongte.alms.platrepay.dto.TdrepayProjectInfoDTO;
import com.hongte.alms.platrepay.dto.TdrepayProjectPeriodInfoDTO;
import com.hongte.alms.platrepay.service.TdrepayRechargeService;
import com.hongte.alms.platrepay.vo.TdrepayRechargeInfoVO;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.core.Result;

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
	private TdrepayRechargeLogMapper tdrepayRechargeLogMapper;

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
	@Value("${recharge.account.yi_dian_car_loan}")
	private String yiDianCarLoanUserId;

	@Value("${recharge.account.car.loan.oid.partner}")
	private String carLoanOidPartner;
	@Value("${recharge.account.house.loan.loan.oid.partner}")
	private String houseLoanOidPartner;
	@Value("${recharge.account.relief.loan.loan.oid.partner}")
	private String reliefLoanOidPartner;
	@Value("${recharge.account.quick.loan.loan.oid.partner}")
	private String quickLoanOidPartner;
	@Value("${recharge.account.car.business.loan.oid.partner}")
	private String carBusinessOidPartner;
	@Value("${recharge.account.second.hand.car.loan.loan.oid.partner}")
	private String secondHandCarLoanOidPartner;
	@Value("${recharge.account.yi_dian_car_loan.loan.oid.partner}")
	private String yiDianCarLoanOidPartner;

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

			if (result != null && result.getData() != null && "0000".equals(result.getReturnCode())) {
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
		rechargeLog.setConfirmTime(vo.getConfirmTime());
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
					if (infoVOs.size() > 1) {
						for (List<TdrepayRechargeInfoVO> rechargeInfoVOs : infoVOs) {
							// 调用 eip 平台资金分发接口
							Result result = sendDistributeFund(rechargeInfoVOs, entry.getKey(), userId);
							handleSendDistributeFundResult(vos, userId, result);
						}
					} else if (infoVOs.size() == 1) {

						// 调用 eip 平台资金分发接口
						Result result = sendDistributeFund(infoVOs.get(0), entry.getKey(), userId);
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

	private void mismatchConditionFilter(List<TdrepayRechargeInfoVO> vos) {
		if (CollectionUtils.isEmpty(vos)) {
			return;
		}

		List<TdrepayRechargeInfoVO> lstVO = new LinkedList<>();
		for (TdrepayRechargeInfoVO vo : vos) {
			Integer repaySource = vo.getRepaySource();
			if (repaySource != null && repaySource.intValue() != 1 && repaySource.intValue() != 2) {
				lstVO.add(vo);
			}
		}

		vos.removeAll(lstVO);

		if (CollectionUtils.isEmpty(vos)) {
			throw new ServiceRuntimeException("请重新选择符合资金分发条件的数据");
		}
	}

	@SuppressWarnings("rawtypes")
	private void handleSendDistributeFundResult(List<TdrepayRechargeInfoVO> vos, String userId, Result result) {
		if (result == null || !"0000".equals(result.getReturnCode())) {
			updateTdrepayRechargeLogProcessStatus(vos, 3, userId);
		}
	}

	private void updateTdrepayRechargeLogProcessStatus(List<TdrepayRechargeInfoVO> vos, Integer processStatus,
			String userId) {
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
	}

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
		 * 将vos中的数据装入一个批次中
		 */
		List<DistributeFundDetailDTO> detailList = new LinkedList<>();

		double totalAmount = 0;

		/*
		 * 记录调用第三方接口日志
		 */
		IssueSendOutsideLog outsideLog = new IssueSendOutsideLog();
		outsideLog.setSystem(Constant.SYSTEM_CODE_EIP);

		List<TdrepayRechargeLog> tdrepayRechargeLogs = new LinkedList<>();
		for (TdrepayRechargeInfoVO vo : rechargeInfoVOs) {
			// 团贷网用户唯一编号 必须为guid转化成的字符串
			String requestNo = UUID.randomUUID().toString();
			String tdUserId = UUID.randomUUID().toString();
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

		try {
			// 调用 eip 平台资金分发接口
			Result result = eipRemote.userDistributeFund(dto);
			outsideLog.setReturnJson(JSONObject.toJSONString(result));
			issueSendOutsideLogService.insert(outsideLog);

			return result;
		} catch (Exception e) {
			LOG.error("批次号:" + batchId + "，调用eip平台资金分发接口失败！DTO 数据：" + dto.toString(), e);
			outsideLog.setReturnJson(e.getMessage());
			issueSendOutsideLogService.insert(outsideLog);
			return null;
		}

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
		default:
			oIdPartner = "";
			break;
		}
		return oIdPartner;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void rePayComplianceWithRequirements(String logId) {
		/*
		 * 读取分发成功的数据
		 */
		List<TdrepayRechargeLog> tdrepayRechargeLogs = queryRechargeSuccessData(logId);
		if (CollectionUtils.isEmpty(tdrepayRechargeLogs)) {
			return;
		}

		/*
		 * 根据 settle_type 是否结清
		 */
		List<TdrepayRechargeLog> settleData = new ArrayList<>(); // 结清数据
		List<TdrepayRechargeLog> notSettleData = new ArrayList<>(); // 未结清数据
		handleSettleTypeData(tdrepayRechargeLogs, settleData, notSettleData);

		/*
		 * 一、结清
		 */
		// TODO 待添加结清逻辑

		/*
		 * 二、未结清
		 */

		if (notSettleData.isEmpty()) {
			return;
		}

		/*
		 * 1、判断 标的当前期是否存在垫付未还记录 需另外创建还垫付记录表 根据 标的还款信息查询接口 和 还垫付信息查询接口 取得数据对比
		 */

		// 有未还垫付记录的数据
		List<TdrepayRechargeLog> rechargeLogs = handleNotSettleData(notSettleData);

		/*
		 * 2、若是, 则调用 偿还垫付接口 还垫付 （到期垫付本息+分润）
		 */
		if (CollectionUtils.isNotEmpty(rechargeLogs)) {
			
			// 按期数从小到大进行排序
			sortChargeLogListByPeriod(rechargeLogs);

			for (TdrepayRechargeLog tdrepayRechargeLog : rechargeLogs) {
				// 根据logId获取 团贷网合规化还款标的充值明细表

				List<TdrepayRechargeDetail> tdrepayRechargeDetails = tdrepayRechargeDetailService
						.selectList(new EntityWrapper<TdrepayRechargeDetail>()
								.eq("log_id", tdrepayRechargeLog.getLogId()).orderBy("fee_type"));
				if (CollectionUtils.isNotEmpty(tdrepayRechargeDetails)) {
					
					// 调用 偿还垫付接口 ， 按期数从小到大顺序调用，若某一期执行失败，则后面期数本次不再继续执行
					Result result = remoteAdvanceShareProfit(tdrepayRechargeLog, tdrepayRechargeDetails);
					if (result == null || !"0000".equals(result.getReturnCode())) {
						break;
					}
				}

			}
		}

		// a、还垫付成功
		// 标记成功 流程结束

		// b、还垫付失败
		// 标记失败 每60分钟重试一次
	}

	private void sortChargeLogListByPeriod(List<TdrepayRechargeLog> rechargeLogs) {
		if (rechargeLogs.size() > 1) {
			Collections.sort(rechargeLogs, new Comparator<TdrepayRechargeLog>() {

				@Override
				public int compare(TdrepayRechargeLog o1, TdrepayRechargeLog o2) {
					return o1.getPeriod() - o2.getPeriod();
				}
			});
		}
	}

	@SuppressWarnings("rawtypes")
	private Result remoteAdvanceShareProfit(TdrepayRechargeLog tdrepayRechargeLog,
			List<TdrepayRechargeDetail> tdrepayRechargeDetails) {
		TdAdvanceShareProfitDTO paramDTO = new TdAdvanceShareProfitDTO();
		paramDTO.setPeriod(tdrepayRechargeLog.getPeriod());
		paramDTO.setProjectId(tdrepayRechargeLog.getProjectId());

		for (TdrepayRechargeDetail tdrepayRechargeDetail : tdrepayRechargeDetails) {

			Integer feeType = tdrepayRechargeDetail.getFeeType();
			BigDecimal feeValue = tdrepayRechargeDetail.getFeeValue();
			BigDecimal principalAndInterest = paramDTO.getPrincipalAndInterest();
			double principalAndInterestDouble = principalAndInterest == null ? 0
					: principalAndInterest.doubleValue();

			switch (feeType) {
			case 10:
				paramDTO.setPrincipalAndInterest(feeValue);
				break;
			case 20:
				paramDTO.setPrincipalAndInterest(BigDecimal.valueOf(
						principalAndInterestDouble + (feeValue == null ? 0 : feeValue.doubleValue())));
				break;
			case 30:
				paramDTO.setTuandaiAmount(feeValue);
				break;
			case 40:
				paramDTO.setOrgAmount(feeValue);
				break;
			case 50:
				paramDTO.setGuaranteeAmount(feeValue);
				break;
			case 60:
				paramDTO.setArbitrationAmount(feeValue);
				break;
			case 70:
				paramDTO.setOverDueAmount(feeValue);
				break;

			default:
				break;
			}
		}
		return eipRemote.advanceShareProfit(paramDTO);
	}

	private List<TdrepayRechargeLog> queryRechargeSuccessData(String logId) {
		TdrepayRechargeLog rechargeLog = tdrepayRechargeLogService
				.selectOne(new EntityWrapper<TdrepayRechargeLog>().eq("log_id", logId));

		if (rechargeLog == null) {
			return Collections.emptyList();
		}

		TdrepayRechargeLog paramLog = new TdrepayRechargeLog();
		paramLog.setProjectId(rechargeLog.getProjectId());
		paramLog.setProcessStatus(2);
		List<Integer> lstStatus = new ArrayList<>();
		lstStatus.add(0);
		lstStatus.add(3);
		return tdrepayRechargeLogService
				.selectList(new EntityWrapper<TdrepayRechargeLog>(paramLog).in("status", lstStatus));
	}

	@SuppressWarnings("rawtypes")
	private List<TdrepayRechargeLog> handleNotSettleData(List<TdrepayRechargeLog> notSettleData) {
		List<TdrepayRechargeLog> rechargeLogs = new ArrayList<>();

		for (TdrepayRechargeLog tdrepayRechargeLog : notSettleData) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("projectId", tdrepayRechargeLog.getProjectId());
			Result result1 = eipRemote.queryProjectPayment(paramMap); // 标的还款信息
			Result result2 = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
			if (result1 != null && result2 != null && "0000".equals(result2.getReturnCode())
					&& "0000".equals(result1.getReturnCode())) {

				// 标的还款信息
				TdProjectPaymentResult projectPaymentResult = null;
				if (result1.getData() != null) {
					String json1 = JSONObject.toJSONString(result1.getData());
					projectPaymentResult = JSONObject.parseObject(json1, TdProjectPaymentResult.class);
				}

				// 还垫付信息
				TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult = null;
				if (result2.getData() != null) {
					String json2 = JSONObject.toJSONString(result2.getData());
					returnAdvanceShareProfitResult = JSONObject.parseObject(json2,
							TdReturnAdvanceShareProfitResult.class);
				}

				// 根据 标的还款信息 和 还垫付信息 判断是否有未还垫付记录
				if (isAdvance(projectPaymentResult, returnAdvanceShareProfitResult)) {
					rechargeLogs.add(tdrepayRechargeLog);
				}
			}
		}

		return rechargeLogs;
	}

	private boolean isAdvance(TdProjectPaymentResult projectPaymentResult,
			TdReturnAdvanceShareProfitResult returnAdvanceShareProfitResult) {
		if (projectPaymentResult != null && CollectionUtils.isNotEmpty(projectPaymentResult.getProjectPayment())) {
			for (TdProjectPaymentDTO tdProjectPaymentDTO : projectPaymentResult.getProjectPayment()) {
				//TODO	判断是否有未还垫付
			}
			return true;
		}
		return false;
	}

	private void handleSettleTypeData(List<TdrepayRechargeLog> tdrepayRechargeLogs, List<TdrepayRechargeLog> settleData,
			List<TdrepayRechargeLog> notSettleData) {
		for (TdrepayRechargeLog tdrepayRechargeLog : tdrepayRechargeLogs) {
			Integer settleType = tdrepayRechargeLog.getSettleType();
			if (settleType != null && (settleType.intValue() == 10 || settleType.intValue() == 11
					|| settleType.intValue() == 20 || settleType.intValue() == 30)) {
				settleData.add(tdrepayRechargeLog);
			} else {
				notSettleData.add(tdrepayRechargeLog);
			}
		}
	}

}
