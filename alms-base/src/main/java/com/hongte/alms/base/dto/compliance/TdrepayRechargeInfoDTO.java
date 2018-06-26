package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.util.List;

import com.hongte.alms.base.entity.TdrepayRechargeDetail;
import com.hongte.alms.base.entity.TdrepayRechargeLog;

public class TdrepayRechargeInfoDTO extends TdrepayRechargeLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TdrepayRechargeDetail> details;

	public List<TdrepayRechargeDetail> getDetails() {
		return details;
	}

	public void setDetails(List<TdrepayRechargeDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeInfoDTO [details=" + details + "]";
	}

}
