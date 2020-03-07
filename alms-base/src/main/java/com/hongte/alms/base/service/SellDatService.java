package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 销售单表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
public interface SellDatService extends BaseService<SellDat> {
	SellDat addSellDat(String properties,String companyName,String productCode,String customerCode,String accountPeriod,String produceDate,String invoiceNumber,
			String rowNumber,String calUnit,String number,String unitPrice,
			String originalAmount,String localAmount,String taxRate,String originalTax,String localhostTax,String importType,Integer index);
	
}
