package com.hongte.alms.base.vo.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DistributeFundRecordVO extends TdrepayRechargeLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 费用明细
	 */
	private List<TdrepayRechargeDetail> details;

	/**
	 * 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
	 */
	private String processStatusStr;

	/**
	 * 实还日期
	 */
	private String factRepayDateStr;

	/**
	 * 资金分发日期
	 */
	private String createTimeStr;

	/**
	 * 被充值人姓名
	 */
	private String person;

	/**
	 * 剩余垫付未还记录
	 */
	private BigDecimal totalSurplusAdvance;

}
