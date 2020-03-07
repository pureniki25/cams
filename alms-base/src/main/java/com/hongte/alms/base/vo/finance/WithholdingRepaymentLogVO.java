package com.hongte.alms.base.vo.finance;

import java.io.Serializable;

import com.hongte.alms.base.entity.WithholdingRepaymentLog;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WithholdingRepaymentLogVO extends WithholdingRepaymentLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 代扣结果（1、成功；0、失败）
	 */
	private String repayStatusStr;

	/**
	 * 代扣平台名称
	 */
	private String platformName;

}
