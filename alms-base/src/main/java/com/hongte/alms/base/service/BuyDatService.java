package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.BuyDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 采购单表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
public interface BuyDatService extends BaseService<BuyDat> {
	BuyDat addBuyDat(String companyName,String productCode,String customerCode,String accountPeriod,String produceDate,String invoiceNumber,
			String rowNumber,String calUnit,String number,String unitPrice,
			String originalAmount,String localAmount,String taxRate,String originalTax,String localhostTax,String buyType,Integer index,String isAssets);
	
	

}
