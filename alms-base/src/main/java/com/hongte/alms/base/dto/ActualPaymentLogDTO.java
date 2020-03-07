package com.hongte.alms.base.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ActualPaymentLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ActualPaymentSingleLogDTO> actualPaymentSingleLogDTOs;

	private String afterId;
	/**
	 * 平台期数
	 */
	private Integer period; 

	/**
	 * 实收总计
	 */
	private double receivedTotal;
}
