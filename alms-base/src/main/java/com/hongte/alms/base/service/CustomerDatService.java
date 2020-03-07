package com.hongte.alms.base.service;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 客户与供应商表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-01-20
 */
public interface CustomerDatService extends BaseService<CustomerDat> {
	
	CustomerDat addCustomerDat(String customerName,String type,String companyName,String kaiPiaoRiQi);
	void addCustomerDatByExcel(String customerCode,String customerName,String type,String companyName);
	void importCustomerDat(MultipartFile file,String companyName) throws Exception;
}
