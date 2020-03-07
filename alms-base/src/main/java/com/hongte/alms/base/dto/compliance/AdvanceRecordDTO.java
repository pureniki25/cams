package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 项目垫付记录 6.28之前上标数据
 * 
 * @author huweiqian
 *
 */

@Data
public class AdvanceRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * 标的ID
     */
	private String projectId;
    /**
     * 垫付ID
     */
	private String advanceId;
    /**
     * 垫付金额
     */
	private BigDecimal advanceAmount;
    /**
     * 是否已还:true 1/false 0
     */
	private Integer isRefund;
    /**
     * 滞纳金
     */
	private BigDecimal penalty;
    /**
     * 平台期数
     */
	private Integer period;
    /**
     * 借款人ID(等于td_user_id)
     */
	private String borrowUserid;
    /**
     * 垫付人ID
     */
	private String overDueUserId;
    /**
     * 添加时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date addDate;
    /**
     * 还款时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundDate;
    /**
     * 是否结清(0:未结清,1:已结清)
     */
	private Integer isComplete;
    /**
     * 已还垫付金额(不包括还分润金额)
     */
	private BigDecimal refundAmount;

	/**
	 * 期数
	 */
	private int periodMerge;
	/**
	 * 总垫付金额
	 */
	private BigDecimal advanceAmountTotal;
	/**
	 * 总垫付滞纳金
	 */
	private BigDecimal penaltyTotal;
	/**
	 * 已还垫付金额
	 */
	private BigDecimal advanceAmountFactTotal;
	/**
	 * 已还垫付滞纳金
	 */
	private BigDecimal penaltyFactTotal;
	/**
	 * 未还垫付金额
	 */
	private BigDecimal advanceAmountSurplus;
	/**
	 * 未还滞纳金
	 */
	private BigDecimal penaltySurplus;
	/**
	 * 垫付人ID
	 */
	private String overDueUserIds;
}
