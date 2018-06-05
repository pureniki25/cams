package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 款项池业务关联表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
@Service("MoneyPoolRepaymentService")
public class MoneyPoolRepaymentServiceImpl extends BaseServiceImpl<MoneyPoolRepaymentMapper, MoneyPoolRepayment> implements MoneyPoolRepaymentService {
	
	@Autowired
	private MoneyPoolMapper moneyPoolMapper;
	@Autowired
	private MoneyPoolRepaymentMapper moneyPoolRepaymentMapper;
	
	@Override
	@Transactional(rollbackFor=ServiceRuntimeException.class)
	public boolean deleteFinanceAddStatement(String mprId) {
		if (mprId==null) {
			throw new ServiceRuntimeException("mprid 不能为空");
		}
		MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(mprId);
		if (moneyPoolRepayment==null) {
			throw new ServiceRuntimeException("找不到此还款登记");
		}
		if (StringUtil.isEmpty(moneyPoolRepayment.getMoneyPoolId())) {
			throw new ServiceRuntimeException("此还款登记没关联银行流水");
		}
		MoneyPool moneyPool = moneyPoolMapper.selectById(moneyPoolRepayment.getMoneyPoolId());
		if (moneyPool.getStatus().equals(RepayRegisterState.完成.toString())) {
			throw new ServiceRuntimeException("已完成的银行流水不能被删除");
		}
		boolean updateMP = moneyPool.deleteById();
		boolean updateMPR = moneyPoolRepayment.deleteById();
		if (updateMP&&updateMPR) {
			return true ;
		}
		throw new ServiceRuntimeException("更新数据库失败");
	}

}
