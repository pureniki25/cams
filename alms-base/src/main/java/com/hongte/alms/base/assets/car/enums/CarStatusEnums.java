package com.hongte.alms.base.assets.car.enums;

public enum CarStatusEnums {
	DEFAULT("0","默认"),
	PENDING("01","待处理"),
	AUCTION("02","拍卖中"),
	AUCTIONED("03","已拍卖"),
	SETTLED("04","已结清"),
	TRANSFERRED_BUS("05","已转公车"),
	TRANSFERRED_LEGAL("06","已移交法务"),
	REVOKED("07","已撤销"),
	AUCTION_BUS("08","转公车申请中"),
	AUCTION_AUDIT("09","拍卖审核中"),
	TRANSFERRED_AUDIT("10","转公车审核中"),
	RETURNED("11","已归还"),
	;
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
