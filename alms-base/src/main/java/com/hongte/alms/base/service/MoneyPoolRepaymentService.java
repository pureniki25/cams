package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 款项池业务关联表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
public interface MoneyPoolRepaymentService extends BaseService<MoneyPoolRepayment> {
	/**
	 * 删除财务新增流水
	 * @author 王继光
	 * 2018年6月5日 上午9:56:53
	 * @param mprId
	 * @return
	 */
	public boolean deleteFinanceAddStatement(String mprId);
}
