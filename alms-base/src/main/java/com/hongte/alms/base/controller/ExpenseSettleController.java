package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.enums.RepayPlanStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.base.vo.module.MoneyPoolRepaymentXindaiDTO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

import feign.Feign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/expenseSettle")
@RefreshScope
public class ExpenseSettleController {

	private Logger logger = LoggerFactory.getLogger(ExpenseSettleController.class);

	@Value("${bmApi.apiUrl}")
	String xindaiAplUrlUrl ;
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;

	@Autowired
	@Qualifier("RenewalBusinessService")
	RenewalBusinessService renewalBusinessService ;
	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService;

	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	BasicRepaymentTypeService basicRepaymentTypeService;

	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@Autowired
	@Qualifier("ExpenseSettleService")
	ExpenseSettleService expenseSettleService;
	
	@Autowired
	TransferOfLitigationMapper transferOfLitigationMapper ;

	private List<ExpenseSettleLackFeeVO> lackFeeList = new ArrayList<>();
	private BigDecimal principal = new BigDecimal(0).setScale(2);
	private BigDecimal interest = new BigDecimal(0).setScale(2);
	private BigDecimal servicecharge = new BigDecimal(0).setScale(2);
	private BigDecimal guaranteeFee = new BigDecimal(0).setScale(2);
	private BigDecimal platformFee = new BigDecimal(0).setScale(2);

	/**
	 * 期内滞纳金
	 */
	private BigDecimal lateFee = new BigDecimal(0);
	/**
	 * 期外逾期费
	 */
	private BigDecimal demurrage = new BigDecimal(0);
	/**
	 * 提前还款违约金
	 */
	private BigDecimal penalty = new BigDecimal(0);
	/**
	 * 往期少缴费用
	 */
	private BigDecimal lackFee = null;
	private BigDecimal balance = new BigDecimal(0);
	private BigDecimal deposit = new BigDecimal(0);

	@GetMapping("/business")
	@ApiOperation(value = "获取房贷结清试算基础数据")
	@ResponseBody
	public Result getBusiness(String businessId) {
		BasicBusiness business = basicBusinessService
				.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		if (business == null) {
			return Result.error("500", "business wasn't found");
		}
		BasicRepaymentType basicRepaymentType = basicRepaymentTypeService.selectById(business.getRepaymentTypeId());
		List<BizOutputRecord> bizOutputRecord = bizOutputRecordService.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("current_status", RepayPlanStatus.REPAYED.getName()).orderBy("fact_repay_date",false));
		Date lastRepayDate = null ;
		if (repaymentBizPlanLists!=null&&repaymentBizPlanLists.size()>0) {
			lastRepayDate = repaymentBizPlanLists.get(0).getFactRepayDate();
		}
		/*List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId)
						.orderBy("period").orderBy("plan_item_type"));*/
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("business", business);
		jsonObject.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		jsonObject.put("output", bizOutputRecord);
		jsonObject.put("lastRepayDate", lastRepayDate==null?lastRepayDate:DateUtil.toDateString(lastRepayDate, DateUtil.DEFAULT_FORMAT_DATE));
		return Result.success(jsonObject);
	}

	@GetMapping("/calByPreSettleDate")
	@ApiOperation(value = "根据清算日期获取房贷结清试算数据")
	@ResponseBody
	public Result<ExpenseSettleVO> calByPreSettleDate(String preSettleDate, String businessId, String afterId) {
		if (StringUtil.isEmpty(businessId) || StringUtil.isEmpty(preSettleDate)) {
			return Result.error("500", "参数不能为空！");
		}
		Date settleDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		/*ExpenseSettleVO expenseSettleVO = new ExpenseSettleVO();
		Date settelDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		BasicBusiness basicBusiness = basicBusinessService.selectById(businessId);
		List<Object> businessIds = renewalBusinessService.selectObjs(new EntityWrapper<RenewalBusiness>().eq("original_business_id", businessId).setSqlSelect("renewal_business_id")) ;
		if (businessIds==null) {
			businessIds = new ArrayList<>();
		}
		businessIds.add(businessId);
		RepaymentBizPlan repaymentBizPlan = repaymentBizPlanService
				.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id", businessId));
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectList(
				new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId).orderBy("due_date"));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService.selectList(
				new EntityWrapper<RepaymentBizPlanListDetail>().in("business_id", businessIds).orderBy("period"));

		if (basicBusiness.getRepaymentTypeId() == 2) {
			expenseSettleVO = calXXHB(settelDate, basicBusiness, repaymentBizPlan, planLists, details);
		} else if (basicBusiness.getRepaymentTypeId() == 5) {
			expenseSettleVO = calDEBX(settelDate, basicBusiness, repaymentBizPlan, planLists, details);
		}*/
		ExpenseSettleVO expenseSettleVO = null ;
		try {
			expenseSettleVO = expenseSettleService.cal(businessId, settleDate);
			return Result.success(expenseSettleVO);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("500", e.getMessage()) ;
		}

	}

	
	private Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> getPastPeriod(
			List<RepaymentBizPlanList > currentPeriods, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> map = new LinkedHashMap<>();
		
		for (RepaymentBizPlanList planList : planLists) {
			int res = DateUtil.dateCompare(planList.getDueDate(), currentPeriods.get(0).getDueDate()) ;
			if (res<0) {
				List<RepaymentBizPlanListDetail> list = new ArrayList<>();
				for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {

					if (repaymentBizPlanListDetail.getPlanListId().equals(planList.getPlanListId())) {
						list.add(repaymentBizPlanListDetail);
					}
				}

				map.put(planList, list);
			}
		}

		return map;

	}
	
	private Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> getPastPeriod(
			RepaymentBizPlanList currentPeriod, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> map = new LinkedHashMap<>();
		for (RepaymentBizPlanList planList : planLists) {
			if (planList.getPeriod() < currentPeriod.getPeriod()) {
				List<RepaymentBizPlanListDetail> list = new ArrayList<>();
				for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {

					if (repaymentBizPlanListDetail.getPlanListId().equals(planList.getPlanListId())) {
						list.add(repaymentBizPlanListDetail);
					}
				}

				map.put(planList, list);
			}
		}

		return map;

	}

	private RepaymentBizPlanList getFirstOverduePlanList(
			Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> map) {
		for (Map.Entry<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> e : map.entrySet()) {
			if (e.getKey().getCurrentStatus().equals("逾期")) {
				return e.getKey();
			}
		}
		return null;

	}

	private ExpenseSettleVO calXXHB(final Date settleDate,final BasicBusiness basicBusiness,final RepaymentBizPlan plan,
			final List<RepaymentBizPlanList> planLists,final List<RepaymentBizPlanListDetail> details) {
		callRemoteService(basicBusiness.getBusinessId());
		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
//		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanList> currentPeriods = findCurrentPeriods(settleDate,planLists);
			
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriods, details);
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods = getPastPeriod(currentPeriods,
				planLists, details);
		RepaymentBizPlanList firstOverDuePeriod = getFirstOverduePlanList(pastPeriods);

		principal = plan.getBorrowMoney();
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
//		guaranteeFee = countFee(currentDetails, 40);
		countGuaranteeFee(basicBusiness.getBusinessId(), currentPeriods);
		platformFee = countFee(currentDetails, 50);

		/*
		 * if (firstOverDuePeriod != null) { int daysBeyoungDueDate =
		 * DateUtil.getDiffDays(firstOverDuePeriod.getDueDate(), settleDate); if
		 * (daysBeyoungDueDate > 1) { lateFee = principal.multiply(new
		 * BigDecimal(0.003)).multiply(new BigDecimal(daysBeyoungDueDate)); } }
		 */

		for (RepaymentBizPlanList repaymentBizPlanList : currentPeriods) {
			if (repaymentBizPlanList.getCurrentStatus().equals("逾期")) {
				int daysBeyoungDueDate = DateUtil.getDiffDays(repaymentBizPlanList.getDueDate(), settleDate);
				if (daysBeyoungDueDate > 1) {
					lateFee = principal.multiply(new BigDecimal(0.003)).multiply(new BigDecimal(daysBeyoungDueDate));
				}
			}
		}
		

		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {
			penalty = countPenalty(currentPeriods, details);
			BigDecimal p6 = principal.multiply(new BigDecimal(0.06));
			if (penalty.compareTo(p6)>=0) {
				penalty = p6 ;
			}
		}
		// TODO 咏康:结余，押金转还款，暂时写死零 那个表还没转过来
		/*
		 * deposit = countFee(details, 90); balance = countBalance(currentDetails);
		 */
		lackFeeList = coutLackFee(pastPeriods, settleDate);
		balance = new BigDecimal(transferOfLitigationMapper.queryOverRepayMoneyByBusinessId(basicBusiness.getBusinessId()));
		return initExpenseSettleVO();

	}

	private BigDecimal countBalance(List<RepaymentBizPlanListDetail> details) {
		BigDecimal tmp = new BigDecimal(0);
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
			tmp.add(repaymentBizPlanListDetail.getFactAmount());
		}
		return tmp;
	}

	private void countLackFee(String businessId, BigDecimal lackFee, List<ExpenseSettleLackFeeVO> lackFeeList) {
		lackFeeList = expenseSettleService.listLackFee(businessId);
		for (ExpenseSettleLackFeeVO e : lackFeeList) {
			lackFee = lackFee.add(e.getServicecharge()).add(e.getLateFee());
		}
	}

	private List<ExpenseSettleLackFeeVO> coutLackFee(
			Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods, Date settleDate) {
		List<ExpenseSettleLackFeeVO> list = new ArrayList<>();
		boolean hasCalLateFee = false;
		List<BigDecimal> instersets = new ArrayList<>(100) ;
		BigDecimal firstLateFee = null;
		for (Map.Entry<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> e : pastPeriods.entrySet()) {
			String afterId = e.getKey().getAfterId();
			ExpenseSettleLackFeeVO expenseSettleLackFeeVO = new ExpenseSettleLackFeeVO();
			expenseSettleLackFeeVO.setPeriod(afterId);
			expenseSettleLackFeeVO.setLateFee(new BigDecimal(0));
			expenseSettleLackFeeVO.setServicecharge(new BigDecimal(0));
			expenseSettleLackFeeVO.setPlatFormFee(new BigDecimal(0));
			expenseSettleLackFeeVO.setPrincipal(new BigDecimal(0));
			expenseSettleLackFeeVO.setInterest(new BigDecimal(0));
			for (RepaymentBizPlanListDetail d : e.getValue()) {
				if (d.getPlanItemType().equals(new Integer(30)) && (d.getFactAmount() == null
						|| d.getFactAmount().subtract(d.getPlanAmount()).compareTo(new BigDecimal(0)) < 0)) {

					expenseSettleLackFeeVO.setServicecharge(d.getPlanAmount()
							.subtract(d.getFactAmount() == null ? new BigDecimal(0) : d.getFactAmount()));

				} else if (d.getPlanItemType().equals(new Integer(50)) && (d.getFactAmount() == null
						|| d.getFactAmount().subtract(d.getPlanAmount()).compareTo(new BigDecimal(0)) < 0)) {

					expenseSettleLackFeeVO.setPlatFormFee(d.getPlanAmount()
							.subtract(d.getFactAmount() == null ? new BigDecimal(0) : d.getFactAmount()));

				} /*else if (d.getPlanItemType().equals(new Integer(10)) && (d.getFactAmount() == null
						|| d.getFactAmount().subtract(d.getPlanAmount()).compareTo(new BigDecimal(0)) < 0)) {

					expenseSettleLackFeeVO.setPrincipal(d.getPlanAmount()
							.subtract(d.getFactAmount() == null ? new BigDecimal(0) : d.getFactAmount()));

				}*/ else if (d.getPlanItemType().equals(new Integer(20)) && (d.getFactAmount() == null
						|| d.getFactAmount().subtract(d.getPlanAmount()).compareTo(new BigDecimal(0)) < 0)) {

					String n = String.valueOf(e.getKey().getAfterId().charAt(0));
					if (instersets.isEmpty()||instersets.size()<Integer.valueOf(n)) {
						instersets.add(Integer.valueOf(n)-1, d.getPlanAmount()
						.subtract(d.getFactAmount() == null ? new BigDecimal(0) : d.getFactAmount()));
					}
					expenseSettleLackFeeVO.setInterest(instersets.get(Integer.valueOf(n)-1));
				} else if (d.getPlanItemType().equals(new Integer(60))) {
					if (firstLateFee == null) {
						if (e.getKey() != null) {
							int daysBeyoungDueDate = DateUtil.getDiffDays(e.getKey().getDueDate(), settleDate);
							BigDecimal lateFeeRate = d.getPlanAmount()
									.divide(e.getKey().getOverdueDays().multiply(principal),2,RoundingMode.HALF_DOWN);
							if (daysBeyoungDueDate > 1) {
								firstLateFee = principal.multiply(lateFeeRate)
										.multiply(new BigDecimal(daysBeyoungDueDate));
								expenseSettleLackFeeVO.setLateFee(firstLateFee);
							}
						}
					}
				}
			}

			if (expenseSettleLackFeeVO.getLateFee().compareTo(new BigDecimal(0)) > 0
					|| expenseSettleLackFeeVO.getPlatFormFee().compareTo(new BigDecimal(0)) > 0
					|| expenseSettleLackFeeVO.getInterest().compareTo(new BigDecimal(0)) > 0
					|| expenseSettleLackFeeVO.getServicecharge().compareTo(new BigDecimal(0)) > 0) {

				list.add(expenseSettleLackFeeVO);
			}
		}

		lackFee = new BigDecimal(0);
		for (ExpenseSettleLackFeeVO expenseSettleLackFeeVO : list) {
			lackFee = lackFee.add(expenseSettleLackFeeVO.getLateFee())
					.add(expenseSettleLackFeeVO.getServicecharge())
					.add(expenseSettleLackFeeVO.getPlatFormFee()
					.add(expenseSettleLackFeeVO.getInterest()));
		}
		return list;

	}

	private void countLackFee(Date settleDate, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		boolean hasCalLateFee = false;
		for (RepaymentBizPlanList planList : planLists) {
			ExpenseSettleLackFeeVO expenseSettleLackFeeVO = new ExpenseSettleLackFeeVO();
			BigDecimal lackServiceCharge = new BigDecimal(0);
			BigDecimal lackLateFee = new BigDecimal(0);
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
				if (repaymentBizPlanListDetail.getPlanListId().equals(planList.getPlanListId())) {
					if (repaymentBizPlanListDetail.getPlanItemType() == 30) {
						if (repaymentBizPlanListDetail.getFactAmount() == null || repaymentBizPlanListDetail
								.getFactAmount().compareTo(repaymentBizPlanListDetail.getPlanAmount()) < 0) {
							lackServiceCharge = lackServiceCharge.add(repaymentBizPlanListDetail.getPlanAmount()
									.subtract(repaymentBizPlanListDetail.getFactAmount() != null
											? repaymentBizPlanListDetail.getFactAmount()
											: new BigDecimal(0)));
						}
					}
				}
			}
			if (!hasCalLateFee) {
				if (planList.getCurrentStatus().equals("逾期")) {
					int days = DateUtil.getDiffDays(planList.getDueDate(), settleDate);
					lackLateFee = principal.multiply(new BigDecimal(0.003)).multiply(new BigDecimal(days));
					hasCalLateFee = true;
				}
			}
			expenseSettleLackFeeVO.setLateFee(lackLateFee);
			expenseSettleLackFeeVO.setServicecharge(lackServiceCharge);
			expenseSettleLackFeeVO.setPeriod(planList.getAfterId());
			lackFee = lackFee.add(lackLateFee).add(lackServiceCharge);
			lackFeeList.add(expenseSettleLackFeeVO);
		}
	}

	class MapKeyComparator implements Comparator<Integer> {
		@Override
		public int compare(Integer str1, Integer str2) {

			return str1.compareTo(str2);
		}
	}

	private List<RepaymentBizPlanList> findCurrentPeriods(Date settleDate, List<RepaymentBizPlanList> planLists) {
		List<RepaymentBizPlanList> list = new ArrayList<>();
		RepaymentBizPlanList planList = findCurrentPeriod(settleDate, planLists);
		for (RepaymentBizPlanList repaymentBizPlanList : planLists) {
			if (planList.getPeriod().equals(repaymentBizPlanList.getPeriod())) {
				list.add(repaymentBizPlanList);
			}
		}
		
		return list;
	}
	private RepaymentBizPlanList findCurrentPeriod(Date settleDate, List<RepaymentBizPlanList> planLists) {
		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
		int diff = DateUtil.getDiffDays(settleDate, finalPeriod.getDueDate());
		RepaymentBizPlanList currentPeriod = null;
		
		// 提前还款结清
		if (diff > 0) {
			RepaymentBizPlanList temp = new RepaymentBizPlanList();
			temp.setDueDate(settleDate);
			temp.setBusinessId("temp");
			// 把提前结清的日期放进PlanLists一起比较
			planLists.add(temp);
			planLists.sort(
					(RepaymentBizPlanList a1, RepaymentBizPlanList a2) -> a1.getDueDate().compareTo(a2.getDueDate()));
			for (int i = 0; i < planLists.size(); i++) {
				if (planLists.get(i).getBusinessId().equals("temp")) {
					currentPeriod = planLists.get(i + 1);
				}
			}

			// 筛选temp记录
			for (Iterator<RepaymentBizPlanList> it = planLists.iterator(); it.hasNext();) {
				RepaymentBizPlanList pList = it.next();
				if (pList.getBusinessId().equals("temp")) {
					it.remove();
				}
			}

		} else {
			// 期外
			currentPeriod = finalPeriod;
		}

		
		
		return currentPeriod;
	}

	private List<RepaymentBizPlanListDetail> findCurrentDetail(List<RepaymentBizPlanList> currentPeriods,
			List<RepaymentBizPlanListDetail> details) {
		List<RepaymentBizPlanListDetail> list = new ArrayList<>();
		
		for (RepaymentBizPlanList repaymentBizPlanList : currentPeriods) {
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
				if (repaymentBizPlanListDetail.getPlanListId().equals(repaymentBizPlanList.getPlanListId())) {
					list.add(repaymentBizPlanListDetail);
				}
			}
		}
		
		
		return list;
	}
	
	private List<RepaymentBizPlanListDetail> findCurrentDetail(RepaymentBizPlanList currentPeriod,
			List<RepaymentBizPlanListDetail> details) {
		List<RepaymentBizPlanListDetail> list = new ArrayList<>();
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
			if (repaymentBizPlanListDetail.getPlanListId().equals(currentPeriod.getPlanListId())) {
				list.add(repaymentBizPlanListDetail);
			}
		}
		return list;
	}

	private BigDecimal countFee(List<RepaymentBizPlanListDetail> currentDetails, Integer feeType) {
		if (feeType == null) {
			return null;
		}
		BigDecimal tmp = new BigDecimal(0);
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : currentDetails) {
			if (repaymentBizPlanListDetail.getPlanItemType().equals(feeType)) {
				tmp = tmp.add(repaymentBizPlanListDetail.getPlanAmount() == null ? new BigDecimal(0)
						: repaymentBizPlanListDetail.getPlanAmount());
			}
		}
		return tmp;
	}

	private BigDecimal countPenalty(List<RepaymentBizPlanList> currentPlanLists,
			List<RepaymentBizPlanListDetail> planListDetails) {
		BigDecimal tmp = new BigDecimal(0);
		int count = 0 ;
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : planListDetails) {
				if (repaymentBizPlanListDetail.getPeriod() > currentPlanLists.get(0).getPeriod()
						&& repaymentBizPlanListDetail.getPlanItemType() == 30) {
					logger.info(++count+"");
					logger.info(repaymentBizPlanListDetail.getPlanAmount().toString());
					tmp = tmp.add(repaymentBizPlanListDetail.getPlanAmount());
				}
			}
		
		
		
		return tmp;
	}
	
	private BigDecimal countPenalty(RepaymentBizPlanList currentPlanList,
			List<RepaymentBizPlanListDetail> planListDetails) {
		BigDecimal tmp = new BigDecimal(0);
		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : planListDetails) {
			if (repaymentBizPlanListDetail.getPeriod() > currentPlanList.getPeriod()
					&& repaymentBizPlanListDetail.getPlanItemType() == 30) {
				tmp = tmp.add(repaymentBizPlanListDetail.getPlanAmount());
			}
		}
		
		
		return tmp;
	}

	private BigDecimal countOutPutMoney(String businessId) {
		List<BizOutputRecord> bizOutputRecords = bizOutputRecordService.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		BigDecimal base = new BigDecimal(0);
		for (BizOutputRecord bizOutputRecord : bizOutputRecords) {
			base = base.add(bizOutputRecord.getFactOutputMoney());
		}
		return base;

	}

	private BigDecimal countPaidPrincipal(List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		BigDecimal tmp = new BigDecimal(0);
		for (RepaymentBizPlanList planList : planLists) {
			if (planList.getCurrentStatus().equals("已还款")) {
				for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
					if (repaymentBizPlanListDetail.getPlanItemType() == 10) {
						tmp = tmp.add(repaymentBizPlanListDetail.getPlanAmount());
					}
				}
			}
		}
		return tmp;
	}
	
	public ResponseData callRemoteService(String businessId) throws RuntimeException {
		logger.info("调用callRemoteService");
		if (xindaiAplUrlUrl==null) {
			logger.error("xindaiAplUrlUrl==null!!!");
			return null ;
		}
		logger.info("xindaiAplUrlUrl:"+xindaiAplUrlUrl);
		DESC desc = new DESC();
		RequestData requestData = new RequestData();
		requestData.setMethodName("AfterLoanRepayment_GetFeeList");
		JSONObject data = new JSONObject() ;
		data.put("businessId", businessId);
		requestData.setData(data.toJSONString());
		logger.info("原始数据-开始");
		logger.info(JSON.toJSONString(requestData));
		logger.info("原始数据-结束");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = desc.Encryption(encryptStr);
		logger.info("请求数据-开始");
		logger.info(encryptStr);
		logger.info("请求数据-结束");
		XindaiService xindaiService = Feign.builder().target(XindaiService.class, xindaiAplUrlUrl);
		String response = xindaiService.dod(encryptStr);

		// 返回数据解密
		ResponseEncryptData resp = JSON.parseObject(response, ResponseEncryptData.class);
		String decryptStr = desc.Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		
		logger.info("信贷返回数据解密-开始");
		logger.info(JSON.toJSONString(respData));
		logger.info("信贷返回数据解密-结束");
		return respData ;
	}
	
	private void countGuaranteeFee(String businessId,List<RepaymentBizPlanList> currentPeriods) {
		BigDecimal g = new BigDecimal(0);
		JSONArray jsonArray = JSONArray.parseArray(callRemoteService(businessId).getData());
		for (Object object : jsonArray) {
			JSONObject j = (JSONObject) object ;
			String feeTypeName = j.getString("fee_type_name");
			BigDecimal feeValue =  j.getBigDecimal("fee_value");
			BigDecimal factFeeValue = j.getBigDecimal("fact_fee_value");
			Integer isOneTimeCharge = j.getInteger("is_one_time_charge");
			if (feeTypeName.equals("担保公司费用")&&isOneTimeCharge.equals(new Integer(1))) {
				if (factFeeValue==null) {
					guaranteeFee = feeValue ;
				}else {
					guaranteeFee = feeValue.subtract(factFeeValue) ;
				}
			}
		}
	}

	private ExpenseSettleVO calDEBX(final Date settleDate,final BasicBusiness basicBusiness,final RepaymentBizPlan plan,final
			List<RepaymentBizPlanList> planLists,final List<RepaymentBizPlanListDetail> details) {
		
		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
//		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanList> currentPeriods = findCurrentPeriods(settleDate, planLists);
		
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriods, details);
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods = getPastPeriod(currentPeriods,
				planLists, details);
		RepaymentBizPlanList firstOverDuePeriod = getFirstOverduePlanList(pastPeriods);

		principal = plan.getBorrowMoney().subtract(countPaidPrincipal(planLists, currentDetails));
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
//		guaranteeFee = countFee(currentDetails, 40);
		countGuaranteeFee(basicBusiness.getBusinessId(), currentPeriods);
		platformFee = countFee(currentDetails, 50);

		/*if (firstOverDuePeriod != null) {
			int daysBeyoungDueDate = DateUtil.getDiffDays(firstOverDuePeriod.getDueDate(), settleDate);
			if (daysBeyoungDueDate > 1) {
				lateFee = principal.multiply(new BigDecimal(0.003)).multiply(new BigDecimal(daysBeyoungDueDate));
			}
		}*/

		for (RepaymentBizPlanList currentPeriod : currentPeriods) {
			if (currentPeriod.getCurrentStatus().equals("逾期")) {
				int daysBeyoungDueDate = DateUtil.getDiffDays(currentPeriod.getDueDate(), settleDate);
				if (daysBeyoungDueDate > 1) {
					lateFee = principal.multiply(new BigDecimal(0.001)).multiply(new BigDecimal(daysBeyoungDueDate));
				}
			}
		}
		
		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {

			penalty = countPenalty(currentPeriods, details);
		}
		lackFeeList = coutLackFee(pastPeriods,settleDate);
		balance = new BigDecimal(transferOfLitigationMapper.queryOverRepayMoneyByBusinessId(basicBusiness.getBusinessId()));
		return initExpenseSettleVO();

	}

	private ExpenseSettleVO initExpenseSettleVO() {
		ExpenseSettleVO expenseSettleVO = new ExpenseSettleVO();
		expenseSettleVO.setPrincipal(principal);
		expenseSettleVO.setInterest(interest);
		expenseSettleVO.setLateFee(lateFee);
		expenseSettleVO.setServicecharge(servicecharge);
		expenseSettleVO.setGuaranteeFee(guaranteeFee);
		expenseSettleVO.setPlatformFee(platformFee);
		expenseSettleVO.setList(lackFeeList);
		expenseSettleVO.setLackFee(lackFee == null ? new BigDecimal(0) : lackFee);
		expenseSettleVO.setBalance(balance);
		expenseSettleVO.setDeposit(deposit);
		expenseSettleVO.setPenalty(penalty);
		expenseSettleVO.setDemurrage(demurrage);
		expenseSettleVO.setBalance(balance);
		return expenseSettleVO;
	}

	private BigDecimal countUnpayPrincipal(List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		BigDecimal tmp = new BigDecimal(0);
		for (RepaymentBizPlanList planList : planLists) {
			if (!planList.getCurrentStatus().equals("已还款")) {
				for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : details) {
					if (repaymentBizPlanListDetail.getPlanItemType() == 10) {
						tmp = tmp.add(repaymentBizPlanListDetail.getPlanAmount());
					}
				}
			}
		}
		return tmp;
	}
}
