package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RepaymentNameDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 出款人
	 */
	private String repaymentName;

}
