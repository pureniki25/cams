package com.hongte.alms.base.mapper;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleProjDto;
import com.hongte.alms.base.entity.RepaymentSettleLog;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 还款结清日志记录表 Mapper 接口
 * </p>
 *
 * @author lxq
 * @since 2018-07-11
 */
public interface RepaymentSettleLogMapper extends SuperMapper<RepaymentSettleLog> {
    /**
     * 整个业务表的还款详情
     * @param businessId
     * @return
     */
    List<RepaymentSettleMoneyDto> selectProjPlanMoney(@Param("businessId")  String businessId, @Param("planId") String planId);

    List<RepaymentSettleProjDto> orderSettleProj(@Param("planId") String planId);
}
