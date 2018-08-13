package com.hongte.alms.base.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
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
     * @author 王继光
     * 2018年8月13日 下午9:13:29
     * @param businessId
     * @param afterId
     * @param isRevokeSettle 是否撤销结清,true=撤销结清,false=撤销还款
     * @return
     * @throws Exception
     */
    public Result revokeConfirm(String businessId,String afterId,boolean isRevokeSettle) throws Exception;
    
    /**
     * 本期已确认还款标的信息
     * @author 王继光
     * 2018年5月26日 下午8:56:39
     * @param businessId
     * @param afterId
     * @return
     */
    public List<JSONObject> selectCurrentPeriodConfirmedProjInfo(String businessId,String afterId);


}
