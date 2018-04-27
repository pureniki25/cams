package com.hongte.alms.base.vo.module;

import java.math.BigDecimal;

/**
 * @author CHENZS
 * @since 2018/4/9
 */
public class ApplyTypeVo {

    private Integer businessTypeId			;		//业务类型ID

    private BigDecimal derateMoney			;		//减免总金额
    
    
    private String businessTypeName;

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public Integer getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Integer businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public BigDecimal getDerateMoney() {
		return derateMoney;
	}

	public void setDerateMoney(BigDecimal derateMoney) {
		this.derateMoney = derateMoney;
	}



   
}
