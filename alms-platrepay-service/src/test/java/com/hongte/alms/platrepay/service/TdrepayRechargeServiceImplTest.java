package com.hongte.alms.platrepay.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.service.TdrepayRechargeService;
import com.hongte.alms.base.vo.compliance.TdrepayRechargeInfoVO;
import com.hongte.alms.platrepay.PlatRepayServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlatRepayServiceApplication.class)
public class TdrepayRechargeServiceImplTest {
	
	@Autowired
	@Qualifier("TdrepayRechargeService")
	private TdrepayRechargeService tdrepayRechargeService;
	
	@Test
	public void testSaveTdrepayRechargeInfo() {
		TdrepayRechargeInfoVO infoVO = new TdrepayRechargeInfoVO();
		infoVO.setProjectId("a252b2a9-3141-4ead-a800-e4834cd051f5");
		infoVO.setAssetType(1);
		infoVO.setOrigBusinessId("aaaaa");
		infoVO.setBusinessType(1);
		infoVO.setFactRepayDate(new Date());
		infoVO.setCustomerName("aa");
		infoVO.setCompanyName("bb");
		infoVO.setRepaySource(1);
		infoVO.setConfirmTime(new Date());
		infoVO.setAfterId("1-01");
		infoVO.setPeriod(2);
		infoVO.setSettleType(1);
		infoVO.setResourceAmount(BigDecimal.valueOf(10));
		infoVO.setFactRepayAmount(BigDecimal.valueOf(10));
		infoVO.setRechargeAmount(BigDecimal.valueOf(10));
		infoVO.setIsComplete(0);
		infoVO.setProjPlanListId("cccc");
		
		TdrepayRechargeDetail detail = new TdrepayRechargeDetail();
		detail.setFeeName("aa");
		detail.setFeeType(10);
		detail.setFeeValue(BigDecimal.valueOf(100));
		TdrepayRechargeDetail detail2 = new TdrepayRechargeDetail();
		detail2.setFeeName("bb");
		detail2.setFeeType(10);
		detail2.setFeeValue(BigDecimal.valueOf(150));
		TdrepayRechargeDetail detail3 = new TdrepayRechargeDetail();
		detail3.setFeeName("cc");
		detail3.setFeeType(30);
		detail3.setFeeValue(BigDecimal.valueOf(300));
		
		List<TdrepayRechargeDetail> details = new LinkedList<>();
		details.add(detail);
		details.add(detail2);
		details.add(detail3);
		
		infoVO.setDetailList(details);
		
		System.out.println(JSONObject.toJSONString(infoVO));
		
		tdrepayRechargeService.saveTdrepayRechargeInfo(infoVO);
		
	}
	
	@Test
	public void testRePayComplianceWithRequirements() {
		tdrepayRechargeService.repayComplianceWithRequirements();
	}

}
