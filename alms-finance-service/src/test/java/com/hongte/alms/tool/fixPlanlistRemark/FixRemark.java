/**
 * 
 */
package com.hongte.alms.tool.fixPlanlistRemark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
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
	@Test
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
}
