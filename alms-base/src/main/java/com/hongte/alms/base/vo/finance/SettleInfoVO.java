/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.util.DateUtil;

import lombok.Data;

/**
 * @author 王继光
 * 2018年6月14日 下午2:58:26
 */
@Data
public class SettleInfoVO {
	/**
	 * 本金	
	 */
	private BigDecimal item10 = BigDecimal.ZERO ;
	/**
	 * 利息	
	 */
	private BigDecimal item20 = BigDecimal.ZERO;
	/**
	 * 月收分公司服务费	
	 */
	private BigDecimal item30 = BigDecimal.ZERO;
	/**
	 * 月收平台费	
	 */
	private BigDecimal item50 = BigDecimal.ZERO;
	/**
	 * 小计	
	 */
	private BigDecimal subtotal = BigDecimal.ZERO;
	/**
	 * 逾期天数	
	 */
	private Integer overDueDays = 0 ;
	/**
	 * 线下逾期费	
	 */
	private BigDecimal offlineOverDue = BigDecimal.ZERO;
	/**
	 * 线上逾期费	
	 */
	private BigDecimal onlineOverDue = BigDecimal.ZERO;
	/**
	 * 提前结清违约金	
	 */
	private BigDecimal penalty = BigDecimal.ZERO;
	
	/**
	 * 根据planListId存储提前违约金
	 * Map<planListId, List<SettleFeesVO>>
	 */
	private Map<String, List<SettleFeesVO>> penaltyBiz = new HashMap<>() ;
	/**
	 * 减免金额	
	 */
	private BigDecimal derate = BigDecimal.ZERO;
	/**
	 * 应收差额(含滞纳金)	
	 */
	private BigDecimal planRepayBalance = BigDecimal.ZERO;
	/**
	 * 合计(含滞纳金)	
	 */
	private BigDecimal total = BigDecimal.ZERO;
	
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date repayPlanDate ;

	/**
	 * 其他费用项总额
	 */
	private BigDecimal other = BigDecimal.ZERO ;
	
	/**
	 * 减免项目
	 */
	private List<SettleFeesVO> derates ;
	
	public void setDerates(List<SettleFeesVO> list) {
		this.derates = list ;
		for (SettleFeesVO settleFeesVO : list) {
			this.derate = this.derate.add(settleFeesVO.getAmount());
		}
	}
	
	/**
	 * 其他费用项明细
	 */
	private List<SettleFeesVO> otherFees ;
	
	public void setOtherFees(List<SettleFeesVO> list) {
		this.otherFees = list;
		for (SettleFeesVO settleFeesVO : list) {
			if (settleFeesVO.getAmount()==null||settleFeesVO.getFeeName()==null) {
				continue;
			}
			this.other = this.other.add(settleFeesVO.getAmount());
		}
	}
	
	/**
	 * 往期少缴费用
	 */
	private List<SettleFeesVO> lackFees ;
	
	public void setLackFees(List<SettleFeesVO> list) {
		this.lackFees = list ;
		for (SettleFeesVO settleFeesVO : list) {
			this.planRepayBalance = this.planRepayBalance.add(settleFeesVO.getAmount());
		}
	}
	
	/**
	 * 提前结清违约金
	 */
	private List<SettleFeesVO> penaltyFees ;
	
	private List<SettleFeesVO> penaltyFeesBiz ;
	
	public void setPenaltyFees(List<SettleFeesVO> list) {
		this.penaltyFees = list ;
		Set<String> feeNames = new HashSet<>() ;
		for (SettleFeesVO settleFeesVO : list) {
			feeNames.add(settleFeesVO.getFeeName());
			this.penalty = this.penalty.add(settleFeesVO.getAmount()).setScale(2, RoundingMode.HALF_UP);
		}
		penaltyFeesBiz = new ArrayList<>() ;
		for (String string : feeNames) {
			SettleFeesVO vo = new SettleFeesVO() ;
			vo.setFeeName(string);
			BigDecimal amount = BigDecimal.ZERO ;
			for (SettleFeesVO fee : penaltyFees) {
				if (fee.getFeeName().equals(string)) {
					amount = amount.add(fee.getAmount());
				}
			}
			vo.setAmount(amount);
			penaltyFeesBiz.add(vo);
		}
		
	}
	
	/*public SettleInfoVO() {
		this.item10.setScale(2, RoundingMode.HALF_UP);
		this.item20.setScale(2, RoundingMode.HALF_UP);
		this.item30.setScale(2, RoundingMode.HALF_UP);
		this.item50.setScale(2, RoundingMode.HALF_UP);
		this.subtotal.setScale(2, RoundingMode.HALF_UP);
		this.offlineOverDue.setScale(2, RoundingMode.HALF_UP);
		this.onlineOverDue.setScale(2, RoundingMode.HALF_UP);
		this.penalty.setScale(2, RoundingMode.HALF_UP);
		this.derate.setScale(2, RoundingMode.HALF_UP);
		this.planRepayBalance.setScale(2, RoundingMode.HALF_UP);
		this.total.setScale(2, RoundingMode.HALF_UP);
	}*/
}
