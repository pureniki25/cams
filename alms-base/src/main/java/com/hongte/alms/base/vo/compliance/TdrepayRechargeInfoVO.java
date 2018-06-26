package com.hongte.alms.base.vo.compliance;

import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;

/**
 * 代充值资金分发参数
 * 
 * @author Administrator
 *
 */
public class TdrepayRechargeInfoVO extends TdrepayRechargeLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 团贷网合规化还款标的充值明细表
	 */
	private List<TdrepayRechargeDetail> detailList;

	/**
	 * 业务类型
	 */
	private String businessTypeStr;

	/**
	 * 还款方式
	 */
	private String repaymentTypeStr;

	/**
	 * 期数类型
	 */
	private String periodTypeStr;

	/**
	 * 平台状态
	 */
	private String platformTypeStr;

	/**
	 * 实还日期
	 */
	private String factRepayDateStr;

	/**
	 * 分发状态
	 */
	private String processStatusStr;

	public List<TdrepayRechargeDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<TdrepayRechargeDetail> detailList) {
		this.detailList = detailList;
	}

	public String getBusinessTypeStr() {
		return businessTypeStr;
	}

	public void setBusinessTypeStr(String businessTypeStr) {
		this.businessTypeStr = businessTypeStr;
	}

	public String getRepaymentTypeStr() {
		return repaymentTypeStr;
	}

	public void setRepaymentTypeStr(String repaymentTypeStr) {
		this.repaymentTypeStr = repaymentTypeStr;
	}

	public String getPeriodTypeStr() {
		return periodTypeStr;
	}

	public void setPeriodTypeStr(String periodTypeStr) {
		this.periodTypeStr = periodTypeStr;
	}

	public String getPlatformTypeStr() {
		return platformTypeStr;
	}

	public void setPlatformTypeStr(String platformTypeStr) {
		this.platformTypeStr = platformTypeStr;
	}

	public String getFactRepayDateStr() {
		return factRepayDateStr;
	}

	public void setFactRepayDateStr(String factRepayDateStr) {
		this.factRepayDateStr = factRepayDateStr;
	}

	public String getProcessStatusStr() {
		return processStatusStr;
	}

	public void setProcessStatusStr(String processStatusStr) {
		this.processStatusStr = processStatusStr;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeInfoVO [detailList=" + detailList + ", businessTypeStr=" + businessTypeStr
				+ ", repaymentTypeStr=" + repaymentTypeStr + ", periodTypeStr=" + periodTypeStr + ", platformTypeStr="
				+ platformTypeStr + ", factRepayDateStr=" + factRepayDateStr + ", processStatusStr=" + processStatusStr
				+ "]";
	}

}
