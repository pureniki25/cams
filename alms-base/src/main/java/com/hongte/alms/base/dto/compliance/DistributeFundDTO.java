package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.util.List;

/**
 * 调用 eip 平台资金分发接口参数DTO
 * @author 胡伟骞
 *
 */
public class DistributeFundDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 团贷分配，商户唯一号(测试，生产不一样)
	 */
	private String oidPartner;
	/**
	 * 出款方帐号
	 */
	private String userId;
	/**
	 * 分发批次号 批次唯一标识 必须为guid转化成的字符串
	 */
	private String batchId;
	/**
	 * 客户端IP
	 */
	private String userIP;
	/**
	 * 分发总金额 保留两位小数 单位（元）
	 */
	private Double totalAmount;
	/**
	 * 用户分发列表
	 */
	private List<DistributeFundDetailDTO> detailList;

	public String getOidPartner() {
		return oidPartner;
	}

	public void setOidPartner(String oidPartner) {
		this.oidPartner = oidPartner;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<DistributeFundDetailDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DistributeFundDetailDTO> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		return "DistributeFundDTO [oidPartner=" + oidPartner + ", userId=" + userId + ", batchId=" + batchId
				+ ", userIP=" + userIP + ", totalAmount=" + totalAmount + ", detailList=" + detailList + "]";
	}

}
