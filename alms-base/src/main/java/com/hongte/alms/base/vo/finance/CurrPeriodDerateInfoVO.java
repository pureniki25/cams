/**
 * 
 */
package com.hongte.alms.base.vo.finance;

import java.math.BigDecimal;
import java.util.List;

import com.hongte.alms.base.entity.ApplyDerateType;

/**
 * @author 王继光
 * 2018年5月14日 下午7:49:05
 */
public class CurrPeriodDerateInfoVO {

	private List<ApplyDerateType> list ;
	private BigDecimal total ;
	public List<ApplyDerateType> getList() {
		return list;
	}
	public void setList(List<ApplyDerateType> list) {
		BigDecimal total = new BigDecimal("0");
		for (ApplyDerateType applyDerateType : list) {
			total = total.add(applyDerateType.getDerateMoney());
		}
		this.total = total ;
		this.list = list;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
