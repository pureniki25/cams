package com.hongte.alms.base.dto.compliance;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 调用 eip 平台资金分发接口参数DTO
 * @author 胡伟骞
 *
 */
@Data
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
	 * 机构类型ID
	 */
	private int orgType;
	/**
	 * 用户分发列表
	 */
	private List<DistributeFundDetailDTO> detailList;

}
