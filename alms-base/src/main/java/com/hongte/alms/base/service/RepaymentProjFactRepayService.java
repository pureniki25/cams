package com.hongte.alms.base.service;

import java.math.BigDecimal;

import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 标实还明细表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjFactRepayService extends BaseService<RepaymentProjFactRepay> {
	/**
	 * 计算本期剩余未还总金额
	 * @author 王继光
	 * 2018年5月24日 下午8:14:42
	 * @param businessId
	 * @param afterId
	 * @return
	 */
	public BigDecimal caluUnpaid(String businessId,String afterId) ;
	
}
