package com.hongte.alms.base.service;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import com.hongte.alms.base.entity.RepaymentSettleLog;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 还款结清日志记录表 服务类
 * </p>
 *
 * @author lxq
 * @since 2018-07-11
 */
public interface RepaymentSettleLogService extends BaseService<RepaymentSettleLog> {


    List<RepaymentSettleMoneyDto> selectProjPlanMoney(String businessId, String planId);
}
