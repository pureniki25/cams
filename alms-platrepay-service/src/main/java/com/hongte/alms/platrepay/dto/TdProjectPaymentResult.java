package com.hongte.alms.platrepay.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 团贷网标的还款信息（应还）
 * 
 * @author 胡伟骞
 *
 */
public class TdProjectPaymentResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String projectId;

	/**
	 * 还款信息
	 */
	private List<TdProjectPaymentDTO> projectPayment;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<TdProjectPaymentDTO> getProjectPayment() {
		return projectPayment;
	}

	public void setProjectPayment(List<TdProjectPaymentDTO> projectPayment) {
		this.projectPayment = projectPayment;
	}

	@Override
	public String toString() {
		return "TdProjectPaymentResult [projectId=" + projectId + ", projectPayment=" + projectPayment + "]";
	}

}
