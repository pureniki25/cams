/**
 * 
 */
package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.ProjExtRate;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.ProjExtRateMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.SettleService;
import com.hongte.alms.base.vo.finance.SettleInfoVO;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

/**
 * @author 王继光 2018年6月14日 下午3:52:47
 */
@Service("SettleService")
public class SettleServiceImpl implements SettleService {
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper;

	@Autowired
	RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper ;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper ;
	
	@Autowired
	ProjExtRateMapper projExtRateMapper ;
	
	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService repaymentProjPlanService ;
	
	@Autowired
	RepaymentProjPlanMapper repaymentProjPlanMapper ;
	@Override
	public JSONObject settleInfo(String businessId, String afterId, String planId) {
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", businessId).eq("after_id", afterId);
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListMapper.selectList(planListEW);
		BigDecimal item10 = new BigDecimal("0");
		BigDecimal item20 = new BigDecimal("0");
		BigDecimal item30 = new BigDecimal("0");
		BigDecimal item50 = new BigDecimal("0");
		BigDecimal offlineOverDue = new BigDecimal("0");
		BigDecimal onlineOverDue = new BigDecimal("0");
		BigDecimal planAmount = new BigDecimal("0");
		BigDecimal lack = new BigDecimal("0");
		BigDecimal factAmount = new BigDecimal("0");
		BigDecimal penalty = new BigDecimal("0");
		List<JSONObject> derateList = new ArrayList<>();
		List<JSONObject> lackList = new ArrayList<>();
		for (RepaymentBizPlanList repaymentBizPlanList : planLists) {
			
		}
		return null;
	}

	@Override
	public SettleInfoVO settleInfoVO(String businessId, String afterId, String planId,Date factRepayDate) {
		RepaymentBizPlanList cur = new RepaymentBizPlanList() ;
		cur.setOrigBusinessId(businessId);cur.setAfterId(afterId);
		cur = repaymentBizPlanListMapper.selectOne(cur);
		if (cur==null) {
			throw new ServiceRuntimeException("找不到当前期还款计划");
		}
		SettleInfoVO infoVO = new SettleInfoVO() ;
		
		infoVO.setItem10(repaymentProjPlanListDetailMapper.calcUnpaidPrincipal(businessId, planId));
		Date today = new Date() ;
		calcCurPeriod(cur,infoVO,factRepayDate==null?today:factRepayDate);
		
		
		
		infoVO.setRepayPlanDate(cur.getDueDate());
		int diff = DateUtil.getDiffDays(cur.getDueDate(), factRepayDate==null?today:factRepayDate);
		if (diff>0&&cur.getCurrentStatus().equals(RepayPlanStatus.OVERDUE.getName())) {
			infoVO.setOverDueDays(diff);
		}
		
		infoVO.setDerates(repaymentBizPlanListDetailMapper.selectLastPlanListDerateFees(businessId,cur.getDueDate(), planId));
		infoVO.setLackFees(repaymentBizPlanListDetailMapper.selectLastPlanListLackFees(businessId,cur.getDueDate(), planId));
		
		infoVO.setPenalty(calcPenalty(cur, planId));
		
		infoVO.setSubtotal(infoVO.getSubtotal().add(infoVO.getItem10()).add(infoVO.getItem20()).add(infoVO.getItem30()).add(infoVO.getItem50()));
		infoVO.setTotal(infoVO.getTotal().add(infoVO.getSubtotal()).add(infoVO.getOfflineOverDue()).add(infoVO.getOnlineOverDue()).add(infoVO.getDerate()).add(infoVO.getPlanRepayBalance()));
		
		return infoVO;
	}
	
	/**
	 * 计算提前还款违约金
	 * @author 王继光
	 * 2018年7月11日 下午10:06:09
	 * @param bizPlanList
	 * @param planId
	 * @return
	 */
	private BigDecimal calcPenalty(RepaymentBizPlanList bizPlanList,String planId) {
		List<ProjExtRate> extRates = projExtRateMapper
				.selectList(new EntityWrapper<ProjExtRate>().eq("business_id", bizPlanList.getOrigBusinessId())
						.ge("begin_peroid", bizPlanList.getPeriod()).le("end_peroid", bizPlanList.getPeriod()));
		BigDecimal penalty = BigDecimal.ZERO;
		for (ProjExtRate projExtRate : extRates) {
			switch (projExtRate.getCalcWay()) {
			//根据计算方式不同分别计算
			case 1:
				//1.借款金额*费率值
				RepaymentProjPlan projPlan = repaymentProjPlanService.selectOne(new EntityWrapper<RepaymentProjPlan>().eq("project_id", projExtRate.getProjectId()));
				penalty = penalty.add(projPlan.getBorrowMoney().multiply(projExtRate.getRateValue())) ;
				break;
			case 2:
				//2剩余本金*费率值
				BigDecimal upaid = repaymentProjPlanMapper.sumProjectItem10Unpaid(projExtRate.getProjectId(), planId);
				penalty = penalty.add(upaid.multiply(projExtRate.getRateValue())) ;
				break;
			case 3:
				//3.1*费率值'
				penalty = penalty.add(projExtRate.getRateValue());
				break;
			default:
				break;
			}
		}
		return penalty;
	}
	
	/**
	 * 计算当前期未还金额
	 * @author 王继光
	 * 2018年7月7日 下午4:33:49
	 * @param repaymentBizPlanList
	 * @param infoVO
	 */
	private void calcCurPeriod(RepaymentBizPlanList repaymentBizPlanList,SettleInfoVO infoVO,Date factRepayDate) {
		BigDecimal item20 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "20", null);
		BigDecimal item30 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "30", null);
		BigDecimal item50 = repaymentProjPlanListDetailMapper.calcBizPlanListUnpaid(repaymentBizPlanList.getPlanListId(), "50", null);
		
		infoVO.setItem20(item20);
		infoVO.setItem30(item30);
		infoVO.setItem50(item50);
		
		
		//TODO 要调用滞纳金计算
		/*infoVO.setOfflineOverDue(infoVO.getOfflineOverDue().add(item60offline));
		infoVO.setOnlineOverDue(infoVO.getOnlineOverDue().add(item60online));*/
	}
	
	/**
	 * 查找往期还款计划
	 * @author 王继光
	 * 2018年7月5日 下午6:23:48
	 * @param cur
	 * @param planId
	 * @return
	 */
	private List<RepaymentBizPlanList> selectLastPlanLists(RepaymentBizPlanList cur, String planId){
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", cur.getOrigBusinessId()).eq("after_id", cur.getAfterId());
		planListEW.lt("due_date", cur.getDueDate());
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		return repaymentBizPlanListMapper.selectList(planListEW);
	}
	
	/**
	 * 查询往后的还款计划
	 * @author 王继光
	 * 2018年7月5日 下午6:25:18
	 * @param cur
	 * @param planId
	 * @return
	 */
	private List<RepaymentBizPlanList> selectNextPlanLists(RepaymentBizPlanList cur,String planId){
		EntityWrapper<RepaymentBizPlanList> planListEW = new EntityWrapper<>();
		planListEW.eq("orig_business_id", cur.getOrigBusinessId());
		planListEW.gt("due_date", cur.getDueDate());
		if (!StringUtil.isEmpty(planId)) {
			planListEW.eq("plan_id", planId);
		}
		planListEW.orderBy("due_date");
		return repaymentBizPlanListMapper.selectList(planListEW);
	}

}
