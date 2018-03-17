package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/expenseSettle")
public class ExpenseSettleController {

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
	private BigDecimal lackFee = new BigDecimal(0);
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
		ExpenseSettleVO expenseSettleVO = expenseSettleService.sum(businessId);
		Date settelDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		BasicBusiness basicBusiness = basicBusinessService.selectById(businessId);
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListService
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId));

		if (basicBusiness.getRepaymentTypeId() == 2) {
			expenseSettleVO = calXXHB(settelDate, basicBusiness, planLists, details);
		} else if (basicBusiness.getRepaymentTypeId() == 5) {
			expenseSettleVO = calDEBX(settelDate, basicBusiness, planLists, details);
		}

		return Result.success(expenseSettleVO);

	}

	private ExpenseSettleVO calXXHB(Date settleDate, BasicBusiness basicBusiness, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {

		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriod, details);
		principal = basicBusiness.getBorrowMoney();
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
		guaranteeFee = countFee(currentDetails, 40);
		platformFee = countFee(currentDetails, 50);
		/*
		 * if (currentPeriod.getCurrentStatus().equals("逾期")) {
		 * 
		 * }
		 */
		int daysBeyoungDueDate = DateUtil.getDiffDays(currentPeriod.getDueDate(), settleDate);
		if (daysBeyoungDueDate > 1) {
			lateFee = principal.multiply(new BigDecimal(0.001)).multiply(new BigDecimal(daysBeyoungDueDate));
		}

		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {
			penalty = countPenalty(currentPeriod, details);
		}
		// countLackFee(basicBusiness.getBusinessId(), lackFee, lackFeeList);'
		countLackFee(settleDate, planLists, currentDetails);
		// TODO 咏康:结余，押金转还款，暂时写死零 那个表还没转过来
		/*
		 * deposit = countFee(details, 90); balance = countBalance(currentDetails);
		 */
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
									.subtract(repaymentBizPlanListDetail.getFactAmount()));
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

	/**
	 * @author 王继光 2018年3月15日 下午2:39:35
	 * @param args
	 */
	/*public static void main(String[] args) {
		class MapKeyComparator implements Comparator<Integer> {
			@Override
			public int compare(Integer str1, Integer str2) {

				return str1.compareTo(str2);
			}
		}

		List<Date> dates = new ArrayList<>();
		dates.add(DateUtil.getDate("2018-01-15"));
		dates.add(DateUtil.getDate("2018-02-15"));
		dates.add(DateUtil.getDate("2018-03-15"));
		dates.add(DateUtil.getDate("2018-04-15"));
		dates.add(DateUtil.getDate("2018-05-15"));
		dates.add(DateUtil.getDate("2018-06-15"));
		Date settleDate = DateUtil.getDate("2018-06-16");
		Map<Integer, Date> map = new TreeMap<>(new MapKeyComparator());
		for (Date date : dates) {
			int diff = DateUtil.getDiffDays(settleDate, date);
			System.out.println(diff);
		}
	}*/

	class MapKeyComparator implements Comparator<Integer> {
		@Override
		public int compare(Integer str1, Integer str2) {

			return str1.compareTo(str2);
		}
	}

	private RepaymentBizPlanList findCurrentPeriod(Date settleDate, List<RepaymentBizPlanList> planLists) {
		for (int i = 0; i < planLists.size(); i++) {
			RepaymentBizPlanList planList = planLists.get(i);
			int diff = DateUtil.getDiffDays(settleDate, planList.getDueDate());
			if (diff == 0) {
				if (planList.getCurrentStatus().equals("还款中")) {
					return planList;
				} else {
					RepaymentBizPlanList nextPlanList = planLists.get(i + 1);
					if (nextPlanList == null) {
						return planList;
					} else {
						return nextPlanList;
					}
				}
			}

			if (diff < 0) {
				return planList;
			}

			if (diff > 0 && i == planLists.size() - 1) {
				return planList;
			}

		}
		return null;
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

	private ExpenseSettleVO calDEBX(Date settleDate, BasicBusiness basicBusiness, List<RepaymentBizPlanList> planLists,
			List<RepaymentBizPlanListDetail> details) {
		RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
		RepaymentBizPlanList currentPeriod = findCurrentPeriod(settleDate, planLists);
		List<RepaymentBizPlanListDetail> currentDetails = findCurrentDetail(currentPeriod, details);

		principal = countUnpayPrincipal(planLists, details);
		interest = countFee(currentDetails, 20);
		servicecharge = countFee(currentDetails, 30);
		guaranteeFee = countFee(currentDetails, 40);
		platformFee = countFee(currentDetails, 50);

		int daysBeyoungDueDate = DateUtil.getDiffDays(currentPeriod.getDueDate(), settleDate);
		if (daysBeyoungDueDate > 1) {
			lateFee = principal.multiply(new BigDecimal(0.001)).multiply(new BigDecimal(daysBeyoungDueDate));
		}

		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			demurrage = principal.multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod));
		} else if (daysBeyoungFinalPeriod == 0) {

		} else if (daysBeyoungFinalPeriod < 0) {
			/*
			 * List<BizOutputRecord> outputRecords = bizOutputRecordService.selectList(new
			 * EntityWrapper<BizOutputRecord>().eq("business_id",
			 * basicBusiness.getBusinessId()).orderBy("fact_output_date")); BizOutputRecord
			 * firstOutPutRecord = outputRecords.get(0);
			 * 
			 * if (DateUtil.dateCompare(firstOutPutRecord.getFactOutputDate(),
			 * DateUtil.getDate("2017-03-01"))>=0) {
			 * 
			 * }
			 */

			penalty = countPenalty(currentPeriod, details);
		}
		
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
		expenseSettleVO.setLackFee(lackFee);
		expenseSettleVO.setBalance(balance);
		expenseSettleVO.setDeposit(deposit);
		expenseSettleVO.setPenalty(penalty);
		expenseSettleVO.setDemurrage(demurrage);
		return expenseSettleVO ;
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
