package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.util.DateUtil;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标的还款计划列表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@Service("RepaymentProjPlanListService")
public class RepaymentProjPlanListServiceImpl extends BaseServiceImpl<RepaymentProjPlanListMapper, RepaymentProjPlanList> implements RepaymentProjPlanListService {
	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService  repaymentProjPlanService;
	
	
	@Autowired
	@Qualifier("RepaymentProjPlanListDetailService")
	RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper;
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService  basicBusinessService;
	
	
  
	private void calLateFee() {
		//所有业务贷后生成的业务
		List<BasicBusiness> basicBusiness =basicBusinessService.selectList((new EntityWrapper<BasicBusiness>().eq("creat_sys_type", 2)));

		for(BasicBusiness business:basicBusiness) {
			//每个业务对应所有贷后生成的标的还款计划
			List<RepaymentProjPlan> projPlans =repaymentProjPlanService.selectList((new EntityWrapper<RepaymentProjPlan>().eq("creat_sys_type", 2)).eq("original_business_id",business.getBusinessId()));
		   
			for(RepaymentProjPlan projPlan:projPlans) {
				//每个标的还款计划对应所有标的列表
				List<RepaymentProjPlanList>  projList=selectList((new EntityWrapper<RepaymentProjPlanList>().eq("creat_sys_type", 2).eq("active", 1)
						.ne("current_status", RepayCurrentStatusEnums.已还款).ne("current_sub_status", RepayRegisterFinanceStatus.还款待确认)).eq("proj_plan_id", projPlan.getProjPlanId()));
				
				   for(RepaymentProjPlanList projPList:projList) {
					   //没有逾期
					   if(isOverDue(new Date(), projPList.getDueDate())==false) {
						   continue;
					   //逾期的期数
					   }else {
						   
					   }
				   }
			}
		
		}
		
	
	
		
	
	}
	
	
	/**
	 *判断是否逾期
	 * @param nowDate
	 * @param repayDate
	 * @return
	 */
	private boolean isOverDue(Date nowDate, Date repayDate) {
		int i = DateUtil.getDiffDays(nowDate, repayDate);
		if (i >= 0) {
			return false;
		} else {
			return true;
		}
	}

	
	
}
