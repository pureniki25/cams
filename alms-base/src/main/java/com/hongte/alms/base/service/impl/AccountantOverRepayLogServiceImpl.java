package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务结余暂收日志表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-24
 */
@Service("AccountantOverRepayLogService")
public class AccountantOverRepayLogServiceImpl extends BaseServiceImpl<AccountantOverRepayLogMapper, AccountantOverRepayLog> implements AccountantOverRepayLogService {

	@Autowired
	private AccountantOverRepayLogMapper accountantOverRepayLogMapper ;

	@Override
	public List<AccountantOverRepayLog> select(String businessId,String afterId, boolean income) {
		EntityWrapper<AccountantOverRepayLog> ew = new EntityWrapper<>() ;
		ew.eq("business_id", businessId);
		if (afterId!=null) {
			ew.eq("business_after_id", afterId);
		}
		if (income) {
			ew.eq("money_type", 1);
		}else {
			ew.eq("money_type",0);
		}
		return accountantOverRepayLogMapper.selectList(ew);
	}

	@Override
	public BigDecimal caluCanUse(String businessId, String afterId) {
		List<AccountantOverRepayLog> income = select(businessId, afterId, true);
		List<AccountantOverRepayLog> output = select(businessId, afterId, false);
		BigDecimal canUse = new BigDecimal(0);
		for (AccountantOverRepayLog accountantOverRepayLog : income) {
			if (accountantOverRepayLog.getFreezeStatus()==null||accountantOverRepayLog.getFreezeStatus().equals(0)) {
				canUse = canUse.add(accountantOverRepayLog.getOverRepayMoney());
			}
		}
		for (AccountantOverRepayLog accountantOverRepayLog : output) {
			canUse = canUse.subtract(accountantOverRepayLog.getOverRepayMoney());
		}
		
		return canUse;
	}

	
	

}
