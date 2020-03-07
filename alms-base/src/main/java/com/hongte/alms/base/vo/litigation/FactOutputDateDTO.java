package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class FactOutputDateDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 出款时间
	 */
	private Date factOutputDate;
}
