package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 还垫付分润DTO（适用于6.28之前上标数据，还垫付分润接口）
 * @author huweiqian
 *
 */
@Data
public class ReturnAdvanceShareProfitForNetDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String projectId;
	/**
	 * 期数
	 */
	private Integer repaymentPeriods;
	/**
	 * 是否结清 (0否  1是)
	 */
	private Integer isComplete;
	/**
	 * 还垫付明细
	 */
	private List<ReturnAdvanceListDTO> returnAdvanceList;
}
