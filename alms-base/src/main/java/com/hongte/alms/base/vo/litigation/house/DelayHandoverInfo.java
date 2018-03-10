package com.hongte.alms.base.vo.litigation.house;

import java.io.Serializable;
import java.util.Date;

public class DelayHandoverInfo implements Serializable {

	private static final long serialVersionUID = 344619978167525960L;

	/**
	 * 推迟移交序号
	 */
	private Integer number;

	/**
	 * 推迟时间
	 */
	private Date retardationTime;

	/**
	 * 推迟理由
	 */
	private String delayReason;

	/**
	 * 区域贷后审批意见
	 */
	private String regionalManager;

	/**
	 * 贷后总监审批意见
	 */
	private String divisionHead;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Date getRetardationTime() {
		return retardationTime;
	}

	public void setRetardationTime(Date retardationTime) {
		this.retardationTime = retardationTime;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getRegionalManager() {
		return regionalManager;
	}

	public void setRegionalManager(String regionalManager) {
		this.regionalManager = regionalManager;
	}

	public String getDivisionHead() {
		return divisionHead;
	}

	public void setDivisionHead(String divisionHead) {
		this.divisionHead = divisionHead;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delayReason == null) ? 0 : delayReason.hashCode());
		result = prime * result + ((divisionHead == null) ? 0 : divisionHead.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((regionalManager == null) ? 0 : regionalManager.hashCode());
		result = prime * result + ((retardationTime == null) ? 0 : retardationTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DelayHandoverInfo other = (DelayHandoverInfo) obj;
		if (delayReason == null) {
			if (other.delayReason != null)
				return false;
		} else if (!delayReason.equals(other.delayReason))
			return false;
		if (divisionHead == null) {
			if (other.divisionHead != null)
				return false;
		} else if (!divisionHead.equals(other.divisionHead))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (regionalManager == null) {
			if (other.regionalManager != null)
				return false;
		} else if (!regionalManager.equals(other.regionalManager))
			return false;
		if (retardationTime == null) {
			if (other.retardationTime != null)
				return false;
		} else if (!retardationTime.equals(other.retardationTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DelayHandoverInfo [number=" + number + ", retardationTime=" + retardationTime + ", delayReason="
				+ delayReason + ", regionalManager=" + regionalManager + ", divisionHead=" + divisionHead + "]";
	}

}
