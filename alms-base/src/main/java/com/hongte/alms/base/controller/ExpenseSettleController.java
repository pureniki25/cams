package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicRepaymentType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicRepaymentTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/expenseSettle")
public class ExpenseSettleController {

	private Logger logger = LoggerFactory.getLogger(ExpenseSettleController.class);

	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;

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

	private List<ExpenseSettleLackFeeVO> lackFeeList = new ArrayList<>();
	private BigDecimal principal = new BigDecimal(0);
	private BigDecimal interest = new BigDecimal(0);
	private BigDecimal servicecharge = new BigDecimal(0);
	private BigDecimal guaranteeFee = new BigDecimal(0);
	private BigDecimal platformFee = new BigDecimal(0);
	private BigDecimal lateFee = new BigDecimal(0);
	private BigDecimal demurrage = new BigDecimal(0);
	private BigDecimal penalty = new BigDecimal(0);
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
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId)
						.orderBy("period").orderBy("plan_item_type"));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("business", business);
		jsonObject.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		jsonObject.put("output", bizOutputRecord);
		return Result.success(jsonObject);
	}

	@GetMapping("/calByPreSettleDate")
	@ApiOperation(value = "根据清算日期获取房贷结清试算数据")
	@ResponseBody
	public Result<ExpenseSettleVO> calByPreSettleDate(String preSettleDate, String businessId, String afterId) {
		if (StringUtil.isEmpty(businessId) || StringUtil.isEmpty(preSettleDate)) {
			return Result.error("500", "参数不能为空！");
		}
		ExpenseSettleVO expenseSettleVO = expenseSettleService.sum(businessId);
		Date settelDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		BasicBusiness basicBusiness = basicBusinessService.selectById(businessId);
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectList(
				new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).orderBy("due_date"));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService.selectList(
				new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId).orderBy("period"));

		if (basicBusiness.getRepaymentTypeId() == 2) {
			expenseSettleVO = calXXHB(settelDate, basicBusiness, planLists, details);
		} else if (basicBusiness.getRepaymentTypeId() == 5) {
			expenseSettleVO = calDEBX(settelDate, basicBusiness, planLists, details);
		}

		return Result.success(expenseSettleVO);

	}

	private Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> getPastPeriod(
			RepaymentBizPlanList currentPeriod, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> map = new TreeMap<>();
		for (RepaymentBizPlanList planList : planLists) {
			if (planList.getPeriod()<currentPeriod.getPeriod()) {
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
	
	private RepaymentBizPlanList getFirstOverduePlanList(Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> map) {
		for (Map.Entry<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> e :map.entrySet()) {
			if (e.getKey().getCurrentStatus().equals("逾期")) {
				return e.getKey();
			}
		}
		return null;
		
	}

	private ExpenseSettleVO calXXHB(Date settleDate, BasicBusiness basicBusiness, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {

		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriod, details);
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods = getPastPeriod(currentPeriod, planLists, currentDetails);
		RepaymentBizPlanList firstOverDuePeriod = getFirstOverduePlanList(pastPeriods);
		
		principal = countOutPutMoney(basicBusiness.getBusinessId());
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
		guaranteeFee = countFee(currentDetails, 40);
		platformFee = countFee(currentDetails, 50);
		
		if (firstOverDuePeriod!=null) {
			int daysBeyoungDueDate = DateUtil.getDiffDays(firstOverDuePeriod.getDueDate(), settleDate);
			if (daysBeyoungDueDate > 1) {
				lateFee = principal.multiply(new BigDecimal(0.003)).multiply(new BigDecimal(daysBeyoungDueDate));
			}
		}
		

		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {
			penalty = countPenalty(currentPeriod, details);
		}
		// TODO 咏康:结余，押金转还款，暂时写死零 那个表还没转过来
		/*
		 * deposit = countFee(details, 90); balance = countBalance(currentDetails);
		 */
		lackFeeList = coutLackFee(pastPeriods);
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

	private List<ExpenseSettleLackFeeVO> coutLackFee(Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods){
		List<ExpenseSettleLackFeeVO> list = new ArrayList<>();
		ExpenseSettleLackFeeVO expenseSettleLackFeeVO = new ExpenseSettleLackFeeVO();
		boolean hasCalLateFee = false ;
		for (Map.Entry<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> e:pastPeriods.entrySet()) {
			String afterId = e.getKey().getAfterId();
			expenseSettleLackFeeVO.setPeriod(afterId);
			expenseSettleLackFeeVO.setLateFee(new BigDecimal(0));
			expenseSettleLackFeeVO.setServicecharge(new BigDecimal(0));
			for (RepaymentBizPlanListDetail d : e.getValue()) {
				BigDecimal servicecharge = expenseSettleLackFeeVO.getServicecharge() ;
				if (d.getPlanItemType().equals(new Integer(30))) {
					servicecharge = servicecharge.add(d.getPlanAmount());
				}else if (d.getPlanItemType().equals(new Integer(50))) {
					servicecharge = servicecharge.add(d.getPlanAmount());
				}
				expenseSettleLackFeeVO.setServicecharge(servicecharge);
			}
			if (!hasCalLateFee&&lateFee!=null) {
				expenseSettleLackFeeVO.setLateFee(lackFee);
				hasCalLateFee=true;
			}
			list.add(expenseSettleLackFeeVO);
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

	private ExpenseSettleVO calDEBX(Date settleDate, BasicBusiness basicBusiness, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriod, details);
		Map<RepaymentBizPlanList, List<RepaymentBizPlanListDetail>> pastPeriods = getPastPeriod(currentPeriod, planLists, currentDetails);
		RepaymentBizPlanList firstOverDuePeriod = getFirstOverduePlanList(pastPeriods);
		
		principal = countOutPutMoney(basicBusiness.getBusinessId())
				.subtract(countPaidPrincipal(planLists, currentDetails));
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
		guaranteeFee = countFee(currentDetails, 40);
		platformFee = countFee(currentDetails, 50);

		if (firstOverDuePeriod!=null) {
			int daysBeyoungDueDate = DateUtil.getDiffDays(firstOverDuePeriod.getDueDate(), settleDate);
			if (daysBeyoungDueDate > 1) {
				lateFee = principal.multiply(new BigDecimal(0.003)).multiply(new BigDecimal(daysBeyoungDueDate));
			}
		}

		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {

			penalty = countPenalty(currentPeriod, details);
		}
		lackFeeList = coutLackFee(pastPeriods);
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
		expenseSettleVO.setLackFee(lackFee==null?new BigDecimal(0):lackFee);
		expenseSettleVO.setBalance(balance);
		expenseSettleVO.setDeposit(deposit);
		expenseSettleVO.setPenalty(penalty);
		expenseSettleVO.setDemurrage(demurrage);
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
