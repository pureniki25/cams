/**
 * 
 */
package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 提前还款违约金计算服务
 * @author 王继光
 * 2018年5月30日 下午8:58:45
 */
public interface PaymentWithPenaltyService {
	BigDecimal caluPenalty(String businessId ,String afterId,Date settleDate);
}
