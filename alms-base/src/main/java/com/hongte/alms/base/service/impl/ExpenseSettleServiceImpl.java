/**
 * 
 */
package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.BizOutputRecordMapper;
import com.hongte.alms.base.mapper.ExpenseSettleMapper;
import com.hongte.alms.base.mapper.RenewalBusinessMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.TransferOfLitigationMapper;
import com.hongte.alms.base.service.ExpenseSettleService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.ExpenseSettleLackFeeVO;
import com.hongte.alms.base.vo.module.ExpenseSettleRepaymentPlanListVO;
import com.hongte.alms.base.vo.module.ExpenseSettleRepaymentPlanVO;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

import feign.Feign;

/**
 * @author 王继光 2018年3月12日 下午5:10:46
 */
@Service("ExpenseSettleService")
@RefreshScope
public class ExpenseSettleServiceImpl implements ExpenseSettleService {
	private Logger logger = LoggerFactory.getLogger(ExpenseSettleServiceImpl.class) ;
	@Autowired
	BasicBusinessMapper basicBusinessMapper;
	@Autowired
	RepaymentBizPlanMapper repaymentBizPlanMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;

	@Autowired
	RenewalBusinessMapper renewalBusinessMapper ;
	
	@Autowired
	BizOutputRecordMapper bizOutputRecordMapper;
	@Autowired
	ExpenseSettleMapper expenseSettleMapper ;
	
	@Autowired
	TransferOfLitigationMapper transferOfLitigationMapper ;
	@Value("${bmApi.apiUrl}")
	String xindaiAplUrlUrl;

	@Override
	public ExpenseSettleVO cal(String preSettleDate, String businessId) {
		BasicBusiness business = basicBusinessMapper.selectById(businessId);
		if (business == null) {
			throw new RuntimeException("业务不存在");
		}

		List<BizOutputRecord> bizOutputRecords = bizOutputRecordMapper.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", false));

		if (bizOutputRecords == null) {
			throw new RuntimeException("业务尚未出款");
		}

		Date outPutDate = bizOutputRecords.get(0).getFactOutputDate();

		/*
		 * 2017.3.22前出款或展期不收取； 2017.3.22-2017.6.5之间出款或展期的前置收取； 2017.6.5之后出款或展期的后置收取；
		 */

		String serviceChargeRule = "";
		if (outPutDate.before(DateUtil.getDate("2017-03-22", "yyyy-MM-dd"))) {
			serviceChargeRule = "";
		} else if (outPutDate.after(DateUtil.getDate("2017-03-22", "yyyy-MM-dd"))
				&& outPutDate.before(DateUtil.getDate("2017-06-05", "yyyy-MM-dd"))) {
			serviceChargeRule = "pre";
		} else {
			serviceChargeRule = "next";
		}

		BigDecimal businessRate = business.getBorrowRate();
		int rateUnit = business.getBorrowRateUnit();

		if (rateUnit == 1) {
			businessRate = businessRate.divide(new BigDecimal(12), 2,RoundingMode.HALF_UP);
		}

		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId)
						.le("due_date", preSettleDate).orderBy("due_date", false));

		List<RepaymentBizPlanList> residualRepaymentBizPlanLists = repaymentBizPlanListMapper
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId)
						.gt("due_date", preSettleDate).orderBy("due_date", false));

		if (repaymentBizPlanLists == null) {
			throw new RuntimeException("业务不存在还款计划");
		}

		Date lastPeriod = repaymentBizPlanLists.get(0).getDueDate();
		Date preSettle = DateUtil.getDate(preSettleDate, "yyyy-MM-dd");

		int differ = 0;
		differ = DateUtil.getDiffDays(lastPeriod, preSettle);

		List<String> list = new ArrayList<>();
		BigDecimal principal = new BigDecimal(0);
		BigDecimal interest = new BigDecimal(0);
		BigDecimal servicecharge = new BigDecimal(0);
		BigDecimal guaranteeFee = new BigDecimal(0);
		BigDecimal platformFee = new BigDecimal(0);
		BigDecimal lateFee = new BigDecimal(0);
		BigDecimal demurrage = new BigDecimal(0);
		BigDecimal penalty = new BigDecimal(0);
		BigDecimal lackFee = new BigDecimal(0);

		for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
			list.add(repaymentBizPlanList.getPlanListId());
		}
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id", list)
						.eq("business_id", businessId).orderBy("period").orderBy("plan_item_type"));

		/*
		 * 1 到期还本息 2 每月付息到期还本 5 等额本息 9 分期还本付息 500 分期还本付息5年 1000 分期还本付息10年
		 */
		if (business.getRepaymentTypeId() == 2) {
			/*
			 * 2017.6.5之前出款的不收取；
			 * 2017.6.5—至今，收取标准：剩余本金*0.5%*剩余还款期数。(注：实际中如有业务与上述一般情况非一致的，以实际为准）
			 */
			boolean penaltyRule = false;
			if (outPutDate.before(DateUtil.getDate("2017-06-05", "yyyy-MM-dd"))) {

			} else {
				penaltyRule = true;
			}

			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
				if (repaymentBizPlanListDetail.getPlanItemName().equals("利息")) {
					interest = interest.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}

				if (repaymentBizPlanListDetail.getPlanItemName().equals("本金")) {
					principal = principal.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}

				if (repaymentBizPlanListDetail.getPlanItemName().equals("服务费")) {
					servicecharge = servicecharge.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}

				if (repaymentBizPlanListDetail.getPlanItemName().equals("滞纳金")) {
					lateFee = lateFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
				if (repaymentBizPlanListDetail.getPlanItemName().equals("平台费")) {
					platformFee = platformFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
				if (repaymentBizPlanListDetail.getPlanItemName().equals("担保费")) {
					guaranteeFee = guaranteeFee.add(repaymentBizPlanListDetail.getPlanAmount()
							.subtract(repaymentBizPlanListDetail.getFactAmount()));
				}
			}

			if (serviceChargeRule.equals("")) {

			} else if (serviceChargeRule.equals("pre")) {

			} else {

			}
			BigDecimal calByMonth = businessRate.divide(new BigDecimal(100), 2,RoundingMode.HALF_UP).multiply(principal);
			BigDecimal calByDay = new BigDecimal(0.001).multiply(new BigDecimal(differ)).multiply(principal);

			if (differ > 10) {
				interest = interest.add(calByMonth);
			} else {
				if (calByDay.compareTo(calByMonth) > 0) {
					interest = interest.add(calByMonth);
				} else {
					interest = interest.add(calByDay);
				}

			}

			if (penaltyRule) {
				penalty = principal.multiply(new BigDecimal(0.005))
						.multiply(new BigDecimal(residualRepaymentBizPlanLists.size()));
			}

		} else if (business.getRepaymentTypeId() == 5) {
			/*
			 * 2017.3之前不收取 2017.3—2017.12.4，收取标准：剩余借款本金 * 分公司服务费率 *
			 * 服务费的剩余还款期数，但不超过剩余本金的6%；超过6% 按6% 收取
			 */
			boolean penaltyRule = false;
			if (outPutDate.before(DateUtil.getDate("2017-06-05", "yyyy-MM-dd"))) {

			} else {
				penaltyRule = true;
			}
		}

		return null;
	}

	@Override
	public ExpenseSettleVO sum( String businessId) {
		BasicBusiness business = basicBusinessMapper.selectById(businessId);
		if (business == null) {
			throw new RuntimeException("业务不存在");
		}

		List<BizOutputRecord> bizOutputRecords = bizOutputRecordMapper.selectList(
				new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).orderBy("fact_output_date", true));

		if (bizOutputRecords == null) {
			throw new RuntimeException("业务尚未出款");
		}
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
		List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailMapper
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("business_id", businessId)
						.orderBy("period").orderBy("plan_item_type"));

		for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
			if (repaymentBizPlanListDetail.getPlanItemName().equals("本金")
					&& repaymentBizPlanListDetail.getPlanItemType() == 10) {
				principal = principal.add(repaymentBizPlanListDetail.getPlanAmount());
			}

			if (repaymentBizPlanListDetail.getPlanItemName().equals("利息")
					&& repaymentBizPlanListDetail.getPlanItemType() == 20) {
				interest = interest.add(repaymentBizPlanListDetail.getPlanAmount());
			}

			if (repaymentBizPlanListDetail.getPlanItemName().indexOf("服务费") > -1) {
				servicecharge = servicecharge.add(repaymentBizPlanListDetail.getPlanAmount());
			}

			if (repaymentBizPlanListDetail.getPlanItemName().equals("滞纳金")
					&& repaymentBizPlanListDetail.getPlanItemType() == 60) {
				lateFee = lateFee.add(repaymentBizPlanListDetail.getPlanAmount());
			}
			if (repaymentBizPlanListDetail.getPlanItemName().indexOf("平台费") > -1) {
				platformFee = platformFee.add(repaymentBizPlanListDetail.getPlanAmount());
			}
			if (repaymentBizPlanListDetail.getPlanItemName().equals("担保费")
					&& repaymentBizPlanListDetail.getPlanItemType() == 40) {
				guaranteeFee = guaranteeFee.add(repaymentBizPlanListDetail.getPlanAmount());
			}
			
			balance = balance.add(repaymentBizPlanListDetail.getFactAmount()==null?new BigDecimal(0):repaymentBizPlanListDetail.getFactAmount());
		}
		list = expenseSettleMapper.listLackFee(businessId);
		ExpenseSettleVO expenseSettleVO = new ExpenseSettleVO() ;
		expenseSettleVO.setPrincipal(principal);
		expenseSettleVO.setInterest(interest);
		expenseSettleVO.setLateFee(lateFee);
		expenseSettleVO.setServicecharge(servicecharge);
		expenseSettleVO.setGuaranteeFee(guaranteeFee);
		expenseSettleVO.setPlatformFee(platformFee);
		expenseSettleVO.setList(list);
		expenseSettleVO.setBalance(balance);
		for (ExpenseSettleLackFeeVO e : list) {
			lackFee = lackFee.add(e.getServicecharge()).add(e.getLateFee());
		}
		expenseSettleVO.setLackFee(lackFee);
		return expenseSettleVO;
	}

	/* (non-Javadoc)
	 * @see com.hongte.alms.base.service.ExpenseSettleService#listLackFee(java.lang.String)
	 */
	@Override
	public List<ExpenseSettleLackFeeVO> listLackFee(String businessId) {
		return  expenseSettleMapper.listLackFee(businessId);
	}

	public ExpenseSettleVO cal(String businessId,Date settleDate) {
		final BasicBusiness basicBusiness = basicBusinessMapper.selectById(businessId);
		RepaymentBizPlan repaymentBizPlan = new RepaymentBizPlan() ;
		repaymentBizPlan.setBusinessId(businessId);
		repaymentBizPlan = repaymentBizPlanMapper
				.selectOne(repaymentBizPlan);
		List<Object> businessIds = renewalBusinessMapper.selectObjs(new EntityWrapper<RenewalBusiness>().eq("original_business_id", businessId).setSqlSelect("renewal_business_id")) ;
		if (businessIds==null) {
			businessIds = new ArrayList<>();
		}
		businessIds.add(businessId);
		final List<RepaymentBizPlanList> planLists = repaymentBizPlanListMapper.selectList(
				new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId).orderBy("due_date"));
		final List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailMapper.selectList(
				new EntityWrapper<RepaymentBizPlanListDetail>().in("business_id", businessIds).orderBy("period"));
		final ExpenseSettleRepaymentPlanVO plan = new ExpenseSettleRepaymentPlanVO(repaymentBizPlan, planLists, details);
		
		ExpenseSettleVO expenseSettleVO = new ExpenseSettleVO() ;
		calPrincipal(settleDate, expenseSettleVO, basicBusiness, plan);
		calInterest(settleDate, expenseSettleVO, basicBusiness, plan);
		calServicecharge(settleDate, expenseSettleVO, basicBusiness, plan);
		calGuaranteeFee(expenseSettleVO, basicBusiness);
		calPlatformFee(settleDate, expenseSettleVO, basicBusiness, plan);
		calDemurrage(settleDate, expenseSettleVO, basicBusiness, plan);
		calPenalty(settleDate, expenseSettleVO, basicBusiness, plan);
		calLateFee(settleDate, expenseSettleVO, basicBusiness, plan);
		calLackFee(settleDate, expenseSettleVO, basicBusiness, plan);
		calBalance(expenseSettleVO, basicBusiness);
		
		return expenseSettleVO;
		
	}
	
	/**
	 * 计算剩余未还本金
	 * @author 王继光
	 * 2018年3月30日 下午2:35:29
	 * @param expenseSettleVO
	 */
	private void calPrincipal(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
			expenseSettleVO.setPrincipal(basicBusiness.getBorrowMoney());
			break;
		case 5:
			expenseSettleVO.setPrincipal(basicBusiness.getBorrowMoney().subtract(plan.calCurrentDetails(settleDate, 10, true)));
			break;
		default:
			/*找不到还款方式233333333333*/
			expenseSettleVO.setPrincipal(new BigDecimal(233333333));
			break;
		}
	}
	
	/**
	 * 计算本期利息
	 * @author 王继光
	 * 2018年3月30日 下午3:18:43
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calInterest(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
		case 5:
			expenseSettleVO.setInterest(plan.calCurrentDetails(settleDate, 20, false));
			break;
		default:
			/*找不到还款方式233333333333*/
			expenseSettleVO.setInterest(new BigDecimal(233333333));
			break;
		}
	}
	
	/**
	 * 计算本期服务费
	 * @author 王继光
	 * 2018年3月30日 下午3:28:29
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calServicecharge(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
		case 5:
			expenseSettleVO.setServicecharge(plan.calCurrentDetails(settleDate, 30, false));
			break;
		default:
			/*找不到还款方式233333333333*/
			expenseSettleVO.setServicecharge(new BigDecimal(233333333));
			break;
		}
	}
	
	/**
	 * 计算本期担保费,调用信贷接口
	 * @author 王继光
	 * 2018年3月30日 下午3:42:55
	 * @param expenseSettleVO
	 * @param basicBusiness
	 */
	private void calGuaranteeFee(ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness) {
		BigDecimal guaranteeFee = new BigDecimal(0);
		logger.info("调用callRemoteService");
		if (xindaiAplUrlUrl==null) {
			logger.error("xindaiAplUrlUrl==null!!!");
		}
		logger.info("xindaiAplUrlUrl:"+xindaiAplUrlUrl);
		DESC desc = new DESC();
		RequestData requestData = new RequestData();
		requestData.setMethodName("AfterLoanRepayment_GetFeeList");
		JSONObject data = new JSONObject() ;
		data.put("businessId", basicBusiness.getBusinessId());
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
		
		BigDecimal g = new BigDecimal(0);
		JSONArray jsonArray = JSONArray.parseArray(respData.getData());
		for (Object object : jsonArray) {
			JSONObject j = (JSONObject) object ;
			String feeTypeName = j.getString("fee_type_name");
			BigDecimal feeValue =  j.getBigDecimal("fee_value");
			BigDecimal factFeeValue = j.getBigDecimal("fact_fee_value");
			Integer isOneTimeCharge = j.getInteger("is_one_time_charge");
			if (feeTypeName.equals("担保公司费用")&&isOneTimeCharge.equals(new Integer(1))) {
				if (factFeeValue==null) {
					guaranteeFee = feeValue ;
					return ;
				}else {
					guaranteeFee = feeValue.subtract(factFeeValue) ;
					return ;
				}
			}
		}
		
		expenseSettleVO.setGuaranteeFee(guaranteeFee);
		
	}
	
	/**
	 * 
	 * 计算本期平台费
	 * @author 王继光
	 * 2018年3月30日 下午3:46:57
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calPlatformFee(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
		case 5:
			expenseSettleVO.setServicecharge(plan.calCurrentDetails(settleDate, 50, false));
			break;
		default:
			/*找不到还款方式233333333333*/
			expenseSettleVO.setServicecharge(new BigDecimal(233333333));
			break;
		}
	}
	
	private void calLateFee(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		BigDecimal rate = null;
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
			//先息后本
			rate = new BigDecimal(0.003);
			break;
		case 5:
			//等额本息
			rate = new BigDecimal(0.001);
			break;
		default:
			/*找不到还款方式233333333333*/
			break;
		}
		if (rate==null) {
			logger.error("找不到滞纳金利率,businessId:"+basicBusiness.getBusinessId()+",repayment_type:"+basicBusiness.getRepaymentTypeId());
			expenseSettleVO.setLateFee(new BigDecimal(233333333));
			return ;
		}
		
		for (ExpenseSettleRepaymentPlanListVO e : plan.findCurrentPeriods(settleDate)) {
			String status = e.getRepaymentBizPlanList().getCurrentStatus() ;
			if (status.equals("逾期")) {
				int daysBeyoungDueDate = DateUtil.getDiffDays(e.getRepaymentBizPlanList().getDueDate(), settleDate);
				if (daysBeyoungDueDate > 1) {
					expenseSettleVO.setLateFee(expenseSettleVO.getLateFee().add(expenseSettleVO.getPrincipal().multiply(rate).multiply(new BigDecimal(daysBeyoungDueDate)))); 
				}
			}
		}
	}
	
	/**
	 * 计算期外逾期费
	 * @author 王继光
	 * 2018年3月30日 下午5:42:02
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calDemurrage(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		ExpenseSettleRepaymentPlanListVO finalPeriod = plan.findFinalPeriod();
		int daysBeyoungFinalPeriod = DateUtil.getDiffDays(finalPeriod.getRepaymentBizPlanList().getDueDate(), settleDate);
		if (daysBeyoungFinalPeriod > 0) {
			expenseSettleVO.setDemurrage(expenseSettleVO.getPrincipal().multiply(new BigDecimal(0.002)).multiply(new BigDecimal(daysBeyoungFinalPeriod)));
		}
	}
	
	/**
	 * 计算提前还款违约金
	 * @author 王继光
	 * 2018年3月30日 下午5:58:40
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 */
	private void calPenalty(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan) {
		BigDecimal penalty = new BigDecimal(0);
		RepaymentBizPlanList lastCurrentPeriod = plan.findCurrentPeriods(settleDate).get(plan.findCurrentPeriods(settleDate).size()-1).getRepaymentBizPlanList() ;
		for (RepaymentBizPlanListDetail detail : plan.allDetails()) {
			if (detail.getPeriod()>lastCurrentPeriod.getPeriod()
					&& detail.getPlanItemType() == 30) {
				penalty = penalty.add(detail.getPlanAmount());
			}
			
		}
		
		switch (basicBusiness.getRepaymentTypeId()) {
		case 2:
			//先息后本
			
			BigDecimal p6 = expenseSettleVO.getPrincipal().multiply(new BigDecimal(0.06));
			if (penalty.compareTo(p6)>=0) {
				penalty = p6 ;
			}
			
			break;
		case 5:
			//等额本息
			break;
		default:
			/*找不到还款方式233333333333*/
			break;
		}
		
		expenseSettleVO.setPenalty(penalty);
		
	}
	
	
	/**
	 * 计算结余
	 * @author 王继光
	 * 2018年3月30日 下午6:04:02
	 * @param expenseSettleVO
	 * @param basicBusiness
	 */
	private void calBalance(ExpenseSettleVO expenseSettleVO , BasicBusiness basicBusiness ) {
		Double balance = transferOfLitigationMapper.queryOverRepayMoneyByBusinessId(basicBusiness.getBusinessId());
		if (balance==null) {
			logger.error("结清试算balance==null!!!");
			expenseSettleVO.setBalance(new BigDecimal(0));
		}else {
			expenseSettleVO.setBalance(new BigDecimal(balance));
		}
	}
	
	/**
	 * 计算往期少缴费用
	 * @author 王继光
	 * 2018年3月30日 下午6:26:20
	 * @param settleDate
	 * @param expenseSettleVO
	 * @param basicBusiness
	 * @param plan
	 * @return
	 */
	private void calLackFee(Date settleDate ,ExpenseSettleVO expenseSettleVO,BasicBusiness basicBusiness ,ExpenseSettleRepaymentPlanVO plan){
		/*往期滞纳金,取最大一期*/
		BigDecimal firstLateFee = null ;
		BigDecimal firstInterest = null ;
		List<ExpenseSettleLackFeeVO> list = new ArrayList<>() ;
		for (ExpenseSettleRepaymentPlanListVO e : plan.findPastPeriods(settleDate)) {
			ExpenseSettleLackFeeVO expenseSettleLackFeeVO = new ExpenseSettleLackFeeVO();
			expenseSettleLackFeeVO.setPeriod(e.getRepaymentBizPlanList().getAfterId());
			expenseSettleLackFeeVO.setLateFee(new BigDecimal(0));
			expenseSettleLackFeeVO.setServicecharge(new BigDecimal(0));
			expenseSettleLackFeeVO.setPlatFormFee(new BigDecimal(0));
			expenseSettleLackFeeVO.setInterest(new BigDecimal(0));
			
			for (RepaymentBizPlanListDetail d : e.getRepaymentBizPlanListDetails()) {
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

					if (firstInterest == null) {
						firstInterest = d.getPlanAmount()
								.subtract(d.getFactAmount() == null ? new BigDecimal(0) : d.getFactAmount());
					}
					expenseSettleLackFeeVO.setInterest(firstInterest);
				} else if (d.getPlanItemType().equals(new Integer(60))) {
					if (firstLateFee == null) {
						if (e.getRepaymentBizPlanList() != null) {
							int daysBeyoungDueDate = DateUtil.getDiffDays(e.getRepaymentBizPlanList().getDueDate(), settleDate);
							BigDecimal lateFeeRate = d.getPlanAmount()
									.divide(e.getRepaymentBizPlanList().getOverdueDays().multiply(expenseSettleVO.getPrincipal()),2,RoundingMode.HALF_UP);
							if (daysBeyoungDueDate > 1) {
								firstLateFee = expenseSettleVO.getPrincipal().multiply(lateFeeRate)
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
		
		BigDecimal lackFee = new BigDecimal(0);
		for (ExpenseSettleLackFeeVO expenseSettleLackFeeVO : list) {
			lackFee = lackFee.add(expenseSettleLackFeeVO.getLateFee())
					.add(expenseSettleLackFeeVO.getServicecharge())
					.add(expenseSettleLackFeeVO.getPlatFormFee()
					.add(expenseSettleLackFeeVO.getInterest()));
		}
		expenseSettleVO.setLackFee(lackFee);
		expenseSettleVO.setList(list);
		
	}
}
