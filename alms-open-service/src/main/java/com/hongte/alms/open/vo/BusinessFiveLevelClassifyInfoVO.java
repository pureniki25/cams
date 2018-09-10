package com.hongte.alms.open.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BusinessFiveLevelClassifyInfoVO implements Serializable {

	private static final long serialVersionUID = -9191343488673646058L;

	@JsonFormat(timezone = "GMT", pattern = "yyyy-MM-dd")
	private Date startDate;

	@JsonFormat(timezone = "GMT", pattern = "yyyy-MM-dd")
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "BusinessFiveLevelClassifyInfoVO [startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
