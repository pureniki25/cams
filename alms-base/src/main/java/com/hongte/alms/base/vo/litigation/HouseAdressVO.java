package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.util.List;

public class HouseAdressVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> houseArea;

	private String detailAddress;

	private String mortgageSituation;

	public List<String> getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(List<String> houseArea) {
		this.houseArea = houseArea;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getMortgageSituation() {
		return mortgageSituation;
	}

	public void setMortgageSituation(String mortgageSituation) {
		this.mortgageSituation = mortgageSituation;
	}

	@Override
	public String toString() {
		return "HouseAdressVO [houseArea=" + houseArea + ", detailAddress=" + detailAddress + ", mortgageSituation="
				+ mortgageSituation + "]";
	}

}
