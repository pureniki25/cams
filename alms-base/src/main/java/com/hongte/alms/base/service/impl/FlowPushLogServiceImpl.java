package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.FlowPushLogMapper;
import com.hongte.alms.base.service.FlowPushLogService;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.core.Result;

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

	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
	      advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);
	    } catch (Exception e) {
	      LOGGER.error(e.getMessage(), e);
	    }
		return null;
	}

}
