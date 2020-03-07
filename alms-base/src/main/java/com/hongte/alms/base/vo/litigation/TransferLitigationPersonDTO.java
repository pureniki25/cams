package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "移交诉讼信息查询接口返回相关人员信息模型", description = "移交诉讼信息查询接口返回相关人员信息模型")
@Data
public class TransferLitigationPersonDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别：1、男；2、女
	 */
	private Integer sex;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 身份证
	 */
	private String identifyCard;
	/**
	 * 人员类型（1、个人；2、企业）
	 */
	private Integer borrowerType;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 电话号码
	 */
	private String telNo;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 营业执照地址
	 */
	private String licenseAddr;
	/**
	 * 法定代表人
	 */
	private String legalPerson;
	/**
	 * 人员类别(1:出借人、债权人; 2：借款人; 3:担保人; 4:共借人; 5:抵押权人/反担保人)
	 */
	private Integer type;

}
