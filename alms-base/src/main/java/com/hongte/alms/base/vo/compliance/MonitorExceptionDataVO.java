package com.hongte.alms.base.vo.compliance;

import java.io.Serializable;

import com.hongte.alms.base.entity.TdrepayRechargeFailRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控合规化异常数据VO
 * 
 * @author huweiqian
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MonitorExceptionDataVO extends TdrepayRechargeFailRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 被充值人
	 */
	private String rechargeddName;
	
	/**
	 * 计数
	 */
	private int count;
	
	/**
	 * 总数
	 */
	private int total;
}
