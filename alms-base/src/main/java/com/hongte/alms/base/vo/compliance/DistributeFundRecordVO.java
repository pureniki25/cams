package com.hongte.alms.base.vo.compliance;

import java.io.Serializable;
import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;

public class DistributeFundRecordVO extends TdrepayRechargeLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 费用明细
	 */
	private List<TdrepayRechargeDetail> details;

	/**
	 * 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
	 */
	private String processStatusStr;

	/**
	 * 实还日期
	 */
	private String factRepayDateStr;

	/**
	 * 资金分发日期
	 */
	private String createTimeStr;

	public List<TdrepayRechargeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TdrepayRechargeDetail> details) {
		this.details = details;
	}

	public String getProcessStatusStr() {
		return processStatusStr;
	}

	public void setProcessStatusStr(String processStatusStr) {
		this.processStatusStr = processStatusStr;
	}

	public String getFactRepayDateStr() {
		return factRepayDateStr;
	}

	public void setFactRepayDateStr(String factRepayDateStr) {
		this.factRepayDateStr = factRepayDateStr;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	@Override
	public String toString() {
		return "DistributeFundRecordVO [details=" + details + ", processStatusStr=" + processStatusStr
				+ ", factRepayDateStr=" + factRepayDateStr + ", createTimeStr=" + createTimeStr + "]";
	}

}
