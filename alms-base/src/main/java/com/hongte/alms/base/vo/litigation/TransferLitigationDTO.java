package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel(value = "移交诉讼信息查询接口返回数据模型", description = "移交诉讼信息查询接口返回数据模型")
@Data
@ToString
public class TransferLitigationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 业务编号
	 */
	private String businessId;
	/**
	 * 业务类型（1：房贷 2：车贷）
	 */
	private Integer businessType;
	/**
	 * 片区
	 */
	private String districtName;
	/**
	 * 分公司
	 */
	private String companyName;
	/**
	 * 主借款人姓名
	 */
	private String customerName;
	/**
	 * 借款开始时间
	 */
	private Date contractStartDate;
	/**
	 * 借款到期时间
	 */
	private Date contractEndDate;
	/**
	 * 借款利率
	 */
	private BigDecimal borrowRate;
	/**
	 * 还款方式
	 */
	private String repaymentTypeName;
	/**
	 * 实际出款金额合计
	 */
	private BigDecimal factOutputMoney;
	/**
	 * 实际出款时间
	 */
	private List<Date> factOutputDates;
	/**
	 * 借款金额
	 */
	private BigDecimal borrowMoney;
	/**
	 * 收款人
	 */
	private List<String> repaymentNames;
	
	/**
	 * 车辆信息
	 */
	private List<TransferLitigationCarDTO> carInfos;
	
	/**
	 * 房产信息
	 */
	private List<TransferLitigationHouseDTO> houseInfos;
	
	/**
	 * 相关人员信息
	 */
	private List<TransferLitigationPersonDTO> customerInfos;

}
