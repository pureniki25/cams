package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 还款确认日志记录表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-25
 */
public interface RepaymentConfirmLogService extends BaseService<RepaymentConfirmLog> {
	/**
     * 撤销还款确认
     * @author 王继光
     * 2018年5月26日 下午12:46:50
     * @param businessId
     * @param afterId
     * @return
     */
    public Result revokeConfirm(String businessId,String afterId);
}
