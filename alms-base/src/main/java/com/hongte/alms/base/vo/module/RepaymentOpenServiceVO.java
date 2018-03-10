package com.hongte.alms.base.vo.module;


import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author chenzs
 * @since 2018/3/09
 */
public class RepaymentOpenServiceVO {

	private static final long serialVersionUID = 1L;

    private String planId;
    private String planListId;
    private String planDetailId;
    private String feeId;
    
    public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanListId() {
		return planListId;
	}
	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}
	public String getPlanDetailId() {
		return planDetailId;
	}
	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}
	public String getFeeId() {
		return feeId;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}


 
}
