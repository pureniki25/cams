package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.FlowPushLogMapper;
import com.hongte.alms.base.service.FlowPushLogService;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.core.Result;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
@Service("FlowPushLogService")
public class FlowPushLogServiceImpl extends BaseServiceImpl<FlowPushLogMapper, FlowPushLog> implements FlowPushLogService {

	@Autowired
	private EipRemote eipRemote;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPushLogServiceImpl.class);
	
	@Override
	public List<DistributeFundRecordVO> queryDistributeFundRecord(String projectId) {
		Result queryProjectPaymentResult = null;
	    Result advanceShareProfitResult = null;
	    
	    Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projectId);
		DistributeFundRecordVO distributeFundRecordVO = new DistributeFundRecordVO();
	    try {
	      LOGGER.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", paramMap);
	      queryProjectPaymentResult = eipRemote.queryProjectPayment(paramMap); // 标的还款信息
	      LOGGER.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", JSONObject.toJSONString(queryProjectPaymentResult));
	      String returnCode = queryProjectPaymentResult.getReturnCode();
	      if("0000".equals(returnCode)) {
	    	 Map<String,Object> retData = (Map<String, Object>) queryProjectPaymentResult.getData();
	    	 String message = retData.get("message").toString();
	    	 //标的ID
	    	 String projectId1 = retData.get("projectId").toString();
	    	 String status = retData.get("status").toString();
	    	 //projectPayment还款信息list
	    	 List<Map<String,Object>> projectPayments = (List<Map<String, Object>>) retData.get("projectPayments");
	    	 if(null != projectPayments) {
	    		 for (Map<String, Object> map : projectPayments) {
	    			 //期数
	    			 int period = Integer.parseInt(map.get("period").toString());
	    			 //还款日期 yyyy-MM-dd
	    			 String addDate =  map.get("addDate").toString();
	    			 //还款状态1 已结清 0逾期
	    			 int projectPaymentsStatus = Integer.parseInt(map.get("status").toString());
	    			 //实还总金额
	    			 BigDecimal totalAmount = null == map.get("totalAmount")?null:new BigDecimal(map.get("totalAmount").toString());
	    			 //borrowerPayment借款人还款信息
	    			 //实还本息
	    			 BigDecimal principalAndInterest = null == map.get("principalAndInterest")?null:new BigDecimal(map.get("principalAndInterest").toString());
	    			 //实还平台服务费
	    			 BigDecimal tuandaiAmount = null == map.get("tuandaiAmount")?null:new BigDecimal(map.get("tuandaiAmount").toString());
	    			 //实还资产端服务费
	    			 BigDecimal orgAmount = null == map.get("orgAmount")?null:new BigDecimal(map.get("orgAmount").toString());
	    			 //实还担保公司服务费
	    			 BigDecimal guaranteeAmount = null == map.get("guaranteeAmount")?null:new BigDecimal(map.get("guaranteeAmount").toString());
	    			 //实还仲裁服务费
	    			 BigDecimal arbitrationAmount = null == map.get("arbitrationAmount")?null:new BigDecimal(map.get("arbitrationAmount").toString());
	    			 //实还中介服务费
	    			 BigDecimal agencyAmount = null == map.get("agencyAmount")?null:new BigDecimal(map.get("agencyAmount").toString());
	    			 //保险费
	    			 BigDecimal insuranceAmount = null == map.get("insuranceAmount")?null:new BigDecimal(map.get("insuranceAmount").toString());
	    			 //特权包
	    			 BigDecimal privilegePackageAmount = null == map.get("privilegePackageAmount")?null:new BigDecimal(map.get("privilegePackageAmount").toString());
	    			 //滞纳金
	    			 BigDecimal penaltyAmount = null == map.get("penaltyAmount")?null:new BigDecimal(map.get("penaltyAmount").toString());
	    			
	    			 //borrowerPayment借款人还款信息list，字段描述
	    			 Map<String,Object> borrowerPaymentMap = (Map<String, Object>) map.get("borrowerPayment");
	    			 if(null != borrowerPaymentMap) {
	    				 //实还本息
		    			 BigDecimal principalAndInterestBorrower = null == map.get("principalAndInterest")?null:new BigDecimal(map.get("principalAndInterest").toString());
		    			 //实还平台服务费
		    			 BigDecimal tuandaiAmountBorrower = null == map.get("tuandaiAmount")?null:new BigDecimal(map.get("tuandaiAmount").toString());
		    			 //实还资产端服务费
		    			 BigDecimal orgAmountBorrower = null == map.get("orgAmount")?null:new BigDecimal(map.get("orgAmount").toString());
		    			 //实还担保公司服务费
		    			 BigDecimal guaranteeAmountBorrower = null == map.get("guaranteeAmount")?null:new BigDecimal(map.get("guaranteeAmount").toString());
		    			 //实还仲裁服务费
		    			 BigDecimal arbitrationAmountBorrower = null == map.get("arbitrationAmount")?null:new BigDecimal(map.get("arbitrationAmount").toString());
		    			 //实还中介服务费
		    			 BigDecimal agencyAmountBorrower = null == map.get("agencyAmount")?null:new BigDecimal(map.get("agencyAmount").toString());
		    			 //保险费
		    			 BigDecimal insuranceAmountBorrower = null == map.get("insuranceAmount")?null:new BigDecimal(map.get("insuranceAmount").toString());
		    			 //特权包
		    			 BigDecimal privilegePackageAmountBorrower = null == map.get("privilegePackageAmount")?null:new BigDecimal(map.get("privilegePackageAmount").toString());
		    		 }
	    			 
	    			//guaranteePayment担保公司垫付信息，字段描述
	    			 Map<String,Object> guaranteePaymentMap = (Map<String, Object>) map.get("guaranteePayment");
	    			 if(null != guaranteePaymentMap) {
	    				 //本金利息
	    				 BigDecimal principalAndInterestGuarantee = null == map.get("principalAndInterest")?null:new BigDecimal(map.get("principalAndInterest").toString());
	    				 //滞纳金
	    				 BigDecimal penaltyAmountGuarantee = null == map.get("penaltyAmount")?null:new BigDecimal(map.get("penaltyAmount").toString());
	    				 //实还平台服务费
	    				 BigDecimal tuandaiAmountGuarantee = null == map.get("tuandaiAmount")?null:new BigDecimal(map.get("tuandaiAmount").toString());
	    				 //实还资产端服务费
	    				 BigDecimal orgAmountGuarantee = null == map.get("orgAmount")?null:new BigDecimal(map.get("orgAmount").toString());
	    				 //实还担保公司服务费
	    				 BigDecimal guaranteeAmountGuarantee = null == map.get("guaranteeAmount")?null:new BigDecimal(map.get("guaranteeAmount").toString());
	    				 //实还仲裁服务费
	    				 BigDecimal arbitrationAmountGuarantee = null == map.get("arbitrationAmount")?null:new BigDecimal(map.get("arbitrationAmount").toString());
	    				 //实还中介服务费
	    				 BigDecimal agencyAmountGuarantee = null == map.get("agencyAmount")?null:new BigDecimal(map.get("agencyAmount").toString());
	    			 }
	    		 }
	    	 }
	      }
//	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
//	      advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
//	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);
	    } catch (Exception e) {
	      LOGGER.error(e.getMessage(), e);
	    }
		return null;
	}

}
