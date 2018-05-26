package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 还款确认日志记录表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-25
 */
@Service("RepaymentConfirmLogService")
public class RepaymentConfirmLogServiceImpl extends BaseServiceImpl<RepaymentConfirmLogMapper, RepaymentConfirmLog> implements RepaymentConfirmLogService {

	@Autowired
	RepaymentConfirmLogMapper confirmLogMapper ;
	@Autowired
	RepaymentProjFactRepayMapper repaymentProjFactRepayMapper ;
	@Autowired
	RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper ;
	@Autowired
	RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper ;
	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper ;
	@Autowired
	RepaymentResourceMapper repaymentResourceMapper ;
	@Autowired
	@Qualifier("MoneyPoolService")
	MoneyPoolService moneyPoolService ;
	@Autowired
	AccountantOverRepayLogMapper accountantOverRepayLogMapper;
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Result revokeConfirm(String businessId, String afterId) {
		List<RepaymentConfirmLog> logs = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId).eq("after_id", afterId).orderBy("`index`",false));
		if (logs==null||logs.size()==0) {
			return Result.error("500", "找不到任何一条相关的确认还款记录");
		}
		RepaymentConfirmLog log = logs.get(0);
		
		if (log.getCanRevoke().equals(0)) {
			return Result.error("500", "该还款记录不能被撤销");
		}
		
		RepaymentConfirmLog lastLog = null ;
		List<RepaymentProjFactRepay> lastfactRepays = null;
		RepaymentProjFactRepay lastfactRepay = null ;
		RepaymentResource lastRepaymentResource = null;
		if (logs.size()>1) {
			lastLog = logs.get(1);
			lastfactRepays = repaymentProjFactRepayMapper.selectList(
					new EntityWrapper<RepaymentProjFactRepay>()
					.eq("confirm_log_id", lastLog.getConfirmLogId())
					.orderBy("create_date",false));
			lastfactRepay = lastfactRepays.get(0);
			lastRepaymentResource = new RepaymentResource() ;
			lastRepaymentResource.setResourceId(lastfactRepay.getRepaySourceId());
			lastRepaymentResource = repaymentResourceMapper.selectOne(lastRepaymentResource);
		}
		
		List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper.selectList(
				new EntityWrapper<RepaymentProjFactRepay>()
				.eq("confirm_log_id", log.getConfirmLogId())
				.orderBy("proj_plan_list_id"));
		
		BigDecimal factAmount = new BigDecimal(0);
		
		for (RepaymentProjFactRepay factRepay : factRepays) {
			factAmount = factAmount.add(factRepay.getFactAmount());
			RepaymentProjPlanListDetail projPlanListDetail = new RepaymentProjPlanListDetail();
			projPlanListDetail.setProjPlanDetailId(factRepay.getProjPlanDetailId());
			projPlanListDetail = repaymentProjPlanListDetailMapper.selectOne(projPlanListDetail);
			projPlanListDetail.setProjFactAmount(projPlanListDetail.getProjFactAmount().subtract(factRepay.getFactAmount()));
			projPlanListDetail.updateById();
			RepaymentBizPlanListDetail planListDetail = new RepaymentBizPlanListDetail();
			planListDetail.setPlanDetailId(projPlanListDetail.getPlanDetailId());
			planListDetail = repaymentBizPlanListDetailMapper.selectOne(planListDetail) ;
			planListDetail.setFactAmount(planListDetail.getFactAmount().subtract(factRepay.getFactAmount()));
			if (lastfactRepay==null) {
				planListDetail.setRepaySource(null);
				planListDetail.setFactRepayDate(null);
			}else {
				planListDetail.setRepaySource(lastfactRepay.getRepaySource());
				planListDetail.setFactRepayDate(lastfactRepay.getFactRepayDate());
			}
			planListDetail.updateById();
			RepaymentResource resource = new RepaymentResource() ;
			resource.setResourceId(factRepay.getRepaySourceId());
			resource = repaymentResourceMapper.selectOne(resource);
			
			moneyPoolService.revokeConfirmRepaidUpdateMoneyPool(resource);
			
			if (resource!=null) {
				resource.deleteById();
			}
			repaymentProjFactRepayMapper.delete(new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_detail_repay_id", factRepay.getProjPlanDetailRepayId()));
		}
		
		RepaymentBizPlanList planList = new RepaymentBizPlanList() ;
		planList.setBusinessId(businessId);
		planList.setAfterId(afterId);
		planList = repaymentBizPlanListMapper.selectOne(planList) ;
		
		BigDecimal unpaid = repaymentBizPlanListMapper.caluBizPlanListUnpaid(planList.getPlanListId());
		BigDecimal planListfactAmount = repaymentBizPlanListMapper.caluBizPlanListFactAmount(planList.getPlanListId());
		if (planListfactAmount.compareTo(new BigDecimal(0))==0) {
			planList.setRepayStatus(null);
		}else if (unpaid.compareTo(new BigDecimal(0))>0) {
			planList.setRepayStatus(SectionRepayStatusEnum.SECTION_REPAID.getKey());
			List<RepaymentBizPlanListDetail> planListDetails = repaymentBizPlanListDetailMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planList.getPlanListId()));
			boolean item10Repaid = false ;
			boolean item20Repaid = false ;
			boolean item30Repaid = false ;
			boolean item50Repaid = false ;
			boolean onlineOverDueRepaid = false ;
			for (RepaymentBizPlanListDetail planListDetail : planListDetails) {
				//某项还完
				switch (planListDetail.getPlanItemType()) {
				case 10:
					item10Repaid = true;
				case 20:
					item20Repaid = true;
				case 30:
					item30Repaid = true;
					break;
				case 50:
					item50Repaid = true;
					break;
				case 60:
					if (planListDetail.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
						onlineOverDueRepaid = true;
					}
					break;
				default:
					break;
				}
			}
			if (item10Repaid&&item20Repaid&&item30Repaid&&item50Repaid&&onlineOverDueRepaid) {
				planList.setRepayStatus(SectionRepayStatusEnum.ONLINE_REPAID.getKey());
			}
			
			
			
			planList.setRepayFlag(RepayedFlag.REPAYING.getKey());
		}
		planList.setFactRepayDate(lastRepaymentResource==null?null:lastRepaymentResource.getRepayDate());
		planList.setFinanceComfirmDate(lastLog==null?null:lastLog.getCreateTime());
		planList.setFinanceConfirmUser(lastLog==null?null:lastLog.getCreateUser());
		planList.setFinanceConfirmUserName(lastLog==null?null:lastLog.getCreateUserName());
		planList.updateById();
		
		if (log.getSurplusRefId()!=null) {
			AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogMapper.selectById(log.getSurplusRefId());
			if (accountantOverRepayLog!=null) {
				accountantOverRepayLog.deleteById();
			}
		}
		
		log.deleteById();
		return Result.success();
	}
	
	@Override
	public List<JSONObject> selectCurrentPeriodConfirmedProjInfo(String businessId, String afterId) {
		List<RepaymentConfirmLog> list = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId).eq("after_id", afterId).orderBy("`index`",false)) ;
		List<JSONObject> res = new ArrayList<>() ;
		for (RepaymentConfirmLog repaymentConfirmLog : list) {
			String json = repaymentConfirmLog.getProjPlanJson();
			List<CurrPeriodProjDetailVO> proj = JSON.parseArray(json, CurrPeriodProjDetailVO.class) ;
			BigDecimal item10 = new BigDecimal(0);
			BigDecimal item20 = new BigDecimal(0);
			BigDecimal item30 = new BigDecimal(0);
			BigDecimal item50 = new BigDecimal(0);
			BigDecimal offlineOverDue = new BigDecimal(0);
			BigDecimal onlineOverDue = new BigDecimal(0);
			BigDecimal subTotal = new BigDecimal(0);
			BigDecimal total = new BigDecimal(0);
			String realName = null ;
			BigDecimal amount = new BigDecimal(0);
			for (CurrPeriodProjDetailVO currPeriodProjDetailVO : proj) {
				realName = currPeriodProjDetailVO.getUserName();
				amount = currPeriodProjDetailVO.getProjAmount();
				item10 = item10.add(currPeriodProjDetailVO.getItem10());
				item20 = item20.add(currPeriodProjDetailVO.getItem20());
				item30 = item30.add(currPeriodProjDetailVO.getItem30());
				item50 = item50.add(currPeriodProjDetailVO.getItem50());
				offlineOverDue = offlineOverDue.add(currPeriodProjDetailVO.getOfflineOverDue());
				onlineOverDue = onlineOverDue.add(currPeriodProjDetailVO.getOnlineOverDue());
				subTotal = subTotal.add(currPeriodProjDetailVO.getSubTotal());
				total = total.add(currPeriodProjDetailVO.getTotal());
			}
			
			JSONObject p = new JSONObject() ;
			p.put("realName", realName);
			p.put("amount", amount);
			p.put("item10", item10);
			p.put("item20", item20);
			p.put("item30", item30);
			p.put("item50", item50);
			p.put("repayDate", repaymentConfirmLog.getRepayDate());
			p.put("offlineOverDue", offlineOverDue);
			p.put("onlineOverDue", onlineOverDue);
			p.put("subTotal", subTotal);
			p.put("total", total);
			p.put("list", proj);
			p.put("type", "实际应还日期");
			res.add(p);
		}
		
		return res;
	}
}
