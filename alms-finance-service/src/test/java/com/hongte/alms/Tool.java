/**
 * 
 */
package com.hongte.alms;

import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;

import cn.hutool.core.io.FileUtil;

/**
 * @author 王继光
 * 2018年9月6日 下午8:47:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class Tool {

	@Autowired
	RepaymentConfirmLogMapper repaymentConfirmLogMapper ;
	
	static final String T = "INSERT INTO `hongte_alms`.`tb_accountant_over_repay_log`(`log_id`,  `business_id`, `business_after_id`, `over_repay_money`, `money_type`, `freeze_status`, `is_refund`, `is_temporary`, `remark`,`src_type`, `create_time`, `create_user`) VALUES ({0},{1},{2},{3}, 1, 0, 0, 0, {4},2,{5}, {6} );\r\n" ;
	static final String T1 = "update tb_repayment_confirm_log set surplus_ref_id = {0} where confirm_log_id = {1} ;\r\n" ;
	@Test
	public void fixRepaymentconfirmLog() {
		List<RepaymentConfirmLog> confirmLogs = repaymentConfirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().where(" surplus_ref_id is not null and surplus_amount != 0 and business_id != (select business_id from tb_accountant_over_repay_log where id = surplus_ref_id )  "));
//		List<RepaymentConfirmLog> confirmLogs = repaymentConfirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().where(" surplus_ref_id is not null and surplus_amount != 0  "));
		StringBuffer insert = new StringBuffer() ;
		StringBuffer update = new StringBuffer() ;
		for (RepaymentConfirmLog repaymentConfirmLog : confirmLogs) {
			String uuid = UUID.randomUUID().toString();
			insert.append(MessageFormat.format(T, 
					"'"+uuid+"'",//log_id
					"'"+repaymentConfirmLog.getBusinessId()+"'",//business_id
					"'"+repaymentConfirmLog.getAfterId()+"'",//business_after_id
					repaymentConfirmLog.getSurplusAmount().toString(),//over_repay_money
					"'收入于"+repaymentConfirmLog.getBusinessId()+" "+repaymentConfirmLog.getAfterId()+"财务确认'",//remark
					"'"+DateUtil.formatDate("yyyy-MM-dd HH:mm:ss", new Date())+"'",//create_time
					repaymentConfirmLog.getCreateUser()==null ? "null":"'"+repaymentConfirmLog.getCreateUser()+"'"
					));
			
			update.append(MessageFormat.format(T1, 
					"'"+uuid+"'",
					"'"+repaymentConfirmLog.getConfirmLogId()+"'")) ;
		}
		
		System.out.println(insert.toString());
		System.out.println(update.toString());
		
		StringBuffer base = new StringBuffer() ;
		base.append("/*").append("\r\n");
		base.append("-- 系统:   贷后管理系统").append("\r\n");
		base.append("-- 开发：  王继光  ").append("\r\n");
		base.append("-- 日期：   2018-9-13").append("\r\n");
		base.append("-- 数据库： hongte_alms").append("\r\n");
		base.append("-- 发布时间：2018-9-13").append("\r\n");
		base.append("-- 发布范围:  uat").append("\r\n");
		base.append("-- 发布方式： 在线发布").append("\r\n");
		base.append("-- uat结余表补充结余记录").append("\r\n");
		base.append("*/").append("\r\n");
		base.append("use hongte_alms;").append("\r\n");
		base.append("\r\n");
		FileUtil.appendUtf8String(base.append(insert.append(update)).toString(), new File("output.sql"));
	}
}
