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


@Controller
@RequestMapping("/expenseSettle")
public class ExpenseSettleController {
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService ;
	
	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService ;
	
	@Autowired
	@Qualifier("BasicRepaymentTypeService")
	BasicRepaymentTypeService basicRepaymentTypeService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService ;
	
	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService ;
	
	@Autowired
	@Qualifier("ExpenseSettleService")
	ExpenseSettleService expenseSettleService ;
	
	@GetMapping("/business")
	@ResponseBody
	public Result getBusiness(String businessId) {
		BasicBusiness business = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
		if (business==null) {
			return Result.error("500", "business wasn't found");
		}
		BasicRepaymentType basicRepaymentType = basicRepaymentTypeService.selectById(business.getRepaymentTypeId());
		List<BizOutputRecord > bizOutputRecord = bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId).orderBy("period").orderBy("plan_item_type"));
		JSONObject jsonObject = new JSONObject() ;
		jsonObject.put("business", business);
		jsonObject.put("repaymentType", basicRepaymentType.getRepaymentTypeName());
		jsonObject.put("output", bizOutputRecord);
		jsonObject.put("repaymentDetails", repaymentBizPlanListDetails);
		ExpenseSettleVO expenseSettleVO = expenseSettleService.sum(businessId);
		jsonObject.put("detail", expenseSettleVO);
		return Result.success(jsonObject) ;
	}
	
	@GetMapping("/calByPreSettleDate")
	@ResponseBody
	public Result<ExpenseSettleVO> calByPreSettleDate(String preSettleDate,String businessId,String afterId) {
		ExpenseSettleVO expenseSettleVO = expenseSettleService.sum(businessId);
		Date settelDate = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");
		BasicBusiness basicBusiness = basicBusinessService.selectById(businessId);
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId));
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId));
		
		if (basicBusiness.getRepaymentTypeId()==2) {
			expenseSettleVO = calXXHB(settelDate,basicBusiness, planLists, details);
		}else if (basicBusiness.getRepaymentTypeId()==5) {
			expenseSettleVO = calDEBX(settelDate,basicBusiness, planLists, details);
		}
		
		return Result.success(expenseSettleVO) ;
		
	}
	
	private ExpenseSettleVO calXXHB(Date settleDate,BasicBusiness basicBusiness,List<RepaymentBizPlanList> planLists,List<RepaymentBizPlanListDetail> details) {
		List<ExpenseSettleLackFeeVO> list = new ArrayList<>();
		BigDecimal principal = new BigDecimal(0);
		BigDecimal interest = new BigDecimal(0);
		BigDecimal servicecharge = new BigDecimal(0);
		BigDecimal guaranteeFee = new BigDecimal(0);
		BigDecimal platformFee = new BigDecimal(0);
		BigDecimal lateFee = new BigDecimal(0);
		BigDecimal demurrage = new BigDecimal(0);
		BigDecimal penalty = new BigDecimal(0);
		BigDecimal lackFee = new BigDecimal(0);
		BigDecimal balance = new BigDecimal(0);
		principal = basicBusiness.getBorrowMoney();
		
		RepaymentBizPlanList downLimit = null ;
		RepaymentBizPlanList upLimit = null ;
		
		
		return null;
		
	}
	public static void main(String[] args) {
		class MapKeyComparator implements Comparator<Integer>{
		    @Override
		    public int compare(Integer str1, Integer str2) {
		        
		        return str1.compareTo(str2);
		    }
		}
		
		List<Date> dates = new ArrayList<>() ;
		dates.add(DateUtil.getDate("2018-01-15"));
		dates.add(DateUtil.getDate("2018-02-15"));
		dates.add(DateUtil.getDate("2018-03-15"));
		dates.add(DateUtil.getDate("2018-04-15"));
		dates.add(DateUtil.getDate("2018-05-15"));
		dates.add(DateUtil.getDate("2018-06-15"));
		Date settleDate = DateUtil.getDate("2018-10-18");
		Map<Integer, Date> map = new TreeMap<>(new MapKeyComparator()) ;
		for (Date date : dates) {
			int diff = DateUtil.getDiffDays(date, settleDate);
			diff = Math.abs(diff) ;
			map.put(diff, date);
		}
		for (Map.Entry<Integer, Date> m : map.entrySet()) {
			System.out.println(DateUtil.formatDate(m.getValue()));
		}
	}
	
	class MapKeyComparator implements Comparator<Integer>{
	    @Override
	    public int compare(Integer str1, Integer str2) {
	        
	        return str1.compareTo(str2);
	    }
	}
	
	private List<RepaymentBizPlanList> findStage(Date settleDate,List<RepaymentBizPlanList> planLists) {
		Map<Integer, RepaymentBizPlanList> map = new TreeMap<>(new MapKeyComparator()) ;
		for (RepaymentBizPlanList repaymentBizPlanList : planLists) {
			Date dueDate = repaymentBizPlanList.getDueDate() ;
			int diff = DateUtil.getDiffDays(dueDate, settleDate);
			diff = Math.abs(diff) ;
			map.put(diff, repaymentBizPlanList);
		}
		List<RepaymentBizPlanList> stage = new ArrayList<>() ;
		for (Map.Entry<Integer, RepaymentBizPlanList> m : map.entrySet()) {
			stage.add(m.getValue());
		}
		return stage;
		
	}
	
	private RepaymentBizPlanList findCurrentPeriod(List<RepaymentBizPlanList> sortedLists) {
		int planListSize = sortedLists.size() ;
		if (planListSize>=2) {
			RepaymentBizPlanList close1 = sortedLists.get(0);
			RepaymentBizPlanList close2 = sortedLists.get(1);
		}
		return null ;
	}
	
	private ExpenseSettleVO calDEBX(Date settleDate,BasicBusiness basicBusiness,List<RepaymentBizPlanList> planLists,List<RepaymentBizPlanListDetail> details) {
		List<ExpenseSettleLackFeeVO> list = new ArrayList<>();
		BigDecimal principal = new BigDecimal(0);
		BigDecimal interest = new BigDecimal(0);
		BigDecimal servicecharge = new BigDecimal(0);
		BigDecimal guaranteeFee = new BigDecimal(0);
		BigDecimal platformFee = new BigDecimal(0);
		BigDecimal lateFee = new BigDecimal(0);
		BigDecimal demurrage = new BigDecimal(0);
		BigDecimal penalty = new BigDecimal(0);
		BigDecimal lackFee = new BigDecimal(0);
		BigDecimal balance = new BigDecimal(0);
		return null;
		
	}
}
