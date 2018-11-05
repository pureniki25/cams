/**
 * 
 */
package com.hongte.alms.tool.fixPlanlistRemark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjFactRepayService;
import com.hongte.alms.base.service.RepaymentResourceService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.finance.SettleFeesVO;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;

/**
 * @author 王继光
 * 2018年9月30日 下午5:02:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
@ComponentScan({"com.hongte.alms"})
public class FixRemark {
	static Logger logger = LoggerFactory.getLogger(FixRemark.class);
	
	@Autowired
	RepaymentBizPlanListMapper bizPlanListMapper ;
	
	@Autowired
	RepaymentConfirmLogMapper confirmLogMapper ;
	
	@Autowired
	@Qualifier("RepaymentConfirmLogService")
	RepaymentConfirmLogService repaymentConfirmLogService;
	@Autowired
	@Qualifier("RepaymentResourceService")
	RepaymentResourceService repaymentResourceService;
	@Autowired
	@Qualifier("WithholdingRepaymentLogService")
	private WithholdingRepaymentLogService withholdingRepaymentLogService;
	@Autowired
	@Qualifier("AccountantOverRepayLogService")
	private AccountantOverRepayLogService accountantOverRepayLogService;
	@Autowired
	@Qualifier("RepaymentProjFactRepayService")
	RepaymentProjFactRepayService repaymentProjFactRepayService;
	@Autowired
	MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;
	
	final String UPDATE_TEMPLATE = "update tb_repayment_biz_plan_list set remark = {0} where business_id = {1} and after_id = {2} ; \r\n" ;
	
	final String[] businessList = {"TDF2022018070301",
			"TDF2082018062904",
			"TDF002018062806",
			"TDF002018061307",
			"TDF2062018070401",
			"TDF2092018060501",
			"TDF2022018070503",
			"TDF2012018062509",
			"TDF2082018070409",
			"TDF2012018062707",
			"TDF2042018062811",
			"TDF2072018070201",
			"TDF2072018070302",
			"TDF2022018070701",
			"TDF2082018070801",
			"TDF2082018070208",
			"TDF2032018070501",
			"TDF8442018062503",
			"TDF2062018062501",
			"TDF2042018062504",
			"TDF2032018062509",
			"TDF8442018062502",
			"TDF2082018062206",
			"TDF2082018060805",
			"TDF2062018062703",
			"TDF2022018061401",
			"TDF2032018060801",
			"TDF2032018062506",
			"TDF2082018062604",
			"TDF2032018061903",
			"TDF2032018062015",
			"TDF2032018062605",
			"TDF2072018062701",
			"TDF8442018062103",
			"TDF2012018062604",
			"TDF2092018061904",
			"TDF2062018062606",
			"TDF2092018062502",
			"TDF8442018062602",
			"TDF002018062704",
			"TDF2092018062705"} ;
	
	@Value("${spring.datasource.url}")
	String URL ;
	public void fix() {
		StringBuffer buffer = new StringBuffer() ;
		for (String businessId : businessList) {
			List<RepaymentConfirmLog> confirmLogs = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().where(" business_id = {0} and surplus_amount > 0 and is_cancelled = 0  ", businessId));
			for (RepaymentConfirmLog repaymentConfirmLog : confirmLogs) {
				RepaymentBizPlanList planList= bizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", repaymentConfirmLog.getBusinessId()).eq("after_id", repaymentConfirmLog.getAfterId())).get(0);
				String remark = planList.getRemark();
				String fault = repaymentConfirmLog.getSurplusAmount().multiply(new BigDecimal(2)).toString() ;
				remark = remark.replaceAll(fault, repaymentConfirmLog.getSurplusAmount().setScale(2, RoundingMode.HALF_UP).toString()) ;
				String updateStr = MessageFormat.format(UPDATE_TEMPLATE, "'"+remark+"'","'"+repaymentConfirmLog.getBusinessId()+"'","'"+repaymentConfirmLog.getAfterId()+"'");
				buffer.append(updateStr).append("\r\r");
			}
		}
		
		System.out.println(buffer.toString());
	}
	
	
	@Test
	public void fix2() {
		final String businessId = "TDXQS1012018082801" ;
		final String afterId = "1-02" ;
		RepaymentBizPlanList bizPlanList = bizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>()
				.eq("business_id", businessId).eq("after_id", afterId)).get(0);
		List<RepaymentConfirmLog> repayLogs = repaymentConfirmLogService
				.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", bizPlanList.getBusinessId())
						.eq("after_id", bizPlanList.getAfterId()).eq("is_cancelled", 0).eq("type", 1)
						.orderBy("create_time"));
		BigDecimal count = BigDecimal.ZERO;
		List<RepaymentResource> allResource = new ArrayList<>();
		List<RepaymentProjFactRepay> allFactRepay = new ArrayList<>();
		for (RepaymentConfirmLog repaymentConfirmLog : repayLogs) {
			count = repaymentConfirmLog.getFactAmount().add(count);

			List<RepaymentResource> repaymentResources = repaymentResourceService
					.selectList(new EntityWrapper<RepaymentResource>()
							.eq("confirm_log_id", repaymentConfirmLog.getConfirmLogId()).eq("is_cancelled", 0)
							.orderBy("repay_date"));

			if (!CollectionUtils.isEmpty(repaymentResources)) {
				allResource.addAll(repaymentResources);
			}

			List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayService
					.selectList(new EntityWrapper<RepaymentProjFactRepay>()
							.eq("confirm_log_id", repaymentConfirmLog.getConfirmLogId()).eq("is_cancelled", 0));

			if (!CollectionUtils.isEmpty(factRepays)) {
				allFactRepay.addAll(factRepays);
			}
		}

//		for (RepaymentResource resource : financeBaseDto.getRepaymentResources()) {
//			allResource.add(resource);
//			count = resource.getRepayAmount().add(count);
//		}
//		allFactRepay.addAll(financeBaseDto.getProjFactRepayArray());

		StringBuffer remark = new StringBuffer();
		remark.append("备注:\r\n");
		for (RepaymentResource repaymentResource : allResource) {
			remark.append(DateUtil.formatDate(repaymentResource.getRepayDate()));
			remark.append("  ");
			if (repaymentResource.getRepaySource().equals("10")) {
				MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper
						.selectById(repaymentResource.getRepaySourceRefId());
				remark.append(moneyPoolRepayment.getBankAccount());
			}
			if (repaymentResource.getRepaySource().equals("20") || repaymentResource.getRepaySource().equals("21")
					|| repaymentResource.getRepaySource().equals("30")
					|| repaymentResource.getRepaySource().equals("31")) {
				WithholdingRepaymentLog log = withholdingRepaymentLogService
						.selectById(repaymentResource.getRepaySourceRefId());
				if (repaymentResource.getRepaySource().equals("20")
						|| repaymentResource.getRepaySource().equals("21")) {
					if (log.getBindPlatformId().equals(PlatformEnum.YB_FORM.getValue())) {
						remark.append("易宝代扣");
					}
					if (log.getBindPlatformId().equals(PlatformEnum.BF_FORM.getValue())) {
						remark.append("宝付代扣");
					}
				}
				if (repaymentResource.getRepaySource().equals("30")
						|| repaymentResource.getRepaySource().equals("31")) {
					remark.append("银行代扣");
				}

			}
			remark.append(RepayPlanRepaySrcEnum.descOf(Integer.valueOf(repaymentResource.getRepaySource())));
			remark.append("收到  ").append(repaymentResource.getRepayAmount()).append("元").append("\r\n");
		}
		remark.append("合计:  ").append(count.setScale(2, RoundingMode.HALF_UP)).append("元").append("\r\n");
		remark.append("明细:\r\n");

		List<SettleFeesVO> feesVOs = new ArrayList<>();
		for (RepaymentProjFactRepay factRepay : allFactRepay) {
			if (factRepay.getFactAmount().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			boolean contain = false;
			for (SettleFeesVO settleFeesVO : feesVOs) {
				if (settleFeesVO.getFeeId().equals(factRepay.getFeeId())) {
					contain = true;
					settleFeesVO.setAmount(settleFeesVO.getAmount().add(factRepay.getFactAmount()));
					break;
				}
			}
			if (!contain) {
				SettleFeesVO feesVO = new SettleFeesVO();
				feesVO.setFeeId(factRepay.getFeeId());
				feesVO.setAmount(factRepay.getFactAmount());
				feesVO.setPlanItemName(factRepay.getPlanItemName());
				if (factRepay.getPlanItemType().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT.getValue())) {
					if (factRepay.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid())) {
						feesVO.setPlanItemName(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getDesc());
					}
					if (factRepay.getFeeId().equals(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid())) {
						feesVO.setPlanItemName(RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getDesc());
					}
				}
				feesVOs.add(feesVO);
			}
		}

		for (SettleFeesVO settleFeesVO : feesVOs) {
			remark.append(settleFeesVO.getAmount().setScale(2, RoundingMode.HALF_UP)).append("元")
					.append(settleFeesVO.getPlanItemName()).append(",");
		}

		BigDecimal balance = BigDecimal.ZERO;
		if (repayLogs.get(repayLogs.size()-1).getSurplusAmount() != null) {
			balance = balance.add(repayLogs.get(repayLogs.size()-1).getSurplusAmount());
		}
		if (balance.compareTo(BigDecimal.ZERO) > 0) {
			remark.append(balance.setScale(2, RoundingMode.HALF_UP)).append("元").append("结余");
		}
		remark.append(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss",
				repayLogs.get(repayLogs.size()-1).getCreateTime()));

		System.out.println(remark);
		
		System.out.println(MessageFormat.format("update tb_repayment_biz_plan_list set remark = {0} where business_id = {1} and after_id = {2} ;",
				"'"+remark+"'","'"+businessId+"'","'"+afterId+"'"));
//		bizPlanList.setRemark(remark.toString());
//		bizPlanList.updateAllColumnById();
	}
}
