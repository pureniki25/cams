package com.hongte.alms.base.service;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.CustomerFirstDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 单位期初余额表 服务类
 * </p>
 *
 * @author czs
 * @since 2020-02-11
 */

public interface CustomerFirstDatService extends BaseService<CustomerFirstDat> {
	void importCustomerFirst(MultipartFile file, String companyName)
			throws Exception;

}
