package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.service.RepaymentProjPlanListDetailService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

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
	private void calLateFee() {
		
		List<RepaymentProjPlanList>  projList=selectList((new EntityWrapper<RepaymentProjPlanList>().eq("creat_sys_type", 2).eq("active", 1)
				.ne("current_status", RepayCurrentStatusEnums.已还款).ne("current_sub_status", RepayRegisterFinanceStatus.还款待确认)));
		
	
	}


	
	
}
