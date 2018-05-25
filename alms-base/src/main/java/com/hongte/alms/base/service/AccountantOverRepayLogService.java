package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.List;

import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 业务结余暂收日志表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-24
 */
public interface AccountantOverRepayLogService extends BaseService<AccountantOverRepayLog> {

	/**
	 * 查询某业务的结余记录
	 * @author 王继光
	 * 2018年5月24日 上午10:48:40
	 * @param businessId
	 * @param afterId,期数,为null时查business所有,非null时查对应期
	 * @param income,true=收入,false=支出
	 * @return
	 */
	public List<AccountantOverRepayLog> select(String businessId,String afterId,boolean income);
	/**
	 * 计算可用结余
	 * @author 王继光
	 * 2018年5月24日 上午10:50:13
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public BigDecimal caluCanUse(String businessId,String afterId);
	
} 
