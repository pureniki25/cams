/**
 * 
 */
package com.hongte.alms.finance.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.FactRepayReq;
import com.hongte.alms.base.entity.RepaymentConfirmLogSynch;
import com.hongte.alms.base.service.RepaymentConfirmLogSynchService;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.FinanceServiceApplication;

/**
 * @author 王继光 2018年10月9日 下午9:25:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class RepaymentConfirmLogSynchServiceTest {

	@Autowired
	@Qualifier("RepaymentConfirmLogSynchService")
	private RepaymentConfirmLogSynchService service;

	@Test
	public void test() {
		FactRepayReq req = new FactRepayReq() ;
		req.setCurPage(2);
		req.setPageSize(20);
		req.setConfirmStart("2018-08-01");
		req.setConfirmEnd("2018-08-30");
		req.setRepayType("混合代扣");
		Page<RepaymentConfirmLogSynch> page = service.page(req);
		PageResult.success(page.getRecords(), page.getTotal());
		System.out.println(JSON.toJSONString(page));
	}
	
}
