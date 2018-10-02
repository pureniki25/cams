/**
 * 
 */
package com.hongte.alms.tool.addRepaymentScheduleForHistory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @author 王继光
 * 2018年9月25日 上午11:02:40
 */
@Data
public class Req implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 4495341836135520429L;
	
	private String projectId ;
	private String orgType ;
	private String contractNo ;
	private BigDecimal amount ;
	private BigDecimal interestRate ;
	private Integer repaymentType ;
	private Integer deadline ;
	private Integer periods ;
	private List<RepaymentSchedule> repaymentSchedules ;
	
}
