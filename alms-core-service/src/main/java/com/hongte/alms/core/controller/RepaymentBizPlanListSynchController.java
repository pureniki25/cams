package com.hongte.alms.core.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanListSynch;
import com.hongte.alms.base.service.RepaymentBizPlanListSynchService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 业务还款计划列表 前端控制器
 * </p>
 *
 * @author 刘正全
 * @since 2018-07-09
 */
@RestController
@RequestMapping("/repaymentBizPlanListSynch")
public class RepaymentBizPlanListSynchController {
	private static final Logger logger = LoggerFactory.getLogger(TransferOfLitigationController.class);
	@Autowired
	@Qualifier("RepaymentBizPlanListSynchService")
	RepaymentBizPlanListSynchService repaymentBizPlanListSynchService;
	
    @Autowired
    private Executor msgThreadAsync;
	
	@PostMapping("/synchDaihouData")
	@ApiOperation(value = "贷后数据同步")
	public Result<Integer> synchDaihouData() {
		msgThreadAsync.execute(new Runnable() {
			@Override
			public void run() {
				Long startLongDate = new Date().getTime();
				// 1循环取10000条插入
				synchRepaymentBizPlanListAdd();
				// 2处理删除
				synchRepaymentBizPlanListDel();
				// 3处理更新
				// 3-1处理tb_repayment_biz_plan_list表更新
				synchRepaymentBizPlanList();

				// 3-4处理tb_repayment_biz_plan表更新
				// 3-2处理tb_basic_businessg表更新
				synchRepaymentBizPlan();
				synchBasicBusinessg();

				// 3-3处理tb_collection_status表更新
				synchCollectionStatus();

				// 3-5处理tb_five_level_classify_business_change_log表更新
				synchFiveLevelClassifyBusinessChangeLog();

				// 3-6处理tb_repayment_biz_plan表更新
				synchBasicBusinessCustomer();

				// 3-7处理tb_repayment_biz_plan_list_detail表更新
				synchRepaymentBizPlanListDetail();

				// 3-8处理tb_basic_company表更新
				synchBasicCompany();

				// 3-9处理tb_tuandai_project_info表更新
				synchTuandaiProjectInfo();

				// 3-10处理tb_money_pool_repayment表更新
				synchMoneyPoolRepayment();

				// 3-11处理tb_basic_business_type表更新
				updateBasicBusinessType();
				Long endLongDate = new Date().getTime();
				System.err.println("同步结束,耗时：" + (endLongDate - startLongDate));
			}
		});

		return Result.success();
	}


	protected void updateBasicBusinessType() {
		repaymentBizPlanListSynchService.updateBasicBusinessType();
	}


	private void synchMoneyPoolRepayment() {
		repaymentBizPlanListSynchService.updateMoneyPoolRepayment();
	}


	private void synchTuandaiProjectInfo() {
		repaymentBizPlanListSynchService.updateTuandaiProjectInfo();
	}


	private void synchBasicCompany() {
		repaymentBizPlanListSynchService.updateBasicCompany();
	}


	private void synchRepaymentBizPlanListDel() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchRepaymentBizPlanListDel()");
		}
		Wrapper<RepaymentBizPlanListSynch> wrapperDelId = new EntityWrapper<>();
		wrapperDelId.isNull("t2.plan_list_id");
		wrapperDelId.last(" limit 1000 ");
		List<String> listDelId = repaymentBizPlanListSynchService.selectdelList(wrapperDelId);
		while(null != listDelId && !listDelId.isEmpty()) {
			repaymentBizPlanListSynchService.deleteBatchIds(listDelId);
			listDelId = repaymentBizPlanListSynchService.selectdelList(wrapperDelId);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchRepaymentBizPlanListDel()");
		}
	}


	private void synchRepaymentBizPlanListAdd() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchRepaymentBizPlanListAdd()");
		}
		Wrapper<RepaymentBizPlanListSynch> wrapperRepaymentBizPlanListSynch = new EntityWrapper<>();
		wrapperRepaymentBizPlanListSynch.isNull("t1.plan_list_id");
		wrapperRepaymentBizPlanListSynch.last(" limit 1 ");
		
		List<Map<String, Object>> list = repaymentBizPlanListSynchService.selectAddList(wrapperRepaymentBizPlanListSynch);
		while(null != list && !list.isEmpty()) {
			repaymentBizPlanListSynchService.addRepaymentBizPlanListSynch();
			list = repaymentBizPlanListSynchService.selectAddList(wrapperRepaymentBizPlanListSynch);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchRepaymentBizPlanListAdd()");
		}
	}
	
	
	/**
	 * @Title: synchRepaymentBizPlanList  
	 * @Description: 同步3-1处理tb_repayment_biz_plan_list表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  null
	 */
	private void synchRepaymentBizPlanList() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchRepaymentBizPlanList()");
		}
		repaymentBizPlanListSynchService.updateRepaymentBizPlanList();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchRepaymentBizPlanList()");
		}
	}
	
	/**
	 * @Title: synchBasicBusinessg  
	 * @Description: 同步3-2处理tb_basic_businessg表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  null
	 */
	private void synchBasicBusinessg() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchBasicBusinessg()");
		}
		repaymentBizPlanListSynchService.updateBasicBusinessg();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchBasicBusinessg()");
		}
	}
	
	/**
	 * @Title: synchCollectionStatus  
	 * @Description: 同步3-3处理tb_collection_status表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  3-3
	 */
	private void synchCollectionStatus() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchCollectionStatus()");
		}
		repaymentBizPlanListSynchService.updateCollectionStatus();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchCollectionStatus()");
		}
	}
	
	/**
	 * @Title: synchRepaymentBizPlan  
	 * @Description: 3-4处理tb_repayment_biz_plan表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  null
	 */
	private void synchRepaymentBizPlan() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchRepaymentBizPlan()");
		}
		repaymentBizPlanListSynchService.updateRepaymentBizPlan();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchRepaymentBizPlan()");
		}
	}
	
	/**
	 * @Title: synchFiveLevelClassifyBusinessChangeLog  
	 * @Description: 同步3-5处理tb_five_level_classify_business_change_log表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  null
	 */
	private void synchFiveLevelClassifyBusinessChangeLog() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchFiveLevelClassifyBusinessChangeLog()");
		}
		repaymentBizPlanListSynchService.updateFiveLevelClassifyBusinessChangeLog();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchFiveLevelClassifyBusinessChangeLog()");
		}
	}
	
	/**
	 * @Title: synchBasicBusinessCustomer  
	 * @Description: 同步3-6处理tb_basic_business_customer表更新 
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  null
	 */
	private void synchBasicBusinessCustomer() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchBasicBusinessCustomer()");
		}
		repaymentBizPlanListSynchService.updateBasicBusinessCustomer();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchBasicBusinessCustomer()");
		}
	}
	
	/**
	 * @Title: synchRepaymentBizPlanListDetail  
	 * @Description: 同步3-7处理tb_repayment_biz_plan_list_detail表更新
	 * @param     参数  
	 * @return void    返回类型  
	 * @throws  
	 */
	private void synchRepaymentBizPlanListDetail() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering synchRepaymentBizPlanListDetail()");
		}
		repaymentBizPlanListSynchService.updateRepaymentBizPlanListDetail();
		if (logger.isDebugEnabled()) {
			logger.debug("exiting synchRepaymentBizPlanListDetail()");
		}
	}

}

