package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 团贷网标的还垫付信息（实还）
 * @author 胡伟骞
 *
 */
public class TdReturnAdvanceShareProfitResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID
	 */
	private String projectId;

	/**
	 * 还垫付信息
	 */
	private List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<TdReturnAdvanceShareProfitDTO> getReturnAdvanceShareProfits() {
		return returnAdvanceShareProfits;
	}

	public void setReturnAdvanceShareProfits(List<TdReturnAdvanceShareProfitDTO> returnAdvanceShareProfits) {
		this.returnAdvanceShareProfits = returnAdvanceShareProfits;
	}

	@Override
	public String toString() {
		return "TdReturnAdvanceShareProfitResult [projectId=" + projectId + ", returnAdvanceShareProfits="
				+ returnAdvanceShareProfits + "]";
	}

}
