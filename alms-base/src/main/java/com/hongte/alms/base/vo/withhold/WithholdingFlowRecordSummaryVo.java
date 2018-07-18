package com.hongte.alms.base.vo.withhold;

import java.math.BigDecimal;

import lombok.Data;

/**
 * <p>
 * 流水总览数据VO
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-17
 */
@Data
public class WithholdingFlowRecordSummaryVo{
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 总手续费
     */
    private BigDecimal totalServiceCharge;
    /**
     * 总笔数
     */
    private Integer totalNumber;
}