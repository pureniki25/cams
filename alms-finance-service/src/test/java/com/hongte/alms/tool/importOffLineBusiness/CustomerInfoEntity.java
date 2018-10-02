/**
 * 
 */
package com.hongte.alms.tool.importOffLineBusiness;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author 王继光 2018年9月11日 下午2:55:13
 */
@Data
@ExcelTarget("CustomerInfoEntity")
public class CustomerInfoEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 2658860262231397957L;

	@Excel(name = "businessId")
	String businessId;
	@Excel(name = "ismainCustomer")
	Integer ismainCustomer;
	@Excel(name = "customerName")
	String customerName;
	@Excel(name = "isReceiptAccount")
	Integer isReceiptAccount;
	@Excel(name = "customerType")
	String customerType;
	@Excel(name = "identifyCard")
	String identifyCard;
	@Excel(name = "phoneNumber")
	String phoneNumber;
	@Excel(name = "ismainlandResident")
	Integer ismainlandResident;
	@Excel(name = "isCompanyBankAccount")
	Integer isCompanyBankAccount;
	@Excel(name = "isMergedCertificate")
	Integer isMergedCertificate;
	@Excel(name = "businessLicence")
	String businessLicence;
	@Excel(name = "registerProvince")
	String registerProvince;
	@Excel(name = "companyLegalPerson")
	String companyLegalPerson;
	@Excel(name = "legalPersonIdentityCard")
	String legalPersonIdentityCard;
	@Excel(name = "legalPersonIsmainCustomer")
	Integer legalPersonIsmainCustomer;

}
