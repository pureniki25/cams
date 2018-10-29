/**
 * 
 */
package com.hongte.alms.tool.addRepaymentScheduleForHistory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.compliance.TdPlatformPlanRepaymentDTO;
import com.hongte.alms.base.dto.compliance.TdProjectPaymentInfoResult;
import com.hongte.alms.base.dto.compliance.TdRefundMonthInfoDTO;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.dto.TdProjectPaymentDTO;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.TuandaiProjectInfoMapper;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;
import com.ht.ussp.core.Result;
import com.ht.ussp.core.ReturnCodeEnum;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 历史标的补发给平台,只补发6月28后生成且在平台处于还款中的期数
 * @author 王继光 2018年9月25日 上午10:12:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
@EnableFeignClients
public class Main {
	@Autowired
	@Qualifier("api")
	AddRepaymentScheduleForHistory api;

	@Autowired
	TuandaiProjectInfoMapper tuandaiProjectInfoMapper;

	@Autowired
	RepaymentProjPlanListMapper repaymentProjPlanListMapper;

	@Autowired
	RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper;

	@Autowired
	BasicBusinessMapper basicBusinessMapper;

	private Logger logger = LoggerFactory.getLogger(Main.class);

	@Test
	public void run() {

		 List<TuandaiProjectInfo> selectList = tuandaiProjectInfoMapper.selectList(new EntityWrapper<TuandaiProjectInfo>()
				 .where(" project_id in  ('c8c82dba-1c06-4633-9b02-c5d9f44496ae','9f63058f-5afb-4207-8687-20a503fa799b','87689d7f-56eb-4522-9f8e-059b034eaaef','2431605c-692f-44a4-90f8-5a5afa6e5561','0416acb9-96f7-40bc-baaf-2ce6fc7b4e10','88b7ef6e-8c88-492b-8f89-46b7ded4ca74','01d94f7b-5e0b-4064-9989-4aa313ffba46','0a551c0f-2aeb-4c34-82fd-9b196e391c9f','b0569ba5-0144-42dd-8f9f-c2fdfd1e9853') ").orderBy("create_time"));
//		List<TuandaiProjectInfo> selectList = tuandaiProjectInfoMapper
//				.selectList(new EntityWrapper<TuandaiProjectInfo>()
//						.where(" PLATE_TYPE = 1 AND DATE(CREATE_TIME) >= '2018-06-28' AND DATE(CREATE_TIME) <= '2018-10-25' ").orderBy("create_time"));
		for (TuandaiProjectInfo tuandaiProjectInfo : selectList) {
			Req req = new Req();

			BasicBusiness basicBusiness = basicBusinessMapper.selectById(tuandaiProjectInfo.getBusinessId());
			if (basicBusiness == null) {
				logger.error("没有找到业务");
				continue;
			}

			Integer businessType = basicBusiness.getBusinessType();
			if (businessType == null) {
				logger.error("没有找到业务类型");
				continue;
			}

			// 25 信用贷 特殊处理
			if (businessType.intValue() == 25) {
				switch (basicBusiness.getBusinessCtype()) {
				case "小微企业贷用信":
					if (tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
						businessType = 30;
					} else {
						businessType = 28;
					}
					break;
				case "业主信用贷用信":
					if (tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
						businessType = 26;
					} else {
						businessType = 29;
					}
					break;
				default:
					break;
				}
			} else if ((businessType.intValue() == 1 || businessType.intValue() == 9)
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 31;
			} else if ((businessType.intValue() == 2 || businessType.intValue() == 11 || businessType.intValue() == 35)
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 32;
			} else if (businessType.intValue() == 20
					&& !tuandaiProjectInfo.getProjectId().equals(tuandaiProjectInfo.getMasterIssueId())) {
				businessType = 33;
			}

			req.setAmount(tuandaiProjectInfo.getFullBorrowMoney());
			req.setContractNo(tuandaiProjectInfo.getBusinessId());
			req.setDeadline(tuandaiProjectInfo.getBorrowLimit());
			req.setInterestRate(basicBusiness.getBorrowRate());
			// 接口 ==> 0：商贸贷 1：业主贷 2：家装分期 3：商贸共借
			// 4：业主共借，5：房贷，6：车贷，7：扶贫贷，8：二手车，9：车全，10：一点车贷，11：房贷共借
			// 本地 ==> '业务类型(9:车贷 11:房贷 35:信用贷 32:共借项目 36 农饲贷 41 二手车商贷 39 车全 47 闪贷 48 扶贫贷)'
			req.setOrgType(BusinessTypeEnum.getOrgTypeByValue(businessType) + "");
			// req.setOrgType(BusinessTypeEnum.getOrgTypeByValue(tuandaiProjectInfo.getProjectType())+"");
			req.setProjectId(tuandaiProjectInfo.getProjectId());
			req.setRepaymentType(tuandaiProjectInfo.getRepaymentType());

			Map<String, String> param = new HashMap<>();
			param.put("projectId", tuandaiProjectInfo.getProjectId());
			Result result = api.getProjectPayment(param);
			// Result queryRepaymentSchedule = api.queryRepaymentSchedule(param);
			req.setRepaymentSchedules(new ArrayList<>());
			if (result != null && result.getData() != null
					&& Constant.REMOTE_EIP_SUCCESS_CODE.equals(result.getReturnCode())) {

				if (result.getData() != null) {
					// 标的还款信息
					TdProjectPaymentInfoResult tdProjectPaymentInfoResult = JSONObject
							.parseObject(JSONObject.toJSONString(result.getData()), TdProjectPaymentInfoResult.class);

					List<TdRefundMonthInfoDTO> periodsList = tdProjectPaymentInfoResult.getPeriodsList();
					if (CollectionUtils.isEmpty(periodsList)) {
						continue;
					}
					for (TdRefundMonthInfoDTO tdRefundMonthInfoDTO : periodsList) {
						if (tdRefundMonthInfoDTO.getStatus() == 3) {
							RepaymentProjPlanList projPlanList = repaymentProjPlanListMapper.selectByProjectIdAndPeriod(
									tuandaiProjectInfo.getProjectId(), tdRefundMonthInfoDTO.getPeriods());

							if (projPlanList == null) {
								System.err.println(tuandaiProjectInfo.getProjectId() + " "
										+ tdRefundMonthInfoDTO.getPeriods() + " projPlanList is null");
								continue;
							}

							RepaymentSchedule schedule = new RepaymentSchedule();
							schedule.setPeriod(tdRefundMonthInfoDTO.getPeriods());

							List<RepaymentProjPlanListDetail> item10 = repaymentProjPlanListDetailMapper
									.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
											.eq("proj_plan_list_id", projPlanList.getProjPlanListId())
											.eq("fee_id", RepayPlanFeeTypeEnum.PRINCIPAL.getUuid()));
							List<RepaymentProjPlanListDetail> item20 = repaymentProjPlanListDetailMapper
									.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
											.eq("proj_plan_list_id", projPlanList.getProjPlanListId())
											.eq("fee_id", RepayPlanFeeTypeEnum.INTEREST.getUuid()));

							schedule.setAmount(item10.get(0).getProjPlanAmount());
							schedule.setInterestAmount(item20.get(0).getProjPlanAmount());
							
							
							if (req.getRepaymentSchedules().size()>0) {
								RepaymentSchedule removeOne = null ;
								for (RepaymentSchedule schedule2 : req.getRepaymentSchedules()) {
									if (schedule2.getPeriod().equals(schedule.getPeriod())) {
										schedule.setAmount(schedule2.getAmount().add(schedule.getAmount()));
										schedule.setInterestAmount(schedule2.getInterestAmount().add(schedule.getInterestAmount()));		
										removeOne = schedule2 ;
									}
								}
							
							
								if (removeOne!=null) {
									req.getRepaymentSchedules().remove(removeOne);
								}
							}
							
							req.getRepaymentSchedules().add(schedule);
						}
					}
				}
			}
		req.setPeriods(req.getRepaymentSchedules().size());
		if (req.getPeriods().equals(0)) {
			continue;
		}
		Result res = api.addRepaymentScheduleForHistory(req);
		if (res.getReturnCode().equals(ReturnCodeEnum.SUCCESS.getReturnCode())) {
			// System.out.println(JSON.toJSONString(result));
			System.out.println(JSON.toJSONString(result.getData()));
			System.out.println(JSON.toJSONString(req));
			System.out.println(JSON.toJSONString(res));
		} else {
			System.err.println(JSON.toJSONString(result.getData()));
			System.err.println(JSON.toJSONString(req));
			System.err.println(JSON.toJSONString(res));
		}
	}

	}

}
