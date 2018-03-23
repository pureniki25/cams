package com.hongte.alms.base.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.FsdHouse;
import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.entity.TransferLitigationLog;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.DocTmpService;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.service.FsdHouseService;
import com.hongte.alms.base.service.SysProvinceService;
import com.hongte.alms.base.service.TransferLitigationCarService;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferLitigationLogService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.BusinessHouse;
import com.hongte.alms.base.vo.litigation.LitigationResponse;
import com.hongte.alms.base.vo.litigation.LitigationResponseData;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.base.vo.litigation.house.HousePlanInfo;
import com.hongte.alms.base.vo.litigation.house.MortgageInfo;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.util.BeanUtils;

@Service("TransferOfLitigationService")
public class TransferLitigationServiceImpl implements TransferOfLitigationService {

	private static final Logger LOG = LoggerFactory.getLogger(TransferLitigationServiceImpl.class);

	private static final String XIAO_DAI_CAR = "xiaodai_car";
	private static final String XIAO_DAI_HOUSE = "xiaodai_house";

	@Autowired
	private TransferOfLitigationMapper transferOfLitigationMapper;

	@Autowired
	@Qualifier("TransfLitigationHouseService")
	private TransferLitigationHouseService transferLitigationHouseService;

	@Autowired
	@Qualifier("TransfLitigationCarService")
	private TransferLitigationCarService transferLitigationCarService;

	@Autowired
	@Qualifier("ProcessService")
	private ProcessService processService;

	@Autowired
	@Qualifier("ProcessTypeStepService")
	private ProcessTypeStepService processTypeStepService;

	@Autowired
	@Qualifier("SysProvinceService")
	private SysProvinceService sysProvinceService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("FsdHouseService")
	private FsdHouseService fsdHouseService;

	@Autowired
	@Qualifier("BasicBusinessService")
	private BasicBusinessService basicBusinessService;

	@Autowired
	@Qualifier("DocTypeService")
	private DocTypeService docTypeService;

	@Autowired
	@Qualifier("DocService")
	private DocService docService;

	@Autowired
	@Qualifier("DocTmpService")
	private DocTmpService docTmpService;

	@Autowired
	@Qualifier("CollectionStatusService")
	private CollectionStatusService collectionStatusService;
	
	@Autowired
	@Qualifier("TransferLitigationLogService")
	private TransferLitigationLogService transferLitigationLogService;

	@Override
	public Map<String, Object> queryCarLoanData(String businessId) {
		if (StringUtil.isEmpty(businessId)) {
			return null;
		}

		Map<String, Object> resultMap = transferOfLitigationMapper.queryCarLoanData(businessId);

		if (resultMap == null) {
			return resultMap;
		}

		Object overdueDays = resultMap.get("overdueDays");
		if (overdueDays != null) {
			resultMap.put("overdueDays", (int) Math.ceil(((BigDecimal) overdueDays).doubleValue()));
		}

		Object repaymentTypeId = resultMap.get("repaymentTypeId");
		String repaymentType = "";
		if (repaymentTypeId != null) {
			switch ((int) repaymentTypeId) {
			case 1:
				repaymentType = "到期还本息";
				break;
			case 2:
				repaymentType = "每月付息到期还本";
				break;
			case 4:
				repaymentType = "等本等息";
				break;
			case 5:
				repaymentType = "等额本息";
				break;
			default:
				repaymentType = "分期还本付息";
				break;
			}
			resultMap.put("repaymentTypeId", repaymentType);
		}

		Object borrowRate = resultMap.get("borrowRate");
		if (borrowRate != null) {
			double value = ((BigDecimal) borrowRate).doubleValue() / 12;
			BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
			double monthBorrowRate = bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			resultMap.put("monthBorrowRate", monthBorrowRate);
		}

		// 查询附件
		List<DocType> docTypes = docTypeService
				.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_Litigation"));
		if (docTypes != null && docTypes.size() == 1) {
			List<Doc> fileList = docService.selectList(new EntityWrapper<Doc>()
					.eq("doc_type_id", docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
			resultMap.put("returnRegFiles", fileList);
		}

		return resultMap;
	}

	@Override
	public HouseLoanVO queryHouseLoanData(String businessId) {

		if (StringUtil.isEmpty(businessId)) {
			return null;
		}

		HouseLoanVO houseLoanData = transferOfLitigationMapper.queryHouseLoanData(businessId);
		if (houseLoanData != null) {
			List<HousePlanInfo> housePlanInfos = transferOfLitigationMapper.queryRepaymentPlanHouse(businessId);
			List<MortgageInfo> mortgageInfos = transferOfLitigationMapper.queryMortgageInfoByBusinessId(businessId);
			houseLoanData.setHousePlanInfos(housePlanInfos);
			houseLoanData.setMortgageInfos(mortgageInfos);
			if (!CollectionUtils.isEmpty(housePlanInfos)) {
				for (HousePlanInfo housePlanInfo : housePlanInfos) {
					housePlanInfo.setRepaymentNum(housePlanInfos.indexOf(housePlanInfo) + 1);
				}
			}
		}
		return houseLoanData;
	}

	@Override
	public TransferOfLitigationVO sendTransferLitigationData(String businessId, String crpId, String sendUrl) {
		TransferOfLitigationVO transferLitigationData = null;
		LitigationResponse litigationResponse = null;
		if (StringUtil.isEmpty(crpId) || StringUtil.isEmpty(businessId)) {
			return transferLitigationData;
		}
		try {
			// 查询基础信息
			transferLitigationData = transferOfLitigationMapper.queryTransferLitigationData(businessId, crpId);

			Integer businessType = transferOfLitigationMapper.queryBusinessType(businessId);

			if (transferLitigationData == null || businessType == null) {
				throw new ServiceRuntimeException("没有找到相关数据，发送诉讼系统失败！");
			}

			transferLitigationData.setCreateUserId(loginUserInfoHelper.getUserId());
			// 判断业务类型，根据业务类型查询对应的房贷或车贷数据
			if (businessType == 1 || businessType == 9) {
				transferLitigationData.setBusinessTypeGroup(XIAO_DAI_CAR);
				transferLitigationData
						.setCarList(transferOfLitigationMapper.queryTransferLitigationCarData(businessId));
			}
			if (businessType == 2 || businessType == 11) {
				transferLitigationData.setBusinessTypeGroup(XIAO_DAI_HOUSE);

				transferLitigationData.setHouseList(assembleBusinessHouse(businessId));
			}
			
			litigationResponse = sendLitigation(transferLitigationData, sendUrl);
			if (litigationResponse != null) {
				if (litigationResponse.getCode() == 1) {
					LitigationResponseData data = litigationResponse.getData();
					if (!data.isImportSuccess()) {
						LOG.error("businessId：" + businessId + "，发送诉讼系统失败！！诉讼系统返回信息：" + data.toString());
						throw new ServiceRuntimeException(data.getMessage());
					}
					LOG.error("businessId：" + businessId + "，发送诉讼系统成功！诉讼系统返回信息：" + data.toString());
				}
			} else {
				LOG.error("businessId：" + businessId + "，发送诉讼系统失败！！没有消息返回");
				throw new ServiceRuntimeException("businessId：" + businessId + "，发送诉讼系统失败！！没有消息返回");
			}
		} catch (Exception e) {
			LOG.error("发送诉讼系统失败！！！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}finally {
			TransferLitigationLog transferLitigationLog = new TransferLitigationLog();
			transferLitigationLog.setBusinessId(businessId);
			transferLitigationLog.setCreateTime(new Date());
			if (transferLitigationData != null) {
				transferLitigationLog.setCreateUser(transferLitigationData.getCreateUserId());
				transferLitigationLog.setSendJson(JSON.toJSONString(transferLitigationData));
			}else {
				transferLitigationLog.setSendJson("没有找到相关诉讼数据！");
			}
			if (litigationResponse != null) {
				transferLitigationLog.setResultCode(litigationResponse.getCode());
				transferLitigationLog.setResultMsg(litigationResponse.getMsg());
				transferLitigationLog.setResultJson(JSON.toJSONString(litigationResponse));
			}else {
				transferLitigationLog.setResultMsg("诉讼系统没有消息返回！");
			}
			transferLitigationLogService.insert(transferLitigationLog);
		}

		return transferLitigationData;
	}

	private List<BusinessHouse> assembleBusinessHouse(String businessId) {
		List<FsdHouse> fsdHouses = fsdHouseService
				.selectList(new EntityWrapper<FsdHouse>().eq("business_id", businessId));
		List<BusinessHouse> businessHouses = new ArrayList<>();
		if (StringUtil.isEmpty(businessId) || CollectionUtils.isEmpty(fsdHouses)) {
			return businessHouses;
		}
		for (FsdHouse fsdHouse : fsdHouses) {
			BusinessHouse businessHouse = new BusinessHouse();
			businessHouse.setBorrowBalance(fsdHouse.getBorrowBalance());
			businessHouse.setBorrowTime(fsdHouse.getBorrowTime());
			businessHouse.setBorrowTotal(fsdHouse.getBorrowTotal());
			businessHouse.setBorrowType(fsdHouse.getBorrowType());
			businessHouse.setBusinessId(businessId);
			businessHouse.setButTime(fsdHouse.getBuyTime());
			businessHouse.setDebtRegion(String.valueOf(fsdHouse.getDebtRatio()));
			businessHouse.setFourthMortgageBalance(fsdHouse.getFourthMortgageBalance());
			businessHouse.setFourthMortgageBank(fsdHouse.getFourthMortgageBank());
			businessHouse.setFourthMortgageTime(
					DateUtil.stringToDate("yyyy-MM-dd HH:mm:ss", fsdHouse.getFourthMortgageTime()));
			businessHouse.setFourthMortgageTotal(fsdHouse.getFourthMortgageTotal());
			businessHouse.setFourthMortgageType(fsdHouse.getFourthMortgageType());
			businessHouse.setFourthMortgageYear(str2Integer(fsdHouse.getFourthMortgageYear()));
			businessHouse.setFsdBankName(fsdHouse.getHousePledgedBank());
			businessHouse.setHouseAddress(fsdHouse.getHouseAddress());
			businessHouse.setHouseArea(fsdHouse.getHouseArea());
			businessHouse.setHouseBelong(fsdHouse.getHouseBelong());
			businessHouse.setHouseName(fsdHouse.getHouseName());
			businessHouse.setHouseNo(fsdHouse.getHouseNo());
			businessHouse.setHousePrice(fsdHouse.getHousePrice());
			businessHouse.setHouseTotal(fsdHouse.getHouseTotal());
			businessHouse.setHouseValue(fsdHouse.getHouseValue());
			businessHouse.setOpenTime(fsdHouse.getOpenTime());
			businessHouse.setPropertyType(fsdHouse.getHouseType());
			businessHouse.setRegisterTime(fsdHouse.getRegisterTime());
			businessHouse.setRemark(fsdHouse.getRemark());
			businessHouse.setSecondMortgageBalance(fsdHouse.getSecondMortgageBalance());
			businessHouse.setSecondMortgageBank(fsdHouse.getSecondMortgageBank());
			businessHouse.setSecondMortgageTime(fsdHouse.getSecondMortgageTime());
			businessHouse.setSecondMortgageTotal(fsdHouse.getSecondMortgageTotal());
			businessHouse.setSecondMortgageType(fsdHouse.getSecondMortgageType());
			businessHouse.setSecondMortgageYear(fsdHouse.getSecondMortgageYear());
			businessHouse.setThirdMortgageBalance(BigDecimal.valueOf(str2Long(fsdHouse.getThirdMortgageBalance())));
			businessHouse.setThirdMortgageBank(fsdHouse.getFourthMortgageBank());
			businessHouse.setThirdMortgageTime(fsdHouse.getThirdMortgageTime());
			businessHouse.setThirdMortgageTotal(BigDecimal.valueOf(str2Long(fsdHouse.getThirdMortgageTotal())));
			businessHouse.setThirdMortgageType(fsdHouse.getThirdMortgageType());
			businessHouse.setThirdMortgageYear(fsdHouse.getThirdMortgageYear());
			businessHouse.setUpdateTime(fsdHouse.getUpdateTime());
			businessHouse.setUpdateUser(fsdHouse.getUpdateUser());
			businessHouse.setXiaodaiHouseId(fsdHouse.getXdHouseId());
			businessHouses.add(businessHouse);
		}
		return businessHouses;
	}

	private Integer str2Integer(String str) {
		return Integer.valueOf(StringUtil.isInteger(str) ? str : "0");
	}

	private Long str2Long(String str) {
		return Long.valueOf(StringUtil.isInteger(str) ? str : "0");
	}

	private LitigationResponse sendLitigation(TransferOfLitigationVO transferOfLitigationVo, String sendUrl)
			throws IOException {
		BufferedReader reader = null;
		HttpURLConnection httpUrlConn = null;
		LitigationResponse litigationResponse = new LitigationResponse();
		try {

			final URL url = new URL(sendUrl);
			// http协议传输
			httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

			String reqJsonStr = JSONObject.toJSONString(transferOfLitigationVo);

			if (!StringUtil.isEmpty(reqJsonStr)) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(reqJsonStr.getBytes("UTF-8"));
				outputStream.close();
			}

			reader = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8"));

			String str = null;
			StringBuilder builder = new StringBuilder();
			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}

			litigationResponse = (LitigationResponse) JSONObject.parseObject(builder.toString(),
					LitigationResponse.class);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		return litigationResponse;

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTransferLitigationHouse(TransferLitigationHouse req, String sendUrl, List<FileVo> files) {

		if (!CollectionUtils.isEmpty(files)) {
			for (FileVo file : files) {
				DocTmp tmp = docTmpService.selectById(file.getOldDocId());// 将临时表保存的上传信息保存到主表中
				if (tmp != null) {
					Doc doc = new Doc();
					BeanUtils.copyProperties(tmp, doc);
					doc.setOriginalName(file.getOriginalName());
					docService.insertOrUpdate(doc);
				}
			}
		}

		ProcessSaveReq processSaveReq = new ProcessSaveReq();
		processSaveReq.setBusinessId(req.getBusinessId());
		processSaveReq.setProcessStatus(Integer.valueOf(req.getProcessStatus()));
		processSaveReq.setTitle(ProcessTypeEnums.HOUSE_LOAN_LITIGATION.getName());
		processSaveReq.setProcessId(req.getProcessId());

		Process process = processService.saveProcess(processSaveReq, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);

		List<TransferLitigationHouse> litigationHouses = transferLitigationHouseService
				.selectList(new EntityWrapper<TransferLitigationHouse>().eq("business_id", req.getBusinessId())
						.eq("process_id", process.getProcessId()));

		if (CollectionUtils.isEmpty(litigationHouses)) {
			req.setProcessId(process.getProcessId());
			req.setCreateUser(process.getCreateUser());
			req.setCreateTime(new Date());
			transferLitigationHouseService.insertAllColumn(req);
		} else {
			req.setUpdateTime(new Date());
			req.setCreateUser(process.getCreateUser());
			transferLitigationHouseService.update(req, new EntityWrapper<TransferLitigationHouse>()
					.eq("business_id", req.getBusinessId()).eq("process_id", req.getProcessId()));
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTransferLitigationCar(TransferLitigationCar req, String sendUrl, List<FileVo> files) {

		if (req == null || StringUtil.isEmpty(req.getBusinessId())) {
			throw new ServiceRuntimeException("参数不能空！");
		}

		if (!CollectionUtils.isEmpty(files)) {
			for (FileVo file : files) {
				DocTmp tmp = docTmpService.selectById(file.getOldDocId());// 将临时表保存的上传信息保存到主表中
				if (tmp != null) {
					Doc doc = new Doc();
					BeanUtils.copyProperties(tmp, doc);
					doc.setOriginalName(file.getOriginalName());
					docService.insertOrUpdate(doc);
				}
			}
		}

		String businessId = req.getBusinessId();

		ProcessSaveReq processSaveReq = new ProcessSaveReq();
		processSaveReq.setBusinessId(businessId);
		processSaveReq.setProcessStatus(Integer.valueOf(req.getProcessStatus()));
		processSaveReq.setTitle(ProcessTypeEnums.CAR_LOAN_LITIGATION.getName());
		processSaveReq.setProcessId(req.getProcessId());

		Process process = processService.saveProcess(processSaveReq, ProcessTypeEnums.CAR_LOAN_LITIGATION);

		List<TransferLitigationCar> litigationCars = transferLitigationCarService
				.selectList(new EntityWrapper<TransferLitigationCar>().eq("business_id", businessId).eq("process_id",
						process.getProcessId()));

		if (CollectionUtils.isEmpty(litigationCars)) {
			req.setProcessId(process.getProcessId());
			req.setCreateUser(process.getCreateUser());
			req.setCreateTime(new Date());
			transferLitigationCarService.insertAllColumn(req);
		} else {
			req.setUpdateTime(new Date());
			req.setCreateUser(process.getCreateUser());
			transferLitigationCarService.update(req, new EntityWrapper<TransferLitigationCar>()
					.eq("business_id", businessId).eq("process_id", req.getProcessId()));
		}

	}

	@Override
	public Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO) {

		if (carLoanBilVO == null || StringUtil.isEmpty(carLoanBilVO.getBusinessId())
				|| carLoanBilVO.getBillDate() == null) {
			return null;
		}
		// 获取车贷基础信息
		String businessId = carLoanBilVO.getBusinessId();
		Date billDate = carLoanBilVO.getBillDate(); // 预计结清日期
		Map<String, Object> resultMap = queryCarLoanData(businessId);

		double innerLateFees = 0; // 期内滞纳金
		double outsideInterest = 0; // 期外逾期利息
		double preLateFees = 0; // 提前还款违约金
		double squaredUp = 0; // 最终结清金额
		double receivableTotal = 0; // 应收合计
		boolean preLateFeesFlag = false;

		if (resultMap != null && !resultMap.isEmpty()) {

			// 查询往期少交费用明细
			resultMap.put("previousFees", transferOfLitigationMapper.queryPreviousFees(businessId, billDate));

			// 当期的 利息、服务费、担保公司费用、平台费
			resultMap.putAll(transferOfLitigationMapper.queryCarLoanFees(businessId, billDate));

			Date minDueDate = (Date) resultMap.get("minDueDate"); // 状态为'逾期', '还款中'的最早应还日期
			Map<String, Object> maxPeriodMap = transferOfLitigationMapper.queryMaxDueDateByBusinessId(businessId); // 合同到期日
			Date maxDueDate = (Date) maxPeriodMap.get("maxDueDate");
			double lastPlanAccrual = ((BigDecimal) maxPeriodMap.get("planAccrual")).doubleValue(); // 最后一期应还利息
			double lastPlanServiceCharge = ((BigDecimal) maxPeriodMap.get("planServiceCharge")).doubleValue(); // 最后一期应还服务费
			double lastGuaranteeCharge = ((BigDecimal) maxPeriodMap.get("plan_guarantee_charge")).doubleValue(); // 最后一期担保公司费用
			double lastPlatformCharge = ((BigDecimal) maxPeriodMap.get("plan_platform_charge")).doubleValue(); // 最后一期平台费
			int overdueDays = differentDays(minDueDate, billDate); // 逾期天数
//			long isPreCharge = (long) resultMap.get("isPreCharge"); // 是否服务费一次性收取业务
			long isPreServiceFees = (long) resultMap.get("isPreServiceFees"); // 是否分公司服务费前置收取
			double planAccrual = ((BigDecimal) resultMap.get("planAccrual")).doubleValue(); // 本期应还利息
			double planServiceCharge = ((BigDecimal) resultMap.get("planServiceCharge")).doubleValue(); // 本期应还服务费
			double planPlatformCharge = ((BigDecimal) resultMap.get("planPlatformCharge")).doubleValue(); // 本期应还平台费
			double planGuaranteeCharge = ((BigDecimal) resultMap.get("planGuaranteeCharge")).doubleValue(); // 本期应还担保公司费用
			double balanceDue = ((BigDecimal) resultMap.get("balanceDue")).doubleValue(); // 往期少交费用合计
			double parkingFees = carLoanBilVO.getParkingFees(); // 停车费
			double gpsFees = carLoanBilVO.getGpsFees(); // GPS费
			double dragFees = carLoanBilVO.getDragFees(); // 拖车费
			double otherFees = carLoanBilVO.getOtherFees(); // 其他费用
			double attorneyFees = carLoanBilVO.getAttorneyFees(); // 律师
			double balance = 0; // 结余
			double cash = 0; // 押金转还款金额

			int curPeriod = (int) resultMap.get("curPeriod"); // // 当前期数
			long totalPeriod = (long) resultMap.get("totalPeriod"); // 总还款期数

			String repaymentTypeId = (String) resultMap.get("repaymentTypeId"); // 还款类型

			int outputPlatformId = (int) resultMap.get("outputPlatformId"); // 出款平台

			double borrowMoney = ((BigDecimal) resultMap.get("borrowMoney")).doubleValue(); // 借款金额

			double surplusPrincipal = ((BigDecimal) resultMap.get("surplusPrincipal")).doubleValue(); // 剩余本金

			double borrowRate = ((BigDecimal) resultMap.get("borrowRate")).doubleValue() * 0.01; // 年利率

			// 还款类型：先息后本

			double innerLate = carLoanBilVO.getInnerLateFees(); // 期内滞纳金计算费率
			double outside = carLoanBilVO.getOutsideInterest(); // 期外逾期利息计算费率
			int preLateFeeType = carLoanBilVO.getPreLateFees(); // 提前还款违约金类型

			innerLateFees = borrowMoney * innerLate * overdueDays;

			// 判断预计结算日期是否超过合同日期
			if (billDate.after(maxDueDate)) {
				planAccrual = lastPlanAccrual; // 预算日期大于最后一期应还日期时，应还利息取最后一期的应还利息
				planServiceCharge = lastPlanServiceCharge;
				planGuaranteeCharge = lastGuaranteeCharge;
				planPlatformCharge = lastPlatformCharge;

				if (overdueDays < 15) {
					outsideInterest = surplusPrincipal * outside / 30 * overdueDays;
				} else if (15 <= overdueDays && overdueDays < 30) {
					outsideInterest = surplusPrincipal * outside;
				} else {
					int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
					int j = overdueDays % 30;
					if (i > 1) { // 若 i 大于 1，说明超过30天, 期外逾期费 = 剩余本金 * 费率 * i + 剩余本金 * 费率 / 30 * j
						outsideInterest = surplusPrincipal * outside * i;
						outsideInterest += surplusPrincipal * outside / 30 * j;
					}
				}

				// 判断是否上标业务： outputPlatformId == 1 是， outputPlatformId == 0 否
				// 判断是否服务费一次性收取业务 isPreCharge == 1， 是 isPreCharge == 0 否
				// 判断是否分公司服务费前置收取 isPreServiceFees == 1， 是 isPreServiceFees == 0 否
				if ("到期还本息".equals(repaymentTypeId) || "每月付息到期还本".equals(repaymentTypeId)) {

					if (outputPlatformId == 1 && isPreServiceFees == 0) {
						outsideInterest = 0;
					}
				} else if ("等额本息".equals(repaymentTypeId)) {

					if (outputPlatformId == 0) {
						if (overdueDays < 15) {
							outsideInterest = borrowMoney * outside / 30 * overdueDays;
						} else if (15 <= overdueDays && overdueDays < 30) {
							outsideInterest = borrowMoney * outside;
						} else {
							int i = (overdueDays / 30) <= 1 ? 1 : (overdueDays / 30);
							int j = overdueDays % 30;
							if (i > 1) { // 若 i 大于 1，说明超过30天, 期外逾期费 = 剩余本金 * 费率 * i + 剩余本金 * 费率 / 30 * j
								outsideInterest = borrowMoney * outside * i;
								outsideInterest += borrowMoney * outside / 30 * j;
							}
						}

					}
				} else {
					return null;
				}

			} else {
				if ("到期还本息".equals(repaymentTypeId) || "每月付息到期还本".equals(repaymentTypeId)) {

					if (outputPlatformId == 1 && isPreServiceFees == 0) {
						preLateFees = ((BigDecimal) resultMap.get("surplusServiceCharge")).doubleValue();
						outsideInterest = 0;
					}
				} else if ("等额本息".equals(repaymentTypeId)) {
					if (outputPlatformId == 0) {
						
						preLateFeesFlag = true;	// 提前还款违约金标识，非上标业务，等额本息为true
						
						switch (preLateFeeType) {
						case 1:
							// 一个月利息
							preLateFees = borrowMoney * borrowRate / 12;
							break;
						case 2:
							// 借款本金 * 0.03
							preLateFees = borrowMoney * 0.03;
							break;
						case 3:
							// 剩余本金 * 0.005 * 剩余期数
							preLateFees = surplusPrincipal * 0.005 * (totalPeriod - curPeriod);
							break;
						default:
							break;
						}
					} else if (outputPlatformId == 1 && isPreServiceFees == 1) {
						preLateFees = ((BigDecimal) resultMap.get("surplusServiceCharge")).doubleValue();
					}
				}
			}

			receivableTotal = borrowMoney + planAccrual + planServiceCharge + planPlatformCharge + planGuaranteeCharge
					+ innerLateFees + outsideInterest + preLateFees + balanceDue + parkingFees + gpsFees + dragFees
					+ otherFees + attorneyFees;
			squaredUp = receivableTotal - cash - balance;
			resultMap.put("innerLateFees", Math.round(innerLateFees * 100) * 0.01);
			resultMap.put("outsideInterest", Math.round(outsideInterest * 100) * 0.01);
			resultMap.put("preLateFees", Math.round(preLateFees * 100) * 0.01);
			resultMap.put("planAccrual", Math.round(planAccrual * 100) * 0.01);
			resultMap.put("receivableTotal", Math.round(receivableTotal * 100) * 0.01);
			resultMap.put("squaredUp", Math.round(squaredUp * 100) * 0.01);
			resultMap.put("cash", Math.round(cash * 100) * 0.01);
			resultMap.put("balance", Math.round(balance * 100) * 0.01);
			resultMap.put("preLateFeesFlag", preLateFeesFlag);
		}

		return resultMap;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveCarProcessApprovalResult(ProcessLogReq req, String sendUrl) {
		try {
			// 存储审批结果信息
			Process process = processService.saveProcessApprovalResult(req, ProcessTypeEnums.CAR_LOAN_LITIGATION);
			String businessId = process.getBusinessId();
			String processId = process.getProcessId();
			List<TransferLitigationCar> cars = transferLitigationCarService
					.selectList(new EntityWrapper<TransferLitigationCar>().eq("business_id", businessId)
							.eq("process_id", processId));
			Integer status = process.getStatus();
			Integer processResult = process.getProcessResult();
			if (!CollectionUtils.isEmpty(cars) && status == ProcessStatusEnums.END.getKey()
					&& processResult == ProcessApproveResult.PASS.getKey()) {
				sendTransferLitigationData(businessId, cars.get(0).getCrpId(), sendUrl);
				// 更新贷后状态为 移交诉讼
				collectionStatusService.setBussinessAfterStatus(req.getBusinessId(), req.getCrpId(), "",
						CollectionStatusEnum.TO_LAW_WORK, CollectionSetWayEnum.MANUAL_SET);
			}
		} catch (Exception e) {
			LOG.error("---saveCarProcessApprovalResult--- 存储房贷审批结果信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveHouseProcessApprovalResult(ProcessLogReq req, String sendUrl) {
		try {
			// 存储审批结果信息
			Process process = processService.saveProcessApprovalResult(req, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);
			String businessId = process.getBusinessId();
			String processId = process.getProcessId();
			List<TransferLitigationHouse> houses = transferLitigationHouseService
					.selectList(new EntityWrapper<TransferLitigationHouse>().eq("business_id", businessId)
							.eq("process_id", processId));
			Integer status = process.getStatus();
			Integer processResult = process.getProcessResult();
			if (!CollectionUtils.isEmpty(houses) && status == ProcessStatusEnums.END.getKey()
					&& processResult == ProcessApproveResult.PASS.getKey()) {
				sendTransferLitigationData(businessId, houses.get(0).getCrpId(), sendUrl);
				// 更新贷后状态为 移交诉讼
				collectionStatusService.setBussinessAfterStatus(req.getBusinessId(), req.getCrpId(), "",
						CollectionStatusEnum.TO_LAW_WORK, CollectionSetWayEnum.MANUAL_SET);
			}
		} catch (Exception e) {
			LOG.error("---saveHouseProcessApprovalResult--- 存储房贷审批结果信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * date2比date1多的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	private int differentDays(Date first, Date second) {
		if (first.after(second)) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(first);
		int cnt = 0;
		while (calendar.getTime().compareTo(second) != 0) {
			calendar.add(Calendar.DATE, 1);
			cnt++;
		}
		return cnt;
	}

	public static void main(String[] args) {
		int i = 61 / 30;
		int j = 20 % 30;
		System.out.println(i);
		System.out.println(j);
	}
}
