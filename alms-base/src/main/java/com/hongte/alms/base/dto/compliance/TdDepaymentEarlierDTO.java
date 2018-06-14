package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提前结清接口参数DTO
 * @author 胡伟骞
 *
 */
public class TdDepaymentEarlierDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 标的ID
	 */
	private String projectId;
	/**
	 * 资产端服务费
	 */
	private BigDecimal assetsCharge;
	/**
	 * 担保公司服务费
	 */
	private BigDecimal guaranteeCharge;
	/**
	 * 中介公司服务费
	 */
	private BigDecimal agencyCharge;
	/**
	 * 提前结清类型
	 */
	private String type;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getAssetsCharge() {
		return assetsCharge;
	}

	public void setAssetsCharge(BigDecimal assetsCharge) {
		this.assetsCharge = assetsCharge;
	}

	public BigDecimal getGuaranteeCharge() {
		return guaranteeCharge;
	}

	public void setGuaranteeCharge(BigDecimal guaranteeCharge) {
		this.guaranteeCharge = guaranteeCharge;
	}

	public BigDecimal getAgencyCharge() {
		return agencyCharge;
	}

	public void setAgencyCharge(BigDecimal agencyCharge) {
		this.agencyCharge = agencyCharge;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TdDepaymentEarlierDTO [projectId=" + projectId + ", assetsCharge=" + assetsCharge + ", guaranteeCharge="
				+ guaranteeCharge + ", agencyCharge=" + agencyCharge + ", type=" + type + "]";
	}

}
