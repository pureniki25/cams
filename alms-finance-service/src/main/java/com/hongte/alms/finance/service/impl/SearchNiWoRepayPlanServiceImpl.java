package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.BizCustomerTypeEnum;
import com.hongte.alms.base.enums.BooleanEnum;
import com.hongte.alms.base.enums.BusinessSourceTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.NiWoProjPlanDto;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.base.RepayPlan.req.*;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import com.hongte.alms.finance.service.SearchNiWoRepayPlanService;
import com.ht.ussp.core.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.common.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author czs
 * @since 2018/6/13
 * 
 */
@Service("SearchNiWoRepayPlanService")
public class SearchNiWoRepayPlanServiceImpl  implements SearchNiWoRepayPlanService {


    private  static Logger logger = LoggerFactory.getLogger(SearchNiWoRepayPlanServiceImpl.class);


    @Autowired
    EipRemote  eipRemote;


	@Override
	public NiWoProjPlanDto getNiWoRepayPlan(String projId) {
		Map<String, Object> paramMap = new HashMap<>();
 		paramMap.put("orderNo", projId);
 		Result result=	eipRemote.queryApplyOrder(paramMap);
 		HashMap<String,String> map=(HashMap) result.getData();
 		NiWoProjPlanDto dto=new NiWoProjPlanDto();
 		System.out.println(map.get("orderNo").toString());
 		dto.setContractUrl(map.get("orderNo")==null?"":map.get("orderNo").toString());
 		dto.setOrderStatus(map.get("orderStatus")==null?0:Integer.valueOf(map.get("orderStatus").toString()));
 		dto.setOrderMsg(map.get("orderMsg")==null?"":map.get("orderMsg").toString());
 		dto.setContractUrl(map.get("contractUrl")==null?"":map.get("contractUrl").toString());
 		dto.setOrderNo(map.get("orderNo")==null?"":map.get("orderNo").toString());
 		dto.setProjectStatus(map.get("projectStatus")==null?0:Integer.valueOf(map.get("projectStatus").toString()));
 		dto.setWithdrawMsg(map.get("withdrawMsg")==null?"":map.get("withdrawMsg").toString());
 		dto.setWithdrawStatus(map.get("withdrawStatus")==null?0:Integer.valueOf(map.get("withdrawStatus").toString()));
 		dto.setWithdrawSuccessTime(map.get("withdrawSuccessTime")==null?0:Long.valueOf(map.get("withdrawSuccessTime").toString()));
 		dto.setWithdrawTime(map.get("withdrawTime")==null?0:Long.valueOf(map.get("withdrawTime").toString()));
 		//dto.setRepaymentPlan(map.get("repaymentPlan"));
 		
 		return  dto;
		
	}




	public static void main(String[] args) {
	//	String str="{	"orderNo" = 1, "orderStatus" = 1, "projectStatus" = 3, "orderMsg" = null, "withdrawStatus" = 0, "withdrawMsg" = null, "withdrawTime" = 0, "withdrawSuccessTime" = 0, "contractUrl" = null}} repaymentPlan=[]}";
//		String str="{orderNo=f4709f216d1042b79109ae1317d72c32, orderStatus=1, projectStatus=3, orderMsg=null, withdrawStatus=0, withdrawMsg=null, withdrawTime=0, withdrawSuccessTime=0, contractUrl=null}";
//		HashMap<String,HashMap<String,String>> map=str;
//		
//		System.out.println(str.length());
	}
}
