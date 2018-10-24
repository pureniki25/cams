/**
 * 
 */
package com.hongte.alms.tool.addRepaymentScheduleForHistory;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author 王继光
 * 2018年9月25日 下午12:37:39
 */
@Data
public class RepaymentSchedule implements Serializable {
	private static final long serialVersionUID = -495259274341219022L;
	private Integer period ;
	private BigDecimal amount ;
	private BigDecimal interestAmount ;
}
