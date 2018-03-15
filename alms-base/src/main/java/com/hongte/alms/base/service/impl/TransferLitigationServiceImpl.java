package com.hongte.alms.base.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.FsdHouse;
import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.service.FsdHouseService;
import com.hongte.alms.base.service.SysProvinceService;
import com.hongte.alms.base.service.TransferLitigationCarService;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.BusinessHouse;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.base.vo.litigation.house.HousePlanInfo;
import com.hongte.alms.base.vo.litigation.house.MortgageInfo;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;

@Service("transferLitigationService")
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
		if (StringUtil.isEmpty(crpId) || StringUtil.isEmpty(businessId)) {
			return transferLitigationData;
		}
		try {
			// 查询基础信息
			transferLitigationData = transferOfLitigationMapper.queryTransferLitigationData(businessId, crpId);

			Integer businessType = transferOfLitigationMapper.queryBusinessType(businessId);

			if (transferLitigationData != null && businessType != null) {
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
				sendLitigation(transferLitigationData, sendUrl);
			}
		} catch (Exception e) {
			LOG.error("发送诉讼系统失败！！！", e);
			throw new ServiceRuntimeException("发送诉讼系统失败！！！", e);
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
			businessHouse.setFourthMortgageTime(DateUtil.stringToDate("yyyy-MM-dd HH:mm:ss", fsdHouse.getFourthMortgageTime()));
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
			businessHouse.setPropertyType(String.valueOf(fsdHouse.getHouseType()));
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

	private void sendLitigation(TransferOfLitigationVO transferOfLitigationVo, String sendUrl) throws ClientProtocolException, IOException {

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			// 创建httpclient对象
			httpClient = HttpClients.createDefault();

			// 创建post方式请求对象
			HttpPost post = new HttpPost(sendUrl);
			// 构造消息头
			post.addHeader("Content-Type", "application/json");

			// 构建消息实体
			StringEntity entity = new StringEntity(JSONObject.toJSONString(transferOfLitigationVo),
					Charset.forName("UTF-8"));
			entity.setContentEncoding("UTF-8");

			// 发送Json格式的数据请求
			entity.setContentType("application/json");
			post.setEntity(entity);
			response = httpClient.execute(post);

			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				LOG.info("--sendLitigation-- 请求成功: " + statusCode);
			}else {
				throw new ServiceRuntimeException("--sendLitigation-- 诉讼数据发送失败！！！" + statusCode);
			}
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					LOG.error("--sendLitigation-- httpClient.close() 失败！！！", e);
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					LOG.error("--sendLitigation-- response.close() 失败！！！", e);
				}
			}
		}

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTransferLitigationHouse(TransferLitigationHouse req, String sendUrl) {

		if (req == null || StringUtil.isEmpty(req.getBusinessId())) {
			throw new ServiceRuntimeException("参数不能空！");
		}

		ProcessSaveReq processSaveReq = new ProcessSaveReq();
		processSaveReq.setBusinessId(req.getBusinessId());
		processSaveReq.setProcessStatus(Integer.valueOf(req.getProcessStatus()));
		processSaveReq.setTitle("房贷移交诉讼审批流程");

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

		List<ProcessTypeStep> processTypeSteps = processTypeStepService.selectList(
				new EntityWrapper<ProcessTypeStep>().eq("type_id", process.getProcessTypeid()).orderBy("step"));
		if (!CollectionUtils.isEmpty(processTypeSteps)
				&& process.getCurrentStep() == processTypeSteps.get(processTypeSteps.size() - 1).getStep()) {
			sendTransferLitigationData(req.getBusinessId(), req.getCrpId(), sendUrl);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveTransferLitigationCar(TransferLitigationCar req, String sendUrl) {

		if (req == null || StringUtil.isEmpty(req.getBusinessId())) {
			throw new ServiceRuntimeException("参数不能空！");
		}

		ProcessSaveReq processSaveReq = new ProcessSaveReq();
		processSaveReq.setBusinessId(req.getBusinessId());
		processSaveReq.setProcessStatus(Integer.valueOf(req.getProcessStatus()));
		processSaveReq.setTitle("车贷移交诉讼审批流程");

		Process process = processService.saveProcess(processSaveReq, ProcessTypeEnums.CAR_LOAN_LITIGATION);

		List<TransferLitigationCar> litigationCars = transferLitigationCarService
				.selectList(new EntityWrapper<TransferLitigationCar>().eq("business_id", req.getBusinessId())
						.eq("process_id", process.getProcessId()));

		if (CollectionUtils.isEmpty(litigationCars)) {
			req.setProcessId(process.getProcessId());
			req.setCreateUser(process.getCreateUser());
			req.setCreateTime(new Date());
			transferLitigationCarService.insertAllColumn(req);
		} else {
			req.setUpdateTime(new Date());
			req.setCreateUser(process.getCreateUser());
			transferLitigationCarService.update(req, new EntityWrapper<TransferLitigationCar>()
					.eq("business_id", req.getBusinessId()).eq("process_id", req.getProcessId()));
		}

		List<ProcessTypeStep> processTypeSteps = processTypeStepService.selectList(
				new EntityWrapper<ProcessTypeStep>().eq("type_id", process.getProcessTypeid()).orderBy("step"));
		if (!CollectionUtils.isEmpty(processTypeSteps)
				&& process.getCurrentStep() == processTypeSteps.get(processTypeSteps.size() - 1).getStep()) {
			sendTransferLitigationData(req.getBusinessId(), req.getCrpId(), sendUrl);
		}
	}
	
	@Override
	public Map<String, Object> queryCarLoanBilDetail(String businessId, Date billDate) {
		if (StringUtil.isEmpty(businessId)) {
			return null;
		}
		// 车贷明细
		Map<String, Object> resultMap = queryCarLoanData(businessId);
		if (resultMap == null || resultMap.isEmpty()) {
			return null;
		}
		// 根据还款计划ID找当期的 利息、服务费、担保公司费用、平台费
		Map<String, Object> carLoanFees = transferOfLitigationMapper.queryCarLoanFees(businessId, billDate);
		carLoanFees.remove("planAccrual");
		
		resultMap.putAll(carLoanFees);
		return resultMap;
	}

	@Override
	public Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO) {

		if (carLoanBilVO == null || StringUtil.isEmpty(carLoanBilVO.getBusinessId())) {
			return null;
		}
		// 获取车贷基础信息
		String businessId = carLoanBilVO.getBusinessId();
		Date billDate = carLoanBilVO.getBillDate(); // 预计结清日期
		
		int preLateFeeType = carLoanBilVO.getPreLateFees(); // 提前还款违约金类型
		double innerLateFees = 0; // 期内滞纳金
		double outsideInterest = 0; // 期外逾期利息
		double preLateFees = 0; // 提前还款违约金

		Map<String, Object> resultMap = queryCarLoanData(businessId);

		// 根据还款计划ID找当期的 利息、服务费、担保公司费用、平台费
		Map<String, Object> carLoanFees = transferOfLitigationMapper.queryCarLoanFees(businessId, billDate);

		int overdueDays = 0; // 逾期天数
		long isPreCharge = 0; // 是否分公司服务费前置收取
		double planAccrual = 0; // 本期利息
		int curPeriod = 0;	// 当前期数
		long totalPeriod = 0;

		if (carLoanFees != null && !carLoanFees.isEmpty()) {
			Date dueDate = (Date) carLoanFees.get("dueDate"); // 当前期数应还日期
			overdueDays = differentDays(dueDate, billDate);
			resultMap.putAll(carLoanFees);
			isPreCharge = (long) carLoanFees.get("isPreCharge");
			planAccrual = ((BigDecimal) carLoanFees.get("planAccrual")).doubleValue();
			curPeriod = (int) carLoanFees.get("curPeriod");
			totalPeriod = (long) carLoanFees.get("totalPeriod");
		}

		if (resultMap != null && !resultMap.isEmpty()) {

			String repaymentTypeId = (String) resultMap.get("repaymentTypeId"); // 还款类型

			int outputPlatformId = (int) resultMap.get("outputPlatformId"); // 出款平台

			double borrowMoney = ((BigDecimal) resultMap.get("borrowMoney")).doubleValue(); // 借款金额

			double surplusPrincipal = ((BigDecimal) resultMap.get("surplusPrincipal")).doubleValue(); // 剩余本金

			double borrowRate = ((BigDecimal) resultMap.get("borrowRate")).doubleValue() * 0.01; // 利率

			// 还款类型：先息后本

			innerLateFees = borrowMoney * carLoanBilVO.getInnerLateFees() * overdueDays;
			outsideInterest = (overdueDays < 15)
					? surplusPrincipal * carLoanBilVO.getOutsideInterest() / 30 * overdueDays
					: surplusPrincipal * carLoanBilVO.getOutsideInterest();

			// 判断是否上标业务： outputPlatformId == 1 是， outputPlatformId == 0 否
			// 判断是否分公司服务费前置收取 isPreCharge == 1， 是 isPreCharge == 0 否
			if ("到期还本息".equals(repaymentTypeId) || "每月付息到期还本".equals(repaymentTypeId)) {
				if (outputPlatformId == 1 && isPreCharge == 0) {
					outsideInterest = 0;
					preLateFees = ((BigDecimal) resultMap.get("surplusServiceCharge")).doubleValue();
				}
			} else if ("等额本息".equals(repaymentTypeId)) {
				planAccrual = surplusPrincipal * borrowRate / 12;
				if (outputPlatformId == 0) {
					outsideInterest = (overdueDays < 15)
							? borrowMoney * carLoanBilVO.getOutsideInterest() / 30 * overdueDays
							: borrowMoney * carLoanBilVO.getOutsideInterest();
				}
				if (outputPlatformId == 1 && isPreCharge == 0) {
					switch (preLateFeeType) {
					case 1:
						preLateFees = borrowMoney * borrowRate / 12;
						break;
					case 2:
						preLateFees = borrowMoney * 0.03;
						break;
					case 3:
						// TODO 剩余期数
						preLateFees = borrowMoney * borrowRate / 12;
						break;

					default:
						break;
					}
				}
			} else {
				return null;
			}
			resultMap.put("innerLateFees", Math.round(innerLateFees * 100) * 0.01);
			resultMap.put("outsideInterest", Math.round(outsideInterest * 100) * 0.01);
			resultMap.put("preLateFees", Math.round(preLateFees * 100) * 0.01);
			resultMap.put("planAccrual", Math.round(planAccrual * 100) * 0.01);
		}

		return resultMap;
	}

	/**
	 * date2比date1多的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	private int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) {
			// 同一年
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else {
					// 不是闰年
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);
		} else {
			// 不同年
			return day2 - day1;
		}
	}
}
