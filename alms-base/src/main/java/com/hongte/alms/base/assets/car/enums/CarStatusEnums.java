package com.hongte.alms.base.assets.car.enums;

public enum CarStatusEnums {
	PENDING("00","待处理"),
	AUCTION("01","拍卖中"),
	AUCTIONED("02","已拍卖"),
	SETTLED("03","已结清"),
	TRANSFERRED_BUS("04","已转公车"),
	TRANSFERRED_LEGAL("05","已移交法务"),
	REVOKED("06","已撤销"),
	TRANSFER_BUS("07","转公车申请中");
	private String statusCode;
	private String statusName;
	private CarStatusEnums(String statusCode,String statusName) {
		this.statusCode=statusCode;
		this.statusName=statusName;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
