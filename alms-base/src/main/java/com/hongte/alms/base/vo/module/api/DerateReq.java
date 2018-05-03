package com.hongte.alms.base.vo.module.api;
/**
 * Created by chenzs on 2018/4/17.
 */

import java.math.BigDecimal;
import java.util.List;

public class DerateReq {
    private String BusinessId;
    
    private String AfterId;

    private BigDecimal PenaltyAmount;

    private List<AddFee> AddFeeList;
    private List<DerateFee> DerateFeeList;
    
	public List<AddFee> getAddFeeList() {
		return AddFeeList;
	}

	public void setAddFeeList(List<AddFee> addFeeList) {
		AddFeeList = addFeeList;
	}

	public List<DerateFee> getDerateFeeList() {
		return DerateFeeList;
	}

	public void setDerateFeeList(List<DerateFee> derateFeeList) {
		DerateFeeList = derateFeeList;
	}

	public String getBusinessId() {
		return BusinessId;
	}

	public void setBusinessId(String businessId) {
		BusinessId = businessId;
	}

	public String getAfterId() {
		return AfterId;
	}

	public void setAfterId(String afterId) {
		AfterId = afterId;
	}

	public BigDecimal getPenaltyAmount() {
		return PenaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		PenaltyAmount = penaltyAmount;
	}


    

}
